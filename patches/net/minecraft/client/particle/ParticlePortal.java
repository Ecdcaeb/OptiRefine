package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ParticlePortal extends Particle {
   private final float portalParticleScale;
   private final double portalPosX;
   private final double portalPosY;
   private final double portalPosZ;

   protected ParticlePortal(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
      this.posX = ☃;
      this.posY = ☃;
      this.posZ = ☃;
      this.portalPosX = this.posX;
      this.portalPosY = this.posY;
      this.portalPosZ = this.posZ;
      float ☃ = this.rand.nextFloat() * 0.6F + 0.4F;
      this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
      this.portalParticleScale = this.particleScale;
      this.particleRed = ☃ * 0.9F;
      this.particleGreen = ☃ * 0.3F;
      this.particleBlue = ☃;
      this.particleMaxAge = (int)(Math.random() * 10.0) + 40;
      this.setParticleTextureIndex((int)(Math.random() * 8.0));
   }

   @Override
   public void move(double var1, double var3, double var5) {
      this.setBoundingBox(this.getBoundingBox().offset(☃, ☃, ☃));
      this.resetPositionToBB();
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float ☃ = (this.particleAge + ☃) / this.particleMaxAge;
      ☃ = 1.0F - ☃;
      ☃ *= ☃;
      ☃ = 1.0F - ☃;
      this.particleScale = this.portalParticleScale * ☃;
      super.renderParticle(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public int getBrightnessForRender(float var1) {
      int ☃ = super.getBrightnessForRender(☃);
      float ☃x = (float)this.particleAge / this.particleMaxAge;
      ☃x *= ☃x;
      ☃x *= ☃x;
      int ☃xx = ☃ & 0xFF;
      int ☃xxx = ☃ >> 16 & 0xFF;
      ☃xxx += (int)(☃x * 15.0F * 16.0F);
      if (☃xxx > 240) {
         ☃xxx = 240;
      }

      return ☃xx | ☃xxx << 16;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      float ☃ = (float)this.particleAge / this.particleMaxAge;
      float var3 = -☃ + ☃ * ☃ * 2.0F;
      float var4 = 1.0F - var3;
      this.posX = this.portalPosX + this.motionX * var4;
      this.posY = this.portalPosY + this.motionY * var4 + (1.0F - ☃);
      this.posZ = this.portalPosZ + this.motionZ * var4;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setExpired();
      }
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticlePortal(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
