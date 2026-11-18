package mods.Hileb.optirefine.optifine.shaders.modern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Fragment-output locations in shader declaration order, independent of framebuffer attachment indices. */
record ShaderDrawTargets(int[] buffers, boolean namedOutputs) {
    static final int LEGACY_COUNT = 8;
    static final int COLOR_COUNT = 16;
    private static final Pattern RENDER_TARGETS = Pattern.compile("\\s*/\\*\\s*RENDERTARGETS\\s*:\\s*(.*?)\\s*\\*/.*");
    private static final Pattern DRAW_BUFFERS = Pattern.compile("\\s*(?:/\\*|//)?\\s*DRAWBUFFERS\\s*:\\s*([0-9N]+)\\s*(?:\\*/.*)?");

    static ShaderDrawTargets parse(String line) {
        Matcher directive = RENDER_TARGETS.matcher(line);
        boolean namedOutputs = directive.matches();
        if (!namedOutputs) {
            directive = DRAW_BUFFERS.matcher(line);
            if (!directive.matches()) return null;
        }
        String value = directive.group(1);
        String[] entries = namedOutputs ? value.split(",", -1) : null;
        boolean extended = namedOutputs || value.indexOf('8') >= 0 || value.indexOf('9') >= 0;
        int count = namedOutputs ? entries.length : value.length();
        if (extended && count > LEGACY_COUNT) throw new IllegalArgumentException("expected 1 to 8 fragment outputs");
        count = Math.min(count, LEGACY_COUNT);
        int[] buffers = new int[count];
        int written = 0;
        for (int index = 0; index < count; index++) {
            String entry = namedOutputs ? entries[index].trim() : value.substring(index, index + 1);
            int buffer = entry.equals("N") ? -1 : Integer.parseInt(entry);
            if (buffer < -1 || buffer >= COLOR_COUNT) throw new IllegalArgumentException("color attachment out of range: " + entry);
            if (buffer >= 0) {
                int bit = 1 << buffer;
                if (extended && (written & bit) != 0) throw new IllegalArgumentException("duplicate color attachment: " + entry);
                written |= bit;
            }
            buffers[index] = buffer;
        }
        return new ShaderDrawTargets(buffers, namedOutputs);
    }

    int mask() {
        int mask = 0;
        for (int buffer : buffers) if (buffer >= 0) mask |= 1 << buffer;
        return mask;
    }

    String legacySettings() {
        StringBuilder settings = new StringBuilder(buffers.length);
        for (int buffer : buffers) settings.append(buffer < 0 ? 'N' : (char) ('0' + buffer));
        return settings.toString();
    }
}
