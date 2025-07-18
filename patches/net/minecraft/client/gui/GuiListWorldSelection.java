package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiListWorldSelection extends GuiListExtended {
   private static final Logger LOGGER = LogManager.getLogger();
   private final GuiWorldSelection worldSelection;
   private final List<GuiListWorldSelectionEntry> entries = Lists.newArrayList();
   private int selectedIdx = -1;

   public GuiListWorldSelection(GuiWorldSelection var1, Minecraft var2, int var3, int var4, int var5, int var6, int var7) {
      super(☃, ☃, ☃, ☃, ☃, ☃);
      this.worldSelection = ☃;
      this.refreshList();
   }

   public void refreshList() {
      ISaveFormat ☃ = this.mc.getSaveLoader();

      List<WorldSummary> ☃x;
      try {
         ☃x = ☃.getSaveList();
      } catch (AnvilConverterException var5) {
         LOGGER.error("Couldn't load level list", var5);
         this.mc.displayGuiScreen(new GuiErrorScreen(I18n.format("selectWorld.unable_to_load"), var5.getMessage()));
         return;
      }

      Collections.sort(☃x);

      for (WorldSummary ☃xx : ☃x) {
         this.entries.add(new GuiListWorldSelectionEntry(this, ☃xx, this.mc.getSaveLoader()));
      }
   }

   public GuiListWorldSelectionEntry getListEntry(int var1) {
      return this.entries.get(☃);
   }

   @Override
   protected int getSize() {
      return this.entries.size();
   }

   @Override
   protected int getScrollBarX() {
      return super.getScrollBarX() + 20;
   }

   @Override
   public int getListWidth() {
      return super.getListWidth() + 50;
   }

   public void selectWorld(int var1) {
      this.selectedIdx = ☃;
      this.worldSelection.selectWorld(this.getSelectedWorld());
   }

   @Override
   protected boolean isSelected(int var1) {
      return ☃ == this.selectedIdx;
   }

   @Nullable
   public GuiListWorldSelectionEntry getSelectedWorld() {
      return this.selectedIdx >= 0 && this.selectedIdx < this.getSize() ? this.getListEntry(this.selectedIdx) : null;
   }

   public GuiWorldSelection getGuiWorldSelection() {
      return this.worldSelection;
   }
}
