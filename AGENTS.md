# Repository Guidelines

> 面向 AI 助手的代码库指南。最后更新 2026-08-05（全量三路语义审计第二轮完成：P1–P26 + core/lib，全部 DIFF 已修或登记）。
> 优先读 `AGENT.md`（现行交接手册，含逐轮崩溃修复记录与硬规则）。

## Project Overview

**OptiRefine** = 用 **Mixin 0.8.7** 把 OptiFine 对 Minecraft **1.12.2** 的字节码补丁重写到
**Cleanroom（= Forge 环境）** 上的 mod。目标：让 OptiFine 功能在 Cleanroom 下可用。

- 125 个 mixin 类已注册（json：30 `mixins` + 95 `client`）；OF 字节码 transformer 已禁用，**mixin 是唯一行为来源**
- 运行时基线是 Cleanroom patched 环境（`patch/forge` SRG），**不是** OptiFine jar 里的类
- 核心原则：mixin 只实现 OptiFine 相对 Cleanroom 的**增量**；`patch/patches` 已有的 Forge 钩子禁止重复
- 当前状态（2026-08-05）：非光影渲染已正常（renderQuadsFlat packed lightmap 修复）；**光影下黑三角未解决**；
  动态光源/putBulkData shaders 钩子已接线待实机验证；诊断工具已删（6fb0748）；`gradlew build` 绿；push 已授权

## Architecture & Data Flow

```
Minecraft 启动 → OptiRefineCore (@IFMLLoadingPlugin, SortingIndex 1000)
  ├─ static 块 setupTransformers() 注册 ASM transformers（core/transformer/）
  │   ├─ OptifineTransformerTransformer: 劫持 optifine.OptiFineClassTransformer
  │   │   （transform→transform0 + couldNotTransform 恒 true = 禁用 OF 补丁；URL.toURI→url2uri 修复）
  │   └─ OptiRefineConfigTransformer: 把 stub mods.Hileb.../Config 引用重写为根包真 Config
  │       （target GameSettingsOptionOF；DefaultMixinPlugin.postApply 对每个 mixin 目标类也调用）
  ├─ getModContainerClass → Container（DummyModContainer，读 coremodLocation 的 /mcmod.info）
  ├─ IMixinConfigPlugin（$DefaultMixinPlugin / $ModMixinPlugin）→ 两个 mixin json
  └─ mixin postApply → CursedMixinExtensions.postApply(ClassNode)
       ├─ @Public / @AccessTransformer: 改目标成员可见性（deobf=true 时 SRG/MCP 双匹配）
       ├─ @AccessibleOperation: 把 native 桥方法占位替换为实际字节码指令（GET/PUT/INVOKE/NEW/INSTANCEOF/NOP）
       ├─ 构造器扩展: @NewConstructor / @ReplaceConstructor / @ShadowSuperConstructor / @ShadowSuper / @ShadowConstructor
       └─ @ChangeSuperClass / @SignatureFix / @Implements（12 注解共 15 个）
```

`optifine/Config.java`（~200 native 桩）编译期占位，运行时引用被 OptiRefineConfigTransformer 重写为
真 Config；`GameSettingsOptionOF` 用 EnumHelper 注入 70+ 个 `GameSettings.Options` 枚举常量；
`OptifineHelper` 持真 OptiFineClassTransformer 实例从 OF jar 提取资源。

运行时命名（已实锤，**决定 desc 写法**）：
- **生产（HMCL Cleanroom）**：类名 MCP（`net.minecraft...`）、成员名 **SRG**（`field_`/`func_`）
- **devrun（unimined）**：类名 MCP、成员名 MCP
- **OF jar 字节码**：类名 Mojang 混淆（`bib`/`bzw`）、成员名 MCP（`modelManager`）——OF 直接访问 vanilla 成员用 MCP 名

## Key Directories

