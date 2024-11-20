/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.awt.image.BufferedImage
 *  java.io.Closeable
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Class
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.System
 *  java.nio.IntBuffer
 *  javax.imageio.ImageIO
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.client.SplashProgress
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.SplashProgress;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(value=Side.CLIENT)
public class TextureUtil {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final IntBuffer DATA_BUFFER = GLAllocation.createDirectIntBuffer((int)0x400000);
    public static final DynamicTexture MISSING_TEXTURE = new DynamicTexture(16, 16);
    public static final int[] MISSING_TEXTURE_DATA = MISSING_TEXTURE.getTextureData();
    private static final float[] COLOR_GAMMAS;
    private static final int[] MIPMAP_BUFFER;

    private static float getColorGamma(int p_188543_0_) {
        return COLOR_GAMMAS[p_188543_0_ & 0xFF];
    }

    public static int glGenTextures() {
        return GlStateManager.generateTexture();
    }

    public static void deleteTexture(int textureId) {
        GlStateManager.deleteTexture((int)textureId);
    }

    public static int uploadTextureImage(int textureId, BufferedImage texture) {
        return TextureUtil.uploadTextureImageAllocate(textureId, texture, false, false);
    }

    public static void uploadTexture(int textureId, int[] p_110988_1_, int p_110988_2_, int p_110988_3_) {
        TextureUtil.bindTexture(textureId);
        TextureUtil.uploadTextureSub(0, p_110988_1_, p_110988_2_, p_110988_3_, 0, 0, false, false, false);
    }

    public static int[][] generateMipmapData(int p_147949_0_, int p_147949_1_, int[][] p_147949_2_) {
        int[][] aint = new int[p_147949_0_ + 1][];
        aint[0] = p_147949_2_[0];
        if (p_147949_0_ > 0) {
            boolean flag = false;
            for (int i = 0; i < p_147949_2_[0].length; ++i) {
                if (p_147949_2_[0][i] >> 24 != 0) continue;
                flag = true;
                break;
            }
            for (int l1 = 1; l1 <= p_147949_0_; ++l1) {
                if (p_147949_2_[l1] != null) {
                    aint[l1] = p_147949_2_[l1];
                    continue;
                }
                int[] aint1 = aint[l1 - 1];
                int[] aint2 = new int[aint1.length >> 2];
                int j = p_147949_1_ >> l1;
                if (j > 0) {
                    int k = aint2.length / j;
                    int l = j << 1;
                    for (int i1 = 0; i1 < j; ++i1) {
                        for (int j1 = 0; j1 < k; ++j1) {
                            int k1 = 2 * (i1 + j1 * l);
                            aint2[i1 + j1 * j] = TextureUtil.blendColors(aint1[k1 + 0], aint1[k1 + 1], aint1[k1 + 0 + l], aint1[k1 + 1 + l], flag);
                        }
                    }
                }
                aint[l1] = aint2;
            }
        }
        return aint;
    }

    private static int blendColors(int p_147943_0_, int p_147943_1_, int p_147943_2_, int p_147943_3_, boolean p_147943_4_) {
        if (p_147943_4_) {
            TextureUtil.MIPMAP_BUFFER[0] = p_147943_0_;
            TextureUtil.MIPMAP_BUFFER[1] = p_147943_1_;
            TextureUtil.MIPMAP_BUFFER[2] = p_147943_2_;
            TextureUtil.MIPMAP_BUFFER[3] = p_147943_3_;
            float f = 0.0f;
            float f1 = 0.0f;
            float f2 = 0.0f;
            float f3 = 0.0f;
            for (int i1 = 0; i1 < 4; ++i1) {
                if (MIPMAP_BUFFER[i1] >> 24 == 0) continue;
                f += TextureUtil.getColorGamma(MIPMAP_BUFFER[i1] >> 24);
                f1 += TextureUtil.getColorGamma(MIPMAP_BUFFER[i1] >> 16);
                f2 += TextureUtil.getColorGamma(MIPMAP_BUFFER[i1] >> 8);
                f3 += TextureUtil.getColorGamma(MIPMAP_BUFFER[i1] >> 0);
            }
            int i2 = (int)(Math.pow((double)(f /= 4.0f), (double)0.45454545454545453) * 255.0);
            int j1 = (int)(Math.pow((double)(f1 /= 4.0f), (double)0.45454545454545453) * 255.0);
            int k1 = (int)(Math.pow((double)(f2 /= 4.0f), (double)0.45454545454545453) * 255.0);
            int l1 = (int)(Math.pow((double)(f3 /= 4.0f), (double)0.45454545454545453) * 255.0);
            if (i2 < 96) {
                i2 = 0;
            }
            return i2 << 24 | j1 << 16 | k1 << 8 | l1;
        }
        int i = TextureUtil.blendColorComponent(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_, 24);
        int j = TextureUtil.blendColorComponent(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_, 16);
        int k = TextureUtil.blendColorComponent(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_, 8);
        int l = TextureUtil.blendColorComponent(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_, 0);
        return i << 24 | j << 16 | k << 8 | l;
    }

