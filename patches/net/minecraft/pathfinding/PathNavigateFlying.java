package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateFlying extends PathNavigate {
   public PathNavigateFlying(EntityLiving var1, World var2) {
      super(☃, ☃);
   }

   @Override
   protected PathFinder getPathFinder() {
      this.nodeProcessor = new FlyingNodeProcessor();
      this.nodeProcessor.setCanEnterDoors(true);
      return new PathFinder(this.nodeProcessor);
   }

   @Override
   protected boolean canNavigate() {
      return this.canFloat() && this.isInLiquid() || !this.entity.isRiding();
   }

   @Override
   protected Vec3d getEntityPosition() {
      return new Vec3d(this.entity.posX, this.entity.posY, this.entity.posZ);
   }

   @Override
   public Path getPathToEntityLiving(Entity var1) {
      return this.getPathToPos(new BlockPos(☃));
   }

   @Override
   public void onUpdateNavigation() {
      this.totalTicks++;
      if (this.tryUpdatePath) {
         this.updatePath();
      }

      if (!this.noPath()) {
         if (this.canNavigate()) {
            this.pathFollow();
         } else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
            Vec3d ☃ = this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex());
            if (MathHelper.floor(this.entity.posX) == MathHelper.floor(☃.x)
               && MathHelper.floor(this.entity.posY) == MathHelper.floor(☃.y)
               && MathHelper.floor(this.entity.posZ) == MathHelper.floor(☃.z)) {
               this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
            }
         }

         this.debugPathFinding();
         if (!this.noPath()) {
            Vec3d ☃ = this.currentPath.getPosition(this.entity);
            this.entity.getMoveHelper().setMoveTo(☃.x, ☃.y, ☃.z, this.speed);
         }
      }
   }

   @Override
   protected boolean isDirectPathBetweenPoints(Vec3d var1, Vec3d var2, int var3, int var4, int var5) {
      int ☃ = MathHelper.floor(☃.x);
      int ☃x = MathHelper.floor(☃.y);
      int ☃xx = MathHelper.floor(☃.z);
      double ☃xxx = ☃.x - ☃.x;
      double ☃xxxx = ☃.y - ☃.y;
      double ☃xxxxx = ☃.z - ☃.z;
      double ☃xxxxxx = ☃xxx * ☃xxx + ☃xxxx * ☃xxxx + ☃xxxxx * ☃xxxxx;
      if (☃xxxxxx < 1.0E-8) {
         return false;
      } else {
         double ☃xxxxxxx = 1.0 / Math.sqrt(☃xxxxxx);
         ☃xxx *= ☃xxxxxxx;
         ☃xxxx *= ☃xxxxxxx;
         ☃xxxxx *= ☃xxxxxxx;
         double ☃xxxxxxxx = 1.0 / Math.abs(☃xxx);
         double ☃xxxxxxxxx = 1.0 / Math.abs(☃xxxx);
         double ☃xxxxxxxxxx = 1.0 / Math.abs(☃xxxxx);
         double ☃xxxxxxxxxxx = ☃ - ☃.x;
         double ☃xxxxxxxxxxxx = ☃x - ☃.y;
         double ☃xxxxxxxxxxxxx = ☃xx - ☃.z;
         if (☃xxx >= 0.0) {
            ☃xxxxxxxxxxx++;
         }

         if (☃xxxx >= 0.0) {
            ☃xxxxxxxxxxxx++;
         }

         if (☃xxxxx >= 0.0) {
            ☃xxxxxxxxxxxxx++;
         }

         ☃xxxxxxxxxxx /= ☃xxx;
         ☃xxxxxxxxxxxx /= ☃xxxx;
         ☃xxxxxxxxxxxxx /= ☃xxxxx;
         int ☃xxxxxxxxxxxxxx = ☃xxx < 0.0 ? -1 : 1;
         int ☃xxxxxxxxxxxxxxx = ☃xxxx < 0.0 ? -1 : 1;
         int ☃xxxxxxxxxxxxxxxx = ☃xxxxx < 0.0 ? -1 : 1;
         int ☃xxxxxxxxxxxxxxxxx = MathHelper.floor(☃.x);
         int ☃xxxxxxxxxxxxxxxxxx = MathHelper.floor(☃.y);
         int ☃xxxxxxxxxxxxxxxxxxx = MathHelper.floor(☃.z);
         int ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx - ☃;
         int ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxx - ☃x;
         int ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx - ☃xx;

         while (☃xxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx > 0 || ☃xxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx > 0 || ☃xxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx > 0) {
            if (☃xxxxxxxxxxx < ☃xxxxxxxxxxxxx && ☃xxxxxxxxxxx <= ☃xxxxxxxxxxxx) {
               ☃xxxxxxxxxxx += ☃xxxxxxxx;
               ☃ += ☃xxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx - ☃;
            } else if (☃xxxxxxxxxxxx < ☃xxxxxxxxxxx && ☃xxxxxxxxxxxx <= ☃xxxxxxxxxxxxx) {
               ☃xxxxxxxxxxxx += ☃xxxxxxxxx;
               ☃x += ☃xxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxx - ☃x;
            } else {
               ☃xxxxxxxxxxxxx += ☃xxxxxxxxxx;
               ☃xx += ☃xxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx - ☃xx;
            }
         }

         return true;
      }
   }

   public void setCanOpenDoors(boolean var1) {
      this.nodeProcessor.setCanOpenDoors(☃);
   }

   public void setCanEnterDoors(boolean var1) {
      this.nodeProcessor.setCanEnterDoors(☃);
   }

   public void setCanFloat(boolean var1) {
      this.nodeProcessor.setCanSwim(☃);
   }

   public boolean canFloat() {
      return this.nodeProcessor.getCanSwim();
   }

   @Override
   public boolean canEntityStandOnPos(BlockPos var1) {
      return this.world.getBlockState(☃).isTopSolid();
   }
}
