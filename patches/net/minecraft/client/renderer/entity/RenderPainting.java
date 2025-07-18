package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class RenderPainting extends Render<EntityPainting> {
   private static final ResourceLocation KRISTOFFER_PAINTING_TEXTURE = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");

   public RenderPainting(RenderManager var1) {
      super(☃);
   }

   public void doRender(EntityPainting var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      GlStateManager.translate(☃, ☃, ☃);
      GlStateManager.rotate(180.0F - ☃, 0.0F, 1.0F, 0.0F);
      GlStateManager.enableRescaleNormal();
      this.bindEntityTexture(☃);
      EntityPainting.EnumArt ☃ = ☃.art;
      float ☃x = 0.0625F;
      GlStateManager.scale(0.0625F, 0.0625F, 0.0625F);
      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.enableOutlineMode(this.getTeamColor(☃));
      }

      this.renderPainting(☃, ☃.sizeX, ☃.sizeY, ☃.offsetX, ☃.offsetY);
      if (this.renderOutlines) {
         GlStateManager.disableOutlineMode();
         GlStateManager.disableColorMaterial();
      }

      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected ResourceLocation getEntityTexture(EntityPainting var1) {
      return KRISTOFFER_PAINTING_TEXTURE;
   }

   private void renderPainting(EntityPainting var1, int var2, int var3, int var4, int var5) {
      float ☃ = -☃ / 2.0F;
      float ☃x = -☃ / 2.0F;
      float ☃xx = 0.5F;
      float ☃xxx = 0.75F;
      float ☃xxxx = 0.8125F;
      float ☃xxxxx = 0.0F;
      float ☃xxxxxx = 0.0625F;
      float ☃xxxxxxx = 0.75F;
      float ☃xxxxxxxx = 0.8125F;
      float ☃xxxxxxxxx = 0.001953125F;
      float ☃xxxxxxxxxx = 0.001953125F;
      float ☃xxxxxxxxxxx = 0.7519531F;
      float ☃xxxxxxxxxxxx = 0.7519531F;
      float ☃xxxxxxxxxxxxx = 0.0F;
      float ☃xxxxxxxxxxxxxx = 0.0625F;

      for (int ☃xxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxx < ☃ / 16; ☃xxxxxxxxxxxxxxx++) {
         for (int ☃xxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxx < ☃ / 16; ☃xxxxxxxxxxxxxxxx++) {
            float ☃xxxxxxxxxxxxxxxxx = ☃ + (☃xxxxxxxxxxxxxxx + 1) * 16;
            float ☃xxxxxxxxxxxxxxxxxx = ☃ + ☃xxxxxxxxxxxxxxx * 16;
            float ☃xxxxxxxxxxxxxxxxxxx = ☃x + (☃xxxxxxxxxxxxxxxx + 1) * 16;
            float ☃xxxxxxxxxxxxxxxxxxxx = ☃x + ☃xxxxxxxxxxxxxxxx * 16;
            this.setLightmap(☃, (☃xxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx) / 2.0F, (☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx) / 2.0F);
            float ☃xxxxxxxxxxxxxxxxxxxxx = (☃ + ☃ - ☃xxxxxxxxxxxxxxx * 16) / 256.0F;
            float ☃xxxxxxxxxxxxxxxxxxxxxx = (☃ + ☃ - (☃xxxxxxxxxxxxxxx + 1) * 16) / 256.0F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxx = (☃ + ☃ - ☃xxxxxxxxxxxxxxxx * 16) / 256.0F;
            float ☃xxxxxxxxxxxxxxxxxxxxxxxx = (☃ + ☃ - (☃xxxxxxxxxxxxxxxx + 1) * 16) / 256.0F;
            Tessellator ☃xxxxxxxxxxxxxxxxxxxxxxxxx = Tessellator.getInstance();
            BufferBuilder ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx.getBuffer();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, -0.5)
               .tex(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx)
               .normal(0.0F, 0.0F, -1.0F)
               .endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, -0.5)
               .tex(☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx)
               .normal(0.0F, 0.0F, -1.0F)
               .endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, -0.5)
               .tex(☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx)
               .normal(0.0F, 0.0F, -1.0F)
               .endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, -0.5)
               .tex(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx)
               .normal(0.0F, 0.0F, -1.0F)
               .endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 0.5).tex(0.75, 0.0).normal(0.0F, 0.0F, 1.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 0.5).tex(0.8125, 0.0).normal(0.0F, 0.0F, 1.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, 0.5).tex(0.8125, 0.0625).normal(0.0F, 0.0F, 1.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, 0.5).tex(0.75, 0.0625).normal(0.0F, 0.0F, 1.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, -0.5).tex(0.75, 0.001953125).normal(0.0F, 1.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, -0.5).tex(0.8125, 0.001953125).normal(0.0F, 1.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 0.5).tex(0.8125, 0.001953125).normal(0.0F, 1.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 0.5).tex(0.75, 0.001953125).normal(0.0F, 1.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, 0.5).tex(0.75, 0.001953125).normal(0.0F, -1.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, 0.5).tex(0.8125, 0.001953125).normal(0.0F, -1.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, -0.5).tex(0.8125, 0.001953125).normal(0.0F, -1.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, -0.5).tex(0.75, 0.001953125).normal(0.0F, -1.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 0.5).tex(0.7519531F, 0.0).normal(-1.0F, 0.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, 0.5).tex(0.7519531F, 0.0625).normal(-1.0F, 0.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, -0.5).tex(0.7519531F, 0.0625).normal(-1.0F, 0.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, -0.5).tex(0.7519531F, 0.0).normal(-1.0F, 0.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, -0.5).tex(0.7519531F, 0.0).normal(1.0F, 0.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, -0.5).tex(0.7519531F, 0.0625).normal(1.0F, 0.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, 0.5).tex(0.7519531F, 0.0625).normal(1.0F, 0.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 0.5).tex(0.7519531F, 0.0).normal(1.0F, 0.0F, 0.0F).endVertex();
            ☃xxxxxxxxxxxxxxxxxxxxxxxxx.draw();
         }
      }
   }

   private void setLightmap(EntityPainting var1, float var2, float var3) {
      int ☃ = MathHelper.floor(☃.posX);
      int ☃x = MathHelper.floor(☃.posY + ☃ / 16.0F);
      int ☃xx = MathHelper.floor(☃.posZ);
      EnumFacing ☃xxx = ☃.facingDirection;
      if (☃xxx == EnumFacing.NORTH) {
         ☃ = MathHelper.floor(☃.posX + ☃ / 16.0F);
      }

      if (☃xxx == EnumFacing.WEST) {
         ☃xx = MathHelper.floor(☃.posZ - ☃ / 16.0F);
      }

      if (☃xxx == EnumFacing.SOUTH) {
         ☃ = MathHelper.floor(☃.posX - ☃ / 16.0F);
      }

      if (☃xxx == EnumFacing.EAST) {
         ☃xx = MathHelper.floor(☃.posZ + ☃ / 16.0F);
      }

      int ☃xxxx = this.renderManager.world.getCombinedLight(new BlockPos(☃, ☃x, ☃xx), 0);
      int ☃xxxxx = ☃xxxx % 65536;
      int ☃xxxxxx = ☃xxxx / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, ☃xxxxx, ☃xxxxxx);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
   }
}
