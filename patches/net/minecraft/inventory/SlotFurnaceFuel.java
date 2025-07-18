package net.minecraft.inventory;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class SlotFurnaceFuel extends Slot {
   public SlotFurnaceFuel(IInventory var1, int var2, int var3, int var4) {
      super(☃, ☃, ☃, ☃);
   }

   @Override
   public boolean isItemValid(ItemStack var1) {
      return TileEntityFurnace.isItemFuel(☃) || isBucket(☃);
   }

   @Override
   public int getItemStackLimit(ItemStack var1) {
      return isBucket(☃) ? 1 : super.getItemStackLimit(☃);
   }

   public static boolean isBucket(ItemStack var0) {
      return ☃.getItem() == Items.BUCKET;
   }
}
