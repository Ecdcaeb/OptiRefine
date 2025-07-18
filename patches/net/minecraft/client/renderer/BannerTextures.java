package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;

public class BannerTextures {
   public static final BannerTextures.Cache BANNER_DESIGNS = new BannerTextures.Cache(
      "B", new ResourceLocation("textures/entity/banner_base.png"), "textures/entity/banner/"
   );
   public static final BannerTextures.Cache SHIELD_DESIGNS = new BannerTextures.Cache(
      "S", new ResourceLocation("textures/entity/shield_base.png"), "textures/entity/shield/"
   );
   public static final ResourceLocation SHIELD_BASE_TEXTURE = new ResourceLocation("textures/entity/shield_base_nopattern.png");
   public static final ResourceLocation BANNER_BASE_TEXTURE = new ResourceLocation("textures/entity/banner/base.png");

   public static class Cache {
      private final Map<String, BannerTextures.CacheEntry> cacheMap = Maps.newLinkedHashMap();
      private final ResourceLocation cacheResourceLocation;
      private final String cacheResourceBase;
      private final String cacheId;

      public Cache(String var1, ResourceLocation var2, String var3) {
         this.cacheId = ☃;
         this.cacheResourceLocation = ☃;
         this.cacheResourceBase = ☃;
      }

      @Nullable
      public ResourceLocation getResourceLocation(String var1, List<BannerPattern> var2, List<EnumDyeColor> var3) {
         if (☃.isEmpty()) {
            return null;
         } else {
            ☃ = this.cacheId + ☃;
            BannerTextures.CacheEntry ☃ = this.cacheMap.get(☃);
            if (☃ == null) {
               if (this.cacheMap.size() >= 256 && !this.freeCacheSlot()) {
                  return BannerTextures.BANNER_BASE_TEXTURE;
               }

               List<String> ☃x = Lists.newArrayList();

               for (BannerPattern ☃xx : ☃) {
                  ☃x.add(this.cacheResourceBase + ☃xx.getFileName() + ".png");
               }

               ☃ = new BannerTextures.CacheEntry();
               ☃.textureLocation = new ResourceLocation(☃);
               Minecraft.getMinecraft().getTextureManager().loadTexture(☃.textureLocation, new LayeredColorMaskTexture(this.cacheResourceLocation, ☃x, ☃));
               this.cacheMap.put(☃, ☃);
            }

            ☃.lastUseMillis = System.currentTimeMillis();
            return ☃.textureLocation;
         }
      }

      private boolean freeCacheSlot() {
         long ☃ = System.currentTimeMillis();
         Iterator<String> ☃x = this.cacheMap.keySet().iterator();

         while (☃x.hasNext()) {
            String ☃xx = ☃x.next();
            BannerTextures.CacheEntry ☃xxx = this.cacheMap.get(☃xx);
            if (☃ - ☃xxx.lastUseMillis > 5000L) {
               Minecraft.getMinecraft().getTextureManager().deleteTexture(☃xxx.textureLocation);
               ☃x.remove();
               return true;
            }
         }

         return this.cacheMap.size() < 256;
      }
   }

   static class CacheEntry {
      public long lastUseMillis;
      public ResourceLocation textureLocation;

      private CacheEntry() {
      }
   }
}
