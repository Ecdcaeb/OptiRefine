package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockMushroom extends BlockBush implements IGrowable {
   protected static final AxisAlignedBB MUSHROOM_AABB = new AxisAlignedBB(0.3F, 0.0, 0.3F, 0.7F, 0.4F, 0.7F);

   protected BlockMushroom() {
      this.setTickRandomly(true);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return MUSHROOM_AABB;
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (☃.nextInt(25) == 0) {
         int ☃ = 5;
         int ☃x = 4;

         for (BlockPos ☃xx : BlockPos.getAllInBoxMutable(☃.add(-4, -1, -4), ☃.add(4, 1, 4))) {
            if (☃.getBlockState(☃xx).getBlock() == this) {
               if (--☃ <= 0) {
                  return;
               }
            }
         }

         BlockPos ☃xxx = ☃.add(☃.nextInt(3) - 1, ☃.nextInt(2) - ☃.nextInt(2), ☃.nextInt(3) - 1);

         for (int ☃xxxx = 0; ☃xxxx < 4; ☃xxxx++) {
            if (☃.isAirBlock(☃xxx) && this.canBlockStay(☃, ☃xxx, this.getDefaultState())) {
               ☃ = ☃xxx;
            }

            ☃xxx = ☃.add(☃.nextInt(3) - 1, ☃.nextInt(2) - ☃.nextInt(2), ☃.nextInt(3) - 1);
         }

         if (☃.isAirBlock(☃xxx) && this.canBlockStay(☃, ☃xxx, this.getDefaultState())) {
            ☃.setBlockState(☃xxx, this.getDefaultState(), 2);
         }
      }
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return super.canPlaceBlockAt(☃, ☃) && this.canBlockStay(☃, ☃, this.getDefaultState());
   }

   @Override
   protected boolean canSustainBush(IBlockState var1) {
      return ☃.isFullBlock();
   }

   @Override
   public boolean canBlockStay(World var1, BlockPos var2, IBlockState var3) {
      if (☃.getY() >= 0 && ☃.getY() < 256) {
         IBlockState ☃ = ☃.getBlockState(☃.down());
         if (☃.getBlock() == Blocks.MYCELIUM) {
            return true;
         } else {
            return ☃.getBlock() == Blocks.DIRT && ☃.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL
               ? true
               : ☃.getLight(☃) < 13 && this.canSustainBush(☃);
         }
      } else {
         return false;
      }
   }

   public boolean generateBigMushroom(World var1, BlockPos var2, IBlockState var3, Random var4) {
      ☃.setBlockToAir(☃);
      WorldGenerator ☃ = null;
      if (this == Blocks.BROWN_MUSHROOM) {
         ☃ = new WorldGenBigMushroom(Blocks.BROWN_MUSHROOM_BLOCK);
      } else if (this == Blocks.RED_MUSHROOM) {
         ☃ = new WorldGenBigMushroom(Blocks.RED_MUSHROOM_BLOCK);
      }

      if (☃ != null && ☃.generate(☃, ☃, ☃)) {
         return true;
      } else {
         ☃.setBlockState(☃, ☃, 3);
         return false;
      }
   }

   @Override
   public boolean canGrow(World var1, BlockPos var2, IBlockState var3, boolean var4) {
      return true;
   }

   @Override
   public boolean canUseBonemeal(World var1, Random var2, BlockPos var3, IBlockState var4) {
      return ☃.nextFloat() < 0.4;
   }

   @Override
   public void grow(World var1, Random var2, BlockPos var3, IBlockState var4) {
      this.generateBigMushroom(☃, ☃, ☃, ☃);
   }
}
