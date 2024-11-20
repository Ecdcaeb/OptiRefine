/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Lists
 *  java.awt.image.BufferedImage
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.List
 *  java.util.function.Function
 *  net.minecraft.client.renderer.texture.PngSizeInfo
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.client.resources.data.AnimationFrame
 *  net.minecraft.client.resources.data.AnimationMetadataSection
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.crash.ICrashReportDetail
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.renderer.texture.PngSizeInfo;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class TextureAtlasSprite {
    private final String iconName;
    protected List<int[][]> framesTextureData = Lists.newArrayList();
    protected int[][] interpolatedFrameData;
    private AnimationMetadataSection animationMetadata;
    protected boolean rotated;
    protected int originX;
    protected int originY;
    protected int width;
    protected int height;
    private float minU;
    private float maxU;
    private float minV;
    private float maxV;
    protected int frameCounter;
    protected int tickCounter;

    protected TextureAtlasSprite(String spriteName) {
        this.iconName = spriteName;
    }

    protected static TextureAtlasSprite makeAtlasSprite(ResourceLocation spriteResourceLocation) {
        return new TextureAtlasSprite(spriteResourceLocation.toString());
    }

    public void initSprite(int inX, int inY, int originInX, int originInY, boolean rotatedIn) {
        this.originX = originInX;
        this.originY = originInY;
        this.rotated = rotatedIn;
        this.minU = (float)originInX / (float)inX;
        this.maxU = (float)(originInX + this.width) / (float)inX;
        this.minV = (float)originInY / (float)inY;
        this.maxV = (float)(originInY + this.height) / (float)inY;
    }

    public void copyFrom(TextureAtlasSprite atlasSpirit) {
        this.originX = atlasSpirit.originX;
        this.originY = atlasSpirit.originY;
        this.width = atlasSpirit.width;
        this.height = atlasSpirit.height;
        this.rotated = atlasSpirit.rotated;
        this.minU = atlasSpirit.minU;
        this.maxU = atlasSpirit.maxU;
        this.minV = atlasSpirit.minV;
        this.maxV = atlasSpirit.maxV;
    }

    public int getOriginX() {
        return this.originX;
    }

    public int getOriginY() {
        return this.originY;
    }

    public int getIconWidth() {
        return this.width;
    }

    public int getIconHeight() {
        return this.height;
    }

    public float getMinU() {
        return this.minU;
    }

    public float getMaxU() {
        return this.maxU;
    }

    public float getInterpolatedU(double u) {
        float f = this.maxU - this.minU;
        return this.minU + f * (float)u / 16.0f;
    }

    public float getUnInterpolatedU(float u) {
        float f = this.maxU - this.minU;
        return (u - this.minU) / f * 16.0f;
    }

    public float getMinV() {
        return this.minV;
    }

    public float getMaxV() {
        return this.maxV;
    }

    public float getInterpolatedV(double v) {
        float f = this.maxV - this.minV;
        return this.minV + f * (float)v / 16.0f;
    }

    public float getUnInterpolatedV(float p_188536_1_) {
        float f = this.maxV - this.minV;
        return (p_188536_1_ - this.minV) / f * 16.0f;
    }

    public String getIconName() {
        return this.iconName;
    }

    public void updateAnimation() {
        ++this.tickCounter;
        if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
            int i = this.animationMetadata.getFrameIndex(this.frameCounter);
            int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
            this.frameCounter = (this.frameCounter + 1) % j;
            this.tickCounter = 0;
            int k = this.animationMetadata.getFrameIndex(this.frameCounter);
            if (i != k && k >= 0 && k < this.framesTextureData.size()) {
                TextureUtil.uploadTextureMipmap((int[][])((int[][])this.framesTextureData.get(k)), (int)this.width, (int)this.height, (int)this.originX, (int)this.originY, (boolean)false, (boolean)false);
            }
        } else if (this.animationMetadata.isInterpolate()) {
            this.updateAnimationInterpolated();
        }
    }

    private void updateAnimationInterpolated() {
        int j;
        int k;
        double d0 = 1.0 - (double)this.tickCounter / (double)this.animationMetadata.getFrameTimeSingle(this.frameCounter);
        int i = this.animationMetadata.getFrameIndex(this.frameCounter);
        if (i != (k = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % (j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount()))) && k >= 0 && k < this.framesTextureData.size()) {
            int[][] aint = (int[][])this.framesTextureData.get(i);
            int[][] aint1 = (int[][])this.framesTextureData.get(k);
            if (this.interpolatedFrameData == null || this.interpolatedFrameData.length != aint.length) {
                this.interpolatedFrameData = new int[aint.length][];
            }
            for (int l = 0; l < aint.length; ++l) {
                if (this.interpolatedFrameData[l] == null) {
                    this.interpolatedFrameData[l] = new int[aint[l].length];
                }
                if (l >= aint1.length || aint1[l].length != aint[l].length) continue;
                for (int i1 = 0; i1 < aint[l].length; ++i1) {
                    int j1 = aint[l][i1];
                    int k1 = aint1[l][i1];
                    int l1 = this.interpolateColor(d0, j1 >> 16 & 0xFF, k1 >> 16 & 0xFF);
                    int i2 = this.interpolateColor(d0, j1 >> 8 & 0xFF, k1 >> 8 & 0xFF);
                    int j2 = this.interpolateColor(d0, j1 & 0xFF, k1 & 0xFF);
                    this.interpolatedFrameData[l][i1] = j1 & 0xFF000000 | l1 << 16 | i2 << 8 | j2;
                }
            }
            TextureUtil.uploadTextureMipmap((int[][])this.interpolatedFrameData, (int)this.width, (int)this.height, (int)this.originX, (int)this.originY, (boolean)false, (boolean)false);
        }
    }

    private int interpolateColor(double p_188535_1_, int p_188535_3_, int p_188535_4_) {
        return (int)(p_188535_1_ * (double)p_188535_3_ + (1.0 - p_188535_1_) * (double)p_188535_4_);
    }

    public int[][] getFrameTextureData(int index) {
        return (int[][])this.framesTextureData.get(index);
    }

    public int getFrameCount() {
        return this.framesTextureData.size();
    }

    public void setIconWidth(int newWidth) {
        this.width = newWidth;
    }

    public void setIconHeight(int newHeight) {
        this.height = newHeight;
    }

    public void loadSprite(PngSizeInfo sizeInfo, boolean p_188538_2_) throws IOException {
        this.resetSprite();
        this.width = sizeInfo.pngWidth;
        this.height = sizeInfo.pngHeight;
        if (p_188538_2_) {
            this.height = this.width;
        } else if (sizeInfo.pngHeight != sizeInfo.pngWidth) {
            throw new RuntimeException("broken aspect ratio and not an animation");
        }
    }

    public void loadSpriteFrames(IResource resource, int mipmaplevels) throws IOException {
        BufferedImage bufferedimage = TextureUtil.readBufferedImage((InputStream)resource.getInputStream());
        AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)resource.getMetadata("animation");
        int[][] aint = new int[mipmaplevels][];
        aint[0] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
        bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[0], 0, bufferedimage.getWidth());
        if (animationmetadatasection == null) {
            this.framesTextureData.add((Object)aint);
        } else {
            int i = bufferedimage.getHeight() / this.width;
            if (animationmetadatasection.getFrameCount() > 0) {
                Iterator lvt_7_1_ = animationmetadatasection.getFrameIndexSet().iterator();
                while (lvt_7_1_.hasNext()) {
                    int j = (Integer)lvt_7_1_.next();
                    if (j >= i) {
                        throw new RuntimeException("invalid frameindex " + j);
                    }
                    this.allocateFrameTextureData(j);
                    this.framesTextureData.set(j, (Object)TextureAtlasSprite.getFrameTextureData(aint, this.width, this.width, j));
                }
                this.animationMetadata = animationmetadatasection;
            } else {
                ArrayList list = Lists.newArrayList();
                for (int k = 0; k < i; ++k) {
                    this.framesTextureData.add((Object)TextureAtlasSprite.getFrameTextureData(aint, this.width, this.width, k));
                    list.add((Object)new AnimationFrame(k, -1));
                }
                this.animationMetadata = new AnimationMetadataSection((List)list, this.width, this.height, animationmetadatasection.getFrameTime(), animationmetadatasection.isInterpolate());
            }
        }
    }

    public void generateMipmaps(int level) {
        ArrayList list = Lists.newArrayList();
        for (int i = 0; i < this.framesTextureData.size(); ++i) {
            int[][] aint = (int[][])this.framesTextureData.get(i);
            if (aint == null) continue;
            try {
                list.add((Object)TextureUtil.generateMipmapData((int)level, (int)this.width, (int[][])aint));
                continue;
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Generating mipmaps for frame");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Frame being iterated");
                crashreportcategory.addCrashSection("Frame index", (Object)i);
                crashreportcategory.addDetail("Frame sizes", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
                throw new ReportedException(crashreport);
            }
        }
        this.setFramesTextureData((List<int[][]>)list);
    }

    private void allocateFrameTextureData(int index) {
        if (this.framesTextureData.size() <= index) {
            for (int i = this.framesTextureData.size(); i <= index; ++i) {
                this.framesTextureData.add(null);
            }
        }
    }

    private static int[][] getFrameTextureData(int[][] data, int rows, int columns, int p_147962_3_) {
        int[][] aint = new int[data.length][];
        for (int i = 0; i < data.length; ++i) {
            int[] aint1 = data[i];
            if (aint1 == null) continue;
            aint[i] = new int[(rows >> i) * (columns >> i)];
            System.arraycopy((Object)aint1, (int)(p_147962_3_ * aint[i].length), (Object)aint[i], (int)0, (int)aint[i].length);
        }
        return aint;
    }

    public void clearFramesTextureData() {
        this.framesTextureData.clear();
    }

    public boolean hasAnimationMetadata() {
        return this.animationMetadata != null;
    }

    public void setFramesTextureData(List<int[][]> newFramesTextureData) {
        this.framesTextureData = newFramesTextureData;
    }

    private void resetSprite() {
        this.animationMetadata = null;
        this.setFramesTextureData((List<int[][]>)Lists.newArrayList());
        this.frameCounter = 0;
        this.tickCounter = 0;
    }

    public String toString() {
        return "TextureAtlasSprite{name='" + this.iconName + "', frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + "}";
    }

    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return false;
    }

    public boolean load(IResourceManager manager, ResourceLocation location, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
        return true;
    }

    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.of();
    }
}
