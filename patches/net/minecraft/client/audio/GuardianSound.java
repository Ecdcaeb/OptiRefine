package net.minecraft.client.audio;

import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;

public class GuardianSound extends MovingSound {
   private final EntityGuardian guardian;

   public GuardianSound(EntityGuardian var1) {
      super(SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.HOSTILE);
      this.guardian = ☃;
      this.attenuationType = ISound.AttenuationType.NONE;
      this.repeat = true;
      this.repeatDelay = 0;
   }

   @Override
   public void update() {
      if (!this.guardian.isDead && this.guardian.hasTargetedEntity()) {
         this.xPosF = (float)this.guardian.posX;
         this.yPosF = (float)this.guardian.posY;
         this.zPosF = (float)this.guardian.posZ;
         float ☃ = this.guardian.getAttackAnimationScale(0.0F);
         this.volume = 0.0F + 1.0F * ☃ * ☃;
         this.pitch = 0.7F + 0.5F * ☃;
      } else {
         this.donePlaying = true;
      }
   }
}
