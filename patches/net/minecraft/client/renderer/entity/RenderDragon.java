package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonDeath;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonEyes;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderDragon extends RenderLiving<EntityDragon> {
   public static final ResourceLocation ENDERCRYSTAL_BEAM_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");
   private static final ResourceLocation DRAGON_EXPLODING_TEXTURES = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
   private static final ResourceLocation DRAGON_TEXTURES = new ResourceLocation("textures/entity/enderdragon/dragon.png");

   public RenderDragon(RenderManager var1) {
      super(☃, new ModelDragon(0.0F), 0.5F);
      this.addLayer(new LayerEnderDragonEyes(this));
      this.addLayer(new LayerEnderDragonDeath());
   }

   protected void applyRotations(EntityDragon var1, float var2, float var3, float var4) {
      float ☃ = (float)☃.getMovementOffsets(7, ☃)[0];
      float ☃x = (float)(☃.getMovementOffsets(5, ☃)[1] - ☃.getMovementOffsets(10, ☃)[1]);
      GlStateManager.rotate(-☃, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(☃x * 10.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.translate(0.0F, 0.0F, 1.0F);
      if (☃.deathTime > 0) {
         float ☃xx = (☃.deathTime + ☃ - 1.0F) / 20.0F * 1.6F;
         ☃xx = MathHelper.sqrt(☃xx);
         if (☃xx > 1.0F) {
            ☃xx = 1.0F;
         }

         GlStateManager.rotate(☃xx * this.getDeathMaxRotation(☃), 0.0F, 0.0F, 1.0F);
      }
   }

   protected void renderModel(EntityDragon var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      if (☃.deathTicks > 0) {
         float ☃ = ☃.deathTicks / 200.0F;
         GlStateManager.depthFunc(515);
         GlStateManager.enableAlpha();
         GlStateManager.alphaFunc(516, ☃);
         this.bindTexture(DRAGON_EXPLODING_TEXTURES);
         this.mainModel.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         GlStateManager.alphaFunc(516, 0.1F);
         GlStateManager.depthFunc(514);
      }

      this.bindEntityTexture(☃);
      this.mainModel.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      if (☃.hurtTime > 0) {
         GlStateManager.depthFunc(514);
         GlStateManager.disableTexture2D();
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
         GlStateManager.color(1.0F, 0.0F, 0.0F, 0.5F);
         this.mainModel.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
         GlStateManager.depthFunc(515);
      }
   }

   public void doRender(EntityDragon var1, double var2, double var4, double var6, float var8, float var9) {
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
      if (☃.healingEnderCrystal != null) {
         this.bindTexture(ENDERCRYSTAL_BEAM_TEXTURES);
         float ☃ = MathHelper.sin((☃.healingEnderCrystal.ticksExisted + ☃) * 0.2F) / 2.0F + 0.5F;
         ☃ = (☃ * ☃ + ☃) * 0.2F;
         renderCrystalBeams(
            ☃,
            ☃,
            ☃,
            ☃,
            ☃.posX + (☃.prevPosX - ☃.posX) * (1.0F - ☃),
            ☃.posY + (☃.prevPosY - ☃.posY) * (1.0F - ☃),
            ☃.posZ + (☃.prevPosZ - ☃.posZ) * (1.0F - ☃),
            ☃.ticksExisted,
            ☃.healingEnderCrystal.posX,
            ☃ + ☃.healingEnderCrystal.posY,
            ☃.healingEnderCrystal.posZ
         );
      }
   }

   public static void renderCrystalBeams(
      double var0, double var2, double var4, float var6, double var7, double var9, double var11, int var13, double var14, double var16, double var18
   ) {
      float ☃ = (float)(☃ - ☃);
      float ☃x = (float)(☃ - 1.0 - ☃);
      float ☃xx = (float)(☃ - ☃);
      float ☃xxx = MathHelper.sqrt(☃ * ☃ + ☃xx * ☃xx);
      float ☃xxxx = MathHelper.sqrt(☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx);
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)☃, (float)☃ + 2.0F, (float)☃);
      GlStateManager.rotate((float)(-Math.atan2(☃xx, ☃)) * (180.0F / (float)Math.PI) - 90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate((float)(-Math.atan2(☃xxx, ☃x)) * (180.0F / (float)Math.PI) - 90.0F, 1.0F, 0.0F, 0.0F);
      Tessellator ☃xxxxx = Tessellator.getInstance();
      BufferBuilder ☃xxxxxx = ☃xxxxx.getBuffer();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableCull();
      GlStateManager.shadeModel(7425);
      float ☃xxxxxxx = 0.0F - (☃ + ☃) * 0.01F;
      float ☃xxxxxxxx = MathHelper.sqrt(☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx) / 32.0F - (☃ + ☃) * 0.01F;
      ☃xxxxxx.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
      int ☃xxxxxxxxx = 8;

      for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx <= 8; ☃xxxxxxxxxx++) {
         float ☃xxxxxxxxxxx = MathHelper.sin(☃xxxxxxxxxx % 8 * (float) (Math.PI * 2) / 8.0F) * 0.75F;
         float ☃xxxxxxxxxxxx = MathHelper.cos(☃xxxxxxxxxx % 8 * (float) (Math.PI * 2) / 8.0F) * 0.75F;
         float ☃xxxxxxxxxxxxx = ☃xxxxxxxxxx % 8 / 8.0F;
         ☃xxxxxx.pos(☃xxxxxxxxxxx * 0.2F, ☃xxxxxxxxxxxx * 0.2F, 0.0).tex(☃xxxxxxxxxxxxx, ☃xxxxxxx).color(0, 0, 0, 255).endVertex();
         ☃xxxxxx.pos(☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxx).tex(☃xxxxxxxxxxxxx, ☃xxxxxxxx).color(255, 255, 255, 255).endVertex();
      }

      ☃xxxxx.draw();
      GlStateManager.enableCull();
      GlStateManager.shadeModel(7424);
      RenderHelper.enableStandardItemLighting();
      GlStateManager.popMatrix();
   }

   protected ResourceLocation getEntityTexture(EntityDragon var1) {
      return DRAGON_TEXTURES;
   }
}
