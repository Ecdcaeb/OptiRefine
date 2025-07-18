package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;

public class RenderTntMinecart extends RenderMinecart<EntityMinecartTNT> {
   public RenderTntMinecart(RenderManager var1) {
      super(☃);
   }

   protected void renderCartContents(EntityMinecartTNT var1, float var2, IBlockState var3) {
      int ☃ = ☃.getFuseTicks();
      if (☃ > -1 && ☃ - ☃ + 1.0F < 10.0F) {
         float ☃x = 1.0F - (☃ - ☃ + 1.0F) / 10.0F;
         ☃x = MathHelper.clamp(☃x, 0.0F, 1.0F);
         ☃x *= ☃x;
         ☃x *= ☃x;
         float ☃xx = 1.0F + ☃x * 0.3F;
         GlStateManager.scale(☃xx, ☃xx, ☃xx);
      }

      super.renderCartContents(☃, ☃, ☃);
      if (☃ > -1 && ☃ / 5 % 2 == 0) {
         BlockRendererDispatcher ☃x = Minecraft.getMinecraft().getBlockRendererDispatcher();
         GlStateManager.disableTexture2D();
         GlStateManager.disableLighting();
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.DST_ALPHA);
         GlStateManager.color(1.0F, 1.0F, 1.0F, (1.0F - (☃ - ☃ + 1.0F) / 100.0F) * 0.8F);
         GlStateManager.pushMatrix();
         ☃x.renderBlockBrightness(Blocks.TNT.getDefaultState(), 1.0F);
         GlStateManager.popMatrix();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableBlend();
         GlStateManager.enableLighting();
         GlStateManager.enableTexture2D();
      }
   }
}
