/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.ChunkRenderContainer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.chunk.RenderChunk
 *  net.minecraft.client.renderer.vertex.VertexBuffer
 *  net.minecraft.util.BlockRenderLayer
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.BlockRenderLayer;

public class VboRenderList
extends ChunkRenderContainer {
    public void renderChunkLayer(BlockRenderLayer blockRenderLayer) {
        if (!this.initialized) {
            return;
        }
        for (RenderChunk renderChunk : this.renderChunks) {
            VertexBuffer vertexBuffer = renderChunk.getVertexBufferByLayer(blockRenderLayer.ordinal());
            GlStateManager.pushMatrix();
            this.preRenderChunk(renderChunk);
            renderChunk.multModelviewMatrix();
            vertexBuffer.bindBuffer();
            this.setupArrayPointers();
            vertexBuffer.drawArrays(7);
            GlStateManager.popMatrix();
        }
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_ARRAY_BUFFER, (int)0);
        GlStateManager.resetColor();
        this.renderChunks.clear();
    }

    private void setupArrayPointers() {
        GlStateManager.glVertexPointer((int)3, (int)5126, (int)28, (int)0);
        GlStateManager.glColorPointer((int)4, (int)5121, (int)28, (int)12);
        GlStateManager.glTexCoordPointer((int)2, (int)5126, (int)28, (int)16);
        OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.glTexCoordPointer((int)2, (int)5122, (int)28, (int)24);
        OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.defaultTexUnit);
    }
}
