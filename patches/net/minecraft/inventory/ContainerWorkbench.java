package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerWorkbench extends Container {
   public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
   public InventoryCraftResult craftResult = new InventoryCraftResult();
   private final World world;
   private final BlockPos pos;
   private final EntityPlayer player;

   public ContainerWorkbench(InventoryPlayer var1, World var2, BlockPos var3) {
      this.world = ☃;
      this.pos = ☃;
      this.player = ☃.player;
      this.addSlotToContainer(new SlotCrafting(☃.player, this.craftMatrix, this.craftResult, 0, 124, 35));

      for (int ☃ = 0; ☃ < 3; ☃++) {
         for (int ☃x = 0; ☃x < 3; ☃x++) {
            this.addSlotToContainer(new Slot(this.craftMatrix, ☃x + ☃ * 3, 30 + ☃x * 18, 17 + ☃ * 18));
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
   public void onCraftMatrixChanged(IInventory var1) {
      this.slotChangedCraftingGrid(this.world, this.player, this.craftMatrix, this.craftResult);
   }

   @Override
   public void onContainerClosed(EntityPlayer var1) {
      super.onContainerClosed(☃);
      if (!this.world.isRemote) {
         this.clearContainer(☃, this.world, this.craftMatrix);
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.world.getBlockState(this.pos).getBlock() != Blocks.CRAFTING_TABLE
         ? false
         : ☃.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack ☃ = ItemStack.EMPTY;
      Slot ☃x = this.inventorySlots.get(☃);
      if (☃x != null && ☃x.getHasStack()) {
         ItemStack ☃xx = ☃x.getStack();
         ☃ = ☃xx.copy();
         if (☃ == 0) {
            ☃xx.getItem().onCreated(☃xx, this.world, ☃);
            if (!this.mergeItemStack(☃xx, 10, 46, true)) {
               return ItemStack.EMPTY;
            }

            ☃x.onSlotChange(☃xx, ☃);
         } else if (☃ >= 10 && ☃ < 37) {
            if (!this.mergeItemStack(☃xx, 37, 46, false)) {
               return ItemStack.EMPTY;
            }
         } else if (☃ >= 37 && ☃ < 46) {
            if (!this.mergeItemStack(☃xx, 10, 37, false)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.mergeItemStack(☃xx, 10, 46, false)) {
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

         ItemStack ☃xxx = ☃x.onTake(☃, ☃xx);
         if (☃ == 0) {
            ☃.dropItem(☃xxx, false);
         }
      }

      return ☃;
   }

   @Override
   public boolean canMergeSlot(ItemStack var1, Slot var2) {
      return ☃.inventory != this.craftResult && super.canMergeSlot(☃, ☃);
   }
}
