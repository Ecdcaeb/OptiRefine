package mods.Hileb.optirefine.optifine.resources;

import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public final class ModernTextureProperties {
    private static final Logger LOGGER = LogManager.getLogger(ModernTextureProperties.class);
    private static final ResourceLocation LOCATION = new ResourceLocation("optifine/texture.properties");
    private static final int LEGACY_CUTOUT = 16;
    private static final String LAB_PBR_MACRO = "#define MC_TEXTURE_FORMAT_LAB_PBR";
    private static final Settings DEFAULTS = new Settings(0, "");
    private static Settings settings = DEFAULTS;
    private static String atlasMacros = "";

    private ModernTextureProperties() {}

    public static void reload(IResourceManager manager) {
        Settings loaded = DEFAULTS;
        try (IResource resource = manager.getResource(LOCATION)) {
            Properties properties = new Properties();
            properties.load(resource.getInputStream());
            loaded = new Settings(parseCutout(properties.getProperty("alpha.cutout")),
                    parseFormat(properties.getProperty("format")));
        } catch (FileNotFoundException ignored) {
            // An absent resource restores G5 texture behavior.
        } catch (IOException | IllegalArgumentException exception) {
            LOGGER.warn("Unable to read {}", LOCATION, exception);
        }
        settings = loaded;
    }

    private static int parseCutout(String value) {
        if (value == null) {
            return 0;
        }
        try {
            return Math.clamp(Integer.parseInt(value.trim()), 1, 255);
        } catch (NumberFormatException exception) {
            LOGGER.warn("Invalid alpha.cutout value {} in {}", value, LOCATION);
            return 0;
        }
    }

    private static String parseFormat(String value) {
        if (value == null) {
            return "";
        }
        String[] parts = value.trim().split("/", -1);
        if (!parts[0].equals("lab-pbr")) {
            LOGGER.warn("Unsupported texture format {} in {}", value, LOCATION);
            return "";
        }
        if (parts.length > 2 || parts.length == 2 && !parts[1].matches("[0-9]+(?:\\.[0-9]+)*")) {
            LOGGER.warn("Invalid LabPBR version in {}", value);
            return "";
        }
        return parts.length == 1 ? LAB_PBR_MACRO + '\n'
                : LAB_PBR_MACRO + '\n' + LAB_PBR_MACRO + '_' + parts[1].replace('.', '_') + '\n';
    }

    public static boolean isLabPbr() {
        return !settings.formatMacros().isEmpty() && Config.isShaders();
    }

    public static void publishBlockAtlasFormat() {
        atlasMacros = isLabPbr() ? settings.formatMacros() : "";
    }

    public static String blockAtlasMacros() {
        return atlasMacros;
    }

    public static void fixTransparentColor(String spriteName, int[] pixels) {
        if (pixels == null || spriteName.startsWith("minecraft:blocks/leaves_")) {
            return;
        }
        int cutoff = spriteName.startsWith("items/", spriteName.indexOf(':') + 1) ? 0 : settings.alphaCutout();
        int opaqueMinimum = cutoff == 0 ? LEGACY_CUTOUT : cutoff;
        long red = 0;
        long green = 0;
        long blue = 0;
        int count = 0;
        for (int pixel : pixels) {
            if ((pixel >>> 24) >= opaqueMinimum) {
                red += pixel >>> 16 & 255;
                green += pixel >>> 8 & 255;
                blue += pixel & 255;
                ++count;
            }
        }
        if (count == 0 && cutoff == 0) {
            return;
        }
        int transparentMaximum = cutoff == 0 ? LEGACY_CUTOUT : cutoff - 1;
        int average = count == 0 ? 0 : (int) (red / count) << 16 | (int) (green / count) << 8 | (int) (blue / count);
        for (int index = 0; index < pixels.length; ++index) {
            int pixel = pixels[index];
            if ((pixel >>> 24) <= transparentMaximum) {
                pixels[index] = count == 0 ? pixel & 0x00FFFFFF : average;
            }
        }
    }

    public static int[][] generateLabPbrMipmaps(int levels, int width, int height, int[][] source, boolean specular) {
        int[][] data = source.length == levels + 1 ? source : Arrays.copyOf(source, levels + 1);
        for (int level = 1; level <= levels; ++level) {
            if (data[level] != null) {
                continue;
            }
            int previousWidth = width >> (level - 1);
            int levelWidth = width >> level;
            int levelHeight = height >> level;
            int[] previous = data[level - 1];
            int[] current = new int[levelWidth * levelHeight];
            data[level] = current;
            for (int y = 0; y < levelHeight; ++y) {
                for (int x = 0; x < levelWidth; ++x) {
                    int index = y * 2 * previousWidth + x * 2;
                    current[y * levelWidth + x] = blend(previous[index], previous[index + 1],
                            previous[index + previousWidth], previous[index + previousWidth + 1], specular);
                }
            }
        }
        return data;
    }

    private static int blend(int a, int b, int c, int d, boolean specular) {
        if (a == b && b == c && c == d) {
            return a;
        }
        int alpha = specular ? split(a >>> 24, b >>> 24, c >>> 24, d >>> 24, 255, true)
                : average(a, b, c, d, 24);
        int red = average(a, b, c, d, 16);
        int green = specular ? split(a >>> 8 & 255, b >>> 8 & 255, c >>> 8 & 255, d >>> 8 & 255, 230, true)
                : average(a, b, c, d, 8);
        int blue = specular ? split(a & 255, b & 255, c & 255, d & 255, 65, false)
                : average(a, b, c, d, 0);
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    private static int average(int a, int b, int c, int d, int shift) {
        return ((a >>> shift & 255) + (b >>> shift & 255) + (c >>> shift & 255) + (d >>> shift & 255)) / 4;
    }

    private static int split(int a, int b, int c, int d, int threshold, boolean discreteHigh) {
        boolean highA = a >= threshold;
        boolean highB = b >= threshold;
        boolean highC = c >= threshold;
        boolean highD = d >= threshold;
        int highCount = (highA ? 1 : 0) + (highB ? 1 : 0) + (highC ? 1 : 0) + (highD ? 1 : 0);
        boolean highWins = highCount > 2;
        if (highWins && discreteHigh) {
            return highA ? a : b;
        }
        int sum = (highA == highWins ? a : 0) + (highB == highWins ? b : 0)
                + (highC == highWins ? c : 0) + (highD == highWins ? d : 0);
        return sum / (highWins ? highCount : 4 - highCount);
    }

    public static int specularMinFilter(boolean mipmaps) {
        return mipmaps ? GL11.GL_NEAREST_MIPMAP_NEAREST : GL11.GL_NEAREST;
    }

    private record Settings(int alphaCutout, String formatMacros) {}
}
