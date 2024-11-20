/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Integer
 *  java.lang.Object
 *  net.minecraft.client.renderer.ChunkRenderContainer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.chunk.RenderChunk
 *  net.minecraft.client.renderer.vertex.VertexBuffer
 *  net.minecraft.util.BlockRenderLayer
 *  net.optifine.render.VboRegion
 *  net.optifine.shaders.ShadersRender
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.optifine.render.VboRegion;
import net.optifine.shaders.ShadersRender;

public class VboRenderList
extends ChunkRenderContainer {
    private double viewEntityX;
    private double viewEntityY;
    private double viewEntityZ;

    public void renderChunkLayer(BlockRenderLayer layer) {
        if (this.initialized) {
            if (Config.isRenderRegions()) {
                int regionX = Integer.MIN_VALUE;
                int regionZ = Integer.MIN_VALUE;
                VboRegion lastVboRegion = null;
                for (RenderChunk renderchunk : this.renderChunks) {
                    VertexBuffer vertexbuffer = renderchunk.getVertexBufferByLayer(layer.ordinal());
                    VboRegion vboRegion = vertexbuffer.getVboRegion();
                    if (vboRegion != lastVboRegion || regionX != renderchunk.regionX || regionZ != renderchunk.regionZ) {
                        if (lastVboRegion != null) {
                            this.drawRegion(regionX, regionZ, lastVboRegion);
                        }
                        regionX = renderchunk.regionX;
                        regionZ = renderchunk.regionZ;
                        lastVboRegion = vboRegion;
                    }
                    vertexbuffer.drawArrays(7);
                }
                if (lastVboRegion != null) {
                    this.drawRegion(regionX, regionZ, lastVboRegion);
                }
            } else {
                for (RenderChunk renderchunk : this.renderChunks) {
                    VertexBuffer vertexbuffer = renderchunk.getVertexBufferByLayer(layer.ordinal());
                    GlStateManager.pushMatrix();
                    this.preRenderChunk(renderchunk);
                    renderchunk.multModelviewMatrix();
                    vertexbuffer.bindBuffer();
                    this.setupArrayPointers();
                    vertexbuffer.drawArrays(7);
                    GlStateManager.popMatrix();
                }
            }
            OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_ARRAY_BUFFER, (int)0);
            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }

    public void setupArrayPointers() {
        if (Config.isShaders()) {
            ShadersRender.setupArrayPointersVbo();
            return;
        }
        GlStateManager.glVertexPointer((int)3, (int)5126, (int)28, (int)0);
        GlStateManager.glColorPointer((int)4, (int)5121, (int)28, (int)12);
        GlStateManager.glTexCoordPointer((int)2, (int)5126, (int)28, (int)16);
        OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.glTexCoordPointer((int)2, (int)5122, (int)28, (int)24);
        OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.defaultTexUnit);
    }

    public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
        this.viewEntityX = viewEntityXIn;
        this.viewEntityY = viewEntityYIn;
        this.viewEntityZ = viewEntityZIn;
        super.initialize(viewEntityXIn, viewEntityYIn, viewEntityZIn);
    }

    private void drawRegion(int regionX, int regionZ, VboRegion vboRegion) {
        GlStateManager.pushMatrix();
        this.preRenderRegion(regionX, 0, regionZ);
        vboRegion.finishDraw(this);
        GlStateManager.popMatrix();
    }

    public void preRenderRegion(int x, int y, int z) {
        GlStateManager.translate((float)((float)((double)x - this.viewEntityX)), (float)((float)((double)y - this.viewEntityY)), (float)((float)((double)z - this.viewEntityZ)));
    }
}
