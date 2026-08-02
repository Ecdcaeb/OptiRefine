package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.optifine.CustomColors;
import net.optifine.GlErrors;
import net.optifine.Lagometer;
import net.optifine.util.MemoryMonitor;
import net.optifine.RandomEntities;
import net.optifine.reflect.ReflectorResolver;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;
import net.optifine.util.TextureUtils;
import net.optifine.util.TimedEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.FloatBuffer;

/**
 * OptiFine additions to {@link EntityRenderer}.
 *
 * <p>Implements the core OptiFine render hooks that are NOT already present in the
 * Cleanroom-patched runtime:</p>
 * <ul>
 *     <li>{@code updateCameraAndRender}: {@code frameInit}/{@code frameFinish}, server-thread wait,
 *     FPS/lagometer overlay and profiler toggle hooks (via {@code @Inject}, so the runtime body —
 *     Kirino render delegation, Forge {@code drawScreen} hook, {@code TileEntityRendererDispatcher}
 *     fixes — is preserved untouched)</li>
 *     <li>{@code getFOVModifier}: dynamic FOV + zoom key (Config.zoomMode)</li>
 *     <li>{@code setupCameraTransform}: fog fancy/fast far-plane multipliers + {@code clipDistance}</li>
 *     <li>{@code renderHand}: {@code Shaders.applyHandDepth} + {@code ShadersRender.renderItemFP}
 *     and the new public {@code renderHand(FI)ZZZ} overload used by {@code ShadersRender}</li>
 *     <li>{@code enableLightmap}/{@code disableLightmap}: shaders notifications</li>
 *     <li>{@code updateLightmap}: {@code CustomColors.updateLightmap} early-out</li>
 *     <li>{@code renderWorld}: {@code Shaders.beginRender}</li>
 *     <li>{@code renderWorldPass}: full shaders-wrapped render loop
 *     ({@code beginRenderPass}, viewport/clear/camera, frustum culling disable,
 *     sky/terrain/particles/weather/water/hand shaders hooks, {@code Shaders.endRender})</li>
 *     <li>{@code renderCloudsCheck}: {@code Config.isCloudsOff}/{@code Shaders.shouldRenderClouds}
 *     condition + {@code clipDistance} projection</li>
 *     <li>{@code updateFogColor}: {@code CustomColors} sky/fog/underwater/underlava colors +
 *     {@code Shaders.setClearColor}</li>
 *     <li>{@code setupFog}: {@code fogStandard} flag, clear-water density, fog-start factor and
 *     NV_fog_distance modes</li>
 *     <li>{@code setFogColorBuffer}: {@code Shaders.setFogColor}</li>
 *     <li>new helpers: {@code setFxaaShader}, {@code frameInit}, {@code frameFinish},
 *     {@code waitForServerThread}, {@code checkLoadVisibleChunks}/{@code loadAllVisibleChunks}</li>
 * </ul>
 *
 * <p>Already provided by the Cleanroom patches and therefore skipped: the Forge
 * {@code ForgeHooksClient.loadEntityShader} branch, the {@code orientCamera} bed/CameraSetup
 * event, the {@code getLightmapColors} hook in {@code updateLightmap}, the block
 * {@code getFogColor}/{@code FogColors} event and the {@code getFogDensity}/{@code onFogRender}
 * hooks (these are re-expressed directly where the OptiFine body contains the same Reflector
 * calls). The Kirino headless render-delegate phases inside {@code renderWorldPass} are a
 * Cleanroom-only feature and are not re-implemented here (see TODO).</p>
 *
 * <p>Minor OptiFine extras left as TODO: new-version/64-bit-Java chat notifications and
 * {@code GuiChatOF} swap in {@code frameInit}, {@code RenderChunk.renderChunksUpdated} reset in
 * {@code loadAllVisibleChunks}, {@code Minecraft.actionKeyF3} toggle, the {@code cameraZoom}
 * scaling in {@code setupCameraTransform} (dead branch in the reference — {@code cameraZoom} is
 * never assigned, so it stays 1.0) and the {@code Config.isRainOff()}/{@code addRainParticles}
 * rain hooks in {@code renderRainSnow}. The {@code ShaderLinkHelper} init in {@code updateRenderer}
 * is already present in the vanilla runtime body, so no hook is needed.</p>
 */
@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 4

    // ===== access-transformed fields (publicized by OptiFine) =====

    @AccessTransformer(deobf = true, name = "field_78516_c")
// [AUDIT-OK] vanilla SRG field_78516_c = itemRenderer, deobf=true
    public ItemRenderer acc_itemRenderer;

    // Note: field_78527_v = mouseFilterXAxis, field_78526_w = mouseFilterYAxis (srg_to_stable_39-1.12.tsrg)
    @AccessTransformer(name = "field_78527_v", deobf = true)
// [AUDIT-OK] vanilla SRG field_78527_v = mouseFilterXAxis, deobf=true
    public MouseFilter acc$mouseFilterXAxis;

    @AccessTransformer(name = "field_78526_w", deobf = true)
// [AUDIT-OK] vanilla SRG field_78526_w = mouseFilterYAxis, deobf=true
    public MouseFilter acc$mouseFilterYAxis;

    @AccessTransformer(name = "field_175080_Q", deobf = true)
// [AUDIT-OK] vanilla SRG field_175080_Q = fogColorRed, deobf=true
    public float acc$fogColorRed;

    @AccessTransformer(name = "field_175082_R", deobf = true)
// [AUDIT-OK] vanilla SRG field_175082_R = fogColorGreen, deobf=true
    public float acc$fogColorGreen;

    @AccessTransformer(name = "field_175081_S", deobf = true)
// [AUDIT-OK] vanilla SRG field_175081_S = fogColorBlue, deobf=true
    public float acc$fogColorBlue;

    @AccessTransformer(name = "field_175084_ae", deobf = true)
// [AUDIT-OK] vanilla SRG field_175084_ae = frameCount, deobf=true
    public int frameCount;

    // ===== shadowed vanilla fields =====

    @Shadow
// [AUDIT-OK] baseline member mc (SRG field_78531_r)
    private Minecraft mc;

    @Shadow
// [AUDIT-OK] baseline member shaderGroup (SRG field_147707_d)
    private net.minecraft.client.shader.ShaderGroup shaderGroup;

    @Shadow
// [AUDIT-OK] baseline member useShader (SRG field_175083_ad)
    private boolean useShader;

    @Shadow
// [AUDIT-OK] baseline member farPlaneDistance (SRG field_78530_s)
    private float farPlaneDistance;

    @Shadow
// [AUDIT-OK] baseline member lightmapColors (SRG field_78504_Q)
    private int[] lightmapColors;

    @Shadow
// [AUDIT-OK] baseline member lightmapTexture (SRG field_78513_d)
    private DynamicTexture lightmapTexture;

    @Shadow
