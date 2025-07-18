package net.minecraft.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryBasic implements IInventory {
   private String inventoryTitle;
   private final int slotsCount;
   private final NonNullList<ItemStack> inventoryContents;
   private List<IInventoryChangedListener> changeListeners;
   private boolean hasCustomName;

   public InventoryBasic(String var1, boolean var2, int var3) {
      this.inventoryTitle = ☃;
      this.hasCustomName = ☃;
      this.slotsCount = ☃;
      this.inventoryContents = NonNullList.withSize(☃, ItemStack.EMPTY);
   }

   public InventoryBasic(ITextComponent var1, int var2) {
      this(☃.getUnformattedText(), true, ☃);
   }

   public void addInventoryChangeListener(IInventoryChangedListener var1) {
      if (this.changeListeners == null) {
         this.changeListeners = Lists.newArrayList();
      }

      this.changeListeners.add(☃);
   }

   public void removeInventoryChangeListener(IInventoryChangedListener var1) {
      this.changeListeners.remove(☃);
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      return ☃ >= 0 && ☃ < this.inventoryContents.size() ? this.inventoryContents.get(☃) : ItemStack.EMPTY;
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      ItemStack ☃ = ItemStackHelper.getAndSplit(this.inventoryContents, ☃, ☃);
      if (!☃.isEmpty()) {
         this.markDirty();
      }

      return ☃;
   }

   public ItemStack addItem(ItemStack var1) {
      ItemStack ☃ = ☃.copy();

      for (int ☃x = 0; ☃x < this.slotsCount; ☃x++) {
         ItemStack ☃xx = this.getStackInSlot(☃x);
         if (☃xx.isEmpty()) {
            this.setInventorySlotContents(☃x, ☃);
            this.markDirty();
            return ItemStack.EMPTY;
         }

         if (ItemStack.areItemsEqual(☃xx, ☃)) {
            int ☃xxx = Math.min(this.getInventoryStackLimit(), ☃xx.getMaxStackSize());
            int ☃xxxx = Math.min(☃.getCount(), ☃xxx - ☃xx.getCount());
            if (☃xxxx > 0) {
               ☃xx.grow(☃xxxx);
               ☃.shrink(☃xxxx);
               if (☃.isEmpty()) {
                  this.markDirty();
                  return ItemStack.EMPTY;
               }
            }
         }
      }

      if (☃.getCount() != ☃.getCount()) {
         this.markDirty();
      }

      return ☃;
   }

   @Override
   public ItemStack removeStackFromSlot(int var1) {
      ItemStack ☃ = this.inventoryContents.get(☃);
      if (☃.isEmpty()) {
         return ItemStack.EMPTY;
      } else {
         this.inventoryContents.set(☃, ItemStack.EMPTY);
         return ☃;
      }
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.inventoryContents.set(☃, ☃);
      if (!☃.isEmpty() && ☃.getCount() > this.getInventoryStackLimit()) {
         ☃.setCount(this.getInventoryStackLimit());
      }

      this.markDirty();
   }

   @Override
   public int getSizeInventory() {
      return this.slotsCount;
   }

   @Override
   public boolean isEmpty() {
      for (ItemStack ☃ : this.inventoryContents) {
         if (!☃.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public String getName() {
      return this.inventoryTitle;
   }

   @Override
   public boolean hasCustomName() {
      return this.hasCustomName;
   }

   public void setCustomName(String var1) {
      this.hasCustomName = true;
      this.inventoryTitle = ☃;
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
   public void markDirty() {
      if (this.changeListeners != null) {
         for (int ☃ = 0; ☃ < this.changeListeners.size(); ☃++) {
            this.changeListeners.get(☃).onInventoryChanged(this);
         }
      }
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
      this.inventoryContents.clear();
   }
}
