package net.minecraft.client.renderer.tileentity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TileEntitySignRenderer extends TileEntitySpecialRenderer<TileEntitySign> {
   private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
   private final ModelSign model = new ModelSign();

   public void render(TileEntitySign var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      Block ☃ = ☃.getBlockType();
      GlStateManager.pushMatrix();
      float ☃x = 0.6666667F;
      if (☃ == Blocks.STANDING_SIGN) {
         GlStateManager.translate((float)☃ + 0.5F, (float)☃ + 0.5F, (float)☃ + 0.5F);
         float ☃xx = ☃.getBlockMetadata() * 360 / 16.0F;
         GlStateManager.rotate(-☃xx, 0.0F, 1.0F, 0.0F);
         this.model.signStick.showModel = true;
      } else {
         int ☃xx = ☃.getBlockMetadata();
         float ☃xxx = 0.0F;
         if (☃xx == 2) {
            ☃xxx = 180.0F;
         }

         if (☃xx == 4) {
            ☃xxx = 90.0F;
         }

         if (☃xx == 5) {
            ☃xxx = -90.0F;
         }

         GlStateManager.translate((float)☃ + 0.5F, (float)☃ + 0.5F, (float)☃ + 0.5F);
         GlStateManager.rotate(-☃xxx, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
         this.model.signStick.showModel = false;
      }

      if (☃ >= 0) {
         this.bindTexture(DESTROY_STAGES[☃]);
         GlStateManager.matrixMode(5890);
         GlStateManager.pushMatrix();
         GlStateManager.scale(4.0F, 2.0F, 1.0F);
         GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
         GlStateManager.matrixMode(5888);
      } else {
         this.bindTexture(SIGN_TEXTURE);
      }

      GlStateManager.enableRescaleNormal();
      GlStateManager.pushMatrix();
      GlStateManager.scale(0.6666667F, -0.6666667F, -0.6666667F);
      this.model.renderSign();
      GlStateManager.popMatrix();
      FontRenderer ☃xxxx = this.getFontRenderer();
      float ☃xxxxx = 0.010416667F;
      GlStateManager.translate(0.0F, 0.33333334F, 0.046666667F);
      GlStateManager.scale(0.010416667F, -0.010416667F, 0.010416667F);
      GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);
      GlStateManager.depthMask(false);
      int ☃xxxxxx = 0;
      if (☃ < 0) {
         for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃.signText.length; ☃xxxxxxx++) {
            if (☃.signText[☃xxxxxxx] != null) {
               ITextComponent ☃xxxxxxxx = ☃.signText[☃xxxxxxx];
               List<ITextComponent> ☃xxxxxxxxx = GuiUtilRenderComponents.splitText(☃xxxxxxxx, 90, ☃xxxx, false, true);
               String ☃xxxxxxxxxx = ☃xxxxxxxxx != null && !☃xxxxxxxxx.isEmpty() ? ☃xxxxxxxxx.get(0).getFormattedText() : "";
               if (☃xxxxxxx == ☃.lineBeingEdited) {
                  ☃xxxxxxxxxx = "> " + ☃xxxxxxxxxx + " <";
                  ☃xxxx.drawString(☃xxxxxxxxxx, -☃xxxx.getStringWidth(☃xxxxxxxxxx) / 2, ☃xxxxxxx * 10 - ☃.signText.length * 5, 0);
               } else {
                  ☃xxxx.drawString(☃xxxxxxxxxx, -☃xxxx.getStringWidth(☃xxxxxxxxxx) / 2, ☃xxxxxxx * 10 - ☃.signText.length * 5, 0);
               }
            }
         }
      }

      GlStateManager.depthMask(true);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.popMatrix();
      if (☃ >= 0) {
         GlStateManager.matrixMode(5890);
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5888);
      }
   }
}
