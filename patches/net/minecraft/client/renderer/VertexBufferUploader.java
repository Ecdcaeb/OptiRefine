package net.minecraft.client.renderer;

import net.minecraft.client.renderer.vertex.VertexBuffer;

public class VertexBufferUploader extends WorldVertexBufferUploader {
   private VertexBuffer vertexBuffer;

   @Override
   public void draw(BufferBuilder var1) {
      ☃.reset();
      this.vertexBuffer.bufferData(☃.getByteBuffer());
   }

   public void setVertexBuffer(VertexBuffer var1) {
      this.vertexBuffer = ☃;
   }
}
