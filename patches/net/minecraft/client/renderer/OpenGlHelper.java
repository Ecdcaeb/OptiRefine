package net.minecraft.client.renderer;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.EXTBlendFuncSeparate;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import oshi.SystemInfo;
import oshi.hardware.Processor;

public class OpenGlHelper {
   private static final Logger LOGGER = LogManager.getLogger();
   public static boolean nvidia;
   public static boolean ati;
   public static int GL_FRAMEBUFFER;
   public static int GL_RENDERBUFFER;
   public static int GL_COLOR_ATTACHMENT0;
   public static int GL_DEPTH_ATTACHMENT;
   public static int GL_FRAMEBUFFER_COMPLETE;
   public static int GL_FB_INCOMPLETE_ATTACHMENT;
   public static int GL_FB_INCOMPLETE_MISS_ATTACH;
   public static int GL_FB_INCOMPLETE_DRAW_BUFFER;
   public static int GL_FB_INCOMPLETE_READ_BUFFER;
   private static OpenGlHelper.FboMode framebufferType;
   public static boolean framebufferSupported;
   private static boolean shadersAvailable;
   private static boolean arbShaders;
   public static int GL_LINK_STATUS;
   public static int GL_COMPILE_STATUS;
   public static int GL_VERTEX_SHADER;
   public static int GL_FRAGMENT_SHADER;
   private static boolean arbMultitexture;
   public static int defaultTexUnit;
   public static int lightmapTexUnit;
   public static int GL_TEXTURE2;
   private static boolean arbTextureEnvCombine;
   public static int GL_COMBINE;
   public static int GL_INTERPOLATE;
   public static int GL_PRIMARY_COLOR;
   public static int GL_CONSTANT;
   public static int GL_PREVIOUS;
   public static int GL_COMBINE_RGB;
   public static int GL_SOURCE0_RGB;
   public static int GL_SOURCE1_RGB;
   public static int GL_SOURCE2_RGB;
   public static int GL_OPERAND0_RGB;
   public static int GL_OPERAND1_RGB;
   public static int GL_OPERAND2_RGB;
   public static int GL_COMBINE_ALPHA;
   public static int GL_SOURCE0_ALPHA;
   public static int GL_SOURCE1_ALPHA;
   public static int GL_SOURCE2_ALPHA;
   public static int GL_OPERAND0_ALPHA;
   public static int GL_OPERAND1_ALPHA;
   public static int GL_OPERAND2_ALPHA;
   private static boolean openGL14;
   public static boolean extBlendFuncSeparate;
   public static boolean openGL21;
   public static boolean shadersSupported;
   private static String logText = "";
   private static String cpu;
   public static boolean vboSupported;
   public static boolean vboSupportedAti;
   private static boolean arbVbo;
   public static int GL_ARRAY_BUFFER;
   public static int GL_STATIC_DRAW;

