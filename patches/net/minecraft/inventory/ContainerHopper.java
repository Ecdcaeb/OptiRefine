package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerHopper extends Container {
   private final IInventory hopperInventory;

   public ContainerHopper(InventoryPlayer var1, IInventory var2, EntityPlayer var3) {
      this.hopperInventory = ☃;
      ☃.openInventory(☃);
      int ☃ = 51;

      for (int ☃x = 0; ☃x < ☃.getSizeInventory(); ☃x++) {
         this.addSlotToContainer(new Slot(☃, ☃x, 44 + ☃x * 18, 20));
      }

      for (int ☃x = 0; ☃x < 3; ☃x++) {
         for (int ☃xx = 0; ☃xx < 9; ☃xx++) {
            this.addSlotToContainer(new Slot(☃, ☃xx + ☃x * 9 + 9, 8 + ☃xx * 18, ☃x * 18 + 51));
         }
      }

      for (int ☃x = 0; ☃x < 9; ☃x++) {
         this.addSlotToContainer(new Slot(☃, ☃x, 8 + ☃x * 18, 109));
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.hopperInventory.isUsableByPlayer(☃);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack ☃ = ItemStack.EMPTY;
      Slot ☃x = this.inventorySlots.get(☃);
      if (☃x != null && ☃x.getHasStack()) {
         ItemStack ☃xx = ☃x.getStack();
         ☃ = ☃xx.copy();
         if (☃ < this.hopperInventory.getSizeInventory()) {
            if (!this.mergeItemStack(☃xx, this.hopperInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.mergeItemStack(☃xx, 0, this.hopperInventory.getSizeInventory(), false)) {
            return ItemStack.EMPTY;
         }

         if (☃xx.isEmpty()) {
            ☃x.putStack(ItemStack.EMPTY);
         } else {
            ☃x.onSlotChanged();
         }
      }

      return ☃;
   }

   @Override
   public void onContainerClosed(EntityPlayer var1) {
      super.onContainerClosed(☃);
      this.hopperInventory.closeInventory(☃);
   }
}
