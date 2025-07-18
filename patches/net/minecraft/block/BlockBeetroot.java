package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBeetroot extends BlockCrops {
   public static final PropertyInteger BEETROOT_AGE = PropertyInteger.create("age", 0, 3);
   private static final AxisAlignedBB[] BEETROOT_AABB = new AxisAlignedBB[]{
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.375, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0)
   };

   @Override
   protected PropertyInteger getAgeProperty() {
      return BEETROOT_AGE;
   }

   @Override
   public int getMaxAge() {
      return 3;
   }

   @Override
   protected Item getSeed() {
      return Items.BEETROOT_SEEDS;
   }

   @Override
   protected Item getCrop() {
      return Items.BEETROOT;
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (☃.nextInt(3) == 0) {
         this.checkAndDropBlock(☃, ☃, ☃);
      } else {
         super.updateTick(☃, ☃, ☃, ☃);
      }
   }

   @Override
   protected int getBonemealAgeIncrease(World var1) {
      return super.getBonemealAgeIncrease(☃) / 3;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, BEETROOT_AGE);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return BEETROOT_AABB[☃.getValue(this.getAgeProperty())];
   }
}
