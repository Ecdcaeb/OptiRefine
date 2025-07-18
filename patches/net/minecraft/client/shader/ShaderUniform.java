package net.minecraft.client.shader;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

public class ShaderUniform {
   private static final Logger LOGGER = LogManager.getLogger();
   private int uniformLocation;
   private final int uniformCount;
   private final int uniformType;
   private final IntBuffer uniformIntBuffer;
   private final FloatBuffer uniformFloatBuffer;
   private final String shaderName;
   private boolean dirty;
   private final ShaderManager shaderManager;

   public ShaderUniform(String var1, int var2, int var3, ShaderManager var4) {
      this.shaderName = ☃;
      this.uniformCount = ☃;
      this.uniformType = ☃;
      this.shaderManager = ☃;
      if (☃ <= 3) {
         this.uniformIntBuffer = BufferUtils.createIntBuffer(☃);
         this.uniformFloatBuffer = null;
      } else {
         this.uniformIntBuffer = null;
         this.uniformFloatBuffer = BufferUtils.createFloatBuffer(☃);
      }

      this.uniformLocation = -1;
      this.markDirty();
   }

   private void markDirty() {
      this.dirty = true;
      if (this.shaderManager != null) {
         this.shaderManager.markDirty();
      }
   }

   public static int parseType(String var0) {
      int ☃ = -1;
      if ("int".equals(☃)) {
         ☃ = 0;
      } else if ("float".equals(☃)) {
         ☃ = 4;
      } else if (☃.startsWith("matrix")) {
         if (☃.endsWith("2x2")) {
            ☃ = 8;
         } else if (☃.endsWith("3x3")) {
            ☃ = 9;
         } else if (☃.endsWith("4x4")) {
            ☃ = 10;
         }
      }

      return ☃;
   }

   public void setUniformLocation(int var1) {
      this.uniformLocation = ☃;
   }

   public String getShaderName() {
      return this.shaderName;
   }

   public void set(float var1) {
      ((Buffer)this.uniformFloatBuffer).position(0);
      this.uniformFloatBuffer.put(0, ☃);
      this.markDirty();
   }

   public void set(float var1, float var2) {
      ((Buffer)this.uniformFloatBuffer).position(0);
      this.uniformFloatBuffer.put(0, ☃);
      this.uniformFloatBuffer.put(1, ☃);
      this.markDirty();
   }

   public void set(float var1, float var2, float var3) {
      ((Buffer)this.uniformFloatBuffer).position(0);
      this.uniformFloatBuffer.put(0, ☃);
      this.uniformFloatBuffer.put(1, ☃);
      this.uniformFloatBuffer.put(2, ☃);
      this.markDirty();
   }

   public void set(float var1, float var2, float var3, float var4) {
      ((Buffer)this.uniformFloatBuffer).position(0);
      this.uniformFloatBuffer.put(☃);
      this.uniformFloatBuffer.put(☃);
      this.uniformFloatBuffer.put(☃);
      this.uniformFloatBuffer.put(☃);
      ((Buffer)this.uniformFloatBuffer).flip();
      this.markDirty();
   }

   public void setSafe(float var1, float var2, float var3, float var4) {
      ((Buffer)this.uniformFloatBuffer).position(0);
      if (this.uniformType >= 4) {
         this.uniformFloatBuffer.put(0, ☃);
      }

      if (this.uniformType >= 5) {
         this.uniformFloatBuffer.put(1, ☃);
      }

      if (this.uniformType >= 6) {
         this.uniformFloatBuffer.put(2, ☃);
      }

      if (this.uniformType >= 7) {
         this.uniformFloatBuffer.put(3, ☃);
      }

      this.markDirty();
   }

   public void set(int var1, int var2, int var3, int var4) {
      ((Buffer)this.uniformIntBuffer).position(0);
      if (this.uniformType >= 0) {
         this.uniformIntBuffer.put(0, ☃);
      }

      if (this.uniformType >= 1) {
         this.uniformIntBuffer.put(1, ☃);
      }

      if (this.uniformType >= 2) {
         this.uniformIntBuffer.put(2, ☃);
      }

      if (this.uniformType >= 3) {
         this.uniformIntBuffer.put(3, ☃);
      }

      this.markDirty();
   }

