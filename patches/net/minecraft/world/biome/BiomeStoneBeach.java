package net.minecraft.world.biome;

import net.minecraft.init.Blocks;

public class BiomeStoneBeach extends Biome {
   public BiomeStoneBeach(Biome.BiomeProperties var1) {
      super(☃);
      this.spawnableCreatureList.clear();
      this.topBlock = Blocks.STONE.getDefaultState();
      this.fillerBlock = Blocks.STONE.getDefaultState();
      this.decorator.treesPerChunk = -999;
      this.decorator.deadBushPerChunk = 0;
      this.decorator.reedsPerChunk = 0;
      this.decorator.cactiPerChunk = 0;
   }
}
