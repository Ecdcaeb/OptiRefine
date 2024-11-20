/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.UnsupportedOperationException
 *  net.minecraft.client.renderer.chunk.CompiledChunk
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.EnumFacing
 */
package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;

static final class CompiledChunk.1
extends CompiledChunk {
    CompiledChunk.1() {
    }

    protected void setLayerUsed(BlockRenderLayer blockRenderLayer) {
        throw new UnsupportedOperationException();
    }

    public void setLayerStarted(BlockRenderLayer blockRenderLayer) {
        throw new UnsupportedOperationException();
    }

    public boolean isVisible(EnumFacing enumFacing, EnumFacing enumFacing2) {
        return false;
    }
}
