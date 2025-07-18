package net.minecraft.client.renderer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.annotation.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Quaternion;

public class GlStateManager {
   private static final FloatBuffer BUF_FLOAT_16 = BufferUtils.createFloatBuffer(16);
   private static final FloatBuffer BUF_FLOAT_4 = BufferUtils.createFloatBuffer(4);
   private static final GlStateManager.AlphaState alphaState = new GlStateManager.AlphaState();
   private static final GlStateManager.BooleanState lightingState = new GlStateManager.BooleanState(2896);
   private static final GlStateManager.BooleanState[] lightState = new GlStateManager.BooleanState[8];
   private static final GlStateManager.ColorMaterialState colorMaterialState;
   private static final GlStateManager.BlendState blendState;
   private static final GlStateManager.DepthState depthState;
   private static final GlStateManager.FogState fogState;
   private static final GlStateManager.CullState cullState;
   private static final GlStateManager.PolygonOffsetState polygonOffsetState;
   private static final GlStateManager.ColorLogicState colorLogicState;
   private static final GlStateManager.TexGenState texGenState;
   private static final GlStateManager.ClearState clearState;
   private static final GlStateManager.StencilState stencilState;
   private static final GlStateManager.BooleanState normalizeState;
   private static int activeTextureUnit;
   private static final GlStateManager.TextureState[] textureState;
   private static int activeShadeModel;
   private static final GlStateManager.BooleanState rescaleNormalState;
   private static final GlStateManager.ColorMask colorMaskState;
   private static final GlStateManager.Color colorState;

   public static void pushAttrib() {
      GL11.glPushAttrib(8256);
   }

   public static void popAttrib() {
      GL11.glPopAttrib();
   }

   public static void disableAlpha() {
      alphaState.alphaTest.setDisabled();
   }

   public static void enableAlpha() {
      alphaState.alphaTest.setEnabled();
   }

   public static void alphaFunc(int var0, float var1) {
      if (☃ != alphaState.func || ☃ != alphaState.ref) {
         alphaState.func = ☃;
         alphaState.ref = ☃;
         GL11.glAlphaFunc(☃, ☃);
      }
   }

   public static void enableLighting() {
      lightingState.setEnabled();
   }

   public static void disableLighting() {
      lightingState.setDisabled();
   }

   public static void enableLight(int var0) {
      lightState[☃].setEnabled();
   }

   public static void disableLight(int var0) {
      lightState[☃].setDisabled();
   }

   public static void enableColorMaterial() {
      colorMaterialState.colorMaterial.setEnabled();
   }

   public static void disableColorMaterial() {
      colorMaterialState.colorMaterial.setDisabled();
   }

   public static void colorMaterial(int var0, int var1) {
      if (☃ != colorMaterialState.face || ☃ != colorMaterialState.mode) {
         colorMaterialState.face = ☃;
         colorMaterialState.mode = ☃;
         GL11.glColorMaterial(☃, ☃);
      }
   }

   public static void glLight(int var0, int var1, FloatBuffer var2) {
      GL11.glLight(☃, ☃, ☃);
   }

   public static void glLightModel(int var0, FloatBuffer var1) {
      GL11.glLightModel(☃, ☃);
   }

   public static void glNormal3f(float var0, float var1, float var2) {
      GL11.glNormal3f(☃, ☃, ☃);
   }

   public static void disableDepth() {
      depthState.depthTest.setDisabled();
   }

   public static void enableDepth() {
      depthState.depthTest.setEnabled();
   }

   public static void depthFunc(int var0) {
      if (☃ != depthState.depthFunc) {
         depthState.depthFunc = ☃;
         GL11.glDepthFunc(☃);
      }
   }

   public static void depthMask(boolean var0) {
      if (☃ != depthState.maskEnabled) {
         depthState.maskEnabled = ☃;
         GL11.glDepthMask(☃);
      }
   }

   public static void disableBlend() {
      blendState.blend.setDisabled();
   }

   public static void enableBlend() {
      blendState.blend.setEnabled();
   }

   public static void blendFunc(GlStateManager.SourceFactor var0, GlStateManager.DestFactor var1) {
      blendFunc(☃.factor, ☃.factor);
   }

   public static void blendFunc(int var0, int var1) {
      if (☃ != blendState.srcFactor || ☃ != blendState.dstFactor) {
         blendState.srcFactor = ☃;
         blendState.dstFactor = ☃;
         GL11.glBlendFunc(☃, ☃);
      }
   }

