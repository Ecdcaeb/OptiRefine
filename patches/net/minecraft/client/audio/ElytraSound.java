package net.minecraft.client.audio;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class ElytraSound extends MovingSound {
   private final EntityPlayerSP player;
   private int time;

   public ElytraSound(EntityPlayerSP var1) {
      super(SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS);
      this.player = ☃;
      this.repeat = true;
      this.repeatDelay = 0;
      this.volume = 0.1F;
   }

   @Override
   public void update() {
      this.time++;
      if (!this.player.isDead && (this.time <= 20 || this.player.isElytraFlying())) {
         this.xPosF = (float)this.player.posX;
         this.yPosF = (float)this.player.posY;
         this.zPosF = (float)this.player.posZ;
         float ☃ = MathHelper.sqrt(
            this.player.motionX * this.player.motionX + this.player.motionZ * this.player.motionZ + this.player.motionY * this.player.motionY
         );
         float ☃x = ☃ / 2.0F;
         if (☃ >= 0.01) {
            this.volume = MathHelper.clamp(☃x * ☃x, 0.0F, 1.0F);
         } else {
            this.volume = 0.0F;
         }

         if (this.time < 20) {
            this.volume = 0.0F;
         } else if (this.time < 40) {
            this.volume = (float)(this.volume * ((this.time - 20) / 20.0));
         }

         float ☃xx = 0.8F;
         if (this.volume > 0.8F) {
            this.pitch = 1.0F + (this.volume - 0.8F);
         } else {
            this.pitch = 1.0F;
         }
      } else {
         this.donePlaying = true;
      }
   }
}
