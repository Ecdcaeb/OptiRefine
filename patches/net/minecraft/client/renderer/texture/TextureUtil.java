package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
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

   private static float getColorGamma(int var0) {
      return COLOR_GAMMAS[☃ & 0xFF];
   }

   public static int glGenTextures() {
      return GlStateManager.generateTexture();
   }

   public static void deleteTexture(int var0) {
      GlStateManager.deleteTexture(☃);
   }

   public static int uploadTextureImage(int var0, BufferedImage var1) {
      return uploadTextureImageAllocate(☃, ☃, false, false);
   }

   public static void uploadTexture(int var0, int[] var1, int var2, int var3) {
      bindTexture(☃);
      uploadTextureSub(0, ☃, ☃, ☃, 0, 0, false, false, false);
   }

   public static int[][] generateMipmapData(int var0, int var1, int[][] var2) {
      int[][] ☃ = new int[☃ + 1][];
      ☃[0] = ☃[0];
      if (☃ > 0) {
         boolean ☃x = false;

         for (int ☃xx = 0; ☃xx < ☃.length; ☃xx++) {
            if (☃[0][☃xx] >> 24 == 0) {
               ☃x = true;
               break;
            }
         }

         for (int ☃xxx = 1; ☃xxx <= ☃; ☃xxx++) {
            if (☃[☃xxx] != null) {
               ☃[☃xxx] = ☃[☃xxx];
            } else {
               int[] ☃xxxx = ☃[☃xxx - 1];
               int[] ☃xxxxx = new int[☃xxxx.length >> 2];
               int ☃xxxxxx = ☃ >> ☃xxx;
               int ☃xxxxxxx = ☃xxxxx.length / ☃xxxxxx;
               int ☃xxxxxxxx = ☃xxxxxx << 1;

               for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < ☃xxxxxx; ☃xxxxxxxxx++) {
                  for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx < ☃xxxxxxx; ☃xxxxxxxxxx++) {
                     int ☃xxxxxxxxxxx = 2 * (☃xxxxxxxxx + ☃xxxxxxxxxx * ☃xxxxxxxx);
                     ☃xxxxx[☃xxxxxxxxx + ☃xxxxxxxxxx * ☃xxxxxx] = blendColors(
                        ☃xxxx[☃xxxxxxxxxxx + 0], ☃xxxx[☃xxxxxxxxxxx + 1], ☃xxxx[☃xxxxxxxxxxx + 0 + ☃xxxxxxxx], ☃xxxx[☃xxxxxxxxxxx + 1 + ☃xxxxxxxx], ☃x
                     );
                  }
               }

               ☃[☃xxx] = ☃xxxxx;
            }
         }
      }

      return ☃;
   }

   private static int blendColors(int var0, int var1, int var2, int var3, boolean var4) {
      if (☃) {
         MIPMAP_BUFFER[0] = ☃;
         MIPMAP_BUFFER[1] = ☃;
         MIPMAP_BUFFER[2] = ☃;
         MIPMAP_BUFFER[3] = ☃;
         float ☃ = 0.0F;
         float ☃x = 0.0F;
         float ☃xx = 0.0F;
         float ☃xxx = 0.0F;

         for (int ☃xxxx = 0; ☃xxxx < 4; ☃xxxx++) {
            if (MIPMAP_BUFFER[☃xxxx] >> 24 != 0) {
               ☃ += getColorGamma(MIPMAP_BUFFER[☃xxxx] >> 24);
               ☃x += getColorGamma(MIPMAP_BUFFER[☃xxxx] >> 16);
               ☃xx += getColorGamma(MIPMAP_BUFFER[☃xxxx] >> 8);
               ☃xxx += getColorGamma(MIPMAP_BUFFER[☃xxxx] >> 0);
            }
         }

         ☃ /= 4.0F;
         ☃x /= 4.0F;
         ☃xx /= 4.0F;
         ☃xxx /= 4.0F;
         int ☃xxxxx = (int)(Math.pow(☃, 0.45454545454545453) * 255.0);
         int ☃xxxxxx = (int)(Math.pow(☃x, 0.45454545454545453) * 255.0);
         int ☃xxxxxxx = (int)(Math.pow(☃xx, 0.45454545454545453) * 255.0);
         int ☃xxxxxxxx = (int)(Math.pow(☃xxx, 0.45454545454545453) * 255.0);
         if (☃xxxxx < 96) {
            ☃xxxxx = 0;
         }

         return ☃xxxxx << 24 | ☃xxxxxx << 16 | ☃xxxxxxx << 8 | ☃xxxxxxxx;
      } else {
         int ☃ = blendColorComponent(☃, ☃, ☃, ☃, 24);
         int ☃x = blendColorComponent(☃, ☃, ☃, ☃, 16);
         int ☃xx = blendColorComponent(☃, ☃, ☃, ☃, 8);
         int ☃xxx = blendColorComponent(☃, ☃, ☃, ☃, 0);
         return ☃ << 24 | ☃x << 16 | ☃xx << 8 | ☃xxx;
      }
   }

   private static int blendColorComponent(int var0, int var1, int var2, int var3, int var4) {
      float ☃ = getColorGamma(☃ >> ☃);
      float ☃x = getColorGamma(☃ >> ☃);
      float ☃xx = getColorGamma(☃ >> ☃);
      float ☃xxx = getColorGamma(☃ >> ☃);
      float ☃xxxx = (float)((float)Math.pow((☃ + ☃x + ☃xx + ☃xxx) * 0.25, 0.45454545454545453));
      return (int)(☃xxxx * 255.0);
   }

   public static void uploadTextureMipmap(int[][] var0, int var1, int var2, int var3, int var4, boolean var5, boolean var6) {
      for (int ☃ = 0; ☃ < ☃.length; ☃++) {
         int[] ☃x = ☃[☃];
         uploadTextureSub(☃, ☃x, ☃ >> ☃, ☃ >> ☃, ☃ >> ☃, ☃ >> ☃, ☃, ☃, ☃.length > 1);
      }
   }

   private static void uploadTextureSub(int var0, int[] var1, int var2, int var3, int var4, int var5, boolean var6, boolean var7, boolean var8) {
      int ☃ = 4194304 / ☃;
      setTextureBlurMipmap(☃, ☃);
      setTextureClamped(☃);
      int ☃x = 0;

      while (☃x < ☃ * ☃) {
         int ☃xx = ☃x / ☃;
         int ☃xxx = Math.min(☃, ☃ - ☃xx);
         int ☃xxxx = ☃ * ☃xxx;
         copyToBufferPos(☃, ☃x, ☃xxxx);
         GlStateManager.glTexSubImage2D(3553, ☃, ☃, ☃ + ☃xx, ☃, ☃xxx, 32993, 33639, DATA_BUFFER);
         ☃x += ☃ * ☃xxx;
      }
   }

   public static int uploadTextureImageAllocate(int var0, BufferedImage var1, boolean var2, boolean var3) {
      allocateTexture(☃, ☃.getWidth(), ☃.getHeight());
      return uploadTextureImageSub(☃, ☃, 0, 0, ☃, ☃);
   }

   public static void allocateTexture(int var0, int var1, int var2) {
      allocateTextureImpl(☃, 0, ☃, ☃);
   }

   public static void allocateTextureImpl(int var0, int var1, int var2, int var3) {
      deleteTexture(☃);
      bindTexture(☃);
      if (☃ >= 0) {
         GlStateManager.glTexParameteri(3553, 33085, ☃);
         GlStateManager.glTexParameteri(3553, 33082, 0);
         GlStateManager.glTexParameteri(3553, 33083, ☃);
         GlStateManager.glTexParameterf(3553, 34049, 0.0F);
      }

      for (int ☃ = 0; ☃ <= ☃; ☃++) {
         GlStateManager.glTexImage2D(3553, ☃, 6408, ☃ >> ☃, ☃ >> ☃, 0, 32993, 33639, null);
      }
   }

   public static int uploadTextureImageSub(int var0, BufferedImage var1, int var2, int var3, boolean var4, boolean var5) {
      bindTexture(☃);
      uploadTextureImageSubImpl(☃, ☃, ☃, ☃, ☃);
      return ☃;
   }

   private static void uploadTextureImageSubImpl(BufferedImage var0, int var1, int var2, boolean var3, boolean var4) {
      int ☃ = ☃.getWidth();
      int ☃x = ☃.getHeight();
      int ☃xx = 4194304 / ☃;
      int[] ☃xxx = new int[☃xx * ☃];
      setTextureBlurred(☃);
      setTextureClamped(☃);

      for (int ☃xxxx = 0; ☃xxxx < ☃ * ☃x; ☃xxxx += ☃ * ☃xx) {
         int ☃xxxxx = ☃xxxx / ☃;
         int ☃xxxxxx = Math.min(☃xx, ☃x - ☃xxxxx);
         int ☃xxxxxxx = ☃ * ☃xxxxxx;
         ☃.getRGB(0, ☃xxxxx, ☃, ☃xxxxxx, ☃xxx, 0, ☃);
         copyToBuffer(☃xxx, ☃xxxxxxx);
         GlStateManager.glTexSubImage2D(3553, 0, ☃, ☃ + ☃xxxxx, ☃, ☃xxxxxx, 32993, 33639, DATA_BUFFER);
      }
   }

   private static void setTextureClamped(boolean var0) {
      if (☃) {
         GlStateManager.glTexParameteri(3553, 10242, 10496);
         GlStateManager.glTexParameteri(3553, 10243, 10496);
      } else {
         GlStateManager.glTexParameteri(3553, 10242, 10497);
         GlStateManager.glTexParameteri(3553, 10243, 10497);
      }
   }

   private static void setTextureBlurred(boolean var0) {
      setTextureBlurMipmap(☃, false);
   }

   private static void setTextureBlurMipmap(boolean var0, boolean var1) {
      if (☃) {
         GlStateManager.glTexParameteri(3553, 10241, ☃ ? 9987 : 9729);
         GlStateManager.glTexParameteri(3553, 10240, 9729);
      } else {
         GlStateManager.glTexParameteri(3553, 10241, ☃ ? 9986 : 9728);
         GlStateManager.glTexParameteri(3553, 10240, 9728);
      }
   }

   private static void copyToBuffer(int[] var0, int var1) {
      copyToBufferPos(☃, 0, ☃);
   }

   private static void copyToBufferPos(int[] var0, int var1, int var2) {
      int[] ☃ = ☃;
      if (Minecraft.getMinecraft().gameSettings.anaglyph) {
         ☃ = updateAnaglyph(☃);
      }

      ((Buffer)DATA_BUFFER).clear();
      DATA_BUFFER.put(☃, ☃, ☃);
      ((Buffer)DATA_BUFFER).position(0).limit(☃);
   }

   static void bindTexture(int var0) {
      GlStateManager.bindTexture(☃);
   }

   public static int[] readImageData(IResourceManager var0, ResourceLocation var1) throws IOException {
      IResource ☃ = null;

      int[] var7;
      try {
         ☃ = ☃.getResource(☃);
         BufferedImage ☃x = readBufferedImage(☃.getInputStream());
         int ☃xx = ☃x.getWidth();
         int ☃xxx = ☃x.getHeight();
         int[] ☃xxxx = new int[☃xx * ☃xxx];
         ☃x.getRGB(0, 0, ☃xx, ☃xxx, ☃xxxx, 0, ☃xx);
         var7 = ☃xxxx;
      } finally {
         IOUtils.closeQuietly(☃);
      }

      return var7;
   }

   public static BufferedImage readBufferedImage(InputStream var0) throws IOException {
      BufferedImage var1;
      try {
         var1 = ImageIO.read(☃);
      } finally {
         IOUtils.closeQuietly(☃);
      }

      return var1;
   }

   public static int[] updateAnaglyph(int[] var0) {
      int[] ☃ = new int[☃.length];

      for (int ☃x = 0; ☃x < ☃.length; ☃x++) {
         ☃[☃x] = anaglyphColor(☃[☃x]);
      }

      return ☃;
   }

   public static int anaglyphColor(int var0) {
      int ☃ = ☃ >> 24 & 0xFF;
      int ☃x = ☃ >> 16 & 0xFF;
      int ☃xx = ☃ >> 8 & 0xFF;
      int ☃xxx = ☃ & 0xFF;
      int ☃xxxx = (☃x * 30 + ☃xx * 59 + ☃xxx * 11) / 100;
      int ☃xxxxx = (☃x * 30 + ☃xx * 70) / 100;
      int ☃xxxxxx = (☃x * 30 + ☃xxx * 70) / 100;
      return ☃ << 24 | ☃xxxx << 16 | ☃xxxxx << 8 | ☃xxxxxx;
   }

   public static void processPixelValues(int[] var0, int var1, int var2) {
      int[] ☃ = new int[☃];
      int ☃x = ☃ / 2;

      for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
         System.arraycopy(☃, ☃xx * ☃, ☃, 0, ☃);
         System.arraycopy(☃, (☃ - 1 - ☃xx) * ☃, ☃, ☃xx * ☃, ☃);
         System.arraycopy(☃, 0, ☃, (☃ - 1 - ☃xx) * ☃, ☃);
      }
   }

   static {
      int ☃ = -16777216;
      int ☃x = -524040;
      int[] ☃xx = new int[]{-524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040};
      int[] ☃xxx = new int[]{-16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216};
      int ☃xxxx = ☃xx.length;

      for (int ☃xxxxx = 0; ☃xxxxx < 16; ☃xxxxx++) {
         System.arraycopy(☃xxxxx < ☃xxxx ? ☃xx : ☃xxx, 0, MISSING_TEXTURE_DATA, 16 * ☃xxxxx, ☃xxxx);
         System.arraycopy(☃xxxxx < ☃xxxx ? ☃xxx : ☃xx, 0, MISSING_TEXTURE_DATA, 16 * ☃xxxxx + ☃xxxx, ☃xxxx);
      }

      MISSING_TEXTURE.updateDynamicTexture();
      COLOR_GAMMAS = new float[256];

      for (int ☃xxxxx = 0; ☃xxxxx < COLOR_GAMMAS.length; ☃xxxxx++) {
         COLOR_GAMMAS[☃xxxxx] = (float)Math.pow(☃xxxxx / 255.0F, 2.2);
      }

      MIPMAP_BUFFER = new int[4];
   }
}
