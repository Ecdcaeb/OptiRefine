package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenBigTree extends WorldGenAbstractTree {
   private Random rand;
   private World world;
   private BlockPos basePos = BlockPos.ORIGIN;
   int heightLimit;
   int height;
   double heightAttenuation = 0.618;
   double branchSlope = 0.381;
   double scaleWidth = 1.0;
   double leafDensity = 1.0;
   int trunkSize = 1;
   int heightLimitLimit = 12;
   int leafDistanceLimit = 4;
   List<WorldGenBigTree.FoliageCoordinates> foliageCoords;

   public WorldGenBigTree(boolean var1) {
      super(☃);
   }

   void generateLeafNodeList() {
      this.height = (int)(this.heightLimit * this.heightAttenuation);
      if (this.height >= this.heightLimit) {
         this.height = this.heightLimit - 1;
      }

      int ☃ = (int)(1.382 + Math.pow(this.leafDensity * this.heightLimit / 13.0, 2.0));
      if (☃ < 1) {
         ☃ = 1;
      }

      int ☃x = this.basePos.getY() + this.height;
      int ☃xx = this.heightLimit - this.leafDistanceLimit;
      this.foliageCoords = Lists.newArrayList();
      this.foliageCoords.add(new WorldGenBigTree.FoliageCoordinates(this.basePos.up(☃xx), ☃x));

      for (; ☃xx >= 0; ☃xx--) {
         float ☃xxx = this.layerSize(☃xx);
         if (!(☃xxx < 0.0F)) {
            for (int ☃xxxx = 0; ☃xxxx < ☃; ☃xxxx++) {
               double ☃xxxxx = this.scaleWidth * ☃xxx * (this.rand.nextFloat() + 0.328);
               double ☃xxxxxx = this.rand.nextFloat() * 2.0F * Math.PI;
               double ☃xxxxxxx = ☃xxxxx * Math.sin(☃xxxxxx) + 0.5;
               double ☃xxxxxxxx = ☃xxxxx * Math.cos(☃xxxxxx) + 0.5;
               BlockPos ☃xxxxxxxxx = this.basePos.add(☃xxxxxxx, (double)(☃xx - 1), ☃xxxxxxxx);
               BlockPos ☃xxxxxxxxxx = ☃xxxxxxxxx.up(this.leafDistanceLimit);
               if (this.checkBlockLine(☃xxxxxxxxx, ☃xxxxxxxxxx) == -1) {
                  int ☃xxxxxxxxxxx = this.basePos.getX() - ☃xxxxxxxxx.getX();
                  int ☃xxxxxxxxxxxx = this.basePos.getZ() - ☃xxxxxxxxx.getZ();
                  double ☃xxxxxxxxxxxxx = ☃xxxxxxxxx.getY() - Math.sqrt(☃xxxxxxxxxxx * ☃xxxxxxxxxxx + ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx) * this.branchSlope;
                  int ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx > ☃x ? ☃x : (int)☃xxxxxxxxxxxxx;
                  BlockPos ☃xxxxxxxxxxxxxxx = new BlockPos(this.basePos.getX(), ☃xxxxxxxxxxxxxx, this.basePos.getZ());
                  if (this.checkBlockLine(☃xxxxxxxxxxxxxxx, ☃xxxxxxxxx) == -1) {
                     this.foliageCoords.add(new WorldGenBigTree.FoliageCoordinates(☃xxxxxxxxx, ☃xxxxxxxxxxxxxxx.getY()));
                  }
               }
            }
         }
      }
   }

   void crosSection(BlockPos var1, float var2, IBlockState var3) {
      int ☃ = (int)(☃ + 0.618);

      for (int ☃x = -☃; ☃x <= ☃; ☃x++) {
         for (int ☃xx = -☃; ☃xx <= ☃; ☃xx++) {
            if (Math.pow(Math.abs(☃x) + 0.5, 2.0) + Math.pow(Math.abs(☃xx) + 0.5, 2.0) <= ☃ * ☃) {
               BlockPos ☃xxx = ☃.add(☃x, 0, ☃xx);
               Material ☃xxxx = this.world.getBlockState(☃xxx).getMaterial();
               if (☃xxxx == Material.AIR || ☃xxxx == Material.LEAVES) {
                  this.setBlockAndNotifyAdequately(this.world, ☃xxx, ☃);
               }
            }
         }
      }
   }

   float layerSize(int var1) {
      if (☃ < this.heightLimit * 0.3F) {
         return -1.0F;
      } else {
         float ☃ = this.heightLimit / 2.0F;
         float ☃x = ☃ - ☃;
         float ☃xx = MathHelper.sqrt(☃ * ☃ - ☃x * ☃x);
         if (☃x == 0.0F) {
            ☃xx = ☃;
         } else if (Math.abs(☃x) >= ☃) {
            return 0.0F;
         }

         return ☃xx * 0.5F;
      }
   }

   float leafSize(int var1) {
      if (☃ < 0 || ☃ >= this.leafDistanceLimit) {
         return -1.0F;
      } else {
         return ☃ != 0 && ☃ != this.leafDistanceLimit - 1 ? 3.0F : 2.0F;
      }
   }

   void generateLeafNode(BlockPos var1) {
      for (int ☃ = 0; ☃ < this.leafDistanceLimit; ☃++) {
         this.crosSection(☃.up(☃), this.leafSize(☃), Blocks.LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, false));
      }
   }

   void limb(BlockPos var1, BlockPos var2, Block var3) {
      BlockPos ☃ = ☃.add(-☃.getX(), -☃.getY(), -☃.getZ());
      int ☃x = this.getGreatestDistance(☃);
      float ☃xx = (float)☃.getX() / ☃x;
      float ☃xxx = (float)☃.getY() / ☃x;
      float ☃xxxx = (float)☃.getZ() / ☃x;

      for (int ☃xxxxx = 0; ☃xxxxx <= ☃x; ☃xxxxx++) {
         BlockPos ☃xxxxxx = ☃.add((double)(0.5F + ☃xxxxx * ☃xx), (double)(0.5F + ☃xxxxx * ☃xxx), (double)(0.5F + ☃xxxxx * ☃xxxx));
         BlockLog.EnumAxis ☃xxxxxxx = this.getLogAxis(☃, ☃xxxxxx);
         this.setBlockAndNotifyAdequately(this.world, ☃xxxxxx, ☃.getDefaultState().withProperty(BlockLog.LOG_AXIS, ☃xxxxxxx));
      }
   }

   private int getGreatestDistance(BlockPos var1) {
      int ☃ = MathHelper.abs(☃.getX());
      int ☃x = MathHelper.abs(☃.getY());
      int ☃xx = MathHelper.abs(☃.getZ());
      if (☃xx > ☃ && ☃xx > ☃x) {
         return ☃xx;
      } else {
         return ☃x > ☃ ? ☃x : ☃;
      }
   }

   private BlockLog.EnumAxis getLogAxis(BlockPos var1, BlockPos var2) {
      BlockLog.EnumAxis ☃ = BlockLog.EnumAxis.Y;
      int ☃x = Math.abs(☃.getX() - ☃.getX());
      int ☃xx = Math.abs(☃.getZ() - ☃.getZ());
      int ☃xxx = Math.max(☃x, ☃xx);
      if (☃xxx > 0) {
         if (☃x == ☃xxx) {
            ☃ = BlockLog.EnumAxis.X;
         } else if (☃xx == ☃xxx) {
            ☃ = BlockLog.EnumAxis.Z;
         }
      }

      return ☃;
   }

   void generateLeaves() {
      for (WorldGenBigTree.FoliageCoordinates ☃ : this.foliageCoords) {
         this.generateLeafNode(☃);
      }
   }

   boolean leafNodeNeedsBase(int var1) {
      return ☃ >= this.heightLimit * 0.2;
   }

   void generateTrunk() {
      BlockPos ☃ = this.basePos;
      BlockPos ☃x = this.basePos.up(this.height);
      Block ☃xx = Blocks.LOG;
      this.limb(☃, ☃x, ☃xx);
      if (this.trunkSize == 2) {
         this.limb(☃.east(), ☃x.east(), ☃xx);
         this.limb(☃.east().south(), ☃x.east().south(), ☃xx);
         this.limb(☃.south(), ☃x.south(), ☃xx);
      }
   }

   void generateLeafNodeBases() {
      for (WorldGenBigTree.FoliageCoordinates ☃ : this.foliageCoords) {
         int ☃x = ☃.getBranchBase();
         BlockPos ☃xx = new BlockPos(this.basePos.getX(), ☃x, this.basePos.getZ());
         if (!☃xx.equals(☃) && this.leafNodeNeedsBase(☃x - this.basePos.getY())) {
            this.limb(☃xx, ☃, Blocks.LOG);
         }
      }
   }

   int checkBlockLine(BlockPos var1, BlockPos var2) {
      BlockPos ☃ = ☃.add(-☃.getX(), -☃.getY(), -☃.getZ());
      int ☃x = this.getGreatestDistance(☃);
      float ☃xx = (float)☃.getX() / ☃x;
      float ☃xxx = (float)☃.getY() / ☃x;
      float ☃xxxx = (float)☃.getZ() / ☃x;
      if (☃x == 0) {
         return -1;
      } else {
         for (int ☃xxxxx = 0; ☃xxxxx <= ☃x; ☃xxxxx++) {
            BlockPos ☃xxxxxx = ☃.add((double)(0.5F + ☃xxxxx * ☃xx), (double)(0.5F + ☃xxxxx * ☃xxx), (double)(0.5F + ☃xxxxx * ☃xxxx));
            if (!this.canGrowInto(this.world.getBlockState(☃xxxxxx).getBlock())) {
               return ☃xxxxx;
            }
         }

         return -1;
      }
   }

   @Override
   public void setDecorationDefaults() {
      this.leafDistanceLimit = 5;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      this.world = ☃;
      this.basePos = ☃;
      this.rand = new Random(☃.nextLong());
      if (this.heightLimit == 0) {
         this.heightLimit = 5 + this.rand.nextInt(this.heightLimitLimit);
      }

      if (!this.validTreeLocation()) {
         return false;
      } else {
         this.generateLeafNodeList();
         this.generateLeaves();
         this.generateTrunk();
         this.generateLeafNodeBases();
         return true;
      }
   }

   private boolean validTreeLocation() {
      Block ☃ = this.world.getBlockState(this.basePos.down()).getBlock();
      if (☃ != Blocks.DIRT && ☃ != Blocks.GRASS && ☃ != Blocks.FARMLAND) {
         return false;
      } else {
         int ☃x = this.checkBlockLine(this.basePos, this.basePos.up(this.heightLimit - 1));
         if (☃x == -1) {
            return true;
         } else if (☃x < 6) {
            return false;
         } else {
            this.heightLimit = ☃x;
            return true;
         }
      }
   }

   static class FoliageCoordinates extends BlockPos {
      private final int branchBase;

      public FoliageCoordinates(BlockPos var1, int var2) {
         super(☃.getX(), ☃.getY(), ☃.getZ());
         this.branchBase = ☃;
      }

      public int getBranchBase() {
         return this.branchBase;
      }
   }
}
