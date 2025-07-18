package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNetherWart extends BlockBush {
   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);
   private static final AxisAlignedBB[] NETHER_WART_AABB = new AxisAlignedBB[]{
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.3125, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.6875, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.875, 1.0)
   };

   protected BlockNetherWart() {
      super(Material.PLANTS, MapColor.RED);
      this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
      this.setTickRandomly(true);
      this.setCreativeTab(null);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return NETHER_WART_AABB[☃.getValue(AGE)];
   }

   @Override
   protected boolean canSustainBush(IBlockState var1) {
      return ☃.getBlock() == Blocks.SOUL_SAND;
   }

   @Override
   public boolean canBlockStay(World var1, BlockPos var2, IBlockState var3) {
      return this.canSustainBush(☃.getBlockState(☃.down()));
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      int ☃ = ☃.getValue(AGE);
      if (☃ < 3 && ☃.nextInt(10) == 0) {
         ☃ = ☃.withProperty(AGE, ☃ + 1);
         ☃.setBlockState(☃, ☃, 2);
      }

      super.updateTick(☃, ☃, ☃, ☃);
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      if (!☃.isRemote) {
         int ☃ = 1;
         if (☃.getValue(AGE) >= 3) {
            ☃ = 2 + ☃.rand.nextInt(3);
            if (☃ > 0) {
               ☃ += ☃.rand.nextInt(☃ + 1);
            }
         }

         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            spawnAsEntity(☃, ☃, new ItemStack(Items.NETHER_WART));
         }
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.AIR;
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Items.NETHER_WART);
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(AGE, ☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(AGE);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, AGE);
   }
}
