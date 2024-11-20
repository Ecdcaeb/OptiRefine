/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Integer
 *  java.lang.Object
 *  java.nio.IntBuffer
 *  net.minecraft.client.renderer.ChunkRenderContainer
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.chunk.ListedRenderChunk
 *  net.minecraft.client.renderer.chunk.RenderChunk
 *  net.minecraft.util.BlockRenderLayer
 */
package net.minecraft.client.renderer;

import java.nio.IntBuffer;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;

public class RenderList
extends ChunkRenderContainer {
    private double viewEntityX;
    private double viewEntityY;
    private double viewEntityZ;
    IntBuffer bufferLists = GLAllocation.createDirectIntBuffer((int)16);

    public void renderChunkLayer(BlockRenderLayer layer) {
        if (this.initialized) {
            if (Config.isRenderRegions()) {
                int regionX = Integer.MIN_VALUE;
                int regionZ = Integer.MIN_VALUE;
                for (RenderChunk renderchunk : this.renderChunks) {
                    ListedRenderChunk listedrenderchunk = (ListedRenderChunk)renderchunk;
                    if (regionX != renderchunk.regionX || regionZ != renderchunk.regionZ) {
                        if (this.bufferLists.position() > 0) {
                            this.drawRegion(regionX, regionZ, this.bufferLists);
                        }
                        regionX = renderchunk.regionX;
                        regionZ = renderchunk.regionZ;
                    }
                    if (this.bufferLists.position() >= this.bufferLists.capacity()) {
                        IntBuffer bufferListsNew = GLAllocation.createDirectIntBuffer((int)(this.bufferLists.capacity() * 2));
                        this.bufferLists.flip();
                        bufferListsNew.put(this.bufferLists);
                        this.bufferLists = bufferListsNew;
                    }
                    this.bufferLists.put(listedrenderchunk.getDisplayList(layer, listedrenderchunk.h()));
                }
                if (this.bufferLists.position() > 0) {
                    this.drawRegion(regionX, regionZ, this.bufferLists);
                }
            } else {
                for (RenderChunk renderchunk : this.renderChunks) {
                    ListedRenderChunk listedrenderchunk = (ListedRenderChunk)renderchunk;
                    GlStateManager.pushMatrix();
                    this.preRenderChunk(renderchunk);
                    GlStateManager.callList((int)listedrenderchunk.getDisplayList(layer, listedrenderchunk.h()));
                    GlStateManager.popMatrix();
                }
            }
            if (Config.isMultiTexture()) {
                GlStateManager.bindCurrentTexture();
            }
            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }

    public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
        this.viewEntityX = viewEntityXIn;
        this.viewEntityY = viewEntityYIn;
        this.viewEntityZ = viewEntityZIn;
        super.initialize(viewEntityXIn, viewEntityYIn, viewEntityZIn);
    }

    private void drawRegion(int regionX, int regionZ, IntBuffer buffer) {
        GlStateManager.pushMatrix();
        this.preRenderRegion(regionX, 0, regionZ);
        buffer.flip();
        GlStateManager.callLists((IntBuffer)buffer);
        buffer.clear();
        GlStateManager.popMatrix();
    }

    public void preRenderRegion(int x, int y, int z) {
        GlStateManager.translate((float)((float)((double)x - this.viewEntityX)), (float)((float)((double)y - this.viewEntityY)), (float)((float)((double)z - this.viewEntityZ)));
    }
}
