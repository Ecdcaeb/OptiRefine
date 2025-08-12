package mods.Hileb.optirefine.optifine;



import net.minecraft.util.EnumFacing;
import optifine.OptiFineClassTransformer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class OptifineHelper {
    private static final OptiFineClassTransformer optifineClassTransformer = new OptiFineClassTransformer();
    public static InputStream getOptifineResource(String path){
        byte[] d = optifineClassTransformer.getOptiFineResource(path);
        if (d == null) return null;
        else return new ByteArrayInputStream(d);
    }

    @SuppressWarnings("unused")
    public static float getFaceBrightness(EnumFacing facing) {
        return switch (facing) {
            case DOWN -> 0.5F;
            case NORTH, SOUTH -> 0.8F;
            case WEST, EAST -> 0.6F;
            default -> 1.0F;
        };
    }
}
