/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.chunk.RenderChunk
 *  net.minecraft.util.EnumFacing
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumFacing;

public static class RenderGlobal.ContainerLocalRenderInformation {
    final RenderChunk renderChunk;
    EnumFacing facing;
    int setFacing;

    public RenderGlobal.ContainerLocalRenderInformation(RenderChunk renderChunkIn, EnumFacing facingIn, int setFacingIn) {
        this.renderChunk = renderChunkIn;
        this.facing = facingIn;
        this.setFacing = setFacingIn;
    }

    public void setDirection(byte p_189561_1_, EnumFacing p_189561_2_) {
        this.setFacing = this.setFacing | p_189561_1_ | 1 << p_189561_2_.ordinal();
    }

    public boolean hasDirection(EnumFacing p_189560_1_) {
        return (this.setFacing & 1 << p_189560_1_.ordinal()) > 0;
    }

    private void initialize(EnumFacing facingIn, int setFacingIn) {
        this.facing = facingIn;
        this.setFacing = setFacingIn;
    }

    static /* synthetic */ void access$000(RenderGlobal.ContainerLocalRenderInformation x0, EnumFacing x1, int x2) {
        x0.initialize(x1, x2);
    }
}
