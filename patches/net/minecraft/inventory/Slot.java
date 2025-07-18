package net.minecraft.inventory;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class Slot {
   private final int slotIndex;
   public final IInventory inventory;
   public int slotNumber;
   public int xPos;
   public int yPos;

   public Slot(IInventory var1, int var2, int var3, int var4) {
      this.inventory = ☃;
      this.slotIndex = ☃;
      this.xPos = ☃;
      this.yPos = ☃;
   }

   public void onSlotChange(ItemStack var1, ItemStack var2) {
      int ☃ = ☃.getCount() - ☃.getCount();
      if (☃ > 0) {
         this.onCrafting(☃, ☃);
      }
   }

   protected void onCrafting(ItemStack var1, int var2) {
   }

   protected void onSwapCraft(int var1) {
   }

   protected void onCrafting(ItemStack var1) {
   }

   public ItemStack onTake(EntityPlayer var1, ItemStack var2) {
      this.onSlotChanged();
      return ☃;
   }

   public boolean isItemValid(ItemStack var1) {
      return true;
   }

   public ItemStack getStack() {
      return this.inventory.getStackInSlot(this.slotIndex);
   }

   public boolean getHasStack() {
      return !this.getStack().isEmpty();
   }

   public void putStack(ItemStack var1) {
      this.inventory.setInventorySlotContents(this.slotIndex, ☃);
      this.onSlotChanged();
   }

   public void onSlotChanged() {
      this.inventory.markDirty();
   }

   public int getSlotStackLimit() {
      return this.inventory.getInventoryStackLimit();
   }

   public int getItemStackLimit(ItemStack var1) {
      return this.getSlotStackLimit();
   }

   @Nullable
   public String getSlotTexture() {
      return null;
   }

   public ItemStack decrStackSize(int var1) {
      return this.inventory.decrStackSize(this.slotIndex, ☃);
   }

   public boolean isHere(IInventory var1, int var2) {
      return ☃ == this.inventory && ☃ == this.slotIndex;
   }

   public boolean canTakeStack(EntityPlayer var1) {
      return true;
   }

   public boolean isEnabled() {
      return true;
   }
}
