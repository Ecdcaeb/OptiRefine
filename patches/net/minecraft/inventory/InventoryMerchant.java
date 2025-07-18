package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchant implements IInventory {
   private final IMerchant merchant;
   private final NonNullList<ItemStack> slots = NonNullList.withSize(3, ItemStack.EMPTY);
   private final EntityPlayer player;
   private MerchantRecipe currentRecipe;
   private int currentRecipeIndex;

   public InventoryMerchant(EntityPlayer var1, IMerchant var2) {
      this.player = ☃;
      this.merchant = ☃;
   }

   @Override
   public int getSizeInventory() {
      return this.slots.size();
   }

   @Override
   public boolean isEmpty() {
      for (ItemStack ☃ : this.slots) {
         if (!☃.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      return this.slots.get(☃);
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      ItemStack ☃ = this.slots.get(☃);
      if (☃ == 2 && !☃.isEmpty()) {
         return ItemStackHelper.getAndSplit(this.slots, ☃, ☃.getCount());
      } else {
         ItemStack ☃x = ItemStackHelper.getAndSplit(this.slots, ☃, ☃);
         if (!☃x.isEmpty() && this.inventoryResetNeededOnSlotChange(☃)) {
            this.resetRecipeAndSlots();
         }

         return ☃x;
      }
   }

   private boolean inventoryResetNeededOnSlotChange(int var1) {
      return ☃ == 0 || ☃ == 1;
   }

   @Override
   public ItemStack removeStackFromSlot(int var1) {
      return ItemStackHelper.getAndRemove(this.slots, ☃);
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.slots.set(☃, ☃);
      if (!☃.isEmpty() && ☃.getCount() > this.getInventoryStackLimit()) {
         ☃.setCount(this.getInventoryStackLimit());
      }

      if (this.inventoryResetNeededOnSlotChange(☃)) {
         this.resetRecipeAndSlots();
      }
   }

   @Override
   public String getName() {
      return "mob.villager";
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
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public boolean isUsableByPlayer(EntityPlayer var1) {
      return this.merchant.getCustomer() == ☃;
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
   public void markDirty() {
      this.resetRecipeAndSlots();
   }

   public void resetRecipeAndSlots() {
      this.currentRecipe = null;
      ItemStack ☃ = this.slots.get(0);
      ItemStack ☃x = this.slots.get(1);
      if (☃.isEmpty()) {
         ☃ = ☃x;
         ☃x = ItemStack.EMPTY;
      }

      if (☃.isEmpty()) {
         this.setInventorySlotContents(2, ItemStack.EMPTY);
      } else {
         MerchantRecipeList ☃xx = this.merchant.getRecipes(this.player);
         if (☃xx != null) {
            MerchantRecipe ☃xxx = ☃xx.canRecipeBeUsed(☃, ☃x, this.currentRecipeIndex);
            if (☃xxx != null && !☃xxx.isRecipeDisabled()) {
               this.currentRecipe = ☃xxx;
               this.setInventorySlotContents(2, ☃xxx.getItemToSell().copy());
            } else if (!☃x.isEmpty()) {
               ☃xxx = ☃xx.canRecipeBeUsed(☃x, ☃, this.currentRecipeIndex);
               if (☃xxx != null && !☃xxx.isRecipeDisabled()) {
                  this.currentRecipe = ☃xxx;
                  this.setInventorySlotContents(2, ☃xxx.getItemToSell().copy());
               } else {
                  this.setInventorySlotContents(2, ItemStack.EMPTY);
               }
            } else {
               this.setInventorySlotContents(2, ItemStack.EMPTY);
            }
         }

         this.merchant.verifySellingItem(this.getStackInSlot(2));
      }
   }

   public MerchantRecipe getCurrentRecipe() {
      return this.currentRecipe;
   }

   public void setCurrentRecipeIndex(int var1) {
      this.currentRecipeIndex = ☃;
      this.resetRecipeAndSlots();
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
      this.slots.clear();
   }
}
