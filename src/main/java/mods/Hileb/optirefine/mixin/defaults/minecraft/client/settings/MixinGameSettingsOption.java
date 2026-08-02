package mods.Hileb.optirefine.mixin.defaults.minecraft.client.settings;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.settings.GameSettings;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraftforge.common.util.EnumHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
@Mixin(GameSettings.Options.class)
public abstract class MixinGameSettingsOption {
    @Unique
    private static final Class<?>[] _optirefine_args0 = new Class[]{String.class, boolean.class, boolean.class};
    @Unique
    private static final Class<?>[] _optirefine_args1 = new Class[]{String.class, boolean.class, boolean.class, float.class, float.class, float.class};

    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options FOG_FANCY = EnumHelper.addEnum(GameSettings.Options.class, "FOG_FANCY", _optirefine_args0, "of.options.FOG_FANCY", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options FOG_START = EnumHelper.addEnum(GameSettings.Options.class, "FOG_START", _optirefine_args0, "of.options.FOG_START", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options MIPMAP_TYPE = EnumHelper.addEnum(GameSettings.Options.class, "MIPMAP_TYPE", _optirefine_args0, "of.options.MIPMAP_TYPE", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SMOOTH_FPS = EnumHelper.addEnum(GameSettings.Options.class, "SMOOTH_FPS", _optirefine_args0, "of.options.SMOOTH_FPS", false, false);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options CLOUDS = EnumHelper.addEnum(GameSettings.Options.class, "CLOUDS", _optirefine_args0, "of.options.CLOUDS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CLOUD_HEIGHT = EnumHelper.addEnum(GameSettings.Options.class, "CLOUD_HEIGHT", _optirefine_args0, "of.options.CLOUD_HEIGHT", true, false);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options TREES = EnumHelper.addEnum(GameSettings.Options.class, "TREES", _optirefine_args0, "of.options.TREES", false, false);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options RAIN = EnumHelper.addEnum(GameSettings.Options.class, "RAIN", _optirefine_args0, "of.options.RAIN", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_WATER = EnumHelper.addEnum(GameSettings.Options.class, "ANIMATED_WATER", _optirefine_args0, "of.options.ANIMATED_WATER", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_LAVA = EnumHelper.addEnum(GameSettings.Options.class, "ANIMATED_LAVA", _optirefine_args0, "of.options.ANIMATED_LAVA", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_FIRE = EnumHelper.addEnum(GameSettings.Options.class, "ANIMATED_FIRE", _optirefine_args0, "of.options.ANIMATED_FIRE", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_PORTAL = EnumHelper.addEnum(GameSettings.Options.class, "ANIMATED_PORTAL", _optirefine_args0, "of.options.ANIMATED_PORTAL", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options AO_LEVEL = EnumHelper.addEnum(GameSettings.Options.class, "AO_LEVEL", _optirefine_args0, "of.options.AO_LEVEL", true, false);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options LAGOMETER = EnumHelper.addEnum(GameSettings.Options.class, "LAGOMETER", _optirefine_args0, "of.options.LAGOMETER", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SHOW_FPS = EnumHelper.addEnum(GameSettings.Options.class, "SHOW_FPS", _optirefine_args0, "of.options.SHOW_FPS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options AUTOSAVE_TICKS = EnumHelper.addEnum(GameSettings.Options.class, "AUTOSAVE_TICKS", _optirefine_args0, "of.options.AUTOSAVE_TICKS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options BETTER_GRASS = EnumHelper.addEnum(GameSettings.Options.class, "BETTER_GRASS", _optirefine_args0, "of.options.BETTER_GRASS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_REDSTONE = EnumHelper.addEnum(GameSettings.Options.class, "ANIMATED_REDSTONE", _optirefine_args0, "of.options.ANIMATED_REDSTONE", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_EXPLOSION = EnumHelper.addEnum(GameSettings.Options.class, "ANIMATED_EXPLOSION", _optirefine_args0, "of.options.ANIMATED_EXPLOSION", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_FLAME = EnumHelper.addEnum(GameSettings.Options.class, "ANIMATED_FLAME", _optirefine_args0, "of.options.ANIMATED_FLAME", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_SMOKE = EnumHelper.addEnum(GameSettings.Options.class, "ANIMATED_SMOKE", _optirefine_args0, "of.options.ANIMATED_SMOKE", false, false);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options WEATHER = EnumHelper.addEnum(GameSettings.Options.class, "WEATHER", _optirefine_args0, "of.options.WEATHER", false, false);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options SKY = EnumHelper.addEnum(GameSettings.Options.class, "SKY", _optirefine_args0, "of.options.SKY", false, false);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options STARS = EnumHelper.addEnum(GameSettings.Options.class, "STARS", _optirefine_args0, "of.options.STARS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SUN_MOON = EnumHelper.addEnum(GameSettings.Options.class, "SUN_MOON", _optirefine_args0, "of.options.SUN_MOON", false, false);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options VIGNETTE = EnumHelper.addEnum(GameSettings.Options.class, "VIGNETTE", _optirefine_args0, "of.options.VIGNETTE", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CHUNK_UPDATES = EnumHelper.addEnum(GameSettings.Options.class, "CHUNK_UPDATES", _optirefine_args0, "of.options.CHUNK_UPDATES", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CHUNK_UPDATES_DYNAMIC = EnumHelper.addEnum(GameSettings.Options.class, "CHUNK_UPDATES_DYNAMIC", _optirefine_args0, "of.options.CHUNK_UPDATES_DYNAMIC", false, false);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options TIME = EnumHelper.addEnum(GameSettings.Options.class, "TIME", _optirefine_args0, "of.options.TIME", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CLEAR_WATER = EnumHelper.addEnum(GameSettings.Options.class, "CLEAR_WATER", _optirefine_args0, "of.options.CLEAR_WATER", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SMOOTH_WORLD = EnumHelper.addEnum(GameSettings.Options.class, "SMOOTH_WORLD", _optirefine_args0, "of.options.SMOOTH_WORLD", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options VOID_PARTICLES = EnumHelper.addEnum(GameSettings.Options.class, "VOID_PARTICLES", _optirefine_args0, "of.options.VOID_PARTICLES", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options WATER_PARTICLES = EnumHelper.addEnum(GameSettings.Options.class, "WATER_PARTICLES", _optirefine_args0, "of.options.WATER_PARTICLES", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options RAIN_SPLASH = EnumHelper.addEnum(GameSettings.Options.class, "RAIN_SPLASH", _optirefine_args0, "of.options.RAIN_SPLASH", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options PORTAL_PARTICLES = EnumHelper.addEnum(GameSettings.Options.class, "PORTAL_PARTICLES", _optirefine_args0, "of.options.PORTAL_PARTICLES", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options POTION_PARTICLES = EnumHelper.addEnum(GameSettings.Options.class, "POTION_PARTICLES", _optirefine_args0, "of.options.POTION_PARTICLES", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options FIREWORK_PARTICLES = EnumHelper.addEnum(GameSettings.Options.class, "FIREWORK_PARTICLES", _optirefine_args0, "of.options.FIREWORK_PARTICLES", false, false);
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public private static final GameSettings.Options PROFILER = EnumHelper.addEnum(GameSettings.Options.class, "PROFILER", _optirefine_args0, "of.options.PROFILER", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options DRIPPING_WATER_LAVA = EnumHelper.addEnum(GameSettings.Options.class, "DRIPPING_WATER_LAVA", _optirefine_args0, "of.options.DRIPPING_WATER_LAVA", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options BETTER_SNOW = EnumHelper.addEnum(GameSettings.Options.class, "BETTER_SNOW", _optirefine_args0, "of.options.BETTER_SNOW", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options FULLSCREEN_MODE = EnumHelper.addEnum(GameSettings.Options.class, "FULLSCREEN_MODE", _optirefine_args0, "of.options.FULLSCREEN_MODE", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_TERRAIN = EnumHelper.addEnum(GameSettings.Options.class, "ANIMATED_TERRAIN", _optirefine_args0, "of.options.ANIMATED_TERRAIN", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SWAMP_COLORS = EnumHelper.addEnum(GameSettings.Options.class, "SWAMP_COLORS", _optirefine_args0, "of.options.SWAMP_COLORS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options RANDOM_ENTITIES = EnumHelper.addEnum(GameSettings.Options.class, "RANDOM_ENTITIES", _optirefine_args0, "of.options.RANDOM_ENTITIES", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SMOOTH_BIOMES = EnumHelper.addEnum(GameSettings.Options.class, "SMOOTH_BIOMES", _optirefine_args0, "of.options.SMOOTH_BIOMES", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CUSTOM_FONTS = EnumHelper.addEnum(GameSettings.Options.class, "CUSTOM_FONTS", _optirefine_args0, "of.options.CUSTOM_FONTS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CUSTOM_COLORS = EnumHelper.addEnum(GameSettings.Options.class, "CUSTOM_COLORS", _optirefine_args0, "of.options.CUSTOM_COLORS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SHOW_CAPES = EnumHelper.addEnum(GameSettings.Options.class, "SHOW_CAPES", _optirefine_args0, "of.options.SHOW_CAPES", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CONNECTED_TEXTURES = EnumHelper.addEnum(GameSettings.Options.class, "CONNECTED_TEXTURES", _optirefine_args0, "of.options.CONNECTED_TEXTURES", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CUSTOM_ITEMS = EnumHelper.addEnum(GameSettings.Options.class, "CUSTOM_ITEMS", _optirefine_args0, "of.options.CUSTOM_ITEMS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options AA_LEVEL = EnumHelper.addEnum(GameSettings.Options.class, "AA_LEVEL", _optirefine_args0, "of.options.AA_LEVEL", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options AF_LEVEL = EnumHelper.addEnum(GameSettings.Options.class, "AF_LEVEL", _optirefine_args0, "of.options.AF_LEVEL", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ANIMATED_TEXTURES = EnumHelper.addEnum(GameSettings.Options.class, "ANIMATED_TEXTURES", _optirefine_args0, "of.options.ANIMATED_TEXTURES", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options NATURAL_TEXTURES = EnumHelper.addEnum(GameSettings.Options.class, "NATURAL_TEXTURES", _optirefine_args0, "of.options.NATURAL_TEXTURES", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options EMISSIVE_TEXTURES = EnumHelper.addEnum(GameSettings.Options.class, "EMISSIVE_TEXTURES", _optirefine_args0, "of.options.EMISSIVE_TEXTURES", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options HELD_ITEM_TOOLTIPS = EnumHelper.addEnum(GameSettings.Options.class, "HELD_ITEM_TOOLTIPS", _optirefine_args0, "of.options.HELD_ITEM_TOOLTIPS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options DROPPED_ITEMS = EnumHelper.addEnum(GameSettings.Options.class, "DROPPED_ITEMS", _optirefine_args0, "of.options.DROPPED_ITEMS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options LAZY_CHUNK_LOADING = EnumHelper.addEnum(GameSettings.Options.class, "LAZY_CHUNK_LOADING", _optirefine_args0, "of.options.LAZY_CHUNK_LOADING", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CUSTOM_SKY = EnumHelper.addEnum(GameSettings.Options.class, "CUSTOM_SKY", _optirefine_args0, "of.options.CUSTOM_SKY", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options FAST_MATH = EnumHelper.addEnum(GameSettings.Options.class, "FAST_MATH", _optirefine_args0, "of.options.FAST_MATH", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options FAST_RENDER = EnumHelper.addEnum(GameSettings.Options.class, "FAST_RENDER", _optirefine_args0, "of.options.FAST_RENDER", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options TRANSLUCENT_BLOCKS = EnumHelper.addEnum(GameSettings.Options.class, "TRANSLUCENT_BLOCKS", _optirefine_args0, "of.options.TRANSLUCENT_BLOCKS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options DYNAMIC_FOV = EnumHelper.addEnum(GameSettings.Options.class, "DYNAMIC_FOV", _optirefine_args0, "of.options.DYNAMIC_FOV", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options DYNAMIC_LIGHTS = EnumHelper.addEnum(GameSettings.Options.class, "DYNAMIC_LIGHTS", _optirefine_args0, "of.options.DYNAMIC_LIGHTS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ALTERNATE_BLOCKS = EnumHelper.addEnum(GameSettings.Options.class, "ALTERNATE_BLOCKS", _optirefine_args0, "of.options.ALTERNATE_BLOCKS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CUSTOM_ENTITY_MODELS = EnumHelper.addEnum(GameSettings.Options.class, "CUSTOM_ENTITY_MODELS", _optirefine_args0, "of.options.CUSTOM_ENTITY_MODELS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options ADVANCED_TOOLTIPS = EnumHelper.addEnum(GameSettings.Options.class, "ADVANCED_TOOLTIPS", _optirefine_args0, "of.options.ADVANCED_TOOLTIPS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SCREENSHOT_SIZE = EnumHelper.addEnum(GameSettings.Options.class, "SCREENSHOT_SIZE", _optirefine_args0, "of.options.SCREENSHOT_SIZE", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options CUSTOM_GUIS = EnumHelper.addEnum(GameSettings.Options.class, "CUSTOM_GUIS", _optirefine_args0, "of.options.CUSTOM_GUIS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options RENDER_REGIONS = EnumHelper.addEnum(GameSettings.Options.class, "RENDER_REGIONS", _optirefine_args0, "of.options.RENDER_REGIONS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SHOW_GL_ERRORS = EnumHelper.addEnum(GameSettings.Options.class, "SHOW_GL_ERRORS", _optirefine_args0, "of.options.SHOW_GL_ERRORS", false, false);
    @SuppressWarnings("unused")
    @Public private static final GameSettings.Options SMART_ANIMATIONS = EnumHelper.addEnum(GameSettings.Options.class, "SMART_ANIMATIONS", _optirefine_args0,"of.options.SMART_ANIMATIONS", false, false);

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
