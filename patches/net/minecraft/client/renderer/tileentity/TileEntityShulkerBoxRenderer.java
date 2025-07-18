package net.minecraft.client.renderer.tileentity;

import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderShulker;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;

public class TileEntityShulkerBoxRenderer extends TileEntitySpecialRenderer<TileEntityShulkerBox> {
   private final ModelShulker model;

   public TileEntityShulkerBoxRenderer(ModelShulker var1) {
      this.model = ☃;
   }

   public void render(TileEntityShulkerBox var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      EnumFacing ☃ = EnumFacing.UP;
      if (☃.hasWorld()) {
         IBlockState ☃x = this.getWorld().getBlockState(☃.getPos());
         if (☃x.getBlock() instanceof BlockShulkerBox) {
            ☃ = ☃x.getValue(BlockShulkerBox.FACING);
         }
      }

      GlStateManager.enableDepth();
      GlStateManager.depthFunc(515);
      GlStateManager.depthMask(true);
      GlStateManager.disableCull();
      if (☃ >= 0) {
         this.bindTexture(DESTROY_STAGES[☃]);
         GlStateManager.matrixMode(5890);
         GlStateManager.pushMatrix();
         GlStateManager.scale(4.0F, 4.0F, 1.0F);
         GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
         GlStateManager.matrixMode(5888);
      } else {
         this.bindTexture(RenderShulker.SHULKER_ENDERGOLEM_TEXTURE[☃.getColor().getMetadata()]);
      }

      GlStateManager.pushMatrix();
      GlStateManager.enableRescaleNormal();
      if (☃ < 0) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, ☃);
      }

      GlStateManager.translate((float)☃ + 0.5F, (float)☃ + 1.5F, (float)☃ + 0.5F);
      GlStateManager.scale(1.0F, -1.0F, -1.0F);
      GlStateManager.translate(0.0F, 1.0F, 0.0F);
      float ☃x = 0.9995F;
      GlStateManager.scale(0.9995F, 0.9995F, 0.9995F);
      GlStateManager.translate(0.0F, -1.0F, 0.0F);
      switch (☃) {
         case DOWN:
            GlStateManager.translate(0.0F, 2.0F, 0.0F);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
         case UP:
         default:
            break;
         case NORTH:
            GlStateManager.translate(0.0F, 1.0F, 1.0F);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            break;
         case SOUTH:
            GlStateManager.translate(0.0F, 1.0F, -1.0F);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            break;
         case WEST:
            GlStateManager.translate(-1.0F, 1.0F, 0.0F);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
            break;
         case EAST:
            GlStateManager.translate(1.0F, 1.0F, 0.0F);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
      }

      this.model.base.render(0.0625F);
      GlStateManager.translate(0.0F, -☃.getProgress(☃) * 0.5F, 0.0F);
      GlStateManager.rotate(270.0F * ☃.getProgress(☃), 0.0F, 1.0F, 0.0F);
      this.model.lid.render(0.0625F);
      GlStateManager.enableCull();
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