   public static void initializeTextures() {
      ContextCapabilities ☃ = GLContext.getCapabilities();
      arbMultitexture = ☃.GL_ARB_multitexture && !☃.OpenGL13;
      arbTextureEnvCombine = ☃.GL_ARB_texture_env_combine && !☃.OpenGL13;
      if (arbMultitexture) {
         logText = logText + "Using ARB_multitexture.\n";
         defaultTexUnit = 33984;
         lightmapTexUnit = 33985;
         GL_TEXTURE2 = 33986;
      } else {
         logText = logText + "Using GL 1.3 multitexturing.\n";
         defaultTexUnit = 33984;
         lightmapTexUnit = 33985;
         GL_TEXTURE2 = 33986;
      }

      if (arbTextureEnvCombine) {
         logText = logText + "Using ARB_texture_env_combine.\n";
         GL_COMBINE = 34160;
         GL_INTERPOLATE = 34165;
         GL_PRIMARY_COLOR = 34167;
         GL_CONSTANT = 34166;
         GL_PREVIOUS = 34168;
         GL_COMBINE_RGB = 34161;
         GL_SOURCE0_RGB = 34176;
         GL_SOURCE1_RGB = 34177;
         GL_SOURCE2_RGB = 34178;
         GL_OPERAND0_RGB = 34192;
         GL_OPERAND1_RGB = 34193;
         GL_OPERAND2_RGB = 34194;
         GL_COMBINE_ALPHA = 34162;
         GL_SOURCE0_ALPHA = 34184;
         GL_SOURCE1_ALPHA = 34185;
         GL_SOURCE2_ALPHA = 34186;
         GL_OPERAND0_ALPHA = 34200;
         GL_OPERAND1_ALPHA = 34201;
         GL_OPERAND2_ALPHA = 34202;
      } else {
         logText = logText + "Using GL 1.3 texture combiners.\n";
         GL_COMBINE = 34160;
         GL_INTERPOLATE = 34165;
         GL_PRIMARY_COLOR = 34167;
         GL_CONSTANT = 34166;
         GL_PREVIOUS = 34168;
         GL_COMBINE_RGB = 34161;
         GL_SOURCE0_RGB = 34176;
         GL_SOURCE1_RGB = 34177;
         GL_SOURCE2_RGB = 34178;
         GL_OPERAND0_RGB = 34192;
         GL_OPERAND1_RGB = 34193;
         GL_OPERAND2_RGB = 34194;
         GL_COMBINE_ALPHA = 34162;
         GL_SOURCE0_ALPHA = 34184;
         GL_SOURCE1_ALPHA = 34185;
         GL_SOURCE2_ALPHA = 34186;
         GL_OPERAND0_ALPHA = 34200;
         GL_OPERAND1_ALPHA = 34201;
         GL_OPERAND2_ALPHA = 34202;
      }

      extBlendFuncSeparate = ☃.GL_EXT_blend_func_separate && !☃.OpenGL14;
      openGL14 = ☃.OpenGL14 || ☃.GL_EXT_blend_func_separate;
      framebufferSupported = openGL14 && (☃.GL_ARB_framebuffer_object || ☃.GL_EXT_framebuffer_object || ☃.OpenGL30);
      if (framebufferSupported) {
         logText = logText + "Using framebuffer objects because ";
         if (☃.OpenGL30) {
            logText = logText + "OpenGL 3.0 is supported and separate blending is supported.\n";
            framebufferType = OpenGlHelper.FboMode.BASE;
            GL_FRAMEBUFFER = 36160;
            GL_RENDERBUFFER = 36161;
            GL_COLOR_ATTACHMENT0 = 36064;
            GL_DEPTH_ATTACHMENT = 36096;
            GL_FRAMEBUFFER_COMPLETE = 36053;
            GL_FB_INCOMPLETE_ATTACHMENT = 36054;
            GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
            GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
            GL_FB_INCOMPLETE_READ_BUFFER = 36060;
         } else if (☃.GL_ARB_framebuffer_object) {
            logText = logText + "ARB_framebuffer_object is supported and separate blending is supported.\n";
            framebufferType = OpenGlHelper.FboMode.ARB;
            GL_FRAMEBUFFER = 36160;
            GL_RENDERBUFFER = 36161;
            GL_COLOR_ATTACHMENT0 = 36064;
            GL_DEPTH_ATTACHMENT = 36096;
            GL_FRAMEBUFFER_COMPLETE = 36053;
            GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
            GL_FB_INCOMPLETE_ATTACHMENT = 36054;
            GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
            GL_FB_INCOMPLETE_READ_BUFFER = 36060;
         } else if (☃.GL_EXT_framebuffer_object) {
            logText = logText + "EXT_framebuffer_object is supported.\n";
            framebufferType = OpenGlHelper.FboMode.EXT;
            GL_FRAMEBUFFER = 36160;
            GL_RENDERBUFFER = 36161;
            GL_COLOR_ATTACHMENT0 = 36064;
            GL_DEPTH_ATTACHMENT = 36096;
            GL_FRAMEBUFFER_COMPLETE = 36053;
            GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
            GL_FB_INCOMPLETE_ATTACHMENT = 36054;
            GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
            GL_FB_INCOMPLETE_READ_BUFFER = 36060;
         }
      } else {
         logText = logText + "Not using framebuffer objects because ";
         logText = logText + "OpenGL 1.4 is " + (☃.OpenGL14 ? "" : "not ") + "supported, ";
         logText = logText + "EXT_blend_func_separate is " + (☃.GL_EXT_blend_func_separate ? "" : "not ") + "supported, ";
         logText = logText + "OpenGL 3.0 is " + (☃.OpenGL30 ? "" : "not ") + "supported, ";
         logText = logText + "ARB_framebuffer_object is " + (☃.GL_ARB_framebuffer_object ? "" : "not ") + "supported, and ";
         logText = logText + "EXT_framebuffer_object is " + (☃.GL_EXT_framebuffer_object ? "" : "not ") + "supported.\n";
      }

      openGL21 = ☃.OpenGL21;
      shadersAvailable = openGL21 || ☃.GL_ARB_vertex_shader && ☃.GL_ARB_fragment_shader && ☃.GL_ARB_shader_objects;
      logText = logText + "Shaders are " + (shadersAvailable ? "" : "not ") + "available because ";
      if (shadersAvailable) {
         if (☃.OpenGL21) {
            logText = logText + "OpenGL 2.1 is supported.\n";
            arbShaders = false;
            GL_LINK_STATUS = 35714;
            GL_COMPILE_STATUS = 35713;
            GL_VERTEX_SHADER = 35633;
            GL_FRAGMENT_SHADER = 35632;
         } else {
            logText = logText + "ARB_shader_objects, ARB_vertex_shader, and ARB_fragment_shader are supported.\n";
            arbShaders = true;
            GL_LINK_STATUS = 35714;
            GL_COMPILE_STATUS = 35713;
            GL_VERTEX_SHADER = 35633;
            GL_FRAGMENT_SHADER = 35632;
         }
      } else {
         logText = logText + "OpenGL 2.1 is " + (☃.OpenGL21 ? "" : "not ") + "supported, ";
         logText = logText + "ARB_shader_objects is " + (☃.GL_ARB_shader_objects ? "" : "not ") + "supported, ";
         logText = logText + "ARB_vertex_shader is " + (☃.GL_ARB_vertex_shader ? "" : "not ") + "supported, and ";
         logText = logText + "ARB_fragment_shader is " + (☃.GL_ARB_fragment_shader ? "" : "not ") + "supported.\n";
      }

      shadersSupported = framebufferSupported && shadersAvailable;
      String ☃x = GL11.glGetString(7936).toLowerCase(Locale.ROOT);
      nvidia = ☃x.contains("nvidia");
      arbVbo = !☃.OpenGL15 && ☃.GL_ARB_vertex_buffer_object;
      vboSupported = ☃.OpenGL15 || arbVbo;
      logText = logText + "VBOs are " + (vboSupported ? "" : "not ") + "available because ";
      if (vboSupported) {
         if (arbVbo) {
            logText = logText + "ARB_vertex_buffer_object is supported.\n";
            GL_STATIC_DRAW = 35044;
            GL_ARRAY_BUFFER = 34962;
         } else {
            logText = logText + "OpenGL 1.5 is supported.\n";
            GL_STATIC_DRAW = 35044;
            GL_ARRAY_BUFFER = 34962;
         }
      }

      ati = ☃x.contains("ati");
      if (ati) {
         if (vboSupported) {
            vboSupportedAti = true;
         } else {
            GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0F);
         }
      }

