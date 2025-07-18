package net.minecraft.block.state;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPistonStructureHelper {
   private final World world;
   private final BlockPos pistonPos;
   private final BlockPos blockToMove;
   private final EnumFacing moveDirection;
   private final List<BlockPos> toMove = Lists.newArrayList();
   private final List<BlockPos> toDestroy = Lists.newArrayList();

   public BlockPistonStructureHelper(World var1, BlockPos var2, EnumFacing var3, boolean var4) {
      this.world = ☃;
      this.pistonPos = ☃;
      if (☃) {
         this.moveDirection = ☃;
         this.blockToMove = ☃.offset(☃);
      } else {
         this.moveDirection = ☃.getOpposite();
         this.blockToMove = ☃.offset(☃, 2);
      }
   }

   public boolean canMove() {
      this.toMove.clear();
      this.toDestroy.clear();
      IBlockState ☃ = this.world.getBlockState(this.blockToMove);
      if (!BlockPistonBase.canPush(☃, this.world, this.blockToMove, this.moveDirection, false, this.moveDirection)) {
         if (☃.getPushReaction() == EnumPushReaction.DESTROY) {
            this.toDestroy.add(this.blockToMove);
            return true;
         } else {
            return false;
         }
      } else if (!this.addBlockLine(this.blockToMove, this.moveDirection)) {
         return false;
      } else {
         for (int ☃x = 0; ☃x < this.toMove.size(); ☃x++) {
            BlockPos ☃xx = this.toMove.get(☃x);
            if (this.world.getBlockState(☃xx).getBlock() == Blocks.SLIME_BLOCK && !this.addBranchingBlocks(☃xx)) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean addBlockLine(BlockPos var1, EnumFacing var2) {
      IBlockState ☃ = this.world.getBlockState(☃);
      Block ☃x = ☃.getBlock();
      if (☃.getMaterial() == Material.AIR) {
         return true;
      } else if (!BlockPistonBase.canPush(☃, this.world, ☃, this.moveDirection, false, ☃)) {
         return true;
      } else if (☃.equals(this.pistonPos)) {
         return true;
      } else if (this.toMove.contains(☃)) {
         return true;
      } else {
         int ☃xx = 1;
         if (☃xx + this.toMove.size() > 12) {
            return false;
         } else {
            while (☃x == Blocks.SLIME_BLOCK) {
               BlockPos ☃xxx = ☃.offset(this.moveDirection.getOpposite(), ☃xx);
               ☃ = this.world.getBlockState(☃xxx);
               ☃x = ☃.getBlock();
               if (☃.getMaterial() == Material.AIR
                  || !BlockPistonBase.canPush(☃, this.world, ☃xxx, this.moveDirection, false, this.moveDirection.getOpposite())
                  || ☃xxx.equals(this.pistonPos)) {
                  break;
               }

               if (++☃xx + this.toMove.size() > 12) {
                  return false;
               }
            }

            int ☃xxxx = 0;

            for (int ☃xxxxx = ☃xx - 1; ☃xxxxx >= 0; ☃xxxxx--) {
               this.toMove.add(☃.offset(this.moveDirection.getOpposite(), ☃xxxxx));
               ☃xxxx++;
            }

            int ☃xxxxx = 1;

            while (true) {
               BlockPos ☃xxxxxx = ☃.offset(this.moveDirection, ☃xxxxx);
               int ☃xxxxxxx = this.toMove.indexOf(☃xxxxxx);
               if (☃xxxxxxx > -1) {
                  this.reorderListAtCollision(☃xxxx, ☃xxxxxxx);

                  for (int ☃xxxxxxxx = 0; ☃xxxxxxxx <= ☃xxxxxxx + ☃xxxx; ☃xxxxxxxx++) {
                     BlockPos ☃xxxxxxxxx = this.toMove.get(☃xxxxxxxx);
                     if (this.world.getBlockState(☃xxxxxxxxx).getBlock() == Blocks.SLIME_BLOCK && !this.addBranchingBlocks(☃xxxxxxxxx)) {
                        return false;
                     }
                  }

                  return true;
               }

               ☃ = this.world.getBlockState(☃xxxxxx);
               if (☃.getMaterial() == Material.AIR) {
                  return true;
               }

               if (!BlockPistonBase.canPush(☃, this.world, ☃xxxxxx, this.moveDirection, true, this.moveDirection) || ☃xxxxxx.equals(this.pistonPos)) {
                  return false;
               }

               if (☃.getPushReaction() == EnumPushReaction.DESTROY) {
                  this.toDestroy.add(☃xxxxxx);
                  return true;
               }

               if (this.toMove.size() >= 12) {
                  return false;
               }

               this.toMove.add(☃xxxxxx);
               ☃xxxx++;
               ☃xxxxx++;
            }
         }
      }
   }

   private void reorderListAtCollision(int var1, int var2) {
      List<BlockPos> ☃ = Lists.newArrayList();
      List<BlockPos> ☃x = Lists.newArrayList();
      List<BlockPos> ☃xx = Lists.newArrayList();
      ☃.addAll(this.toMove.subList(0, ☃));
      ☃x.addAll(this.toMove.subList(this.toMove.size() - ☃, this.toMove.size()));
      ☃xx.addAll(this.toMove.subList(☃, this.toMove.size() - ☃));
      this.toMove.clear();
      this.toMove.addAll(☃);
      this.toMove.addAll(☃x);
      this.toMove.addAll(☃xx);
   }

   private boolean addBranchingBlocks(BlockPos var1) {
      for (EnumFacing ☃ : EnumFacing.values()) {
         if (☃.getAxis() != this.moveDirection.getAxis() && !this.addBlockLine(☃.offset(☃), ☃)) {
            return false;
         }
      }

      return true;
   }

   public List<BlockPos> getBlocksToMove() {
      return this.toMove;
   }

   public List<BlockPos> getBlocksToDestroy() {
      return this.toDestroy;
   }
}
