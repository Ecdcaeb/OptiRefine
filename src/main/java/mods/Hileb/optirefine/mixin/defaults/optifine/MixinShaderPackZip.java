package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.shaders.modern.ComputeShaderPaths;
import net.optifine.util.StrUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

import static mods.Hileb.optirefine.optifine.shaders.modern.ComputeShaderPaths.ALL_VARIANTS;
import static mods.Hileb.optirefine.optifine.shaders.modern.ComputeShaderPaths.EXTENSION;

@Mixin(targets = "net.optifine.shaders.ShaderPackZip", remap = false)
public abstract class MixinShaderPackZip implements ComputeShaderPaths.ZipIndex {
    @Shadow protected ZipFile packZipFile;
    @Shadow protected String baseFolder;
    @Unique private Map<String, Integer> optiRefine$computeIndex;

    @Shadow
    private String resolveRelative(String path) {
        throw new AssertionError();
    }

    @Override
    public int optiRefine$computeVariants(String base) {
        // Let G5 handle lazy opening or retrying a failed ZIP open through its stream API.
        if (packZipFile == null) return ALL_VARIANTS;
        if (optiRefine$computeIndex == null) {
            optiRefine$computeIndex = optiRefine$indexComputeEntries(packZipFile, baseFolder);
        }

        String path = StrUtils.removePrefix(base, "/");
        boolean relative = path.contains("..");
        if (!relative && !path.endsWith(".")) return optiRefine$computeIndex.getOrDefault(path, 0);

        // Normalize the whole filename so its last segment is never treated as a parent token.
        String normalized = resolveRelative(path + EXTENSION);
        if (normalized.isEmpty()) return ALL_VARIANTS;
        int normalizedVariants = optiRefine$computeIndex.getOrDefault(normalized.substring(0, normalized.length() - EXTENSION.length()), 0);
        if (relative) return normalizedVariants;

        // A base ending in '.' only triggers G5's normalization for the unsuffixed shader.
        return (optiRefine$computeIndex.getOrDefault(path, 0) & ~1) | (normalizedVariants & 1);
    }

    @Unique
    private static Map<String, Integer> optiRefine$indexComputeEntries(ZipFile zip, String baseFolder) {
        Map<String, Integer> index = new HashMap<>();
        int prefixLength = baseFolder.length();
        for (var entries = zip.entries(); entries.hasMoreElements(); ) {
            String name = entries.nextElement().getName();
            if (!name.startsWith(baseFolder)) continue;
            int end = name.length();
            // ZipFile.getEntry also accepts a directory name without its trailing slash.
            if (name.endsWith(EXTENSION + "/")) end--;
            else if (!name.endsWith(EXTENSION)) continue;
            int stemEnd = end - EXTENSION.length();
            if (stemEnd < prefixLength) continue;

            String stem = name.substring(prefixLength, stemEnd);
            index.merge(stem, 1, (first, second) -> first | second);
            int suffixStart = stem.length() - 2;
            if (suffixStart >= 0 && stem.charAt(suffixStart) == '_') {
                char suffix = stem.charAt(suffixStart + 1);
                if (suffix >= 'a' && suffix <= 'z') {
                    index.merge(stem.substring(0, suffixStart), 1 << (suffix - 'a' + 1), (first, second) -> first | second);
                }
            }
        }
        return index;
    }

    @Inject(method = "close", at = @At("RETURN"))
    private void optiRefine$clearComputeIndex(CallbackInfo ci) {
        optiRefine$computeIndex = null;
    }
}