|路径|用途|
|---|---|
|`src/main/java/mods/Hileb/optirefine/core/`|加载入口、ASM transformers、两个 IMixinConfigPlugin、Container|
|`.../core/transformer/` + `dev/`|生产 transformer + dev-only transformer（仅 deobf 环境注册）|
|`.../library/cursedmixinextensions/`|自维护"cursed" mixin 后处理（12 注解 + isSrgRuntime 双匹配）|
|`.../library/foundationx/`|ASM 工具（TransformerHelper、NonLoadingClassWriter、mini 补丁引擎）|
|`.../optifine/`|`Config` stub、`GameSettingsOptionOF`、`OptifineHelper`|
|`.../mixin/defaults/`|**125 个 mixin**，镜像 vanilla 包树（`minecraft/`，另有 `optifine/`×2、`minecraftforge/`×2）|
|`.../mixin/mods/`|空（per-mod 环境，暂未用）|
|`src/main/resources/`|mixin json ×2、`optirefine_at.cfg`（35 条，构建时改名进 `META-INF/`）|
|`src/main/resource-templates/`|blossom 模板：`mcmod.info`、`pack.mcmeta`（`{{ x }}` 占位符）|
|`patch/forge/`|运行时基线（SRG 名，Cleanroom 环境类，完整 vanilla+Forge 树）|
|`patch/deobf/`|forge 反混淆 MCP 源（2185 文件，**优先 diff**；由 `.alma/deobf.py` 重生成）|
|`patch/optifine/`|OptiFine 目标行为（MCP，含 OF 自加成员，~300 文件，无 net/optifine 包）|
|`patch/patches/`|Cleanroom 已落地改动（`.java.patch`，SRG，`--- before/+++ after` 格式，~250 文件）|
|`.alma/`|本地脚本/todos（gitignored；含 mixin_map.txt、javap_* 反汇编、~130 份 build_fix*.log）|

## Development Commands

```bash
# 构建（必须 JDK 26；wrapper 是 Gradle 9.6.1）
export JAVA_HOME="D:\Program Disk\Java\jdk-26.0.2"
./gradlew.bat build --console=plain        # 产物 build/libs/optirefine-0.0.1-indev.jar（remap + manifest）
./gradlew.bat runClient                    # devrun（MCP 运行时，由 unimined 生成）

# 部署到测试环境（用户实机；先确认游戏进程已退出，避免 jar 截断）
cp build/libs/optirefine-0.0.1-indev.jar "D:\Program Disk\HMCL\.minecraft\versions\cleanroom-0.6.7-alpha\mods\optirefine-0.0.1-indev.jar"
# 部署后验证完整性（曾发生 cp 截断导致 Truncated class file 崩溃）：
"D:\Program Disk\Java\jdk-26.0.2\bin\jar.exe" tf <目标 jar> > /dev/null && echo OK
```

无 lint / 无 test / 无 format 任务。CI（build.yml/release.yml）只跑 `./gradlew build`（JDK 26 temurin）。

## Code Conventions & Common Patterns

- **注入方法前缀**：`optiRefine$`（如 `optiRefine$registerSprite`、`optiRefine$initFields`）
- **访问器字段**：`acc_` 前缀；`@AccessibleOperation` 桥方法 `_acc_<Class>_<member>_` 或 `BufferBuilder_xxx`
- **OF 新增成员**：`@SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})`（有时加 `"MissingUnique"`）；MixinExtras 传值用 `@Share(namespace = "optirefine")`
- **Mixin 0.8.7 硬规则**（每条都踩过坑，详见 AGENT.md §4）：
  - static 成员（含 `static final`）必须是 `private static` + `@Public`，否则 apply 报 `non-private static field`
  - `@Shadow` 只准指目标类**自己声明**的成员（接口/继承成员会崩）
  - 非 void 目标方法注入用 `CallbackInfoReturnable<T>`；static 目标方法 handler 必须 `private static`
  - `@At("NEW")` 不支持数组 → 用 `@ModifyConstant(intValue, ordinal)`
  - `@Unique public` 与目标同名成员会被**静默丢弃**
  - `@AccessTransformer` 占位字段：**public 声明、不带初始值**
  - **`@Shadow` 方法不能带 `@Unique`**（apply 崩 "cannot be @Unique"——悬空注解会粘到下一个成员）
