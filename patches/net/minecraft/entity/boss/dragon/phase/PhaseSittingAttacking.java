package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.SoundEvents;

public class PhaseSittingAttacking extends PhaseSittingBase {
   private int attackingTicks;

   public PhaseSittingAttacking(EntityDragon var1) {
      super(☃);
   }

   @Override
   public void doClientRenderEffects() {
      this.dragon
         .world
         .playSound(
            this.dragon.posX,
            this.dragon.posY,
            this.dragon.posZ,
            SoundEvents.ENTITY_ENDERDRAGON_GROWL,
            this.dragon.getSoundCategory(),
            2.5F,
            0.8F + this.dragon.getRNG().nextFloat() * 0.3F,
            false
         );
   }

   @Override
   public void doLocalUpdate() {
      if (this.attackingTicks++ >= 40) {
         this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_FLAMING);
      }
   }

   @Override
   public void initPhase() {
      this.attackingTicks = 0;
   }

   @Override
   public PhaseList<PhaseSittingAttacking> getType() {
      return PhaseList.SITTING_ATTACKING;
   }
}
