package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerShulkerBox extends Container {
   private final IInventory inventory;

   public ContainerShulkerBox(InventoryPlayer var1, IInventory var2, EntityPlayer var3) {
      this.inventory = ☃;
      ☃.openInventory(☃);
      int ☃ = 3;
      int ☃x = 9;

      for (int ☃xx = 0; ☃xx < 3; ☃xx++) {
         for (int ☃xxx = 0; ☃xxx < 9; ☃xxx++) {
            this.addSlotToContainer(new SlotShulkerBox(☃, ☃xxx + ☃xx * 9, 8 + ☃xxx * 18, 18 + ☃xx * 18));
         }
      }

      for (int ☃xx = 0; ☃xx < 3; ☃xx++) {
         for (int ☃xxx = 0; ☃xxx < 9; ☃xxx++) {
            this.addSlotToContainer(new Slot(☃, ☃xxx + ☃xx * 9 + 9, 8 + ☃xxx * 18, 84 + ☃xx * 18));
         }
      }

      for (int ☃xx = 0; ☃xx < 9; ☃xx++) {
         this.addSlotToContainer(new Slot(☃, ☃xx, 8 + ☃xx * 18, 142));
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.inventory.isUsableByPlayer(☃);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack ☃ = ItemStack.EMPTY;
      Slot ☃x = this.inventorySlots.get(☃);
      if (☃x != null && ☃x.getHasStack()) {
         ItemStack ☃xx = ☃x.getStack();
         ☃ = ☃xx.copy();
         if (☃ < this.inventory.getSizeInventory()) {
            if (!this.mergeItemStack(☃xx, this.inventory.getSizeInventory(), this.inventorySlots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.mergeItemStack(☃xx, 0, this.inventory.getSizeInventory(), false)) {
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
      this.inventory.closeInventory(☃);
   }
}
