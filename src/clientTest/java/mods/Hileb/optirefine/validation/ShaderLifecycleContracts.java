package mods.Hileb.optirefine.validation;

import mods.Hileb.optirefine.optifine.shaders.modern.ModernShaderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.shaders.Program;
import net.optifine.shaders.ProgramStage;
import net.optifine.shaders.Programs;
import net.optifine.shaders.Shaders;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/** Real GL consumers of stage samplers and an active-pack resize failure. */
final class ShaderLifecycleContracts {
    private static float originalQuality;
    private static final List<Integer> resizePrograms = new ArrayList<>();

    private ShaderLifecycleContracts() {}

    static void createResizeFailurePack(Minecraft mc) throws Exception {
        IntBuffer limit = BufferUtils.createIntBuffer(16);
        int axis = -1, maximum = 0;
        for (int candidate = 1; candidate >= 0; candidate--) {
            limit.clear();
            GL30.glGetInteger(GL43.GL_MAX_COMPUTE_WORK_GROUP_COUNT, candidate, limit);
            if (limit.get(0) < Integer.MAX_VALUE) { axis = candidate; maximum = limit.get(0); break; }
        }
        if (axis < 0) throw new AssertionError("No representable render-relative group overflow on this device");
        int pixels = Math.round((axis == 0 ? mc.displayWidth : mc.displayHeight) * Shaders.configRenderResMul);
        float scale = (float)(maximum * 0.75 / pixels);
        String groups = axis == 0 ? scale + ",0.0" : "0.0," + scale;
        String compute = "#version 430\nlayout(local_size_x=1,local_size_y=1,local_size_z=1) in;\n"
                + "const vec2 workGroupsRender=vec2(" + groups + ");\n"
                + "layout(rgba8) uniform writeonly image2D colorimg0;\n"
                + "void main(){imageStore(colorimg0,ivec2(0),vec4(0.0));}\n";
        Path packs = mc.gameDir.toPath().resolve("shaderpacks");
        try (ZipInputStream source = new ZipInputStream(Files.newInputStream(packs.resolve("modern-zero.zip")));
             ZipOutputStream destination = new ZipOutputStream(Files.newOutputStream(packs.resolve("modern-resize-failure.zip")))) {
            boolean replaced = false;
            for (ZipEntry entry; (entry = source.getNextEntry()) != null;) {
                destination.putNextEntry(new ZipEntry(entry.getName()));
                if (entry.getName().equals("shaders/composite.csh")) {
                    destination.write(compute.getBytes(StandardCharsets.US_ASCII)); replaced = true;
                } else source.transferTo(destination);
                destination.closeEntry();
            }
            if (!replaced) throw new AssertionError("Missing zero-dispatch fixture template");
        }
    }

    static void requestInvalidResize() throws Exception {
        if (!ModernShaderPipeline.isReady() || ModernShaderPipeline.getDispatchCount() == 0) {
            throw new AssertionError("Resize-failure pack must first load and dispatch successfully");
        }
        resizePrograms.clear();
        Field registry = Shaders.class.getDeclaredField("programs"); registry.setAccessible(true);
        int count = ((Programs)registry.get(null)).getCount();
        for (int owner = 0; owner < count; owner++) {
            for (int suffix = 0; suffix < ModernShaderPipeline.getComputeProgramCount(owner); suffix++) {
                resizePrograms.add(ModernShaderPipeline.getComputeProgramId(owner, suffix));
            }
        }
        originalQuality = Shaders.configRenderResMul;
        Shaders.configRenderResMul = originalQuality * 2;
        Shaders.scheduleResize();
    }

