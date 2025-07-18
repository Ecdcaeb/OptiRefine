package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanDetected implements GuiListExtended.IGuiListEntry {
   private final GuiMultiplayer screen;
   protected final Minecraft mc;
   protected final LanServerInfo serverData;
   private long lastClickTime;

   protected ServerListEntryLanDetected(GuiMultiplayer var1, LanServerInfo var2) {
      this.screen = ☃;
      this.serverData = ☃;
      this.mc = Minecraft.getMinecraft();
   }

   @Override
   public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9) {
      this.mc.fontRenderer.drawString(I18n.format("lanServer.title"), ☃ + 32 + 3, ☃ + 1, 16777215);
      this.mc.fontRenderer.drawString(this.serverData.getServerMotd(), ☃ + 32 + 3, ☃ + 12, 8421504);
      if (this.mc.gameSettings.hideServerAddress) {
         this.mc.fontRenderer.drawString(I18n.format("selectServer.hiddenAddress"), ☃ + 32 + 3, ☃ + 12 + 11, 3158064);
      } else {
         this.mc.fontRenderer.drawString(this.serverData.getServerIpPort(), ☃ + 32 + 3, ☃ + 12 + 11, 3158064);
      }
   }

   @Override
   public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.screen.selectServer(☃);
      if (Minecraft.getSystemTime() - this.lastClickTime < 250L) {
         this.screen.connectToSelected();
      }

      this.lastClickTime = Minecraft.getSystemTime();
      return false;
   }

   @Override
   public void updatePosition(int var1, int var2, int var3, float var4) {
   }

   @Override
   public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   public LanServerInfo getServerData() {
      return this.serverData;
   }
}
