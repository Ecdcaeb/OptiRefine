package net.minecraft.client.renderer;

import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.optifine.render.VboRegion;
import net.optifine.shaders.ShadersRender;

public class VboRenderList extends ChunkRenderContainer {
   private double viewEntityX;
   private double viewEntityY;
   private double viewEntityZ;

   @Override
   public void renderChunkLayer(BlockRenderLayer layer) {
      if (this.initialized) {
         if (!Config.isRenderRegions()) {
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
         } else {
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
         }

         OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
         GlStateManager.resetColor();
         this.renderChunks.clear();
      }
   }

   public void setupArrayPointers() {
      if (Config.isShaders()) {
         ShadersRender.setupArrayPointersVbo();
      } else {
         GlStateManager.glVertexPointer(3, 5126, 28, 0);
         GlStateManager.glColorPointer(4, 5121, 28, 12);
         GlStateManager.glTexCoordPointer(2, 5126, 28, 16);
         OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
         GlStateManager.glTexCoordPointer(2, 5122, 28, 24);
         OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
      }
   }

   @Override
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
      GlStateManager.translate((float)(x - this.viewEntityX), (float)(y - this.viewEntityY), (float)(z - this.viewEntityZ));
   }
}
