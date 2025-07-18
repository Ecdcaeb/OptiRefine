package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCactus extends Block {
   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
   protected static final AxisAlignedBB CACTUS_COLLISION_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.9375, 0.9375);
   protected static final AxisAlignedBB CACTUS_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375);

   protected BlockCactus() {
      super(Material.CACTUS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      BlockPos ☃ = ☃.up();
      if (☃.isAirBlock(☃)) {
         int ☃x = 1;

         while (☃.getBlockState(☃.down(☃x)).getBlock() == this) {
            ☃x++;
         }

         if (☃x < 3) {
            int ☃xx = ☃.getValue(AGE);
            if (☃xx == 15) {
               ☃.setBlockState(☃, this.getDefaultState());
               IBlockState ☃xxx = ☃.withProperty(AGE, 0);
               ☃.setBlockState(☃, ☃xxx, 4);
               ☃xxx.neighborChanged(☃, ☃, this, ☃);
            } else {
               ☃.setBlockState(☃, ☃.withProperty(AGE, ☃xx + 1), 4);
            }
         }
      }
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return CACTUS_COLLISION_AABB;
   }

   @Override
   public AxisAlignedBB getSelectedBoundingBox(IBlockState var1, World var2, BlockPos var3) {
      return CACTUS_AABB.offset(☃);
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return super.canPlaceBlockAt(☃, ☃) ? this.canBlockStay(☃, ☃) : false;
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!this.canBlockStay(☃, ☃)) {
         ☃.destroyBlock(☃, true);
      }
   }

   public boolean canBlockStay(World var1, BlockPos var2) {
      for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL) {
         Material ☃x = ☃.getBlockState(☃.offset(☃)).getMaterial();
         if (☃x.isSolid() || ☃x == Material.LAVA) {
            return false;
         }
      }

      Block ☃x = ☃.getBlockState(☃.down()).getBlock();
      return ☃x == Blocks.CACTUS || ☃x == Blocks.SAND && !☃.getBlockState(☃.up()).getMaterial().isLiquid();
   }

   @Override
   public void onEntityCollision(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      ☃.attackEntityFrom(DamageSource.CACTUS, 1.0F);
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