   public static void tryBlendFuncSeparate(
      GlStateManager.SourceFactor var0, GlStateManager.DestFactor var1, GlStateManager.SourceFactor var2, GlStateManager.DestFactor var3
   ) {
      tryBlendFuncSeparate(☃.factor, ☃.factor, ☃.factor, ☃.factor);
   }

   public static void tryBlendFuncSeparate(int var0, int var1, int var2, int var3) {
      if (☃ != blendState.srcFactor || ☃ != blendState.dstFactor || ☃ != blendState.srcFactorAlpha || ☃ != blendState.dstFactorAlpha) {
         blendState.srcFactor = ☃;
         blendState.dstFactor = ☃;
         blendState.srcFactorAlpha = ☃;
         blendState.dstFactorAlpha = ☃;
         OpenGlHelper.glBlendFunc(☃, ☃, ☃, ☃);
      }
   }

   public static void glBlendEquation(int var0) {
      GL14.glBlendEquation(☃);
   }

   public static void enableOutlineMode(int var0) {
      BUF_FLOAT_4.put(0, (☃ >> 16 & 0xFF) / 255.0F);
      BUF_FLOAT_4.put(1, (☃ >> 8 & 0xFF) / 255.0F);
      BUF_FLOAT_4.put(2, (☃ >> 0 & 0xFF) / 255.0F);
      BUF_FLOAT_4.put(3, (☃ >> 24 & 0xFF) / 255.0F);
      glTexEnv(8960, 8705, BUF_FLOAT_4);
      glTexEnvi(8960, 8704, 34160);
      glTexEnvi(8960, 34161, 7681);
      glTexEnvi(8960, 34176, 34166);
      glTexEnvi(8960, 34192, 768);
      glTexEnvi(8960, 34162, 7681);
      glTexEnvi(8960, 34184, 5890);
      glTexEnvi(8960, 34200, 770);
   }

   public static void disableOutlineMode() {
      glTexEnvi(8960, 8704, 8448);
      glTexEnvi(8960, 34161, 8448);
      glTexEnvi(8960, 34162, 8448);
      glTexEnvi(8960, 34176, 5890);
      glTexEnvi(8960, 34184, 5890);
      glTexEnvi(8960, 34192, 768);
      glTexEnvi(8960, 34200, 770);
   }

   public static void enableFog() {
      fogState.fog.setEnabled();
   }

   public static void disableFog() {
      fogState.fog.setDisabled();
   }

   public static void setFog(GlStateManager.FogMode var0) {
      setFog(☃.capabilityId);
   }

   private static void setFog(int var0) {
      if (☃ != fogState.mode) {
         fogState.mode = ☃;
         GL11.glFogi(2917, ☃);
      }
   }

   public static void setFogDensity(float var0) {
      if (☃ != fogState.density) {
         fogState.density = ☃;
         GL11.glFogf(2914, ☃);
      }
   }

   public static void setFogStart(float var0) {
      if (☃ != fogState.start) {
         fogState.start = ☃;
         GL11.glFogf(2915, ☃);
      }
   }

   public static void setFogEnd(float var0) {
      if (☃ != fogState.end) {
         fogState.end = ☃;
         GL11.glFogf(2916, ☃);
      }
   }

   public static void glFog(int var0, FloatBuffer var1) {
      GL11.glFog(☃, ☃);
   }

   public static void glFogi(int var0, int var1) {
      GL11.glFogi(☃, ☃);
   }

   public static void enableCull() {
      cullState.cullFace.setEnabled();
   }

   public static void disableCull() {
      cullState.cullFace.setDisabled();
   }

   public static void cullFace(GlStateManager.CullFace var0) {
      cullFace(☃.mode);
   }

   private static void cullFace(int var0) {
      if (☃ != cullState.mode) {
         cullState.mode = ☃;
         GL11.glCullFace(☃);
      }
   }

   public static void glPolygonMode(int var0, int var1) {
      GL11.glPolygonMode(☃, ☃);
   }

   public static void enablePolygonOffset() {
      polygonOffsetState.polygonOffsetFill.setEnabled();
   }

   public static void disablePolygonOffset() {
      polygonOffsetState.polygonOffsetFill.setDisabled();
   }

