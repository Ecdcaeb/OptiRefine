# Repository Guidelines

> 面向 AI 助手的代码库指南。最后更新 2026-08-03（渲染链三路语义审计 + 全量修复后）。
> 优先读 `AGENT.md`（现行交接手册，含逐轮修复记录与硬规则）。

## Project Overview

**OptiRefine** = 用 **Mixin 0.8.7** 把 OptiFine 对 Minecraft **1.12.2** 的字节码补丁重写到
**Cleanroom（= Forge 环境）** 上的 mod。目标：让 OptiFine 功能在 Cleanroom 下可用。

- 122 个 mixin 类，已全部注册；OF 字节码 transformer 已禁用，**mixin 是唯一行为来源**
- 运行时基线是 Cleanroom patched 环境（`patch/forge` SRG），**不是** OptiFine jar 里的类
- 核心原则：mixin 只实现 OptiFine 相对 Cleanroom 的**增量**；`patch/patches` 已有的 Forge 钩子禁止重复
- 当前状态：实机崩溃逐轮修复（用户 HMCL 跑 `cleanroom-0.6.7-alpha` + `OptiFine_1.12.2_HD_U_G6_pre1.jar`），`gradlew build` 绿，**无稳定发行版**

## Architecture & Data Flow

```
Minecraft 启动 → OptiRefineCore (@IFMLLoadingPlugin, SortingIndex 1000)
  ├─ 注册 ASM transformers（core/transformer/）
  │   ├─ OptifineTransformerTransformer: 劫持 optifine.OptiFineClassTransformer（couldNotTransform 恒 true = 禁用 OF 补丁）
  │   └─ OptiRefineConfigTransformer: mixin postApply 时把 stub Config 引用重写为真 Config
  ├─ IMixinConfigPlugin（$DefaultMixinPlugin / $ModMixinPlugin）→ 两个 mixin json
  └─ mixin postApply → CursedMixinExtensions.postApply(ClassNode)
       ├─ @Public / @AccessTransformer: 改目标成员可见性
       ├─ @AccessibleOperation: 把 native 桥方法占位替换为实际字节码指令（GET/PUT/INVOKE/NEW）
       ├─ 构造器扩展: @NewConstructor / @ReplaceConstructor / @ShadowSuperConstructor
       └─ @ChangeSuperClass / @SignatureFix / @Implements
```

运行时命名（已实锤，**决定 desc 写法**）：
- **生产（HMCL Cleanroom）**：类名 MCP（`net.minecraft...`）、成员名 **SRG**（`field_`/`func_`）
- **devrun**：类名 MCP、成员名 MCP
- **OF jar 字节码**：类名 SRG（`bib`/`bzw`）、成员名 MCP（`modelManager`）—— OF 直接访问 vanilla 成员用 MCP 名，mixin 需提供同名成员

**desc 规则**：`@AccessibleOperation`/`@AccessTransformer` 的 desc/name 写 **SRG 名**（vanilla 成员）
或 **OF 成员名**（MCP，tsrg 无），并开 `deobf=true`；`CursedMixinExtensions` 用 `isSrgRuntime()`
探测 + 双匹配自动适配 SRG/MCP 两种运行时（未来 Cleanroom 切 MCP 成员名运行时零改动）。

## 运行时大坑（2026-08-03 实锤，每条都崩过）

- **cleanmix 不注入 `@Unique` 字段初始化器**（行号启发式失败）→ 带初始化器的字段用
  `@Inject(method = "<init>*", at = @At("RETURN"))` 通配构造器注入（handler 只收 `CallbackInfo`，
  无需匹配构造器参数）；字段声明**不要写 `= expr`**（单一初始化路径）。
- **`@Unique` 对 public 字段无效**（官方 javadoc）→ 需要初始化的 OF 公开字段：
  `private @Unique @Public`（private 注入初始化器、无冲突不重命名；cursed `@Public` 运行时转 public）。
- **cursed `@NewConstructor` 生成的构造器不含目标类字段初始化**（vanilla 声明初始化器丢失）
  → 生成体内手动补（如 `AmbientOcclusionFace` 的 `vertexBrightness`/`blockPosArr`）。
- **`@NewConstructor` 构造器不经过 Mixin 的 `<init>*` 注入** → 该路径的字段必须方法体内手动初始化。
- **vanilla `growBuffer` 后 `rawFloatBuffer` 是只读**（`asReadOnlyBuffer`）；OF 保持可写
  → `@Inject(growBuffer, TAIL)` 恢复 `byteBuffer.asFloatBuffer()`（shaders `SVertexBuilder.calcNormal` 要写）。
- **运行时构造器 arity 以实际崩溃为准**：`ContainerLocalRenderInformation` 三方资料是 3 参（Forge），
  实际运行时是 **vanilla 4 参**（`RenderGlobal,RenderChunk,EnumFacing,int`）→ 二分实测。
