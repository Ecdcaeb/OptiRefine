package mods.Hileb.optirefine.validation;

import mods.Hileb.optirefine.optifine.Config;
import mods.Hileb.optirefine.optifine.client.GameSettingsOptionOF;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.Locale;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItems;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Exercises option-triggered reloads against the live native resource consumers. */
public final class SelectiveResourceContracts {
    private static final ModelResourceLocation FIXTURE_MODEL = new ModelResourceLocation("mcpatcher/cit/modern", "inventory");
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/blocks/diamond_block.png");

    private SelectiveResourceContracts() {}

    /** Run on the client thread after modern-base.zip is refreshed, before enabling shader packs. */
    public static void verify(Minecraft mc) throws Exception {
        require(mc.isCallingFromMinecraftThread(), "Selective resource contracts require the client thread");
        require(!Config.isShaders(), "Selective resource contracts must precede shader pack activation");
        ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
        try (NativeState nativeState = new NativeState(mc);
             TextureProbe texture = new TextureProbe(mc);
             OptionsState options = new OptionsState(mc.gameSettings)) {
            assertSword(mc, sword, options.customItems);
            exercise(mc, sword, texture, nativeState, "CUSTOM_ITEMS", () ->
                    mc.gameSettings.setOptionValue(GameSettingsOptionOF.CUSTOM_ITEMS, 1));
            require(options.customItems != customItems(mc.gameSettings), "CUSTOM_ITEMS did not toggle");
            assertSword(mc, sword, !options.customItems);
            exercise(mc, sword, texture, nativeState, "CUSTOM_ITEMS restored", () ->
                    mc.gameSettings.setOptionValue(GameSettingsOptionOF.CUSTOM_ITEMS, 1));
            assertSword(mc, sword, options.customItems);

            int alternateAf = options.afLevel == 1 ? 2 : 1;
            exercise(mc, sword, texture, nativeState, "AF_LEVEL", () ->
                    mc.gameSettings.setOptionFloatValue(GameSettingsOptionOF.AF_LEVEL, alternateAf));
            require(afLevel(mc.gameSettings) == alternateAf, "AF_LEVEL did not change through the option API");
            exercise(mc, sword, texture, nativeState, "AF_LEVEL restored", () ->
                    mc.gameSettings.setOptionFloatValue(GameSettingsOptionOF.AF_LEVEL, options.afLevel));
            require(afLevel(mc.gameSettings) == options.afLevel, "AF_LEVEL was not restored");

            exercise(mc, sword, texture, nativeState, "ALTERNATE_BLOCKS", () ->
                    mc.gameSettings.setOptionValue(GameSettingsOptionOF.ALTERNATE_BLOCKS, 1));
            require(options.alternateBlocks != alternateBlocks(mc.gameSettings), "ALTERNATE_BLOCKS did not toggle");
            exercise(mc, sword, texture, nativeState, "ALTERNATE_BLOCKS restored", () ->
                    mc.gameSettings.setOptionValue(GameSettingsOptionOF.ALTERNATE_BLOCKS, 1));
            require(options.alternateBlocks == alternateBlocks(mc.gameSettings), "ALTERNATE_BLOCKS was not restored");
        }
    }

    private static void exercise(Minecraft mc, ItemStack sword, TextureProbe texture, NativeState nativeState,
                                 String label, Runnable change) throws Exception {
        IBakedModel oldSword = vanillaSword(mc, sword);
        TextureAtlasSprite oldStone = mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone");
        // Model baking also rebuilds the atlas. A separate SimpleTexture proves TEXTURES was included.
        texture.invalidate();
        change.run();
        // Cleanroom executes scheduled reloads immediately when called on the Minecraft thread.
        require(vanillaSword(mc, sword) != oldSword, label + " retained the old baked item model");
        require(mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone") != oldStone,
                label + " retained the old stitched atlas sprite");
        texture.assertReloaded(label);
        assertSword(mc, sword, customItems(mc.gameSettings));
        nativeState.assertUnchanged(label);
    }

    private static IBakedModel vanillaSword(Minecraft mc, ItemStack sword) {
        return mc.getRenderItem().getItemModelMesher().getItemModel(sword);
    }

