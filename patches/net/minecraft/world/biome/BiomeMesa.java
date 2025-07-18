package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeMesa extends Biome {
   protected static final IBlockState COARSE_DIRT = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
   protected static final IBlockState GRASS = Blocks.GRASS.getDefaultState();
   protected static final IBlockState HARDENED_CLAY = Blocks.HARDENED_CLAY.getDefaultState();
   protected static final IBlockState STAINED_HARDENED_CLAY = Blocks.STAINED_HARDENED_CLAY.getDefaultState();
   protected static final IBlockState ORANGE_STAINED_HARDENED_CLAY = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
   protected static final IBlockState RED_SAND = Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
   private IBlockState[] clayBands;
   private long worldSeed;
   private NoiseGeneratorPerlin pillarNoise;
   private NoiseGeneratorPerlin pillarRoofNoise;
   private NoiseGeneratorPerlin clayBandsOffsetNoise;
   private final boolean brycePillars;
   private final boolean hasForest;

   public BiomeMesa(boolean var1, boolean var2, Biome.BiomeProperties var3) {
      super(☃);
      this.brycePillars = ☃;
      this.hasForest = ☃;
      this.spawnableCreatureList.clear();
      this.topBlock = RED_SAND;
      this.fillerBlock = STAINED_HARDENED_CLAY;
      this.decorator.treesPerChunk = -999;
      this.decorator.deadBushPerChunk = 20;
      this.decorator.reedsPerChunk = 3;
      this.decorator.cactiPerChunk = 5;
      this.decorator.flowersPerChunk = 0;
      this.spawnableCreatureList.clear();
      if (☃) {
         this.decorator.treesPerChunk = 5;
      }
   }

   @Override
   protected BiomeDecorator createBiomeDecorator() {
      return new BiomeMesa.Decorator();
   }

   @Override
   public WorldGenAbstractTree getRandomTreeFeature(Random var1) {
      return TREE_FEATURE;
   }

   @Override
   public int getFoliageColorAtPos(BlockPos var1) {
      return 10387789;
   }

   @Override
   public int getGrassColorAtPos(BlockPos var1) {
      return 9470285;
   }

   @Override
   public void genTerrainBlocks(World var1, Random var2, ChunkPrimer var3, int var4, int var5, double var6) {
      if (this.clayBands == null || this.worldSeed != ☃.getSeed()) {
         this.generateBands(☃.getSeed());
      }

      if (this.pillarNoise == null || this.pillarRoofNoise == null || this.worldSeed != ☃.getSeed()) {
         Random ☃ = new Random(this.worldSeed);
         this.pillarNoise = new NoiseGeneratorPerlin(☃, 4);
         this.pillarRoofNoise = new NoiseGeneratorPerlin(☃, 1);
      }

      this.worldSeed = ☃.getSeed();
      double ☃ = 0.0;
      if (this.brycePillars) {
         int ☃x = (☃ & -16) + (☃ & 15);
         int ☃xx = (☃ & -16) + (☃ & 15);
         double ☃xxx = Math.min(Math.abs(☃), this.pillarNoise.getValue(☃x * 0.25, ☃xx * 0.25));
         if (☃xxx > 0.0) {
            double ☃xxxx = 0.001953125;
            double ☃xxxxx = Math.abs(this.pillarRoofNoise.getValue(☃x * 0.001953125, ☃xx * 0.001953125));
            ☃ = ☃xxx * ☃xxx * 2.5;
            double ☃xxxxxx = Math.ceil(☃xxxxx * 50.0) + 14.0;
            if (☃ > ☃xxxxxx) {
               ☃ = ☃xxxxxx;
            }

            ☃ += 64.0;
         }
      }

      int ☃x = ☃ & 15;
      int ☃xx = ☃ & 15;
      int ☃xxx = ☃.getSeaLevel();
      IBlockState ☃xxxx = STAINED_HARDENED_CLAY;
      IBlockState ☃xxxxx = this.fillerBlock;
      int ☃xxxxxx = (int)(☃ / 3.0 + 3.0 + ☃.nextDouble() * 0.25);
      boolean ☃xxxxxxx = Math.cos(☃ / 3.0 * Math.PI) > 0.0;
      int ☃xxxxxxxx = -1;
      boolean ☃xxxxxxxxx = false;
      int ☃xxxxxxxxxx = 0;

      for (int ☃xxxxxxxxxxx = 255; ☃xxxxxxxxxxx >= 0; ☃xxxxxxxxxxx--) {
         if (☃.getBlockState(☃xx, ☃xxxxxxxxxxx, ☃x).getMaterial() == Material.AIR && ☃xxxxxxxxxxx < (int)☃) {
            ☃.setBlockState(☃xx, ☃xxxxxxxxxxx, ☃x, STONE);
         }

         if (☃xxxxxxxxxxx <= ☃.nextInt(5)) {
            ☃.setBlockState(☃xx, ☃xxxxxxxxxxx, ☃x, BEDROCK);
         } else if (☃xxxxxxxxxx < 15 || this.brycePillars) {
            IBlockState ☃xxxxxxxxxxxx = ☃.getBlockState(☃xx, ☃xxxxxxxxxxx, ☃x);
            if (☃xxxxxxxxxxxx.getMaterial() == Material.AIR) {
               ☃xxxxxxxx = -1;
            } else if (☃xxxxxxxxxxxx.getBlock() == Blocks.STONE) {
               if (☃xxxxxxxx == -1) {
                  ☃xxxxxxxxx = false;
                  if (☃xxxxxx <= 0) {
                     ☃xxxx = AIR;
                     ☃xxxxx = STONE;
                  } else if (☃xxxxxxxxxxx >= ☃xxx - 4 && ☃xxxxxxxxxxx <= ☃xxx + 1) {
                     ☃xxxx = STAINED_HARDENED_CLAY;
                     ☃xxxxx = this.fillerBlock;
                  }

                  if (☃xxxxxxxxxxx < ☃xxx && (☃xxxx == null || ☃xxxx.getMaterial() == Material.AIR)) {
                     ☃xxxx = WATER;
                  }

                  ☃xxxxxxxx = ☃xxxxxx + Math.max(0, ☃xxxxxxxxxxx - ☃xxx);
                  if (☃xxxxxxxxxxx < ☃xxx - 1) {
                     ☃.setBlockState(☃xx, ☃xxxxxxxxxxx, ☃x, ☃xxxxx);
                     if (☃xxxxx.getBlock() == Blocks.STAINED_HARDENED_CLAY) {
                        ☃.setBlockState(☃xx, ☃xxxxxxxxxxx, ☃x, ORANGE_STAINED_HARDENED_CLAY);
                     }
                  } else if (!this.hasForest || ☃xxxxxxxxxxx <= 86 + ☃xxxxxx * 2) {
                     if (☃xxxxxxxxxxx <= ☃xxx + 3 + ☃xxxxxx) {
                        ☃.setBlockState(☃xx, ☃xxxxxxxxxxx, ☃x, this.topBlock);
                        ☃xxxxxxxxx = true;
                     } else {
                        IBlockState ☃xxxxxxxxxxxxx;
                        if (☃xxxxxxxxxxx < 64 || ☃xxxxxxxxxxx > 127) {
                           ☃xxxxxxxxxxxxx = ORANGE_STAINED_HARDENED_CLAY;
                        } else if (☃xxxxxxx) {
                           ☃xxxxxxxxxxxxx = HARDENED_CLAY;
                        } else {
                           ☃xxxxxxxxxxxxx = this.getBand(☃, ☃xxxxxxxxxxx, ☃);
                        }

                        ☃.setBlockState(☃xx, ☃xxxxxxxxxxx, ☃x, ☃xxxxxxxxxxxxx);
                     }
                  } else if (☃xxxxxxx) {
                     ☃.setBlockState(☃xx, ☃xxxxxxxxxxx, ☃x, COARSE_DIRT);
                  } else {
                     ☃.setBlockState(☃xx, ☃xxxxxxxxxxx, ☃x, GRASS);
                  }
               } else if (☃xxxxxxxx > 0) {
                  ☃xxxxxxxx--;
                  if (☃xxxxxxxxx) {
                     ☃.setBlockState(☃xx, ☃xxxxxxxxxxx, ☃x, ORANGE_STAINED_HARDENED_CLAY);
                  } else {
                     ☃.setBlockState(☃xx, ☃xxxxxxxxxxx, ☃x, this.getBand(☃, ☃xxxxxxxxxxx, ☃));
                  }
               }

               ☃xxxxxxxxxx++;
            }
         }
      }
   }

   private void generateBands(long var1) {
      this.clayBands = new IBlockState[64];
      Arrays.fill(this.clayBands, HARDENED_CLAY);
      Random ☃ = new Random(☃);
      this.clayBandsOffsetNoise = new NoiseGeneratorPerlin(☃, 1);

      for (int ☃x = 0; ☃x < 64; ☃x++) {
         ☃x += ☃.nextInt(5) + 1;
         if (☃x < 64) {
            this.clayBands[☃x] = ORANGE_STAINED_HARDENED_CLAY;
         }
      }

      int ☃xx = ☃.nextInt(4) + 2;

      for (int ☃xxx = 0; ☃xxx < ☃xx; ☃xxx++) {
         int ☃xxxx = ☃.nextInt(3) + 1;
         int ☃xxxxx = ☃.nextInt(64);

         for (int ☃xxxxxx = 0; ☃xxxxx + ☃xxxxxx < 64 && ☃xxxxxx < ☃xxxx; ☃xxxxxx++) {
            this.clayBands[☃xxxxx + ☃xxxxxx] = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);
         }
      }

      int ☃xxx = ☃.nextInt(4) + 2;

      for (int ☃xxxx = 0; ☃xxxx < ☃xxx; ☃xxxx++) {
         int ☃xxxxx = ☃.nextInt(3) + 2;
         int ☃xxxxxx = ☃.nextInt(64);

         for (int ☃xxxxxxx = 0; ☃xxxxxx + ☃xxxxxxx < 64 && ☃xxxxxxx < ☃xxxxx; ☃xxxxxxx++) {
            this.clayBands[☃xxxxxx + ☃xxxxxxx] = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.BROWN);
         }
      }

      int ☃xxxx = ☃.nextInt(4) + 2;

      for (int ☃xxxxx = 0; ☃xxxxx < ☃xxxx; ☃xxxxx++) {
         int ☃xxxxxx = ☃.nextInt(3) + 1;
         int ☃xxxxxxx = ☃.nextInt(64);

         for (int ☃xxxxxxxx = 0; ☃xxxxxxx + ☃xxxxxxxx < 64 && ☃xxxxxxxx < ☃xxxxxx; ☃xxxxxxxx++) {
            this.clayBands[☃xxxxxxx + ☃xxxxxxxx] = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.RED);
         }
      }

      int ☃xxxxx = ☃.nextInt(3) + 3;
      int ☃xxxxxx = 0;

      for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃xxxxx; ☃xxxxxxx++) {
         int ☃xxxxxxxx = 1;
         ☃xxxxxx += ☃.nextInt(16) + 4;

         for (int ☃xxxxxxxxx = 0; ☃xxxxxx + ☃xxxxxxxxx < 64 && ☃xxxxxxxxx < 1; ☃xxxxxxxxx++) {
            this.clayBands[☃xxxxxx + ☃xxxxxxxxx] = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);
            if (☃xxxxxx + ☃xxxxxxxxx > 1 && ☃.nextBoolean()) {
               this.clayBands[☃xxxxxx + ☃xxxxxxxxx - 1] = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
            }

            if (☃xxxxxx + ☃xxxxxxxxx < 63 && ☃.nextBoolean()) {
               this.clayBands[☃xxxxxx + ☃xxxxxxxxx + 1] = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
            }
         }
      }
   }

   private IBlockState getBand(int var1, int var2, int var3) {
      int ☃ = (int)Math.round(this.clayBandsOffsetNoise.getValue(☃ / 512.0, ☃ / 512.0) * 2.0);
      return this.clayBands[(☃ + ☃ + 64) % 64];
   }

   class Decorator extends BiomeDecorator {
      private Decorator() {
      }

      @Override
      protected void generateOres(World var1, Random var2) {
         super.generateOres(☃, ☃);
         this.genStandardOre1(☃, ☃, 20, this.goldGen, 32, 80);
      }
   }
}
