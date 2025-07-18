package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockBasePressurePlate extends Block {
   protected static final AxisAlignedBB PRESSED_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.03125, 0.9375);
   protected static final AxisAlignedBB UNPRESSED_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.0625, 0.9375);
   protected static final AxisAlignedBB PRESSURE_AABB = new AxisAlignedBB(0.125, 0.0, 0.125, 0.875, 0.25, 0.875);

   protected BlockBasePressurePlate(Material var1) {
      this(☃, ☃.getMaterialMapColor());
   }

   protected BlockBasePressurePlate(Material var1, MapColor var2) {
      super(☃, ☃);
      this.setCreativeTab(CreativeTabs.REDSTONE);
      this.setTickRandomly(true);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      boolean ☃ = this.getRedstoneStrength(☃) > 0;
      return ☃ ? PRESSED_AABB : UNPRESSED_AABB;
   }

   @Override
   public int tickRate(World var1) {
      return 20;
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

   @Override
   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return true;
   }

   @Override
   public boolean canSpawnInBlock() {
      return true;
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return this.canBePlacedOn(☃, ☃.down());
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!this.canBePlacedOn(☃, ☃.down())) {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);
      }
   }

   private boolean canBePlacedOn(World var1, BlockPos var2) {
      return ☃.getBlockState(☃).isTopSolid() || ☃.getBlockState(☃).getBlock() instanceof BlockFence;
   }

   @Override
   public void randomTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!☃.isRemote) {
         int ☃ = this.getRedstoneStrength(☃);
         if (☃ > 0) {
            this.updateState(☃, ☃, ☃, ☃);
         }
      }
   }

   @Override
   public void onEntityCollision(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      if (!☃.isRemote) {
         int ☃ = this.getRedstoneStrength(☃);
         if (☃ == 0) {
            this.updateState(☃, ☃, ☃, ☃);
         }
      }
   }

   protected void updateState(World var1, BlockPos var2, IBlockState var3, int var4) {
      int ☃ = this.computeRedstoneStrength(☃, ☃);
      boolean ☃x = ☃ > 0;
      boolean ☃xx = ☃ > 0;
      if (☃ != ☃) {
         ☃ = this.setRedstoneStrength(☃, ☃);
         ☃.setBlockState(☃, ☃, 2);
         this.updateNeighbors(☃, ☃);
         ☃.markBlockRangeForRenderUpdate(☃, ☃);
      }

      if (!☃xx && ☃x) {
         this.playClickOffSound(☃, ☃);
      } else if (☃xx && !☃x) {
         this.playClickOnSound(☃, ☃);
      }

      if (☃xx) {
         ☃.scheduleUpdate(new BlockPos(☃), this, this.tickRate(☃));
      }
   }

   protected abstract void playClickOnSound(World var1, BlockPos var2);

   protected abstract void playClickOffSound(World var1, BlockPos var2);

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if (this.getRedstoneStrength(☃) > 0) {
         this.updateNeighbors(☃, ☃);
      }

      super.breakBlock(☃, ☃, ☃);
   }

   protected void updateNeighbors(World var1, BlockPos var2) {
      ☃.notifyNeighborsOfStateChange(☃, this, false);
      ☃.notifyNeighborsOfStateChange(☃.down(), this, false);
   }

   @Override
   public int getWeakPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return this.getRedstoneStrength(☃);
   }

   @Override
   public int getStrongPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return ☃ == EnumFacing.UP ? this.getRedstoneStrength(☃) : 0;
   }

   @Override
   public boolean canProvidePower(IBlockState var1) {
      return true;
   }

   @Override
   public EnumPushReaction getPushReaction(IBlockState var1) {
      return EnumPushReaction.DESTROY;
   }

   protected abstract int computeRedstoneStrength(World var1, BlockPos var2);

   protected abstract int getRedstoneStrength(IBlockState var1);

   protected abstract IBlockState setRedstoneStrength(IBlockState var1, int var2);

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
