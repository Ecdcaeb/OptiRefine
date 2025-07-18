package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PathNavigateClimber extends PathNavigateGround {
   private BlockPos targetPosition;

   public PathNavigateClimber(EntityLiving var1, World var2) {
      super(☃, ☃);
   }

   @Override
   public Path getPathToPos(BlockPos var1) {
      this.targetPosition = ☃;
      return super.getPathToPos(☃);
   }

   @Override
   public Path getPathToEntityLiving(Entity var1) {
      this.targetPosition = new BlockPos(☃);
      return super.getPathToEntityLiving(☃);
   }

   @Override
   public boolean tryMoveToEntityLiving(Entity var1, double var2) {
      Path ☃ = this.getPathToEntityLiving(☃);
      if (☃ != null) {
         return this.setPath(☃, ☃);
      } else {
         this.targetPosition = new BlockPos(☃);
         this.speed = ☃;
         return true;
      }
   }

   @Override
   public void onUpdateNavigation() {
      if (!this.noPath()) {
         super.onUpdateNavigation();
      } else {
         if (this.targetPosition != null) {
            double ☃ = this.entity.width * this.entity.width;
            if (!(this.entity.getDistanceSqToCenter(this.targetPosition) < ☃)
               && (
                  !(this.entity.posY > this.targetPosition.getY())
                     || !(
                        this.entity
                              .getDistanceSqToCenter(new BlockPos(this.targetPosition.getX(), MathHelper.floor(this.entity.posY), this.targetPosition.getZ()))
                           < ☃
                     )
               )) {
               this.entity.getMoveHelper().setMoveTo(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ(), this.speed);
            } else {
               this.targetPosition = null;
            }
         }
      }
   }
}
