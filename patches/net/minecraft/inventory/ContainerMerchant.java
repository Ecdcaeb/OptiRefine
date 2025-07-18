package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerMerchant extends Container {
   private final IMerchant merchant;
   private final InventoryMerchant merchantInventory;
   private final World world;

   public ContainerMerchant(InventoryPlayer var1, IMerchant var2, World var3) {
      this.merchant = ☃;
      this.world = ☃;
      this.merchantInventory = new InventoryMerchant(☃.player, ☃);
      this.addSlotToContainer(new Slot(this.merchantInventory, 0, 36, 53));
      this.addSlotToContainer(new Slot(this.merchantInventory, 1, 62, 53));
      this.addSlotToContainer(new SlotMerchantResult(☃.player, ☃, this.merchantInventory, 2, 120, 53));

      for (int ☃ = 0; ☃ < 3; ☃++) {
         for (int ☃x = 0; ☃x < 9; ☃x++) {
            this.addSlotToContainer(new Slot(☃, ☃x + ☃ * 9 + 9, 8 + ☃x * 18, 84 + ☃ * 18));
         }
      }

      for (int ☃ = 0; ☃ < 9; ☃++) {
         this.addSlotToContainer(new Slot(☃, ☃, 8 + ☃ * 18, 142));
      }
   }

   public InventoryMerchant getMerchantInventory() {
      return this.merchantInventory;
   }

   @Override
   public void onCraftMatrixChanged(IInventory var1) {
      this.merchantInventory.resetRecipeAndSlots();
      super.onCraftMatrixChanged(☃);
   }

   public void setCurrentRecipeIndex(int var1) {
      this.merchantInventory.setCurrentRecipeIndex(☃);
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.merchant.getCustomer() == ☃;
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack ☃ = ItemStack.EMPTY;
      Slot ☃x = this.inventorySlots.get(☃);
      if (☃x != null && ☃x.getHasStack()) {
         ItemStack ☃xx = ☃x.getStack();
         ☃ = ☃xx.copy();
         if (☃ == 2) {
            if (!this.mergeItemStack(☃xx, 3, 39, true)) {
               return ItemStack.EMPTY;
            }

            ☃x.onSlotChange(☃xx, ☃);
         } else if (☃ != 0 && ☃ != 1) {
            if (☃ >= 3 && ☃ < 30) {
               if (!this.mergeItemStack(☃xx, 30, 39, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (☃ >= 30 && ☃ < 39 && !this.mergeItemStack(☃xx, 3, 30, false)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.mergeItemStack(☃xx, 3, 39, false)) {
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

   @Override
   public void onContainerClosed(EntityPlayer var1) {
      super.onContainerClosed(☃);
      this.merchant.setCustomer(null);
      super.onContainerClosed(☃);
      if (!this.world.isRemote) {
         ItemStack ☃ = this.merchantInventory.removeStackFromSlot(0);
         if (!☃.isEmpty()) {
            ☃.dropItem(☃, false);
         }

         ☃ = this.merchantInventory.removeStackFromSlot(1);
         if (!☃.isEmpty()) {
            ☃.dropItem(☃, false);
         }
      }
   }
}
