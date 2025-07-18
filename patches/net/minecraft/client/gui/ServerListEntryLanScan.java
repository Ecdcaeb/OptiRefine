package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry {
   private final Minecraft mc = Minecraft.getMinecraft();

   @Override
   public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9) {
      int ☃ = ☃ + ☃ / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2;
      this.mc
         .fontRenderer
         .drawString(
            I18n.format("lanServer.scanning"),
            this.mc.currentScreen.width / 2 - this.mc.fontRenderer.getStringWidth(I18n.format("lanServer.scanning")) / 2,
            ☃,
            16777215
         );
      String ☃x;
      switch ((int)(Minecraft.getSystemTime() / 300L % 4L)) {
         case 0:
         default:
            ☃x = "O o o";
            break;
         case 1:
         case 3:
            ☃x = "o O o";
            break;
         case 2:
            ☃x = "o o O";
      }

      this.mc
         .fontRenderer
         .drawString(☃x, this.mc.currentScreen.width / 2 - this.mc.fontRenderer.getStringWidth(☃x) / 2, ☃ + this.mc.fontRenderer.FONT_HEIGHT, 8421504);
   }

   @Override
   public void updatePosition(int var1, int var2, int var3, float var4) {
   }

   @Override
   public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
      return false;
   }

   @Override
   public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
   }
}