    private static int blendColorComponent(int p_147944_0_, int p_147944_1_, int p_147944_2_, int p_147944_3_, int p_147944_4_) {
        float f = TextureUtil.getColorGamma(p_147944_0_ >> p_147944_4_);
        float f1 = TextureUtil.getColorGamma(p_147944_1_ >> p_147944_4_);
        float f2 = TextureUtil.getColorGamma(p_147944_2_ >> p_147944_4_);
        float f3 = TextureUtil.getColorGamma(p_147944_3_ >> p_147944_4_);
        float f4 = (float)((double)((float)Math.pow((double)((double)(f + f1 + f2 + f3) * 0.25), (double)0.45454545454545453)));
        return (int)((double)f4 * 255.0);
    }

    public static void uploadTextureMipmap(int[][] p_147955_0_, int p_147955_1_, int p_147955_2_, int p_147955_3_, int p_147955_4_, boolean p_147955_5_, boolean p_147955_6_) {
        for (int i = 0; i < p_147955_0_.length; ++i) {
            int[] aint = p_147955_0_[i];
            if (p_147955_1_ >> i <= 0 || p_147955_2_ >> i <= 0) break;
            TextureUtil.uploadTextureSub(i, aint, p_147955_1_ >> i, p_147955_2_ >> i, p_147955_3_ >> i, p_147955_4_ >> i, p_147955_5_, p_147955_6_, p_147955_0_.length > 1);
        }
    }

    private static void uploadTextureSub(int p_147947_0_, int[] p_147947_1_, int p_147947_2_, int p_147947_3_, int p_147947_4_, int p_147947_5_, boolean p_147947_6_, boolean p_147947_7_, boolean p_147947_8_) {
        int l;
        int i = 0x400000 / p_147947_2_;
        TextureUtil.setTextureBlurMipmap(p_147947_6_, p_147947_8_);
        TextureUtil.setTextureClamped(p_147947_7_);
        for (int j = 0; j < p_147947_2_ * p_147947_3_; j += p_147947_2_ * l) {
            int k = j / p_147947_2_;
            l = Math.min((int)i, (int)(p_147947_3_ - k));
            int i1 = p_147947_2_ * l;
            TextureUtil.copyToBufferPos(p_147947_1_, j, i1);
            GlStateManager.glTexSubImage2D((int)3553, (int)p_147947_0_, (int)p_147947_4_, (int)(p_147947_5_ + k), (int)p_147947_2_, (int)l, (int)32993, (int)33639, (IntBuffer)DATA_BUFFER);
        }
    }

    public static int uploadTextureImageAllocate(int textureId, BufferedImage texture, boolean blur, boolean clamp) {
        TextureUtil.allocateTexture(textureId, texture.getWidth(), texture.getHeight());
        return TextureUtil.uploadTextureImageSub(textureId, texture, 0, 0, blur, clamp);
    }

