package net.minecraft.client.resources;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.InsecureTextureException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class SkinManager {
   private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
   private final TextureManager textureManager;
   private final File skinCacheDir;
   private final MinecraftSessionService sessionService;
   private final LoadingCache<GameProfile, Map<Type, MinecraftProfileTexture>> skinCacheLoader;

   public SkinManager(TextureManager var1, File var2, MinecraftSessionService var3) {
      this.textureManager = ☃;
      this.skinCacheDir = ☃;
      this.sessionService = ☃;
      this.skinCacheLoader = CacheBuilder.newBuilder()
         .expireAfterAccess(15L, TimeUnit.SECONDS)
         .build(new CacheLoader<GameProfile, Map<Type, MinecraftProfileTexture>>() {
            public Map<Type, MinecraftProfileTexture> load(GameProfile var1) throws Exception {
               try {
                  return Minecraft.getMinecraft().getSessionService().getTextures(☃, false);
               } catch (Throwable var3x) {
                  return Maps.newHashMap();
               }
            }
         });
   }

   public ResourceLocation loadSkin(MinecraftProfileTexture var1, Type var2) {
      return this.loadSkin(☃, ☃, null);
   }

   public ResourceLocation loadSkin(final MinecraftProfileTexture var1, final Type var2, @Nullable final SkinManager.SkinAvailableCallback var3) {
      final ResourceLocation ☃ = new ResourceLocation("skins/" + ☃.getHash());
      ITextureObject ☃x = this.textureManager.getTexture(☃);
      if (☃x != null) {
         if (☃ != null) {
            ☃.skinAvailable(☃, ☃, ☃);
         }
      } else {
         File ☃xx = new File(this.skinCacheDir, ☃.getHash().length() > 2 ? ☃.getHash().substring(0, 2) : "xx");
         File ☃xxx = new File(☃xx, ☃.getHash());
         final IImageBuffer ☃xxxx = ☃ == Type.SKIN ? new ImageBufferDownload() : null;
         ThreadDownloadImageData ☃xxxxx = new ThreadDownloadImageData(☃xxx, ☃.getUrl(), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer() {
            @Override
            public BufferedImage parseUserSkin(BufferedImage var1x) {
               if (☃ != null) {
                  ☃ = ☃.parseUserSkin(☃);
               }

               return ☃;
            }

            @Override
            public void skinAvailable() {
               if (☃ != null) {
                  ☃.skinAvailable();
               }

               if (☃ != null) {
                  ☃.skinAvailable(☃, ☃, ☃);
               }
            }
         });
         this.textureManager.loadTexture(☃, ☃xxxxx);
      }

      return ☃;
   }

   public void loadProfileTextures(final GameProfile var1, final SkinManager.SkinAvailableCallback var2, final boolean var3) {
      THREAD_POOL.submit(new Runnable() {
         @Override
         public void run() {
            final Map<Type, MinecraftProfileTexture> ☃ = Maps.newHashMap();

            try {
               ☃.putAll(SkinManager.this.sessionService.getTextures(☃, ☃));
            } catch (InsecureTextureException var3x) {
            }

            if (☃.isEmpty() && ☃.getId().equals(Minecraft.getMinecraft().getSession().getProfile().getId())) {
               ☃.getProperties().clear();
               ☃.getProperties().putAll(Minecraft.getMinecraft().getProfileProperties());
               ☃.putAll(SkinManager.this.sessionService.getTextures(☃, false));
            }

            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
               @Override
               public void run() {
                  if (☃.containsKey(Type.SKIN)) {
                     SkinManager.this.loadSkin(☃.get(Type.SKIN), Type.SKIN, ☃);
                  }

                  if (☃.containsKey(Type.CAPE)) {
                     SkinManager.this.loadSkin(☃.get(Type.CAPE), Type.CAPE, ☃);
                  }
               }
            });
         }
      });
   }

   public Map<Type, MinecraftProfileTexture> loadSkinFromCache(GameProfile var1) {
      return (Map<Type, MinecraftProfileTexture>)this.skinCacheLoader.getUnchecked(☃);
   }

   public interface SkinAvailableCallback {
      void skinAvailable(Type var1, ResourceLocation var2, MinecraftProfileTexture var3);
   }
}