- **desc 规则（@AccessibleOperation/@AccessTransformer）**：
  - vanilla 成员 → 写 **SRG 名 + `deobf=true`**（SRG 运行时用原始名直查；deobf 仅 MCP 运行时 remap）。
    实锤：`makeAtlasSprite`=func_176604_a、`VertexBuffer.bufferData`=func_181722_a，写 MCP 名会 NoSuchMethodError
  - OF 自加成员（tsrg 无）→ 写 **MCP 名**（deobf 无影响）
- **三方对照工作流**：`patch/patches` 有 → 跳过；`diff -u patch/deobf/... patch/optifine/...` → 真正增量；只做增量
- **OF 自加成员判断**：`patch/optifine/<类>` 有、`patch/deobf/<类>` 没有 → OF 新增 → mixin 需 `@Unique`/`@Public` 提供（名字用 OF 的 MCP 名）
- 行尾 **CRLF**；Python 写文件用 `newline=''` 且模式串带 `\r\n`；改完 `wc -l` 防截断事故（发生过 17 行截断）

## 运行时大坑（实锤，每条都崩过）

- **cleanmix 不注入 `@Unique` 实例字段初始化器** → 带初始化器的字段用
  `@Inject(method = "<init>*", at = @At("RETURN"))` 通配构造器注入；字段声明**不要写 `= expr`**
- **`@Unique` 对 public 字段无效** → 需要初始化的 OF 公开字段：`private @Unique @Public` + `<init>*` 注入
- **cursed `@NewConstructor` 生成的构造器不含目标类字段初始化，也不经过 `<init>*` 注入** → 方法体内手动补
- **vanilla `growBuffer` 后 `rawFloatBuffer` 是只读**（`asReadOnlyBuffer`）→ TAIL 恢复 `asFloatBuffer()`（SVertexBuilder.calcNormal 要写）
- **运行时构造器 arity 以实际崩溃为准**（`ContainerLocalRenderInformation` 是非静态内部类 → JVM 4 参含 this$0）
- **FML AT 的 `<init>` 条目在 Cleanroom 不生效** → 用 cursed `@AccessTransformer`
- **`@At("INVOKE")` 默认在调用前**（BEFORE）→ 需要调用后执行时显式 `shift = At.Shift.AFTER`
- **方法有重载时必须写完整签名**，否则 remap 报 `ambiguous ... use FLAG_FIRST`
- **Forge 基线方法可能与 vanilla 不同**（`VertexBufferUploader.draw` 有前置 `reset()`）→ 注入位置对运行时而非 deobf
- **`@At(FIELD)` 的 target owner 必须用字段声明类**（cleanmix 严格按 owner 匹配，继承字段用子类 owner 会找不到）
- **OF 自加成员缺口 = NoSuchMethodError**：`getWorld()`（DynamicLights）、`resetClouds()`+cloudRenderer（云高度滑块）、`getRenderChunk`（getBoundingBoxParent 链）等，均由 MixinRenderGlobal 补 `@Unique @Public` 提供

## Important Files

