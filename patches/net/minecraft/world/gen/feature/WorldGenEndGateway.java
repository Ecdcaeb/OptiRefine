package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenEndGateway extends WorldGenerator {
   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for (BlockPos.MutableBlockPos ☃ : BlockPos.getAllInBoxMutable(☃.add(-1, -2, -1), ☃.add(1, 2, 1))) {
         boolean ☃x = ☃.getX() == ☃.getX();
         boolean ☃xx = ☃.getY() == ☃.getY();
         boolean ☃xxx = ☃.getZ() == ☃.getZ();
         boolean ☃xxxx = Math.abs(☃.getY() - ☃.getY()) == 2;
         if (☃x && ☃xx && ☃xxx) {
            this.setBlockAndNotifyAdequately(☃, new BlockPos(☃), Blocks.END_GATEWAY.getDefaultState());
         } else if (☃xx) {
            this.setBlockAndNotifyAdequately(☃, ☃, Blocks.AIR.getDefaultState());
         } else if (☃xxxx && ☃x && ☃xxx) {
            this.setBlockAndNotifyAdequately(☃, ☃, Blocks.BEDROCK.getDefaultState());
         } else if ((☃x || ☃xxx) && !☃xxxx) {
            this.setBlockAndNotifyAdequately(☃, ☃, Blocks.BEDROCK.getDefaultState());
         } else {
            this.setBlockAndNotifyAdequately(☃, ☃, Blocks.AIR.getDefaultState());
         }
      }

      return true;
   }
}
