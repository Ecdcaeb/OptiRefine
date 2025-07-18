package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileEntityNote extends TileEntity {
   public byte note;
   public boolean previousRedstoneState;

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      ☃.setByte("note", this.note);
      ☃.setBoolean("powered", this.previousRedstoneState);
      return ☃;
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.note = ☃.getByte("note");
      this.note = (byte)MathHelper.clamp(this.note, 0, 24);
      this.previousRedstoneState = ☃.getBoolean("powered");
   }

   public void changePitch() {
      this.note = (byte)((this.note + 1) % 25);
      this.markDirty();
   }

   public void triggerNote(World var1, BlockPos var2) {
      if (☃.getBlockState(☃.up()).getMaterial() == Material.AIR) {
         IBlockState ☃ = ☃.getBlockState(☃.down());
         Material ☃x = ☃.getMaterial();
         int ☃xx = 0;
         if (☃x == Material.ROCK) {
            ☃xx = 1;
         }

         if (☃x == Material.SAND) {
            ☃xx = 2;
         }

         if (☃x == Material.GLASS) {
            ☃xx = 3;
         }

         if (☃x == Material.WOOD) {
            ☃xx = 4;
         }

         Block ☃xxx = ☃.getBlock();
         if (☃xxx == Blocks.CLAY) {
            ☃xx = 5;
         }

         if (☃xxx == Blocks.GOLD_BLOCK) {
            ☃xx = 6;
         }

         if (☃xxx == Blocks.WOOL) {
            ☃xx = 7;
         }

         if (☃xxx == Blocks.PACKED_ICE) {
            ☃xx = 8;
         }

         if (☃xxx == Blocks.BONE_BLOCK) {
            ☃xx = 9;
         }

         ☃.addBlockEvent(☃, Blocks.NOTEBLOCK, ☃xx, this.note);
      }
   }
}
