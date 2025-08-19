package mods.Hileb.optirefine.mixin.defaults.minecraft.client.settings;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.core.OptiRefineLog;
import mods.Hileb.optirefine.library.common.utils.Lazy;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Mixin(GameSettings.class)
public abstract class MixinGameSettings {

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
    @Unique
    @Public
    private static final int DEFAULT = 0;
    @Unique
    @Public
    private static final int FAST = 1;
    @Unique
    @Public
    private static final int FANCY = 2;
    @Unique
    @Public
    private static final int OFF = 3;
    @Unique
    @Public
    private static final int SMART = 4;
    @Unique
    @Public
    private static final int ANIM_ON = 0;
    @Unique
    @Public
    private static final int ANIM_GENERATED = 1;
    @Unique
    @Public
    private static final int ANIM_OFF = 2;
    @Unique
    @Public
    private static final String DEFAULT_STR = "Default";
    @Unique
    private static final int[] OF_TREES_VALUES = new int[]{0, 1, 4, 2};
    @Unique
    private static final int[] OF_DYNAMIC_LIGHTS = new int[]{3, 1, 2};
    @Unique
    private static final String[] KEYS_DYNAMIC_LIGHTS = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
    @Unique
    public KeyBinding ofKeyBindZoom;
    @Unique
    private File optionsFileOF;

    @Shadow
    public int limitFramerate;
    @Shadow
    public KeyBinding[] keyBindings;
    @Shadow
    public int renderDistanceChunks;

    @Shadow
    public int guiScale;

    @Shadow
    protected Minecraft mc;

    @AccessibleOperation
    private static native GameSettings cast_GameSettings(Object o);

    @Inject(method = "setOptionValue", at = @At("HEAD"))
    public void fosetOptionValue(GameSettings.Options settingsOption, int value, CallbackInfo ci) {
        this.optiRefine$setOptionValueOF(settingsOption, value);
    }

    @Definition(id = "guiScale", field = "Lnet/minecraft/client/settings/GameSettings;guiScale:I")
    @Expression("this.guiScale = @(?)")
    @ModifyExpressionValue(method = "setOptionValue", at = @At("MIXINEXTRAS:EXPRESSION"))
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
    public boolean anaglyph;

