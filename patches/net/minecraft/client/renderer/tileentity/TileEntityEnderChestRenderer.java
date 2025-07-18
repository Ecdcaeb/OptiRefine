package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.ResourceLocation;

public class TileEntityEnderChestRenderer extends TileEntitySpecialRenderer<TileEntityEnderChest> {
   private static final ResourceLocation ENDER_CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/ender.png");
   private final ModelChest modelChest = new ModelChest();

   public void render(TileEntityEnderChest var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      int ☃ = 0;
      if (☃.hasWorld()) {
         ☃ = ☃.getBlockMetadata();
      }

      if (☃ >= 0) {
         this.bindTexture(DESTROY_STAGES[☃]);
         GlStateManager.matrixMode(5890);
         GlStateManager.pushMatrix();
         GlStateManager.scale(4.0F, 4.0F, 1.0F);
         GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
         GlStateManager.matrixMode(5888);
      } else {
         this.bindTexture(ENDER_CHEST_TEXTURE);
      }

      GlStateManager.pushMatrix();
      GlStateManager.enableRescaleNormal();
      GlStateManager.color(1.0F, 1.0F, 1.0F, ☃);
      GlStateManager.translate((float)☃, (float)☃ + 1.0F, (float)☃ + 1.0F);
      GlStateManager.scale(1.0F, -1.0F, -1.0F);
      GlStateManager.translate(0.5F, 0.5F, 0.5F);
      int ☃x = 0;
      if (☃ == 2) {
         ☃x = 180;
      }

      if (☃ == 3) {
         ☃x = 0;
      }

      if (☃ == 4) {
         ☃x = 90;
      }

      if (☃ == 5) {
         ☃x = -90;
      }

      GlStateManager.rotate(☃x, 0.0F, 1.0F, 0.0F);
      GlStateManager.translate(-0.5F, -0.5F, -0.5F);
      float ☃xx = ☃.prevLidAngle + (☃.lidAngle - ☃.prevLidAngle) * ☃;
      ☃xx = 1.0F - ☃xx;
      ☃xx = 1.0F - ☃xx * ☃xx * ☃xx;
      this.modelChest.chestLid.rotateAngleX = -(☃xx * (float) (Math.PI / 2));
      this.modelChest.renderAll();
      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      if (☃ >= 0) {
         GlStateManager.matrixMode(5890);
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5888);
      }
   }
}
