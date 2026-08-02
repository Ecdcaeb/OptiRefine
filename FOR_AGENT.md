# FOR_AGENT.md — OptiRefine 交接 / 工具链迁移手册

> **停工交接文档。** 读完再动代码。  
> 最后更新：2026-08-02  
> 用途：迁移工具链、换 agent、恢复上下文时的唯一权威说明。

---

## 0. 一句话状态

OptiRefine = 用 **Mixin** 把 OptiFine 对 1.12.2 的字节码 patch 重写到 **Cleanroom/Forge** 上。
**122 个 mixin 文件已写完并注册**；2026-08-02 已完成一轮运行时规则审查 + 工具链升级，`gradlew build` 通过（compileJava + remap），但**尚未在游戏中实机验证**，不能当“可发版”。

---

## 0.5 工具链（2026-08-02 已升级，参照 CleanroomModTemplate `mixin` 分支）

| 组件 | 版本 | 备注 |
|---|---|---|
| Gradle | 9.6.1（wrapper） | 本机已缓存 dist |
| JDK | **26**（`D:\Program Disk\Java\jdk-26.0.2`） | build.gradle toolchain=26；**不要用 JDK 8/21** |
| unimined | 1.4.26-kappa | 旧 1.4.9-kappa 已弃 |
| shadow / blossom / idea-ext | 9.5.1 / 2.2.0 / 1.4.1 | |
| Cleanroom loader | 0.5.17-alpha | 旧 0.4.2-alpha 已弃 |
| sponge-mixin | 0.20.13+mixin.0.8.7 | |

- 构建：`./gradlew build`（JAVA_HOME 指到 JDK 26；旧 wrapper 9.1.0 会被自动换到 9.6.1）
- `mcmod.info`/`pack.mcmeta` 已迁到 `src/main/resource-templates/`（blossom `{{ x }}` 语法，`src/main/resources/` 下不再有这两个文件）
- manifest 带 `MixinConfigs: optirefine.default.mixin.json,optirefine.mods.mixin.json` 与 `crl.dev.mixin`
- **编译 classpath 现在按 vanilla MCP 类优先**（旧构建靠 OF jar 的类遮蔽才能过编译，已废弃）：mixin 引用 OF 独有成员必须走 `@AccessibleOperation`/`@Shadow`（见 §4），否则编译报错——这是特性不是 bug

---

## 1. 项目是什么

| 项 | 说明 |
|---|---|
| MC | 1.12.2 |
| 运行时 | **Cleanroom（=Forge 环境）**，不是 vanilla |
| 目标 | OptiFine 兼容 Cleanroom，减少破坏性 |
| 手段 | Mixin + mixinextras + cursed mixin extensions |
| 注册 | `src/main/resources/optirefine.default.mixin.json` |
| 主包 | `mods.Hileb.optirefine` |

**核心原则（最重要）：**  
Mixin **只实现** OptiFine 相对 Cleanroom 的**增量**。Cleanroom/`patch/patches` 已有的 Forge 钩子 **禁止重复做**。

---

## 2. 目录地图（迁移后先认路）

```
OptiRefine/
├── FOR_AGENT.md                 ← 本文件
├── README.md                    ← 对外状态（122/122 已更新，但仍写“无可用版本”）
├── srg_to_stable_39-1.12.tsrg   ← SRG→MCP 映射
├── patch/
│   ├── forge/                   ← 运行时基线（SRG 名，Cleanroom 环境类）
│   ├── optifine/                ← OptiFine 目标行为（MCP 名）
│   ├── patches/                 ← Cleanroom 已应用 patch（.java.patch）
│   └── deobf/                   ← 由 .alma/deobf.py 生成：forge 反混淆后的 MCP 源（2185 文件）
├── src/main/java/mods/Hileb/optirefine/
│   ├── core/                    ← 加载入口、Blackboard、transformer
│   ├── library/                 ← cursed mixin / foundationx ASM 工具
│   ├── optifine/                ← Config stub 等（运行时 remap 到真 Config）
│   └── mixin/                   ← 122 个 mixin（defaults 为主）
├── src/main/resources/
│   └── optirefine.default.mixin.json
└── .alma/                       ← 本地脚本/todos（非构建必需）
```

### 数字快照（2026-08-02）