   public void set(float[] var1) {
      if (☃.length < this.uniformCount) {
         LOGGER.warn("Uniform.set called with a too-small value array (expected {}, got {}). Ignoring.", this.uniformCount, ☃.length);
      } else {
         ((Buffer)this.uniformFloatBuffer).position(0);
         this.uniformFloatBuffer.put(☃);
         ((Buffer)this.uniformFloatBuffer).position(0);
         this.markDirty();
      }
   }

   public void set(
      float var1,
      float var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13,
      float var14,
      float var15,
      float var16
   ) {
      ((Buffer)this.uniformFloatBuffer).position(0);
      this.uniformFloatBuffer.put(0, ☃);
      this.uniformFloatBuffer.put(1, ☃);
      this.uniformFloatBuffer.put(2, ☃);
      this.uniformFloatBuffer.put(3, ☃);
      this.uniformFloatBuffer.put(4, ☃);
      this.uniformFloatBuffer.put(5, ☃);
      this.uniformFloatBuffer.put(6, ☃);
      this.uniformFloatBuffer.put(7, ☃);
      this.uniformFloatBuffer.put(8, ☃);
      this.uniformFloatBuffer.put(9, ☃);
      this.uniformFloatBuffer.put(10, ☃);
      this.uniformFloatBuffer.put(11, ☃);
      this.uniformFloatBuffer.put(12, ☃);
      this.uniformFloatBuffer.put(13, ☃);
      this.uniformFloatBuffer.put(14, ☃);
      this.uniformFloatBuffer.put(15, ☃);
      this.markDirty();
   }

   public void set(Matrix4f var1) {
      this.set(☃.m00, ☃.m01, ☃.m02, ☃.m03, ☃.m10, ☃.m11, ☃.m12, ☃.m13, ☃.m20, ☃.m21, ☃.m22, ☃.m23, ☃.m30, ☃.m31, ☃.m32, ☃.m33);
   }

   public void upload() {
      if (!this.dirty) {
      }

      this.dirty = false;
      if (this.uniformType <= 3) {
         this.uploadInt();
      } else if (this.uniformType <= 7) {
         this.uploadFloat();
      } else {
         if (this.uniformType > 10) {
            LOGGER.warn("Uniform.upload called, but type value ({}) is not a valid type. Ignoring.", this.uniformType);
            return;
         }

         this.uploadFloatMatrix();
      }
   }

   private void uploadInt() {
      switch (this.uniformType) {
         case 0:
            OpenGlHelper.glUniform1(this.uniformLocation, this.uniformIntBuffer);
            break;
         case 1:
            OpenGlHelper.glUniform2(this.uniformLocation, this.uniformIntBuffer);
            break;
         case 2:
            OpenGlHelper.glUniform3(this.uniformLocation, this.uniformIntBuffer);
            break;
         case 3:
            OpenGlHelper.glUniform4(this.uniformLocation, this.uniformIntBuffer);
            break;
         default:
            LOGGER.warn("Uniform.upload called, but count value ({}) is  not in the range of 1 to 4. Ignoring.", this.uniformCount);
      }
   }

   private void uploadFloat() {
      switch (this.uniformType) {
         case 4:
            OpenGlHelper.glUniform1(this.uniformLocation, this.uniformFloatBuffer);
            break;
         case 5:
            OpenGlHelper.glUniform2(this.uniformLocation, this.uniformFloatBuffer);
            break;
         case 6:
            OpenGlHelper.glUniform3(this.uniformLocation, this.uniformFloatBuffer);
            break;
         case 7:
            OpenGlHelper.glUniform4(this.uniformLocation, this.uniformFloatBuffer);
            break;
         default:
            LOGGER.warn("Uniform.upload called, but count value ({}) is not in the range of 1 to 4. Ignoring.", this.uniformCount);
      }
   }

   private void uploadFloatMatrix() {
      switch (this.uniformType) {
         case 8:
            OpenGlHelper.glUniformMatrix2(this.uniformLocation, true, this.uniformFloatBuffer);
            break;
         case 9:
            OpenGlHelper.glUniformMatrix3(this.uniformLocation, true, this.uniformFloatBuffer);
            break;
         case 10:
            OpenGlHelper.glUniformMatrix4(this.uniformLocation, true, this.uniformFloatBuffer);
      }
   }
}
