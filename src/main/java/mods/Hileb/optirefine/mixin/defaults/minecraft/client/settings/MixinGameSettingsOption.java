package mods.Hileb.optirefine.mixin.defaults.minecraft.client.settings;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.optifine.Config;
import mods.Hileb.optirefine.optifine.client.GameSettingsOptionOF;
import net.minecraft.client.settings.GameSettings;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
@Mixin(GameSettings.Options.class)
public abstract class MixinGameSettingsOption {
    @Unique
    private static final Class<?>[] _optirefine_args0 = new Class[]{String.class, boolean.class, boolean.class};
    @Unique
    private static final Class<?>[] _optirefine_args1 = new Class[]{String.class, boolean.class, boolean.class, float.class, float.class, float.class};

    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options FOG_FANCY = GameSettingsOptionOF.FOG_FANCY;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options FOG_START = GameSettingsOptionOF.FOG_START;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options MIPMAP_TYPE = GameSettingsOptionOF.MIPMAP_TYPE;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SMOOTH_FPS = GameSettingsOptionOF.SMOOTH_FPS;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options CLOUDS = GameSettingsOptionOF.CLOUDS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CLOUD_HEIGHT = GameSettingsOptionOF.CLOUD_HEIGHT;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options TREES = GameSettingsOptionOF.TREES;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options RAIN = GameSettingsOptionOF.RAIN;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_WATER = GameSettingsOptionOF.ANIMATED_WATER;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_LAVA = GameSettingsOptionOF.ANIMATED_LAVA;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_FIRE = GameSettingsOptionOF.ANIMATED_FIRE;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_PORTAL = GameSettingsOptionOF.ANIMATED_PORTAL;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options AO_LEVEL = GameSettingsOptionOF.AO_LEVEL;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options LAGOMETER = GameSettingsOptionOF.LAGOMETER;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SHOW_FPS = GameSettingsOptionOF.SHOW_FPS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options AUTOSAVE_TICKS = GameSettingsOptionOF.AUTOSAVE_TICKS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options BETTER_GRASS = GameSettingsOptionOF.BETTER_GRASS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_REDSTONE = GameSettingsOptionOF.ANIMATED_REDSTONE;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_EXPLOSION = GameSettingsOptionOF.ANIMATED_EXPLOSION;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_FLAME = GameSettingsOptionOF.ANIMATED_FLAME;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_SMOKE = GameSettingsOptionOF.ANIMATED_SMOKE;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options WEATHER = GameSettingsOptionOF.WEATHER;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options SKY = GameSettingsOptionOF.SKY;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options STARS = GameSettingsOptionOF.STARS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SUN_MOON = GameSettingsOptionOF.SUN_MOON;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options VIGNETTE = GameSettingsOptionOF.VIGNETTE;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CHUNK_UPDATES = GameSettingsOptionOF.CHUNK_UPDATES;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CHUNK_UPDATES_DYNAMIC = GameSettingsOptionOF.CHUNK_UPDATES_DYNAMIC;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options TIME = GameSettingsOptionOF.TIME;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CLEAR_WATER = GameSettingsOptionOF.CLEAR_WATER;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SMOOTH_WORLD = GameSettingsOptionOF.SMOOTH_WORLD;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options VOID_PARTICLES = GameSettingsOptionOF.VOID_PARTICLES;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options WATER_PARTICLES = GameSettingsOptionOF.WATER_PARTICLES;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options RAIN_SPLASH = GameSettingsOptionOF.RAIN_SPLASH;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options PORTAL_PARTICLES = GameSettingsOptionOF.PORTAL_PARTICLES;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options POTION_PARTICLES = GameSettingsOptionOF.POTION_PARTICLES;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options FIREWORK_PARTICLES = GameSettingsOptionOF.FIREWORK_PARTICLES;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options PROFILER = GameSettingsOptionOF.PROFILER;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options DRIPPING_WATER_LAVA = GameSettingsOptionOF.DRIPPING_WATER_LAVA;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options BETTER_SNOW = GameSettingsOptionOF.BETTER_SNOW;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options FULLSCREEN_MODE = GameSettingsOptionOF.FULLSCREEN_MODE;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_TERRAIN = GameSettingsOptionOF.ANIMATED_TERRAIN;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SWAMP_COLORS = GameSettingsOptionOF.SWAMP_COLORS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options RANDOM_ENTITIES = GameSettingsOptionOF.RANDOM_ENTITIES;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SMOOTH_BIOMES = GameSettingsOptionOF.SMOOTH_BIOMES;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CUSTOM_FONTS = GameSettingsOptionOF.CUSTOM_FONTS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CUSTOM_COLORS = GameSettingsOptionOF.CUSTOM_COLORS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SHOW_CAPES = GameSettingsOptionOF.SHOW_CAPES;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CONNECTED_TEXTURES = GameSettingsOptionOF.CONNECTED_TEXTURES;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CUSTOM_ITEMS = GameSettingsOptionOF.CUSTOM_ITEMS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options AA_LEVEL = GameSettingsOptionOF.AA_LEVEL;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options AF_LEVEL = GameSettingsOptionOF.AF_LEVEL;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_TEXTURES = GameSettingsOptionOF.ANIMATED_TEXTURES;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options NATURAL_TEXTURES = GameSettingsOptionOF.NATURAL_TEXTURES;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options EMISSIVE_TEXTURES = GameSettingsOptionOF.EMISSIVE_TEXTURES;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options HELD_ITEM_TOOLTIPS = GameSettingsOptionOF.HELD_ITEM_TOOLTIPS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options DROPPED_ITEMS = GameSettingsOptionOF.DROPPED_ITEMS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options LAZY_CHUNK_LOADING = GameSettingsOptionOF.LAZY_CHUNK_LOADING;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CUSTOM_SKY = GameSettingsOptionOF.CUSTOM_SKY;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options FAST_MATH = GameSettingsOptionOF.FAST_MATH;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options FAST_RENDER = GameSettingsOptionOF.FAST_RENDER;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options TRANSLUCENT_BLOCKS = GameSettingsOptionOF.TRANSLUCENT_BLOCKS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options DYNAMIC_FOV = GameSettingsOptionOF.DYNAMIC_FOV;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options DYNAMIC_LIGHTS = GameSettingsOptionOF.DYNAMIC_LIGHTS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ALTERNATE_BLOCKS = GameSettingsOptionOF.ALTERNATE_BLOCKS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CUSTOM_ENTITY_MODELS = GameSettingsOptionOF.CUSTOM_ENTITY_MODELS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ADVANCED_TOOLTIPS = GameSettingsOptionOF.ADVANCED_TOOLTIPS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SCREENSHOT_SIZE = GameSettingsOptionOF.SCREENSHOT_SIZE;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CUSTOM_GUIS = GameSettingsOptionOF.CUSTOM_GUIS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options RENDER_REGIONS = GameSettingsOptionOF.RENDER_REGIONS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SHOW_GL_ERRORS = GameSettingsOptionOF.SHOW_GL_ERRORS;
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SMART_ANIMATIONS = GameSettingsOptionOF.SMART_ANIMATIONS;

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.settings.GameSettings$Options field_148271_N F", deobf = true)
    private static native void Options_valueMin_set(GameSettings.Options options, float value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.settings.GameSettings$Options field_148272_O F", deobf = true)
    private static native void Options_valueMax_set(GameSettings.Options options, float value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "field_148270_M", access = Opcodes.ACC_PUBLIC, deobf = true)
    private float acc_valueStep;

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.settings.GameSettings$Options field_148270_M F", deobf = true)
    private static native void Options_valueStep_set(GameSettings.Options options, float value);

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void optiRefine$initOptionValues(CallbackInfo ci) {
        for (GameSettings.Options opt : GameSettings.Options.values()) {
            float min = 0.0F;
            float max = 1.0F;
            float step = 0.0F;
            switch (opt.name()) {
                case "FOV": min = 30.0F; max = 110.0F; step = 1.0F; break;
                case "RENDER_DISTANCE": min = 2.0F; max = 16.0F; step = 1.0F; break;
                case "FRAMERATE_LIMIT": min = 0.0F; max = 260.0F; step = 5.0F; break;
                case "MIPMAP_LEVELS": min = 0.0F; max = 4.0F; step = 1.0F; break;
                case "MIPMAP_TYPE": min = 0.0F; max = 3.0F; step = 1.0F; break;
                case "FULLSCREEN_MODE": min = 0.0F; max = (float) Config.getDisplayModes().length; step = 1.0F; break;
                case "AA_LEVEL": min = 0.0F; max = 16.0F; step = 1.0F; break;
                case "AF_LEVEL": min = 1.0F; max = 16.0F; step = 1.0F; break;
                default: break;
            }
            Options_valueMin_set(opt, min);
            Options_valueMax_set(opt, max);
            Options_valueStep_set(opt, step);
        }
    }
}
