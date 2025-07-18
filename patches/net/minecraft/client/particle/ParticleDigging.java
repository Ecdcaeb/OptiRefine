package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleDigging extends Particle {
   private final IBlockState sourceState;
   private BlockPos sourcePos;

   protected ParticleDigging(World var1, double var2, double var4, double var6, double var8, double var10, double var12, IBlockState var14) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.sourceState = ☃;
      this.setParticleTexture(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(☃));
      this.particleGravity = ☃.getBlock().blockParticleGravity;
      this.particleRed = 0.6F;
      this.particleGreen = 0.6F;
      this.particleBlue = 0.6F;
      this.particleScale /= 2.0F;
   }

   public ParticleDigging setBlockPos(BlockPos var1) {
      this.sourcePos = ☃;
      if (this.sourceState.getBlock() == Blocks.GRASS) {
         return this;
      } else {
         this.multiplyColor(☃);
         return this;
      }
   }

   public ParticleDigging init() {
      this.sourcePos = new BlockPos(this.posX, this.posY, this.posZ);
      Block ☃ = this.sourceState.getBlock();
      if (☃ == Blocks.GRASS) {
         return this;
      } else {
         this.multiplyColor(this.sourcePos);
         return this;
      }
   }

   protected void multiplyColor(@Nullable BlockPos var1) {
      int ☃ = Minecraft.getMinecraft().getBlockColors().colorMultiplier(this.sourceState, this.world, ☃, 0);
      this.particleRed *= (☃ >> 16 & 0xFF) / 255.0F;
      this.particleGreen *= (☃ >> 8 & 0xFF) / 255.0F;
      this.particleBlue *= (☃ & 0xFF) / 255.0F;
   }

   @Override
   public int getFXLayer() {
      return 1;
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float ☃ = (this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
      float ☃x = ☃ + 0.015609375F;
      float ☃xx = (this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
      float ☃xxx = ☃xx + 0.015609375F;
      float ☃xxxx = 0.1F * this.particleScale;
      if (this.particleTexture != null) {
         ☃ = this.particleTexture.getInterpolatedU(this.particleTextureJitterX / 4.0F * 16.0F);
         ☃x = this.particleTexture.getInterpolatedU((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F);
         ☃xx = this.particleTexture.getInterpolatedV(this.particleTextureJitterY / 4.0F * 16.0F);
         ☃xxx = this.particleTexture.getInterpolatedV((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F);
      }

      float ☃xxxxx = (float)(this.prevPosX + (this.posX - this.prevPosX) * ☃ - interpPosX);
      float ☃xxxxxx = (float)(this.prevPosY + (this.posY - this.prevPosY) * ☃ - interpPosY);
      float ☃xxxxxxx = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * ☃ - interpPosZ);
      int ☃xxxxxxxx = this.getBrightnessForRender(☃);
      int ☃xxxxxxxxx = ☃xxxxxxxx >> 16 & 65535;
      int ☃xxxxxxxxxx = ☃xxxxxxxx & 65535;
      ☃.pos(☃xxxxx - ☃ * ☃xxxx - ☃ * ☃xxxx, ☃xxxxxx - ☃ * ☃xxxx, ☃xxxxxxx - ☃ * ☃xxxx - ☃ * ☃xxxx)
         .tex(☃, ☃xxx)
         .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
         .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
         .endVertex();
      ☃.pos(☃xxxxx - ☃ * ☃xxxx + ☃ * ☃xxxx, ☃xxxxxx + ☃ * ☃xxxx, ☃xxxxxxx - ☃ * ☃xxxx + ☃ * ☃xxxx)
         .tex(☃, ☃xx)
         .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
         .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
         .endVertex();
      ☃.pos(☃xxxxx + ☃ * ☃xxxx + ☃ * ☃xxxx, ☃xxxxxx + ☃ * ☃xxxx, ☃xxxxxxx + ☃ * ☃xxxx + ☃ * ☃xxxx)
         .tex(☃x, ☃xx)
         .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
         .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
         .endVertex();
      ☃.pos(☃xxxxx + ☃ * ☃xxxx - ☃ * ☃xxxx, ☃xxxxxx - ☃ * ☃xxxx, ☃xxxxxxx + ☃ * ☃xxxx - ☃ * ☃xxxx)
         .tex(☃x, ☃xxx)
         .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
         .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
         .endVertex();
   }

   @Override
   public int getBrightnessForRender(float var1) {
      int ☃ = super.getBrightnessForRender(☃);
      int ☃x = 0;
      if (this.world.isBlockLoaded(this.sourcePos)) {
         ☃x = this.world.getCombinedLight(this.sourcePos, 0);
      }

      return ☃ == 0 ? ☃x : ☃;
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleDigging(☃, ☃, ☃, ☃, ☃, ☃, ☃, Block.getStateById(☃[0])).init();
      }
   }
}
