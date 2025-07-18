package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PhaseSittingFlaming extends PhaseSittingBase {
   private int flameTicks;
   private int flameCount;
   private EntityAreaEffectCloud areaEffectCloud;

   public PhaseSittingFlaming(EntityDragon var1) {
      super(☃);
   }

   @Override
   public void doClientRenderEffects() {
      this.flameTicks++;
      if (this.flameTicks % 2 == 0 && this.flameTicks < 10) {
         Vec3d ☃ = this.dragon.getHeadLookVec(1.0F).normalize();
         ☃.rotateYaw((float) (-Math.PI / 4));
         double ☃x = this.dragon.dragonPartHead.posX;
         double ☃xx = this.dragon.dragonPartHead.posY + this.dragon.dragonPartHead.height / 2.0F;
         double ☃xxx = this.dragon.dragonPartHead.posZ;

         for (int ☃xxxx = 0; ☃xxxx < 8; ☃xxxx++) {
            double ☃xxxxx = ☃x + this.dragon.getRNG().nextGaussian() / 2.0;
            double ☃xxxxxx = ☃xx + this.dragon.getRNG().nextGaussian() / 2.0;
            double ☃xxxxxxx = ☃xxx + this.dragon.getRNG().nextGaussian() / 2.0;

            for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < 6; ☃xxxxxxxx++) {
               this.dragon
                  .world
                  .spawnParticle(EnumParticleTypes.DRAGON_BREATH, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx, -☃.x * 0.08F * ☃xxxxxxxx, -☃.y * 0.6F, -☃.z * 0.08F * ☃xxxxxxxx);
            }

            ☃.rotateYaw((float) (Math.PI / 16));
         }
      }
   }

   @Override
   public void doLocalUpdate() {
      this.flameTicks++;
      if (this.flameTicks >= 200) {
         if (this.flameCount >= 4) {
            this.dragon.getPhaseManager().setPhase(PhaseList.TAKEOFF);
         } else {
            this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
         }
      } else if (this.flameTicks == 10) {
         Vec3d ☃ = new Vec3d(this.dragon.dragonPartHead.posX - this.dragon.posX, 0.0, this.dragon.dragonPartHead.posZ - this.dragon.posZ).normalize();
         float ☃x = 5.0F;
         double ☃xx = this.dragon.dragonPartHead.posX + ☃.x * 5.0 / 2.0;
         double ☃xxx = this.dragon.dragonPartHead.posZ + ☃.z * 5.0 / 2.0;
         double ☃xxxx = this.dragon.dragonPartHead.posY + this.dragon.dragonPartHead.height / 2.0F;
         BlockPos.MutableBlockPos ☃xxxxx = new BlockPos.MutableBlockPos(MathHelper.floor(☃xx), MathHelper.floor(☃xxxx), MathHelper.floor(☃xxx));

         while (this.dragon.world.isAirBlock(☃xxxxx)) {
            ☃xxxxx.setPos(MathHelper.floor(☃xx), MathHelper.floor(--☃xxxx), MathHelper.floor(☃xxx));
         }

         ☃xxxx = MathHelper.floor(☃xxxx) + 1;
         this.areaEffectCloud = new EntityAreaEffectCloud(this.dragon.world, ☃xx, ☃xxxx, ☃xxx);
         this.areaEffectCloud.setOwner(this.dragon);
         this.areaEffectCloud.setRadius(5.0F);
         this.areaEffectCloud.setDuration(200);
         this.areaEffectCloud.setParticle(EnumParticleTypes.DRAGON_BREATH);
         this.areaEffectCloud.addEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE));
         this.dragon.world.spawnEntity(this.areaEffectCloud);
      }
   }

   @Override
   public void initPhase() {
      this.flameTicks = 0;
      this.flameCount++;
   }

   @Override
   public void removeAreaEffect() {
      if (this.areaEffectCloud != null) {
         this.areaEffectCloud.setDead();
         this.areaEffectCloud = null;
      }
   }

   @Override
   public PhaseList<PhaseSittingFlaming> getType() {
      return PhaseList.SITTING_FLAMING;
   }

   public void resetFlameCount() {
      this.flameCount = 0;
   }
}
