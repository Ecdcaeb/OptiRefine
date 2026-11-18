package mods.Hileb.optirefine.optifine.shaders.modern;

import net.minecraft.client.renderer.GlStateManager;
import net.optifine.shaders.FlipTextures;
import net.optifine.shaders.Program;
import net.optifine.shaders.Programs;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.RenderScale;
import net.optifine.shaders.config.ShaderLine;
import net.optifine.shaders.uniform.ShaderUniform1i;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.util.vector.Vector4f;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;

import static mods.Hileb.optirefine.optifine.shaders.modern.ShaderDrawTargets.COLOR_COUNT;
import static mods.Hileb.optirefine.optifine.shaders.modern.ShaderDrawTargets.LEGACY_COUNT;

/** Pack-owned color textures and their per-program mapping onto G5's physical draw slots. */
final class ShaderColorTargets implements AutoCloseable {
    static final int LEGACY_MASK = (1 << LEGACY_COUNT) - 1;
    private static final String SIZE_PREFIX = "size.buffer.";
    private final Target[] targets = new Target[COLOR_COUNT];
    private final ShaderDrawTargets[] declarations;
    private final int[] flipOn;
    private final int[] flipOff;
    private final Map<IntBuffer, Pass> passes = new IdentityHashMap<>();
    private int owned;
    private int flipped;
    private int attachedToMain;
    private int attachedToScratch;
    private int framebuffer;
    private FloatBuffer clearValue;
    private IntBuffer clearInteger;
    boolean composite;

    ShaderColorTargets(int programs) {
        declarations = new ShaderDrawTargets[programs];
        flipOn = new int[programs];
        flipOff = new int[programs];
    }

    static int textureUnit(int index) {
        return index >= LEGACY_COUNT ? index + 8 : switch (index) {
            case 0, 1, 2, 3 -> index;
            default -> index + 3;
        };
    }

    private Target target(int index) {
        Target target = targets[index];
        if (target == null) {
            target = new Target();
            targets[index] = target;
            owned |= 1 << index;
        }
        return target;
    }

