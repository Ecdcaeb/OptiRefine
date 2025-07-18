package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiWorldSelection extends GuiScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   protected GuiScreen prevScreen;
   protected String title = "Select world";
   private String worldVersTooltip;
   private GuiButton deleteButton;
   private GuiButton selectButton;
   private GuiButton renameButton;
   private GuiButton copyButton;
   private GuiListWorldSelection selectionList;

   public GuiWorldSelection(GuiScreen var1) {
      this.prevScreen = ☃;
   }

   @Override
   public void initGui() {
      this.title = I18n.format("selectWorld.title");
      this.selectionList = new GuiListWorldSelection(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
      this.postInit();
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      this.selectionList.handleMouseInput();
   }

   public void postInit() {
      this.selectButton = this.addButton(new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20, I18n.format("selectWorld.select")));
      this.addButton(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20, I18n.format("selectWorld.create")));
      this.renameButton = this.addButton(new GuiButton(4, this.width / 2 - 154, this.height - 28, 72, 20, I18n.format("selectWorld.edit")));
      this.deleteButton = this.addButton(new GuiButton(2, this.width / 2 - 76, this.height - 28, 72, 20, I18n.format("selectWorld.delete")));
      this.copyButton = this.addButton(new GuiButton(5, this.width / 2 + 4, this.height - 28, 72, 20, I18n.format("selectWorld.recreate")));
      this.addButton(new GuiButton(0, this.width / 2 + 82, this.height - 28, 72, 20, I18n.format("gui.cancel")));
      this.selectButton.enabled = false;
      this.deleteButton.enabled = false;
      this.renameButton.enabled = false;
      this.copyButton.enabled = false;
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         GuiListWorldSelectionEntry ☃ = this.selectionList.getSelectedWorld();
         if (☃.id == 2) {
            if (☃ != null) {
               ☃.deleteWorld();
            }
         } else if (☃.id == 1) {
            if (☃ != null) {
               ☃.joinWorld();
            }
         } else if (☃.id == 3) {
            this.mc.displayGuiScreen(new GuiCreateWorld(this));
         } else if (☃.id == 4) {
            if (☃ != null) {
               ☃.editWorld();
            }
         } else if (☃.id == 0) {
            this.mc.displayGuiScreen(this.prevScreen);
         } else if (☃.id == 5 && ☃ != null) {
            ☃.recreateWorld();
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.worldVersTooltip = null;
      this.selectionList.drawScreen(☃, ☃, ☃);
      this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
      super.drawScreen(☃, ☃, ☃);
      if (this.worldVersTooltip != null) {
         this.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.worldVersTooltip)), ☃, ☃);
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(☃, ☃, ☃);
      this.selectionList.mouseClicked(☃, ☃, ☃);
   }

   @Override
   protected void mouseReleased(int var1, int var2, int var3) {
      super.mouseReleased(☃, ☃, ☃);
      this.selectionList.mouseReleased(☃, ☃, ☃);
   }

   public void setVersionTooltip(String var1) {
      this.worldVersTooltip = ☃;
   }

   public void selectWorld(@Nullable GuiListWorldSelectionEntry var1) {
      boolean ☃ = ☃ != null;
      this.selectButton.enabled = ☃;
      this.deleteButton.enabled = ☃;
      this.renameButton.enabled = ☃;
      this.copyButton.enabled = ☃;
   }
}
