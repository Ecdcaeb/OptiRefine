package net.minecraft.inventory;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class ItemStackHelper {
   public static ItemStack getAndSplit(List<ItemStack> var0, int var1, int var2) {
      return ☃ >= 0 && ☃ < ☃.size() && !☃.get(☃).isEmpty() && ☃ > 0 ? ☃.get(☃).splitStack(☃) : ItemStack.EMPTY;
   }

   public static ItemStack getAndRemove(List<ItemStack> var0, int var1) {
      return ☃ >= 0 && ☃ < ☃.size() ? ☃.set(☃, ItemStack.EMPTY) : ItemStack.EMPTY;
   }

   public static NBTTagCompound saveAllItems(NBTTagCompound var0, NonNullList<ItemStack> var1) {
      return saveAllItems(☃, ☃, true);
   }

   public static NBTTagCompound saveAllItems(NBTTagCompound var0, NonNullList<ItemStack> var1, boolean var2) {
      NBTTagList ☃ = new NBTTagList();

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         ItemStack ☃xx = ☃.get(☃x);
         if (!☃xx.isEmpty()) {
            NBTTagCompound ☃xxx = new NBTTagCompound();
            ☃xxx.setByte("Slot", (byte)☃x);
            ☃xx.writeToNBT(☃xxx);
            ☃.appendTag(☃xxx);
         }
      }

      if (!☃.isEmpty() || ☃) {
         ☃.setTag("Items", ☃);
      }

      return ☃;
   }

   public static void loadAllItems(NBTTagCompound var0, NonNullList<ItemStack> var1) {
      NBTTagList ☃ = ☃.getTagList("Items", 10);

      for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
         NBTTagCompound ☃xx = ☃.getCompoundTagAt(☃x);
         int ☃xxx = ☃xx.getByte("Slot") & 255;
         if (☃xxx >= 0 && ☃xxx < ☃.size()) {
            ☃.set(☃xxx, new ItemStack(☃xx));
         }
      }
   }
}
