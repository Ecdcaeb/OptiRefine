package net.minecraft.client.renderer;

import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;

public class RenderList extends ChunkRenderContainer {
   @Override
   public void renderChunkLayer(BlockRenderLayer var1) {
      if (this.initialized) {
         for (RenderChunk ☃ : this.renderChunks) {
            ListedRenderChunk ☃x = (ListedRenderChunk)☃;
            GlStateManager.pushMatrix();
            this.preRenderChunk(☃);
            GlStateManager.callList(☃x.getDisplayList(☃, ☃x.getCompiledChunk()));
            GlStateManager.popMatrix();
         }

         GlStateManager.resetColor();
         this.renderChunks.clear();
      }
   }
}