| 指标 | 值 |
|---|---|
| mixin `.java` | **122** |
| json 注册 unique | **122** |
| `@Checked` | 目标 100%（批量补过） |
| `patch/deobf` | **2185** 文件（与 forge 对齐） |
| deobf 残留 | 约 **46** 个 `func_`、**103** 个 `field_`（tsrg 无映射或冲突） |

---

## 3. 三方对照工作流（写/审 mixin 必做）

| 路径 | 角色 | 命名 |
|---|---|---|
| `patch/forge/**` | **运行时基线** | SRG（`func_`/`field_`） |
| `patch/deobf/**` | forge 的 MCP 可读版 | MCP（优先用来 diff） |
| `patch/optifine/**` | OptiFine 目标行为 | MCP |
| `patch/patches/**` | Cleanroom 已落地改动 | SRG `.patch` |
| `srg_to_stable_39-1.12.tsrg` | 映射表 | — |

**推荐顺序：**

1. 看 `patch/patches/<类>.java.patch` 的 `+` 行 → **已存在，跳过**  
2. `diff -u patch/deobf/.../Foo.java patch/optifine/.../Foo.java` → 真正要做的增量  
3. 只把第 2 步有、第 1 步没有的改动做成 mixin  
4. 注册 json（若新文件）+ 加 `@Checked`

**不要**再往 mixin 尾部粘贴大段 diff 注释（用户要求已全部删掉）。

---

## 4. 硬规则（用户明确，违反=运行时炸）

运行时基线 = **Cleanroom patched 环境**，不是 OptiFine jar 里的类。

| 场景 | 正确做法 | 错误做法 |
|---|---|---|
| 访问**基线已有**私有字段/方法 | `@Shadow` 或 `@AccessibleOperation` | 瞎猜签名 |
| **A 类**要调 **B 类 OptiFine 新增**方法 | `@AccessibleOperation`；或 B 的接口 mixin 注入 default/实现后再用 | 在 A 上 `@Shadow` B 的新方法 |
| OptiFine **新增实例方法**到目标类 | `@Unique`（+ `@Public` 如需对外） | `@Shadow` 不存在的方法；`@WrapMethod` 不存在的方法 |
| OptiFine **新增构造器** / 改构造 | **cursed mixin extensions**：`@NewConstructor` / `@ReplaceConstructor` / `@ShadowSuperConstructor` 等 | `@Unique` **不能**当构造器用；不要对 `new` 指 `@Unique` |
| Cleanroom 已有 Forge 钩子 | **跳过**，javadoc 注明 | 再包一层导致双调用 |

### cursed / AccessibleOperation 要点

```java
// 访问私有成员（运行时存在）
@SuppressWarnings({"unused", "MissingUnique"})
@AccessibleOperation(opcode = Opcodes.GETFIELD,
    desc = "net.minecraft.client.renderer.BufferBuilder animatedSprites Ljava/util/BitSet;")
private static native BitSet BufferBuilder_animatedSprites_get(BufferBuilder buffer);

// NEW 必须配对 Construction 哨兵（见 MixinModelRenderer / MixinBufferBuilder / MixinRenderChunk）
@AccessibleOperation(opcode = Opcodes.NEW, desc = "...")
private static native Xxx _new_Xxx(AccessibleOperation.Construction c, ...);
// 调用处: _new_Xxx(AccessibleOperation.Construction.construction(), ...)
```

### 命名与注解

- 类：`@Checked` + `@Mixin(Target.class)` 或 `@Mixin(targets="...$Inner")`
- 注入方法前缀：`optiRefine$`
- 新字段：`@Unique`，OptiFine 原名；对外 `@Unique @Public`
- Config stub：`mods.Hileb.optirefine.optifine.Config`（运行时 transformer remap 到真 `Config`）
- `net.optifine.*` 直接 import（由 OptiFine jar 提供）

### Reflector

Cleanroom 下 **直接展开** 为 Forge API：

- `Reflector.ForgeHooksClient_*` → `ForgeHooksClient.*`
- `Reflector.ForgeEventFactory_*` → `ForgeEventFactory.*`
- 若 patch 已含 → 整段跳过

---

## 5. 范例 mixin（风格对齐）

优先模仿：

- `mixin/.../renderer/MixinGlStateManager.java`
- `mixin/.../renderer/MixinItemRenderer.java`
- `mixin/.../renderer/MixinOpenGlHelper.java`
- `mixin/.../texture/MixinTextureMap.java`
- `mixin/.../texture/MixinTextureAtlasSprite.java`

接口补方法范例：

