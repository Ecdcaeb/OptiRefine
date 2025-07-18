package net.minecraft.client.audio;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class MovingSoundMinecartRiding extends MovingSound {
   private final EntityPlayer player;
   private final EntityMinecart minecart;

   public MovingSoundMinecartRiding(EntityPlayer var1, EntityMinecart var2) {
      super(SoundEvents.ENTITY_MINECART_INSIDE, SoundCategory.NEUTRAL);
      this.player = ☃;
      this.minecart = ☃;
      this.attenuationType = ISound.AttenuationType.NONE;
      this.repeat = true;
      this.repeatDelay = 0;
   }

   @Override
   public void update() {
      if (!this.minecart.isDead && this.player.isRiding() && this.player.getRidingEntity() == this.minecart) {
         float ☃ = MathHelper.sqrt(this.minecart.motionX * this.minecart.motionX + this.minecart.motionZ * this.minecart.motionZ);
         if (☃ >= 0.01) {
            this.volume = 0.0F + MathHelper.clamp(☃, 0.0F, 1.0F) * 0.75F;
         } else {
            this.volume = 0.0F;
         }
      } else {
         this.donePlaying = true;
      }
   }
}
