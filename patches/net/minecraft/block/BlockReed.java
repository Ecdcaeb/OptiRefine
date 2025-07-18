package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockReed extends Block {
   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
   protected static final AxisAlignedBB REED_AABB = new AxisAlignedBB(0.125, 0.0, 0.125, 0.875, 1.0, 0.875);

   protected BlockReed() {
      super(Material.PLANTS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
      this.setTickRandomly(true);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return REED_AABB;
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (☃.getBlockState(☃.down()).getBlock() == Blocks.REEDS || this.checkForDrop(☃, ☃, ☃)) {
         if (☃.isAirBlock(☃.up())) {
            int ☃ = 1;

            while (☃.getBlockState(☃.down(☃)).getBlock() == this) {
               ☃++;
            }

            if (☃ < 3) {
               int ☃x = ☃.getValue(AGE);
               if (☃x == 15) {
                  ☃.setBlockState(☃.up(), this.getDefaultState());
                  ☃.setBlockState(☃, ☃.withProperty(AGE, 0), 4);
               } else {
                  ☃.setBlockState(☃, ☃.withProperty(AGE, ☃x + 1), 4);
               }
            }
         }
      }
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      Block ☃ = ☃.getBlockState(☃.down()).getBlock();
      if (☃ == this) {
         return true;
      } else if (☃ != Blocks.GRASS && ☃ != Blocks.DIRT && ☃ != Blocks.SAND) {
         return false;
      } else {
         BlockPos ☃x = ☃.down();

         for (EnumFacing ☃xx : EnumFacing.Plane.HORIZONTAL) {
            IBlockState ☃xxx = ☃.getBlockState(☃x.offset(☃xx));
            if (☃xxx.getMaterial() == Material.WATER || ☃xxx.getBlock() == Blocks.FROSTED_ICE) {
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      this.checkForDrop(☃, ☃, ☃);
   }

   protected final boolean checkForDrop(World var1, BlockPos var2, IBlockState var3) {
      if (this.canBlockStay(☃, ☃)) {
         return true;
      } else {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);
         return false;
      }
   }

   public boolean canBlockStay(World var1, BlockPos var2) {
      return this.canPlaceBlockAt(☃, ☃);
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return NULL_AABB;
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.REEDS;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Items.REEDS);
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(AGE, ☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(AGE);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, AGE);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
