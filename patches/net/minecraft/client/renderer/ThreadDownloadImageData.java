package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadDownloadImageData extends SimpleTexture {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final AtomicInteger TEXTURE_DOWNLOADER_THREAD_ID = new AtomicInteger(0);
   @Nullable
   private final File cacheFile;
   private final String imageUrl;
   @Nullable
   private final IImageBuffer imageBuffer;
   @Nullable
   private BufferedImage bufferedImage;
   @Nullable
   private Thread imageThread;
   private boolean textureUploaded;

   public ThreadDownloadImageData(@Nullable File var1, String var2, ResourceLocation var3, @Nullable IImageBuffer var4) {
      super(☃);
      this.cacheFile = ☃;
      this.imageUrl = ☃;
      this.imageBuffer = ☃;
   }

   private void checkTextureUploaded() {
      if (!this.textureUploaded) {
         if (this.bufferedImage != null) {
            if (this.textureLocation != null) {
               this.deleteGlTexture();
            }

            TextureUtil.uploadTextureImage(super.getGlTextureId(), this.bufferedImage);
            this.textureUploaded = true;
         }
      }
   }

   @Override
   public int getGlTextureId() {
      this.checkTextureUploaded();
      return super.getGlTextureId();
   }

   public void setBufferedImage(BufferedImage var1) {
      this.bufferedImage = ☃;
      if (this.imageBuffer != null) {
         this.imageBuffer.skinAvailable();
      }
   }

   @Override
   public void loadTexture(IResourceManager var1) throws IOException {
      if (this.bufferedImage == null && this.textureLocation != null) {
         super.loadTexture(☃);
      }

      if (this.imageThread == null) {
         if (this.cacheFile != null && this.cacheFile.isFile()) {
            LOGGER.debug("Loading http texture from local cache ({})", this.cacheFile);

            try {
               this.bufferedImage = ImageIO.read(this.cacheFile);
               if (this.imageBuffer != null) {
                  this.setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage));
               }
            } catch (IOException var3) {
               LOGGER.error("Couldn't load skin {}", this.cacheFile, var3);
               this.loadTextureFromServer();
            }
         } else {
            this.loadTextureFromServer();
         }
      }
   }

   protected void loadTextureFromServer() {
      this.imageThread = new Thread("Texture Downloader #" + TEXTURE_DOWNLOADER_THREAD_ID.incrementAndGet()) {
         @Override
         public void run() {
            HttpURLConnection ☃ = null;
            ThreadDownloadImageData.LOGGER
               .debug("Downloading http texture from {} to {}", ThreadDownloadImageData.this.imageUrl, ThreadDownloadImageData.this.cacheFile);

            try {
               ☃ = (HttpURLConnection)new URL(ThreadDownloadImageData.this.imageUrl).openConnection(Minecraft.getMinecraft().getProxy());
               ☃.setDoInput(true);
               ☃.setDoOutput(false);
               ☃.connect();
               if (☃.getResponseCode() / 100 == 2) {
                  BufferedImage ☃x;
                  if (ThreadDownloadImageData.this.cacheFile != null) {
                     FileUtils.copyInputStreamToFile(☃.getInputStream(), ThreadDownloadImageData.this.cacheFile);
                     ☃x = ImageIO.read(ThreadDownloadImageData.this.cacheFile);
                  } else {
                     ☃x = TextureUtil.readBufferedImage(☃.getInputStream());
                  }

                  if (ThreadDownloadImageData.this.imageBuffer != null) {
                     ☃x = ThreadDownloadImageData.this.imageBuffer.parseUserSkin(☃x);
                  }

                  ThreadDownloadImageData.this.setBufferedImage(☃x);
                  return;
               }
            } catch (Exception var6) {
               ThreadDownloadImageData.LOGGER.error("Couldn't download http texture", var6);
               return;
            } finally {
               if (☃ != null) {
                  ☃.disconnect();
               }
            }
         }
      };
      this.imageThread.setDaemon(true);
      this.imageThread.start();
   }
}
