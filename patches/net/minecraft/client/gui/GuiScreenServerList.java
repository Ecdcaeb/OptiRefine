package net.minecraft.client.gui;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiScreenServerList extends GuiScreen {
   private final GuiScreen lastScreen;
   private final ServerData serverData;
   private GuiTextField ipEdit;

   public GuiScreenServerList(GuiScreen var1, ServerData var2) {
      this.lastScreen = ☃;
      this.serverData = ☃;
   }

   @Override
   public void updateScreen() {
      this.ipEdit.updateCursorCounter();
   }

   @Override
   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, I18n.format("selectServer.select")));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel")));
      this.ipEdit = new GuiTextField(2, this.fontRenderer, this.width / 2 - 100, 116, 200, 20);
      this.ipEdit.setMaxStringLength(128);
      this.ipEdit.setFocused(true);
      this.ipEdit.setText(this.mc.gameSettings.lastServer);
      this.buttonList.get(0).enabled = !this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0;
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.mc.gameSettings.lastServer = this.ipEdit.getText();
      this.mc.gameSettings.saveOptions();
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         if (☃.id == 1) {
            this.lastScreen.confirmClicked(false, 0);
         } else if (☃.id == 0) {
            this.serverData.serverIP = this.ipEdit.getText();
            this.lastScreen.confirmClicked(true, 0);
         }
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (this.ipEdit.textboxKeyTyped(☃, ☃)) {
         this.buttonList.get(0).enabled = !this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0;
      } else if (☃ == 28 || ☃ == 156) {
         this.actionPerformed(this.buttonList.get(0));
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(☃, ☃, ☃);
      this.ipEdit.mouseClicked(☃, ☃, ☃);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, I18n.format("selectServer.direct"), this.width / 2, 20, 16777215);
      this.drawString(this.fontRenderer, I18n.format("addServer.enterIp"), this.width / 2 - 100, 100, 10526880);
      this.ipEdit.drawTextBox();
      super.drawScreen(☃, ☃, ☃);
   }
}
