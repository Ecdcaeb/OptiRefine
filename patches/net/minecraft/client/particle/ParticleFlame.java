package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleFlame extends Particle {
   private final float flameScale;

   protected ParticleFlame(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.motionX = this.motionX * 0.01F + ☃;
      this.motionY = this.motionY * 0.01F + ☃;
      this.motionZ = this.motionZ * 0.01F + ☃;
      this.posX = this.posX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
      this.posY = this.posY + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
      this.posZ = this.posZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
      this.flameScale = this.particleScale;
      this.particleRed = 1.0F;
      this.particleGreen = 1.0F;
      this.particleBlue = 1.0F;
      this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2)) + 4;
      this.setParticleTextureIndex(48);
   }

   @Override
   public void move(double var1, double var3, double var5) {
      this.setBoundingBox(this.getBoundingBox().offset(☃, ☃, ☃));
      this.resetPositionToBB();
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float ☃ = (this.particleAge + ☃) / this.particleMaxAge;
      this.particleScale = this.flameScale * (1.0F - ☃ * ☃ * 0.5F);
      super.renderParticle(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public int getBrightnessForRender(float var1) {
      float ☃ = (this.particleAge + ☃) / this.particleMaxAge;
      ☃ = MathHelper.clamp(☃, 0.0F, 1.0F);
      int ☃x = super.getBrightnessForRender(☃);
      int ☃xx = ☃x & 0xFF;
      int ☃xxx = ☃x >> 16 & 0xFF;
      ☃xx += (int)(☃ * 15.0F * 16.0F);
      if (☃xx > 240) {
         ☃xx = 240;
      }

      return ☃xx | ☃xxx << 16;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setExpired();
      }

      this.move(this.motionX, this.motionY, this.motionZ);
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
         return new ParticleFlame(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
