package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseDying extends PhaseBase {
   private Vec3d targetLocation;
   private int time;

   public PhaseDying(EntityDragon var1) {
      super(☃);
   }

   @Override
   public void doClientRenderEffects() {
      if (this.time++ % 10 == 0) {
         float ☃ = (this.dragon.getRNG().nextFloat() - 0.5F) * 8.0F;
         float ☃x = (this.dragon.getRNG().nextFloat() - 0.5F) * 4.0F;
         float ☃xx = (this.dragon.getRNG().nextFloat() - 0.5F) * 8.0F;
         this.dragon
            .world
            .spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.dragon.posX + ☃, this.dragon.posY + 2.0 + ☃x, this.dragon.posZ + ☃xx, 0.0, 0.0, 0.0);
      }
   }

   @Override
   public void doLocalUpdate() {
      this.time++;
      if (this.targetLocation == null) {
         BlockPos ☃ = this.dragon.world.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION);
         this.targetLocation = new Vec3d(☃.getX(), ☃.getY(), ☃.getZ());
      }

      double ☃ = this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
      if (!(☃ < 100.0) && !(☃ > 22500.0) && !this.dragon.collidedHorizontally && !this.dragon.collidedVertically) {
         this.dragon.setHealth(1.0F);
      } else {
         this.dragon.setHealth(0.0F);
      }
   }

   @Override
   public void initPhase() {
      this.targetLocation = null;
      this.time = 0;
   }

   @Override
   public float getMaxRiseOrFall() {
      return 3.0F;
   }

   @Nullable
   @Override
   public Vec3d getTargetLocation() {
      return this.targetLocation;
   }

   @Override
   public PhaseList<PhaseDying> getType() {
      return PhaseList.DYING;
   }
}
