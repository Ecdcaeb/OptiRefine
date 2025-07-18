package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;

public class BiomeSavanna extends Biome {
   private static final WorldGenSavannaTree SAVANNA_TREE = new WorldGenSavannaTree(false);

   protected BiomeSavanna(Biome.BiomeProperties var1) {
      super(☃);
      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityHorse.class, 1, 2, 6));
      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityDonkey.class, 1, 1, 1));
      if (this.getBaseHeight() > 1.1F) {
         this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityLlama.class, 8, 4, 4));
      }

      this.decorator.treesPerChunk = 1;
      this.decorator.flowersPerChunk = 4;
      this.decorator.grassPerChunk = 20;
   }

   @Override
   public WorldGenAbstractTree getRandomTreeFeature(Random var1) {
      return (WorldGenAbstractTree)(☃.nextInt(5) > 0 ? SAVANNA_TREE : TREE_FEATURE);
   }

   @Override
   public void decorate(World var1, Random var2, BlockPos var3) {
      DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);

      for (int ☃ = 0; ☃ < 7; ☃++) {
         int ☃x = ☃.nextInt(16) + 8;
         int ☃xx = ☃.nextInt(16) + 8;
         int ☃xxx = ☃.nextInt(☃.getHeight(☃.add(☃x, 0, ☃xx)).getY() + 32);
         DOUBLE_PLANT_GENERATOR.generate(☃, ☃, ☃.add(☃x, ☃xxx, ☃xx));
      }

      super.decorate(☃, ☃, ☃);
   }

   @Override
   public Class<? extends Biome> getBiomeClass() {
      return BiomeSavanna.class;
   }
}
