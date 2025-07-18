package net.minecraft.inventory;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryCraftResult implements IInventory {
   private final NonNullList<ItemStack> stackResult = NonNullList.withSize(1, ItemStack.EMPTY);
   private IRecipe recipeUsed;

   @Override
   public int getSizeInventory() {
      return 1;
   }

   @Override
   public boolean isEmpty() {
      for (ItemStack ☃ : this.stackResult) {
         if (!☃.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      return this.stackResult.get(0);
   }

   @Override
   public String getName() {
      return "Result";
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
   public ItemStack decrStackSize(int var1, int var2) {
      return ItemStackHelper.getAndRemove(this.stackResult, 0);
   }

   @Override
   public ItemStack removeStackFromSlot(int var1) {
      return ItemStackHelper.getAndRemove(this.stackResult, 0);
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.stackResult.set(0, ☃);
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
      this.stackResult.clear();
   }

   public void setRecipeUsed(@Nullable IRecipe var1) {
      this.recipeUsed = ☃;
   }

   @Nullable
   public IRecipe getRecipeUsed() {
      return this.recipeUsed;
   }
}
