package net.minecraft.client.gui;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.util.text.TextFormatting;

public abstract class GuiResourcePackList extends GuiListExtended {
   protected final Minecraft mc;
   protected final List<ResourcePackListEntry> resourcePackEntries;

   public GuiResourcePackList(Minecraft var1, int var2, int var3, List<ResourcePackListEntry> var4) {
      super(☃, ☃, ☃, 32, ☃ - 55 + 4, 36);
      this.mc = ☃;
      this.resourcePackEntries = ☃;
      this.centerListVertically = false;
      this.setHasListHeader(true, (int)(☃.fontRenderer.FONT_HEIGHT * 1.5F));
   }

   @Override
   protected void drawListHeader(int var1, int var2, Tessellator var3) {
      String ☃ = TextFormatting.UNDERLINE + "" + TextFormatting.BOLD + this.getListHeader();
      this.mc.fontRenderer.drawString(☃, ☃ + this.width / 2 - this.mc.fontRenderer.getStringWidth(☃) / 2, Math.min(this.top + 3, ☃), 16777215);
   }

   protected abstract String getListHeader();

   public List<ResourcePackListEntry> getList() {
      return this.resourcePackEntries;
   }

   @Override
   protected int getSize() {
      return this.getList().size();
   }

   public ResourcePackListEntry getListEntry(int var1) {
      return this.getList().get(☃);
   }

   @Override
   public int getListWidth() {
      return this.width;
   }

   @Override
   protected int getScrollBarX() {
      return this.right - 6;
   }
}
