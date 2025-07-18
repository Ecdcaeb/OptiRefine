package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleExplosion extends Particle {
   protected ParticleExplosion(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.motionX = ☃ + (Math.random() * 2.0 - 1.0) * 0.05F;
      this.motionY = ☃ + (Math.random() * 2.0 - 1.0) * 0.05F;
      this.motionZ = ☃ + (Math.random() * 2.0 - 1.0) * 0.05F;
      float ☃ = this.rand.nextFloat() * 0.3F + 0.7F;
      this.particleRed = ☃;
      this.particleGreen = ☃;
      this.particleBlue = ☃;
      this.particleScale = this.rand.nextFloat() * this.rand.nextFloat() * 6.0F + 1.0F;
      this.particleMaxAge = (int)(16.0 / (this.rand.nextFloat() * 0.8 + 0.2)) + 2;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setExpired();
      }

      this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
      this.motionY += 0.004;
      this.move(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.9F;
      this.motionY *= 0.9F;
      this.motionZ *= 0.9F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleExplosion(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
