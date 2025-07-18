package net.minecraft.tileentity;

import net.minecraft.block.BlockBed;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

public class TileEntityBed extends TileEntity {
   private EnumDyeColor color = EnumDyeColor.RED;

   public void setItemValues(ItemStack var1) {
      this.setColor(EnumDyeColor.byMetadata(☃.getMetadata()));
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      if (☃.hasKey("color")) {
         this.color = EnumDyeColor.byMetadata(☃.getInteger("color"));
      }
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      ☃.setInteger("color", this.color.getMetadata());
      return ☃;
   }

   @Override
   public NBTTagCompound getUpdateTag() {
      return this.writeToNBT(new NBTTagCompound());
   }

   @Override
   public SPacketUpdateTileEntity getUpdatePacket() {
      return new SPacketUpdateTileEntity(this.pos, 11, this.getUpdateTag());
   }

   public EnumDyeColor getColor() {
      return this.color;
   }

   public void setColor(EnumDyeColor var1) {
      this.color = ☃;
      this.markDirty();
   }

   public boolean isHeadPiece() {
      return BlockBed.isHeadPiece(this.getBlockMetadata());
   }

   public ItemStack getItemStack() {
      return new ItemStack(Items.BED, 1, this.color.getMetadata());
   }
}
