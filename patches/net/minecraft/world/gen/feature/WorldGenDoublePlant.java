package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDoublePlant extends WorldGenerator {
   private BlockDoublePlant.EnumPlantType plantType;

   public void setPlantType(BlockDoublePlant.EnumPlantType var1) {
      this.plantType = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      boolean ☃ = false;

      for (int ☃x = 0; ☃x < 64; ☃x++) {
         BlockPos ☃xx = ☃.add(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
         if (☃.isAirBlock(☃xx) && (!☃.provider.isNether() || ☃xx.getY() < 254) && Blocks.DOUBLE_PLANT.canPlaceBlockAt(☃, ☃xx)) {
            Blocks.DOUBLE_PLANT.placeAt(☃, ☃xx, this.plantType, 2);
            ☃ = true;
         }
      }

      return ☃;
   }
}
