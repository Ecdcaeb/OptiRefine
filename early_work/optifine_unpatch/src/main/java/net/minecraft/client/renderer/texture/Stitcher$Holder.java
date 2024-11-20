/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Comparable
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.client.renderer.texture.Stitcher
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Exception performing whole class analysis ignored.
 */
@SideOnly(value=Side.CLIENT)
public static class Stitcher.Holder
implements Comparable<Stitcher.Holder> {
    private final TextureAtlasSprite sprite;
    private final int width;
    private final int height;
    private final int mipmapLevelHolder;
    private boolean rotated;
    private float scaleFactor = 1.0f;

    public Stitcher.Holder(TextureAtlasSprite theTextureIn, int mipmapLevelHolderIn) {
        this.sprite = theTextureIn;
        this.width = theTextureIn.getIconWidth();
        this.height = theTextureIn.getIconHeight();
        this.mipmapLevelHolder = mipmapLevelHolderIn;
        this.rotated = Stitcher.getMipmapDimension((int)this.height, (int)mipmapLevelHolderIn) > Stitcher.getMipmapDimension((int)this.width, (int)mipmapLevelHolderIn);
    }

    public TextureAtlasSprite getAtlasSprite() {
        return this.sprite;
    }

    public int getWidth() {
        int i = this.rotated ? this.height : this.width;
        return Stitcher.getMipmapDimension((int)((int)((float)i * this.scaleFactor)), (int)this.mipmapLevelHolder);
    }

    public int getHeight() {
        int i = this.rotated ? this.width : this.height;
        return Stitcher.getMipmapDimension((int)((int)((float)i * this.scaleFactor)), (int)this.mipmapLevelHolder);
    }

    public void rotate() {
        this.rotated = !this.rotated;
    }

    public boolean isRotated() {
        return this.rotated;
    }

    public void setNewDimension(int p_94196_1_) {
        if (this.width > p_94196_1_ && this.height > p_94196_1_) {
            this.scaleFactor = (float)p_94196_1_ / (float)Math.min((int)this.width, (int)this.height);
        }
    }

    public String toString() {
        return "Holder{width=" + this.width + ", height=" + this.height + ", name=" + this.sprite.getIconName() + "}";
    }

    public int compareTo(Stitcher.Holder p_compareTo_1_) {
        int i;
        if (this.getHeight() == p_compareTo_1_.getHeight()) {
            if (this.getWidth() == p_compareTo_1_.getWidth()) {
                if (this.sprite.getIconName() == null) {
                    return p_compareTo_1_.sprite.getIconName() == null ? 0 : -1;
                }
                return this.sprite.getIconName().compareTo(p_compareTo_1_.sprite.getIconName());
            }
            i = this.getWidth() < p_compareTo_1_.getWidth() ? 1 : -1;
        } else {
            i = this.getHeight() < p_compareTo_1_.getHeight() ? 1 : -1;
        }
        return i;
    }
}
