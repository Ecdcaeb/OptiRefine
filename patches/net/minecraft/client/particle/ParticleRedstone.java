package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleRedstone extends Particle {
   float reddustParticleScale;

   protected ParticleRedstone(World var1, double var2, double var4, double var6, float var8, float var9, float var10) {
      this(☃, ☃, ☃, ☃, 1.0F, ☃, ☃, ☃);
   }

   protected ParticleRedstone(World var1, double var2, double var4, double var6, float var8, float var9, float var10, float var11) {
      super(☃, ☃, ☃, ☃, 0.0, 0.0, 0.0);
      this.motionX *= 0.1F;
      this.motionY *= 0.1F;
      this.motionZ *= 0.1F;
      if (☃ == 0.0F) {
         ☃ = 1.0F;
      }

      float ☃ = (float)Math.random() * 0.4F + 0.6F;
      this.particleRed = ((float)(Math.random() * 0.2F) + 0.8F) * ☃ * ☃;
      this.particleGreen = ((float)(Math.random() * 0.2F) + 0.8F) * ☃ * ☃;
      this.particleBlue = ((float)(Math.random() * 0.2F) + 0.8F) * ☃ * ☃;
      this.particleScale *= 0.75F;
      this.particleScale *= ☃;
      this.reddustParticleScale = this.particleScale;
      this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
      this.particleMaxAge = (int)(this.particleMaxAge * ☃);
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float ☃ = (this.particleAge + ☃) / this.particleMaxAge * 32.0F;
      ☃ = MathHelper.clamp(☃, 0.0F, 1.0F);
      this.particleScale = this.reddustParticleScale * ☃;
      super.renderParticle(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
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
      this.move(this.motionX, this.motionY, this.motionZ);
      if (this.posY == this.prevPosY) {
         this.motionX *= 1.1;
         this.motionZ *= 1.1;
      }

      this.motionX *= 0.96F;
      this.motionY *= 0.96F;
      this.motionZ *= 0.96F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleRedstone(☃, ☃, ☃, ☃, (float)☃, (float)☃, (float)☃);
      }
   }
}