- **FML AT 的 `<init>` 条目在 Cleanroom 不生效** → 用 cursed `@AccessTransformer`（postApply 双匹配）
  放在对应 mixin 里（`MixinContainerLocalRenderInformation` 范例）。
- **`@At("INVOKE")` 默认在调用前**（BEFORE）→ 需要调用后执行时显式 `shift = At.Shift.AFTER`
  （Render 火贴图清理踩过）。
- **`@Redirect`/`@Inject` 的 method 选择**：目标方法有重载时必须写**完整签名**，否则 remap 报
  `ambiguous ... use FLAG_FIRST`。
- **Forge 基线方法可能与 vanilla 不同**（`VertexBufferUploader.draw` 开头有 `reset()`）→
  注入位置要对运行时（patch/forge + patch/patches）而非 deobf 假设。

## Key Directories

| 路径 | 用途 |
|---|---|
| `src/main/java/mods/Hileb/optirefine/core/` | 加载入口、ASM transformers、MixinConfigPlugin |
| `.../core/transformer/` + `dev/` | 生产 transformer + dev-only transformer |
| `.../library/cursedmixinextensions/` | 自维护"cursed" mixin 后处理（12 个注解） |
| `.../library/foundationx/` | ASM 工具（NonLoadingClassWriter 等） |
| `.../optifine/` | `Config` stub（运行时字节码重写为真 Config）、`GameSettingsOptionOF`（45 个枚举常量） |
| `.../mixin/defaults/` | **122 个 mixin**，镜像 vanilla 包树（`minecraft/...`，另有 `optifine/`、`minecraftforge/`） |
| `.../mixin/mods/` | 空（per-mod 环境，暂未用） |
| `src/main/resources/` | mixin json ×2、`optirefine_at.cfg`（构建时改名进 `META-INF/`） |
| `src/main/resource-templates/` | blossom 模板：`mcmod.info`、`pack.mcmeta`（`{{ x }}` 占位符） |
| `patch/forge/` | 运行时基线（SRG 名，Cleanroom 环境类） |
| `patch/deobf/` | forge 反混淆 MCP 源（2185 文件，**优先 diff**） |
| `patch/optifine/` | OptiFine 目标行为（MCP，含 OF 自加成员） |
| `patch/patches/` | Cleanroom 已落地改动（`.java.patch`，SRG） |
| `.alma/` | 本地脚本/todos（gitignored，非构建必需） |

## Development Commands

```bash
# 构建（必须 JDK 26；wrapper 是 Gradle 9.6.1）
export JAVA_HOME="D:\Program Disk\Java\jdk-26.0.2"
./gradlew.bat build --console=plain        # 产物 build/libs/optirefine-0.0.1-indev.jar（remap + manifest）
./gradlew.bat runClient                    # devrun（MCP 运行时，由 unimined 生成）

# 部署到测试环境（用户实机）
cp build/libs/optirefine-0.0.1-indev.jar "D:\Program Disk\HMCL\.minecraft\versions\cleanroom-0.6.7-alpha\mods\optirefine-0.0.1-indev.jar"
```

无 lint / 无 test / 无 format 任务。CI（`.github/workflows/build.yml`、`release.yml`）只跑 `./gradlew build`。

## Code Conventions & Common Patterns

- **注入方法前缀**：`optiRefine$`（如 `optiRefine$registerSprite`）
- **访问器字段**：`acc_` / `acc$` 前缀（`@AccessibleOperation` 桥方法名 `_acc_<Class>_<member>_` 或 `TextureMap_bindTexture` 式）
- **Mixin 0.8.7 硬规则**（每条都踩过坑，详见 AGENT.md §4）：
  - static 成员（含 `static final`）必须是 `private static` + `@Public`，否则 apply 报 `non-private static field`
  - `@Shadow` 只准指目标类**自己声明**的成员（接口/继承成员会崩）
  - 非 void 目标方法注入用 `CallbackInfoReturnable<T>`；static 目标方法 handler 必须 `private static`
  - `@At("NEW")` 不支持数组 → 用 `@ModifyConstant(intValue, ordinal)`
  - `@Unique public` 与目标同名成员会被**静默丢弃**
  - `@AccessTransformer` 占位字段：**public 声明、不带初始值**（`= null` 会在 `<init>` 注入 putfield 后悬空）
- **NEW 必须配对哨兵**：`@AccessibleOperation(opcode=NEW)` + `AccessibleOperation.Construction.construction()`
- **mixin json 的 `compatibilityLevel` 是 `JAVA_17`**（enum-switch `$SwitchMap` 合成类要求）
- 错误处理：无统一框架；`Config.isShaders()` 等 OF 静态调用走 stub；日志用 `OptiRefineLog.log`（Log4j）
- 行尾 **CRLF**；Python 写文件用 `newline=''` + 模式串带 `\r\n`；改完 `wc -l` 防截断事故（发生过）

