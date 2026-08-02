# AGENT.md — OptiRefine 交接手册（当前状态）

> **读完再动代码。**
> 最后更新：2026-08-02（实机崩溃修复循环进行中）
> 本文档是 FOR_AGENT.md 的**现行版**：FOR_AGENT.md 描述的是阶段 1（工具链迁移 + 全量审查），
> 其中的 Blackboard / `@Checked` / transformer / REPATCH_CONFIG 等**已删除**，以下方为准。

---

## 0. 一句话状态

OptiRefine = 用 **Mixin** 把 OptiFine 对 1.12.2 的字节码 patch 重写到 **Cleanroom（Forge 环境）** 上。
**122 个 mixin 已注册**；`gradlew build` 绿；**OF 字节码 transformer 已完全禁用**（mixin 是唯一行为来源）。
当前处于**逐轮实机崩溃修复**：用户用 HMCL 跑 `cleanroom-0.6.7-alpha` + `preview_OptiFine_1.12.2_HD_U_G6_pre1.jar`，
每轮发 `latest.log` / crash-reports，修完拷贝 jar 再测。

**本地 8+ commits 未 push**（用户要求测过再 push）。

---

## 0.5 运行时命名（本轮最重要的事实，已实锤）

| 环境 | 类名 | 成员名（字段/方法） |
|---|---|---|
| **生产（HMCL Cleanroom）** | MCP（`net.minecraft...`） | **SRG**（`field_`/`func_`） |
| devrun（gradle 开发环境） | MCP | **MCP** |
| OptiFine jar（发布版字节码） | **SRG**（`bib`/`bzw`） | **MCP**（`modelManager` 等） |

证据链：

- `IllegalAccessError: Config tried to access protected field ...ResourcePackRepository.field_110617_f`
  —— JVM 原生报字段名 `field_110617_f` 且字段**存在**（可见性错误而非 NoSuchFieldError）→ 运行时成员名 = SRG
- `@Shadow`（unimined 构建期 remap 成 SRG）全部 apply 成功 → 运行时按 SRG 匹配
- `NoSuchFieldError: RenderItem does not have member field 'modelManager'` —— OF jar 字节码访问
  MCP 名 `modelManager`，运行时没有（该字段是 OF patch 自加的，vanilla 无）→ 需要 mixin 提供

**推论（cursed 处理器设计）**：desc/name 一律写 **SRG 名**（vanilla 成员）或 **OF 成员名**（MCP，tsrg 无），
并**开启 `deobf=true`**。处理器 `CursedMixinExtensions` 已有：

- `isSrgRuntime(targetClass)`：探测目标类成员名是否有 `field_/func_` 前缀 → 判断运行时命名
- `@AccessTransformer`：`deobf=true` 时**双匹配**（原始 SRG 名 + remap 后 MCP 名都试）
- `@AccessibleOperation`：`deobf=true` 时仅当运行时为 MCP 才 remap（SRG 运行时直接用原始名）

未来 roadmap 若 Cleanroom 切 MCP 成员名运行时，处理器自动适配，mixin 代码零改动。

