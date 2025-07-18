package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class EntityAIMoveToBlock extends EntityAIBase {
   private final EntityCreature creature;
   private final double movementSpeed;
   protected int runDelay;
   private int timeoutCounter;
   private int maxStayTicks;
   protected BlockPos destinationBlock = BlockPos.ORIGIN;
   private boolean isAboveDestination;
   private final int searchLength;

   public EntityAIMoveToBlock(EntityCreature var1, double var2, int var4) {
      this.creature = ☃;
      this.movementSpeed = ☃;
      this.searchLength = ☃;
      this.setMutexBits(5);
   }

   @Override
   public boolean shouldExecute() {
      if (this.runDelay > 0) {
         this.runDelay--;
         return false;
      } else {
         this.runDelay = 200 + this.creature.getRNG().nextInt(200);
         return this.searchForDestination();
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 1200 && this.shouldMoveTo(this.creature.world, this.destinationBlock);
   }

   @Override
   public void startExecuting() {
      this.creature
         .getNavigator()
         .tryMoveToXYZ(this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5, this.movementSpeed);
      this.timeoutCounter = 0;
      this.maxStayTicks = this.creature.getRNG().nextInt(this.creature.getRNG().nextInt(1200) + 1200) + 1200;
   }

   @Override
   public void updateTask() {
      if (this.creature.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0) {
         this.isAboveDestination = false;
         this.timeoutCounter++;
         if (this.timeoutCounter % 40 == 0) {
            this.creature
               .getNavigator()
               .tryMoveToXYZ(this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5, this.movementSpeed);
         }
      } else {
         this.isAboveDestination = true;
         this.timeoutCounter--;
      }
   }

   protected boolean getIsAboveDestination() {
      return this.isAboveDestination;
   }

   private boolean searchForDestination() {
      int ☃ = this.searchLength;
      int ☃x = 1;
      BlockPos ☃xx = new BlockPos(this.creature);

      for (int ☃xxx = 0; ☃xxx <= 1; ☃xxx = ☃xxx > 0 ? -☃xxx : 1 - ☃xxx) {
         for (int ☃xxxx = 0; ☃xxxx < ☃; ☃xxxx++) {
            for (int ☃xxxxx = 0; ☃xxxxx <= ☃xxxx; ☃xxxxx = ☃xxxxx > 0 ? -☃xxxxx : 1 - ☃xxxxx) {
               for (int ☃xxxxxx = ☃xxxxx < ☃xxxx && ☃xxxxx > -☃xxxx ? ☃xxxx : 0; ☃xxxxxx <= ☃xxxx; ☃xxxxxx = ☃xxxxxx > 0 ? -☃xxxxxx : 1 - ☃xxxxxx) {
                  BlockPos ☃xxxxxxx = ☃xx.add(☃xxxxx, ☃xxx - 1, ☃xxxxxx);
                  if (this.creature.isWithinHomeDistanceFromPosition(☃xxxxxxx) && this.shouldMoveTo(this.creature.world, ☃xxxxxxx)) {
                     this.destinationBlock = ☃xxxxxxx;
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   protected abstract boolean shouldMoveTo(World var1, BlockPos var2);
}
