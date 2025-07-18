package net.minecraft.client.renderer;

public class Tessellator {
   private final BufferBuilder buffer;
   private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
   private static final Tessellator INSTANCE = new Tessellator(2097152);

   public static Tessellator getInstance() {
      return INSTANCE;
   }

   public Tessellator(int var1) {
      this.buffer = new BufferBuilder(☃);
   }

   public void draw() {
      this.buffer.finishDrawing();
      this.vboUploader.draw(this.buffer);
   }

   public BufferBuilder getBuffer() {
      return this.buffer;
   }
}
