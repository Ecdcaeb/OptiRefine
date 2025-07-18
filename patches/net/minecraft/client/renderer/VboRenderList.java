package net.minecraft.client.renderer;

import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.BlockRenderLayer;

public class VboRenderList extends ChunkRenderContainer {
   @Override
   public void renderChunkLayer(BlockRenderLayer var1) {
      if (this.initialized) {
         for (RenderChunk ☃ : this.renderChunks) {
            VertexBuffer ☃x = ☃.getVertexBufferByLayer(☃.ordinal());
            GlStateManager.pushMatrix();
            this.preRenderChunk(☃);
            ☃.multModelviewMatrix();
            ☃x.bindBuffer();
            this.setupArrayPointers();
            ☃x.drawArrays(7);
            GlStateManager.popMatrix();
         }

         OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
         GlStateManager.resetColor();
         this.renderChunks.clear();
      }
   }

   private void setupArrayPointers() {
      GlStateManager.glVertexPointer(3, 5126, 28, 0);
      GlStateManager.glColorPointer(4, 5121, 28, 12);
      GlStateManager.glTexCoordPointer(2, 5126, 28, 16);
      OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.glTexCoordPointer(2, 5122, 28, 24);
      OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
   }
}
