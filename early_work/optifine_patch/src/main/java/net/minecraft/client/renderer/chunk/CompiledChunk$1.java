/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.UnsupportedOperationException
 *  java.util.BitSet
 *  net.minecraft.client.renderer.chunk.CompiledChunk
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.EnumFacing
 */
package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;

static final class CompiledChunk.1
extends CompiledChunk {
    CompiledChunk.1() {
    }

    protected void setLayerUsed(BlockRenderLayer layer) {
        throw new UnsupportedOperationException();
    }

    public void setLayerStarted(BlockRenderLayer layer) {
        throw new UnsupportedOperationException();
    }

    public boolean isVisible(EnumFacing facing, EnumFacing facing2) {
        return false;
    }

    public void setAnimatedSprites(BlockRenderLayer layer, BitSet animatedSprites) {
        throw new UnsupportedOperationException();
    }
}
