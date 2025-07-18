package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerFurnace extends Container {
   private final IInventory tileFurnace;
   private int cookTime;
   private int totalCookTime;
   private int furnaceBurnTime;
   private int currentItemBurnTime;

   public ContainerFurnace(InventoryPlayer var1, IInventory var2) {
      this.tileFurnace = ☃;
      this.addSlotToContainer(new Slot(☃, 0, 56, 17));
      this.addSlotToContainer(new SlotFurnaceFuel(☃, 1, 56, 53));
      this.addSlotToContainer(new SlotFurnaceOutput(☃.player, ☃, 2, 116, 35));

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
   public void addListener(IContainerListener var1) {
      super.addListener(☃);
      ☃.sendAllWindowProperties(this, this.tileFurnace);
   }

   @Override
   public void detectAndSendChanges() {
      super.detectAndSendChanges();

      for (int ☃ = 0; ☃ < this.listeners.size(); ☃++) {
         IContainerListener ☃x = this.listeners.get(☃);
         if (this.cookTime != this.tileFurnace.getField(2)) {
            ☃x.sendWindowProperty(this, 2, this.tileFurnace.getField(2));
         }

         if (this.furnaceBurnTime != this.tileFurnace.getField(0)) {
            ☃x.sendWindowProperty(this, 0, this.tileFurnace.getField(0));
         }

         if (this.currentItemBurnTime != this.tileFurnace.getField(1)) {
            ☃x.sendWindowProperty(this, 1, this.tileFurnace.getField(1));
         }

         if (this.totalCookTime != this.tileFurnace.getField(3)) {
            ☃x.sendWindowProperty(this, 3, this.tileFurnace.getField(3));
         }
      }

      this.cookTime = this.tileFurnace.getField(2);
      this.furnaceBurnTime = this.tileFurnace.getField(0);
      this.currentItemBurnTime = this.tileFurnace.getField(1);
      this.totalCookTime = this.tileFurnace.getField(3);
   }

   @Override
   public void updateProgressBar(int var1, int var2) {
      this.tileFurnace.setField(☃, ☃);
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.tileFurnace.isUsableByPlayer(☃);
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
         } else if (☃ != 1 && ☃ != 0) {
            if (!FurnaceRecipes.instance().getSmeltingResult(☃xx).isEmpty()) {
               if (!this.mergeItemStack(☃xx, 0, 1, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (TileEntityFurnace.isItemFuel(☃xx)) {
               if (!this.mergeItemStack(☃xx, 1, 2, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (☃ >= 3 && ☃ < 30) {
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
}
