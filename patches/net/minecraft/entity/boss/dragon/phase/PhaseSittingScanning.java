package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PhaseSittingScanning extends PhaseSittingBase {
   private int scanningTime;

   public PhaseSittingScanning(EntityDragon var1) {
      super(☃);
   }

   @Override
   public void doLocalUpdate() {
      this.scanningTime++;
      EntityLivingBase ☃ = this.dragon.world.getNearestAttackablePlayer(this.dragon, 20.0, 10.0);
      if (☃ != null) {
         if (this.scanningTime > 25) {
            this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_ATTACKING);
         } else {
            Vec3d ☃x = new Vec3d(☃.posX - this.dragon.posX, 0.0, ☃.posZ - this.dragon.posZ).normalize();
            Vec3d ☃xx = new Vec3d(
                  MathHelper.sin(this.dragon.rotationYaw * (float) (Math.PI / 180.0)),
                  0.0,
                  -MathHelper.cos(this.dragon.rotationYaw * (float) (Math.PI / 180.0))
               )
               .normalize();
            float ☃xxx = (float)☃xx.dotProduct(☃x);
            float ☃xxxx = (float)(Math.acos(☃xxx) * 180.0F / (float)Math.PI) + 0.5F;
            if (☃xxxx < 0.0F || ☃xxxx > 10.0F) {
               double ☃xxxxx = ☃.posX - this.dragon.dragonPartHead.posX;
               double ☃xxxxxx = ☃.posZ - this.dragon.dragonPartHead.posZ;
               double ☃xxxxxxx = MathHelper.clamp(
                  MathHelper.wrapDegrees(180.0 - MathHelper.atan2(☃xxxxx, ☃xxxxxx) * 180.0F / (float)Math.PI - this.dragon.rotationYaw), -100.0, 100.0
               );
               this.dragon.randomYawVelocity *= 0.8F;
               float ☃xxxxxxxx = MathHelper.sqrt(☃xxxxx * ☃xxxxx + ☃xxxxxx * ☃xxxxxx) + 1.0F;
               float ☃xxxxxxxxx = ☃xxxxxxxx;
               if (☃xxxxxxxx > 40.0F) {
                  ☃xxxxxxxx = 40.0F;
               }

               this.dragon.randomYawVelocity = (float)(this.dragon.randomYawVelocity + ☃xxxxxxx * (0.7F / ☃xxxxxxxx / ☃xxxxxxxxx));
               this.dragon.rotationYaw = this.dragon.rotationYaw + this.dragon.randomYawVelocity;
            }
         }
      } else if (this.scanningTime >= 100) {
         ☃ = this.dragon.world.getNearestAttackablePlayer(this.dragon, 150.0, 150.0);
         this.dragon.getPhaseManager().setPhase(PhaseList.TAKEOFF);
         if (☃ != null) {
            this.dragon.getPhaseManager().setPhase(PhaseList.CHARGING_PLAYER);
            this.dragon.getPhaseManager().getPhase(PhaseList.CHARGING_PLAYER).setTarget(new Vec3d(☃.posX, ☃.posY, ☃.posZ));
         }
      }
   }

   @Override
   public void initPhase() {
      this.scanningTime = 0;
   }

   @Override
   public PhaseList<PhaseSittingScanning> getType() {
      return PhaseList.SITTING_SCANNING;
   }
}