    void properties(Properties properties, Programs programs) {
        if (properties == null) return;
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith(SIZE_PREFIX)) {
                int index = ModernShaderSource.colorIndex(key.substring(SIZE_PREFIX.length()));
                if (index < 0) continue;
                String value = properties.getProperty(key).trim();
                try {
                    target(index).size = Size.parse(value);
                } catch (IllegalArgumentException invalid) {
                    throw ModernShaderPipeline.failure(key + ": expected two positive pixel sizes or two positive relative scales (got " + value + ")");
                }
            } else if (key.startsWith("flip.")) {
                int separator = key.lastIndexOf('.');
                if (separator <= 5) continue;
                int index = ModernShaderSource.colorIndex(key.substring(separator + 1));
                Program program = programs.getProgram(key.substring(5, separator));
                if (index < 0 || program == null) continue;
                String value = properties.getProperty(key).trim();
                if (value.equals("true")) flipOn[program.getIndex()] |= 1 << index;
                else if (value.equals("false")) flipOff[program.getIndex()] |= 1 << index;
                else continue;
                if (index >= LEGACY_COUNT) target(index);
            }
        }
    }

    void declare(Program program, ShaderDrawTargets outputs) {
        declarations[program.getIndex()] = outputs;
    }

    void require(int mask) {
        for (int bits = mask & ~LEGACY_MASK; bits != 0; bits &= bits - 1) target(Integer.numberOfTrailingZeros(bits));
    }

    void settings(ShaderLine line, String path, ToIntFunction<String> parseFormat) {
        String name = line.getName();
        int end = "colortex".length();
        while (end < name.length() && Character.isDigit(name.charAt(end))) end++;
        int index = ModernShaderSource.colorIndex(name.substring(0, end));
        if (index < LEGACY_COUNT) return;
        Target target = target(index);
        if (line.isConstIntSuffix("Format")) {
            int format = parseFormat.applyAsInt(line.getValue());
            if (format == 0) throw ModernShaderPipeline.failure(path + ": unknown texture format " + line.getValue());
            target.format = format;
        } else if (line.isConstBoolSuffix("Clear")) {
            target.clear = line.getValueBool();
        } else if (line.isConstVec4Suffix("ClearColor")) {
            target.color = line.getValueVec4();
        }
    }

    void linkOutputs(Program program, int id) {
        ShaderDrawTargets outputs = declarations[program.getIndex()];
        if (outputs == null || !outputs.namedOutputs()) return;
        for (int i = 0; i < outputs.buffers().length; ++i) GL30.glBindFragDataLocation(id, i, "outColor" + i);
    }

    /** Builds a physical draw-buffer list only when a program writes pack-owned targets. */
    int configure(Program program) {
        ShaderDrawTargets outputs = declarations[program.getIndex()];
        if (outputs == null || (outputs.mask() & owned) == 0) return 0;
        int maxOutputs = GL11.glGetInteger(GL20.GL_MAX_DRAW_BUFFERS);
        int maxAttachments = GL11.glGetInteger(GL30.GL_MAX_COLOR_ATTACHMENTS);
        int[] buffers = outputs.buffers();
        if (buffers.length > maxOutputs) throw ModernShaderPipeline.failure(program.getName() + ": too many simultaneous draw targets");
        int available = ((1 << Math.min(LEGACY_COUNT, maxAttachments)) - 1) & ~outputs.mask();
        int[] attachments = new int[LEGACY_COUNT];
        Arrays.fill(attachments, -1);
        Size size = null;
        boolean normalSize = false;
        IntBuffer draw = program.getDrawBuffersBuffer();
        draw.clear().limit(buffers.length);
        boolean[] legacyFlips = program.getToggleColorTextures();
        Arrays.fill(legacyFlips, false);
        for (int output = 0; output < buffers.length; ++output) {
            int index = buffers[output];
            if (index < 0) {
                draw.put(output, GL11.GL_NONE);
                continue;
            }
            Target target = targets[index];
            int slot = index;
            if (index >= LEGACY_COUNT) {
                if (available == 0) throw ModernShaderPipeline.failure(program.getName() + ": not enough framebuffer attachments");
                slot = Integer.numberOfTrailingZeros(available);
                available &= ~(1 << slot);
            }
            if (slot >= maxAttachments) throw ModernShaderPipeline.failure(program.getName() + ": color attachment " + slot + " is unavailable");
            if (target != null) attachments[slot] = index;
            else legacyFlips[index] = true;
            Size targetSize = target == null ? null : target.size;
            if (targetSize == null) normalSize = true;
            else if (size == null) size = targetSize;
            else if (!size.equals(targetSize)) throw ModernShaderPipeline.failure(program.getName() + ": draw targets have different sizes");
            draw.put(output, GL30.GL_COLOR_ATTACHMENT0 + slot);
        }
        if (size != null && (normalSize || !supportsSizedTargets(program))) {
            throw ModernShaderPipeline.failure(program.getName() + ": fixed and render-sized outputs cannot share a pass, and fixed targets require prepare, deferred, or composite");
        }
        program.setDrawBuffers(draw);
        passes.put(draw, new Pass(attachments, size, outputs.mask()));
        return buffers.length;
    }

    private static boolean supportsSizedTargets(Program program) {
        String name = program.getName();
        return name.startsWith("prepare") || name.startsWith("deferred") || name.startsWith("composite");
    }

    void suppressLegacyFlips(Program program) {
        for (int bits = owned & LEGACY_MASK; bits != 0; bits &= bits - 1) {
            program.getToggleColorTextures()[Integer.numberOfTrailingZeros(bits)] = false;
        }
    }

    void allocate(int width, int height, int[] legacyFormats, boolean[] legacyClear, Vector4f[] legacyColors, IntUnaryOperator pixelFormat) {
        if (owned == 0) return;
        if (!Shaders.capabilities.OpenGL30) throw ModernShaderPipeline.failure("Extended or fixed-size color targets require OpenGL 3.0");
        if (textureUnit(31 - Integer.numberOfLeadingZeros(owned)) >= GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS)) {
            throw ModernShaderPipeline.failure("The shader pack uses more color sampler units than this driver supports");
        }
        deleteTextures();
        framebuffer = GL30.glGenFramebuffers();
        if (clearValue == null) clearValue = BufferUtils.createFloatBuffer(4);
        int maxSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        int active = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        int binding = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        int previousFramebuffer = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, framebuffer);
        try {
            for (int bits = owned; bits != 0; bits &= bits - 1) {
                int index = Integer.numberOfTrailingZeros(bits);
                Target target = targets[index];
                int format = index < LEGACY_COUNT ? legacyFormats[index] : target.format;
                if (index < LEGACY_COUNT) {
                    target.clear = legacyClear[index];
                    target.color = legacyColors[index];
                }
                target.width = target.size == null ? width : target.size.width(width);
                target.height = target.size == null ? height : target.size.height(height);
                if (target.width > maxSize || target.height > maxSize) {
                    throw ModernShaderPipeline.failure("colortex" + index + ": requested " + target.width + 'x' + target.height + " exceeds GL_MAX_TEXTURE_SIZE " + maxSize);
                }
                int external = pixelFormat.applyAsInt(format);
                target.integer = external == GL30.GL_RGBA_INTEGER || external == GL30.GL_BGRA_INTEGER;
                target.unsigned = isUnsigned(format);
                target.a = allocateTexture(target, format, external);
                target.b = allocateTexture(target, format, external);
                attach(0, target.a);
                GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
                if (GL30.glCheckFramebufferStatus(GL30.GL_DRAW_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
                    throw ModernShaderPipeline.failure("colortex" + index + ": framebuffer is incomplete for internal format 0x" + Integer.toHexString(format));
                }
                clear(target, target.a);
                clear(target, target.b);
            }
            attachedToScratch = 1;
        } finally {
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, previousFramebuffer);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, binding);
            GL13.glActiveTexture(active);
        }
    }

    private static boolean isUnsigned(int format) {
        return switch (format) {
            case GL30.GL_R8UI, GL30.GL_R16UI, GL30.GL_R32UI, GL30.GL_RG8UI, GL30.GL_RG16UI, GL30.GL_RG32UI,
                 GL30.GL_RGB8UI, GL30.GL_RGB16UI, GL30.GL_RGB32UI, GL30.GL_RGBA8UI, GL30.GL_RGBA16UI, GL30.GL_RGBA32UI, 0x906F -> true;
            default -> false;
        };
    }

    private static int allocateTexture(Target target, int format, int external) {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        int filter = target.integer ? GL11.GL_NEAREST : GL11.GL_LINEAR;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 33071);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 33071);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, target.width, target.height, 0, external, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        return texture;
    }

    void clearFrame() {
        if (owned == 0 || framebuffer == 0) return;
        int previous = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, framebuffer);
        try {
            detachScratchExcept(1);
            GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
            for (int bits = owned; bits != 0; bits &= bits - 1) {
                Target target = targets[Integer.numberOfTrailingZeros(bits)];
                if (target.clear) {
                    clear(target, target.a);
                    clear(target, target.b);
                }
            }
            attachedToScratch = 1;
        } finally {
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, previous);
        }
    }

    private void clear(Target target, int texture) {
        attach(0, texture);
        Vector4f color = target.color;
        if (target.integer) {
            if (clearInteger == null) clearInteger = BufferUtils.createIntBuffer(4);
            clearInteger.put(0, color == null ? 0 : (int) color.x).put(1, color == null ? 0 : (int) color.y)
                    .put(2, color == null ? 0 : (int) color.z).put(3, color == null ? 0 : (int) color.w);
            if (target.unsigned) GL30C.glClearBufferuiv(GL11.GL_COLOR, 0, clearInteger);
            else GL30C.glClearBufferiv(GL11.GL_COLOR, 0, clearInteger);
        } else {
            clearValue.put(0, color == null ? 0 : color.x).put(1, color == null ? 0 : color.y)
                    .put(2, color == null ? 0 : color.z).put(3, color == null ? 0 : color.w);
            GL30C.glClearBufferfv(GL11.GL_COLOR, 0, clearValue);
        }
    }

    void samplers(boolean cached, int firstBuffer) {
        if (framebuffer == 0) return;
        for (int bits = owned & (-1 << firstBuffer); bits != 0; bits &= bits - 1) {
            int index = Integer.numberOfTrailingZeros(bits);
            bindSampler(index, cached);
        }
        if (cached) GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
        else GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    void uniforms(ShaderUniform1i[] uniforms) {
        for (int bits = owned & ~LEGACY_MASK; bits != 0; bits &= bits - 1) {
            int index = Integer.numberOfTrailingZeros(bits);
            uniforms[index - LEGACY_COUNT].setValue(textureUnit(index));
        }
    }

    void mipmaps(int mask, boolean cached) {
        if (framebuffer == 0) return;
        int buffers = mask & owned;
        if (buffers == 0) return;
        for (int bits = buffers; bits != 0; bits &= bits - 1) {
            int index = Integer.numberOfTrailingZeros(bits);
            bindSampler(index, cached);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                    targets[index].integer ? GL11.GL_NEAREST_MIPMAP_NEAREST : GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        }
        if (cached) GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
        else GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    private void bindSampler(int index, boolean cached) {
        int unit = GL13.GL_TEXTURE0 + textureUnit(index);
        int texture = texture(index, false);
        if (cached) {
            GlStateManager.setActiveTexture(unit);
            GlStateManager.bindTexture(texture);
        } else {
            GL13.glActiveTexture(unit);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        }
    }

    int texture(int index, boolean alternate) {
        Target target = targets[index];
        if (target == null) return 0;
        return ((flipped & 1 << index) != 0) == alternate ? target.a : target.b;
    }

    boolean owns(int index) {
        return (owned & 1 << index) != 0;
    }

    void route(IntBuffer draw, FlipTextures legacy, int legacyCount, int mainFramebuffer, boolean alternate) {
        if (framebuffer == 0) return;
        Pass pass = passes.get(draw);
        if (attachedToMain == 0 && (pass == null || pass.size != null)) return;
        if (GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING) != mainFramebuffer) return;
        int bound = 0;
        if (pass != null && pass.size == null) {
            for (int slot = 0; slot < pass.attachments.length; ++slot) {
                int index = pass.attachments[slot];
                if (index < 0) continue;
                attach(slot, texture(index, alternate));
                bound |= 1 << slot;
            }
        }
        for (int bits = attachedToMain & ~bound; bits != 0; bits &= bits - 1) {
            int slot = Integer.numberOfTrailingZeros(bits);
            attach(slot, slot >= legacyCount ? 0 : alternate ? legacy.getB(slot) : legacy.getA(slot));
        }
        attachedToMain = bound;
    }

    void draw(Program program, Runnable original) {
        Pass pass = passes.get(program.getDrawBuffers());
        if (pass == null || pass.size == null || framebuffer == 0) {
            original.run();
            return;
        }
        int previous = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
        var stack = org.lwjgl.system.MemoryStack.stackPush();
        try (stack) {
            IntBuffer viewport = stack.mallocInt(4);
            GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, framebuffer);
            try {
                Target first = targets[Integer.numberOfTrailingZeros(pass.outputs)];
                int bound = 0;
                for (int slot = 0; slot < pass.attachments.length; ++slot) {
                    int index = pass.attachments[slot];
                    if (index < 0) continue;
                    attach(slot, texture(index, true));
                    bound |= 1 << slot;
                }
                detachScratchExcept(bound);
                attachedToScratch = bound;
                GL20.glDrawBuffers(program.getDrawBuffers());
                RenderScale scale = program.getRenderScale();
                if (scale == null) GL11.glViewport(0, 0, first.width, first.height);
                else GL11.glViewport((int) (first.width * scale.getOffsetX()), (int) (first.height * scale.getOffsetY()),
                        (int) (first.width * scale.getScale()), (int) (first.height * scale.getScale()));
                original.run();
            } finally {
                GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, previous);
                GL11.glViewport(viewport.get(0), viewport.get(1), viewport.get(2), viewport.get(3));
            }
        }
    }

    private void detachScratchExcept(int bound) {
        for (int bits = attachedToScratch & ~bound; bits != 0; bits &= bits - 1) attach(Integer.numberOfTrailingZeros(bits), 0);
    }

    int flip(Program program, boolean pre) {
        if (owned == 0) return 0;
        Pass pass = pre ? null : passes.get(program.getDrawBuffers());
        int index = program.getIndex();
        int mask = (((pass == null ? 0 : pass.outputs) | flipOn[index]) & ~flipOff[index]) & owned;
        flipped ^= mask;
        return mask;
    }

    void refreshSamplers(int mask) {
        if (framebuffer == 0 || (mask & owned) == 0) return;
        int active = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        for (int bits = mask & owned; bits != 0; bits &= bits - 1) {
            int index = Integer.numberOfTrailingZeros(bits);
            GlStateManager.setActiveTexture(GL13.GL_TEXTURE0 + textureUnit(index));
            if (GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) == texture(index, true)) {
                GlStateManager.bindTexture(texture(index, false));
            }
        }
        GlStateManager.setActiveTexture(active);
    }

    private static void attach(int slot, int texture) {
        GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0 + slot, GL11.GL_TEXTURE_2D, texture, 0);
    }

    private void deleteTextures() {
        if (framebuffer != 0) GL30.glDeleteFramebuffers(framebuffer);
        framebuffer = 0;
        flipped = attachedToMain = attachedToScratch = 0;
        for (Target target : targets) {
            if (target == null) continue;
            if (target.a != 0) GlStateManager.deleteTexture(target.a);
            if (target.b != 0) GlStateManager.deleteTexture(target.b);
            target.a = target.b = 0;
        }
    }

    @Override
    public void close() {
        deleteTextures();
        passes.clear();
    }

    private record Pass(int[] attachments, Size size, int outputs) {}

    private static final class Target {
        Size size;
        int format = GL11.GL_RGBA8;
        boolean clear = true;
        Vector4f color;
        boolean integer;
        boolean unsigned;
        int width;
        int height;
        int a;
        int b;
    }

    private record Size(boolean relative, float x, float y) {
        static Size parse(String value) {
            String[] components = value.split("\\s+");
            if (components.length != 2) throw new IllegalArgumentException();
            boolean firstInteger = components[0].matches("[0-9]+");
            boolean secondInteger = components[1].matches("[0-9]+");
            if (firstInteger != secondInteger) throw new IllegalArgumentException();
            float x = Float.parseFloat(components[0]);
            float y = Float.parseFloat(components[1]);
            if (!Float.isFinite(x) || !Float.isFinite(y) || x <= 0 || y <= 0) throw new IllegalArgumentException();
            return new Size(!firstInteger, x, y);
        }

        int width(int width) { return Math.max(1, (int) (relative ? width * x : x)); }
        int height(int height) { return Math.max(1, (int) (relative ? height * y : y)); }
    }
}
