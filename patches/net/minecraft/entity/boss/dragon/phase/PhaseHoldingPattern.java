package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseHoldingPattern extends PhaseBase {
   private Path currentPath;
   private Vec3d targetLocation;
   private boolean clockwise;

   public PhaseHoldingPattern(EntityDragon var1) {
      super(☃);
   }

   @Override
   public PhaseList<PhaseHoldingPattern> getType() {
      return PhaseList.HOLDING_PATTERN;
   }

   @Override
   public void doLocalUpdate() {
      double ☃ = this.targetLocation == null ? 0.0 : this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
      if (☃ < 100.0 || ☃ > 22500.0 || this.dragon.collidedHorizontally || this.dragon.collidedVertically) {
         this.findNewTarget();
      }
   }

   @Override
   public void initPhase() {
      this.currentPath = null;
      this.targetLocation = null;
   }

   @Nullable
   @Override
   public Vec3d getTargetLocation() {
      return this.targetLocation;
   }

   private void findNewTarget() {
      if (this.currentPath != null && this.currentPath.isFinished()) {
         BlockPos ☃ = this.dragon.world.getTopSolidOrLiquidBlock(new BlockPos(WorldGenEndPodium.END_PODIUM_LOCATION));
         int ☃x = this.dragon.getFightManager() == null ? 0 : this.dragon.getFightManager().getNumAliveCrystals();
         if (this.dragon.getRNG().nextInt(☃x + 3) == 0) {
            this.dragon.getPhaseManager().setPhase(PhaseList.LANDING_APPROACH);
            return;
         }

         double ☃xx = 64.0;
         EntityPlayer ☃xxx = this.dragon.world.getNearestAttackablePlayer(☃, ☃xx, ☃xx);
         if (☃xxx != null) {
            ☃xx = ☃xxx.getDistanceSqToCenter(☃) / 512.0;
         }

         if (☃xxx != null && (this.dragon.getRNG().nextInt(MathHelper.abs((int)☃xx) + 2) == 0 || this.dragon.getRNG().nextInt(☃x + 2) == 0)) {
            this.strafePlayer(☃xxx);
            return;
         }
      }

      if (this.currentPath == null || this.currentPath.isFinished()) {
         int ☃xxxx = this.dragon.initPathPoints();
         int ☃xxxxx = ☃xxxx;
         if (this.dragon.getRNG().nextInt(8) == 0) {
            this.clockwise = !this.clockwise;
            ☃xxxxx = ☃xxxx + 6;
         }

         if (this.clockwise) {
            ☃xxxxx++;
         } else {
            ☃xxxxx--;
         }

         if (this.dragon.getFightManager() != null && this.dragon.getFightManager().getNumAliveCrystals() >= 0) {
            ☃xxxxx %= 12;
            if (☃xxxxx < 0) {
               ☃xxxxx += 12;
            }
         } else {
            ☃xxxxx -= 12;
            ☃xxxxx &= 7;
            ☃xxxxx += 12;
         }

         this.currentPath = this.dragon.findPath(☃xxxx, ☃xxxxx, null);
         if (this.currentPath != null) {
            this.currentPath.incrementPathIndex();
         }
      }

      this.navigateToNextPathNode();
   }

   private void strafePlayer(EntityPlayer var1) {
      this.dragon.getPhaseManager().setPhase(PhaseList.STRAFE_PLAYER);
      this.dragon.getPhaseManager().getPhase(PhaseList.STRAFE_PLAYER).setTarget(☃);
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

   @Override
   public void onCrystalDestroyed(EntityEnderCrystal var1, BlockPos var2, DamageSource var3, @Nullable EntityPlayer var4) {
      if (☃ != null && !☃.capabilities.disableDamage) {
         this.strafePlayer(☃);
      }
   }
}
