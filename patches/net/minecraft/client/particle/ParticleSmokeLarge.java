package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleSmokeLarge extends ParticleSmokeNormal {
   protected ParticleSmokeLarge(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃, 2.5F);
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleSmokeLarge(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