## Important Files

| 文件 | 说明 |
|---|---|
| `src/main/java/mods/Hileb/optirefine/core/OptiRefineCore.java` | 入口；两个 IMixinConfigPlugin；postApply 管线 |
| `src/main/java/mods/Hileb/optirefine/library/cursedmixinextensions/CursedMixinExtensions.java` | cursed 处理器（`isSrgRuntime`、AT 双匹配、AccessibleOperation 生成） |
| `src/main/resources/optirefine.default.mixin.json` | 122 mixin 注册表（`mixins` 30 + `client` 92） |
| `src/main/resources/optirefine_at.cfg` | AT（33 条；字段/类/构造器条目有效，方法条目编译期无效） |
| `AGENT.md` | **现行交接手册**（崩溃修复记录、硬规则、部署流程） |
| `FOR_AGENT.md` | 阶段 1 历史文档（Blackboard/@Checked 等已删，部分过时） |
| `gradle.properties` | `is_coremod=true`、`enable_shadow=false`、`enable_junit_testing=true` |
| `srg_to_stable_39-1.12.tsrg` | SRG→MCP 映射（对照用 `.gradle/unimined/local/mappings/srg2mcp.tsrg` 更全） |

## Runtime/Tooling Preferences

- **JDK 26 必须**（`D:\Program Disk\Java\jdk-26.0.2`）；toolchain 26；勿用 JDK 8/21
- Gradle 9.6.1 wrapper；unimined 1.4.26-kappa（mixin remap + refmap 在此，非 javac AP）；shadow 9.5.1；blossom 2.2.0
- 依赖：`compileOnly` sponge-mixin 0.20.13+mixin.0.8.7、lwjglx、`net.optifine:optifine:1.12.2-HD_U_G5_unpatched_srg`；`runtimeOnly` optifine G5 + OptiFineDevTweaker
- 编译 classpath 按 vanilla MCP 类优先（不用 OF jar 遮蔽）；mixin 引用 OF 独有成员必须走 `@AccessibleOperation`/`@Shadow`
- git：分支 `dev`；提交身份 `Hileb <107909747+Ecdcaeb@users.noreply.github.com>`（`git -c` 传入，勿改全局）；**push 需用户确认**
- **不要访问** `D:\Program Disk\HMCL\.minecraft\libraries\`（用户禁止）

## Testing & QA

- **零自动化测试**：无 `src/test`、无 `*Test*.java`（JUnit5 配置存在但空跑）
- QA 方式 1：`.alma/` 静态扫描脚本（gitignored）
  - `python .alma/scan_review.py` —— 运行时规则审查（@Shadow/注入目标 vs `patch/deobf`、@Unique 冲突、NEW+Construction 配对）；期望输出 `NO PROBLEMS FOUND`
  - `python .alma/scan_accessible_ops.py` / `scan_wrapmethod_arity.py` / `check_registered.py`（json 注册核对）
  - `python .alma/deobf.py` —— 重新生成 `patch/deobf`
- QA 方式 2（主流程）：**实机崩溃循环**——用户跑 HMCL，发 `versions\cleanroom-0.6.7-alpha\logs\latest.log` /
  `crash-reports\crash-*.txt`；修一个 → build → commit → 拷贝 jar → 再测。日志 grep 模式：
  `FATAL|Mixin apply|InvalidInjection|NoSuchField|NoSuchMethod|IllegalAccess`
- 行为对照基准：`diff -u patch/deobf/... patch/optifine/...`（OF 增量 = mixin 要做的事）

## 渲染链已知遗留（2026-08-03 审计后，不崩但功能缺失）

- `getRenderQuads` face 传 `null`（OF 按 quad face 传）→ 智能树叶等 per-face 定制失效（LOW-MED）
- `AmbientOcclusionFace.updateVertexBrightness` 用 vanilla 版（OF 重写含 `fixAoLightValue` 未移植）
  → `Config.getAmbientOcclusionLevel` 只影响 `aoLightValueOpaque`（已接线），平滑光照细节与 OF 有差
- `MixinModelBakery` loadModel basePath 缺 `models/` 前缀（LOW）
- `CustomColors.getDurabilityColor` 挂钩 TODO（Cleanroom patch 删了 hsvToRGB，改 Forge `getRGBDurabilityForDisplay` 路径）
- `quadsToTriangles`/`isQuadsToTriangles` 依赖 OF 性能选项（默认关；已修 getVertexCount/uploader 顺序）
- `putBulkData` 运行时版（Cleanroom patch）无 SVertexBuilder shaders 钩子（MED，已知）
- `MixinWorldEntitySpawner.findChunksForSpawning` OF 逻辑未实现（注释掉）
- `MixinFMLClientHandler` brand 追加需运行时验证
