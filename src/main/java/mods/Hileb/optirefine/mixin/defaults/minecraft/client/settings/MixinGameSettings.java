package mods.Hileb.optirefine.mixin.defaults.minecraft.client.settings;


import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.core.OptiRefineLog;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import mods.Hileb.optirefine.optifine.client.GameSettingsOptionOF;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.optifine.*;
import net.optifine.shaders.Shaders;
import net.optifine.util.KeyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
@Mixin(GameSettings.class)
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 4
public abstract class MixinGameSettings {

    // [AUDIT-OK] OF-added fields (ofFogType..ofAnimatedTextures; 80+), not in baseline
    @Unique
    public int ofFogType = 1;
    @Unique
    public float ofFogStart = 0.8F;
    @Unique
    public int ofMipmapType = 0;
    @Unique
    public boolean ofOcclusionFancy = false;
    @Unique
    public boolean ofSmoothFps = false;
    @Unique
    public boolean ofSmoothWorld = Config.isSingleProcessor();
    @Unique
    public boolean ofLazyChunkLoading = Config.isSingleProcessor();
    @Unique
    public boolean ofRenderRegions = false;
    @Unique
    public boolean ofSmartAnimations = false;
    @Unique
    public float ofAoLevel = 1.0F;
    @Unique
    public int ofAaLevel = 0;
    @Unique
    public int ofAfLevel = 1;
    @Unique
    public int ofClouds = 0;
    @Unique
    public float ofCloudsHeight = 0.0F;
    @Unique
    public int ofTrees = 0;
    @Unique
    public int ofRain = 0;
    @Unique
    public int ofDroppedItems = 0;
    @Unique
    public int ofBetterGrass = 3;
    @Unique
    public int ofAutoSaveTicks = 4000;
    @Unique
    public boolean ofLagometer = false;
    @Unique
    public boolean ofProfiler = false;
    @Unique
    public boolean ofShowFps = false;
    @Unique
    public boolean ofWeather = true;
    @Unique
    public boolean ofSky = true;
    @Unique
    public boolean ofStars = true;
    @Unique
    public boolean ofSunMoon = true;
    @Unique
    public int ofVignette = 0;
    @Unique
    public int ofChunkUpdates = 1;
    @Unique
    public boolean ofChunkUpdatesDynamic = false;
    @Unique
    public int ofTime = 0;
    @Unique
    public boolean ofClearWater = false;
    @Unique
    public boolean ofBetterSnow = false;
    @Unique
    public String ofFullscreenMode = "Default";
    @Unique
    public boolean ofSwampColors = true;
    @Unique
    public boolean ofRandomEntities = true;
    @Unique
    public boolean ofSmoothBiomes = true;
    @Unique
    public boolean ofCustomFonts = true;
    @Unique
    public boolean ofCustomColors = true;
    @Unique
    public boolean ofCustomSky = true;
    @Unique
    public boolean ofShowCapes = true;
    @Unique
    public int ofConnectedTextures = 2;
    @Unique
    public boolean ofCustomItems = true;
    @Unique
    public boolean ofNaturalTextures = false;
    @Unique
    public boolean ofEmissiveTextures = true;
    @Unique
    public boolean ofFastMath = false;
    @Unique
    public boolean ofFastRender = false;
    @Unique
    public int ofTranslucentBlocks = 0;
    @Unique
    public boolean ofDynamicFov = true;
    @Unique
    public boolean ofAlternateBlocks = true;
    @Unique
    public int ofDynamicLights = 3;
    @Unique
    public boolean ofCustomEntityModels = true;
    @Unique
    public boolean ofCustomGuis = true;
    @Unique
    public boolean ofShowGlErrors = true;
    @Unique
    public int ofScreenshotSize = 1;
    @Unique
    public int ofAnimatedWater = 0;
    @Unique
    public int ofAnimatedLava = 0;
    @Unique
    public boolean ofAnimatedFire = true;
    @Unique
    public boolean ofAnimatedPortal = true;
    @Unique
    public boolean ofAnimatedRedstone = true;
    @Unique
    public boolean ofAnimatedExplosion = true;
    @Unique
    public boolean ofAnimatedFlame = true;
    @Unique
    public boolean ofAnimatedSmoke = true;
    @Unique
    public boolean ofVoidParticles = true;
    @Unique
    public boolean ofWaterParticles = true;
    @Unique
    public boolean ofRainSplash = true;
    @Unique
    public boolean ofPortalParticles = true;
    @Unique
    public boolean ofPotionParticles = true;
    @Unique
    public boolean ofFireworkParticles = true;
    @Unique
    public boolean ofDrippingWaterLava = true;
    @Unique
    public boolean ofAnimatedTerrain = true;
    @Unique
    public boolean ofAnimatedTextures = true;
    @Public
    // [AUDIT-OK] OF-added static constants DEFAULT/FAST/FANCY/OFF/SMART (private static + @Public per mixin rule)
    private static final int DEFAULT = 0;
    @Public
    private static final int FAST = 1;
    @Public
    private static final int FANCY = 2;
    @Public
    private static final int OFF = 3;
    @Public
    private static final int SMART = 4;
    @Public
    // [AUDIT-OK] OF-added static constants ANIM_ON/ANIM_GENERATED/ANIM_OFF (private static + @Public)
    private static final int ANIM_ON = 0;
    @Public
    private static final int ANIM_GENERATED = 1;
    @Public
    private static final int ANIM_OFF = 2;
    @Public
    // [AUDIT-OK] OF-added static constant DEFAULT_STR (private static + @Public)
    private static final String DEFAULT_STR = "Default";
    // [AUDIT-OK] OF-added static arrays OF_TREES_VALUES/OF_DYNAMIC_LIGHTS/KEYS_DYNAMIC_LIGHTS (matches OF values)
    private static final int[] OF_TREES_VALUES = new int[]{0, 1, 4, 2};
    @Unique
    private static final int[] OF_DYNAMIC_LIGHTS = new int[]{3, 1, 2};
    private static final String[] KEYS_DYNAMIC_LIGHTS = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
    @Unique
    // [AUDIT-OK] OF-added fields ofKeyBindZoom/optionsFileOF (@Unique), not in baseline
    public KeyBinding ofKeyBindZoom;
    private File optionsFileOF;

    @Shadow
    // [AUDIT-OK] baseline member limitFramerate exists in GameSettings
    public int limitFramerate;
    @Shadow
    // [AUDIT-OK] baseline member keyBindings exists in GameSettings
    public KeyBinding[] keyBindings;
    @Shadow
    // [AUDIT-OK] baseline member renderDistanceChunks exists in GameSettings
    public int renderDistanceChunks;

    @Shadow
    // [AUDIT-OK] baseline member guiScale exists in GameSettings
    public int guiScale;

    @Shadow
    // [AUDIT-OK] baseline member mc exists in GameSettings
    protected Minecraft mc;

    @AccessibleOperation
    // [AUDIT-OK] NOP cast helper (call removed, arg stays)
    private static native GameSettings cast_GameSettings(Object o);

