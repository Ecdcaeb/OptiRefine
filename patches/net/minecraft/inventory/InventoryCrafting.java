package net.minecraft.inventory;

import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryCrafting implements IInventory {
   private final NonNullList<ItemStack> stackList;
   private final int inventoryWidth;
   private final int inventoryHeight;
   private final Container eventHandler;

   public InventoryCrafting(Container var1, int var2, int var3) {
      this.stackList = NonNullList.withSize(☃ * ☃, ItemStack.EMPTY);
      this.eventHandler = ☃;
      this.inventoryWidth = ☃;
      this.inventoryHeight = ☃;
   }

   @Override
   public int getSizeInventory() {
      return this.stackList.size();
   }

   @Override
   public boolean isEmpty() {
      for (ItemStack ☃ : this.stackList) {
         if (!☃.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      return ☃ >= this.getSizeInventory() ? ItemStack.EMPTY : this.stackList.get(☃);
   }

   public ItemStack getStackInRowAndColumn(int var1, int var2) {
      return ☃ >= 0 && ☃ < this.inventoryWidth && ☃ >= 0 && ☃ <= this.inventoryHeight ? this.getStackInSlot(☃ + ☃ * this.inventoryWidth) : ItemStack.EMPTY;
   }

   @Override
   public String getName() {
      return "container.crafting";
   }

   @Override
   public boolean hasCustomName() {
      return false;
   }

   @Override
   public ITextComponent getDisplayName() {
      return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName()));
   }

   @Override
   public ItemStack removeStackFromSlot(int var1) {
      return ItemStackHelper.getAndRemove(this.stackList, ☃);
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      ItemStack ☃ = ItemStackHelper.getAndSplit(this.stackList, ☃, ☃);
      if (!☃.isEmpty()) {
         this.eventHandler.onCraftMatrixChanged(this);
      }

      return ☃;
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.stackList.set(☃, ☃);
      this.eventHandler.onCraftMatrixChanged(this);
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public void markDirty() {
   }

   @Override
   public boolean isUsableByPlayer(EntityPlayer var1) {
      return true;
   }

   @Override
   public void openInventory(EntityPlayer var1) {
   }

   @Override
   public void closeInventory(EntityPlayer var1) {
   }

   @Override
   public boolean isItemValidForSlot(int var1, ItemStack var2) {
      return true;
   }

   @Override
   public int getField(int var1) {
      return 0;
   }

   @Override
   public void setField(int var1, int var2) {
   }

   @Override
   public int getFieldCount() {
      return 0;
   }

   @Override
   public void clear() {
      this.stackList.clear();
   }

   public int getHeight() {
      return this.inventoryHeight;
   }

   public int getWidth() {
      return this.inventoryWidth;
   }

   public void fillStackedContents(RecipeItemHelper var1) {
      for (ItemStack ☃ : this.stackList) {
         ☃.accountStack(☃);
      }
   }
}
