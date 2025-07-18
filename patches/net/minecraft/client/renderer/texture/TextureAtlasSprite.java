package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

public class TextureAtlasSprite {
   private final String iconName;
   protected List<int[][]> framesTextureData = Lists.newArrayList();
   protected int[][] interpolatedFrameData;
   private AnimationMetadataSection animationMetadata;
   protected boolean rotated;
   protected int originX;
   protected int originY;
   protected int width;
   protected int height;
   private float minU;
   private float maxU;
   private float minV;
   private float maxV;
   protected int frameCounter;
   protected int tickCounter;

   protected TextureAtlasSprite(String var1) {
      this.iconName = ☃;
   }

   protected static TextureAtlasSprite makeAtlasSprite(ResourceLocation var0) {
      return new TextureAtlasSprite(☃.toString());
   }

   public void initSprite(int var1, int var2, int var3, int var4, boolean var5) {
      this.originX = ☃;
      this.originY = ☃;
      this.rotated = ☃;
      float ☃ = (float)(0.01F / ☃);
      float ☃x = (float)(0.01F / ☃);
      this.minU = ☃ / (float)☃ + ☃;
      this.maxU = (☃ + this.width) / (float)☃ - ☃;
      this.minV = (float)☃ / ☃ + ☃x;
      this.maxV = (float)(☃ + this.height) / ☃ - ☃x;
   }

   public void copyFrom(TextureAtlasSprite var1) {
      this.originX = ☃.originX;
      this.originY = ☃.originY;
      this.width = ☃.width;
      this.height = ☃.height;
      this.rotated = ☃.rotated;
      this.minU = ☃.minU;
      this.maxU = ☃.maxU;
      this.minV = ☃.minV;
      this.maxV = ☃.maxV;
   }

   public int getOriginX() {
      return this.originX;
   }

   public int getOriginY() {
      return this.originY;
   }

   public int getIconWidth() {
      return this.width;
   }

   public int getIconHeight() {
      return this.height;
   }

   public float getMinU() {
      return this.minU;
   }

   public float getMaxU() {
      return this.maxU;
   }

   public float getInterpolatedU(double var1) {
      float ☃ = this.maxU - this.minU;
      return this.minU + ☃ * (float)☃ / 16.0F;
   }

   public float getUnInterpolatedU(float var1) {
      float ☃ = this.maxU - this.minU;
      return (☃ - this.minU) / ☃ * 16.0F;
   }

   public float getMinV() {
      return this.minV;
   }

   public float getMaxV() {
      return this.maxV;
   }

   public float getInterpolatedV(double var1) {
      float ☃ = this.maxV - this.minV;
      return this.minV + ☃ * (float)☃ / 16.0F;
   }

   public float getUnInterpolatedV(float var1) {
      float ☃ = this.maxV - this.minV;
      return (☃ - this.minV) / ☃ * 16.0F;
   }

   public String getIconName() {
      return this.iconName;
   }