    @WrapOperation(method = "setOptionValue", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/FMLClientHandler;refreshResources([Lnet/minecraftforge/client/resource/IResourceType;)V"))
    public void notReloadForShader(FMLClientHandler instance, IResourceType[] inclusion, Operation<Void> original) {
        if (!this.anaglyph && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.an.shaders1"), Lang.get("of.message.an.shaders2"));
            this.anaglyph = !this.anaglyph;
        } else {
            original.call(instance, inclusion);
        }
    }

    @Inject(method = "setOptionValue", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;fancyGraphics:Z"))
    public void hookForupdateRenderClouds(GameSettings.Options settingsOption, int value, CallbackInfo ci) {
        this.optiRefine$updateRenderClouds();
    }

    @WrapMethod(method = "loadOptions")
    public void hookForoadOfOptions(Operation<Void> original) {
        boolean init = this.optionsFile == null;
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
    public void hookFor_saveOfOptions(GameSettings instance, Operation<Void> original) {
        this.saveOfOptions();
        original.call(instance);
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.RenderGlobal resetClouds ()V")
    private static native void RenderGlobal_resetClouds(RenderGlobal renderGlobal);

    @Unique
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
    public boolean enableVsync;

    @Unique
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
    private static native void MathHelper_fastMath_set(boolean b);


    @Unique
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

    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
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

            return var2 + distChunks + " " + descr + "";
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
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.settings.GameSettings$Options valueMax F")
    private static native float Options_valueMax_get(GameSettings.Options options);

    @Shadow
    private static native String getTranslation(String[] strArray, int index) ;

    @Shadow
    private File optionsFile;

    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
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
    @Unique
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
    public int clouds;

    @Shadow
    public boolean fancyGraphics;

    @Unique
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
    @Unique
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
    public native void saveOptions();

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public void updateVSync() {
        Display.setVSyncEnabled(this.enableVsync);
    }

    @Unique
    private void optiRefine$updateWaterOpacity() {
        if (Config.isIntegratedServerRunning()) {
            Config.waterOpacityChanged = true;
        }

        ClearWater.updateWaterOpacity(cast_GameSettings(this), this.mc.world);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
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

    @Unique
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
    private static int optiRefine$limit(int val, int[] vals) {
        int index = org.apache.commons.lang3.ArrayUtils.indexOf(vals, val);
        return index < 0 ? vals[0] : val;
    }
}
/*
+++ net/minecraft/client/settings/GameSettings.java	Tue Aug 19 14:59:58 2025
@@ -3,47 +3,65 @@
 import com.google.common.base.Splitter;
 import com.google.common.collect.ImmutableSet;
 import com.google.common.collect.Lists;
 import com.google.common.collect.Maps;
 import com.google.common.collect.Sets;
 import com.google.gson.Gson;
+import java.io.BufferedReader;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
+import java.io.InputStreamReader;
 import java.io.OutputStreamWriter;
 import java.io.PrintWriter;
 import java.lang.reflect.ParameterizedType;
 import java.lang.reflect.Type;
 import java.nio.charset.StandardCharsets;
+import java.util.Arrays;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Set;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.gui.GuiNewChat;
+import net.minecraft.client.gui.GuiScreen;
 import net.minecraft.client.gui.chat.NarratorChatListener;
+import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.texture.TextureMap;
 import net.minecraft.client.resources.I18n;
 import net.minecraft.client.tutorial.TutorialSteps;
-import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.entity.player.EnumPlayerModelParts;
+import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.network.play.client.CPacketClientSettings;
 import net.minecraft.util.EnumHandSide;
 import net.minecraft.util.JsonUtils;
 import net.minecraft.util.SoundCategory;
 import net.minecraft.util.datafix.FixTypes;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.world.EnumDifficulty;
+import net.minecraftforge.client.resource.IResourceType;
+import net.optifine.ClearWater;
+import net.optifine.CustomColors;
+import net.optifine.CustomGuis;
+import net.optifine.CustomSky;
+import net.optifine.DynamicLights;
+import net.optifine.Lang;
+import net.optifine.NaturalTextures;
+import net.optifine.RandomEntities;
+import net.optifine.reflect.Reflector;
+import net.optifine.shaders.Shaders;
+import net.optifine.util.KeyUtils;
 import org.apache.commons.io.IOUtils;
 import org.apache.commons.lang3.ArrayUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;
 import org.lwjgl.input.Keyboard;
 import org.lwjgl.input.Mouse;
 import org.lwjgl.opengl.Display;
+import org.lwjgl.opengl.DisplayMode;

 public class GameSettings {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();
    private static final Type TYPE_LIST_STRING = new ParameterizedType() {
       public Type[] getActualTypeArguments() {
@@ -78,13 +96,13 @@
    public int limitFramerate = 120;
    public int clouds = 2;
    public boolean fancyGraphics = true;
    public int ambientOcclusion = 2;
    public List<String> resourcePacks = Lists.newArrayList();
    public List<String> incompatibleResourcePacks = Lists.newArrayList();
-   public EntityPlayer.EnumChatVisibility chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
+   public EnumChatVisibility chatVisibility = EnumChatVisibility.FULL;
    public boolean chatColours = true;
    public boolean chatLinks = true;
    public boolean chatLinksPrompt = true;
    public float chatOpacity = 1.0F;
    public boolean snooperEnabled = true;
    public boolean fullScreen;
@@ -146,75 +164,216 @@
       new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"),
       new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"),
       new KeyBinding("key.hotbar.9", 10, "key.categories.inventory")
    };
    public KeyBinding keyBindSaveToolbar = new KeyBinding("key.saveToolbarActivator", 46, "key.categories.creative");
    public KeyBinding keyBindLoadToolbar = new KeyBinding("key.loadToolbarActivator", 45, "key.categories.creative");
-   public KeyBinding[] keyBindings = (KeyBinding[])ArrayUtils.addAll(
-      new KeyBinding[]{
-         this.keyBindAttack,
-         this.keyBindUseItem,
-         this.keyBindForward,
-         this.keyBindLeft,
-         this.keyBindBack,
-         this.keyBindRight,
-         this.keyBindJump,
-         this.keyBindSneak,
-         this.keyBindSprint,
-         this.keyBindDrop,
-         this.keyBindInventory,
-         this.keyBindChat,
-         this.keyBindPlayerList,
-         this.keyBindPickBlock,
-         this.keyBindCommand,
-         this.keyBindScreenshot,
-         this.keyBindTogglePerspective,
-         this.keyBindSmoothCamera,
-         this.keyBindFullscreen,
-         this.keyBindSpectatorOutlines,
-         this.keyBindSwapHands,
-         this.keyBindSaveToolbar,
-         this.keyBindLoadToolbar,
-         this.keyBindAdvancements
-      },
-      this.keyBindsHotbar
-   );
+   public KeyBinding[] keyBindings;
    protected Minecraft mc;
    private File optionsFile;
-   public EnumDifficulty difficulty = EnumDifficulty.NORMAL;
+   public EnumDifficulty difficulty;
    public boolean hideGUI;
    public int thirdPersonView;
    public boolean showDebugInfo;
    public boolean showDebugProfilerChart;
    public boolean showLagometer;
-   public String lastServer = "";
+   public String lastServer;
    public boolean smoothCamera;
    public boolean debugCamEnable;
-   public float fovSetting = 70.0F;
+   public float fovSetting;
    public float gammaSetting;
    public float saturation;
    public int guiScale;
    public int particleSetting;
    public int narrator;
-   public String language = "en_us";
+   public String language;
    public boolean forceUnicodeFont;
+   public int ofFogType = 1;
+   public float ofFogStart = 0.8F;
+   public int ofMipmapType = 0;
+   public boolean ofOcclusionFancy = false;
+   public boolean ofSmoothFps = false;
+   public boolean ofSmoothWorld = Config.isSingleProcessor();
+   public boolean ofLazyChunkLoading = Config.isSingleProcessor();
+   public boolean ofRenderRegions = false;
+   public boolean ofSmartAnimations = false;
+   public float ofAoLevel = 1.0F;
+   public int ofAaLevel = 0;
+   public int ofAfLevel = 1;
+   public int ofClouds = 0;
+   public float ofCloudsHeight = 0.0F;
+   public int ofTrees = 0;
+   public int ofRain = 0;
+   public int ofDroppedItems = 0;
+   public int ofBetterGrass = 3;
+   public int ofAutoSaveTicks = 4000;
+   public boolean ofLagometer = false;
+   public boolean ofProfiler = false;
+   public boolean ofShowFps = false;
+   public boolean ofWeather = true;
+   public boolean ofSky = true;
+   public boolean ofStars = true;
+   public boolean ofSunMoon = true;
+   public int ofVignette = 0;
+   public int ofChunkUpdates = 1;
+   public boolean ofChunkUpdatesDynamic = false;
+   public int ofTime = 0;
+   public boolean ofClearWater = false;
+   public boolean ofBetterSnow = false;
+   public String ofFullscreenMode = "Default";
+   public boolean ofSwampColors = true;
+   public boolean ofRandomEntities = true;
+   public boolean ofSmoothBiomes = true;
+   public boolean ofCustomFonts = true;
+   public boolean ofCustomColors = true;
+   public boolean ofCustomSky = true;
+   public boolean ofShowCapes = true;
+   public int ofConnectedTextures = 2;
+   public boolean ofCustomItems = true;
+   public boolean ofNaturalTextures = false;
+   public boolean ofEmissiveTextures = true;
+   public boolean ofFastMath = false;
+   public boolean ofFastRender = false;
+   public int ofTranslucentBlocks = 0;
+   public boolean ofDynamicFov = true;
+   public boolean ofAlternateBlocks = true;
+   public int ofDynamicLights = 3;
+   public boolean ofCustomEntityModels = true;
+   public boolean ofCustomGuis = true;
+   public boolean ofShowGlErrors = true;
+   public int ofScreenshotSize = 1;
+   public int ofAnimatedWater = 0;
+   public int ofAnimatedLava = 0;
+   public boolean ofAnimatedFire = true;
+   public boolean ofAnimatedPortal = true;
+   public boolean ofAnimatedRedstone = true;
+   public boolean ofAnimatedExplosion = true;
+   public boolean ofAnimatedFlame = true;
+   public boolean ofAnimatedSmoke = true;
+   public boolean ofVoidParticles = true;
+   public boolean ofWaterParticles = true;
+   public boolean ofRainSplash = true;
+   public boolean ofPortalParticles = true;
+   public boolean ofPotionParticles = true;
+   public boolean ofFireworkParticles = true;
+   public boolean ofDrippingWaterLava = true;
+   public boolean ofAnimatedTerrain = true;
+   public boolean ofAnimatedTextures = true;
+   public static final int DEFAULT = 0;
+   public static final int FAST = 1;
+   public static final int FANCY = 2;
+   public static final int OFF = 3;
+   public static final int SMART = 4;
+   public static final int ANIM_ON = 0;
+   public static final int ANIM_GENERATED = 1;
+   public static final int ANIM_OFF = 2;
+   public static final String DEFAULT_STR = "Default";
+   private static final int[] OF_TREES_VALUES = new int[]{0, 1, 4, 2};
+   private static final int[] OF_DYNAMIC_LIGHTS = new int[]{3, 1, 2};
+   private static final String[] KEYS_DYNAMIC_LIGHTS = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
+   public KeyBinding ofKeyBindZoom;
+   private File optionsFileOF;
+   private boolean needsResourceRefresh = false;

    public GameSettings(Minecraft var1, File var2) {
+      this.setForgeKeybindProperties();
+      this.keyBindings = (KeyBinding[])ArrayUtils.addAll(
+         new KeyBinding[]{
+            this.keyBindAttack,
+            this.keyBindUseItem,
+            this.keyBindForward,
+            this.keyBindLeft,
+            this.keyBindBack,
+            this.keyBindRight,
+            this.keyBindJump,
+            this.keyBindSneak,
+            this.keyBindSprint,
+            this.keyBindDrop,
+            this.keyBindInventory,
+            this.keyBindChat,
+            this.keyBindPlayerList,
+            this.keyBindPickBlock,
+            this.keyBindCommand,
+            this.keyBindScreenshot,
+            this.keyBindTogglePerspective,
+            this.keyBindSmoothCamera,
+            this.keyBindFullscreen,
+            this.keyBindSpectatorOutlines,
+            this.keyBindSwapHands,
+            this.keyBindSaveToolbar,
+            this.keyBindLoadToolbar,
+            this.keyBindAdvancements
+         },
+         this.keyBindsHotbar
+      );
+      this.difficulty = EnumDifficulty.NORMAL;
+      this.lastServer = "";
+      this.fovSetting = 70.0F;
+      this.language = "en_us";
       this.mc = var1;
       this.optionsFile = new File(var2, "options.txt");
       if (var1.isJava64bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) {
          GameSettings.Options.RENDER_DISTANCE.setValueMax(32.0F);
+         long var3 = 1000000L;
+         if (Runtime.getRuntime().maxMemory() >= 1500L * var3) {
+            GameSettings.Options.RENDER_DISTANCE.setValueMax(48.0F);
+         }
+
+         if (Runtime.getRuntime().maxMemory() >= 2500L * var3) {
+            GameSettings.Options.RENDER_DISTANCE.setValueMax(64.0F);
+         }
       } else {
          GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0F);
       }

       this.renderDistanceChunks = var1.isJava64bit() ? 12 : 8;
+      this.optionsFileOF = new File(var2, "optionsof.txt");
+      this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
+      this.ofKeyBindZoom = new KeyBinding("of.key.zoom", 46, "key.categories.misc");
+      this.keyBindings = (KeyBinding[])ArrayUtils.add(this.keyBindings, this.ofKeyBindZoom);
+      KeyUtils.fixKeyConflicts(this.keyBindings, new KeyBinding[]{this.ofKeyBindZoom});
+      this.renderDistanceChunks = 8;
       this.loadOptions();
+      Config.initGameSettings(this);
    }

    public GameSettings() {
+      this.setForgeKeybindProperties();
+      this.keyBindings = (KeyBinding[])ArrayUtils.addAll(
+         new KeyBinding[]{
+            this.keyBindAttack,
+            this.keyBindUseItem,
+            this.keyBindForward,
+            this.keyBindLeft,
+            this.keyBindBack,
+            this.keyBindRight,
+            this.keyBindJump,
+            this.keyBindSneak,
+            this.keyBindSprint,
+            this.keyBindDrop,
+            this.keyBindInventory,
+            this.keyBindChat,
+            this.keyBindPlayerList,
+            this.keyBindPickBlock,
+            this.keyBindCommand,
+            this.keyBindScreenshot,
+            this.keyBindTogglePerspective,
+            this.keyBindSmoothCamera,
+            this.keyBindFullscreen,
+            this.keyBindSpectatorOutlines,
+            this.keyBindSwapHands,
+            this.keyBindSaveToolbar,
+            this.keyBindLoadToolbar,
+            this.keyBindAdvancements
+         },
+         this.keyBindsHotbar
+      );
+      this.difficulty = EnumDifficulty.NORMAL;
+      this.lastServer = "";
+      this.fovSetting = 70.0F;
+      this.language = "en_us";
    }

    public static String getKeyDisplayString(int var0) {
       if (var0 < 0) {
          switch (var0) {
             case -100:
@@ -230,25 +389,26 @@
          return var0 < 256 ? Keyboard.getKeyName(var0) : String.format("%c", (char)(var0 - 256)).toUpperCase();
       }
    }

    public static boolean isKeyDown(KeyBinding var0) {
       int var1 = var0.getKeyCode();
-      if (var1 == 0 || var1 >= 256) {
-         return false;
-      } else {
+      if (var1 != 0 && var1 < 256) {
          return var1 < 0 ? Mouse.isButtonDown(var1 + 100) : Keyboard.isKeyDown(var1);
+      } else {
+         return false;
       }
    }

    public void setOptionKeyBinding(KeyBinding var1, int var2) {
       var1.setKeyCode(var2);
       this.saveOptions();
    }

    public void setOptionFloatValue(GameSettings.Options var1, float var2) {
+      this.setOptionFloatValueOF(var1, var2);
       if (var1 == GameSettings.Options.SENSITIVITY) {
          this.mouseSensitivity = var2;
       }

       if (var1 == GameSettings.Options.FOV) {
          this.fovSetting = var2;
@@ -257,12 +417,19 @@
       if (var1 == GameSettings.Options.GAMMA) {
          this.gammaSetting = var2;
       }

       if (var1 == GameSettings.Options.FRAMERATE_LIMIT) {
          this.limitFramerate = (int)var2;
+         this.enableVsync = false;
+         if (this.limitFramerate <= 0) {
+            this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
+            this.enableVsync = true;
+         }
+
+         this.updateVSync();
       }

       if (var1 == GameSettings.Options.CHAT_OPACITY) {
          this.chatOpacity = var2;
          this.mc.ingameGUI.getChatGUI().refreshChat();
       }
@@ -302,12 +469,13 @@
          this.renderDistanceChunks = (int)var2;
          this.mc.renderGlobal.setDisplayListEntitiesDirty();
       }
    }

    public void setOptionValue(GameSettings.Options var1, int var2) {
+      this.setOptionValueOF(var1, var2);
       if (var1 == GameSettings.Options.RENDER_DISTANCE) {
          this.setOptionFloatValue(var1, MathHelper.clamp((float)(this.renderDistanceChunks + var2), var1.getValueMin(), var1.getValueMax()));
       }

       if (var1 == GameSettings.Options.MAIN_HAND) {
          this.mainHand = this.mainHand.opposite();
@@ -315,13 +483,32 @@

       if (var1 == GameSettings.Options.INVERT_MOUSE) {
          this.invertMouse = !this.invertMouse;
       }

       if (var1 == GameSettings.Options.GUI_SCALE) {
-         this.guiScale = this.guiScale + var2 & 3;
+         this.guiScale += var2;
+         if (GuiScreen.isShiftKeyDown()) {
+            this.guiScale = 0;
+         }
+
+         DisplayMode var3 = Config.getLargestDisplayMode();
+         int var4 = var3.getWidth() / 320;
+         int var5 = var3.getHeight() / 240;
+         int var6 = Math.min(var4, var5);
+         if (this.guiScale < 0) {
+            this.guiScale = var6 - 1;
+         }
+
+         if (this.mc.isUnicode() && this.guiScale % 2 != 0) {
+            this.guiScale += var2;
+         }
+
+         if (this.guiScale < 0 || this.guiScale >= var6) {
+            this.guiScale = 0;
+         }
       }

       if (var1 == GameSettings.Options.PARTICLES) {
          this.particleSetting = (this.particleSetting + var2) % 3;
       }

@@ -340,28 +527,39 @@

       if (var1 == GameSettings.Options.FBO_ENABLE) {
          this.fboEnable = !this.fboEnable;
       }

       if (var1 == GameSettings.Options.ANAGLYPH) {
+         if (!this.anaglyph && Config.isShaders()) {
+            Config.showGuiMessage(Lang.get("of.message.an.shaders1"), Lang.get("of.message.an.shaders2"));
+            return;
+         }
+
          this.anaglyph = !this.anaglyph;
          this.mc.refreshResources();
+         if (Reflector.FMLClientHandler_refreshResources.exists()) {
+            Object var7 = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
+            IResourceType var8 = (IResourceType)Reflector.VanillaResourceType_TEXTURES.getValue();
+            Reflector.call(var7, Reflector.FMLClientHandler_refreshResources, new IResourceType[]{var8});
+         }
       }

       if (var1 == GameSettings.Options.GRAPHICS) {
          this.fancyGraphics = !this.fancyGraphics;
+         this.updateRenderClouds();
          this.mc.renderGlobal.loadRenderers();
       }

       if (var1 == GameSettings.Options.AMBIENT_OCCLUSION) {
          this.ambientOcclusion = (this.ambientOcclusion + var2) % 3;
          this.mc.renderGlobal.loadRenderers();
       }

       if (var1 == GameSettings.Options.CHAT_VISIBILITY) {
-         this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + var2) % 3);
+         this.chatVisibility = EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + var2) % 3);
       }

       if (var1 == GameSettings.Options.CHAT_COLOR) {
          this.chatColours = !this.chatColours;
       }

@@ -433,13 +631,16 @@
       }

       this.saveOptions();
    }

    public float getOptionFloatValue(GameSettings.Options var1) {
-      if (var1 == GameSettings.Options.FOV) {
+      float var2 = this.getOptionFloatValueOF(var1);
+      if (var2 != Float.MAX_VALUE) {
+         return var2;
+      } else if (var1 == GameSettings.Options.FOV) {
          return this.fovSetting;
       } else if (var1 == GameSettings.Options.GAMMA) {
          return this.gammaSetting;
       } else if (var1 == GameSettings.Options.SATURATION) {
          return this.saturation;
       } else if (var1 == GameSettings.Options.SENSITIVITY) {
@@ -514,552 +715,2052 @@
       }

       return I18n.format(var0[var1]);
    }

    public String getKeyBinding(GameSettings.Options var1) {
-      Object var2 = I18n.format(var1.getTranslation()) + ": ";
-      if (var1.isFloat()) {
-         float var6 = this.getOptionFloatValue(var1);
-         float var4 = var1.normalizeValue(var6);
-         if (var1 == GameSettings.Options.SENSITIVITY) {
-            if (var4 == 0.0F) {
-               return var2 + I18n.format("options.sensitivity.min");
-            } else {
-               return var4 == 1.0F ? var2 + I18n.format("options.sensitivity.max") : var2 + (int)(var4 * 200.0F) + "%";
-            }
-         } else if (var1 == GameSettings.Options.FOV) {
-            if (var6 == 70.0F) {
-               return var2 + I18n.format("options.fov.min");
+      String var2 = this.getKeyBindingOF(var1);
+      if (var2 != null) {
+         return var2;
+      } else {
+         Object var3 = I18n.format(var1.getTranslation()) + ": ";
+         if (var1.isFloat()) {
+            float var7 = this.getOptionFloatValue(var1);
+            float var5 = var1.normalizeValue(var7);
+            if (var1 == GameSettings.Options.SENSITIVITY) {
+               if (var5 == 0.0F) {
+                  return var3 + I18n.format("options.sensitivity.min");
+               } else {
+                  return var5 == 1.0F ? var3 + I18n.format("options.sensitivity.max") : var3 + (int)(var5 * 200.0F) + "%";
+               }
+            } else if (var1 == GameSettings.Options.FOV) {
+               if (var7 == 70.0F) {
+                  return var3 + I18n.format("options.fov.min");
+               } else {
+                  return var7 == 110.0F ? var3 + I18n.format("options.fov.max") : var3 + (int)var7;
+               }
+            } else if (var1 == GameSettings.Options.FRAMERATE_LIMIT) {
+               return var7 == var1.valueMax ? var3 + I18n.format("options.framerateLimit.max") : var3 + I18n.format("options.framerate", (int)var7);
+            } else if (var1 == GameSettings.Options.RENDER_CLOUDS) {
+               return var7 == var1.valueMin ? var3 + I18n.format("options.cloudHeight.min") : var3 + ((int)var7 + 128);
+            } else if (var1 == GameSettings.Options.GAMMA) {
+               if (var5 == 0.0F) {
+                  return var3 + I18n.format("options.gamma.min");
+               } else {
+                  return var5 == 1.0F ? var3 + I18n.format("options.gamma.max") : var3 + "+" + (int)(var5 * 100.0F) + "%";
+               }
+            } else if (var1 == GameSettings.Options.SATURATION) {
+               return var3 + (int)(var5 * 400.0F) + "%";
+            } else if (var1 == GameSettings.Options.CHAT_OPACITY) {
+               return var3 + (int)(var5 * 90.0F + 10.0F) + "%";
+            } else if (var1 == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
+               return var3 + GuiNewChat.calculateChatboxHeight(var5) + "px";
+            } else if (var1 == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
+               return var3 + GuiNewChat.calculateChatboxHeight(var5) + "px";
+            } else if (var1 == GameSettings.Options.CHAT_WIDTH) {
+               return var3 + GuiNewChat.calculateChatboxWidth(var5) + "px";
+            } else if (var1 == GameSettings.Options.RENDER_DISTANCE) {
+               return var3 + I18n.format("options.chunks", (int)var7);
+            } else if (var1 == GameSettings.Options.MIPMAP_LEVELS) {
+               if (var7 >= 4.0) {
+                  return var3 + Lang.get("of.general.max");
+               } else {
+                  return var7 == 0.0F ? var3 + I18n.format("options.off") : var3 + (int)var7;
+               }
             } else {
-               return var6 == 110.0F ? var2 + I18n.format("options.fov.max") : var2 + (int)var6;
+               return var5 == 0.0F ? var3 + I18n.format("options.off") : var3 + (int)(var5 * 100.0F) + "%";
             }
-         } else if (var1 == GameSettings.Options.FRAMERATE_LIMIT) {
-            return var6 == var1.valueMax ? var2 + I18n.format("options.framerateLimit.max") : var2 + I18n.format("options.framerate", (int)var6);
+         } else if (var1.isBoolean()) {
+            boolean var6 = this.getOptionOrdinalValue(var1);
+            return var6 ? var3 + I18n.format("options.on") : var3 + I18n.format("options.off");
+         } else if (var1 == GameSettings.Options.MAIN_HAND) {
+            return var3 + this.mainHand;
+         } else if (var1 == GameSettings.Options.GUI_SCALE) {
+            return this.guiScale >= GUISCALES.length ? var3 + this.guiScale + "x" : var3 + getTranslation(GUISCALES, this.guiScale);
+         } else if (var1 == GameSettings.Options.CHAT_VISIBILITY) {
+            return var3 + I18n.format(this.chatVisibility.getResourceKey());
+         } else if (var1 == GameSettings.Options.PARTICLES) {
+            return var3 + getTranslation(PARTICLES, this.particleSetting);
+         } else if (var1 == GameSettings.Options.AMBIENT_OCCLUSION) {
+            return var3 + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
          } else if (var1 == GameSettings.Options.RENDER_CLOUDS) {
-            return var6 == var1.valueMin ? var2 + I18n.format("options.cloudHeight.min") : var2 + ((int)var6 + 128);
-         } else if (var1 == GameSettings.Options.GAMMA) {
-            if (var4 == 0.0F) {
-               return var2 + I18n.format("options.gamma.min");
+            return var3 + getTranslation(CLOUDS_TYPES, this.clouds);
+         } else if (var1 == GameSettings.Options.GRAPHICS) {
+            if (this.fancyGraphics) {
+               return var3 + I18n.format("options.graphics.fancy");
             } else {
-               return var4 == 1.0F ? var2 + I18n.format("options.gamma.max") : var2 + "+" + (int)(var4 * 100.0F) + "%";
+               String var4 = "options.graphics.fast";
+               return var3 + I18n.format("options.graphics.fast");
             }
-         } else if (var1 == GameSettings.Options.SATURATION) {
-            return var2 + (int)(var4 * 400.0F) + "%";
-         } else if (var1 == GameSettings.Options.CHAT_OPACITY) {
-            return var2 + (int)(var4 * 90.0F + 10.0F) + "%";
-         } else if (var1 == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
-            return var2 + GuiNewChat.calculateChatboxHeight(var4) + "px";
-         } else if (var1 == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
-            return var2 + GuiNewChat.calculateChatboxHeight(var4) + "px";
-         } else if (var1 == GameSettings.Options.CHAT_WIDTH) {
-            return var2 + GuiNewChat.calculateChatboxWidth(var4) + "px";
-         } else if (var1 == GameSettings.Options.RENDER_DISTANCE) {
-            return var2 + I18n.format("options.chunks", (int)var6);
-         } else if (var1 == GameSettings.Options.MIPMAP_LEVELS) {
-            return var6 == 0.0F ? var2 + I18n.format("options.off") : var2 + (int)var6;
+         } else if (var1 == GameSettings.Options.ATTACK_INDICATOR) {
+            return var3 + getTranslation(ATTACK_INDICATORS, this.attackIndicator);
+         } else if (var1 == GameSettings.Options.NARRATOR) {
+            return NarratorChatListener.INSTANCE.isActive()
+               ? var3 + getTranslation(NARRATOR_MODES, this.narrator)
+               : var3 + I18n.format("options.narrator.notavailable");
          } else {
-            return var4 == 0.0F ? var2 + I18n.format("options.off") : var2 + (int)(var4 * 100.0F) + "%";
+            return (String)var3;
          }
-      } else if (var1.isBoolean()) {
-         boolean var5 = this.getOptionOrdinalValue(var1);
-         return var5 ? var2 + I18n.format("options.on") : var2 + I18n.format("options.off");
-      } else if (var1 == GameSettings.Options.MAIN_HAND) {
-         return var2 + this.mainHand;
-      } else if (var1 == GameSettings.Options.GUI_SCALE) {
-         return var2 + getTranslation(GUISCALES, this.guiScale);
-      } else if (var1 == GameSettings.Options.CHAT_VISIBILITY) {
-         return var2 + I18n.format(this.chatVisibility.getResourceKey());
-      } else if (var1 == GameSettings.Options.PARTICLES) {
-         return var2 + getTranslation(PARTICLES, this.particleSetting);
-      } else if (var1 == GameSettings.Options.AMBIENT_OCCLUSION) {
-         return var2 + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
-      } else if (var1 == GameSettings.Options.RENDER_CLOUDS) {
-         return var2 + getTranslation(CLOUDS_TYPES, this.clouds);
-      } else if (var1 == GameSettings.Options.GRAPHICS) {
-         if (this.fancyGraphics) {
-            return var2 + I18n.format("options.graphics.fancy");
-         } else {
-            String var3 = "options.graphics.fast";
-            return var2 + I18n.format("options.graphics.fast");
+      }
+   }
+
+   public void loadOptions() {
+      FileInputStream var1 = null;
+
+      label542: {
+         try {
+            if (this.optionsFile.exists()) {
+               this.soundLevels.clear();
+               List var2 = IOUtils.readLines(var1 = new FileInputStream(this.optionsFile), StandardCharsets.UTF_8);
+               NBTTagCompound var3 = new NBTTagCompound();
+
+               for (String var5 : var2) {
+                  try {
+                     Iterator var6 = COLON_SPLITTER.omitEmptyStrings().limit(2).split(var5).iterator();
+                     var3.setString((String)var6.next(), (String)var6.next());
+                  } catch (Exception var18) {
+                     LOGGER.warn("Skipping bad option: {}", var5);
+                  }
+               }
+
+               var3 = this.dataFix(var3);
+
+               for (String var24 : var3.getKeySet()) {
+                  String var25 = var3.getString(var24);
+
+                  try {
+                     if ("mouseSensitivity".equals(var24)) {
+                        this.mouseSensitivity = this.parseFloat(var25);
+                     }
+
+                     if ("fov".equals(var24)) {
+                        this.fovSetting = this.parseFloat(var25) * 40.0F + 70.0F;
+                     }
+
+                     if ("gamma".equals(var24)) {
+                        this.gammaSetting = this.parseFloat(var25);
+                     }
+
+                     if ("saturation".equals(var24)) {
+                        this.saturation = this.parseFloat(var25);
+                     }
+
+                     if ("invertYMouse".equals(var24)) {
+                        this.invertMouse = "true".equals(var25);
+                     }
+
+                     if ("renderDistance".equals(var24)) {
+                        this.renderDistanceChunks = Integer.parseInt(var25);
+                     }
+
+                     if ("guiScale".equals(var24)) {
+                        this.guiScale = Integer.parseInt(var25);
+                     }
+
+                     if ("particles".equals(var24)) {
+                        this.particleSetting = Integer.parseInt(var25);
+                     }
+
+                     if ("bobView".equals(var24)) {
+                        this.viewBobbing = "true".equals(var25);
+                     }
+
+                     if ("anaglyph3d".equals(var24)) {
+                        this.anaglyph = "true".equals(var25);
+                     }
+
+                     if ("maxFps".equals(var24)) {
+                        this.limitFramerate = Integer.parseInt(var25);
+                        if (this.enableVsync) {
+                           this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
+                        }
+
+                        if (this.limitFramerate <= 0) {
+                           this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
+                        }
+                     }
+
+                     if ("fboEnable".equals(var24)) {
+                        this.fboEnable = "true".equals(var25);
+                     }
+
+                     if ("difficulty".equals(var24)) {
+                        this.difficulty = EnumDifficulty.byId(Integer.parseInt(var25));
+                     }
+
+                     if ("fancyGraphics".equals(var24)) {
+                        this.fancyGraphics = "true".equals(var25);
+                        this.updateRenderClouds();
+                     }
+
+                     if ("tutorialStep".equals(var24)) {
+                        this.tutorialStep = TutorialSteps.getTutorial(var25);
+                     }
+
+                     if ("ao".equals(var24)) {
+                        if ("true".equals(var25)) {
+                           this.ambientOcclusion = 2;
+                        } else if ("false".equals(var25)) {
+                           this.ambientOcclusion = 0;
+                        } else {
+                           this.ambientOcclusion = Integer.parseInt(var25);
+                        }
+                     }
+
+                     if ("renderClouds".equals(var24)) {
+                        if ("true".equals(var25)) {
+                           this.clouds = 2;
+                        } else if ("false".equals(var25)) {
+                           this.clouds = 0;
+                        } else if ("fast".equals(var25)) {
+                           this.clouds = 1;
+                        }
+                     }
+
+                     if ("attackIndicator".equals(var24)) {
+                        if ("0".equals(var25)) {
+                           this.attackIndicator = 0;
+                        } else if ("1".equals(var25)) {
+                           this.attackIndicator = 1;
+                        } else if ("2".equals(var25)) {
+                           this.attackIndicator = 2;
+                        }
+                     }
+
+                     if ("resourcePacks".equals(var24)) {
+                        this.resourcePacks = (List<String>)JsonUtils.gsonDeserialize(GSON, var25, TYPE_LIST_STRING);
+                        if (this.resourcePacks == null) {
+                           this.resourcePacks = Lists.newArrayList();
+                        }
+                     }
+
+                     if ("incompatibleResourcePacks".equals(var24)) {
+                        this.incompatibleResourcePacks = (List<String>)JsonUtils.gsonDeserialize(GSON, var25, TYPE_LIST_STRING);
+                        if (this.incompatibleResourcePacks == null) {
+                           this.incompatibleResourcePacks = Lists.newArrayList();
+                        }
+                     }
+
+                     if ("lastServer".equals(var24)) {
+                        this.lastServer = var25;
+                     }
+
+                     if ("lang".equals(var24)) {
+                        this.language = var25;
+                     }
+
+                     if ("chatVisibility".equals(var24)) {
+                        this.chatVisibility = EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(var25));
+                     }
+
+                     if ("chatColors".equals(var24)) {
+                        this.chatColours = "true".equals(var25);
+                     }
+
+                     if ("chatLinks".equals(var24)) {
+                        this.chatLinks = "true".equals(var25);
+                     }
+
+                     if ("chatLinksPrompt".equals(var24)) {
+                        this.chatLinksPrompt = "true".equals(var25);
+                     }
+
+                     if ("chatOpacity".equals(var24)) {
+                        this.chatOpacity = this.parseFloat(var25);
+                     }
+
+                     if ("snooperEnabled".equals(var24)) {
+                        this.snooperEnabled = "true".equals(var25);
+                     }
+
+                     if ("fullscreen".equals(var24)) {
+                        this.fullScreen = "true".equals(var25);
+                     }
+
+                     if ("enableVsync".equals(var24)) {
+                        this.enableVsync = "true".equals(var25);
+                        if (this.enableVsync) {
+                           this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
+                        }
+
+                        this.updateVSync();
+                     }
+
+                     if ("useVbo".equals(var24)) {
+                        this.useVbo = "true".equals(var25);
+                     }
+
+                     if ("hideServerAddress".equals(var24)) {
+                        this.hideServerAddress = "true".equals(var25);
+                     }
+
+                     if ("advancedItemTooltips".equals(var24)) {
+                        this.advancedItemTooltips = "true".equals(var25);
+                     }
+
+                     if ("pauseOnLostFocus".equals(var24)) {
+                        this.pauseOnLostFocus = "true".equals(var25);
+                     }
+
+                     if ("touchscreen".equals(var24)) {
+                        this.touchscreen = "true".equals(var25);
+                     }
+
+                     if ("overrideHeight".equals(var24)) {
+                        this.overrideHeight = Integer.parseInt(var25);
+                     }
+
+                     if ("overrideWidth".equals(var24)) {
+                        this.overrideWidth = Integer.parseInt(var25);
+                     }
+
+                     if ("heldItemTooltips".equals(var24)) {
+                        this.heldItemTooltips = "true".equals(var25);
+                     }
+
+                     if ("chatHeightFocused".equals(var24)) {
+                        this.chatHeightFocused = this.parseFloat(var25);
+                     }
+
+                     if ("chatHeightUnfocused".equals(var24)) {
+                        this.chatHeightUnfocused = this.parseFloat(var25);
+                     }
+
+                     if ("chatScale".equals(var24)) {
+                        this.chatScale = this.parseFloat(var25);
+                     }
+
+                     if ("chatWidth".equals(var24)) {
+                        this.chatWidth = this.parseFloat(var25);
+                     }
+
+                     if ("mipmapLevels".equals(var24)) {
+                        this.mipmapLevels = Integer.parseInt(var25);
+                     }
+
+                     if ("forceUnicodeFont".equals(var24)) {
+                        this.forceUnicodeFont = "true".equals(var25);
+                     }
+
+                     if ("reducedDebugInfo".equals(var24)) {
+                        this.reducedDebugInfo = "true".equals(var25);
+                     }
+
+                     if ("useNativeTransport".equals(var24)) {
+                        this.useNativeTransport = "true".equals(var25);
+                     }
+
+                     if ("entityShadows".equals(var24)) {
+                        this.entityShadows = "true".equals(var25);
+                     }
+
+                     if ("mainHand".equals(var24)) {
+                        this.mainHand = "left".equals(var25) ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
+                     }
+
+                     if ("showSubtitles".equals(var24)) {
+                        this.showSubtitles = "true".equals(var25);
+                     }
+
+                     if ("realmsNotifications".equals(var24)) {
+                        this.realmsNotifications = "true".equals(var25);
+                     }
+
+                     if ("enableWeakAttacks".equals(var24)) {
+                        this.enableWeakAttacks = "true".equals(var25);
+                     }
+
+                     if ("autoJump".equals(var24)) {
+                        this.autoJump = "true".equals(var25);
+                     }
+
+                     if ("narrator".equals(var24)) {
+                        this.narrator = Integer.parseInt(var25);
+                     }
+
+                     for (KeyBinding var10 : this.keyBindings) {
+                        if (var24.equals("key_" + var10.getKeyDescription())) {
+                           if (Reflector.KeyModifier_valueFromString.exists()) {
+                              if (var25.indexOf(58) != -1) {
+                                 String[] var11 = var25.split(":");
+                                 Object var12 = Reflector.call(Reflector.KeyModifier_valueFromString, new Object[]{var11[1]});
+                                 Reflector.call(var10, Reflector.ForgeKeyBinding_setKeyModifierAndCode, new Object[]{var12, Integer.parseInt(var11[0])});
+                              } else {
+                                 Object var34 = Reflector.getFieldValue(Reflector.KeyModifier_NONE);
+                                 Reflector.call(var10, Reflector.ForgeKeyBinding_setKeyModifierAndCode, new Object[]{var34, Integer.parseInt(var25)});
+                              }
+                           } else {
+                              var10.setKeyCode(Integer.parseInt(var25));
+                           }
+                        }
+                     }
+
+                     for (SoundCategory var32 : SoundCategory.values()) {
+                        if (var24.equals("soundCategory_" + var32.getName())) {
+                           this.soundLevels.put(var32, this.parseFloat(var25));
+                        }
+                     }
+
+                     for (EnumPlayerModelParts var33 : EnumPlayerModelParts.values()) {
+                        if (var24.equals("modelPart_" + var33.getPartName())) {
+                           this.setModelPartEnabled(var33, "true".equals(var25));
+                        }
+                     }
+                  } catch (Exception var19) {
+                     LOGGER.warn("Skipping bad option: {}:{}", var24, var25);
+                     var19.printStackTrace();
+                  }
+               }
+
+               KeyBinding.resetKeyBindingArrayAndHash();
+               break label542;
+            }
+         } catch (Exception var20) {
+            LOGGER.error("Failed to load options", var20);
+            break label542;
+         } finally {
+            IOUtils.closeQuietly(var1);
          }
-      } else if (var1 == GameSettings.Options.ATTACK_INDICATOR) {
-         return var2 + getTranslation(ATTACK_INDICATORS, this.attackIndicator);
-      } else if (var1 == GameSettings.Options.NARRATOR) {
-         return NarratorChatListener.INSTANCE.isActive()
-            ? var2 + getTranslation(NARRATOR_MODES, this.narrator)
-            : var2 + I18n.format("options.narrator.notavailable");
+
+         return;
+      }
+
+      this.loadOfOptions();
+   }
+
+   private NBTTagCompound dataFix(NBTTagCompound var1) {
+      int var2 = 0;
+
+      try {
+         var2 = Integer.parseInt(var1.getString("version"));
+      } catch (RuntimeException var4) {
+      }
+
+      return this.mc.getDataFixer().process(FixTypes.OPTIONS, var1, var2);
+   }
+
+   private float parseFloat(String var1) {
+      if ("true".equals(var1)) {
+         return 1.0F;
       } else {
-         return (String)var2;
+         return "false".equals(var1) ? 0.0F : Float.parseFloat(var1);
       }
    }

-   public void loadOptions() {
+   public void saveOptions() {
+      if (Reflector.FMLClientHandler.exists()) {
+         Object var1 = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
+         if (var1 != null && Reflector.callBoolean(var1, Reflector.FMLClientHandler_isLoading, new Object[0])) {
+            return;
+         }
+      }
+
+      PrintWriter var14 = null;
+
       try {
-         if (!this.optionsFile.exists()) {
+         var14 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8));
+         var14.println("version:1343");
+         var14.println("invertYMouse:" + this.invertMouse);
+         var14.println("mouseSensitivity:" + this.mouseSensitivity);
+         var14.println("fov:" + (this.fovSetting - 70.0F) / 40.0F);
+         var14.println("gamma:" + this.gammaSetting);
+         var14.println("saturation:" + this.saturation);
+         var14.println("renderDistance:" + this.renderDistanceChunks);
+         var14.println("guiScale:" + this.guiScale);
+         var14.println("particles:" + this.particleSetting);
+         var14.println("bobView:" + this.viewBobbing);
+         var14.println("anaglyph3d:" + this.anaglyph);
+         var14.println("maxFps:" + this.limitFramerate);
+         var14.println("fboEnable:" + this.fboEnable);
+         var14.println("difficulty:" + this.difficulty.getId());
+         var14.println("fancyGraphics:" + this.fancyGraphics);
+         var14.println("ao:" + this.ambientOcclusion);
+         switch (this.clouds) {
+            case 0:
+               var14.println("renderClouds:false");
+               break;
+            case 1:
+               var14.println("renderClouds:fast");
+               break;
+            case 2:
+               var14.println("renderClouds:true");
+         }
+
+         var14.println("resourcePacks:" + GSON.toJson(this.resourcePacks));
+         var14.println("incompatibleResourcePacks:" + GSON.toJson(this.incompatibleResourcePacks));
+         var14.println("lastServer:" + this.lastServer);
+         var14.println("lang:" + this.language);
+         var14.println("chatVisibility:" + this.chatVisibility.getChatVisibility());
+         var14.println("chatColors:" + this.chatColours);
+         var14.println("chatLinks:" + this.chatLinks);
+         var14.println("chatLinksPrompt:" + this.chatLinksPrompt);
+         var14.println("chatOpacity:" + this.chatOpacity);
+         var14.println("snooperEnabled:" + this.snooperEnabled);
+         var14.println("fullscreen:" + this.fullScreen);
+         var14.println("enableVsync:" + this.enableVsync);
+         var14.println("useVbo:" + this.useVbo);
+         var14.println("hideServerAddress:" + this.hideServerAddress);
+         var14.println("advancedItemTooltips:" + this.advancedItemTooltips);
+         var14.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
+         var14.println("touchscreen:" + this.touchscreen);
+         var14.println("overrideWidth:" + this.overrideWidth);
+         var14.println("overrideHeight:" + this.overrideHeight);
+         var14.println("heldItemTooltips:" + this.heldItemTooltips);
+         var14.println("chatHeightFocused:" + this.chatHeightFocused);
+         var14.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
+         var14.println("chatScale:" + this.chatScale);
+         var14.println("chatWidth:" + this.chatWidth);
+         var14.println("mipmapLevels:" + this.mipmapLevels);
+         var14.println("forceUnicodeFont:" + this.forceUnicodeFont);
+         var14.println("reducedDebugInfo:" + this.reducedDebugInfo);
+         var14.println("useNativeTransport:" + this.useNativeTransport);
+         var14.println("entityShadows:" + this.entityShadows);
+         var14.println("mainHand:" + (this.mainHand == EnumHandSide.LEFT ? "left" : "right"));
+         var14.println("attackIndicator:" + this.attackIndicator);
+         var14.println("showSubtitles:" + this.showSubtitles);
+         var14.println("realmsNotifications:" + this.realmsNotifications);
+         var14.println("enableWeakAttacks:" + this.enableWeakAttacks);
+         var14.println("autoJump:" + this.autoJump);
+         var14.println("narrator:" + this.narrator);
+         var14.println("tutorialStep:" + this.tutorialStep.getName());
+
+         for (KeyBinding var5 : this.keyBindings) {
+            if (Reflector.ForgeKeyBinding_getKeyModifier.exists()) {
+               String var6 = "key_" + var5.getKeyDescription() + ":" + var5.getKeyCode();
+               Object var7 = Reflector.call(var5, Reflector.ForgeKeyBinding_getKeyModifier, new Object[0]);
+               Object var8 = Reflector.getFieldValue(Reflector.KeyModifier_NONE);
+               var14.println(var7 != var8 ? var6 + ":" + var7 : var6);
+            } else {
+               var14.println("key_" + var5.getKeyDescription() + ":" + var5.getKeyCode());
+            }
+         }
+
+         for (SoundCategory var21 : SoundCategory.values()) {
+            var14.println("soundCategory_" + var21.getName() + ":" + this.getSoundLevel(var21));
+         }
+
+         for (EnumPlayerModelParts var22 : EnumPlayerModelParts.values()) {
+            var14.println("modelPart_" + var22.getPartName() + ":" + this.setModelParts.contains(var22));
+         }
+      } catch (Exception var12) {
+         LOGGER.error("Failed to save options", var12);
+      } finally {
+         IOUtils.closeQuietly(var14);
+      }
+
+      this.saveOfOptions();
+      this.sendSettingsToServer();
+   }
+
+   public float getSoundLevel(SoundCategory var1) {
+      return this.soundLevels.containsKey(var1) ? this.soundLevels.get(var1) : 1.0F;
+   }
+
+   public void setSoundLevel(SoundCategory var1, float var2) {
+      this.mc.getSoundHandler().setSoundLevel(var1, var2);
+      this.soundLevels.put(var1, var2);
+   }
+
+   public void sendSettingsToServer() {
+      if (this.mc.player != null) {
+         int var1 = 0;
+
+         for (EnumPlayerModelParts var3 : this.setModelParts) {
+            var1 |= var3.getPartMask();
+         }
+
+         this.mc
+            .player
+            .connection
+            .sendPacket(new CPacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, var1, this.mainHand));
+      }
+   }
+
+   public Set<EnumPlayerModelParts> getModelParts() {
+      return ImmutableSet.copyOf(this.setModelParts);
+   }
+
+   public void setModelPartEnabled(EnumPlayerModelParts var1, boolean var2) {
+      if (var2) {
+         this.setModelParts.add(var1);
+      } else {
+         this.setModelParts.remove(var1);
+      }
+
+      this.sendSettingsToServer();
+   }
+
+   public void switchModelPartEnabled(EnumPlayerModelParts var1) {
+      if (this.getModelParts().contains(var1)) {
+         this.setModelParts.remove(var1);
+      } else {
+         this.setModelParts.add(var1);
+      }
+
+      this.sendSettingsToServer();
+   }
+
+   public int shouldRenderClouds() {
+      return this.renderDistanceChunks >= 4 ? this.clouds : 0;
+   }
+
+   public boolean isUsingNativeTransport() {
+      return this.useNativeTransport;
+   }
+
+   private void setOptionFloatValueOF(GameSettings.Options var1, float var2) {
+      if (var1 == GameSettings.Options.CLOUD_HEIGHT) {
+         this.ofCloudsHeight = var2;
+         this.mc.renderGlobal.resetClouds();
+      }
+
+      if (var1 == GameSettings.Options.AO_LEVEL) {
+         this.ofAoLevel = var2;
+         this.mc.renderGlobal.loadRenderers();
+      }
+
+      if (var1 == GameSettings.Options.AA_LEVEL) {
+         int var3 = (int)var2;
+         if (var3 > 0 && Config.isShaders()) {
+            Config.showGuiMessage(Lang.get("of.message.aa.shaders1"), Lang.get("of.message.aa.shaders2"));
             return;
          }

-         this.soundLevels.clear();
-         List var1 = IOUtils.readLines(new FileInputStream(this.optionsFile));
-         NBTTagCompound var2 = new NBTTagCompound();
+         int[] var4 = new int[]{0, 2, 4, 6, 8, 12, 16};
+         this.ofAaLevel = 0;

-         for (String var4 : var1) {
-            try {
-               Iterator var5 = COLON_SPLITTER.omitEmptyStrings().limit(2).split(var4).iterator();
-               var2.setString((String)var5.next(), (String)var5.next());
-            } catch (Exception var10) {
-               LOGGER.warn("Skipping bad option: {}", var4);
+         for (int var5 = 0; var5 < var4.length; var5++) {
+            if (var3 >= var4[var5]) {
+               this.ofAaLevel = var4[var5];
             }
          }

-         var2 = this.dataFix(var2);
+         this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
+      }
+
+      if (var1 == GameSettings.Options.AF_LEVEL) {
+         int var6 = (int)var2;
+         if (var6 > 1 && Config.isShaders()) {
+            Config.showGuiMessage(Lang.get("of.message.af.shaders1"), Lang.get("of.message.af.shaders2"));
+            return;
+         }
+
+         this.ofAfLevel = 1;
+
+         while (this.ofAfLevel * 2 <= var6) {
+            this.ofAfLevel *= 2;
+         }
+
+         this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
+         this.mc.refreshResources();
+      }
+
+      if (var1 == GameSettings.Options.MIPMAP_TYPE) {
+         int var7 = (int)var2;
+         this.ofMipmapType = Config.limit(var7, 0, 3);
+         this.mc.refreshResources();
+      }
+
+      if (var1 == GameSettings.Options.FULLSCREEN_MODE) {
+         int var8 = (int)var2 - 1;
+         String[] var9 = Config.getDisplayModeNames();
+         if (var8 < 0 || var8 >= var9.length) {
+            this.ofFullscreenMode = "Default";
+            return;
+         }
+
+         this.ofFullscreenMode = var9[var8];
+      }
+   }
+
+   private float getOptionFloatValueOF(GameSettings.Options var1) {
+      if (var1 == GameSettings.Options.CLOUD_HEIGHT) {
+         return this.ofCloudsHeight;
+      } else if (var1 == GameSettings.Options.AO_LEVEL) {
+         return this.ofAoLevel;
+      } else if (var1 == GameSettings.Options.AA_LEVEL) {
+         return this.ofAaLevel;
+      } else if (var1 == GameSettings.Options.AF_LEVEL) {
+         return this.ofAfLevel;
+      } else if (var1 == GameSettings.Options.MIPMAP_TYPE) {
+         return this.ofMipmapType;
+      } else if (var1 == GameSettings.Options.FRAMERATE_LIMIT) {
+         return this.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() && this.enableVsync ? 0.0F : this.limitFramerate;
+      } else if (var1 == GameSettings.Options.FULLSCREEN_MODE) {
+         if (this.ofFullscreenMode.equals("Default")) {
+            return 0.0F;
+         } else {
+            List var2 = Arrays.asList(Config.getDisplayModeNames());
+            int var3 = var2.indexOf(this.ofFullscreenMode);
+            return var3 < 0 ? 0.0F : var3 + 1;
+         }
+      } else {
+         return Float.MAX_VALUE;
+      }
+   }
+
+   private void setOptionValueOF(GameSettings.Options var1, int var2) {
+      if (var1 == GameSettings.Options.FOG_FANCY) {
+         switch (this.ofFogType) {
+            case 1:
+               this.ofFogType = 2;
+               if (!Config.isFancyFogAvailable()) {
+                  this.ofFogType = 3;
+               }
+               break;
+            case 2:
+               this.ofFogType = 3;
+               break;
+            case 3:
+               this.ofFogType = 1;
+               break;
+            default:
+               this.ofFogType = 1;
+         }
+      }
+
+      if (var1 == GameSettings.Options.FOG_START) {
+         this.ofFogStart += 0.2F;
+         if (this.ofFogStart > 0.81F) {
+            this.ofFogStart = 0.2F;
+         }
+      }
+
+      if (var1 == GameSettings.Options.SMOOTH_FPS) {
+         this.ofSmoothFps = !this.ofSmoothFps;
+      }
+
+      if (var1 == GameSettings.Options.SMOOTH_WORLD) {
+         this.ofSmoothWorld = !this.ofSmoothWorld;
+         Config.updateThreadPriorities();
+      }
+
+      if (var1 == GameSettings.Options.CLOUDS) {
+         this.ofClouds++;
+         if (this.ofClouds > 3) {
+            this.ofClouds = 0;
+         }
+
+         this.updateRenderClouds();
+         this.mc.renderGlobal.resetClouds();
+      }
+
+      if (var1 == GameSettings.Options.TREES) {
+         this.ofTrees = nextValue(this.ofTrees, OF_TREES_VALUES);
+         this.mc.renderGlobal.loadRenderers();
+      }
+
+      if (var1 == GameSettings.Options.DROPPED_ITEMS) {
+         this.ofDroppedItems++;
+         if (this.ofDroppedItems > 2) {
+            this.ofDroppedItems = 0;
+         }
+      }
+
+      if (var1 == GameSettings.Options.RAIN) {
+         this.ofRain++;
+         if (this.ofRain > 3) {
+            this.ofRain = 0;
+         }
+      }
+
+      if (var1 == GameSettings.Options.ANIMATED_WATER) {
+         this.ofAnimatedWater++;
+         if (this.ofAnimatedWater == 1) {
+            this.ofAnimatedWater++;
+         }
+
+         if (this.ofAnimatedWater > 2) {
+            this.ofAnimatedWater = 0;
+         }
+      }
+
+      if (var1 == GameSettings.Options.ANIMATED_LAVA) {
+         this.ofAnimatedLava++;
+         if (this.ofAnimatedLava == 1) {
+            this.ofAnimatedLava++;
+         }
+
+         if (this.ofAnimatedLava > 2) {
+            this.ofAnimatedLava = 0;
+         }
+      }
+
+      if (var1 == GameSettings.Options.ANIMATED_FIRE) {
+         this.ofAnimatedFire = !this.ofAnimatedFire;
+      }
+
+      if (var1 == GameSettings.Options.ANIMATED_PORTAL) {
+         this.ofAnimatedPortal = !this.ofAnimatedPortal;
+      }
+
+      if (var1 == GameSettings.Options.ANIMATED_REDSTONE) {
+         this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
+      }
+
+      if (var1 == GameSettings.Options.ANIMATED_EXPLOSION) {
+         this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
+      }
+
+      if (var1 == GameSettings.Options.ANIMATED_FLAME) {
+         this.ofAnimatedFlame = !this.ofAnimatedFlame;
+      }
+
+      if (var1 == GameSettings.Options.ANIMATED_SMOKE) {
+         this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
+      }
+
+      if (var1 == GameSettings.Options.VOID_PARTICLES) {
+         this.ofVoidParticles = !this.ofVoidParticles;
+      }
+
+      if (var1 == GameSettings.Options.WATER_PARTICLES) {
+         this.ofWaterParticles = !this.ofWaterParticles;
+      }
+
+      if (var1 == GameSettings.Options.PORTAL_PARTICLES) {
+         this.ofPortalParticles = !this.ofPortalParticles;
+      }
+
+      if (var1 == GameSettings.Options.POTION_PARTICLES) {
+         this.ofPotionParticles = !this.ofPotionParticles;
+      }
+
+      if (var1 == GameSettings.Options.FIREWORK_PARTICLES) {
+         this.ofFireworkParticles = !this.ofFireworkParticles;
+      }

-         for (String var15 : var2.getKeySet()) {
-            String var16 = var2.getString(var15);
+      if (var1 == GameSettings.Options.DRIPPING_WATER_LAVA) {
+         this.ofDrippingWaterLava = !this.ofDrippingWaterLava;
+      }
+
+      if (var1 == GameSettings.Options.ANIMATED_TERRAIN) {
+         this.ofAnimatedTerrain = !this.ofAnimatedTerrain;
+      }
+
+      if (var1 == GameSettings.Options.ANIMATED_TEXTURES) {
+         this.ofAnimatedTextures = !this.ofAnimatedTextures;
+      }
+
+      if (var1 == GameSettings.Options.RAIN_SPLASH) {
+         this.ofRainSplash = !this.ofRainSplash;
+      }
+
+      if (var1 == GameSettings.Options.LAGOMETER) {
+         this.ofLagometer = !this.ofLagometer;
+      }
+
+      if (var1 == GameSettings.Options.SHOW_FPS) {
+         this.ofShowFps = !this.ofShowFps;
+      }
+
+      if (var1 == GameSettings.Options.AUTOSAVE_TICKS) {
+         short var3 = 900;
+         this.ofAutoSaveTicks = Math.max(this.ofAutoSaveTicks / var3 * var3, var3);
+         this.ofAutoSaveTicks *= 2;
+         if (this.ofAutoSaveTicks > 32 * var3) {
+            this.ofAutoSaveTicks = var3;
+         }
+      }
+
+      if (var1 == GameSettings.Options.BETTER_GRASS) {
+         this.ofBetterGrass++;
+         if (this.ofBetterGrass > 3) {
+            this.ofBetterGrass = 1;
+         }
+
+         this.mc.renderGlobal.loadRenderers();
+      }
+
+      if (var1 == GameSettings.Options.CONNECTED_TEXTURES) {
+         this.ofConnectedTextures++;
+         if (this.ofConnectedTextures > 3) {
+            this.ofConnectedTextures = 1;
+         }
+
+         if (this.ofConnectedTextures == 2) {
+            this.mc.renderGlobal.loadRenderers();
+         } else {
+            this.mc.refreshResources();
+         }
+      }
+
+      if (var1 == GameSettings.Options.WEATHER) {
+         this.ofWeather = !this.ofWeather;
+      }
+
+      if (var1 == GameSettings.Options.SKY) {
+         this.ofSky = !this.ofSky;
+      }
+
+      if (var1 == GameSettings.Options.STARS) {
+         this.ofStars = !this.ofStars;
+      }
+
+      if (var1 == GameSettings.Options.SUN_MOON) {
+         this.ofSunMoon = !this.ofSunMoon;
+      }
+
+      if (var1 == GameSettings.Options.VIGNETTE) {
+         this.ofVignette++;
+         if (this.ofVignette > 2) {
+            this.ofVignette = 0;
+         }
+      }
+
+      if (var1 == GameSettings.Options.CHUNK_UPDATES) {
+         this.ofChunkUpdates++;
+         if (this.ofChunkUpdates > 5) {
+            this.ofChunkUpdates = 1;
+         }
+      }
+
+      if (var1 == GameSettings.Options.CHUNK_UPDATES_DYNAMIC) {
+         this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
+      }
+
+      if (var1 == GameSettings.Options.TIME) {
+         this.ofTime++;
+         if (this.ofTime > 2) {
+            this.ofTime = 0;
+         }
+      }

+      if (var1 == GameSettings.Options.CLEAR_WATER) {
+         this.ofClearWater = !this.ofClearWater;
+         this.updateWaterOpacity();
+      }
+
+      if (var1 == GameSettings.Options.PROFILER) {
+         this.ofProfiler = !this.ofProfiler;
+      }
+
+      if (var1 == GameSettings.Options.BETTER_SNOW) {
+         this.ofBetterSnow = !this.ofBetterSnow;
+         this.mc.renderGlobal.loadRenderers();
+      }
+
+      if (var1 == GameSettings.Options.SWAMP_COLORS) {
+         this.ofSwampColors = !this.ofSwampColors;
+         CustomColors.updateUseDefaultGrassFoliageColors();
+         this.mc.renderGlobal.loadRenderers();
+      }
+
+      if (var1 == GameSettings.Options.RANDOM_ENTITIES) {
+         this.ofRandomEntities = !this.ofRandomEntities;
+         RandomEntities.update();
+      }
+
+      if (var1 == GameSettings.Options.SMOOTH_BIOMES) {
+         this.ofSmoothBiomes = !this.ofSmoothBiomes;
+         CustomColors.updateUseDefaultGrassFoliageColors();
+         this.mc.renderGlobal.loadRenderers();
+      }
+
+      if (var1 == GameSettings.Options.CUSTOM_FONTS) {
+         this.ofCustomFonts = !this.ofCustomFonts;
+         this.mc.fontRenderer.onResourceManagerReload(Config.getResourceManager());
+         this.mc.standardGalacticFontRenderer.onResourceManagerReload(Config.getResourceManager());
+      }
+
+      if (var1 == GameSettings.Options.CUSTOM_COLORS) {
+         this.ofCustomColors = !this.ofCustomColors;
+         CustomColors.update();
+         this.mc.renderGlobal.loadRenderers();
+      }
+
+      if (var1 == GameSettings.Options.CUSTOM_ITEMS) {
+         this.ofCustomItems = !this.ofCustomItems;
+         this.mc.refreshResources();
+      }
+
+      if (var1 == GameSettings.Options.CUSTOM_SKY) {
+         this.ofCustomSky = !this.ofCustomSky;
+         CustomSky.update();
+      }
+
+      if (var1 == GameSettings.Options.SHOW_CAPES) {
+         this.ofShowCapes = !this.ofShowCapes;
+      }
+
+      if (var1 == GameSettings.Options.NATURAL_TEXTURES) {
+         this.ofNaturalTextures = !this.ofNaturalTextures;
+         NaturalTextures.update();
+         this.mc.renderGlobal.loadRenderers();
+      }
+
+      if (var1 == GameSettings.Options.EMISSIVE_TEXTURES) {
+         this.ofEmissiveTextures = !this.ofEmissiveTextures;
+         this.mc.refreshResources();
+      }
+
+      if (var1 == GameSettings.Options.FAST_MATH) {
+         this.ofFastMath = !this.ofFastMath;
+         MathHelper.fastMath = this.ofFastMath;
+      }
+
+      if (var1 == GameSettings.Options.FAST_RENDER) {
+         if (!this.ofFastRender && Config.isShaders()) {
+            Config.showGuiMessage(Lang.get("of.message.fr.shaders1"), Lang.get("of.message.fr.shaders2"));
+            return;
+         }
+
+         this.ofFastRender = !this.ofFastRender;
+         if (this.ofFastRender) {
+            this.mc.entityRenderer.stopUseShader();
+         }
+
+         Config.updateFramebufferSize();
+      }
+
+      if (var1 == GameSettings.Options.TRANSLUCENT_BLOCKS) {
+         if (this.ofTranslucentBlocks == 0) {
+            this.ofTranslucentBlocks = 1;
+         } else if (this.ofTranslucentBlocks == 1) {
+            this.ofTranslucentBlocks = 2;
+         } else if (this.ofTranslucentBlocks == 2) {
+            this.ofTranslucentBlocks = 0;
+         } else {
+            this.ofTranslucentBlocks = 0;
+         }
+
+         this.mc.renderGlobal.loadRenderers();
+      }
+
+      if (var1 == GameSettings.Options.LAZY_CHUNK_LOADING) {
+         this.ofLazyChunkLoading = !this.ofLazyChunkLoading;
+      }
+
+      if (var1 == GameSettings.Options.RENDER_REGIONS) {
+         this.ofRenderRegions = !this.ofRenderRegions;
+         this.mc.renderGlobal.loadRenderers();
+      }
+
+      if (var1 == GameSettings.Options.SMART_ANIMATIONS) {
+         this.ofSmartAnimations = !this.ofSmartAnimations;
+         this.mc.renderGlobal.loadRenderers();
+      }
+
+      if (var1 == GameSettings.Options.DYNAMIC_FOV) {
+         this.ofDynamicFov = !this.ofDynamicFov;
+      }
+
+      if (var1 == GameSettings.Options.ALTERNATE_BLOCKS) {
+         this.ofAlternateBlocks = !this.ofAlternateBlocks;
+         this.mc.refreshResources();
+      }
+
+      if (var1 == GameSettings.Options.DYNAMIC_LIGHTS) {
+         this.ofDynamicLights = nextValue(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
+         DynamicLights.removeLights(this.mc.renderGlobal);
+      }
+
+      if (var1 == GameSettings.Options.SCREENSHOT_SIZE) {
+         this.ofScreenshotSize++;
+         if (this.ofScreenshotSize > 4) {
+            this.ofScreenshotSize = 1;
+         }
+
+         if (!OpenGlHelper.isFramebufferEnabled()) {
+            this.ofScreenshotSize = 1;
+         }
+      }
+
+      if (var1 == GameSettings.Options.CUSTOM_ENTITY_MODELS) {
+         this.ofCustomEntityModels = !this.ofCustomEntityModels;
+         this.mc.refreshResources();
+      }
+
+      if (var1 == GameSettings.Options.CUSTOM_GUIS) {
+         this.ofCustomGuis = !this.ofCustomGuis;
+         CustomGuis.update();
+      }
+
+      if (var1 == GameSettings.Options.SHOW_GL_ERRORS) {
+         this.ofShowGlErrors = !this.ofShowGlErrors;
+      }
+
+      if (var1 == GameSettings.Options.HELD_ITEM_TOOLTIPS) {
+         this.heldItemTooltips = !this.heldItemTooltips;
+      }
+
+      if (var1 == GameSettings.Options.ADVANCED_TOOLTIPS) {
+         this.advancedItemTooltips = !this.advancedItemTooltips;
+      }
+   }
+
+   private String getKeyBindingOF(GameSettings.Options var1) {
+      String var2 = I18n.format(var1.getTranslation()) + ": ";
+      if (var2 == null) {
+         var2 = var1.getTranslation();
+      }
+
+      if (var1 == GameSettings.Options.RENDER_DISTANCE) {
+         int var12 = (int)this.getOptionFloatValue(var1);
+         String var5 = I18n.format("of.options.renderDistance.tiny");
+         byte var6 = 2;
+         if (var12 >= 4) {
+            var5 = I18n.format("of.options.renderDistance.short");
+            var6 = 4;
+         }
+
+         if (var12 >= 8) {
+            var5 = I18n.format("of.options.renderDistance.normal");
+            var6 = 8;
+         }
+
+         if (var12 >= 16) {
+            var5 = I18n.format("of.options.renderDistance.far");
+            var6 = 16;
+         }
+
+         if (var12 >= 32) {
+            var5 = Lang.get("of.options.renderDistance.extreme");
+            var6 = 32;
+         }
+
+         if (var12 >= 48) {
+            var5 = Lang.get("of.options.renderDistance.insane");
+            var6 = 48;
+         }
+
+         if (var12 >= 64) {
+            var5 = Lang.get("of.options.renderDistance.ludicrous");
+            var6 = 64;
+         }
+
+         int var7 = this.renderDistanceChunks - var6;
+         String var8 = var5;
+         if (var7 > 0) {
+            var8 = var5 + "+";
+         }
+
+         return var2 + var12 + " " + var8 + "";
+      } else if (var1 == GameSettings.Options.FOG_FANCY) {
+         switch (this.ofFogType) {
+            case 1:
+               return var2 + Lang.getFast();
+            case 2:
+               return var2 + Lang.getFancy();
+            case 3:
+               return var2 + Lang.getOff();
+            default:
+               return var2 + Lang.getOff();
+         }
+      } else if (var1 == GameSettings.Options.FOG_START) {
+         return var2 + this.ofFogStart;
+      } else if (var1 == GameSettings.Options.MIPMAP_TYPE) {
+         switch (this.ofMipmapType) {
+            case 0:
+               return var2 + Lang.get("of.options.mipmap.nearest");
+            case 1:
+               return var2 + Lang.get("of.options.mipmap.linear");
+            case 2:
+               return var2 + Lang.get("of.options.mipmap.bilinear");
+            case 3:
+               return var2 + Lang.get("of.options.mipmap.trilinear");
+            default:
+               return var2 + "of.options.mipmap.nearest";
+         }
+      } else if (var1 == GameSettings.Options.SMOOTH_FPS) {
+         return this.ofSmoothFps ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.SMOOTH_WORLD) {
+         return this.ofSmoothWorld ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.CLOUDS) {
+         switch (this.ofClouds) {
+            case 1:
+               return var2 + Lang.getFast();
+            case 2:
+               return var2 + Lang.getFancy();
+            case 3:
+               return var2 + Lang.getOff();
+            default:
+               return var2 + Lang.getDefault();
+         }
+      } else if (var1 == GameSettings.Options.TREES) {
+         switch (this.ofTrees) {
+            case 1:
+               return var2 + Lang.getFast();
+            case 2:
+               return var2 + Lang.getFancy();
+            case 3:
+            default:
+               return var2 + Lang.getDefault();
+            case 4:
+               return var2 + Lang.get("of.general.smart");
+         }
+      } else if (var1 == GameSettings.Options.DROPPED_ITEMS) {
+         switch (this.ofDroppedItems) {
+            case 1:
+               return var2 + Lang.getFast();
+            case 2:
+               return var2 + Lang.getFancy();
+            default:
+               return var2 + Lang.getDefault();
+         }
+      } else if (var1 == GameSettings.Options.RAIN) {
+         switch (this.ofRain) {
+            case 1:
+               return var2 + Lang.getFast();
+            case 2:
+               return var2 + Lang.getFancy();
+            case 3:
+               return var2 + Lang.getOff();
+            default:
+               return var2 + Lang.getDefault();
+         }
+      } else if (var1 == GameSettings.Options.ANIMATED_WATER) {
+         switch (this.ofAnimatedWater) {
+            case 1:
+               return var2 + Lang.get("of.options.animation.dynamic");
+            case 2:
+               return var2 + Lang.getOff();
+            default:
+               return var2 + Lang.getOn();
+         }
+      } else if (var1 == GameSettings.Options.ANIMATED_LAVA) {
+         switch (this.ofAnimatedLava) {
+            case 1:
+               return var2 + Lang.get("of.options.animation.dynamic");
+            case 2:
+               return var2 + Lang.getOff();
+            default:
+               return var2 + Lang.getOn();
+         }
+      } else if (var1 == GameSettings.Options.ANIMATED_FIRE) {
+         return this.ofAnimatedFire ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.ANIMATED_PORTAL) {
+         return this.ofAnimatedPortal ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.ANIMATED_REDSTONE) {
+         return this.ofAnimatedRedstone ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.ANIMATED_EXPLOSION) {
+         return this.ofAnimatedExplosion ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.ANIMATED_FLAME) {
+         return this.ofAnimatedFlame ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.ANIMATED_SMOKE) {
+         return this.ofAnimatedSmoke ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.VOID_PARTICLES) {
+         return this.ofVoidParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.WATER_PARTICLES) {
+         return this.ofWaterParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.PORTAL_PARTICLES) {
+         return this.ofPortalParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.POTION_PARTICLES) {
+         return this.ofPotionParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.FIREWORK_PARTICLES) {
+         return this.ofFireworkParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.DRIPPING_WATER_LAVA) {
+         return this.ofDrippingWaterLava ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.ANIMATED_TERRAIN) {
+         return this.ofAnimatedTerrain ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.ANIMATED_TEXTURES) {
+         return this.ofAnimatedTextures ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.RAIN_SPLASH) {
+         return this.ofRainSplash ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.LAGOMETER) {
+         return this.ofLagometer ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.SHOW_FPS) {
+         return this.ofShowFps ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.AUTOSAVE_TICKS) {
+         short var11 = 900;
+         if (this.ofAutoSaveTicks <= var11) {
+            return var2 + Lang.get("of.options.save.45s");
+         } else if (this.ofAutoSaveTicks <= 2 * var11) {
+            return var2 + Lang.get("of.options.save.90s");
+         } else if (this.ofAutoSaveTicks <= 4 * var11) {
+            return var2 + Lang.get("of.options.save.3min");
+         } else if (this.ofAutoSaveTicks <= 8 * var11) {
+            return var2 + Lang.get("of.options.save.6min");
+         } else {
+            return this.ofAutoSaveTicks <= 16 * var11 ? var2 + Lang.get("of.options.save.12min") : var2 + Lang.get("of.options.save.24min");
+         }
+      } else if (var1 == GameSettings.Options.BETTER_GRASS) {
+         switch (this.ofBetterGrass) {
+            case 1:
+               return var2 + Lang.getFast();
+            case 2:
+               return var2 + Lang.getFancy();
+            default:
+               return var2 + Lang.getOff();
+         }
+      } else if (var1 == GameSettings.Options.CONNECTED_TEXTURES) {
+         switch (this.ofConnectedTextures) {
+            case 1:
+               return var2 + Lang.getFast();
+            case 2:
+               return var2 + Lang.getFancy();
+            default:
+               return var2 + Lang.getOff();
+         }
+      } else if (var1 == GameSettings.Options.WEATHER) {
+         return this.ofWeather ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.SKY) {
+         return this.ofSky ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.STARS) {
+         return this.ofStars ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.SUN_MOON) {
+         return this.ofSunMoon ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.VIGNETTE) {
+         switch (this.ofVignette) {
+            case 1:
+               return var2 + Lang.getFast();
+            case 2:
+               return var2 + Lang.getFancy();
+            default:
+               return var2 + Lang.getDefault();
+         }
+      } else if (var1 == GameSettings.Options.CHUNK_UPDATES) {
+         return var2 + this.ofChunkUpdates;
+      } else if (var1 == GameSettings.Options.CHUNK_UPDATES_DYNAMIC) {
+         return this.ofChunkUpdatesDynamic ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.TIME) {
+         if (this.ofTime == 1) {
+            return var2 + Lang.get("of.options.time.dayOnly");
+         } else {
+            return this.ofTime == 2 ? var2 + Lang.get("of.options.time.nightOnly") : var2 + Lang.getDefault();
+         }
+      } else if (var1 == GameSettings.Options.CLEAR_WATER) {
+         return this.ofClearWater ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.AA_LEVEL) {
+         String var10 = "";
+         if (this.ofAaLevel != Config.getAntialiasingLevel()) {
+            var10 = " (" + Lang.get("of.general.restart") + ")";
+         }
+
+         return this.ofAaLevel == 0 ? var2 + Lang.getOff() + var10 : var2 + this.ofAaLevel + var10;
+      } else if (var1 == GameSettings.Options.AF_LEVEL) {
+         return this.ofAfLevel == 1 ? var2 + Lang.getOff() : var2 + this.ofAfLevel;
+      } else if (var1 == GameSettings.Options.PROFILER) {
+         return this.ofProfiler ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.BETTER_SNOW) {
+         return this.ofBetterSnow ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.SWAMP_COLORS) {
+         return this.ofSwampColors ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.RANDOM_ENTITIES) {
+         return this.ofRandomEntities ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.SMOOTH_BIOMES) {
+         return this.ofSmoothBiomes ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.CUSTOM_FONTS) {
+         return this.ofCustomFonts ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.CUSTOM_COLORS) {
+         return this.ofCustomColors ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.CUSTOM_SKY) {
+         return this.ofCustomSky ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.SHOW_CAPES) {
+         return this.ofShowCapes ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.CUSTOM_ITEMS) {
+         return this.ofCustomItems ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.NATURAL_TEXTURES) {
+         return this.ofNaturalTextures ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.EMISSIVE_TEXTURES) {
+         return this.ofEmissiveTextures ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.FAST_MATH) {
+         return this.ofFastMath ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.FAST_RENDER) {
+         return this.ofFastRender ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.TRANSLUCENT_BLOCKS) {
+         if (this.ofTranslucentBlocks == 1) {
+            return var2 + Lang.getFast();
+         } else {
+            return this.ofTranslucentBlocks == 2 ? var2 + Lang.getFancy() : var2 + Lang.getDefault();
+         }
+      } else if (var1 == GameSettings.Options.LAZY_CHUNK_LOADING) {
+         return this.ofLazyChunkLoading ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.RENDER_REGIONS) {
+         return this.ofRenderRegions ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.SMART_ANIMATIONS) {
+         return this.ofSmartAnimations ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.DYNAMIC_FOV) {
+         return this.ofDynamicFov ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.ALTERNATE_BLOCKS) {
+         return this.ofAlternateBlocks ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.DYNAMIC_LIGHTS) {
+         int var9 = indexOf(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
+         return var2 + getTranslation(KEYS_DYNAMIC_LIGHTS, var9);
+      } else if (var1 == GameSettings.Options.SCREENSHOT_SIZE) {
+         return this.ofScreenshotSize <= 1 ? var2 + Lang.getDefault() : var2 + this.ofScreenshotSize + "x";
+      } else if (var1 == GameSettings.Options.CUSTOM_ENTITY_MODELS) {
+         return this.ofCustomEntityModels ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.CUSTOM_GUIS) {
+         return this.ofCustomGuis ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.SHOW_GL_ERRORS) {
+         return this.ofShowGlErrors ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.FULLSCREEN_MODE) {
+         return this.ofFullscreenMode.equals("Default") ? var2 + Lang.getDefault() : var2 + this.ofFullscreenMode;
+      } else if (var1 == GameSettings.Options.HELD_ITEM_TOOLTIPS) {
+         return this.heldItemTooltips ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.ADVANCED_TOOLTIPS) {
+         return this.advancedItemTooltips ? var2 + Lang.getOn() : var2 + Lang.getOff();
+      } else if (var1 == GameSettings.Options.FRAMERATE_LIMIT) {
+         float var4 = this.getOptionFloatValue(var1);
+         if (var4 == 0.0F) {
+            return var2 + Lang.get("of.options.framerateLimit.vsync");
+         } else {
+            return var4 == var1.valueMax ? var2 + I18n.format("options.framerateLimit.max") : var2 + (int)var4 + " fps";
+         }
+      } else {
+         return null;
+      }
+   }
+
+   public void loadOfOptions() {
+      try {
+         File var1 = this.optionsFileOF;
+         if (!var1.exists()) {
+            var1 = this.optionsFile;
+         }
+
+         if (!var1.exists()) {
+            return;
+         }
+
+         BufferedReader var2 = new BufferedReader(new InputStreamReader(new FileInputStream(var1), StandardCharsets.UTF_8));
+         String var3 = "";
+
+         while ((var3 = var2.readLine()) != null) {
             try {
-               if ("mouseSensitivity".equals(var15)) {
-                  this.mouseSensitivity = this.parseFloat(var16);
+               String[] var4 = var3.split(":");
+               if (var4[0].equals("ofRenderDistanceChunks") && var4.length >= 2) {
+                  this.renderDistanceChunks = Integer.valueOf(var4[1]);
+                  this.renderDistanceChunks = Config.limit(this.renderDistanceChunks, 2, 1024);
                }

-               if ("fov".equals(var15)) {
-                  this.fovSetting = this.parseFloat(var16) * 40.0F + 70.0F;
+               if (var4[0].equals("ofFogType") && var4.length >= 2) {
+                  this.ofFogType = Integer.valueOf(var4[1]);
+                  this.ofFogType = Config.limit(this.ofFogType, 1, 3);
                }

-               if ("gamma".equals(var15)) {
-                  this.gammaSetting = this.parseFloat(var16);
+               if (var4[0].equals("ofFogStart") && var4.length >= 2) {
+                  this.ofFogStart = Float.valueOf(var4[1]);
+                  if (this.ofFogStart < 0.2F) {
+                     this.ofFogStart = 0.2F;
+                  }
+
+                  if (this.ofFogStart > 0.81F) {
+                     this.ofFogStart = 0.8F;
+                  }
                }

-               if ("saturation".equals(var15)) {
-                  this.saturation = this.parseFloat(var16);
+               if (var4[0].equals("ofMipmapType") && var4.length >= 2) {
+                  this.ofMipmapType = Integer.valueOf(var4[1]);
+                  this.ofMipmapType = Config.limit(this.ofMipmapType, 0, 3);
                }

-               if ("invertYMouse".equals(var15)) {
-                  this.invertMouse = "true".equals(var16);
+               if (var4[0].equals("ofOcclusionFancy") && var4.length >= 2) {
+                  this.ofOcclusionFancy = Boolean.valueOf(var4[1]);
                }

-               if ("renderDistance".equals(var15)) {
-                  this.renderDistanceChunks = Integer.parseInt(var16);
+               if (var4[0].equals("ofSmoothFps") && var4.length >= 2) {
+                  this.ofSmoothFps = Boolean.valueOf(var4[1]);
                }

-               if ("guiScale".equals(var15)) {
-                  this.guiScale = Integer.parseInt(var16);
+               if (var4[0].equals("ofSmoothWorld") && var4.length >= 2) {
+                  this.ofSmoothWorld = Boolean.valueOf(var4[1]);
                }

-               if ("particles".equals(var15)) {
-                  this.particleSetting = Integer.parseInt(var16);
+               if (var4[0].equals("ofAoLevel") && var4.length >= 2) {
+                  this.ofAoLevel = Float.valueOf(var4[1]);
+                  this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
                }

-               if ("bobView".equals(var15)) {
-                  this.viewBobbing = "true".equals(var16);
+               if (var4[0].equals("ofClouds") && var4.length >= 2) {
+                  this.ofClouds = Integer.valueOf(var4[1]);
+                  this.ofClouds = Config.limit(this.ofClouds, 0, 3);
+                  this.updateRenderClouds();
                }

-               if ("anaglyph3d".equals(var15)) {
-                  this.anaglyph = "true".equals(var16);
+               if (var4[0].equals("ofCloudsHeight") && var4.length >= 2) {
+                  this.ofCloudsHeight = Float.valueOf(var4[1]);
+                  this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
                }

-               if ("maxFps".equals(var15)) {
-                  this.limitFramerate = Integer.parseInt(var16);
+               if (var4[0].equals("ofTrees") && var4.length >= 2) {
+                  this.ofTrees = Integer.valueOf(var4[1]);
+                  this.ofTrees = limit(this.ofTrees, OF_TREES_VALUES);
                }

-               if ("fboEnable".equals(var15)) {
-                  this.fboEnable = "true".equals(var16);
+               if (var4[0].equals("ofDroppedItems") && var4.length >= 2) {
+                  this.ofDroppedItems = Integer.valueOf(var4[1]);
+                  this.ofDroppedItems = Config.limit(this.ofDroppedItems, 0, 2);
                }

-               if ("difficulty".equals(var15)) {
-                  this.difficulty = EnumDifficulty.byId(Integer.parseInt(var16));
+               if (var4[0].equals("ofRain") && var4.length >= 2) {
+                  this.ofRain = Integer.valueOf(var4[1]);
+                  this.ofRain = Config.limit(this.ofRain, 0, 3);
                }

-               if ("fancyGraphics".equals(var15)) {
-                  this.fancyGraphics = "true".equals(var16);
+               if (var4[0].equals("ofAnimatedWater") && var4.length >= 2) {
+                  this.ofAnimatedWater = Integer.valueOf(var4[1]);
+                  this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
                }

-               if ("tutorialStep".equals(var15)) {
-                  this.tutorialStep = TutorialSteps.getTutorial(var16);
+               if (var4[0].equals("ofAnimatedLava") && var4.length >= 2) {
+                  this.ofAnimatedLava = Integer.valueOf(var4[1]);
+                  this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
                }

-               if ("ao".equals(var15)) {
-                  if ("true".equals(var16)) {
-                     this.ambientOcclusion = 2;
-                  } else if ("false".equals(var16)) {
-                     this.ambientOcclusion = 0;
-                  } else {
-                     this.ambientOcclusion = Integer.parseInt(var16);
-                  }
+               if (var4[0].equals("ofAnimatedFire") && var4.length >= 2) {
+                  this.ofAnimatedFire = Boolean.valueOf(var4[1]);
                }

-               if ("renderClouds".equals(var15)) {
-                  if ("true".equals(var16)) {
-                     this.clouds = 2;
-                  } else if ("false".equals(var16)) {
-                     this.clouds = 0;
-                  } else if ("fast".equals(var16)) {
-                     this.clouds = 1;
-                  }
+               if (var4[0].equals("ofAnimatedPortal") && var4.length >= 2) {
+                  this.ofAnimatedPortal = Boolean.valueOf(var4[1]);
                }

-               if ("attackIndicator".equals(var15)) {
-                  if ("0".equals(var16)) {
-                     this.attackIndicator = 0;
-                  } else if ("1".equals(var16)) {
-                     this.attackIndicator = 1;
-                  } else if ("2".equals(var16)) {
-                     this.attackIndicator = 2;
-                  }
+               if (var4[0].equals("ofAnimatedRedstone") && var4.length >= 2) {
+                  this.ofAnimatedRedstone = Boolean.valueOf(var4[1]);
                }

-               if ("resourcePacks".equals(var15)) {
-                  this.resourcePacks = JsonUtils.gsonDeserialize(GSON, var16, TYPE_LIST_STRING);
-                  if (this.resourcePacks == null) {
-                     this.resourcePacks = Lists.newArrayList();
-                  }
+               if (var4[0].equals("ofAnimatedExplosion") && var4.length >= 2) {
+                  this.ofAnimatedExplosion = Boolean.valueOf(var4[1]);
                }

-               if ("incompatibleResourcePacks".equals(var15)) {
-                  this.incompatibleResourcePacks = JsonUtils.gsonDeserialize(GSON, var16, TYPE_LIST_STRING);
-                  if (this.incompatibleResourcePacks == null) {
-                     this.incompatibleResourcePacks = Lists.newArrayList();
-                  }
+               if (var4[0].equals("ofAnimatedFlame") && var4.length >= 2) {
+                  this.ofAnimatedFlame = Boolean.valueOf(var4[1]);
                }

-               if ("lastServer".equals(var15)) {
-                  this.lastServer = var16;
+               if (var4[0].equals("ofAnimatedSmoke") && var4.length >= 2) {
+                  this.ofAnimatedSmoke = Boolean.valueOf(var4[1]);
                }

-               if ("lang".equals(var15)) {
-                  this.language = var16;
+               if (var4[0].equals("ofVoidParticles") && var4.length >= 2) {
+                  this.ofVoidParticles = Boolean.valueOf(var4[1]);
                }

-               if ("chatVisibility".equals(var15)) {
-                  this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(var16));
+               if (var4[0].equals("ofWaterParticles") && var4.length >= 2) {
+                  this.ofWaterParticles = Boolean.valueOf(var4[1]);
                }

-               if ("chatColors".equals(var15)) {
-                  this.chatColours = "true".equals(var16);
+               if (var4[0].equals("ofPortalParticles") && var4.length >= 2) {
+                  this.ofPortalParticles = Boolean.valueOf(var4[1]);
                }

-               if ("chatLinks".equals(var15)) {
-                  this.chatLinks = "true".equals(var16);
+               if (var4[0].equals("ofPotionParticles") && var4.length >= 2) {
+                  this.ofPotionParticles = Boolean.valueOf(var4[1]);
                }

-               if ("chatLinksPrompt".equals(var15)) {
-                  this.chatLinksPrompt = "true".equals(var16);
+               if (var4[0].equals("ofFireworkParticles") && var4.length >= 2) {
+                  this.ofFireworkParticles = Boolean.valueOf(var4[1]);
                }

-               if ("chatOpacity".equals(var15)) {
-                  this.chatOpacity = this.parseFloat(var16);
+               if (var4[0].equals("ofDrippingWaterLava") && var4.length >= 2) {
+                  this.ofDrippingWaterLava = Boolean.valueOf(var4[1]);
                }

-               if ("snooperEnabled".equals(var15)) {
-                  this.snooperEnabled = "true".equals(var16);
+               if (var4[0].equals("ofAnimatedTerrain") && var4.length >= 2) {
+                  this.ofAnimatedTerrain = Boolean.valueOf(var4[1]);
                }

-               if ("fullscreen".equals(var15)) {
-                  this.fullScreen = "true".equals(var16);
+               if (var4[0].equals("ofAnimatedTextures") && var4.length >= 2) {
+                  this.ofAnimatedTextures = Boolean.valueOf(var4[1]);
                }

-               if ("enableVsync".equals(var15)) {
-                  this.enableVsync = "true".equals(var16);
+               if (var4[0].equals("ofRainSplash") && var4.length >= 2) {
+                  this.ofRainSplash = Boolean.valueOf(var4[1]);
                }

-               if ("useVbo".equals(var15)) {
-                  this.useVbo = "true".equals(var16);
+               if (var4[0].equals("ofLagometer") && var4.length >= 2) {
+                  this.ofLagometer = Boolean.valueOf(var4[1]);
                }

-               if ("hideServerAddress".equals(var15)) {
-                  this.hideServerAddress = "true".equals(var16);
+               if (var4[0].equals("ofShowFps") && var4.length >= 2) {
+                  this.ofShowFps = Boolean.valueOf(var4[1]);
                }

-               if ("advancedItemTooltips".equals(var15)) {
-                  this.advancedItemTooltips = "true".equals(var16);
+               if (var4[0].equals("ofAutoSaveTicks") && var4.length >= 2) {
+                  this.ofAutoSaveTicks = Integer.valueOf(var4[1]);
+                  this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
                }

-               if ("pauseOnLostFocus".equals(var15)) {
-                  this.pauseOnLostFocus = "true".equals(var16);
+               if (var4[0].equals("ofBetterGrass") && var4.length >= 2) {
+                  this.ofBetterGrass = Integer.valueOf(var4[1]);
+                  this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
                }

-               if ("touchscreen".equals(var15)) {
-                  this.touchscreen = "true".equals(var16);
+               if (var4[0].equals("ofConnectedTextures") && var4.length >= 2) {
+                  this.ofConnectedTextures = Integer.valueOf(var4[1]);
+                  this.ofConnectedTextures = Config.limit(this.ofConnectedTextures, 1, 3);
                }

-               if ("overrideHeight".equals(var15)) {
-                  this.overrideHeight = Integer.parseInt(var16);
+               if (var4[0].equals("ofWeather") && var4.length >= 2) {
+                  this.ofWeather = Boolean.valueOf(var4[1]);
                }

-               if ("overrideWidth".equals(var15)) {
-                  this.overrideWidth = Integer.parseInt(var16);
+               if (var4[0].equals("ofSky") && var4.length >= 2) {
+                  this.ofSky = Boolean.valueOf(var4[1]);
                }

-               if ("heldItemTooltips".equals(var15)) {
-                  this.heldItemTooltips = "true".equals(var16);
+               if (var4[0].equals("ofStars") && var4.length >= 2) {
+                  this.ofStars = Boolean.valueOf(var4[1]);
                }

-               if ("chatHeightFocused".equals(var15)) {
-                  this.chatHeightFocused = this.parseFloat(var16);
+               if (var4[0].equals("ofSunMoon") && var4.length >= 2) {
+                  this.ofSunMoon = Boolean.valueOf(var4[1]);
                }

-               if ("chatHeightUnfocused".equals(var15)) {
-                  this.chatHeightUnfocused = this.parseFloat(var16);
+               if (var4[0].equals("ofVignette") && var4.length >= 2) {
+                  this.ofVignette = Integer.valueOf(var4[1]);
+                  this.ofVignette = Config.limit(this.ofVignette, 0, 2);
                }

-               if ("chatScale".equals(var15)) {
-                  this.chatScale = this.parseFloat(var16);
+               if (var4[0].equals("ofChunkUpdates") && var4.length >= 2) {
+                  this.ofChunkUpdates = Integer.valueOf(var4[1]);
+                  this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
                }

-               if ("chatWidth".equals(var15)) {
-                  this.chatWidth = this.parseFloat(var16);
+               if (var4[0].equals("ofChunkUpdatesDynamic") && var4.length >= 2) {
+                  this.ofChunkUpdatesDynamic = Boolean.valueOf(var4[1]);
                }

-               if ("mipmapLevels".equals(var15)) {
-                  this.mipmapLevels = Integer.parseInt(var16);
+               if (var4[0].equals("ofTime") && var4.length >= 2) {
+                  this.ofTime = Integer.valueOf(var4[1]);
+                  this.ofTime = Config.limit(this.ofTime, 0, 2);
                }

-               if ("forceUnicodeFont".equals(var15)) {
-                  this.forceUnicodeFont = "true".equals(var16);
+               if (var4[0].equals("ofClearWater") && var4.length >= 2) {
+                  this.ofClearWater = Boolean.valueOf(var4[1]);
+                  this.updateWaterOpacity();
                }

-               if ("reducedDebugInfo".equals(var15)) {
-                  this.reducedDebugInfo = "true".equals(var16);
+               if (var4[0].equals("ofAaLevel") && var4.length >= 2) {
+                  this.ofAaLevel = Integer.valueOf(var4[1]);
+                  this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
                }

-               if ("useNativeTransport".equals(var15)) {
-                  this.useNativeTransport = "true".equals(var16);
+               if (var4[0].equals("ofAfLevel") && var4.length >= 2) {
+                  this.ofAfLevel = Integer.valueOf(var4[1]);
+                  this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
                }

-               if ("entityShadows".equals(var15)) {
-                  this.entityShadows = "true".equals(var16);
+               if (var4[0].equals("ofProfiler") && var4.length >= 2) {
+                  this.ofProfiler = Boolean.valueOf(var4[1]);
                }

-               if ("mainHand".equals(var15)) {
-                  this.mainHand = "left".equals(var16) ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
+               if (var4[0].equals("ofBetterSnow") && var4.length >= 2) {
+                  this.ofBetterSnow = Boolean.valueOf(var4[1]);
                }

-               if ("showSubtitles".equals(var15)) {
-                  this.showSubtitles = "true".equals(var16);
+               if (var4[0].equals("ofSwampColors") && var4.length >= 2) {
+                  this.ofSwampColors = Boolean.valueOf(var4[1]);
                }

-               if ("realmsNotifications".equals(var15)) {
-                  this.realmsNotifications = "true".equals(var16);
+               if (var4[0].equals("ofRandomEntities") && var4.length >= 2) {
+                  this.ofRandomEntities = Boolean.valueOf(var4[1]);
                }

-               if ("enableWeakAttacks".equals(var15)) {
-                  this.enableWeakAttacks = "true".equals(var16);
+               if (var4[0].equals("ofSmoothBiomes") && var4.length >= 2) {
+                  this.ofSmoothBiomes = Boolean.valueOf(var4[1]);
                }

-               if ("autoJump".equals(var15)) {
-                  this.autoJump = "true".equals(var16);
+               if (var4[0].equals("ofCustomFonts") && var4.length >= 2) {
+                  this.ofCustomFonts = Boolean.valueOf(var4[1]);
                }

-               if ("narrator".equals(var15)) {
-                  this.narrator = Integer.parseInt(var16);
+               if (var4[0].equals("ofCustomColors") && var4.length >= 2) {
+                  this.ofCustomColors = Boolean.valueOf(var4[1]);
                }

-               for (KeyBinding var9 : this.keyBindings) {
-                  if (var15.equals("key_" + var9.getKeyDescription())) {
-                     var9.setKeyCode(Integer.parseInt(var16));
-                  }
+               if (var4[0].equals("ofCustomItems") && var4.length >= 2) {
+                  this.ofCustomItems = Boolean.valueOf(var4[1]);
                }

-               for (SoundCategory var23 : SoundCategory.values()) {
-                  if (var15.equals("soundCategory_" + var23.getName())) {
-                     this.soundLevels.put(var23, this.parseFloat(var16));
-                  }
+               if (var4[0].equals("ofCustomSky") && var4.length >= 2) {
+                  this.ofCustomSky = Boolean.valueOf(var4[1]);
                }

-               for (EnumPlayerModelParts var24 : EnumPlayerModelParts.values()) {
-                  if (var15.equals("modelPart_" + var24.getPartName())) {
-                     this.setModelPartEnabled(var24, "true".equals(var16));
-                  }
+               if (var4[0].equals("ofShowCapes") && var4.length >= 2) {
+                  this.ofShowCapes = Boolean.valueOf(var4[1]);
                }
-            } catch (Exception var11) {
-               LOGGER.warn("Skipping bad option: {}:{}", var15, var16);
-            }
-         }

-         KeyBinding.resetKeyBindingArrayAndHash();
-      } catch (Exception var12) {
-         LOGGER.error("Failed to load options", var12);
-      }
-   }
+               if (var4[0].equals("ofNaturalTextures") && var4.length >= 2) {
+                  this.ofNaturalTextures = Boolean.valueOf(var4[1]);
+               }

-   private NBTTagCompound dataFix(NBTTagCompound var1) {
-      int var2 = 0;
+               if (var4[0].equals("ofEmissiveTextures") && var4.length >= 2) {
+                  this.ofEmissiveTextures = Boolean.valueOf(var4[1]);
+               }

-      try {
-         var2 = Integer.parseInt(var1.getString("version"));
-      } catch (RuntimeException var4) {
-      }
+               if (var4[0].equals("ofLazyChunkLoading") && var4.length >= 2) {
+                  this.ofLazyChunkLoading = Boolean.valueOf(var4[1]);
+               }

-      return this.mc.getDataFixer().process(FixTypes.OPTIONS, var1, var2);
-   }
+               if (var4[0].equals("ofRenderRegions") && var4.length >= 2) {
+                  this.ofRenderRegions = Boolean.valueOf(var4[1]);
+               }

-   private float parseFloat(String var1) {
-      if ("true".equals(var1)) {
-         return 1.0F;
-      } else {
-         return "false".equals(var1) ? 0.0F : Float.parseFloat(var1);
-      }
-   }
+               if (var4[0].equals("ofSmartAnimations") && var4.length >= 2) {
+                  this.ofSmartAnimations = Boolean.valueOf(var4[1]);
+               }

-   public void saveOptions() {
-      PrintWriter var1 = null;
+               if (var4[0].equals("ofDynamicFov") && var4.length >= 2) {
+                  this.ofDynamicFov = Boolean.valueOf(var4[1]);
+               }

-      try {
-         var1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8));
-         var1.println("version:1343");
-         var1.println("invertYMouse:" + this.invertMouse);
-         var1.println("mouseSensitivity:" + this.mouseSensitivity);
-         var1.println("fov:" + (this.fovSetting - 70.0F) / 40.0F);
-         var1.println("gamma:" + this.gammaSetting);
-         var1.println("saturation:" + this.saturation);
-         var1.println("renderDistance:" + this.renderDistanceChunks);
-         var1.println("guiScale:" + this.guiScale);
-         var1.println("particles:" + this.particleSetting);
-         var1.println("bobView:" + this.viewBobbing);
-         var1.println("anaglyph3d:" + this.anaglyph);
-         var1.println("maxFps:" + this.limitFramerate);
-         var1.println("fboEnable:" + this.fboEnable);
-         var1.println("difficulty:" + this.difficulty.getId());
-         var1.println("fancyGraphics:" + this.fancyGraphics);
-         var1.println("ao:" + this.ambientOcclusion);
-         switch (this.clouds) {
-            case 0:
-               var1.println("renderClouds:false");
-               break;
-            case 1:
-               var1.println("renderClouds:fast");
-               break;
-            case 2:
-               var1.println("renderClouds:true");
-         }
+               if (var4[0].equals("ofAlternateBlocks") && var4.length >= 2) {
+                  this.ofAlternateBlocks = Boolean.valueOf(var4[1]);
+               }

-         var1.println("resourcePacks:" + GSON.toJson(this.resourcePacks));
-         var1.println("incompatibleResourcePacks:" + GSON.toJson(this.incompatibleResourcePacks));
-         var1.println("lastServer:" + this.lastServer);
-         var1.println("lang:" + this.language);
-         var1.println("chatVisibility:" + this.chatVisibility.getChatVisibility());
-         var1.println("chatColors:" + this.chatColours);
-         var1.println("chatLinks:" + this.chatLinks);
-         var1.println("chatLinksPrompt:" + this.chatLinksPrompt);
-         var1.println("chatOpacity:" + this.chatOpacity);
-         var1.println("snooperEnabled:" + this.snooperEnabled);
-         var1.println("fullscreen:" + this.fullScreen);
-         var1.println("enableVsync:" + this.enableVsync);
-         var1.println("useVbo:" + this.useVbo);
-         var1.println("hideServerAddress:" + this.hideServerAddress);
-         var1.println("advancedItemTooltips:" + this.advancedItemTooltips);
-         var1.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
-         var1.println("touchscreen:" + this.touchscreen);
-         var1.println("overrideWidth:" + this.overrideWidth);
-         var1.println("overrideHeight:" + this.overrideHeight);
-         var1.println("heldItemTooltips:" + this.heldItemTooltips);
-         var1.println("chatHeightFocused:" + this.chatHeightFocused);
-         var1.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
-         var1.println("chatScale:" + this.chatScale);
-         var1.println("chatWidth:" + this.chatWidth);
-         var1.println("mipmapLevels:" + this.mipmapLevels);
-         var1.println("forceUnicodeFont:" + this.forceUnicodeFont);
-         var1.println("reducedDebugInfo:" + this.reducedDebugInfo);
-         var1.println("useNativeTransport:" + this.useNativeTransport);
-         var1.println("entityShadows:" + this.entityShadows);
-         var1.println("mainHand:" + (this.mainHand == EnumHandSide.LEFT ? "left" : "right"));
-         var1.println("attackIndicator:" + this.attackIndicator);
-         var1.println("showSubtitles:" + this.showSubtitles);
-         var1.println("realmsNotifications:" + this.realmsNotifications);
-         var1.println("enableWeakAttacks:" + this.enableWeakAttacks);
-         var1.println("autoJump:" + this.autoJump);
-         var1.println("narrator:" + this.narrator);
-         var1.println("tutorialStep:" + this.tutorialStep.getName());
+               if (var4[0].equals("ofDynamicLights") && var4.length >= 2) {
+                  this.ofDynamicLights = Integer.valueOf(var4[1]);
+                  this.ofDynamicLights = limit(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
+               }

-         for (KeyBinding var5 : this.keyBindings) {
-            var1.println("key_" + var5.getKeyDescription() + ":" + var5.getKeyCode());
-         }
+               if (var4[0].equals("ofScreenshotSize") && var4.length >= 2) {
+                  this.ofScreenshotSize = Integer.valueOf(var4[1]);
+                  this.ofScreenshotSize = Config.limit(this.ofScreenshotSize, 1, 4);
+               }

-         for (SoundCategory var17 : SoundCategory.values()) {
-            var1.println("soundCategory_" + var17.getName() + ":" + this.getSoundLevel(var17));
-         }
+               if (var4[0].equals("ofCustomEntityModels") && var4.length >= 2) {
+                  this.ofCustomEntityModels = Boolean.valueOf(var4[1]);
+               }

-         for (EnumPlayerModelParts var18 : EnumPlayerModelParts.values()) {
-            var1.println("modelPart_" + var18.getPartName() + ":" + this.setModelParts.contains(var18));
-         }
-      } catch (Exception var9) {
-         LOGGER.error("Failed to save options", var9);
-      } finally {
-         IOUtils.closeQuietly(var1);
-      }
+               if (var4[0].equals("ofCustomGuis") && var4.length >= 2) {
+                  this.ofCustomGuis = Boolean.valueOf(var4[1]);
+               }

-      this.sendSettingsToServer();
-   }
+               if (var4[0].equals("ofShowGlErrors") && var4.length >= 2) {
+                  this.ofShowGlErrors = Boolean.valueOf(var4[1]);
+               }

-   public float getSoundLevel(SoundCategory var1) {
-      return this.soundLevels.containsKey(var1) ? this.soundLevels.get(var1) : 1.0F;
-   }
+               if (var4[0].equals("ofFullscreenMode") && var4.length >= 2) {
+                  this.ofFullscreenMode = var4[1];
+               }

-   public void setSoundLevel(SoundCategory var1, float var2) {
-      this.mc.getSoundHandler().setSoundLevel(var1, var2);
-      this.soundLevels.put(var1, var2);
-   }
+               if (var4[0].equals("ofFastMath") && var4.length >= 2) {
+                  this.ofFastMath = Boolean.valueOf(var4[1]);
+                  MathHelper.fastMath = this.ofFastMath;
+               }

-   public void sendSettingsToServer() {
-      if (this.mc.player != null) {
-         int var1 = 0;
+               if (var4[0].equals("ofFastRender") && var4.length >= 2) {
+                  this.ofFastRender = Boolean.valueOf(var4[1]);
+               }

-         for (EnumPlayerModelParts var3 : this.setModelParts) {
-            var1 |= var3.getPartMask();
+               if (var4[0].equals("ofTranslucentBlocks") && var4.length >= 2) {
+                  this.ofTranslucentBlocks = Integer.valueOf(var4[1]);
+                  this.ofTranslucentBlocks = Config.limit(this.ofTranslucentBlocks, 0, 2);
+               }
+
+               if (var4[0].equals("key_" + this.ofKeyBindZoom.getKeyDescription())) {
+                  this.ofKeyBindZoom.setKeyCode(Integer.parseInt(var4[1]));
+               }
+            } catch (Exception var5) {
+               Config.dbg("Skipping bad option: " + var3);
+               var5.printStackTrace();
+            }
          }

-         this.mc
-            .player
-            .connection
-            .sendPacket(new CPacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, var1, this.mainHand));
+         KeyUtils.fixKeyConflicts(this.keyBindings, new KeyBinding[]{this.ofKeyBindZoom});
+         KeyBinding.resetKeyBindingArrayAndHash();
+         var2.close();
+      } catch (Exception var6) {
+         Config.warn("Failed to load options");
+         var6.printStackTrace();
       }
    }

-   public Set<EnumPlayerModelParts> getModelParts() {
-      return ImmutableSet.copyOf(this.setModelParts);
+   public void saveOfOptions() {
+      try {
+         PrintWriter var1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFileOF), StandardCharsets.UTF_8));
+         var1.println("ofFogType:" + this.ofFogType);
+         var1.println("ofFogStart:" + this.ofFogStart);
+         var1.println("ofMipmapType:" + this.ofMipmapType);
+         var1.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
+         var1.println("ofSmoothFps:" + this.ofSmoothFps);
+         var1.println("ofSmoothWorld:" + this.ofSmoothWorld);
+         var1.println("ofAoLevel:" + this.ofAoLevel);
+         var1.println("ofClouds:" + this.ofClouds);
+         var1.println("ofCloudsHeight:" + this.ofCloudsHeight);
+         var1.println("ofTrees:" + this.ofTrees);
+         var1.println("ofDroppedItems:" + this.ofDroppedItems);
+         var1.println("ofRain:" + this.ofRain);
+         var1.println("ofAnimatedWater:" + this.ofAnimatedWater);
+         var1.println("ofAnimatedLava:" + this.ofAnimatedLava);
+         var1.println("ofAnimatedFire:" + this.ofAnimatedFire);
+         var1.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
+         var1.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
+         var1.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
+         var1.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
+         var1.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
+         var1.println("ofVoidParticles:" + this.ofVoidParticles);
+         var1.println("ofWaterParticles:" + this.ofWaterParticles);
+         var1.println("ofPortalParticles:" + this.ofPortalParticles);
+         var1.println("ofPotionParticles:" + this.ofPotionParticles);
+         var1.println("ofFireworkParticles:" + this.ofFireworkParticles);
+         var1.println("ofDrippingWaterLava:" + this.ofDrippingWaterLava);
+         var1.println("ofAnimatedTerrain:" + this.ofAnimatedTerrain);
+         var1.println("ofAnimatedTextures:" + this.ofAnimatedTextures);
+         var1.println("ofRainSplash:" + this.ofRainSplash);
+         var1.println("ofLagometer:" + this.ofLagometer);
+         var1.println("ofShowFps:" + this.ofShowFps);
+         var1.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
+         var1.println("ofBetterGrass:" + this.ofBetterGrass);
+         var1.println("ofConnectedTextures:" + this.ofConnectedTextures);
+         var1.println("ofWeather:" + this.ofWeather);
+         var1.println("ofSky:" + this.ofSky);
+         var1.println("ofStars:" + this.ofStars);
+         var1.println("ofSunMoon:" + this.ofSunMoon);
+         var1.println("ofVignette:" + this.ofVignette);
+         var1.println("ofChunkUpdates:" + this.ofChunkUpdates);
+         var1.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
+         var1.println("ofTime:" + this.ofTime);
+         var1.println("ofClearWater:" + this.ofClearWater);
+         var1.println("ofAaLevel:" + this.ofAaLevel);
+         var1.println("ofAfLevel:" + this.ofAfLevel);
+         var1.println("ofProfiler:" + this.ofProfiler);
+         var1.println("ofBetterSnow:" + this.ofBetterSnow);
+         var1.println("ofSwampColors:" + this.ofSwampColors);
+         var1.println("ofRandomEntities:" + this.ofRandomEntities);
+         var1.println("ofSmoothBiomes:" + this.ofSmoothBiomes);
+         var1.println("ofCustomFonts:" + this.ofCustomFonts);
+         var1.println("ofCustomColors:" + this.ofCustomColors);
+         var1.println("ofCustomItems:" + this.ofCustomItems);
+         var1.println("ofCustomSky:" + this.ofCustomSky);
+         var1.println("ofShowCapes:" + this.ofShowCapes);
+         var1.println("ofNaturalTextures:" + this.ofNaturalTextures);
+         var1.println("ofEmissiveTextures:" + this.ofEmissiveTextures);
+         var1.println("ofLazyChunkLoading:" + this.ofLazyChunkLoading);
+         var1.println("ofRenderRegions:" + this.ofRenderRegions);
+         var1.println("ofSmartAnimations:" + this.ofSmartAnimations);
+         var1.println("ofDynamicFov:" + this.ofDynamicFov);
+         var1.println("ofAlternateBlocks:" + this.ofAlternateBlocks);
+         var1.println("ofDynamicLights:" + this.ofDynamicLights);
+         var1.println("ofScreenshotSize:" + this.ofScreenshotSize);
+         var1.println("ofCustomEntityModels:" + this.ofCustomEntityModels);
+         var1.println("ofCustomGuis:" + this.ofCustomGuis);
+         var1.println("ofShowGlErrors:" + this.ofShowGlErrors);
+         var1.println("ofFullscreenMode:" + this.ofFullscreenMode);
+         var1.println("ofFastMath:" + this.ofFastMath);
+         var1.println("ofFastRender:" + this.ofFastRender);
+         var1.println("ofTranslucentBlocks:" + this.ofTranslucentBlocks);
+         var1.println("key_" + this.ofKeyBindZoom.getKeyDescription() + ":" + this.ofKeyBindZoom.getKeyCode());
+         var1.close();
+      } catch (Exception var2) {
+         Config.warn("Failed to save options");
+         var2.printStackTrace();
+      }
+   }
+
+   private void updateRenderClouds() {
+      switch (this.ofClouds) {
+         case 1:
+            this.clouds = 1;
+            break;
+         case 2:
+            this.clouds = 2;
+            break;
+         case 3:
+            this.clouds = 0;
+            break;
+         default:
+            if (this.fancyGraphics) {
+               this.clouds = 2;
+            } else {
+               this.clouds = 1;
+            }
+      }
    }

-   public void setModelPartEnabled(EnumPlayerModelParts var1, boolean var2) {
-      if (var2) {
-         this.setModelParts.add(var1);
+   public void resetSettings() {
+      this.renderDistanceChunks = 8;
+      this.viewBobbing = true;
+      this.anaglyph = false;
+      this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
+      this.enableVsync = false;
+      this.updateVSync();
+      this.mipmapLevels = 4;
+      this.fancyGraphics = true;
+      this.ambientOcclusion = 2;
+      this.clouds = 2;
+      this.fovSetting = 70.0F;
+      this.gammaSetting = 0.0F;
+      this.guiScale = 0;
+      this.particleSetting = 0;
+      this.heldItemTooltips = true;
+      this.useVbo = false;
+      this.forceUnicodeFont = false;
+      this.ofFogType = 1;
+      this.ofFogStart = 0.8F;
+      this.ofMipmapType = 0;
+      this.ofOcclusionFancy = false;
+      this.ofSmartAnimations = false;
+      this.ofSmoothFps = false;
+      Config.updateAvailableProcessors();
+      this.ofSmoothWorld = Config.isSingleProcessor();
+      this.ofLazyChunkLoading = false;
+      this.ofRenderRegions = false;
+      this.ofFastMath = false;
+      this.ofFastRender = false;
+      this.ofTranslucentBlocks = 0;
+      this.ofDynamicFov = true;
+      this.ofAlternateBlocks = true;
+      this.ofDynamicLights = 3;
+      this.ofScreenshotSize = 1;
+      this.ofCustomEntityModels = true;
+      this.ofCustomGuis = true;
+      this.ofShowGlErrors = true;
+      this.ofAoLevel = 1.0F;
+      this.ofAaLevel = 0;
+      this.ofAfLevel = 1;
+      this.ofClouds = 0;
+      this.ofCloudsHeight = 0.0F;
+      this.ofTrees = 0;
+      this.ofRain = 0;
+      this.ofBetterGrass = 3;
+      this.ofAutoSaveTicks = 4000;
+      this.ofLagometer = false;
+      this.ofShowFps = false;
+      this.ofProfiler = false;
+      this.ofWeather = true;
+      this.ofSky = true;
+      this.ofStars = true;
+      this.ofSunMoon = true;
+      this.ofVignette = 0;
+      this.ofChunkUpdates = 1;
+      this.ofChunkUpdatesDynamic = false;
+      this.ofTime = 0;
+      this.ofClearWater = false;
+      this.ofBetterSnow = false;
+      this.ofFullscreenMode = "Default";
+      this.ofSwampColors = true;
+      this.ofRandomEntities = true;
+      this.ofSmoothBiomes = true;
+      this.ofCustomFonts = true;
+      this.ofCustomColors = true;
+      this.ofCustomItems = true;
+      this.ofCustomSky = true;
+      this.ofShowCapes = true;
+      this.ofConnectedTextures = 2;
+      this.ofNaturalTextures = false;
+      this.ofEmissiveTextures = true;
+      this.ofAnimatedWater = 0;
+      this.ofAnimatedLava = 0;
+      this.ofAnimatedFire = true;
+      this.ofAnimatedPortal = true;
+      this.ofAnimatedRedstone = true;
+      this.ofAnimatedExplosion = true;
+      this.ofAnimatedFlame = true;
+      this.ofAnimatedSmoke = true;
+      this.ofVoidParticles = true;
+      this.ofWaterParticles = true;
+      this.ofRainSplash = true;
+      this.ofPortalParticles = true;
+      this.ofPotionParticles = true;
+      this.ofFireworkParticles = true;
+      this.ofDrippingWaterLava = true;
+      this.ofAnimatedTerrain = true;
+      this.ofAnimatedTextures = true;
+      Shaders.setShaderPack("OFF");
+      Shaders.configAntialiasingLevel = 0;
+      Shaders.uninit();
+      Shaders.storeConfig();
+      this.updateWaterOpacity();
+      this.mc.refreshResources();
+      this.saveOptions();
+   }
+
+   public void updateVSync() {
+      Display.setVSyncEnabled(this.enableVsync);
+   }
+
+   private void updateWaterOpacity() {
+      if (Config.isIntegratedServerRunning()) {
+         Config.waterOpacityChanged = true;
+      }
+
+      ClearWater.updateWaterOpacity(this, this.mc.world);
+   }
+
+   public void setAllAnimations(boolean var1) {
+      int var2 = var1 ? 0 : 2;
+      this.ofAnimatedWater = var2;
+      this.ofAnimatedLava = var2;
+      this.ofAnimatedFire = var1;
+      this.ofAnimatedPortal = var1;
+      this.ofAnimatedRedstone = var1;
+      this.ofAnimatedExplosion = var1;
+      this.ofAnimatedFlame = var1;
+      this.ofAnimatedSmoke = var1;
+      this.ofVoidParticles = var1;
+      this.ofWaterParticles = var1;
+      this.ofRainSplash = var1;
+      this.ofPortalParticles = var1;
+      this.ofPotionParticles = var1;
+      this.ofFireworkParticles = var1;
+      this.particleSetting = var1 ? 0 : 2;
+      this.ofDrippingWaterLava = var1;
+      this.ofAnimatedTerrain = var1;
+      this.ofAnimatedTextures = var1;
+   }
+
+   private static int nextValue(int var0, int[] var1) {
+      int var2 = indexOf(var0, var1);
+      if (var2 < 0) {
+         return var1[0];
       } else {
-         this.setModelParts.remove(var1);
+         if (++var2 >= var1.length) {
+            var2 = 0;
+         }
+
+         return var1[var2];
       }
+   }

-      this.sendSettingsToServer();
+   private static int limit(int var0, int[] var1) {
+      int var2 = indexOf(var0, var1);
+      return var2 < 0 ? var1[0] : var0;
    }

-   public void switchModelPartEnabled(EnumPlayerModelParts var1) {
-      if (this.getModelParts().contains(var1)) {
-         this.setModelParts.remove(var1);
-      } else {
-         this.setModelParts.add(var1);
+   private static int indexOf(int var0, int[] var1) {
+      for (int var2 = 0; var2 < var1.length; var2++) {
+         if (var1[var2] == var0) {
+            return var2;
+         }
       }

-      this.sendSettingsToServer();
+      return -1;
    }

-   public int shouldRenderClouds() {
-      return this.renderDistanceChunks >= 4 ? this.clouds : 0;
+   private void setForgeKeybindProperties() {
+      if (Reflector.KeyConflictContext_IN_GAME.exists()) {
+         if (Reflector.ForgeKeyBinding_setKeyConflictContext.exists()) {
+            Object var1 = Reflector.getFieldValue(Reflector.KeyConflictContext_IN_GAME);
+            Reflector.call(this.keyBindForward, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+            Reflector.call(this.keyBindLeft, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+            Reflector.call(this.keyBindBack, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+            Reflector.call(this.keyBindRight, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+            Reflector.call(this.keyBindJump, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+            Reflector.call(this.keyBindSneak, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+            Reflector.call(this.keyBindSprint, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+            Reflector.call(this.keyBindAttack, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+            Reflector.call(this.keyBindChat, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+            Reflector.call(this.keyBindPlayerList, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+            Reflector.call(this.keyBindCommand, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+            Reflector.call(this.keyBindTogglePerspective, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+            Reflector.call(this.keyBindSmoothCamera, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+            Reflector.call(this.keyBindSwapHands, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{var1});
+         }
+      }
    }

-   public boolean isUsingNativeTransport() {
-      return this.useNativeTransport;
+   public void onGuiClosed() {
+      if (this.needsResourceRefresh) {
+         this.mc.scheduleResourcesRefresh();
+         this.needsResourceRefresh = false;
+      }
    }

    public static enum Options {
       INVERT_MOUSE("options.invertMouse", false, true),
       SENSITIVITY("options.sensitivity", true, false),
       FOV("options.fov", true, false, 30.0F, 110.0F, 1.0F),
       GAMMA("options.gamma", true, false),
       SATURATION("options.saturation", true, false),
       RENDER_DISTANCE("options.renderDistance", true, false, 2.0F, 16.0F, 1.0F),
       VIEW_BOBBING("options.viewBobbing", false, true),
       ANAGLYPH("options.anaglyph", false, true),
-      FRAMERATE_LIMIT("options.framerateLimit", true, false, 10.0F, 260.0F, 10.0F),
+      FRAMERATE_LIMIT("options.framerateLimit", true, false, 0.0F, 260.0F, 5.0F),
       FBO_ENABLE("options.fboEnable", false, true),
       RENDER_CLOUDS("options.renderClouds", false, false),
       GRAPHICS("options.graphics", false, false),
       AMBIENT_OCCLUSION("options.ao", false, false),
       GUI_SCALE("options.guiScale", false, false),
       PARTICLES("options.particles", false, false),
@@ -1084,13 +2785,85 @@
       MAIN_HAND("options.mainHand", false, false),
       ATTACK_INDICATOR("options.attackIndicator", false, false),
       ENABLE_WEAK_ATTACKS("options.enableWeakAttacks", false, true),
       SHOW_SUBTITLES("options.showSubtitles", false, true),
       REALMS_NOTIFICATIONS("options.realmsNotifications", false, true),
       AUTO_JUMP("options.autoJump", false, true),
-      NARRATOR("options.narrator", false, false);
+      NARRATOR("options.narrator", false, false),
+      FOG_FANCY("of.options.FOG_FANCY", false, false),
+      FOG_START("of.options.FOG_START", false, false),
+      MIPMAP_TYPE("of.options.MIPMAP_TYPE", true, false, 0.0F, 3.0F, 1.0F),
+      SMOOTH_FPS("of.options.SMOOTH_FPS", false, false),
+      CLOUDS("of.options.CLOUDS", false, false),
+      CLOUD_HEIGHT("of.options.CLOUD_HEIGHT", true, false),
+      TREES("of.options.TREES", false, false),
+      RAIN("of.options.RAIN", false, false),
+      ANIMATED_WATER("of.options.ANIMATED_WATER", false, false),
+      ANIMATED_LAVA("of.options.ANIMATED_LAVA", false, false),
+      ANIMATED_FIRE("of.options.ANIMATED_FIRE", false, false),
+      ANIMATED_PORTAL("of.options.ANIMATED_PORTAL", false, false),
+      AO_LEVEL("of.options.AO_LEVEL", true, false),
+      LAGOMETER("of.options.LAGOMETER", false, false),
+      SHOW_FPS("of.options.SHOW_FPS", false, false),
+      AUTOSAVE_TICKS("of.options.AUTOSAVE_TICKS", false, false),
+      BETTER_GRASS("of.options.BETTER_GRASS", false, false),
+      ANIMATED_REDSTONE("of.options.ANIMATED_REDSTONE", false, false),
+      ANIMATED_EXPLOSION("of.options.ANIMATED_EXPLOSION", false, false),
+      ANIMATED_FLAME("of.options.ANIMATED_FLAME", false, false),
+      ANIMATED_SMOKE("of.options.ANIMATED_SMOKE", false, false),
+      WEATHER("of.options.WEATHER", false, false),
+      SKY("of.options.SKY", false, false),
+      STARS("of.options.STARS", false, false),
+      SUN_MOON("of.options.SUN_MOON", false, false),
+      VIGNETTE("of.options.VIGNETTE", false, false),
+      CHUNK_UPDATES("of.options.CHUNK_UPDATES", false, false),
+      CHUNK_UPDATES_DYNAMIC("of.options.CHUNK_UPDATES_DYNAMIC", false, false),
+      TIME("of.options.TIME", false, false),
+      CLEAR_WATER("of.options.CLEAR_WATER", false, false),
+      SMOOTH_WORLD("of.options.SMOOTH_WORLD", false, false),
+      VOID_PARTICLES("of.options.VOID_PARTICLES", false, false),
+      WATER_PARTICLES("of.options.WATER_PARTICLES", false, false),
+      RAIN_SPLASH("of.options.RAIN_SPLASH", false, false),
+      PORTAL_PARTICLES("of.options.PORTAL_PARTICLES", false, false),
+      POTION_PARTICLES("of.options.POTION_PARTICLES", false, false),
+      FIREWORK_PARTICLES("of.options.FIREWORK_PARTICLES", false, false),
+      PROFILER("of.options.PROFILER", false, false),
+      DRIPPING_WATER_LAVA("of.options.DRIPPING_WATER_LAVA", false, false),
+      BETTER_SNOW("of.options.BETTER_SNOW", false, false),
+      FULLSCREEN_MODE("of.options.FULLSCREEN_MODE", true, false, 0.0F, Config.getDisplayModes().length, 1.0F),
+      ANIMATED_TERRAIN("of.options.ANIMATED_TERRAIN", false, false),
+      SWAMP_COLORS("of.options.SWAMP_COLORS", false, false),
+      RANDOM_ENTITIES("of.options.RANDOM_ENTITIES", false, false),
+      SMOOTH_BIOMES("of.options.SMOOTH_BIOMES", false, false),
+      CUSTOM_FONTS("of.options.CUSTOM_FONTS", false, false),
+      CUSTOM_COLORS("of.options.CUSTOM_COLORS", false, false),
+      SHOW_CAPES("of.options.SHOW_CAPES", false, false),
+      CONNECTED_TEXTURES("of.options.CONNECTED_TEXTURES", false, false),
+      CUSTOM_ITEMS("of.options.CUSTOM_ITEMS", false, false),
+      AA_LEVEL("of.options.AA_LEVEL", true, false, 0.0F, 16.0F, 1.0F),
+      AF_LEVEL("of.options.AF_LEVEL", true, false, 1.0F, 16.0F, 1.0F),
+      ANIMATED_TEXTURES("of.options.ANIMATED_TEXTURES", false, false),
+      NATURAL_TEXTURES("of.options.NATURAL_TEXTURES", false, false),
+      EMISSIVE_TEXTURES("of.options.EMISSIVE_TEXTURES", false, false),
+      HELD_ITEM_TOOLTIPS("of.options.HELD_ITEM_TOOLTIPS", false, false),
+      DROPPED_ITEMS("of.options.DROPPED_ITEMS", false, false),
+      LAZY_CHUNK_LOADING("of.options.LAZY_CHUNK_LOADING", false, false),
+      CUSTOM_SKY("of.options.CUSTOM_SKY", false, false),
+      FAST_MATH("of.options.FAST_MATH", false, false),
+      FAST_RENDER("of.options.FAST_RENDER", false, false),
+      TRANSLUCENT_BLOCKS("of.options.TRANSLUCENT_BLOCKS", false, false),
+      DYNAMIC_FOV("of.options.DYNAMIC_FOV", false, false),
+      DYNAMIC_LIGHTS("of.options.DYNAMIC_LIGHTS", false, false),
+      ALTERNATE_BLOCKS("of.options.ALTERNATE_BLOCKS", false, false),
+      CUSTOM_ENTITY_MODELS("of.options.CUSTOM_ENTITY_MODELS", false, false),
+      ADVANCED_TOOLTIPS("of.options.ADVANCED_TOOLTIPS", false, false),
+      SCREENSHOT_SIZE("of.options.SCREENSHOT_SIZE", false, false),
+      CUSTOM_GUIS("of.options.CUSTOM_GUIS", false, false),
+      RENDER_REGIONS("of.options.RENDER_REGIONS", false, false),
+      SHOW_GL_ERRORS("of.options.SHOW_GL_ERRORS", false, false),
+      SMART_ANIMATIONS("of.options.SMART_ANIMATIONS", false, false);

       private final boolean isFloat;
       private final boolean isBoolean;
       private final String translation;
       private final float valueStep;
       private float valueMin;
 */

