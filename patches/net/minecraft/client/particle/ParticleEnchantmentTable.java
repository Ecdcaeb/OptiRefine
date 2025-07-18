package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleEnchantmentTable extends Particle {
   private final float oSize;
   private final double coordX;
   private final double coordY;
   private final double coordZ;

   protected ParticleEnchantmentTable(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
      this.coordX = ☃;
      this.coordY = ☃;
      this.coordZ = ☃;
      this.prevPosX = ☃ + ☃;
      this.prevPosY = ☃ + ☃;
      this.prevPosZ = ☃ + ☃;
      this.posX = this.prevPosX;
      this.posY = this.prevPosY;
      this.posZ = this.prevPosZ;
      float ☃ = this.rand.nextFloat() * 0.6F + 0.4F;
      this.particleScale = this.rand.nextFloat() * 0.5F + 0.2F;
      this.oSize = this.particleScale;
      this.particleRed = 0.9F * ☃;
      this.particleGreen = 0.9F * ☃;
      this.particleBlue = ☃;
      this.particleMaxAge = (int)(Math.random() * 10.0) + 30;
      this.setParticleTextureIndex((int)(Math.random() * 26.0 + 1.0 + 224.0));
   }

   @Override
   public void move(double var1, double var3, double var5) {
      this.setBoundingBox(this.getBoundingBox().offset(☃, ☃, ☃));
      this.resetPositionToBB();
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
      ☃ = 1.0F - ☃;
      float ☃x = 1.0F - ☃;
      ☃x *= ☃x;
      ☃x *= ☃x;
      this.posX = this.coordX + this.motionX * ☃;
      this.posY = this.coordY + this.motionY * ☃ - ☃x * 1.2F;
      this.posZ = this.coordZ + this.motionZ * ☃;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setExpired();
      }
   }

   public static class EnchantmentTable implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleEnchantmentTable(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
