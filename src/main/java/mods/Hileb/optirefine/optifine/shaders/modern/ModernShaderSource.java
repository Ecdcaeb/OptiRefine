package mods.Hileb.optirefine.optifine.shaders.modern;

import net.optifine.shaders.config.MacroState;
import net.optifine.shaders.config.ShaderLine;
import net.optifine.shaders.config.ShaderParser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Load-time metadata, evaluated after the same includes/options/macros as G5 shader compilation. */
public final class ModernShaderSource {
    private static final Pattern UNIFORM = Pattern.compile("\\buniform\\s+(?:(?:readonly|writeonly|coherent|volatile|restrict|highp|mediump|lowp)\\s+)*(\\w+)\\s+(\\w+)");
    private static final Pattern GROUPS = Pattern.compile("\\bconst\\s+ivec3\\s+workGroups\\s*=\\s*ivec3\\s*\\(([^)]*)\\)\\s*;");
    private static final Pattern RELATIVE = Pattern.compile("\\bconst\\s+vec2\\s+workGroupsRender\\s*=\\s*vec2\\s*\\(([^)]*)\\)\\s*;");
    private static final Pattern DEFINE = Pattern.compile("^\\s*#\\s*define\\s+(\\w+)(?:\\s+(.*))?$");
    private static final Pattern UNDEF = Pattern.compile("^\\s*#\\s*undef\\s+(\\w+).*$");
    private static final Pattern WORD = Pattern.compile("\\b[A-Za-z_]\\w*\\b");
    private final MacroState macros = new MacroState();
    private final Map<String, String> definitions = new HashMap<>();
    public int colors;
    public int depths;
    public int shadowColors;
    public int shadowDepths;
    public boolean images;
    public int imageMask;
    public boolean localSize;
    public boolean centerDepthSmooth;
    public int mipmaps;
    public int groupsX = -1;
    public int groupsY;
    public int groupsZ;
    public float relativeX = 1.0F;
    public float relativeY = 1.0F;
    public String error;
    private boolean blockComment;

    public void accept(String source) {
        // The include resolver may insert several macro lines in one returned line.
        for (String line : source.split("\\n")) {
            line = stripComments(line);
            if (!macros.processLine(line)) continue;
            Matcher define = DEFINE.matcher(line);
            if (define.matches()) {
                definitions.put(define.group(1), define.group(2) == null ? "" : define.group(2));
                continue;
            }
            Matcher undef = UNDEF.matcher(line);
            if (undef.matches()) {
                definitions.remove(undef.group(1));
                continue;
            }
            if (line.trim().startsWith("#")) continue;
            Matcher uniform = UNIFORM.matcher(line);
            while (uniform.find()) uniform(uniform.group(1), uniform.group(2));
            String expanded = expand(line);
            if (expanded.matches(".*\\blayout\\s*\\([^)]*\\blocal_size_[xyz]\\s*=.*")) localSize = true;
            try {
                Matcher fixed = GROUPS.matcher(expanded);
                if (fixed.find()) {
                    String[] values = fixed.group(1).split(",");
                    if (values.length != 3) throw new IllegalArgumentException("expected three components");
                    groupsX = Integer.parseInt(values[0].trim());
                    groupsY = Integer.parseInt(values[1].trim());
                    groupsZ = Integer.parseInt(values[2].trim());
                    if (groupsX < 0 || groupsY < 0 || groupsZ < 0) throw new IllegalArgumentException("workGroups must be nonnegative");
                } else if (expanded.contains("workGroups") && expanded.matches(".*\\bconst\\s+ivec3\\s+workGroups\\b.*")) {
                    throw new IllegalArgumentException("invalid workGroups declaration");
                }
                Matcher relative = RELATIVE.matcher(expanded);
                if (relative.find()) {
                    String[] values = relative.group(1).split(",");
                    if (values.length != 2) throw new IllegalArgumentException("expected two components");
                    relativeX = number(values[0]);
                    relativeY = number(values[1]);
                    if (relativeX < 0 || relativeY < 0 || !Float.isFinite(relativeX) || !Float.isFinite(relativeY)) throw new IllegalArgumentException("workGroupsRender must be finite and nonnegative");
                } else if (expanded.matches(".*\\bconst\\s+vec2\\s+workGroupsRender\\b.*")) {
                    throw new IllegalArgumentException("invalid workGroupsRender declaration");
                }
            } catch (IllegalArgumentException exception) {
                error = "Invalid compute dispatch configuration: " + expanded.trim() + " (" + exception.getMessage() + ")";
            }
            ShaderLine parsed = ShaderParser.parseLine(expanded);
            if (parsed != null && parsed.isConstBoolSuffix("MipmapEnabled", true)) {
                String name = parsed.getName();
                name = name.substring(0, name.length() - "MipmapEnabled".length());
                int index = net.optifine.shaders.Shaders.getBufferIndexFromString(name);
                if (index >= 0) mipmaps |= 1 << index;
            }
        }
    }