|文件|说明|
|---|---|
|`src/main/java/mods/Hileb/optirefine/core/OptiRefineCore.java`|入口；两个 IMixinConfigPlugin；postApply 管线；DummyModContainer|
|`src/main/java/mods/Hileb/optirefine/core/transformer/OptifineTransformerTransformer.java`|禁用 OF 自带 transformer|
|`src/main/java/mods/Hileb/optirefine/core/transformer/OptiRefineConfigTransformer.java`|stub Config → 真 Config 引用重写|
|`src/main/java/mods/Hileb/optirefine/library/cursedmixinextensions/CursedMixinExtensions.java`|cursed 处理器（`isSrgRuntime`、AT 双匹配、AccessibleOperation 生成）|
|`src/main/java/mods/Hileb/optirefine/optifine/Config.java`|`Config` stub（~200 native 方法，编译期 API 面）|
|`src/main/resources/optirefine.default.mixin.json`|125 mixin 注册表（refmap `optifine.refmap.json`；JAVA_17；mixinextras 0.5.0-rc.4）|
|`src/main/resources/optirefine_at.cfg`|AT（35 条；字段/类/构造器条目有效，方法条目编译期无效）|
|`AGENT.md`|**现行交接手册**（运行时命名、三方对照、硬规则、崩溃修复记录、git 约定）|
|`FOR_AGENT.md`|阶段 1 历史文档（Blackboard/@Checked/REPATCH_CONFIG 已删，部分过时）|
|`srg_to_stable_39-1.12.tsrg`|SRG→MCP 映射（对照用 `.gradle/unimined/local/mappings/srg2mcp.tsrg` 更全）|
|`.alma/cleanroom_patch_summary.txt`|逐类标注 Cleanroom 已落地 patch（ADDED = SKIP in mixin）|

## Runtime/Tooling Preferences

