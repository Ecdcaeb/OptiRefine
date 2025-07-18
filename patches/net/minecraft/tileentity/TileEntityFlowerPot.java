package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;

public class TileEntityFlowerPot extends TileEntity {
   private Item flowerPotItem;
   private int flowerPotData;

   public TileEntityFlowerPot() {
   }

   public TileEntityFlowerPot(Item var1, int var2) {
      this.flowerPotItem = ☃;
      this.flowerPotData = ☃;
   }

   public static void registerFixesFlowerPot(DataFixer var0) {
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      ResourceLocation ☃ = Item.REGISTRY.getNameForObject(this.flowerPotItem);
      ☃.setString("Item", ☃ == null ? "" : ☃.toString());
      ☃.setInteger("Data", this.flowerPotData);
      return ☃;
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      if (☃.hasKey("Item", 8)) {
         this.flowerPotItem = Item.getByNameOrId(☃.getString("Item"));
      } else {
         this.flowerPotItem = Item.getItemById(☃.getInteger("Item"));
      }

      this.flowerPotData = ☃.getInteger("Data");
   }

   @Nullable
   @Override
   public SPacketUpdateTileEntity getUpdatePacket() {
      return new SPacketUpdateTileEntity(this.pos, 5, this.getUpdateTag());
   }

   @Override
   public NBTTagCompound getUpdateTag() {
      return this.writeToNBT(new NBTTagCompound());
   }

   public void setItemStack(ItemStack var1) {
      this.flowerPotItem = ☃.getItem();
      this.flowerPotData = ☃.getMetadata();
   }

   public ItemStack getFlowerItemStack() {
      return this.flowerPotItem == null ? ItemStack.EMPTY : new ItemStack(this.flowerPotItem, 1, this.flowerPotData);
   }

   @Nullable
   public Item getFlowerPotItem() {
      return this.flowerPotItem;
   }

   public int getFlowerPotData() {
      return this.flowerPotData;
   }
}
