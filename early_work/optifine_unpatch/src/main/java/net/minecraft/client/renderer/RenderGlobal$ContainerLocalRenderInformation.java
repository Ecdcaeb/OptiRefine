/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  javax.annotation.Nullable
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.chunk.RenderChunk
 *  net.minecraft.util.EnumFacing
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
class RenderGlobal.ContainerLocalRenderInformation {
    final RenderChunk renderChunk;
    final EnumFacing facing;
    byte setFacing;
    final int counter;

    private RenderGlobal.ContainerLocalRenderInformation(RenderGlobal this$0, @Nullable RenderChunk renderChunkIn, EnumFacing facingIn, int counterIn) {
        this.renderChunk = renderChunkIn;
        this.facing = facingIn;
        this.counter = counterIn;
    }

    public void setDirection(byte p_189561_1_, EnumFacing p_189561_2_) {
        this.setFacing = (byte)(this.setFacing | p_189561_1_ | 1 << p_189561_2_.ordinal());
    }

    public boolean hasDirection(EnumFacing p_189560_1_) {
        return (this.setFacing & 1 << p_189560_1_.ordinal()) > 0;
    }
}
