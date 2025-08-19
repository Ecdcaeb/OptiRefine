package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

public class MixinTextureUtil {
}
/*

--- net/minecraft/client/renderer/texture/TextureUtil.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/texture/TextureUtil.java	Tue Aug 19 14:59:58 2025
@@ -9,23 +9,26 @@
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.GLAllocation;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.resources.IResource;
 import net.minecraft.client.resources.IResourceManager;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.Mipmaps;
+import net.optifine.reflect.Reflector;
 import org.apache.commons.io.IOUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public class TextureUtil {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final IntBuffer DATA_BUFFER = GLAllocation.createDirectIntBuffer(4194304);
    public static final DynamicTexture MISSING_TEXTURE = new DynamicTexture(16, 16);
    public static final int[] MISSING_TEXTURE_DATA = MISSING_TEXTURE.getTextureData();
    private static final float[] COLOR_GAMMAS;
    private static final int[] MIPMAP_BUFFER;
+   private static int[] dataArray = new int[4194304];

    private static float getColorGamma(int var0) {
       return COLOR_GAMMAS[var0 & 0xFF];
    }

    public static int glGenTextures() {
@@ -48,13 +51,13 @@
    public static int[][] generateMipmapData(int var0, int var1, int[][] var2) {
       int[][] var3 = new int[var0 + 1][];
       var3[0] = var2[0];
       if (var0 > 0) {
          boolean var4 = false;

-         for (int var5 = 0; var5 < var2.length; var5++) {
+         for (int var5 = 0; var5 < var2[0].length; var5++) {
             if (var2[0][var5] >> 24 == 0) {
                var4 = true;
                break;
             }
          }

@@ -81,51 +84,13 @@
       }

       return var3;
    }

    private static int blendColors(int var0, int var1, int var2, int var3, boolean var4) {
-      if (var4) {
-         MIPMAP_BUFFER[0] = var0;
-         MIPMAP_BUFFER[1] = var1;
-         MIPMAP_BUFFER[2] = var2;
-         MIPMAP_BUFFER[3] = var3;
-         float var13 = 0.0F;
-         float var15 = 0.0F;
-         float var17 = 0.0F;
-         float var19 = 0.0F;
-
-         for (int var9 = 0; var9 < 4; var9++) {
-            if (MIPMAP_BUFFER[var9] >> 24 != 0) {
-               var13 += getColorGamma(MIPMAP_BUFFER[var9] >> 24);
-               var15 += getColorGamma(MIPMAP_BUFFER[var9] >> 16);
-               var17 += getColorGamma(MIPMAP_BUFFER[var9] >> 8);
-               var19 += getColorGamma(MIPMAP_BUFFER[var9] >> 0);
-            }
-         }
-
-         var13 /= 4.0F;
-         var15 /= 4.0F;
-         var17 /= 4.0F;
-         var19 /= 4.0F;
-         int var21 = (int)(Math.pow(var13, 0.45454545454545453) * 255.0);
-         int var10 = (int)(Math.pow(var15, 0.45454545454545453) * 255.0);
-         int var11 = (int)(Math.pow(var17, 0.45454545454545453) * 255.0);
-         int var12 = (int)(Math.pow(var19, 0.45454545454545453) * 255.0);
-         if (var21 < 96) {
-            var21 = 0;
-         }
-
-         return var21 << 24 | var10 << 16 | var11 << 8 | var12;
-      } else {
-         int var5 = blendColorComponent(var0, var1, var2, var3, 24);
-         int var6 = blendColorComponent(var0, var1, var2, var3, 16);
-         int var7 = blendColorComponent(var0, var1, var2, var3, 8);
-         int var8 = blendColorComponent(var0, var1, var2, var3, 0);
-         return var5 << 24 | var6 << 16 | var7 << 8 | var8;
-      }
+      return Mipmaps.alphaBlend(var0, var1, var2, var3);
    }

    private static int blendColorComponent(int var0, int var1, int var2, int var3, int var4) {
       float var5 = getColorGamma(var0 >> var4);
       float var6 = getColorGamma(var1 >> var4);
       float var7 = getColorGamma(var2 >> var4);
@@ -142,21 +107,21 @@
    }

    private static void uploadTextureSub(int var0, int[] var1, int var2, int var3, int var4, int var5, boolean var6, boolean var7, boolean var8) {
       int var9 = 4194304 / var2;
       setTextureBlurMipmap(var6, var8);
       setTextureClamped(var7);
-      int var10 = 0;
+      int var11 = 0;

-      while (var10 < var2 * var3) {
-         int var11 = var10 / var2;
-         int var12 = Math.min(var9, var3 - var11);
-         int var13 = var2 * var12;
-         copyToBufferPos(var1, var10, var13);
-         GlStateManager.glTexSubImage2D(3553, var0, var4, var5 + var11, var2, var12, 32993, 33639, DATA_BUFFER);
-         var10 += var2 * var12;
+      while (var11 < var2 * var3) {
+         int var12 = var11 / var2;
+         int var10 = Math.min(var9, var3 - var12);
+         int var13 = var2 * var10;
+         copyToBufferPos(var1, var11, var13);
+         GlStateManager.glTexSubImage2D(3553, var0, var4, var5 + var12, var2, var10, 32993, 33639, DATA_BUFFER);
+         var11 += var2 * var10;
       }
    }

    public static int uploadTextureImageAllocate(int var0, BufferedImage var1, boolean var2, boolean var3) {
       allocateTexture(var0, var1.getWidth(), var1.getHeight());
       return uploadTextureImageSub(var0, var1, 0, 0, var2, var3);
@@ -164,23 +129,31 @@

    public static void allocateTexture(int var0, int var1, int var2) {
       allocateTextureImpl(var0, 0, var1, var2);
    }

    public static void allocateTextureImpl(int var0, int var1, int var2, int var3) {
-      deleteTexture(var0);
-      bindTexture(var0);
+      Class<TextureUtil> var4 = TextureUtil.class;
+      if (Reflector.SplashScreen.exists()) {
+         var4 = Reflector.SplashScreen.getTargetClass();
+      }
+
+      synchronized (var4) {
+         deleteTexture(var0);
+         bindTexture(var0);
+      }
+
       if (var1 >= 0) {
          GlStateManager.glTexParameteri(3553, 33085, var1);
          GlStateManager.glTexParameteri(3553, 33082, 0);
          GlStateManager.glTexParameteri(3553, 33083, var1);
          GlStateManager.glTexParameterf(3553, 34049, 0.0F);
       }

-      for (int var4 = 0; var4 <= var1; var4++) {
-         GlStateManager.glTexImage2D(3553, var4, 6408, var2 >> var4, var3 >> var4, 0, 32993, 33639, null);
+      for (int var5 = 0; var5 <= var1; var5++) {
+         GlStateManager.glTexImage2D(3553, var5, 6408, var2 >> var5, var3 >> var5, 0, 32993, 33639, (IntBuffer)null);
       }
    }

    public static int uploadTextureImageSub(int var0, BufferedImage var1, int var2, int var3, boolean var4, boolean var5) {
       bindTexture(var0);
       uploadTextureImageSubImpl(var1, var2, var3, var4, var5);
@@ -188,13 +161,13 @@
    }

    private static void uploadTextureImageSubImpl(BufferedImage var0, int var1, int var2, boolean var3, boolean var4) {
       int var5 = var0.getWidth();
       int var6 = var0.getHeight();
       int var7 = 4194304 / var5;
-      int[] var8 = new int[var7 * var5];
+      int[] var8 = dataArray;
       setTextureBlurred(var3);
       setTextureClamped(var4);

       for (int var9 = 0; var9 < var5 * var6; var9 += var5 * var7) {
          int var10 = var9 / var5;
          int var11 = Math.min(var7, var6 - var10);
@@ -202,32 +175,33 @@
          var0.getRGB(0, var10, var5, var11, var8, 0, var5);
          copyToBuffer(var8, var12);
          GlStateManager.glTexSubImage2D(3553, 0, var1, var2 + var10, var5, var11, 32993, 33639, DATA_BUFFER);
       }
    }

-   private static void setTextureClamped(boolean var0) {
+   public static void setTextureClamped(boolean var0) {
       if (var0) {
-         GlStateManager.glTexParameteri(3553, 10242, 10496);
-         GlStateManager.glTexParameteri(3553, 10243, 10496);
+         GlStateManager.glTexParameteri(3553, 10242, 33071);
+         GlStateManager.glTexParameteri(3553, 10243, 33071);
       } else {
          GlStateManager.glTexParameteri(3553, 10242, 10497);
          GlStateManager.glTexParameteri(3553, 10243, 10497);
       }
    }

    private static void setTextureBlurred(boolean var0) {
       setTextureBlurMipmap(var0, false);
    }

-   private static void setTextureBlurMipmap(boolean var0, boolean var1) {
+   public static void setTextureBlurMipmap(boolean var0, boolean var1) {
       if (var0) {
          GlStateManager.glTexParameteri(3553, 10241, var1 ? 9987 : 9729);
          GlStateManager.glTexParameteri(3553, 10240, 9729);
       } else {
-         GlStateManager.glTexParameteri(3553, 10241, var1 ? 9986 : 9728);
+         int var2 = Config.getMipmapType();
+         GlStateManager.glTexParameteri(3553, 10241, var1 ? var2 : 9728);
          GlStateManager.glTexParameteri(3553, 10240, 9728);
       }
    }

    private static void copyToBuffer(int[] var0, int var1) {
       copyToBufferPos(var0, 0, var1);
@@ -248,37 +222,45 @@
       GlStateManager.bindTexture(var0);
    }

    public static int[] readImageData(IResourceManager var0, ResourceLocation var1) throws IOException {
       IResource var2 = null;

-      int[] var7;
+      Object var5;
       try {
          var2 = var0.getResource(var1);
-         BufferedImage var3 = readBufferedImage(var2.getInputStream());
-         int var4 = var3.getWidth();
-         int var5 = var3.getHeight();
-         int[] var6 = new int[var4 * var5];
-         var3.getRGB(0, 0, var4, var5, var6, 0, var4);
-         var7 = var6;
+         BufferedImage var4 = readBufferedImage(var2.getInputStream());
+         if (var4 != null) {
+            int var11 = var4.getWidth();
+            int var6 = var4.getHeight();
+            int[] var7 = new int[var11 * var6];
+            var4.getRGB(0, 0, var11, var6, var7, 0, var11);
+            return var7;
+         }
+
+         var5 = null;
       } finally {
          IOUtils.closeQuietly(var2);
       }

-      return var7;
+      return (int[])var5;
    }

    public static BufferedImage readBufferedImage(InputStream var0) throws IOException {
-      BufferedImage var1;
-      try {
-         var1 = ImageIO.read(var0);
-      } finally {
-         IOUtils.closeQuietly(var0);
-      }
+      if (var0 == null) {
+         return null;
+      } else {
+         BufferedImage var1;
+         try {
+            var1 = ImageIO.read(var0);
+         } finally {
+            IOUtils.closeQuietly(var0);
+         }

-      return var1;
+         return var1;
+      }
    }

    public static int[] updateAnaglyph(int[] var0) {
       int[] var1 = new int[var0.length];

       for (int var2 = 0; var2 < var0.length; var2++) {
 */
