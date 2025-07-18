package net.minecraft.client.particle;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleSuspend extends Particle {
   protected ParticleSuspend(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃ - 0.125, ☃, ☃, ☃, ☃);
      this.particleRed = 0.4F;
      this.particleGreen = 0.4F;
      this.particleBlue = 0.7F;
      this.setParticleTextureIndex(0);
      this.setSize(0.01F, 0.01F);
      this.particleScale = this.particleScale * (this.rand.nextFloat() * 0.6F + 0.2F);
      this.motionX = ☃ * 0.0;
      this.motionY = ☃ * 0.0;
      this.motionZ = ☃ * 0.0;
      this.particleMaxAge = (int)(16.0 / (Math.random() * 0.8 + 0.2));
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.move(this.motionX, this.motionY, this.motionZ);
      if (this.world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).getMaterial() != Material.WATER) {
         this.setExpired();
      }

      if (this.particleMaxAge-- <= 0) {
         this.setExpired();
      }
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleSuspend(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