   public void updateAnimation() {
      this.tickCounter++;
      if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
         int ☃ = this.animationMetadata.getFrameIndex(this.frameCounter);
         int ☃x = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
         this.frameCounter = (this.frameCounter + 1) % ☃x;
         this.tickCounter = 0;
         int ☃xx = this.animationMetadata.getFrameIndex(this.frameCounter);
         if (☃ != ☃xx && ☃xx >= 0 && ☃xx < this.framesTextureData.size()) {
            TextureUtil.uploadTextureMipmap(this.framesTextureData.get(☃xx), this.width, this.height, this.originX, this.originY, false, false);
         }
      } else if (this.animationMetadata.isInterpolate()) {
         this.updateAnimationInterpolated();
      }
   }

   private void updateAnimationInterpolated() {
      double ☃ = 1.0 - (double)this.tickCounter / this.animationMetadata.getFrameTimeSingle(this.frameCounter);
      int ☃x = this.animationMetadata.getFrameIndex(this.frameCounter);
      int ☃xx = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
      int ☃xxx = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % ☃xx);
      if (☃x != ☃xxx && ☃xxx >= 0 && ☃xxx < this.framesTextureData.size()) {
         int[][] ☃xxxx = this.framesTextureData.get(☃x);
         int[][] ☃xxxxx = this.framesTextureData.get(☃xxx);
         if (this.interpolatedFrameData == null || this.interpolatedFrameData.length != ☃xxxx.length) {
            this.interpolatedFrameData = new int[☃xxxx.length][];
         }

         for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxxx.length; ☃xxxxxx++) {
            if (this.interpolatedFrameData[☃xxxxxx] == null) {
               this.interpolatedFrameData[☃xxxxxx] = new int[☃xxxx[☃xxxxxx].length];
            }

            if (☃xxxxxx < ☃xxxxx.length && ☃xxxxx[☃xxxxxx].length == ☃xxxx[☃xxxxxx].length) {
               for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃xxxx[☃xxxxxx].length; ☃xxxxxxx++) {
                  int ☃xxxxxxxx = ☃xxxx[☃xxxxxx][☃xxxxxxx];
                  int ☃xxxxxxxxx = ☃xxxxx[☃xxxxxx][☃xxxxxxx];
                  int ☃xxxxxxxxxx = this.interpolateColor(☃, ☃xxxxxxxx >> 16 & 0xFF, ☃xxxxxxxxx >> 16 & 0xFF);
                  int ☃xxxxxxxxxxx = this.interpolateColor(☃, ☃xxxxxxxx >> 8 & 0xFF, ☃xxxxxxxxx >> 8 & 0xFF);
                  int ☃xxxxxxxxxxxx = this.interpolateColor(☃, ☃xxxxxxxx & 0xFF, ☃xxxxxxxxx & 0xFF);
                  this.interpolatedFrameData[☃xxxxxx][☃xxxxxxx] = ☃xxxxxxxx & 0xFF000000 | ☃xxxxxxxxxx << 16 | ☃xxxxxxxxxxx << 8 | ☃xxxxxxxxxxxx;
               }
            }
         }

         TextureUtil.uploadTextureMipmap(this.interpolatedFrameData, this.width, this.height, this.originX, this.originY, false, false);
      }
   }

   private int interpolateColor(double var1, int var3, int var4) {
      return (int)(☃ * ☃ + (1.0 - ☃) * ☃);
   }

   public int[][] getFrameTextureData(int var1) {
      return this.framesTextureData.get(☃);
   }

   public int getFrameCount() {
      return this.framesTextureData.size();
   }

   public void setIconWidth(int var1) {
      this.width = ☃;
   }

   public void setIconHeight(int var1) {
      this.height = ☃;
   }

   public void loadSprite(PngSizeInfo var1, boolean var2) throws IOException {
      this.resetSprite();
      this.width = ☃.pngWidth;
      this.height = ☃.pngHeight;
      if (☃) {
         this.height = this.width;
      } else if (☃.pngHeight != ☃.pngWidth) {
         throw new RuntimeException("broken aspect ratio and not an animation");
      }
   }

   public void loadSpriteFrames(IResource var1, int var2) throws IOException {
      BufferedImage ☃ = TextureUtil.readBufferedImage(☃.getInputStream());
      AnimationMetadataSection ☃x = ☃.getMetadata("animation");
      int[][] ☃xx = new int[☃][];
      ☃xx[0] = new int[☃.getWidth() * ☃.getHeight()];
      ☃.getRGB(0, 0, ☃.getWidth(), ☃.getHeight(), ☃xx[0], 0, ☃.getWidth());
      if (☃x == null) {
         this.framesTextureData.add(☃xx);
      } else {
         int ☃xxx = ☃.getHeight() / this.width;
         if (☃x.getFrameCount() > 0) {
            for (int ☃xxxx : ☃x.getFrameIndexSet()) {
               if (☃xxxx >= ☃xxx) {
                  throw new RuntimeException("invalid frameindex " + ☃xxxx);
               }

               this.allocateFrameTextureData(☃xxxx);
               this.framesTextureData.set(☃xxxx, getFrameTextureData(☃xx, this.width, this.width, ☃xxxx));
            }

            this.animationMetadata = ☃x;
         } else {
            List<AnimationFrame> ☃xxxx = Lists.newArrayList();

            for (int ☃xxxxx = 0; ☃xxxxx < ☃xxx; ☃xxxxx++) {
               this.framesTextureData.add(getFrameTextureData(☃xx, this.width, this.width, ☃xxxxx));
               ☃xxxx.add(new AnimationFrame(☃xxxxx, -1));
            }

            this.animationMetadata = new AnimationMetadataSection(☃xxxx, this.width, this.height, ☃x.getFrameTime(), ☃x.isInterpolate());
         }
      }
   }

   public void generateMipmaps(int var1) {
      List<int[][]> ☃ = Lists.newArrayList();

      for (int ☃x = 0; ☃x < this.framesTextureData.size(); ☃x++) {
         final int[][] ☃xx = this.framesTextureData.get(☃x);
         if (☃xx != null) {
            try {
               ☃.add(TextureUtil.generateMipmapData(☃, this.width, ☃xx));
            } catch (Throwable var8) {
               CrashReport ☃xxx = CrashReport.makeCrashReport(var8, "Generating mipmaps for frame");
               CrashReportCategory ☃xxxx = ☃xxx.makeCategory("Frame being iterated");
               ☃xxxx.addCrashSection("Frame index", ☃x);
               ☃xxxx.addDetail("Frame sizes", new ICrashReportDetail<String>() {
                  public String call() throws Exception {
                     StringBuilder ☃xxxxx = new StringBuilder();

                     for (int[] ☃x : ☃) {
                        if (☃xxxxx.length() > 0) {
                           ☃xxxxx.append(", ");
                        }

                        ☃xxxxx.append(☃x == null ? "null" : ☃x.length);
                     }

                     return ☃xxxxx.toString();
                  }
               });
               throw new ReportedException(☃xxx);
            }
         }
      }

      this.setFramesTextureData(☃);
   }

   private void allocateFrameTextureData(int var1) {
      if (this.framesTextureData.size() <= ☃) {
         for (int ☃ = this.framesTextureData.size(); ☃ <= ☃; ☃++) {
            this.framesTextureData.add(null);
         }
      }
   }

   private static int[][] getFrameTextureData(int[][] var0, int var1, int var2, int var3) {
      int[][] ☃ = new int[☃.length][];

      for (int ☃x = 0; ☃x < ☃.length; ☃x++) {
         int[] ☃xx = ☃[☃x];
         if (☃xx != null) {
            ☃[☃x] = new int[(☃ >> ☃x) * (☃ >> ☃x)];
            System.arraycopy(☃xx, ☃ * ☃[☃x].length, ☃[☃x], 0, ☃[☃x].length);
         }
      }

      return ☃;
   }

   public void clearFramesTextureData() {
      this.framesTextureData.clear();
   }

   public boolean hasAnimationMetadata() {
      return this.animationMetadata != null;
   }

   public void setFramesTextureData(List<int[][]> var1) {
      this.framesTextureData = ☃;
   }

   private void resetSprite() {
      this.animationMetadata = null;
      this.setFramesTextureData(Lists.newArrayList());
      this.frameCounter = 0;
      this.tickCounter = 0;
   }

   @Override
   public String toString() {
      return "TextureAtlasSprite{name='"
         + this.iconName
         + '\''
         + ", frameCount="
         + this.framesTextureData.size()
         + ", rotated="
         + this.rotated
         + ", x="
         + this.originX
         + ", y="
         + this.originY
         + ", height="
         + this.height
         + ", width="
         + this.width
         + ", u0="
         + this.minU
         + ", u1="
         + this.maxU
         + ", v0="
         + this.minV
         + ", v1="
         + this.maxV
         + '}';
   }
}