    @ModifyConstant(method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V", constant = @Constant(floatValue = 32.0f))
    // [AUDIT-OK] target <init>(Minecraft,File)V; 32.0F unique in ctor; constant->48/64 folds OF memory-based setValueMax upgrades
    public float make_RENDER_DISTANCE(float constant){
        long var3 = 1000000L;
        if (Runtime.getRuntime().maxMemory() >= 1500L * var3) {
            return 48.0F;
        }
        if (Runtime.getRuntime().maxMemory() >= 2500L * var3) {
            return 64.0F;
        }
        return constant;
    }

    @Inject(method = "setOptionValue", at = @At("HEAD"))
    // [AUDIT-OK] target setOptionValue(Options,I)V matches baseline; HEAD inject calls OF setOptionValueOF first like OF
    public void fosetOptionValue(GameSettings.Options settingsOption, int value, CallbackInfo ci) {
        this.optiRefine$setOptionValueOF(settingsOption, value);
    }

    @Definition(id = "guiScale", field = "Lnet/minecraft/client/settings/GameSettings;guiScale:I")
    @Expression("this.guiScale = @(?)")
    @ModifyExpressionValue(method = "setOptionValue", at = @At("MIXINEXTRAS:EXPRESSION"))
    // [AUDIT-OK] target setOptionValue; guiScale expression rewrite replicates OF GUI_SCALE block
    public int addGuiScaleByConfig(int original, @Local(argsOnly = true) int value) {
        this.guiScale += value;
        if (GuiScreen.isShiftKeyDown()) {
            this.guiScale = 0;
        }

        DisplayMode mode = Config.getLargestDisplayMode();
        int maxScaleWidth = mode.getWidth() / 320;
        int maxScaleHeight = mode.getHeight() / 240;
        int maxGuiScale = Math.min(maxScaleWidth, maxScaleHeight);
        if (this.guiScale < 0) {
            this.guiScale = maxGuiScale - 1;
        }

        if (this.mc.isUnicode() && this.guiScale % 2 != 0) {
            this.guiScale += value;
        }

        if (this.guiScale < 0 || this.guiScale >= maxGuiScale) {
            this.guiScale = 0;
        }
        return this.guiScale;
    }

    @Shadow
    // [AUDIT-OK] baseline member anaglyph exists in GameSettings
    public boolean anaglyph;

    @WrapOperation(method = "setOptionValue", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/FMLClientHandler;refreshResources([Lnet/minecraftforge/client/resource/IResourceType;)V"))
    // [AUDIT-OK] target setOptionValue; anaglyph+shaders guard matches OF (toggle already applied by vanilla body before wrap)
    public void notReloadForShader(FMLClientHandler instance, IResourceType[] inclusion, Operation<Void> original) {
        if (this.anaglyph && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.an.shaders1"), Lang.get("of.message.an.shaders2"));
            this.anaglyph = false;
        } else {
            original.call(instance, inclusion);
        }
    }

    @Inject(method = "setOptionValue", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;fancyGraphics:Z", shift = At.Shift.AFTER))
    // [AUDIT-FIXED] FIELD inject shift AFTER: runs after `fancyGraphics = !fancyGraphics` -> updateRenderClouds sees the toggled value (OF semantics)
    public void hookForupdateRenderClouds(GameSettings.Options settingsOption, int value, CallbackInfo ci) {
        this.optiRefine$updateRenderClouds();
    }

    @WrapMethod(method = "loadOptions")
    // [AUDIT-ISSUE] OF ctor forces renderDistanceChunks=8 BEFORE loadOptions; mixin omits it -> fresh installs default 12 (64-bit) instead of 8. Also optionsFileOF = Launch.minecraftHome vs OF new File(optionsFile.parent, "optionsof.txt") (same dir normally; needs-verification)
    public void hookForoadOfOptions(Operation<Void> original) {
        boolean init = this.optionsFileOF == null;
        if (init) {
            this.optionsFileOF = new File(Launch.minecraftHome, "optionsof.txt");
            this.limitFramerate = (int) GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
            this.ofKeyBindZoom = new KeyBinding("of.key.zoom", 46, "key.categories.misc");
            this.keyBindings = ArrayUtils.add(this.keyBindings, this.ofKeyBindZoom);
        }
        original.call();
        this.loadOfOptions();
        if (init) {
            Config.initGameSettings(cast_GameSettings(this));
        }
    }

    @WrapOperation(method = "saveOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/GameSettings;sendSettingsToServer()V"))
    // [AUDIT-OK] target saveOptions()V; saveOfOptions before sendSettingsToServer matches OF order
    public void hookFor_saveOfOptions(GameSettings instance, Operation<Void> original) {
        this.saveOfOptions();
        original.call(instance);
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.RenderGlobal resetClouds ()V")
    // [AUDIT-OK] OF member RenderGlobal.resetClouds()V (OF RenderGlobal:2998), MCP name (not in tsrg)
    private static native void RenderGlobal_resetClouds(RenderGlobal renderGlobal);

    @Unique
    // [AUDIT-ISSUE] DEAD CODE: defined but never invoked - no WrapMethod/Inject on setOptionFloatValue/getOptionFloatValue -> OF float options (CLOUD_HEIGHT/AO_LEVEL/AA_LEVEL/AF_LEVEL/MIPMAP_TYPE/FULLSCREEN_MODE) not dispatched (sliders no-op, show 0.0F). Fix: WrapMethod both vanilla methods calling OF-* helper first (mirror OF setOptionFloatValue/getOptionFloatValue)
    private void optiRefine$setOptionFloatValueOF(GameSettings.Options option, float val) {
        if (option == GameSettingsOptionOF.CLOUD_HEIGHT) {
            this.ofCloudsHeight = val;
            RenderGlobal_resetClouds(this.mc.renderGlobal);
        }

        if (option == GameSettingsOptionOF.AO_LEVEL) {
            this.ofAoLevel = val;
            this.mc.renderGlobal.loadRenderers();
        }

        if (option == GameSettingsOptionOF.AA_LEVEL) {
            int valInt = (int) val;
            if (valInt > 0 && Config.isShaders()) {
                Config.showGuiMessage(Lang.get("of.message.aa.shaders1"), Lang.get("of.message.aa.shaders2"));
                return;
            }

            int[] aaLevels = new int[]{0, 2, 4, 6, 8, 12, 16};
            this.ofAaLevel = 0;

            for (int l = 0; l < aaLevels.length; l++) {
                if (valInt >= aaLevels[l]) {
                    this.ofAaLevel = aaLevels[l];
                }
            }

            this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
        }

        if (option == GameSettingsOptionOF.AF_LEVEL) {
            int valInt = (int) val;
            if (valInt > 1 && Config.isShaders()) {
                Config.showGuiMessage(Lang.get("of.message.af.shaders1"), Lang.get("of.message.af.shaders2"));
                return;
            }

            this.ofAfLevel = 1;

            while (this.ofAfLevel * 2 <= valInt) {
                this.ofAfLevel *= 2;
            }

            this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
            FMLClientHandler.instance().scheduleResourcesRefresh((o)->true); // TODO : figure out what types is needed
        }

        if (option == GameSettingsOptionOF.MIPMAP_TYPE) {
            int valInt = (int) val;
            this.ofMipmapType = Config.limit(valInt, 0, 3);
            FMLClientHandler.instance().scheduleResourcesRefresh(VanillaResourceType.TEXTURES);
        }

        if (option == GameSettingsOptionOF.FULLSCREEN_MODE) {
            int index = (int) val - 1;
            String[] modeNames = Config.getDisplayModeNames();
            if (index < 0 || index >= modeNames.length) {
                this.ofFullscreenMode = "Default";
                return;
            }

            this.ofFullscreenMode = modeNames[index];
        }
    }

    @Shadow
    // [AUDIT-OK] baseline member enableVsync exists in GameSettings
    public boolean enableVsync;

    // [AUDIT-ISSUE] DEAD CODE: same as setOptionFloatValueOF - never called; getOptionFloatValue never dispatches to OF branch. Fix: WrapMethod getOptionFloatValue (return OF value if != Float.MAX_VALUE, else original)
    private float optiRefine$getOptionFloatValueOF(GameSettings.Options settingOption) {
        if (settingOption == GameSettingsOptionOF.CLOUD_HEIGHT) {
            return this.ofCloudsHeight;
        } else if (settingOption == GameSettingsOptionOF.AO_LEVEL) {
            return this.ofAoLevel;
        } else if (settingOption == GameSettingsOptionOF.AA_LEVEL) {
            return this.ofAaLevel;
        } else if (settingOption == GameSettingsOptionOF.AF_LEVEL) {
            return this.ofAfLevel;
        } else if (settingOption == GameSettingsOptionOF.MIPMAP_TYPE) {
            return this.ofMipmapType;
        } else if (settingOption == GameSettings.Options.FRAMERATE_LIMIT) {
            return this.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() && this.enableVsync ? 0.0F : this.limitFramerate;
        } else if (settingOption == GameSettingsOptionOF.FULLSCREEN_MODE) {
            if (this.ofFullscreenMode.equals("Default")) {
                return 0.0F;
            } else {
                var modeList = Arrays.asList(Config.getDisplayModeNames());
                int index = modeList.indexOf(this.ofFullscreenMode);
                return index < 0 ? 0.0F : index + 1;
            }
        } else {
            return Float.MAX_VALUE;
        }
    }


    @AccessibleOperation(opcode = Opcodes.PUTSTATIC, desc = "net.minecraft.util.math.MathHelper fastMath Z")
    // [AUDIT-OK] OF field MathHelper.fastMath Z (OF MathHelper:19), MCP name (not in tsrg)
    private static native void MathHelper_fastMath_set(boolean b);


    @Unique
    // [AUDIT-OK] body mirrors OF setOptionValueOF; all GameSettingsOptionOF.* constants verified present in GameSettingsOptionOF.java (72/72, no missing/mismatched)
    private void optiRefine$setOptionValueOF(GameSettings.Options par1EnumOptions, int par2) {
        if (par1EnumOptions == GameSettingsOptionOF.FOG_FANCY) {
            switch (this.ofFogType) {
                case 1:
                    this.ofFogType = 2;
                    if (!Config.isFancyFogAvailable()) {
                        this.ofFogType = 3;
                    }
                    break;
                case 2:
                    this.ofFogType = 3;
                    break;
                case 3:
                default:
                    this.ofFogType = 1;
            }
        }

        if (par1EnumOptions == GameSettingsOptionOF.FOG_START) {
            this.ofFogStart += 0.2F;
            if (this.ofFogStart > 0.81F) {
                this.ofFogStart = 0.2F;
            }
        }

        if (par1EnumOptions == GameSettingsOptionOF.SMOOTH_FPS) {
            this.ofSmoothFps = !this.ofSmoothFps;
        }

        if (par1EnumOptions == GameSettingsOptionOF.SMOOTH_WORLD) {
            this.ofSmoothWorld = !this.ofSmoothWorld;
            Config.updateThreadPriorities();
        }

        if (par1EnumOptions == GameSettingsOptionOF.CLOUDS) {
            this.ofClouds++;
            if (this.ofClouds > 3) {
                this.ofClouds = 0;
            }

            this.optiRefine$updateRenderClouds();
            RenderGlobal_resetClouds(this.mc.renderGlobal);
        }

        if (par1EnumOptions == GameSettingsOptionOF.TREES) {
            this.ofTrees = optiRefine$nextValue(this.ofTrees, OF_TREES_VALUES);
            this.mc.renderGlobal.loadRenderers();
        }

        if (par1EnumOptions == GameSettingsOptionOF.DROPPED_ITEMS) {
            this.ofDroppedItems++;
            if (this.ofDroppedItems > 2) {
                this.ofDroppedItems = 0;
            }
        }

        if (par1EnumOptions == GameSettingsOptionOF.RAIN) {
            this.ofRain++;
            if (this.ofRain > 3) {
                this.ofRain = 0;
            }
        }

        if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_WATER) {
            this.ofAnimatedWater++;
            if (this.ofAnimatedWater == 1) {
                this.ofAnimatedWater++;
            }

            if (this.ofAnimatedWater > 2) {
                this.ofAnimatedWater = 0;
            }
        }

        if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_LAVA) {
            this.ofAnimatedLava++;
            if (this.ofAnimatedLava == 1) {
                this.ofAnimatedLava++;
            }

            if (this.ofAnimatedLava > 2) {
                this.ofAnimatedLava = 0;
            }
        }

        if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_FIRE) {
            this.ofAnimatedFire = !this.ofAnimatedFire;
        }

        if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_PORTAL) {
            this.ofAnimatedPortal = !this.ofAnimatedPortal;
        }

        if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_REDSTONE) {
            this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
        }

