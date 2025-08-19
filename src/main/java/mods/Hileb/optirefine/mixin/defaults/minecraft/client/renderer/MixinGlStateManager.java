package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

public class MixinGlStateManager {
}
/*
--- net/minecraft/client/renderer/GlStateManager.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/GlStateManager.java	Tue Aug 19 14:59:58 2025
@@ -2,12 +2,17 @@

 import java.nio.Buffer;
 import java.nio.ByteBuffer;
 import java.nio.FloatBuffer;
 import java.nio.IntBuffer;
 import javax.annotation.Nullable;
+import net.optifine.SmartAnimations;
+import net.optifine.render.GlAlphaState;
+import net.optifine.render.GlBlendState;
+import net.optifine.shaders.Shaders;
+import net.optifine.util.LockCounter;
 import org.lwjgl.BufferUtils;
 import org.lwjgl.opengl.GL11;
 import org.lwjgl.opengl.GL14;
 import org.lwjgl.opengl.GLContext;
 import org.lwjgl.util.vector.Quaternion;

@@ -31,34 +36,52 @@
    private static int activeTextureUnit;
    private static final GlStateManager.TextureState[] textureState;
    private static int activeShadeModel;
    private static final GlStateManager.BooleanState rescaleNormalState;
    private static final GlStateManager.ColorMask colorMaskState;
    private static final GlStateManager.Color colorState;
+   public static boolean clearEnabled = true;
+   private static LockCounter alphaLock = new LockCounter();
+   private static GlAlphaState alphaLockState = new GlAlphaState();
+   private static LockCounter blendLock = new LockCounter();
+   private static GlBlendState blendLockState = new GlBlendState();
+   private static boolean creatingDisplayList = false;

    public static void pushAttrib() {
       GL11.glPushAttrib(8256);
    }

    public static void popAttrib() {
       GL11.glPopAttrib();
    }

    public static void disableAlpha() {
-      alphaState.alphaTest.setDisabled();
+      if (alphaLock.isLocked()) {
+         alphaLockState.setDisabled();
+      } else {
+         alphaState.alphaTest.setDisabled();
+      }
    }

    public static void enableAlpha() {
-      alphaState.alphaTest.setEnabled();
+      if (alphaLock.isLocked()) {
+         alphaLockState.setEnabled();
+      } else {
+         alphaState.alphaTest.setEnabled();
+      }
    }

    public static void alphaFunc(int var0, float var1) {
-      if (var0 != alphaState.func || var1 != alphaState.ref) {
-         alphaState.func = var0;
-         alphaState.ref = var1;
-         GL11.glAlphaFunc(var0, var1);
+      if (alphaLock.isLocked()) {
+         alphaLockState.setFuncRef(var0, var1);
+      } else {
+         if (var0 != alphaState.func || var1 != alphaState.ref) {
+            alphaState.func = var0;
+            alphaState.ref = var1;
+            GL11.glAlphaFunc(var0, var1);
+         }
       }
    }

    public static void enableLighting() {
       lightingState.setEnabled();
    }
@@ -123,44 +146,70 @@
          depthState.maskEnabled = var0;
          GL11.glDepthMask(var0);
       }
    }

    public static void disableBlend() {
-      blendState.blend.setDisabled();
+      if (blendLock.isLocked()) {
+         blendLockState.setDisabled();
+      } else {
+         blendState.blend.setDisabled();
+      }
    }

    public static void enableBlend() {
-      blendState.blend.setEnabled();
+      if (blendLock.isLocked()) {
+         blendLockState.setEnabled();
+      } else {
+         blendState.blend.setEnabled();
+      }
    }

    public static void blendFunc(GlStateManager.SourceFactor var0, GlStateManager.DestFactor var1) {
       blendFunc(var0.factor, var1.factor);
    }

    public static void blendFunc(int var0, int var1) {
-      if (var0 != blendState.srcFactor || var1 != blendState.dstFactor) {
-         blendState.srcFactor = var0;
-         blendState.dstFactor = var1;
-         GL11.glBlendFunc(var0, var1);
+      if (blendLock.isLocked()) {
+         blendLockState.setFactors(var0, var1);
+      } else {
+         if (var0 != blendState.srcFactor || var1 != blendState.dstFactor || var0 != blendState.srcFactorAlpha || var1 != blendState.dstFactorAlpha) {
+            blendState.srcFactor = var0;
+            blendState.dstFactor = var1;
+            blendState.srcFactorAlpha = var0;
+            blendState.dstFactorAlpha = var1;
+            if (Config.isShaders()) {
+               Shaders.uniform_blendFunc.setValue(var0, var1, var0, var1);
+            }
+
+            GL11.glBlendFunc(var0, var1);
+         }
       }
    }

    public static void tryBlendFuncSeparate(
       GlStateManager.SourceFactor var0, GlStateManager.DestFactor var1, GlStateManager.SourceFactor var2, GlStateManager.DestFactor var3
    ) {
       tryBlendFuncSeparate(var0.factor, var1.factor, var2.factor, var3.factor);
    }

    public static void tryBlendFuncSeparate(int var0, int var1, int var2, int var3) {
-      if (var0 != blendState.srcFactor || var1 != blendState.dstFactor || var2 != blendState.srcFactorAlpha || var3 != blendState.dstFactorAlpha) {
-         blendState.srcFactor = var0;
-         blendState.dstFactor = var1;
-         blendState.srcFactorAlpha = var2;
-         blendState.dstFactorAlpha = var3;
-         OpenGlHelper.glBlendFunc(var0, var1, var2, var3);
+      if (blendLock.isLocked()) {
+         blendLockState.setFactors(var0, var1, var2, var3);
+      } else {
+         if (var0 != blendState.srcFactor || var1 != blendState.dstFactor || var2 != blendState.srcFactorAlpha || var3 != blendState.dstFactorAlpha) {
+            blendState.srcFactor = var0;
+            blendState.dstFactor = var1;
+            blendState.srcFactorAlpha = var2;
+            blendState.dstFactorAlpha = var3;
+            if (Config.isShaders()) {
+               Shaders.uniform_blendFunc.setValue(var0, var1, var2, var3);
+            }
+
+            OpenGlHelper.glBlendFunc(var0, var1, var2, var3);
+         }
       }
    }

    public static void glBlendEquation(int var0) {
       GL14.glBlendEquation(var0);
    }
@@ -203,19 +252,29 @@
    }

    private static void setFog(int var0) {
       if (var0 != fogState.mode) {
          fogState.mode = var0;
          GL11.glFogi(2917, var0);
+         if (Config.isShaders()) {
+            Shaders.setFogMode(var0);
+         }
       }
    }

    public static void setFogDensity(float var0) {
+      if (var0 < 0.0F) {
+         var0 = 0.0F;
+      }
+
       if (var0 != fogState.density) {
          fogState.density = var0;
          GL11.glFogf(2914, var0);
+         if (Config.isShaders()) {
+            Shaders.setFogDensity(var0);
+         }
       }
    }

    public static void setFogStart(float var0) {
       if (var0 != fogState.start) {
          fogState.start = var0;
@@ -372,25 +431,30 @@

    public static int generateTexture() {
       return GL11.glGenTextures();
    }

    public static void deleteTexture(int var0) {
-      GL11.glDeleteTextures(var0);
+      if (var0 != 0) {
+         GL11.glDeleteTextures(var0);

-      for (GlStateManager.TextureState var4 : textureState) {
-         if (var4.textureName == var0) {
-            var4.textureName = -1;
+         for (GlStateManager.TextureState var4 : textureState) {
+            if (var4.textureName == var0) {
+               var4.textureName = 0;
+            }
          }
       }
    }

    public static void bindTexture(int var0) {
       if (var0 != textureState[activeTextureUnit].textureName) {
          textureState[activeTextureUnit].textureName = var0;
          GL11.glBindTexture(3553, var0);
+         if (SmartAnimations.isActive()) {
+            SmartAnimations.textureRendered(var0);
+         }
       }
    }

    public static void glTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, @Nullable IntBuffer var8) {
       GL11.glTexImage2D(var0, var1, var2, var3, var4, var5, var6, var7, var8);
    }
@@ -459,13 +523,15 @@
          clearState.color.alpha = var3;
          GL11.glClearColor(var0, var1, var2, var3);
       }
    }

    public static void clear(int var0) {
-      GL11.glClear(var0);
+      if (clearEnabled) {
+         GL11.glClear(var0);
+      }
    }

    public static void matrixMode(int var0) {
       GL11.glMatrixMode(var0);
    }

@@ -620,32 +686,71 @@
    public static void glEnd() {
       GL11.glEnd();
    }

    public static void glDrawArrays(int var0, int var1, int var2) {
       GL11.glDrawArrays(var0, var1, var2);
+      if (Config.isShaders() && !creatingDisplayList) {
+         int var3 = Shaders.activeProgram.getCountInstances();
+         if (var3 > 1) {
+            for (int var4 = 1; var4 < var3; var4++) {
+               Shaders.uniform_instanceId.setValue(var4);
+               GL11.glDrawArrays(var0, var1, var2);
+            }
+
+            Shaders.uniform_instanceId.setValue(0);
+         }
+      }
    }

    public static void glLineWidth(float var0) {
       GL11.glLineWidth(var0);
    }

    public static void callList(int var0) {
       GL11.glCallList(var0);
+      if (Config.isShaders() && !creatingDisplayList) {
+         int var1 = Shaders.activeProgram.getCountInstances();
+         if (var1 > 1) {
+            for (int var2 = 1; var2 < var1; var2++) {
+               Shaders.uniform_instanceId.setValue(var2);
+               GL11.glCallList(var0);
+            }
+
+            Shaders.uniform_instanceId.setValue(0);
+         }
+      }
+   }
+
+   public static void callLists(IntBuffer var0) {
+      GL11.glCallLists(var0);
+      if (Config.isShaders() && !creatingDisplayList) {
+         int var1 = Shaders.activeProgram.getCountInstances();
+         if (var1 > 1) {
+            for (int var2 = 1; var2 < var1; var2++) {
+               Shaders.uniform_instanceId.setValue(var2);
+               GL11.glCallLists(var0);
+            }
+
+            Shaders.uniform_instanceId.setValue(0);
+         }
+      }
    }

    public static void glDeleteLists(int var0, int var1) {
       GL11.glDeleteLists(var0, var1);
    }

    public static void glNewList(int var0, int var1) {
       GL11.glNewList(var0, var1);
+      creatingDisplayList = true;
    }

    public static void glEndList() {
       GL11.glEndList();
+      creatingDisplayList = false;
    }

    public static int glGenLists(int var0) {
       return GL11.glGenLists(var0);
    }

@@ -678,12 +783,138 @@
    }

    public static void disableBlendProfile(GlStateManager.Profile var0) {
       var0.clean();
    }

+   public static int getActiveTextureUnit() {
+      return OpenGlHelper.defaultTexUnit + activeTextureUnit;
+   }
+
+   public static void bindCurrentTexture() {
+      GL11.glBindTexture(3553, textureState[activeTextureUnit].textureName);
+   }
+
+   public static int getBoundTexture() {
+      return textureState[activeTextureUnit].textureName;
+   }
+
+   public static void checkBoundTexture() {
+      if (Config.isMinecraftThread()) {
+         int var0 = GL11.glGetInteger(34016);
+         int var1 = GL11.glGetInteger(32873);
+         int var2 = getActiveTextureUnit();
+         int var3 = getBoundTexture();
+         if (var3 > 0) {
+            if (var0 != var2 || var1 != var3) {
+               Config.dbg("checkTexture: act: " + var2 + ", glAct: " + var0 + ", tex: " + var3 + ", glTex: " + var1);
+            }
+         }
+      }
+   }
+
+   public static void deleteTextures(IntBuffer var0) {
+      ((Buffer)var0).rewind();
+
+      while (var0.position() < var0.limit()) {
+         int var1 = var0.get();
+         deleteTexture(var1);
+      }
+
+      ((Buffer)var0).rewind();
+   }
+
+   public static boolean isFogEnabled() {
+      return fogState.fog.currentState;
+   }
+
+   public static void setFogEnabled(boolean var0) {
+      fogState.fog.setState(var0);
+   }
+
+   public static void lockAlpha(GlAlphaState var0) {
+      if (!alphaLock.isLocked()) {
+         getAlphaState(alphaLockState);
+         setAlphaState(var0);
+         alphaLock.lock();
+      }
+   }
+
+   public static void unlockAlpha() {
+      if (alphaLock.unlock()) {
+         setAlphaState(alphaLockState);
+      }
+   }
+
+   public static void getAlphaState(GlAlphaState var0) {
+      if (alphaLock.isLocked()) {
+         var0.setState(alphaLockState);
+      } else {
+         var0.setState(alphaState.alphaTest.currentState, alphaState.func, alphaState.ref);
+      }
+   }
+
+   public static void setAlphaState(GlAlphaState var0) {
+      if (alphaLock.isLocked()) {
+         alphaLockState.setState(var0);
+      } else {
+         alphaState.alphaTest.setState(var0.isEnabled());
+         alphaFunc(var0.getFunc(), var0.getRef());
+      }
+   }
+
+   public static void lockBlend(GlBlendState var0) {
+      if (!blendLock.isLocked()) {
+         getBlendState(blendLockState);
+         setBlendState(var0);
+         blendLock.lock();
+      }
+   }
+
+   public static void unlockBlend() {
+      if (blendLock.unlock()) {
+         setBlendState(blendLockState);
+      }
+   }
+
+   public static void getBlendState(GlBlendState var0) {
+      if (blendLock.isLocked()) {
+         var0.setState(blendLockState);
+      } else {
+         var0.setState(blendState.blend.currentState, blendState.srcFactor, blendState.dstFactor, blendState.srcFactorAlpha, blendState.dstFactorAlpha);
+      }
+   }
+
+   public static void setBlendState(GlBlendState var0) {
+      if (blendLock.isLocked()) {
+         blendLockState.setState(var0);
+      } else {
+         blendState.blend.setState(var0.isEnabled());
+         if (!var0.isSeparate()) {
+            blendFunc(var0.getSrcFactor(), var0.getDstFactor());
+         } else {
+            tryBlendFuncSeparate(var0.getSrcFactor(), var0.getDstFactor(), var0.getSrcFactorAlpha(), var0.getDstFactorAlpha());
+         }
+      }
+   }
+
+   public static void glMultiDrawArrays(int var0, IntBuffer var1, IntBuffer var2) {
+      GL14.glMultiDrawArrays(var0, var1, var2);
+      if (Config.isShaders() && !creatingDisplayList) {
+         int var3 = Shaders.activeProgram.getCountInstances();
+         if (var3 > 1) {
+            for (int var4 = 1; var4 < var3; var4++) {
+               Shaders.uniform_instanceId.setValue(var4);
+               GL14.glMultiDrawArrays(var0, var1, var2);
+            }
+
+            Shaders.uniform_instanceId.setValue(0);
+         }
+      }
+   }
+
    static {
       for (int var0 = 0; var0 < 8; var0++) {
          lightState[var0] = new GlStateManager.BooleanState(16384 + var0);
       }

       colorMaterialState = new GlStateManager.ColorMaterialState();
@@ -694,15 +925,15 @@
       polygonOffsetState = new GlStateManager.PolygonOffsetState();
       colorLogicState = new GlStateManager.ColorLogicState();
       texGenState = new GlStateManager.TexGenState();
       clearState = new GlStateManager.ClearState();
       stencilState = new GlStateManager.StencilState();
       normalizeState = new GlStateManager.BooleanState(2977);
-      textureState = new GlStateManager.TextureState[8];
+      textureState = new GlStateManager.TextureState[32];

-      for (int var1 = 0; var1 < 8; var1++) {
+      for (int var1 = 0; var1 < textureState.length; var1++) {
          textureState[var1] = new GlStateManager.TextureState();
       }

       activeShadeModel = 7425;
       rescaleNormalState = new GlStateManager.BooleanState(32826);
       colorMaskState = new GlStateManager.ColorMask();
@@ -923,24 +1154,24 @@
    public static enum Profile {
       DEFAULT {
          public void apply() {
             GlStateManager.disableAlpha();
             GlStateManager.alphaFunc(519, 0.0F);
             GlStateManager.disableLighting();
-            GlStateManager.glLightModel(2899, RenderHelper.setColorBuffer(0.2F, 0.2F, 0.2F, 1.0F));
+            GL11.glLightModel(2899, RenderHelper.setColorBuffer(0.2F, 0.2F, 0.2F, 1.0F));

             for (int var1 = 0; var1 < 8; var1++) {
                GlStateManager.disableLight(var1);
-               GlStateManager.glLight(16384 + var1, 4608, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
-               GlStateManager.glLight(16384 + var1, 4611, RenderHelper.setColorBuffer(0.0F, 0.0F, 1.0F, 0.0F));
+               GL11.glLight(16384 + var1, 4608, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
+               GL11.glLight(16384 + var1, 4611, RenderHelper.setColorBuffer(0.0F, 0.0F, 1.0F, 0.0F));
                if (var1 == 0) {
-                  GlStateManager.glLight(16384 + var1, 4609, RenderHelper.setColorBuffer(1.0F, 1.0F, 1.0F, 1.0F));
-                  GlStateManager.glLight(16384 + var1, 4610, RenderHelper.setColorBuffer(1.0F, 1.0F, 1.0F, 1.0F));
+                  GL11.glLight(16384 + var1, 4609, RenderHelper.setColorBuffer(1.0F, 1.0F, 1.0F, 1.0F));
+                  GL11.glLight(16384 + var1, 4610, RenderHelper.setColorBuffer(1.0F, 1.0F, 1.0F, 1.0F));
                } else {
-                  GlStateManager.glLight(16384 + var1, 4609, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
-                  GlStateManager.glLight(16384 + var1, 4610, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
+                  GL11.glLight(16384 + var1, 4609, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
+                  GL11.glLight(16384 + var1, 4610, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
                }
             }

             GlStateManager.disableColorMaterial();
             GlStateManager.colorMaterial(1032, 5634);
             GlStateManager.disableDepth();
@@ -948,21 +1179,21 @@
             GlStateManager.depthMask(true);
             GlStateManager.disableBlend();
             GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
             GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
             );
-            GlStateManager.glBlendEquation(32774);
+            GL14.glBlendEquation(32774);
             GlStateManager.disableFog();
-            GlStateManager.glFogi(2917, 2048);
+            GL11.glFogi(2917, 2048);
             GlStateManager.setFogDensity(1.0F);
             GlStateManager.setFogStart(0.0F);
             GlStateManager.setFogEnd(1.0F);
-            GlStateManager.glFog(2918, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
+            GL11.glFog(2918, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
             if (GLContext.getCapabilities().GL_NV_fog_distance) {
-               GlStateManager.glFogi(2917, 34140);
+               GL11.glFogi(2917, 34140);
             }

             GlStateManager.doPolygonOffset(0.0F, 0.0F);
             GlStateManager.disableColorLogic();
             GlStateManager.colorLogicOp(5379);
             GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
@@ -979,47 +1210,47 @@
             GlStateManager.texGen(GlStateManager.TexGen.R, 9217, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
             GlStateManager.disableTexGenCoord(GlStateManager.TexGen.Q);
             GlStateManager.texGen(GlStateManager.TexGen.Q, 9216);
             GlStateManager.texGen(GlStateManager.TexGen.Q, 9474, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
             GlStateManager.texGen(GlStateManager.TexGen.Q, 9217, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
             GlStateManager.setActiveTexture(0);
-            GlStateManager.glTexParameteri(3553, 10240, 9729);
-            GlStateManager.glTexParameteri(3553, 10241, 9986);
-            GlStateManager.glTexParameteri(3553, 10242, 10497);
-            GlStateManager.glTexParameteri(3553, 10243, 10497);
-            GlStateManager.glTexParameteri(3553, 33085, 1000);
-            GlStateManager.glTexParameteri(3553, 33083, 1000);
-            GlStateManager.glTexParameteri(3553, 33082, -1000);
-            GlStateManager.glTexParameterf(3553, 34049, 0.0F);
-            GlStateManager.glTexEnvi(8960, 8704, 8448);
-            GlStateManager.glTexEnv(8960, 8705, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
-            GlStateManager.glTexEnvi(8960, 34161, 8448);
-            GlStateManager.glTexEnvi(8960, 34162, 8448);
-            GlStateManager.glTexEnvi(8960, 34176, 5890);
-            GlStateManager.glTexEnvi(8960, 34177, 34168);
-            GlStateManager.glTexEnvi(8960, 34178, 34166);
-            GlStateManager.glTexEnvi(8960, 34184, 5890);
-            GlStateManager.glTexEnvi(8960, 34185, 34168);
-            GlStateManager.glTexEnvi(8960, 34186, 34166);
-            GlStateManager.glTexEnvi(8960, 34192, 768);
-            GlStateManager.glTexEnvi(8960, 34193, 768);
-            GlStateManager.glTexEnvi(8960, 34194, 770);
-            GlStateManager.glTexEnvi(8960, 34200, 770);
-            GlStateManager.glTexEnvi(8960, 34201, 770);
-            GlStateManager.glTexEnvi(8960, 34202, 770);
-            GlStateManager.glTexEnvf(8960, 34163, 1.0F);
-            GlStateManager.glTexEnvf(8960, 3356, 1.0F);
+            GL11.glTexParameteri(3553, 10240, 9729);
+            GL11.glTexParameteri(3553, 10241, 9986);
+            GL11.glTexParameteri(3553, 10242, 10497);
+            GL11.glTexParameteri(3553, 10243, 10497);
+            GL11.glTexParameteri(3553, 33085, 1000);
+            GL11.glTexParameteri(3553, 33083, 1000);
+            GL11.glTexParameteri(3553, 33082, -1000);
+            GL11.glTexParameterf(3553, 34049, 0.0F);
+            GL11.glTexEnvi(8960, 8704, 8448);
+            GL11.glTexEnv(8960, 8705, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 0.0F));
+            GL11.glTexEnvi(8960, 34161, 8448);
+            GL11.glTexEnvi(8960, 34162, 8448);
+            GL11.glTexEnvi(8960, 34176, 5890);
+            GL11.glTexEnvi(8960, 34177, 34168);
+            GL11.glTexEnvi(8960, 34178, 34166);
+            GL11.glTexEnvi(8960, 34184, 5890);
+            GL11.glTexEnvi(8960, 34185, 34168);
+            GL11.glTexEnvi(8960, 34186, 34166);
+            GL11.glTexEnvi(8960, 34192, 768);
+            GL11.glTexEnvi(8960, 34193, 768);
+            GL11.glTexEnvi(8960, 34194, 770);
+            GL11.glTexEnvi(8960, 34200, 770);
+            GL11.glTexEnvi(8960, 34201, 770);
+            GL11.glTexEnvi(8960, 34202, 770);
+            GL11.glTexEnvf(8960, 34163, 1.0F);
+            GL11.glTexEnvf(8960, 3356, 1.0F);
             GlStateManager.disableNormalize();
             GlStateManager.shadeModel(7425);
             GlStateManager.disableRescaleNormal();
             GlStateManager.colorMask(true, true, true, true);
             GlStateManager.clearDepth(1.0);
-            GlStateManager.glLineWidth(1.0F);
-            GlStateManager.glNormal3f(0.0F, 0.0F, 1.0F);
-            GlStateManager.glPolygonMode(1028, 6914);
-            GlStateManager.glPolygonMode(1029, 6914);
+            GL11.glLineWidth(1.0F);
+            GL11.glNormal3f(0.0F, 0.0F, 1.0F);
+            GL11.glPolygonMode(1028, 6914);
+            GL11.glPolygonMode(1029, 6914);
          }

          public void clean() {
          }
       },
       PLAYER_SKIN {
 */
