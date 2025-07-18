package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseTakeoff extends PhaseBase {
   private boolean firstTick;
   private Path currentPath;
   private Vec3d targetLocation;

   public PhaseTakeoff(EntityDragon var1) {
      super(☃);
   }

   @Override
   public void doLocalUpdate() {
      if (!this.firstTick && this.currentPath != null) {
         BlockPos ☃ = this.dragon.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
         double ☃x = this.dragon.getDistanceSqToCenter(☃);
         if (☃x > 100.0) {
            this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
         }
      } else {
         this.firstTick = false;
         this.findNewTarget();
      }
   }

   @Override
   public void initPhase() {
      this.firstTick = true;
      this.currentPath = null;
      this.targetLocation = null;
   }

   private void findNewTarget() {
      int ☃ = this.dragon.initPathPoints();
      Vec3d ☃x = this.dragon.getHeadLookVec(1.0F);
      int ☃xx = this.dragon.getNearestPpIdx(-☃x.x * 40.0, 105.0, -☃x.z * 40.0);
      if (this.dragon.getFightManager() != null && this.dragon.getFightManager().getNumAliveCrystals() > 0) {
         ☃xx %= 12;
         if (☃xx < 0) {
            ☃xx += 12;
         }
      } else {
         ☃xx -= 12;
         ☃xx &= 7;
         ☃xx += 12;
      }

      this.currentPath = this.dragon.findPath(☃, ☃xx, null);
      if (this.currentPath != null) {
         this.currentPath.incrementPathIndex();
         this.navigateToNextPathNode();
      }
   }

   private void navigateToNextPathNode() {
      Vec3d ☃ = this.currentPath.getCurrentPos();
      this.currentPath.incrementPathIndex();

      double ☃x;
      do {
         ☃x = ☃.y + this.dragon.getRNG().nextFloat() * 20.0F;
      } while (☃x < ☃.y);

      this.targetLocation = new Vec3d(☃.x, ☃x, ☃.z);
   }

   @Nullable
   @Override
   public Vec3d getTargetLocation() {
      return this.targetLocation;
   }

   @Override
   public PhaseList<PhaseTakeoff> getType() {
      return PhaseList.TAKEOFF;
   }
}
