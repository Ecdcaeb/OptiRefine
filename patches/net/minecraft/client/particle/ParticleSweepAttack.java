package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticleSweepAttack extends Particle {
   private static final ResourceLocation SWEEP_TEXTURE = new ResourceLocation("textures/entity/sweep.png");
   private static final VertexFormat VERTEX_FORMAT = new VertexFormat()
      .addElement(DefaultVertexFormats.POSITION_3F)
      .addElement(DefaultVertexFormats.TEX_2F)
      .addElement(DefaultVertexFormats.COLOR_4UB)
      .addElement(DefaultVertexFormats.TEX_2S)
      .addElement(DefaultVertexFormats.NORMAL_3B)
      .addElement(DefaultVertexFormats.PADDING_1B);
   private int life;
   private final int lifeTime;
   private final TextureManager textureManager;
   private final float size;

   protected ParticleSweepAttack(TextureManager var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      super(☃, ☃, ☃, ☃, 0.0, 0.0, 0.0);
      this.textureManager = ☃;
      this.lifeTime = 4;
      float ☃ = this.rand.nextFloat() * 0.6F + 0.4F;
      this.particleRed = ☃;
      this.particleGreen = ☃;
      this.particleBlue = ☃;
      this.size = 1.0F - (float)☃ * 0.5F;
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      int ☃ = (int)((this.life + ☃) * 3.0F / this.lifeTime);
      if (☃ <= 7) {
         this.textureManager.bindTexture(SWEEP_TEXTURE);
         float ☃x = ☃ % 4 / 4.0F;
         float ☃xx = ☃x + 0.24975F;
         float ☃xxx = ☃ / 2 / 2.0F;
         float ☃xxxx = ☃xxx + 0.4995F;
         float ☃xxxxx = 1.0F * this.size;
         float ☃xxxxxx = (float)(this.prevPosX + (this.posX - this.prevPosX) * ☃ - interpPosX);
         float ☃xxxxxxx = (float)(this.prevPosY + (this.posY - this.prevPosY) * ☃ - interpPosY);
         float ☃xxxxxxxx = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * ☃ - interpPosZ);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableLighting();
         RenderHelper.disableStandardItemLighting();
         ☃.begin(7, VERTEX_FORMAT);
         ☃.pos(☃xxxxxx - ☃ * ☃xxxxx - ☃ * ☃xxxxx, ☃xxxxxxx - ☃ * ☃xxxxx * 0.5F, ☃xxxxxxxx - ☃ * ☃xxxxx - ☃ * ☃xxxxx)
            .tex(☃xx, ☃xxxx)
            .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
            .lightmap(0, 240)
            .normal(0.0F, 1.0F, 0.0F)
            .endVertex();
         ☃.pos(☃xxxxxx - ☃ * ☃xxxxx + ☃ * ☃xxxxx, ☃xxxxxxx + ☃ * ☃xxxxx * 0.5F, ☃xxxxxxxx - ☃ * ☃xxxxx + ☃ * ☃xxxxx)
            .tex(☃xx, ☃xxx)
            .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
            .lightmap(0, 240)
            .normal(0.0F, 1.0F, 0.0F)
            .endVertex();
         ☃.pos(☃xxxxxx + ☃ * ☃xxxxx + ☃ * ☃xxxxx, ☃xxxxxxx + ☃ * ☃xxxxx * 0.5F, ☃xxxxxxxx + ☃ * ☃xxxxx + ☃ * ☃xxxxx)
            .tex(☃x, ☃xxx)
            .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
            .lightmap(0, 240)
            .normal(0.0F, 1.0F, 0.0F)
            .endVertex();
         ☃.pos(☃xxxxxx + ☃ * ☃xxxxx - ☃ * ☃xxxxx, ☃xxxxxxx - ☃ * ☃xxxxx * 0.5F, ☃xxxxxxxx + ☃ * ☃xxxxx - ☃ * ☃xxxxx)
            .tex(☃x, ☃xxxx)
            .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
            .lightmap(0, 240)
            .normal(0.0F, 1.0F, 0.0F)
            .endVertex();
         Tessellator.getInstance().draw();
         GlStateManager.enableLighting();
      }
   }

   @Override
   public int getBrightnessForRender(float var1) {
      return 61680;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.life++;
      if (this.life == this.lifeTime) {
         this.setExpired();
      }
   }

   @Override
   public int getFXLayer() {
      return 3;
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleSweepAttack(Minecraft.getMinecraft().getTextureManager(), ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
