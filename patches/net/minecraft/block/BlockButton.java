package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockButton extends BlockDirectional {
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   protected static final AxisAlignedBB AABB_DOWN_OFF = new AxisAlignedBB(0.3125, 0.875, 0.375, 0.6875, 1.0, 0.625);
   protected static final AxisAlignedBB AABB_UP_OFF = new AxisAlignedBB(0.3125, 0.0, 0.375, 0.6875, 0.125, 0.625);
   protected static final AxisAlignedBB AABB_NORTH_OFF = new AxisAlignedBB(0.3125, 0.375, 0.875, 0.6875, 0.625, 1.0);
   protected static final AxisAlignedBB AABB_SOUTH_OFF = new AxisAlignedBB(0.3125, 0.375, 0.0, 0.6875, 0.625, 0.125);
   protected static final AxisAlignedBB AABB_WEST_OFF = new AxisAlignedBB(0.875, 0.375, 0.3125, 1.0, 0.625, 0.6875);
   protected static final AxisAlignedBB AABB_EAST_OFF = new AxisAlignedBB(0.0, 0.375, 0.3125, 0.125, 0.625, 0.6875);
   protected static final AxisAlignedBB AABB_DOWN_ON = new AxisAlignedBB(0.3125, 0.9375, 0.375, 0.6875, 1.0, 0.625);
   protected static final AxisAlignedBB AABB_UP_ON = new AxisAlignedBB(0.3125, 0.0, 0.375, 0.6875, 0.0625, 0.625);
   protected static final AxisAlignedBB AABB_NORTH_ON = new AxisAlignedBB(0.3125, 0.375, 0.9375, 0.6875, 0.625, 1.0);
   protected static final AxisAlignedBB AABB_SOUTH_ON = new AxisAlignedBB(0.3125, 0.375, 0.0, 0.6875, 0.625, 0.0625);
   protected static final AxisAlignedBB AABB_WEST_ON = new AxisAlignedBB(0.9375, 0.375, 0.3125, 1.0, 0.625, 0.6875);
   protected static final AxisAlignedBB AABB_EAST_ON = new AxisAlignedBB(0.0, 0.375, 0.3125, 0.0625, 0.625, 0.6875);
   private final boolean wooden;

   protected BlockButton(boolean var1) {
      super(Material.CIRCUITS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.REDSTONE);
      this.wooden = ☃;
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return NULL_AABB;
   }

   @Override
   public int tickRate(World var1) {
      return this.wooden ? 30 : 20;
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
      return canPlaceBlock(☃, ☃, ☃);
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      for (EnumFacing ☃ : EnumFacing.values()) {
         if (canPlaceBlock(☃, ☃, ☃)) {
            return true;
         }
      }

      return false;
   }

   protected static boolean canPlaceBlock(World var0, BlockPos var1, EnumFacing var2) {
      BlockPos ☃ = ☃.offset(☃.getOpposite());
      IBlockState ☃x = ☃.getBlockState(☃);
      boolean ☃xx = ☃x.getBlockFaceShape(☃, ☃, ☃) == BlockFaceShape.SOLID;
      Block ☃xxx = ☃x.getBlock();
      return ☃ == EnumFacing.UP ? ☃xxx == Blocks.HOPPER || !isExceptionBlockForAttaching(☃xxx) && ☃xx : !isExceptBlockForAttachWithPiston(☃xxx) && ☃xx;
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return canPlaceBlock(☃, ☃, ☃)
         ? this.getDefaultState().withProperty(FACING, ☃).withProperty(POWERED, false)
         : this.getDefaultState().withProperty(FACING, EnumFacing.DOWN).withProperty(POWERED, false);
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (this.checkForDrop(☃, ☃, ☃) && !canPlaceBlock(☃, ☃, ☃.getValue(FACING))) {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);
      }
   }

   private boolean checkForDrop(World var1, BlockPos var2, IBlockState var3) {
      if (this.canPlaceBlockAt(☃, ☃)) {
         return true;
      } else {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);
         return false;
      }
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      EnumFacing ☃ = ☃.getValue(FACING);
      boolean ☃x = ☃.getValue(POWERED);
      switch (☃) {
         case EAST:
            return ☃x ? AABB_EAST_ON : AABB_EAST_OFF;
         case WEST:
            return ☃x ? AABB_WEST_ON : AABB_WEST_OFF;
         case SOUTH:
            return ☃x ? AABB_SOUTH_ON : AABB_SOUTH_OFF;
         case NORTH:
         default:
            return ☃x ? AABB_NORTH_ON : AABB_NORTH_OFF;
         case UP:
            return ☃x ? AABB_UP_ON : AABB_UP_OFF;
         case DOWN:
            return ☃x ? AABB_DOWN_ON : AABB_DOWN_OFF;
      }
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (☃.getValue(POWERED)) {
         return true;
      } else {
         ☃.setBlockState(☃, ☃.withProperty(POWERED, true), 3);
         ☃.markBlockRangeForRenderUpdate(☃, ☃);
         this.playClickSound(☃, ☃, ☃);
         this.notifyNeighbors(☃, ☃, ☃.getValue(FACING));
         ☃.scheduleUpdate(☃, this, this.tickRate(☃));
         return true;
      }
   }

   protected abstract void playClickSound(@Nullable EntityPlayer var1, World var2, BlockPos var3);

   protected abstract void playReleaseSound(World var1, BlockPos var2);

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if (☃.getValue(POWERED)) {
         this.notifyNeighbors(☃, ☃, ☃.getValue(FACING));
      }

      super.breakBlock(☃, ☃, ☃);
   }

   @Override
   public int getWeakPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return ☃.getValue(POWERED) ? 15 : 0;
   }

   @Override
   public int getStrongPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      if (!☃.getValue(POWERED)) {
         return 0;
      } else {
         return ☃.getValue(FACING) == ☃ ? 15 : 0;
      }
   }

   @Override
   public boolean canProvidePower(IBlockState var1) {
      return true;
   }

   @Override
   public void randomTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!☃.isRemote) {
         if (☃.getValue(POWERED)) {
            if (this.wooden) {
               this.checkPressed(☃, ☃, ☃);
            } else {
               ☃.setBlockState(☃, ☃.withProperty(POWERED, false));
               this.notifyNeighbors(☃, ☃, ☃.getValue(FACING));
               this.playReleaseSound(☃, ☃);
               ☃.markBlockRangeForRenderUpdate(☃, ☃);
            }
         }
      }
   }

   @Override
   public void onEntityCollision(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      if (!☃.isRemote) {
         if (this.wooden) {
            if (!☃.getValue(POWERED)) {
               this.checkPressed(☃, ☃, ☃);
            }
         }
      }
   }

   private void checkPressed(IBlockState var1, World var2, BlockPos var3) {
      List<? extends Entity> ☃ = ☃.getEntitiesWithinAABB(EntityArrow.class, ☃.getBoundingBox(☃, ☃).offset(☃));
      boolean ☃x = !☃.isEmpty();
      boolean ☃xx = ☃.getValue(POWERED);
      if (☃x && !☃xx) {
         ☃.setBlockState(☃, ☃.withProperty(POWERED, true));
         this.notifyNeighbors(☃, ☃, ☃.getValue(FACING));
         ☃.markBlockRangeForRenderUpdate(☃, ☃);
         this.playClickSound(null, ☃, ☃);
      }

      if (!☃x && ☃xx) {
         ☃.setBlockState(☃, ☃.withProperty(POWERED, false));
         this.notifyNeighbors(☃, ☃, ☃.getValue(FACING));
         ☃.markBlockRangeForRenderUpdate(☃, ☃);
         this.playReleaseSound(☃, ☃);
      }

      if (☃x) {
         ☃.scheduleUpdate(new BlockPos(☃), this, this.tickRate(☃));
      }
   }

   private void notifyNeighbors(World var1, BlockPos var2, EnumFacing var3) {
      ☃.notifyNeighborsOfStateChange(☃, this, false);
      ☃.notifyNeighborsOfStateChange(☃.offset(☃.getOpposite()), this, false);
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      EnumFacing ☃;
      switch (☃ & 7) {
         case 0:
            ☃ = EnumFacing.DOWN;
            break;
         case 1:
            ☃ = EnumFacing.EAST;
            break;
         case 2:
            ☃ = EnumFacing.WEST;
            break;
         case 3:
            ☃ = EnumFacing.SOUTH;
            break;
         case 4:
            ☃ = EnumFacing.NORTH;
            break;
         case 5:
         default:
            ☃ = EnumFacing.UP;
      }

      return this.getDefaultState().withProperty(FACING, ☃).withProperty(POWERED, (☃ & 8) > 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃;
      switch ((EnumFacing)☃.getValue(FACING)) {
         case EAST:
            ☃ = 1;
            break;
         case WEST:
            ☃ = 2;
            break;
         case SOUTH:
            ☃ = 3;
            break;
         case NORTH:
            ☃ = 4;
            break;
         case UP:
         default:
            ☃ = 5;
            break;
         case DOWN:
            ☃ = 0;
      }

      if (☃.getValue(POWERED)) {
         ☃ |= 8;
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
      return new BlockStateContainer(this, FACING, POWERED);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
