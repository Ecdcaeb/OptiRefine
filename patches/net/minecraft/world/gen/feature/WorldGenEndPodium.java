package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenEndPodium extends WorldGenerator {
   public static final BlockPos END_PODIUM_LOCATION = BlockPos.ORIGIN;
   public static final BlockPos END_PODIUM_CHUNK_POS = new BlockPos(END_PODIUM_LOCATION.getX() - 4 & -16, 0, END_PODIUM_LOCATION.getZ() - 4 & -16);
   private final boolean activePortal;

   public WorldGenEndPodium(boolean var1) {
      this.activePortal = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for (BlockPos.MutableBlockPos ☃ : BlockPos.getAllInBoxMutable(
         new BlockPos(☃.getX() - 4, ☃.getY() - 1, ☃.getZ() - 4), new BlockPos(☃.getX() + 4, ☃.getY() + 32, ☃.getZ() + 4)
      )) {
         double ☃x = ☃.getDistance(☃.getX(), ☃.getY(), ☃.getZ());
         if (☃x <= 3.5) {
            if (☃.getY() < ☃.getY()) {
               if (☃x <= 2.5) {
                  this.setBlockAndNotifyAdequately(☃, ☃, Blocks.BEDROCK.getDefaultState());
               } else if (☃.getY() < ☃.getY()) {
                  this.setBlockAndNotifyAdequately(☃, ☃, Blocks.END_STONE.getDefaultState());
               }
            } else if (☃.getY() > ☃.getY()) {
               this.setBlockAndNotifyAdequately(☃, ☃, Blocks.AIR.getDefaultState());
            } else if (☃x > 2.5) {
               this.setBlockAndNotifyAdequately(☃, ☃, Blocks.BEDROCK.getDefaultState());
            } else if (this.activePortal) {
               this.setBlockAndNotifyAdequately(☃, new BlockPos(☃), Blocks.END_PORTAL.getDefaultState());
            } else {
               this.setBlockAndNotifyAdequately(☃, new BlockPos(☃), Blocks.AIR.getDefaultState());
            }
         }
      }

      for (int ☃x = 0; ☃x < 4; ☃x++) {
         this.setBlockAndNotifyAdequately(☃, ☃.up(☃x), Blocks.BEDROCK.getDefaultState());
      }

      BlockPos ☃x = ☃.up(2);

      for (EnumFacing ☃xx : EnumFacing.Plane.HORIZONTAL) {
         this.setBlockAndNotifyAdequately(☃, ☃x.offset(☃xx), Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, ☃xx));
      }

      return true;
   }
}
