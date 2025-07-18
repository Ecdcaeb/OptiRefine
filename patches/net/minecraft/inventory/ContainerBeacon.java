package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerBeacon extends Container {
   private final IInventory tileBeacon;
   private final ContainerBeacon.BeaconSlot beaconSlot;

   public ContainerBeacon(IInventory var1, IInventory var2) {
      this.tileBeacon = ☃;
      this.beaconSlot = new ContainerBeacon.BeaconSlot(☃, 0, 136, 110);
      this.addSlotToContainer(this.beaconSlot);
      int ☃ = 36;
      int ☃x = 137;

      for (int ☃xx = 0; ☃xx < 3; ☃xx++) {
         for (int ☃xxx = 0; ☃xxx < 9; ☃xxx++) {
            this.addSlotToContainer(new Slot(☃, ☃xxx + ☃xx * 9 + 9, 36 + ☃xxx * 18, 137 + ☃xx * 18));
         }
      }

      for (int ☃xx = 0; ☃xx < 9; ☃xx++) {
         this.addSlotToContainer(new Slot(☃, ☃xx, 36 + ☃xx * 18, 195));
      }
   }

   @Override
   public void addListener(IContainerListener var1) {
      super.addListener(☃);
      ☃.sendAllWindowProperties(this, this.tileBeacon);
   }

   @Override
   public void updateProgressBar(int var1, int var2) {
      this.tileBeacon.setField(☃, ☃);
   }

   public IInventory getTileEntity() {
      return this.tileBeacon;
   }

   @Override
   public void onContainerClosed(EntityPlayer var1) {
      super.onContainerClosed(☃);
      if (!☃.world.isRemote) {
         ItemStack ☃ = this.beaconSlot.decrStackSize(this.beaconSlot.getSlotStackLimit());
         if (!☃.isEmpty()) {
            ☃.dropItem(☃, false);
         }
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.tileBeacon.isUsableByPlayer(☃);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack ☃ = ItemStack.EMPTY;
      Slot ☃x = this.inventorySlots.get(☃);
      if (☃x != null && ☃x.getHasStack()) {
         ItemStack ☃xx = ☃x.getStack();
         ☃ = ☃xx.copy();
         if (☃ == 0) {
            if (!this.mergeItemStack(☃xx, 1, 37, true)) {
               return ItemStack.EMPTY;
            }

            ☃x.onSlotChange(☃xx, ☃);
         } else if (!this.beaconSlot.getHasStack() && this.beaconSlot.isItemValid(☃xx) && ☃xx.getCount() == 1) {
            if (!this.mergeItemStack(☃xx, 0, 1, false)) {
               return ItemStack.EMPTY;
            }
         } else if (☃ >= 1 && ☃ < 28) {
            if (!this.mergeItemStack(☃xx, 28, 37, false)) {
               return ItemStack.EMPTY;
            }
         } else if (☃ >= 28 && ☃ < 37) {
            if (!this.mergeItemStack(☃xx, 1, 28, false)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.mergeItemStack(☃xx, 1, 37, false)) {
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

   class BeaconSlot extends Slot {
      public BeaconSlot(IInventory var2, int var3, int var4, int var5) {
         super(☃, ☃, ☃, ☃);
      }

      @Override
      public boolean isItemValid(ItemStack var1) {
         Item ☃ = ☃.getItem();
         return ☃ == Items.EMERALD || ☃ == Items.DIAMOND || ☃ == Items.GOLD_INGOT || ☃ == Items.IRON_INGOT;
      }

      @Override
      public int getSlotStackLimit() {
         return 1;
      }
   }
}
