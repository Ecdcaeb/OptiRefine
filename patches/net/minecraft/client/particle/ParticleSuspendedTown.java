package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleSuspendedTown extends Particle {
   protected ParticleSuspendedTown(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      float ☃ = this.rand.nextFloat() * 0.1F + 0.2F;
      this.particleRed = ☃;
      this.particleGreen = ☃;
      this.particleBlue = ☃;
      this.setParticleTextureIndex(0);
      this.setSize(0.02F, 0.02F);
      this.particleScale = this.particleScale * (this.rand.nextFloat() * 0.6F + 0.5F);
      this.motionX *= 0.02F;
      this.motionY *= 0.02F;
      this.motionZ *= 0.02F;
      this.particleMaxAge = (int)(20.0 / (Math.random() * 0.8 + 0.2));
   }

   @Override
   public void move(double var1, double var3, double var5) {
      this.setBoundingBox(this.getBoundingBox().offset(☃, ☃, ☃));
      this.resetPositionToBB();
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.move(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.99;
      this.motionY *= 0.99;
      this.motionZ *= 0.99;
      if (this.particleMaxAge-- <= 0) {
         this.setExpired();
      }
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleSuspendedTown(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   public static class HappyVillagerFactory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         Particle ☃ = new ParticleSuspendedTown(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         ☃.setParticleTextureIndex(82);
         ☃.setRBGColorF(1.0F, 1.0F, 1.0F);
         return ☃;
      }
   }
}
