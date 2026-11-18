package mods.Hileb.optirefine.optifine.shaders.modern;

import net.optifine.shaders.IShaderPack;
import net.optifine.shaders.ShaderPackZip;

public final class ComputeShaderPaths {
    public static final String EXTENSION = ".csh";
    public static final int ALL_VARIANTS = (1 << 27) - 1;

    private ComputeShaderPaths() {}

    // Bit 0 is the unsuffixed shader, then bits 1 through 26 are _a through _z.
    public static int variants(IShaderPack pack, String base) {
        if (pack instanceof ZipIndex zip && pack.getClass() == ShaderPackZip.class) {
            return zip.optiRefine$computeVariants(base);
        }
        return ALL_VARIANTS;
    }

    public static String path(String base, int variant) {
        return variant == 0 ? base + EXTENSION : base + "_" + (char) ('a' + variant - 1) + EXTENSION;
    }

    public interface ZipIndex {
        int optiRefine$computeVariants(String base);
    }
}
