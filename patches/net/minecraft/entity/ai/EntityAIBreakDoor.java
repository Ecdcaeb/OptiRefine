package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.EnumDifficulty;

public class EntityAIBreakDoor extends EntityAIDoorInteract {
   private int breakingTime;
   private int previousBreakProgress = -1;

   public EntityAIBreakDoor(EntityLiving var1) {
      super(☃);
   }

   @Override
   public boolean shouldExecute() {
      if (!super.shouldExecute()) {
         return false;
      } else {
         return !this.entity.world.getGameRules().getBoolean("mobGriefing") ? false : !BlockDoor.isOpen(this.entity.world, this.doorPosition);
      }
   }

   @Override
   public void startExecuting() {
      super.startExecuting();
      this.breakingTime = 0;
   }

   @Override
   public boolean shouldContinueExecuting() {
      double ☃ = this.entity.getDistanceSq(this.doorPosition);
      return this.breakingTime <= 240 && !BlockDoor.isOpen(this.entity.world, this.doorPosition) && ☃ < 4.0;
   }

   @Override
   public void resetTask() {
      super.resetTask();
      this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, -1);
   }

   @Override
   public void updateTask() {
      super.updateTask();
      if (this.entity.getRNG().nextInt(20) == 0) {
         this.entity.world.playEvent(1019, this.doorPosition, 0);
      }

      this.breakingTime++;
      int ☃ = (int)(this.breakingTime / 240.0F * 10.0F);
      if (☃ != this.previousBreakProgress) {
         this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, ☃);
         this.previousBreakProgress = ☃;
      }

      if (this.breakingTime == 240 && this.entity.world.getDifficulty() == EnumDifficulty.HARD) {
         this.entity.world.setBlockToAir(this.doorPosition);
         this.entity.world.playEvent(1021, this.doorPosition, 0);
         this.entity.world.playEvent(2001, this.doorPosition, Block.getIdFromBlock(this.doorBlock));
      }
   }
}
