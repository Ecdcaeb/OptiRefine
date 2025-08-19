package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.optifine.http.HttpPipeline;
import net.optifine.http.HttpRequest;
import net.optifine.http.HttpResponse;
import net.optifine.player.CapeImageBuffer;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.Proxy;

@Mixin(ThreadDownloadImageData.class)
public abstract class MixinThreadDownloadImageData extends SimpleTexture{
    @Unique
    public Boolean imageFound = null;
    @Unique
    public boolean pipeline = false;

    @Shadow
    private BufferedImage bufferedImage;

    public MixinThreadDownloadImageData(ResourceLocation p_i1275_1) {
        super(p_i1275_1);
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEINTERFACE, desc = "net.minecraft.client.renderer.texture.ITextureObject getMultiTexID ()Lnet.optifine.shaders.MultiTexID;")
    private native static MultiTexID getMultiTexID(ITextureObject iTextureObject);

    @WrapOperation(method = "checkTextureUploaded", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;uploadTextureImage(ILjava/awt/image/BufferedImage;)I"))
    public int blockTextureUploadForConfig(int textureId, BufferedImage texture, Operation<Integer> original){
        if (Config.isShaders()) {
            return ShadersTex.loadSimpleTexture(textureId, texture, false, false, Config.getResourceManager(), this.textureLocation, getMultiTexID((ThreadDownloadImageData)(Object)this));
        } else {
            return TextureUtil.uploadTextureImage(textureId, texture);
        }
    }

    @Inject(method = "setBufferedImage", at = @At("TAIL"))
    public void afterImageSetted(BufferedImage bufferedImageIn, CallbackInfo ci){
        this.imageFound = this.bufferedImage != null;
    }

    @WrapOperation(method = "loadTextureFromServer", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;setDaemon(Z)V"))
    public void setPipelineForLoadTextureFromServer(Thread value, boolean on, Operation<Void> original){
        if (this.optiRefine$shouldPipeline()) {
            original.call(this.imageThread = new Thread(this::optiRefine$loadPipelined, value.getName()), on);
        } else {
            original.call(value, on);
        }
    }


    @Unique
    private boolean optiRefine$shouldPipeline() {
        if (!this.pipeline) {
            return false;
        } else {
            var proxy = Minecraft.getMinecraft().getProxy();
            return (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.SOCKS) && this.imageUrl.startsWith("http://");
        }
    }
    @Shadow @Final
    private String imageUrl;

    @Unique
    private void optiRefine$loadPipelined() {
        try {
            HttpRequest req = HttpPipeline.makeRequest(this.imageUrl, Minecraft.getMinecraft().getProxy());
            HttpResponse resp = HttpPipeline.executeRequest(req);
            if (resp.getStatus() / 100 == 2) {
                byte[] body = resp.getBody();
                ByteArrayInputStream bais = new ByteArrayInputStream(body);
                BufferedImage var2;
                if (this.cacheFile != null) {
                    FileUtils.copyInputStreamToFile(bais, this.cacheFile);
                    var2 = ImageIO.read(this.cacheFile);
                } else {
                    var2 = TextureUtil.readBufferedImage(bais);
                }

                if (this.imageBuffer != null) {
                    var2 = this.imageBuffer.parseUserSkin(var2);
                }

                this.setBufferedImage(var2);
            }
        } catch (Exception var9) {
            LOGGER.error("Couldn't download http texture: " + var9.getClass().getName() + ": " + var9.getMessage());
        } finally {
            this.optiRefine$loadingFinished();
        }
    }

    @Shadow @Final
    private File cacheFile;

    @Shadow @Final
    private static Logger LOGGER;

    @Shadow @Final
    private IImageBuffer imageBuffer;

    @Shadow
    public abstract void setBufferedImage(BufferedImage bufferedImageIn);

    @Shadow @Nullable private Thread imageThread;

    @Unique
    private void optiRefine$loadingFinished() {
        this.imageFound = this.bufferedImage != null;
        if (this.imageBuffer instanceof CapeImageBuffer) {
            CapeImageBuffer cib = (CapeImageBuffer)this.imageBuffer;
            cib.cleanup();
        }
    }

    @Unique
    public IImageBuffer getImageBuffer() {
        return this.imageBuffer;
    }
}
/*
--- net/minecraft/client/renderer/ThreadDownloadImageData.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/ThreadDownloadImageData.java	Tue Aug 19 14:59:58 2025
@@ -1,21 +1,29 @@
 package net.minecraft.client.renderer;

 import java.awt.image.BufferedImage;
+import java.io.ByteArrayInputStream;
 import java.io.File;
 import java.io.IOException;
 import java.net.HttpURLConnection;
+import java.net.Proxy;
 import java.net.URL;
+import java.net.Proxy.Type;
 import java.util.concurrent.atomic.AtomicInteger;
 import javax.annotation.Nullable;
 import javax.imageio.ImageIO;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.texture.SimpleTexture;
 import net.minecraft.client.renderer.texture.TextureUtil;
 import net.minecraft.client.resources.IResourceManager;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.http.HttpPipeline;
+import net.optifine.http.HttpRequest;
+import net.optifine.http.HttpResponse;
+import net.optifine.player.CapeImageBuffer;
+import net.optifine.shaders.ShadersTex;
 import org.apache.commons.io.FileUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public class ThreadDownloadImageData extends SimpleTexture {
    private static final Logger LOGGER = LogManager.getLogger();
@@ -27,29 +35,35 @@
    private final IImageBuffer imageBuffer;
    @Nullable
    private BufferedImage bufferedImage;
    @Nullable
    private Thread imageThread;
    private boolean textureUploaded;
+   public Boolean imageFound = null;
+   public boolean pipeline = false;

    public ThreadDownloadImageData(@Nullable File var1, String var2, ResourceLocation var3, @Nullable IImageBuffer var4) {
       super(var3);
       this.cacheFile = var1;
       this.imageUrl = var2;
       this.imageBuffer = var4;
    }

    private void checkTextureUploaded() {
-      if (!this.textureUploaded) {
-         if (this.bufferedImage != null) {
-            if (this.textureLocation != null) {
-               this.deleteGlTexture();
-            }
+      if (!this.textureUploaded && this.bufferedImage != null) {
+         this.textureUploaded = true;
+         if (this.textureLocation != null) {
+            this.deleteGlTexture();
+         }

+         if (Config.isShaders()) {
+            ShadersTex.loadSimpleTexture(
+               super.getGlTextureId(), this.bufferedImage, false, false, Config.getResourceManager(), this.textureLocation, this.getMultiTexID()
+            );
+         } else {
             TextureUtil.uploadTextureImage(super.getGlTextureId(), this.bufferedImage);
-            this.textureUploaded = true;
          }
       }
    }

    public int getGlTextureId() {
       this.checkTextureUploaded();
@@ -58,12 +72,14 @@

    public void setBufferedImage(BufferedImage var1) {
       this.bufferedImage = var1;
       if (this.imageBuffer != null) {
          this.imageBuffer.skinAvailable();
       }
+
+      this.imageFound = this.bufferedImage != null;
    }

    public void loadTexture(IResourceManager var1) throws IOException {
       if (this.bufferedImage == null && this.textureLocation != null) {
          super.loadTexture(var1);
       }
@@ -74,12 +90,14 @@

             try {
                this.bufferedImage = ImageIO.read(this.cacheFile);
                if (this.imageBuffer != null) {
                   this.setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage));
                }
+
+               this.loadingFinished();
             } catch (IOException var3) {
                LOGGER.error("Couldn't load skin {}", this.cacheFile, var3);
                this.loadTextureFromServer();
             }
          } else {
             this.loadTextureFromServer();
@@ -90,19 +108,28 @@
    protected void loadTextureFromServer() {
       this.imageThread = new Thread("Texture Downloader #" + TEXTURE_DOWNLOADER_THREAD_ID.incrementAndGet()) {
          public void run() {
             HttpURLConnection var1 = null;
             ThreadDownloadImageData.LOGGER
                .debug("Downloading http texture from {} to {}", ThreadDownloadImageData.this.imageUrl, ThreadDownloadImageData.this.cacheFile);
+            if (ThreadDownloadImageData.this.shouldPipeline()) {
+               ThreadDownloadImageData.this.loadPipelined();
+            } else {
+               try {
+                  var1 = (HttpURLConnection)new URL(ThreadDownloadImageData.this.imageUrl).openConnection(Minecraft.getMinecraft().getProxy());
+                  var1.setDoInput(true);
+                  var1.setDoOutput(false);
+                  var1.connect();
+                  if (var1.getResponseCode() / 100 != 2) {
+                     if (var1.getErrorStream() != null) {
+                        Config.readAll(var1.getErrorStream());
+                     }
+
+                     return;
+                  }

-            try {
-               var1 = (HttpURLConnection)new URL(ThreadDownloadImageData.this.imageUrl).openConnection(Minecraft.getMinecraft().getProxy());
-               var1.setDoInput(true);
-               var1.setDoOutput(false);
-               var1.connect();
-               if (var1.getResponseCode() / 100 == 2) {
                   BufferedImage var2;
                   if (ThreadDownloadImageData.this.cacheFile != null) {
                      FileUtils.copyInputStreamToFile(var1.getInputStream(), ThreadDownloadImageData.this.cacheFile);
                      var2 = ImageIO.read(ThreadDownloadImageData.this.cacheFile);
                   } else {
                      var2 = TextureUtil.readBufferedImage(var1.getInputStream());
@@ -110,22 +137,74 @@

                   if (ThreadDownloadImageData.this.imageBuffer != null) {
                      var2 = ThreadDownloadImageData.this.imageBuffer.parseUserSkin(var2);
                   }

                   ThreadDownloadImageData.this.setBufferedImage(var2);
+               } catch (Exception var6) {
+                  ThreadDownloadImageData.LOGGER.error("Couldn't download http texture: " + var6.getMessage());
                   return;
-               }
-            } catch (Exception var6) {
-               ThreadDownloadImageData.LOGGER.error("Couldn't download http texture", var6);
-               return;
-            } finally {
-               if (var1 != null) {
-                  var1.disconnect();
+               } finally {
+                  if (var1 != null) {
+                     var1.disconnect();
+                  }
+
+                  ThreadDownloadImageData.this.loadingFinished();
                }
             }
          }
       };
       this.imageThread.setDaemon(true);
       this.imageThread.start();
+   }
+
+   private boolean shouldPipeline() {
+      if (!this.pipeline) {
+         return false;
+      } else {
+         Proxy var1 = Minecraft.getMinecraft().getProxy();
+         return var1.type() != Type.DIRECT && var1.type() != Type.SOCKS ? false : this.imageUrl.startsWith("http://");
+      }
+   }
+
+   private void loadPipelined() {
+      try {
+         HttpRequest var1 = HttpPipeline.makeRequest(this.imageUrl, Minecraft.getMinecraft().getProxy());
+         HttpResponse var2 = HttpPipeline.executeRequest(var1);
+         if (var2.getStatus() / 100 == 2) {
+            byte[] var4 = var2.getBody();
+            ByteArrayInputStream var5 = new ByteArrayInputStream(var4);
+            BufferedImage var3;
+            if (this.cacheFile != null) {
+               FileUtils.copyInputStreamToFile(var5, this.cacheFile);
+               var3 = ImageIO.read(this.cacheFile);
+            } else {
+               var3 = TextureUtil.readBufferedImage(var5);
+            }
+
+            if (this.imageBuffer != null) {
+               var3 = this.imageBuffer.parseUserSkin(var3);
+            }
+
+            this.setBufferedImage(var3);
+            return;
+         }
+      } catch (Exception var9) {
+         LOGGER.error("Couldn't download http texture: " + var9.getClass().getName() + ": " + var9.getMessage());
+         return;
+      } finally {
+         this.loadingFinished();
+      }
+   }
+
+   private void loadingFinished() {
+      this.imageFound = this.bufferedImage != null;
+      if (this.imageBuffer instanceof CapeImageBuffer) {
+         CapeImageBuffer var1 = (CapeImageBuffer)this.imageBuffer;
+         var1.cleanup();
+      }
+   }
+
+   public IImageBuffer getImageBuffer() {
+      return this.imageBuffer;
    }
 }
 */
