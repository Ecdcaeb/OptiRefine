package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class BlockSnowBlock extends Block {
   protected BlockSnowBlock() {
      super(Material.CRAFTED_SNOW);
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.SNOWBALL;
   }

   @Override
   public int quantityDropped(Random var1) {
      return 4;
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (☃.getLightFor(EnumSkyBlock.BLOCK, ☃) > 11) {
         this.dropBlockAsItem(☃, ☃, ☃.getBlockState(☃), 0);
         ☃.setBlockToAir(☃);
      }
   }
}
