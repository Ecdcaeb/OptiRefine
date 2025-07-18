package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockMelon extends Block {
   protected BlockMelon() {
      super(Material.GOURD, MapColor.LIME);
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.MELON;
   }

   @Override
   public int quantityDropped(Random var1) {
      return 3 + ☃.nextInt(5);
   }

   @Override
   public int quantityDroppedWithBonus(int var1, Random var2) {
      return Math.min(9, this.quantityDropped(☃) + ☃.nextInt(1 + ☃));
   }
}
