package net.minecraft.client.resources;

import net.minecraft.client.gui.GuiScreenResourcePacks;

public class ResourcePackListEntryFound extends ResourcePackListEntry {
   private final ResourcePackRepository.Entry resourcePackEntry;

   public ResourcePackListEntryFound(GuiScreenResourcePacks var1, ResourcePackRepository.Entry var2) {
      super(☃);
      this.resourcePackEntry = ☃;
   }

   @Override
   protected void bindResourcePackIcon() {
      this.resourcePackEntry.bindTexturePackIcon(this.mc.getTextureManager());
   }

   @Override
   protected int getResourcePackFormat() {
      return this.resourcePackEntry.getPackFormat();
   }

   @Override
   protected String getResourcePackDescription() {
      return this.resourcePackEntry.getTexturePackDescription();
   }

   @Override
   protected String getResourcePackName() {
      return this.resourcePackEntry.getResourcePackName();
   }

   public ResourcePackRepository.Entry getResourcePackEntry() {
      return this.resourcePackEntry;
   }
}
