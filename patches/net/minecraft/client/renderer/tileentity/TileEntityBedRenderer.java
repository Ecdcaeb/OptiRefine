package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBed;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityBedRenderer extends TileEntitySpecialRenderer<TileEntityBed> {
   private static final ResourceLocation[] TEXTURES;
   private ModelBed model = new ModelBed();
   private int version = this.model.getModelVersion();

   public void render(TileEntityBed var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      if (this.version != this.model.getModelVersion()) {
         this.model = new ModelBed();
         this.version = this.model.getModelVersion();
      }

      boolean ☃ = ☃.getWorld() != null;
      boolean ☃x = ☃ ? ☃.isHeadPiece() : true;
      EnumDyeColor ☃xx = ☃ != null ? ☃.getColor() : EnumDyeColor.RED;
      int ☃xxx = ☃ ? ☃.getBlockMetadata() & 3 : 0;
      if (☃ >= 0) {
         this.bindTexture(DESTROY_STAGES[☃]);
         GlStateManager.matrixMode(5890);
         GlStateManager.pushMatrix();
         GlStateManager.scale(4.0F, 4.0F, 1.0F);
         GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
         GlStateManager.matrixMode(5888);
      } else {
         ResourceLocation ☃xxxx = TEXTURES[☃xx.getMetadata()];
         if (☃xxxx != null) {
            this.bindTexture(☃xxxx);
         }
      }

      if (☃) {
         this.renderPiece(☃x, ☃, ☃, ☃, ☃xxx, ☃);
      } else {
         GlStateManager.pushMatrix();
         this.renderPiece(true, ☃, ☃, ☃, ☃xxx, ☃);
         this.renderPiece(false, ☃, ☃, ☃ - 1.0, ☃xxx, ☃);
         GlStateManager.popMatrix();
      }

      if (☃ >= 0) {
         GlStateManager.matrixMode(5890);
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5888);
      }
   }

   private void renderPiece(boolean var1, double var2, double var4, double var6, int var8, float var9) {
      this.model.preparePiece(☃);
      GlStateManager.pushMatrix();
      float ☃ = 0.0F;
      float ☃x = 0.0F;
      float ☃xx = 0.0F;
      if (☃ == EnumFacing.NORTH.getHorizontalIndex()) {
         ☃ = 0.0F;
      } else if (☃ == EnumFacing.SOUTH.getHorizontalIndex()) {
         ☃ = 180.0F;
         ☃x = 1.0F;
         ☃xx = 1.0F;
      } else if (☃ == EnumFacing.WEST.getHorizontalIndex()) {
         ☃ = -90.0F;
         ☃xx = 1.0F;
      } else if (☃ == EnumFacing.EAST.getHorizontalIndex()) {
         ☃ = 90.0F;
         ☃x = 1.0F;
      }

      GlStateManager.translate((float)☃ + ☃x, (float)☃ + 0.5625F, (float)☃ + ☃xx);
      GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(☃, 0.0F, 0.0F, 1.0F);
      GlStateManager.enableRescaleNormal();
      GlStateManager.pushMatrix();
      this.model.render();
      GlStateManager.popMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F, ☃);
      GlStateManager.popMatrix();
   }

   static {
      EnumDyeColor[] ☃ = EnumDyeColor.values();
      TEXTURES = new ResourceLocation[☃.length];

      for (EnumDyeColor ☃x : ☃) {
         TEXTURES[☃x.getMetadata()] = new ResourceLocation("textures/entity/bed/" + ☃x.getDyeColorName() + ".png");
      }
   }
}
