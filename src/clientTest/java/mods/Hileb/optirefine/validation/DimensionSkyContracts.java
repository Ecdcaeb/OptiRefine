package mods.Hileb.optirefine.validation;

import mods.Hileb.optirefine.optifine.Config;
import mods.Hileb.optirefine.optifine.client.GameSettingsOptionOF;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.IRenderHandler;
import net.optifine.CustomSky;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/** Exercises G5 sky selection with real resource packs and the live Forge sky hook. */
final class DimensionSkyContracts {
    private static final int HIGH_DIMENSION = Integer.MAX_VALUE;
    private static final int NEGATIVE_DIMENSION = Integer.MIN_VALUE;

    private DimensionSkyContracts() {}

    static void verify(Minecraft mc) throws IOException {
        ResourcePackRepository repository = mc.getResourcePackRepository();
        List<ResourcePackRepository.Entry> original = List.copyOf(repository.getRepositoryEntries());
        boolean enabled = Config.isCustomSky();
        Path directory = repository.getDirResourcepacks().toPath();
        Path base = Files.createTempFile(directory, "dimension-sky-base-", ".zip");
        Path higher = null;
        boolean fixturesSelected = false;
        try {
            higher = Files.createTempFile(directory, "dimension-sky-higher-", ".zip");
            writePacks(base, higher);
            repository.updateRepositoryEntriesAll();
            List<ResourcePackRepository.Entry> selected = new ArrayList<>(original);
            selected.add(pack(repository, base));
            repository.setRepositories(selected);
            fixturesSelected = true;
            if (!enabled) {
                mc.gameSettings.setOptionValue(GameSettingsOptionOF.CUSTOM_SKY, 1);
            }
            mc.refreshResources();

            WorldClient world = mc.world;
            require(world.provider.getDimensionType() == DimensionType.OVERWORLD
                            && world.provider.getDimension() == 0,
                    "Sky contract requires a real Overworld provider");
            require(CustomSky.hasSkyLayers(world), "Vanilla world0 layers were lost");
            // Assign IDs after reload while keeping the real Overworld provider type.
            requireLayers(world, HIGH_DIMENSION, true);
            requireLayers(world, NEGATIVE_DIMENSION, true);
            requireLayers(world, HIGH_DIMENSION - 1, false);
            requireTexture(mc, HIGH_DIMENSION, "modern", true);
            requireTexture(mc, HIGH_DIMENSION, "legacy", false);
            requireTexture(mc, HIGH_DIMENSION, "gap", false);
            requireTexture(mc, NEGATIVE_DIMENSION, "negative", true);
            verifyForgeRenderer(mc);

            selected.add(pack(repository, higher));
            repository.setRepositories(selected);
            mc.refreshResources();
            requireLayers(world, HIGH_DIMENSION, true);
            requireTexture(mc, HIGH_DIMENSION, "higher", true);
            requireTexture(mc, HIGH_DIMENSION, "modern", false);

            mc.gameSettings.setOptionValue(GameSettingsOptionOF.CUSTOM_SKY, 1);
            requireLayers(world, HIGH_DIMENSION, false);
            requireLayers(world, NEGATIVE_DIMENSION, false);
            mc.gameSettings.setOptionValue(GameSettingsOptionOF.CUSTOM_SKY, 1);
            requireLayers(world, HIGH_DIMENSION, true);

            repository.setRepositories(original);
            mc.refreshResources();
            fixturesSelected = false;
            requireLayers(world, HIGH_DIMENSION, false);
            requireLayers(world, NEGATIVE_DIMENSION, false);
        } finally {
            repository.setRepositories(original);
            try {
                if (Config.isCustomSky() != enabled) {
                    mc.gameSettings.setOptionValue(GameSettingsOptionOF.CUSTOM_SKY, 1);
                }
                if (fixturesSelected) {
                    mc.refreshResources();
                }
            } finally {
                removePack(repository, base);
                removePack(repository, higher);
                repository.updateRepositoryEntriesAll();
            }
        }
    }

    private static void requireLayers(WorldClient world, int dimension, boolean expected) {
        int previous = world.provider.getDimension();
        try {
            world.provider.setDimension(dimension);
            require(CustomSky.hasSkyLayers(world) == expected, "Unexpected sky layers for dimension " + dimension);
        } finally {
            world.provider.setDimension(previous);
        }
    }

