/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Object
 *  java.lang.String
 *  java.nio.ByteBuffer
 *  java.nio.FloatBuffer
 *  java.nio.IntBuffer
 *  javax.annotation.Nullable
 *  net.minecraft.client.renderer.GlStateManager$1
 *  net.minecraft.client.renderer.GlStateManager$AlphaState
 *  net.minecraft.client.renderer.GlStateManager$BlendState
 *  net.minecraft.client.renderer.GlStateManager$BooleanState
 *  net.minecraft.client.renderer.GlStateManager$ClearState
 *  net.minecraft.client.renderer.GlStateManager$Color
 *  net.minecraft.client.renderer.GlStateManager$ColorLogicState
 *  net.minecraft.client.renderer.GlStateManager$ColorMask
 *  net.minecraft.client.renderer.GlStateManager$ColorMaterialState
 *  net.minecraft.client.renderer.GlStateManager$CullFace
 *  net.minecraft.client.renderer.GlStateManager$CullState
 *  net.minecraft.client.renderer.GlStateManager$DepthState
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$FogMode
 *  net.minecraft.client.renderer.GlStateManager$FogState
 *  net.minecraft.client.renderer.GlStateManager$LogicOp
 *  net.minecraft.client.renderer.GlStateManager$PolygonOffsetState
 *  net.minecraft.client.renderer.GlStateManager$Profile
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.GlStateManager$StencilState
 *  net.minecraft.client.renderer.GlStateManager$TexGen
 *  net.minecraft.client.renderer.GlStateManager$TexGenCoord
 *  net.minecraft.client.renderer.GlStateManager$TexGenState
 *  net.minecraft.client.renderer.GlStateManager$TextureState
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.optifine.SmartAnimations
 *  net.optifine.render.GlAlphaState
 *  net.optifine.render.GlBlendState
 *  net.optifine.shaders.Shaders
 *  net.optifine.util.LockCounter
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL14
 *  org.lwjgl.util.vector.Quaternion
 */
package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.optifine.SmartAnimations;
import net.optifine.render.GlAlphaState;
import net.optifine.render.GlBlendState;
import net.optifine.shaders.Shaders;
import net.optifine.util.LockCounter;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Quaternion;

/*
 * Exception performing whole class analysis ignored.
 */
public class GlStateManager {
    private static final FloatBuffer BUF_FLOAT_16 = BufferUtils.createFloatBuffer((int)16);
    private static final FloatBuffer BUF_FLOAT_4 = BufferUtils.createFloatBuffer((int)4);
    private static final AlphaState alphaState = new AlphaState(null);
    private static final BooleanState lightingState = new BooleanState(2896);
    private static final BooleanState[] lightState = new BooleanState[8];
    private static final ColorMaterialState colorMaterialState;
    private static final BlendState blendState;
    private static final DepthState depthState;
    private static final FogState fogState;
    private static final CullState cullState;
    private static final PolygonOffsetState polygonOffsetState;
    private static final ColorLogicState colorLogicState;
    private static final TexGenState texGenState;
    private static final ClearState clearState;
    private static final StencilState stencilState;
    private static final BooleanState normalizeState;
    private static int activeTextureUnit;
    private static final TextureState[] textureState;
    private static int activeShadeModel;
    private static final BooleanState rescaleNormalState;
    private static final ColorMask colorMaskState;
    private static final Color colorState;
    public static boolean clearEnabled;
    private static LockCounter alphaLock;
    private static GlAlphaState alphaLockState;
    private static LockCounter blendLock;
    private static GlBlendState blendLockState;
    private static boolean creatingDisplayList;

    public static void pushAttrib() {
        GL11.glPushAttrib((int)8256);
    }

    public static void popAttrib() {
        GL11.glPopAttrib();
    }

    public static void disableAlpha() {
        if (alphaLock.isLocked()) {
            alphaLockState.setDisabled();
            return;
        }
        GlStateManager.alphaState.alphaTest.setDisabled();
    }

    public static void enableAlpha() {
        if (alphaLock.isLocked()) {
            alphaLockState.setEnabled();
            return;
        }
        GlStateManager.alphaState.alphaTest.setEnabled();
    }

    public static void alphaFunc(int func, float ref) {
        if (alphaLock.isLocked()) {
            alphaLockState.setFuncRef(func, ref);
            return;
        }
        if (func != GlStateManager.alphaState.func || ref != GlStateManager.alphaState.ref) {
            GlStateManager.alphaState.func = func;
            GlStateManager.alphaState.ref = ref;
            GL11.glAlphaFunc((int)func, (float)ref);
        }
    }

