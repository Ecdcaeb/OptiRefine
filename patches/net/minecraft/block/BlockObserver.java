package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockObserver extends BlockDirectional {
   public static final PropertyBool POWERED = PropertyBool.create("powered");

   public BlockObserver() {
      super(Material.ROCK);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH).withProperty(POWERED, false));
      this.setCreativeTab(CreativeTabs.REDSTONE);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING, POWERED);
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
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (☃.getValue(POWERED)) {
         ☃.setBlockState(☃, ☃.withProperty(POWERED, false), 2);
      } else {
         ☃.setBlockState(☃, ☃.withProperty(POWERED, true), 2);
         ☃.scheduleUpdate(☃, this, 2);
      }

      this.updateNeighborsInFront(☃, ☃, ☃);
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
   }

   public void observedNeighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.isRemote && ☃.offset(☃.getValue(FACING)).equals(☃)) {
         this.startSignal(☃, ☃, ☃);
      }
   }

   private void startSignal(IBlockState var1, World var2, BlockPos var3) {
      if (!☃.getValue(POWERED)) {
         if (!☃.isUpdateScheduled(☃, this)) {
            ☃.scheduleUpdate(☃, this, 2);
         }
      }
   }

   protected void updateNeighborsInFront(World var1, BlockPos var2, IBlockState var3) {
      EnumFacing ☃ = ☃.getValue(FACING);
      BlockPos ☃x = ☃.offset(☃.getOpposite());
      ☃.neighborChanged(☃x, this, ☃);
      ☃.notifyNeighborsOfStateExcept(☃x, this, ☃);
   }

   @Override
   public boolean canProvidePower(IBlockState var1) {
      return true;
   }

   @Override
   public int getStrongPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return ☃.getWeakPower(☃, ☃, ☃);
   }

   @Override
   public int getWeakPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return ☃.getValue(POWERED) && ☃.getValue(FACING) == ☃ ? 15 : 0;
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!☃.isRemote) {
         if (☃.getValue(POWERED)) {
            this.updateTick(☃, ☃, ☃, ☃.rand);
         }

         this.startSignal(☃, ☃, ☃);
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if (☃.getValue(POWERED) && ☃.isUpdateScheduled(☃, this)) {
         this.updateNeighborsInFront(☃, ☃, ☃.withProperty(POWERED, false));
      }
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(☃, ☃).getOpposite());
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getIndex();
      if (☃.getValue(POWERED)) {
         ☃ |= 8;
      }

      return ☃;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(☃ & 7));
   }
}
