package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeHills extends Biome {
   private final WorldGenerator silverfishSpawner = new WorldGenMinable(
      Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONE), 9
   );
   private final WorldGenTaiga2 spruceGenerator = new WorldGenTaiga2(false);
   private final BiomeHills.Type type;

   protected BiomeHills(BiomeHills.Type var1, Biome.BiomeProperties var2) {
      super(☃);
      if (☃ == BiomeHills.Type.EXTRA_TREES) {
         this.decorator.treesPerChunk = 3;
      }

      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityLlama.class, 5, 4, 6));
      this.type = ☃;
   }

   @Override
   public WorldGenAbstractTree getRandomTreeFeature(Random var1) {
      return (WorldGenAbstractTree)(☃.nextInt(3) > 0 ? this.spruceGenerator : super.getRandomTreeFeature(☃));
   }

   @Override
   public void decorate(World var1, Random var2, BlockPos var3) {
      super.decorate(☃, ☃, ☃);
      int ☃ = 3 + ☃.nextInt(6);

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         int ☃xx = ☃.nextInt(16);
         int ☃xxx = ☃.nextInt(28) + 4;
         int ☃xxxx = ☃.nextInt(16);
         BlockPos ☃xxxxx = ☃.add(☃xx, ☃xxx, ☃xxxx);
         if (☃.getBlockState(☃xxxxx).getBlock() == Blocks.STONE) {
            ☃.setBlockState(☃xxxxx, Blocks.EMERALD_ORE.getDefaultState(), 2);
         }
      }

      for (int ☃xx = 0; ☃xx < 7; ☃xx++) {
         int ☃xxx = ☃.nextInt(16);
         int ☃xxxx = ☃.nextInt(64);
         int ☃xxxxx = ☃.nextInt(16);
         this.silverfishSpawner.generate(☃, ☃, ☃.add(☃xxx, ☃xxxx, ☃xxxxx));
      }
   }

   @Override
   public void genTerrainBlocks(World var1, Random var2, ChunkPrimer var3, int var4, int var5, double var6) {
      this.topBlock = Blocks.GRASS.getDefaultState();
      this.fillerBlock = Blocks.DIRT.getDefaultState();
      if ((☃ < -1.0 || ☃ > 2.0) && this.type == BiomeHills.Type.MUTATED) {
         this.topBlock = Blocks.GRAVEL.getDefaultState();
         this.fillerBlock = Blocks.GRAVEL.getDefaultState();
      } else if (☃ > 1.0 && this.type != BiomeHills.Type.EXTRA_TREES) {
         this.topBlock = Blocks.STONE.getDefaultState();
         this.fillerBlock = Blocks.STONE.getDefaultState();
      }

      this.generateBiomeTerrain(☃, ☃, ☃, ☃, ☃, ☃);
   }

   public static enum Type {
      NORMAL,
      EXTRA_TREES,
      MUTATED;
   }
}
