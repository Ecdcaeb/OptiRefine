/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.ChunkRenderContainer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.chunk.ListedRenderChunk
 *  net.minecraft.client.renderer.chunk.RenderChunk
 *  net.minecraft.util.BlockRenderLayer
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;

public class RenderList
extends ChunkRenderContainer {
    public void renderChunkLayer(BlockRenderLayer blockRenderLayer) {
        if (!this.initialized) {
            return;
        }
        for (RenderChunk renderChunk : this.renderChunks) {
            ListedRenderChunk listedRenderChunk = (ListedRenderChunk)renderChunk;
            GlStateManager.pushMatrix();
            this.preRenderChunk(renderChunk);
            GlStateManager.callList((int)listedRenderChunk.getDisplayList(blockRenderLayer, listedRenderChunk.h()));
            GlStateManager.popMatrix();
        }
        GlStateManager.resetColor();
        this.renderChunks.clear();
    }
}
