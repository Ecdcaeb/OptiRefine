package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityEnchantmentTableRenderer extends TileEntitySpecialRenderer<TileEntityEnchantmentTable> {
   private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation("textures/entity/enchanting_table_book.png");
   private final ModelBook modelBook = new ModelBook();

   public void render(TileEntityEnchantmentTable var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)☃ + 0.5F, (float)☃ + 0.75F, (float)☃ + 0.5F);
      float ☃ = ☃.tickCount + ☃;
      GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(☃ * 0.1F) * 0.01F, 0.0F);
      float ☃x = ☃.bookRotation - ☃.bookRotationPrev;

      while (☃x >= (float) Math.PI) {
         ☃x -= (float) (Math.PI * 2);
      }

      while (☃x < (float) -Math.PI) {
         ☃x += (float) (Math.PI * 2);
      }

      float ☃xx = ☃.bookRotationPrev + ☃x * ☃;
      GlStateManager.rotate(-☃xx * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
      this.bindTexture(TEXTURE_BOOK);
      float ☃xxx = ☃.pageFlipPrev + (☃.pageFlip - ☃.pageFlipPrev) * ☃ + 0.25F;
      float ☃xxxx = ☃.pageFlipPrev + (☃.pageFlip - ☃.pageFlipPrev) * ☃ + 0.75F;
      ☃xxx = (☃xxx - MathHelper.fastFloor(☃xxx)) * 1.6F - 0.3F;
      ☃xxxx = (☃xxxx - MathHelper.fastFloor(☃xxxx)) * 1.6F - 0.3F;
      if (☃xxx < 0.0F) {
         ☃xxx = 0.0F;
      }

      if (☃xxxx < 0.0F) {
         ☃xxxx = 0.0F;
      }

      if (☃xxx > 1.0F) {
         ☃xxx = 1.0F;
      }

      if (☃xxxx > 1.0F) {
         ☃xxxx = 1.0F;
      }

      float ☃xxxxx = ☃.bookSpreadPrev + (☃.bookSpread - ☃.bookSpreadPrev) * ☃;
      GlStateManager.enableCull();
      this.modelBook.render(null, ☃, ☃xxx, ☃xxxx, ☃xxxxx, 0.0F, 0.0625F);
      GlStateManager.popMatrix();
   }
}
