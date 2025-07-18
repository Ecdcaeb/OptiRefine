package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ParticleEmitter extends Particle {
   private final Entity attachedEntity;
   private int age;
   private final int lifetime;
   private final EnumParticleTypes particleTypes;

   public ParticleEmitter(World var1, Entity var2, EnumParticleTypes var3) {
      this(☃, ☃, ☃, 3);
   }

   public ParticleEmitter(World var1, Entity var2, EnumParticleTypes var3, int var4) {
      super(☃, ☃.posX, ☃.getEntityBoundingBox().minY + ☃.height / 2.0F, ☃.posZ, ☃.motionX, ☃.motionY, ☃.motionZ);
      this.attachedEntity = ☃;
      this.lifetime = ☃;
      this.particleTypes = ☃;
      this.onUpdate();
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
   }

   @Override
   public void onUpdate() {
      for (int ☃ = 0; ☃ < 16; ☃++) {
         double ☃x = this.rand.nextFloat() * 2.0F - 1.0F;
         double ☃xx = this.rand.nextFloat() * 2.0F - 1.0F;
         double ☃xxx = this.rand.nextFloat() * 2.0F - 1.0F;
         if (!(☃x * ☃x + ☃xx * ☃xx + ☃xxx * ☃xxx > 1.0)) {
            double ☃xxxx = this.attachedEntity.posX + ☃x * this.attachedEntity.width / 4.0;
            double ☃xxxxx = this.attachedEntity.getEntityBoundingBox().minY + this.attachedEntity.height / 2.0F + ☃xx * this.attachedEntity.height / 4.0;
            double ☃xxxxxx = this.attachedEntity.posZ + ☃xxx * this.attachedEntity.width / 4.0;
            this.world.spawnParticle(this.particleTypes, false, ☃xxxx, ☃xxxxx, ☃xxxxxx, ☃x, ☃xx + 0.2, ☃xxx);
         }
      }

      this.age++;
      if (this.age >= this.lifetime) {
         this.setExpired();
      }
   }

   @Override
   public int getFXLayer() {
      return 3;
   }
}
