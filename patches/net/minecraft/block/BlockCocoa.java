package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCocoa extends BlockHorizontal implements IGrowable {
   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);
   protected static final AxisAlignedBB[] COCOA_EAST_AABB = new AxisAlignedBB[]{
      new AxisAlignedBB(0.6875, 0.4375, 0.375, 0.9375, 0.75, 0.625),
      new AxisAlignedBB(0.5625, 0.3125, 0.3125, 0.9375, 0.75, 0.6875),
      new AxisAlignedBB(0.4375, 0.1875, 0.25, 0.9375, 0.75, 0.75)
   };
   protected static final AxisAlignedBB[] COCOA_WEST_AABB = new AxisAlignedBB[]{
      new AxisAlignedBB(0.0625, 0.4375, 0.375, 0.3125, 0.75, 0.625),
      new AxisAlignedBB(0.0625, 0.3125, 0.3125, 0.4375, 0.75, 0.6875),
      new AxisAlignedBB(0.0625, 0.1875, 0.25, 0.5625, 0.75, 0.75)
   };
   protected static final AxisAlignedBB[] COCOA_NORTH_AABB = new AxisAlignedBB[]{
      new AxisAlignedBB(0.375, 0.4375, 0.0625, 0.625, 0.75, 0.3125),
      new AxisAlignedBB(0.3125, 0.3125, 0.0625, 0.6875, 0.75, 0.4375),
      new AxisAlignedBB(0.25, 0.1875, 0.0625, 0.75, 0.75, 0.5625)
   };
   protected static final AxisAlignedBB[] COCOA_SOUTH_AABB = new AxisAlignedBB[]{
      new AxisAlignedBB(0.375, 0.4375, 0.6875, 0.625, 0.75, 0.9375),
      new AxisAlignedBB(0.3125, 0.3125, 0.5625, 0.6875, 0.75, 0.9375),
      new AxisAlignedBB(0.25, 0.1875, 0.4375, 0.75, 0.75, 0.9375)
   };

   public BlockCocoa() {
      super(Material.PLANTS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(AGE, 0));
      this.setTickRandomly(true);
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!this.canBlockStay(☃, ☃, ☃)) {
         this.dropBlock(☃, ☃, ☃);
      } else if (☃.rand.nextInt(5) == 0) {
         int ☃ = ☃.getValue(AGE);
         if (☃ < 2) {
            ☃.setBlockState(☃, ☃.withProperty(AGE, ☃ + 1), 2);
         }
      }
   }

   public boolean canBlockStay(World var1, BlockPos var2, IBlockState var3) {
      ☃ = ☃.offset(☃.getValue(FACING));
      IBlockState ☃ = ☃.getBlockState(☃);
      return ☃.getBlock() == Blocks.LOG && ☃.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE;
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
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      int ☃ = ☃.getValue(AGE);
      switch ((EnumFacing)☃.getValue(FACING)) {
         case SOUTH:
            return COCOA_SOUTH_AABB[☃];
         case NORTH:
         default:
            return COCOA_NORTH_AABB[☃];
         case WEST:
            return COCOA_WEST_AABB[☃];
         case EAST:
            return COCOA_EAST_AABB[☃];
      }
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
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      EnumFacing ☃ = EnumFacing.fromAngle(☃.rotationYaw);
      ☃.setBlockState(☃, ☃.withProperty(FACING, ☃), 2);
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      if (!☃.getAxis().isHorizontal()) {
         ☃ = EnumFacing.NORTH;
      }

      return this.getDefaultState().withProperty(FACING, ☃.getOpposite()).withProperty(AGE, 0);
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!this.canBlockStay(☃, ☃, ☃)) {
         this.dropBlock(☃, ☃, ☃);
      }
   }

   private void dropBlock(World var1, BlockPos var2, IBlockState var3) {
      ☃.setBlockState(☃, Blocks.AIR.getDefaultState(), 3);
      this.dropBlockAsItem(☃, ☃, ☃, 0);
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      int ☃ = ☃.getValue(AGE);
      int ☃x = 1;
      if (☃ >= 2) {
         ☃x = 3;
      }

      for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
         spawnAsEntity(☃, ☃, new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()));
      }
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage());
   }

   @Override
   public boolean canGrow(World var1, BlockPos var2, IBlockState var3, boolean var4) {
      return ☃.getValue(AGE) < 2;
   }

   @Override
   public boolean canUseBonemeal(World var1, Random var2, BlockPos var3, IBlockState var4) {
      return true;
   }

   @Override
   public void grow(World var1, Random var2, BlockPos var3, IBlockState var4) {
      ☃.setBlockState(☃, ☃.withProperty(AGE, ☃.getValue(AGE) + 1), 2);
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(☃)).withProperty(AGE, (☃ & 15) >> 2);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getHorizontalIndex();
      return ☃ | ☃.getValue(AGE) << 2;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING, AGE);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
