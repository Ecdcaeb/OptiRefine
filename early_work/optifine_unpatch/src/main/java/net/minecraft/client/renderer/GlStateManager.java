/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Quaternion;

public class GlStateManager {
    private static final FloatBuffer BUF_FLOAT_16;
    private static final FloatBuffer BUF_FLOAT_4;
    private static final AlphaState alphaState;
    private static final BooleanState lightingState;
    private static final BooleanState[] lightState;
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

    public static void pushAttrib() {
        GL11.glPushAttrib((int)8256);
    }

    public static void popAttrib() {
        GL11.glPopAttrib();
    }

    public static void disableAlpha() {
        GlStateManager.alphaState.alphaTest.setDisabled();
    }

    public static void enableAlpha() {
        GlStateManager.alphaState.alphaTest.setEnabled();
    }

    public static void alphaFunc(int n, float f) {
        if (n != GlStateManager.alphaState.func || f != GlStateManager.alphaState.ref) {
            GlStateManager.alphaState.func = n;
            GlStateManager.alphaState.ref = f;
            GL11.glAlphaFunc((int)n, (float)f);
        }
    }

    public static void enableLighting() {
        lightingState.setEnabled();
    }

    public static void disableLighting() {
        lightingState.setDisabled();
    }

    public static void enableLight(int n) {
        lightState[n].setEnabled();
    }

    public static void disableLight(int n) {
        lightState[n].setDisabled();
    }

    public static void enableColorMaterial() {
        GlStateManager.colorMaterialState.colorMaterial.setEnabled();
    }

    public static void disableColorMaterial() {
        GlStateManager.colorMaterialState.colorMaterial.setDisabled();
    }

    public static void colorMaterial(int n, int n2) {
        if (n != GlStateManager.colorMaterialState.face || n2 != GlStateManager.colorMaterialState.mode) {
            GlStateManager.colorMaterialState.face = n;
            GlStateManager.colorMaterialState.mode = n2;
            GL11.glColorMaterial((int)n, (int)n2);
        }
    }

    public static void glLight(int n, int n2, FloatBuffer floatBuffer) {
        GL11.glLight((int)n, (int)n2, (FloatBuffer)floatBuffer);
    }

    public static void glLightModel(int n, FloatBuffer floatBuffer) {
        GL11.glLightModel((int)n, (FloatBuffer)floatBuffer);
    }

    public static void glNormal3f(float f, float f2, float f3) {
        GL11.glNormal3f((float)f, (float)f2, (float)f3);
    }

    public static void disableDepth() {
        GlStateManager.depthState.depthTest.setDisabled();
    }

    public static void enableDepth() {
        GlStateManager.depthState.depthTest.setEnabled();
    }

    public static void depthFunc(int n) {
        if (n != GlStateManager.depthState.depthFunc) {
            GlStateManager.depthState.depthFunc = n;
            GL11.glDepthFunc((int)n);
        }
    }

    public static void depthMask(boolean bl) {
        if (bl != GlStateManager.depthState.maskEnabled) {
            GlStateManager.depthState.maskEnabled = bl;
            GL11.glDepthMask((boolean)bl);
        }
    }

    public static void disableBlend() {
        GlStateManager.blendState.blend.setDisabled();
    }

    public static void enableBlend() {
        GlStateManager.blendState.blend.setEnabled();
    }

    public static void blendFunc(SourceFactor sourceFactor, DestFactor destFactor) {
        GlStateManager.blendFunc(sourceFactor.factor, destFactor.factor);
    }

    public static void blendFunc(int n, int n2) {
        if (n != GlStateManager.blendState.srcFactor || n2 != GlStateManager.blendState.dstFactor) {
            GlStateManager.blendState.srcFactor = n;
            GlStateManager.blendState.dstFactor = n2;
            GL11.glBlendFunc((int)n, (int)n2);
        }
    }

    public static void tryBlendFuncSeparate(SourceFactor sourceFactor, DestFactor destFactor, SourceFactor sourceFactor2, DestFactor destFactor2) {
        GlStateManager.tryBlendFuncSeparate(sourceFactor.factor, destFactor.factor, sourceFactor2.factor, destFactor2.factor);
    }

