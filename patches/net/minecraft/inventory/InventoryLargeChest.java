package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public class InventoryLargeChest implements ILockableContainer {
   private final String name;
   private final ILockableContainer upperChest;
   private final ILockableContainer lowerChest;

   public InventoryLargeChest(String var1, ILockableContainer var2, ILockableContainer var3) {
      this.name = ☃;
      if (☃ == null) {
         ☃ = ☃;
      }

      if (☃ == null) {
         ☃ = ☃;
      }

      this.upperChest = ☃;
      this.lowerChest = ☃;
      if (☃.isLocked()) {
         ☃.setLockCode(☃.getLockCode());
      } else if (☃.isLocked()) {
         ☃.setLockCode(☃.getLockCode());
      }
   }

   @Override
   public int getSizeInventory() {
      return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
   }

   @Override
   public boolean isEmpty() {
      return this.upperChest.isEmpty() && this.lowerChest.isEmpty();
   }

   public boolean isPartOfLargeChest(IInventory var1) {
      return this.upperChest == ☃ || this.lowerChest == ☃;
   }

   @Override
   public String getName() {
      if (this.upperChest.hasCustomName()) {
         return this.upperChest.getName();
      } else {
         return this.lowerChest.hasCustomName() ? this.lowerChest.getName() : this.name;
      }
   }

   @Override
   public boolean hasCustomName() {
      return this.upperChest.hasCustomName() || this.lowerChest.hasCustomName();
   }

   @Override
   public ITextComponent getDisplayName() {
      return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName()));
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      return ☃ >= this.upperChest.getSizeInventory()
         ? this.lowerChest.getStackInSlot(☃ - this.upperChest.getSizeInventory())
         : this.upperChest.getStackInSlot(☃);
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      return ☃ >= this.upperChest.getSizeInventory()
         ? this.lowerChest.decrStackSize(☃ - this.upperChest.getSizeInventory(), ☃)
         : this.upperChest.decrStackSize(☃, ☃);
   }

   @Override
   public ItemStack removeStackFromSlot(int var1) {
      return ☃ >= this.upperChest.getSizeInventory()
         ? this.lowerChest.removeStackFromSlot(☃ - this.upperChest.getSizeInventory())
         : this.upperChest.removeStackFromSlot(☃);
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      if (☃ >= this.upperChest.getSizeInventory()) {
         this.lowerChest.setInventorySlotContents(☃ - this.upperChest.getSizeInventory(), ☃);
      } else {
         this.upperChest.setInventorySlotContents(☃, ☃);
      }
   }

   @Override
   public int getInventoryStackLimit() {
      return this.upperChest.getInventoryStackLimit();
   }

   @Override
   public void markDirty() {
      this.upperChest.markDirty();
      this.lowerChest.markDirty();
   }

   @Override
   public boolean isUsableByPlayer(EntityPlayer var1) {
      return this.upperChest.isUsableByPlayer(☃) && this.lowerChest.isUsableByPlayer(☃);
   }

   @Override
   public void openInventory(EntityPlayer var1) {
      this.upperChest.openInventory(☃);
      this.lowerChest.openInventory(☃);
   }

   @Override
   public void closeInventory(EntityPlayer var1) {
      this.upperChest.closeInventory(☃);
      this.lowerChest.closeInventory(☃);
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
   public boolean isLocked() {
      return this.upperChest.isLocked() || this.lowerChest.isLocked();
   }

   @Override
   public void setLockCode(LockCode var1) {
      this.upperChest.setLockCode(☃);
      this.lowerChest.setLockCode(☃);
   }

   @Override
   public LockCode getLockCode() {
      return this.upperChest.getLockCode();
   }

   @Override
   public String getGuiID() {
      return this.upperChest.getGuiID();
   }

   @Override
   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      return new ContainerChest(☃, this, ☃);
   }

   @Override
   public void clear() {
      this.upperChest.clear();
      this.lowerChest.clear();
   }
}
