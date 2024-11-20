/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.io.File
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.Runtime
 *  java.lang.String
 *  java.lang.Throwable
 *  java.net.URI
 *  java.nio.ByteBuffer
 *  java.nio.FloatBuffer
 *  java.nio.IntBuffer
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.Locale
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper$1
 *  net.minecraft.client.renderer.OpenGlHelper$FboMode
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.settings.GameSettings$Options
 *  net.minecraft.util.Util
 *  net.minecraft.util.Util$EnumOS
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.Sys
 *  org.lwjgl.opengl.ARBCopyBuffer
 *  org.lwjgl.opengl.ARBFramebufferObject
 *  org.lwjgl.opengl.ARBMultitexture
 *  org.lwjgl.opengl.ARBShaderObjects
 *  org.lwjgl.opengl.ARBVertexBufferObject
 *  org.lwjgl.opengl.ARBVertexShader
 *  org.lwjgl.opengl.ContextCapabilities
 *  org.lwjgl.opengl.EXTBlendFuncSeparate
 *  org.lwjgl.opengl.EXTFramebufferObject
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL13
 *  org.lwjgl.opengl.GL14
 *  org.lwjgl.opengl.GL15
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL30
 *  org.lwjgl.opengl.GL31
 *  org.lwjgl.opengl.GLContext
 *  oshi.SystemInfo
 *  oshi.hardware.Processor
 */
