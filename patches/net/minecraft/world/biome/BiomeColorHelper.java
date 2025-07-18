package net.minecraft.world.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BiomeColorHelper {
   private static final BiomeColorHelper.ColorResolver GRASS_COLOR = new BiomeColorHelper.ColorResolver() {
      @Override
      public int getColorAtPos(Biome var1, BlockPos var2) {
         return ☃.getGrassColorAtPos(☃);
      }
   };
   private static final BiomeColorHelper.ColorResolver FOLIAGE_COLOR = new BiomeColorHelper.ColorResolver() {
      @Override
      public int getColorAtPos(Biome var1, BlockPos var2) {
         return ☃.getFoliageColorAtPos(☃);
      }
   };
   private static final BiomeColorHelper.ColorResolver WATER_COLOR = new BiomeColorHelper.ColorResolver() {
      @Override
      public int getColorAtPos(Biome var1, BlockPos var2) {
         return ☃.getWaterColor();
      }
   };

   private static int getColorAtPos(IBlockAccess var0, BlockPos var1, BiomeColorHelper.ColorResolver var2) {
      int ☃ = 0;
      int ☃x = 0;
      int ☃xx = 0;

      for (BlockPos.MutableBlockPos ☃xxx : BlockPos.getAllInBoxMutable(☃.add(-1, 0, -1), ☃.add(1, 0, 1))) {
         int ☃xxxx = ☃.getColorAtPos(☃.getBiome(☃xxx), ☃xxx);
         ☃ += (☃xxxx & 0xFF0000) >> 16;
         ☃x += (☃xxxx & 0xFF00) >> 8;
         ☃xx += ☃xxxx & 0xFF;
      }

      return (☃ / 9 & 0xFF) << 16 | (☃x / 9 & 0xFF) << 8 | ☃xx / 9 & 0xFF;
   }

   public static int getGrassColorAtPos(IBlockAccess var0, BlockPos var1) {
      return getColorAtPos(☃, ☃, GRASS_COLOR);
   }

   public static int getFoliageColorAtPos(IBlockAccess var0, BlockPos var1) {
      return getColorAtPos(☃, ☃, FOLIAGE_COLOR);
   }

   public static int getWaterColorAtPos(IBlockAccess var0, BlockPos var1) {
      return getColorAtPos(☃, ☃, WATER_COLOR);
   }

   interface ColorResolver {
      int getColorAtPos(Biome var1, BlockPos var2);
   }
}
