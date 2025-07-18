package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ParticleExplosionHuge extends Particle {
   private int timeSinceStart;
   private final int maximumTime = 8;

   protected ParticleExplosionHuge(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, 0.0, 0.0, 0.0);
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
   }

   @Override
   public void onUpdate() {
      for (int ☃ = 0; ☃ < 6; ☃++) {
         double ☃x = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
         double ☃xx = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
         double ☃xxx = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
         this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, ☃x, ☃xx, ☃xxx, (float)this.timeSinceStart / this.maximumTime, 0.0, 0.0);
      }

      this.timeSinceStart++;
      if (this.timeSinceStart == this.maximumTime) {
         this.setExpired();
      }
   }

   @Override
   public int getFXLayer() {
      return 1;
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleExplosionHuge(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
