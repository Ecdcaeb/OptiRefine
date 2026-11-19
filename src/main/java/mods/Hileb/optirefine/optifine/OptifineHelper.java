package mods.Hileb.optirefine.optifine;



import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
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

    public static InputStream getOptifineResource(ResourceLocation location) {
        return getOptifineResource(getAssetPath(location));
    }

    public static boolean hasOptifineResource(ResourceLocation location) {
        return optifineClassTransformer.getOptiFineResource(getAssetPath(location)) != null;
    }

    private static String getAssetPath(ResourceLocation location) {
        return "assets/" + location.getNamespace() + "/" + location.getPath();
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
