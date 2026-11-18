package mods.Hileb.optirefine.optifine.client;

import net.optifine.DynamicLight;

/** The light values published to chunk workers at the end of a G5 lighting update. */
public record DynamicLightSample(double x, double y, double z, int level, boolean underwater) {
    public DynamicLightSample(DynamicLight light) {
        this(light.getLastPosX(), light.getLastPosY(), light.getLastPosZ(), light.getLastLightLevel(), light.isUnderwater());
    }

    public boolean matches(DynamicLight light) {
        return x == light.getLastPosX() && y == light.getLastPosY() && z == light.getLastPosZ()
                && level == light.getLastLightLevel() && underwater == light.isUnderwater();
    }
}
