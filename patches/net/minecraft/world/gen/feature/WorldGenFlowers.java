package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenFlowers extends WorldGenerator {
   private BlockFlower flower;
   private IBlockState state;

   public WorldGenFlowers(BlockFlower var1, BlockFlower.EnumFlowerType var2) {
      this.setGeneratedBlock(☃, ☃);
   }

   public void setGeneratedBlock(BlockFlower var1, BlockFlower.EnumFlowerType var2) {
      this.flower = ☃;
      this.state = ☃.getDefaultState().withProperty(☃.getTypeProperty(), ☃);
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for (int ☃ = 0; ☃ < 64; ☃++) {
         BlockPos ☃x = ☃.add(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
         if (☃.isAirBlock(☃x) && (!☃.provider.isNether() || ☃x.getY() < 255) && this.flower.canBlockStay(☃, ☃x, this.state)) {
            ☃.setBlockState(☃x, this.state, 2);
         }
      }

      return true;
   }
}
