package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleFallingDust extends Particle {
   float oSize;
   final float rotSpeed;

   protected ParticleFallingDust(World var1, double var2, double var4, double var6, float var8, float var9, float var10) {
      super(☃, ☃, ☃, ☃, 0.0, 0.0, 0.0);
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      this.particleRed = ☃;
      this.particleGreen = ☃;
      this.particleBlue = ☃;
      float ☃ = 0.9F;
      this.particleScale *= 0.75F;
      this.particleScale *= 0.9F;
      this.oSize = this.particleScale;
      this.particleMaxAge = (int)(32.0 / (Math.random() * 0.8 + 0.2));
      this.particleMaxAge = (int)(this.particleMaxAge * 0.9F);
      this.rotSpeed = ((float)Math.random() - 0.5F) * 0.1F;
      this.particleAngle = (float)Math.random() * (float) (Math.PI * 2);
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float ☃ = (this.particleAge + ☃) / this.particleMaxAge * 32.0F;
      ☃ = MathHelper.clamp(☃, 0.0F, 1.0F);
      this.particleScale = this.oSize * ☃;
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

      this.prevParticleAngle = this.particleAngle;
      this.particleAngle = this.particleAngle + (float) Math.PI * this.rotSpeed * 2.0F;
      if (this.onGround) {
         this.prevParticleAngle = this.particleAngle = 0.0F;
      }

      this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
      this.move(this.motionX, this.motionY, this.motionZ);
      this.motionY -= 0.003F;
      this.motionY = Math.max(this.motionY, -0.14F);
   }

   public static class Factory implements IParticleFactory {
      @Nullable
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         IBlockState ☃ = Block.getStateById(☃[0]);
         if (☃.getBlock() != Blocks.AIR && ☃.getRenderType() == EnumBlockRenderType.INVISIBLE) {
            return null;
         } else {
            int ☃x = Minecraft.getMinecraft().getBlockColors().getColor(☃, ☃, new BlockPos(☃, ☃, ☃));
            if (☃.getBlock() instanceof BlockFalling) {
               ☃x = ((BlockFalling)☃.getBlock()).getDustColor(☃);
            }

            float ☃xx = (☃x >> 16 & 0xFF) / 255.0F;
            float ☃xxx = (☃x >> 8 & 0xFF) / 255.0F;
            float ☃xxxx = (☃x & 0xFF) / 255.0F;
            return new ParticleFallingDust(☃, ☃, ☃, ☃, ☃xx, ☃xxx, ☃xxxx);
         }
      }
   }
}
