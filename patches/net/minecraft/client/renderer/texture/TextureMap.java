package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureMap extends AbstractTexture implements ITickableTextureObject {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
   public static final ResourceLocation LOCATION_BLOCKS_TEXTURE = new ResourceLocation("textures/atlas/blocks.png");
   private final List<TextureAtlasSprite> listAnimatedSprites = Lists.newArrayList();
   private final Map<String, TextureAtlasSprite> mapRegisteredSprites = Maps.newHashMap();
   private final Map<String, TextureAtlasSprite> mapUploadedSprites = Maps.newHashMap();
   private final String basePath;
   private final ITextureMapPopulator iconCreator;
   private int mipmapLevels;
   private final TextureAtlasSprite missingImage = new TextureAtlasSprite("missingno");

   public TextureMap(String var1) {
      this(☃, null);
   }

   public TextureMap(String var1, @Nullable ITextureMapPopulator var2) {
      this.basePath = ☃;
      this.iconCreator = ☃;
   }

   private void initMissingImage() {
      int[] ☃ = TextureUtil.MISSING_TEXTURE_DATA;
      this.missingImage.setIconWidth(16);
      this.missingImage.setIconHeight(16);
      int[][] ☃x = new int[this.mipmapLevels + 1][];
      ☃x[0] = ☃;
      this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][]{☃x}));
   }

   @Override
   public void loadTexture(IResourceManager var1) throws IOException {
      if (this.iconCreator != null) {
         this.loadSprites(☃, this.iconCreator);
      }
   }

   public void loadSprites(IResourceManager var1, ITextureMapPopulator var2) {
      this.mapRegisteredSprites.clear();
      ☃.registerSprites(this);
      this.initMissingImage();
      this.deleteGlTexture();
      this.loadTextureAtlas(☃);
   }

   public void loadTextureAtlas(IResourceManager var1) {
      int ☃ = Minecraft.getGLMaximumTextureSize();
      Stitcher ☃x = new Stitcher(☃, ☃, 0, this.mipmapLevels);
      this.mapUploadedSprites.clear();
      this.listAnimatedSprites.clear();
      int ☃xx = Integer.MAX_VALUE;
      int ☃xxx = 1 << this.mipmapLevels;

      for (Entry<String, TextureAtlasSprite> ☃xxxx : this.mapRegisteredSprites.entrySet()) {
         TextureAtlasSprite ☃xxxxx = ☃xxxx.getValue();
         ResourceLocation ☃xxxxxx = this.getResourceLocation(☃xxxxx);
         IResource ☃xxxxxxx = null;

         try {
            PngSizeInfo ☃xxxxxxxx = PngSizeInfo.makeFromResource(☃.getResource(☃xxxxxx));
            ☃xxxxxxx = ☃.getResource(☃xxxxxx);
            boolean ☃xxxxxxxxx = ☃xxxxxxx.getMetadata("animation") != null;
            ☃xxxxx.loadSprite(☃xxxxxxxx, ☃xxxxxxxxx);
         } catch (RuntimeException var22) {
            LOGGER.error("Unable to parse metadata from {}", ☃xxxxxx, var22);
            continue;
         } catch (IOException var23) {
            LOGGER.error("Using missing texture, unable to load {}", ☃xxxxxx, var23);
            continue;
         } finally {
            IOUtils.closeQuietly(☃xxxxxxx);
         }

         ☃xx = Math.min(☃xx, Math.min(☃xxxxx.getIconWidth(), ☃xxxxx.getIconHeight()));
         int ☃xxxxxxxx = Math.min(Integer.lowestOneBit(☃xxxxx.getIconWidth()), Integer.lowestOneBit(☃xxxxx.getIconHeight()));
         if (☃xxxxxxxx < ☃xxx) {
            LOGGER.warn(
               "Texture {} with size {}x{} limits mip level from {} to {}",
               ☃xxxxxx,
               ☃xxxxx.getIconWidth(),
               ☃xxxxx.getIconHeight(),
               MathHelper.log2(☃xxx),
               MathHelper.log2(☃xxxxxxxx)
            );
            ☃xxx = ☃xxxxxxxx;
         }

         ☃x.addSprite(☃xxxxx);
      }

      int ☃xxxx = Math.min(☃xx, ☃xxx);
      int ☃xxxxx = MathHelper.log2(☃xxxx);
      if (☃xxxxx < this.mipmapLevels) {
         LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.basePath, this.mipmapLevels, ☃xxxxx, ☃xxxx);
         this.mipmapLevels = ☃xxxxx;
      }

      this.missingImage.generateMipmaps(this.mipmapLevels);
      ☃x.addSprite(this.missingImage);

      try {
         ☃x.doStitch();
      } catch (StitcherException var21) {
         throw var21;
      }

      LOGGER.info("Created: {}x{} {}-atlas", ☃x.getCurrentWidth(), ☃x.getCurrentHeight(), this.basePath);
      TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, ☃x.getCurrentWidth(), ☃x.getCurrentHeight());
      Map<String, TextureAtlasSprite> ☃xxxxxx = Maps.newHashMap(this.mapRegisteredSprites);

      for (TextureAtlasSprite ☃xxxxxxx : ☃x.getStichSlots()) {
         if (☃xxxxxxx == this.missingImage || this.generateMipmaps(☃, ☃xxxxxxx)) {
            String ☃xxxxxxxx = ☃xxxxxxx.getIconName();
            ☃xxxxxx.remove(☃xxxxxxxx);
            this.mapUploadedSprites.put(☃xxxxxxxx, ☃xxxxxxx);

            try {
               TextureUtil.uploadTextureMipmap(
                  ☃xxxxxxx.getFrameTextureData(0),
                  ☃xxxxxxx.getIconWidth(),
                  ☃xxxxxxx.getIconHeight(),
                  ☃xxxxxxx.getOriginX(),
                  ☃xxxxxxx.getOriginY(),
                  false,
                  false
               );
            } catch (Throwable var20) {
               CrashReport ☃xxxxxxxxx = CrashReport.makeCrashReport(var20, "Stitching texture atlas");
               CrashReportCategory ☃xxxxxxxxxx = ☃xxxxxxxxx.makeCategory("Texture being stitched together");
               ☃xxxxxxxxxx.addCrashSection("Atlas path", this.basePath);
               ☃xxxxxxxxxx.addCrashSection("Sprite", ☃xxxxxxx);
               throw new ReportedException(☃xxxxxxxxx);
            }

            if (☃xxxxxxx.hasAnimationMetadata()) {
               this.listAnimatedSprites.add(☃xxxxxxx);
            }
         }
      }

      for (TextureAtlasSprite ☃xxxxxxxx : ☃xxxxxx.values()) {
         ☃xxxxxxxx.copyFrom(this.missingImage);
      }
   }

   private boolean generateMipmaps(IResourceManager var1, final TextureAtlasSprite var2) {
      ResourceLocation ☃ = this.getResourceLocation(☃);
      IResource ☃x = null;

      label45: {
         boolean var6;
         try {
            ☃x = ☃.getResource(☃);
            ☃.loadSpriteFrames(☃x, this.mipmapLevels + 1);
            break label45;
         } catch (RuntimeException var13) {
            LOGGER.error("Unable to parse metadata from {}", ☃, var13);
            return false;
         } catch (IOException var14) {
            LOGGER.error("Using missing texture, unable to load {}", ☃, var14);
            var6 = false;
         } finally {
            IOUtils.closeQuietly(☃x);
         }

         return var6;
      }

      try {
         ☃.generateMipmaps(this.mipmapLevels);
         return true;
      } catch (Throwable var12) {
         CrashReport ☃xx = CrashReport.makeCrashReport(var12, "Applying mipmap");
         CrashReportCategory ☃xxx = ☃xx.makeCategory("Sprite being mipmapped");
         ☃xxx.addDetail("Sprite name", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return ☃.getIconName();
            }
         });
         ☃xxx.addDetail("Sprite size", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return ☃.getIconWidth() + " x " + ☃.getIconHeight();
            }
         });
         ☃xxx.addDetail("Sprite frames", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return ☃.getFrameCount() + " frames";
            }
         });
         ☃xxx.addCrashSection("Mipmap levels", this.mipmapLevels);
         throw new ReportedException(☃xx);
      }
   }

   private ResourceLocation getResourceLocation(TextureAtlasSprite var1) {
      ResourceLocation ☃ = new ResourceLocation(☃.getIconName());
      return new ResourceLocation(☃.getNamespace(), String.format("%s/%s%s", this.basePath, ☃.getPath(), ".png"));
   }

   public TextureAtlasSprite getAtlasSprite(String var1) {
      TextureAtlasSprite ☃ = this.mapUploadedSprites.get(☃);
      if (☃ == null) {
         ☃ = this.missingImage;
      }

      return ☃;
   }

   public void updateAnimations() {
      TextureUtil.bindTexture(this.getGlTextureId());

      for (TextureAtlasSprite ☃ : this.listAnimatedSprites) {
         ☃.updateAnimation();
      }
   }

   public TextureAtlasSprite registerSprite(ResourceLocation var1) {
      if (☃ == null) {
         throw new IllegalArgumentException("Location cannot be null!");
      } else {
         TextureAtlasSprite ☃ = this.mapRegisteredSprites.get(☃);
         if (☃ == null) {
            ☃ = TextureAtlasSprite.makeAtlasSprite(☃);
            this.mapRegisteredSprites.put(☃.toString(), ☃);
         }

         return ☃;
      }
   }

   @Override
   public void tick() {
      this.updateAnimations();
   }

   public void setMipmapLevels(int var1) {
      this.mipmapLevels = ☃;
   }

   public TextureAtlasSprite getMissingSprite() {
      return this.missingImage;
   }
}
