package mods.Hileb.optirefine.optifine.shaders.modern;

import mods.Hileb.optirefine.core.OptiRefineLog;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.renderer.GlStateManager;
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
    private static final int[] IMAGE_FORMATS = new int[8];
    private static final int[] MAIN_TEXTURES_A = new int[6];
    private static final int[] MAIN_FORMATS_A = new int[6];
    private static final int[] MAIN_FORMATS_B = new int[6];
    private static final int[] SHADOW_ALT = new int[8];
    private static final IntBuffer LOCAL_SIZE = BufferUtils.createIntBuffer(16);
    private static final int[] GROUP_LIMITS = new int[3];
    private static Program[] prepare;
    private static ICustomTexture[] prepareTextures;
    private static ICustomTexture[] shadowcompTextures;
    private static ICustomTexture[] gbuffersTextures;
    private static Program[] shadowcomp;
    private static Compute[][] computes;
    private static FlipTextures colors;
    private static IntBuffer depths;
    private static IntBuffer shadowColors;
    private static IntBuffer shadowDepths;
    private static int colorCount;
    private static int depthCount;
    private static int shadowColorCount;
    private static int shadowDepthCount;
    private static int shadowWidth;
    private static int shadowHeight;
    private static int mainFramebuffer;
    private static int shadowFramebuffer;
    private static boolean imagesRequested;
    private static int requestedImageMask;
    private static boolean dispatching;
    private static boolean ready;
    private static ModernShaderSource scanning;
    private static String scanningPath;
    private static int requiredColors;
    private static int requiredDepths;
    private static int requiredShadowColors;
    private static int requiredShadowDepths;
    private static String pendingError;
    private static String graphicsError;
    private static boolean modernRequested;
    private static boolean requiredCenterDepthSmooth;
    private static long dispatchCount;
    private static String lastDispatchPath;
    private static int lastGroupsX;
    private static int lastGroupsY;
    private static int lastGroupsZ;

    private ModernShaderPipeline() {}

    public static void register(Programs programs) {
        prepare = programs.makeComposites("prepare", 100);
        shadowcomp = programs.makeComposites("shadowcomp", 100);
        computes = new Compute[programs.getCount()][];
    }

    public static void initializeUniforms(ShaderUniforms uniforms) {
        for (int i = 0; i < IMAGE_NAMES.length; i++) IMAGE_UNIFORMS[i] = uniforms.make1i(IMAGE_NAMES[i]);
    }

    public static void beginPack() {
        close();
        requiredColors = requiredDepths = requiredShadowColors = requiredShadowDepths = 0;
        pendingError = null;
        graphicsError = null;
        modernRequested = false;
        requiredCenterDepthSmooth = false;
        scanning = null;
        dispatchCount = 0;
        lastDispatchPath = null;
    }

    public static void beginSource(String path) {
        scanning = new ModernShaderSource();
        scanningPath = path;
    }

    public static void sourceLine(String line) {
        if (scanningPath != null) {
            String name = scanningPath.substring(scanningPath.lastIndexOf('/') + 1);
            if (name.startsWith("prepare") || name.startsWith("shadowcomp")) modernRequested = true;
        }
        if (scanning != null) scanning.accept(line);
    }

    public static void endSource(Program program) {
        if (scanning == null) return;
        requirements(scanning, scanningPath);
        program.setCompositeMipmapSetting(program.getCompositeMipmapSetting() | scanning.mipmaps);
        scanning = null;
    }

    private static void requirements(ModernShaderSource source, String path) {
        requiredColors = Math.max(requiredColors, source.colors);
        requiredDepths = Math.max(requiredDepths, source.depths);
        requiredShadowColors = Math.max(requiredShadowColors, source.shadowColors);
        requiredShadowDepths = Math.max(requiredShadowDepths, source.shadowDepths);
        imagesRequested |= source.images;
        requestedImageMask |= source.imageMask;
        requiredCenterDepthSmooth |= source.centerDepthSmooth;
        if (source.images && !Shaders.capabilities.OpenGL42) pendingError = path + ": image uniforms require OpenGL 4.2 (current: " + Shaders.glVersionString + ")";
        if (source.error != null) pendingError = path + ": " + source.error;
    }

    public static int requiredColors() { return requiredColors; }
    public static int requiredDepths() { return requiredDepths; }
    public static int requiredShadowColors() { return requiredShadowColors; }
    public static int requiredShadowDepths() { return requiredShadowDepths; }
    public static boolean requiresCenterDepthSmooth() { return requiredCenterDepthSmooth; }

    public static void graphicsError(String message) { graphicsError = message; }

    public static void checkLoadError() {
        if (pendingError != null) throw failure(pendingError);
        if ((modernRequested || imagesRequested) && graphicsError != null) throw failure(graphicsError);
    }

    public static void loadComputes(Program owner, String vertexPath) {
        if (owner.getProgramStage() == ProgramStage.GBUFFERS || owner.getProgramStage() == ProgramStage.NONE || vertexPath.contains("<disabled>")) return;
        String base = vertexPath.substring(0, vertexPath.length() - 4);
        ArrayList<Compute> loaded = new ArrayList<>();
        try {
            for (int suffix = 0; suffix < 27; suffix++) {
                String path = base + (suffix == 0 ? "" : "_" + (char)('a' + suffix - 1)) + ".csh";
                Compute compute = loadCompute(owner, path);
                if (compute != null) {
                    loaded.add(compute);
                    // Publish immediately, so failure cleanup owns every successful link.
                    computes[owner.getIndex()] = loaded.toArray(new Compute[0]);
                }
            }
        } catch (IOException exception) {
            throw failure(base + ": " + exception.getMessage(), exception);
        }
        if (isShadowComposite(owner) && (owner.getId() != 0 || !loaded.isEmpty())) requiredShadowDepths = Math.max(requiredShadowDepths, 1);
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
        modernRequested = true;
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
        checkLoadError();
        if (Shaders.capabilities.OpenGL43) {
            for (int axis = 0; axis < 3; axis++) {
                LOCAL_SIZE.clear();
                GL30.glGetInteger(GL43.GL_MAX_COMPUTE_WORK_GROUP_COUNT, axis, LOCAL_SIZE);
                GROUP_LIMITS[axis] = LOCAL_SIZE.get(0);
            }
            for (Compute[] stages : computes) {
                if (stages == null) continue;
                for (Compute compute : stages) compute.resize();
            }
        }
        ready = true;
        bindImages();
    }

    public static void mainTargets(FlipTextures flip, IntBuffer depthTextures, int count, int depthCountIn, int framebuffer) {
        colors = flip;
        depths = depthTextures;
        colorCount = count;
        depthCount = depthCountIn;
        mainFramebuffer = framebuffer;
        if (imagesRequested) {
            int active = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            int texture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
            try {
                for (int i = 0; i < Math.min(count, 6); i++) {
                    MAIN_TEXTURES_A[i] = flip.getA(i);
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, flip.getA(i));
                    MAIN_FORMATS_A[i] = imageFormat(GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT));
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, flip.getB(i));
                    MAIN_FORMATS_B[i] = imageFormat(GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT));
                    if ((requestedImageMask & 1 << i) != 0) {
                        validateImageFormat(MAIN_FORMATS_A[i], IMAGE_NAMES[i]);
                        validateImageFormat(MAIN_FORMATS_B[i], IMAGE_NAMES[i]);
                    }
                }
            } finally { GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture); GL13.glActiveTexture(active); }
        }
        if (ready && Shaders.capabilities.OpenGL43) {
            for (Compute[] stages : computes) {
                if (stages != null) for (Compute compute : stages) compute.resize();
            }
        }
    }

    public static void shadowTargets(IntBuffer colorTextures, IntBuffer depthTextures, int count, int depthCountIn, int width, int height, int framebuffer) {
        shadowColors = colorTextures;
        shadowDepths = depthTextures;
        shadowColorCount = count;
        shadowDepthCount = depthCountIn;
        shadowWidth = width;
        shadowHeight = height;
        shadowFramebuffer = framebuffer;
        if (!imagesRequested && !hasGraphics(shadowcomp)) return;
        int active = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        int texture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        try {
            for (int i = 0; i < count; i++) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextures.get(i));
                int format = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT);
                if (i < 2) IMAGE_FORMATS[6 + i] = imageFormat(format);
                if (i < 2 && (requestedImageMask & 1 << (6 + i)) != 0) validateImageFormat(IMAGE_FORMATS[6 + i], IMAGE_NAMES[6 + i]);
                if (!hasGraphics(shadowcomp)) continue;
                SHADOW_ALT[i] = GL11.glGenTextures();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, SHADOW_ALT[i]);
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
        if (!imagesRequested || Shaders.activeProgramID == 0) return;
        graphicsBarrier();
        for (int i = 0; i < IMAGE_UNIFORMS.length; i++) IMAGE_UNIFORMS[i].setValue(i);
        if (ready) bindImages();
    }

    public static void bindImages() {
        if (!imagesRequested || !Shaders.capabilities.OpenGL42) return;
        for (int i = 0; i < 6; i++) {
            int texture = colors != null && i < colorCount && (requestedImageMask & 1 << i) != 0 ? colors.getA(i) : 0;
            if (texture != 0) IMAGE_FORMATS[i] = texture == MAIN_TEXTURES_A[i] ? MAIN_FORMATS_A[i] : MAIN_FORMATS_B[i];
            GL42.glBindImageTexture(i, texture, 0, false, 0, GL15_READ_WRITE, texture == 0 ? GL11.GL_RGBA8 : IMAGE_FORMATS[i]);
        }
        for (int i = 0; i < 2; i++) {
            int texture = shadowColors != null && i < shadowColorCount && (requestedImageMask & 1 << (6 + i)) != 0 ? shadowColors.get(i) : 0;
            GL42.glBindImageTexture(6 + i, texture, 0, false, 0, GL15_READ_WRITE, texture == 0 ? GL11.GL_RGBA8 : IMAGE_FORMATS[6 + i]);
        }
    }

    public static void graphicsBarrier() {
        if (ready && imagesRequested) GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT | GL42.GL_TEXTURE_FETCH_BARRIER_BIT);
    }
    private static final int GL15_READ_WRITE = 35002;

    public static boolean hasComputes(Program owner) {
        return computes != null && computes[owner.getIndex()] != null;
    }

    public static boolean hasStages(Program[] programs) {
        if (programs == null) return false;
        for (Program program : programs) if (program.getId() != 0 || hasComputes(program)) return true;
        return false;
    }

    private static boolean hasGraphics(Program[] programs) {
        if (programs == null) return false;
        for (Program program : programs) if (program.getId() != 0) return true;
        return false;
    }

    public static boolean isShadowComposite(Program program) { return program.getName().startsWith("shadowcomp"); }

    public static void requireShadowComposite(Program program) {
        if (isShadowComposite(program) && (program.getId() != 0 || hasComputes(program))) {
            requiredShadowDepths = Math.max(requiredShadowDepths, 1);
            if (program.getId() != 0) {
                String settings = program.getDrawBufSettings();
                requiredShadowColors = Math.max(requiredShadowColors, 1);
                if (settings != null) for (int i = 0; i < settings.length(); i++) {
                    int attachment = settings.charAt(i) - '0';
                    if (attachment >= 0 && attachment < 8) requiredShadowColors = Math.max(requiredShadowColors, attachment + 1);
                }
            }
        }
    }

    /** Compute writes main images in place; it never performs the graphics pass's ping-pong flip. */
    public static void dispatch(Program owner) {
        if (!ready || dispatching || !hasComputes(owner)) return;
        Compute[] stages = computes[owner.getIndex()];
        Program previous = Shaders.activeProgram;
        boolean shadow = Shaders.isShadowPass;
        boolean glowing = Shaders.isEntitiesGlowing;
        boolean rendering = Shaders.isRenderingDfb;
        dispatching = true;
        GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
        try {
            if (owner.getProgramStage() == ProgramStage.SHADOW) {
                bindSamplers();
                bindCustomTextures(gbuffersTextures);
            }
            Shaders.isShadowPass = false;
            Shaders.isEntitiesGlowing = false;
            Shaders.isRenderingDfb = false;
            for (Compute compute : stages) {
                Shaders.useProgram(compute.uniformProgram);
                if (compute.uniformProgram.getId() == 0) throw failure(compute.path + ": OpenGL rejected the linked compute program");
                bindImages();
                GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT | GL42.GL_TEXTURE_FETCH_BARRIER_BIT);
                GL43.glDispatchCompute(compute.x, compute.y, compute.z);
                GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT | GL42.GL_TEXTURE_FETCH_BARRIER_BIT);
                dispatchCount++;
                lastDispatchPath = compute.path;
                lastGroupsX = compute.x;
                lastGroupsY = compute.y;
                lastGroupsZ = compute.z;
                mipmaps(compute.metadata.mipmaps, isShadowComposite(owner));
            }
        } finally {
            Shaders.useProgram(previous);
            Shaders.isShadowPass = shadow;
            Shaders.isEntitiesGlowing = glowing;
            Shaders.isRenderingDfb = rendering;
            GL11.glPopAttrib();
            dispatching = false;
        }
    }

    private static void bindSamplers() {
        for (int i = 0; i < colorCount; i++) bindTexture(COLOR_UNITS[i], colors.getA(i));
        for (int i = 0; i < depthCount; i++) bindTexture(i == 0 ? 6 : 10 + i, depths.get(i));
        for (int i = 0; i < shadowDepthCount; i++) bindTexture(4 + i, shadowDepths.get(i));
        for (int i = 0; i < shadowColorCount; i++) bindTexture(13 + i, shadowColors.get(i));
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
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
        prepareTextures = prepareIn;
        shadowcompTextures = shadowcompIn;
        gbuffersTextures = gbuffersIn;
    }

    private static void bindTexture(int unit, int texture) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
    }

    private static void mipmaps(int mask, boolean shadow) {
        int count = shadow ? shadowColorCount : colorCount;
        for (int i = 0; i < count; i++) if ((mask & 1 << i) != 0) {
            bindTexture(shadow ? 13 + i : COLOR_UNITS[i], shadow ? shadowColors.get(i) : colors.getA(i));
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    public static void renderPrepare() { renderStages(prepare, false); }
    public static void renderShadowcomp() { renderStages(shadowcomp, true); }

    private static void renderStages(Program[] stages, boolean shadow) {
        if (!ready || !hasStages(stages)) return;
        Program previous = Shaders.activeProgram;
        boolean previousShadow = Shaders.isShadowPass;
        boolean previousGlow = Shaders.isEntitiesGlowing;
        boolean previousRendering = Shaders.isRenderingDfb;
        int framebuffer = GL11.glGetInteger(EXTFramebufferObject.GL_FRAMEBUFFER_BINDING_EXT);
        int matrixMode = GL11.glGetInteger(GL11.GL_MATRIX_MODE);
        int width = shadow ? shadowWidth : Shaders.renderWidth;
        int height = shadow ? shadowHeight : Shaders.renderHeight;
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
            EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, shadow ? shadowFramebuffer : mainFramebuffer);
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
                bindCustomTextures(shadow ? shadowcompTextures : prepareTextures);
                bindImages();
                dispatch(program);
                if (program.getId() == 0) continue;
                int count = shadow ? shadowColorCount : colorCount;
                for (int i = 0; i < count; i++) attach(i, shadow ? SHADOW_ALT[i] : colors.getB(i));
                GL20.glDrawBuffers(program.getDrawBuffers());
                Shaders.useProgram(program);
                mipmaps(program.getCompositeMipmapSetting(), shadow);
                RenderScale scale = program.getRenderScale();
                if (scale != null) GL11.glViewport((int)(width * scale.getOffsetX()), (int)(height * scale.getOffsetY()), (int)(width * scale.getScale()), (int)(height * scale.getScale()));
                Shaders.drawComposite();
                if (imagesRequested) GL42.glMemoryBarrier(GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT | GL42.GL_TEXTURE_FETCH_BARRIER_BIT);
                GL11.glViewport(0, 0, width, height);
                for (int i = 0; i < count; i++) if (program.getToggleColorTextures()[i]) {
                    if (shadow) {
                        int current = shadowColors.get(i);
                        shadowColors.put(i, SHADOW_ALT[i]);
                        SHADOW_ALT[i] = current;
                    } else colors.flip(i);
                }
            }
        } finally {
            int count = shadow ? shadowColorCount : colorCount;
            for (int i = 0; i < count; i++) attach(i, shadow ? shadowColors.get(i) : colors.getA(i));
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
            bindImages();
        }
    }

    private static void attach(int attachment, int texture) {
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT + attachment, GL11.GL_TEXTURE_2D, texture, 0);
    }

    public static void deleteShadowAlternates() {
        for (int i = 0; i < SHADOW_ALT.length; i++) if (SHADOW_ALT[i] != 0) {
            GlStateManager.deleteTexture(SHADOW_ALT[i]);
            SHADOW_ALT[i] = 0;
        }
    }

    public static void close() {
        ready = false;
        scanning = null;
        deleteShadowAlternates();
        deleteCustomTextures(prepareTextures);
        deleteCustomTextures(shadowcompTextures);
        prepareTextures = shadowcompTextures = gbuffersTextures = null;
        if (computes != null) for (int i = 0; i < computes.length; i++) {
            Compute[] stages = computes[i];
            if (stages != null) for (Compute compute : stages) GL20.glDeleteProgram(compute.id);
            computes[i] = null;
        }
        if (imagesRequested && Shaders.capabilities != null && Shaders.capabilities.OpenGL42) {
            for (int i = 0; i < 8; i++) GL42.glBindImageTexture(i, 0, 0, false, 0, GL15_READ_WRITE, GL11.GL_RGBA8);
        }
        imagesRequested = false;
        requestedImageMask = 0;
        colors = null;
        depths = shadowColors = shadowDepths = null;
        colorCount = depthCount = shadowColorCount = shadowDepthCount = 0;
        mainFramebuffer = shadowFramebuffer = 0;
        Arrays.fill(IMAGE_FORMATS, GL11.GL_RGBA8);
    }

    private static void deleteCustomTextures(ICustomTexture[] textures) {
        if (textures != null) for (ICustomTexture texture : textures) texture.deleteTexture();
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

    private static final class Compute {
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
    public static long getDispatchCount() { return dispatchCount; }
    public static String getLastDispatchPath() { return lastDispatchPath; }
    public static int getLastGroupsX() { return lastGroupsX; }
    public static int getLastGroupsY() { return lastGroupsY; }
    public static int getLastGroupsZ() { return lastGroupsZ; }
    public static boolean isReady() { return ready; }
    public static int getMainImageTexture(int index) { return ready && index >= 0 && index < Math.min(6, colorCount) ? colors.getA(index) : 0; }
    public static int getShadowImageTexture(int index) { return ready && index >= 0 && index < Math.min(2, shadowColorCount) ? shadowColors.get(index) : 0; }
    public static int getImageInternalFormat(int unit) { return IMAGE_FORMATS[unit]; }
    public static int getComputeProgramCount(int programIndex) {
        return computes != null && programIndex >= 0 && programIndex < computes.length && computes[programIndex] != null ? computes[programIndex].length : 0;
    }
    public static int getComputeProgramId(int programIndex, int suffixIndex) {
        return suffixIndex >= 0 && suffixIndex < getComputeProgramCount(programIndex) ? computes[programIndex][suffixIndex].id : 0;
    }
    public static int getShadowAlternateTexture(int index) { return SHADOW_ALT[index]; }
}
