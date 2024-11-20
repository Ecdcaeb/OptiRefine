/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.block.model.BlockFaceUV
 *  net.minecraft.client.renderer.block.model.FaceBakery$Rotation
 */
package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;

class FaceBakery.1
extends FaceBakery.Rotation {
    FaceBakery.1() {
    }

    BlockFaceUV makeRotatedUV(float p_188007_1_, float p_188007_2_, float p_188007_3_, float p_188007_4_) {
        return new BlockFaceUV(new float[]{p_188007_1_, p_188007_2_, p_188007_3_, p_188007_4_}, 0);
    }
}
