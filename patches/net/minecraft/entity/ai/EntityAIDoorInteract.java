package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;

public abstract class EntityAIDoorInteract extends EntityAIBase {
   protected EntityLiving entity;
   protected BlockPos doorPosition = BlockPos.ORIGIN;
   protected BlockDoor doorBlock;
   boolean hasStoppedDoorInteraction;
   float entityPositionX;
   float entityPositionZ;

   public EntityAIDoorInteract(EntityLiving var1) {
      this.entity = ☃;
      if (!(☃.getNavigator() instanceof PathNavigateGround)) {
         throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
      }
   }

   @Override
   public boolean shouldExecute() {
      if (!this.entity.collidedHorizontally) {
         return false;
      } else {
         PathNavigateGround ☃ = (PathNavigateGround)this.entity.getNavigator();
         Path ☃x = ☃.getPath();
         if (☃x != null && !☃x.isFinished() && ☃.getEnterDoors()) {
            for (int ☃xx = 0; ☃xx < Math.min(☃x.getCurrentPathIndex() + 2, ☃x.getCurrentPathLength()); ☃xx++) {
               PathPoint ☃xxx = ☃x.getPathPointFromIndex(☃xx);
               this.doorPosition = new BlockPos(☃xxx.x, ☃xxx.y + 1, ☃xxx.z);
               if (!(this.entity.getDistanceSq(this.doorPosition.getX(), this.entity.posY, this.doorPosition.getZ()) > 2.25)) {
                  this.doorBlock = this.getBlockDoor(this.doorPosition);
                  if (this.doorBlock != null) {
                     return true;
                  }
               }
            }

            this.doorPosition = new BlockPos(this.entity).up();
            this.doorBlock = this.getBlockDoor(this.doorPosition);
            return this.doorBlock != null;
         } else {
            return false;
         }
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return !this.hasStoppedDoorInteraction;
   }

   @Override
   public void startExecuting() {
      this.hasStoppedDoorInteraction = false;
      this.entityPositionX = (float)(this.doorPosition.getX() + 0.5F - this.entity.posX);
      this.entityPositionZ = (float)(this.doorPosition.getZ() + 0.5F - this.entity.posZ);
   }

   @Override
   public void updateTask() {
      float ☃ = (float)(this.doorPosition.getX() + 0.5F - this.entity.posX);
      float ☃x = (float)(this.doorPosition.getZ() + 0.5F - this.entity.posZ);
      float ☃xx = this.entityPositionX * ☃ + this.entityPositionZ * ☃x;
      if (☃xx < 0.0F) {
         this.hasStoppedDoorInteraction = true;
      }
   }

   private BlockDoor getBlockDoor(BlockPos var1) {
      IBlockState ☃ = this.entity.world.getBlockState(☃);
      Block ☃x = ☃.getBlock();
      return ☃x instanceof BlockDoor && ☃.getMaterial() == Material.WOOD ? (BlockDoor)☃x : null;
   }
}
