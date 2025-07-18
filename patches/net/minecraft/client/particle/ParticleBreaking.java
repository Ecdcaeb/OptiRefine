package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class ParticleBreaking extends Particle {
   protected ParticleBreaking(World var1, double var2, double var4, double var6, Item var8) {
      this(☃, ☃, ☃, ☃, ☃, 0);
   }

   protected ParticleBreaking(World var1, double var2, double var4, double var6, double var8, double var10, double var12, Item var14, int var15) {
      this(☃, ☃, ☃, ☃, ☃, ☃);
      this.motionX *= 0.1F;
      this.motionY *= 0.1F;
      this.motionZ *= 0.1F;
      this.motionX += ☃;
      this.motionY += ☃;
      this.motionZ += ☃;
   }

   protected ParticleBreaking(World var1, double var2, double var4, double var6, Item var8, int var9) {
      super(☃, ☃, ☃, ☃, 0.0, 0.0, 0.0);
      this.setParticleTexture(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(☃, ☃));
      this.particleRed = 1.0F;
      this.particleGreen = 1.0F;
      this.particleBlue = 1.0F;
      this.particleGravity = Blocks.SNOW.blockParticleGravity;
      this.particleScale /= 2.0F;
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

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         int ☃ = ☃.length > 1 ? ☃[1] : 0;
         return new ParticleBreaking(☃, ☃, ☃, ☃, ☃, ☃, ☃, Item.getItemById(☃[0]), ☃);
      }
   }

   public static class SlimeFactory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleBreaking(☃, ☃, ☃, ☃, Items.SLIME_BALL);
      }
   }

   public static class SnowballFactory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleBreaking(☃, ☃, ☃, ☃, Items.SNOWBALL);
      }
   }
}
