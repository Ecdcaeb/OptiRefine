package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockBookshelf extends Block {
   public BlockBookshelf() {
      super(Material.WOOD);
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public int quantityDropped(Random var1) {
      return 3;
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.BOOK;
   }
}
