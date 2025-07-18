package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.resources.I18n;

public class GuiYesNo extends GuiScreen {
   protected GuiYesNoCallback parentScreen;
   protected String messageLine1;
   private final String messageLine2;
   private final List<String> listLines = Lists.newArrayList();
   protected String confirmButtonText;
   protected String cancelButtonText;
   protected int parentButtonClickedId;
   private int ticksUntilEnable;

   public GuiYesNo(GuiYesNoCallback var1, String var2, String var3, int var4) {
      this.parentScreen = ☃;
      this.messageLine1 = ☃;
      this.messageLine2 = ☃;
      this.parentButtonClickedId = ☃;
      this.confirmButtonText = I18n.format("gui.yes");
      this.cancelButtonText = I18n.format("gui.no");
   }

   public GuiYesNo(GuiYesNoCallback var1, String var2, String var3, String var4, String var5, int var6) {
      this.parentScreen = ☃;
      this.messageLine1 = ☃;
      this.messageLine2 = ☃;
      this.confirmButtonText = ☃;
      this.cancelButtonText = ☃;
      this.parentButtonClickedId = ☃;
   }

   @Override
   public void initGui() {
      this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 155, this.height / 6 + 96, this.confirmButtonText));
      this.buttonList.add(new GuiOptionButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.cancelButtonText));
      this.listLines.clear();
      this.listLines.addAll(this.fontRenderer.listFormattedStringToWidth(this.messageLine2, this.width - 50));
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      this.parentScreen.confirmClicked(☃.id == 0, this.parentButtonClickedId);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, this.messageLine1, this.width / 2, 70, 16777215);
      int ☃ = 90;

      for (String ☃x : this.listLines) {
         this.drawCenteredString(this.fontRenderer, ☃x, this.width / 2, ☃, 16777215);
         ☃ += this.fontRenderer.FONT_HEIGHT;
      }

      super.drawScreen(☃, ☃, ☃);
   }

   public void setButtonDelay(int var1) {
      this.ticksUntilEnable = ☃;

      for (GuiButton ☃ : this.buttonList) {
         ☃.enabled = false;
      }
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      if (--this.ticksUntilEnable == 0) {
         for (GuiButton ☃ : this.buttonList) {
            ☃.enabled = true;
         }
      }
   }
}
