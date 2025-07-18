package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;

public class GuiErrorScreen extends GuiScreen {
   private final String title;
   private final String message;

   public GuiErrorScreen(String var1, String var2) {
      this.title = ☃;
      this.message = ☃;
   }

   @Override
   public void initGui() {
      super.initGui();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 140, I18n.format("gui.cancel")));
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawGradientRect(0, 0, this.width, this.height, -12574688, -11530224);
      this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 90, 16777215);
      this.drawCenteredString(this.fontRenderer, this.message, this.width / 2, 110, 16777215);
      super.drawScreen(☃, ☃, ☃);
   }

   @Override
   protected void keyTyped(char var1, int var2) {
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      this.mc.displayGuiScreen(null);
   }
}