package net.minecraft.client.renderer;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ARBCopyBuffer;
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
import org.lwjgl.opengl.GL31;
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
    private static FboMode framebufferType;
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
    private static String logText;
    private static String cpu;
    public static boolean vboSupported;
    public static boolean vboSupportedAti;
    private static boolean arbVbo;
    public static int GL_ARRAY_BUFFER;
    public static int GL_STATIC_DRAW;
    public static float lastBrightnessX;
    public static float lastBrightnessY;
    public static boolean openGL31;
    public static boolean vboRegions;
    public static int GL_COPY_READ_BUFFER;
    public static int GL_COPY_WRITE_BUFFER;
    public static final int GL_QUADS = 7;
    public static final int GL_TRIANGLES = 4;

    public static void initializeTextures() {
        Config.initDisplay();
        ContextCapabilities contextcapabilities = GLContext.getCapabilities();
        arbMultitexture = contextcapabilities.GL_ARB_multitexture && !contextcapabilities.OpenGL13;
        arbTextureEnvCombine = contextcapabilities.GL_ARB_texture_env_combine && !contextcapabilities.OpenGL13;
        openGL31 = contextcapabilities.OpenGL31;
        if (openGL31) {
            GL_COPY_READ_BUFFER = 36662;
            GL_COPY_WRITE_BUFFER = 36663;
        } else {
            GL_COPY_READ_BUFFER = 36662;
            GL_COPY_WRITE_BUFFER = 36663;
        }
        boolean copyBuffer = openGL31 || contextcapabilities.GL_ARB_copy_buffer;
        boolean multiDrawArrays = contextcapabilities.OpenGL14;
        boolean bl = vboRegions = copyBuffer && multiDrawArrays;
        if (!vboRegions) {
            ArrayList list = new ArrayList();
            if (!copyBuffer) {
                list.add((Object)"OpenGL 1.3, ARB_copy_buffer");
            }
            if (!multiDrawArrays) {
                list.add((Object)"OpenGL 1.4");
            }
            String vboRegionWarn = "VboRegions not supported, missing: " + Config.listToString((List)list);
            Config.dbg((String)vboRegionWarn);
            logText = logText + vboRegionWarn + "\n";
        }
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
        extBlendFuncSeparate = contextcapabilities.GL_EXT_blend_func_separate && !contextcapabilities.OpenGL14;
        openGL14 = contextcapabilities.OpenGL14 || contextcapabilities.GL_EXT_blend_func_separate;
        boolean bl2 = framebufferSupported = openGL14 && (contextcapabilities.GL_ARB_framebuffer_object || contextcapabilities.GL_EXT_framebuffer_object || contextcapabilities.OpenGL30);
        if (framebufferSupported) {
            logText = logText + "Using framebuffer objects because ";
            if (contextcapabilities.OpenGL30) {
                logText = logText + "OpenGL 3.0 is supported and separate blending is supported.\n";
                framebufferType = FboMode.BASE;
                GL_FRAMEBUFFER = 36160;
                GL_RENDERBUFFER = 36161;
                GL_COLOR_ATTACHMENT0 = 36064;
                GL_DEPTH_ATTACHMENT = 36096;
                GL_FRAMEBUFFER_COMPLETE = 36053;
                GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            } else if (contextcapabilities.GL_ARB_framebuffer_object) {
                logText = logText + "ARB_framebuffer_object is supported and separate blending is supported.\n";
                framebufferType = FboMode.ARB;
                GL_FRAMEBUFFER = 36160;
                GL_RENDERBUFFER = 36161;
                GL_COLOR_ATTACHMENT0 = 36064;
                GL_DEPTH_ATTACHMENT = 36096;
                GL_FRAMEBUFFER_COMPLETE = 36053;
                GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            } else if (contextcapabilities.GL_EXT_framebuffer_object) {
                logText = logText + "EXT_framebuffer_object is supported.\n";
                framebufferType = FboMode.EXT;
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
            logText = logText + "OpenGL 1.4 is " + (contextcapabilities.OpenGL14 ? "" : "not ") + "supported, ";
            logText = logText + "EXT_blend_func_separate is " + (contextcapabilities.GL_EXT_blend_func_separate ? "" : "not ") + "supported, ";
            logText = logText + "OpenGL 3.0 is " + (contextcapabilities.OpenGL30 ? "" : "not ") + "supported, ";
            logText = logText + "ARB_framebuffer_object is " + (contextcapabilities.GL_ARB_framebuffer_object ? "" : "not ") + "supported, and ";
            logText = logText + "EXT_framebuffer_object is " + (contextcapabilities.GL_EXT_framebuffer_object ? "" : "not ") + "supported.\n";
        }
        openGL21 = contextcapabilities.OpenGL21;
        shadersAvailable = openGL21 || contextcapabilities.GL_ARB_vertex_shader && contextcapabilities.GL_ARB_fragment_shader && contextcapabilities.GL_ARB_shader_objects;
        logText = logText + "Shaders are " + (shadersAvailable ? "" : "not ") + "available because ";
        if (shadersAvailable) {
            if (contextcapabilities.OpenGL21) {
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
            logText = logText + "OpenGL 2.1 is " + (contextcapabilities.OpenGL21 ? "" : "not ") + "supported, ";
            logText = logText + "ARB_shader_objects is " + (contextcapabilities.GL_ARB_shader_objects ? "" : "not ") + "supported, ";
            logText = logText + "ARB_vertex_shader is " + (contextcapabilities.GL_ARB_vertex_shader ? "" : "not ") + "supported, and ";
            logText = logText + "ARB_fragment_shader is " + (contextcapabilities.GL_ARB_fragment_shader ? "" : "not ") + "supported.\n";
        }
        shadersSupported = framebufferSupported && shadersAvailable;
        String s = GL11.glGetString((int)7936).toLowerCase(Locale.ROOT);
        nvidia = s.contains((CharSequence)"nvidia");
        arbVbo = !contextcapabilities.OpenGL15 && contextcapabilities.GL_ARB_vertex_buffer_object;
        vboSupported = contextcapabilities.OpenGL15 || arbVbo;
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
        if (ati = s.contains((CharSequence)"ati")) {
            if (vboSupported) {
                vboSupportedAti = true;
            } else {
                GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0f);
            }
        }
        try {
            Processor[] aprocessor = new SystemInfo().getHardware().getProcessors();
            cpu = String.format((String)"%dx %s", (Object[])new Object[]{aprocessor.length, aprocessor[0]}).replaceAll("\\s+", " ");
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static boolean areShadersSupported() {
        return shadersSupported;
    }

    public static String getLogText() {
        return logText;
    }

    public static int glGetProgrami(int program, int pname) {
        return arbShaders ? ARBShaderObjects.glGetObjectParameteriARB((int)program, (int)pname) : GL20.glGetProgrami((int)program, (int)pname);
    }

    public static void glAttachShader(int program, int shaderIn) {
        if (arbShaders) {
            ARBShaderObjects.glAttachObjectARB((int)program, (int)shaderIn);
        } else {
            GL20.glAttachShader((int)program, (int)shaderIn);
        }
    }

    public static void glDeleteShader(int shaderIn) {
        if (arbShaders) {
            ARBShaderObjects.glDeleteObjectARB((int)shaderIn);
        } else {
            GL20.glDeleteShader((int)shaderIn);
        }
    }

    public static int glCreateShader(int type) {
        return arbShaders ? ARBShaderObjects.glCreateShaderObjectARB((int)type) : GL20.glCreateShader((int)type);
    }

    public static void glShaderSource(int shaderIn, ByteBuffer string) {
        if (arbShaders) {
            ARBShaderObjects.glShaderSourceARB((int)shaderIn, (ByteBuffer)string);
        } else {
            GL20.glShaderSource((int)shaderIn, (ByteBuffer)string);
        }
    }

    public static void glCompileShader(int shaderIn) {
        if (arbShaders) {
            ARBShaderObjects.glCompileShaderARB((int)shaderIn);
        } else {
            GL20.glCompileShader((int)shaderIn);
        }
    }

    public static int glGetShaderi(int shaderIn, int pname) {
        return arbShaders ? ARBShaderObjects.glGetObjectParameteriARB((int)shaderIn, (int)pname) : GL20.glGetShaderi((int)shaderIn, (int)pname);
    }

    public static String glGetShaderInfoLog(int shaderIn, int maxLength) {
        return arbShaders ? ARBShaderObjects.glGetInfoLogARB((int)shaderIn, (int)maxLength) : GL20.glGetShaderInfoLog((int)shaderIn, (int)maxLength);
    }

    public static String glGetProgramInfoLog(int program, int maxLength) {
        return arbShaders ? ARBShaderObjects.glGetInfoLogARB((int)program, (int)maxLength) : GL20.glGetProgramInfoLog((int)program, (int)maxLength);
    }

    public static void glUseProgram(int program) {
        if (arbShaders) {
            ARBShaderObjects.glUseProgramObjectARB((int)program);
        } else {
            GL20.glUseProgram((int)program);
        }
    }

    public static int glCreateProgram() {
        return arbShaders ? ARBShaderObjects.glCreateProgramObjectARB() : GL20.glCreateProgram();
    }

    public static void glDeleteProgram(int program) {
        if (arbShaders) {
            ARBShaderObjects.glDeleteObjectARB((int)program);
        } else {
            GL20.glDeleteProgram((int)program);
        }
    }

    public static void glLinkProgram(int program) {
        if (arbShaders) {
            ARBShaderObjects.glLinkProgramARB((int)program);
        } else {
            GL20.glLinkProgram((int)program);
        }
    }

    public static int glGetUniformLocation(int programObj, CharSequence name) {
        return arbShaders ? ARBShaderObjects.glGetUniformLocationARB((int)programObj, (CharSequence)name) : GL20.glGetUniformLocation((int)programObj, (CharSequence)name);
    }

    public static void glUniform1(int location, IntBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform1ARB((int)location, (IntBuffer)values);
        } else {
            GL20.glUniform1((int)location, (IntBuffer)values);
        }
    }

    public static void glUniform1i(int location, int v0) {
        if (arbShaders) {
            ARBShaderObjects.glUniform1iARB((int)location, (int)v0);
        } else {
            GL20.glUniform1i((int)location, (int)v0);
        }
    }

    public static void glUniform1(int location, FloatBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform1ARB((int)location, (FloatBuffer)values);
        } else {
            GL20.glUniform1((int)location, (FloatBuffer)values);
        }
    }

    public static void glUniform2(int location, IntBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform2ARB((int)location, (IntBuffer)values);
        } else {
            GL20.glUniform2((int)location, (IntBuffer)values);
        }
    }

    public static void glUniform2(int location, FloatBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform2ARB((int)location, (FloatBuffer)values);
        } else {
            GL20.glUniform2((int)location, (FloatBuffer)values);
        }
    }

    public static void glUniform3(int location, IntBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform3ARB((int)location, (IntBuffer)values);
        } else {
            GL20.glUniform3((int)location, (IntBuffer)values);
        }
    }

    public static void glUniform3(int location, FloatBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform3ARB((int)location, (FloatBuffer)values);
        } else {
            GL20.glUniform3((int)location, (FloatBuffer)values);
        }
    }

    public static void glUniform4(int location, IntBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform4ARB((int)location, (IntBuffer)values);
        } else {
            GL20.glUniform4((int)location, (IntBuffer)values);
        }
    }

    public static void glUniform4(int location, FloatBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform4ARB((int)location, (FloatBuffer)values);
        } else {
            GL20.glUniform4((int)location, (FloatBuffer)values);
        }
    }

    public static void glUniformMatrix2(int location, boolean transpose, FloatBuffer matrices) {
        if (arbShaders) {
            ARBShaderObjects.glUniformMatrix2ARB((int)location, (boolean)transpose, (FloatBuffer)matrices);
        } else {
            GL20.glUniformMatrix2((int)location, (boolean)transpose, (FloatBuffer)matrices);
        }
    }

    public static void glUniformMatrix3(int location, boolean transpose, FloatBuffer matrices) {
        if (arbShaders) {
            ARBShaderObjects.glUniformMatrix3ARB((int)location, (boolean)transpose, (FloatBuffer)matrices);
        } else {
            GL20.glUniformMatrix3((int)location, (boolean)transpose, (FloatBuffer)matrices);
        }
    }

    public static void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices) {
        if (arbShaders) {
            ARBShaderObjects.glUniformMatrix4ARB((int)location, (boolean)transpose, (FloatBuffer)matrices);
        } else {
            GL20.glUniformMatrix4((int)location, (boolean)transpose, (FloatBuffer)matrices);
        }
    }

    public static int glGetAttribLocation(int program, CharSequence name) {
        return arbShaders ? ARBVertexShader.glGetAttribLocationARB((int)program, (CharSequence)name) : GL20.glGetAttribLocation((int)program, (CharSequence)name);
    }

    public static int glGenBuffers() {
        return arbVbo ? ARBVertexBufferObject.glGenBuffersARB() : GL15.glGenBuffers();
    }

    public static void glBindBuffer(int target, int buffer) {
        if (arbVbo) {
            ARBVertexBufferObject.glBindBufferARB((int)target, (int)buffer);
        } else {
            GL15.glBindBuffer((int)target, (int)buffer);
        }
    }

    public static void glBufferData(int target, ByteBuffer data, int usage) {
        if (arbVbo) {
            ARBVertexBufferObject.glBufferDataARB((int)target, (ByteBuffer)data, (int)usage);
        } else {
            GL15.glBufferData((int)target, (ByteBuffer)data, (int)usage);
        }
    }

    public static void glDeleteBuffers(int buffer) {
        if (arbVbo) {
            ARBVertexBufferObject.glDeleteBuffersARB((int)buffer);
        } else {
            GL15.glDeleteBuffers((int)buffer);
        }
    }

    public static boolean useVbo() {
        if (Config.isMultiTexture()) {
            return false;
        }
        if (Config.isRenderRegions() && !vboRegions) {
            return false;
        }
        return vboSupported && Minecraft.getMinecraft().gameSettings.useVbo;
    }

    public static void glBindFramebuffer(int target, int framebufferIn) {
        if (framebufferSupported) {
            switch (1.$SwitchMap$net$minecraft$client$renderer$OpenGlHelper$FboMode[framebufferType.ordinal()]) {
                case 1: {
                    GL30.glBindFramebuffer((int)target, (int)framebufferIn);
                    break;
                }
                case 2: {
                    ARBFramebufferObject.glBindFramebuffer((int)target, (int)framebufferIn);
                    break;
                }
                case 3: {
                    EXTFramebufferObject.glBindFramebufferEXT((int)target, (int)framebufferIn);
                }
            }
        }
    }

    public static void glBindRenderbuffer(int target, int renderbuffer) {
        if (framebufferSupported) {
            switch (1.$SwitchMap$net$minecraft$client$renderer$OpenGlHelper$FboMode[framebufferType.ordinal()]) {
                case 1: {
                    GL30.glBindRenderbuffer((int)target, (int)renderbuffer);
                    break;
                }
                case 2: {
                    ARBFramebufferObject.glBindRenderbuffer((int)target, (int)renderbuffer);
                    break;
                }
                case 3: {
                    EXTFramebufferObject.glBindRenderbufferEXT((int)target, (int)renderbuffer);
                }
            }
        }
    }

    public static void glDeleteRenderbuffers(int renderbuffer) {
        if (framebufferSupported) {
            switch (1.$SwitchMap$net$minecraft$client$renderer$OpenGlHelper$FboMode[framebufferType.ordinal()]) {
                case 1: {
                    GL30.glDeleteRenderbuffers((int)renderbuffer);
                    break;
                }
                case 2: {
                    ARBFramebufferObject.glDeleteRenderbuffers((int)renderbuffer);
                    break;
                }
                case 3: {
                    EXTFramebufferObject.glDeleteRenderbuffersEXT((int)renderbuffer);
                }
            }
        }
    }

    public static void glDeleteFramebuffers(int framebufferIn) {
        if (framebufferSupported) {
            switch (1.$SwitchMap$net$minecraft$client$renderer$OpenGlHelper$FboMode[framebufferType.ordinal()]) {
                case 1: {
                    GL30.glDeleteFramebuffers((int)framebufferIn);
                    break;
                }
                case 2: {
                    ARBFramebufferObject.glDeleteFramebuffers((int)framebufferIn);
                    break;
                }
                case 3: {
                    EXTFramebufferObject.glDeleteFramebuffersEXT((int)framebufferIn);
                }
            }
        }
    }

    public static int glGenFramebuffers() {
        if (!framebufferSupported) {
            return -1;
        }
        switch (1.$SwitchMap$net$minecraft$client$renderer$OpenGlHelper$FboMode[framebufferType.ordinal()]) {
            case 1: {
                return GL30.glGenFramebuffers();
            }
            case 2: {
                return ARBFramebufferObject.glGenFramebuffers();
            }
            case 3: {
                return EXTFramebufferObject.glGenFramebuffersEXT();
            }
        }
        return -1;
    }

    public static int glGenRenderbuffers() {
        if (!framebufferSupported) {
            return -1;
        }
        switch (1.$SwitchMap$net$minecraft$client$renderer$OpenGlHelper$FboMode[framebufferType.ordinal()]) {
            case 1: {
                return GL30.glGenRenderbuffers();
            }
            case 2: {
                return ARBFramebufferObject.glGenRenderbuffers();
            }
            case 3: {
                return EXTFramebufferObject.glGenRenderbuffersEXT();
            }
        }
        return -1;
    }

    public static void glRenderbufferStorage(int target, int internalFormat, int width, int height) {
        if (framebufferSupported) {
            switch (1.$SwitchMap$net$minecraft$client$renderer$OpenGlHelper$FboMode[framebufferType.ordinal()]) {
                case 1: {
                    GL30.glRenderbufferStorage((int)target, (int)internalFormat, (int)width, (int)height);
                    break;
                }
                case 2: {
                    ARBFramebufferObject.glRenderbufferStorage((int)target, (int)internalFormat, (int)width, (int)height);
                    break;
                }
                case 3: {
                    EXTFramebufferObject.glRenderbufferStorageEXT((int)target, (int)internalFormat, (int)width, (int)height);
                }
            }
        }
    }

    public static void glFramebufferRenderbuffer(int target, int attachment, int renderBufferTarget, int renderBuffer) {
        if (framebufferSupported) {
            switch (1.$SwitchMap$net$minecraft$client$renderer$OpenGlHelper$FboMode[framebufferType.ordinal()]) {
                case 1: {
                    GL30.glFramebufferRenderbuffer((int)target, (int)attachment, (int)renderBufferTarget, (int)renderBuffer);
                    break;
                }
                case 2: {
                    ARBFramebufferObject.glFramebufferRenderbuffer((int)target, (int)attachment, (int)renderBufferTarget, (int)renderBuffer);
                    break;
                }
                case 3: {
                    EXTFramebufferObject.glFramebufferRenderbufferEXT((int)target, (int)attachment, (int)renderBufferTarget, (int)renderBuffer);
                }
            }
        }
    }

    public static int glCheckFramebufferStatus(int target) {
        if (!framebufferSupported) {
            return -1;
        }
        switch (1.$SwitchMap$net$minecraft$client$renderer$OpenGlHelper$FboMode[framebufferType.ordinal()]) {
            case 1: {
                return GL30.glCheckFramebufferStatus((int)target);
            }
            case 2: {
                return ARBFramebufferObject.glCheckFramebufferStatus((int)target);
            }
            case 3: {
                return EXTFramebufferObject.glCheckFramebufferStatusEXT((int)target);
            }
        }
        return -1;
    }

    public static void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
        if (framebufferSupported) {
            switch (1.$SwitchMap$net$minecraft$client$renderer$OpenGlHelper$FboMode[framebufferType.ordinal()]) {
                case 1: {
                    GL30.glFramebufferTexture2D((int)target, (int)attachment, (int)textarget, (int)texture, (int)level);
                    break;
                }
                case 2: {
                    ARBFramebufferObject.glFramebufferTexture2D((int)target, (int)attachment, (int)textarget, (int)texture, (int)level);
                    break;
                }
                case 3: {
                    EXTFramebufferObject.glFramebufferTexture2DEXT((int)target, (int)attachment, (int)textarget, (int)texture, (int)level);
                }
            }
        }
    }

    public static void setActiveTexture(int texture) {
        if (arbMultitexture) {
            ARBMultitexture.glActiveTextureARB((int)texture);
        } else {
            GL13.glActiveTexture((int)texture);
        }
    }

    public static void setClientActiveTexture(int texture) {
        if (arbMultitexture) {
            ARBMultitexture.glClientActiveTextureARB((int)texture);
        } else {
            GL13.glClientActiveTexture((int)texture);
        }
    }

    public static void setLightmapTextureCoords(int target, float p_77475_1_, float t) {
        if (arbMultitexture) {
            ARBMultitexture.glMultiTexCoord2fARB((int)target, (float)p_77475_1_, (float)t);
        } else {
            GL13.glMultiTexCoord2f((int)target, (float)p_77475_1_, (float)t);
        }
        if (target == lightmapTexUnit) {
            lastBrightnessX = p_77475_1_;
            lastBrightnessY = t;
        }
    }

    public static void glBlendFunc(int sFactorRGB, int dFactorRGB, int sfactorAlpha, int dfactorAlpha) {
        if (openGL14) {
            if (extBlendFuncSeparate) {
                EXTBlendFuncSeparate.glBlendFuncSeparateEXT((int)sFactorRGB, (int)dFactorRGB, (int)sfactorAlpha, (int)dfactorAlpha);
            } else {
                GL14.glBlendFuncSeparate((int)sFactorRGB, (int)dFactorRGB, (int)sfactorAlpha, (int)dfactorAlpha);
            }
        } else {
            GL11.glBlendFunc((int)sFactorRGB, (int)dFactorRGB);
        }
    }

    public static boolean isFramebufferEnabled() {
        if (Config.isFastRender()) {
            return false;
        }
        if (Config.isAntialiasing()) {
            return false;
        }
        return framebufferSupported && Minecraft.getMinecraft().gameSettings.fboEnable;
    }

    public static void glBufferData(int target, long size, int usage) {
        if (arbVbo) {
            ARBVertexBufferObject.glBufferDataARB((int)target, (long)size, (int)usage);
        } else {
            GL15.glBufferData((int)target, (long)size, (int)usage);
        }
    }

    public static void glBufferSubData(int target, long offset, ByteBuffer data) {
        if (arbVbo) {
            ARBVertexBufferObject.glBufferSubDataARB((int)target, (long)offset, (ByteBuffer)data);
        } else {
            GL15.glBufferSubData((int)target, (long)offset, (ByteBuffer)data);
        }
    }

    public static void glCopyBufferSubData(int readTarget, int writeTarget, long readOffset, long writeOffset, long size) {
        if (openGL31) {
            GL31.glCopyBufferSubData((int)readTarget, (int)writeTarget, (long)readOffset, (long)writeOffset, (long)size);
        } else {
            ARBCopyBuffer.glCopyBufferSubData((int)readTarget, (int)writeTarget, (long)readOffset, (long)writeOffset, (long)size);
        }
    }

    public static String getCpu() {
        return cpu == null ? "<unknown>" : cpu;
    }

    public static void renderDirections(int p_188785_0_) {
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GL11.glLineWidth((float)4.0f);
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(0.0, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos((double)p_188785_0_, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(0.0, (double)p_188785_0_, 0.0).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, (double)p_188785_0_).color(0, 0, 0, 255).endVertex();
        tessellator.draw();
        GL11.glLineWidth((float)2.0f);
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(0.0, 0.0, 0.0).color(255, 0, 0, 255).endVertex();
        bufferbuilder.pos((double)p_188785_0_, 0.0, 0.0).color(255, 0, 0, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, 0.0).color(0, 255, 0, 255).endVertex();
        bufferbuilder.pos(0.0, (double)p_188785_0_, 0.0).color(0, 255, 0, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, 0.0).color(127, 127, 255, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, (double)p_188785_0_).color(127, 127, 255, 255).endVertex();
        tessellator.draw();
        GL11.glLineWidth((float)1.0f);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableTexture2D();
    }

    public static void openFile(File fileIn) {
        String s = fileIn.getAbsolutePath();
        if (Util.getOSType() == Util.EnumOS.OSX) {
            try {
                LOGGER.info(s);
                Runtime.getRuntime().exec(new String[]{"/usr/bin/open", s});
                return;
            }
            catch (IOException ioexception1) {
                LOGGER.error("Couldn't open file", (Throwable)ioexception1);
            }
        } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
            String s1 = String.format((String)"cmd.exe /C start \"Open file\" \"%s\"", (Object[])new Object[]{s});
            try {
                Runtime.getRuntime().exec(s1);
                return;
            }
            catch (IOException ioexception) {
                LOGGER.error("Couldn't open file", (Throwable)ioexception);
            }
        }
        boolean flag = false;
        try {
            Class oclass = Class.forName((String)"java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{fileIn.toURI()});
        }
        catch (Throwable throwable) {
            LOGGER.error("Couldn't open link", throwable);
            flag = true;
        }
        if (flag) {
            LOGGER.info("Opening via system class!");
            Sys.openURL((String)("file://" + s));
        }
    }

    static {
        logText = "";
        lastBrightnessX = 0.0f;
        lastBrightnessY = 0.0f;
    }
}