- `MixinITextureObject`：`getMultiTexID()` default → `MixinTextureMap` 再 `@Shadow` 合法

构造器范例：

- `MixinModelBox` / `MixinBufferBuilderState`：`@ShadowSuperConstructor` + `@NewConstructor` + `_Object()` 占位

---

## 6. core / library（已整理过）

### 保留运行时逻辑

- `OptiRefineBlackboard.CLASSES`：允许 patch 的白名单  
- `REPATCH_CONFIG` → `config/optirefine_patch_cfg.json` 按类开关  
- `isOverwritePatches(name)`：仅看配置（**已删** `IS_INDEV` / `INDEV_CLASS` / `TEST_INDEV_CLASS`）

### core 已修

- `OptiRefineCore`：`;;`、null `coremodLocation`、`ModMixinPlugin` split 越界、shouldApply 日志降 debug  
- `OptiRefineLog`：`write(char[],off,len)` 正确切片  
- Blackboard：死 import、重复 `LoadingScreenRenderer`、过时 TODO 注释

### Config remap

- `OptiRefineConfigTransformer.transform0`：mixin postApply 时把 stub Config 引用 remap 到默认包 `Config`  
- 非 mixin 目标含 `GameSettingsOptionOF`

---

## 7. 审查结论与未完成项（迁移后优先）

### 已修（本轮 2026-08-02 确认）

| 项 | 说明 |
|---|---|
| `MixinRenderItem` | `@Shadow renderModel` 签名改为 `(IBakedModel, ItemStack)` / `(IBakedModel, int, ItemStack)`；2 参 color 用 `@Unique` 转发；emissive `@Inject` target 同步修正 |
| `MixinRenderChunk` | `createRegionRenderCache` 从错误 `@Shadow` 改为 `@Unique` 实现 `new ChunkCache(...)` |
| `MixinTextureMap` | `counterIndexInMap.reset()` 移到 `registerSprites` **BEFORE**；detectMipmap 仍 AFTER |
| `MixinStitcher` | 补 `expandAndAllocateSlot` OptiFine 扩容决策 |
| 尾部 diff 注释 | 全库已删 |
| json | 122 全注册，含 `MixinBlockModelRender$AmbientOcclusionFace` |

### 本轮（2026-08-02 工具链升级后）新修：编译期审查 + 修复

**审查工具**：`.alma/scan_review.py`（对照 `patch/deobf` 基线校验 `@Shadow`/注入目标/`@Unique` 冲突/NEW+Construction；Mixin 0.8 语义：private @Unique 会被重命名、public @Unique 与目标方法同名同签名会被**静默丢弃**、`@Override+@Unique` 是合法覆写）—— 当前 **NO PROBLEMS FOUND**。

**运行时修复（旧会话遗留）**：

| 文件 | 修复 |
|---|---|
| `MixinFontRenderer` | 删死代码 `doDraw`；`@Expression("(float)((int)?)")` + `@Local(ordinal=1)` 实现 OF 浮点步进 `posX += f`；删 `@Shadow setColor/enableAlpha/bindTexture`（基线不存在→apply 崩溃）；ctor `@WrapOperation` target 改为 `TextureManager.bindTexture` |
| `MixinRenderGlobal` | `countTileEntitiesRendered/renderedEntity/renderInfosEntities/renderInfosTileEntities` `@Shadow`→`@Unique @Public`（OF 新增字段） |
| `MixinRenderItem` | `modelLocation/renderItemGui/renderModelEmissive` `@Shadow`→`@Unique @Public` |
| `MixinSetVisibility` | `setVisible` `@Unique`→`@Overwrite`（原 public @Unique 与 Forge 已加的 `setVisible(EnumFacing,EnumFacing,boolean)` 冲突被静默丢弃，导致 `optiRefine$bits` 存储与基线 `bitSet` 不同步） |
| `MixinBufferBuilder` | 删重复 `putColorRGB_F4`（与基线相同，被丢弃） |
| `MixinWorldClient` | 删无用 `@Unique visibleChunks`（基线已有同名） |
| `MixinTextureMap` | 删 `@Shadow getMultiTexID/getGlTextureId`（接口/继承成员不能 @Shadow，apply 崩溃；mixin 已 implements `ITickableTextureObject`） |

**编译修复（升级后 vanilla 类优先 classpath 暴露，~200 个错误）**：

