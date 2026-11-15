package mods.Hileb.optirefine.optifine.shaders.modern;

import mods.Hileb.optirefine.core.OptiRefineLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextComponentString;
import net.optifine.shaders.IShaderPack;
import net.optifine.shaders.ICustomTexture;
import net.optifine.shaders.Program;
import net.optifine.shaders.ProgramStage;
import net.optifine.shaders.Programs;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.RenderScale;
import net.optifine.shaders.config.ShaderOption;
import net.optifine.shaders.config.ShaderPackParser;
import net.optifine.shaders.uniform.ShaderUniform1i;
import net.optifine.shaders.FlipTextures;
import net.optifine.shaders.uniform.ShaderUniforms;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public final class ModernShaderPipeline {
    private static final String[] IMAGE_NAMES = {"colorimg0", "colorimg1", "colorimg2", "colorimg3", "colorimg4", "colorimg5", "shadowcolorimg0", "shadowcolorimg1"};
    private static final int[] COLOR_UNITS = {0, 1, 2, 3, 7, 8, 9, 10};
    private static final ShaderUniform1i[] IMAGE_UNIFORMS = new ShaderUniform1i[8];
    private static final IntBuffer LOCAL_SIZE = BufferUtils.createIntBuffer(16);
    private static final int[] GROUP_LIMITS = new int[3];
    private static Program[] prepare;
    private static Program[] shadowcomp;
    private static int programCount;
    private static ShaderPackState state;


    private ModernShaderPipeline() {}

    public static void register(Programs programs) {
        prepare = programs.makeComposites("prepare", 100);
        shadowcomp = programs.makeComposites("shadowcomp", 100);
        programCount = programs.getCount();
    }

    public static void initializeUniforms(ShaderUniforms uniforms) {
        for (int i = 0; i < IMAGE_NAMES.length; i++) IMAGE_UNIFORMS[i] = uniforms.make1i(IMAGE_NAMES[i]);
    }

    public static void beginPack() {
        close();
        state = new ShaderPackState(programCount);
    }

    public static void beginSource(String path) {
        if (state == null || state.isClosed()) return;
        state.scanning = new ModernShaderSource();
        state.scanningPath = path;
    }

    public static void sourceLine(String line) {
        if (state == null || state.isClosed()) return;
        if (state.scanningPath != null) {
            String name = state.scanningPath.substring(state.scanningPath.lastIndexOf('/') + 1);
            if (name.startsWith("prepare") || name.startsWith("shadowcomp")) state.modernRequested = true;
        }
        if (state.scanning != null) state.scanning.accept(line);
    }

    public static void endSource(Program program) {
        if (state == null || state.isClosed() || state.scanning == null) return;
        requirements(state.scanning, state.scanningPath);
        program.setCompositeMipmapSetting(program.getCompositeMipmapSetting() | state.scanning.mipmaps);
        state.scanning = null;
    }

    private static void requirements(ModernShaderSource source, String path) {
        state.requiredColors = Math.max(state.requiredColors, source.colors);
        state.requiredDepths = Math.max(state.requiredDepths, source.depths);
        state.requiredShadowColors = Math.max(state.requiredShadowColors, source.shadowColors);
        state.requiredShadowDepths = Math.max(state.requiredShadowDepths, source.shadowDepths);
        state.imagesRequested |= source.images;
        state.requestedImageMask |= source.imageMask;
        state.requiredCenterDepthSmooth |= source.centerDepthSmooth;
        if (source.images && !Shaders.capabilities.OpenGL42) state.pendingError = path + ": image uniforms require OpenGL 4.2 (current: " + Shaders.glVersionString + ")";
        if (source.error != null) state.pendingError = path + ": " + source.error;
    }

    public static int requiredColors() { return state == null ? 0 : state.requiredColors; }
    public static int requiredDepths() { return state == null ? 0 : state.requiredDepths; }
    public static int requiredShadowColors() { return state == null ? 0 : state.requiredShadowColors; }
    public static int requiredShadowDepths() { return state == null ? 0 : state.requiredShadowDepths; }
    public static boolean requiresCenterDepthSmooth() { return state != null && state.requiredCenterDepthSmooth; }

    public static void graphicsError(String message) { if (state != null && !state.isClosed()) state.graphicsError = message; }

    public static void checkLoadError() {
        if (state == null || state.isClosed()) return;
        if (state.pendingError != null) throw failure(state.pendingError);
        if ((state.modernRequested || state.imagesRequested) && state.graphicsError != null) throw failure(state.graphicsError);
    }

    public static void loadComputes(Program owner, String vertexPath) {
        if (state == null || state.isClosed() || owner.getProgramStage() == ProgramStage.GBUFFERS || owner.getProgramStage() == ProgramStage.NONE || vertexPath.contains("<disabled>")) return;
        String base = vertexPath.substring(0, vertexPath.length() - 4);
        ArrayList<Compute> loaded = new ArrayList<>();
        try {
            for (int suffix = 0; suffix < 27; suffix++) {
                String path = base + (suffix == 0 ? "" : "_" + (char)('a' + suffix - 1)) + ".csh";
                Compute compute = loadCompute(owner, path);
                if (compute != null) {
                    loaded.add(compute);
                    // Publish immediately, so failure cleanup owns every successful link.
                    state.computes[owner.getIndex()] = loaded.toArray(new Compute[0]);
                }
            }
        } catch (IOException exception) {
            throw failure(base + ": " + exception.getMessage(), exception);
        }
        if (isShadowComposite(owner) && (owner.getId() != 0 || !loaded.isEmpty())) state.requiredShadowDepths = Math.max(state.requiredShadowDepths, 1);
    }

    private static Compute loadCompute(Program owner, String path) throws IOException {
        IShaderPack pack = Shaders.getShaderPack();
        InputStream stream = pack.getResourceAsStream(path);
        if (stream == null) return null;
        StringBuilder code = new StringBuilder(16384);
        ModernShaderSource metadata = new ModernShaderSource();
        ArrayList<String> files = new ArrayList<>();
        ShaderOption[] options = Shaders.getShaderPackOptions();
        ShaderOption[] changed = options == null ? new ShaderOption[0] : Shaders.getChangedOptions(options);
        try (BufferedReader input = new BufferedReader(new InputStreamReader(stream, StandardCharsets.US_ASCII));
             BufferedReader reader = ShaderPackParser.resolveIncludes(input, path, pack, 0, files, 0)) {
            if (!Shaders.capabilities.OpenGL43) throw failure(path + ": compute shaders require OpenGL 4.3 (current: " + Shaders.glVersionString + ")");
            String line;
            while ((line = reader.readLine()) != null) {
                for (ShaderOption option : changed) {
                    if (option.matchesLine(line)) { line = option.getSourceLine(); break; }
                }
                code.append(line).append('\n');
                metadata.accept(line);
            }
        }
        requirements(metadata, path);
        state.modernRequested = true;
        checkLoadError();
        if (!metadata.localSize) throw failure(path + ": missing compute local size declaration");
        int shader = GL20.glCreateShader(GL43.GL_COMPUTE_SHADER);
        if (shader == 0) throw failure(path + ": could not create compute shader");
        int program = 0;
        boolean linked = false;
        try {
            GL20.glShaderSource(shader, code);
            GL20.glCompileShader(shader);
            if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 0) throw failure(path + ": compute compilation failed\n" + GL20.glGetShaderInfoLog(shader, 32768) + "\nIncludes: " + files);
            program = GL20.glCreateProgram();
            if (program == 0) throw failure(path + ": could not create compute program");
            GL20.glAttachShader(program, shader);
            GL20.glLinkProgram(program);
            if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == 0) throw failure(path + ": compute link failed\n" + GL20.glGetProgramInfoLog(program, 32768));
            LOCAL_SIZE.clear();
            GL20.glGetProgram(program, GL43.GL_COMPUTE_WORK_GROUP_SIZE, LOCAL_SIZE);
            Compute compute = new Compute(owner, path, program, metadata, LOCAL_SIZE.get(0), LOCAL_SIZE.get(1), LOCAL_SIZE.get(2));
            linked = true;
            return compute;
        } finally {
            if (program != 0) GL20.glDetachShader(program, shader);
            GL20.glDeleteShader(shader);
            if (program != 0 && !linked) GL20.glDeleteProgram(program);
        }
    }

    public static void completePack() {
        if (state == null || state.isClosed()) return;
        checkLoadError();
        if (Shaders.capabilities.OpenGL43) {
            for (int axis = 0; axis < 3; axis++) {
                LOCAL_SIZE.clear();
                GL30.glGetInteger(GL43.GL_MAX_COMPUTE_WORK_GROUP_COUNT, axis, LOCAL_SIZE);
                GROUP_LIMITS[axis] = LOCAL_SIZE.get(0);
            }
            for (Compute[] stages : state.computes) {
                if (stages == null) continue;
                for (Compute compute : stages) compute.resize();
            }
        }
        state.preparePrograms = activeStages(prepare);
        state.shadowcompPrograms = activeStages(shadowcomp);
        state.ready = true;
        bindImages();
    }

    public static void mainTargets(FlipTextures flip, IntBuffer depthTextures, int count, int depthCountIn, int framebuffer) {
        if (state == null || state.isClosed()) return;
        state.colors = flip;
        state.depths = depthTextures;
        state.colorCount = count;
        state.depthCount = depthCountIn;
        state.mainFramebuffer = framebuffer;
        if (state.imagesRequested) {
            int active = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            int texture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
            try {
                for (int i = 0; i < Math.min(count, 6); i++) {
                    state.mainTexturesA[i] = flip.getA(i);
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, flip.getA(i));
                    state.mainFormatsA[i] = imageFormat(GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT));
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, flip.getB(i));
                    state.mainFormatsB[i] = imageFormat(GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT));
                    if ((state.requestedImageMask & 1 << i) != 0) {
                        validateImageFormat(state.mainFormatsA[i], IMAGE_NAMES[i]);
                        validateImageFormat(state.mainFormatsB[i], IMAGE_NAMES[i]);
                    }
                }
            } finally { GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture); GL13.glActiveTexture(active); }
        }
        if (state.ready && Shaders.capabilities.OpenGL43) {
            for (Compute[] stages : state.computes) {
                if (stages != null) for (Compute compute : stages) compute.resize();
            }
        }
    }

    public static void shadowTargets(IntBuffer colorTextures, IntBuffer depthTextures, int count, int depthCountIn, int width, int height, int framebuffer) {
        if (state == null || state.isClosed()) return;
        state.shadowColors = colorTextures;
        state.shadowDepths = depthTextures;
        state.shadowColorCount = count;
        state.shadowDepthCount = depthCountIn;
        state.shadowWidth = width;
        state.shadowHeight = height;
        state.shadowFramebuffer = framebuffer;
        if (!state.imagesRequested && !hasGraphics(shadowcomp)) return;
        int active = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        int texture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        try {
            for (int i = 0; i < count; i++) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextures.get(i));
                int format = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT);
                if (i < 2) state.imageFormats[6 + i] = imageFormat(format);
                if (i < 2 && (state.requestedImageMask & 1 << (6 + i)) != 0) validateImageFormat(state.imageFormats[6 + i], IMAGE_NAMES[6 + i]);
                if (!hasGraphics(shadowcomp)) continue;
                state.shadowAlternates[i] = GL11.glGenTextures();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, state.shadowAlternates[i]);
                int filter = Shaders.shadowColorFilterNearest[i] ? GL11.GL_NEAREST : GL11.GL_LINEAR;
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12_CLAMP_TO_EDGE);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12_CLAMP_TO_EDGE);
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
            }
        } finally { GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture); GL13.glActiveTexture(active); }
    }

    private static final int GL12_CLAMP_TO_EDGE = 33071;

    // This is J9's conversion of legacy unsized formats to their sized image formats.
    private static int imageFormat(int format) {
        return switch (format) {
            case 8194 -> 33321;
            case 33319 -> 33323;
            case GL11.GL_RGB, 10768 -> GL11.GL_RGB8;
            case GL11.GL_RGBA, 32855 -> GL11.GL_RGBA8;
            case 35901 -> GL11.GL_RGB16;
            default -> format;
        };
    }

    private static void validateImageFormat(int format, String name) {
        switch (format) {
            case 33321, 33323, 32856, 36756, 36757, 36759,
                 33322, 33324, 32859, 36760, 36761, 36763,
                 33325, 33327, 34842, 33326, 33328, 34836,
                 33333, 33339, 36226, 33334, 33340, 36208,
                 36238, 36220, 36232, 36214, 32857, 36975, 35898 -> { }
            default -> throw failure(name + ": internal texture format 0x" + Integer.toHexString(format) + " does not support image load/store; select a supported R, RG or RGBA format");
        }
    }

    public static void onUseProgram() {
        // G5 configures these only for deferred packs; prepare can produce them independently.
        if (state != null && state.ready && state.colorCount > 4 && Shaders.activeProgramID != 0
                && Shaders.activeProgram.getProgramStage() == ProgramStage.GBUFFERS) {
            Shaders.uniform_gaux1.setValue(7);
            Shaders.uniform_gaux2.setValue(8);
            Shaders.uniform_gaux3.setValue(9);
            Shaders.uniform_gaux4.setValue(10);
        }
        if (state == null || !state.imagesRequested || Shaders.activeProgramID == 0) return;
        graphicsBarrier();
        for (int i = 0; i < IMAGE_UNIFORMS.length; i++) IMAGE_UNIFORMS[i].setValue(i);
        if (state.ready) bindImages();
    }

    public static void bindImages() {
        if (state == null || !state.imagesRequested || !Shaders.capabilities.OpenGL42) return;
        for (int i = 0; i < 6; i++) {
            int texture = state.colors != null && i < state.colorCount && (state.requestedImageMask & 1 << i) != 0 ? state.colors.getA(i) : 0;
            if (texture != 0) state.imageFormats[i] = texture == state.mainTexturesA[i] ? state.mainFormatsA[i] : state.mainFormatsB[i];
            GL42.glBindImageTexture(i, texture, 0, false, 0, GL15_READ_WRITE, texture == 0 ? GL11.GL_RGBA8 : state.imageFormats[i]);
        }
        for (int i = 0; i < 2; i++) {
            int texture = state.shadowColors != null && i < state.shadowColorCount && (state.requestedImageMask & 1 << (6 + i)) != 0 ? state.shadowColors.get(i) : 0;
            GL42.glBindImageTexture(6 + i, texture, 0, false, 0, GL15_READ_WRITE, texture == 0 ? GL11.GL_RGBA8 : state.imageFormats[6 + i]);
        }
    }

    public static void graphicsBarrier() {
        if (state != null && state.ready && state.imagesRequested) GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT | GL42.GL_TEXTURE_FETCH_BARRIER_BIT);
    }
    private static final int GL15_READ_WRITE = 35002;

    public static boolean hasComputes(Program owner) {
        return state != null && state.computes[owner.getIndex()] != null;
    }

    private static Program[] activeStages(Program[] programs) {
        if (programs == null) return ShaderPackState.NO_STAGES;
        int count = 0;
        for (Program program : programs) if (program.getId() != 0 || hasComputes(program)) count++;
        if (count == 0) return ShaderPackState.NO_STAGES;
        if (count == programs.length) return programs;
        Program[] active = new Program[count];
        int index = 0;
        for (Program program : programs) {
            if (program.getId() != 0 || hasComputes(program)) active[index++] = program;
            if (index == count) break;
        }
        return active;
    }

    private static boolean hasGraphics(Program[] programs) {
        if (programs == null) return false;
        for (Program program : programs) if (program.getId() != 0) return true;
        return false;
    }

    public static boolean isShadowComposite(Program program) { return program.getName().startsWith("shadowcomp"); }

    public static void requireShadowComposite(Program program) {
        if (state == null || state.isClosed()) return;
        if (isShadowComposite(program) && (program.getId() != 0 || hasComputes(program))) {
            state.requiredShadowDepths = Math.max(state.requiredShadowDepths, 1);
            if (program.getId() != 0) {
                String settings = program.getDrawBufSettings();
                state.requiredShadowColors = Math.max(state.requiredShadowColors, 1);
                if (settings != null) for (int i = 0; i < settings.length(); i++) {
                    int attachment = settings.charAt(i) - '0';
                    if (attachment >= 0 && attachment < 8) state.requiredShadowColors = Math.max(state.requiredShadowColors, attachment + 1);
                }
            }
        }
    }

    /** Compute writes main images in place; it never performs the graphics pass's ping-pong flip. */
    public static void dispatch(Program owner) {
        if (state == null || !state.ready || state.dispatching || !hasComputes(owner)) return;
        Compute[] stages = state.computes[owner.getIndex()];
        Program previous = Shaders.activeProgram;
        boolean shadow = Shaders.isShadowPass;
        boolean glowing = Shaders.isEntitiesGlowing;
        boolean rendering = Shaders.isRenderingDfb;
        state.dispatching = true;
        GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
        try {
            if (owner.getProgramStage() == ProgramStage.SHADOW) {
                bindSamplers();
                bindCustomTextures(state.gbuffersTextures);
            }
            Shaders.isShadowPass = false;
            Shaders.isEntitiesGlowing = false;
            Shaders.isRenderingDfb = false;
            for (Compute compute : stages) {
                Shaders.useProgram(compute.uniformProgram);
                if (compute.uniformProgram.getId() == 0) throw failure(compute.path + ": OpenGL rejected the linked compute program");
                GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT | GL42.GL_TEXTURE_FETCH_BARRIER_BIT);
                GL43.glDispatchCompute(compute.x, compute.y, compute.z);
                GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT | GL42.GL_TEXTURE_FETCH_BARRIER_BIT);
                state.dispatchCount++;
                state.lastDispatchPath = compute.path;
                state.lastGroupsX = compute.x;
                state.lastGroupsY = compute.y;
                state.lastGroupsZ = compute.z;
                mipmaps(compute.metadata.mipmaps, isShadowComposite(owner));
            }
        } finally {
            Shaders.useProgram(previous);
            Shaders.isShadowPass = shadow;
            Shaders.isEntitiesGlowing = glowing;
            Shaders.isRenderingDfb = rendering;
            GL11.glPopAttrib();
            state.dispatching = false;
        }
    }

    private static void bindSamplers() {
        for (int i = 0; i < state.colorCount; i++) bindTexture(COLOR_UNITS[i], state.colors.getA(i));
        for (int i = 0; i < state.depthCount; i++) bindTexture(i == 0 ? 6 : 10 + i, state.depths.get(i));
        for (int i = 0; i < state.shadowDepthCount; i++) bindTexture(4 + i, state.shadowDepths.get(i));
        for (int i = 0; i < state.shadowColorCount; i++) bindTexture(13 + i, state.shadowColors.get(i));
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    private static void rebindFlippedGbuffersSamplers(boolean shadow, int flippedBuffers) {
        if (flippedBuffers == 0) return;
        int active = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        try {
            int count = shadow ? state.shadowColorCount : state.colorCount;
            // Main units 0..3 belong to block textures, lightmaps and material maps in gbuffers.
            for (int i = shadow ? 0 : 4; i < count; i++) {
                if ((flippedBuffers & 1 << i) == 0) continue;
                GlStateManager.setActiveTexture(GL13.GL_TEXTURE0 + (shadow ? 13 + i : COLOR_UNITS[i]));
                int previous = shadow ? state.shadowAlternates[i] : state.colors.getB(i);
                // Keep pack/custom renderer overrides that were restored by glPopAttrib.
                if (GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) == previous) {
                    GlStateManager.bindTexture(shadow ? state.shadowColors.get(i) : state.colors.getA(i));
                }
            }
        } finally {
            GlStateManager.setActiveTexture(active);
        }
    }

    private static void bindCustomTextures(ICustomTexture[] textures) {
        if (textures == null) return;
        for (ICustomTexture texture : textures) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + texture.getTextureUnit());
            GL11.glBindTexture(texture.getTarget(), texture.getTextureId());
        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    public static Properties stageTextureProperties(Properties source, String stage) {
        Properties result = new Properties();
        String prefix = "texture." + stage + ".";
        for (Object key : source.keySet()) {
            String name = (String)key;
            if (name.startsWith(prefix)) result.setProperty("texture.composite." + name.substring(prefix.length()), source.getProperty(name));
        }
        return result;
    }

    public static void stageTextures(ICustomTexture[] prepareIn, ICustomTexture[] shadowcompIn, ICustomTexture[] gbuffersIn) {
        if (state == null || state.isClosed()) return;
        state.prepareTextures = prepareIn;
        state.shadowcompTextures = shadowcompIn;
        state.gbuffersTextures = gbuffersIn;
    }

    private static void bindTexture(int unit, int texture) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
    }

    private static void mipmaps(int mask, boolean shadow) {
        int count = shadow ? state.shadowColorCount : state.colorCount;
        for (int i = 0; i < count; i++) if ((mask & 1 << i) != 0) {
            bindTexture(shadow ? 13 + i : COLOR_UNITS[i], shadow ? state.shadowColors.get(i) : state.colors.getA(i));
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    public static void renderPrepare() { renderStages(false); }
    public static void renderShadowcomp() { renderStages(true); }

    private static void renderStages(boolean shadow) {
        if (state == null || !state.ready) return;
        Program[] stages = shadow ? state.shadowcompPrograms : state.preparePrograms;
        if (stages.length == 0) return;
        Program previous = Shaders.activeProgram;
        boolean previousShadow = Shaders.isShadowPass;
        boolean previousGlow = Shaders.isEntitiesGlowing;
        boolean previousRendering = Shaders.isRenderingDfb;
        int framebuffer = GL11.glGetInteger(EXTFramebufferObject.GL_FRAMEBUFFER_BINDING_EXT);
        int matrixMode = GL11.glGetInteger(GL11.GL_MATRIX_MODE);
        int width = shadow ? state.shadowWidth : Shaders.renderWidth;
        int height = shadow ? state.shadowHeight : Shaders.renderHeight;
        int flippedBuffers = 0;
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 1, 0, 1, 0, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        try {
            Shaders.isShadowPass = false;
            Shaders.isEntitiesGlowing = false;
            Shaders.isRenderingDfb = false;
            EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, shadow ? state.shadowFramebuffer : state.mainFramebuffer);
            GL11.glViewport(0, 0, width, height);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthFunc(GL11.GL_ALWAYS);
            GL11.glDepthMask(false);
            for (Program program : stages) {
                if (program.getId() == 0 && !hasComputes(program)) continue;
                bindSamplers();
                bindCustomTextures(shadow ? state.shadowcompTextures : state.prepareTextures);
                dispatch(program);
                if (program.getId() == 0) continue;
                int count = shadow ? state.shadowColorCount : state.colorCount;
                for (int i = 0; i < count; i++) attach(i, shadow ? state.shadowAlternates[i] : state.colors.getB(i));
                GL20.glDrawBuffers(program.getDrawBuffers());
                Shaders.useProgram(program);
                mipmaps(program.getCompositeMipmapSetting(), shadow);
                RenderScale scale = program.getRenderScale();
                if (scale != null) GL11.glViewport((int)(width * scale.getOffsetX()), (int)(height * scale.getOffsetY()), (int)(width * scale.getScale()), (int)(height * scale.getScale()));
                Shaders.drawComposite();
                if (state.imagesRequested) GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT | GL42.GL_TEXTURE_FETCH_BARRIER_BIT);
                GL11.glViewport(0, 0, width, height);
                for (int i = 0; i < count; i++) if (program.getToggleColorTextures()[i]) {
                    if (shadow) {
                        int current = state.shadowColors.get(i);
                        state.shadowColors.put(i, state.shadowAlternates[i]);
                        state.shadowAlternates[i] = current;
                    } else state.colors.flip(i);
                    flippedBuffers ^= 1 << i;
                }
            }
        } finally {
            int count = shadow ? state.shadowColorCount : state.colorCount;
            for (int i = 0; i < count; i++) attach(i, shadow ? state.shadowColors.get(i) : state.colors.getA(i));
            Shaders.useProgram(previous);
            Shaders.isShadowPass = previousShadow;
            Shaders.isEntitiesGlowing = previousGlow;
            Shaders.isRenderingDfb = previousRendering;
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPopMatrix();
            GL11.glMatrixMode(matrixMode);
            EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, framebuffer);
            GL11.glPopAttrib();
            rebindFlippedGbuffersSamplers(shadow, flippedBuffers);
            bindImages();
        }
    }

    private static void attach(int attachment, int texture) {
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT + attachment, GL11.GL_TEXTURE_2D, texture, 0);
    }

    public static void deleteShadowAlternates() {
        if (state != null) state.deleteShadowAlternates();
    }

    public static void close() {
        if (state != null) state.close();
    }

    public static LoadException failure(String message) { return failure(message, null); }
    public static LoadException failure(String message, Throwable cause) {
        String text = "[OptiRefine shaders] " + message;
        OptiRefineLog.log.error(text);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(text));
        return new LoadException(message, cause);
    }

    public static final class LoadException extends IllegalStateException {
        private LoadException(String message, Throwable cause) { super(message, cause); }
    }

    static final class Compute {
        final Program uniformProgram;
        final int id;
        final String path;
        final ModernShaderSource metadata;
        final int localX;
        final int localY;
        int x;
        int y;
        int z;

        Compute(Program owner, String path, int id, ModernShaderSource metadata, int localX, int localY, int localZ) {
            if (localX < 1 || localY < 1 || localZ < 1) throw failure(path + ": missing or invalid compute local size");
            this.uniformProgram = new Program(owner.getIndex(), path, ProgramStage.COMPOSITE, true);
            this.uniformProgram.setId(id);
            this.id = id;
            this.path = path;
            this.metadata = metadata;
            this.localX = localX;
            this.localY = localY;
        }

        void resize() {
            x = metadata.groupsX >= 0 ? metadata.groupsX : groups(Shaders.renderWidth, metadata.relativeX, localX);
            y = metadata.groupsX >= 0 ? metadata.groupsY : groups(Shaders.renderHeight, metadata.relativeY, localY);
            z = metadata.groupsX >= 0 ? metadata.groupsZ : 1;
            if (x < 0 || y < 0 || z < 0 || x > GROUP_LIMITS[0] || y > GROUP_LIMITS[1] || z > GROUP_LIMITS[2]) throw failure(path + ": compute group count " + x + ", " + y + ", " + z + " exceeds GL limits " + Arrays.toString(GROUP_LIMITS));
        }

        private static int groups(int pixels, float scale, int local) {
            // J9 first rounds scaled render dimensions up, then rounds the local-size division up.
            return (int)Math.ceil(Math.ceil((float)pixels * scale) / local);
        }
    }

    /** Read-only integration hooks for the real-client compute/pixel probes. */
    public static long getDispatchCount() { return state == null ? 0 : state.dispatchCount; }
    public static String getLastDispatchPath() { return state == null ? null : state.lastDispatchPath; }
    public static int getLastGroupsX() { return state == null ? 0 : state.lastGroupsX; }
    public static int getLastGroupsY() { return state == null ? 0 : state.lastGroupsY; }
    public static int getLastGroupsZ() { return state == null ? 0 : state.lastGroupsZ; }
    public static boolean isReady() { return state != null && state.ready; }
    public static int getMainImageTexture(int index) { return state != null && state.ready && index >= 0 && index < Math.min(6, state.colorCount) ? state.colors.getA(index) : 0; }
    public static int getShadowImageTexture(int index) { return state != null && state.ready && index >= 0 && index < Math.min(2, state.shadowColorCount) ? state.shadowColors.get(index) : 0; }
    public static int getImageInternalFormat(int unit) { return state == null ? GL11.GL_RGBA8 : state.imageFormats[unit]; }
    public static int getComputeProgramCount(int programIndex) {
        return state != null && programIndex >= 0 && programIndex < state.computes.length && state.computes[programIndex] != null ? state.computes[programIndex].length : 0;
    }
    public static int getComputeProgramId(int programIndex, int suffixIndex) {
        return suffixIndex >= 0 && suffixIndex < getComputeProgramCount(programIndex) ? state.computes[programIndex][suffixIndex].id : 0;
    }
    public static int getShadowAlternateTexture(int index) { return state == null ? 0 : state.shadowAlternates[index]; }
}
