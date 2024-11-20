/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
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
 *  net.optifine.EmissiveTextures
 *  net.optifine.shaders.MultiTexID
 *  net.optifine.shaders.ShadersTex
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
import net.optifine.EmissiveTextures;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleTexture
extends AbstractTexture {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final ResourceLocation textureLocation;
    public ResourceLocation locationEmissive;
    public boolean isEmissive;

    public SimpleTexture(ResourceLocation textureResourceLocation) {
        this.textureLocation = textureResourceLocation;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        IResource iresource = null;
        try {
            iresource = resourceManager.getResource(this.textureLocation);
            BufferedImage bufferedimage = TextureUtil.readBufferedImage((InputStream)iresource.getInputStream());
            boolean flag = false;
            boolean flag1 = false;
            if (iresource.hasMetadata()) {
                try {
                    TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");
                    if (texturemetadatasection != null) {
                        flag = texturemetadatasection.getTextureBlur();
                        flag1 = texturemetadatasection.getTextureClamp();
                    }
                }
                catch (RuntimeException runtimeexception) {
                    LOGGER.warn("Failed reading metadata of: {}", (Object)this.textureLocation, (Object)runtimeexception);
                }
            }
            if (Config.isShaders()) {
                ShadersTex.loadSimpleTexture((int)this.getGlTextureId(), (BufferedImage)bufferedimage, (boolean)flag, (boolean)flag1, (IResourceManager)resourceManager, (ResourceLocation)this.textureLocation, (MultiTexID)this.getMultiTexID());
            } else {
                TextureUtil.uploadTextureImageAllocate((int)this.getGlTextureId(), (BufferedImage)bufferedimage, (boolean)flag, (boolean)flag1);
            }
            if (EmissiveTextures.isActive()) {
                EmissiveTextures.loadTexture((ResourceLocation)this.textureLocation, (SimpleTexture)this);
            }
        }
        catch (Throwable throwable) {
            IOUtils.closeQuietly(iresource);
            throw throwable;
        }
        IOUtils.closeQuietly((Closeable)iresource);
    }
}