- `MixinBlockModelRenderer`：补 18 个 import；`stateIn.f()/g()`→`getOffset()/useNeighborBrightness()`；`renderQuadsSmooth` 尾部移进循环（原 only-last-quad bug）；`isEmissive/getVertexDataSingle/isMultiTexture/getXOffset/getQuadEmissive/isDrawing/getYOffset/getZOffset` → `@AccessibleOperation` helpers；`makeCustomQuadsRenderModel*` 改调自身 `@Unique renderQuadsSmooth/Flat`；`renderModel` 补 `return rendered`
- `MixinEntityRenderer`：import 修正（`IBlockState` 包、`MemoryMonitor`→`net.optifine.util`）；`ofKeyBindZoom/ofShowFps/ofProfiler/ofCloudsHeight/ofSmoothFps/ofChunkUpdates/ofLazyChunkLoading` → GameSettings GET/PUT helpers；`ClippingHelper.disabled`、`RenderGlobal.damagedBlocks`、`GlStateManager.isFogEnabled/setFogEnabled` → helpers；`ShadersRender.renderHand0/1/2/renderFPOverlay(this,...)` 加 `(EntityRenderer)(Object)` cast；`RenderGlobal.getCountLoadedChunks/getCountChunksToUpdate/hasNoChunkUpdates` → helpers（MixinRenderGlobal 补 `@Unique @Public` 实现，`getCountLoadedChunks` 用 `ChunkProviderClient.loadedChunks` GETFIELD）
- `MixinRenderGlobal`：import 修正（`BlockPos.PooledMutableBlockPos`、`IBlockState` 包）；`renderEntitiesCounter` → GETSTATIC/PUTSTATIC helpers；`fogStandard/updateItemRenderDistance/updateTextRenderDistance` → helpers；`iblockstate.hasTileEntity()`→`getBlock().hasTileEntity(iblockstate)`；补 `@Shadow chunksToUpdate/renderDispatcher/registerSprite/getResourceLocation/generateMipmaps`；删重复 `hasNoChunkUpdates`（基线已有，语义相同）
- `MixinGlStateManager`：补 `@Shadow alphaFunc/deleteTexture/blendFunc/tryBlendFuncSeparate`（static 上下文调用需要）
- `MixinTextureAtlasSprite`：`@NewConstructor _TextureAtlasSprite(String,boolean)` + `@ShadowSuper _Object` + `_new_TextureAtlasSprite`（NEW+Construction）替代 `new TextureAtlasSprite(name,true)`；`copyFrom` 的 OF 字段 → GETFIELD helpers；`loadShadersSprites` 的 `new TextureAtlasSprite(s)`（protected 构造器）→ 由 AT 放 public；补 `@Unique setAnimationMetadata`（运行时需要，勿重复添加）；`fixTransparentColor` 内层 `l2`→`l6`
- `MixinTextureMap`：`@Shadow registerSprite/getResourceLocation/generateMipmaps`；~30 个 `@AccessibleOperation` helpers（sprite 字段读写、`getMultiTexID`、`isAbsoluteLocation`、`bindSpriteTexture`、`updateIndexInMap`、`getFramesTextureData/getAnimationMetadata` 等）；`TextureUtil.bindTexture` → INVOKESTATIC helper（**AT 方法条目编译期不生效**，字段/类/构造器 AT 生效）
- `MixinDynamicTexture`：`@Redirect` target `uploadTexture(ILjava/nio/IntBuffer;II)`→`uploadTexture(I[III)`，handler 参数 `IntBuffer`→`int[]`（原来匹配不到注入点）
- `optirefine_at.cfg` 新增：`GlStateManager$AlphaState/BlendState/FogState/TextureState/BooleanState`、`RenderGlobal$ContainerLocalRenderInformation`+`renderChunk`、`LayerArmorBase ENCHANTED_ITEM_GLINT_RES`、`TextureState <init>`、`TextureAtlasSprite width/height/frameCounter/<init>(String)`、`EntityRenderer frameCount`、`TextureUtil DATA_BUFFER`（方法 AT 无效，`bindTexture` 用 helper）

### 全库逐文件浏览（2026-08-02，6 个并行只读 agent 覆盖 122 文件）+ 人工核实后修复

**修复（真实 bug）**：

