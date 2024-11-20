/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.awt.image.BufferedImage
 *  java.io.Closeable
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.Throwable
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.client.resources.data.TextureMetadataSection
 *  net.minecraft.util.ResourceLocation
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleTexture
extends AbstractTexture {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final ResourceLocation textureLocation;

    public SimpleTexture(ResourceLocation resourceLocation) {
        this.textureLocation = resourceLocation;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void loadTexture(IResourceManager iResourceManager) throws IOException {
        this.deleteGlTexture();
        IResource iResource = null;
        try {
            iResource = iResourceManager.getResource(this.textureLocation);
            BufferedImage bufferedImage = TextureUtil.readBufferedImage((InputStream)iResource.getInputStream());
            boolean \u26032 = false;
            boolean \u26033 = false;
            if (iResource.hasMetadata()) {
                try {
                    TextureMetadataSection textureMetadataSection = (TextureMetadataSection)iResource.getMetadata("texture");
                    if (textureMetadataSection != null) {
                        \u26032 = textureMetadataSection.getTextureBlur();
                        \u26033 = textureMetadataSection.getTextureClamp();
                    }
                }
                catch (RuntimeException runtimeException) {
                    LOGGER.warn("Failed reading metadata of: {}", (Object)this.textureLocation, (Object)runtimeException);
                }
            }
            TextureUtil.uploadTextureImageAllocate((int)this.getGlTextureId(), (BufferedImage)bufferedImage, (boolean)\u26032, (boolean)\u26033);
        }
        catch (Throwable throwable) {
            IOUtils.closeQuietly(iResource);
            throw throwable;
        }
        IOUtils.closeQuietly((Closeable)iResource);
    }
}
