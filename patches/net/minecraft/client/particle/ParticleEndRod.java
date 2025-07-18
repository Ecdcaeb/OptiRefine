package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleEndRod extends ParticleSimpleAnimated {
   public ParticleEndRod(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, 176, 8, -5.0E-4F);
      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
      this.particleScale *= 0.75F;
      this.particleMaxAge = 60 + this.rand.nextInt(12);
      this.setColorFade(15916745);
   }

   @Override
   public void move(double var1, double var3, double var5) {
      this.setBoundingBox(this.getBoundingBox().offset(☃, ☃, ☃));
      this.resetPositionToBB();
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleEndRod(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
