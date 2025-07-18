package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGrass extends Block implements IGrowable {
   public static final PropertyBool SNOWY = PropertyBool.create("snowy");

   protected BlockGrass() {
      super(Material.GRASS);
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
            ☃.setBlockState(☃, Blocks.DIRT.getDefaultState());
         } else {
            if (☃.getLightFromNeighbors(☃.up()) >= 9) {
               for (int ☃ = 0; ☃ < 4; ☃++) {
                  BlockPos ☃x = ☃.add(☃.nextInt(3) - 1, ☃.nextInt(5) - 3, ☃.nextInt(3) - 1);
                  if (☃x.getY() >= 0 && ☃x.getY() < 256 && !☃.isBlockLoaded(☃x)) {
                     return;
                  }

                  IBlockState ☃xx = ☃.getBlockState(☃x.up());
                  IBlockState ☃xxx = ☃.getBlockState(☃x);
                  if (☃xxx.getBlock() == Blocks.DIRT
                     && ☃xxx.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT
                     && ☃.getLightFromNeighbors(☃x.up()) >= 4
                     && ☃xx.getLightOpacity() <= 2) {
                     ☃.setBlockState(☃x, Blocks.GRASS.getDefaultState());
                  }
               }
            }
         }
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Blocks.DIRT.getItemDropped(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), ☃, ☃);
   }

   @Override
   public boolean canGrow(World var1, BlockPos var2, IBlockState var3, boolean var4) {
      return true;
   }

   @Override
   public boolean canUseBonemeal(World var1, Random var2, BlockPos var3, IBlockState var4) {
      return true;
   }

   @Override
   public void grow(World var1, Random var2, BlockPos var3, IBlockState var4) {
      BlockPos ☃ = ☃.up();

      label38:
      for (int ☃x = 0; ☃x < 128; ☃x++) {
         BlockPos ☃xx = ☃;

         for (int ☃xxx = 0; ☃xxx < ☃x / 16; ☃xxx++) {
            ☃xx = ☃xx.add(☃.nextInt(3) - 1, (☃.nextInt(3) - 1) * ☃.nextInt(3) / 2, ☃.nextInt(3) - 1);
            if (☃.getBlockState(☃xx.down()).getBlock() != Blocks.GRASS || ☃.getBlockState(☃xx).isNormalCube()) {
               continue label38;
            }
         }

         if (☃.getBlockState(☃xx).getBlock().material == Material.AIR) {
            if (☃.nextInt(8) == 0) {
               BlockFlower.EnumFlowerType ☃xxxx = ☃.getBiome(☃xx).pickRandomFlower(☃, ☃xx);
               BlockFlower ☃xxxxx = ☃xxxx.getBlockType().getBlock();
               IBlockState ☃xxxxxx = ☃xxxxx.getDefaultState().withProperty(☃xxxxx.getTypeProperty(), ☃xxxx);
               if (☃xxxxx.canBlockStay(☃, ☃xx, ☃xxxxxx)) {
                  ☃.setBlockState(☃xx, ☃xxxxxx, 3);
               }
            } else {
               IBlockState ☃xxxx = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);
               if (Blocks.TALLGRASS.canBlockStay(☃, ☃xx, ☃xxxx)) {
                  ☃.setBlockState(☃xx, ☃xxxx, 3);
               }
            }
         }
      }
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT_MIPPED;
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
