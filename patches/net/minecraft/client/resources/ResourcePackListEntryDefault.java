package net.minecraft.client.resources;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenResourcePacks;

public class ResourcePackListEntryDefault extends ResourcePackListEntryServer {
   public ResourcePackListEntryDefault(GuiScreenResourcePacks var1) {
      super(☃, Minecraft.getMinecraft().getResourcePackRepository().rprDefaultResourcePack);
   }

   @Override
   protected String getResourcePackName() {
      return "Default";
   }

   @Override
   public boolean isServerPack() {
      return false;
   }
}
