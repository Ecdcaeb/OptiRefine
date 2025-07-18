package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class Barrier extends Particle {
   protected Barrier(World var1, double var2, double var4, double var6, Item var8) {
      super(☃, ☃, ☃, ☃, 0.0, 0.0, 0.0);
      this.setParticleTexture(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(☃));
      this.particleRed = 1.0F;
      this.particleGreen = 1.0F;
      this.particleBlue = 1.0F;
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      this.particleGravity = 0.0F;
      this.particleMaxAge = 80;
   }

   @Override
   public int getFXLayer() {
      return 1;
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float ☃ = this.particleTexture.getMinU();
      float ☃x = this.particleTexture.getMaxU();
      float ☃xx = this.particleTexture.getMinV();
      float ☃xxx = this.particleTexture.getMaxV();
      float ☃xxxx = 0.5F;
      float ☃xxxxx = (float)(this.prevPosX + (this.posX - this.prevPosX) * ☃ - interpPosX);
      float ☃xxxxxx = (float)(this.prevPosY + (this.posY - this.prevPosY) * ☃ - interpPosY);
      float ☃xxxxxxx = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * ☃ - interpPosZ);
      int ☃xxxxxxxx = this.getBrightnessForRender(☃);
      int ☃xxxxxxxxx = ☃xxxxxxxx >> 16 & 65535;
      int ☃xxxxxxxxxx = ☃xxxxxxxx & 65535;
      ☃.pos(☃xxxxx - ☃ * 0.5F - ☃ * 0.5F, ☃xxxxxx - ☃ * 0.5F, ☃xxxxxxx - ☃ * 0.5F - ☃ * 0.5F)
         .tex(☃x, ☃xxx)
         .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
         .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
         .endVertex();
      ☃.pos(☃xxxxx - ☃ * 0.5F + ☃ * 0.5F, ☃xxxxxx + ☃ * 0.5F, ☃xxxxxxx - ☃ * 0.5F + ☃ * 0.5F)
         .tex(☃x, ☃xx)
         .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
         .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
         .endVertex();
      ☃.pos(☃xxxxx + ☃ * 0.5F + ☃ * 0.5F, ☃xxxxxx + ☃ * 0.5F, ☃xxxxxxx + ☃ * 0.5F + ☃ * 0.5F)
         .tex(☃, ☃xx)
         .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
         .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
         .endVertex();
      ☃.pos(☃xxxxx + ☃ * 0.5F - ☃ * 0.5F, ☃xxxxxx - ☃ * 0.5F, ☃xxxxxxx + ☃ * 0.5F - ☃ * 0.5F)
         .tex(☃, ☃xxx)
         .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
         .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
         .endVertex();
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new Barrier(☃, ☃, ☃, ☃, Item.getItemFromBlock(Blocks.BARRIER));
      }
   }
}
