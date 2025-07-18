package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeForestMutated extends BiomeForest {
   public BiomeForestMutated(Biome.BiomeProperties var1) {
      super(BiomeForest.Type.BIRCH, ☃);
   }

   @Override
   public WorldGenAbstractTree getRandomTreeFeature(Random var1) {
      return ☃.nextBoolean() ? BiomeForest.SUPER_BIRCH_TREE : BiomeForest.BIRCH_TREE;
   }
}