    private static void assertSword(Minecraft mc, ItemStack sword, boolean enabled) {
        IBakedModel vanilla = vanillaSword(mc, sword);
        IBakedModel rendered = mc.getRenderItem().getItemModelWithOverrides(sword, mc.world, mc.player);
        IBakedModel custom = CustomItems.getCustomItemModel(sword, vanilla, null, true);
        require(Config.isCustomItems() == enabled, "Custom-items consumer disagrees with the option");
        if (!enabled) {
            require(rendered == vanilla, "Disabled custom items still select a CIT model in RenderItem");
            require(custom == vanilla, "Disabled custom items retained their native loaded CIT rules/models");
            require(vanilla.getParticleTexture() == mc.getTextureMapBlocks().getAtlasSprite("minecraft:items/diamond_sword"),
                    "Disabled custom items did not restore the native diamond-sword texture");
            return;
        }
        IBakedModel fixture = mc.getRenderItem().getItemModelMesher().getModelManager().getModel(FIXTURE_MODEL);
        require(fixture != mc.getRenderItem().getItemModelMesher().getModelManager().getMissingModel(),
                "Modern fixture CIT model was not baked");
        require(rendered == fixture && custom == fixture && rendered != vanilla,
                "Native diamond-sword consumers did not select the installed modern fixture model");
        TextureAtlasSprite diamond = mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/diamond_block");
        require(fixture.getParticleTexture() == diamond, "Modern fixture model has the wrong particle texture");
        int quads = assertQuads(fixture.getQuads(null, null, 0), diamond);
        for (EnumFacing face : EnumFacing.values()) quads += assertQuads(fixture.getQuads(null, face, 0), diamond);
        require(quads > 0, "Modern fixture model contains no rendered geometry");
    }

    private static int assertQuads(List<BakedQuad> quads, TextureAtlasSprite expected) {
        for (BakedQuad quad : quads) require(quad.getSprite() == expected, "CIT geometry uses a stale or incorrect atlas sprite");
        return quads.size();
    }

    /** Uses the ordinary native loader and an actual resource, without registering a reload listener. */
    private static final class TextureProbe implements AutoCloseable {
        private final TextureManager manager;
        private final Map<ResourceLocation, ITextureObject> textures;
        private final ITextureObject previous;
        private final SimpleTexture probe = new SimpleTexture(TEXTURE);
        private final Field textureId = field(AbstractTexture.class, "glTextureId", "field_110553_a");
        private final int width, height;

        @SuppressWarnings("unchecked")
        private TextureProbe(Minecraft mc) throws Exception {
            manager = mc.getTextureManager();
            textures = (Map<ResourceLocation, ITextureObject>) field(TextureManager.class, "mapTextureObjects", "field_110585_a").get(manager);
            previous = textures.get(TEXTURE);
            try (IResource resource = mc.getResourceManager().getResource(TEXTURE)) {
                BufferedImage image = ImageIO.read(resource.getInputStream());
                require(image != null, "Native texture probe resource is not an image");
                width = image.getWidth();
                height = image.getHeight();
            }
            int binding = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
            try {
                require(manager.loadTexture(TEXTURE, probe), "Native texture probe could not load its resource");
                assertReloaded("Initial native texture load");
            } catch (Exception | Error failure) {
                close();
                throw failure;
            } finally {
                GlStateManager.bindTexture(binding);
            }
        }

        private void invalidate() {
            probe.deleteGlTexture();
        }

