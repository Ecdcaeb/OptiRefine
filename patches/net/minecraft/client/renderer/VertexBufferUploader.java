package net.minecraft.client.renderer;

import net.minecraft.client.renderer.vertex.VertexBuffer;

public class VertexBufferUploader extends WorldVertexBufferUploader {
   private VertexBuffer vertexBuffer;

   @Override
   public void draw(BufferBuilder vertexBufferIn) {
      if (vertexBufferIn.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
         vertexBufferIn.quadsToTriangles();
         this.vertexBuffer.setDrawMode(vertexBufferIn.getDrawMode());
      }

      this.vertexBuffer.bufferData(vertexBufferIn.getByteBuffer());
      vertexBufferIn.reset();
   }

   public void setVertexBuffer(VertexBuffer vertexBufferIn) {
      this.vertexBuffer = vertexBufferIn;
   }
}
