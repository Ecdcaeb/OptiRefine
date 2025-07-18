package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.util.glu.GLU;

public class GLAllocation {
   public static synchronized int generateDisplayLists(int var0) {
      int ☃ = GlStateManager.glGenLists(☃);
      if (☃ == 0) {
         int ☃x = GlStateManager.glGetError();
         String ☃xx = "No error code reported";
         if (☃x != 0) {
            ☃xx = GLU.gluErrorString(☃x);
         }

         throw new IllegalStateException("glGenLists returned an ID of 0 for a count of " + ☃ + ", GL error (" + ☃x + "): " + ☃xx);
      } else {
         return ☃;
      }
   }

   public static synchronized void deleteDisplayLists(int var0, int var1) {
      GlStateManager.glDeleteLists(☃, ☃);
   }

   public static synchronized void deleteDisplayLists(int var0) {
      deleteDisplayLists(☃, 1);
   }

   public static synchronized ByteBuffer createDirectByteBuffer(int var0) {
      return ByteBuffer.allocateDirect(☃).order(ByteOrder.nativeOrder());
   }

   public static IntBuffer createDirectIntBuffer(int var0) {
      return createDirectByteBuffer(☃ << 2).asIntBuffer();
   }

   public static FloatBuffer createDirectFloatBuffer(int var0) {
      return createDirectByteBuffer(☃ << 2).asFloatBuffer();
   }
}
