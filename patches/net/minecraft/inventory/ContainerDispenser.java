package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerDispenser extends Container {
   private final IInventory dispenserInventory;

   public ContainerDispenser(IInventory var1, IInventory var2) {
      this.dispenserInventory = ☃;

      for (int ☃ = 0; ☃ < 3; ☃++) {
         for (int ☃x = 0; ☃x < 3; ☃x++) {
            this.addSlotToContainer(new Slot(☃, ☃x + ☃ * 3, 62 + ☃x * 18, 17 + ☃ * 18));
         }
      }

      for (int ☃ = 0; ☃ < 3; ☃++) {
         for (int ☃x = 0; ☃x < 9; ☃x++) {
            this.addSlotToContainer(new Slot(☃, ☃x + ☃ * 9 + 9, 8 + ☃x * 18, 84 + ☃ * 18));
         }
      }

      for (int ☃ = 0; ☃ < 9; ☃++) {
         this.addSlotToContainer(new Slot(☃, ☃, 8 + ☃ * 18, 142));
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.dispenserInventory.isUsableByPlayer(☃);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack ☃ = ItemStack.EMPTY;
      Slot ☃x = this.inventorySlots.get(☃);
      if (☃x != null && ☃x.getHasStack()) {
         ItemStack ☃xx = ☃x.getStack();
         ☃ = ☃xx.copy();
         if (☃ < 9) {
            if (!this.mergeItemStack(☃xx, 9, 45, true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.mergeItemStack(☃xx, 0, 9, false)) {
            return ItemStack.EMPTY;
         }

         if (☃xx.isEmpty()) {
            ☃x.putStack(ItemStack.EMPTY);
         } else {
            ☃x.onSlotChanged();
         }

         if (☃xx.getCount() == ☃.getCount()) {
            return ItemStack.EMPTY;
         }

         ☃x.onTake(☃, ☃xx);
      }

      return ☃;
   }
}
