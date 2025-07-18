package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.math.MathHelper;

public abstract class RenderArrow<T extends EntityArrow> extends Render<T> {
   public RenderArrow(RenderManager var1) {
      super(☃);
   }

   public void doRender(T var1, double var2, double var4, double var6, float var8, float var9) {
      this.bindEntityTexture(☃);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.pushMatrix();
      GlStateManager.disableLighting();
      GlStateManager.translate((float)☃, (float)☃, (float)☃);
      GlStateManager.rotate(☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃ - 90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃, 0.0F, 0.0F, 1.0F);
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      int ☃xx = 0;
      float ☃xxx = 0.0F;
      float ☃xxxx = 0.5F;
      float ☃xxxxx = 0.0F;
      float ☃xxxxxx = 0.15625F;
      float ☃xxxxxxx = 0.0F;
      float ☃xxxxxxxx = 0.15625F;
      float ☃xxxxxxxxx = 0.15625F;
      float ☃xxxxxxxxxx = 0.3125F;
      float ☃xxxxxxxxxxx = 0.05625F;
      GlStateManager.enableRescaleNormal();
      float ☃xxxxxxxxxxxx = ☃.arrowShake - ☃;
      if (☃xxxxxxxxxxxx > 0.0F) {
         float ☃xxxxxxxxxxxxx = -MathHelper.sin(☃xxxxxxxxxxxx * 3.0F) * ☃xxxxxxxxxxxx;
         GlStateManager.rotate(☃xxxxxxxxxxxxx, 0.0F, 0.0F, 1.0F);
      }

      GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(0.05625F, 0.05625F, 0.05625F);
      GlStateManager.translate(-4.0F, 0.0F, 0.0F);
      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.enableOutlineMode(this.getTeamColor(☃));
      }

      GlStateManager.glNormal3f(0.05625F, 0.0F, 0.0F);
      ☃x.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃x.pos(-7.0, -2.0, -2.0).tex(0.0, 0.15625).endVertex();
      ☃x.pos(-7.0, -2.0, 2.0).tex(0.15625, 0.15625).endVertex();
      ☃x.pos(-7.0, 2.0, 2.0).tex(0.15625, 0.3125).endVertex();
      ☃x.pos(-7.0, 2.0, -2.0).tex(0.0, 0.3125).endVertex();
      ☃.draw();
      GlStateManager.glNormal3f(-0.05625F, 0.0F, 0.0F);
      ☃x.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃x.pos(-7.0, 2.0, -2.0).tex(0.0, 0.15625).endVertex();
      ☃x.pos(-7.0, 2.0, 2.0).tex(0.15625, 0.15625).endVertex();
      ☃x.pos(-7.0, -2.0, 2.0).tex(0.15625, 0.3125).endVertex();
      ☃x.pos(-7.0, -2.0, -2.0).tex(0.0, 0.3125).endVertex();
      ☃.draw();

      for (int ☃xxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxx++) {
         GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.glNormal3f(0.0F, 0.0F, 0.05625F);
         ☃x.begin(7, DefaultVertexFormats.POSITION_TEX);
         ☃x.pos(-8.0, -2.0, 0.0).tex(0.0, 0.0).endVertex();
         ☃x.pos(8.0, -2.0, 0.0).tex(0.5, 0.0).endVertex();
         ☃x.pos(8.0, 2.0, 0.0).tex(0.5, 0.15625).endVertex();
         ☃x.pos(-8.0, 2.0, 0.0).tex(0.0, 0.15625).endVertex();
         ☃.draw();
      }

      if (this.renderOutlines) {
         GlStateManager.disableOutlineMode();
         GlStateManager.disableColorMaterial();
      }

      GlStateManager.disableRescaleNormal();
      GlStateManager.enableLighting();
      GlStateManager.popMatrix();
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }
}
