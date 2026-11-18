package mods.Hileb.optirefine.validation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.PngSizeInfo;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.optifine.SmartAnimations;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersTex;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class NpotAtlasContracts {
    private static final int MIP_LEVEL = 4;
    private static final String NAMESPACE = "optirefine_npot";
    private static final ResourceLocation WIDE = sprite("wide");
    private static final ResourceLocation TALL = sprite("tall");
    private static final ResourceLocation ANIMATED = sprite("animated");
    private static final ResourceLocation CUSTOM = sprite("custom");
    private static final ResourceLocation SKIPPED = sprite("skipped");
    private static final int RED = 0xFFFF0000;
    private static final int GREEN = 0xFF00FF00;
    private static final int BLUE = 0xFF0000FF;
    private static final int ORANGE = 0xFFFF7F00;
    private static final int PURPLE = 0xFFBF40BF;
    private static final int NORMAL = 0xFF4080FF;
    private static final int SPECULAR = 0xFF606060;

    private NpotAtlasContracts() {}

    public static void verify(Minecraft mc) throws Exception {
        exercise(mc, false);
    }

    public static void verifyShaderSidecars(Minecraft mc) throws Exception {
        exercise(mc, true);
    }

    private static void exercise(Minecraft mc, boolean shaders) throws Exception {
        GameSettings settings = mc.gameSettings;
        TextureMap atlas = mc.getTextureMapBlocks();
        int mipmaps = settings.mipmapLevels;
        int atlasMipmaps = atlas.getMipmapLevels();
        Field mipType = GameSettings.class.getField("ofMipmapType");
        Field afLevel = GameSettings.class.getField("ofAfLevel");
        Field smart = GameSettings.class.getField("ofSmartAnimations");
        Field terrain = GameSettings.class.getField("ofAnimatedTerrain");
        int previousType = mipType.getInt(settings);
        int previousAf = afLevel.getInt(settings);
        boolean previousSmart = smart.getBoolean(settings);
        boolean previousTerrain = terrain.getBoolean(settings);
        boolean previousNormal = Shaders.configNormalMap;
        boolean previousSpecular = Shaders.configSpecularMap;
        ResourcePackRepository repository = mc.getResourcePackRepository();
        List<ResourcePackRepository.Entry> previous = List.copyOf(repository.getRepositoryEntries());
        Path fixture = Files.createTempFile(mc.gameDir.toPath().resolve("resourcepacks"), "optirefine-npot-", ".zip");
        int binding = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        boolean atlasWasBound = binding == atlas.getGlTextureId();
        Registration registration = new Registration(atlas, shaders);
        ResourcePackRepository.Entry fixtureEntry = null;
        try {
            createPack(fixture);
            repository.updateRepositoryEntriesAll();
            fixtureEntry = repository.getRepositoryEntriesAll().stream()
                    .filter(entry -> entry.getResourcePackName().equals(fixture.getFileName().toString()))
                    .findFirst().orElseThrow(() -> new AssertionError("NPOT fixture pack was not discovered"));
            List<ResourcePackRepository.Entry> selected = new ArrayList<>(previous);
            selected.add(fixtureEntry);
            repository.setRepositories(selected);
            settings.mipmapLevels = MIP_LEVEL;
            atlas.setMipmapLevels(MIP_LEVEL);
            mipType.setInt(settings, 3);
            afLevel.setInt(settings, shaders ? 1 : 2);
            smart.setBoolean(settings, false);
            SmartAnimations.update();
            terrain.setBoolean(settings, true);
            if (shaders) {
                Shaders.configNormalMap = true;
                Shaders.configSpecularMap = true;
            }
            MinecraftForge.EVENT_BUS.register(registration);
            mc.refreshResources();
            require(registration.post, "Forge did not post the block atlas stitch event");
            if (registration.failure != null) throw registration.failure;
        } finally {
            MinecraftForge.EVENT_BUS.unregister(registration);
            settings.mipmapLevels = mipmaps;
            atlas.setMipmapLevels(atlasMipmaps);
            mipType.setInt(settings, previousType);
            afLevel.setInt(settings, previousAf);
            smart.setBoolean(settings, previousSmart);
            SmartAnimations.update();
            terrain.setBoolean(settings, previousTerrain);
            Shaders.configNormalMap = previousNormal;
            Shaders.configSpecularMap = previousSpecular;
            repository.setRepositories(previous);
            try {
                mc.refreshResources();
            } finally {
                if (fixtureEntry != null) {
                    fixtureEntry.closeResourcePack();
                }
                Files.deleteIfExists(fixture);
                repository.updateRepositoryEntriesAll();
                GlStateManager.bindTexture(atlasWasBound ? atlas.getGlTextureId() : GL11.glIsTexture(binding) ? binding : 0);
            }
        }
    }

    private static void verifyAtlas(TextureMap atlas, Registration registration, boolean shaders) throws ReflectiveOperationException {
        require(registration.supplied.loadedSize && registration.supplied.loadedPixels,
                "Forge resource-backed sprite did not pass its normal load hooks");
        require(atlas.getMipmapLevels() == MIP_LEVEL, "NPOT sprites lowered the block atlas mipmap level");
        TextureAtlasSprite wide = atlas.getAtlasSprite(WIDE.toString());
        TextureAtlasSprite tall = atlas.getAtlasSprite(TALL.toString());
        TextureAtlasSprite animated = atlas.getAtlasSprite(ANIMATED.toString());
        TextureAtlasSprite custom = atlas.getAtlasSprite(CUSTOM.toString());
        require(tall == registration.supplied, "Atlas replaced the Forge-supplied sprite");
        require(custom == registration.custom && registration.custom.loads == 1 && registration.skipped.loads == 1,
                "Forge custom loaders lost their load or extraction behavior");
        require(atlas.getAtlasSprite(SKIPPED.toString()) == atlas.getMissingSprite(), "Atlas stitched an excluded custom sprite");
        checkFrames(wide, 384, 64, 0);
        checkFrames(tall, 16, 48, 0);
        checkFrames(animated, 32, 32, 0, 2);
        checkFrames(custom, 16, 16, 0);
        require(animated.getFrameCount() == 3 && animated.getFrameTextureData(1) == null,
                "Mip generation compacted sparse pack animation indices");

        int base = atlas.getGlTextureId();
        GlStateManager.bindTexture(base);
        require(GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL) == MIP_LEVEL,
                "Block atlas did not allocate four GL mip levels");
        if (!shaders) {
            require(GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER) == GL11.GL_LINEAR_MIPMAP_LINEAR,
                    "G5 trilinear multitexture setting was lost");
        }
        for (int level = 0; level <= MIP_LEVEL; level++) {
            ImageLevel image = ImageLevel.read(base, level);
            image.verify(wide, 0, level);
            image.verify(tall, 0, level);
            image.verify(animated, 2, level);
            image.verify(custom, 0, level);
        }
        ImageLevel image = ImageLevel.read(base, 0);
        require(image.pixel(wide.getOriginX(), wide.getOriginY()) == RED
                        && image.pixel(wide.getOriginX() + 383, wide.getOriginY() + 63) == GREEN,
                "Wide sprite lost its ends or orientation during resampling");
        require(image.pixel(tall.getOriginX(), tall.getOriginY()) == ORANGE
                        && image.pixel(tall.getOriginX() + 15, tall.getOriginY() + 47) == PURPLE,
                "Height-only resampling lost the bottom of the Forge sprite");
        require(image.pixel(animated.getOriginX(), animated.getOriginY() + 31) == BLUE,
                "Resampling blended a neighboring frame into the initial frame");

        TextureAtlasSprite animatedVariant;
        int animatedVariantId;
        if (shaders) {
            MultiTexID maps = ShadersTex.getMultiTexID(atlas);
            animatedVariant = sidecar(animated, "spriteNormal");
            animatedVariantId = maps.norm;
            TextureAtlasSprite normal = sidecar(wide, "spriteNormal");
            TextureAtlasSprite specular = sidecar(wide, "spriteSpecular");
            checkFrames(normal, 384, 64, 0);
            checkFrames(specular, 384, 64, 0);
            require(normal.getFrameTextureData(0)[0][0] == NORMAL && specular.getFrameTextureData(0)[0][0] == SPECULAR,
                    "Resized shader sidecars were replaced by G5 defaults");
            for (int level = 0; level <= MIP_LEVEL; level++) {
                ImageLevel.read(maps.norm, level).verify(normal, 0, level);
                ImageLevel.read(maps.spec, level).verify(specular, 0, level);
            }
        } else {
            animatedVariant = sidecar(animated, "spriteSingle");
            animatedVariantId = TextureAtlasSprite.class.getField("glSpriteTextureId").getInt(animated);
        }
        checkFrames(animatedVariant, 32, 32, 0, 2);
        ImageLevel.read(animatedVariantId, MIP_LEVEL).verify(animatedVariant, 2, MIP_LEVEL);
        atlas.updateAnimations();
        for (int level = 0; level <= MIP_LEVEL; level++) {
            image = ImageLevel.read(base, level);
            int pixel = image.pixel(animated.getOriginX() >> level, animated.getOriginY() >> level);
            require(pixel == 0xFF007F7F, "Interpolated NPOT frame was not uploaded at mip level " + level);
        }
        atlas.updateAnimations();
        for (int level = 0; level <= MIP_LEVEL; level++) {
            ImageLevel.read(base, level).verify(animated, 0, level);
        }
        ImageLevel.read(animatedVariantId, MIP_LEVEL).verify(animatedVariant, 0, MIP_LEVEL);
        require(GL11.glGetError() == GL11.GL_NO_ERROR, "GL rejected an NPOT atlas upload");
    }

    private static TextureAtlasSprite sidecar(TextureAtlasSprite sprite, String field) throws ReflectiveOperationException {
        TextureAtlasSprite result = (TextureAtlasSprite) TextureAtlasSprite.class.getField(field).get(sprite);
        require(result != null, "Sprite is missing " + field);
        return result;
    }

    private static void checkFrames(TextureAtlasSprite sprite, int width, int height, int... frames) {
        require(sprite.getIconWidth() == width && sprite.getIconHeight() == height,
                "Wrong aligned sprite bounds for " + sprite.getIconName());
        require(((sprite.getOriginX() | sprite.getOriginY()) & ((1 << MIP_LEVEL) - 1)) == 0,
                "Sprite origin is off the mip grid for " + sprite.getIconName());
        for (int frame : frames) {
            int[][] data = sprite.getFrameTextureData(frame);
            require(data.length == MIP_LEVEL + 1, "Wrong frame mip count for " + sprite.getIconName());
            for (int level = 0; level <= MIP_LEVEL; level++) {
                require(data[level] != null && data[level].length == (width >> level) * (height >> level),
                        "Wrong frame mip storage for " + sprite.getIconName() + " at level " + level);
            }
        }
    }

    private static void createPack(Path file) throws IOException {
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(file))) {
            text(zip, "pack.mcmeta", "{\"pack\":{\"pack_format\":3,\"description\":\"NPOT atlas validation\"}}");
            png(zip, "wide", 380, 53, (x, y) -> x < 190 ? RED : GREEN);
            png(zip, "tall", 16, 37, (x, y) -> y < 18 ? ORANGE : PURPLE);
            png(zip, "animated", 21, 63, (x, y) -> y < 21 ? GREEN : y < 42 ? RED : BLUE);
            text(zip, path("animated") + ".mcmeta", "{\"animation\":{\"frametime\":2,\"interpolate\":true,\"frames\":[2,0]}}");
            png(zip, "wide_n", 190, 27, (x, y) -> NORMAL);
            png(zip, "wide_s", 95, 14, (x, y) -> SPECULAR);
            png(zip, "animated_n", 11, 33, (x, y) -> y < 11 ? NORMAL : y < 22 ? 0xFF8080FF : 0xFFB080FF);
            text(zip, path("animated_n") + ".mcmeta", "{\"animation\":{\"frametime\":2,\"interpolate\":true,\"frames\":[2,0]}}");
        }
    }

    private static void png(ZipOutputStream zip, String name, int width, int height, PixelSource pixels) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) image.setRGB(x, y, pixels.pixel(x, y));
        }
        zip.putNextEntry(new ZipEntry(path(name)));
        ImageIO.write(image, "png", zip);
        zip.closeEntry();
    }

    private static void text(ZipOutputStream zip, String name, String contents) throws IOException {
        zip.putNextEntry(new ZipEntry(name));
        zip.write(contents.getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
    }

    private static String path(String name) {
        return "assets/" + NAMESPACE + "/textures/blocks/" + name + ".png";
    }

    private static ResourceLocation sprite(String name) {
        return new ResourceLocation(NAMESPACE, "blocks/" + name);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    @FunctionalInterface
    private interface PixelSource {
        int pixel(int x, int y);
    }

    private record ImageLevel(int width, int height, IntBuffer pixels) {
        static ImageLevel read(int texture, int level) {
            GlStateManager.bindTexture(texture);
            int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, level, GL11.GL_TEXTURE_WIDTH);
            int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, level, GL11.GL_TEXTURE_HEIGHT);
            require(width > 0 && height > 0, "GL texture " + texture + " has no mip level " + level);
            IntBuffer pixels = BufferUtils.createIntBuffer(width * height);
            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, level, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixels);
            return new ImageLevel(width, height, pixels);
        }

        int pixel(int x, int y) {
            return pixels.get(y * width + x);
        }

        void verify(TextureAtlasSprite sprite, int frame, int level) {
            int x = sprite.getOriginX() >> level;
            int y = sprite.getOriginY() >> level;
            int spriteWidth = sprite.getIconWidth() >> level;
            int spriteHeight = sprite.getIconHeight() >> level;
            require(x + spriteWidth <= width && y + spriteHeight <= height, "Sprite extends past GL mip storage");
            int[] expected = sprite.getFrameTextureData(frame)[level];
            for (int row = 0; row < spriteHeight; ++row) {
                for (int column = 0; column < spriteWidth; ++column) {
                    if (pixel(x + column, y + row) != expected[row * spriteWidth + column]) {
                        throw new AssertionError("Wrong atlas pixels for " + sprite.getIconName() + " at mip level " + level);
                    }
                }
            }
        }
    }

    public static final class Registration {
        private final TextureMap atlas;
        private final boolean shaders;
        private final ResourceSprite supplied = new ResourceSprite();
        private final CustomSprite custom = new CustomSprite(CUSTOM, false);
        private final CustomSprite skipped = new CustomSprite(SKIPPED, true);
        private boolean post;
        private AssertionError failure;

        private Registration(TextureMap atlas, boolean shaders) {
            this.atlas = atlas;
            this.shaders = shaders;
        }

        @SubscribeEvent
        public void before(TextureStitchEvent.Pre event) {
            if (event.getMap() == atlas) {
                atlas.registerSprite(WIDE);
                atlas.registerSprite(ANIMATED);
                require(atlas.setTextureEntry(supplied) && atlas.setTextureEntry(custom) && atlas.setTextureEntry(skipped),
                        "Forge rejected fixture sprite registration");
            }
        }

        @SubscribeEvent
        public void after(TextureStitchEvent.Post event) {
            if (event.getMap() != atlas) return;
            post = true;
            // G5 ticks the atlas during resource reload, after the initial upload.
            int binding = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
            try {
                verifyAtlas(atlas, this, shaders);
            } catch (AssertionError error) {
                failure = error;
            } catch (ReflectiveOperationException error) {
                failure = new AssertionError("Unable to inspect the stitched atlas", error);
            } finally {
                GlStateManager.bindTexture(binding);
            }
        }
    }

    private static final class ResourceSprite extends TextureAtlasSprite {
        private boolean loadedSize;
        private boolean loadedPixels;

        private ResourceSprite() {
            super(TALL.toString());
        }

        @Override
        public void loadSprite(PngSizeInfo size, boolean animated) throws IOException {
            // Resource-backed Forge sprites can supply rectangular bounds even in the legacy path.
            super.loadSprite(size, true);
            if (!animated) {
                setIconHeight(size.pngHeight);
            }
            loadedSize = true;
        }

        @Override
        public void loadSpriteFrames(IResource resource, int levels) throws IOException {
            super.loadSpriteFrames(resource, levels);
            loadedPixels = true;
        }
    }

    private static final class CustomSprite extends TextureAtlasSprite {
        private final boolean skip;
        private int loads;

        private CustomSprite(ResourceLocation name, boolean skip) {
            super(name.toString());
            this.skip = skip;
        }

        @Override
        public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
            return true;
        }

        @Override
        public boolean load(IResourceManager manager, ResourceLocation location, Function<ResourceLocation, TextureAtlasSprite> sprites) {
            ++loads;
            if (!skip) {
                setIconWidth(16);
                setIconHeight(16);
                int[] pixels = new int[16 * 16];
                Arrays.fill(pixels, 0xFF778899);
                setFramesTextureData(List.<int[][]>of(new int[][]{pixels}));
            }
            return skip;
        }
    }
}
