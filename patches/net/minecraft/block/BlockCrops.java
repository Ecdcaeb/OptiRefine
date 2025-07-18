package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCrops extends BlockBush implements IGrowable {
   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
   private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[]{
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.375, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.625, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.75, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.875, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
   };

   protected BlockCrops() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(this.getAgeProperty(), 0));
      this.setTickRandomly(true);
      this.setCreativeTab(null);
      this.setHardness(0.0F);
      this.setSoundType(SoundType.PLANT);
      this.disableStats();
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return CROPS_AABB[☃.getValue(this.getAgeProperty())];
   }

   @Override
   protected boolean canSustainBush(IBlockState var1) {
      return ☃.getBlock() == Blocks.FARMLAND;
   }

   protected PropertyInteger getAgeProperty() {
      return AGE;
   }

   public int getMaxAge() {
      return 7;
   }

   protected int getAge(IBlockState var1) {
      return ☃.getValue(this.getAgeProperty());
   }

   public IBlockState withAge(int var1) {
      return this.getDefaultState().withProperty(this.getAgeProperty(), ☃);
   }

   public boolean isMaxAge(IBlockState var1) {
      return ☃.getValue(this.getAgeProperty()) >= this.getMaxAge();
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      super.updateTick(☃, ☃, ☃, ☃);
      if (☃.getLightFromNeighbors(☃.up()) >= 9) {
         int ☃ = this.getAge(☃);
         if (☃ < this.getMaxAge()) {
            float ☃x = getGrowthChance(this, ☃, ☃);
            if (☃.nextInt((int)(25.0F / ☃x) + 1) == 0) {
               ☃.setBlockState(☃, this.withAge(☃ + 1), 2);
            }
         }
      }
   }

   public void grow(World var1, BlockPos var2, IBlockState var3) {
      int ☃ = this.getAge(☃) + this.getBonemealAgeIncrease(☃);
      int ☃x = this.getMaxAge();
      if (☃ > ☃x) {
         ☃ = ☃x;
      }

      ☃.setBlockState(☃, this.withAge(☃), 2);
   }

   protected int getBonemealAgeIncrease(World var1) {
      return MathHelper.getInt(☃.rand, 2, 5);
   }

   protected static float getGrowthChance(Block var0, World var1, BlockPos var2) {
      float ☃ = 1.0F;
      BlockPos ☃x = ☃.down();

      for (int ☃xx = -1; ☃xx <= 1; ☃xx++) {
         for (int ☃xxx = -1; ☃xxx <= 1; ☃xxx++) {
            float ☃xxxx = 0.0F;
            IBlockState ☃xxxxx = ☃.getBlockState(☃x.add(☃xx, 0, ☃xxx));
            if (☃xxxxx.getBlock() == Blocks.FARMLAND) {
               ☃xxxx = 1.0F;
               if (☃xxxxx.getValue(BlockFarmland.MOISTURE) > 0) {
                  ☃xxxx = 3.0F;
               }
            }

            if (☃xx != 0 || ☃xxx != 0) {
               ☃xxxx /= 4.0F;
            }

            ☃ += ☃xxxx;
         }
      }

      BlockPos ☃xx = ☃.north();
      BlockPos ☃xxx = ☃.south();
      BlockPos ☃xxxxxx = ☃.west();
      BlockPos ☃xxxxxxx = ☃.east();
      boolean ☃xxxxxxxx = ☃ == ☃.getBlockState(☃xxxxxx).getBlock() || ☃ == ☃.getBlockState(☃xxxxxxx).getBlock();
      boolean ☃xxxxxxxxx = ☃ == ☃.getBlockState(☃xx).getBlock() || ☃ == ☃.getBlockState(☃xxx).getBlock();
      if (☃xxxxxxxx && ☃xxxxxxxxx) {
         ☃ /= 2.0F;
      } else {
         boolean ☃xxxxxxxxxx = ☃ == ☃.getBlockState(☃xxxxxx.north()).getBlock()
            || ☃ == ☃.getBlockState(☃xxxxxxx.north()).getBlock()
            || ☃ == ☃.getBlockState(☃xxxxxxx.south()).getBlock()
            || ☃ == ☃.getBlockState(☃xxxxxx.south()).getBlock();
         if (☃xxxxxxxxxx) {
            ☃ /= 2.0F;
         }
      }

      return ☃;
   }

   @Override
   public boolean canBlockStay(World var1, BlockPos var2, IBlockState var3) {
      return (☃.getLight(☃) >= 8 || ☃.canSeeSky(☃)) && this.canSustainBush(☃.getBlockState(☃.down()));
   }

   protected Item getSeed() {
      return Items.WHEAT_SEEDS;
   }

   protected Item getCrop() {
      return Items.WHEAT;
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      super.dropBlockAsItemWithChance(☃, ☃, ☃, ☃, 0);
      if (!☃.isRemote) {
         int ☃ = this.getAge(☃);
         if (☃ >= this.getMaxAge()) {
            int ☃x = 3 + ☃;

            for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
               if (☃.rand.nextInt(2 * this.getMaxAge()) <= ☃) {
                  spawnAsEntity(☃, ☃, new ItemStack(this.getSeed()));
               }
            }
         }
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return this.isMaxAge(☃) ? this.getCrop() : this.getSeed();
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(this.getSeed());
   }

   @Override
   public boolean canGrow(World var1, BlockPos var2, IBlockState var3, boolean var4) {
      return !this.isMaxAge(☃);
   }

   @Override
   public boolean canUseBonemeal(World var1, Random var2, BlockPos var3, IBlockState var4) {
      return true;
   }

   @Override
   public void grow(World var1, Random var2, BlockPos var3, IBlockState var4) {
      this.grow(☃, ☃, ☃);
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.withAge(☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return this.getAge(☃);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, AGE);
   }
}