**不要访问** `D:\Program Disk\HMCL\.minecraft\libraries\`（用户禁止）；运行时 jar
`versions\cleanroom-0.6.7-alpha\cleanroom-0.6.7-alpha.jar` 是**原版 Mojang 混淆**（`bib.class`），
对对照没有用——对照一律用 `patch/` 下三方（见 §3）。

---

## 1. 项目

| 项 | 说明 |
|---|---|
| MC | 1.12.2 / Cleanroom 0.6.7-alpha（=Forge 环境） |
| 手段 | Mixin 0.8.7（sponge-mixin 0.20.13）+ mixinextras 0.5.4 + cursed mixin extensions（自维护） |
| 注册 | `src/main/resources/optirefine.default.mixin.json`（+ `optirefine.mods.mixin.json`） |
| 主包 | `mods.Hileb.optirefine` |
| 构建 | Gradle 9.6.1 / **JDK 26**（`JAVA_HOME=D:\Program Disk\Java\jdk-26.0.2`）/ unimined 1.4.26-kappa |

**核心原则**：mixin **只实现** OptiFine 相对 Cleanroom 的**增量**；Cleanroom/`patch/patches` 已有的
Forge 钩子**禁止重复做**。mixin 的成员只准引用**运行时存在**的成员（vanilla SRG + mixin 自己加的 OF 成员）。

---

## 2. 目录

```
OptiRefine/
├── AGENT.md                    ← 本文件（现行）
├── FOR_AGENT.md                ← 阶段 1 历史文档（部分过时，见 §0 说明）
├── README.md                   ← 对外（无可用版本警告保留）
├── srg_to_stable_39-1.12.tsrg  ← SRG→MCP（旧表；对照用 .gradle/unimined/.../srg2mcp.tsrg 更准）
├── patch/
│   ├── forge/                  ← 运行时基线（SRG 名）
│   ├── optifine/               ← OptiFine 目标行为（MCP 名，含 OF 自加成员）
│   ├── patches/                ← Cleanroom 已落地 patch（.java.patch）
│   └── deobf/                  ← forge 反混淆 MCP 源（2185 文件，部分类不完整）
├── src/main/java/mods/Hileb/optirefine/
│   ├── core/                   ← 加载入口（transformer 已禁用，couldNotTransform 恒 true）
│   ├── library/cursedmixinextensions/ ← 处理器（isSrgRuntime 双匹配在此）
│   ├── optifine/               ← Config stub 等
│   └── mixin/                  ← 122 个 mixin
├── src/main/resources/
│   ├── optirefine.default.mixin.json
│   └── optirefine_at.cfg       ← AT（仅字段/类/构造器条目生效；方法条目编译期无效）
└── .alma/                      ← 本地脚本/todos（gitignored）
```

---

## 3. 三方对照工作流

| 路径 | 角色 | 命名 |
|---|---|---|
| `patch/forge/**` | 运行时基线 | SRG |
| `patch/deobf/**` | 基线 MCP 可读版（优先 diff） | MCP |
| `patch/optifine/**` | OptiFine 目标行为 | MCP |
| `patch/patches/**` | Cleanroom 已落地改动 | SRG |

顺序：`patch/patches` 有 → 跳过；`diff -u patch/deobf/... patch/optifine/...` → 真正增量；只做增量。

**OF 自加成员判断**：`patch/optifine/<类>` 里有、`patch/deobf/<类>` 里没有 → OF 新增 →
mixin 需 `@Unique`/`@Public` 提供（名字用 OF 的 MCP 名，运行时与 mixin 提供名一致）。

---

## 4. Mixin 0.8.7 硬规则（每一条都踩过坑）

| 场景 | 正确做法 | 错误做法 |
|---|---|---|
| 访问基线已有私有成员 | `@Shadow`（名=SRG 或 MCP，构建期 remap） | 瞎猜签名 |
| **static 成员**（字段/方法，含 `static final` 常量） | `private static` + `@Public`（普通成员 merge，保留原名） | 任何 `public/protected static`（mixin apply 直接报 `non-private static field`，**final 也不例外**） |
| OF 新增**实例方法**到目标类 | `@Unique`（对外 `@Unique @Public`） | `@Shadow` 不存在的方法 |
| OF 新增**字段**到目标类 | `@Unique`（**不带初始值**——`= null` 会在 `<init>` 注入 putfield，字段被删后悬空 NoSuchFieldError） | `= null` / final 带值 |
| OF 新增字段被 **OF jar 跨类访问** | `@Public` 字段（`@AccessTransformer` 的声明字段必须 **public**，无显式 access 时默认取声明可见性） | protected/private 声明（目标字段只被提升到 protected → IllegalAccessError） |
| `@Inject` 目标方法**非 void** | handler 用 `CallbackInfoReturnable<T>` | `CallbackInfo`（InvalidInjectionException） |
| `@Inject`/`@WrapOperation` 目标是 **static 方法** | handler 必须 `private static` | 非 static handler |
| `@At("NEW")` 目标为**数组** | 不支持（ANEWARRAY）→ 改用 `@ModifyConstant(intValue, ordinal)` | `@At("NEW")` 指数组创建 |
| `@Unique public` 与目标类**同名成员**（任意签名） | 会被**静默丢弃** → 若必须保留：改 `@Overwrite`（仅同签名时）或绕行 | 依赖被丢弃的 @Unique |
| 构造器注入/替换 | cursed `@NewConstructor`/`@ReplaceConstructor`/`@ShadowSuperConstructor` | `@Unique` 冒充构造器 |
| OF 成员跨类调用（`Config.getModelManager` 等） | 目标类 mixin 提供同名成员（字段+初始化/方法） | 以为 OF jar 能直接访问 |

### cursed / AccessibleOperation 要点

```java
// 访问私有成员（运行时存在；vanilla 成员 desc 写 SRG 名 + deobf=true；
// OF 成员（tsrg 无）写 MCP 名，deobf 无影响）
@AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.BufferBuilder animatedSprites Ljava/util/BitSet;", deobf = true)
private static native BitSet BufferBuilder_animatedSprites_get(BufferBuilder buffer);

// NEW 必须配对 Construction 哨兵
@AccessibleOperation(opcode = Opcodes.NEW, desc = "...", deobf = true)
private static native Xxx _new_Xxx(AccessibleOperation.Construction c, ...);
// 调用: _new_Xxx(AccessibleOperation.Construction.construction(), ...)
```

`compatibilityLevel = JAVA_17`（两个 mixin json 都是；enum-switch 的 `$SwitchMap` 合成类要求）。

---

## 5. 本轮实机崩溃修复记录（2026-08-02，按时间）

| 崩溃 | 根因 | 修复 |
|---|---|---|
| `MixinOpenGlHelper` / `MixinGlStateManager` apply 失败 | `public static final` 常量（GL_QUADS 等） | `private static final` + `@Public` |
| `@At("NEW")` 指 `TextureState[8]` 数组 | NEW 不支持数组 | `@ModifyConstant(intValue=8, ordinal=0)` → 32 + TAIL 初始化 |
| `NoSuchFieldError acc_field_110617_f`（`<init>` putfield） | `@AccessTransformer` 占位字段带 `= null` | 去 final/初始值 |
| `Config ... field_110617_f` IllegalAccessError | AT 声明字段 protected → 目标只提升到 protected | 全部 AT 字段声明改 **public**（static 的用显式 access 保持 private static） |
| `registerSprite` InvalidInjectionException | 非 void 目标用 CallbackInfo | `CallbackInfoReturnable<TextureAtlasSprite>` |
| `NoSuchMethodError TextureMap.isAbsoluteLocation` | OF 私有方法未提供 | mixin 补 `isAbsoluteLocation`/`isAbsoluteLocationPath` |
| `MixinTileEntityBeaconRenderer depthMask` | static 目标、非 static handler | handler `private static` |
| `NoSuchFieldError RenderItem.modelManager` | OF 自加字段未提供 | MixinRenderItem 加 `@Public modelManager` + `<init>` RETURN 赋值 |
| `Discarding @Unique putColorRGBA(5参)/isColorDisabled/putBulkData` | 待定（不再调查，用户叫停） | 保持 `@Unique public` 原样 |

**事故记录**：改 MixinBufferBuilder 的脚本曾把文件截断成 17 行（空 mixin 编译照样过，提交里
“629 deletions”）。**改完必须 `wc -l` / diff 确认行数合理**，别信编译通过。

---

## 6. 构建 / 部署 / git

```bash
export JAVA_HOME="D:\Program Disk\Java\jdk-26.0.2"
./gradlew.bat build --console=plain      # 1–2 分钟，BUILD SUCCESSFUL
cp build/libs/optirefine-0.0.1-indev.jar "D:\Program Disk\HMCL\.minecraft\versions\cleanroom-0.6.7-alpha\mods\optirefine-0.0.1-indev.jar"
```

- 测试输入：`...\logs\latest.log`（grep `FATAL|Mixin apply|InvalidInjection|NoSuchField|IllegalAccess`）+
  `...\crash-reports\crash-*.txt`（看堆栈）
- git：`git -c user.name="Hileb" -c user.email="107909747+Ecdcaeb@users.noreply.github.com" commit -m "..."`
  （不改全局配置）；**不 push**（用户确认后再推 origin/dev）
- 行尾：仓库 CRLF；Python 写文件用 `newline=''` 且模式串带 `\r\n`；改完留意 `LF will be replaced by CRLF` 警告
- 分支：`dev`（原 `agent_rework`）

---

## 7. 已知遗留 / 注意

- `MixinGuiVideoSettings.optiRefine$actionPerformedRightClick` 未接线（OF 右键递减由 transformer
  注入 GuiScreen，mixin 侧无注入点）
- 空壳 mixin（注册无操作，保留）：`MixinModelRotation`、`MixinMatrix4f`、`MixinBlockStateContainer`(×2)、
  `MixinI18n`、`MixinGuiIngameForge`、`MixinCompiledChunk_1`
- `@Shadow` 只准指目标类**自己声明**的成员（接口/继承成员会 apply 崩溃）
- OF jar 直接访问的 vanilla 成员都用 MCP 名（`modelManager` 型问题）→ 逐个崩溃驱动补 `@Unique @Public`
- FOR_AGENT.md §6 的 Blackboard / REPATCH_CONFIG 描述已失效（代码已删）

---

## 8. 不要做的事

1. 不要访问 `D:\Program Disk\HMCL\.minecraft\libraries\`（用户禁止）
2. 不要 `public/protected static`（含 final 常量）出现在 mixin 里
3. 不要 `@Shadow` / `@WrapMethod` 基线不存在的成员
4. 不要用 `@Unique` 冒充构造器 / 替代 `new`
5. 不要重复 Cleanroom 已打的 Forge 钩子
6. 不要 push（等用户测试确认）
7. 不要恢复 mixin 尾部巨型 patch 注释；不要给 @AccessTransformer 占位字段带初始值
8. 改文件后确认行数没缩水（防截断事故）

---

**交接完成。** 下一任：等用户下一轮 `latest.log`，按 §5 表格模式修；修完 build + commit + 拷贝 jar。
