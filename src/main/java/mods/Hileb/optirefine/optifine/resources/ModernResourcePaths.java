package mods.Hileb.optirefine.optifine.resources;

import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** The legacy feature view of a pack; the pack itself always retains its identity. */
public final class ModernResourcePaths {
    private static final String LEGACY = "mcpatcher/";
    private static final String MODERN = "optifine/";
    private static final String ASSETS = "assets/minecraft/";
    private static final ConcurrentMap<ResourceLocation, PackPaths> PACK_PATHS = new ConcurrentHashMap<>();

    private ModernResourcePaths() {
    }

    /** Clears cached translated names when resource loading is restarted. */
    public static void clearCache() {
        PACK_PATHS.clear();
    }


    /** Cached names avoid allocating translated paths during resource existence checks. */
    public static PackPaths forLocation(ResourceLocation location) {
        if (!"minecraft".equals(location.getNamespace())) {
            return null;
        }
        String path = location.getPath();
        if (!path.startsWith(LEGACY) && !isModernLegacyFeature(path)) {
            return null;
        }
        return PACK_PATHS.computeIfAbsent(location, ModernResourcePaths::createPackPaths);
    }

    private static PackPaths createPackPaths(ResourceLocation location) {
        String path = location.getPath();
        String relative = path.substring(path.startsWith(LEGACY) ? LEGACY.length() : MODERN.length());
        return new PackPaths(ASSETS + MODERN + relative, ASSETS + LEGACY + relative, ASSETS + path);
    }

    /** Modern-only G5 roots such as cem/, random/, font/ and gui/ keep their original semantics. */
    public static boolean isModernLegacyFeature(String path) {
        if (!path.startsWith(MODERN)) {
            return false;
        }
        int offset = MODERN.length();
        return path.startsWith("anim/", offset)
                || path.startsWith("cit/", offset)
                || path.startsWith("ctm/", offset)
                || path.startsWith("sky/", offset)
                || path.startsWith("colormap/", offset)
                || path.startsWith("lightmap/", offset)
                || path.startsWith("mob/", offset)
                || path.equals("optifine/cit.properties")
                || path.equals("optifine/cit.properties.mcmeta")
                || path.equals("optifine/color.properties")
                || path.equals("optifine/color.properties.mcmeta");
    }

    /** Called only during pack discovery, before G5 inspects the concrete source-pack type. */
    public static String[] discoveryPrefixes(String[] prefixes) {
        int aliases = 0;
        for (String prefix : prefixes) {
            if (prefix.startsWith(LEGACY)) {
                ++aliases;
            }
        }
        if (aliases == 0) {
            return prefixes;
        }
        String[] expanded = Arrays.copyOf(prefixes, prefixes.length + aliases);
        int next = prefixes.length;
        for (String prefix : prefixes) {
            if (prefix.startsWith(LEGACY)) {
                expanded[next++] = MODERN + prefix.substring(LEGACY.length());
            }
        }
        return expanded;
    }

    /** One logical filename is loaded once, and all relative references keep G5's legacy base path. */
    public static String[] discoveryFiles(String[] files, String[] originalPrefixes) {
        LinkedHashSet<String> canonical = new LinkedHashSet<>();
        for (String file : files) {
            String name = file;
            if (file.startsWith(MODERN)) {
                for (String prefix : originalPrefixes) {
                    if (prefix.startsWith(LEGACY)
                            && file.regionMatches(MODERN.length(), prefix, LEGACY.length(), prefix.length() - LEGACY.length())) {
                        name = LEGACY + file.substring(MODERN.length());
                        break;
                    }
                }
            }
            canonical.add(name);
        }
        return canonical.toArray(new String[canonical.size()]);
    }

    /** G5's CIT/CTM parsers recognize mcpatcher/ as an absolute feature path. */
    public static String normalizeReference(String path, String basePath) {
        if (basePath != null && basePath.startsWith(LEGACY) && isModernLegacyFeature(path)) {
            return LEGACY + path.substring(MODERN.length());
        }
        return path;
    }

    public record PackPaths(String modern, String legacy, String requested) {
    }
}
