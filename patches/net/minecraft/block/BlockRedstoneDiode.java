package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRedstoneDiode extends BlockHorizontal {
   protected static final AxisAlignedBB REDSTONE_DIODE_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
   protected final boolean isRepeaterPowered;

   protected BlockRedstoneDiode(boolean var1) {
      super(Material.CIRCUITS);
      this.isRepeaterPowered = ☃;
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return REDSTONE_DIODE_AABB;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return ☃.getBlockState(☃.down()).isTopSolid() ? super.canPlaceBlockAt(☃, ☃) : false;
   }

   public boolean canBlockStay(World var1, BlockPos var2) {
      return ☃.getBlockState(☃.down()).isTopSolid();
   }

   @Override
   public void randomTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!this.isLocked(☃, ☃, ☃)) {
         boolean ☃ = this.shouldBePowered(☃, ☃, ☃);
         if (this.isRepeaterPowered && !☃) {
            ☃.setBlockState(☃, this.getUnpoweredState(☃), 2);
         } else if (!this.isRepeaterPowered) {
            ☃.setBlockState(☃, this.getPoweredState(☃), 2);
            if (!☃) {
               ☃.updateBlockTick(☃, this.getPoweredState(☃).getBlock(), this.getTickDelay(☃), -1);
            }
         }
      }
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return ☃.getAxis() != EnumFacing.Axis.Y;
   }

   protected boolean isPowered(IBlockState var1) {
      return this.isRepeaterPowered;
   }

   @Override
   public int getStrongPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return ☃.getWeakPower(☃, ☃, ☃);
   }

   @Override
   public int getWeakPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      if (!this.isPowered(☃)) {
         return 0;
      } else {
         return ☃.getValue(FACING) == ☃ ? this.getActiveSignal(☃, ☃, ☃) : 0;
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (this.canBlockStay(☃, ☃)) {
         this.updateState(☃, ☃, ☃);
      } else {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);

         for (EnumFacing ☃ : EnumFacing.values()) {
            ☃.notifyNeighborsOfStateChange(☃.offset(☃), this, false);
         }
      }
   }

   protected void updateState(World var1, BlockPos var2, IBlockState var3) {
      if (!this.isLocked(☃, ☃, ☃)) {
         boolean ☃ = this.shouldBePowered(☃, ☃, ☃);
         if (this.isRepeaterPowered != ☃ && !☃.isBlockTickPending(☃, this)) {
            int ☃x = -1;
            if (this.isFacingTowardsRepeater(☃, ☃, ☃)) {
               ☃x = -3;
            } else if (this.isRepeaterPowered) {
               ☃x = -2;
            }

            ☃.updateBlockTick(☃, this, this.getDelay(☃), ☃x);
         }
      }
   }

   public boolean isLocked(IBlockAccess var1, BlockPos var2, IBlockState var3) {
      return false;
   }

   protected boolean shouldBePowered(World var1, BlockPos var2, IBlockState var3) {
      return this.calculateInputStrength(☃, ☃, ☃) > 0;
   }

   protected int calculateInputStrength(World var1, BlockPos var2, IBlockState var3) {
      EnumFacing ☃ = ☃.getValue(FACING);
      BlockPos ☃x = ☃.offset(☃);
      int ☃xx = ☃.getRedstonePower(☃x, ☃);
      if (☃xx >= 15) {
         return ☃xx;
      } else {
         IBlockState ☃xxx = ☃.getBlockState(☃x);
         return Math.max(☃xx, ☃xxx.getBlock() == Blocks.REDSTONE_WIRE ? ☃xxx.getValue(BlockRedstoneWire.POWER) : 0);
      }
   }

   protected int getPowerOnSides(IBlockAccess var1, BlockPos var2, IBlockState var3) {
      EnumFacing ☃ = ☃.getValue(FACING);
      EnumFacing ☃x = ☃.rotateY();
      EnumFacing ☃xx = ☃.rotateYCCW();
      return Math.max(this.getPowerOnSide(☃, ☃.offset(☃x), ☃x), this.getPowerOnSide(☃, ☃.offset(☃xx), ☃xx));
   }

   protected int getPowerOnSide(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      IBlockState ☃ = ☃.getBlockState(☃);
      Block ☃x = ☃.getBlock();
      if (this.isAlternateInput(☃)) {
         if (☃x == Blocks.REDSTONE_BLOCK) {
            return 15;
         } else {
            return ☃x == Blocks.REDSTONE_WIRE ? ☃.getValue(BlockRedstoneWire.POWER) : ☃.getStrongPower(☃, ☃);
         }
      } else {
         return 0;
      }
   }

   @Override
   public boolean canProvidePower(IBlockState var1) {
      return true;
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, ☃.getHorizontalFacing().getOpposite());
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      if (this.shouldBePowered(☃, ☃, ☃)) {
         ☃.scheduleUpdate(☃, this, 1);
      }
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      this.notifyNeighbors(☃, ☃, ☃);
   }

   protected void notifyNeighbors(World var1, BlockPos var2, IBlockState var3) {
      EnumFacing ☃ = ☃.getValue(FACING);
      BlockPos ☃x = ☃.offset(☃.getOpposite());
      ☃.neighborChanged(☃x, this, ☃);
      ☃.notifyNeighborsOfStateExcept(☃x, this, ☃);
   }

   @Override
   public void onPlayerDestroy(World var1, BlockPos var2, IBlockState var3) {
      if (this.isRepeaterPowered) {
         for (EnumFacing ☃ : EnumFacing.values()) {
            ☃.notifyNeighborsOfStateChange(☃.offset(☃), this, false);
         }
      }

      super.onPlayerDestroy(☃, ☃, ☃);
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   protected boolean isAlternateInput(IBlockState var1) {
      return ☃.canProvidePower();
   }

   protected int getActiveSignal(IBlockAccess var1, BlockPos var2, IBlockState var3) {
      return 15;
   }

   public static boolean isDiode(IBlockState var0) {
      return Blocks.UNPOWERED_REPEATER.isSameDiode(☃) || Blocks.UNPOWERED_COMPARATOR.isSameDiode(☃);
   }

   public boolean isSameDiode(IBlockState var1) {
      Block ☃ = ☃.getBlock();
      return ☃ == this.getPoweredState(this.getDefaultState()).getBlock() || ☃ == this.getUnpoweredState(this.getDefaultState()).getBlock();
   }

   public boolean isFacingTowardsRepeater(World var1, BlockPos var2, IBlockState var3) {
      EnumFacing ☃ = ☃.getValue(FACING).getOpposite();
      BlockPos ☃x = ☃.offset(☃);
      return isDiode(☃.getBlockState(☃x)) ? ☃.getBlockState(☃x).getValue(FACING) != ☃ : false;
   }

   protected int getTickDelay(IBlockState var1) {
      return this.getDelay(☃);
   }

   protected abstract int getDelay(IBlockState var1);

   protected abstract IBlockState getPoweredState(IBlockState var1);

   protected abstract IBlockState getUnpoweredState(IBlockState var1);

   @Override
   public boolean isAssociatedBlock(Block var1) {
      return this.isSameDiode(☃.getDefaultState());
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return ☃ == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }
}
