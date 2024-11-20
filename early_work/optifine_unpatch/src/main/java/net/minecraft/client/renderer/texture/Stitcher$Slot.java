/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  net.minecraft.client.renderer.texture.Stitcher$Holder
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public static class Stitcher.Slot {
    private final int originX;
    private final int originY;
    private final int width;
    private final int height;
    private List<Stitcher.Slot> subSlots;
    private Stitcher.Holder holder;

    public Stitcher.Slot(int originXIn, int originYIn, int widthIn, int heightIn) {
        this.originX = originXIn;
        this.originY = originYIn;
        this.width = widthIn;
        this.height = heightIn;
    }

    public Stitcher.Holder getStitchHolder() {
        return this.holder;
    }

    public int getOriginX() {
        return this.originX;
    }

    public int getOriginY() {
        return this.originY;
    }

    public boolean addSlot(Stitcher.Holder holderIn) {
        if (this.holder != null) {
            return false;
        }
        int i = holderIn.getWidth();
        int j = holderIn.getHeight();
        if (i <= this.width && j <= this.height) {
            if (i == this.width && j == this.height) {
                this.holder = holderIn;
                return true;
            }
            if (this.subSlots == null) {
                this.subSlots = Lists.newArrayListWithCapacity((int)1);
                this.subSlots.add((Object)new Stitcher.Slot(this.originX, this.originY, i, j));
                int k = this.width - i;
                int l = this.height - j;
                if (l > 0 && k > 0) {
                    int j1;
                    int i1 = Math.max((int)this.height, (int)k);
                    if (i1 >= (j1 = Math.max((int)this.width, (int)l))) {
                        this.subSlots.add((Object)new Stitcher.Slot(this.originX, this.originY + j, i, l));
                        this.subSlots.add((Object)new Stitcher.Slot(this.originX + i, this.originY, k, this.height));
                    } else {
                        this.subSlots.add((Object)new Stitcher.Slot(this.originX + i, this.originY, k, j));
                        this.subSlots.add((Object)new Stitcher.Slot(this.originX, this.originY + j, this.width, l));
                    }
                } else if (k == 0) {
                    this.subSlots.add((Object)new Stitcher.Slot(this.originX, this.originY + j, i, l));
                } else if (l == 0) {
                    this.subSlots.add((Object)new Stitcher.Slot(this.originX + i, this.originY, k, j));
                }
            }
            for (Stitcher.Slot stitcher$slot : this.subSlots) {
                if (!stitcher$slot.addSlot(holderIn)) continue;
                return true;
            }
            return false;
        }
        return false;
    }

    public void getAllStitchSlots(List<Stitcher.Slot> p_94184_1_) {
        if (this.holder != null) {
            p_94184_1_.add((Object)this);
        } else if (this.subSlots != null) {
            for (Stitcher.Slot stitcher$slot : this.subSlots) {
                stitcher$slot.getAllStitchSlots(p_94184_1_);
            }
        }
    }

    public String toString() {
        return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + String.valueOf((Object)this.holder) + ", subSlots=" + String.valueOf(this.subSlots) + "}";
    }
}
