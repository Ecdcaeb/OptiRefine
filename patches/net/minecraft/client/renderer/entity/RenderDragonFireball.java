package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.util.ResourceLocation;

public class RenderDragonFireball extends Render<EntityDragonFireball> {
   private static final ResourceLocation DRAGON_FIREBALL_TEXTURE = new ResourceLocation("textures/entity/enderdragon/dragon_fireball.png");

   public RenderDragonFireball(RenderManager var1) {
      super(☃);
   }

   public void doRender(EntityDragonFireball var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      this.bindEntityTexture(☃);
      GlStateManager.translate((float)☃, (float)☃, (float)☃);
      GlStateManager.enableRescaleNormal();
      GlStateManager.scale(2.0F, 2.0F, 2.0F);
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      float ☃xx = 1.0F;
      float ☃xxx = 0.5F;
      float ☃xxxx = 0.25F;
      GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.enableOutlineMode(this.getTeamColor(☃));
      }

      ☃x.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
      ☃x.pos(-0.5, -0.25, 0.0).tex(0.0, 1.0).normal(0.0F, 1.0F, 0.0F).endVertex();
      ☃x.pos(0.5, -0.25, 0.0).tex(1.0, 1.0).normal(0.0F, 1.0F, 0.0F).endVertex();
      ☃x.pos(0.5, 0.75, 0.0).tex(1.0, 0.0).normal(0.0F, 1.0F, 0.0F).endVertex();
      ☃x.pos(-0.5, 0.75, 0.0).tex(0.0, 0.0).normal(0.0F, 1.0F, 0.0F).endVertex();
      ☃.draw();
      if (this.renderOutlines) {
         GlStateManager.disableOutlineMode();
         GlStateManager.disableColorMaterial();
      }

      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected ResourceLocation getEntityTexture(EntityDragonFireball var1) {
      return DRAGON_FIREBALL_TEXTURE;
   }
}
