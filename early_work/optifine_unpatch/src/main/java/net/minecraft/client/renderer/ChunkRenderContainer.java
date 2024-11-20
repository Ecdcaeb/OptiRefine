/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  java.lang.Object
 *  java.util.List
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.chunk.RenderChunk
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.math.BlockPos
 */
package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;

public abstract class ChunkRenderContainer {
    private double viewEntityX;
    private double viewEntityY;
    private double viewEntityZ;
    protected List<RenderChunk> renderChunks = Lists.newArrayListWithCapacity((int)17424);
    protected boolean initialized;

    public void initialize(double d, double d2, double d3) {
        this.initialized = true;
        this.renderChunks.clear();
        this.viewEntityX = d;
        this.viewEntityY = d2;
        this.viewEntityZ = d3;
    }

    public void preRenderChunk(RenderChunk renderChunk) {
        BlockPos blockPos = renderChunk.getPosition();
        GlStateManager.translate((float)((float)((double)blockPos.p() - this.viewEntityX)), (float)((float)((double)blockPos.q() - this.viewEntityY)), (float)((float)((double)blockPos.r() - this.viewEntityZ)));
    }

    public void addRenderChunk(RenderChunk renderChunk, BlockRenderLayer blockRenderLayer) {
        this.renderChunks.add((Object)renderChunk);
    }

    public abstract void renderChunkLayer(BlockRenderLayer var1);
}
