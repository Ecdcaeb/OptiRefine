/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.block.model.BlockFaceUV
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
static abstract class FaceBakery.Rotation {
    private FaceBakery.Rotation() {
    }

    public BlockFaceUV rotateUV(BlockFaceUV p_188006_1_) {
        float f = p_188006_1_.getVertexU(p_188006_1_.getVertexRotatedRev(0));
        float f1 = p_188006_1_.getVertexV(p_188006_1_.getVertexRotatedRev(0));
        float f2 = p_188006_1_.getVertexU(p_188006_1_.getVertexRotatedRev(2));
        float f3 = p_188006_1_.getVertexV(p_188006_1_.getVertexRotatedRev(2));
        return this.makeRotatedUV(f, f1, f2, f3);
    }

    abstract BlockFaceUV makeRotatedUV(float var1, float var2, float var3, float var4);
}