    public static void tryBlendFuncSeparate(int n, int n2, int n3, int n4) {
        if (n != GlStateManager.blendState.srcFactor || n2 != GlStateManager.blendState.dstFactor || n3 != GlStateManager.blendState.srcFactorAlpha || n4 != GlStateManager.blendState.dstFactorAlpha) {
            GlStateManager.blendState.srcFactor = n;
            GlStateManager.blendState.dstFactor = n2;
            GlStateManager.blendState.srcFactorAlpha = n3;
            GlStateManager.blendState.dstFactorAlpha = n4;
            OpenGlHelper.glBlendFunc((int)n, (int)n2, (int)n3, (int)n4);
        }
    }

    public static void glBlendEquation(int n) {
        GL14.glBlendEquation((int)n);
    }

    public static void enableOutlineMode(int n) {
        BUF_FLOAT_4.put(0, (float)(n >> 16 & 0xFF) / 255.0f);
        BUF_FLOAT_4.put(1, (float)(n >> 8 & 0xFF) / 255.0f);
        BUF_FLOAT_4.put(2, (float)(n >> 0 & 0xFF) / 255.0f);
        BUF_FLOAT_4.put(3, (float)(n >> 24 & 0xFF) / 255.0f);
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

    private static void setFog(int n) {
        if (n != GlStateManager.fogState.mode) {
            GlStateManager.fogState.mode = n;
            GL11.glFogi((int)2917, (int)n);
        }
    }

    public static void setFogDensity(float f) {
        if (f != GlStateManager.fogState.density) {
            GlStateManager.fogState.density = f;
            GL11.glFogf((int)2914, (float)f);
        }
    }

    public static void setFogStart(float f) {
        if (f != GlStateManager.fogState.start) {
            GlStateManager.fogState.start = f;
            GL11.glFogf((int)2915, (float)f);
        }
    }

    public static void setFogEnd(float f) {
        if (f != GlStateManager.fogState.end) {
            GlStateManager.fogState.end = f;
            GL11.glFogf((int)2916, (float)f);
        }
    }

    public static void glFog(int n, FloatBuffer floatBuffer) {
        GL11.glFog((int)n, (FloatBuffer)floatBuffer);
    }

    public static void glFogi(int n, int n2) {
        GL11.glFogi((int)n, (int)n2);
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

    private static void cullFace(int n) {
        if (n != GlStateManager.cullState.mode) {
            GlStateManager.cullState.mode = n;
            GL11.glCullFace((int)n);
        }
    }

    public static void glPolygonMode(int n, int n2) {
        GL11.glPolygonMode((int)n, (int)n2);
    }

    public static void enablePolygonOffset() {
        GlStateManager.polygonOffsetState.polygonOffsetFill.setEnabled();
    }

    public static void disablePolygonOffset() {
        GlStateManager.polygonOffsetState.polygonOffsetFill.setDisabled();
    }

    public static void doPolygonOffset(float f, float f2) {
        if (f != GlStateManager.polygonOffsetState.factor || f2 != GlStateManager.polygonOffsetState.units) {
            GlStateManager.polygonOffsetState.factor = f;
            GlStateManager.polygonOffsetState.units = f2;
            GL11.glPolygonOffset((float)f, (float)f2);
        }
    }

    public static void enableColorLogic() {
        GlStateManager.colorLogicState.colorLogicOp.setEnabled();
    }

    public static void disableColorLogic() {
        GlStateManager.colorLogicState.colorLogicOp.setDisabled();
    }

    public static void colorLogicOp(LogicOp logicOp) {
        GlStateManager.colorLogicOp(logicOp.opcode);
    }

    public static void colorLogicOp(int n) {
        if (n != GlStateManager.colorLogicState.opcode) {
            GlStateManager.colorLogicState.opcode = n;
            GL11.glLogicOp((int)n);
        }
    }

    public static void enableTexGenCoord(TexGen texGen) {
        GlStateManager.texGenCoord((TexGen)texGen).textureGen.setEnabled();
    }

    public static void disableTexGenCoord(TexGen texGen) {
        GlStateManager.texGenCoord((TexGen)texGen).textureGen.setDisabled();
    }

    public static void texGen(TexGen texGen, int n) {
        TexGenCoord texGenCoord = GlStateManager.texGenCoord(texGen);
        if (n != texGenCoord.param) {
            texGenCoord.param = n;
            GL11.glTexGeni((int)texGenCoord.coord, (int)9472, (int)n);
        }
    }

    public static void texGen(TexGen texGen, int n, FloatBuffer floatBuffer) {
        GL11.glTexGen((int)GlStateManager.texGenCoord((TexGen)texGen).coord, (int)n, (FloatBuffer)floatBuffer);
    }

    private static TexGenCoord texGenCoord(TexGen texGen) {
        switch (1.field_179175_a[texGen.ordinal()]) {
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

    public static void setActiveTexture(int n) {
        if (activeTextureUnit != n - OpenGlHelper.defaultTexUnit) {
            activeTextureUnit = n - OpenGlHelper.defaultTexUnit;
            OpenGlHelper.setActiveTexture((int)n);
        }
    }

    public static void enableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setEnabled();
    }

    public static void disableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setDisabled();
    }

    public static void glTexEnv(int n, int n2, FloatBuffer floatBuffer) {
        GL11.glTexEnv((int)n, (int)n2, (FloatBuffer)floatBuffer);
    }

    public static void glTexEnvi(int n, int n2, int n3) {
        GL11.glTexEnvi((int)n, (int)n2, (int)n3);
    }

    public static void glTexEnvf(int n, int n2, float f) {
        GL11.glTexEnvf((int)n, (int)n2, (float)f);
    }

    public static void glTexParameterf(int n, int n2, float f) {
        GL11.glTexParameterf((int)n, (int)n2, (float)f);
    }

    public static void glTexParameteri(int n, int n2, int n3) {
        GL11.glTexParameteri((int)n, (int)n2, (int)n3);
    }

    public static int glGetTexLevelParameteri(int n, int n2, int n3) {
        return GL11.glGetTexLevelParameteri((int)n, (int)n2, (int)n3);
    }

    public static int generateTexture() {
        return GL11.glGenTextures();
    }

    public static void deleteTexture(int n) {
        GL11.glDeleteTextures((int)n);
        for (TextureState textureState : GlStateManager.textureState) {
            if (textureState.textureName != n) continue;
            textureState.textureName = -1;
        }
    }

    public static void bindTexture(int n) {
        if (n != GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName) {
            GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = n;
            GL11.glBindTexture((int)3553, (int)n);
        }
    }

    public static void glTexImage2D(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, @Nullable IntBuffer intBuffer) {
        GL11.glTexImage2D((int)n, (int)n2, (int)n3, (int)n4, (int)n5, (int)n6, (int)n7, (int)n8, (IntBuffer)intBuffer);
    }

    public static void glTexSubImage2D(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, IntBuffer intBuffer) {
        GL11.glTexSubImage2D((int)n, (int)n2, (int)n3, (int)n4, (int)n5, (int)n6, (int)n7, (int)n8, (IntBuffer)intBuffer);
    }

    public static void glCopyTexSubImage2D(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        GL11.glCopyTexSubImage2D((int)n, (int)n2, (int)n3, (int)n4, (int)n5, (int)n6, (int)n7, (int)n8);
    }

    public static void glGetTexImage(int n, int n2, int n3, int n4, IntBuffer intBuffer) {
        GL11.glGetTexImage((int)n, (int)n2, (int)n3, (int)n4, (IntBuffer)intBuffer);
    }

    public static void enableNormalize() {
        normalizeState.setEnabled();
    }

    public static void disableNormalize() {
        normalizeState.setDisabled();
    }

    public static void shadeModel(int n) {
        if (n != activeShadeModel) {
            activeShadeModel = n;
            GL11.glShadeModel((int)n);
        }
    }

    public static void enableRescaleNormal() {
        rescaleNormalState.setEnabled();
    }

    public static void disableRescaleNormal() {
        rescaleNormalState.setDisabled();
    }

    public static void viewport(int n, int n2, int n3, int n4) {
        GL11.glViewport((int)n, (int)n2, (int)n3, (int)n4);
    }

    public static void colorMask(boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        if (bl != GlStateManager.colorMaskState.red || bl2 != GlStateManager.colorMaskState.green || bl3 != GlStateManager.colorMaskState.blue || bl4 != GlStateManager.colorMaskState.alpha) {
            GlStateManager.colorMaskState.red = bl;
            GlStateManager.colorMaskState.green = bl2;
            GlStateManager.colorMaskState.blue = bl3;
            GlStateManager.colorMaskState.alpha = bl4;
            GL11.glColorMask((boolean)bl, (boolean)bl2, (boolean)bl3, (boolean)bl4);
        }
    }

    public static void clearDepth(double d) {
        if (d != GlStateManager.clearState.depth) {
            GlStateManager.clearState.depth = d;
            GL11.glClearDepth((double)d);
        }
    }

    public static void clearColor(float f, float f2, float f3, float f4) {
        if (f != GlStateManager.clearState.color.red || f2 != GlStateManager.clearState.color.green || f3 != GlStateManager.clearState.color.blue || f4 != GlStateManager.clearState.color.alpha) {
            GlStateManager.clearState.color.red = f;
            GlStateManager.clearState.color.green = f2;
            GlStateManager.clearState.color.blue = f3;
            GlStateManager.clearState.color.alpha = f4;
            GL11.glClearColor((float)f, (float)f2, (float)f3, (float)f4);
        }
    }

    public static void clear(int n) {
        GL11.glClear((int)n);
    }

    public static void matrixMode(int n) {
        GL11.glMatrixMode((int)n);
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

    public static void getFloat(int n, FloatBuffer floatBuffer) {
        GL11.glGetFloat((int)n, (FloatBuffer)floatBuffer);
    }

    public static void ortho(double d, double d2, double d3, double d4, double d5, double d6) {
        GL11.glOrtho((double)d, (double)d2, (double)d3, (double)d4, (double)d5, (double)d6);
    }

    public static void rotate(float f, float f2, float f3, float f4) {
        GL11.glRotatef((float)f, (float)f2, (float)f3, (float)f4);
    }

    public static void scale(float f, float f2, float f3) {
        GL11.glScalef((float)f, (float)f2, (float)f3);
    }

    public static void scale(double d, double d2, double d3) {
        GL11.glScaled((double)d, (double)d2, (double)d3);
    }

    public static void translate(float f, float f2, float f3) {
        GL11.glTranslatef((float)f, (float)f2, (float)f3);
    }

    public static void translate(double d, double d2, double d3) {
        GL11.glTranslated((double)d, (double)d2, (double)d3);
    }

    public static void multMatrix(FloatBuffer floatBuffer) {
        GL11.glMultMatrix((FloatBuffer)floatBuffer);
    }

    public static void rotate(Quaternion quaternion) {
        GlStateManager.multMatrix(GlStateManager.quatToGlMatrix(BUF_FLOAT_16, quaternion));
    }

    public static FloatBuffer quatToGlMatrix(FloatBuffer floatBuffer, Quaternion quaternion) {
        floatBuffer.clear();
        float f = quaternion.x * quaternion.x;
        \u2603 = quaternion.x * quaternion.y;
        \u2603 = quaternion.x * quaternion.z;
        \u2603 = quaternion.x * quaternion.w;
        \u2603 = quaternion.y * quaternion.y;
        \u2603 = quaternion.y * quaternion.z;
        \u2603 = quaternion.y * quaternion.w;
        \u2603 = quaternion.z * quaternion.z;
        \u2603 = quaternion.z * quaternion.w;
        floatBuffer.put(1.0f - 2.0f * (\u2603 + \u2603));
        floatBuffer.put(2.0f * (\u2603 + \u2603));
        floatBuffer.put(2.0f * (\u2603 - \u2603));
        floatBuffer.put(0.0f);
        floatBuffer.put(2.0f * (\u2603 - \u2603));
        floatBuffer.put(1.0f - 2.0f * (f + \u2603));
        floatBuffer.put(2.0f * (\u2603 + \u2603));
        floatBuffer.put(0.0f);
        floatBuffer.put(2.0f * (\u2603 + \u2603));
        floatBuffer.put(2.0f * (\u2603 - \u2603));
        floatBuffer.put(1.0f - 2.0f * (f + \u2603));
        floatBuffer.put(0.0f);
        floatBuffer.put(0.0f);
        floatBuffer.put(0.0f);
        floatBuffer.put(0.0f);
        floatBuffer.put(1.0f);
        floatBuffer.rewind();
        return floatBuffer;
    }

    public static void color(float f, float f2, float f3, float f4) {
        if (f != GlStateManager.colorState.red || f2 != GlStateManager.colorState.green || f3 != GlStateManager.colorState.blue || f4 != GlStateManager.colorState.alpha) {
            GlStateManager.colorState.red = f;
            GlStateManager.colorState.green = f2;
            GlStateManager.colorState.blue = f3;
            GlStateManager.colorState.alpha = f4;
            GL11.glColor4f((float)f, (float)f2, (float)f3, (float)f4);
        }
    }

    public static void color(float f, float f2, float f3) {
        GlStateManager.color(f, f2, f3, 1.0f);
    }

    public static void glTexCoord2f(float f, float f2) {
        GL11.glTexCoord2f((float)f, (float)f2);
    }

    public static void glVertex3f(float f, float f2, float f3) {
        GL11.glVertex3f((float)f, (float)f2, (float)f3);
    }

    public static void resetColor() {
        GlStateManager.colorState.red = -1.0f;
        GlStateManager.colorState.green = -1.0f;
        GlStateManager.colorState.blue = -1.0f;
        GlStateManager.colorState.alpha = -1.0f;
    }

    public static void glNormalPointer(int n, int n2, ByteBuffer byteBuffer) {
        GL11.glNormalPointer((int)n, (int)n2, (ByteBuffer)byteBuffer);
    }

    public static void glTexCoordPointer(int n, int n2, int n3, int n4) {
        GL11.glTexCoordPointer((int)n, (int)n2, (int)n3, (long)n4);
    }

    public static void glTexCoordPointer(int n, int n2, int n3, ByteBuffer byteBuffer) {
        GL11.glTexCoordPointer((int)n, (int)n2, (int)n3, (ByteBuffer)byteBuffer);
    }

    public static void glVertexPointer(int n, int n2, int n3, int n4) {
        GL11.glVertexPointer((int)n, (int)n2, (int)n3, (long)n4);
    }

    public static void glVertexPointer(int n, int n2, int n3, ByteBuffer byteBuffer) {
        GL11.glVertexPointer((int)n, (int)n2, (int)n3, (ByteBuffer)byteBuffer);
    }

    public static void glColorPointer(int n, int n2, int n3, int n4) {
        GL11.glColorPointer((int)n, (int)n2, (int)n3, (long)n4);
    }

    public static void glColorPointer(int n, int n2, int n3, ByteBuffer byteBuffer) {
        GL11.glColorPointer((int)n, (int)n2, (int)n3, (ByteBuffer)byteBuffer);
    }

    public static void glDisableClientState(int n) {
        GL11.glDisableClientState((int)n);
    }

    public static void glEnableClientState(int n) {
        GL11.glEnableClientState((int)n);
    }

    public static void glBegin(int n) {
        GL11.glBegin((int)n);
    }

    public static void glEnd() {
        GL11.glEnd();
    }

    public static void glDrawArrays(int n, int n2, int n3) {
        GL11.glDrawArrays((int)n, (int)n2, (int)n3);
    }

    public static void glLineWidth(float f) {
        GL11.glLineWidth((float)f);
    }

    public static void callList(int n) {
        GL11.glCallList((int)n);
    }

    public static void glDeleteLists(int n, int n2) {
        GL11.glDeleteLists((int)n, (int)n2);
    }

    public static void glNewList(int n, int n2) {
        GL11.glNewList((int)n, (int)n2);
    }

    public static void glEndList() {
        GL11.glEndList();
    }

    public static int glGenLists(int n) {
        return GL11.glGenLists((int)n);
    }

    public static void glPixelStorei(int n, int n2) {
        GL11.glPixelStorei((int)n, (int)n2);
    }

    public static void glReadPixels(int n, int n2, int n3, int n4, int n5, int n6, IntBuffer intBuffer) {
        GL11.glReadPixels((int)n, (int)n2, (int)n3, (int)n4, (int)n5, (int)n6, (IntBuffer)intBuffer);
    }

    public static int glGetError() {
        return GL11.glGetError();
    }

    public static String glGetString(int n) {
        return GL11.glGetString((int)n);
    }

    public static void glGetInteger(int n, IntBuffer intBuffer) {
        GL11.glGetInteger((int)n, (IntBuffer)intBuffer);
    }

    public static int glGetInteger(int n) {
        return GL11.glGetInteger((int)n);
    }

    public static void enableBlendProfile(Profile profile) {
        profile.apply();
    }

    public static void disableBlendProfile(Profile profile) {
        profile.clean();
    }

    static {
        int n;
        BUF_FLOAT_16 = BufferUtils.createFloatBuffer((int)16);
        BUF_FLOAT_4 = BufferUtils.createFloatBuffer((int)4);
        alphaState = new AlphaState(null);
        lightingState = new BooleanState(2896);
        lightState = new BooleanState[8];
        for (n = 0; n < 8; ++n) {
            GlStateManager.lightState[n] = new BooleanState(16384 + n);
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
        textureState = new TextureState[8];
        for (n = 0; n < 8; ++n) {
            GlStateManager.textureState[n] = new TextureState(null);
        }
        activeShadeModel = 7425;
        rescaleNormalState = new BooleanState(32826);
        colorMaskState = new ColorMask(null);
        colorState = new Color();
    }
}
