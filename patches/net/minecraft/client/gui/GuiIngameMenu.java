package net.minecraft.client.gui;

import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;

public class GuiIngameMenu extends GuiScreen {
   private int saveStep;
   private int visibleTime;

   @Override
   public void initGui() {
      this.saveStep = 0;
      this.buttonList.clear();
      int ☃ = -16;
      int ☃x = 98;
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + -16, I18n.format("menu.returnToMenu")));
      if (!this.mc.isIntegratedServerRunning()) {
         this.buttonList.get(0).displayString = I18n.format("menu.disconnect");
      }

      this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + -16, I18n.format("menu.returnToGame")));
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20, I18n.format("menu.options")));
      GuiButton ☃xx = this.addButton(new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + -16, 98, 20, I18n.format("menu.shareToLan")));
      ☃xx.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
      this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + -16, 98, 20, I18n.format("gui.advancements")));
      this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + -16, 98, 20, I18n.format("gui.stats")));
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      switch (☃.id) {
         case 0:
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
            break;
         case 1:
            boolean ☃ = this.mc.isIntegratedServerRunning();
            boolean ☃x = this.mc.isConnectedToRealms();
            ☃.enabled = false;
            this.mc.world.sendQuittingDisconnectingPacket();
            this.mc.loadWorld(null);
            if (☃) {
               this.mc.displayGuiScreen(new GuiMainMenu());
            } else if (☃x) {
               RealmsBridge ☃xx = new RealmsBridge();
               ☃xx.switchToRealms(new GuiMainMenu());
            } else {
               this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            }
         case 2:
         case 3:
         default:
            break;
         case 4:
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
            break;
         case 5:
            this.mc.displayGuiScreen(new GuiScreenAdvancements(this.mc.player.connection.getAdvancementManager()));
            break;
         case 6:
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
            break;
         case 7:
            this.mc.displayGuiScreen(new GuiShareToLan(this));
      }
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      this.visibleTime++;
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, I18n.format("menu.game"), this.width / 2, 40, 16777215);
      super.drawScreen(☃, ☃, ☃);
   }
}
