package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleSimpleAnimated extends Particle {
   private final int textureIdx;
   private final int numAgingFrames;
   private final float yAccel;
   private float baseAirFriction = 0.91F;
   private float fadeTargetRed;
   private float fadeTargetGreen;
   private float fadeTargetBlue;
   private boolean fadingColor;

   public ParticleSimpleAnimated(World var1, double var2, double var4, double var6, int var8, int var9, float var10) {
      super(☃, ☃, ☃, ☃);
      this.textureIdx = ☃;
      this.numAgingFrames = ☃;
      this.yAccel = ☃;
   }

   public void setColor(int var1) {
      float ☃ = ((☃ & 0xFF0000) >> 16) / 255.0F;
      float ☃x = ((☃ & 0xFF00) >> 8) / 255.0F;
      float ☃xx = ((☃ & 0xFF) >> 0) / 255.0F;
      float ☃xxx = 1.0F;
      this.setRBGColorF(☃ * 1.0F, ☃x * 1.0F, ☃xx * 1.0F);
   }

   public void setColorFade(int var1) {
      this.fadeTargetRed = ((☃ & 0xFF0000) >> 16) / 255.0F;
      this.fadeTargetGreen = ((☃ & 0xFF00) >> 8) / 255.0F;
      this.fadeTargetBlue = ((☃ & 0xFF) >> 0) / 255.0F;
      this.fadingColor = true;
   }

   @Override
   public boolean shouldDisableDepth() {
      return true;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setExpired();
      }

      if (this.particleAge > this.particleMaxAge / 2) {
         this.setAlphaF(1.0F - ((float)this.particleAge - this.particleMaxAge / 2) / this.particleMaxAge);
         if (this.fadingColor) {
            this.particleRed = this.particleRed + (this.fadeTargetRed - this.particleRed) * 0.2F;
            this.particleGreen = this.particleGreen + (this.fadeTargetGreen - this.particleGreen) * 0.2F;
            this.particleBlue = this.particleBlue + (this.fadeTargetBlue - this.particleBlue) * 0.2F;
         }
      }

      this.setParticleTextureIndex(this.textureIdx + (this.numAgingFrames - 1 - this.particleAge * this.numAgingFrames / this.particleMaxAge));
      this.motionY = this.motionY + this.yAccel;
      this.move(this.motionX, this.motionY, this.motionZ);
      this.motionX = this.motionX * this.baseAirFriction;
      this.motionY = this.motionY * this.baseAirFriction;
      this.motionZ = this.motionZ * this.baseAirFriction;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }
   }

   @Override
   public int getBrightnessForRender(float var1) {
      return 15728880;
   }

   protected void setBaseAirFriction(float var1) {
      this.baseAirFriction = ☃;
   }
}
