package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMycelium extends Block {
   public static final PropertyBool SNOWY = PropertyBool.create("snowy");

   protected BlockMycelium() {
      super(Material.GRASS, MapColor.PURPLE);
      this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, false));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      Block ☃ = ☃.getBlockState(☃.up()).getBlock();
      return ☃.withProperty(SNOWY, ☃ == Blocks.SNOW || ☃ == Blocks.SNOW_LAYER);
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!☃.isRemote) {
         if (☃.getLightFromNeighbors(☃.up()) < 4 && ☃.getBlockState(☃.up()).getLightOpacity() > 2) {
            ☃.setBlockState(☃, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
         } else {
            if (☃.getLightFromNeighbors(☃.up()) >= 9) {
               for (int ☃ = 0; ☃ < 4; ☃++) {
                  BlockPos ☃x = ☃.add(☃.nextInt(3) - 1, ☃.nextInt(5) - 3, ☃.nextInt(3) - 1);
                  IBlockState ☃xx = ☃.getBlockState(☃x);
                  IBlockState ☃xxx = ☃.getBlockState(☃x.up());
                  if (☃xx.getBlock() == Blocks.DIRT
                     && ☃xx.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT
                     && ☃.getLightFromNeighbors(☃x.up()) >= 4
                     && ☃xxx.getLightOpacity() <= 2) {
                     ☃.setBlockState(☃x, this.getDefaultState());
                  }
               }
            }
         }
      }
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      super.randomDisplayTick(☃, ☃, ☃, ☃);
      if (☃.nextInt(10) == 0) {
         ☃.spawnParticle(EnumParticleTypes.TOWN_AURA, ☃.getX() + ☃.nextFloat(), ☃.getY() + 1.1F, ☃.getZ() + ☃.nextFloat(), 0.0, 0.0, 0.0);
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Blocks.DIRT.getItemDropped(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), ☃, ☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return 0;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, SNOWY);
   }
}