    public static void enableLighting() {
        lightingState.setEnabled();
    }

    public static void disableLighting() {
        lightingState.setDisabled();
    }

    public static void enableLight(int light) {
        lightState[light].setEnabled();
    }

    public static void disableLight(int light) {
        lightState[light].setDisabled();
    }

    public static void enableColorMaterial() {
        GlStateManager.colorMaterialState.colorMaterial.setEnabled();
    }

    public static void disableColorMaterial() {
        GlStateManager.colorMaterialState.colorMaterial.setDisabled();
    }

    public static void colorMaterial(int face, int mode) {
        if (face != GlStateManager.colorMaterialState.face || mode != GlStateManager.colorMaterialState.mode) {
            GlStateManager.colorMaterialState.face = face;
            GlStateManager.colorMaterialState.mode = mode;
            GL11.glColorMaterial((int)face, (int)mode);
        }
    }

    public static void glLight(int light, int pname, FloatBuffer params) {
        GL11.glLight((int)light, (int)pname, (FloatBuffer)params);
    }

    public static void glLightModel(int pname, FloatBuffer params) {
        GL11.glLightModel((int)pname, (FloatBuffer)params);
    }

    public static void glNormal3f(float nx, float ny, float nz) {
        GL11.glNormal3f((float)nx, (float)ny, (float)nz);
    }

    public static void disableDepth() {
        GlStateManager.depthState.depthTest.setDisabled();
    }

    public static void enableDepth() {
        GlStateManager.depthState.depthTest.setEnabled();
    }

    public static void depthFunc(int depthFunc) {
        if (depthFunc != GlStateManager.depthState.depthFunc) {
            GlStateManager.depthState.depthFunc = depthFunc;
            GL11.glDepthFunc((int)depthFunc);
        }
    }

    public static void depthMask(boolean flagIn) {
        if (flagIn != GlStateManager.depthState.maskEnabled) {
            GlStateManager.depthState.maskEnabled = flagIn;
            GL11.glDepthMask((boolean)flagIn);
        }
    }

    public static void disableBlend() {
        if (blendLock.isLocked()) {
            blendLockState.setDisabled();
            return;
        }
        GlStateManager.blendState.blend.setDisabled();
    }

    public static void enableBlend() {
        if (blendLock.isLocked()) {
            blendLockState.setEnabled();
            return;
        }
        GlStateManager.blendState.blend.setEnabled();
    }

    public static void blendFunc(SourceFactor srcFactor, DestFactor dstFactor) {
        GlStateManager.blendFunc(srcFactor.factor, dstFactor.factor);
    }

    public static void blendFunc(int srcFactor, int dstFactor) {
        if (blendLock.isLocked()) {
            blendLockState.setFactors(srcFactor, dstFactor);
            return;
        }
        if (srcFactor != GlStateManager.blendState.srcFactor || dstFactor != GlStateManager.blendState.dstFactor || srcFactor != GlStateManager.blendState.srcFactorAlpha || dstFactor != GlStateManager.blendState.dstFactorAlpha) {
            GlStateManager.blendState.srcFactor = srcFactor;
            GlStateManager.blendState.dstFactor = dstFactor;
            GlStateManager.blendState.srcFactorAlpha = srcFactor;
            GlStateManager.blendState.dstFactorAlpha = dstFactor;
            if (Config.isShaders()) {
                Shaders.uniform_blendFunc.setValue(srcFactor, dstFactor, srcFactor, dstFactor);
            }
            GL11.glBlendFunc((int)srcFactor, (int)dstFactor);
        }
    }

    public static void tryBlendFuncSeparate(SourceFactor srcFactor, DestFactor dstFactor, SourceFactor srcFactorAlpha, DestFactor dstFactorAlpha) {
        GlStateManager.tryBlendFuncSeparate(srcFactor.factor, dstFactor.factor, srcFactorAlpha.factor, dstFactorAlpha.factor);
    }

