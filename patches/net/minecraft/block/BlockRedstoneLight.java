package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRedstoneLight extends Block {
   private final boolean isOn;

   public BlockRedstoneLight(boolean var1) {
      super(Material.REDSTONE_LIGHT);
      this.isOn = ☃;
      if (☃) {
         this.setLightLevel(1.0F);
      }
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!☃.isRemote) {
         if (this.isOn && !☃.isBlockPowered(☃)) {
            ☃.setBlockState(☃, Blocks.REDSTONE_LAMP.getDefaultState(), 2);
         } else if (!this.isOn && ☃.isBlockPowered(☃)) {
            ☃.setBlockState(☃, Blocks.LIT_REDSTONE_LAMP.getDefaultState(), 2);
         }
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.isRemote) {
         if (this.isOn && !☃.isBlockPowered(☃)) {
            ☃.scheduleUpdate(☃, this, 4);
         } else if (!this.isOn && ☃.isBlockPowered(☃)) {
            ☃.setBlockState(☃, Blocks.LIT_REDSTONE_LAMP.getDefaultState(), 2);
         }
      }
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!☃.isRemote) {
         if (this.isOn && !☃.isBlockPowered(☃)) {
            ☃.setBlockState(☃, Blocks.REDSTONE_LAMP.getDefaultState(), 2);
         }
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.REDSTONE_LAMP);
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Blocks.REDSTONE_LAMP);
   }

   @Override
   protected ItemStack getSilkTouchDrop(IBlockState var1) {
      return new ItemStack(Blocks.REDSTONE_LAMP);
   }
}
