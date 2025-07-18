package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import java.net.IDN;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;

public class GuiScreenAddServer extends GuiScreen {
   private final GuiScreen parentScreen;
   private final ServerData serverData;
   private GuiTextField serverIPField;
   private GuiTextField serverNameField;
   private GuiButton serverResourcePacks;
   private final Predicate<String> addressFilter = new Predicate<String>() {
      public boolean apply(@Nullable String var1) {
         if (StringUtils.isNullOrEmpty(☃)) {
            return true;
         } else {
            String[] ☃ = ☃.split(":");
            if (☃.length == 0) {
               return true;
            } else {
               try {
                  String ☃x = IDN.toASCII(☃[0]);
                  return true;
               } catch (IllegalArgumentException var4) {
                  return false;
               }
            }
         }
      }
   };

   public GuiScreenAddServer(GuiScreen var1, ServerData var2) {
      this.parentScreen = ☃;
      this.serverData = ☃;
   }

   @Override
   public void updateScreen() {
      this.serverNameField.updateCursorCounter();
      this.serverIPField.updateCursorCounter();
   }

   @Override
   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 18, I18n.format("addServer.add")));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 18, I18n.format("gui.cancel")));
      this.serverResourcePacks = this.addButton(
         new GuiButton(
            2,
            this.width / 2 - 100,
            this.height / 4 + 72,
            I18n.format("addServer.resourcePack") + ": " + this.serverData.getResourceMode().getMotd().getFormattedText()
         )
      );
      this.serverNameField = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, 66, 200, 20);
      this.serverNameField.setFocused(true);
      this.serverNameField.setText(this.serverData.serverName);
      this.serverIPField = new GuiTextField(1, this.fontRenderer, this.width / 2 - 100, 106, 200, 20);
      this.serverIPField.setMaxStringLength(128);
      this.serverIPField.setText(this.serverData.serverIP);
      this.serverIPField.setValidator(this.addressFilter);
      this.buttonList.get(0).enabled = !this.serverIPField.getText().isEmpty()
         && this.serverIPField.getText().split(":").length > 0
         && !this.serverNameField.getText().isEmpty();
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         if (☃.id == 2) {
            this.serverData
               .setResourceMode(
                  ServerData.ServerResourceMode.values()[(this.serverData.getResourceMode().ordinal() + 1) % ServerData.ServerResourceMode.values().length]
               );
            this.serverResourcePacks.displayString = I18n.format("addServer.resourcePack")
               + ": "
               + this.serverData.getResourceMode().getMotd().getFormattedText();
         } else if (☃.id == 1) {
            this.parentScreen.confirmClicked(false, 0);
         } else if (☃.id == 0) {
            this.serverData.serverName = this.serverNameField.getText();
            this.serverData.serverIP = this.serverIPField.getText();
            this.parentScreen.confirmClicked(true, 0);
         }
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      this.serverNameField.textboxKeyTyped(☃, ☃);
      this.serverIPField.textboxKeyTyped(☃, ☃);
      if (☃ == 15) {
         this.serverNameField.setFocused(!this.serverNameField.isFocused());
         this.serverIPField.setFocused(!this.serverIPField.isFocused());
      }

      if (☃ == 28 || ☃ == 156) {
         this.actionPerformed(this.buttonList.get(0));
      }

      this.buttonList.get(0).enabled = !this.serverIPField.getText().isEmpty()
         && this.serverIPField.getText().split(":").length > 0
         && !this.serverNameField.getText().isEmpty();
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(☃, ☃, ☃);
      this.serverIPField.mouseClicked(☃, ☃, ☃);
      this.serverNameField.mouseClicked(☃, ☃, ☃);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, I18n.format("addServer.title"), this.width / 2, 17, 16777215);
      this.drawString(this.fontRenderer, I18n.format("addServer.enterName"), this.width / 2 - 100, 53, 10526880);
      this.drawString(this.fontRenderer, I18n.format("addServer.enterIp"), this.width / 2 - 100, 94, 10526880);
      this.serverNameField.drawTextBox();
      this.serverIPField.drawTextBox();
      super.drawScreen(☃, ☃, ☃);
   }
}
