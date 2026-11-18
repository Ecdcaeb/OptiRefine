package mods.Hileb.optirefine.optifine.shaders.modern;

import net.minecraft.client.renderer.culling.ClippingHelper;

import java.util.Properties;

public final class ShaderPassProperties {
    private static final ClippingHelper UNCULLED_CLIPPING = new ClippingHelper() {
        @Override
        public boolean isBoxInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
            return true;
        }
    };

    private static boolean shadowTerrain = true;
    private static boolean shadowEntities = true;
    private static boolean shadowBlockEntities = true;
    private static boolean shadowCulling = true;
    private static boolean particlesBeforeDeferred = true;

    private ShaderPassProperties() {}

    public static void reset() {
        shadowTerrain = true;
        shadowEntities = true;
        shadowBlockEntities = true;
        shadowCulling = true;
        particlesBeforeDeferred = true;
    }

    public static void load(Properties properties) {
        shadowTerrain = enabled(properties, "shadowTerrain");
        shadowEntities = enabled(properties, "shadowEntities");
        shadowBlockEntities = enabled(properties, "shadowBlockEntities");
        shadowCulling = enabled(properties, "shadow.culling");
        particlesBeforeDeferred = enabled(properties, "particles.before.deferred");
    }

    private static boolean enabled(Properties properties, String name) {
        return !"false".equals(properties.getProperty(name));
    }

    public static boolean shadowTerrain() {
        return shadowTerrain;
    }

    public static boolean shadowEntities() {
        return shadowEntities;
    }

    public static boolean shadowBlockEntities() {
        return shadowBlockEntities;
    }

    public static boolean shadowCulling() {
        return shadowCulling;
    }

    public static boolean particlesBeforeDeferred() {
        return particlesBeforeDeferred;
    }

    public static ClippingHelper unculledClippingHelper() {
        return UNCULLED_CLIPPING;
    }
}
