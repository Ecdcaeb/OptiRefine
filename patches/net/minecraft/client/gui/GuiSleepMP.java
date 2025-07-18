package net.minecraft.client.gui;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.CPacketEntityAction;

public class GuiSleepMP extends GuiChat {
   @Override
   public void initGui() {
      super.initGui();
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, I18n.format("multiplayer.stopSleeping")));
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (☃ == 1) {
         this.wakeFromSleep();
      } else if (☃ != 28 && ☃ != 156) {
         super.keyTyped(☃, ☃);
      } else {
         String ☃ = this.inputField.getText().trim();
         if (!☃.isEmpty()) {
            this.mc.player.sendChatMessage(☃);
         }

         this.inputField.setText("");
         this.mc.ingameGUI.getChatGUI().resetScroll();
      }
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.id == 1) {
         this.wakeFromSleep();
      } else {
         super.actionPerformed(☃);
      }
   }

   private void wakeFromSleep() {
      NetHandlerPlayClient ☃ = this.mc.player.connection;
      ☃.sendPacket(new CPacketEntityAction(this.mc.player, CPacketEntityAction.Action.STOP_SLEEPING));
   }
}
