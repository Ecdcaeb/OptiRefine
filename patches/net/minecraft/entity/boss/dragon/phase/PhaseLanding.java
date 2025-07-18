package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseLanding extends PhaseBase {
   private Vec3d targetLocation;

   public PhaseLanding(EntityDragon var1) {
      super(☃);
   }

   @Override
   public void doClientRenderEffects() {
      Vec3d ☃ = this.dragon.getHeadLookVec(1.0F).normalize();
      ☃.rotateYaw((float) (-Math.PI / 4));
      double ☃x = this.dragon.dragonPartHead.posX;
      double ☃xx = this.dragon.dragonPartHead.posY + this.dragon.dragonPartHead.height / 2.0F;
      double ☃xxx = this.dragon.dragonPartHead.posZ;

      for (int ☃xxxx = 0; ☃xxxx < 8; ☃xxxx++) {
         double ☃xxxxx = ☃x + this.dragon.getRNG().nextGaussian() / 2.0;
         double ☃xxxxxx = ☃xx + this.dragon.getRNG().nextGaussian() / 2.0;
         double ☃xxxxxxx = ☃xxx + this.dragon.getRNG().nextGaussian() / 2.0;
         this.dragon
            .world
            .spawnParticle(
               EnumParticleTypes.DRAGON_BREATH,
               ☃xxxxx,
               ☃xxxxxx,
               ☃xxxxxxx,
               -☃.x * 0.08F + this.dragon.motionX,
               -☃.y * 0.3F + this.dragon.motionY,
               -☃.z * 0.08F + this.dragon.motionZ
            );
         ☃.rotateYaw((float) (Math.PI / 16));
      }
   }

   @Override
   public void doLocalUpdate() {
      if (this.targetLocation == null) {
         this.targetLocation = new Vec3d(this.dragon.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION));
      }

      if (this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ) < 1.0) {
         this.dragon.getPhaseManager().getPhase(PhaseList.SITTING_FLAMING).resetFlameCount();
         this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
      }
   }

   @Override
   public float getMaxRiseOrFall() {
      return 1.5F;
   }

   @Override
   public float getYawFactor() {
      float ☃ = MathHelper.sqrt(this.dragon.motionX * this.dragon.motionX + this.dragon.motionZ * this.dragon.motionZ) + 1.0F;
      float ☃x = Math.min(☃, 40.0F);
      return ☃x / ☃;
   }

   @Override
   public void initPhase() {
      this.targetLocation = null;
   }

   @Nullable
   @Override
   public Vec3d getTargetLocation() {
      return this.targetLocation;
   }

   @Override
   public PhaseList<PhaseLanding> getType() {
      return PhaseList.LANDING;
   }
}
