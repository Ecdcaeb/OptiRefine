package mods.Hileb.optirefine.optifine.shaders.modern;

import net.minecraft.client.renderer.GlStateManager;
import net.optifine.shaders.FlipTextures;
import net.optifine.shaders.ICustomTexture;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.Program;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL42;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Set;

/** Resources and metadata belonging to one shader-pack load. */
final class ShaderPackState implements AutoCloseable {
    static final Program[] NO_STAGES = new Program[0];
    Program[] preparePrograms = NO_STAGES;
    Program[] shadowcompPrograms = NO_STAGES;

    final int[] imageFormats = new int[8];
    private final int[] boundImageTextures = new int[8];
    private final int[] boundImageFormats = new int[8];
    final int[] mainTexturesA = new int[6];
    final int[] mainFormatsA = new int[6];

    final int[] mainFormatsB = new int[6];
    final int[] shadowAlternates = new int[8];
    final ModernShaderPipeline.Compute[][] computes;

    ICustomTexture[] prepareTextures;
    ICustomTexture[] shadowcompTextures;
    ICustomTexture[] gbuffersTextures; // borrowed from the pack loader; never deleted here

    FlipTextures colors; // borrowed G5 targets
    IntBuffer depths; // borrowed G5 targets
    IntBuffer shadowColors; // borrowed G5 targets
    IntBuffer shadowDepths; // borrowed G5 targets
    int colorCount;
    int depthCount;
    int shadowColorCount;
    int shadowDepthCount;
    int shadowWidth;
    int shadowHeight;
    int mainFramebuffer;
    int shadowFramebuffer;

    boolean imagesRequested;
    int requestedImageMask;
    boolean dispatching;
    boolean ready;
    ModernShaderSource scanning;
    String scanningPath;
    int requiredDepths;
    int requiredColors;
    int requiredShadowColors;
    int requiredShadowDepths;
    String pendingError;
    String graphicsError;
    boolean modernRequested;
    boolean requiredCenterDepthSmooth;
    long dispatchCount;
    String lastDispatchPath;
    int lastGroupsX;
    int lastGroupsY;
    int lastGroupsZ;

    private boolean closed;

    ShaderPackState(int programCount) {
        this.computes = new ModernShaderPipeline.Compute[programCount][];
        Arrays.fill(imageFormats, GL11.GL_RGBA8);
        invalidateImageBindings();
    }

    boolean isClosed() { return closed; }

    void invalidateImageBindings() {
        Arrays.fill(boundImageTextures, -1);
    }

    void bindImage(int unit, int texture, int format) {
        if (boundImageTextures[unit] == texture && boundImageFormats[unit] == format) return;
        GL42.glBindImageTexture(unit, texture, 0, false, 0, 35002, format);
        boundImageTextures[unit] = texture;
        boundImageFormats[unit] = format;
    }

    void deleteShadowAlternates() {
        for (int i = 0; i < shadowAlternates.length; i++) {
            if (shadowAlternates[i] != 0) {
                GlStateManager.deleteTexture(shadowAlternates[i]);
                shadowAlternates[i] = 0;
            }
        }
    }
 
    @Override
    public void close() {
        if (closed) return;
        closed = true;
        ready = false;
        preparePrograms = shadowcompPrograms = NO_STAGES;
        scanning = null;
        deleteShadowAlternates();

        Set<ICustomTexture> deleted = java.util.Collections.newSetFromMap(new IdentityHashMap<>());
        deleteOwnedTextures(prepareTextures, deleted);
        deleteOwnedTextures(shadowcompTextures, deleted);
        prepareTextures = null;
        shadowcompTextures = null;
        gbuffersTextures = null;

        for (int i = 0; i < computes.length; i++) {
            ModernShaderPipeline.Compute[] stages = computes[i];
            if (stages != null) {
                for (ModernShaderPipeline.Compute compute : stages) GL20.glDeleteProgram(compute.id);
                computes[i] = null;
            }
        }

        if (imagesRequested && Shaders.capabilities != null && Shaders.capabilities.OpenGL42) {
            for (int i = 0; i < 8; i++) GL42.glBindImageTexture(i, 0, 0, false, 0, 35002, GL11.GL_RGBA8);
        }
        imagesRequested = false;
        requestedImageMask = 0;
        colors = null;
        depths = shadowColors = shadowDepths = null;
        colorCount = depthCount = shadowColorCount = shadowDepthCount = 0;
        mainFramebuffer = shadowFramebuffer = 0;
        Arrays.fill(imageFormats, GL11.GL_RGBA8);
        Arrays.fill(mainTexturesA, 0);
        Arrays.fill(mainFormatsA, 0);
        Arrays.fill(mainFormatsB, 0);
    }

    private static void deleteOwnedTextures(ICustomTexture[] textures, Set<ICustomTexture> deleted) {
        if (textures == null) return;
        for (ICustomTexture texture : textures) {
            if (texture != null && deleted.add(texture)) texture.deleteTexture();
        }
    }
}
