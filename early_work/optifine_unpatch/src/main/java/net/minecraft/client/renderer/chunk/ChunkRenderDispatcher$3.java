/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.Runnable
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.chunk.CompiledChunk
 *  net.minecraft.client.renderer.chunk.RenderChunk
 *  net.minecraft.util.BlockRenderLayer
 */
package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;

class ChunkRenderDispatcher.3
implements Runnable {
    final /* synthetic */ BlockRenderLayer val$p_188245_1_;
    final /* synthetic */ BufferBuilder val$p_188245_2_;
    final /* synthetic */ RenderChunk val$p_188245_3_;
    final /* synthetic */ CompiledChunk val$p_188245_4_;
    final /* synthetic */ double val$p_188245_5_;

    ChunkRenderDispatcher.3() {
        this.val$p_188245_1_ = blockRenderLayer;
        this.val$p_188245_2_ = bufferBuilder;
        this.val$p_188245_3_ = renderChunk;
        this.val$p_188245_4_ = compiledChunk;
        this.val$p_188245_5_ = d;
    }

    public void run() {
        ChunkRenderDispatcher.this.uploadChunk(this.val$p_188245_1_, this.val$p_188245_2_, this.val$p_188245_3_, this.val$p_188245_4_, this.val$p_188245_5_);
    }
}
