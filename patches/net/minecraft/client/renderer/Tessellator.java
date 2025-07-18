package net.minecraft.client.renderer;

import net.optifine.SmartAnimations;

public class Tessellator {
   private final BufferBuilder buffer;
   private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
   private static final Tessellator INSTANCE = new Tessellator(2097152);

   public static Tessellator getInstance() {
      return INSTANCE;
   }

   public Tessellator(int bufferSize) {
      this.buffer = new BufferBuilder(bufferSize);
   }

   public void draw() {
      if (this.buffer.animatedSprites != null) {
         SmartAnimations.spritesRendered(this.buffer.animatedSprites);
      }

      this.buffer.finishDrawing();
      this.vboUploader.draw(this.buffer);
   }

   public BufferBuilder getBuffer() {
      return this.buffer;
   }
}
