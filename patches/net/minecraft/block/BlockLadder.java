package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLadder extends Block {
   public static final PropertyDirection FACING = BlockHorizontal.FACING;
   protected static final AxisAlignedBB LADDER_EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.1875, 1.0, 1.0);
   protected static final AxisAlignedBB LADDER_WEST_AABB = new AxisAlignedBB(0.8125, 0.0, 0.0, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB LADDER_SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.1875);
   protected static final AxisAlignedBB LADDER_NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.8125, 1.0, 1.0, 1.0);

   protected BlockLadder() {
      super(Material.CIRCUITS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      switch ((EnumFacing)☃.getValue(FACING)) {
         case NORTH:
            return LADDER_NORTH_AABB;
         case SOUTH:
            return LADDER_SOUTH_AABB;
         case WEST:
            return LADDER_WEST_AABB;
         case EAST:
         default:
            return LADDER_EAST_AABB;
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
   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
      if (this.canAttachTo(☃, ☃.west(), ☃)) {
         return true;
      } else if (this.canAttachTo(☃, ☃.east(), ☃)) {
         return true;
      } else {
         return this.canAttachTo(☃, ☃.north(), ☃) ? true : this.canAttachTo(☃, ☃.south(), ☃);
      }
   }

   private boolean canAttachTo(World var1, BlockPos var2, EnumFacing var3) {
      IBlockState ☃ = ☃.getBlockState(☃);
      boolean ☃x = isExceptBlockForAttachWithPiston(☃.getBlock());
      return !☃x && ☃.getBlockFaceShape(☃, ☃, ☃) == BlockFaceShape.SOLID && !☃.canProvidePower();
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      if (☃.getAxis().isHorizontal() && this.canAttachTo(☃, ☃.offset(☃.getOpposite()), ☃)) {
         return this.getDefaultState().withProperty(FACING, ☃);
      } else {
         for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL) {
            if (this.canAttachTo(☃, ☃.offset(☃.getOpposite()), ☃)) {
               return this.getDefaultState().withProperty(FACING, ☃);
            }
         }

         return this.getDefaultState();
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      EnumFacing ☃ = ☃.getValue(FACING);
      if (!this.canAttachTo(☃, ☃.offset(☃.getOpposite()), ☃)) {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);
      }

      super.neighborChanged(☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      EnumFacing ☃ = EnumFacing.byIndex(☃);
      if (☃.getAxis() == EnumFacing.Axis.Y) {
         ☃ = EnumFacing.NORTH;
      }

      return this.getDefaultState().withProperty(FACING, ☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(FACING).getIndex();
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      return ☃.withProperty(FACING, ☃.rotate(☃.getValue(FACING)));
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      return ☃.withRotation(☃.toRotation(☃.getValue(FACING)));
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