   public static void doPolygonOffset(float var0, float var1) {
      if (☃ != polygonOffsetState.factor || ☃ != polygonOffsetState.units) {
         polygonOffsetState.factor = ☃;
         polygonOffsetState.units = ☃;
         GL11.glPolygonOffset(☃, ☃);
      }
   }

   public static void enableColorLogic() {
      colorLogicState.colorLogicOp.setEnabled();
   }

   public static void disableColorLogic() {
      colorLogicState.colorLogicOp.setDisabled();
   }

   public static void colorLogicOp(GlStateManager.LogicOp var0) {
      colorLogicOp(☃.opcode);
   }

   public static void colorLogicOp(int var0) {
      if (☃ != colorLogicState.opcode) {
         colorLogicState.opcode = ☃;
         GL11.glLogicOp(☃);
      }
   }

   public static void enableTexGenCoord(GlStateManager.TexGen var0) {
      texGenCoord(☃).textureGen.setEnabled();
   }

   public static void disableTexGenCoord(GlStateManager.TexGen var0) {
      texGenCoord(☃).textureGen.setDisabled();
   }

   public static void texGen(GlStateManager.TexGen var0, int var1) {
      GlStateManager.TexGenCoord ☃ = texGenCoord(☃);
      if (☃ != ☃.param) {
         ☃.param = ☃;
         GL11.glTexGeni(☃.coord, 9472, ☃);
      }
   }

   public static void texGen(GlStateManager.TexGen var0, int var1, FloatBuffer var2) {
      GL11.glTexGen(texGenCoord(☃).coord, ☃, ☃);
   }

   private static GlStateManager.TexGenCoord texGenCoord(GlStateManager.TexGen var0) {
      switch (☃) {
         case S:
            return texGenState.s;
         case T:
            return texGenState.t;
         case R:
            return texGenState.r;
         case Q:
            return texGenState.q;
         default:
            return texGenState.s;
      }
   }

   public static void setActiveTexture(int var0) {
      if (activeTextureUnit != ☃ - OpenGlHelper.defaultTexUnit) {
         activeTextureUnit = ☃ - OpenGlHelper.defaultTexUnit;
         OpenGlHelper.setActiveTexture(☃);
      }
   }

   public static void enableTexture2D() {
      textureState[activeTextureUnit].texture2DState.setEnabled();
   }

   public static void disableTexture2D() {
      textureState[activeTextureUnit].texture2DState.setDisabled();
   }

   public static void glTexEnv(int var0, int var1, FloatBuffer var2) {
      GL11.glTexEnv(☃, ☃, ☃);
   }

   public static void glTexEnvi(int var0, int var1, int var2) {
      GL11.glTexEnvi(☃, ☃, ☃);
   }

   public static void glTexEnvf(int var0, int var1, float var2) {
      GL11.glTexEnvf(☃, ☃, ☃);
   }

   public static void glTexParameterf(int var0, int var1, float var2) {
      GL11.glTexParameterf(☃, ☃, ☃);
   }

   public static void glTexParameteri(int var0, int var1, int var2) {
      GL11.glTexParameteri(☃, ☃, ☃);
   }

   public static int glGetTexLevelParameteri(int var0, int var1, int var2) {
      return GL11.glGetTexLevelParameteri(☃, ☃, ☃);
   }

   public static int generateTexture() {
      return GL11.glGenTextures();
   }

   public static void deleteTexture(int var0) {
      GL11.glDeleteTextures(☃);

      for (GlStateManager.TextureState ☃ : textureState) {
         if (☃.textureName == ☃) {
            ☃.textureName = -1;
         }
      }
   }

   public static void bindTexture(int var0) {
      if (☃ != textureState[activeTextureUnit].textureName) {
         textureState[activeTextureUnit].textureName = ☃;
         GL11.glBindTexture(3553, ☃);
      }
   }

