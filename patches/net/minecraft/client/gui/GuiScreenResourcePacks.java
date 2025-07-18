package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.client.resources.ResourcePackListEntryDefault;
import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraft.client.resources.ResourcePackListEntryServer;
import net.minecraft.client.resources.ResourcePackRepository;

public class GuiScreenResourcePacks extends GuiScreen {
   private final GuiScreen parentScreen;
   private List<ResourcePackListEntry> availableResourcePacks;
   private List<ResourcePackListEntry> selectedResourcePacks;
   private GuiResourcePackAvailable availableResourcePacksList;
   private GuiResourcePackSelected selectedResourcePacksList;
   private boolean changed;

   public GuiScreenResourcePacks(GuiScreen var1) {
      this.parentScreen = ☃;
   }

   @Override
   public void initGui() {
      this.buttonList.add(new GuiOptionButton(2, this.width / 2 - 154, this.height - 48, I18n.format("resourcePack.openFolder")));
      this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 4, this.height - 48, I18n.format("gui.done")));
      if (!this.changed) {
         this.availableResourcePacks = Lists.newArrayList();
         this.selectedResourcePacks = Lists.newArrayList();
         ResourcePackRepository ☃ = this.mc.getResourcePackRepository();
         ☃.updateRepositoryEntriesAll();
         List<ResourcePackRepository.Entry> ☃x = Lists.newArrayList(☃.getRepositoryEntriesAll());
         ☃x.removeAll(☃.getRepositoryEntries());

         for (ResourcePackRepository.Entry ☃xx : ☃x) {
            this.availableResourcePacks.add(new ResourcePackListEntryFound(this, ☃xx));
         }

         ResourcePackRepository.Entry ☃xx = ☃.getResourcePackEntry();
         if (☃xx != null) {
            this.selectedResourcePacks.add(new ResourcePackListEntryServer(this, ☃.getServerResourcePack()));
         }

         for (ResourcePackRepository.Entry ☃xxx : Lists.reverse(☃.getRepositoryEntries())) {
            this.selectedResourcePacks.add(new ResourcePackListEntryFound(this, ☃xxx));
         }

         this.selectedResourcePacks.add(new ResourcePackListEntryDefault(this));
      }

      this.availableResourcePacksList = new GuiResourcePackAvailable(this.mc, 200, this.height, this.availableResourcePacks);
      this.availableResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
      this.availableResourcePacksList.registerScrollButtons(7, 8);
      this.selectedResourcePacksList = new GuiResourcePackSelected(this.mc, 200, this.height, this.selectedResourcePacks);
      this.selectedResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 + 4);
      this.selectedResourcePacksList.registerScrollButtons(7, 8);
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      this.selectedResourcePacksList.handleMouseInput();
      this.availableResourcePacksList.handleMouseInput();
   }

   public boolean hasResourcePackEntry(ResourcePackListEntry var1) {
      return this.selectedResourcePacks.contains(☃);
   }

   public List<ResourcePackListEntry> getListContaining(ResourcePackListEntry var1) {
      return this.hasResourcePackEntry(☃) ? this.selectedResourcePacks : this.availableResourcePacks;
   }

   public List<ResourcePackListEntry> getAvailableResourcePacks() {
      return this.availableResourcePacks;
   }

   public List<ResourcePackListEntry> getSelectedResourcePacks() {
      return this.selectedResourcePacks;
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         if (☃.id == 2) {
            File ☃ = this.mc.getResourcePackRepository().getDirResourcepacks();
            OpenGlHelper.openFile(☃);
         } else if (☃.id == 1) {
            if (this.changed) {
               List<ResourcePackRepository.Entry> ☃ = Lists.newArrayList();

               for (ResourcePackListEntry ☃x : this.selectedResourcePacks) {
                  if (☃x instanceof ResourcePackListEntryFound) {
                     ☃.add(((ResourcePackListEntryFound)☃x).getResourcePackEntry());
                  }
               }

               Collections.reverse(☃);
               this.mc.getResourcePackRepository().setRepositories(☃);
               this.mc.gameSettings.resourcePacks.clear();
               this.mc.gameSettings.incompatibleResourcePacks.clear();

               for (ResourcePackRepository.Entry ☃xx : ☃) {
                  this.mc.gameSettings.resourcePacks.add(☃xx.getResourcePackName());
                  if (☃xx.getPackFormat() != 3) {
                     this.mc.gameSettings.incompatibleResourcePacks.add(☃xx.getResourcePackName());
                  }
               }

               this.mc.gameSettings.saveOptions();
               this.mc.refreshResources();
            }

            this.mc.displayGuiScreen(this.parentScreen);
         }
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(☃, ☃, ☃);
      this.availableResourcePacksList.mouseClicked(☃, ☃, ☃);
      this.selectedResourcePacksList.mouseClicked(☃, ☃, ☃);
   }

   @Override
   protected void mouseReleased(int var1, int var2, int var3) {
      super.mouseReleased(☃, ☃, ☃);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawBackground(0);
      this.availableResourcePacksList.drawScreen(☃, ☃, ☃);
      this.selectedResourcePacksList.drawScreen(☃, ☃, ☃);
      this.drawCenteredString(this.fontRenderer, I18n.format("resourcePack.title"), this.width / 2, 16, 16777215);
      this.drawCenteredString(this.fontRenderer, I18n.format("resourcePack.folderInfo"), this.width / 2 - 77, this.height - 26, 8421504);
      super.drawScreen(☃, ☃, ☃);
   }

   public void markChanged() {
      this.changed = true;
   }
}
