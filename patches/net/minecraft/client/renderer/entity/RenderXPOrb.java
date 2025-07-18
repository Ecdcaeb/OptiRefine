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

public class RenderXPOrb extends Render<EntityXPOrb> {
   private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");

   public RenderXPOrb(RenderManager var1) {
      super(☃);
      this.shadowSize = 0.15F;
      this.shadowOpaque = 0.75F;
   }

   public void doRender(EntityXPOrb var1, double var2, double var4, double var6, float var8, float var9) {
      if (!this.renderOutlines) {
         GlStateManager.pushMatrix();
         GlStateManager.translate((float)☃, (float)☃, (float)☃);
         this.bindEntityTexture(☃);
         RenderHelper.enableStandardItemLighting();
         int ☃ = ☃.getTextureByXP();
         float ☃x = (☃ % 4 * 16 + 0) / 64.0F;
         float ☃xx = (☃ % 4 * 16 + 16) / 64.0F;
         float ☃xxx = (☃ / 4 * 16 + 0) / 64.0F;
         float ☃xxxx = (☃ / 4 * 16 + 16) / 64.0F;
         float ☃xxxxx = 1.0F;
         float ☃xxxxxx = 0.5F;
         float ☃xxxxxxx = 0.25F;
         int ☃xxxxxxxx = ☃.getBrightnessForRender();
         int ☃xxxxxxxxx = ☃xxxxxxxx % 65536;
         int ☃xxxxxxxxxx = ☃xxxxxxxx / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, ☃xxxxxxxxx, ☃xxxxxxxxxx);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         float ☃xxxxxxxxxxx = 255.0F;
         float ☃xxxxxxxxxxxx = (☃.xpColor + ☃) / 2.0F;
         ☃xxxxxxxxxx = (int)((MathHelper.sin(☃xxxxxxxxxxxx + 0.0F) + 1.0F) * 0.5F * 255.0F);
         int ☃xxxxxxxxxxxxx = 255;
         int ☃xxxxxxxxxxxxxx = (int)((MathHelper.sin(☃xxxxxxxxxxxx + (float) (Math.PI * 4.0 / 3.0)) + 1.0F) * 0.1F * 255.0F);
         GlStateManager.translate(0.0F, 0.1F, 0.0F);
         GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
         float ☃xxxxxxxxxxxxxxx = 0.3F;
         GlStateManager.scale(0.3F, 0.3F, 0.3F);
         Tessellator ☃xxxxxxxxxxxxxxxx = Tessellator.getInstance();
         BufferBuilder ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx.getBuffer();
         ☃xxxxxxxxxxxxxxxxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
         ☃xxxxxxxxxxxxxxxxx.pos(-0.5, -0.25, 0.0).tex(☃x, ☃xxxx).color(☃xxxxxxxxxx, 255, ☃xxxxxxxxxxxxxx, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
         ☃xxxxxxxxxxxxxxxxx.pos(0.5, -0.25, 0.0).tex(☃xx, ☃xxxx).color(☃xxxxxxxxxx, 255, ☃xxxxxxxxxxxxxx, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
         ☃xxxxxxxxxxxxxxxxx.pos(0.5, 0.75, 0.0).tex(☃xx, ☃xxx).color(☃xxxxxxxxxx, 255, ☃xxxxxxxxxxxxxx, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
         ☃xxxxxxxxxxxxxxxxx.pos(-0.5, 0.75, 0.0).tex(☃x, ☃xxx).color(☃xxxxxxxxxx, 255, ☃xxxxxxxxxxxxxx, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
         ☃xxxxxxxxxxxxxxxx.draw();
         GlStateManager.disableBlend();
         GlStateManager.disableRescaleNormal();
         GlStateManager.popMatrix();
         super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   protected ResourceLocation getEntityTexture(EntityXPOrb var1) {
      return EXPERIENCE_ORB_TEXTURES;
   }
}
