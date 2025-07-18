package net.minecraft.client.renderer.entity.layers;

import java.util.Random;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.boss.EntityDragon;

public class LayerEnderDragonDeath implements LayerRenderer<EntityDragon> {
   public void doRenderLayer(EntityDragon var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (☃.deathTicks > 0) {
         Tessellator ☃ = Tessellator.getInstance();
         BufferBuilder ☃x = ☃.getBuffer();
         RenderHelper.disableStandardItemLighting();
         float ☃xx = (☃.deathTicks + ☃) / 200.0F;
         float ☃xxx = 0.0F;
         if (☃xx > 0.8F) {
            ☃xxx = (☃xx - 0.8F) / 0.2F;
         }

         Random ☃xxxx = new Random(432L);
         GlStateManager.disableTexture2D();
         GlStateManager.shadeModel(7425);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
         GlStateManager.disableAlpha();
         GlStateManager.enableCull();
         GlStateManager.depthMask(false);
         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, -1.0F, -2.0F);

         for (int ☃xxxxx = 0; ☃xxxxx < (☃xx + ☃xx * ☃xx) / 2.0F * 60.0F; ☃xxxxx++) {
            GlStateManager.rotate(☃xxxx.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(☃xxxx.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(☃xxxx.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(☃xxxx.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(☃xxxx.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(☃xxxx.nextFloat() * 360.0F + ☃xx * 90.0F, 0.0F, 0.0F, 1.0F);
            float ☃xxxxxx = ☃xxxx.nextFloat() * 20.0F + 5.0F + ☃xxx * 10.0F;
            float ☃xxxxxxx = ☃xxxx.nextFloat() * 2.0F + 1.0F + ☃xxx * 2.0F;
            ☃x.begin(6, DefaultVertexFormats.POSITION_COLOR);
            ☃x.pos(0.0, 0.0, 0.0).color(255, 255, 255, (int)(255.0F * (1.0F - ☃xxx))).endVertex();
            ☃x.pos(-0.866 * ☃xxxxxxx, ☃xxxxxx, -0.5F * ☃xxxxxxx).color(255, 0, 255, 0).endVertex();
            ☃x.pos(0.866 * ☃xxxxxxx, ☃xxxxxx, -0.5F * ☃xxxxxxx).color(255, 0, 255, 0).endVertex();
            ☃x.pos(0.0, ☃xxxxxx, 1.0F * ☃xxxxxxx).color(255, 0, 255, 0).endVertex();
            ☃x.pos(-0.866 * ☃xxxxxxx, ☃xxxxxx, -0.5F * ☃xxxxxxx).color(255, 0, 255, 0).endVertex();
            ☃.draw();
         }

         GlStateManager.popMatrix();
         GlStateManager.depthMask(true);
         GlStateManager.disableCull();
         GlStateManager.disableBlend();
         GlStateManager.shadeModel(7424);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.enableTexture2D();
         GlStateManager.enableAlpha();
         RenderHelper.enableStandardItemLighting();
      }
   }

   @Override
   public boolean shouldCombineTextures() {
      return false;
   }
}
