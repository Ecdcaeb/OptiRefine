package net.minecraft.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleRain extends Particle {
   protected ParticleRain(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃, 0.0, 0.0, 0.0);
      this.motionX *= 0.3F;
      this.motionY = Math.random() * 0.2F + 0.1F;
      this.motionZ *= 0.3F;
      this.particleRed = 1.0F;
      this.particleGreen = 1.0F;
      this.particleBlue = 1.0F;
      this.setParticleTextureIndex(19 + this.rand.nextInt(4));
      this.setSize(0.01F, 0.01F);
      this.particleGravity = 0.06F;
      this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionY = this.motionY - this.particleGravity;
      this.move(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.98F;
      this.motionY *= 0.98F;
      this.motionZ *= 0.98F;
      if (this.particleMaxAge-- <= 0) {
         this.setExpired();
      }

      if (this.onGround) {
         if (Math.random() < 0.5) {
            this.setExpired();
         }

         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }

      BlockPos ☃ = new BlockPos(this.posX, this.posY, this.posZ);
      IBlockState ☃x = this.world.getBlockState(☃);
      Material ☃xx = ☃x.getMaterial();
      if (☃xx.isLiquid() || ☃xx.isSolid()) {
         double ☃xxx;
         if (☃x.getBlock() instanceof BlockLiquid) {
            ☃xxx = 1.0F - BlockLiquid.getLiquidHeightPercent(☃x.getValue(BlockLiquid.LEVEL));
         } else {
            ☃xxx = ☃x.getBoundingBox(this.world, ☃).maxY;
         }

         double ☃xxxx = MathHelper.floor(this.posY) + ☃xxx;
         if (this.posY < ☃xxxx) {
            this.setExpired();
         }
      }
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleRain(☃, ☃, ☃, ☃);
      }
   }
}