      try {
         Processor[] ☃xx = new SystemInfo().getHardware().getProcessors();
         cpu = String.format("%dx %s", ☃xx.length, ☃xx[0]).replaceAll("\\s+", " ");
      } catch (Throwable var3) {
      }
   }

   public static boolean areShadersSupported() {
      return shadersSupported;
   }

   public static String getLogText() {
      return logText;
   }

   public static int glGetProgrami(int var0, int var1) {
      return arbShaders ? ARBShaderObjects.glGetObjectParameteriARB(☃, ☃) : GL20.glGetProgrami(☃, ☃);
   }

   public static void glAttachShader(int var0, int var1) {
      if (arbShaders) {
         ARBShaderObjects.glAttachObjectARB(☃, ☃);
      } else {
         GL20.glAttachShader(☃, ☃);
      }
   }

   public static void glDeleteShader(int var0) {
      if (arbShaders) {
         ARBShaderObjects.glDeleteObjectARB(☃);
      } else {
         GL20.glDeleteShader(☃);
      }
   }

   public static int glCreateShader(int var0) {
      return arbShaders ? ARBShaderObjects.glCreateShaderObjectARB(☃) : GL20.glCreateShader(☃);
   }

   public static void glShaderSource(int var0, ByteBuffer var1) {
      if (arbShaders) {
         ARBShaderObjects.glShaderSourceARB(☃, ☃);
      } else {
         GL20.glShaderSource(☃, ☃);
      }
   }

   public static void glCompileShader(int var0) {
      if (arbShaders) {
         ARBShaderObjects.glCompileShaderARB(☃);
      } else {
         GL20.glCompileShader(☃);
      }
   }

   public static int glGetShaderi(int var0, int var1) {
      return arbShaders ? ARBShaderObjects.glGetObjectParameteriARB(☃, ☃) : GL20.glGetShaderi(☃, ☃);
   }

   public static String glGetShaderInfoLog(int var0, int var1) {
      return arbShaders ? ARBShaderObjects.glGetInfoLogARB(☃, ☃) : GL20.glGetShaderInfoLog(☃, ☃);
   }

   public static String glGetProgramInfoLog(int var0, int var1) {
      return arbShaders ? ARBShaderObjects.glGetInfoLogARB(☃, ☃) : GL20.glGetProgramInfoLog(☃, ☃);
   }

   public static void glUseProgram(int var0) {
      if (arbShaders) {
         ARBShaderObjects.glUseProgramObjectARB(☃);
      } else {
         GL20.glUseProgram(☃);
      }
   }

   public static int glCreateProgram() {
      return arbShaders ? ARBShaderObjects.glCreateProgramObjectARB() : GL20.glCreateProgram();
   }

   public static void glDeleteProgram(int var0) {
      if (arbShaders) {
         ARBShaderObjects.glDeleteObjectARB(☃);
      } else {
         GL20.glDeleteProgram(☃);
      }
   }

   public static void glLinkProgram(int var0) {
      if (arbShaders) {
         ARBShaderObjects.glLinkProgramARB(☃);
      } else {
         GL20.glLinkProgram(☃);
      }
   }

   public static int glGetUniformLocation(int var0, CharSequence var1) {
      return arbShaders ? ARBShaderObjects.glGetUniformLocationARB(☃, ☃) : GL20.glGetUniformLocation(☃, ☃);
   }

   public static void glUniform1(int var0, IntBuffer var1) {
      if (arbShaders) {
         ARBShaderObjects.glUniform1ARB(☃, ☃);
      } else {
         GL20.glUniform1(☃, ☃);
      }
   }

   public static void glUniform1i(int var0, int var1) {
      if (arbShaders) {
         ARBShaderObjects.glUniform1iARB(☃, ☃);
      } else {
         GL20.glUniform1i(☃, ☃);
      }
   }

   public static void glUniform1(int var0, FloatBuffer var1) {
      if (arbShaders) {
         ARBShaderObjects.glUniform1ARB(☃, ☃);
      } else {
         GL20.glUniform1(☃, ☃);
      }
   }

   public static void glUniform2(int var0, IntBuffer var1) {
      if (arbShaders) {
         ARBShaderObjects.glUniform2ARB(☃, ☃);
      } else {
         GL20.glUniform2(☃, ☃);
      }
   }

   public static void glUniform2(int var0, FloatBuffer var1) {
      if (arbShaders) {
         ARBShaderObjects.glUniform2ARB(☃, ☃);
      } else {
         GL20.glUniform2(☃, ☃);
      }
   }

   public static void glUniform3(int var0, IntBuffer var1) {
      if (arbShaders) {
         ARBShaderObjects.glUniform3ARB(☃, ☃);
      } else {
         GL20.glUniform3(☃, ☃);
      }
   }

   public static void glUniform3(int var0, FloatBuffer var1) {
      if (arbShaders) {
         ARBShaderObjects.glUniform3ARB(☃, ☃);
      } else {
         GL20.glUniform3(☃, ☃);
      }
   }

   public static void glUniform4(int var0, IntBuffer var1) {
      if (arbShaders) {
         ARBShaderObjects.glUniform4ARB(☃, ☃);
      } else {
         GL20.glUniform4(☃, ☃);
      }
   }

   public static void glUniform4(int var0, FloatBuffer var1) {
      if (arbShaders) {
         ARBShaderObjects.glUniform4ARB(☃, ☃);
      } else {
         GL20.glUniform4(☃, ☃);
      }
   }

   public static void glUniformMatrix2(int var0, boolean var1, FloatBuffer var2) {
      if (arbShaders) {
         ARBShaderObjects.glUniformMatrix2ARB(☃, ☃, ☃);
      } else {
         GL20.glUniformMatrix2(☃, ☃, ☃);
      }
   }

   public static void glUniformMatrix3(int var0, boolean var1, FloatBuffer var2) {
      if (arbShaders) {
         ARBShaderObjects.glUniformMatrix3ARB(☃, ☃, ☃);
      } else {
         GL20.glUniformMatrix3(☃, ☃, ☃);
      }
   }

   public static void glUniformMatrix4(int var0, boolean var1, FloatBuffer var2) {
      if (arbShaders) {
         ARBShaderObjects.glUniformMatrix4ARB(☃, ☃, ☃);
      } else {
         GL20.glUniformMatrix4(☃, ☃, ☃);
      }
   }

   public static int glGetAttribLocation(int var0, CharSequence var1) {
      return arbShaders ? ARBVertexShader.glGetAttribLocationARB(☃, ☃) : GL20.glGetAttribLocation(☃, ☃);
   }

   public static int glGenBuffers() {
      return arbVbo ? ARBVertexBufferObject.glGenBuffersARB() : GL15.glGenBuffers();
   }

   public static void glBindBuffer(int var0, int var1) {
      if (arbVbo) {
         ARBVertexBufferObject.glBindBufferARB(☃, ☃);
      } else {
         GL15.glBindBuffer(☃, ☃);
      }
   }

   public static void glBufferData(int var0, ByteBuffer var1, int var2) {
      if (arbVbo) {
         ARBVertexBufferObject.glBufferDataARB(☃, ☃, ☃);
      } else {
         GL15.glBufferData(☃, ☃, ☃);
      }
   }

   public static void glDeleteBuffers(int var0) {
      if (arbVbo) {
         ARBVertexBufferObject.glDeleteBuffersARB(☃);
      } else {
         GL15.glDeleteBuffers(☃);
      }
   }

   public static boolean useVbo() {
      return vboSupported && Minecraft.getMinecraft().gameSettings.useVbo;
   }

   public static void glBindFramebuffer(int var0, int var1) {
      if (framebufferSupported) {
         switch (framebufferType) {
            case BASE:
               GL30.glBindFramebuffer(☃, ☃);
               break;
            case ARB:
               ARBFramebufferObject.glBindFramebuffer(☃, ☃);
               break;
            case EXT:
               EXTFramebufferObject.glBindFramebufferEXT(☃, ☃);
         }
      }
   }

   public static void glBindRenderbuffer(int var0, int var1) {
      if (framebufferSupported) {
         switch (framebufferType) {
            case BASE:
               GL30.glBindRenderbuffer(☃, ☃);
               break;
            case ARB:
               ARBFramebufferObject.glBindRenderbuffer(☃, ☃);
               break;
            case EXT:
               EXTFramebufferObject.glBindRenderbufferEXT(☃, ☃);
         }
      }
   }

   public static void glDeleteRenderbuffers(int var0) {
      if (framebufferSupported) {
         switch (framebufferType) {
            case BASE:
               GL30.glDeleteRenderbuffers(☃);
               break;
            case ARB:
               ARBFramebufferObject.glDeleteRenderbuffers(☃);
               break;
            case EXT:
               EXTFramebufferObject.glDeleteRenderbuffersEXT(☃);
         }
      }
   }

   public static void glDeleteFramebuffers(int var0) {
      if (framebufferSupported) {
         switch (framebufferType) {
            case BASE:
               GL30.glDeleteFramebuffers(☃);
               break;
            case ARB:
               ARBFramebufferObject.glDeleteFramebuffers(☃);
               break;
            case EXT:
               EXTFramebufferObject.glDeleteFramebuffersEXT(☃);
         }
      }
   }

   public static int glGenFramebuffers() {
      if (!framebufferSupported) {
         return -1;
      } else {
         switch (framebufferType) {
            case BASE:
               return GL30.glGenFramebuffers();
            case ARB:
               return ARBFramebufferObject.glGenFramebuffers();
            case EXT:
               return EXTFramebufferObject.glGenFramebuffersEXT();
            default:
               return -1;
         }
      }
   }

   public static int glGenRenderbuffers() {
      if (!framebufferSupported) {
         return -1;
      } else {
         switch (framebufferType) {
            case BASE:
               return GL30.glGenRenderbuffers();
            case ARB:
               return ARBFramebufferObject.glGenRenderbuffers();
            case EXT:
               return EXTFramebufferObject.glGenRenderbuffersEXT();
            default:
               return -1;
         }
      }
   }

   public static void glRenderbufferStorage(int var0, int var1, int var2, int var3) {
      if (framebufferSupported) {
         switch (framebufferType) {
            case BASE:
               GL30.glRenderbufferStorage(☃, ☃, ☃, ☃);
               break;
            case ARB:
               ARBFramebufferObject.glRenderbufferStorage(☃, ☃, ☃, ☃);
               break;
            case EXT:
               EXTFramebufferObject.glRenderbufferStorageEXT(☃, ☃, ☃, ☃);
         }
      }
   }

   public static void glFramebufferRenderbuffer(int var0, int var1, int var2, int var3) {
      if (framebufferSupported) {
         switch (framebufferType) {
            case BASE:
               GL30.glFramebufferRenderbuffer(☃, ☃, ☃, ☃);
               break;
            case ARB:
               ARBFramebufferObject.glFramebufferRenderbuffer(☃, ☃, ☃, ☃);
               break;
            case EXT:
               EXTFramebufferObject.glFramebufferRenderbufferEXT(☃, ☃, ☃, ☃);
         }
      }
   }

   public static int glCheckFramebufferStatus(int var0) {
      if (!framebufferSupported) {
         return -1;
      } else {
         switch (framebufferType) {
            case BASE:
               return GL30.glCheckFramebufferStatus(☃);
            case ARB:
               return ARBFramebufferObject.glCheckFramebufferStatus(☃);
            case EXT:
               return EXTFramebufferObject.glCheckFramebufferStatusEXT(☃);
            default:
               return -1;
         }
      }
   }

   public static void glFramebufferTexture2D(int var0, int var1, int var2, int var3, int var4) {
      if (framebufferSupported) {
         switch (framebufferType) {
            case BASE:
               GL30.glFramebufferTexture2D(☃, ☃, ☃, ☃, ☃);
               break;
            case ARB:
               ARBFramebufferObject.glFramebufferTexture2D(☃, ☃, ☃, ☃, ☃);
               break;
            case EXT:
               EXTFramebufferObject.glFramebufferTexture2DEXT(☃, ☃, ☃, ☃, ☃);
         }
      }
   }

   public static void setActiveTexture(int var0) {
      if (arbMultitexture) {
         ARBMultitexture.glActiveTextureARB(☃);
      } else {
         GL13.glActiveTexture(☃);
      }
   }

   public static void setClientActiveTexture(int var0) {
      if (arbMultitexture) {
         ARBMultitexture.glClientActiveTextureARB(☃);
      } else {
         GL13.glClientActiveTexture(☃);
      }
   }

   public static void setLightmapTextureCoords(int var0, float var1, float var2) {
      if (arbMultitexture) {
         ARBMultitexture.glMultiTexCoord2fARB(☃, ☃, ☃);
      } else {
         GL13.glMultiTexCoord2f(☃, ☃, ☃);
      }
   }

   public static void glBlendFunc(int var0, int var1, int var2, int var3) {
      if (openGL14) {
         if (extBlendFuncSeparate) {
            EXTBlendFuncSeparate.glBlendFuncSeparateEXT(☃, ☃, ☃, ☃);
         } else {
            GL14.glBlendFuncSeparate(☃, ☃, ☃, ☃);
         }
      } else {
         GL11.glBlendFunc(☃, ☃);
      }
   }

   public static boolean isFramebufferEnabled() {
      return framebufferSupported && Minecraft.getMinecraft().gameSettings.fboEnable;
   }

   public static String getCpu() {
      return cpu == null ? "<unknown>" : cpu;
   }

   public static void renderDirections(int var0) {
      GlStateManager.disableTexture2D();
      GlStateManager.depthMask(false);
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      GL11.glLineWidth(4.0F);
      ☃x.begin(1, DefaultVertexFormats.POSITION_COLOR);
      ☃x.pos(0.0, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
      ☃x.pos(☃, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
      ☃x.pos(0.0, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
      ☃x.pos(0.0, ☃, 0.0).color(0, 0, 0, 255).endVertex();
      ☃x.pos(0.0, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
      ☃x.pos(0.0, 0.0, ☃).color(0, 0, 0, 255).endVertex();
      ☃.draw();
      GL11.glLineWidth(2.0F);
      ☃x.begin(1, DefaultVertexFormats.POSITION_COLOR);
      ☃x.pos(0.0, 0.0, 0.0).color(255, 0, 0, 255).endVertex();
      ☃x.pos(☃, 0.0, 0.0).color(255, 0, 0, 255).endVertex();
      ☃x.pos(0.0, 0.0, 0.0).color(0, 255, 0, 255).endVertex();
      ☃x.pos(0.0, ☃, 0.0).color(0, 255, 0, 255).endVertex();
      ☃x.pos(0.0, 0.0, 0.0).color(127, 127, 255, 255).endVertex();
      ☃x.pos(0.0, 0.0, ☃).color(127, 127, 255, 255).endVertex();
      ☃.draw();
      GL11.glLineWidth(1.0F);
      GlStateManager.depthMask(true);
      GlStateManager.enableTexture2D();
   }

   public static void openFile(File var0) {
      String ☃ = ☃.getAbsolutePath();
      if (Util.getOSType() == Util.EnumOS.OSX) {
         try {
            LOGGER.info(☃);
            Runtime.getRuntime().exec(new String[]{"/usr/bin/open", ☃});
            return;
         } catch (IOException var7) {
            LOGGER.error("Couldn't open file", var7);
         }
      } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
         String ☃x = String.format("cmd.exe /C start \"Open file\" \"%s\"", ☃);

         try {
            Runtime.getRuntime().exec(☃x);
            return;
         } catch (IOException var6) {
            LOGGER.error("Couldn't open file", var6);
         }
      }

      boolean ☃x = false;

      try {
         Class<?> ☃xx = Class.forName("java.awt.Desktop");
         Object ☃xxx = ☃xx.getMethod("getDesktop").invoke(null);
         ☃xx.getMethod("browse", URI.class).invoke(☃xxx, ☃.toURI());
      } catch (Throwable var5) {
         LOGGER.error("Couldn't open link", var5);
         ☃x = true;
      }

      if (☃x) {
         LOGGER.info("Opening via system class!");
         Sys.openURL("file://" + ☃);
      }
   }

   static enum FboMode {
      BASE,
      ARB,
      EXT;
   }
}