    public static void tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha) {
        if (blendLock.isLocked()) {
            blendLockState.setFactors(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
            return;
        }
        if (srcFactor != GlStateManager.blendState.srcFactor || dstFactor != GlStateManager.blendState.dstFactor || srcFactorAlpha != GlStateManager.blendState.srcFactorAlpha || dstFactorAlpha != GlStateManager.blendState.dstFactorAlpha) {
            GlStateManager.blendState.srcFactor = srcFactor;
            GlStateManager.blendState.dstFactor = dstFactor;
            GlStateManager.blendState.srcFactorAlpha = srcFactorAlpha;
            GlStateManager.blendState.dstFactorAlpha = dstFactorAlpha;
            if (Config.isShaders()) {
                Shaders.uniform_blendFunc.setValue(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
            }
            OpenGlHelper.glBlendFunc((int)srcFactor, (int)dstFactor, (int)srcFactorAlpha, (int)dstFactorAlpha);
        }
    }

    public static void glBlendEquation(int blendEquation) {
        GL14.glBlendEquation((int)blendEquation);
    }

    public static void enableOutlineMode(int p_187431_0_) {
        BUF_FLOAT_4.put(0, (float)(p_187431_0_ >> 16 & 0xFF) / 255.0f);
        BUF_FLOAT_4.put(1, (float)(p_187431_0_ >> 8 & 0xFF) / 255.0f);
        BUF_FLOAT_4.put(2, (float)(p_187431_0_ >> 0 & 0xFF) / 255.0f);
        BUF_FLOAT_4.put(3, (float)(p_187431_0_ >> 24 & 0xFF) / 255.0f);
        GlStateManager.glTexEnv(8960, 8705, BUF_FLOAT_4);
        GlStateManager.glTexEnvi(8960, 8704, 34160);
        GlStateManager.glTexEnvi(8960, 34161, 7681);
        GlStateManager.glTexEnvi(8960, 34176, 34166);
        GlStateManager.glTexEnvi(8960, 34192, 768);
        GlStateManager.glTexEnvi(8960, 34162, 7681);
        GlStateManager.glTexEnvi(8960, 34184, 5890);
        GlStateManager.glTexEnvi(8960, 34200, 770);
    }

    public static void disableOutlineMode() {
        GlStateManager.glTexEnvi(8960, 8704, 8448);
        GlStateManager.glTexEnvi(8960, 34161, 8448);
        GlStateManager.glTexEnvi(8960, 34162, 8448);
        GlStateManager.glTexEnvi(8960, 34176, 5890);
        GlStateManager.glTexEnvi(8960, 34184, 5890);
        GlStateManager.glTexEnvi(8960, 34192, 768);
        GlStateManager.glTexEnvi(8960, 34200, 770);
    }

    public static void enableFog() {
        GlStateManager.fogState.fog.setEnabled();
    }

    public static void disableFog() {
        GlStateManager.fogState.fog.setDisabled();
    }

    public static void setFog(FogMode fogMode) {
        GlStateManager.setFog(fogMode.capabilityId);
    }

    private static void setFog(int param) {
        if (param != GlStateManager.fogState.mode) {
            GlStateManager.fogState.mode = param;
            GL11.glFogi((int)2917, (int)param);
            if (Config.isShaders()) {
                Shaders.setFogMode((int)param);
            }
        }
    }

    public static void setFogDensity(float param) {
        if (param < 0.0f) {
            param = 0.0f;
        }
        if (param != GlStateManager.fogState.density) {
            GlStateManager.fogState.density = param;
            GL11.glFogf((int)2914, (float)param);
            if (Config.isShaders()) {
                Shaders.setFogDensity((float)param);
            }
        }
    }

    public static void setFogStart(float param) {
        if (param != GlStateManager.fogState.start) {
            GlStateManager.fogState.start = param;
            GL11.glFogf((int)2915, (float)param);
        }
    }

    public static void setFogEnd(float param) {
        if (param != GlStateManager.fogState.end) {
            GlStateManager.fogState.end = param;
            GL11.glFogf((int)2916, (float)param);
        }
    }

    public static void glFog(int pname, FloatBuffer param) {
        GL11.glFog((int)pname, (FloatBuffer)param);
    }

    public static void glFogi(int pname, int param) {
        GL11.glFogi((int)pname, (int)param);
    }

    public static void enableCull() {
        GlStateManager.cullState.cullFace.setEnabled();
    }

    public static void disableCull() {
        GlStateManager.cullState.cullFace.setDisabled();
    }

    public static void cullFace(CullFace cullFace) {
        GlStateManager.cullFace(cullFace.mode);
    }

    private static void cullFace(int mode) {
        if (mode != GlStateManager.cullState.mode) {
            GlStateManager.cullState.mode = mode;
            GL11.glCullFace((int)mode);
        }
    }

    public static void glPolygonMode(int face, int mode) {
        GL11.glPolygonMode((int)face, (int)mode);
    }

    public static void enablePolygonOffset() {
        GlStateManager.polygonOffsetState.polygonOffsetFill.setEnabled();
    }

    public static void disablePolygonOffset() {
        GlStateManager.polygonOffsetState.polygonOffsetFill.setDisabled();
    }

    public static void doPolygonOffset(float factor, float units) {
        if (factor != GlStateManager.polygonOffsetState.factor || units != GlStateManager.polygonOffsetState.units) {
            GlStateManager.polygonOffsetState.factor = factor;
            GlStateManager.polygonOffsetState.units = units;
            GL11.glPolygonOffset((float)factor, (float)units);
        }
    }

    public static void enableColorLogic() {
        GlStateManager.colorLogicState.colorLogicOp.setEnabled();
    }

    public static void disableColorLogic() {
        GlStateManager.colorLogicState.colorLogicOp.setDisabled();
    }

    public static void colorLogicOp(LogicOp logicOperation) {
        GlStateManager.colorLogicOp(logicOperation.opcode);
    }

    public static void colorLogicOp(int opcode) {
        if (opcode != GlStateManager.colorLogicState.opcode) {
            GlStateManager.colorLogicState.opcode = opcode;
            GL11.glLogicOp((int)opcode);
        }
    }

    public static void enableTexGenCoord(TexGen texGen) {
        GlStateManager.texGenCoord((TexGen)texGen).textureGen.setEnabled();
    }

    public static void disableTexGenCoord(TexGen texGen) {
        GlStateManager.texGenCoord((TexGen)texGen).textureGen.setDisabled();
    }

    public static void texGen(TexGen texGen, int param) {
        TexGenCoord glstatemanager$texgencoord = GlStateManager.texGenCoord(texGen);
        if (param != glstatemanager$texgencoord.param) {
            glstatemanager$texgencoord.param = param;
            GL11.glTexGeni((int)glstatemanager$texgencoord.coord, (int)9472, (int)param);
        }
    }

    public static void texGen(TexGen texGen, int pname, FloatBuffer params) {
        GL11.glTexGen((int)GlStateManager.texGenCoord((TexGen)texGen).coord, (int)pname, (FloatBuffer)params);
    }

    private static TexGenCoord texGenCoord(TexGen texGen) {
        switch (1.$SwitchMap$net$minecraft$client$renderer$GlStateManager$TexGen[texGen.ordinal()]) {
            case 1: {
                return GlStateManager.texGenState.s;
            }
            case 2: {
                return GlStateManager.texGenState.t;
            }
            case 3: {
                return GlStateManager.texGenState.r;
            }
            case 4: {
                return GlStateManager.texGenState.q;
            }
        }
        return GlStateManager.texGenState.s;
    }

    public static void setActiveTexture(int texture) {
        if (activeTextureUnit != texture - OpenGlHelper.defaultTexUnit) {
            activeTextureUnit = texture - OpenGlHelper.defaultTexUnit;
            OpenGlHelper.setActiveTexture((int)texture);
        }
    }

    public static void enableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setEnabled();
    }

    public static void disableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setDisabled();
    }

    public static void glTexEnv(int target, int parameterName, FloatBuffer parameters) {
        GL11.glTexEnv((int)target, (int)parameterName, (FloatBuffer)parameters);
    }

    public static void glTexEnvi(int target, int parameterName, int parameter) {
        GL11.glTexEnvi((int)target, (int)parameterName, (int)parameter);
    }

    public static void glTexEnvf(int target, int parameterName, float parameter) {
        GL11.glTexEnvf((int)target, (int)parameterName, (float)parameter);
    }

    public static void glTexParameterf(int target, int parameterName, float parameter) {
        GL11.glTexParameterf((int)target, (int)parameterName, (float)parameter);
    }

    public static void glTexParameteri(int target, int parameterName, int parameter) {
        GL11.glTexParameteri((int)target, (int)parameterName, (int)parameter);
    }

    public static int glGetTexLevelParameteri(int target, int level, int parameterName) {
        return GL11.glGetTexLevelParameteri((int)target, (int)level, (int)parameterName);
    }

    public static int generateTexture() {
        return GL11.glGenTextures();
    }

    public static void deleteTexture(int texture) {
        if (texture == 0) {
            return;
        }
        GL11.glDeleteTextures((int)texture);
        for (TextureState glstatemanager$texturestate : textureState) {
            if (glstatemanager$texturestate.textureName != texture) continue;
            glstatemanager$texturestate.textureName = 0;
        }
    }

    public static void bindTexture(int texture) {
        if (texture != GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName) {
            GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = texture;
            GL11.glBindTexture((int)3553, (int)texture);
            if (SmartAnimations.isActive()) {
                SmartAnimations.textureRendered((int)texture);
            }
        }
    }

    public static void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, @Nullable IntBuffer pixels) {
        GL11.glTexImage2D((int)target, (int)level, (int)internalFormat, (int)width, (int)height, (int)border, (int)format, (int)type, (IntBuffer)pixels);
    }

    public static void glTexSubImage2D(int target, int level, int xOffset, int yOffset, int width, int height, int format, int type, IntBuffer pixels) {
        GL11.glTexSubImage2D((int)target, (int)level, (int)xOffset, (int)yOffset, (int)width, (int)height, (int)format, (int)type, (IntBuffer)pixels);
    }

    public static void glCopyTexSubImage2D(int target, int level, int xOffset, int yOffset, int x, int y, int width, int height) {
        GL11.glCopyTexSubImage2D((int)target, (int)level, (int)xOffset, (int)yOffset, (int)x, (int)y, (int)width, (int)height);
    }

    public static void glGetTexImage(int target, int level, int format, int type, IntBuffer pixels) {
        GL11.glGetTexImage((int)target, (int)level, (int)format, (int)type, (IntBuffer)pixels);
    }

    public static void enableNormalize() {
        normalizeState.setEnabled();
    }

    public static void disableNormalize() {
        normalizeState.setDisabled();
    }

    public static void shadeModel(int mode) {
        if (mode != activeShadeModel) {
            activeShadeModel = mode;
            GL11.glShadeModel((int)mode);
        }
    }

    public static void enableRescaleNormal() {
        rescaleNormalState.setEnabled();
    }

    public static void disableRescaleNormal() {
        rescaleNormalState.setDisabled();
    }

    public static void viewport(int x, int y, int width, int height) {
        GL11.glViewport((int)x, (int)y, (int)width, (int)height);
    }

    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        if (red != GlStateManager.colorMaskState.red || green != GlStateManager.colorMaskState.green || blue != GlStateManager.colorMaskState.blue || alpha != GlStateManager.colorMaskState.alpha) {
            GlStateManager.colorMaskState.red = red;
            GlStateManager.colorMaskState.green = green;
            GlStateManager.colorMaskState.blue = blue;
            GlStateManager.colorMaskState.alpha = alpha;
            GL11.glColorMask((boolean)red, (boolean)green, (boolean)blue, (boolean)alpha);
        }
    }

    public static void clearDepth(double depth) {
        if (depth != GlStateManager.clearState.depth) {
            GlStateManager.clearState.depth = depth;
            GL11.glClearDepth((double)depth);
        }
    }

    public static void clearColor(float red, float green, float blue, float alpha) {
        if (red != GlStateManager.clearState.color.red || green != GlStateManager.clearState.color.green || blue != GlStateManager.clearState.color.blue || alpha != GlStateManager.clearState.color.alpha) {
            GlStateManager.clearState.color.red = red;
            GlStateManager.clearState.color.green = green;
            GlStateManager.clearState.color.blue = blue;
            GlStateManager.clearState.color.alpha = alpha;
            GL11.glClearColor((float)red, (float)green, (float)blue, (float)alpha);
        }
    }

    public static void clear(int mask) {
        if (!clearEnabled) {
            return;
        }
        GL11.glClear((int)mask);
    }

    public static void matrixMode(int mode) {
        GL11.glMatrixMode((int)mode);
    }

    public static void loadIdentity() {
        GL11.glLoadIdentity();
    }

    public static void pushMatrix() {
        GL11.glPushMatrix();
    }

    public static void popMatrix() {
        GL11.glPopMatrix();
    }

    public static void getFloat(int pname, FloatBuffer params) {
        GL11.glGetFloat((int)pname, (FloatBuffer)params);
    }

    public static void ortho(double left, double right, double bottom, double top, double zNear, double zFar) {
        GL11.glOrtho((double)left, (double)right, (double)bottom, (double)top, (double)zNear, (double)zFar);
    }

    public static void rotate(float angle, float x, float y, float z) {
        GL11.glRotatef((float)angle, (float)x, (float)y, (float)z);
    }

    public static void scale(float x, float y, float z) {
        GL11.glScalef((float)x, (float)y, (float)z);
    }

    public static void scale(double x, double y, double z) {
        GL11.glScaled((double)x, (double)y, (double)z);
    }

    public static void translate(float x, float y, float z) {
        GL11.glTranslatef((float)x, (float)y, (float)z);
    }

    public static void translate(double x, double y, double z) {
        GL11.glTranslated((double)x, (double)y, (double)z);
    }

    public static void multMatrix(FloatBuffer matrix) {
        GL11.glMultMatrix((FloatBuffer)matrix);
    }

    public static void rotate(Quaternion quaternionIn) {
        GlStateManager.multMatrix(GlStateManager.quatToGlMatrix(BUF_FLOAT_16, quaternionIn));
    }

    public static FloatBuffer quatToGlMatrix(FloatBuffer buffer, Quaternion quaternionIn) {
        buffer.clear();
        float f = quaternionIn.x * quaternionIn.x;
        float f1 = quaternionIn.x * quaternionIn.y;
        float f2 = quaternionIn.x * quaternionIn.z;
        float f3 = quaternionIn.x * quaternionIn.w;
        float f4 = quaternionIn.y * quaternionIn.y;
        float f5 = quaternionIn.y * quaternionIn.z;
        float f6 = quaternionIn.y * quaternionIn.w;
        float f7 = quaternionIn.z * quaternionIn.z;
        float f8 = quaternionIn.z * quaternionIn.w;
        buffer.put(1.0f - 2.0f * (f4 + f7));
        buffer.put(2.0f * (f1 + f8));
        buffer.put(2.0f * (f2 - f6));
        buffer.put(0.0f);
        buffer.put(2.0f * (f1 - f8));
        buffer.put(1.0f - 2.0f * (f + f7));
        buffer.put(2.0f * (f5 + f3));
        buffer.put(0.0f);
        buffer.put(2.0f * (f2 + f6));
        buffer.put(2.0f * (f5 - f3));
        buffer.put(1.0f - 2.0f * (f + f4));
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(1.0f);
        buffer.rewind();
        return buffer;
    }

    public static void color(float colorRed, float colorGreen, float colorBlue, float colorAlpha) {
        if (colorRed != GlStateManager.colorState.red || colorGreen != GlStateManager.colorState.green || colorBlue != GlStateManager.colorState.blue || colorAlpha != GlStateManager.colorState.alpha) {
            GlStateManager.colorState.red = colorRed;
            GlStateManager.colorState.green = colorGreen;
            GlStateManager.colorState.blue = colorBlue;
            GlStateManager.colorState.alpha = colorAlpha;
            GL11.glColor4f((float)colorRed, (float)colorGreen, (float)colorBlue, (float)colorAlpha);
        }
    }

    public static void color(float colorRed, float colorGreen, float colorBlue) {
        GlStateManager.color(colorRed, colorGreen, colorBlue, 1.0f);
    }

    public static void glTexCoord2f(float sCoord, float tCoord) {
        GL11.glTexCoord2f((float)sCoord, (float)tCoord);
    }

    public static void glVertex3f(float x, float y, float z) {
        GL11.glVertex3f((float)x, (float)y, (float)z);
    }

    public static void resetColor() {
        GlStateManager.colorState.red = -1.0f;
        GlStateManager.colorState.green = -1.0f;
        GlStateManager.colorState.blue = -1.0f;
        GlStateManager.colorState.alpha = -1.0f;
    }

    public static void glNormalPointer(int type, int stride, ByteBuffer buffer) {
        GL11.glNormalPointer((int)type, (int)stride, (ByteBuffer)buffer);
    }

    public static void glTexCoordPointer(int size, int type, int stride, int buffer_offset) {
        GL11.glTexCoordPointer((int)size, (int)type, (int)stride, (long)buffer_offset);
    }

    public static void glTexCoordPointer(int size, int type, int stride, ByteBuffer buffer) {
        GL11.glTexCoordPointer((int)size, (int)type, (int)stride, (ByteBuffer)buffer);
    }

    public static void glVertexPointer(int size, int type, int stride, int buffer_offset) {
        GL11.glVertexPointer((int)size, (int)type, (int)stride, (long)buffer_offset);
    }

    public static void glVertexPointer(int size, int type, int stride, ByteBuffer buffer) {
        GL11.glVertexPointer((int)size, (int)type, (int)stride, (ByteBuffer)buffer);
    }

    public static void glColorPointer(int size, int type, int stride, int buffer_offset) {
        GL11.glColorPointer((int)size, (int)type, (int)stride, (long)buffer_offset);
    }

    public static void glColorPointer(int size, int type, int stride, ByteBuffer buffer) {
        GL11.glColorPointer((int)size, (int)type, (int)stride, (ByteBuffer)buffer);
    }

    public static void glDisableClientState(int cap) {
        GL11.glDisableClientState((int)cap);
    }

    public static void glEnableClientState(int cap) {
        GL11.glEnableClientState((int)cap);
    }

    public static void glBegin(int mode) {
        GL11.glBegin((int)mode);
    }

    public static void glEnd() {
        GL11.glEnd();
    }

    public static void glDrawArrays(int mode, int first, int count) {
        int countInstances;
        GL11.glDrawArrays((int)mode, (int)first, (int)count);
        if (Config.isShaders() && !creatingDisplayList && (countInstances = Shaders.activeProgram.getCountInstances()) > 1) {
            for (int i = 1; i < countInstances; ++i) {
                Shaders.uniform_instanceId.setValue(i);
                GL11.glDrawArrays((int)mode, (int)first, (int)count);
            }
            Shaders.uniform_instanceId.setValue(0);
        }
    }

    public static void glLineWidth(float width) {
        GL11.glLineWidth((float)width);
    }

    public static void callList(int list) {
        int countInstances;
        GL11.glCallList((int)list);
        if (Config.isShaders() && !creatingDisplayList && (countInstances = Shaders.activeProgram.getCountInstances()) > 1) {
            for (int i = 1; i < countInstances; ++i) {
                Shaders.uniform_instanceId.setValue(i);
                GL11.glCallList((int)list);
            }
            Shaders.uniform_instanceId.setValue(0);
        }
    }

    public static void callLists(IntBuffer lists) {
        int countInstances;
        GL11.glCallLists((IntBuffer)lists);
        if (Config.isShaders() && !creatingDisplayList && (countInstances = Shaders.activeProgram.getCountInstances()) > 1) {
            for (int i = 1; i < countInstances; ++i) {
                Shaders.uniform_instanceId.setValue(i);
                GL11.glCallLists((IntBuffer)lists);
            }
            Shaders.uniform_instanceId.setValue(0);
        }
    }

    public static void glDeleteLists(int list, int range) {
        GL11.glDeleteLists((int)list, (int)range);
    }

    public static void glNewList(int list, int mode) {
        GL11.glNewList((int)list, (int)mode);
        creatingDisplayList = true;
    }

    public static void glEndList() {
        GL11.glEndList();
        creatingDisplayList = false;
    }

    public static int glGenLists(int range) {
        return GL11.glGenLists((int)range);
    }

    public static void glPixelStorei(int parameterName, int param) {
        GL11.glPixelStorei((int)parameterName, (int)param);
    }

    public static void glReadPixels(int x, int y, int width, int height, int format, int type, IntBuffer pixels) {
        GL11.glReadPixels((int)x, (int)y, (int)width, (int)height, (int)format, (int)type, (IntBuffer)pixels);
    }

    public static int glGetError() {
        return GL11.glGetError();
    }

    public static String glGetString(int name) {
        return GL11.glGetString((int)name);
    }

    public static void glGetInteger(int parameterName, IntBuffer parameters) {
        GL11.glGetInteger((int)parameterName, (IntBuffer)parameters);
    }

    public static int glGetInteger(int parameterName) {
        return GL11.glGetInteger((int)parameterName);
    }

    public static void enableBlendProfile(Profile p_187408_0_) {
        p_187408_0_.apply();
    }

    public static void disableBlendProfile(Profile p_187440_0_) {
        p_187440_0_.clean();
    }

    public static int getActiveTextureUnit() {
        return OpenGlHelper.defaultTexUnit + activeTextureUnit;
    }

    public static void bindCurrentTexture() {
        GL11.glBindTexture((int)3553, (int)GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName);
    }

    public static int getBoundTexture() {
        return GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName;
    }

    public static void checkBoundTexture() {
        if (!Config.isMinecraftThread()) {
            return;
        }
        int glAct = GL11.glGetInteger((int)34016);
        int glTex = GL11.glGetInteger((int)32873);
        int act = GlStateManager.getActiveTextureUnit();
        int tex = GlStateManager.getBoundTexture();
        if (tex <= 0) {
            return;
        }
        if (glAct != act || glTex != tex) {
            Config.dbg((String)("checkTexture: act: " + act + ", glAct: " + glAct + ", tex: " + tex + ", glTex: " + glTex));
        }
    }

    public static void deleteTextures(IntBuffer buf) {
        buf.rewind();
        while (buf.position() < buf.limit()) {
            int texId = buf.get();
            GlStateManager.deleteTexture(texId);
        }
        buf.rewind();
    }

    public static boolean isFogEnabled() {
        return BooleanState.access$100((BooleanState)GlStateManager.fogState.fog);
    }

    public static void setFogEnabled(boolean state) {
        GlStateManager.fogState.fog.setState(state);
    }

    public static void lockAlpha(GlAlphaState stateNew) {
        if (alphaLock.isLocked()) {
            return;
        }
        GlStateManager.getAlphaState(alphaLockState);
        GlStateManager.setAlphaState(stateNew);
        alphaLock.lock();
    }

    public static void unlockAlpha() {
        if (!alphaLock.unlock()) {
            return;
        }
        GlStateManager.setAlphaState(alphaLockState);
    }

    public static void getAlphaState(GlAlphaState state) {
        if (alphaLock.isLocked()) {
            state.setState(alphaLockState);
            return;
        }
        state.setState(BooleanState.access$100((BooleanState)GlStateManager.alphaState.alphaTest), GlStateManager.alphaState.func, GlStateManager.alphaState.ref);
    }

    public static void setAlphaState(GlAlphaState state) {
        if (alphaLock.isLocked()) {
            alphaLockState.setState(state);
            return;
        }
        GlStateManager.alphaState.alphaTest.setState(state.isEnabled());
        GlStateManager.alphaFunc(state.getFunc(), state.getRef());
    }

    public static void lockBlend(GlBlendState stateNew) {
        if (blendLock.isLocked()) {
            return;
        }
        GlStateManager.getBlendState(blendLockState);
        GlStateManager.setBlendState(stateNew);
        blendLock.lock();
    }

    public static void unlockBlend() {
        if (!blendLock.unlock()) {
            return;
        }
        GlStateManager.setBlendState(blendLockState);
    }

    public static void getBlendState(GlBlendState gbs) {
        if (blendLock.isLocked()) {
            gbs.setState(blendLockState);
            return;
        }
        gbs.setState(BooleanState.access$100((BooleanState)GlStateManager.blendState.blend), GlStateManager.blendState.srcFactor, GlStateManager.blendState.dstFactor, GlStateManager.blendState.srcFactorAlpha, GlStateManager.blendState.dstFactorAlpha);
    }

    public static void setBlendState(GlBlendState gbs) {
        if (blendLock.isLocked()) {
            blendLockState.setState(gbs);
            return;
        }
        GlStateManager.blendState.blend.setState(gbs.isEnabled());
        if (!gbs.isSeparate()) {
            GlStateManager.blendFunc(gbs.getSrcFactor(), gbs.getDstFactor());
        } else {
            GlStateManager.tryBlendFuncSeparate(gbs.getSrcFactor(), gbs.getDstFactor(), gbs.getSrcFactorAlpha(), gbs.getDstFactorAlpha());
        }
    }

    public static void glMultiDrawArrays(int mode, IntBuffer bFirst, IntBuffer bCount) {
        int countInstances;
        GL14.glMultiDrawArrays((int)mode, (IntBuffer)bFirst, (IntBuffer)bCount);
        if (Config.isShaders() && !creatingDisplayList && (countInstances = Shaders.activeProgram.getCountInstances()) > 1) {
            for (int i = 1; i < countInstances; ++i) {
                Shaders.uniform_instanceId.setValue(i);
                GL14.glMultiDrawArrays((int)mode, (IntBuffer)bFirst, (IntBuffer)bCount);
            }
            Shaders.uniform_instanceId.setValue(0);
        }
    }

    static {
        clearEnabled = true;
        alphaLock = new LockCounter();
        alphaLockState = new GlAlphaState();
        blendLock = new LockCounter();
        blendLockState = new GlBlendState();
        creatingDisplayList = false;
        for (int i = 0; i < 8; ++i) {
            GlStateManager.lightState[i] = new BooleanState(16384 + i);
        }
        colorMaterialState = new ColorMaterialState(null);
        blendState = new BlendState(null);
        depthState = new DepthState(null);
        fogState = new FogState(null);
        cullState = new CullState(null);
        polygonOffsetState = new PolygonOffsetState(null);
        colorLogicState = new ColorLogicState(null);
        texGenState = new TexGenState(null);
        clearState = new ClearState(null);
        stencilState = new StencilState(null);
        normalizeState = new BooleanState(2977);
        textureState = new TextureState[32];
        for (int j = 0; j < textureState.length; ++j) {
            GlStateManager.textureState[j] = new TextureState(null);
        }
        activeShadeModel = 7425;
        rescaleNormalState = new BooleanState(32826);
        colorMaskState = new ColorMask(null);
        colorState = new Color();
    }
}
