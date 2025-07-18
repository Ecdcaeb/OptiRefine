package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseLandingApproach extends PhaseBase {
   private Path currentPath;
   private Vec3d targetLocation;

   public PhaseLandingApproach(EntityDragon var1) {
      super(☃);
   }

   @Override
   public PhaseList<PhaseLandingApproach> getType() {
      return PhaseList.LANDING_APPROACH;
   }

   @Override
   public void initPhase() {
      this.currentPath = null;
      this.targetLocation = null;
   }

   @Override
   public void doLocalUpdate() {
      double ☃ = this.targetLocation == null ? 0.0 : this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
      if (☃ < 100.0 || ☃ > 22500.0 || this.dragon.collidedHorizontally || this.dragon.collidedVertically) {
         this.findNewTarget();
      }
   }

   @Nullable
   @Override
   public Vec3d getTargetLocation() {
      return this.targetLocation;
   }

   private void findNewTarget() {
      if (this.currentPath == null || this.currentPath.isFinished()) {
         int ☃ = this.dragon.initPathPoints();
         BlockPos ☃x = this.dragon.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
         EntityPlayer ☃xx = this.dragon.world.getNearestAttackablePlayer(☃x, 128.0, 128.0);
         int ☃xxx;
         if (☃xx != null) {
            Vec3d ☃xxxx = new Vec3d(☃xx.posX, 0.0, ☃xx.posZ).normalize();
            ☃xxx = this.dragon.getNearestPpIdx(-☃xxxx.x * 40.0, 105.0, -☃xxxx.z * 40.0);
         } else {
            ☃xxx = this.dragon.getNearestPpIdx(40.0, ☃x.getY(), 0.0);
         }

         PathPoint ☃xxxx = new PathPoint(☃x.getX(), ☃x.getY(), ☃x.getZ());
         this.currentPath = this.dragon.findPath(☃, ☃xxx, ☃xxxx);
         if (this.currentPath != null) {
            this.currentPath.incrementPathIndex();
         }
      }

      this.navigateToNextPathNode();
      if (this.currentPath != null && this.currentPath.isFinished()) {
         this.dragon.getPhaseManager().setPhase(PhaseList.LANDING);
      }
   }

   private void navigateToNextPathNode() {
      if (this.currentPath != null && !this.currentPath.isFinished()) {
         Vec3d ☃ = this.currentPath.getCurrentPos();
         this.currentPath.incrementPathIndex();
         double ☃x = ☃.x;
         double ☃xx = ☃.z;

         double ☃xxx;
         do {
            ☃xxx = ☃.y + this.dragon.getRNG().nextFloat() * 20.0F;
         } while (☃xxx < ☃.y);

         this.targetLocation = new Vec3d(☃x, ☃xxx, ☃xx);
      }
   }
}
