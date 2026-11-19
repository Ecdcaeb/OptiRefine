package mods.Hileb.optirefine.validation;

import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.lang.reflect.Field;
import java.util.Map;

public final class StellarResourceContracts {
    private static final String CTM_TILE = "mcpatcher/ctm/default/0_glass_white/0";
    private static final ResourceLocation PATCHED = new ResourceLocation(CTM_TILE + ".png");
    private static final ResourceLocation MISSING = new ResourceLocation(CTM_TILE + "_missing.png");
    private static final ResourceLocation VANILLA = new ResourceLocation("textures/blocks/glass.png");

    private StellarResourceContracts() {}

    /** Run at the main menu with StellarCore's cache and Fancy connected textures configured. */
    public static void verify(Minecraft mc) throws Exception {
        Class<?> index = Class.forName("github.kasuminova.stellarcore.client.resource.ClasspathAssetIndex");
        // Warm synchronously so StellarCore cannot take its uncached stream fallback.
        var classpathContains = index.getMethod("contains", ResourceLocation.class);
        require(Boolean.FALSE.equals(classpathContains.invoke(null, PATCHED)),
                "G5 CTM fixture should be absent from StellarCore's classpath index");
        verifyCache(mc);
        verifyAtlas(mc, "startup");
        mc.refreshResources();
        verifyCache(mc);
        verifyAtlas(mc, "reload");
    }

    private static void verifyCache(Minecraft mc) throws Exception {
        DefaultResourcePack pack = Config.getDefaultResourcePack();
        Field enabledField = DefaultResourcePack.class.getDeclaredField("stellar_core$cacheEnabled");
        enabledField.setAccessible(true);
        boolean wasEnabled = enabledField.getBoolean(pack);
        try {
            if (!wasEnabled) pack.getClass().getMethod("stellar_core$enableCache").invoke(pack);
            pack.getClass().getMethod("stellar_core$onReload").invoke(pack);

            require(pack.resourceExists(PATCHED), "Default pack rejected G5's patched CTM tile");
            require(pack.resourceExists(VANILLA), "Default pack rejected a vanilla texture");
            require(!pack.resourceExists(MISSING), "Default pack advertised a missing texture");

            Field cacheField = DefaultResourcePack.class.getDeclaredField("stellar_core$resourceExistsCache");
            cacheField.setAccessible(true);
            Map<?, ?> cache = (Map<?, ?>) cacheField.get(pack);
            require(Boolean.TRUE.equals(cache.get(PATCHED)) && Boolean.TRUE.equals(cache.get(VANILLA))
                            && Boolean.FALSE.equals(cache.get(MISSING)),
                    "StellarCore did not cache the positive and negative lookup results");
            require(pack.resourceExists(PATCHED) && !pack.resourceExists(MISSING),
                    "Repeated existence queries disagreed with the initial results");

            try (IResource resource = mc.getResourceManager().getResource(PATCHED)) {
                require(pack.getPackName().equals(resource.getResourcePackName()),
                        "Expected the default pack to supply the patched tile");
                require(ImageIO.read(resource.getInputStream()) != null, "Patched CTM tile could not be decoded");
            }
        } finally {
            if (!wasEnabled) pack.getClass().getMethod("stellar_core$disableCache").invoke(pack);
        }
    }

    private static void verifyAtlas(Minecraft mc, String phase) {
        var atlas = mc.getTextureMapBlocks();
        require(atlas.getAtlasSprite("minecraft:" + CTM_TILE) != atlas.getMissingSprite(),
                "Patched CTM tile was missing from the atlas after " + phase);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