    static void verifyResizeRecovery(Minecraft mc) throws Exception {
        try {
            if ((Boolean)Class.forName("Config").getMethod("isShaders").invoke(null) || ModernShaderPipeline.isReady()) {
                throw new AssertionError("Active-pack resize failure did not switch shaders OFF");
            }
            for (int program : resizePrograms) if (GL20.glIsProgram(program)) {
                throw new AssertionError("Resize failure leaked an owned compute program");
            }
            if (mc.world == null || mc.player == null) throw new AssertionError("Resize failure exited the world");
            int framebuffer = mc.getFramebuffer().framebufferObject;
            if (!GL30.glIsFramebuffer(framebuffer) || !GL11.glIsTexture(mc.getFramebuffer().framebufferTexture)) {
                throw new AssertionError("Resize recovery deleted the game framebuffer or its color texture");
            }
            // Client ticks precede Minecraft's framebuffer bind for the next render.
            int previous = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
            try {
                GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, framebuffer);
                if (GL30.glCheckFramebufferStatus(GL30.GL_DRAW_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
                    throw new AssertionError("Resize recovery left the game framebuffer incomplete");
                }
            } finally {
                GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, previous);
            }
        } finally {
            Shaders.configRenderResMul = originalQuality;
            Shaders.scheduleResize();
            resizePrograms.clear();
        }
    }

    static void verifySamplers() throws Exception {
        int id = samplerProgram();
        Program consumer = new Program(Shaders.ProgramTerrain.getIndex(), "contract_gbuffers", ProgramStage.GBUFFERS, true);
        consumer.setId(id);
        try {
            verifySampler(consumer, false, 5, 8);
            verifySampler(consumer, true, 0, 13);
        } finally {
            GL20.glDeleteProgram(id);
        }
    }

    private static void verifySampler(Program consumer, boolean shadow, int index, int unit) {
        int active = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE0 + unit);
        int original = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        int before = image(shadow, index), custom = 0;
        if (before == 0) throw new AssertionError("Missing stage sampler fixture target");
        try {
            GlStateManager.bindTexture(before);
            GlStateManager.setActiveTexture(active);
            stage(shadow);
            int after = image(shadow, index);
            if (before == after || readTexture(before) == readTexture(after)) {
                throw new AssertionError("Stage fixture must distinguish old and flipped sampler contents");
            }
            if (sample(consumer, unit) != readTexture(after)) {
                throw new AssertionError((shadow ? "shadowcomp" : "prepare") + " sampler reads the old target after a graphics flip");
            }
            custom = customTexture();
            GlStateManager.setActiveTexture(GL13.GL_TEXTURE0 + unit);
            GlStateManager.bindTexture(custom);
            GlStateManager.setActiveTexture(active);
            stage(shadow);
            if (sample(consumer, unit) != 0x173B65) throw new AssertionError("Stage restoration overwrote a custom sampler binding");
        } finally {
            GlStateManager.setActiveTexture(GL13.GL_TEXTURE0 + unit);
            GlStateManager.bindTexture(original);
            GlStateManager.setActiveTexture(active);
            if (custom != 0) GlStateManager.deleteTexture(custom);
        }
    }

    private static int image(boolean shadow, int index) {
        return shadow ? ModernShaderPipeline.getShadowImageTexture(index) : ModernShaderPipeline.getMainImageTexture(index);
    }

    private static void stage(boolean shadow) {
        if (shadow) ModernShaderPipeline.renderShadowcomp(); else ModernShaderPipeline.renderPrepare();
    }

    private static int customTexture() {
        GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
        int texture = GL11.glGenTextures();
        try {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            ByteBuffer pixels = BufferUtils.createByteBuffer(4 * 4 * 4);
            for (int i = 0; i < 16; i++) pixels.put((byte)0x17).put((byte)0x3B).put((byte)0x65).put((byte)255);
            pixels.flip();
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, 4, 4, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
            return texture;
        } catch (RuntimeException | Error error) {
            GL11.glDeleteTextures(texture); throw error;
        } finally { GL11.glPopAttrib(); }
    }

    private static int sample(Program program, int unit) {
        int previousRead = GL11.glGetInteger(GL30.GL_READ_FRAMEBUFFER_BINDING);
        int previousDraw = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
        Program previousProgram = Shaders.activeProgram;
        boolean previousShadow = Shaders.isShadowPass, previousGlow = Shaders.isEntitiesGlowing, previousRendering = Shaders.isRenderingDfb;
        int framebuffer = GL30.glGenFramebuffers(), output = GL11.glGenTextures();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        try {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, output);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, 1, 1, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer);
            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, output, 0);
            GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0); GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
            GL11.glViewport(0, 0, 1, 1);
            GL11.glDisable(GL11.GL_DEPTH_TEST); GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND); GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glColorMask(true, true, true, true);
            Shaders.isShadowPass = false; Shaders.isEntitiesGlowing = false; Shaders.isRenderingDfb = false;
            Shaders.useProgram(program);
            GL20.glUniform1i(GL20.glGetUniformLocation(program.getId(), "chooseShadow"), unit == 13 ? 1 : 0);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(-1, -1); GL11.glVertex2f(1, -1); GL11.glVertex2f(1, 1); GL11.glVertex2f(-1, 1);
            GL11.glEnd();
            return readPixel(0, 0);
        } finally {
            Shaders.useProgram(previousProgram);
            Shaders.isShadowPass = previousShadow; Shaders.isEntitiesGlowing = previousGlow; Shaders.isRenderingDfb = previousRendering;
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, previousRead);
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, previousDraw);
            GL11.glPopAttrib();
            GL30.glDeleteFramebuffers(framebuffer); GL11.glDeleteTextures(output);
        }
    }

    private static int readTexture(int texture) {
        int previous = GL11.glGetInteger(GL30.GL_READ_FRAMEBUFFER_BINDING), framebuffer = GL30.glGenFramebuffers();
        try {
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, framebuffer);
            GL30.glFramebufferTexture2D(GL30.GL_READ_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0);
            GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
            return readPixel(2, 2);
        } finally {
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, previous); GL30.glDeleteFramebuffers(framebuffer);
        }
    }

    private static int readPixel(int x, int y) {
        ByteBuffer pixel = BufferUtils.createByteBuffer(4);
        GL11.glReadPixels(x, y, 1, 1, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixel);
        return ((pixel.get(0) & 255) << 16) | ((pixel.get(1) & 255) << 8) | (pixel.get(2) & 255);
    }

    private static int samplerProgram() {
        return linkProgram("#version 130\nvoid main(){gl_Position=gl_Vertex;}",
                "#version 130\nuniform sampler2D gaux2;uniform sampler2D shadowcolor0;uniform int chooseShadow;out vec4 color;void main(){color=chooseShadow!=0?texelFetch(shadowcolor0,ivec2(2,2),0):texelFetch(gaux2,ivec2(2,2),0);}");
    }

    static int linkProgram(String vertexSource, String fragmentSource) {
        int vertex = shader(GL20.GL_VERTEX_SHADER, vertexSource);
        int fragment = 0, program = 0;
        boolean linked = false;
        try {
            fragment = shader(GL20.GL_FRAGMENT_SHADER, fragmentSource);
            program = GL20.glCreateProgram();
            GL20.glAttachShader(program, vertex); GL20.glAttachShader(program, fragment); GL20.glLinkProgram(program);
            if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == 0) throw new AssertionError(GL20.glGetProgramInfoLog(program, 8192));
            linked = true;
            return program;
        } finally {
            GL20.glDeleteShader(vertex);
            if (fragment != 0) GL20.glDeleteShader(fragment);
            if (!linked && program != 0) GL20.glDeleteProgram(program);
        }
    }

    private static int shader(int type, String source) {
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, source); GL20.glCompileShader(shader);
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 0) {
            String log = GL20.glGetShaderInfoLog(shader, 8192); GL20.glDeleteShader(shader); throw new AssertionError(log);
        }
        return shader;
    }
}
