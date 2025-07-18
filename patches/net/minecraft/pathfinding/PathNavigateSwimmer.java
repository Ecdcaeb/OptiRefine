package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateSwimmer extends PathNavigate {
   public PathNavigateSwimmer(EntityLiving var1, World var2) {
      super(☃, ☃);
   }

   @Override
   protected PathFinder getPathFinder() {
      return new PathFinder(new SwimNodeProcessor());
   }

   @Override
   protected boolean canNavigate() {
      return this.isInLiquid();
   }

   @Override
   protected Vec3d getEntityPosition() {
      return new Vec3d(this.entity.posX, this.entity.posY + this.entity.height * 0.5, this.entity.posZ);
   }

   @Override
   protected void pathFollow() {
      Vec3d ☃ = this.getEntityPosition();
      float ☃x = this.entity.width * this.entity.width;
      int ☃xx = 6;
      if (☃.squareDistanceTo(this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex())) < ☃x) {
         this.currentPath.incrementPathIndex();
      }

      for (int ☃xxx = Math.min(this.currentPath.getCurrentPathIndex() + 6, this.currentPath.getCurrentPathLength() - 1);
         ☃xxx > this.currentPath.getCurrentPathIndex();
         ☃xxx--
      ) {
         Vec3d ☃xxxx = this.currentPath.getVectorFromIndex(this.entity, ☃xxx);
         if (!(☃xxxx.squareDistanceTo(☃) > 36.0) && this.isDirectPathBetweenPoints(☃, ☃xxxx, 0, 0, 0)) {
            this.currentPath.setCurrentPathIndex(☃xxx);
            break;
         }
      }

      this.checkForStuck(☃);
   }

   @Override
   protected boolean isDirectPathBetweenPoints(Vec3d var1, Vec3d var2, int var3, int var4, int var5) {
      RayTraceResult ☃ = this.world.rayTraceBlocks(☃, new Vec3d(☃.x, ☃.y + this.entity.height * 0.5, ☃.z), false, true, false);
      return ☃ == null || ☃.typeOfHit == RayTraceResult.Type.MISS;
   }

   @Override
   public boolean canEntityStandOnPos(BlockPos var1) {
      return !this.world.getBlockState(☃).isFullBlock();
   }
}