| 文件 | 修复 |
|---|---|
| `library/.../CursedMixinExtensions.java` | **字段型 AccessibleOperation 的 owner 不做 `.`→`/` 替换**（INVOKE/NEW 有）→ 点分 owner 生成非法字节码；补 `owner.replace('.','/')`（影响全部 GET/PUT 字段操作） |
| `MixinBlockModelRenderer` | `quadBounds[52+j]` → `[2+j]`（NORTH2，float[12] 越界，平滑 AO 渲染必 AIOOBE）；删死 helper `BlockModelRenderer_renderQuadsSmooth` |
| `MixinModelRenderer` | `renderWithRotation` @WrapMethod 缺 `original.call(scale)`（模型渲染被吞） |
| `MixinIntegratedServer` | `injectSaveAllWorlds` 非 cancellable 的 `return` 不生效 → `cancellable=true` + `ci.cancel()` |
| `MixinVboRenderList` | `shouldRender` 在 `!isRenderRegions()` 时也返回 false（丢 vanilla 渲染）→ 加 `return true` 回退 |
| `MixinBakedQuadRetextured` | `@Redirect` target `BakedQuadRetextured;sprite` → `BakedQuad;sprite`（字段声明在父类，原 target 运行时匹配不到） |
| `MixinRenderXPOrb` | 自定义颜色 `col<0` 时 r/g/b 留 0 → 黑球；补 `else` 设 -1 回退 |
| `MixinGameSettings` | anaglyph 守卫 `!this.anaglyph` 时序不可达 → `this.anaglyph` + 翻回 false（与 OF 阻止语义一致） |
| `MixinGuiCustomizeSkin` | 重复 `add(done)`（Done 按钮出现两次）→ 删除 |
| `MixinImageBufferDownload` | 14 个 `@ModifyConstant` handler `kRef=0` 时返回 0 → `imageWidth=64` 被改成 0 → vanilla 倍增循环死循环；全部加 `kRef.get() > 0 ? ... : constant` 保护 |
| `MixinBlockFluidRenderer` | 删冗余重复 `@Inject(HEAD)`（optirefine namespace 的 renderEnv share 无人读） |
| `MixinRenderLivingBase` | 删 `@Unique @Public NAME_TAG_RANGE/_SNEAK`（与基线同名字段，public @Unique 会被静默丢弃） |
| `MixinCompiledChunk_1` | 删死 `setAnimatedSprites` throw 桩 |
| `MixinGuiDownloadTerrain` | 字段名 `gradle_properties$customLoadingScreen`（模板占位符）→ `optiRefine$customLoadingScreen` |
| `MixinBlockPartDeserializer` | 清注释残留（parseAngle 行为保留，与 vanilla 等价） |

**已核实非问题（勿重复排查）**：`rebuildWorldView` 空 wrap（OF 本就掏空）；`ClassInheritanceMultiMap` empty 缓存（OF 同款缺陷）；`GameSettings` VRAM 双 if / MIPMAP default 字面 key（与 OF 一致）；`ScreenShotHelper`（与 OF 一致）；`LayerEntityOnShoulder` UUID `==`（OF 同款）；`DefaultResourcePack` @WrapOperation（cleanroom patch 后是 `invokestatic FolderResourcePack.validatePath`，target 匹配）；`parseAngle`（vanilla 也无默认值）。

**已知缺失/遗留（不炸但未实现）**：`MixinGuiVideoSettings.optiRefine$actionPerformedRightClick` 未接线（OF 的右键递减由 transformer 注入 GuiScreen，mixin 侧无注入点）；空壳 mixin（无成员）：`MixinModelRotation`、`MixinMatrix4f`、`MixinBlockStateContainer`(world/chunk)、`MixinI18n`(util/text/translation)、`MixinGuiIngameForge`、`MixinCompiledChunk_1`、`MixinBlockStateContainer`(block/state)——注册但无操作，保留；cosmetic：`MixinBakedQuad`/`MixinPlayerChunkMap` handler 重名、`MixinTessellator` handler 名 `ijdraw`、`MixinLayerArmorBase` 重复 import + 死 `@Shadow skipRenderGlint`。

### 已知 / 未完成（**仍不能当可玩**）

1. **运行时验证缺失**：以上全部修复只到 `gradlew build` 通过；mixin apply、AccessibleOperation desc 与运行时 SRG 的映射、cursed 构造器扩展等**均未实机验证**。下一步：起游戏看日志（先看 `@Checked` mixin 是否全部 apply 成功，再看 `AccessibleOperation` 是否有 desc 不匹配告警）
2. **`MixinFontRenderer` 浮点步进**：`@Local(ordinal = 1)` 依赖 LVT 顺序（f1 在前 f 在后）；若实机报 local 缺失，改为捕获 `(int)` 前值或重写步进
3. **`RegionRenderCacheBuilder`**：OF 1.12.2 中位于 `net.minecraft.client.renderer` 包（编译 classpath 确认）；运行时由 OF jar 提供，未验证加载
4. **@Checked 不等于正确**：批量打标，仍可能有逻辑错误