        if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_EXPLOSION) {
            this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
        }

        if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_FLAME) {
            this.ofAnimatedFlame = !this.ofAnimatedFlame;
        }

        if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_SMOKE) {
            this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
        }

        if (par1EnumOptions == GameSettingsOptionOF.VOID_PARTICLES) {
            this.ofVoidParticles = !this.ofVoidParticles;
        }

        if (par1EnumOptions == GameSettingsOptionOF.WATER_PARTICLES) {
            this.ofWaterParticles = !this.ofWaterParticles;
        }

        if (par1EnumOptions == GameSettingsOptionOF.PORTAL_PARTICLES) {
            this.ofPortalParticles = !this.ofPortalParticles;
        }

        if (par1EnumOptions == GameSettingsOptionOF.POTION_PARTICLES) {
            this.ofPotionParticles = !this.ofPotionParticles;
        }

        if (par1EnumOptions == GameSettingsOptionOF.FIREWORK_PARTICLES) {
            this.ofFireworkParticles = !this.ofFireworkParticles;
        }

        if (par1EnumOptions == GameSettingsOptionOF.DRIPPING_WATER_LAVA) {
            this.ofDrippingWaterLava = !this.ofDrippingWaterLava;
        }

        if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_TERRAIN) {
            this.ofAnimatedTerrain = !this.ofAnimatedTerrain;
        }

        if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_TEXTURES) {
            this.ofAnimatedTextures = !this.ofAnimatedTextures;
        }

        if (par1EnumOptions == GameSettingsOptionOF.RAIN_SPLASH) {
            this.ofRainSplash = !this.ofRainSplash;
        }

        if (par1EnumOptions == GameSettingsOptionOF.LAGOMETER) {
            this.ofLagometer = !this.ofLagometer;
        }

        if (par1EnumOptions == GameSettingsOptionOF.SHOW_FPS) {
            this.ofShowFps = !this.ofShowFps;
        }

        if (par1EnumOptions == GameSettingsOptionOF.AUTOSAVE_TICKS) {
            int step = 900;
            this.ofAutoSaveTicks = Math.max(this.ofAutoSaveTicks / step * step, step);
            this.ofAutoSaveTicks *= 2;
            if (this.ofAutoSaveTicks > 32 * step) {
                this.ofAutoSaveTicks = step;
            }
        }

        if (par1EnumOptions == GameSettingsOptionOF.BETTER_GRASS) {
            this.ofBetterGrass++;
            if (this.ofBetterGrass > 3) {
                this.ofBetterGrass = 1;
            }

            this.mc.renderGlobal.loadRenderers();
        }

        if (par1EnumOptions == GameSettingsOptionOF.CONNECTED_TEXTURES) {
            this.ofConnectedTextures++;
            if (this.ofConnectedTextures > 3) {
                this.ofConnectedTextures = 1;
            }

            if (this.ofConnectedTextures == 2) {
                this.mc.renderGlobal.loadRenderers();
            } else {
                FMLClientHandler.instance().scheduleResourcesRefresh(VanillaResourceType.TEXTURES);
            }
        }

        if (par1EnumOptions == GameSettingsOptionOF.WEATHER) {
            this.ofWeather = !this.ofWeather;
        }

        if (par1EnumOptions == GameSettingsOptionOF.SKY) {
            this.ofSky = !this.ofSky;
        }

        if (par1EnumOptions == GameSettingsOptionOF.STARS) {
            this.ofStars = !this.ofStars;
        }

        if (par1EnumOptions == GameSettingsOptionOF.SUN_MOON) {
            this.ofSunMoon = !this.ofSunMoon;
        }

        if (par1EnumOptions == GameSettingsOptionOF.VIGNETTE) {
            this.ofVignette++;
            if (this.ofVignette > 2) {
                this.ofVignette = 0;
            }
        }

        if (par1EnumOptions == GameSettingsOptionOF.CHUNK_UPDATES) {
            this.ofChunkUpdates++;
            if (this.ofChunkUpdates > 5) {
                this.ofChunkUpdates = 1;
            }
        }

        if (par1EnumOptions == GameSettingsOptionOF.CHUNK_UPDATES_DYNAMIC) {
            this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
        }

        if (par1EnumOptions == GameSettingsOptionOF.TIME) {
            this.ofTime++;
            if (this.ofTime > 2) {
                this.ofTime = 0;
            }
        }

        if (par1EnumOptions == GameSettingsOptionOF.CLEAR_WATER) {
            this.ofClearWater = !this.ofClearWater;
            this.optiRefine$updateWaterOpacity();
        }

        if (par1EnumOptions == GameSettingsOptionOF.PROFILER) {
            this.ofProfiler = !this.ofProfiler;
        }

        if (par1EnumOptions == GameSettingsOptionOF.BETTER_SNOW) {
            this.ofBetterSnow = !this.ofBetterSnow;
            this.mc.renderGlobal.loadRenderers();
        }

        if (par1EnumOptions == GameSettingsOptionOF.SWAMP_COLORS) {
            this.ofSwampColors = !this.ofSwampColors;
            CustomColors.updateUseDefaultGrassFoliageColors();
            this.mc.renderGlobal.loadRenderers();
        }

        if (par1EnumOptions == GameSettingsOptionOF.RANDOM_ENTITIES) {
            this.ofRandomEntities = !this.ofRandomEntities;
            RandomEntities.update();
        }

        if (par1EnumOptions == GameSettingsOptionOF.SMOOTH_BIOMES) {
            this.ofSmoothBiomes = !this.ofSmoothBiomes;
            CustomColors.updateUseDefaultGrassFoliageColors();
            this.mc.renderGlobal.loadRenderers();
        }

        if (par1EnumOptions == GameSettingsOptionOF.CUSTOM_FONTS) {
            this.ofCustomFonts = !this.ofCustomFonts;
            this.mc.fontRenderer.onResourceManagerReload(Config.getResourceManager());
            this.mc.standardGalacticFontRenderer.onResourceManagerReload(Config.getResourceManager());
        }

        if (par1EnumOptions == GameSettingsOptionOF.CUSTOM_COLORS) {
            this.ofCustomColors = !this.ofCustomColors;
            CustomColors.update();
            this.mc.renderGlobal.loadRenderers();
        }

        if (par1EnumOptions == GameSettingsOptionOF.CUSTOM_ITEMS) {
            this.ofCustomItems = !this.ofCustomItems;
            FMLClientHandler.instance().scheduleResourcesRefresh((o)->true); // TODO : figure out what types is needed
        }

        if (par1EnumOptions == GameSettingsOptionOF.CUSTOM_SKY) {
            this.ofCustomSky = !this.ofCustomSky;
            CustomSky.update();
        }

        if (par1EnumOptions == GameSettingsOptionOF.SHOW_CAPES) {
            this.ofShowCapes = !this.ofShowCapes;
        }

        if (par1EnumOptions == GameSettingsOptionOF.NATURAL_TEXTURES) {
            this.ofNaturalTextures = !this.ofNaturalTextures;
            NaturalTextures.update();
            this.mc.renderGlobal.loadRenderers();
        }

        if (par1EnumOptions == GameSettingsOptionOF.EMISSIVE_TEXTURES) {
            this.ofEmissiveTextures = !this.ofEmissiveTextures;
            FMLClientHandler.instance().scheduleResourcesRefresh(VanillaResourceType.TEXTURES);
        }

        if (par1EnumOptions == GameSettingsOptionOF.FAST_MATH) {
            this.ofFastMath = !this.ofFastMath;
            MathHelper_fastMath_set(this.ofFastMath);
        }

        if (par1EnumOptions == GameSettingsOptionOF.FAST_RENDER) {
            if (!this.ofFastRender && Config.isShaders()) {
                Config.showGuiMessage(Lang.get("of.message.fr.shaders1"), Lang.get("of.message.fr.shaders2"));
                return;
            }

            this.ofFastRender = !this.ofFastRender;
            if (this.ofFastRender) {
                this.mc.entityRenderer.stopUseShader();
            }

            Config.updateFramebufferSize();
        }

        if (par1EnumOptions == GameSettingsOptionOF.TRANSLUCENT_BLOCKS) {
            if (this.ofTranslucentBlocks == 0) {
                this.ofTranslucentBlocks = 1;
            } else if (this.ofTranslucentBlocks == 1) {
                this.ofTranslucentBlocks = 2;
            } else if (this.ofTranslucentBlocks == 2) {
                this.ofTranslucentBlocks = 0;
            } else {
                this.ofTranslucentBlocks = 0;
            }

            this.mc.renderGlobal.loadRenderers();
        }

        if (par1EnumOptions == GameSettingsOptionOF.LAZY_CHUNK_LOADING) {
            this.ofLazyChunkLoading = !this.ofLazyChunkLoading;
        }

        if (par1EnumOptions == GameSettingsOptionOF.RENDER_REGIONS) {
            this.ofRenderRegions = !this.ofRenderRegions;
            this.mc.renderGlobal.loadRenderers();
        }

        if (par1EnumOptions == GameSettingsOptionOF.SMART_ANIMATIONS) {
            this.ofSmartAnimations = !this.ofSmartAnimations;
            this.mc.renderGlobal.loadRenderers();
        }

        if (par1EnumOptions == GameSettingsOptionOF.DYNAMIC_FOV) {
            this.ofDynamicFov = !this.ofDynamicFov;
        }

        if (par1EnumOptions == GameSettingsOptionOF.ALTERNATE_BLOCKS) {
            this.ofAlternateBlocks = !this.ofAlternateBlocks;
            FMLClientHandler.instance().scheduleResourcesRefresh((o)->true); // TODO : figure out what types is needed
        }

        if (par1EnumOptions == GameSettingsOptionOF.DYNAMIC_LIGHTS) {
            this.ofDynamicLights = optiRefine$nextValue(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
            DynamicLights.removeLights(this.mc.renderGlobal);
        }

        if (par1EnumOptions == GameSettingsOptionOF.SCREENSHOT_SIZE) {
            this.ofScreenshotSize++;
            if (this.ofScreenshotSize > 4) {
                this.ofScreenshotSize = 1;
            }

            if (!OpenGlHelper.isFramebufferEnabled()) {
                this.ofScreenshotSize = 1;
            }
        }

        if (par1EnumOptions == GameSettingsOptionOF.CUSTOM_ENTITY_MODELS) {
            this.ofCustomEntityModels = !this.ofCustomEntityModels;
            FMLClientHandler.instance().scheduleResourcesRefresh(VanillaResourceType.MODELS);
        }

        if (par1EnumOptions == GameSettingsOptionOF.CUSTOM_GUIS) {
            this.ofCustomGuis = !this.ofCustomGuis;
            CustomGuis.update();
        }

        if (par1EnumOptions == GameSettingsOptionOF.SHOW_GL_ERRORS) {
            this.ofShowGlErrors = !this.ofShowGlErrors;
        }

        if (par1EnumOptions == GameSettingsOptionOF.HELD_ITEM_TOOLTIPS) {
            this.heldItemTooltips = !this.heldItemTooltips;
        }

        if (par1EnumOptions == GameSettingsOptionOF.ADVANCED_TOOLTIPS) {
            this.advancedItemTooltips = !this.advancedItemTooltips;
        }
    }

    @Shadow
    public boolean advancedItemTooltips;

    @Shadow
    public boolean heldItemTooltips;

    @Shadow
    public abstract float getOptionFloatValue(GameSettings.Options settingOption);

    @WrapMethod(method = "getKeyBinding")
    // [AUDIT-OK] target getKeyBinding(Options)Ljava/lang/String; matches baseline; OF-first dispatch mirrors OF getKeyBinding
    public String getKeyBindingOFForged(GameSettings.Options settingOption, Operation<String> original){
        String str = getKeyBindingOF(settingOption);
        if (str != null) {
            OptiRefineLog.log.info("getKeyBindingOF : {}", str);
            return str;
        }
        else return original.call(settingOption);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] branch chain mirrors OF getKeyBindingOF; every GameSettingsOptionOF.* constant exists in GameSettingsOptionOF.java; FRAMERATE_LIMIT branch uses Options_valueMax_get like OF
    private String getKeyBindingOF(GameSettings.Options par1EnumOptions) {
        String var2 = I18n.hasKey(par1EnumOptions.getTranslation()) ? I18n.format(par1EnumOptions.getTranslation()) + ": " : par1EnumOptions.getTranslation();

        if (par1EnumOptions == GameSettings.Options.RENDER_DISTANCE) {
            int distChunks = (int) this.getOptionFloatValue(par1EnumOptions);
            String str = I18n.format("of.options.renderDistance.tiny");
            int baseDist = 2;
            if (distChunks >= 4) {
                str = I18n.format("of.options.renderDistance.short");
                baseDist = 4;
            }

            if (distChunks >= 8) {
                str = I18n.format("of.options.renderDistance.normal");
                baseDist = 8;
            }

            if (distChunks >= 16) {
                str = I18n.format("of.options.renderDistance.far");
                baseDist = 16;
            }

            if (distChunks >= 32) {
                str = Lang.get("of.options.renderDistance.extreme");
                baseDist = 32;
            }

            if (distChunks >= 48) {
                str = Lang.get("of.options.renderDistance.insane");
                baseDist = 48;
            }

            if (distChunks >= 64) {
                str = Lang.get("of.options.renderDistance.ludicrous");
                baseDist = 64;
            }

            int diff = this.renderDistanceChunks - baseDist;
            String descr = str;
            if (diff > 0) {
                descr = str + "+";
            }

            return var2 + distChunks + " " + descr;
        } else if (par1EnumOptions == GameSettingsOptionOF.FOG_FANCY) {
            return switch (this.ofFogType) {
                case 1 -> var2 + Lang.getFast();
                case 2 -> var2 + Lang.getFancy();
                default -> var2 + Lang.getOff();
            };
        } else if (par1EnumOptions == GameSettingsOptionOF.FOG_START) {
            return var2 + this.ofFogStart;
        } else if (par1EnumOptions == GameSettingsOptionOF.MIPMAP_TYPE) {
            return switch (this.ofMipmapType) {
                case 0 -> var2 + Lang.get("of.options.mipmap.nearest");
                case 1 -> var2 + Lang.get("of.options.mipmap.linear");
                case 2 -> var2 + Lang.get("of.options.mipmap.bilinear");
                case 3 -> var2 + Lang.get("of.options.mipmap.trilinear");
                default -> var2 + "of.options.mipmap.nearest";
            };
        } else if (par1EnumOptions == GameSettingsOptionOF.SMOOTH_FPS) {
            return this.ofSmoothFps ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.SMOOTH_WORLD) {
            return this.ofSmoothWorld ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.CLOUDS) {
            return switch (this.ofClouds) {
                case 1 -> var2 + Lang.getFast();
                case 2 -> var2 + Lang.getFancy();
                case 3 -> var2 + Lang.getOff();
                default -> var2 + Lang.getDefault();
            };
        } else if (par1EnumOptions == GameSettingsOptionOF.TREES) {
            return switch (this.ofTrees) {
                case 1 -> var2 + Lang.getFast();
                case 2 -> var2 + Lang.getFancy();
                case 4 -> var2 + Lang.get("of.general.smart");
                default -> var2 + Lang.getDefault();
            };
        } else if (par1EnumOptions == GameSettingsOptionOF.DROPPED_ITEMS) {
            return switch (this.ofDroppedItems) {
                case 1 -> var2 + Lang.getFast();
                case 2 -> var2 + Lang.getFancy();
                default -> var2 + Lang.getDefault();
            };
        } else if (par1EnumOptions == GameSettingsOptionOF.RAIN) {
            return switch (this.ofRain) {
                case 1 -> var2 + Lang.getFast();
                case 2 -> var2 + Lang.getFancy();
                case 3 -> var2 + Lang.getOff();
                default -> var2 + Lang.getDefault();
            };
        } else if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_WATER) {
            return switch (this.ofAnimatedWater) {
                case 1 -> var2 + Lang.get("of.options.animation.dynamic");
                case 2 -> var2 + Lang.getOff();
                default -> var2 + Lang.getOn();
            };
        } else if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_LAVA) {
            return switch (this.ofAnimatedLava) {
                case 1 -> var2 + Lang.get("of.options.animation.dynamic");
                case 2 -> var2 + Lang.getOff();
                default -> var2 + Lang.getOn();
            };
        } else if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_FIRE) {
            return this.ofAnimatedFire ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_PORTAL) {
            return this.ofAnimatedPortal ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_REDSTONE) {
            return this.ofAnimatedRedstone ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_EXPLOSION) {
            return this.ofAnimatedExplosion ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_FLAME) {
            return this.ofAnimatedFlame ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_SMOKE) {
            return this.ofAnimatedSmoke ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.VOID_PARTICLES) {
            return this.ofVoidParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.WATER_PARTICLES) {
            return this.ofWaterParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.PORTAL_PARTICLES) {
            return this.ofPortalParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.POTION_PARTICLES) {
            return this.ofPotionParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.FIREWORK_PARTICLES) {
            return this.ofFireworkParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.DRIPPING_WATER_LAVA) {
            return this.ofDrippingWaterLava ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_TERRAIN) {
            return this.ofAnimatedTerrain ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.ANIMATED_TEXTURES) {
            return this.ofAnimatedTextures ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.RAIN_SPLASH) {
            return this.ofRainSplash ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.LAGOMETER) {
            return this.ofLagometer ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.SHOW_FPS) {
            return this.ofShowFps ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.AUTOSAVE_TICKS) {
            int step = 900;
            if (this.ofAutoSaveTicks <= step) {
                return var2 + Lang.get("of.options.save.45s");
            } else if (this.ofAutoSaveTicks <= 2 * step) {
                return var2 + Lang.get("of.options.save.90s");
            } else if (this.ofAutoSaveTicks <= 4 * step) {
                return var2 + Lang.get("of.options.save.3min");
            } else if (this.ofAutoSaveTicks <= 8 * step) {
                return var2 + Lang.get("of.options.save.6min");
            } else {
                return this.ofAutoSaveTicks <= 16 * step ? var2 + Lang.get("of.options.save.12min") : var2 + Lang.get("of.options.save.24min");
            }
        } else if (par1EnumOptions == GameSettingsOptionOF.BETTER_GRASS) {
            return switch (this.ofBetterGrass) {
                case 1 -> var2 + Lang.getFast();
                case 2 -> var2 + Lang.getFancy();
                default -> var2 + Lang.getOff();
            };
        } else if (par1EnumOptions == GameSettingsOptionOF.CONNECTED_TEXTURES) {
            return switch (this.ofConnectedTextures) {
                case 1 -> var2 + Lang.getFast();
                case 2 -> var2 + Lang.getFancy();
                default -> var2 + Lang.getOff();
            };
        } else if (par1EnumOptions == GameSettingsOptionOF.WEATHER) {
            return this.ofWeather ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.SKY) {
            return this.ofSky ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.STARS) {
            return this.ofStars ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.SUN_MOON) {
            return this.ofSunMoon ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.VIGNETTE) {
            return switch (this.ofVignette) {
                case 1 -> var2 + Lang.getFast();
                case 2 -> var2 + Lang.getFancy();
                default -> var2 + Lang.getDefault();
            };
        } else if (par1EnumOptions == GameSettingsOptionOF.CHUNK_UPDATES) {
            return var2 + this.ofChunkUpdates;
        } else if (par1EnumOptions == GameSettingsOptionOF.CHUNK_UPDATES_DYNAMIC) {
            return this.ofChunkUpdatesDynamic ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.TIME) {
            if (this.ofTime == 1) {
                return var2 + Lang.get("of.options.time.dayOnly");
            } else {
                return this.ofTime == 2 ? var2 + Lang.get("of.options.time.nightOnly") : var2 + Lang.getDefault();
            }
        } else if (par1EnumOptions == GameSettingsOptionOF.CLEAR_WATER) {
            return this.ofClearWater ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.AA_LEVEL) {
            String suffix = "";
            if (this.ofAaLevel != Config.getAntialiasingLevel()) {
                suffix = " (" + Lang.get("of.general.restart") + ")";
            }

            return this.ofAaLevel == 0 ? var2 + Lang.getOff() + suffix : var2 + this.ofAaLevel + suffix;
        } else if (par1EnumOptions == GameSettingsOptionOF.AF_LEVEL) {
            return this.ofAfLevel == 1 ? var2 + Lang.getOff() : var2 + this.ofAfLevel;
        } else if (par1EnumOptions == GameSettingsOptionOF.PROFILER) {
            return this.ofProfiler ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.BETTER_SNOW) {
            return this.ofBetterSnow ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.SWAMP_COLORS) {
            return this.ofSwampColors ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.RANDOM_ENTITIES) {
            return this.ofRandomEntities ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.SMOOTH_BIOMES) {
            return this.ofSmoothBiomes ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.CUSTOM_FONTS) {
            return this.ofCustomFonts ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.CUSTOM_COLORS) {
            return this.ofCustomColors ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.CUSTOM_SKY) {
            return this.ofCustomSky ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.SHOW_CAPES) {
            return this.ofShowCapes ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.CUSTOM_ITEMS) {
            return this.ofCustomItems ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.NATURAL_TEXTURES) {
            return this.ofNaturalTextures ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.EMISSIVE_TEXTURES) {
            return this.ofEmissiveTextures ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.FAST_MATH) {
            return this.ofFastMath ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.FAST_RENDER) {
            return this.ofFastRender ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.TRANSLUCENT_BLOCKS) {
            if (this.ofTranslucentBlocks == 1) {
                return var2 + Lang.getFast();
            } else {
                return this.ofTranslucentBlocks == 2 ? var2 + Lang.getFancy() : var2 + Lang.getDefault();
            }
        } else if (par1EnumOptions == GameSettingsOptionOF.LAZY_CHUNK_LOADING) {
            return this.ofLazyChunkLoading ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.RENDER_REGIONS) {
            return this.ofRenderRegions ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.SMART_ANIMATIONS) {
            return this.ofSmartAnimations ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.DYNAMIC_FOV) {
            return this.ofDynamicFov ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.ALTERNATE_BLOCKS) {
            return this.ofAlternateBlocks ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.DYNAMIC_LIGHTS) {
            int index = org.apache.commons.lang3.ArrayUtils.indexOf(OF_DYNAMIC_LIGHTS, this.ofDynamicLights);
            return var2 + getTranslation(KEYS_DYNAMIC_LIGHTS, index);
        } else if (par1EnumOptions == GameSettingsOptionOF.SCREENSHOT_SIZE) {
            return this.ofScreenshotSize <= 1 ? var2 + Lang.getDefault() : var2 + this.ofScreenshotSize + "x";
        } else if (par1EnumOptions == GameSettingsOptionOF.CUSTOM_ENTITY_MODELS) {
            return this.ofCustomEntityModels ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.CUSTOM_GUIS) {
            return this.ofCustomGuis ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.SHOW_GL_ERRORS) {
            return this.ofShowGlErrors ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.FULLSCREEN_MODE) {
            return this.ofFullscreenMode.equals("Default") ? var2 + Lang.getDefault() : var2 + this.ofFullscreenMode;
        } else if (par1EnumOptions == GameSettingsOptionOF.HELD_ITEM_TOOLTIPS) {
            return this.heldItemTooltips ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettingsOptionOF.ADVANCED_TOOLTIPS) {
            return this.advancedItemTooltips ? var2 + Lang.getOn() : var2 + Lang.getOff();
        } else if (par1EnumOptions == GameSettings.Options.FRAMERATE_LIMIT) {
            float var6 = this.getOptionFloatValue(par1EnumOptions);
            if (var6 == 0.0F) {
                return var2 + Lang.get("of.options.framerateLimit.vsync");
            } else {
                return var6 == Options_valueMax_get(par1EnumOptions) ? var2 + I18n.format("options.framerateLimit.max") : var2 + (int) var6 + " fps";
            }
        } else {
            return null;
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.settings.GameSettings$Options field_148272_O F")
    // [AUDIT-OK] vanilla SRG field_148272_O = GameSettings$Options.valueMax; missing deobf=true (devrun only)
    private static native float Options_valueMax_get(GameSettings.Options options);

    @Shadow
    // [AUDIT-OK] baseline member getTranslation (static) exists in GameSettings
    private static native String getTranslation(String[] strArray, int index) ;

    @Shadow
    // [AUDIT-OK] baseline member optionsFile exists in GameSettings
    private File optionsFile;

    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added @Unique method loadOfOptions; key bounds verified vs OF (ofRenderDistanceChunks 2..1024, ofFogStart 0.2..0.8, etc.)
    public void loadOfOptions() {
        try {
            File ofReadFile = this.optionsFileOF;
            if (!ofReadFile.exists()) {
                ofReadFile = this.optionsFile;
            }

            if (!ofReadFile.exists()) {
                return;
            }

            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(ofReadFile), StandardCharsets.UTF_8));
            String s;

            while ((s = bufferedreader.readLine()) != null) {
                try {
                    String[] as = s.split(":");
                    if (as[0].equals("ofRenderDistanceChunks") && as.length >= 2) {
                        this.renderDistanceChunks = Integer.parseInt(as[1]);
                        this.renderDistanceChunks = Config.limit(this.renderDistanceChunks, 2, 1024);
                    }

                    if (as[0].equals("ofFogType") && as.length >= 2) {
                        this.ofFogType = Integer.parseInt(as[1]);
                        this.ofFogType = Config.limit(this.ofFogType, 1, 3);
                    }

                    if (as[0].equals("ofFogStart") && as.length >= 2) {
                        this.ofFogStart = Float.parseFloat(as[1]);
                        if (this.ofFogStart < 0.2F) {
                            this.ofFogStart = 0.2F;
                        }

                        if (this.ofFogStart > 0.81F) {
                            this.ofFogStart = 0.8F;
                        }
                    }

                    if (as[0].equals("ofMipmapType") && as.length >= 2) {
                        this.ofMipmapType = Integer.parseInt(as[1]);
                        this.ofMipmapType = Config.limit(this.ofMipmapType, 0, 3);
                    }

                    if (as[0].equals("ofOcclusionFancy") && as.length >= 2) {
                        this.ofOcclusionFancy = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofSmoothFps") && as.length >= 2) {
                        this.ofSmoothFps = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofSmoothWorld") && as.length >= 2) {
                        this.ofSmoothWorld = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofAoLevel") && as.length >= 2) {
                        this.ofAoLevel = Float.parseFloat(as[1]);
                        this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
                    }

                    if (as[0].equals("ofClouds") && as.length >= 2) {
                        this.ofClouds = Integer.parseInt(as[1]);
                        this.ofClouds = Config.limit(this.ofClouds, 0, 3);
                        this.optiRefine$updateRenderClouds();
                    }

                    if (as[0].equals("ofCloudsHeight") && as.length >= 2) {
                        this.ofCloudsHeight = Float.parseFloat(as[1]);
                        this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
                    }

                    if (as[0].equals("ofTrees") && as.length >= 2) {
                        this.ofTrees = Integer.parseInt(as[1]);
                        this.ofTrees = optiRefine$limit(this.ofTrees, OF_TREES_VALUES);
                    }

                    if (as[0].equals("ofDroppedItems") && as.length >= 2) {
                        this.ofDroppedItems = Integer.parseInt(as[1]);
                        this.ofDroppedItems = Config.limit(this.ofDroppedItems, 0, 2);
                    }

                    if (as[0].equals("ofRain") && as.length >= 2) {
                        this.ofRain = Integer.parseInt(as[1]);
                        this.ofRain = Config.limit(this.ofRain, 0, 3);
                    }

                    if (as[0].equals("ofAnimatedWater") && as.length >= 2) {
                        this.ofAnimatedWater = Integer.parseInt(as[1]);
                        this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
                    }

                    if (as[0].equals("ofAnimatedLava") && as.length >= 2) {
                        this.ofAnimatedLava = Integer.parseInt(as[1]);
                        this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
                    }

                    if (as[0].equals("ofAnimatedFire") && as.length >= 2) {
                        this.ofAnimatedFire = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofAnimatedPortal") && as.length >= 2) {
                        this.ofAnimatedPortal = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofAnimatedRedstone") && as.length >= 2) {
                        this.ofAnimatedRedstone = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofAnimatedExplosion") && as.length >= 2) {
                        this.ofAnimatedExplosion = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofAnimatedFlame") && as.length >= 2) {
                        this.ofAnimatedFlame = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofAnimatedSmoke") && as.length >= 2) {
                        this.ofAnimatedSmoke = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofVoidParticles") && as.length >= 2) {
                        this.ofVoidParticles = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofWaterParticles") && as.length >= 2) {
                        this.ofWaterParticles = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofPortalParticles") && as.length >= 2) {
                        this.ofPortalParticles = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofPotionParticles") && as.length >= 2) {
                        this.ofPotionParticles = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofFireworkParticles") && as.length >= 2) {
                        this.ofFireworkParticles = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofDrippingWaterLava") && as.length >= 2) {
                        this.ofDrippingWaterLava = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofAnimatedTerrain") && as.length >= 2) {
                        this.ofAnimatedTerrain = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofAnimatedTextures") && as.length >= 2) {
                        this.ofAnimatedTextures = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofRainSplash") && as.length >= 2) {
                        this.ofRainSplash = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofLagometer") && as.length >= 2) {
                        this.ofLagometer = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofShowFps") && as.length >= 2) {
                        this.ofShowFps = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofAutoSaveTicks") && as.length >= 2) {
                        this.ofAutoSaveTicks = Integer.parseInt(as[1]);
                        this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
                    }

                    if (as[0].equals("ofBetterGrass") && as.length >= 2) {
                        this.ofBetterGrass = Integer.parseInt(as[1]);
                        this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
                    }

                    if (as[0].equals("ofConnectedTextures") && as.length >= 2) {
                        this.ofConnectedTextures = Integer.parseInt(as[1]);
                        this.ofConnectedTextures = Config.limit(this.ofConnectedTextures, 1, 3);
                    }

                    if (as[0].equals("ofWeather") && as.length >= 2) {
                        this.ofWeather = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofSky") && as.length >= 2) {
                        this.ofSky = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofStars") && as.length >= 2) {
                        this.ofStars = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofSunMoon") && as.length >= 2) {
                        this.ofSunMoon = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofVignette") && as.length >= 2) {
                        this.ofVignette = Integer.parseInt(as[1]);
                        this.ofVignette = Config.limit(this.ofVignette, 0, 2);
                    }

                    if (as[0].equals("ofChunkUpdates") && as.length >= 2) {
                        this.ofChunkUpdates = Integer.parseInt(as[1]);
                        this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
                    }

                    if (as[0].equals("ofChunkUpdatesDynamic") && as.length >= 2) {
                        this.ofChunkUpdatesDynamic = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofTime") && as.length >= 2) {
                        this.ofTime = Integer.parseInt(as[1]);
                        this.ofTime = Config.limit(this.ofTime, 0, 2);
                    }

                    if (as[0].equals("ofClearWater") && as.length >= 2) {
                        this.ofClearWater = Boolean.parseBoolean(as[1]);
                        this.optiRefine$updateWaterOpacity();
                    }

                    if (as[0].equals("ofAaLevel") && as.length >= 2) {
                        this.ofAaLevel = Integer.parseInt(as[1]);
                        this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
                    }

                    if (as[0].equals("ofAfLevel") && as.length >= 2) {
                        this.ofAfLevel = Integer.parseInt(as[1]);
                        this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
                    }

                    if (as[0].equals("ofProfiler") && as.length >= 2) {
                        this.ofProfiler = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofBetterSnow") && as.length >= 2) {
                        this.ofBetterSnow = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofSwampColors") && as.length >= 2) {
                        this.ofSwampColors = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofRandomEntities") && as.length >= 2) {
                        this.ofRandomEntities = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofSmoothBiomes") && as.length >= 2) {
                        this.ofSmoothBiomes = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofCustomFonts") && as.length >= 2) {
                        this.ofCustomFonts = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofCustomColors") && as.length >= 2) {
                        this.ofCustomColors = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofCustomItems") && as.length >= 2) {
                        this.ofCustomItems = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofCustomSky") && as.length >= 2) {
                        this.ofCustomSky = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofShowCapes") && as.length >= 2) {
                        this.ofShowCapes = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofNaturalTextures") && as.length >= 2) {
                        this.ofNaturalTextures = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofEmissiveTextures") && as.length >= 2) {
                        this.ofEmissiveTextures = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofLazyChunkLoading") && as.length >= 2) {
                        this.ofLazyChunkLoading = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofRenderRegions") && as.length >= 2) {
                        this.ofRenderRegions = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofSmartAnimations") && as.length >= 2) {
                        this.ofSmartAnimations = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofDynamicFov") && as.length >= 2) {
                        this.ofDynamicFov = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofAlternateBlocks") && as.length >= 2) {
                        this.ofAlternateBlocks = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofDynamicLights") && as.length >= 2) {
                        this.ofDynamicLights = Integer.parseInt(as[1]);
                        this.ofDynamicLights = optiRefine$limit(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
                    }

                    if (as[0].equals("ofScreenshotSize") && as.length >= 2) {
                        this.ofScreenshotSize = Integer.parseInt(as[1]);
                        this.ofScreenshotSize = Config.limit(this.ofScreenshotSize, 1, 4);
                    }

                    if (as[0].equals("ofCustomEntityModels") && as.length >= 2) {
                        this.ofCustomEntityModels = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofCustomGuis") && as.length >= 2) {
                        this.ofCustomGuis = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofShowGlErrors") && as.length >= 2) {
                        this.ofShowGlErrors = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofFullscreenMode") && as.length >= 2) {
                        this.ofFullscreenMode = as[1];
                    }

                    if (as[0].equals("ofFastMath") && as.length >= 2) {
                        this.ofFastMath = Boolean.parseBoolean(as[1]);
                        MathHelper_fastMath_set(this.ofFastMath);
                    }

                    if (as[0].equals("ofFastRender") && as.length >= 2) {
                        this.ofFastRender = Boolean.parseBoolean(as[1]);
                    }

                    if (as[0].equals("ofTranslucentBlocks") && as.length >= 2) {
                        this.ofTranslucentBlocks = Integer.parseInt(as[1]);
                        this.ofTranslucentBlocks = Config.limit(this.ofTranslucentBlocks, 0, 2);
                    }

                    if (as[0].equals("key_" + this.ofKeyBindZoom.getKeyDescription())) {
                        this.ofKeyBindZoom.setKeyCode(Integer.parseInt(as[1]));
                    }
                } catch (Exception var5) {
                    Config.dbg("Skipping bad option: " + s);
                    var5.printStackTrace(OptiRefineLog.logOut);
                }
            }

            KeyUtils.fixKeyConflicts(this.keyBindings, new KeyBinding[]{this.ofKeyBindZoom});
            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        } catch (Exception var6) {
            Config.warn("Failed to load options");
            var6.printStackTrace(OptiRefineLog.logOut);
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added @Unique method saveOfOptions; key set matches OF
    public void saveOfOptions() {
        try {
            PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFileOF), StandardCharsets.UTF_8));
            printwriter.println("ofFogType:" + this.ofFogType);
            printwriter.println("ofFogStart:" + this.ofFogStart);
            printwriter.println("ofMipmapType:" + this.ofMipmapType);
            printwriter.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
            printwriter.println("ofSmoothFps:" + this.ofSmoothFps);
            printwriter.println("ofSmoothWorld:" + this.ofSmoothWorld);
            printwriter.println("ofAoLevel:" + this.ofAoLevel);
            printwriter.println("ofClouds:" + this.ofClouds);
            printwriter.println("ofCloudsHeight:" + this.ofCloudsHeight);
            printwriter.println("ofTrees:" + this.ofTrees);
            printwriter.println("ofDroppedItems:" + this.ofDroppedItems);
            printwriter.println("ofRain:" + this.ofRain);
            printwriter.println("ofAnimatedWater:" + this.ofAnimatedWater);
            printwriter.println("ofAnimatedLava:" + this.ofAnimatedLava);
            printwriter.println("ofAnimatedFire:" + this.ofAnimatedFire);
            printwriter.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
            printwriter.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
            printwriter.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
            printwriter.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
            printwriter.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
            printwriter.println("ofVoidParticles:" + this.ofVoidParticles);
            printwriter.println("ofWaterParticles:" + this.ofWaterParticles);
            printwriter.println("ofPortalParticles:" + this.ofPortalParticles);
            printwriter.println("ofPotionParticles:" + this.ofPotionParticles);
            printwriter.println("ofFireworkParticles:" + this.ofFireworkParticles);
            printwriter.println("ofDrippingWaterLava:" + this.ofDrippingWaterLava);
            printwriter.println("ofAnimatedTerrain:" + this.ofAnimatedTerrain);
            printwriter.println("ofAnimatedTextures:" + this.ofAnimatedTextures);
            printwriter.println("ofRainSplash:" + this.ofRainSplash);
            printwriter.println("ofLagometer:" + this.ofLagometer);
            printwriter.println("ofShowFps:" + this.ofShowFps);
            printwriter.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
            printwriter.println("ofBetterGrass:" + this.ofBetterGrass);
            printwriter.println("ofConnectedTextures:" + this.ofConnectedTextures);
            printwriter.println("ofWeather:" + this.ofWeather);
            printwriter.println("ofSky:" + this.ofSky);
            printwriter.println("ofStars:" + this.ofStars);
            printwriter.println("ofSunMoon:" + this.ofSunMoon);
            printwriter.println("ofVignette:" + this.ofVignette);
            printwriter.println("ofChunkUpdates:" + this.ofChunkUpdates);
            printwriter.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
            printwriter.println("ofTime:" + this.ofTime);
            printwriter.println("ofClearWater:" + this.ofClearWater);
            printwriter.println("ofAaLevel:" + this.ofAaLevel);
            printwriter.println("ofAfLevel:" + this.ofAfLevel);
            printwriter.println("ofProfiler:" + this.ofProfiler);
            printwriter.println("ofBetterSnow:" + this.ofBetterSnow);
            printwriter.println("ofSwampColors:" + this.ofSwampColors);
            printwriter.println("ofRandomEntities:" + this.ofRandomEntities);
            printwriter.println("ofSmoothBiomes:" + this.ofSmoothBiomes);
            printwriter.println("ofCustomFonts:" + this.ofCustomFonts);
            printwriter.println("ofCustomColors:" + this.ofCustomColors);
            printwriter.println("ofCustomItems:" + this.ofCustomItems);
            printwriter.println("ofCustomSky:" + this.ofCustomSky);
            printwriter.println("ofShowCapes:" + this.ofShowCapes);
            printwriter.println("ofNaturalTextures:" + this.ofNaturalTextures);
            printwriter.println("ofEmissiveTextures:" + this.ofEmissiveTextures);
            printwriter.println("ofLazyChunkLoading:" + this.ofLazyChunkLoading);
            printwriter.println("ofRenderRegions:" + this.ofRenderRegions);
            printwriter.println("ofSmartAnimations:" + this.ofSmartAnimations);
            printwriter.println("ofDynamicFov:" + this.ofDynamicFov);
            printwriter.println("ofAlternateBlocks:" + this.ofAlternateBlocks);
            printwriter.println("ofDynamicLights:" + this.ofDynamicLights);
            printwriter.println("ofScreenshotSize:" + this.ofScreenshotSize);
            printwriter.println("ofCustomEntityModels:" + this.ofCustomEntityModels);
            printwriter.println("ofCustomGuis:" + this.ofCustomGuis);
            printwriter.println("ofShowGlErrors:" + this.ofShowGlErrors);
            printwriter.println("ofFullscreenMode:" + this.ofFullscreenMode);
            printwriter.println("ofFastMath:" + this.ofFastMath);
            printwriter.println("ofFastRender:" + this.ofFastRender);
            printwriter.println("ofTranslucentBlocks:" + this.ofTranslucentBlocks);
            printwriter.println("key_" + this.ofKeyBindZoom.getKeyDescription() + ":" + this.ofKeyBindZoom.getKeyCode());
            printwriter.close();
        } catch (Exception var2) {
            Config.warn("Failed to save options");
            var2.printStackTrace(OptiRefineLog.logOut);
        }
    }

    @Shadow
    // [AUDIT-OK] baseline member clouds exists in GameSettings
    public int clouds;

    @Shadow
    // [AUDIT-OK] baseline member fancyGraphics exists in GameSettings
    public boolean fancyGraphics;

    @Unique
    // [AUDIT-OK] OF-added @Unique method updateRenderClouds, body matches OF
    private void optiRefine$updateRenderClouds() {
        switch (this.ofClouds) {
            case 1:
                this.clouds = 1;
                break;
            case 2:
                this.clouds = 2;
                break;
            case 3:
                this.clouds = 0;
                break;
            default:
                if (this.fancyGraphics) {
                    this.clouds = 2;
                } else {
                    this.clouds = 1;
                }
        }
    }



    @Shadow
    // [AUDIT-OK] baseline members forceUnicodeFont/useVbo/viewBobbing/mipmapLevels/ambientOcclusion/fovSetting/gammaSetting/particleSetting exist in GameSettings
    public boolean forceUnicodeFont;

    @Shadow
    public boolean useVbo;

    @Shadow
    public boolean viewBobbing;

    @Shadow
    public int mipmapLevels;

    @Shadow
    public int ambientOcclusion;

    @Shadow
    public float fovSetting;

    @Shadow
    public float gammaSetting;

    @Shadow
    public int particleSetting;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added @Unique method resetSettings, body matches OF (incl. Shaders.setShaderPack("OFF")/uninit/storeConfig)
    public void resetSettings() {
        this.renderDistanceChunks = 8;
        this.viewBobbing = true;
        this.anaglyph = false;
        this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
        this.enableVsync = false;
        this.updateVSync();
        this.mipmapLevels = 4;
        this.fancyGraphics = true;
        this.ambientOcclusion = 2;
        this.clouds = 2;
        this.fovSetting = 70.0F;
        this.gammaSetting = 0.0F;
        this.guiScale = 0;
        this.particleSetting = 0;
        this.heldItemTooltips = true;
        this.useVbo = false;
        this.forceUnicodeFont = false;
        this.ofFogType = 1;
        this.ofFogStart = 0.8F;
        this.ofMipmapType = 0;
        this.ofOcclusionFancy = false;
        this.ofSmartAnimations = false;
        this.ofSmoothFps = false;
        Config.updateAvailableProcessors();
        this.ofSmoothWorld = Config.isSingleProcessor();
        this.ofLazyChunkLoading = false;
        this.ofRenderRegions = false;
        this.ofFastMath = false;
        this.ofFastRender = false;
        this.ofTranslucentBlocks = 0;
        this.ofDynamicFov = true;
        this.ofAlternateBlocks = true;
        this.ofDynamicLights = 3;
        this.ofScreenshotSize = 1;
        this.ofCustomEntityModels = true;
        this.ofCustomGuis = true;
        this.ofShowGlErrors = true;
        this.ofAoLevel = 1.0F;
        this.ofAaLevel = 0;
        this.ofAfLevel = 1;
        this.ofClouds = 0;
        this.ofCloudsHeight = 0.0F;
        this.ofTrees = 0;
        this.ofRain = 0;
        this.ofBetterGrass = 3;
        this.ofAutoSaveTicks = 4000;
        this.ofLagometer = false;
        this.ofShowFps = false;
        this.ofProfiler = false;
        this.ofWeather = true;
        this.ofSky = true;
        this.ofStars = true;
        this.ofSunMoon = true;
        this.ofVignette = 0;
        this.ofChunkUpdates = 1;
        this.ofChunkUpdatesDynamic = false;
        this.ofTime = 0;
        this.ofClearWater = false;
        this.ofBetterSnow = false;
        this.ofFullscreenMode = "Default";
        this.ofSwampColors = true;
        this.ofRandomEntities = true;
        this.ofSmoothBiomes = true;
        this.ofCustomFonts = true;
        this.ofCustomColors = true;
        this.ofCustomItems = true;
        this.ofCustomSky = true;
        this.ofShowCapes = true;
        this.ofConnectedTextures = 2;
        this.ofNaturalTextures = false;
        this.ofEmissiveTextures = true;
        this.ofAnimatedWater = 0;
        this.ofAnimatedLava = 0;
        this.ofAnimatedFire = true;
        this.ofAnimatedPortal = true;
        this.ofAnimatedRedstone = true;
        this.ofAnimatedExplosion = true;
        this.ofAnimatedFlame = true;
        this.ofAnimatedSmoke = true;
        this.ofVoidParticles = true;
        this.ofWaterParticles = true;
        this.ofRainSplash = true;
        this.ofPortalParticles = true;
        this.ofPotionParticles = true;
        this.ofFireworkParticles = true;
        this.ofDrippingWaterLava = true;
        this.ofAnimatedTerrain = true;
        this.ofAnimatedTextures = true;
        Shaders.setShaderPack("OFF");
        Shaders.configAntialiasingLevel = 0;
        Shaders.uninit();
        Shaders.storeConfig();
        this.optiRefine$updateWaterOpacity();
        FMLClientHandler.instance().scheduleResourcesRefresh(VanillaResourceType.MODELS, VanillaResourceType.LANGUAGES, VanillaResourceType.TEXTURES, VanillaResourceType.SHADERS);
        this.saveOptions();
    }

    @Shadow
    // [AUDIT-OK] baseline member saveOptions exists in GameSettings
    public native void saveOptions();

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    // [AUDIT-OK] OF-added @Unique method updateVSync, matches OF
    public void updateVSync() {
        Display.setVSyncEnabled(this.enableVsync);
    }

    // [AUDIT-OK] OF-added @Unique method updateWaterOpacity, matches OF
    private void optiRefine$updateWaterOpacity() {
        if (Config.isIntegratedServerRunning()) {
            Config.waterOpacityChanged = true;
        }

        ClearWater.updateWaterOpacity(cast_GameSettings(this), this.mc.world);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    // [AUDIT-OK] OF-added @Unique method setAllAnimations, matches OF
    public void setAllAnimations(boolean flag) {
        int animVal = flag ? 0 : 2;
        this.ofAnimatedWater = animVal;
        this.ofAnimatedLava = animVal;
        this.ofAnimatedFire = flag;
        this.ofAnimatedPortal = flag;
        this.ofAnimatedRedstone = flag;
        this.ofAnimatedExplosion = flag;
        this.ofAnimatedFlame = flag;
        this.ofAnimatedSmoke = flag;
        this.ofVoidParticles = flag;
        this.ofWaterParticles = flag;
        this.ofRainSplash = flag;
        this.ofPortalParticles = flag;
        this.ofPotionParticles = flag;
        this.ofFireworkParticles = flag;
        this.particleSetting = flag ? 0 : 2;
        this.ofDrippingWaterLava = flag;
        this.ofAnimatedTerrain = flag;
        this.ofAnimatedTextures = flag;
    }

    // [AUDIT-OK] OF-added @Unique static helper nextValue, matches OF
    private static int optiRefine$nextValue(int val, int[] vals) {
        int index = org.apache.commons.lang3.ArrayUtils.indexOf(vals, val);
        if (index < 0) {
            return vals[0];
        } else {
            if (++index >= vals.length) {
                index = 0;
            }

            return vals[index];
        }
    }

    @Unique
    // [AUDIT-OK] OF-added @Unique static helper limit, matches OF
    private static int optiRefine$limit(int val, int[] vals) {
        int index = org.apache.commons.lang3.ArrayUtils.indexOf(vals, val);
        return index < 0 ? vals[0] : val;
    }
}
