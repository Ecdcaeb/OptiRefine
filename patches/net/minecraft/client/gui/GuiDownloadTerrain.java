package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;

public class GuiDownloadTerrain extends GuiScreen {
   @Override
   public void initGui() {
      this.buttonList.clear();
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawBackground(0);
      this.drawCenteredString(this.fontRenderer, I18n.format("multiplayer.downloadingTerrain"), this.width / 2, this.height / 2 - 50, 16777215);
      super.drawScreen(☃, ☃, ☃);
   }

   @Override
   public boolean doesGuiPauseGame() {
      return false;
   }
}
