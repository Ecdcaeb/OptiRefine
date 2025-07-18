package net.minecraft.client.gui;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

public class GuiWorldEdit extends GuiScreen {
   private final GuiScreen lastScreen;
   private GuiTextField nameEdit;
   private final String worldId;

   public GuiWorldEdit(GuiScreen var1, String var2) {
      this.lastScreen = ☃;
      this.worldId = ☃;
   }

   @Override
   public void updateScreen() {
      this.nameEdit.updateCursorCounter();
   }

   @Override
   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      GuiButton ☃ = this.addButton(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 24 + 12, I18n.format("selectWorld.edit.resetIcon")));
      this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 48 + 12, I18n.format("selectWorld.edit.openFolder")));
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, I18n.format("selectWorld.edit.save")));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel")));
      ☃.enabled = this.mc.getSaveLoader().getFile(this.worldId, "icon.png").isFile();
      ISaveFormat ☃x = this.mc.getSaveLoader();
      WorldInfo ☃xx = ☃x.getWorldInfo(this.worldId);
      String ☃xxx = ☃xx == null ? "" : ☃xx.getWorldName();
      this.nameEdit = new GuiTextField(2, this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
      this.nameEdit.setFocused(true);
      this.nameEdit.setText(☃xxx);
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         if (☃.id == 1) {
            this.mc.displayGuiScreen(this.lastScreen);
         } else if (☃.id == 0) {
            ISaveFormat ☃ = this.mc.getSaveLoader();
            ☃.renameWorld(this.worldId, this.nameEdit.getText().trim());
            this.mc.displayGuiScreen(this.lastScreen);
         } else if (☃.id == 3) {
            ISaveFormat ☃ = this.mc.getSaveLoader();
            FileUtils.deleteQuietly(☃.getFile(this.worldId, "icon.png"));
            ☃.enabled = false;
         } else if (☃.id == 4) {
            ISaveFormat ☃ = this.mc.getSaveLoader();
            OpenGlHelper.openFile(☃.getFile(this.worldId, "icon.png").getParentFile());
         }
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      this.nameEdit.textboxKeyTyped(☃, ☃);
      this.buttonList.get(2).enabled = !this.nameEdit.getText().trim().isEmpty();
      if (☃ == 28 || ☃ == 156) {
         this.actionPerformed(this.buttonList.get(2));
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(☃, ☃, ☃);
      this.nameEdit.mouseClicked(☃, ☃, ☃);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, I18n.format("selectWorld.edit.title"), this.width / 2, 20, 16777215);
      this.drawString(this.fontRenderer, I18n.format("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
      this.nameEdit.drawTextBox();
      super.drawScreen(☃, ☃, ☃);
   }
}
