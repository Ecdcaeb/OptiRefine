package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

public class MixinOpenGlHelper {
}
/*
+++ net/minecraft/client/renderer/OpenGlHelper.java	Tue Aug 19 14:59:58 2025
@@ -3,20 +3,22 @@
 import java.io.File;
 import java.io.IOException;
 import java.net.URI;
 import java.nio.ByteBuffer;
 import java.nio.FloatBuffer;
 import java.nio.IntBuffer;
+import java.util.ArrayList;
 import java.util.Locale;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.client.settings.GameSettings;
 import net.minecraft.util.Util;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;
 import org.lwjgl.Sys;
+import org.lwjgl.opengl.ARBCopyBuffer;
 import org.lwjgl.opengl.ARBFramebufferObject;
 import org.lwjgl.opengl.ARBMultitexture;
 import org.lwjgl.opengl.ARBShaderObjects;
 import org.lwjgl.opengl.ARBVertexBufferObject;
 import org.lwjgl.opengl.ARBVertexShader;
 import org.lwjgl.opengl.ContextCapabilities;
@@ -25,12 +27,13 @@
 import org.lwjgl.opengl.GL11;
 import org.lwjgl.opengl.GL13;
 import org.lwjgl.opengl.GL14;
 import org.lwjgl.opengl.GL15;
 import org.lwjgl.opengl.GL20;
 import org.lwjgl.opengl.GL30;
+import org.lwjgl.opengl.GL31;
 import org.lwjgl.opengl.GLContext;
 import oshi.SystemInfo;
 import oshi.hardware.Processor;

 public class OpenGlHelper {
    private static final Logger LOGGER = LogManager.getLogger();
@@ -85,17 +88,53 @@
    private static String cpu;
    public static boolean vboSupported;
    public static boolean vboSupportedAti;
    private static boolean arbVbo;
    public static int GL_ARRAY_BUFFER;
    public static int GL_STATIC_DRAW;
+   public static float lastBrightnessX = 0.0F;
+   public static float lastBrightnessY = 0.0F;
+   public static boolean openGL31;
+   public static boolean vboRegions;
+   public static int GL_COPY_READ_BUFFER;
+   public static int GL_COPY_WRITE_BUFFER;
+   public static final int GL_QUADS = 7;
+   public static final int GL_TRIANGLES = 4;

    public static void initializeTextures() {
+      Config.initDisplay();
       ContextCapabilities var0 = GLContext.getCapabilities();
       arbMultitexture = var0.GL_ARB_multitexture && !var0.OpenGL13;
       arbTextureEnvCombine = var0.GL_ARB_texture_env_combine && !var0.OpenGL13;
+      openGL31 = var0.OpenGL31;
+      if (openGL31) {
+         GL_COPY_READ_BUFFER = 36662;
+         GL_COPY_WRITE_BUFFER = 36663;
+      } else {
+         GL_COPY_READ_BUFFER = 36662;
+         GL_COPY_WRITE_BUFFER = 36663;
+      }
+
+      boolean var1 = openGL31 || var0.GL_ARB_copy_buffer;
+      boolean var2 = var0.OpenGL14;
+      vboRegions = var1 && var2;
+      if (!vboRegions) {
+         ArrayList var3 = new ArrayList();
+         if (!var1) {
+            var3.add("OpenGL 1.3, ARB_copy_buffer");
+         }
+
+         if (!var2) {
+            var3.add("OpenGL 1.4");
+         }
+
+         String var4 = "VboRegions not supported, missing: " + Config.listToString(var3);
+         Config.dbg(var4);
+         logText = logText + var4 + "\n";
+      }
+
       if (arbMultitexture) {
          logText = logText + "Using ARB_multitexture.\n";
          defaultTexUnit = 33984;
          lightmapTexUnit = 33985;
          GL_TEXTURE2 = 33986;
       } else {
@@ -224,14 +263,14 @@
          logText = logText + "ARB_shader_objects is " + (var0.GL_ARB_shader_objects ? "" : "not ") + "supported, ";
          logText = logText + "ARB_vertex_shader is " + (var0.GL_ARB_vertex_shader ? "" : "not ") + "supported, and ";
          logText = logText + "ARB_fragment_shader is " + (var0.GL_ARB_fragment_shader ? "" : "not ") + "supported.\n";
       }

       shadersSupported = framebufferSupported && shadersAvailable;
-      String var1 = GL11.glGetString(7936).toLowerCase(Locale.ROOT);
-      nvidia = var1.contains("nvidia");
+      String var6 = GL11.glGetString(7936).toLowerCase(Locale.ROOT);
+      nvidia = var6.contains("nvidia");
       arbVbo = !var0.OpenGL15 && var0.GL_ARB_vertex_buffer_object;
       vboSupported = var0.OpenGL15 || arbVbo;
       logText = logText + "VBOs are " + (vboSupported ? "" : "not ") + "available because ";
       if (vboSupported) {
          if (arbVbo) {
             logText = logText + "ARB_vertex_buffer_object is supported.\n";
@@ -241,25 +280,25 @@
             logText = logText + "OpenGL 1.5 is supported.\n";
             GL_STATIC_DRAW = 35044;
             GL_ARRAY_BUFFER = 34962;
          }
       }

-      ati = var1.contains("ati");
+      ati = var6.contains("ati");
       if (ati) {
          if (vboSupported) {
             vboSupportedAti = true;
          } else {
             GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0F);
          }
       }

       try {
-         Processor[] var2 = new SystemInfo().getHardware().getProcessors();
-         cpu = String.format("%dx %s", var2.length, var2[0]).replaceAll("\\s+", " ");
-      } catch (Throwable var3) {
+         Processor[] var7 = new SystemInfo().getHardware().getProcessors();
+         cpu = String.format("%dx %s", var7.length, var7[0]).replaceAll("\\s+", " ");
+      } catch (Throwable var5) {
       }
    }

    public static boolean areShadersSupported() {
       return shadersSupported;
    }
@@ -478,13 +517,17 @@
       } else {
          GL15.glDeleteBuffers(var0);
       }
    }

    public static boolean useVbo() {
-      return vboSupported && Minecraft.getMinecraft().gameSettings.useVbo;
+      if (Config.isMultiTexture()) {
+         return false;
+      } else {
+         return Config.isRenderRegions() && !vboRegions ? false : vboSupported && Minecraft.getMinecraft().gameSettings.useVbo;
+      }
    }

    public static void glBindFramebuffer(int var0, int var1) {
       if (framebufferSupported) {
          switch (framebufferType) {
             case BASE:
@@ -659,12 +702,17 @@
    public static void setLightmapTextureCoords(int var0, float var1, float var2) {
       if (arbMultitexture) {
          ARBMultitexture.glMultiTexCoord2fARB(var0, var1, var2);
       } else {
          GL13.glMultiTexCoord2f(var0, var1, var2);
       }
+
+      if (var0 == lightmapTexUnit) {
+         lastBrightnessX = var1;
+         lastBrightnessY = var2;
+      }
    }

    public static void glBlendFunc(int var0, int var1, int var2, int var3) {
       if (openGL14) {
          if (extBlendFuncSeparate) {
             EXTBlendFuncSeparate.glBlendFuncSeparateEXT(var0, var1, var2, var3);
@@ -674,13 +722,41 @@
       } else {
          GL11.glBlendFunc(var0, var1);
       }
    }

    public static boolean isFramebufferEnabled() {
-      return framebufferSupported && Minecraft.getMinecraft().gameSettings.fboEnable;
+      if (Config.isFastRender()) {
+         return false;
+      } else {
+         return Config.isAntialiasing() ? false : framebufferSupported && Minecraft.getMinecraft().gameSettings.fboEnable;
+      }
+   }
+
+   public static void glBufferData(int var0, long var1, int var3) {
+      if (arbVbo) {
+         ARBVertexBufferObject.glBufferDataARB(var0, var1, var3);
+      } else {
+         GL15.glBufferData(var0, var1, var3);
+      }
+   }
+
+   public static void glBufferSubData(int var0, long var1, ByteBuffer var3) {
+      if (arbVbo) {
+         ARBVertexBufferObject.glBufferSubDataARB(var0, var1, var3);
+      } else {
+         GL15.glBufferSubData(var0, var1, var3);
+      }
+   }
+
+   public static void glCopyBufferSubData(int var0, int var1, long var2, long var4, long var6) {
+      if (openGL31) {
+         GL31.glCopyBufferSubData(var0, var1, var2, var4, var6);
+      } else {
+         ARBCopyBuffer.glCopyBufferSubData(var0, var1, var2, var4, var6);
+      }
    }

    public static String getCpu() {
       return cpu == null ? "<unknown>" : cpu;
    }

 */