### 旧遗留（已按上方处理，保留供对照）

1. ~~`MixinFontRenderer.optiRefine$doDraw`~~ → 已删（无调用点）；浮点步进已用 `@ModifyExpressionValue` 实现
2. ~~全库审查未完成~~ → `scan_review.py` 全绿
3. ~~`@Checked` 不等于正确~~ → 仍成立，见上
4. ~~并行会话~~ → 先读再写
5. ~~不要本地完整编译~~ → 已可用：`JAVA_HOME` 指 JDK 26 后 `./gradlew build` 全链路通过

### 合理为空 / 不需 mixin

- `IntegerCache`：纯重命名  
- `GameRules` 顶层：无 OF 逻辑；`$Value` 有 `MixinGameRulesValues`  
- 大量 `$1` 匿名内部类：外层 mixin 覆盖即可  
- `ModelRotation` 等：Cleanroom 已含

---

## 8. 工具链（`.alma/`）

| 脚本 | 作用 |
|---|---|
| **`deobf.py`** | `patch/forge` → `patch/deobf`（全局替换 func_/field_/类名） |
| **`scan_review.py`** | **运行时规则审查（本轮新增）**：`@Shadow`/注入目标 vs deobf 基线；`@Unique` 冲突（签名感知 + Mixin 0.8 丢弃语义）；NEW+Construction 配对。用法：`python .alma/scan_review.py` |
| `check_registered.py` | mixin 文件 vs json |
| `check_blackboard.py` | Blackboard.CLASSES vs mixin 目标（内部类会误报） |
| `validate.py` / `add_checked.py` | 结构 / 批量 @Checked |
| `scan_mixins.py` / `scan2.py` / `scan3.py` | 历史扫描 |
| `todos-*.md` | 会话 todos |

### 反混淆用法

```bash
# 在项目根
python .alma/deobf.py
# 可选
python .alma/deobf.py --src patch/forge --out patch/deobf --tsrg srg_to_stable_39-1.12.tsrg
```

说明：

- 字段 tsrg 行格式：`field_xxx name`（2 token）  
- 方法：`func_xxx (desc) name`（3+ token）  
- **func_/field_ 按全局唯一名直接 replace**（用户要求，不做复杂按类消歧）  
- 残留 SRG：tsrg 无条目或极少冲突；diff 时留意  

迁移工具链时：

1. 保留 `srg_to_stable_39-1.12.tsrg` + `patch/{forge,optifine,patches,deobf}`  
2. 保留 `.alma/deobf.py`（或迁到 `scripts/`）  
3. IDE/agent 默认 diff：`deobf` vs `optifine`  
4. 构建仍用源码 mixin + Cleanroom，**不要**把 `patch/deobf` 当编译源

---

## 9. 迁移检查清单

- [ ] 克隆/同步仓库，确认 122 mixin + json  
- [ ] 有 `patch/deobf`；否则跑 `python .alma/deobf.py`  
- [ ] 读本文件 §4 硬规则  
- [ ] 从 §7 未完成项接着审查（先 FontRenderer 浮点步进）  
- [ ] 改 mixin 后：对照 deobf/optifine/patches，更新 json，勿粘 diff 注释  
- [ ] 多 agent：**read-before-write**  
- [ ] 对外仍视为 **无稳定发行版**（README 警告保留）

---

## 10. 不要做的事

1. 不要 `@Shadow` / `@WrapMethod` **Cleanroom 基线不存在**的成员  
2. 不要用 `@Unique` 冒充构造器 / 替代 `new` 目标类型的新构造  
3. 不要重复 Cleanroom 已打的 Forge 事件/钩子  
4. 不要恢复 mixin 尾部巨型 patch 注释  
5. 不要把 `patch/deobf` 当运行时 classpath  
6. 不要在没读文件的情况下批量覆盖并行会话的改动  

---

---

**交接完成。** 下一任：先跑 deobf（若缺），再按 §7 做运行时规则审查，不要假设 122=`@Checked` 等于可玩。