- **JDK 26 必须**（`D:\Program Disk\Java\jdk-26.0.2`）；toolchain 26；勿用 JDK 8/21
- Gradle 9.6.1 wrapper；unimined 1.4.26-kappa（mixin remap + refmap 在此，非 javac AP）；shadow 9.5.1（`enable_shadow=false`）；blossom 2.2.0
- 依赖：`compileOnly` sponge-mixin 0.20.13+mixin.0.8.7、lwjglx 1.0.0、`net.optifine:optifine:1.12.2-HD_U_G5_unpatched_srg`；`runtimeOnly` optifine G5 + OptiFineDevTweaker
- 编译 classpath 按 vanilla MCP 类优先（不用 OF jar 遮蔽）；mixin 引用 OF 独有成员必须走 `@AccessibleOperation`/`@Shadow`
- git：分支 `dev`；提交身份 `Hileb <107909747+Ecdcaeb@users.noreply.github.com>`（`git -c` 传入，勿改全局）；**push 已授权**（origin/dev=6fb0748）；CRLF 行尾
- **不要访问** `D:\Program Disk\HMCL\.minecraft\libraries\`（用户禁止）

## Testing & QA

- **零自动化测试**：无 `src/test`、无 `*Test*.java`（JUnit5 配置存在但空跑）
- QA 方式 1：`.alma/` 静态扫描脚本（gitignored）
  - `python .alma/scan_review.py` —— 运行时规则审查（@Shadow/注入目标 vs `patch/deobf`、@Unique 冲突、NEW+Construction 配对）；期望 `NO PROBLEMS FOUND`
  - `python .alma/scan_accessible_ops.py` —— @AccessibleOperation desc/opcode 形状审计
  - `python .alma/scan_wrapmethod_arity.py` —— @WrapMethod handler arity vs Forge 基线
  - `python .alma/check_registered.py` —— json 注册核对（期望 `ALL REGISTERED` + `registered total: 125`）
  - `python .alma/deobf.py` —— 重新生成 `patch/deobf`
- QA 方式 2（主流程）：**实机崩溃循环**——用户 HMCL 跑 `cleanroom-0.6.7-alpha`（+ `preview_OptiFine_1.12.2_HD_U_G6_pre1.jar`），发 `logs\latest.log` / `crash-reports\crash-*.txt`；修 → build → commit → 拷贝 jar → 再测。日志 grep 模式：`FATAL|Mixin apply|InvalidInjection|NoSuchField|NoSuchMethod|IllegalAccess`
- 行为对照基准：`diff -u patch/deobf/... patch/optifine/...`（OF 增量 = mixin 要做的事）
- 125 mixin 三路语义审计已完成一轮（P1–P27 分批，修复已提交）；动态光源/光影黑三角待实机验证

## 渲染链已知遗留（未修，按优先级）

- **光影下黑三角**（方块+物品）——未解决；格式切换正常（开=56/关=28），数据/指针链待查（SVertexBuilder、attrib 布局）
- `getRenderQuads` face 传 `null`（OF 按 quad face 传）→ per-face 定制失效（LOW-MED）
- `AmbientOcclusionFace.updateVertexBrightness` 用 vanilla 版（OF fixAoLightValue 未移植）
- `MixinModelBakery` loadModel basePath 缺 `models/` 前缀（LOW）
- `CustomColors.getDurabilityColor` 挂钩 TODO（Cleanroom patch 删了 hsvToRGB）
- `MixinWorldEntitySpawner.findChunksForSpawning` OF 逻辑未实现（注释掉）
- `MixinFMLClientHandler` brand 追加待运行时验证
- Kirino headless 4 段（renderWorldPass 的 Cleanroom 渲染委托阶段）未重实现（TODO）
- emissive 重渲染在 Cleanroom patch 替换 renderQuad 后为死代码（已知）
- `@AccessibleOperation` desc 点分隔风格（26 处 MIXED-SEPARATORS，处理器统一转斜杠，无功能影响）

## 二轮审计修复记录（2026-08-05，P1–P26 + core/lib 全量复查）

HIGH：①`rebuildChunk$renderBlock` layersUsed 索引用 unfixed ordinal（OF:235 用 fixBlockLayer 后 ordinal）→ 草/火把等重映射方块不可见；②`MixinThreadDownloadImageData` markUploaded 置 HEAD 使上传守卫恒 false → 皮肤/披风永不传 GL；③FaceBakery 三方法步长硬编码 7 而 makeQuadVertexData 已扩 56 → shaders 模式 quad 布局错位（疑似黑三角根因）。

MED：④`DynamicLights.update` 注入点移到 setupTerrain HEAD（OF 在 if 块外每帧执行）；⑤onEntityAdded/Removed 补 `RandomEntities.entityLoaded/Unloaded`；⑥MixinTextureUtil 两处 `@AccessTransformer` 补 SRG 名+deobf=true；⑦MixinBakedQuadRetextured texture GETFIELD 补 deobf=true；⑧MixinImageBufferDownload 补 const56（HD 皮肤镜像源矩形）；⑨MixinTextureAtlasSprite 恢复 cleanroom patch 删除的 0.01F UV inset；⑩MixinItemRenderer updateEquippedItem PUTFIELD ordinal=1→0（运行时单次写入，ordinal=1 找不到注入点）；⑪ModelRenderer render/renderWithRotation 全替换含 scaleX/Y/Z 分支。

LOW/注释清理：renderCloudsCheck 补 `renderDistanceChunks>=4`（OF:1549）；BlockFluidRenderer 补返回前 `setSprite(null)`（OF:290）；MixinRender.renderEntityOnFire 循环内补 `setSprite`（OF:138，多纹理火纹）；23 条过期 [AUDIT-ISSUE] 注释更新为 [AUDIT-OK]（onPlayerPositionSet/float 选项/State 内嵌类 desc 等——均先前已修复）；scan_review.py 支持 `<init>*` 通配与 patch/patches 落地方法（恢复 NO PROBLEMS FOUND）。

实证关闭：`State` 为非静态内部类（forge:582），NEW/@NewConstructor desc 含外层 this$0 正确；`stateQuadSprites` 有 cursed @Public 无 IllegalAccessError；CustomItems 走 Reflector.ModelLoader 独立路径不调 ModelBakery.loadItemModel（MED 关闭）；SVertexFormat.duplicate 深拷贝（FORGE_BAKED 非 live 引用）。