// [AUDIT-OK] baseline member lightmapUpdateNeeded (SRG field_78536_aa)
    private boolean lightmapUpdateNeeded;

    @Shadow
// [AUDIT-OK] baseline member torchFlickerX (SRG field_78514_e)
    private float torchFlickerX;

    @Shadow
// [AUDIT-OK] baseline member fovModifierHand (SRG field_78507_R)
    private float fovModifierHand;

    @Shadow
// [AUDIT-OK] baseline member fovModifierHandPrev (SRG field_78506_S)
    private float fovModifierHandPrev;

    @Shadow
// [AUDIT-OK] baseline member cloudFog (SRG field_78500_U)
    private boolean cloudFog;

    @Shadow
// [AUDIT-OK] baseline member renderHand (SRG field_175074_C)
    private boolean renderHand;

    @Shadow
// [AUDIT-OK] baseline member debugView (SRG field_175078_W)
    private boolean debugView;

    // ===== shadowed vanilla methods =====

    @Shadow
// [AUDIT-OK] baseline member loadShader(Lnet/minecraft/util/ResourceLocation;)V (SRG func_175069_a)
    protected abstract void loadShader(ResourceLocation resourceLocationIn);

    @Shadow
// [AUDIT-OK] baseline member updateFogColor(F)V (SRG func_78466_h)
    protected abstract void updateFogColor(float partialTicks);

    @Shadow
// [AUDIT-OK] baseline member setupFog(IF)V (SRG func_78468_a)
    protected abstract void setupFog(int fogMode, float partialTicks);

    @Shadow
// [AUDIT-OK] baseline member setupFogColor(Z)V (SRG func_191514_d)
    protected abstract void setupFogColor(boolean black);

    @Shadow
// [AUDIT-OK] baseline member isDrawBlockOutline()Z (SRG func_175070_n)
    protected abstract boolean isDrawBlockOutline();

    @Shadow
// [AUDIT-OK] baseline member setupCameraTransform(FI)V (SRG func_78479_a)
    protected abstract void setupCameraTransform(float partialTicks, int pass);

    @Shadow
// [AUDIT-OK] baseline member renderCloudsCheck(LRenderGlobal;FIDDD)V (SRG func_180437_a)
    protected abstract void renderCloudsCheck(RenderGlobal renderGlobalIn, float partialTicks, int pass, double x, double y, double z);

    @Shadow
// [AUDIT-OK] baseline member renderRainSnow(F)V (SRG func_78474_d)
    protected abstract void renderRainSnow(float partialTicks);

    @Shadow
// [AUDIT-OK] baseline member renderHand(FI)V (SRG func_78476_b)
    protected abstract void renderHand(float partialTicks, int pass);

    @Shadow
// [AUDIT-OK] baseline member getFOVModifier(FZ)F (SRG func_78481_a)
    protected abstract float getFOVModifier(float partialTicks, boolean useFOVSetting);

    @Shadow
// [AUDIT-OK] baseline member updateLightmap(F)V (SRG func_78472_g)
    protected abstract void updateLightmap(float partialTicks);

    @Shadow
// [AUDIT-OK] baseline member enableLightmap()V (SRG func_180436_i)
    protected abstract void enableLightmap();

    @Shadow
// [AUDIT-OK] baseline member disableLightmap()V (SRG func_175072_h)
    protected abstract void disableLightmap();

    @Shadow
// [AUDIT-OK] baseline member hurtCameraEffect(F)V (SRG func_78482_e)
    protected abstract void hurtCameraEffect(float partialTicks);

    @Shadow
