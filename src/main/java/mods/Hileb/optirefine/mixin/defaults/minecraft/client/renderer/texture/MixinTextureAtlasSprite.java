package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

public class MixinTextureAtlasSprite {
}
/*
--- net/minecraft/client/renderer/texture/TextureAtlasSprite.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/texture/TextureAtlasSprite.java	Tue Aug 19 14:59:58 2025
@@ -1,21 +1,29 @@
 package net.minecraft.client.renderer.texture;

+import com.google.common.collect.ImmutableList;
 import com.google.common.collect.Lists;
 import java.awt.image.BufferedImage;
 import java.io.IOException;
 import java.util.ArrayList;
+import java.util.Collection;
 import java.util.List;
+import java.util.function.Function;
 import net.minecraft.client.resources.IResource;
+import net.minecraft.client.resources.IResourceManager;
 import net.minecraft.client.resources.data.AnimationFrame;
 import net.minecraft.client.resources.data.AnimationMetadataSection;
 import net.minecraft.crash.CrashReport;
 import net.minecraft.crash.CrashReportCategory;
 import net.minecraft.crash.ICrashReportDetail;
 import net.minecraft.util.ReportedException;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.SmartAnimations;
+import net.optifine.shaders.Shaders;
+import net.optifine.util.CounterInt;
+import net.optifine.util.TextureUtils;

 public class TextureAtlasSprite {
    private final String iconName;
    protected List<int[][]> framesTextureData = Lists.newArrayList();
    protected int[][] interpolatedFrameData;
    private AnimationMetadataSection animationMetadata;
@@ -27,15 +35,40 @@
    private float minU;
    private float maxU;
    private float minV;
    private float maxV;
    protected int frameCounter;
    protected int tickCounter;
+   private int indexInMap = -1;
+   public float baseU;
+   public float baseV;
+   public int sheetWidth;
+   public int sheetHeight;
+   public int glSpriteTextureId = -1;
+   public TextureAtlasSprite spriteSingle = null;
+   public boolean isSpriteSingle = false;
+   public int mipmapLevels = 0;
+   public TextureAtlasSprite spriteNormal = null;
+   public TextureAtlasSprite spriteSpecular = null;
+   public boolean isShadersSprite = false;
+   public boolean isDependencyParent = false;
+   public boolean isEmissive = false;
+   public TextureAtlasSprite spriteEmissive = null;
+   private int animationIndex = -1;
+   private boolean animationActive = false;

-   protected TextureAtlasSprite(String var1) {
+   private TextureAtlasSprite(String var1, boolean var2) {
       this.iconName = var1;
+      this.isSpriteSingle = var2;
+   }
+
+   public TextureAtlasSprite(String var1) {
+      this.iconName = var1;
+      if (Config.isMultiTexture()) {
+         this.spriteSingle = new TextureAtlasSprite(this.getIconName() + ".spriteSingle", true);
+      }
    }

    protected static TextureAtlasSprite makeAtlasSprite(ResourceLocation var0) {
       return new TextureAtlasSprite(var0.toString());
    }

@@ -46,24 +79,52 @@
       float var6 = (float)(0.01F / var1);
       float var7 = (float)(0.01F / var2);
       this.minU = var3 / (float)var1 + var6;
       this.maxU = (var3 + this.width) / (float)var1 - var6;
       this.minV = (float)var4 / var2 + var7;
       this.maxV = (float)(var4 + this.height) / var2 - var7;
+      this.baseU = Math.min(this.minU, this.maxU);
+      this.baseV = Math.min(this.minV, this.maxV);
+      if (this.spriteSingle != null) {
+         this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
+      }
+
+      if (this.spriteNormal != null) {
+         this.spriteNormal.copyFrom(this);
+      }
+
+      if (this.spriteSpecular != null) {
+         this.spriteSpecular.copyFrom(this);
+      }
    }

    public void copyFrom(TextureAtlasSprite var1) {
       this.originX = var1.originX;
       this.originY = var1.originY;
       this.width = var1.width;
       this.height = var1.height;
       this.rotated = var1.rotated;
       this.minU = var1.minU;
       this.maxU = var1.maxU;
       this.minV = var1.minV;
       this.maxV = var1.maxV;
+      if (var1 != Config.getTextureMap().getMissingSprite()) {
+         this.indexInMap = var1.indexInMap;
+      }
+
+      this.baseU = var1.baseU;
+      this.baseV = var1.baseV;
+      this.sheetWidth = var1.sheetWidth;
+      this.sheetHeight = var1.sheetHeight;
+      this.glSpriteTextureId = var1.glSpriteTextureId;
+      this.mipmapLevels = var1.mipmapLevels;
+      if (this.spriteSingle != null) {
+         this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
+      }
+
+      this.animationIndex = var1.animationIndex;
    }

    public int getOriginX() {
       return this.originX;
    }

@@ -117,24 +178,37 @@

    public String getIconName() {
       return this.iconName;
    }

    public void updateAnimation() {
-      this.tickCounter++;
-      if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
-         int var1 = this.animationMetadata.getFrameIndex(this.frameCounter);
-         int var2 = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
-         this.frameCounter = (this.frameCounter + 1) % var2;
-         this.tickCounter = 0;
-         int var3 = this.animationMetadata.getFrameIndex(this.frameCounter);
-         if (var1 != var3 && var3 >= 0 && var3 < this.framesTextureData.size()) {
-            TextureUtil.uploadTextureMipmap(this.framesTextureData.get(var3), this.width, this.height, this.originX, this.originY, false, false);
+      if (this.animationMetadata != null) {
+         this.animationActive = SmartAnimations.isActive() ? SmartAnimations.isSpriteRendered(this.animationIndex) : true;
+         this.tickCounter++;
+         if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
+            int var1 = this.animationMetadata.getFrameIndex(this.frameCounter);
+            int var2 = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
+            this.frameCounter = (this.frameCounter + 1) % var2;
+            this.tickCounter = 0;
+            int var3 = this.animationMetadata.getFrameIndex(this.frameCounter);
+            boolean var4 = false;
+            boolean var5 = this.isSpriteSingle;
+            if (!this.animationActive) {
+               return;
+            }
+
+            if (var1 != var3 && var3 >= 0 && var3 < this.framesTextureData.size()) {
+               TextureUtil.uploadTextureMipmap(this.framesTextureData.get(var3), this.width, this.height, this.originX, this.originY, var4, var5);
+            }
+         } else if (this.animationMetadata.isInterpolate()) {
+            if (!this.animationActive) {
+               return;
+            }
+
+            this.updateAnimationInterpolated();
          }
-      } else if (this.animationMetadata.isInterpolate()) {
-         this.updateAnimationInterpolated();
       }
    }

    private void updateAnimationInterpolated() {
       double var1 = 1.0 - (double)this.tickCounter / this.animationMetadata.getFrameTimeSingle(this.frameCounter);
       int var3 = this.animationMetadata.getFrameIndex(this.frameCounter);
@@ -179,32 +253,47 @@
    public int getFrameCount() {
       return this.framesTextureData.size();
    }

    public void setIconWidth(int var1) {
       this.width = var1;
+      if (this.spriteSingle != null) {
+         this.spriteSingle.setIconWidth(this.width);
+      }
    }

    public void setIconHeight(int var1) {
       this.height = var1;
+      if (this.spriteSingle != null) {
+         this.spriteSingle.setIconHeight(this.height);
+      }
    }

    public void loadSprite(PngSizeInfo var1, boolean var2) throws IOException {
       this.resetSprite();
       this.width = var1.pngWidth;
       this.height = var1.pngHeight;
       if (var2) {
          this.height = this.width;
       } else if (var1.pngHeight != var1.pngWidth) {
          throw new RuntimeException("broken aspect ratio and not an animation");
       }
+
+      if (this.spriteSingle != null) {
+         this.spriteSingle.width = this.width;
+         this.spriteSingle.height = this.height;
+      }
    }

    public void loadSpriteFrames(IResource var1, int var2) throws IOException {
       BufferedImage var3 = TextureUtil.readBufferedImage(var1.getInputStream());
-      AnimationMetadataSection var4 = var1.getMetadata("animation");
+      if (this.width != var3.getWidth()) {
+         var3 = TextureUtils.scaleImage(var3, this.width);
+      }
+
+      AnimationMetadataSection var4 = (AnimationMetadataSection)var1.getMetadata("animation");
       int[][] var5 = new int[var2][];
       var5[0] = new int[var3.getWidth() * var3.getHeight()];
       var3.getRGB(0, 0, var3.getWidth(), var3.getHeight(), var5[0], 0, var3.getWidth());
       if (var4 == null) {
          this.framesTextureData.add(var5);
       } else {
@@ -218,20 +307,41 @@
                this.allocateFrameTextureData(var8);
                this.framesTextureData.set(var8, getFrameTextureData(var5, this.width, this.width, var8));
             }

             this.animationMetadata = var4;
          } else {
-            ArrayList var9 = Lists.newArrayList();
+            ArrayList var12 = Lists.newArrayList();

-            for (int var10 = 0; var10 < var6; var10++) {
-               this.framesTextureData.add(getFrameTextureData(var5, this.width, this.width, var10));
-               var9.add(new AnimationFrame(var10, -1));
+            for (int var14 = 0; var14 < var6; var14++) {
+               this.framesTextureData.add(getFrameTextureData(var5, this.width, this.width, var14));
+               var12.add(new AnimationFrame(var14, -1));
             }

-            this.animationMetadata = new AnimationMetadataSection(var9, this.width, this.height, var4.getFrameTime(), var4.isInterpolate());
+            this.animationMetadata = new AnimationMetadataSection(var12, this.width, this.height, var4.getFrameTime(), var4.isInterpolate());
+         }
+      }
+
+      if (!this.isShadersSprite) {
+         if (Config.isShaders()) {
+            this.loadShadersSprites();
+         }
+
+         for (int var10 = 0; var10 < this.framesTextureData.size(); var10++) {
+            int[][] var13 = this.framesTextureData.get(var10);
+            if (var13 != null && !this.iconName.startsWith("minecraft:blocks/leaves_")) {
+               for (int var15 = 0; var15 < var13.length; var15++) {
+                  int[] var9 = var13[var15];
+                  this.fixTransparentColor(var9);
+               }
+            }
+         }
+
+         if (this.spriteSingle != null) {
+            IResource var11 = Config.getResourceManager().getResource(var1.getResourceLocation());
+            this.spriteSingle.loadSpriteFrames(var11, var2);
          }
       }
    }

    public void generateMipmaps(int var1) {
       ArrayList var2 = Lists.newArrayList();
@@ -263,20 +373,27 @@
                throw new ReportedException(var6);
             }
          }
       }

       this.setFramesTextureData(var2);
+      if (this.spriteSingle != null) {
+         this.spriteSingle.generateMipmaps(var1);
+      }
    }

    private void allocateFrameTextureData(int var1) {
       if (this.framesTextureData.size() <= var1) {
          for (int var2 = this.framesTextureData.size(); var2 <= var1; var2++) {
             this.framesTextureData.add(null);
          }
       }
+
+      if (this.spriteSingle != null) {
+         this.spriteSingle.allocateFrameTextureData(var1);
+      }
    }

    private static int[][] getFrameTextureData(int[][] var0, int var1, int var2, int var3) {
       int[][] var4 = new int[var0.length][];

       for (int var5 = 0; var5 < var0.length; var5++) {
@@ -289,27 +406,36 @@

       return var4;
    }

    public void clearFramesTextureData() {
       this.framesTextureData.clear();
+      if (this.spriteSingle != null) {
+         this.spriteSingle.clearFramesTextureData();
+      }
    }

    public boolean hasAnimationMetadata() {
       return this.animationMetadata != null;
    }

    public void setFramesTextureData(List<int[][]> var1) {
       this.framesTextureData = var1;
+      if (this.spriteSingle != null) {
+         this.spriteSingle.setFramesTextureData(var1);
+      }
    }

    private void resetSprite() {
       this.animationMetadata = null;
       this.setFramesTextureData(Lists.newArrayList());
       this.frameCounter = 0;
       this.tickCounter = 0;
+      if (this.spriteSingle != null) {
+         this.spriteSingle.resetSprite();
+      }
    }

    public String toString() {
       return "TextureAtlasSprite{name='"
          + this.iconName
          + '\''
@@ -331,8 +457,170 @@
          + this.maxU
          + ", v0="
          + this.minV
          + ", v1="
          + this.maxV
          + '}';
+   }
+
+   public boolean hasCustomLoader(IResourceManager var1, ResourceLocation var2) {
+      return false;
+   }
+
+   public boolean load(IResourceManager var1, ResourceLocation var2, Function<ResourceLocation, TextureAtlasSprite> var3) {
+      return true;
+   }
+
+   public Collection<ResourceLocation> getDependencies() {
+      return ImmutableList.of();
+   }
+
+   public int getIndexInMap() {
+      return this.indexInMap;
+   }
+
+   public void setIndexInMap(int var1) {
+      this.indexInMap = var1;
+   }
+
+   public void updateIndexInMap(CounterInt var1) {
+      if (this.indexInMap < 0) {
+         this.indexInMap = var1.nextValue();
+      }
+   }
+
+   public int getAnimationIndex() {
+      return this.animationIndex;
+   }
+
+   public void setAnimationIndex(int var1) {
+      this.animationIndex = var1;
+      if (this.spriteNormal != null) {
+         this.spriteNormal.setAnimationIndex(var1);
+      }
+
+      if (this.spriteSpecular != null) {
+         this.spriteSpecular.setAnimationIndex(var1);
+      }
+   }
+
+   public boolean isAnimationActive() {
+      return this.animationActive;
+   }
+
+   private void fixTransparentColor(int[] var1) {
+      if (var1 != null) {
+         long var2 = 0L;
+         long var4 = 0L;
+         long var6 = 0L;
+         long var8 = 0L;
+
+         for (int var10 = 0; var10 < var1.length; var10++) {
+            int var11 = var1[var10];
+            int var12 = var11 >> 24 & 0xFF;
+            if (var12 >= 16) {
+               int var13 = var11 >> 16 & 0xFF;
+               int var14 = var11 >> 8 & 0xFF;
+               int var15 = var11 & 0xFF;
+               var2 += var13;
+               var4 += var14;
+               var6 += var15;
+               var8++;
+            }
+         }
+
+         if (var8 > 0L) {
+            int var17 = (int)(var2 / var8);
+            int var18 = (int)(var4 / var8);
+            int var19 = (int)(var6 / var8);
+            int var20 = var17 << 16 | var18 << 8 | var19;
+
+            for (int var21 = 0; var21 < var1.length; var21++) {
+               int var22 = var1[var21];
+               int var16 = var22 >> 24 & 0xFF;
+               if (var16 <= 16) {
+                  var1[var21] = var20;
+               }
+            }
+         }
+      }
+   }
+
+   public double getSpriteU16(float var1) {
+      float var2 = this.maxU - this.minU;
+      return (var1 - this.minU) / var2 * 16.0F;
+   }
+
+   public double getSpriteV16(float var1) {
+      float var2 = this.maxV - this.minV;
+      return (var1 - this.minV) / var2 * 16.0F;
+   }
+
+   public void bindSpriteTexture() {
+      if (this.glSpriteTextureId < 0) {
+         this.glSpriteTextureId = TextureUtil.glGenTextures();
+         TextureUtil.allocateTextureImpl(this.glSpriteTextureId, this.mipmapLevels, this.width, this.height);
+         TextureUtils.applyAnisotropicLevel();
+      }
+
+      TextureUtils.bindTexture(this.glSpriteTextureId);
+   }
+
+   public void deleteSpriteTexture() {
+      if (this.glSpriteTextureId >= 0) {
+         TextureUtil.deleteTexture(this.glSpriteTextureId);
+         this.glSpriteTextureId = -1;
+      }
+   }
+
+   public float toSingleU(float var1) {
+      var1 -= this.baseU;
+      float var2 = (float)this.sheetWidth / this.width;
+      return var1 * var2;
+   }
+
+   public float toSingleV(float var1) {
+      var1 -= this.baseV;
+      float var2 = (float)this.sheetHeight / this.height;
+      return var1 * var2;
+   }
+
+   public List<int[][]> getFramesTextureData() {
+      ArrayList var1 = new ArrayList();
+      var1.addAll(this.framesTextureData);
+      return var1;
+   }
+
+   public AnimationMetadataSection getAnimationMetadata() {
+      return this.animationMetadata;
+   }
+
+   public void setAnimationMetadata(AnimationMetadataSection var1) {
+      this.animationMetadata = var1;
+   }
+
+   private void loadShadersSprites() {
+      if (Shaders.configNormalMap) {
+         String var1 = this.iconName + "_n";
+         ResourceLocation var2 = new ResourceLocation(var1);
+         var2 = Config.getTextureMap().completeResourceLocation(var2);
+         if (Config.hasResource(var2)) {
+            this.spriteNormal = new TextureAtlasSprite(var1);
+            this.spriteNormal.isShadersSprite = true;
+            this.spriteNormal.copyFrom(this);
+            Config.getTextureMap().generateMipmaps(Config.getResourceManager(), this.spriteNormal);
+         }
+      }
+
+      if (Shaders.configSpecularMap) {
+         String var3 = this.iconName + "_s";
+         ResourceLocation var5 = new ResourceLocation(var3);
+         var5 = Config.getTextureMap().completeResourceLocation(var5);
+         if (Config.hasResource(var5)) {
+            this.spriteSpecular = new TextureAtlasSprite(var3);
+            this.spriteSpecular.isShadersSprite = true;
+            this.spriteSpecular.copyFrom(this);
+            Config.getTextureMap().generateMipmaps(Config.getResourceManager(), this.spriteSpecular);
+         }
+      }
    }
 }
 */