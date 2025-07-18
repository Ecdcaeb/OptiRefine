package net.minecraft.client.renderer.texture;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.optifine.SmartAnimations;
import net.optifine.shaders.Shaders;
import net.optifine.util.CounterInt;
import net.optifine.util.TextureUtils;

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
   private int indexInMap = -1;
   public float baseU;
   public float baseV;
   public int sheetWidth;
   public int sheetHeight;
   public int glSpriteTextureId = -1;
   public TextureAtlasSprite spriteSingle = null;
   public boolean isSpriteSingle = false;
   public int mipmapLevels = 0;
   public TextureAtlasSprite spriteNormal = null;
   public TextureAtlasSprite spriteSpecular = null;
   public boolean isShadersSprite = false;
   public boolean isDependencyParent = false;
   public boolean isEmissive = false;
   public TextureAtlasSprite spriteEmissive = null;
   private int animationIndex = -1;
   private boolean animationActive = false;

   private TextureAtlasSprite(String iconName, boolean isSpritesingle) {
      this.iconName = iconName;
      this.isSpriteSingle = isSpritesingle;
   }

   public TextureAtlasSprite(String spriteName) {
      this.iconName = spriteName;
      if (Config.isMultiTexture()) {
         this.spriteSingle = new TextureAtlasSprite(this.getIconName() + ".spriteSingle", true);
      }
   }

   protected static TextureAtlasSprite makeAtlasSprite(ResourceLocation spriteResourceLocation) {
      return new TextureAtlasSprite(spriteResourceLocation.toString());
   }

   public void initSprite(int inX, int inY, int originInX, int originInY, boolean rotatedIn) {
      this.originX = originInX;
      this.originY = originInY;
      this.rotated = rotatedIn;
      float f = (float)(0.01F / inX);
      float f1 = (float)(0.01F / inY);
      this.minU = originInX / (float)inX + f;
      this.maxU = (originInX + this.width) / (float)inX - f;
      this.minV = (float)originInY / inY + f1;
      this.maxV = (float)(originInY + this.height) / inY - f1;
      this.baseU = Math.min(this.minU, this.maxU);
      this.baseV = Math.min(this.minV, this.maxV);
      if (this.spriteSingle != null) {
         this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
      }

      if (this.spriteNormal != null) {
         this.spriteNormal.copyFrom(this);
      }

      if (this.spriteSpecular != null) {
         this.spriteSpecular.copyFrom(this);
      }
   }

   public void copyFrom(TextureAtlasSprite atlasSpirit) {
      this.originX = atlasSpirit.originX;
      this.originY = atlasSpirit.originY;
      this.width = atlasSpirit.width;
      this.height = atlasSpirit.height;
      this.rotated = atlasSpirit.rotated;
      this.minU = atlasSpirit.minU;
      this.maxU = atlasSpirit.maxU;
      this.minV = atlasSpirit.minV;
      this.maxV = atlasSpirit.maxV;
      if (atlasSpirit != Config.getTextureMap().getMissingSprite()) {
         this.indexInMap = atlasSpirit.indexInMap;
      }

      this.baseU = atlasSpirit.baseU;
      this.baseV = atlasSpirit.baseV;
      this.sheetWidth = atlasSpirit.sheetWidth;
      this.sheetHeight = atlasSpirit.sheetHeight;
      this.glSpriteTextureId = atlasSpirit.glSpriteTextureId;
      this.mipmapLevels = atlasSpirit.mipmapLevels;
      if (this.spriteSingle != null) {
         this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
      }

      this.animationIndex = atlasSpirit.animationIndex;
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

   public float getInterpolatedU(double u) {
      float f = this.maxU - this.minU;
      return this.minU + f * (float)u / 16.0F;
   }

   public float getUnInterpolatedU(float u) {
      float f = this.maxU - this.minU;
      return (u - this.minU) / f * 16.0F;
   }

   public float getMinV() {
      return this.minV;
   }

   public float getMaxV() {
      return this.maxV;
   }

   public float getInterpolatedV(double v) {
      float f = this.maxV - this.minV;
      return this.minV + f * (float)v / 16.0F;
   }

   public float getUnInterpolatedV(float p_188536_1_) {
      float f = this.maxV - this.minV;
      return (p_188536_1_ - this.minV) / f * 16.0F;
   }

   public String getIconName() {
      return this.iconName;
   }

   public void updateAnimation() {
      if (this.animationMetadata != null) {
         this.animationActive = SmartAnimations.isActive() ? SmartAnimations.isSpriteRendered(this.animationIndex) : true;
         this.tickCounter++;
         if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
            int i = this.animationMetadata.getFrameIndex(this.frameCounter);
            int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
            this.frameCounter = (this.frameCounter + 1) % j;
            this.tickCounter = 0;
            int k = this.animationMetadata.getFrameIndex(this.frameCounter);
            boolean texBlur = false;
            boolean texClamp = this.isSpriteSingle;
            if (!this.animationActive) {
               return;
            }

            if (i != k && k >= 0 && k < this.framesTextureData.size()) {
               TextureUtil.uploadTextureMipmap(this.framesTextureData.get(k), this.width, this.height, this.originX, this.originY, texBlur, texClamp);
            }
         } else if (this.animationMetadata.isInterpolate()) {
            if (!this.animationActive) {
               return;
            }

            this.updateAnimationInterpolated();
         }
      }
   }

   private void updateAnimationInterpolated() {
      double d0 = 1.0 - (double)this.tickCounter / this.animationMetadata.getFrameTimeSingle(this.frameCounter);
      int i = this.animationMetadata.getFrameIndex(this.frameCounter);
      int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
      int k = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % j);
      if (i != k && k >= 0 && k < this.framesTextureData.size()) {
         int[][] aint = this.framesTextureData.get(i);
         int[][] aint1 = this.framesTextureData.get(k);
         if (this.interpolatedFrameData == null || this.interpolatedFrameData.length != aint.length) {
            this.interpolatedFrameData = new int[aint.length][];
         }

         for (int l = 0; l < aint.length; l++) {
            if (this.interpolatedFrameData[l] == null) {
               this.interpolatedFrameData[l] = new int[aint[l].length];
            }

            if (l < aint1.length && aint1[l].length == aint[l].length) {
               for (int i1 = 0; i1 < aint[l].length; i1++) {
                  int j1 = aint[l][i1];
                  int k1 = aint1[l][i1];
                  int l1 = this.interpolateColor(d0, j1 >> 16 & 0xFF, k1 >> 16 & 0xFF);
                  int i2 = this.interpolateColor(d0, j1 >> 8 & 0xFF, k1 >> 8 & 0xFF);
                  int j2 = this.interpolateColor(d0, j1 & 0xFF, k1 & 0xFF);
                  this.interpolatedFrameData[l][i1] = j1 & 0xFF000000 | l1 << 16 | i2 << 8 | j2;
               }
            }
         }

         TextureUtil.uploadTextureMipmap(this.interpolatedFrameData, this.width, this.height, this.originX, this.originY, false, false);
      }
   }

   private int interpolateColor(double p_188535_1_, int p_188535_3_, int p_188535_4_) {
      return (int)(p_188535_1_ * p_188535_3_ + (1.0 - p_188535_1_) * p_188535_4_);
   }

   public int[][] getFrameTextureData(int index) {
      return this.framesTextureData.get(index);
   }

   public int getFrameCount() {
      return this.framesTextureData.size();
   }

   public void setIconWidth(int newWidth) {
      this.width = newWidth;
      if (this.spriteSingle != null) {
         this.spriteSingle.setIconWidth(this.width);
      }
   }

   public void setIconHeight(int newHeight) {
      this.height = newHeight;
      if (this.spriteSingle != null) {
         this.spriteSingle.setIconHeight(this.height);
      }
   }

   public void loadSprite(PngSizeInfo sizeInfo, boolean p_188538_2_) throws IOException {
      this.resetSprite();
      this.width = sizeInfo.pngWidth;
      this.height = sizeInfo.pngHeight;
      if (p_188538_2_) {
         this.height = this.width;
      } else if (sizeInfo.pngHeight != sizeInfo.pngWidth) {
         throw new RuntimeException("broken aspect ratio and not an animation");
      }

      if (this.spriteSingle != null) {
         this.spriteSingle.width = this.width;
         this.spriteSingle.height = this.height;
      }
   }

   public void loadSpriteFrames(IResource resource, int mipmaplevels) throws IOException {
      BufferedImage bufferedimage = TextureUtil.readBufferedImage(resource.getInputStream());
      if (this.width != bufferedimage.getWidth()) {
         bufferedimage = TextureUtils.scaleImage(bufferedimage, this.width);
      }

      AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)resource.getMetadata("animation");
      int[][] aint = new int[mipmaplevels][];
      aint[0] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
      bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[0], 0, bufferedimage.getWidth());
      if (animationmetadatasection == null) {
         this.framesTextureData.add(aint);
      } else {
         int i = bufferedimage.getHeight() / this.width;
         if (animationmetadatasection.getFrameCount() > 0) {
            for (int j : animationmetadatasection.getFrameIndexSet()) {
               if (j >= i) {
                  throw new RuntimeException("invalid frameindex " + j);
               }

               this.allocateFrameTextureData(j);
               this.framesTextureData.set(j, getFrameTextureData(aint, this.width, this.width, j));
            }

            this.animationMetadata = animationmetadatasection;
         } else {
            List<AnimationFrame> list = Lists.newArrayList();

            for (int k = 0; k < i; k++) {
               this.framesTextureData.add(getFrameTextureData(aint, this.width, this.width, k));
               list.add(new AnimationFrame(k, -1));
            }

            this.animationMetadata = new AnimationMetadataSection(
               list, this.width, this.height, animationmetadatasection.getFrameTime(), animationmetadatasection.isInterpolate()
            );
         }
      }

      if (!this.isShadersSprite) {
         if (Config.isShaders()) {
            this.loadShadersSprites();
         }

         for (int ix = 0; ix < this.framesTextureData.size(); ix++) {
            int[][] datas = this.framesTextureData.get(ix);
            if (datas != null && !this.iconName.startsWith("minecraft:blocks/leaves_")) {
               for (int di = 0; di < datas.length; di++) {
                  int[] data = datas[di];
                  this.fixTransparentColor(data);
               }
            }
         }

         if (this.spriteSingle != null) {
            IResource resourceSingle = Config.getResourceManager().getResource(resource.getResourceLocation());
            this.spriteSingle.loadSpriteFrames(resourceSingle, mipmaplevels);
         }
      }
   }

   public void generateMipmaps(int level) {
      List<int[][]> list = Lists.newArrayList();

      for (int i = 0; i < this.framesTextureData.size(); i++) {
         final int[][] aint = this.framesTextureData.get(i);
         if (aint != null) {
            try {
               list.add(TextureUtil.generateMipmapData(level, this.width, aint));
            } catch (Throwable var8) {
               CrashReport crashreport = CrashReport.makeCrashReport(var8, "Generating mipmaps for frame");
               CrashReportCategory crashreportcategory = crashreport.makeCategory("Frame being iterated");
               crashreportcategory.addCrashSection("Frame index", i);
               crashreportcategory.addDetail("Frame sizes", new ICrashReportDetail<String>() {
                  public String call() throws Exception {
                     StringBuilder stringbuilder = new StringBuilder();

                     for (int[] aint1 : aint) {
                        if (stringbuilder.length() > 0) {
                           stringbuilder.append(", ");
                        }

                        stringbuilder.append(aint1 == null ? "null" : aint1.length);
                     }

                     return stringbuilder.toString();
                  }
               });
               throw new ReportedException(crashreport);
            }
         }
      }

      this.setFramesTextureData(list);
      if (this.spriteSingle != null) {
         this.spriteSingle.generateMipmaps(level);
      }
   }

   private void allocateFrameTextureData(int index) {
      if (this.framesTextureData.size() <= index) {
         for (int i = this.framesTextureData.size(); i <= index; i++) {
            this.framesTextureData.add(null);
         }
      }

      if (this.spriteSingle != null) {
         this.spriteSingle.allocateFrameTextureData(index);
      }
   }

   private static int[][] getFrameTextureData(int[][] data, int rows, int columns, int p_147962_3_) {
      int[][] aint = new int[data.length][];

      for (int i = 0; i < data.length; i++) {
         int[] aint1 = data[i];
         if (aint1 != null) {
            aint[i] = new int[(rows >> i) * (columns >> i)];
            System.arraycopy(aint1, p_147962_3_ * aint[i].length, aint[i], 0, aint[i].length);
         }
      }

      return aint;
   }

   public void clearFramesTextureData() {
      this.framesTextureData.clear();
      if (this.spriteSingle != null) {
         this.spriteSingle.clearFramesTextureData();
      }
   }

   public boolean hasAnimationMetadata() {
      return this.animationMetadata != null;
   }

   public void setFramesTextureData(List<int[][]> newFramesTextureData) {
      this.framesTextureData = newFramesTextureData;
      if (this.spriteSingle != null) {
         this.spriteSingle.setFramesTextureData(newFramesTextureData);
      }
   }

   private void resetSprite() {
      this.animationMetadata = null;
      this.setFramesTextureData(Lists.newArrayList());
      this.frameCounter = 0;
      this.tickCounter = 0;
      if (this.spriteSingle != null) {
         this.spriteSingle.resetSprite();
      }
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

   public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
      return false;
   }

   public boolean load(IResourceManager manager, ResourceLocation location, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
      return true;
   }

   public Collection<ResourceLocation> getDependencies() {
      return ImmutableList.of();
   }

   public int getIndexInMap() {
      return this.indexInMap;
   }

   public void setIndexInMap(int indexInMap) {
      this.indexInMap = indexInMap;
   }

   public void updateIndexInMap(CounterInt counter) {
      if (this.indexInMap < 0) {
         this.indexInMap = counter.nextValue();
      }
   }

   public int getAnimationIndex() {
      return this.animationIndex;
   }

   public void setAnimationIndex(int animationIndex) {
      this.animationIndex = animationIndex;
      if (this.spriteNormal != null) {
         this.spriteNormal.setAnimationIndex(animationIndex);
      }

      if (this.spriteSpecular != null) {
         this.spriteSpecular.setAnimationIndex(animationIndex);
      }
   }

   public boolean isAnimationActive() {
      return this.animationActive;
   }

   private void fixTransparentColor(int[] data) {
      if (data != null) {
         long redSum = 0L;
         long greenSum = 0L;
         long blueSum = 0L;
         long count = 0L;

         for (int i = 0; i < data.length; i++) {
            int col = data[i];
            int alpha = col >> 24 & 0xFF;
            if (alpha >= 16) {
               int red = col >> 16 & 0xFF;
               int green = col >> 8 & 0xFF;
               int blue = col & 0xFF;
               redSum += red;
               greenSum += green;
               blueSum += blue;
               count++;
            }
         }

         if (count > 0L) {
            int redAvg = (int)(redSum / count);
            int greenAvg = (int)(greenSum / count);
            int blueAvg = (int)(blueSum / count);
            int colAvg = redAvg << 16 | greenAvg << 8 | blueAvg;

            for (int ix = 0; ix < data.length; ix++) {
               int col = data[ix];
               int alpha = col >> 24 & 0xFF;
               if (alpha <= 16) {
                  data[ix] = colAvg;
               }
            }
         }
      }
   }

   public double getSpriteU16(float atlasU) {
      float dU = this.maxU - this.minU;
      return (atlasU - this.minU) / dU * 16.0F;
   }

   public double getSpriteV16(float atlasV) {
      float dV = this.maxV - this.minV;
      return (atlasV - this.minV) / dV * 16.0F;
   }

   public void bindSpriteTexture() {
      if (this.glSpriteTextureId < 0) {
         this.glSpriteTextureId = TextureUtil.glGenTextures();
         TextureUtil.allocateTextureImpl(this.glSpriteTextureId, this.mipmapLevels, this.width, this.height);
         TextureUtils.applyAnisotropicLevel();
      }

      TextureUtils.bindTexture(this.glSpriteTextureId);
   }

   public void deleteSpriteTexture() {
      if (this.glSpriteTextureId >= 0) {
         TextureUtil.deleteTexture(this.glSpriteTextureId);
         this.glSpriteTextureId = -1;
      }
   }

   public float toSingleU(float u) {
      u -= this.baseU;
      float ku = (float)this.sheetWidth / this.width;
      return u * ku;
   }

   public float toSingleV(float v) {
      v -= this.baseV;
      float kv = (float)this.sheetHeight / this.height;
      return v * kv;
   }

   public List<int[][]> getFramesTextureData() {
      List<int[][]> datas = new ArrayList<>();
      datas.addAll(this.framesTextureData);
      return datas;
   }

   public AnimationMetadataSection getAnimationMetadata() {
      return this.animationMetadata;
   }

   public void setAnimationMetadata(AnimationMetadataSection animationMetadata) {
      this.animationMetadata = animationMetadata;
   }

   private void loadShadersSprites() {
      if (Shaders.configNormalMap) {
         String nameNormal = this.iconName + "_n";
         ResourceLocation locNormal = new ResourceLocation(nameNormal);
         locNormal = Config.getTextureMap().completeResourceLocation(locNormal);
         if (Config.hasResource(locNormal)) {
            this.spriteNormal = new TextureAtlasSprite(nameNormal);
            this.spriteNormal.isShadersSprite = true;
            this.spriteNormal.copyFrom(this);
            Config.getTextureMap().generateMipmaps(Config.getResourceManager(), this.spriteNormal);
         }
      }

      if (Shaders.configSpecularMap) {
         String nameSpecular = this.iconName + "_s";
         ResourceLocation locSpecular = new ResourceLocation(nameSpecular);
         locSpecular = Config.getTextureMap().completeResourceLocation(locSpecular);
         if (Config.hasResource(locSpecular)) {
            this.spriteSpecular = new TextureAtlasSprite(nameSpecular);
            this.spriteSpecular.isShadersSprite = true;
            this.spriteSpecular.copyFrom(this);
            Config.getTextureMap().generateMipmaps(Config.getResourceManager(), this.spriteSpecular);
         }
      }
   }
}
