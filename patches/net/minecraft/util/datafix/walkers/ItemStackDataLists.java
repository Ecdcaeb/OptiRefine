package net.minecraft.util.datafix.walkers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.IDataFixer;

public class ItemStackDataLists extends Filtered {
   private final String[] matchingTags;

   public ItemStackDataLists(Class<?> var1, String... var2) {
      super(☃);
      this.matchingTags = ☃;
   }

   @Override
   NBTTagCompound filteredProcess(IDataFixer var1, NBTTagCompound var2, int var3) {
      for (String ☃ : this.matchingTags) {
         ☃ = DataFixesManager.processInventory(☃, ☃, ☃, ☃);
      }

      return ☃;
   }
}
