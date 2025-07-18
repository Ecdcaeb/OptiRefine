package net.minecraft.client.renderer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.annotation.Nullable;
import net.optifine.SmartAnimations;
import net.optifine.render.GlAlphaState;
import net.optifine.render.GlBlendState;
import net.optifine.shaders.Shaders;
import net.optifine.util.LockCounter;
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
   public static boolean clearEnabled = true;
   private static LockCounter alphaLock = new LockCounter();
   private static GlAlphaState alphaLockState = new GlAlphaState();
   private static LockCounter blendLock = new LockCounter();
   private static GlBlendState blendLockState = new GlBlendState();
   private static boolean creatingDisplayList = false;

   public static void pushAttrib() {
      GL11.glPushAttrib(8256);
   }

   public static void popAttrib() {
      GL11.glPopAttrib();
   }

   public static void disableAlpha() {
      if (alphaLock.isLocked()) {
         alphaLockState.setDisabled();
      } else {
         alphaState.alphaTest.setDisabled();
      }
   }

   public static void enableAlpha() {
      if (alphaLock.isLocked()) {
         alphaLockState.setEnabled();
      } else {
         alphaState.alphaTest.setEnabled();
      }
   }

   public static void alphaFunc(int func, float ref) {
      if (alphaLock.isLocked()) {
         alphaLockState.setFuncRef(func, ref);
      } else {
         if (func != alphaState.func || ref != alphaState.ref) {
            alphaState.func = func;
            alphaState.ref = ref;
            GL11.glAlphaFunc(func, ref);
         }
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
      colorMaterialState.colorMaterial.setEnabled();
   }

   public static void disableColorMaterial() {
      colorMaterialState.colorMaterial.setDisabled();
   }

   public static void colorMaterial(int face, int mode) {
      if (face != colorMaterialState.face || mode != colorMaterialState.mode) {
         colorMaterialState.face = face;
         colorMaterialState.mode = mode;
         GL11.glColorMaterial(face, mode);
      }
   }

   public static void glLight(int light, int pname, FloatBuffer params) {
      GL11.glLight(light, pname, params);
   }

   public static void glLightModel(int pname, FloatBuffer params) {
      GL11.glLightModel(pname, params);
   }

   public static void glNormal3f(float nx, float ny, float nz) {
      GL11.glNormal3f(nx, ny, nz);
   }

   public static void disableDepth() {
      depthState.depthTest.setDisabled();
   }

   public static void enableDepth() {
      depthState.depthTest.setEnabled();
   }

   public static void depthFunc(int depthFunc) {
      if (depthFunc != depthState.depthFunc) {
         depthState.depthFunc = depthFunc;
         GL11.glDepthFunc(depthFunc);
      }
   }

   public static void depthMask(boolean flagIn) {
      if (flagIn != depthState.maskEnabled) {
         depthState.maskEnabled = flagIn;
         GL11.glDepthMask(flagIn);
      }
   }

   public static void disableBlend() {
      if (blendLock.isLocked()) {
         blendLockState.setDisabled();
      } else {
         blendState.blend.setDisabled();
      }
   }

   public static void enableBlend() {
      if (blendLock.isLocked()) {
         blendLockState.setEnabled();
      } else {
         blendState.blend.setEnabled();
      }
   }

   public static void blendFunc(GlStateManager.SourceFactor srcFactor, GlStateManager.DestFactor dstFactor) {
      blendFunc(srcFactor.factor, dstFactor.factor);
   }

   public static void blendFunc(int srcFactor, int dstFactor) {
      if (blendLock.isLocked()) {
         blendLockState.setFactors(srcFactor, dstFactor);
      } else {
         if (srcFactor != blendState.srcFactor
            || dstFactor != blendState.dstFactor
            || srcFactor != blendState.srcFactorAlpha
            || dstFactor != blendState.dstFactorAlpha) {
            blendState.srcFactor = srcFactor;
            blendState.dstFactor = dstFactor;
            blendState.srcFactorAlpha = srcFactor;
            blendState.dstFactorAlpha = dstFactor;
            if (Config.isShaders()) {
               Shaders.uniform_blendFunc.setValue(srcFactor, dstFactor, srcFactor, dstFactor);
            }

            GL11.glBlendFunc(srcFactor, dstFactor);
         }
      }
   }

   public static void tryBlendFuncSeparate(
      GlStateManager.SourceFactor srcFactor,
      GlStateManager.DestFactor dstFactor,
      GlStateManager.SourceFactor srcFactorAlpha,
      GlStateManager.DestFactor dstFactorAlpha
   ) {
      tryBlendFuncSeparate(srcFactor.factor, dstFactor.factor, srcFactorAlpha.factor, dstFactorAlpha.factor);
   }

   public static void tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha) {
      if (blendLock.isLocked()) {
         blendLockState.setFactors(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
      } else {
         if (srcFactor != blendState.srcFactor
            || dstFactor != blendState.dstFactor
            || srcFactorAlpha != blendState.srcFactorAlpha
            || dstFactorAlpha != blendState.dstFactorAlpha) {
            blendState.srcFactor = srcFactor;
            blendState.dstFactor = dstFactor;
            blendState.srcFactorAlpha = srcFactorAlpha;
            blendState.dstFactorAlpha = dstFactorAlpha;
            if (Config.isShaders()) {
               Shaders.uniform_blendFunc.setValue(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
            }

            OpenGlHelper.glBlendFunc(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
         }
      }
   }

   public static void glBlendEquation(int blendEquation) {
      GL14.glBlendEquation(blendEquation);
   }

   public static void enableOutlineMode(int p_187431_0_) {
      BUF_FLOAT_4.put(0, (p_187431_0_ >> 16 & 0xFF) / 255.0F);
      BUF_FLOAT_4.put(1, (p_187431_0_ >> 8 & 0xFF) / 255.0F);
      BUF_FLOAT_4.put(2, (p_187431_0_ >> 0 & 0xFF) / 255.0F);
      BUF_FLOAT_4.put(3, (p_187431_0_ >> 24 & 0xFF) / 255.0F);
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

   public static void setFog(GlStateManager.FogMode fogMode) {
      setFog(fogMode.capabilityId);
   }

   private static void setFog(int param) {
      if (param != fogState.mode) {
         fogState.mode = param;
         GL11.glFogi(2917, param);
         if (Config.isShaders()) {
            Shaders.setFogMode(param);
         }
      }
   }

   public static void setFogDensity(float param) {
      if (param < 0.0F) {
         param = 0.0F;
      }

      if (param != fogState.density) {
         fogState.density = param;
         GL11.glFogf(2914, param);
         if (Config.isShaders()) {
            Shaders.setFogDensity(param);
         }
      }
   }

   public static void setFogStart(float param) {
      if (param != fogState.start) {
         fogState.start = param;
         GL11.glFogf(2915, param);
      }
   }

   public static void setFogEnd(float param) {
      if (param != fogState.end) {
         fogState.end = param;
         GL11.glFogf(2916, param);
      }
   }

   public static void glFog(int pname, FloatBuffer param) {
      GL11.glFog(pname, param);
   }

   public static void glFogi(int pname, int param) {
      GL11.glFogi(pname, param);
   }

   public static void enableCull() {
      cullState.cullFace.setEnabled();
   }

   public static void disableCull() {
      cullState.cullFace.setDisabled();
   }

   public static void cullFace(GlStateManager.CullFace cullFace) {
      cullFace(cullFace.mode);
   }

   private static void cullFace(int mode) {
      if (mode != cullState.mode) {
         cullState.mode = mode;
         GL11.glCullFace(mode);
      }
   }

   public static void glPolygonMode(int face, int mode) {
      GL11.glPolygonMode(face, mode);
   }

   public static void enablePolygonOffset() {
      polygonOffsetState.polygonOffsetFill.setEnabled();
   }

   public static void disablePolygonOffset() {
      polygonOffsetState.polygonOffsetFill.setDisabled();
   }

   public static void doPolygonOffset(float factor, float units) {
      if (factor != polygonOffsetState.factor || units != polygonOffsetState.units) {
         polygonOffsetState.factor = factor;
         polygonOffsetState.units = units;
         GL11.glPolygonOffset(factor, units);
      }
   }

   public static void enableColorLogic() {
      colorLogicState.colorLogicOp.setEnabled();
   }

   public static void disableColorLogic() {
      colorLogicState.colorLogicOp.setDisabled();
   }

   public static void colorLogicOp(GlStateManager.LogicOp logicOperation) {
      colorLogicOp(logicOperation.opcode);
   }

   public static void colorLogicOp(int opcode) {
      if (opcode != colorLogicState.opcode) {
         colorLogicState.opcode = opcode;
         GL11.glLogicOp(opcode);
      }
   }

   public static void enableTexGenCoord(GlStateManager.TexGen texGen) {
      texGenCoord(texGen).textureGen.setEnabled();
   }

   public static void disableTexGenCoord(GlStateManager.TexGen texGen) {
      texGenCoord(texGen).textureGen.setDisabled();
   }

   public static void texGen(GlStateManager.TexGen texGen, int param) {
      GlStateManager.TexGenCoord glstatemanager$texgencoord = texGenCoord(texGen);
      if (param != glstatemanager$texgencoord.param) {
         glstatemanager$texgencoord.param = param;
         GL11.glTexGeni(glstatemanager$texgencoord.coord, 9472, param);
      }
   }

   public static void texGen(GlStateManager.TexGen texGen, int pname, FloatBuffer params) {
      GL11.glTexGen(texGenCoord(texGen).coord, pname, params);
   }

   private static GlStateManager.TexGenCoord texGenCoord(GlStateManager.TexGen texGen) {
      switch (texGen) {
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

   public static void setActiveTexture(int texture) {
      if (activeTextureUnit != texture - OpenGlHelper.defaultTexUnit) {
         activeTextureUnit = texture - OpenGlHelper.defaultTexUnit;
         OpenGlHelper.setActiveTexture(texture);
      }
   }

   public static void enableTexture2D() {
      textureState[activeTextureUnit].texture2DState.setEnabled();
   }

   public static void disableTexture2D() {
      textureState[activeTextureUnit].texture2DState.setDisabled();
   }

   public static void glTexEnv(int target, int parameterName, FloatBuffer parameters) {
      GL11.glTexEnv(target, parameterName, parameters);
   }

   public static void glTexEnvi(int target, int parameterName, int parameter) {
      GL11.glTexEnvi(target, parameterName, parameter);
   }

   public static void glTexEnvf(int target, int parameterName, float parameter) {
      GL11.glTexEnvf(target, parameterName, parameter);
   }

   public static void glTexParameterf(int target, int parameterName, float parameter) {
      GL11.glTexParameterf(target, parameterName, parameter);
   }

   public static void glTexParameteri(int target, int parameterName, int parameter) {
      GL11.glTexParameteri(target, parameterName, parameter);
   }

   public static int glGetTexLevelParameteri(int target, int level, int parameterName) {
      return GL11.glGetTexLevelParameteri(target, level, parameterName);
   }

   public static int generateTexture() {
      return GL11.glGenTextures();
   }

   public static void deleteTexture(int texture) {
      if (texture != 0) {
         GL11.glDeleteTextures(texture);

         for (GlStateManager.TextureState glstatemanager$texturestate : textureState) {
            if (glstatemanager$texturestate.textureName == texture) {
               glstatemanager$texturestate.textureName = 0;
            }
         }
      }
   }

   public static void bindTexture(int texture) {
      if (texture != textureState[activeTextureUnit].textureName) {
         textureState[activeTextureUnit].textureName = texture;
         GL11.glBindTexture(3553, texture);
         if (SmartAnimations.isActive()) {
            SmartAnimations.textureRendered(texture);
         }
      }
   }

   public static void glTexImage2D(
      int target, int level, int internalFormat, int width, int height, int border, int format, int type, @Nullable IntBuffer pixels
   ) {
      GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, type, pixels);
   }

   public static void glTexSubImage2D(int target, int level, int xOffset, int yOffset, int width, int height, int format, int type, IntBuffer pixels) {
      GL11.glTexSubImage2D(target, level, xOffset, yOffset, width, height, format, type, pixels);
   }

   public static void glCopyTexSubImage2D(int target, int level, int xOffset, int yOffset, int x, int y, int width, int height) {
      GL11.glCopyTexSubImage2D(target, level, xOffset, yOffset, x, y, width, height);
   }

   public static void glGetTexImage(int target, int level, int format, int type, IntBuffer pixels) {
      GL11.glGetTexImage(target, level, format, type, pixels);
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
         GL11.glShadeModel(mode);
      }
   }

   public static void enableRescaleNormal() {
      rescaleNormalState.setEnabled();
   }

   public static void disableRescaleNormal() {
      rescaleNormalState.setDisabled();
   }

   public static void viewport(int x, int y, int width, int height) {
      GL11.glViewport(x, y, width, height);
   }

   public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
      if (red != colorMaskState.red || green != colorMaskState.green || blue != colorMaskState.blue || alpha != colorMaskState.alpha) {
         colorMaskState.red = red;
         colorMaskState.green = green;
         colorMaskState.blue = blue;
         colorMaskState.alpha = alpha;
         GL11.glColorMask(red, green, blue, alpha);
      }
   }

   public static void clearDepth(double depth) {
      if (depth != clearState.depth) {
         clearState.depth = depth;
         GL11.glClearDepth(depth);
      }
   }

   public static void clearColor(float red, float green, float blue, float alpha) {
      if (red != clearState.color.red || green != clearState.color.green || blue != clearState.color.blue || alpha != clearState.color.alpha) {
         clearState.color.red = red;
         clearState.color.green = green;
         clearState.color.blue = blue;
         clearState.color.alpha = alpha;
         GL11.glClearColor(red, green, blue, alpha);
      }
   }

   public static void clear(int mask) {
      if (clearEnabled) {
         GL11.glClear(mask);
      }
   }

   public static void matrixMode(int mode) {
      GL11.glMatrixMode(mode);
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
      GL11.glGetFloat(pname, params);
   }

   public static void ortho(double left, double right, double bottom, double top, double zNear, double zFar) {
      GL11.glOrtho(left, right, bottom, top, zNear, zFar);
   }

   public static void rotate(float angle, float x, float y, float z) {
      GL11.glRotatef(angle, x, y, z);
   }

   public static void scale(float x, float y, float z) {
      GL11.glScalef(x, y, z);
   }

   public static void scale(double x, double y, double z) {
      GL11.glScaled(x, y, z);
   }

   public static void translate(float x, float y, float z) {
      GL11.glTranslatef(x, y, z);
   }

   public static void translate(double x, double y, double z) {
      GL11.glTranslated(x, y, z);
   }

   public static void multMatrix(FloatBuffer matrix) {
      GL11.glMultMatrix(matrix);
   }

   public static void rotate(Quaternion quaternionIn) {
      multMatrix(quatToGlMatrix(BUF_FLOAT_16, quaternionIn));
   }

   public static FloatBuffer quatToGlMatrix(FloatBuffer buffer, Quaternion quaternionIn) {
      ((Buffer)buffer).clear();
      float f = quaternionIn.x * quaternionIn.x;
      float f1 = quaternionIn.x * quaternionIn.y;
      float f2 = quaternionIn.x * quaternionIn.z;
      float f3 = quaternionIn.x * quaternionIn.w;
      float f4 = quaternionIn.y * quaternionIn.y;
      float f5 = quaternionIn.y * quaternionIn.z;
      float f6 = quaternionIn.y * quaternionIn.w;
      float f7 = quaternionIn.z * quaternionIn.z;
      float f8 = quaternionIn.z * quaternionIn.w;
      buffer.put(1.0F - 2.0F * (f4 + f7));
      buffer.put(2.0F * (f1 + f8));
      buffer.put(2.0F * (f2 - f6));
      buffer.put(0.0F);
      buffer.put(2.0F * (f1 - f8));
      buffer.put(1.0F - 2.0F * (f + f7));
      buffer.put(2.0F * (f5 + f3));
      buffer.put(0.0F);
      buffer.put(2.0F * (f2 + f6));
      buffer.put(2.0F * (f5 - f3));
      buffer.put(1.0F - 2.0F * (f + f4));
      buffer.put(0.0F);
      buffer.put(0.0F);
      buffer.put(0.0F);
      buffer.put(0.0F);
      buffer.put(1.0F);
      ((Buffer)buffer).rewind();
      return buffer;
   }

   public static void color(float colorRed, float colorGreen, float colorBlue, float colorAlpha) {
      if (colorRed != colorState.red || colorGreen != colorState.green || colorBlue != colorState.blue || colorAlpha != colorState.alpha) {
         colorState.red = colorRed;
         colorState.green = colorGreen;
         colorState.blue = colorBlue;
         colorState.alpha = colorAlpha;
         GL11.glColor4f(colorRed, colorGreen, colorBlue, colorAlpha);
      }
   }

   public static void color(float colorRed, float colorGreen, float colorBlue) {
      color(colorRed, colorGreen, colorBlue, 1.0F);
   }

   public static void glTexCoord2f(float sCoord, float tCoord) {
      GL11.glTexCoord2f(sCoord, tCoord);
   }

   public static void glVertex3f(float x, float y, float z) {
      GL11.glVertex3f(x, y, z);
   }

   public static void resetColor() {
      colorState.red = -1.0F;
      colorState.green = -1.0F;
      colorState.blue = -1.0F;
      colorState.alpha = -1.0F;
   }

   public static void glNormalPointer(int type, int stride, ByteBuffer buffer) {
      GL11.glNormalPointer(type, stride, buffer);
   }

   public static void glTexCoordPointer(int size, int type, int stride, int buffer_offset) {
      GL11.glTexCoordPointer(size, type, stride, buffer_offset);
   }

   public static void glTexCoordPointer(int size, int type, int stride, ByteBuffer buffer) {
      GL11.glTexCoordPointer(size, type, stride, buffer);
   }

   public static void glVertexPointer(int size, int type, int stride, int buffer_offset) {
      GL11.glVertexPointer(size, type, stride, buffer_offset);
   }

   public static void glVertexPointer(int size, int type, int stride, ByteBuffer buffer) {
      GL11.glVertexPointer(size, type, stride, buffer);
   }

   public static void glColorPointer(int size, int type, int stride, int buffer_offset) {
      GL11.glColorPointer(size, type, stride, buffer_offset);
   }

   public static void glColorPointer(int size, int type, int stride, ByteBuffer buffer) {
      GL11.glColorPointer(size, type, stride, buffer);
   }

   public static void glDisableClientState(int cap) {
      GL11.glDisableClientState(cap);
   }

   public static void glEnableClientState(int cap) {
      GL11.glEnableClientState(cap);
   }

   public static void glBegin(int mode) {
      GL11.glBegin(mode);
   }

   public static void glEnd() {
      GL11.glEnd();
   }

   public static void glDrawArrays(int mode, int first, int count) {
      GL11.glDrawArrays(mode, first, count);
      if (Config.isShaders() && !creatingDisplayList) {
         int countInstances = Shaders.activeProgram.getCountInstances();
         if (countInstances > 1) {
            for (int i = 1; i < countInstances; i++) {
               Shaders.uniform_instanceId.setValue(i);
               GL11.glDrawArrays(mode, first, count);
            }

            Shaders.uniform_instanceId.setValue(0);
         }
      }
   }

   public static void glLineWidth(float width) {
      GL11.glLineWidth(width);
   }

   public static void callList(int list) {
      GL11.glCallList(list);
      if (Config.isShaders() && !creatingDisplayList) {
         int countInstances = Shaders.activeProgram.getCountInstances();
         if (countInstances > 1) {
            for (int i = 1; i < countInstances; i++) {
               Shaders.uniform_instanceId.setValue(i);
               GL11.glCallList(list);
            }

            Shaders.uniform_instanceId.setValue(0);
         }
      }
   }

   public static void callLists(IntBuffer lists) {
      GL11.glCallLists(lists);
      if (Config.isShaders() && !creatingDisplayList) {
         int countInstances = Shaders.activeProgram.getCountInstances();
         if (countInstances > 1) {
            for (int i = 1; i < countInstances; i++) {
               Shaders.uniform_instanceId.setValue(i);
               GL11.glCallLists(lists);
            }

            Shaders.uniform_instanceId.setValue(0);
         }
      }
   }

   public static void glDeleteLists(int list, int range) {
      GL11.glDeleteLists(list, range);
   }

   public static void glNewList(int list, int mode) {
      GL11.glNewList(list, mode);
      creatingDisplayList = true;
   }

   public static void glEndList() {
      GL11.glEndList();
      creatingDisplayList = false;
   }

   public static int glGenLists(int range) {
      return GL11.glGenLists(range);
   }

   public static void glPixelStorei(int parameterName, int param) {
      GL11.glPixelStorei(parameterName, param);
   }

   public static void glReadPixels(int x, int y, int width, int height, int format, int type, IntBuffer pixels) {
      GL11.glReadPixels(x, y, width, height, format, type, pixels);
   }

   public static int glGetError() {
      return GL11.glGetError();
   }

   public static String glGetString(int name) {
      return GL11.glGetString(name);
   }

   public static void glGetInteger(int parameterName, IntBuffer parameters) {
      GL11.glGetInteger(parameterName, parameters);
   }

   public static int glGetInteger(int parameterName) {
      return GL11.glGetInteger(parameterName);
   }

   public static void enableBlendProfile(GlStateManager.Profile p_187408_0_) {
      p_187408_0_.apply();
   }

   public static void disableBlendProfile(GlStateManager.Profile p_187440_0_) {
      p_187440_0_.clean();
   }

   public static int getActiveTextureUnit() {
      return OpenGlHelper.defaultTexUnit + activeTextureUnit;
   }

   public static void bindCurrentTexture() {
      GL11.glBindTexture(3553, textureState[activeTextureUnit].textureName);
   }

   public static int getBoundTexture() {
      return textureState[activeTextureUnit].textureName;
   }

   public static void checkBoundTexture() {
      if (Config.isMinecraftThread()) {
         int glAct = GL11.glGetInteger(34016);
         int glTex = GL11.glGetInteger(32873);
         int act = getActiveTextureUnit();
         int tex = getBoundTexture();
         if (tex > 0) {
            if (glAct != act || glTex != tex) {
               Config.dbg("checkTexture: act: " + act + ", glAct: " + glAct + ", tex: " + tex + ", glTex: " + glTex);
            }
         }
      }
   }

   public static void deleteTextures(IntBuffer buf) {
      ((Buffer)buf).rewind();

      while (buf.position() < buf.limit()) {
         int texId = buf.get();
         deleteTexture(texId);
      }

      ((Buffer)buf).rewind();
   }

   public static boolean isFogEnabled() {
      return fogState.fog.currentState;
   }

   public static void setFogEnabled(boolean state) {
      fogState.fog.setState(state);
   }

   public static void lockAlpha(GlAlphaState stateNew) {
      if (!alphaLock.isLocked()) {
         getAlphaState(alphaLockState);
         setAlphaState(stateNew);
         alphaLock.lock();
      }
   }

   public static void unlockAlpha() {
      if (alphaLock.unlock()) {
         setAlphaState(alphaLockState);
      }
   }

   public static void getAlphaState(GlAlphaState state) {
      if (alphaLock.isLocked()) {
         state.setState(alphaLockState);
      } else {
         state.setState(alphaState.alphaTest.currentState, alphaState.func, alphaState.ref);
      }
   }

   public static void setAlphaState(GlAlphaState state) {
      if (alphaLock.isLocked()) {
         alphaLockState.setState(state);
      } else {
         alphaState.alphaTest.setState(state.isEnabled());
         alphaFunc(state.getFunc(), state.getRef());
      }
   }

   public static void lockBlend(GlBlendState stateNew) {
      if (!blendLock.isLocked()) {
         getBlendState(blendLockState);
         setBlendState(stateNew);
         blendLock.lock();
      }
   }

   public static void unlockBlend() {
      if (blendLock.unlock()) {
         setBlendState(blendLockState);
      }
   }

   public static void getBlendState(GlBlendState gbs) {
      if (blendLock.isLocked()) {
         gbs.setState(blendLockState);
      } else {
         gbs.setState(blendState.blend.currentState, blendState.srcFactor, blendState.dstFactor, blendState.srcFactorAlpha, blendState.dstFactorAlpha);
      }
   }

   public static void setBlendState(GlBlendState gbs) {
      if (blendLock.isLocked()) {
         blendLockState.setState(gbs);
      } else {
         blendState.blend.setState(gbs.isEnabled());
         if (!gbs.isSeparate()) {
            blendFunc(gbs.getSrcFactor(), gbs.getDstFactor());
         } else {
            tryBlendFuncSeparate(gbs.getSrcFactor(), gbs.getDstFactor(), gbs.getSrcFactorAlpha(), gbs.getDstFactorAlpha());
         }
      }
   }

   public static void glMultiDrawArrays(int mode, IntBuffer bFirst, IntBuffer bCount) {
      GL14.glMultiDrawArrays(mode, bFirst, bCount);
      if (Config.isShaders() && !creatingDisplayList) {
         int countInstances = Shaders.activeProgram.getCountInstances();
         if (countInstances > 1) {
            for (int i = 1; i < countInstances; i++) {
               Shaders.uniform_instanceId.setValue(i);
               GL14.glMultiDrawArrays(mode, bFirst, bCount);
            }

            Shaders.uniform_instanceId.setValue(0);
         }
      }
   }

   static {
      for (int i = 0; i < 8; i++) {
         lightState[i] = new GlStateManager.BooleanState(16384 + i);
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
      textureState = new GlStateManager.TextureState[32];

      for (int j = 0; j < textureState.length; j++) {
         textureState[j] = new GlStateManager.TextureState();
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

      public BooleanState(int capabilityIn) {
         this.capability = capabilityIn;
      }

      public void setDisabled() {
         this.setState(false);
      }

      public void setEnabled() {
         this.setState(true);
      }

      public void setState(boolean state) {
         if (state != this.currentState) {
            this.currentState = state;
            if (state) {
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

      public Color(float redIn, float greenIn, float blueIn, float alphaIn) {
         this.red = redIn;
         this.green = greenIn;
         this.blue = blueIn;
         this.alpha = alphaIn;
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

      private CullFace(int modeIn) {
         this.mode = modeIn;
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

      private DestFactor(int factorIn) {
         this.factor = factorIn;
      }
   }

   public static enum FogMode {
      LINEAR(9729),
      EXP(2048),
      EXP2(2049);

      public final int capabilityId;

      private FogMode(int capabilityIn) {
         this.capabilityId = capabilityIn;
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

      private LogicOp(int opcodeIn) {
         this.opcode = opcodeIn;
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
            GL11.glLightModel(2899, RenderHelper.setColorBuffer(0.2F, 0.2F, 0.2F, 1.0F));

            for (int i = 0; i < 8; i++) {
               GlStateManager.disableLight(i);
               GL11.glLight(16384 + i, 4608, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
               GL11.glLight(16384 + i, 4611, RenderHelper.setColorBuffer(0.0F, 0.0F, 1.0F, 0.0F));
               if (i == 0) {
                  GL11.glLight(16384 + i, 4609, RenderHelper.setColorBuffer(1.0F, 1.0F, 1.0F, 1.0F));
                  GL11.glLight(16384 + i, 4610, RenderHelper.setColorBuffer(1.0F, 1.0F, 1.0F, 1.0F));
               } else {
                  GL11.glLight(16384 + i, 4609, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
                  GL11.glLight(16384 + i, 4610, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
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
            GL14.glBlendEquation(32774);
            GlStateManager.disableFog();
            GL11.glFogi(2917, 2048);
            GlStateManager.setFogDensity(1.0F);
            GlStateManager.setFogStart(0.0F);
            GlStateManager.setFogEnd(1.0F);
            GL11.glFog(2918, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
               GL11.glFogi(2917, 34140);
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
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10241, 9986);
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
            GL11.glTexParameteri(3553, 33085, 1000);
            GL11.glTexParameteri(3553, 33083, 1000);
            GL11.glTexParameteri(3553, 33082, -1000);
            GL11.glTexParameterf(3553, 34049, 0.0F);
            GL11.glTexEnvi(8960, 8704, 8448);
            GL11.glTexEnv(8960, 8705, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexEnvi(8960, 34161, 8448);
            GL11.glTexEnvi(8960, 34162, 8448);
            GL11.glTexEnvi(8960, 34176, 5890);
            GL11.glTexEnvi(8960, 34177, 34168);
            GL11.glTexEnvi(8960, 34178, 34166);
            GL11.glTexEnvi(8960, 34184, 5890);
            GL11.glTexEnvi(8960, 34185, 34168);
            GL11.glTexEnvi(8960, 34186, 34166);
            GL11.glTexEnvi(8960, 34192, 768);
            GL11.glTexEnvi(8960, 34193, 768);
            GL11.glTexEnvi(8960, 34194, 770);
            GL11.glTexEnvi(8960, 34200, 770);
            GL11.glTexEnvi(8960, 34201, 770);
            GL11.glTexEnvi(8960, 34202, 770);
            GL11.glTexEnvf(8960, 34163, 1.0F);
            GL11.glTexEnvf(8960, 3356, 1.0F);
            GlStateManager.disableNormalize();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableRescaleNormal();
            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.clearDepth(1.0);
            GL11.glLineWidth(1.0F);
            GL11.glNormal3f(0.0F, 0.0F, 1.0F);
            GL11.glPolygonMode(1028, 6914);
            GL11.glPolygonMode(1029, 6914);
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

      private SourceFactor(int factorIn) {
         this.factor = factorIn;
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

      public TexGenCoord(int coordIn, int capabilityIn) {
         this.coord = coordIn;
         this.textureGen = new GlStateManager.BooleanState(capabilityIn);
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