        private void assertReloaded(String label) throws IllegalAccessException {
            int id = textureId.getInt(probe);
            require(textures.get(TEXTURE) == probe && id >= 0 && GL11.glIsTexture(id),
                    label + " did not reload the actual native SimpleTexture");
            int binding = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
            try {
                GlStateManager.bindTexture(id);
                require(GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) == width
                                && GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT) == height,
                        label + " did not upload the loaded texture resource");
            } finally {
                GlStateManager.bindTexture(binding);
            }
        }

        @Override public void close() {
            if (previous == null) textures.remove(TEXTURE, probe);
            else textures.put(TEXTURE, previous);
            probe.deleteGlTexture();
        }
    }

    private static final class NativeState implements AutoCloseable {
        private final Minecraft mc;
        private final SoundManager soundManager;
        private final Object engine;
        private final Field loaded = field(SoundManager.class, "loaded", "field_148617_f");
        private final Field soundSystem = field(SoundManager.class, "sndSystem", "field_148620_e");
        private final Object locale;
        private final Map<String, String> translations;
        private final Map<String, String> expectedTranslations;
        private final String sentinel = "optirefine.contract.selective." + UUID.randomUUID();
        private final List<?> reloadListeners, soundListeners;
        private final List<?> originalReloadListeners, originalSoundListeners;

        @SuppressWarnings("unchecked")
        private NativeState(Minecraft mc) throws Exception {
            this.mc = mc;
            soundManager = (SoundManager) field(SoundHandler.class, "sndManager", "field_147694_f").get(mc.getSoundHandler());
            engine = soundSystem.get(soundManager);
            require(engine != null && loaded.getBoolean(soundManager), "Selective contracts require the live sound engine to finish starting");
            locale = field(I18n.class, "i18nLocale", "field_135054_a").get(null);
            translations = (Map<String, String>) field(Locale.class, "properties", "field_135032_a").get(locale);
            require(I18n.hasKey("options.language"), "Native language resources are not loaded");
            reloadListeners = (List<?>) field(SimpleReloadableResourceManager.class, "reloadListeners", "field_110546_b").get(mc.getResourceManager());
            soundListeners = (List<?>) field(SoundManager.class, "listeners", "field_188777_o").get(soundManager);
            originalReloadListeners = new ArrayList<>(reloadListeners);
            originalSoundListeners = new ArrayList<>(soundListeners);
            translations.put(sentinel, "Native language state survives selective reload");
            expectedTranslations = new HashMap<>(translations);
        }

        private void assertUnchanged(String label) throws Exception {
            require(field(SoundHandler.class, "sndManager", "field_147694_f").get(mc.getSoundHandler()) == soundManager
                            && soundSystem.get(soundManager) == engine && loaded.getBoolean(soundManager),
                    label + " restarted or unloaded the live native sound engine");
            require(field(I18n.class, "i18nLocale", "field_135054_a").get(null) == locale
                            && field(Locale.class, "properties", "field_135032_a").get(locale) == translations
                            && expectedTranslations.equals(translations)
                            && expectedTranslations.get(sentinel).equals(I18n.format(sentinel)),
                    label + " reloaded native language resources");
            require(sameListeners(reloadListeners, originalReloadListeners) && sameListeners(soundListeners, originalSoundListeners),
                    label + " changed external resource or sound listeners");
        }

        @Override public void close() {
            translations.remove(sentinel);
        }
    }

    private static final class OptionsState implements AutoCloseable {
        private final GameSettings settings;
        private final boolean customItems, alternateBlocks;
        private final int afLevel;

        private OptionsState(GameSettings settings) throws Exception {
            this.settings = settings;
            customItems = customItems(settings);
            alternateBlocks = alternateBlocks(settings);
            afLevel = afLevel(settings);
        }

        @Override public void close() throws Exception {
            try {
                if (afLevel(settings) != afLevel) settings.setOptionFloatValue(GameSettingsOptionOF.AF_LEVEL, afLevel);
            } finally {
                try {
                    if (customItems(settings) != customItems) settings.setOptionValue(GameSettingsOptionOF.CUSTOM_ITEMS, 1);
                } finally {
                    if (alternateBlocks(settings) != alternateBlocks) settings.setOptionValue(GameSettingsOptionOF.ALTERNATE_BLOCKS, 1);
                }
            }
        }
    }

    private static boolean sameListeners(List<?> actual, List<?> expected) {
        if (actual.size() != expected.size()) return false;
        for (int i = 0; i < actual.size(); i++) if (actual.get(i) != expected.get(i)) return false;
        return true;
    }

    private static boolean customItems(GameSettings settings) throws Exception {
        return GameSettings.class.getField("ofCustomItems").getBoolean(settings);
    }

    private static boolean alternateBlocks(GameSettings settings) throws Exception {
        return GameSettings.class.getField("ofAlternateBlocks").getBoolean(settings);
    }

    private static int afLevel(GameSettings settings) throws Exception {
        return GameSettings.class.getField("ofAfLevel").getInt(settings);
    }

    private static Field field(Class<?> owner, String mcp, String srg) throws NoSuchFieldException {
        Field result;
        try { result = owner.getDeclaredField(mcp); }
        catch (NoSuchFieldException ignored) { result = owner.getDeclaredField(srg); }
        result.setAccessible(true);
        return result;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
