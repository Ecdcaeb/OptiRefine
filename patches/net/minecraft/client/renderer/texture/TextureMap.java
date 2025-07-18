package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.optifine.BetterGrass;
import net.optifine.ConnectedTextures;
import net.optifine.CustomItems;
import net.optifine.EmissiveTextures;
import net.optifine.SmartAnimations;
import net.optifine.SpriteDependencies;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.shaders.ShadersTex;
import net.optifine.util.CounterInt;
import net.optifine.util.TextureUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureMap extends AbstractTexture implements ITickableTextureObject {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
   public static final ResourceLocation LOCATION_BLOCKS_TEXTURE = new ResourceLocation("textures/atlas/blocks.png");
   private final List<TextureAtlasSprite> listAnimatedSprites;
   private final Map<String, TextureAtlasSprite> mapRegisteredSprites;
   private final Map<String, TextureAtlasSprite> mapUploadedSprites;
   private final String basePath;
   private final ITextureMapPopulator iconCreator;
   private int mipmapLevels;
   private final TextureAtlasSprite missingImage;
   private TextureAtlasSprite[] iconGrid = null;
   private int iconGridSize = -1;
   private int iconGridCountX = -1;
   private int iconGridCountY = -1;
   private double iconGridSizeU = -1.0;
   private double iconGridSizeV = -1.0;
   private CounterInt counterIndexInMap = new CounterInt(0);
   public int atlasWidth = 0;
   public int atlasHeight = 0;
   private int countAnimationsActive;
   private int frameCountAnimations;

   public TextureMap(String basePathIn) {
      this(basePathIn, (ITextureMapPopulator)null);
   }

   public TextureMap(String basePathIn, boolean skipFirst) {
      this(basePathIn, (ITextureMapPopulator)null, skipFirst);
   }

   public TextureMap(String basePathIn, @Nullable ITextureMapPopulator iconCreatorIn) {
      this(basePathIn, iconCreatorIn, false);
   }

   public TextureMap(String basePathIn, ITextureMapPopulator iconCreatorIn, boolean skipFirst) {
      this.listAnimatedSprites = Lists.newArrayList();
      this.mapRegisteredSprites = Maps.newHashMap();
      this.mapUploadedSprites = Maps.newHashMap();
      this.missingImage = new TextureAtlasSprite("missingno");
      this.basePath = basePathIn;
      this.iconCreator = iconCreatorIn;
   }

   private void initMissingImage() {
      int size = this.getMinSpriteSize();
      int[] aint = this.getMissingImageData(size);
      this.missingImage.setIconWidth(size);
      this.missingImage.setIconHeight(size);
      int[][] aint1 = new int[this.mipmapLevels + 1][];
      aint1[0] = aint;
      this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][]{aint1}));
      this.missingImage.setIndexInMap(this.counterIndexInMap.nextValue());
   }

   @Override
   public void loadTexture(IResourceManager resourceManager) throws IOException {
      if (this.iconCreator != null) {
         this.loadSprites(resourceManager, this.iconCreator);
      }
   }

   public void loadSprites(IResourceManager resourceManager, ITextureMapPopulator iconCreatorIn) {
      this.mapRegisteredSprites.clear();
      this.counterIndexInMap.reset();
      Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, new Object[]{this});
      iconCreatorIn.registerSprites(this);
      if (this.mipmapLevels >= 4) {
         this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, resourceManager);
         Config.log("Mipmap levels: " + this.mipmapLevels);
      }

      this.initMissingImage();
      this.deleteGlTexture();
      this.loadTextureAtlas(resourceManager);
   }

   public void loadTextureAtlas(IResourceManager resourceManager) {
      Config.dbg("Multitexture: " + Config.isMultiTexture());
      if (Config.isMultiTexture()) {
         for (TextureAtlasSprite ts : this.mapUploadedSprites.values()) {
            ts.deleteSpriteTexture();
         }
      }

      ConnectedTextures.updateIcons(this);
      CustomItems.updateIcons(this);
      BetterGrass.updateIcons(this);
      int i = TextureUtils.getGLMaximumTextureSize();
      Stitcher stitcher = new Stitcher(i, i, 0, this.mipmapLevels);
      this.mapUploadedSprites.clear();
      this.listAnimatedSprites.clear();
      int j = Integer.MAX_VALUE;
      int minSpriteSize = this.getMinSpriteSize();
      this.iconGridSize = minSpriteSize;
      int k = 1 << this.mipmapLevels;
      int countCustomLoader = 0;
      int countCustomLoaderSkipped = 0;
      SpriteDependencies.reset();
      List<TextureAtlasSprite> listRegisteredSprites = new ArrayList<>(this.mapRegisteredSprites.values());

      for (int ix = 0; ix < listRegisteredSprites.size(); ix++) {
         TextureAtlasSprite textureatlassprite = SpriteDependencies.resolveDependencies(listRegisteredSprites, ix, this);
         ResourceLocation resourcelocation = this.getResourceLocation(textureatlassprite);
         IResource iresource = null;
         textureatlassprite.updateIndexInMap(this.counterIndexInMap);
         if (textureatlassprite.hasCustomLoader(resourceManager, resourcelocation)) {
            if (textureatlassprite.load(resourceManager, resourcelocation, lx -> this.mapRegisteredSprites.get(lx.toString()))) {
               Config.detail("Custom loader (skipped): " + textureatlassprite);
               countCustomLoaderSkipped++;
               continue;
            }

            Config.detail("Custom loader: " + textureatlassprite);
            countCustomLoader++;
         } else {
            try {
               PngSizeInfo pngsizeinfo = PngSizeInfo.makeFromResource(resourceManager.getResource(resourcelocation));
               iresource = resourceManager.getResource(resourcelocation);
               boolean flag = iresource.getMetadata("animation") != null;
               textureatlassprite.loadSprite(pngsizeinfo, flag);
            } catch (RuntimeException var31) {
               LOGGER.error("Unable to parse metadata from {}", resourcelocation, var31);
               ReflectorForge.FMLClientHandler_trackBrokenTexture(resourcelocation, var31.getMessage());
               continue;
            } catch (IOException var32) {
               LOGGER.error("Using missing texture, unable to load " + resourcelocation + ", " + var32.getClass().getName());
               ReflectorForge.FMLClientHandler_trackMissingTexture(resourcelocation);
               continue;
            } finally {
               IOUtils.closeQuietly(iresource);
            }
         }

         int ws = textureatlassprite.getIconWidth();
         int hs = textureatlassprite.getIconHeight();
         if (ws >= 1 && hs >= 1) {
            if (ws < minSpriteSize || this.mipmapLevels > 0) {
               int ws2 = this.mipmapLevels > 0 ? TextureUtils.scaleToGrid(ws, minSpriteSize) : TextureUtils.scaleToMin(ws, minSpriteSize);
               if (ws2 != ws) {
                  if (!TextureUtils.isPowerOfTwo(ws)) {
                     Config.log("Scaled non power of 2: " + textureatlassprite.getIconName() + ", " + ws + " -> " + ws2);
                  } else {
                     Config.log("Scaled too small texture: " + textureatlassprite.getIconName() + ", " + ws + " -> " + ws2);
                  }

                  int hs2 = hs * ws2 / ws;
                  textureatlassprite.setIconWidth(ws2);
                  textureatlassprite.setIconHeight(hs2);
               }
            }

            j = Math.min(j, Math.min(textureatlassprite.getIconWidth(), textureatlassprite.getIconHeight()));
            int j1 = Math.min(Integer.lowestOneBit(textureatlassprite.getIconWidth()), Integer.lowestOneBit(textureatlassprite.getIconHeight()));
            if (j1 < k) {
               LOGGER.warn(
                  "Texture {} with size {}x{} limits mip level from {} to {}",
                  resourcelocation,
                  textureatlassprite.getIconWidth(),
                  textureatlassprite.getIconHeight(),
                  MathHelper.log2(k),
                  MathHelper.log2(j1)
               );
               k = j1;
            }

            if (this.generateMipmaps(resourceManager, textureatlassprite)) {
               stitcher.addSprite(textureatlassprite);
            }
         } else {
            Config.warn("Invalid sprite size: " + textureatlassprite);
         }
      }

      if (countCustomLoader > 0) {
         Config.dbg("Custom loader sprites: " + countCustomLoader);
      }

      if (countCustomLoaderSkipped > 0) {
         Config.dbg("Custom loader sprites (skipped): " + countCustomLoaderSkipped);
      }

      if (SpriteDependencies.getCountDependencies() > 0) {
         Config.dbg("Sprite dependencies: " + SpriteDependencies.getCountDependencies());
      }

      int l = Math.min(j, k);
      int i1 = MathHelper.log2(l);
      if (i1 < 0) {
         i1 = 0;
      }

      if (i1 < this.mipmapLevels) {
         LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.basePath, this.mipmapLevels, i1, l);
         this.mipmapLevels = i1;
      }

      this.missingImage.generateMipmaps(this.mipmapLevels);
      stitcher.addSprite(this.missingImage);

      try {
         stitcher.doStitch();
      } catch (StitcherException var30) {
         throw var30;
      }

      LOGGER.info("Created: {}x{} {}-atlas", stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), this.basePath);
      if (Config.isShaders()) {
         ShadersTex.allocateTextureMap(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), stitcher, this);
      } else {
         TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
      }

      Map<String, TextureAtlasSprite> map = Maps.newHashMap(this.mapRegisteredSprites);

      for (TextureAtlasSprite textureatlassprite1 : stitcher.getStichSlots()) {
         String s = textureatlassprite1.getIconName();
         map.remove(s);
         this.mapUploadedSprites.put(s, textureatlassprite1);

         try {
            if (Config.isShaders()) {
               ShadersTex.uploadTexSubForLoadAtlas(
                  this,
                  textureatlassprite1.getIconName(),
                  textureatlassprite1.getFrameTextureData(0),
                  textureatlassprite1.getIconWidth(),
                  textureatlassprite1.getIconHeight(),
                  textureatlassprite1.getOriginX(),
                  textureatlassprite1.getOriginY(),
                  false,
                  false
               );
            } else {
               TextureUtil.uploadTextureMipmap(
                  textureatlassprite1.getFrameTextureData(0),
                  textureatlassprite1.getIconWidth(),
                  textureatlassprite1.getIconHeight(),
                  textureatlassprite1.getOriginX(),
                  textureatlassprite1.getOriginY(),
                  false,
                  false
               );
            }
         } catch (Throwable var29) {
            CrashReport crashreport = CrashReport.makeCrashReport(var29, "Stitching texture atlas");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Texture being stitched together");
            crashreportcategory.addCrashSection("Atlas path", this.basePath);
            crashreportcategory.addCrashSection("Sprite", textureatlassprite1);
            throw new ReportedException(crashreport);
         }

         if (textureatlassprite1.hasAnimationMetadata()) {
            textureatlassprite1.setAnimationIndex(this.listAnimatedSprites.size());
            this.listAnimatedSprites.add(textureatlassprite1);
         }
      }

      for (TextureAtlasSprite textureatlassprite2 : map.values()) {
         textureatlassprite2.copyFrom(this.missingImage);
      }

      Config.log("Animated sprites: " + this.listAnimatedSprites.size());
      if (Config.isMultiTexture()) {
         int sheetWidth = stitcher.getCurrentWidth();
         int sheetHeight = stitcher.getCurrentHeight();

         for (TextureAtlasSprite tas : stitcher.getStichSlots()) {
            tas.sheetWidth = sheetWidth;
            tas.sheetHeight = sheetHeight;
            tas.mipmapLevels = this.mipmapLevels;
            TextureAtlasSprite ss = tas.spriteSingle;
            if (ss != null) {
               if (ss.getIconWidth() <= 0) {
                  ss.setIconWidth(tas.getIconWidth());
                  ss.setIconHeight(tas.getIconHeight());
                  ss.initSprite(tas.getIconWidth(), tas.getIconHeight(), 0, 0, false);
                  ss.clearFramesTextureData();
                  List<int[][]> frameDatas = tas.getFramesTextureData();
                  ss.setFramesTextureData(frameDatas);
                  ss.setAnimationMetadata(tas.getAnimationMetadata());
               }

               ss.sheetWidth = sheetWidth;
               ss.sheetHeight = sheetHeight;
               ss.mipmapLevels = this.mipmapLevels;
               ss.setAnimationIndex(tas.getAnimationIndex());
               tas.bindSpriteTexture();
               boolean texBlur = false;
               boolean texClamp = true;

               try {
                  TextureUtil.uploadTextureMipmap(
                     ss.getFrameTextureData(0), ss.getIconWidth(), ss.getIconHeight(), ss.getOriginX(), ss.getOriginY(), texBlur, texClamp
                  );
               } catch (Exception var28) {
                  Config.dbg("Error uploading sprite single: " + ss + ", parent: " + tas);
                  var28.printStackTrace();
               }
            }
         }

         Config.getMinecraft().getTextureManager().bindTexture(LOCATION_BLOCKS_TEXTURE);
      }

      Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, new Object[]{this});
      this.updateIconGrid(stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
      if (Config.equals(System.getProperty("saveTextureMap"), "true")) {
         Config.dbg("Exporting texture map: " + this.basePath);
         TextureUtils.saveGlTexture(
            "debug/" + this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight()
         );
      }
   }

   public boolean generateMipmaps(IResourceManager resourceManager, final TextureAtlasSprite texture) {
      ResourceLocation resourcelocation = this.getResourceLocation(texture);
      IResource iresource = null;
      if (texture.hasCustomLoader(resourceManager, resourcelocation)) {
         TextureUtils.generateCustomMipmaps(texture, this.mipmapLevels);
      } else {
         label60: {
            boolean crashreportcategory;
            try {
               iresource = resourceManager.getResource(resourcelocation);
               texture.loadSpriteFrames(iresource, this.mipmapLevels + 1);
               break label60;
            } catch (RuntimeException var14) {
               LOGGER.error("Unable to parse metadata from {}", resourcelocation, var14);
               return false;
            } catch (IOException var15) {
               LOGGER.error("Using missing texture, unable to load {}", resourcelocation, var15);
               boolean flag = false;
               crashreportcategory = flag;
            } finally {
               IOUtils.closeQuietly(iresource);
            }

            return crashreportcategory;
         }
      }

      try {
         texture.generateMipmaps(this.mipmapLevels);
         return true;
      } catch (Throwable var13) {
         CrashReport crashreport = CrashReport.makeCrashReport(var13, "Applying mipmap");
         CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
         crashreportcategory.addDetail("Sprite name", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return texture.getIconName();
            }
         });
         crashreportcategory.addDetail("Sprite size", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return texture.getIconWidth() + " x " + texture.getIconHeight();
            }
         });
         crashreportcategory.addDetail("Sprite frames", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return texture.getFrameCount() + " frames";
            }
         });
         crashreportcategory.addCrashSection("Mipmap levels", this.mipmapLevels);
         throw new ReportedException(crashreport);
      }
   }

   public ResourceLocation getResourceLocation(TextureAtlasSprite p_184396_1_) {
      ResourceLocation resourcelocation = new ResourceLocation(p_184396_1_.getIconName());
      return this.completeResourceLocation(resourcelocation);
   }

   public ResourceLocation completeResourceLocation(ResourceLocation resourcelocation) {
      return this.isAbsoluteLocation(resourcelocation)
         ? new ResourceLocation(resourcelocation.getNamespace(), resourcelocation.getPath() + ".png")
         : new ResourceLocation(resourcelocation.getNamespace(), String.format("%s/%s%s", this.basePath, resourcelocation.getPath(), ".png"));
   }

   public TextureAtlasSprite getAtlasSprite(String iconName) {
      TextureAtlasSprite textureatlassprite = this.mapUploadedSprites.get(iconName);
      if (textureatlassprite == null) {
         textureatlassprite = this.missingImage;
      }

      return textureatlassprite;
   }

   public void updateAnimations() {
      boolean hasNormal = false;
      boolean hasSpecular = false;
      TextureUtil.bindTexture(this.getGlTextureId());
      int countActive = 0;

      for (TextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {
         if (this.isTerrainAnimationActive(textureatlassprite)) {
            textureatlassprite.updateAnimation();
            if (textureatlassprite.isAnimationActive()) {
               countActive++;
            }

            if (textureatlassprite.spriteNormal != null) {
               hasNormal = true;
            }

            if (textureatlassprite.spriteSpecular != null) {
               hasSpecular = true;
            }
         }
      }

      if (Config.isMultiTexture()) {
         for (TextureAtlasSprite ts : this.listAnimatedSprites) {
            if (this.isTerrainAnimationActive(ts)) {
               TextureAtlasSprite spriteSingle = ts.spriteSingle;
               if (spriteSingle != null) {
                  if (ts == TextureUtils.iconClock || ts == TextureUtils.iconCompass) {
                     spriteSingle.frameCounter = ts.frameCounter;
                  }

                  ts.bindSpriteTexture();
                  spriteSingle.updateAnimation();
                  if (spriteSingle.isAnimationActive()) {
                     countActive++;
                  }
               }
            }
         }

         TextureUtil.bindTexture(this.getGlTextureId());
      }

      if (Config.isShaders()) {
         if (hasNormal) {
            TextureUtil.bindTexture(this.getMultiTexID().norm);

            for (TextureAtlasSprite textureatlasspritex : this.listAnimatedSprites) {
               if (textureatlasspritex.spriteNormal != null && this.isTerrainAnimationActive(textureatlasspritex)) {
                  if (textureatlasspritex == TextureUtils.iconClock || textureatlasspritex == TextureUtils.iconCompass) {
                     textureatlasspritex.spriteNormal.frameCounter = textureatlasspritex.frameCounter;
                  }

                  textureatlasspritex.spriteNormal.updateAnimation();
                  if (textureatlasspritex.spriteNormal.isAnimationActive()) {
                     countActive++;
                  }
               }
            }
         }

         if (hasSpecular) {
            TextureUtil.bindTexture(this.getMultiTexID().spec);

            for (TextureAtlasSprite textureatlasspritexx : this.listAnimatedSprites) {
               if (textureatlasspritexx.spriteSpecular != null && this.isTerrainAnimationActive(textureatlasspritexx)) {
                  if (textureatlasspritexx == TextureUtils.iconClock || textureatlasspritexx == TextureUtils.iconCompass) {
                     textureatlasspritexx.spriteNormal.frameCounter = textureatlasspritexx.frameCounter;
                  }

                  textureatlasspritexx.spriteSpecular.updateAnimation();
                  if (textureatlasspritexx.spriteSpecular.isAnimationActive()) {
                     countActive++;
                  }
               }
            }
         }

         if (hasNormal || hasSpecular) {
            TextureUtil.bindTexture(this.getGlTextureId());
         }
      }

      int frameCount = Config.getMinecraft().entityRenderer.frameCount;
      if (frameCount != this.frameCountAnimations) {
         this.countAnimationsActive = countActive;
         this.frameCountAnimations = frameCount;
      }

      if (SmartAnimations.isActive()) {
         SmartAnimations.resetSpritesRendered();
      }
   }

   public TextureAtlasSprite registerSprite(ResourceLocation location) {
      if (location == null) {
         throw new IllegalArgumentException("Location cannot be null!");
      } else {
         TextureAtlasSprite textureatlassprite = this.mapRegisteredSprites.get(location.toString());
         if (textureatlassprite == null) {
            textureatlassprite = TextureAtlasSprite.makeAtlasSprite(location);
            this.mapRegisteredSprites.put(location.toString(), textureatlassprite);
            textureatlassprite.updateIndexInMap(this.counterIndexInMap);
            if (Config.isEmissiveTextures()) {
               this.checkEmissive(location, textureatlassprite);
            }
         }

         return textureatlassprite;
      }
   }

   public void tick() {
      this.updateAnimations();
   }

   public void setMipmapLevels(int mipmapLevelsIn) {
      this.mipmapLevels = mipmapLevelsIn;
   }

   public TextureAtlasSprite getMissingSprite() {
      return this.missingImage;
   }

   @Nullable
   public TextureAtlasSprite getTextureExtry(String name) {
      return this.mapRegisteredSprites.get(name);
   }

   public boolean setTextureEntry(TextureAtlasSprite entry) {
      String name = entry.getIconName();
      if (!this.mapRegisteredSprites.containsKey(name)) {
         this.mapRegisteredSprites.put(name, entry);
         entry.updateIndexInMap(this.counterIndexInMap);
         return true;
      } else {
         return false;
      }
   }

   public String getBasePath() {
      return this.basePath;
   }

   public int getMipmapLevels() {
      return this.mipmapLevels;
   }

   private boolean isAbsoluteLocation(ResourceLocation loc) {
      String path = loc.getPath();
      return this.isAbsoluteLocationPath(path);
   }

   private boolean isAbsoluteLocationPath(String resPath) {
      String path = resPath.toLowerCase();
      return path.startsWith("mcpatcher/") || path.startsWith("optifine/");
   }

   public TextureAtlasSprite getSpriteSafe(String name) {
      ResourceLocation loc = new ResourceLocation(name);
      return this.mapRegisteredSprites.get(loc.toString());
   }

   public TextureAtlasSprite getRegisteredSprite(ResourceLocation loc) {
      return this.mapRegisteredSprites.get(loc.toString());
   }

   private boolean isTerrainAnimationActive(TextureAtlasSprite ts) {
      if (ts == TextureUtils.iconWaterStill || ts == TextureUtils.iconWaterFlow) {
         return Config.isAnimatedWater();
      } else if (ts == TextureUtils.iconLavaStill || ts == TextureUtils.iconLavaFlow) {
         return Config.isAnimatedLava();
      } else if (ts == TextureUtils.iconFireLayer0 || ts == TextureUtils.iconFireLayer1) {
         return Config.isAnimatedFire();
      } else if (ts == TextureUtils.iconPortal) {
         return Config.isAnimatedPortal();
      } else {
         return ts != TextureUtils.iconClock && ts != TextureUtils.iconCompass ? Config.isAnimatedTerrain() : true;
      }
   }

   public int getCountRegisteredSprites() {
      return this.counterIndexInMap.getValue();
   }

   private int detectMaxMipmapLevel(Map mapSprites, IResourceManager rm) {
      int minSize = this.detectMinimumSpriteSize(mapSprites, rm, 20);
      if (minSize < 16) {
         minSize = 16;
      }

      minSize = MathHelper.smallestEncompassingPowerOfTwo(minSize);
      if (minSize > 16) {
         Config.log("Sprite size: " + minSize);
      }

      int minLevel = MathHelper.log2(minSize);
      if (minLevel < 4) {
         minLevel = 4;
      }

      return minLevel;
   }

   private int detectMinimumSpriteSize(Map mapSprites, IResourceManager rm, int percentScale) {
      Map mapSizeCounts = new HashMap();

      for (Entry entry : mapSprites.entrySet()) {
         TextureAtlasSprite sprite = (TextureAtlasSprite)entry.getValue();
         ResourceLocation loc = new ResourceLocation(sprite.getIconName());
         ResourceLocation locComplete = this.completeResourceLocation(loc);
         if (!sprite.hasCustomLoader(rm, loc)) {
            try {
               IResource res = rm.getResource(locComplete);
               if (res != null) {
                  InputStream in = res.getInputStream();
                  if (in != null) {
                     Dimension dim = TextureUtils.getImageSize(in, "png");
                     in.close();
                     if (dim != null) {
                        int width = dim.width;
                        int width2 = MathHelper.smallestEncompassingPowerOfTwo(width);
                        if (!mapSizeCounts.containsKey(width2)) {
                           mapSizeCounts.put(width2, 1);
                        } else {
                           int count = (Integer)mapSizeCounts.get(width2);
                           mapSizeCounts.put(width2, count + 1);
                        }
                     }
                  }
               }
            } catch (Exception var17) {
            }
         }
      }

      int countSprites = 0;
      Set setSizes = mapSizeCounts.keySet();
      Set setSizesSorted = new TreeSet(setSizes);

      for (int size : setSizesSorted) {
         int count = (Integer)mapSizeCounts.get(size);
         countSprites += count;
      }

      int minSize = 16;
      int countScale = 0;
      int countScaleMax = countSprites * percentScale / 100;

      for (int size : setSizesSorted) {
         int count = (Integer)mapSizeCounts.get(size);
         countScale += count;
         if (size > minSize) {
            minSize = size;
         }

         if (countScale > countScaleMax) {
            return minSize;
         }
      }

      return minSize;
   }

   private int getMinSpriteSize() {
      int minSize = 1 << this.mipmapLevels;
      if (minSize < 8) {
         minSize = 8;
      }

      return minSize;
   }

   private int[] getMissingImageData(int size) {
      BufferedImage bi = new BufferedImage(16, 16, 2);
      bi.setRGB(0, 0, 16, 16, TextureUtil.MISSING_TEXTURE_DATA, 0, 16);
      BufferedImage bi2 = TextureUtils.scaleImage(bi, size);
      int[] data = new int[size * size];
      bi2.getRGB(0, 0, size, size, data, 0, size);
      return data;
   }

   public boolean isTextureBound() {
      int boundTexId = GlStateManager.getBoundTexture();
      int texId = this.getGlTextureId();
      return boundTexId == texId;
   }

   private void updateIconGrid(int sheetWidth, int sheetHeight) {
      this.iconGridCountX = -1;
      this.iconGridCountY = -1;
      this.iconGrid = null;
      if (this.iconGridSize > 0) {
         this.iconGridCountX = sheetWidth / this.iconGridSize;
         this.iconGridCountY = sheetHeight / this.iconGridSize;
         this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
         this.iconGridSizeU = 1.0 / this.iconGridCountX;
         this.iconGridSizeV = 1.0 / this.iconGridCountY;

         for (TextureAtlasSprite ts : this.mapUploadedSprites.values()) {
            double deltaU = 0.5 / sheetWidth;
            double deltaV = 0.5 / sheetHeight;
            double uMin = Math.min(ts.getMinU(), ts.getMaxU()) + deltaU;
            double vMin = Math.min(ts.getMinV(), ts.getMaxV()) + deltaV;
            double uMax = Math.max(ts.getMinU(), ts.getMaxU()) - deltaU;
            double vMax = Math.max(ts.getMinV(), ts.getMaxV()) - deltaV;
            int iuMin = (int)(uMin / this.iconGridSizeU);
            int ivMin = (int)(vMin / this.iconGridSizeV);
            int iuMax = (int)(uMax / this.iconGridSizeU);
            int ivMax = (int)(vMax / this.iconGridSizeV);

            for (int iu = iuMin; iu <= iuMax; iu++) {
               if (iu >= 0 && iu < this.iconGridCountX) {
                  for (int iv = ivMin; iv <= ivMax; iv++) {
                     if (iv >= 0 && iv < this.iconGridCountX) {
                        int index = iv * this.iconGridCountX + iu;
                        this.iconGrid[index] = ts;
                     } else {
                        Config.warn("Invalid grid V: " + iv + ", icon: " + ts.getIconName());
                     }
                  }
               } else {
                  Config.warn("Invalid grid U: " + iu + ", icon: " + ts.getIconName());
               }
            }
         }
      }
   }

   public TextureAtlasSprite getIconByUV(double u, double v) {
      if (this.iconGrid == null) {
         return null;
      } else {
         int iu = (int)(u / this.iconGridSizeU);
         int iv = (int)(v / this.iconGridSizeV);
         int index = iv * this.iconGridCountX + iu;
         return index >= 0 && index <= this.iconGrid.length ? this.iconGrid[index] : null;
      }
   }

   private void checkEmissive(ResourceLocation locSprite, TextureAtlasSprite sprite) {
      String suffixEm = EmissiveTextures.getSuffixEmissive();
      if (suffixEm != null) {
         if (!locSprite.getPath().endsWith(suffixEm)) {
            ResourceLocation locSpriteEm = new ResourceLocation(locSprite.getNamespace(), locSprite.getPath() + suffixEm);
            ResourceLocation locPngEm = this.completeResourceLocation(locSpriteEm);
            if (Config.hasResource(locPngEm)) {
               TextureAtlasSprite spriteEmissive = this.registerSprite(locSpriteEm);
               spriteEmissive.isEmissive = true;
               sprite.spriteEmissive = spriteEmissive;
            }
         }
      }
   }

   public int getCountAnimations() {
      return this.listAnimatedSprites.size();
   }

   public int getCountAnimationsActive() {
      return this.countAnimationsActive;
   }
}
