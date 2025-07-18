package net.minecraft.client.renderer.tileentity;

import javax.annotation.Nullable;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TileEntityBannerRenderer extends TileEntitySpecialRenderer<TileEntityBanner> {
   private final ModelBanner bannerModel = new ModelBanner();

   public void render(TileEntityBanner var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      boolean ☃ = ☃.getWorld() != null;
      boolean ☃x = !☃ || ☃.getBlockType() == Blocks.STANDING_BANNER;
      int ☃xx = ☃ ? ☃.getBlockMetadata() : 0;
      long ☃xxx = ☃ ? ☃.getWorld().getTotalWorldTime() : 0L;
      GlStateManager.pushMatrix();
      float ☃xxxx = 0.6666667F;
      if (☃x) {
         GlStateManager.translate((float)☃ + 0.5F, (float)☃ + 0.5F, (float)☃ + 0.5F);
         float ☃xxxxx = ☃xx * 360 / 16.0F;
         GlStateManager.rotate(-☃xxxxx, 0.0F, 1.0F, 0.0F);
         this.bannerModel.bannerStand.showModel = true;
      } else {
         float ☃xxxxx = 0.0F;
         if (☃xx == 2) {
            ☃xxxxx = 180.0F;
         }

         if (☃xx == 4) {
            ☃xxxxx = 90.0F;
         }

         if (☃xx == 5) {
            ☃xxxxx = -90.0F;
         }

         GlStateManager.translate((float)☃ + 0.5F, (float)☃ - 0.16666667F, (float)☃ + 0.5F);
         GlStateManager.rotate(-☃xxxxx, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
         this.bannerModel.bannerStand.showModel = false;
      }

      BlockPos ☃xxxxxx = ☃.getPos();
      float ☃xxxxxxx = ☃xxxxxx.getX() * 7 + ☃xxxxxx.getY() * 9 + ☃xxxxxx.getZ() * 13 + (float)☃xxx + ☃;
      this.bannerModel.bannerSlate.rotateAngleX = (-0.0125F + 0.01F * MathHelper.cos(☃xxxxxxx * (float) Math.PI * 0.02F)) * (float) Math.PI;
      GlStateManager.enableRescaleNormal();
      ResourceLocation ☃xxxxxxxx = this.getBannerResourceLocation(☃);
      if (☃xxxxxxxx != null) {
         this.bindTexture(☃xxxxxxxx);
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.6666667F, -0.6666667F, -0.6666667F);
         this.bannerModel.renderBanner();
         GlStateManager.popMatrix();
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, ☃);
      GlStateManager.popMatrix();
   }

   @Nullable
   private ResourceLocation getBannerResourceLocation(TileEntityBanner var1) {
      return BannerTextures.BANNER_DESIGNS.getResourceLocation(☃.getPatternResourceLocation(), ☃.getPatternList(), ☃.getColorList());
   }
}
