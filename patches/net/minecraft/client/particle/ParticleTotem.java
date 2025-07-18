package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleTotem extends ParticleSimpleAnimated {
   public ParticleTotem(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, 176, 8, -0.05F);
      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
      this.particleScale *= 0.75F;
      this.particleMaxAge = 60 + this.rand.nextInt(12);
      if (this.rand.nextInt(4) == 0) {
         this.setRBGColorF(0.6F + this.rand.nextFloat() * 0.2F, 0.6F + this.rand.nextFloat() * 0.3F, this.rand.nextFloat() * 0.2F);
      } else {
         this.setRBGColorF(0.1F + this.rand.nextFloat() * 0.2F, 0.4F + this.rand.nextFloat() * 0.3F, this.rand.nextFloat() * 0.2F);
      }

      this.setBaseAirFriction(0.6F);
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleTotem(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
