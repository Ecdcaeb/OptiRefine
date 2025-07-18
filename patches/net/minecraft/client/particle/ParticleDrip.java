package net.minecraft.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleDrip extends Particle {
   private final Material materialType;
   private int bobTimer;

   protected ParticleDrip(World var1, double var2, double var4, double var6, Material var8) {
      super(☃, ☃, ☃, ☃, 0.0, 0.0, 0.0);
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      if (☃ == Material.WATER) {
         this.particleRed = 0.0F;
         this.particleGreen = 0.0F;
         this.particleBlue = 1.0F;
      } else {
         this.particleRed = 1.0F;
         this.particleGreen = 0.0F;
         this.particleBlue = 0.0F;
      }

      this.setParticleTextureIndex(113);
      this.setSize(0.01F, 0.01F);
      this.particleGravity = 0.06F;
      this.materialType = ☃;
      this.bobTimer = 40;
      this.particleMaxAge = (int)(64.0 / (Math.random() * 0.8 + 0.2));
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
   }

   @Override
   public int getBrightnessForRender(float var1) {
      return this.materialType == Material.WATER ? super.getBrightnessForRender(☃) : 257;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.materialType == Material.WATER) {
         this.particleRed = 0.2F;
         this.particleGreen = 0.3F;
         this.particleBlue = 1.0F;
      } else {
         this.particleRed = 1.0F;
         this.particleGreen = 16.0F / (40 - this.bobTimer + 16);
         this.particleBlue = 4.0F / (40 - this.bobTimer + 8);
      }

      this.motionY = this.motionY - this.particleGravity;
      if (this.bobTimer-- > 0) {
         this.motionX *= 0.02;
         this.motionY *= 0.02;
         this.motionZ *= 0.02;
         this.setParticleTextureIndex(113);
      } else {
         this.setParticleTextureIndex(112);
      }

      this.move(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.98F;
      this.motionY *= 0.98F;
      this.motionZ *= 0.98F;
      if (this.particleMaxAge-- <= 0) {
         this.setExpired();
      }

      if (this.onGround) {
         if (this.materialType == Material.WATER) {
            this.setExpired();
            this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0);
         } else {
            this.setParticleTextureIndex(114);
         }

         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }

      BlockPos ☃ = new BlockPos(this.posX, this.posY, this.posZ);
      IBlockState ☃x = this.world.getBlockState(☃);
      Material ☃xx = ☃x.getMaterial();
      if (☃xx.isLiquid() || ☃xx.isSolid()) {
         double ☃xxx = 0.0;
         if (☃x.getBlock() instanceof BlockLiquid) {
            ☃xxx = BlockLiquid.getLiquidHeightPercent(☃x.getValue(BlockLiquid.LEVEL));
         }

         double ☃xxxx = MathHelper.floor(this.posY) + 1 - ☃xxx;
         if (this.posY < ☃xxxx) {
            this.setExpired();
         }
      }
   }

   public static class LavaFactory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleDrip(☃, ☃, ☃, ☃, Material.LAVA);
      }
   }

   public static class WaterFactory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleDrip(☃, ☃, ☃, ☃, Material.WATER);
      }
   }
}