   public static void glTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, @Nullable IntBuffer var8) {
      GL11.glTexImage2D(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   public static void glTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, IntBuffer var8) {
      GL11.glTexSubImage2D(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   public static void glCopyTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      GL11.glCopyTexSubImage2D(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   public static void glGetTexImage(int var0, int var1, int var2, int var3, IntBuffer var4) {
      GL11.glGetTexImage(☃, ☃, ☃, ☃, ☃);
   }

   public static void enableNormalize() {
      normalizeState.setEnabled();
   }

   public static void disableNormalize() {
      normalizeState.setDisabled();
   }

   public static void shadeModel(int var0) {
      if (☃ != activeShadeModel) {
         activeShadeModel = ☃;
         GL11.glShadeModel(☃);
      }
   }

   public static void enableRescaleNormal() {
      rescaleNormalState.setEnabled();
   }

   public static void disableRescaleNormal() {
      rescaleNormalState.setDisabled();
   }

   public static void viewport(int var0, int var1, int var2, int var3) {
      GL11.glViewport(☃, ☃, ☃, ☃);
   }

   public static void colorMask(boolean var0, boolean var1, boolean var2, boolean var3) {
      if (☃ != colorMaskState.red || ☃ != colorMaskState.green || ☃ != colorMaskState.blue || ☃ != colorMaskState.alpha) {
         colorMaskState.red = ☃;
         colorMaskState.green = ☃;
         colorMaskState.blue = ☃;
         colorMaskState.alpha = ☃;
         GL11.glColorMask(☃, ☃, ☃, ☃);
      }
   }

   public static void clearDepth(double var0) {
      if (☃ != clearState.depth) {
         clearState.depth = ☃;
         GL11.glClearDepth(☃);
      }
   }

   public static void clearColor(float var0, float var1, float var2, float var3) {
      if (☃ != clearState.color.red || ☃ != clearState.color.green || ☃ != clearState.color.blue || ☃ != clearState.color.alpha) {
         clearState.color.red = ☃;
         clearState.color.green = ☃;
         clearState.color.blue = ☃;
         clearState.color.alpha = ☃;
         GL11.glClearColor(☃, ☃, ☃, ☃);
      }
   }

   public static void clear(int var0) {
      GL11.glClear(☃);
   }

   public static void matrixMode(int var0) {
      GL11.glMatrixMode(☃);
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

   public static void getFloat(int var0, FloatBuffer var1) {
      GL11.glGetFloat(☃, ☃);
   }

   public static void ortho(double var0, double var2, double var4, double var6, double var8, double var10) {
      GL11.glOrtho(☃, ☃, ☃, ☃, ☃, ☃);
   }

   public static void rotate(float var0, float var1, float var2, float var3) {
      GL11.glRotatef(☃, ☃, ☃, ☃);
   }

   public static void scale(float var0, float var1, float var2) {
      GL11.glScalef(☃, ☃, ☃);
   }

   public static void scale(double var0, double var2, double var4) {
      GL11.glScaled(☃, ☃, ☃);
   }

   public static void translate(float var0, float var1, float var2) {
      GL11.glTranslatef(☃, ☃, ☃);
   }

   public static void translate(double var0, double var2, double var4) {
      GL11.glTranslated(☃, ☃, ☃);
   }

   public static void multMatrix(FloatBuffer var0) {
      GL11.glMultMatrix(☃);
   }

   public static void rotate(Quaternion var0) {
      multMatrix(quatToGlMatrix(BUF_FLOAT_16, ☃));
   }

   public static FloatBuffer quatToGlMatrix(FloatBuffer var0, Quaternion var1) {
      ((Buffer)☃).clear();
      float ☃ = ☃.x * ☃.x;
      float ☃x = ☃.x * ☃.y;
      float ☃xx = ☃.x * ☃.z;
      float ☃xxx = ☃.x * ☃.w;
      float ☃xxxx = ☃.y * ☃.y;
      float ☃xxxxx = ☃.y * ☃.z;
      float ☃xxxxxx = ☃.y * ☃.w;
      float ☃xxxxxxx = ☃.z * ☃.z;
      float ☃xxxxxxxx = ☃.z * ☃.w;
      ☃.put(1.0F - 2.0F * (☃xxxx + ☃xxxxxxx));
      ☃.put(2.0F * (☃x + ☃xxxxxxxx));
      ☃.put(2.0F * (☃xx - ☃xxxxxx));
      ☃.put(0.0F);
      ☃.put(2.0F * (☃x - ☃xxxxxxxx));
      ☃.put(1.0F - 2.0F * (☃ + ☃xxxxxxx));
      ☃.put(2.0F * (☃xxxxx + ☃xxx));
      ☃.put(0.0F);
      ☃.put(2.0F * (☃xx + ☃xxxxxx));
      ☃.put(2.0F * (☃xxxxx - ☃xxx));
      ☃.put(1.0F - 2.0F * (☃ + ☃xxxx));
      ☃.put(0.0F);
      ☃.put(0.0F);
      ☃.put(0.0F);
      ☃.put(0.0F);
      ☃.put(1.0F);
      ((Buffer)☃).rewind();
      return ☃;
   }

   public static void color(float var0, float var1, float var2, float var3) {
      if (☃ != colorState.red || ☃ != colorState.green || ☃ != colorState.blue || ☃ != colorState.alpha) {
         colorState.red = ☃;
         colorState.green = ☃;
         colorState.blue = ☃;
         colorState.alpha = ☃;
         GL11.glColor4f(☃, ☃, ☃, ☃);
      }
   }

   public static void color(float var0, float var1, float var2) {
      color(☃, ☃, ☃, 1.0F);
   }

   public static void glTexCoord2f(float var0, float var1) {
      GL11.glTexCoord2f(☃, ☃);
   }

   public static void glVertex3f(float var0, float var1, float var2) {
      GL11.glVertex3f(☃, ☃, ☃);
   }

   public static void resetColor() {
      colorState.red = -1.0F;
      colorState.green = -1.0F;
      colorState.blue = -1.0F;
      colorState.alpha = -1.0F;
   }

   public static void glNormalPointer(int var0, int var1, ByteBuffer var2) {
      GL11.glNormalPointer(☃, ☃, ☃);
   }

   public static void glTexCoordPointer(int var0, int var1, int var2, int var3) {
      GL11.glTexCoordPointer(☃, ☃, ☃, ☃);
   }

   public static void glTexCoordPointer(int var0, int var1, int var2, ByteBuffer var3) {
      GL11.glTexCoordPointer(☃, ☃, ☃, ☃);
   }

   public static void glVertexPointer(int var0, int var1, int var2, int var3) {
      GL11.glVertexPointer(☃, ☃, ☃, ☃);
   }

   public static void glVertexPointer(int var0, int var1, int var2, ByteBuffer var3) {
      GL11.glVertexPointer(☃, ☃, ☃, ☃);
   }

   public static void glColorPointer(int var0, int var1, int var2, int var3) {
      GL11.glColorPointer(☃, ☃, ☃, ☃);
   }

   public static void glColorPointer(int var0, int var1, int var2, ByteBuffer var3) {
      GL11.glColorPointer(☃, ☃, ☃, ☃);
   }

   public static void glDisableClientState(int var0) {
      GL11.glDisableClientState(☃);
   }

   public static void glEnableClientState(int var0) {
      GL11.glEnableClientState(☃);
   }

   public static void glBegin(int var0) {
      GL11.glBegin(☃);
   }

   public static void glEnd() {
      GL11.glEnd();
   }

   public static void glDrawArrays(int var0, int var1, int var2) {
      GL11.glDrawArrays(☃, ☃, ☃);
   }

   public static void glLineWidth(float var0) {
      GL11.glLineWidth(☃);
   }

   public static void callList(int var0) {
      GL11.glCallList(☃);
   }

   public static void glDeleteLists(int var0, int var1) {
      GL11.glDeleteLists(☃, ☃);
   }

   public static void glNewList(int var0, int var1) {
      GL11.glNewList(☃, ☃);
   }

   public static void glEndList() {
      GL11.glEndList();
   }

   public static int glGenLists(int var0) {
      return GL11.glGenLists(☃);
   }

   public static void glPixelStorei(int var0, int var1) {
      GL11.glPixelStorei(☃, ☃);
   }

   public static void glReadPixels(int var0, int var1, int var2, int var3, int var4, int var5, IntBuffer var6) {
      GL11.glReadPixels(☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   public static int glGetError() {
      return GL11.glGetError();
   }

   public static String glGetString(int var0) {
      return GL11.glGetString(☃);
   }

   public static void glGetInteger(int var0, IntBuffer var1) {
      GL11.glGetInteger(☃, ☃);
   }

   public static int glGetInteger(int var0) {
      return GL11.glGetInteger(☃);
   }

   public static void enableBlendProfile(GlStateManager.Profile var0) {
      ☃.apply();
   }

   public static void disableBlendProfile(GlStateManager.Profile var0) {
      ☃.clean();
   }

   static {
      for (int ☃ = 0; ☃ < 8; ☃++) {
         lightState[☃] = new GlStateManager.BooleanState(16384 + ☃);
      }

      colorMaterialState = new GlStateManager.ColorMaterialState();
      blendState = new GlStateManager.BlendState();
      depthState = new GlStateManager.DepthState();
      fogState = new GlStateManager.FogState();
      cullState = new GlStateManager.CullState();
      polygonOffsetState = new GlStateManager.PolygonOffsetState();
      colorLogicState = new GlStateManager.ColorLogicState();
      texGenState = new GlStateManager.TexGenState();
      clearState = new GlStateManager.ClearState();
      stencilState = new GlStateManager.StencilState();
      normalizeState = new GlStateManager.BooleanState(2977);
      textureState = new GlStateManager.TextureState[8];

      for (int ☃ = 0; ☃ < 8; ☃++) {
         textureState[☃] = new GlStateManager.TextureState();
      }

      activeShadeModel = 7425;
      rescaleNormalState = new GlStateManager.BooleanState(32826);
      colorMaskState = new GlStateManager.ColorMask();
      colorState = new GlStateManager.Color();
   }

   static class AlphaState {
      public GlStateManager.BooleanState alphaTest = new GlStateManager.BooleanState(3008);
      public int func = 519;
      public float ref = -1.0F;

      private AlphaState() {
      }
   }

   static class BlendState {
      public GlStateManager.BooleanState blend = new GlStateManager.BooleanState(3042);
      public int srcFactor = 1;
      public int dstFactor = 0;
      public int srcFactorAlpha = 1;
      public int dstFactorAlpha = 0;

      private BlendState() {
      }
   }

   static class BooleanState {
      private final int capability;
      private boolean currentState;

      public BooleanState(int var1) {
         this.capability = ☃;
      }

      public void setDisabled() {
         this.setState(false);
      }

      public void setEnabled() {
         this.setState(true);
      }

      public void setState(boolean var1) {
         if (☃ != this.currentState) {
            this.currentState = ☃;
            if (☃) {
               GL11.glEnable(this.capability);
            } else {
               GL11.glDisable(this.capability);
            }
         }
      }
   }

   static class ClearState {
      public double depth = 1.0;
      public GlStateManager.Color color = new GlStateManager.Color(0.0F, 0.0F, 0.0F, 0.0F);

      private ClearState() {
      }
   }

   static class Color {
      public float red = 1.0F;
      public float green = 1.0F;
      public float blue = 1.0F;
      public float alpha = 1.0F;

      public Color() {
         this(1.0F, 1.0F, 1.0F, 1.0F);
      }

      public Color(float var1, float var2, float var3, float var4) {
         this.red = ☃;
         this.green = ☃;
         this.blue = ☃;
         this.alpha = ☃;
      }
   }

   static class ColorLogicState {
      public GlStateManager.BooleanState colorLogicOp = new GlStateManager.BooleanState(3058);
      public int opcode = 5379;

      private ColorLogicState() {
      }
   }

   static class ColorMask {
      public boolean red = true;
      public boolean green = true;
      public boolean blue = true;
      public boolean alpha = true;

      private ColorMask() {
      }
   }

   static class ColorMaterialState {
      public GlStateManager.BooleanState colorMaterial = new GlStateManager.BooleanState(2903);
      public int face = 1032;
      public int mode = 5634;

      private ColorMaterialState() {
      }
   }

   public static enum CullFace {
      FRONT(1028),
      BACK(1029),
      FRONT_AND_BACK(1032);

      public final int mode;

      private CullFace(int var3) {
         this.mode = ☃;
      }
   }

   static class CullState {
      public GlStateManager.BooleanState cullFace = new GlStateManager.BooleanState(2884);
      public int mode = 1029;

      private CullState() {
      }
   }

   static class DepthState {
      public GlStateManager.BooleanState depthTest = new GlStateManager.BooleanState(2929);
      public boolean maskEnabled = true;
      public int depthFunc = 513;

      private DepthState() {
      }
   }

   public static enum DestFactor {
      CONSTANT_ALPHA(32771),
      CONSTANT_COLOR(32769),
      DST_ALPHA(772),
      DST_COLOR(774),
      ONE(1),
      ONE_MINUS_CONSTANT_ALPHA(32772),
      ONE_MINUS_CONSTANT_COLOR(32770),
      ONE_MINUS_DST_ALPHA(773),
      ONE_MINUS_DST_COLOR(775),
      ONE_MINUS_SRC_ALPHA(771),
      ONE_MINUS_SRC_COLOR(769),
      SRC_ALPHA(770),
      SRC_COLOR(768),
      ZERO(0);

      public final int factor;

      private DestFactor(int var3) {
         this.factor = ☃;
      }
   }

   public static enum FogMode {
      LINEAR(9729),
      EXP(2048),
      EXP2(2049);

      public final int capabilityId;

      private FogMode(int var3) {
         this.capabilityId = ☃;
      }
   }

   static class FogState {
      public GlStateManager.BooleanState fog = new GlStateManager.BooleanState(2912);
      public int mode = 2048;
      public float density = 1.0F;
      public float start;
      public float end = 1.0F;

      private FogState() {
      }
   }

   public static enum LogicOp {
      AND(5377),
      AND_INVERTED(5380),
      AND_REVERSE(5378),
      CLEAR(5376),
      COPY(5379),
      COPY_INVERTED(5388),
      EQUIV(5385),
      INVERT(5386),
      NAND(5390),
      NOOP(5381),
      NOR(5384),
      OR(5383),
      OR_INVERTED(5389),
      OR_REVERSE(5387),
      SET(5391),
      XOR(5382);

      public final int opcode;

      private LogicOp(int var3) {
         this.opcode = ☃;
      }
   }

   static class PolygonOffsetState {
      public GlStateManager.BooleanState polygonOffsetFill = new GlStateManager.BooleanState(32823);
      public GlStateManager.BooleanState polygonOffsetLine = new GlStateManager.BooleanState(10754);
      public float factor;
      public float units;

      private PolygonOffsetState() {
      }
   }

   public static enum Profile {
      DEFAULT {
         @Override
         public void apply() {
            GlStateManager.disableAlpha();
            GlStateManager.alphaFunc(519, 0.0F);
            GlStateManager.disableLighting();
            GlStateManager.glLightModel(2899, RenderHelper.setColorBuffer(0.2F, 0.2F, 0.2F, 1.0F));

            for (int ☃ = 0; ☃ < 8; ☃++) {
               GlStateManager.disableLight(☃);
               GlStateManager.glLight(16384 + ☃, 4608, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
               GlStateManager.glLight(16384 + ☃, 4611, RenderHelper.setColorBuffer(0.0F, 0.0F, 1.0F, 0.0F));
               if (☃ == 0) {
                  GlStateManager.glLight(16384 + ☃, 4609, RenderHelper.setColorBuffer(1.0F, 1.0F, 1.0F, 1.0F));
                  GlStateManager.glLight(16384 + ☃, 4610, RenderHelper.setColorBuffer(1.0F, 1.0F, 1.0F, 1.0F));
               } else {
                  GlStateManager.glLight(16384 + ☃, 4609, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
                  GlStateManager.glLight(16384 + ☃, 4610, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
               }
            }

            GlStateManager.disableColorMaterial();
            GlStateManager.colorMaterial(1032, 5634);
            GlStateManager.disableDepth();
            GlStateManager.depthFunc(513);
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.tryBlendFuncSeparate(
               GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
            );
            GlStateManager.glBlendEquation(32774);
            GlStateManager.disableFog();
            GlStateManager.glFogi(2917, 2048);
            GlStateManager.setFogDensity(1.0F);
            GlStateManager.setFogStart(0.0F);
            GlStateManager.setFogEnd(1.0F);
            GlStateManager.glFog(2918, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
               GlStateManager.glFogi(2917, 34140);
            }

            GlStateManager.doPolygonOffset(0.0F, 0.0F);
            GlStateManager.disableColorLogic();
            GlStateManager.colorLogicOp(5379);
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
            GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.S, 9474, RenderHelper.setColorBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.texGen(GlStateManager.TexGen.S, 9217, RenderHelper.setColorBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
            GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.T, 9474, RenderHelper.setColorBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GlStateManager.texGen(GlStateManager.TexGen.T, 9217, RenderHelper.setColorBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
            GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.R, 9474, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.texGen(GlStateManager.TexGen.R, 9217, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.Q);
            GlStateManager.texGen(GlStateManager.TexGen.Q, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.Q, 9474, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.texGen(GlStateManager.TexGen.Q, 9217, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.setActiveTexture(0);
            GlStateManager.glTexParameteri(3553, 10240, 9729);
            GlStateManager.glTexParameteri(3553, 10241, 9986);
            GlStateManager.glTexParameteri(3553, 10242, 10497);
            GlStateManager.glTexParameteri(3553, 10243, 10497);
            GlStateManager.glTexParameteri(3553, 33085, 1000);
            GlStateManager.glTexParameteri(3553, 33083, 1000);
            GlStateManager.glTexParameteri(3553, 33082, -1000);
            GlStateManager.glTexParameterf(3553, 34049, 0.0F);
            GlStateManager.glTexEnvi(8960, 8704, 8448);
            GlStateManager.glTexEnv(8960, 8705, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.glTexEnvi(8960, 34161, 8448);
            GlStateManager.glTexEnvi(8960, 34162, 8448);
            GlStateManager.glTexEnvi(8960, 34176, 5890);
            GlStateManager.glTexEnvi(8960, 34177, 34168);
            GlStateManager.glTexEnvi(8960, 34178, 34166);
            GlStateManager.glTexEnvi(8960, 34184, 5890);
            GlStateManager.glTexEnvi(8960, 34185, 34168);
            GlStateManager.glTexEnvi(8960, 34186, 34166);
            GlStateManager.glTexEnvi(8960, 34192, 768);
            GlStateManager.glTexEnvi(8960, 34193, 768);
            GlStateManager.glTexEnvi(8960, 34194, 770);
            GlStateManager.glTexEnvi(8960, 34200, 770);
            GlStateManager.glTexEnvi(8960, 34201, 770);
            GlStateManager.glTexEnvi(8960, 34202, 770);
            GlStateManager.glTexEnvf(8960, 34163, 1.0F);
            GlStateManager.glTexEnvf(8960, 3356, 1.0F);
            GlStateManager.disableNormalize();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableRescaleNormal();
            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.clearDepth(1.0);
            GlStateManager.glLineWidth(1.0F);
            GlStateManager.glNormal3f(0.0F, 0.0F, 1.0F);
            GlStateManager.glPolygonMode(1028, 6914);
            GlStateManager.glPolygonMode(1029, 6914);
         }

         @Override
         public void clean() {
         }
      },
      PLAYER_SKIN {
         @Override
         public void apply() {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         }

         @Override
         public void clean() {
            GlStateManager.disableBlend();
         }
      },
      TRANSPARENT_MODEL {
         @Override
         public void apply() {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.alphaFunc(516, 0.003921569F);
         }

         @Override
         public void clean() {
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.depthMask(true);
         }
      };

      private Profile() {
      }

      public abstract void apply();

      public abstract void clean();
   }

   public static enum SourceFactor {
      CONSTANT_ALPHA(32771),
      CONSTANT_COLOR(32769),
      DST_ALPHA(772),
      DST_COLOR(774),
      ONE(1),
      ONE_MINUS_CONSTANT_ALPHA(32772),
      ONE_MINUS_CONSTANT_COLOR(32770),
      ONE_MINUS_DST_ALPHA(773),
      ONE_MINUS_DST_COLOR(775),
      ONE_MINUS_SRC_ALPHA(771),
      ONE_MINUS_SRC_COLOR(769),
      SRC_ALPHA(770),
      SRC_ALPHA_SATURATE(776),
      SRC_COLOR(768),
      ZERO(0);

      public final int factor;

      private SourceFactor(int var3) {
         this.factor = ☃;
      }
   }

   static class StencilFunc {
      public int func = 519;
      public int mask = -1;

      private StencilFunc() {
      }
   }

   static class StencilState {
      public GlStateManager.StencilFunc func = new GlStateManager.StencilFunc();
      public int mask = -1;
      public int fail = 7680;
      public int zfail = 7680;
      public int zpass = 7680;

      private StencilState() {
      }
   }

   public static enum TexGen {
      S,
      T,
      R,
      Q;
   }

   static class TexGenCoord {
      public GlStateManager.BooleanState textureGen;
      public int coord;
      public int param = -1;

      public TexGenCoord(int var1, int var2) {
         this.coord = ☃;
         this.textureGen = new GlStateManager.BooleanState(☃);
      }
   }

   static class TexGenState {
      public GlStateManager.TexGenCoord s = new GlStateManager.TexGenCoord(8192, 3168);
      public GlStateManager.TexGenCoord t = new GlStateManager.TexGenCoord(8193, 3169);
      public GlStateManager.TexGenCoord r = new GlStateManager.TexGenCoord(8194, 3170);
      public GlStateManager.TexGenCoord q = new GlStateManager.TexGenCoord(8195, 3171);

      private TexGenState() {
      }
   }

   static class TextureState {
      public GlStateManager.BooleanState texture2DState = new GlStateManager.BooleanState(3553);
      public int textureName;

      private TextureState() {
      }
   }
}
