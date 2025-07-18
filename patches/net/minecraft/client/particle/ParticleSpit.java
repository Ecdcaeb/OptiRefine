package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleSpit extends ParticleExplosion {
   protected ParticleSpit(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.particleGravity = 0.5F;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      this.motionY = this.motionY - (0.004 + 0.04 * this.particleGravity);
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleSpit(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
