package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
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

public class BlockTorch extends Block {
   public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>() {
      public boolean apply(@Nullable EnumFacing var1) {
         return ☃ != EnumFacing.DOWN;
      }
   });
   protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.4F, 0.0, 0.4F, 0.6F, 0.6F, 0.6F);
   protected static final AxisAlignedBB TORCH_NORTH_AABB = new AxisAlignedBB(0.35F, 0.2F, 0.7F, 0.65F, 0.8F, 1.0);
   protected static final AxisAlignedBB TORCH_SOUTH_AABB = new AxisAlignedBB(0.35F, 0.2F, 0.0, 0.65F, 0.8F, 0.3F);
   protected static final AxisAlignedBB TORCH_WEST_AABB = new AxisAlignedBB(0.7F, 0.2F, 0.35F, 1.0, 0.8F, 0.65F);
   protected static final AxisAlignedBB TORCH_EAST_AABB = new AxisAlignedBB(0.0, 0.2F, 0.35F, 0.3F, 0.8F, 0.65F);

   protected BlockTorch() {
      super(Material.CIRCUITS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      switch ((EnumFacing)☃.getValue(FACING)) {
         case EAST:
            return TORCH_EAST_AABB;
         case WEST:
            return TORCH_WEST_AABB;
         case SOUTH:
            return TORCH_SOUTH_AABB;
         case NORTH:
            return TORCH_NORTH_AABB;
         default:
            return STANDING_AABB;
      }
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return NULL_AABB;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   private boolean canPlaceOn(World var1, BlockPos var2) {
      Block ☃ = ☃.getBlockState(☃).getBlock();
      boolean ☃x = ☃ == Blocks.END_GATEWAY || ☃ == Blocks.LIT_PUMPKIN;
      if (☃.getBlockState(☃).isTopSolid()) {
         return !☃x;
      } else {
         boolean ☃xx = ☃ instanceof BlockFence || ☃ == Blocks.GLASS || ☃ == Blocks.COBBLESTONE_WALL || ☃ == Blocks.STAINED_GLASS;
         return ☃xx && !☃x;
      }
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      for (EnumFacing ☃ : FACING.getAllowedValues()) {
         if (this.canPlaceAt(☃, ☃, ☃)) {
            return true;
         }
      }

      return false;
   }

   private boolean canPlaceAt(World var1, BlockPos var2, EnumFacing var3) {
      BlockPos ☃ = ☃.offset(☃.getOpposite());
      IBlockState ☃x = ☃.getBlockState(☃);
      Block ☃xx = ☃x.getBlock();
      BlockFaceShape ☃xxx = ☃x.getBlockFaceShape(☃, ☃, ☃);
      if (☃.equals(EnumFacing.UP) && this.canPlaceOn(☃, ☃)) {
         return true;
      } else {
         return ☃ != EnumFacing.UP && ☃ != EnumFacing.DOWN ? !isExceptBlockForAttachWithPiston(☃xx) && ☃xxx == BlockFaceShape.SOLID : false;
      }
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      if (this.canPlaceAt(☃, ☃, ☃)) {
         return this.getDefaultState().withProperty(FACING, ☃);
      } else {
         for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL) {
            if (this.canPlaceAt(☃, ☃, ☃)) {
               return this.getDefaultState().withProperty(FACING, ☃);
            }
         }

         return this.getDefaultState();
      }
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      this.checkForDrop(☃, ☃, ☃);
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      this.onNeighborChangeInternal(☃, ☃, ☃);
   }

   protected boolean onNeighborChangeInternal(World var1, BlockPos var2, IBlockState var3) {
      if (!this.checkForDrop(☃, ☃, ☃)) {
         return true;
      } else {
         EnumFacing ☃ = ☃.getValue(FACING);
         EnumFacing.Axis ☃x = ☃.getAxis();
         EnumFacing ☃xx = ☃.getOpposite();
         BlockPos ☃xxx = ☃.offset(☃xx);
         boolean ☃xxxx = false;
         if (☃x.isHorizontal() && ☃.getBlockState(☃xxx).getBlockFaceShape(☃, ☃xxx, ☃) != BlockFaceShape.SOLID) {
            ☃xxxx = true;
         } else if (☃x.isVertical() && !this.canPlaceOn(☃, ☃xxx)) {
            ☃xxxx = true;
         }

         if (☃xxxx) {
            this.dropBlockAsItem(☃, ☃, ☃, 0);
            ☃.setBlockToAir(☃);
            return true;
         } else {
            return false;
         }
      }
   }

   protected boolean checkForDrop(World var1, BlockPos var2, IBlockState var3) {
      if (☃.getBlock() == this && this.canPlaceAt(☃, ☃, ☃.getValue(FACING))) {
         return true;
      } else {
         if (☃.getBlockState(☃).getBlock() == this) {
            this.dropBlockAsItem(☃, ☃, ☃, 0);
            ☃.setBlockToAir(☃);
         }

         return false;
      }
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      EnumFacing ☃ = ☃.getValue(FACING);
      double ☃x = ☃.getX() + 0.5;
      double ☃xx = ☃.getY() + 0.7;
      double ☃xxx = ☃.getZ() + 0.5;
      double ☃xxxx = 0.22;
      double ☃xxxxx = 0.27;
      if (☃.getAxis().isHorizontal()) {
         EnumFacing ☃xxxxxx = ☃.getOpposite();
         ☃.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ☃x + 0.27 * ☃xxxxxx.getXOffset(), ☃xx + 0.22, ☃xxx + 0.27 * ☃xxxxxx.getZOffset(), 0.0, 0.0, 0.0);
         ☃.spawnParticle(EnumParticleTypes.FLAME, ☃x + 0.27 * ☃xxxxxx.getXOffset(), ☃xx + 0.22, ☃xxx + 0.27 * ☃xxxxxx.getZOffset(), 0.0, 0.0, 0.0);
      } else {
         ☃.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ☃x, ☃xx, ☃xxx, 0.0, 0.0, 0.0);
         ☃.spawnParticle(EnumParticleTypes.FLAME, ☃x, ☃xx, ☃xxx, 0.0, 0.0, 0.0);
      }
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      IBlockState ☃ = this.getDefaultState();
      switch (☃) {
         case 1:
            ☃ = ☃.withProperty(FACING, EnumFacing.EAST);
            break;
         case 2:
            ☃ = ☃.withProperty(FACING, EnumFacing.WEST);
            break;
         case 3:
            ☃ = ☃.withProperty(FACING, EnumFacing.SOUTH);
            break;
         case 4:
            ☃ = ☃.withProperty(FACING, EnumFacing.NORTH);
            break;
         case 5:
         default:
            ☃ = ☃.withProperty(FACING, EnumFacing.UP);
      }

      return ☃;
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      switch ((EnumFacing)☃.getValue(FACING)) {
         case EAST:
            ☃ |= 1;
            break;
         case WEST:
            ☃ |= 2;
            break;
         case SOUTH:
            ☃ |= 3;
            break;
         case NORTH:
            ☃ |= 4;
            break;
         case DOWN:
         case UP:
         default:
            ☃ |= 5;
      }

      return ☃;
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
