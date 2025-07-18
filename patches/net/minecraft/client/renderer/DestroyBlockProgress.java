package net.minecraft.client.renderer;

import net.minecraft.util.math.BlockPos;

public class DestroyBlockProgress {
   private final int miningPlayerEntId;
   private final BlockPos position;
   private int partialBlockProgress;
   private int createdAtCloudUpdateTick;

   public DestroyBlockProgress(int var1, BlockPos var2) {
      this.miningPlayerEntId = ☃;
      this.position = ☃;
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public void setPartialBlockDamage(int var1) {
      if (☃ > 10) {
         ☃ = 10;
      }

      this.partialBlockProgress = ☃;
   }

   public int getPartialBlockDamage() {
      return this.partialBlockProgress;
   }

   public void setCloudUpdateTick(int var1) {
      this.createdAtCloudUpdateTick = ☃;
   }

   public int getCreationCloudUpdateTick() {
      return this.createdAtCloudUpdateTick;
   }
}
