package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

public class MixinTextureMap {
}
/*

--- net/minecraft/client/renderer/texture/TextureMap.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/texture/TextureMap.java	Tue Aug 19 14:59:58 2025
@@ -1,247 +1,535 @@
 package net.minecraft.client.renderer.texture;

 import com.google.common.collect.Lists;
 import com.google.common.collect.Maps;
+import java.awt.Dimension;
+import java.awt.image.BufferedImage;
 import java.io.IOException;
+import java.io.InputStream;
+import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
+import java.util.Set;
+import java.util.TreeSet;
 import java.util.Map.Entry;
 import javax.annotation.Nullable;
-import net.minecraft.client.Minecraft;
+import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.StitcherException;
 import net.minecraft.client.resources.IResource;
 import net.minecraft.client.resources.IResourceManager;
 import net.minecraft.crash.CrashReport;
 import net.minecraft.crash.CrashReportCategory;
 import net.minecraft.crash.ICrashReportDetail;
 import net.minecraft.util.ReportedException;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.math.MathHelper;
+import net.optifine.BetterGrass;
+import net.optifine.ConnectedTextures;
+import net.optifine.CustomItems;
+import net.optifine.EmissiveTextures;
+import net.optifine.SmartAnimations;
+import net.optifine.SpriteDependencies;
+import net.optifine.reflect.Reflector;
+import net.optifine.reflect.ReflectorForge;
+import net.optifine.shaders.ShadersTex;
+import net.optifine.util.CounterInt;
+import net.optifine.util.TextureUtils;
 import org.apache.commons.io.IOUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public class TextureMap extends AbstractTexture implements ITickableTextureObject {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
    public static final ResourceLocation LOCATION_BLOCKS_TEXTURE = new ResourceLocation("textures/atlas/blocks.png");
-   private final List<TextureAtlasSprite> listAnimatedSprites = Lists.newArrayList();
-   private final Map<String, TextureAtlasSprite> mapRegisteredSprites = Maps.newHashMap();
-   private final Map<String, TextureAtlasSprite> mapUploadedSprites = Maps.newHashMap();
+   private final List<TextureAtlasSprite> listAnimatedSprites;
+   private final Map<String, TextureAtlasSprite> mapRegisteredSprites;
+   private final Map<String, TextureAtlasSprite> mapUploadedSprites;
    private final String basePath;
    private final ITextureMapPopulator iconCreator;
    private int mipmapLevels;
-   private final TextureAtlasSprite missingImage = new TextureAtlasSprite("missingno");
+   private final TextureAtlasSprite missingImage;
+   private TextureAtlasSprite[] iconGrid = null;
+   private int iconGridSize = -1;
+   private int iconGridCountX = -1;
+   private int iconGridCountY = -1;
+   private double iconGridSizeU = -1.0;
+   private double iconGridSizeV = -1.0;
+   private CounterInt counterIndexInMap = new CounterInt(0);
+   public int atlasWidth = 0;
+   public int atlasHeight = 0;
+   private int countAnimationsActive;
+   private int frameCountAnimations;

    public TextureMap(String var1) {
-      this(var1, null);
+      this(var1, (ITextureMapPopulator)null);
+   }
+
+   public TextureMap(String var1, boolean var2) {
+      this(var1, (ITextureMapPopulator)null, var2);
    }

    public TextureMap(String var1, @Nullable ITextureMapPopulator var2) {
+      this(var1, var2, false);
+   }
+
+   public TextureMap(String var1, ITextureMapPopulator var2, boolean var3) {
+      this.listAnimatedSprites = Lists.newArrayList();
+      this.mapRegisteredSprites = Maps.newHashMap();
+      this.mapUploadedSprites = Maps.newHashMap();
+      this.missingImage = new TextureAtlasSprite("missingno");
       this.basePath = var1;
       this.iconCreator = var2;
    }

    private void initMissingImage() {
-      int[] var1 = TextureUtil.MISSING_TEXTURE_DATA;
-      this.missingImage.setIconWidth(16);
-      this.missingImage.setIconHeight(16);
-      int[][] var2 = new int[this.mipmapLevels + 1][];
-      var2[0] = var1;
-      this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][]{var2}));
+      int var1 = this.getMinSpriteSize();
+      int[] var2 = this.getMissingImageData(var1);
+      this.missingImage.setIconWidth(var1);
+      this.missingImage.setIconHeight(var1);
+      int[][] var3 = new int[this.mipmapLevels + 1][];
+      var3[0] = var2;
+      this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][]{var3}));
+      this.missingImage.setIndexInMap(this.counterIndexInMap.nextValue());
    }

    public void loadTexture(IResourceManager var1) throws IOException {
       if (this.iconCreator != null) {
          this.loadSprites(var1, this.iconCreator);
       }
    }

    public void loadSprites(IResourceManager var1, ITextureMapPopulator var2) {
       this.mapRegisteredSprites.clear();
+      this.counterIndexInMap.reset();
+      Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, new Object[]{this});
       var2.registerSprites(this);
+      if (this.mipmapLevels >= 4) {
+         this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, var1);
+         Config.log("Mipmap levels: " + this.mipmapLevels);
+      }
+
       this.initMissingImage();
       this.deleteGlTexture();
       this.loadTextureAtlas(var1);
    }

    public void loadTextureAtlas(IResourceManager var1) {
-      int var2 = Minecraft.getGLMaximumTextureSize();
-      Stitcher var3 = new Stitcher(var2, var2, 0, this.mipmapLevels);
+      Config.dbg("Multitexture: " + Config.isMultiTexture());
+      if (Config.isMultiTexture()) {
+         for (TextureAtlasSprite var3 : this.mapUploadedSprites.values()) {
+            var3.deleteSpriteTexture();
+         }
+      }
+
+      ConnectedTextures.updateIcons(this);
+      CustomItems.updateIcons(this);
+      BetterGrass.updateIcons(this);
+      int var34 = TextureUtils.getGLMaximumTextureSize();
+      Stitcher var35 = new Stitcher(var34, var34, 0, this.mipmapLevels);
       this.mapUploadedSprites.clear();
       this.listAnimatedSprites.clear();
       int var4 = Integer.MAX_VALUE;
-      int var5 = 1 << this.mipmapLevels;
+      int var5 = this.getMinSpriteSize();
+      this.iconGridSize = var5;
+      int var6 = 1 << this.mipmapLevels;
+      int var7 = 0;
+      int var8 = 0;
+      SpriteDependencies.reset();
+      ArrayList var9 = new ArrayList<>(this.mapRegisteredSprites.values());
+
+      for (int var10 = 0; var10 < var9.size(); var10++) {
+         TextureAtlasSprite var11 = SpriteDependencies.resolveDependencies(var9, var10, this);
+         ResourceLocation var12 = this.getResourceLocation(var11);
+         IResource var13 = null;
+         var11.updateIndexInMap(this.counterIndexInMap);
+         if (var11.hasCustomLoader(var1, var12)) {
+            if (var11.load(var1, var12, var1x -> this.mapRegisteredSprites.get(var1x.toString()))) {
+               Config.detail("Custom loader (skipped): " + var11);
+               var8++;
+               continue;
+            }

-      for (Entry var7 : this.mapRegisteredSprites.entrySet()) {
-         TextureAtlasSprite var8 = (TextureAtlasSprite)var7.getValue();
-         ResourceLocation var9 = this.getResourceLocation(var8);
-         IResource var10 = null;
+            Config.detail("Custom loader: " + var11);
+            var7++;
+         } else {
+            try {
+               PngSizeInfo var14 = PngSizeInfo.makeFromResource(var1.getResource(var12));
+               var13 = var1.getResource(var12);
+               boolean var15 = var13.getMetadata("animation") != null;
+               var11.loadSprite(var14, var15);
+            } catch (RuntimeException var31) {
+               LOGGER.error("Unable to parse metadata from {}", var12, var31);
+               ReflectorForge.FMLClientHandler_trackBrokenTexture(var12, var31.getMessage());
+               continue;
+            } catch (IOException var32) {
+               LOGGER.error("Using missing texture, unable to load " + var12 + ", " + var32.getClass().getName());
+               ReflectorForge.FMLClientHandler_trackMissingTexture(var12);
+               continue;
+            } finally {
+               IOUtils.closeQuietly(var13);
+            }
+         }

-         try {
-            PngSizeInfo var11 = PngSizeInfo.makeFromResource(var1.getResource(var9));
-            var10 = var1.getResource(var9);
-            boolean var12 = var10.getMetadata("animation") != null;
-            var8.loadSprite(var11, var12);
-         } catch (RuntimeException var22) {
-            LOGGER.error("Unable to parse metadata from {}", var9, var22);
-            continue;
-         } catch (IOException var23) {
-            LOGGER.error("Using missing texture, unable to load {}", var9, var23);
-            continue;
-         } finally {
-            IOUtils.closeQuietly(var10);
-         }
-
-         var4 = Math.min(var4, Math.min(var8.getIconWidth(), var8.getIconHeight()));
-         int var32 = Math.min(Integer.lowestOneBit(var8.getIconWidth()), Integer.lowestOneBit(var8.getIconHeight()));
-         if (var32 < var5) {
-            LOGGER.warn(
-               "Texture {} with size {}x{} limits mip level from {} to {}",
-               var9,
-               var8.getIconWidth(),
-               var8.getIconHeight(),
-               MathHelper.log2(var5),
-               MathHelper.log2(var32)
-            );
-            var5 = var32;
-         }
-
-         var3.addSprite(var8);
-      }
-
-      int var25 = Math.min(var4, var5);
-      int var26 = MathHelper.log2(var25);
-      if (var26 < this.mipmapLevels) {
-         LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.basePath, this.mipmapLevels, var26, var25);
-         this.mipmapLevels = var26;
+         int var42 = var11.getIconWidth();
+         int var46 = var11.getIconHeight();
+         if (var42 >= 1 && var46 >= 1) {
+            if (var42 < var5 || this.mipmapLevels > 0) {
+               int var16 = this.mipmapLevels > 0 ? TextureUtils.scaleToGrid(var42, var5) : TextureUtils.scaleToMin(var42, var5);
+               if (var16 != var42) {
+                  if (!TextureUtils.isPowerOfTwo(var42)) {
+                     Config.log("Scaled non power of 2: " + var11.getIconName() + ", " + var42 + " -> " + var16);
+                  } else {
+                     Config.log("Scaled too small texture: " + var11.getIconName() + ", " + var42 + " -> " + var16);
+                  }
+
+                  int var17 = var46 * var16 / var42;
+                  var11.setIconWidth(var16);
+                  var11.setIconHeight(var17);
+               }
+            }
+
+            var4 = Math.min(var4, Math.min(var11.getIconWidth(), var11.getIconHeight()));
+            int var49 = Math.min(Integer.lowestOneBit(var11.getIconWidth()), Integer.lowestOneBit(var11.getIconHeight()));
+            if (var49 < var6) {
+               LOGGER.warn(
+                  "Texture {} with size {}x{} limits mip level from {} to {}",
+                  var12,
+                  var11.getIconWidth(),
+                  var11.getIconHeight(),
+                  MathHelper.log2(var6),
+                  MathHelper.log2(var49)
+               );
+               var6 = var49;
+            }
+
+            if (this.generateMipmaps(var1, var11)) {
+               var35.addSprite(var11);
+            }
+         } else {
+            Config.warn("Invalid sprite size: " + var11);
+         }
+      }
+
+      if (var7 > 0) {
+         Config.dbg("Custom loader sprites: " + var7);
+      }
+
+      if (var8 > 0) {
+         Config.dbg("Custom loader sprites (skipped): " + var8);
+      }
+
+      if (SpriteDependencies.getCountDependencies() > 0) {
+         Config.dbg("Sprite dependencies: " + SpriteDependencies.getCountDependencies());
+      }
+
+      int var36 = Math.min(var4, var6);
+      int var37 = MathHelper.log2(var36);
+      if (var37 < 0) {
+         var37 = 0;
+      }
+
+      if (var37 < this.mipmapLevels) {
+         LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.basePath, this.mipmapLevels, var37, var36);
+         this.mipmapLevels = var37;
       }

       this.missingImage.generateMipmaps(this.mipmapLevels);
-      var3.addSprite(this.missingImage);
+      var35.addSprite(this.missingImage);

       try {
-         var3.doStitch();
-      } catch (StitcherException var21) {
-         throw var21;
+         var35.doStitch();
+      } catch (StitcherException var30) {
+         throw var30;
       }

-      LOGGER.info("Created: {}x{} {}-atlas", var3.getCurrentWidth(), var3.getCurrentHeight(), this.basePath);
-      TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, var3.getCurrentWidth(), var3.getCurrentHeight());
-      HashMap var27 = Maps.newHashMap(this.mapRegisteredSprites);
-
-      for (TextureAtlasSprite var30 : var3.getStichSlots()) {
-         if (var30 == this.missingImage || this.generateMipmaps(var1, var30)) {
-            String var33 = var30.getIconName();
-            var27.remove(var33);
-            this.mapUploadedSprites.put(var33, var30);
+      LOGGER.info("Created: {}x{} {}-atlas", var35.getCurrentWidth(), var35.getCurrentHeight(), this.basePath);
+      if (Config.isShaders()) {
+         ShadersTex.allocateTextureMap(this.getGlTextureId(), this.mipmapLevels, var35.getCurrentWidth(), var35.getCurrentHeight(), var35, this);
+      } else {
+         TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, var35.getCurrentWidth(), var35.getCurrentHeight());
+      }

-            try {
+      HashMap var38 = Maps.newHashMap(this.mapRegisteredSprites);
+
+      for (TextureAtlasSprite var43 : var35.getStichSlots()) {
+         String var47 = var43.getIconName();
+         var38.remove(var47);
+         this.mapUploadedSprites.put(var47, var43);
+
+         try {
+            if (Config.isShaders()) {
+               ShadersTex.uploadTexSubForLoadAtlas(
+                  this,
+                  var43.getIconName(),
+                  var43.getFrameTextureData(0),
+                  var43.getIconWidth(),
+                  var43.getIconHeight(),
+                  var43.getOriginX(),
+                  var43.getOriginY(),
+                  false,
+                  false
+               );
+            } else {
                TextureUtil.uploadTextureMipmap(
-                  var30.getFrameTextureData(0), var30.getIconWidth(), var30.getIconHeight(), var30.getOriginX(), var30.getOriginY(), false, false
+                  var43.getFrameTextureData(0), var43.getIconWidth(), var43.getIconHeight(), var43.getOriginX(), var43.getOriginY(), false, false
                );
-            } catch (Throwable var20) {
-               CrashReport var13 = CrashReport.makeCrashReport(var20, "Stitching texture atlas");
-               CrashReportCategory var14 = var13.makeCategory("Texture being stitched together");
-               var14.addCrashSection("Atlas path", this.basePath);
-               var14.addCrashSection("Sprite", var30);
-               throw new ReportedException(var13);
             }
+         } catch (Throwable var29) {
+            CrashReport var51 = CrashReport.makeCrashReport(var29, "Stitching texture atlas");
+            CrashReportCategory var18 = var51.makeCategory("Texture being stitched together");
+            var18.addCrashSection("Atlas path", this.basePath);
+            var18.addCrashSection("Sprite", var43);
+            throw new ReportedException(var51);
+         }
+
+         if (var43.hasAnimationMetadata()) {
+            var43.setAnimationIndex(this.listAnimatedSprites.size());
+            this.listAnimatedSprites.add(var43);
+         }
+      }
+
+      for (TextureAtlasSprite var44 : var38.values()) {
+         var44.copyFrom(this.missingImage);
+      }

-            if (var30.hasAnimationMetadata()) {
-               this.listAnimatedSprites.add(var30);
+      Config.log("Animated sprites: " + this.listAnimatedSprites.size());
+      if (Config.isMultiTexture()) {
+         int var41 = var35.getCurrentWidth();
+         int var45 = var35.getCurrentHeight();
+
+         for (TextureAtlasSprite var52 : var35.getStichSlots()) {
+            var52.sheetWidth = var41;
+            var52.sheetHeight = var45;
+            var52.mipmapLevels = this.mipmapLevels;
+            TextureAtlasSprite var53 = var52.spriteSingle;
+            if (var53 != null) {
+               if (var53.getIconWidth() <= 0) {
+                  var53.setIconWidth(var52.getIconWidth());
+                  var53.setIconHeight(var52.getIconHeight());
+                  var53.initSprite(var52.getIconWidth(), var52.getIconHeight(), 0, 0, false);
+                  var53.clearFramesTextureData();
+                  List var19 = var52.getFramesTextureData();
+                  var53.setFramesTextureData(var19);
+                  var53.setAnimationMetadata(var52.getAnimationMetadata());
+               }
+
+               var53.sheetWidth = var41;
+               var53.sheetHeight = var45;
+               var53.mipmapLevels = this.mipmapLevels;
+               var53.setAnimationIndex(var52.getAnimationIndex());
+               var52.bindSpriteTexture();
+               boolean var54 = false;
+               boolean var20 = true;
+
+               try {
+                  TextureUtil.uploadTextureMipmap(
+                     var53.getFrameTextureData(0), var53.getIconWidth(), var53.getIconHeight(), var53.getOriginX(), var53.getOriginY(), var54, var20
+                  );
+               } catch (Exception var28) {
+                  Config.dbg("Error uploading sprite single: " + var53 + ", parent: " + var52);
+                  var28.printStackTrace();
+               }
             }
          }
+
+         Config.getMinecraft().getTextureManager().bindTexture(LOCATION_BLOCKS_TEXTURE);
       }

-      for (TextureAtlasSprite var31 : var27.values()) {
-         var31.copyFrom(this.missingImage);
+      Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, new Object[]{this});
+      this.updateIconGrid(var35.getCurrentWidth(), var35.getCurrentHeight());
+      if (Config.equals(System.getProperty("saveTextureMap"), "true")) {
+         Config.dbg("Exporting texture map: " + this.basePath);
+         TextureUtils.saveGlTexture(
+            "debug/" + this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, var35.getCurrentWidth(), var35.getCurrentHeight()
+         );
       }
    }

-   private boolean generateMipmaps(IResourceManager var1, final TextureAtlasSprite var2) {
+   public boolean generateMipmaps(IResourceManager var1, final TextureAtlasSprite var2) {
       ResourceLocation var3 = this.getResourceLocation(var2);
       IResource var4 = null;
+      if (var2.hasCustomLoader(var1, var3)) {
+         TextureUtils.generateCustomMipmaps(var2, this.mipmapLevels);
+      } else {
+         label60: {
+            boolean var7;
+            try {
+               var4 = var1.getResource(var3);
+               var2.loadSpriteFrames(var4, this.mipmapLevels + 1);
+               break label60;
+            } catch (RuntimeException var14) {
+               LOGGER.error("Unable to parse metadata from {}", var3, var14);
+               return false;
+            } catch (IOException var15) {
+               LOGGER.error("Using missing texture, unable to load {}", var3, var15);
+               boolean var5 = false;
+               var7 = var5;
+            } finally {
+               IOUtils.closeQuietly(var4);
+            }

-      label45: {
-         boolean var6;
-         try {
-            var4 = var1.getResource(var3);
-            var2.loadSpriteFrames(var4, this.mipmapLevels + 1);
-            break label45;
-         } catch (RuntimeException var13) {
-            LOGGER.error("Unable to parse metadata from {}", var3, var13);
-            return false;
-         } catch (IOException var14) {
-            LOGGER.error("Using missing texture, unable to load {}", var3, var14);
-            var6 = false;
-         } finally {
-            IOUtils.closeQuietly(var4);
+            return var7;
          }
-
-         return var6;
       }

       try {
          var2.generateMipmaps(this.mipmapLevels);
          return true;
-      } catch (Throwable var12) {
-         CrashReport var17 = CrashReport.makeCrashReport(var12, "Applying mipmap");
-         CrashReportCategory var7 = var17.makeCategory("Sprite being mipmapped");
-         var7.addDetail("Sprite name", new ICrashReportDetail<String>() {
+      } catch (Throwable var13) {
+         CrashReport var6 = CrashReport.makeCrashReport(var13, "Applying mipmap");
+         CrashReportCategory var18 = var6.makeCategory("Sprite being mipmapped");
+         var18.addDetail("Sprite name", new ICrashReportDetail<String>() {
             public String call() throws Exception {
                return var2.getIconName();
             }
          });
-         var7.addDetail("Sprite size", new ICrashReportDetail<String>() {
+         var18.addDetail("Sprite size", new ICrashReportDetail<String>() {
             public String call() throws Exception {
                return var2.getIconWidth() + " x " + var2.getIconHeight();
             }
          });
-         var7.addDetail("Sprite frames", new ICrashReportDetail<String>() {
+         var18.addDetail("Sprite frames", new ICrashReportDetail<String>() {
             public String call() throws Exception {
                return var2.getFrameCount() + " frames";
             }
          });
-         var7.addCrashSection("Mipmap levels", this.mipmapLevels);
-         throw new ReportedException(var17);
+         var18.addCrashSection("Mipmap levels", this.mipmapLevels);
+         throw new ReportedException(var6);
       }
    }

-   private ResourceLocation getResourceLocation(TextureAtlasSprite var1) {
+   public ResourceLocation getResourceLocation(TextureAtlasSprite var1) {
       ResourceLocation var2 = new ResourceLocation(var1.getIconName());
-      return new ResourceLocation(var2.getNamespace(), String.format("%s/%s%s", this.basePath, var2.getPath(), ".png"));
+      return this.completeResourceLocation(var2);
+   }
+
+   public ResourceLocation completeResourceLocation(ResourceLocation var1) {
+      return this.isAbsoluteLocation(var1)
+         ? new ResourceLocation(var1.getNamespace(), var1.getPath() + ".png")
+         : new ResourceLocation(var1.getNamespace(), String.format("%s/%s%s", this.basePath, var1.getPath(), ".png"));
    }

    public TextureAtlasSprite getAtlasSprite(String var1) {
       TextureAtlasSprite var2 = this.mapUploadedSprites.get(var1);
       if (var2 == null) {
          var2 = this.missingImage;
       }

       return var2;
    }

    public void updateAnimations() {
+      boolean var1 = false;
+      boolean var2 = false;
       TextureUtil.bindTexture(this.getGlTextureId());
+      int var3 = 0;
+
+      for (TextureAtlasSprite var5 : this.listAnimatedSprites) {
+         if (this.isTerrainAnimationActive(var5)) {
+            var5.updateAnimation();
+            if (var5.isAnimationActive()) {
+               var3++;
+            }
+
+            if (var5.spriteNormal != null) {
+               var1 = true;
+            }
+
+            if (var5.spriteSpecular != null) {
+               var2 = true;
+            }
+         }
+      }
+
+      if (Config.isMultiTexture()) {
+         for (TextureAtlasSprite var11 : this.listAnimatedSprites) {
+            if (this.isTerrainAnimationActive(var11)) {
+               TextureAtlasSprite var6 = var11.spriteSingle;
+               if (var6 != null) {
+                  if (var11 == TextureUtils.iconClock || var11 == TextureUtils.iconCompass) {
+                     var6.frameCounter = var11.frameCounter;
+                  }
+
+                  var11.bindSpriteTexture();
+                  var6.updateAnimation();
+                  if (var6.isAnimationActive()) {
+                     var3++;
+                  }
+               }
+            }
+         }
+
+         TextureUtil.bindTexture(this.getGlTextureId());
+      }
+
+      if (Config.isShaders()) {
+         if (var1) {
+            TextureUtil.bindTexture(this.getMultiTexID().norm);
+
+            for (TextureAtlasSprite var12 : this.listAnimatedSprites) {
+               if (var12.spriteNormal != null && this.isTerrainAnimationActive(var12)) {
+                  if (var12 == TextureUtils.iconClock || var12 == TextureUtils.iconCompass) {
+                     var12.spriteNormal.frameCounter = var12.frameCounter;
+                  }
+
+                  var12.spriteNormal.updateAnimation();
+                  if (var12.spriteNormal.isAnimationActive()) {
+                     var3++;
+                  }
+               }
+            }
+         }
+
+         if (var2) {
+            TextureUtil.bindTexture(this.getMultiTexID().spec);
+
+            for (TextureAtlasSprite var13 : this.listAnimatedSprites) {
+               if (var13.spriteSpecular != null && this.isTerrainAnimationActive(var13)) {
+                  if (var13 == TextureUtils.iconClock || var13 == TextureUtils.iconCompass) {
+                     var13.spriteNormal.frameCounter = var13.frameCounter;
+                  }
+
+                  var13.spriteSpecular.updateAnimation();
+                  if (var13.spriteSpecular.isAnimationActive()) {
+                     var3++;
+                  }
+               }
+            }
+         }

-      for (TextureAtlasSprite var2 : this.listAnimatedSprites) {
-         var2.updateAnimation();
+         if (var1 || var2) {
+            TextureUtil.bindTexture(this.getGlTextureId());
+         }
+      }
+
+      int var10 = Config.getMinecraft().entityRenderer.frameCount;
+      if (var10 != this.frameCountAnimations) {
+         this.countAnimationsActive = var3;
+         this.frameCountAnimations = var10;
+      }
+
+      if (SmartAnimations.isActive()) {
+         SmartAnimations.resetSpritesRendered();
       }
    }

    public TextureAtlasSprite registerSprite(ResourceLocation var1) {
       if (var1 == null) {
          throw new IllegalArgumentException("Location cannot be null!");
       } else {
-         TextureAtlasSprite var2 = this.mapRegisteredSprites.get(var1);
+         TextureAtlasSprite var2 = this.mapRegisteredSprites.get(var1.toString());
          if (var2 == null) {
             var2 = TextureAtlasSprite.makeAtlasSprite(var1);
             this.mapRegisteredSprites.put(var1.toString(), var2);
+            var2.updateIndexInMap(this.counterIndexInMap);
+            if (Config.isEmissiveTextures()) {
+               this.checkEmissive(var1, var2);
+            }
          }

          return var2;
       }
    }

@@ -252,8 +540,247 @@
    public void setMipmapLevels(int var1) {
       this.mipmapLevels = var1;
    }

    public TextureAtlasSprite getMissingSprite() {
       return this.missingImage;
+   }
+
+   @Nullable
+   public TextureAtlasSprite getTextureExtry(String var1) {
+      return this.mapRegisteredSprites.get(var1);
+   }
+
+   public boolean setTextureEntry(TextureAtlasSprite var1) {
+      String var2 = var1.getIconName();
+      if (!this.mapRegisteredSprites.containsKey(var2)) {
+         this.mapRegisteredSprites.put(var2, var1);
+         var1.updateIndexInMap(this.counterIndexInMap);
+         return true;
+      } else {
+         return false;
+      }
+   }
+
+   public String getBasePath() {
+      return this.basePath;
+   }
+
+   public int getMipmapLevels() {
+      return this.mipmapLevels;
+   }
+
+   private boolean isAbsoluteLocation(ResourceLocation var1) {
+      String var2 = var1.getPath();
+      return this.isAbsoluteLocationPath(var2);
+   }
+
+   private boolean isAbsoluteLocationPath(String var1) {
+      String var2 = var1.toLowerCase();
+      return var2.startsWith("mcpatcher/") || var2.startsWith("optifine/");
+   }
+
+   public TextureAtlasSprite getSpriteSafe(String var1) {
+      ResourceLocation var2 = new ResourceLocation(var1);
+      return this.mapRegisteredSprites.get(var2.toString());
+   }
+
+   public TextureAtlasSprite getRegisteredSprite(ResourceLocation var1) {
+      return this.mapRegisteredSprites.get(var1.toString());
+   }
+
+   private boolean isTerrainAnimationActive(TextureAtlasSprite var1) {
+      if (var1 == TextureUtils.iconWaterStill || var1 == TextureUtils.iconWaterFlow) {
+         return Config.isAnimatedWater();
+      } else if (var1 == TextureUtils.iconLavaStill || var1 == TextureUtils.iconLavaFlow) {
+         return Config.isAnimatedLava();
+      } else if (var1 == TextureUtils.iconFireLayer0 || var1 == TextureUtils.iconFireLayer1) {
+         return Config.isAnimatedFire();
+      } else if (var1 == TextureUtils.iconPortal) {
+         return Config.isAnimatedPortal();
+      } else {
+         return var1 != TextureUtils.iconClock && var1 != TextureUtils.iconCompass ? Config.isAnimatedTerrain() : true;
+      }
+   }
+
+   public int getCountRegisteredSprites() {
+      return this.counterIndexInMap.getValue();
+   }
+
+   private int detectMaxMipmapLevel(Map var1, IResourceManager var2) {
+      int var3 = this.detectMinimumSpriteSize(var1, var2, 20);
+      if (var3 < 16) {
+         var3 = 16;
+      }
+
+      var3 = MathHelper.smallestEncompassingPowerOfTwo(var3);
+      if (var3 > 16) {
+         Config.log("Sprite size: " + var3);
+      }
+
+      int var4 = MathHelper.log2(var3);
+      if (var4 < 4) {
+         var4 = 4;
+      }
+
+      return var4;
+   }
+
+   private int detectMinimumSpriteSize(Map var1, IResourceManager var2, int var3) {
+      HashMap var4 = new HashMap();
+
+      for (Entry var7 : var1.entrySet()) {
+         TextureAtlasSprite var8 = (TextureAtlasSprite)var7.getValue();
+         ResourceLocation var9 = new ResourceLocation(var8.getIconName());
+         ResourceLocation var10 = this.completeResourceLocation(var9);
+         if (!var8.hasCustomLoader(var2, var9)) {
+            try {
+               IResource var11 = var2.getResource(var10);
+               if (var11 != null) {
+                  InputStream var12 = var11.getInputStream();
+                  if (var12 != null) {
+                     Dimension var13 = TextureUtils.getImageSize(var12, "png");
+                     var12.close();
+                     if (var13 != null) {
+                        int var14 = var13.width;
+                        int var15 = MathHelper.smallestEncompassingPowerOfTwo(var14);
+                        if (!var4.containsKey(var15)) {
+                           var4.put(var15, 1);
+                        } else {
+                           int var16 = (Integer)var4.get(var15);
+                           var4.put(var15, var16 + 1);
+                        }
+                     }
+                  }
+               }
+            } catch (Exception var17) {
+            }
+         }
+      }
+
+      int var18 = 0;
+      Set var19 = var4.keySet();
+      TreeSet var20 = new TreeSet(var19);
+
+      for (int var23 : var20) {
+         int var25 = (Integer)var4.get(var23);
+         var18 += var25;
+      }
+
+      int var22 = 16;
+      int var24 = 0;
+      int var26 = var18 * var3 / 100;
+
+      for (int var28 : var20) {
+         int var29 = (Integer)var4.get(var28);
+         var24 += var29;
+         if (var28 > var22) {
+            var22 = var28;
+         }
+
+         if (var24 > var26) {
+            return var22;
+         }
+      }
+
+      return var22;
+   }
+
+   private int getMinSpriteSize() {
+      int var1 = 1 << this.mipmapLevels;
+      if (var1 < 8) {
+         var1 = 8;
+      }
+
+      return var1;
+   }
+
+   private int[] getMissingImageData(int var1) {
+      BufferedImage var2 = new BufferedImage(16, 16, 2);
+      var2.setRGB(0, 0, 16, 16, TextureUtil.MISSING_TEXTURE_DATA, 0, 16);
+      BufferedImage var3 = TextureUtils.scaleImage(var2, var1);
+      int[] var4 = new int[var1 * var1];
+      var3.getRGB(0, 0, var1, var1, var4, 0, var1);
+      return var4;
+   }
+
+   public boolean isTextureBound() {
+      int var1 = GlStateManager.getBoundTexture();
+      int var2 = this.getGlTextureId();
+      return var1 == var2;
+   }
+
+   private void updateIconGrid(int var1, int var2) {
+      this.iconGridCountX = -1;
+      this.iconGridCountY = -1;
+      this.iconGrid = null;
+      if (this.iconGridSize > 0) {
+         this.iconGridCountX = var1 / this.iconGridSize;
+         this.iconGridCountY = var2 / this.iconGridSize;
+         this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
+         this.iconGridSizeU = 1.0 / this.iconGridCountX;
+         this.iconGridSizeV = 1.0 / this.iconGridCountY;
+
+         for (TextureAtlasSprite var4 : this.mapUploadedSprites.values()) {
+            double var5 = 0.5 / var1;
+            double var7 = 0.5 / var2;
+            double var9 = Math.min(var4.getMinU(), var4.getMaxU()) + var5;
+            double var11 = Math.min(var4.getMinV(), var4.getMaxV()) + var7;
+            double var13 = Math.max(var4.getMinU(), var4.getMaxU()) - var5;
+            double var15 = Math.max(var4.getMinV(), var4.getMaxV()) - var7;
+            int var17 = (int)(var9 / this.iconGridSizeU);
+            int var18 = (int)(var11 / this.iconGridSizeV);
+            int var19 = (int)(var13 / this.iconGridSizeU);
+            int var20 = (int)(var15 / this.iconGridSizeV);
+
+            for (int var21 = var17; var21 <= var19; var21++) {
+               if (var21 >= 0 && var21 < this.iconGridCountX) {
+                  for (int var22 = var18; var22 <= var20; var22++) {
+                     if (var22 >= 0 && var22 < this.iconGridCountX) {
+                        int var23 = var22 * this.iconGridCountX + var21;
+                        this.iconGrid[var23] = var4;
+                     } else {
+                        Config.warn("Invalid grid V: " + var22 + ", icon: " + var4.getIconName());
+                     }
+                  }
+               } else {
+                  Config.warn("Invalid grid U: " + var21 + ", icon: " + var4.getIconName());
+               }
+            }
+         }
+      }
+   }
+
+   public TextureAtlasSprite getIconByUV(double var1, double var3) {
+      if (this.iconGrid == null) {
+         return null;
+      } else {
+         int var5 = (int)(var1 / this.iconGridSizeU);
+         int var6 = (int)(var3 / this.iconGridSizeV);
+         int var7 = var6 * this.iconGridCountX + var5;
+         return var7 >= 0 && var7 <= this.iconGrid.length ? this.iconGrid[var7] : null;
+      }
+   }
+
+   private void checkEmissive(ResourceLocation var1, TextureAtlasSprite var2) {
+      String var3 = EmissiveTextures.getSuffixEmissive();
+      if (var3 != null) {
+         if (!var1.getPath().endsWith(var3)) {
+            ResourceLocation var4 = new ResourceLocation(var1.getNamespace(), var1.getPath() + var3);
+            ResourceLocation var5 = this.completeResourceLocation(var4);
+            if (Config.hasResource(var5)) {
+               TextureAtlasSprite var6 = this.registerSprite(var4);
+               var6.isEmissive = true;
+               var2.spriteEmissive = var6;
+            }
+         }
+      }
+   }
+
+   public int getCountAnimations() {
+      return this.listAnimatedSprites.size();
+   }
+
+   public int getCountAnimationsActive() {
+      return this.countAnimationsActive;
    }
 }
 */
