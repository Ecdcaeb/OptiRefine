package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockGlowstone extends Block {
   public BlockGlowstone(Material var1) {
      super(☃);
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public int quantityDroppedWithBonus(int var1, Random var2) {
      return MathHelper.clamp(this.quantityDropped(☃) + ☃.nextInt(☃ + 1), 1, 4);
   }

   @Override
   public int quantityDropped(Random var1) {
      return 2 + ☃.nextInt(3);
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.GLOWSTONE_DUST;
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return MapColor.SAND;
   }
}
