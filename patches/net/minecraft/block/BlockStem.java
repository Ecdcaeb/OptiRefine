package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStem extends BlockBush implements IGrowable {
   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
   public static final PropertyDirection FACING = BlockTorch.FACING;
   private final Block crop;
   protected static final AxisAlignedBB[] STEM_AABB = new AxisAlignedBB[]{
      new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.125, 0.625),
      new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.25, 0.625),
      new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.375, 0.625),
      new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.5, 0.625),
      new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.625, 0.625),
      new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.75, 0.625),
      new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.875, 0.625),
      new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625)
   };

   protected BlockStem(Block var1) {
      this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(FACING, EnumFacing.UP));
      this.crop = ☃;
      this.setTickRandomly(true);
      this.setCreativeTab(null);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return STEM_AABB[☃.getValue(AGE)];
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      int ☃ = ☃.getValue(AGE);
      ☃ = ☃.withProperty(FACING, EnumFacing.UP);

      for (EnumFacing ☃x : EnumFacing.Plane.HORIZONTAL) {
         if (☃.getBlockState(☃.offset(☃x)).getBlock() == this.crop && ☃ == 7) {
            ☃ = ☃.withProperty(FACING, ☃x);
            break;
         }
      }

      return ☃;
   }

   @Override
   protected boolean canSustainBush(IBlockState var1) {
      return ☃.getBlock() == Blocks.FARMLAND;
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      super.updateTick(☃, ☃, ☃, ☃);
      if (☃.getLightFromNeighbors(☃.up()) >= 9) {
         float ☃ = BlockCrops.getGrowthChance(this, ☃, ☃);
         if (☃.nextInt((int)(25.0F / ☃) + 1) == 0) {
            int ☃x = ☃.getValue(AGE);
            if (☃x < 7) {
               ☃ = ☃.withProperty(AGE, ☃x + 1);
               ☃.setBlockState(☃, ☃, 2);
            } else {
               for (EnumFacing ☃xx : EnumFacing.Plane.HORIZONTAL) {
                  if (☃.getBlockState(☃.offset(☃xx)).getBlock() == this.crop) {
                     return;
                  }
               }

               ☃ = ☃.offset(EnumFacing.Plane.HORIZONTAL.random(☃));
               Block ☃xxx = ☃.getBlockState(☃.down()).getBlock();
               if (☃.getBlockState(☃).getBlock().material == Material.AIR && (☃xxx == Blocks.FARMLAND || ☃xxx == Blocks.DIRT || ☃xxx == Blocks.GRASS)) {
                  ☃.setBlockState(☃, this.crop.getDefaultState());
               }
            }
         }
      }
   }

   public void growStem(World var1, BlockPos var2, IBlockState var3) {
      int ☃ = ☃.getValue(AGE) + MathHelper.getInt(☃.rand, 2, 5);
      ☃.setBlockState(☃, ☃.withProperty(AGE, Math.min(7, ☃)), 2);
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      super.dropBlockAsItemWithChance(☃, ☃, ☃, ☃, ☃);
      if (!☃.isRemote) {
         Item ☃ = this.getSeedItem();
         if (☃ != null) {
            int ☃x = ☃.getValue(AGE);

            for (int ☃xx = 0; ☃xx < 3; ☃xx++) {
               if (☃.rand.nextInt(15) <= ☃x) {
                  spawnAsEntity(☃, ☃, new ItemStack(☃));
               }
            }
         }
      }
   }

   @Nullable
   protected Item getSeedItem() {
      if (this.crop == Blocks.PUMPKIN) {
         return Items.PUMPKIN_SEEDS;
      } else {
         return this.crop == Blocks.MELON_BLOCK ? Items.MELON_SEEDS : null;
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.AIR;
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      Item ☃ = this.getSeedItem();
      return ☃ == null ? ItemStack.EMPTY : new ItemStack(☃);
   }

   @Override
   public boolean canGrow(World var1, BlockPos var2, IBlockState var3, boolean var4) {
      return ☃.getValue(AGE) != 7;
   }

   @Override
   public boolean canUseBonemeal(World var1, Random var2, BlockPos var3, IBlockState var4) {
      return true;
   }

   @Override
   public void grow(World var1, Random var2, BlockPos var3, IBlockState var4) {
      this.growStem(☃, ☃, ☃);
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
      return new BlockStateContainer(this, AGE, FACING);
   }
}
