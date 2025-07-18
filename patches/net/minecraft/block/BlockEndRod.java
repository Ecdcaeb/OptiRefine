package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEndRod extends BlockDirectional {
   protected static final AxisAlignedBB END_ROD_VERTICAL_AABB = new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
   protected static final AxisAlignedBB END_ROD_NS_AABB = new AxisAlignedBB(0.375, 0.375, 0.0, 0.625, 0.625, 1.0);
   protected static final AxisAlignedBB END_ROD_EW_AABB = new AxisAlignedBB(0.0, 0.375, 0.375, 1.0, 0.625, 0.625);

   protected BlockEndRod() {
      super(Material.CIRCUITS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      return ☃.withProperty(FACING, ☃.rotate(☃.getValue(FACING)));
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      return ☃.withProperty(FACING, ☃.mirror(☃.getValue(FACING)));
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      switch (☃.getValue(FACING).getAxis()) {
         case X:
         default:
            return END_ROD_EW_AABB;
         case Z:
            return END_ROD_NS_AABB;
         case Y:
            return END_ROD_VERTICAL_AABB;
      }
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
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return true;
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      IBlockState ☃ = ☃.getBlockState(☃.offset(☃.getOpposite()));
      if (☃.getBlock() == Blocks.END_ROD) {
         EnumFacing ☃x = ☃.getValue(FACING);
         if (☃x == ☃) {
            return this.getDefaultState().withProperty(FACING, ☃.getOpposite());
         }
      }

      return this.getDefaultState().withProperty(FACING, ☃);
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      EnumFacing ☃ = ☃.getValue(FACING);
      double ☃x = ☃.getX() + 0.55 - ☃.nextFloat() * 0.1F;
      double ☃xx = ☃.getY() + 0.55 - ☃.nextFloat() * 0.1F;
      double ☃xxx = ☃.getZ() + 0.55 - ☃.nextFloat() * 0.1F;
      double ☃xxxx = 0.4F - (☃.nextFloat() + ☃.nextFloat()) * 0.4F;
      if (☃.nextInt(5) == 0) {
         ☃.spawnParticle(
            EnumParticleTypes.END_ROD,
            ☃x + ☃.getXOffset() * ☃xxxx,
            ☃xx + ☃.getYOffset() * ☃xxxx,
            ☃xxx + ☃.getZOffset() * ☃xxxx,
            ☃.nextGaussian() * 0.005,
            ☃.nextGaussian() * 0.005,
            ☃.nextGaussian() * 0.005
         );
      }
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      IBlockState ☃ = this.getDefaultState();
      return ☃.withProperty(FACING, EnumFacing.byIndex(☃));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(FACING).getIndex();
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING);
   }

   @Override
   public EnumPushReaction getPushReaction(IBlockState var1) {
      return EnumPushReaction.NORMAL;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
