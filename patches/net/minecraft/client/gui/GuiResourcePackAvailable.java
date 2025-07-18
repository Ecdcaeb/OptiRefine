package net.minecraft.client.gui;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;

public class GuiResourcePackAvailable extends GuiResourcePackList {
   public GuiResourcePackAvailable(Minecraft var1, int var2, int var3, List<ResourcePackListEntry> var4) {
      super(☃, ☃, ☃, ☃);
   }

   @Override
   protected String getListHeader() {
      return I18n.format("resourcePack.available.title");
   }
}
