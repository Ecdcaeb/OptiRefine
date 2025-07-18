package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeTaiga extends Biome {
   private static final WorldGenTaiga1 PINE_GENERATOR = new WorldGenTaiga1();
   private static final WorldGenTaiga2 SPRUCE_GENERATOR = new WorldGenTaiga2(false);
   private static final WorldGenMegaPineTree MEGA_PINE_GENERATOR = new WorldGenMegaPineTree(false, false);
   private static final WorldGenMegaPineTree MEGA_SPRUCE_GENERATOR = new WorldGenMegaPineTree(false, true);
   private static final WorldGenBlockBlob FOREST_ROCK_GENERATOR = new WorldGenBlockBlob(Blocks.MOSSY_COBBLESTONE, 0);
   private final BiomeTaiga.Type type;

   public BiomeTaiga(BiomeTaiga.Type var1, Biome.BiomeProperties var2) {
      super(☃);
      this.type = ☃;
      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityWolf.class, 8, 4, 4));
      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
      this.decorator.treesPerChunk = 10;
      if (☃ != BiomeTaiga.Type.MEGA && ☃ != BiomeTaiga.Type.MEGA_SPRUCE) {
         this.decorator.grassPerChunk = 1;
         this.decorator.mushroomsPerChunk = 1;
      } else {
         this.decorator.grassPerChunk = 7;
         this.decorator.deadBushPerChunk = 1;
         this.decorator.mushroomsPerChunk = 3;
      }
   }

   @Override
   public WorldGenAbstractTree getRandomTreeFeature(Random var1) {
      if ((this.type == BiomeTaiga.Type.MEGA || this.type == BiomeTaiga.Type.MEGA_SPRUCE) && ☃.nextInt(3) == 0) {
         return this.type != BiomeTaiga.Type.MEGA_SPRUCE && ☃.nextInt(13) != 0 ? MEGA_PINE_GENERATOR : MEGA_SPRUCE_GENERATOR;
      } else {
         return (WorldGenAbstractTree)(☃.nextInt(3) == 0 ? PINE_GENERATOR : SPRUCE_GENERATOR);
      }
   }

   @Override
   public WorldGenerator getRandomWorldGenForGrass(Random var1) {
      return ☃.nextInt(5) > 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
   }

   @Override
   public void decorate(World var1, Random var2, BlockPos var3) {
      if (this.type == BiomeTaiga.Type.MEGA || this.type == BiomeTaiga.Type.MEGA_SPRUCE) {
         int ☃ = ☃.nextInt(3);

         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            int ☃xx = ☃.nextInt(16) + 8;
            int ☃xxx = ☃.nextInt(16) + 8;
            BlockPos ☃xxxx = ☃.getHeight(☃.add(☃xx, 0, ☃xxx));
            FOREST_ROCK_GENERATOR.generate(☃, ☃, ☃xxxx);
         }
      }

      DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);

      for (int ☃ = 0; ☃ < 7; ☃++) {
         int ☃x = ☃.nextInt(16) + 8;
         int ☃xx = ☃.nextInt(16) + 8;
         int ☃xxx = ☃.nextInt(☃.getHeight(☃.add(☃x, 0, ☃xx)).getY() + 32);
         DOUBLE_PLANT_GENERATOR.generate(☃, ☃, ☃.add(☃x, ☃xxx, ☃xx));
      }

      super.decorate(☃, ☃, ☃);
   }

   @Override
   public void genTerrainBlocks(World var1, Random var2, ChunkPrimer var3, int var4, int var5, double var6) {
      if (this.type == BiomeTaiga.Type.MEGA || this.type == BiomeTaiga.Type.MEGA_SPRUCE) {
         this.topBlock = Blocks.GRASS.getDefaultState();
         this.fillerBlock = Blocks.DIRT.getDefaultState();
         if (☃ > 1.75) {
            this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
         } else if (☃ > -0.95) {
            this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
         }
      }

      this.generateBiomeTerrain(☃, ☃, ☃, ☃, ☃, ☃);
   }

   public static enum Type {
      NORMAL,
      MEGA,
      MEGA_SPRUCE;
   }
}
