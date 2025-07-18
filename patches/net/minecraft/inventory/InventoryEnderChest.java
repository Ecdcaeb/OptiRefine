package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityEnderChest;

public class InventoryEnderChest extends InventoryBasic {
   private TileEntityEnderChest associatedChest;

   public InventoryEnderChest() {
      super("container.enderchest", false, 27);
   }

   public void setChestTileEntity(TileEntityEnderChest var1) {
      this.associatedChest = ☃;
   }

   public void loadInventoryFromNBT(NBTTagList var1) {
      for (int ☃ = 0; ☃ < this.getSizeInventory(); ☃++) {
         this.setInventorySlotContents(☃, ItemStack.EMPTY);
      }

      for (int ☃ = 0; ☃ < ☃.tagCount(); ☃++) {
         NBTTagCompound ☃x = ☃.getCompoundTagAt(☃);
         int ☃xx = ☃x.getByte("Slot") & 255;
         if (☃xx >= 0 && ☃xx < this.getSizeInventory()) {
            this.setInventorySlotContents(☃xx, new ItemStack(☃x));
         }
      }
   }

   public NBTTagList saveInventoryToNBT() {
      NBTTagList ☃ = new NBTTagList();

      for (int ☃x = 0; ☃x < this.getSizeInventory(); ☃x++) {
         ItemStack ☃xx = this.getStackInSlot(☃x);
         if (!☃xx.isEmpty()) {
            NBTTagCompound ☃xxx = new NBTTagCompound();
            ☃xxx.setByte("Slot", (byte)☃x);
            ☃xx.writeToNBT(☃xxx);
            ☃.appendTag(☃xxx);
         }
      }

      return ☃;
   }

   @Override
   public boolean isUsableByPlayer(EntityPlayer var1) {
      return this.associatedChest != null && !this.associatedChest.canBeUsed(☃) ? false : super.isUsableByPlayer(☃);
   }

   @Override
   public void openInventory(EntityPlayer var1) {
      if (this.associatedChest != null) {
         this.associatedChest.openChest();
      }

      super.openInventory(☃);
   }

   @Override
   public void closeInventory(EntityPlayer var1) {
      if (this.associatedChest != null) {
         this.associatedChest.closeChest();
      }

      super.closeInventory(☃);
      this.associatedChest = null;
   }
}
