/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.awt.Image
 *  java.awt.image.BufferedImage
 *  java.awt.image.ImageObserver
 *  java.io.Closeable
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.List
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.item.EnumDyeColor
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorMethod
 *  net.optifine.shaders.MultiTexID
 *  net.optifine.shaders.ShadersTex
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.texture;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LayeredColorMaskTexture
extends AbstractTexture {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ResourceLocation textureLocation;
    private final List<String> listTextures;
    private final List<EnumDyeColor> listDyeColors;

    public LayeredColorMaskTexture(ResourceLocation textureLocationIn, List<String> p_i46101_2_, List<EnumDyeColor> p_i46101_3_) {
        this.textureLocation = textureLocationIn;
        this.listTextures = p_i46101_2_;
        this.listDyeColors = p_i46101_3_;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        block16: {
            this.deleteGlTexture();
            iresource = null;
            try {
                iresource = resourceManager.getResource(this.textureLocation);
                bufferedimage1 = TextureUtil.readBufferedImage((InputStream)iresource.getInputStream());
                i = bufferedimage1.getType();
                if (i == 0) {
                    i = 6;
                }
                bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), i);
                graphics = bufferedimage.getGraphics();
                graphics.drawImage((Image)bufferedimage1, 0, 0, (ImageObserver)null);
                j = 0;
lbl14:
                // 2 sources

                while (true) {
                    if (j >= 17 || j >= this.listTextures.size() || j >= this.listDyeColors.size()) {
                    }
                    ** GOTO lbl-1000
                    break;
                }
            }
            catch (IOException ioexception) {
                LayeredColorMaskTexture.LOGGER.error("Couldn't load layered image", (Throwable)ioexception);
                IOUtils.closeQuietly((Closeable)iresource);
                return;
            }
            catch (Throwable var19_20) {
                IOUtils.closeQuietly(iresource);
                throw var19_20;
            }
            IOUtils.closeQuietly((Closeable)iresource);
            if (!Config.isShaders()) {
                TextureUtil.uploadTextureImage((int)this.getGlTextureId(), (BufferedImage)bufferedimage);
                return;
            }
            break block16;
lbl-1000:
            // 1 sources

            {
                block15: {
                    iresource1 = null;
                    try {
                        s = (String)this.listTextures.get(j);
                        k = ((EnumDyeColor)this.listDyeColors.get(j)).getColorValue();
                        if (s == null) break block15;
                        iresource1 = resourceManager.getResource(new ResourceLocation(s));
                        v0 = bufferedimage2 = Reflector.MinecraftForgeClient_getImageLayer.exists() != false ? (BufferedImage)Reflector.call((ReflectorMethod)Reflector.MinecraftForgeClient_getImageLayer, (Object[])new Object[]{new ResourceLocation(s), resourceManager}) : TextureUtil.readBufferedImage((InputStream)iresource1.getInputStream());
                        if (bufferedimage2.getWidth() == bufferedimage.getWidth() && bufferedimage2.getHeight() == bufferedimage.getHeight() && bufferedimage2.getType() == 6) {
                            for (l = 0; l < bufferedimage2.getHeight(); ++l) {
                                for (i1 = 0; i1 < bufferedimage2.getWidth(); ++i1) {
                                    j1 = bufferedimage2.getRGB(i1, l);
                                    if ((j1 & -16777216) == 0) continue;
                                    k1 = (j1 & 0xFF0000) << 8 & -16777216;
                                    l1 = bufferedimage1.getRGB(i1, l);
                                    i2 = MathHelper.multiplyColor((int)l1, (int)k) & 0xFFFFFF;
                                    bufferedimage2.setRGB(i1, l, k1 | i2);
                                }
                            }
                            bufferedimage.getGraphics().drawImage((Image)bufferedimage2, 0, 0, (ImageObserver)null);
                        }
                    }
                    finally {
                        IOUtils.closeQuietly(iresource1);
                    }
                }
                ++j;
                ** continue;
            }
        }
        ShadersTex.loadSimpleTexture((int)this.getGlTextureId(), (BufferedImage)bufferedimage, (boolean)false, (boolean)false, (IResourceManager)resourceManager, (ResourceLocation)this.textureLocation, (MultiTexID)this.getMultiTexID());
    }
}
