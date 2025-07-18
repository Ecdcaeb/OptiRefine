package net.minecraft.client.renderer;

import java.nio.Buffer;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;

public class RenderList extends ChunkRenderContainer {
   private double viewEntityX;
   private double viewEntityY;
   private double viewEntityZ;
   IntBuffer bufferLists = GLAllocation.createDirectIntBuffer(16);

   @Override
   public void renderChunkLayer(BlockRenderLayer layer) {
      if (this.initialized) {
         if (!Config.isRenderRegions()) {
            for (RenderChunk renderchunk : this.renderChunks) {
               ListedRenderChunk listedrenderchunk = (ListedRenderChunk)renderchunk;
               GlStateManager.pushMatrix();
               this.preRenderChunk(renderchunk);
               GlStateManager.callList(listedrenderchunk.getDisplayList(layer, listedrenderchunk.h()));
               GlStateManager.popMatrix();
            }
         } else {
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
                  IntBuffer bufferListsNew = GLAllocation.createDirectIntBuffer(this.bufferLists.capacity() * 2);
                  ((Buffer)this.bufferLists).flip();
                  bufferListsNew.put(this.bufferLists);
                  this.bufferLists = bufferListsNew;
               }

               this.bufferLists.put(listedrenderchunk.getDisplayList(layer, listedrenderchunk.h()));
            }

            if (this.bufferLists.position() > 0) {
               this.drawRegion(regionX, regionZ, this.bufferLists);
            }
         }

         if (Config.isMultiTexture()) {
            GlStateManager.bindCurrentTexture();
         }

         GlStateManager.resetColor();
         this.renderChunks.clear();
      }
   }

   @Override
   public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
      this.viewEntityX = viewEntityXIn;
      this.viewEntityY = viewEntityYIn;
      this.viewEntityZ = viewEntityZIn;
      super.initialize(viewEntityXIn, viewEntityYIn, viewEntityZIn);
   }

   private void drawRegion(int regionX, int regionZ, IntBuffer buffer) {
      GlStateManager.pushMatrix();
      this.preRenderRegion(regionX, 0, regionZ);
      ((Buffer)buffer).flip();
      GlStateManager.callLists(buffer);
      ((Buffer)buffer).clear();
      GlStateManager.popMatrix();
   }

   public void preRenderRegion(int x, int y, int z) {
      GlStateManager.translate((float)(x - this.viewEntityX), (float)(y - this.viewEntityY), (float)(z - this.viewEntityZ));
   }
}