    private static void verifyForgeRenderer(Minecraft mc) {
        WorldClient world = mc.world;
        WorldProvider provider = world.provider;
        IRenderHandler previous = provider.getSkyRenderer();
        int dimension = provider.getDimension();
        long time = world.getWorldTime();
        int binding = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        int[] calls = {0};
        try {
            provider.setDimension(HIGH_DIMENSION);
            world.setWorldTime(6000L);
            provider.setSkyRenderer(new IRenderHandler() {
                @Override
                public void render(float partialTicks, WorldClient renderedWorld, Minecraft minecraft) {
                    require(renderedWorld == world && minecraft == mc && partialTicks == 0.25F,
                            "Forge sky renderer received the wrong render context");
                    require(GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) == 0,
                            "A sky layer ran before the Forge sky renderer");
                    ++calls[0];
                }
            });
            GlStateManager.bindTexture(0);
            mc.renderGlobal.renderSky(0.25F, 2);
            require(calls[0] == 1, "Forge sky renderer did not retain ownership of its dimension");
            require(GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) == 0,
                    "A sky layer ran after the Forge sky renderer");
        } finally {
            provider.setSkyRenderer(previous);
            provider.setDimension(dimension);
            world.setWorldTime(time);
            GlStateManager.bindTexture(binding);
        }
    }

    private static void requireTexture(Minecraft mc, int dimension, String name, boolean expected) {
        ResourceLocation texture = new ResourceLocation("mcpatcher/sky/world" + dimension + "/contract-" + name + ".png");
        require((mc.getTextureManager().getTexture(texture) != null) == expected,
                "Unexpected G5 load result for " + texture);
    }

    private static void writePacks(Path base, Path higher) throws IOException {
        BufferedImage sky = new BufferedImage(12, 8, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < sky.getHeight(); ++y) {
            for (int x = 0; x < sky.getWidth(); ++x) {
                sky.setRGB(x, y, 0x336699);
            }
        }
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(base))) {
            writeMetadata(zip);
            writeLayer(zip, "optifine", 0, 1, "overworld", sky);
            writeLayer(zip, "optifine", HIGH_DIMENSION, 1, "modern", sky);
            writeLayer(zip, "mcpatcher", HIGH_DIMENSION, 1, "legacy", sky);
            writeLayer(zip, "optifine", HIGH_DIMENSION, 3, "gap", sky);
            writeLayer(zip, "mcpatcher", NEGATIVE_DIMENSION, 1, "negative", sky);
        }
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(higher))) {
            writeMetadata(zip);
            writeLayer(zip, "mcpatcher", HIGH_DIMENSION, 1, "higher", sky);
        }
    }

    private static void writeMetadata(ZipOutputStream zip) throws IOException {
        writeText(zip, "pack.mcmeta", "{\"pack\":{\"description\":\"Dimension sky contract\",\"pack_format\":3}}");
    }

    private static void writeLayer(ZipOutputStream zip, String root, int dimension, int index, String name,
                                   BufferedImage sky) throws IOException {
        String directory = "assets/minecraft/" + root + "/sky/world" + dimension + "/";
        String texture = "contract-" + name + ".png";
        writeText(zip, directory + "sky" + index + ".properties", "source=./" + texture + "\n" + """
                startFadeIn=6:00
                endFadeIn=7:00
                startFadeOut=17:00
                endFadeOut=18:00
                weather=clear rain thunder
                rotate=false
                """);
        zip.putNextEntry(new ZipEntry(directory + texture));
        require(ImageIO.write(sky, "png", zip), "PNG writer unavailable for sky contract");
        zip.closeEntry();
    }

    private static void writeText(ZipOutputStream zip, String path, String contents) throws IOException {
        zip.putNextEntry(new ZipEntry(path));
        zip.write(contents.getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
    }

    private static ResourcePackRepository.Entry pack(ResourcePackRepository repository, Path file) {
        return repository.getRepositoryEntriesAll().stream()
                .filter(entry -> entry.getResourcePackName().equals(file.getFileName().toString()))
                .findFirst().orElseThrow(() -> new AssertionError("Sky fixture is unavailable: " + file));
    }

    private static void removePack(ResourcePackRepository repository, Path file) throws IOException {
        if (file == null) {
            return;
        }
        for (ResourcePackRepository.Entry entry : repository.getRepositoryEntriesAll()) {
            if (entry.getResourcePackName().equals(file.getFileName().toString())) {
                entry.closeResourcePack();
                break;
            }
        }
        Files.deleteIfExists(file);
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