    private void uniform(String type, String name) {
        boolean image = type.equals("image2D") || type.equals("iimage2D") || type.equals("uimage2D");
        if (name.equals("centerDepthSmooth")) centerDepthSmooth = true;
        int index;
        if ((index = index(name, "colorimg")) >= 0) {
            if (!image || index > 5) error = "Unsupported image uniform " + type + " " + name + "; supported main images are colorimg0..5";
            else { images = true; imageMask |= 1 << index; colors = Math.max(colors, index + 1); }
        } else if ((index = index(name, "shadowcolorimg")) >= 0) {
            if (!image || index > 1) error = "Unsupported image uniform " + type + " " + name + "; supported shadow images are shadowcolorimg0..1";
            else { images = true; imageMask |= 1 << (6 + index); shadowColors = Math.max(shadowColors, index + 1); shadowDepths = Math.max(shadowDepths, 1); }
        } else if ((index = ShaderParser.getColorIndex(name)) >= 0) {
            colors = Math.max(colors, index + 1);
        } else if ((index = index(name, "colortex")) >= 8) {
            error = "This G5 backport supports colortex0..7; requested " + name;
        } else if ((index = ShaderParser.getShadowColorIndex(name)) >= 0) {
            shadowColors = Math.max(shadowColors, index + 1);
            shadowDepths = Math.max(shadowDepths, 1);
        } else if ((index = ShaderParser.getShadowDepthIndex(name)) >= 0) {
            shadowDepths = Math.max(shadowDepths, index + 1);
        } else if ((index = ShaderParser.getDepthIndex(name)) >= 0) {
            depths = Math.max(depths, index + 1);
        }
    }

    private String expand(String line) {
        for (int depth = 0; depth < 16; depth++) {
            Matcher matcher = WORD.matcher(line);
            StringBuffer result = new StringBuffer();
            boolean changed = false;
            while (matcher.find()) {
                String replacement = definitions.get(matcher.group());
                if (replacement != null) {
                    matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
                    changed = true;
                }
            }
            matcher.appendTail(result);
            if (!changed) return line;
            line = result.toString();
        }
        return line;
    }

    private String stripComments(String line) {
        StringBuilder result = new StringBuilder(line.length());
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            char next = i + 1 < line.length() ? line.charAt(i + 1) : 0;
            if (blockComment) {
                if (ch == '*' && next == '/') { blockComment = false; i++; result.append(' '); }
            } else if (ch == '/' && next == '*') {
                blockComment = true;
                i++;
            } else if (ch == '/' && next == '/') {
                break;
            } else result.append(ch);
        }
        return result.toString();
    }

    private static float number(String value) {
        return Float.parseFloat(value.trim().replaceFirst("[fF]$", ""));
    }

    private static int index(String name, String prefix) {
        if (!name.startsWith(prefix) || name.length() == prefix.length()) return -1;
        try { return Integer.parseInt(name.substring(prefix.length())); }
        catch (NumberFormatException ignored) { return -1; }
    }
}
