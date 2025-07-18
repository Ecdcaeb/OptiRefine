package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiLabel extends Gui {
   protected int width = 200;
   protected int height = 20;
   public int x;
   public int y;
   private final List<String> labels;
   public int id;
   private boolean centered;
   public boolean visible = true;
   private boolean labelBgEnabled;
   private final int textColor;
   private int backColor;
   private int ulColor;
   private int brColor;
   private final FontRenderer fontRenderer;
   private int border;

   public GuiLabel(FontRenderer var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.fontRenderer = ☃;
      this.id = ☃;
      this.x = ☃;
      this.y = ☃;
      this.width = ☃;
      this.height = ☃;
      this.labels = Lists.newArrayList();
      this.centered = false;
      this.labelBgEnabled = false;
      this.textColor = ☃;
      this.backColor = -1;
      this.ulColor = -1;
      this.brColor = -1;
      this.border = 0;
   }

   public void addLine(String var1) {
      this.labels.add(I18n.format(☃));
   }

   public GuiLabel setCentered() {
      this.centered = true;
      return this;
   }

   public void drawLabel(Minecraft var1, int var2, int var3) {
      if (this.visible) {
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
         this.drawLabelBackground(☃, ☃, ☃);
         int ☃ = this.y + this.height / 2 + this.border / 2;
         int ☃x = ☃ - this.labels.size() * 10 / 2;

         for (int ☃xx = 0; ☃xx < this.labels.size(); ☃xx++) {
            if (this.centered) {
               this.drawCenteredString(this.fontRenderer, this.labels.get(☃xx), this.x + this.width / 2, ☃x + ☃xx * 10, this.textColor);
            } else {
               this.drawString(this.fontRenderer, this.labels.get(☃xx), this.x, ☃x + ☃xx * 10, this.textColor);
            }
         }
      }
   }

   protected void drawLabelBackground(Minecraft var1, int var2, int var3) {
      if (this.labelBgEnabled) {
         int ☃ = this.width + this.border * 2;
         int ☃x = this.height + this.border * 2;
         int ☃xx = this.x - this.border;
         int ☃xxx = this.y - this.border;
         drawRect(☃xx, ☃xxx, ☃xx + ☃, ☃xxx + ☃x, this.backColor);
         this.drawHorizontalLine(☃xx, ☃xx + ☃, ☃xxx, this.ulColor);
         this.drawHorizontalLine(☃xx, ☃xx + ☃, ☃xxx + ☃x, this.brColor);
         this.drawVerticalLine(☃xx, ☃xxx, ☃xxx + ☃x, this.ulColor);
         this.drawVerticalLine(☃xx + ☃, ☃xxx, ☃xxx + ☃x, this.brColor);
      }
   }
}
