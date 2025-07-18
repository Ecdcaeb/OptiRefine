package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileEntityDispenser extends TileEntityLockableLoot {
   private static final Random RNG = new Random();
   private NonNullList<ItemStack> stacks = NonNullList.withSize(9, ItemStack.EMPTY);

   @Override
   public int getSizeInventory() {
      return 9;
   }

   @Override
   public boolean isEmpty() {
      for (ItemStack ☃ : this.stacks) {
         if (!☃.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public int getDispenseSlot() {
      this.fillWithLoot(null);
      int ☃ = -1;
      int ☃x = 1;

      for (int ☃xx = 0; ☃xx < this.stacks.size(); ☃xx++) {
         if (!this.stacks.get(☃xx).isEmpty() && RNG.nextInt(☃x++) == 0) {
            ☃ = ☃xx;
         }
      }

      return ☃;
   }

   public int addItemStack(ItemStack var1) {
      for (int ☃ = 0; ☃ < this.stacks.size(); ☃++) {
         if (this.stacks.get(☃).isEmpty()) {
            this.setInventorySlotContents(☃, ☃);
            return ☃;
         }
      }

      return -1;
   }

   @Override
   public String getName() {
      return this.hasCustomName() ? this.customName : "container.dispenser";
   }

   public static void registerFixes(DataFixer var0) {
      ☃.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityDispenser.class, "Items"));
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
      if (!this.checkLootAndRead(☃)) {
         ItemStackHelper.loadAllItems(☃, this.stacks);
      }

      if (☃.hasKey("CustomName", 8)) {
         this.customName = ☃.getString("CustomName");
      }
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      if (!this.checkLootAndWrite(☃)) {
         ItemStackHelper.saveAllItems(☃, this.stacks);
      }

      if (this.hasCustomName()) {
         ☃.setString("CustomName", this.customName);
      }

      return ☃;
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public String getGuiID() {
      return "minecraft:dispenser";
   }

   @Override
   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      this.fillWithLoot(☃);
      return new ContainerDispenser(☃, this);
   }

   @Override
   protected NonNullList<ItemStack> getItems() {
      return this.stacks;
   }
}
