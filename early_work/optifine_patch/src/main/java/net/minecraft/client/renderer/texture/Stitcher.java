/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.List
 *  java.util.Set
 *  net.minecraft.client.renderer.StitcherException
 *  net.minecraft.client.renderer.texture.Stitcher$Holder
 *  net.minecraft.client.renderer.texture.Stitcher$Slot
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.util.math.MathHelper
 *  net.optifine.util.MathUtils
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.MathHelper;
import net.optifine.util.MathUtils;

public class Stitcher {
    private final int mipmapLevelStitcher;
    private final Set<Holder> setStitchHolders = Sets.newHashSetWithExpectedSize((int)256);
    private final List<Slot> stitchSlots = Lists.newArrayListWithCapacity((int)256);
    private int currentWidth;
    private int currentHeight;
    private final int maxWidth;
    private final int maxHeight;
    private final int maxTileDimension;

    public Stitcher(int maxWidthIn, int maxHeightIn, int maxTileDimensionIn, int mipmapLevelStitcherIn) {
        this.mipmapLevelStitcher = mipmapLevelStitcherIn;
        this.maxWidth = maxWidthIn;
        this.maxHeight = maxHeightIn;
        this.maxTileDimension = maxTileDimensionIn;
    }

    public int getCurrentWidth() {
        return this.currentWidth;
    }

    public int getCurrentHeight() {
        return this.currentHeight;
    }

    public void addSprite(TextureAtlasSprite textureAtlas) {
        Holder stitcher$holder = new Holder(textureAtlas, this.mipmapLevelStitcher);
        if (this.maxTileDimension > 0) {
            stitcher$holder.setNewDimension(this.maxTileDimension);
        }
        this.setStitchHolders.add((Object)stitcher$holder);
    }

    public void doStitch() {
        Holder[] astitcher$holder = (Holder[])this.setStitchHolders.toArray((Object[])new Holder[this.setStitchHolders.size()]);
        Arrays.sort((Object[])astitcher$holder);
        for (Holder stitcher$holder : astitcher$holder) {
            if (this.allocateSlot(stitcher$holder)) continue;
            String s = String.format((String)"Unable to fit: %s, size: %dx%d, atlas: %dx%d, atlasMax: %dx%d - Maybe try a lower resolution resourcepack?", (Object[])new Object[]{stitcher$holder.getAtlasSprite().getIconName(), stitcher$holder.getAtlasSprite().getIconWidth(), stitcher$holder.getAtlasSprite().getIconHeight(), this.currentWidth, this.currentHeight, this.maxWidth, this.maxHeight});
            throw new StitcherException(stitcher$holder, s);
        }
        this.currentWidth = MathHelper.smallestEncompassingPowerOfTwo((int)this.currentWidth);
        this.currentHeight = MathHelper.smallestEncompassingPowerOfTwo((int)this.currentHeight);
    }

    public List<TextureAtlasSprite> getStichSlots() {
        ArrayList list = Lists.newArrayList();
        for (Slot stitcher$slot : this.stitchSlots) {
            stitcher$slot.getAllStitchSlots((List)list);
        }
        ArrayList list1 = Lists.newArrayList();
        for (Slot stitcher$slot1 : list) {
            Holder stitcher$holder = stitcher$slot1.getStitchHolder();
            TextureAtlasSprite textureatlassprite = stitcher$holder.getAtlasSprite();
            textureatlassprite.initSprite(this.currentWidth, this.currentHeight, stitcher$slot1.getOriginX(), stitcher$slot1.getOriginY(), stitcher$holder.isRotated());
            list1.add((Object)textureatlassprite);
        }
        return list1;
    }

    private static int getMipmapDimension(int p_147969_0_, int p_147969_1_) {
        return (p_147969_0_ >> p_147969_1_) + ((p_147969_0_ & (1 << p_147969_1_) - 1) == 0 ? 0 : 1) << p_147969_1_;
    }

    private boolean allocateSlot(Holder p_94310_1_) {
        TextureAtlasSprite textureatlassprite = p_94310_1_.getAtlasSprite();
        boolean flag = textureatlassprite.getIconWidth() != textureatlassprite.getIconHeight();
        for (int i = 0; i < this.stitchSlots.size(); ++i) {
            if (((Slot)this.stitchSlots.get(i)).addSlot(p_94310_1_)) {
                return true;
            }
            if (!flag) continue;
            p_94310_1_.rotate();
            if (((Slot)this.stitchSlots.get(i)).addSlot(p_94310_1_)) {
                return true;
            }
            p_94310_1_.rotate();
        }
        return this.expandAndAllocateSlot(p_94310_1_);
    }

    private boolean expandAndAllocateSlot(Holder p_94311_1_) {
        Slot stitcher$slot;
        boolean flag;
        boolean flag2;
        int i = Math.min((int)p_94311_1_.getWidth(), (int)p_94311_1_.getHeight());
        int j = Math.max((int)p_94311_1_.getWidth(), (int)p_94311_1_.getHeight());
        int k = MathHelper.smallestEncompassingPowerOfTwo((int)this.currentWidth);
        int l = MathHelper.smallestEncompassingPowerOfTwo((int)this.currentHeight);
        int i1 = MathHelper.smallestEncompassingPowerOfTwo((int)(this.currentWidth + i));
        int j1 = MathHelper.smallestEncompassingPowerOfTwo((int)(this.currentHeight + i));
        boolean flag1 = i1 <= this.maxWidth;
        boolean bl = flag2 = j1 <= this.maxHeight;
        if (!flag1 && !flag2) {
            return false;
        }
        int po2DownHeight = MathUtils.roundDownToPowerOfTwo((int)this.currentHeight);
        boolean bl2 = flag = flag1 && i1 <= 2 * po2DownHeight;
        if (this.currentWidth == 0 && this.currentHeight == 0) {
            flag = true;
        }
        if (flag) {
            if (p_94311_1_.getWidth() > p_94311_1_.getHeight()) {
                p_94311_1_.rotate();
            }
            if (this.currentHeight == 0) {
                this.currentHeight = p_94311_1_.getHeight();
            }
            stitcher$slot = new Slot(this.currentWidth, 0, p_94311_1_.getWidth(), this.currentHeight);
            this.currentWidth += p_94311_1_.getWidth();
        } else {
            stitcher$slot = new Slot(0, this.currentHeight, this.currentWidth, p_94311_1_.getHeight());
            this.currentHeight += p_94311_1_.getHeight();
        }
        stitcher$slot.addSlot(p_94311_1_);
        this.stitchSlots.add((Object)stitcher$slot);
        return true;
    }

    static /* synthetic */ int access$000(int x0, int x1) {
        return Stitcher.getMipmapDimension(x0, x1);
    }
}