// [AUDIT-OK] baseline member applyBobbing(F)V (SRG func_78475_f)
    protected abstract void applyBobbing(float partialTicks);

    // ===== cross-class private access =====

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.RenderGlobal field_147595_R Z", deobf = true)
// [AUDIT-ISSUE] vanilla SRG field_147595_R = displayListEntitiesDirty verified, but deobf=true missing -> devrun breaks; SRG runtime OK
    private static native void RenderGlobal_displayListEntitiesDirty_set(RenderGlobal renderGlobal, boolean value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.RenderGlobal field_72738_E Ljava/util/Map;", deobf = true)
// [AUDIT-ISSUE] vanilla SRG field_72738_E = damagedBlocks verified, but deobf=true missing -> devrun breaks; SRG runtime OK
    private static native java.util.Map RenderGlobal_damagedBlocks_get(RenderGlobal renderGlobal);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.settings.GameSettings ofKeyBindZoom Lnet/minecraft/client/settings/KeyBinding;")
// [AUDIT-OK] OF member ofKeyBindZoom, not in baseline (MixinGameSettings provides)
    private static native net.minecraft.client.settings.KeyBinding GameSettings_ofKeyBindZoom_get(GameSettings gameSettings);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.settings.GameSettings ofShowFps Z")
// [AUDIT-OK] OF member ofShowFps, not in baseline
    private static native boolean GameSettings_ofShowFps_get(GameSettings gameSettings);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.settings.GameSettings ofProfiler Z")
// [AUDIT-OK] OF member ofProfiler, not in baseline
    private static native boolean GameSettings_ofProfiler_get(GameSettings gameSettings);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.settings.GameSettings ofCloudsHeight F")
// [AUDIT-OK] OF member ofCloudsHeight, not in baseline
    private static native float GameSettings_ofCloudsHeight_get(GameSettings gameSettings);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.settings.GameSettings ofSmoothFps Z")
// [AUDIT-OK] OF member ofSmoothFps, not in baseline
    private static native boolean GameSettings_ofSmoothFps_get(GameSettings gameSettings);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.culling.ClippingHelper disabled Z")
// [AUDIT-OK] OF member ClippingHelper.disabled, not in baseline (MixinClippingHelper @Public provides)
    private static native boolean ClippingHelper_disabled_get(ClippingHelper clippingHelper);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.culling.ClippingHelper disabled Z")
// [AUDIT-OK] OF member ClippingHelper.disabled, not in baseline
    private static native void ClippingHelper_disabled_set(ClippingHelper clippingHelper, boolean value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager isFogEnabled ()Z")
// [AUDIT-OK] OF member isFogEnabled()Z (MixinGlStateManager provides @Public)
    private static native boolean GlStateManager_isFogEnabled();

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager setFogEnabled (Z)V")
// [AUDIT-OK] OF member setFogEnabled(Z)V (MixinGlStateManager provides @Public)
    private static native void GlStateManager_setFogEnabled(boolean enabled);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.settings.GameSettings ofChunkUpdates I")
// [AUDIT-OK] OF member ofChunkUpdates, not in baseline
    private static native int GameSettings_ofChunkUpdates_get(GameSettings gameSettings);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.settings.GameSettings ofChunkUpdates I")
// [AUDIT-OK] OF member ofChunkUpdates, not in baseline
    private static native void GameSettings_ofChunkUpdates_set(GameSettings gameSettings, int value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.settings.GameSettings ofLazyChunkLoading Z")
// [AUDIT-OK] OF member ofLazyChunkLoading, not in baseline
    private static native boolean GameSettings_ofLazyChunkLoading_get(GameSettings gameSettings);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.settings.GameSettings ofLazyChunkLoading Z")
// [AUDIT-OK] OF member ofLazyChunkLoading, not in baseline
    private static native void GameSettings_ofLazyChunkLoading_set(GameSettings gameSettings, boolean value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.RenderGlobal getCountLoadedChunks ()I")
// [AUDIT-OK] OF member getCountLoadedChunks()I (MixinRenderGlobal provides @Public)
    private static native int RenderGlobal_getCountLoadedChunks(RenderGlobal renderGlobal);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.RenderGlobal getCountChunksToUpdate ()I")
// [AUDIT-OK] OF member getCountChunksToUpdate()I (MixinRenderGlobal provides @Public)
    private static native int RenderGlobal_getCountChunksToUpdate(RenderGlobal renderGlobal);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.RenderGlobal func_184384_n ()Z", deobf = true)
// [AUDIT-ISSUE] vanilla SRG func_184384_n = hasNoChunkUpdates verified, but deobf=true missing -> devrun breaks; SRG runtime OK
    private static native boolean RenderGlobal_hasNoChunkUpdates(RenderGlobal renderGlobal);    // ===== new fields (OptiFine) =====

    @Unique
// [AUDIT-OK] OF-added field, not in baseline
    private boolean initialized = false;

    @Unique
// [AUDIT-OK] OF-added field, not in baseline
    private World updatedWorld = null;

    @Unique
// [AUDIT-OK] OF-added field, not in baseline (@Unique public; OF: public boolean fogStandard)
    public boolean fogStandard = false;

    @Unique
// [AUDIT-OK] OF-added field, not in baseline
    private float clipDistance = 128.0F;

    @Unique
// [AUDIT-OK] OF-added field, not in baseline
    private long lastServerTime = 0L;

    @Unique
// [AUDIT-OK] OF-added field, not in baseline
    private int lastServerTicks = 0;

    @Unique
// [AUDIT-OK] OF-added field, not in baseline
    private int serverWaitTime = 0;

    @Unique
// [AUDIT-OK] OF-added field, not in baseline
    private int serverWaitTimeCurrent = 0;

    @Unique
// [AUDIT-OK] OF-added field, not in baseline
    private float avgServerTimeDiff = 0.0F;

    @Unique
// [AUDIT-OK] OF-added field, not in baseline
    private float avgServerTickDiff = 0.0F;

    @Unique
// [AUDIT-OK] OF-added field, not in baseline
    private ShaderGroup[] fxaaShaders = new ShaderGroup[10];

    @Unique
// [AUDIT-OK] OF-added field, not in baseline
    private boolean loadVisibleChunks = false;

    // ===== loadShader =====

    @WrapMethod(method = "loadShader")
    private void ifloadShader(ResourceLocation resourceLocationIn, Operation<Void> original) {
// [AUDIT-OK] target loadShader(Lnet/minecraft/util/ResourceLocation;)V matches baseline
        if (OpenGlHelper.isFramebufferEnabled()) {
            original.call(resourceLocationIn);
        }
    }

    // ===== updateRenderer =====

    // Note: OptiFine inits the shader link helper at the top of updateRenderer(). The Cleanroom
    // runtime keeps the vanilla updateRenderer() body (ShaderLinkHelper.setNewStaticShaderLinkHelper()
    // when OpenGlHelper.shadersSupported and no helper is set yet), so no hook is required here.

    // ===== getFOVModifier =====

    /**
     * OptiFine: dynamic FOV + zoom key handling. The Forge
     * {@code ForgeHooksClient.getFOVModifier} hook (already in the runtime) is kept at the end.
     */
    @WrapMethod(method = "getFOVModifier")
    private float optiRefine$getFOVModifier(float partialTicks, boolean useFOVSetting, Operation<Float> original) {
// [AUDIT-OK] target getFOVModifier(FZ)F matches baseline; Forge getFOVModifier hook kept
        if (this.debugView) {
            return 90.0F;
        }
        Entity entity = this.mc.getRenderViewEntity();
        float fov = 70.0F;
        if (useFOVSetting) {
            fov = this.mc.gameSettings.fovSetting;
            if (Config.isDynamicFov()) {
                fov *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks;
            }
        }
        boolean zoom = false;
        if (this.mc.currentScreen == null) {
            zoom = GameSettingsIsKeyDown(GameSettings_ofKeyBindZoom_get(this.mc.gameSettings));
        }
        if (zoom) {
            if (!Config.zoomMode) {
                Config.zoomMode = true;
                Config.zoomSmoothCamera = this.mc.gameSettings.smoothCamera;
                this.mc.gameSettings.smoothCamera = true;
                RenderGlobal_displayListEntitiesDirty_set(this.mc.renderGlobal, true);
            }
            if (Config.zoomMode) {
                fov /= 4.0F;
            }
        } else if (Config.zoomMode) {
            Config.zoomMode = false;
            this.mc.gameSettings.smoothCamera = Config.zoomSmoothCamera;
            this.acc$mouseFilterXAxis = new MouseFilter();
            this.acc$mouseFilterYAxis = new MouseFilter();
            RenderGlobal_displayListEntitiesDirty_set(this.mc.renderGlobal, true);
        }
        if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() <= 0.0F) {
            float f1 = (float) ((EntityLivingBase) entity).deathTime + partialTicks;
            fov /= (1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F;
        }
        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
        if (iblockstate.getMaterial() == net.minecraft.block.material.Material.WATER) {
            fov = fov * 60.0F / 70.0F;
        }
        return ForgeHooksClient.getFOVModifier((net.minecraft.client.renderer.EntityRenderer)(Object) this, entity, iblockstate, partialTicks, fov);
    }

    @SuppressWarnings("unused")
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.settings.GameSettings func_100015_a (Lnet/minecraft/client/settings/KeyBinding;)Z", deobf = true)
// [AUDIT-ISSUE] vanilla SRG func_100015_a = GameSettings.isKeyDown verified, but deobf=true missing -> devrun breaks; SRG runtime OK
    private static native boolean GameSettingsIsKeyDown(net.minecraft.client.settings.KeyBinding key);

    // ===== setupCameraTransform =====

    /**
     * OptiFine: fog fancy/fast far-plane multipliers + clipDistance (min 173.0F).
     * The reference's {@code cameraZoom} scaling branch is skipped — {@code cameraZoom} is never
     * assigned anywhere in the OptiFine reference, so the branch is dead code (see class javadoc TODO).
     */
    @Inject(method = "setupCameraTransform", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;farPlaneDistance:F", opcode = Opcodes.PUTFIELD))
    private void optiRefine$farPlaneDistance(CallbackInfo ci) {
// [AUDIT-OK] target setupCameraTransform(FI)V PUTFIELD farPlaneDistance matches baseline (line 668)
        if (Config.isFogFancy()) {
            this.farPlaneDistance *= 0.95F;
        }
        if (Config.isFogFast()) {
            this.farPlaneDistance *= 0.83F;
        }
        this.clipDistance = this.farPlaneDistance * 2.0F;
        if (this.clipDistance < 173.0F) {
            this.clipDistance = 173.0F;
        }
    }

    /**
     * OptiFine: project against {@code clipDistance} instead of {@code farPlaneDistance * 2}.
     */
    @Redirect(method = "setupCameraTransform", at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void optiRefine$setupCameraPerspective(float fovY, float aspect, float zNear, float zFar) {
// [AUDIT-OK] target setupCameraTransform(FI)V gluPerspective matches baseline
        Project.gluPerspective(fovY, aspect, zNear, this.clipDistance);
    }

    // ===== renderHand =====

    /**
     * OptiFine: {@code renderHand(FI)} delegates to the new public overload.
     */
    @WrapMethod(method = "renderHand(FI)V")
    private void optiRefine$renderHand(float partialTicks, int pass, Operation<Void> original) {
// [AUDIT-OK] target renderHand(FI)V (SRG func_78476_b) matches baseline
        this.renderHand(partialTicks, pass, true, true, false);
    }

    /**
     * OptiFine: the refactored {@code renderHand} body (public, called by {@code ShadersRender}).
     * The Forge {@code ForgeHooksClient.renderFirstPersonHand} hook is kept.
     */
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
// [AUDIT-OK] OF-added overload renderHand(FI...ZZZ)V, not in baseline (OF: public; called by ShadersRender)
    public void renderHand(float partialTicks, int pass, boolean renderItem, boolean renderOverlays, boolean isMainHand) {
        if (!this.debugView) {
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            float f = 0.07F;
            if (this.mc.gameSettings.anaglyph) {
                GlStateManager.translate((float) (-(pass * 2 - 1)) * 0.07F, 0.0F, 0.0F);
            }
            if (Config.isShaders()) {
                Shaders.applyHandDepth();
            }
            Project.gluPerspective(this.getFOVModifier(partialTicks, false), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            if (this.mc.gameSettings.anaglyph) {
                GlStateManager.translate((float) (pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
            }
            boolean sleeping = false;
            if (renderItem) {
                GlStateManager.pushMatrix();
                this.hurtCameraEffect(partialTicks);
                if (this.mc.gameSettings.viewBobbing) {
                    this.applyBobbing(partialTicks);
                }
                sleeping = this.mc.getRenderViewEntity() instanceof EntityLivingBase
                        && ((EntityLivingBase) this.mc.getRenderViewEntity()).isPlayerSleeping();
                boolean renderFirstPerson = !ForgeHooksClient.renderFirstPersonHand(this.mc.renderGlobal, partialTicks, pass);
                if (renderFirstPerson && this.mc.gameSettings.thirdPersonView == 0 && !sleeping
                        && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator()) {
                    this.enableLightmap();
                    if (Config.isShaders()) {
                        ShadersRender.renderItemFP(this.acc_itemRenderer, partialTicks, isMainHand);
                    } else {
                        this.acc_itemRenderer.renderItemInFirstPerson(partialTicks);
                    }
                    this.disableLightmap();
                }
                GlStateManager.popMatrix();
            }
            if (!renderOverlays) {
                return;
            }
            this.disableLightmap();
            if (this.mc.gameSettings.thirdPersonView == 0 && !sleeping) {
                this.acc_itemRenderer.renderOverlays(partialTicks);
                this.hurtCameraEffect(partialTicks);
            }
            if (this.mc.gameSettings.viewBobbing) {
                this.applyBobbing(partialTicks);
            }
        }
    }

    // ===== lightmap =====

    @WrapMethod(method = "enableLightmap")
    private void optiRefine$enableLightmap(Operation<Void> original) {
// [AUDIT-OK] target enableLightmap()V matches baseline
        original.call();
        if (Config.isShaders()) {
            Shaders.enableLightmap();
        }
    }

    @WrapMethod(method = "disableLightmap")
    private void optiRefine$disableLightmap(Operation<Void> original) {
// [AUDIT-OK] target disableLightmap()V matches baseline
        original.call();
        if (Config.isShaders()) {
            Shaders.disableLightmap();
        }
    }

    /**
     * OptiFine: {@code CustomColors.updateLightmap} early-out. The Forge
     * {@code getLightmapColors} hook and clamps are inside {@code original} and preserved.
     */
    @WrapMethod(method = "updateLightmap")
    private void optiRefine$updateLightmap(float partialTicks, Operation<Void> original) {
// [AUDIT-OK] target updateLightmap(F)V matches baseline
        if (this.lightmapUpdateNeeded) {
            this.mc.profiler.startSection("lightTex");
            WorldClient world = this.mc.world;
            if (world != null) {
                if (Config.isCustomColors() && CustomColors.updateLightmap(world, this.torchFlickerX, this.lightmapColors, this.mc.player.isPotionActive(MobEffects.NIGHT_VISION), partialTicks)) {
                    this.lightmapTexture.updateDynamicTexture();
                    this.lightmapUpdateNeeded = false;
                    this.mc.profiler.endSection();
                    return;
                }
                original.call(partialTicks);
            }
        }
    }

    // ===== updateCameraAndRender =====

    /**
     * OptiFine: {@code frameInit} at the very start of the frame. The runtime body (Kirino render
     * delegation, Forge hooks) is preserved because this is an edge injection.
     */
    @Inject(method = "updateCameraAndRender", at = @At("HEAD"))
    private void optiRefine$frameInit(CallbackInfo ci) {
// [AUDIT-OK] target updateCameraAndRender(FJ)V (SRG func_181560_a) matches baseline
        this.frameInit();
    }

    /**
     * OptiFine: FPS counter + lagometer overlay right after the game overlay.
     */
    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V", shift = At.Shift.AFTER))
    private void optiRefine$fpsOverlay(CallbackInfo ci) {
// [AUDIT-OK] target updateCameraAndRender(FJ)V renderGameOverlay(F)V inject point matches baseline (line 1090)
        if (GameSettings_ofShowFps_get(this.mc.gameSettings) && !this.mc.gameSettings.showDebugInfo) {
            Config.drawFps();
        }
        if (this.mc.gameSettings.showDebugInfo) {
            Lagometer.showLagometer(new ScaledResolution(this.mc));
        }
    }

    /**
     * OptiFine: {@code frameFinish} + {@code waitForServerThread} + memory/lagometer update +
     * profiler toggle at the very end of the frame.
     */
    @Inject(method = "updateCameraAndRender", at = @At("TAIL"))
    private void optiRefine$frameEnd(CallbackInfo ci) {
// [AUDIT-OK] target updateCameraAndRender(FJ)V matches baseline
        this.frameFinish();
        this.waitForServerThread();
        MemoryMonitor.update();
        Lagometer.updateLagometer();
        if (GameSettings_ofProfiler_get(this.mc.gameSettings)) {
            this.mc.gameSettings.showDebugProfilerChart = true;
        }
    }

    // ===== renderWorld =====

    /**
     * OptiFine: {@code Shaders.beginRender} right after mouse-over, before the render passes.
     */
    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;enableDepth()V"))
    private void optiRefine$beginRender(float partialTicks, long finishTimeNano, CallbackInfo ci) {
// [AUDIT-OK] target renderWorld(FJ)V (SRG func_78471_a) enableDepth()V inject point matches baseline (line 1230)
        if (Config.isShaders()) {
            Shaders.beginRender(this.mc, partialTicks, finishTimeNano);
        }
    }

    // ===== renderWorldPass =====

    /**
     * OptiFine: the shaders-wrapped render loop. Reflector calls are expanded to the direct Forge
     * API (render pass hooks, block highlight, render-last). The Kirino headless phases are a
     * Cleanroom-only feature and are not reproduced here (TODO: dedicated Kirino integration).
     */
    @WrapMethod(method = "renderWorldPass")
    private void optiRefine$renderWorldPass(int pass, float partialTicks, long finishTimeNano, Operation<Void> original) {
// [AUDIT-OK] target renderWorldPass(IFJ)V (SRG func_175068_a) matches baseline
        boolean shaders = Config.isShaders();
        if (shaders) {
            Shaders.beginRenderPass(pass, partialTicks, finishTimeNano);
        }
        RenderGlobal renderglobal = this.mc.renderGlobal;
        ParticleManager particlemanager = this.mc.effectRenderer;
        boolean flag = this.isDrawBlockOutline();
        GlStateManager.enableCull();
        this.mc.profiler.endStartSection("clear");
        if (shaders) {
            Shaders.setViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        } else {
            GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        }
        this.updateFogColor(partialTicks);
        GlStateManager.clear(16640);
        if (shaders) {
            Shaders.clearRenderBuffer();
        }
        this.mc.profiler.endStartSection("camera");
        this.setupCameraTransform(partialTicks, pass);
        if (shaders) {
            Shaders.setCamera(partialTicks);
        }
        ActiveRenderInfo.updateRenderInfo(this.mc.getRenderViewEntity(), this.mc.gameSettings.thirdPersonView == 2);
        this.mc.profiler.endStartSection("frustum");
        ClippingHelper clippinghelper = ClippingHelperImpl.getInstance();
        this.mc.profiler.endStartSection("culling");
        ClippingHelper_disabled_set(clippinghelper, Config.isShaders() && !Shaders.isFrustumCulling());
        Frustum frustum = new Frustum(clippinghelper);
        Entity entity = this.mc.getRenderViewEntity();
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
        if (shaders) {
            ShadersRender.setFrustrumPosition(frustum, d0, d1, d2);
        } else {
            frustum.setPosition(d0, d1, d2);
        }
        if ((Config.isSkyEnabled() || Config.isSunMoonEnabled() || Config.isStarsEnabled()) && !Shaders.isShadowPass) {
            this.setupFog(-1, partialTicks);
            this.mc.profiler.endStartSection("sky");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.clipDistance);
            GlStateManager.matrixMode(5888);
            if (shaders) {
                Shaders.beginSky();
            }
            renderglobal.renderSky(partialTicks, pass);
            if (shaders) {
                Shaders.endSky();
            }
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.clipDistance);
            GlStateManager.matrixMode(5888);
        } else {
            GlStateManager.disableBlend();
        }
        this.setupFog(0, partialTicks);
        GlStateManager.shadeModel(7425);
        if (entity.posY + (double) entity.getEyeHeight() < 128.0D + (double) GameSettings_ofCloudsHeight_get(this.mc.gameSettings) * 128.0D) {
            this.renderCloudsCheck(renderglobal, partialTicks, pass, d0, d1, d2);
        }
        this.mc.profiler.endStartSection("prepareterrain");
        this.setupFog(0, partialTicks);
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        this.mc.profiler.endStartSection("terrain_setup");
        this.checkLoadVisibleChunks(entity, partialTicks, frustum, this.mc.player.isSpectator());
        if (shaders) {
            ShadersRender.setupTerrain(renderglobal, entity, partialTicks, frustum, this.frameCount++, this.mc.player.isSpectator());
        } else {
            renderglobal.setupTerrain(entity, partialTicks, frustum, this.frameCount++, this.mc.player.isSpectator());
        }
        if (pass == 0 || pass == 2) {
            this.mc.profiler.endStartSection("updatechunks");
            Lagometer.timerChunkUpload.start();
            this.mc.renderGlobal.updateChunks(finishTimeNano);
            Lagometer.timerChunkUpload.end();
        }
        this.mc.profiler.endStartSection("terrain");
        Lagometer.timerTerrain.start();
        if (GameSettings_ofSmoothFps_get(this.mc.gameSettings) && pass > 0) {
            this.mc.profiler.endStartSection("finish");
            GL11.glFinish();
            this.mc.profiler.endStartSection("terrain");
        }
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        if (shaders) {
            ShadersRender.beginTerrainSolid();
        }
        renderglobal.renderBlockLayer(BlockRenderLayer.SOLID, partialTicks, pass, entity);
        GlStateManager.enableAlpha();
        if (shaders) {
            ShadersRender.beginTerrainCutoutMipped();
        }
        this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, this.mc.gameSettings.mipmapLevels > 0);
        renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, partialTicks, pass, entity);
        this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        if (shaders) {
            ShadersRender.beginTerrainCutout();
        }
        renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT, partialTicks, pass, entity);
        this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        if (shaders) {
            ShadersRender.endTerrain();
        }
        Lagometer.timerTerrain.end();
        GlStateManager.shadeModel(7424);
        GlStateManager.alphaFunc(516, 0.1F);
        if (!this.debugView) {
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            this.mc.profiler.endStartSection("entities");
            ForgeHooksClient.setRenderPass(0);
            renderglobal.renderEntities(entity, frustum, partialTicks);
            ForgeHooksClient.setRenderPass(-1);
            RenderHelper.disableStandardItemLighting();
            this.disableLightmap();
        }
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        if (flag && this.mc.objectMouseOver != null && !entity.isInsideOfMaterial(net.minecraft.block.material.Material.WATER)) {
            EntityPlayer entityplayer = (EntityPlayer) entity;
            GlStateManager.disableAlpha();
            this.mc.profiler.endStartSection("outline");
            if (!ForgeHooksClient.onDrawBlockHighlight(renderglobal, entityplayer, this.mc.objectMouseOver, 0, partialTicks)) {
                renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, partialTicks);
            }
            GlStateManager.enableAlpha();
        }
        if (this.mc.debugRenderer.shouldRender()) {
            boolean fogEnabled = GlStateManager_isFogEnabled();
            GlStateManager.disableFog();
            this.mc.debugRenderer.renderDebug(partialTicks, finishTimeNano);
            GlStateManager_setFogEnabled(fogEnabled);
        }
        if (!RenderGlobal_damagedBlocks_get(renderglobal).isEmpty()) {
            this.mc.profiler.endStartSection("destroyProgress");
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
            renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), entity, partialTicks);
            this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
            GlStateManager.disableBlend();
        }
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableBlend();
        if (!this.debugView) {
            this.enableLightmap();
            this.mc.profiler.endStartSection("litParticles");
            if (shaders) {
                Shaders.beginLitParticles();
            }
            particlemanager.renderLitParticles(entity, partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, partialTicks);
            this.mc.profiler.endStartSection("particles");
            if (shaders) {
                Shaders.beginParticles();
            }
            particlemanager.renderParticles(entity, partialTicks);
            if (shaders) {
                Shaders.endParticles();
            }
            this.disableLightmap();
        }
        GlStateManager.depthMask(false);
        if (Config.isShaders()) {
            GlStateManager.depthMask(Shaders.isRainDepth());
        }
        GlStateManager.enableCull();
        this.mc.profiler.endStartSection("weather");
        if (shaders) {
            Shaders.beginWeather();
        }
        this.renderRainSnow(partialTicks);
        if (shaders) {
            Shaders.endWeather();
        }
        GlStateManager.depthMask(true);
        renderglobal.renderWorldBorder(entity, partialTicks);
        if (shaders) {
            ShadersRender.renderHand0((net.minecraft.client.renderer.EntityRenderer)(Object) this, partialTicks, pass);
            Shaders.preWater();
        }
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(516, 0.1F);
        this.setupFog(0, partialTicks);
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.shadeModel(7425);
        this.mc.profiler.endStartSection("translucent");
        if (shaders) {
            Shaders.beginWater();
        }
        renderglobal.renderBlockLayer(BlockRenderLayer.TRANSLUCENT, partialTicks, pass, entity);
        if (shaders) {
            Shaders.endWater();
        }
        if (!this.debugView) {
            RenderHelper.enableStandardItemLighting();
            this.mc.profiler.endStartSection("entities");
            ForgeHooksClient.setRenderPass(1);
            renderglobal.renderEntities(entity, frustum, partialTicks);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            ForgeHooksClient.setRenderPass(-1);
            RenderHelper.disableStandardItemLighting();
        }
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableFog();
        if (entity.posY + (double) entity.getEyeHeight() >= 128.0D + (double) GameSettings_ofCloudsHeight_get(this.mc.gameSettings) * 128.0D) {
            this.mc.profiler.endStartSection("aboveClouds");
            this.renderCloudsCheck(renderglobal, partialTicks, pass, d0, d1, d2);
        }
        this.mc.profiler.endStartSection("forge_render_last");
        ForgeHooksClient.dispatchRenderLast(renderglobal, partialTicks);
        this.mc.profiler.endStartSection("hand");
        if (this.renderHand && !Shaders.isShadowPass) {
            if (shaders) {
                ShadersRender.renderHand1((net.minecraft.client.renderer.EntityRenderer)(Object) this, partialTicks, pass);
                Shaders.renderCompositeFinal();
            }
            GlStateManager.clear(256);
            if (shaders) {
                ShadersRender.renderFPOverlay((net.minecraft.client.renderer.EntityRenderer)(Object) this, partialTicks, pass);
            } else {
                this.renderHand(partialTicks, pass);
            }
        }
        if (shaders) {
            Shaders.endRender();
        }
    }

    // ===== renderCloudsCheck =====

    /**
     * OptiFine: {@code !Config.isCloudsOff() && Shaders.shouldRenderClouds(...)} condition.
     */
    @Redirect(method = "renderCloudsCheck", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/GameSettings;shouldRenderClouds()I"))
    private int optiRefine$shouldRenderClouds(net.minecraft.client.settings.GameSettings settings) {
// [AUDIT-OK] target renderCloudsCheck(LRenderGlobal;FIDDD)V shouldRenderClouds()I matches baseline
        if (Config.isCloudsOff()) {
            return 0;
        }
        return Shaders.shouldRenderClouds(settings) ? 1 : 0;
    }

    @Redirect(method = "renderCloudsCheck", at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V", ordinal = 0))
    private void optiRefine$cloudsPerspective0(float fovY, float aspect, float zNear, float zFar) {
// [AUDIT-OK] target renderCloudsCheck gluPerspective ordinal 0 matches baseline
        Project.gluPerspective(fovY, aspect, zNear, this.clipDistance * 4.0F);
    }

    @Redirect(method = "renderCloudsCheck", at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V", ordinal = 1))
    private void optiRefine$cloudsPerspective1(float fovY, float aspect, float zNear, float zFar) {
// [AUDIT-OK] target renderCloudsCheck gluPerspective ordinal 1 matches baseline
        Project.gluPerspective(fovY, aspect, zNear, this.clipDistance);
    }

    // ===== updateFogColor =====

    /**
     * OptiFine: {@code CustomColors.getWorldSkyColor} applied to the sky color.
     */
    @Redirect(method = "updateFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getSkyColor(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d optiRefine$getWorldSkyColor(World world, Entity entity, float partialTicks) {
// [AUDIT-OK] target updateFogColor(F)V World.getSkyColor matches baseline
        Vec3d sky = world.getSkyColor(entity, partialTicks);
        return CustomColors.getWorldSkyColor(sky, world, entity, partialTicks);
    }

    /**
     * OptiFine: {@code CustomColors.getWorldFogColor} applied to the fog color.
     */
    @Redirect(method = "updateFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getFogColor(F)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d optiRefine$getWorldFogColor(World world, float partialTicks) {
// [AUDIT-OK] target updateFogColor(F)V World.getFogColor matches baseline
        Vec3d fog = world.getFogColor(partialTicks);
        return CustomColors.getWorldFogColor(fog, world, this.mc.getRenderViewEntity(), partialTicks);
    }

    /**
     * OptiFine: {@code CustomColors.getUnderwaterColor}/{@code getUnderlavaColor} right after the
     * block fog color chain, before the {@code fogColor2} blending.
     */
    @Inject(method = "updateFogColor", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;fogColor2:F", opcode = Opcodes.GETFIELD, ordinal = 0))
    private void optiRefine$customFogColors(float partialTicks, CallbackInfo ci) {
// [AUDIT-OK] target updateFogColor(F)V GETFIELD fogColor2 (SRG field_78535_ad) matches baseline (line 1777)
        Entity entity = this.mc.getRenderViewEntity();
        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
        if (iblockstate.getMaterial() == net.minecraft.block.material.Material.WATER) {
            Vec3d underwater = CustomColors.getUnderwaterColor(this.mc.world, entity.posX, entity.posY + 1.0D, entity.posZ);
            if (underwater != null) {
                this.acc$fogColorRed = (float) underwater.x;
                this.acc$fogColorGreen = (float) underwater.y;
                this.acc$fogColorBlue = (float) underwater.z;
            }
        } else if (iblockstate.getMaterial() == net.minecraft.block.material.Material.LAVA) {
            Vec3d underlava = CustomColors.getUnderlavaColor(this.mc.world, entity.posX, entity.posY + 1.0D, entity.posZ);
            if (underlava != null) {
                this.acc$fogColorRed = (float) underlava.x;
                this.acc$fogColorGreen = (float) underlava.y;
                this.acc$fogColorBlue = (float) underlava.z;
            }
        }
    }

    /**
     * OptiFine: {@code Shaders.setClearColor} instead of {@code GlStateManager.clearColor}.
     */
    @Redirect(method = "updateFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;clearColor(FFFF)V"))
    private void optiRefine$setClearColor(float red, float green, float blue, float alpha) {
// [AUDIT-OK] target updateFogColor(F)V GlStateManager.clearColor matches baseline
        Shaders.setClearColor(red, green, blue, alpha);
    }

    // ===== setupFog =====

    /**
     * OptiFine: fog setup with {@code fogStandard} flag, clear-water density, {@code Config.getFogStart}
     * factor and NV_fog_distance modes. The Forge {@code getFogDensity}/{@code onFogRender} hooks
     * (already in the runtime) are kept as direct calls.
     */
    @WrapMethod(method = "setupFog")
    private void optiRefine$setupFog(int fogMode, float partialTicks, Operation<Void> original) {
// [AUDIT-OK] target setupFog(IF)V matches baseline; Forge getFogDensity/onFogRender hooks kept
        this.fogStandard = false;
        Entity entity = this.mc.getRenderViewEntity();
        this.setupFogColor(false);
        GlStateManager.glNormal3f(0.0F, -1.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
        float fogDensityHook = ForgeHooksClient.getFogDensity((net.minecraft.client.renderer.EntityRenderer)(Object) this, entity, iblockstate, partialTicks, 0.1F);
        if (fogDensityHook >= 0.0F) {
            GlStateManager.setFogDensity(fogDensityHook);
        } else if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(MobEffects.BLINDNESS)) {
            float f1 = 5.0F;
            int i = ((EntityLivingBase) entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
            if (i < 20) {
                f1 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float) i / 20.0F);
            }
            GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
            if (fogMode == -1) {
                GlStateManager.setFogStart(0.0F);
                GlStateManager.setFogEnd(f1 * 0.8F);
            } else {
                GlStateManager.setFogStart(f1 * 0.25F);
                GlStateManager.setFogEnd(f1);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy()) {
                GlStateManager.glFogi(34138, 34139);
            }
        } else if (this.cloudFog) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity(0.1F);
        } else if (iblockstate.getMaterial() == net.minecraft.block.material.Material.WATER) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            float f2 = Config.isClearWater() ? 0.02F : 0.1F;
            if (entity instanceof EntityLivingBase) {
                if (((EntityLivingBase) entity).isPotionActive(MobEffects.WATER_BREATHING)) {
                    GlStateManager.setFogDensity(0.01F);
                } else {
                    float f3 = 0.1F - EnchantmentHelper.getRespirationModifier((EntityLivingBase) entity) * 0.03F;
                    GlStateManager.setFogDensity(Config.limit(f3, 0.0F, f2));
                }
            } else {
                GlStateManager.setFogDensity(f2);
            }
        } else if (iblockstate.getMaterial() == net.minecraft.block.material.Material.LAVA) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity(2.0F);
        } else {
            float f4 = this.farPlaneDistance;
            this.fogStandard = true;
            GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
            if (fogMode == -1) {
                GlStateManager.setFogStart(0.0F);
                GlStateManager.setFogEnd(f4);
            } else {
                GlStateManager.setFogStart(f4 * Config.getFogStart());
                GlStateManager.setFogEnd(f4);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                if (Config.isFogFancy()) {
                    GlStateManager.glFogi(34138, 34139);
                }
                if (Config.isFogFast()) {
                    GlStateManager.glFogi(34138, 34140);
                }
            }
            if (this.mc.world.provider.doesXZShowFog((int) entity.posX, (int) entity.posZ) || this.mc.ingameGUI.getBossOverlay().shouldCreateFog()) {
                GlStateManager.setFogStart(f4 * 0.05F);
                GlStateManager.setFogEnd(f4);
            }
            ForgeHooksClient.onFogRender((net.minecraft.client.renderer.EntityRenderer)(Object) this, entity, iblockstate, partialTicks, fogMode, f4);
        }
        GlStateManager.enableColorMaterial();
        GlStateManager.enableFog();
        GlStateManager.colorMaterial(1028, 4608);
    }

    // ===== setFogColorBuffer =====

    /**
     * OptiFine: notify shaders of the fog color.
     */
    @WrapMethod(method = "setFogColorBuffer")
    private FloatBuffer optiRefine$setFogColorBuffer(float red, float green, float blue, float alpha, Operation<FloatBuffer> original) {
// [AUDIT-OK] target setFogColorBuffer(FFFF)Ljava/nio/FloatBuffer; (SRG func_78469_a) matches baseline
        if (Config.isShaders()) {
            Shaders.setFogColor(red, green, blue);
        }
        return original.call(red, green, blue, alpha);
    }

    // ===== new methods (OptiFine) =====

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    public boolean setFxaaShader(int level) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return false;
        } else if (this.shaderGroup != null && this.shaderGroup != this.fxaaShaders[2] && this.shaderGroup != this.fxaaShaders[4]) {
            return true;
        } else if (level != 2 && level != 4) {
            if (this.shaderGroup == null) {
                return true;
            } else {
                this.shaderGroup.deleteShaderGroup();
                this.shaderGroup = null;
                return true;
            }
        } else if (this.shaderGroup != null && this.shaderGroup == this.fxaaShaders[level]) {
            return true;
        } else if (this.mc.world == null) {
            return true;
        } else {
            this.loadShader(new ResourceLocation("shaders/post/fxaa_of_" + level + "x.json"));
            this.fxaaShaders[level] = this.shaderGroup;
            return this.useShader;
        }
    }

    /**
     * OptiFine first-frame init. The new-version/64-bit chat notifications and the GuiChatOF swap
     * are cosmetic and left as TODO.
     */
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    private void frameInit() {
        GlErrors.frameStart();
        if (!this.initialized) {
            ReflectorResolver.resolve();
            TextureUtils.registerResourceListener();
            if (Config.getBitsOs() == 64 && Config.getBitsJre() == 32) {
                Config.setNotify64BitJava(true);
            }
            this.initialized = true;
        }
        Config.checkDisplayMode();
        WorldClient world = this.mc.world;
        if (world != null) {
            if (Config.isNotify64BitJava()) {
                Config.setNotify64BitJava(false);
            }
        }
        if (this.updatedWorld != world) {
            RandomEntities.worldChanged(this.updatedWorld, world);
            Config.updateThreadPriorities();
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
            this.updatedWorld = world;
        }
        if (!this.setFxaaShader(Shaders.configAntialiasingLevel)) {
            Shaders.configAntialiasingLevel = 0;
        }
    }

    /**
     * OptiFine end-of-frame GL error report.
     */
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    private void frameFinish() {
        if (this.mc.world != null && Config.isShowGlErrors() && TimedEvent.isActive("CheckGlErrorFrameFinish", 10000L)) {
            int error = GlStateManager.glGetError();
            if (error != 0 && GlErrors.isEnabled(error)) {
                String message = Config.getGlErrorString(error);
                TextComponentString text = new TextComponentString(I18n.format("of.message.openglError", error, message));
                this.mc.ingameGUI.getChatGUI().printChatMessage(text);
            }
        }
    }

    /**
     * OptiFine smooth-world server thread synchronization.
     */
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    private void waitForServerThread() {
        this.serverWaitTimeCurrent = 0;
        if (!Config.isSmoothWorld() || !Config.isSingleProcessor()) {
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
        } else if (this.mc.isIntegratedServerRunning()) {
            IntegratedServer server = this.mc.getIntegratedServer();
            if (server != null) {
                boolean paused = this.mc.isGamePaused();
                if (!paused && !(this.mc.currentScreen instanceof net.minecraft.client.gui.GuiDownloadTerrain)) {
                    if (this.serverWaitTime > 0) {
                        Lagometer.timerServer.start();
                        Config.sleep(this.serverWaitTime);
                        Lagometer.timerServer.end();
                        this.serverWaitTimeCurrent = this.serverWaitTime;
                    }
                    long time = System.nanoTime() / 1000000L;
                    if (this.lastServerTime != 0L && this.lastServerTicks != 0) {
                        long diff = time - this.lastServerTime;
                        if (diff < 0L) {
                            this.lastServerTime = time;
                            diff = 0L;
                        }
                        if (diff >= 50L) {
                            this.lastServerTime = time;
                            int ticks = server.getTickCounter();
                            int tickDiff = ticks - this.lastServerTicks;
                            if (tickDiff < 0) {
                                this.lastServerTicks = ticks;
                                tickDiff = 0;
                            }
                            if (tickDiff < 1 && this.serverWaitTime < 100) {
                                this.serverWaitTime += 2;
                            }
                            if (tickDiff > 1 && this.serverWaitTime > 0) {
                                this.serverWaitTime--;
                            }
                            this.lastServerTicks = ticks;
                        }
                    } else {
                        this.lastServerTime = time;
                        this.lastServerTicks = server.getTickCounter();
                        this.avgServerTickDiff = 1.0F;
                        this.avgServerTimeDiff = 50.0F;
                    }
                } else {
                    if (this.mc.currentScreen instanceof net.minecraft.client.gui.GuiDownloadTerrain) {
                        Config.sleep(20L);
                    }
                    this.lastServerTime = 0L;
                    this.lastServerTicks = 0;
                }
            }
        }
    }

    /**
     * OptiFine F3+F4 visible-chunks loader. The {@code Minecraft.actionKeyF3} toggle is left as
     * TODO (private field access).
     */
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    private void checkLoadVisibleChunks(Entity entity, float partialTicks, ICamera camera, boolean spectator) {
        int messageId = 201435902;
        if (this.loadVisibleChunks) {
            this.loadVisibleChunks = false;
            this.loadAllVisibleChunks(entity, partialTicks, camera, spectator);
            this.mc.ingameGUI.getChatGUI().deleteChatLine(messageId);
        }
        if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(38)) {
            if (this.mc.gameSettings.keyBindAdvancements.getKeyCode() == 38) {
                if (this.mc.currentScreen instanceof GuiScreenAdvancements) {
                    this.mc.displayGuiScreen(null);
                }
                while (Keyboard.next()) {
                }
            }
            if (this.mc.currentScreen != null) {
                return;
            }
            this.loadVisibleChunks = true;
            TextComponentString message = new TextComponentString(I18n.format("of.message.loadingVisibleChunks"));
            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(message, messageId);
        }
    }

    /**
     * OptiFine: force-load all visible chunks. The {@code RenderChunk.renderChunksUpdated} reset is
     * left as TODO (not provided by any OptiRefine mixin).
     */
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    private void loadAllVisibleChunks(Entity entity, double partialTicks, ICamera camera, boolean spectator) {
        int chunkUpdates = GameSettings_ofChunkUpdates_get(this.mc.gameSettings);
        boolean lazyChunkLoading = GameSettings_ofLazyChunkLoading_get(this.mc.gameSettings);
        try {
            GameSettings_ofChunkUpdates_set(this.mc.gameSettings, 1000);
            GameSettings_ofLazyChunkLoading_set(this.mc.gameSettings, false);
            RenderGlobal renderglobal = Config.getRenderGlobal();
            int loadedChunks = RenderGlobal_getCountLoadedChunks(renderglobal);
            long time = System.currentTimeMillis();
            Config.dbg("Loading visible chunks");
            long timeLimit = System.currentTimeMillis() + 5000L;
            int updated = 0;
            boolean loading;
            do {
                loading = false;
                for (int i = 0; i < 100; i++) {
                    RenderGlobal_displayListEntitiesDirty_set(renderglobal, true);
                    renderglobal.setupTerrain(entity, partialTicks, camera, this.frameCount++, spectator);
                    if (!RenderGlobal_hasNoChunkUpdates(renderglobal)) {
                        loading = true;
                    }
                    updated += RenderGlobal_getCountChunksToUpdate(renderglobal);
                    while (!RenderGlobal_hasNoChunkUpdates(renderglobal)) {
                        renderglobal.updateChunks(System.nanoTime() + 1000000000L);
                    }
                    updated -= RenderGlobal_getCountChunksToUpdate(renderglobal);
                    if (!loading) {
                        break;
                    }
                }
                if (RenderGlobal_getCountLoadedChunks(renderglobal) != loadedChunks) {
                    loading = true;
                    loadedChunks = RenderGlobal_getCountLoadedChunks(renderglobal);
                }
                if (System.currentTimeMillis() > timeLimit) {
                    Config.log("Chunks loaded: " + updated);
                    timeLimit = System.currentTimeMillis() + 5000L;
                }
            } while (loading);
            Config.log("Chunks loaded: " + updated);
            Config.log("Finished loading visible chunks");
        } finally {
            GameSettings_ofChunkUpdates_set(this.mc.gameSettings, chunkUpdates);
            GameSettings_ofLazyChunkLoading_set(this.mc.gameSettings, lazyChunkLoading);
        }
    }
}
