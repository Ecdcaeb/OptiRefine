package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleWaterWake extends Particle {
   protected ParticleWaterWake(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, 0.0, 0.0, 0.0);
      this.motionX *= 0.3F;
      this.motionY = Math.random() * 0.2F + 0.1F;
      this.motionZ *= 0.3F;
      this.particleRed = 1.0F;
      this.particleGreen = 1.0F;
      this.particleBlue = 1.0F;
      this.setParticleTextureIndex(19);
      this.setSize(0.01F, 0.01F);
      this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
      this.particleGravity = 0.0F;
      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionY = this.motionY - this.particleGravity;
      this.move(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.98F;
      this.motionY *= 0.98F;
      this.motionZ *= 0.98F;
      int ☃ = 60 - this.particleMaxAge;
      float ☃x = ☃ * 0.001F;
      this.setSize(☃x, ☃x);
      this.setParticleTextureIndex(19 + ☃ % 4);
      if (this.particleMaxAge-- <= 0) {
         this.setExpired();
      }
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleWaterWake(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
