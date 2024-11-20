/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.awt.image.BufferedImage
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Thread
 *  java.util.concurrent.atomic.AtomicInteger
 *  javax.annotation.Nullable
 *  javax.imageio.ImageIO
 *  net.minecraft.client.renderer.IImageBuffer
 *  net.minecraft.client.renderer.texture.SimpleTexture
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.util.ResourceLocation
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
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

    public ThreadDownloadImageData(@Nullable File file, String string, ResourceLocation resourceLocation, @Nullable IImageBuffer iImageBuffer) {
        super(resourceLocation);
        this.cacheFile = file;
        this.imageUrl = string;
        this.imageBuffer = iImageBuffer;
    }

    private void checkTextureUploaded() {
        if (this.textureUploaded) {
            return;
        }
        if (this.bufferedImage != null) {
            if (this.textureLocation != null) {
                this.deleteGlTexture();
            }
            TextureUtil.uploadTextureImage((int)super.getGlTextureId(), (BufferedImage)this.bufferedImage);
            this.textureUploaded = true;
        }
    }

    public int getGlTextureId() {
        this.checkTextureUploaded();
        return super.getGlTextureId();
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        if (this.imageBuffer != null) {
            this.imageBuffer.skinAvailable();
        }
    }

    public void loadTexture(IResourceManager iResourceManager) throws IOException {
        if (this.bufferedImage == null && this.textureLocation != null) {
            super.loadTexture(iResourceManager);
        }
        if (this.imageThread == null) {
            if (this.cacheFile != null && this.cacheFile.isFile()) {
                LOGGER.debug("Loading http texture from local cache ({})", (Object)this.cacheFile);
                try {
                    this.bufferedImage = ImageIO.read((File)this.cacheFile);
                    if (this.imageBuffer != null) {
                        this.setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage));
                    }
                }
                catch (IOException iOException) {
                    LOGGER.error("Couldn't load skin {}", (Object)this.cacheFile, (Object)iOException);
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

    static /* synthetic */ String access$000(ThreadDownloadImageData threadDownloadImageData) {
        return threadDownloadImageData.imageUrl;
    }

    static /* synthetic */ File access$100(ThreadDownloadImageData threadDownloadImageData) {
        return threadDownloadImageData.cacheFile;
    }

    static /* synthetic */ Logger access$200() {
        return LOGGER;
    }

    static /* synthetic */ IImageBuffer access$300(ThreadDownloadImageData threadDownloadImageData) {
        return threadDownloadImageData.imageBuffer;
    }
}
