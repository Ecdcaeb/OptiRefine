package net.minecraft.pathfinding;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateGround extends PathNavigate {
   private boolean shouldAvoidSun;

   public PathNavigateGround(EntityLiving var1, World var2) {
      super(☃, ☃);
   }

   @Override
   protected PathFinder getPathFinder() {
      this.nodeProcessor = new WalkNodeProcessor();
      this.nodeProcessor.setCanEnterDoors(true);
      return new PathFinder(this.nodeProcessor);
   }

   @Override
   protected boolean canNavigate() {
      return this.entity.onGround || this.getCanSwim() && this.isInLiquid() || this.entity.isRiding();
   }

   @Override
   protected Vec3d getEntityPosition() {
      return new Vec3d(this.entity.posX, this.getPathablePosY(), this.entity.posZ);
   }

   @Override
   public Path getPathToPos(BlockPos var1) {
      if (this.world.getBlockState(☃).getMaterial() == Material.AIR) {
         BlockPos ☃ = ☃.down();

         while (☃.getY() > 0 && this.world.getBlockState(☃).getMaterial() == Material.AIR) {
            ☃ = ☃.down();
         }

         if (☃.getY() > 0) {
            return super.getPathToPos(☃.up());
         }

         while (☃.getY() < this.world.getHeight() && this.world.getBlockState(☃).getMaterial() == Material.AIR) {
            ☃ = ☃.up();
         }

         ☃ = ☃;
      }

      if (!this.world.getBlockState(☃).getMaterial().isSolid()) {
         return super.getPathToPos(☃);
      } else {
         BlockPos ☃ = ☃.up();

         while (☃.getY() < this.world.getHeight() && this.world.getBlockState(☃).getMaterial().isSolid()) {
            ☃ = ☃.up();
         }

         return super.getPathToPos(☃);
      }
   }

   @Override
   public Path getPathToEntityLiving(Entity var1) {
      return this.getPathToPos(new BlockPos(☃));
   }

   private int getPathablePosY() {
      if (this.entity.isInWater() && this.getCanSwim()) {
         int ☃ = (int)this.entity.getEntityBoundingBox().minY;
         Block ☃x = this.world.getBlockState(new BlockPos(MathHelper.floor(this.entity.posX), ☃, MathHelper.floor(this.entity.posZ))).getBlock();
         int ☃xx = 0;

         while (☃x == Blocks.FLOWING_WATER || ☃x == Blocks.WATER) {
            ☃x = this.world.getBlockState(new BlockPos(MathHelper.floor(this.entity.posX), ++☃, MathHelper.floor(this.entity.posZ))).getBlock();
            if (++☃xx > 16) {
               return (int)this.entity.getEntityBoundingBox().minY;
            }
         }

         return ☃;
      } else {
         return (int)(this.entity.getEntityBoundingBox().minY + 0.5);
      }
   }

   @Override
   protected void removeSunnyPath() {
      super.removeSunnyPath();
      if (this.shouldAvoidSun) {
         if (this.world
            .canSeeSky(
               new BlockPos(MathHelper.floor(this.entity.posX), (int)(this.entity.getEntityBoundingBox().minY + 0.5), MathHelper.floor(this.entity.posZ))
            )) {
            return;
         }

         for (int ☃ = 0; ☃ < this.currentPath.getCurrentPathLength(); ☃++) {
            PathPoint ☃x = this.currentPath.getPathPointFromIndex(☃);
            if (this.world.canSeeSky(new BlockPos(☃x.x, ☃x.y, ☃x.z))) {
               this.currentPath.setCurrentPathLength(☃ - 1);
               return;
            }
         }
      }
   }

   @Override
   protected boolean isDirectPathBetweenPoints(Vec3d var1, Vec3d var2, int var3, int var4, int var5) {
      int ☃ = MathHelper.floor(☃.x);
      int ☃x = MathHelper.floor(☃.z);
      double ☃xx = ☃.x - ☃.x;
      double ☃xxx = ☃.z - ☃.z;
      double ☃xxxx = ☃xx * ☃xx + ☃xxx * ☃xxx;
      if (☃xxxx < 1.0E-8) {
         return false;
      } else {
         double ☃xxxxx = 1.0 / Math.sqrt(☃xxxx);
         ☃xx *= ☃xxxxx;
         ☃xxx *= ☃xxxxx;
         ☃ += 2;
         ☃ += 2;
         if (!this.isSafeToStandAt(☃, (int)☃.y, ☃x, ☃, ☃, ☃, ☃, ☃xx, ☃xxx)) {
            return false;
         } else {
            ☃ -= 2;
            ☃ -= 2;
            double ☃xxxxxx = 1.0 / Math.abs(☃xx);
            double ☃xxxxxxx = 1.0 / Math.abs(☃xxx);
            double ☃xxxxxxxx = ☃ - ☃.x;
            double ☃xxxxxxxxx = ☃x - ☃.z;
            if (☃xx >= 0.0) {
               ☃xxxxxxxx++;
            }

            if (☃xxx >= 0.0) {
               ☃xxxxxxxxx++;
            }

            ☃xxxxxxxx /= ☃xx;
            ☃xxxxxxxxx /= ☃xxx;
            int ☃xxxxxxxxxx = ☃xx < 0.0 ? -1 : 1;
            int ☃xxxxxxxxxxx = ☃xxx < 0.0 ? -1 : 1;
            int ☃xxxxxxxxxxxx = MathHelper.floor(☃.x);
            int ☃xxxxxxxxxxxxx = MathHelper.floor(☃.z);
            int ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxx - ☃;
            int ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx - ☃x;

            while (☃xxxxxxxxxxxxxx * ☃xxxxxxxxxx > 0 || ☃xxxxxxxxxxxxxxx * ☃xxxxxxxxxxx > 0) {
               if (☃xxxxxxxx < ☃xxxxxxxxx) {
                  ☃xxxxxxxx += ☃xxxxxx;
                  ☃ += ☃xxxxxxxxxx;
                  ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxx - ☃;
               } else {
                  ☃xxxxxxxxx += ☃xxxxxxx;
                  ☃x += ☃xxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx - ☃x;
               }

               if (!this.isSafeToStandAt(☃, (int)☃.y, ☃x, ☃, ☃, ☃, ☃, ☃xx, ☃xxx)) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private boolean isSafeToStandAt(int var1, int var2, int var3, int var4, int var5, int var6, Vec3d var7, double var8, double var10) {
      int ☃ = ☃ - ☃ / 2;
      int ☃x = ☃ - ☃ / 2;
      if (!this.isPositionClear(☃, ☃, ☃x, ☃, ☃, ☃, ☃, ☃, ☃)) {
         return false;
      } else {
         for (int ☃xx = ☃; ☃xx < ☃ + ☃; ☃xx++) {
            for (int ☃xxx = ☃x; ☃xxx < ☃x + ☃; ☃xxx++) {
               double ☃xxxx = ☃xx + 0.5 - ☃.x;
               double ☃xxxxx = ☃xxx + 0.5 - ☃.z;
               if (!(☃xxxx * ☃ + ☃xxxxx * ☃ < 0.0)) {
                  PathNodeType ☃xxxxxx = this.nodeProcessor.getPathNodeType(this.world, ☃xx, ☃ - 1, ☃xxx, this.entity, ☃, ☃, ☃, true, true);
                  if (☃xxxxxx == PathNodeType.WATER) {
                     return false;
                  }

                  if (☃xxxxxx == PathNodeType.LAVA) {
                     return false;
                  }

                  if (☃xxxxxx == PathNodeType.OPEN) {
                     return false;
                  }

                  ☃xxxxxx = this.nodeProcessor.getPathNodeType(this.world, ☃xx, ☃, ☃xxx, this.entity, ☃, ☃, ☃, true, true);
                  float ☃xxxxxxx = this.entity.getPathPriority(☃xxxxxx);
                  if (☃xxxxxxx < 0.0F || ☃xxxxxxx >= 8.0F) {
                     return false;
                  }

                  if (☃xxxxxx == PathNodeType.DAMAGE_FIRE || ☃xxxxxx == PathNodeType.DANGER_FIRE || ☃xxxxxx == PathNodeType.DAMAGE_OTHER) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   private boolean isPositionClear(int var1, int var2, int var3, int var4, int var5, int var6, Vec3d var7, double var8, double var10) {
      for (BlockPos ☃ : BlockPos.getAllInBox(new BlockPos(☃, ☃, ☃), new BlockPos(☃ + ☃ - 1, ☃ + ☃ - 1, ☃ + ☃ - 1))) {
         double ☃x = ☃.getX() + 0.5 - ☃.x;
         double ☃xx = ☃.getZ() + 0.5 - ☃.z;
         if (!(☃x * ☃ + ☃xx * ☃ < 0.0)) {
            Block ☃xxx = this.world.getBlockState(☃).getBlock();
            if (!☃xxx.isPassable(this.world, ☃)) {
               return false;
            }
         }
      }

      return true;
   }

   public void setBreakDoors(boolean var1) {
      this.nodeProcessor.setCanOpenDoors(☃);
   }

   public void setEnterDoors(boolean var1) {
      this.nodeProcessor.setCanEnterDoors(☃);
   }

   public boolean getEnterDoors() {
      return this.nodeProcessor.getCanEnterDoors();
   }

   public void setCanSwim(boolean var1) {
      this.nodeProcessor.setCanSwim(☃);
   }

   public boolean getCanSwim() {
      return this.nodeProcessor.getCanSwim();
   }

   public void setAvoidSun(boolean var1) {
      this.shouldAvoidSun = ☃;
   }
}
