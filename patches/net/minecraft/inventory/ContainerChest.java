package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerChest extends Container {
   private final IInventory lowerChestInventory;
   private final int numRows;

   public ContainerChest(IInventory var1, IInventory var2, EntityPlayer var3) {
      this.lowerChestInventory = ☃;
      this.numRows = ☃.getSizeInventory() / 9;
      ☃.openInventory(☃);
      int ☃ = (this.numRows - 4) * 18;

      for (int ☃x = 0; ☃x < this.numRows; ☃x++) {
         for (int ☃xx = 0; ☃xx < 9; ☃xx++) {
            this.addSlotToContainer(new Slot(☃, ☃xx + ☃x * 9, 8 + ☃xx * 18, 18 + ☃x * 18));
         }
      }

      for (int ☃x = 0; ☃x < 3; ☃x++) {
         for (int ☃xx = 0; ☃xx < 9; ☃xx++) {
            this.addSlotToContainer(new Slot(☃, ☃xx + ☃x * 9 + 9, 8 + ☃xx * 18, 103 + ☃x * 18 + ☃));
         }
      }

      for (int ☃x = 0; ☃x < 9; ☃x++) {
         this.addSlotToContainer(new Slot(☃, ☃x, 8 + ☃x * 18, 161 + ☃));
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.lowerChestInventory.isUsableByPlayer(☃);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack ☃ = ItemStack.EMPTY;
      Slot ☃x = this.inventorySlots.get(☃);
      if (☃x != null && ☃x.getHasStack()) {
         ItemStack ☃xx = ☃x.getStack();
         ☃ = ☃xx.copy();
         if (☃ < this.numRows * 9) {
            if (!this.mergeItemStack(☃xx, this.numRows * 9, this.inventorySlots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.mergeItemStack(☃xx, 0, this.numRows * 9, false)) {
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
      this.lowerChestInventory.closeInventory(☃);
   }

   public IInventory getLowerChestInventory() {
      return this.lowerChestInventory;
   }
}
