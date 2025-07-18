package net.minecraft.inventory;

import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ContainerHorseInventory extends Container {
   private final IInventory horseInventory;
   private final AbstractHorse horse;

   public ContainerHorseInventory(IInventory var1, IInventory var2, final AbstractHorse var3, EntityPlayer var4) {
      this.horseInventory = ☃;
      this.horse = ☃;
      int ☃ = 3;
      ☃.openInventory(☃);
      int ☃x = -18;
      this.addSlotToContainer(new Slot(☃, 0, 8, 18) {
         @Override
         public boolean isItemValid(ItemStack var1) {
            return ☃.getItem() == Items.SADDLE && !this.getHasStack() && ☃.canBeSaddled();
         }

         @Override
         public boolean isEnabled() {
            return ☃.canBeSaddled();
         }
      });
      this.addSlotToContainer(new Slot(☃, 1, 8, 36) {
         @Override
         public boolean isItemValid(ItemStack var1) {
            return ☃.isArmor(☃);
         }

         @Override
         public boolean isEnabled() {
            return ☃.wearsArmor();
         }

         @Override
         public int getSlotStackLimit() {
            return 1;
         }
      });
      if (☃ instanceof AbstractChestHorse && ((AbstractChestHorse)☃).hasChest()) {
         for (int ☃xx = 0; ☃xx < 3; ☃xx++) {
            for (int ☃xxx = 0; ☃xxx < ((AbstractChestHorse)☃).getInventoryColumns(); ☃xxx++) {
               this.addSlotToContainer(new Slot(☃, 2 + ☃xxx + ☃xx * ((AbstractChestHorse)☃).getInventoryColumns(), 80 + ☃xxx * 18, 18 + ☃xx * 18));
            }
         }
      }

      for (int ☃xx = 0; ☃xx < 3; ☃xx++) {
         for (int ☃xxx = 0; ☃xxx < 9; ☃xxx++) {
            this.addSlotToContainer(new Slot(☃, ☃xxx + ☃xx * 9 + 9, 8 + ☃xxx * 18, 102 + ☃xx * 18 + -18));
         }
      }

      for (int ☃xx = 0; ☃xx < 9; ☃xx++) {
         this.addSlotToContainer(new Slot(☃, ☃xx, 8 + ☃xx * 18, 142));
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.horseInventory.isUsableByPlayer(☃) && this.horse.isEntityAlive() && this.horse.getDistance(☃) < 8.0F;
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack ☃ = ItemStack.EMPTY;
      Slot ☃x = this.inventorySlots.get(☃);
      if (☃x != null && ☃x.getHasStack()) {
         ItemStack ☃xx = ☃x.getStack();
         ☃ = ☃xx.copy();
         if (☃ < this.horseInventory.getSizeInventory()) {
            if (!this.mergeItemStack(☃xx, this.horseInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (this.getSlot(1).isItemValid(☃xx) && !this.getSlot(1).getHasStack()) {
            if (!this.mergeItemStack(☃xx, 1, 2, false)) {
               return ItemStack.EMPTY;
            }
         } else if (this.getSlot(0).isItemValid(☃xx)) {
            if (!this.mergeItemStack(☃xx, 0, 1, false)) {
               return ItemStack.EMPTY;
            }
         } else if (this.horseInventory.getSizeInventory() <= 2 || !this.mergeItemStack(☃xx, 2, this.horseInventory.getSizeInventory(), false)) {
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
      this.horseInventory.closeInventory(☃);
   }
}
