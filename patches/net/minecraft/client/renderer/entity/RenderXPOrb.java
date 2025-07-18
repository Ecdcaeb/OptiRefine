package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.optifine.CustomColors;

public class RenderXPOrb extends Render<EntityXPOrb> {
   private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");

   public RenderXPOrb(RenderManager renderManagerIn) {
      super(renderManagerIn);
      this.shadowSize = 0.15F;
      this.shadowOpaque = 0.75F;
   }

   public void doRender(EntityXPOrb entity, double x, double y, double z, float entityYaw, float partialTicks) {
      if (!this.renderOutlines) {
         GlStateManager.pushMatrix();
         GlStateManager.translate((float)x, (float)y, (float)z);
         this.bindEntityTexture(entity);
         RenderHelper.enableStandardItemLighting();
         int i = entity.getTextureByXP();
         float f = (i % 4 * 16 + 0) / 64.0F;
         float f1 = (i % 4 * 16 + 16) / 64.0F;
         float f2 = (i / 4 * 16 + 0) / 64.0F;
         float f3 = (i / 4 * 16 + 16) / 64.0F;
         float f4 = 1.0F;
         float f5 = 0.5F;
         float f6 = 0.25F;
         int j = entity.getBrightnessForRender();
         int k = j % 65536;
         int l = j / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k, l);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         float f8 = 255.0F;
         float f9 = (entity.xpColor + partialTicks) / 2.0F;
         if (Config.isCustomColors()) {
            f9 = CustomColors.getXpOrbTimer(f9);
         }

         l = (int)((MathHelper.sin(f9 + 0.0F) + 1.0F) * 0.5F * 255.0F);
         int i1 = 255;
         int j1 = (int)((MathHelper.sin(f9 + (float) (Math.PI * 4.0 / 3.0)) + 1.0F) * 0.1F * 255.0F);
         GlStateManager.translate(0.0F, 0.1F, 0.0F);
         GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
         float f7 = 0.3F;
         GlStateManager.scale(0.3F, 0.3F, 0.3F);
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
         int red = l;
         int green = 255;
         int blue = j1;
         if (Config.isCustomColors()) {
            int col = CustomColors.getXpOrbColor(f9);
            if (col >= 0) {
               red = col >> 16 & 0xFF;
               green = col >> 8 & 0xFF;
               blue = col >> 0 & 0xFF;
            }
         }

         bufferbuilder.pos(-0.5, -0.25, 0.0).tex(f, f3).color(red, green, blue, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
         bufferbuilder.pos(0.5, -0.25, 0.0).tex(f1, f3).color(red, green, blue, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
         bufferbuilder.pos(0.5, 0.75, 0.0).tex(f1, f2).color(red, green, blue, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
         bufferbuilder.pos(-0.5, 0.75, 0.0).tex(f, f2).color(red, green, blue, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
         tessellator.draw();
         GlStateManager.disableBlend();
         GlStateManager.disableRescaleNormal();
         GlStateManager.popMatrix();
         super.doRender(entity, x, y, z, entityYaw, partialTicks);
      }
   }

   protected ResourceLocation getEntityTexture(EntityXPOrb entity) {
      return EXPERIENCE_ORB_TEXTURES;
   }
}