    public static void allocateTexture(int textureId, int width, int height) {
        TextureUtil.allocateTextureImpl(textureId, 0, width, height);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void allocateTextureImpl(int glTextureId, int mipmapLevels, int width, int height) {
        Class<SplashProgress> clazz = SplashProgress.class;
        synchronized (SplashProgress.class) {
            TextureUtil.deleteTexture(glTextureId);
            TextureUtil.bindTexture(glTextureId);
            // ** MonitorExit[var4_4] (shouldn't be in output)
            if (mipmapLevels >= 0) {
                GlStateManager.glTexParameteri((int)3553, (int)33085, (int)mipmapLevels);
                GlStateManager.glTexParameteri((int)3553, (int)33082, (int)0);
                GlStateManager.glTexParameteri((int)3553, (int)33083, (int)mipmapLevels);
                GlStateManager.glTexParameterf((int)3553, (int)34049, (float)0.0f);
            }
            for (int i = 0; i <= mipmapLevels; ++i) {
                GlStateManager.glTexImage2D((int)3553, (int)i, (int)6408, (int)(width >> i), (int)(height >> i), (int)0, (int)32993, (int)33639, (IntBuffer)null);
            }
            return;
        }
    }

    public static int uploadTextureImageSub(int textureId, BufferedImage p_110995_1_, int p_110995_2_, int p_110995_3_, boolean p_110995_4_, boolean p_110995_5_) {
        TextureUtil.bindTexture(textureId);
        TextureUtil.uploadTextureImageSubImpl(p_110995_1_, p_110995_2_, p_110995_3_, p_110995_4_, p_110995_5_);
        return textureId;
    }

    private static void uploadTextureImageSubImpl(BufferedImage p_110993_0_, int p_110993_1_, int p_110993_2_, boolean p_110993_3_, boolean p_110993_4_) {
        int i = p_110993_0_.getWidth();
        int j = p_110993_0_.getHeight();
        int k = 0x400000 / i;
        int[] aint = new int[k * i];
        TextureUtil.setTextureBlurred(p_110993_3_);
        TextureUtil.setTextureClamped(p_110993_4_);
        for (int l = 0; l < i * j; l += i * k) {
            int i1 = l / i;
            int j1 = Math.min((int)k, (int)(j - i1));
            int k1 = i * j1;
            p_110993_0_.getRGB(0, i1, i, j1, aint, 0, i);
            TextureUtil.copyToBuffer(aint, k1);
            GlStateManager.glTexSubImage2D((int)3553, (int)0, (int)p_110993_1_, (int)(p_110993_2_ + i1), (int)i, (int)j1, (int)32993, (int)33639, (IntBuffer)DATA_BUFFER);
        }
    }

    private static void setTextureClamped(boolean p_110997_0_) {
        if (p_110997_0_) {
            GlStateManager.glTexParameteri((int)3553, (int)10242, (int)10496);
            GlStateManager.glTexParameteri((int)3553, (int)10243, (int)10496);
        } else {
            GlStateManager.glTexParameteri((int)3553, (int)10242, (int)10497);
            GlStateManager.glTexParameteri((int)3553, (int)10243, (int)10497);
        }
    }

    private static void setTextureBlurred(boolean p_147951_0_) {
        TextureUtil.setTextureBlurMipmap(p_147951_0_, false);
    }

    private static void setTextureBlurMipmap(boolean p_147954_0_, boolean p_147954_1_) {
        if (p_147954_0_) {
            GlStateManager.glTexParameteri((int)3553, (int)10241, (int)(p_147954_1_ ? 9987 : 9729));
            GlStateManager.glTexParameteri((int)3553, (int)10240, (int)9729);
        } else {
            GlStateManager.glTexParameteri((int)3553, (int)10241, (int)(p_147954_1_ ? 9986 : 9728));
            GlStateManager.glTexParameteri((int)3553, (int)10240, (int)9728);
        }
    }

    private static void copyToBuffer(int[] p_110990_0_, int p_110990_1_) {
        TextureUtil.copyToBufferPos(p_110990_0_, 0, p_110990_1_);
    }

    private static void copyToBufferPos(int[] p_110994_0_, int p_110994_1_, int p_110994_2_) {
        int[] aint = p_110994_0_;
        if (Minecraft.getMinecraft().gameSettings.anaglyph) {
            aint = TextureUtil.updateAnaglyph(p_110994_0_);
        }
        DATA_BUFFER.clear();
        DATA_BUFFER.put(aint, p_110994_1_, p_110994_2_);
        DATA_BUFFER.position(0).limit(p_110994_2_);
    }

    static void bindTexture(int p_94277_0_) {
        GlStateManager.bindTexture((int)p_94277_0_);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int[] readImageData(IResourceManager resourceManager, ResourceLocation imageLocation) throws IOException {
        int[] aint1;
        IResource iresource = null;
        try {
            iresource = resourceManager.getResource(imageLocation);
            BufferedImage bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
            int i = bufferedimage.getWidth();
            int j = bufferedimage.getHeight();
            int[] aint = new int[i * j];
            bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
            aint1 = aint;
        }
        finally {
            IOUtils.closeQuietly((Closeable)iresource);
        }
        return aint1;
    }

    public static BufferedImage readBufferedImage(InputStream imageStream) throws IOException {
        BufferedImage bufferedimage;
        try {
            bufferedimage = ImageIO.read((InputStream)imageStream);
        }
        finally {
            IOUtils.closeQuietly((InputStream)imageStream);
        }
        return bufferedimage;
    }

    public static int[] updateAnaglyph(int[] p_110985_0_) {
        int[] aint = new int[p_110985_0_.length];
        for (int i = 0; i < p_110985_0_.length; ++i) {
            aint[i] = TextureUtil.anaglyphColor(p_110985_0_[i]);
        }
        return aint;
    }

    public static int anaglyphColor(int p_177054_0_) {
        int i = p_177054_0_ >> 24 & 0xFF;
        int j = p_177054_0_ >> 16 & 0xFF;
        int k = p_177054_0_ >> 8 & 0xFF;
        int l = p_177054_0_ & 0xFF;
        int i1 = (j * 30 + k * 59 + l * 11) / 100;
        int j1 = (j * 30 + k * 70) / 100;
        int k1 = (j * 30 + l * 70) / 100;
        return i << 24 | i1 << 16 | j1 << 8 | k1;
    }

    public static void processPixelValues(int[] p_147953_0_, int p_147953_1_, int p_147953_2_) {
        int[] aint = new int[p_147953_1_];
        int i = p_147953_2_ / 2;
        for (int j = 0; j < i; ++j) {
            System.arraycopy((Object)p_147953_0_, (int)(j * p_147953_1_), (Object)aint, (int)0, (int)p_147953_1_);
            System.arraycopy((Object)p_147953_0_, (int)((p_147953_2_ - 1 - j) * p_147953_1_), (Object)p_147953_0_, (int)(j * p_147953_1_), (int)p_147953_1_);
            System.arraycopy((Object)aint, (int)0, (Object)p_147953_0_, (int)((p_147953_2_ - 1 - j) * p_147953_1_), (int)p_147953_1_);
        }
    }

    static {
        int i = -16777216;
        int j = -524040;
        int[] aint = new int[]{-524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040};
        int[] aint1 = new int[]{-16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216};
        int k = aint.length;
        for (int l = 0; l < 16; ++l) {
            System.arraycopy((Object)(l < k ? aint : aint1), (int)0, (Object)MISSING_TEXTURE_DATA, (int)(16 * l), (int)k);
            System.arraycopy((Object)(l < k ? aint1 : aint), (int)0, (Object)MISSING_TEXTURE_DATA, (int)(16 * l + k), (int)k);
        }
        MISSING_TEXTURE.updateDynamicTexture();
        COLOR_GAMMAS = new float[256];
        for (int i1 = 0; i1 < COLOR_GAMMAS.length; ++i1) {
            TextureUtil.COLOR_GAMMAS[i1] = (float)Math.pow((double)((float)i1 / 255.0f), (double)2.2);
        }
        MIPMAP_BUFFER = new int[4];
    }
}
