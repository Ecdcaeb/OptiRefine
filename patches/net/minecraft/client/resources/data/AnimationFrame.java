package net.minecraft.client.resources.data;

public class AnimationFrame {
   private final int frameIndex;
   private final int frameTime;

   public AnimationFrame(int var1) {
      this(☃, -1);
   }

   public AnimationFrame(int var1, int var2) {
      this.frameIndex = ☃;
      this.frameTime = ☃;
   }

   public boolean hasNoTime() {
      return this.frameTime == -1;
   }

   public int getFrameTime() {
      return this.frameTime;
   }

   public int getFrameIndex() {
      return this.frameIndex;
   }
}
