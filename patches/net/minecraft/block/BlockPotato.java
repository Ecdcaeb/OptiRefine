package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPotato extends BlockCrops {
   private static final AxisAlignedBB[] POTATO_AABB = new AxisAlignedBB[]{
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.1875, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.3125, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.375, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.4375, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5625, 1.0)
   };

   @Override
   protected Item getSeed() {
      return Items.POTATO;
   }

   @Override
   protected Item getCrop() {
      return Items.POTATO;
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      super.dropBlockAsItemWithChance(☃, ☃, ☃, ☃, ☃);
      if (!☃.isRemote) {
         if (this.isMaxAge(☃) && ☃.rand.nextInt(50) == 0) {
            spawnAsEntity(☃, ☃, new ItemStack(Items.POISONOUS_POTATO));
         }
      }
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return POTATO_AABB[☃.getValue(this.getAgeProperty())];
   }
}
