package net.minecraft.world.biome;

import net.minecraft.init.Blocks;

public class BiomeBeach extends Biome {
   public BiomeBeach(Biome.BiomeProperties var1) {
      super(☃);
      this.spawnableCreatureList.clear();
      this.topBlock = Blocks.SAND.getDefaultState();
      this.fillerBlock = Blocks.SAND.getDefaultState();
      this.decorator.treesPerChunk = -999;
      this.decorator.deadBushPerChunk = 0;
      this.decorator.reedsPerChunk = 0;
      this.decorator.cactiPerChunk = 0;
   }
}
