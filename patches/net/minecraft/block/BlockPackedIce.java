package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPackedIce extends Block {
   public BlockPackedIce() {
      super(Material.PACKED_ICE);
      this.slipperiness = 0.98F;
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }
}
