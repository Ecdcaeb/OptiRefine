/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.awt.image.BufferedImage
 *  java.io.ByteArrayInputStream
 *  java.io.File
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Boolean
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Thread
 *  java.net.Proxy
 *  java.net.Proxy$Type
 *  java.util.concurrent.atomic.AtomicInteger
 *  javax.annotation.Nullable
 *  javax.imageio.ImageIO
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.IImageBuffer
 *  net.minecraft.client.renderer.texture.SimpleTexture
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.util.ResourceLocation
 *  net.optifine.http.HttpPipeline
 *  net.optifine.http.HttpRequest
 *  net.optifine.http.HttpResponse
 *  net.optifine.player.CapeImageBuffer
 *  net.optifine.shaders.MultiTexID
 *  net.optifine.shaders.ShadersTex
 *  org.apache.commons.io.FileUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.optifine.http.HttpPipeline;
import net.optifine.http.HttpRequest;
import net.optifine.http.HttpResponse;
import net.optifine.player.CapeImageBuffer;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadDownloadImageData
extends SimpleTexture {
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
    public Boolean imageFound = null;
    public boolean pipeline = false;

    public ThreadDownloadImageData(@Nullable File cacheFileIn, String imageUrlIn, ResourceLocation textureResourceLocation, @Nullable IImageBuffer imageBufferIn) {
        super(textureResourceLocation);
        this.cacheFile = cacheFileIn;
        this.imageUrl = imageUrlIn;
        this.imageBuffer = imageBufferIn;
    }

    private void checkTextureUploaded() {
        if (!this.textureUploaded && this.bufferedImage != null) {
            this.textureUploaded = true;
            if (this.textureLocation != null) {
                this.deleteGlTexture();
            }
            if (Config.isShaders()) {
                ShadersTex.loadSimpleTexture((int)super.getGlTextureId(), (BufferedImage)this.bufferedImage, (boolean)false, (boolean)false, (IResourceManager)Config.getResourceManager(), (ResourceLocation)this.textureLocation, (MultiTexID)this.getMultiTexID());
            } else {
                TextureUtil.uploadTextureImage((int)super.getGlTextureId(), (BufferedImage)this.bufferedImage);
            }
        }
    }

    public int getGlTextureId() {
        this.checkTextureUploaded();
        return super.getGlTextureId();
    }

    public void setBufferedImage(BufferedImage bufferedImageIn) {
        this.bufferedImage = bufferedImageIn;
        if (this.imageBuffer != null) {
            this.imageBuffer.skinAvailable();
        }
        this.imageFound = this.bufferedImage != null;
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException {
        if (this.bufferedImage == null && this.textureLocation != null) {
            super.loadTexture(resourceManager);
        }
        if (this.imageThread == null) {
            if (this.cacheFile != null && this.cacheFile.isFile()) {
                LOGGER.debug("Loading http texture from local cache ({})", (Object)this.cacheFile);
                try {
                    this.bufferedImage = ImageIO.read((File)this.cacheFile);
                    if (this.imageBuffer != null) {
                        this.setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage));
                    }
                    this.loadingFinished();
                }
                catch (IOException ioexception) {
                    LOGGER.error("Couldn't load skin {}", (Object)this.cacheFile, (Object)ioexception);
                    this.loadTextureFromServer();
                }
            } else {
                this.loadTextureFromServer();
            }
        }
    }

    protected void loadTextureFromServer() {
        this.imageThread = new /* Unavailable Anonymous Inner Class!! */;
        this.imageThread.setDaemon(true);
        this.imageThread.start();
    }

    private boolean shouldPipeline() {
        if (!this.pipeline) {
            return false;
        }
        Proxy proxy = Minecraft.getMinecraft().getProxy();
        if (proxy.type() != Proxy.Type.DIRECT && proxy.type() != Proxy.Type.SOCKS) {
            return false;
        }
        return this.imageUrl.startsWith("http://");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void loadPipelined() {
        try {
            HttpRequest req = HttpPipeline.makeRequest((String)this.imageUrl, (Proxy)Minecraft.getMinecraft().getProxy());
            HttpResponse resp = HttpPipeline.executeRequest((HttpRequest)req);
            if (resp.getStatus() / 100 == 2) {
                BufferedImage var2;
                byte[] body = resp.getBody();
                ByteArrayInputStream bais = new ByteArrayInputStream(body);
                if (this.cacheFile != null) {
                    FileUtils.copyInputStreamToFile((InputStream)bais, (File)this.cacheFile);
                    var2 = ImageIO.read((File)this.cacheFile);
                } else {
                    var2 = TextureUtil.readBufferedImage((InputStream)bais);
                }
                if (this.imageBuffer != null) {
                    var2 = this.imageBuffer.parseUserSkin(var2);
                }
                this.setBufferedImage(var2);
                return;
            }
        }
        catch (Exception var6) {
            LOGGER.error("Couldn't download http texture: " + var6.getClass().getName() + ": " + var6.getMessage());
            return;
        }
        finally {
            this.loadingFinished();
        }
    }

    private void loadingFinished() {
        this.imageFound = this.bufferedImage != null;
        if (this.imageBuffer instanceof CapeImageBuffer) {
            CapeImageBuffer cib = (CapeImageBuffer)this.imageBuffer;
            cib.cleanup();
        }
    }

    public IImageBuffer getImageBuffer() {
        return this.imageBuffer;
    }

    static /* synthetic */ String access$000(ThreadDownloadImageData x0) {
        return x0.imageUrl;
    }

    static /* synthetic */ File access$100(ThreadDownloadImageData x0) {
        return x0.cacheFile;
    }

    static /* synthetic */ Logger access$200() {
        return LOGGER;
    }

    static /* synthetic */ boolean access$300(ThreadDownloadImageData x0) {
        return x0.shouldPipeline();
    }

    static /* synthetic */ void access$400(ThreadDownloadImageData x0) {
        x0.loadPipelined();
    }

    static /* synthetic */ IImageBuffer access$300(ThreadDownloadImageData x0) {
        return x0.imageBuffer;
    }

    static /* synthetic */ void access$600(ThreadDownloadImageData x0) {
        x0.loadingFinished();
    }
}
