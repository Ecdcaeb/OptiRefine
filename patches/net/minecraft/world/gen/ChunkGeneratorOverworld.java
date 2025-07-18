package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.WoodlandMansion;

public class ChunkGeneratorOverworld implements IChunkGenerator {
   protected static final IBlockState STONE = Blocks.STONE.getDefaultState();
   private final Random rand;
   private final NoiseGeneratorOctaves minLimitPerlinNoise;
   private final NoiseGeneratorOctaves maxLimitPerlinNoise;
   private final NoiseGeneratorOctaves mainPerlinNoise;
   private final NoiseGeneratorPerlin surfaceNoise;
   public NoiseGeneratorOctaves scaleNoise;
   public NoiseGeneratorOctaves depthNoise;
   public NoiseGeneratorOctaves forestNoise;
   private final World world;
   private final boolean mapFeaturesEnabled;
   private final WorldType terrainType;
   private final double[] heightMap;
   private final float[] biomeWeights;
   private ChunkGeneratorSettings settings;
   private IBlockState oceanBlock = Blocks.WATER.getDefaultState();
   private double[] depthBuffer = new double[256];
   private final MapGenBase caveGenerator = new MapGenCaves();
   private final MapGenStronghold strongholdGenerator = new MapGenStronghold();
   private final MapGenVillage villageGenerator = new MapGenVillage();
   private final MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
   private final MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
   private final MapGenBase ravineGenerator = new MapGenRavine();
   private final StructureOceanMonument oceanMonumentGenerator = new StructureOceanMonument();
   private final WoodlandMansion woodlandMansionGenerator = new WoodlandMansion(this);
   private Biome[] biomesForGeneration;
   double[] mainNoiseRegion;
   double[] minLimitRegion;
   double[] maxLimitRegion;
   double[] depthRegion;

   public ChunkGeneratorOverworld(World var1, long var2, boolean var4, String var5) {
      this.world = ☃;
      this.mapFeaturesEnabled = ☃;
      this.terrainType = ☃.getWorldInfo().getTerrainType();
      this.rand = new Random(☃);
      this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
      this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
      this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
      this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
      this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
      this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
      this.forestNoise = new NoiseGeneratorOctaves(this.rand, 8);
      this.heightMap = new double[825];
      this.biomeWeights = new float[25];

      for (int ☃ = -2; ☃ <= 2; ☃++) {
         for (int ☃x = -2; ☃x <= 2; ☃x++) {
            float ☃xx = 10.0F / MathHelper.sqrt(☃ * ☃ + ☃x * ☃x + 0.2F);
            this.biomeWeights[☃ + 2 + (☃x + 2) * 5] = ☃xx;
         }
      }

      if (☃ != null) {
         this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(☃).build();
         this.oceanBlock = this.settings.useLavaOceans ? Blocks.LAVA.getDefaultState() : Blocks.WATER.getDefaultState();
         ☃.setSeaLevel(this.settings.seaLevel);
      }
   }

   public void setBlocksInChunk(int var1, int var2, ChunkPrimer var3) {
      this.biomesForGeneration = this.world.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, ☃ * 4 - 2, ☃ * 4 - 2, 10, 10);
      this.generateHeightmap(☃ * 4, 0, ☃ * 4);

      for (int ☃ = 0; ☃ < 4; ☃++) {
         int ☃x = ☃ * 5;
         int ☃xx = (☃ + 1) * 5;

         for (int ☃xxx = 0; ☃xxx < 4; ☃xxx++) {
            int ☃xxxx = (☃x + ☃xxx) * 33;
            int ☃xxxxx = (☃x + ☃xxx + 1) * 33;
            int ☃xxxxxx = (☃xx + ☃xxx) * 33;
            int ☃xxxxxxx = (☃xx + ☃xxx + 1) * 33;

            for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < 32; ☃xxxxxxxx++) {
               double ☃xxxxxxxxx = 0.125;
               double ☃xxxxxxxxxx = this.heightMap[☃xxxx + ☃xxxxxxxx];
               double ☃xxxxxxxxxxx = this.heightMap[☃xxxxx + ☃xxxxxxxx];
               double ☃xxxxxxxxxxxx = this.heightMap[☃xxxxxx + ☃xxxxxxxx];
               double ☃xxxxxxxxxxxxx = this.heightMap[☃xxxxxxx + ☃xxxxxxxx];
               double ☃xxxxxxxxxxxxxx = (this.heightMap[☃xxxx + ☃xxxxxxxx + 1] - ☃xxxxxxxxxx) * 0.125;
               double ☃xxxxxxxxxxxxxxx = (this.heightMap[☃xxxxx + ☃xxxxxxxx + 1] - ☃xxxxxxxxxxx) * 0.125;
               double ☃xxxxxxxxxxxxxxxx = (this.heightMap[☃xxxxxx + ☃xxxxxxxx + 1] - ☃xxxxxxxxxxxx) * 0.125;
               double ☃xxxxxxxxxxxxxxxxx = (this.heightMap[☃xxxxxxx + ☃xxxxxxxx + 1] - ☃xxxxxxxxxxxxx) * 0.125;

               for (int ☃xxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxx < 8; ☃xxxxxxxxxxxxxxxxxx++) {
                  double ☃xxxxxxxxxxxxxxxxxxx = 0.25;
                  double ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxx;
                  double ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx;
                  double ☃xxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxx - ☃xxxxxxxxxx) * 0.25;
                  double ☃xxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxx - ☃xxxxxxxxxxx) * 0.25;

                  for (int ☃xxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxxxx++) {
                     double ☃xxxxxxxxxxxxxxxxxxxxxxxxx = 0.25;
                     double ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxx) * 0.25;
                     double var45 = ☃xxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxxx;

                     for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
                        if ((var45 += ☃xxxxxxxxxxxxxxxxxxxxxxxxxx) > 0.0) {
                           ☃.setBlockState(
                              ☃ * 4 + ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxx * 8 + ☃xxxxxxxxxxxxxxxxxx, ☃xxx * 4 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, STONE
                           );
                        } else if (☃xxxxxxxx * 8 + ☃xxxxxxxxxxxxxxxxxx < this.settings.seaLevel) {
                           ☃.setBlockState(
                              ☃ * 4 + ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxx * 8 + ☃xxxxxxxxxxxxxxxxxx, ☃xxx * 4 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, this.oceanBlock
                           );
                        }
                     }

                     ☃xxxxxxxxxxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxxxxxxxx;
                     ☃xxxxxxxxxxxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxxxxxxxxx;
                  }

                  ☃xxxxxxxxxx += ☃xxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxx += ☃xxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxxx;
               }
            }
         }
      }
   }

   public void replaceBiomeBlocks(int var1, int var2, ChunkPrimer var3, Biome[] var4) {
      double ☃ = 0.03125;
      this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, ☃ * 16, ☃ * 16, 16, 16, 0.0625, 0.0625, 1.0);

      for (int ☃x = 0; ☃x < 16; ☃x++) {
         for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
            Biome ☃xxx = ☃[☃xx + ☃x * 16];
            ☃xxx.genTerrainBlocks(this.world, this.rand, ☃, ☃ * 16 + ☃x, ☃ * 16 + ☃xx, this.depthBuffer[☃xx + ☃x * 16]);
         }
      }
   }

   @Override
   public Chunk generateChunk(int var1, int var2) {
      this.rand.setSeed(☃ * 341873128712L + ☃ * 132897987541L);
      ChunkPrimer ☃ = new ChunkPrimer();
      this.setBlocksInChunk(☃, ☃, ☃);
      this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, ☃ * 16, ☃ * 16, 16, 16);
      this.replaceBiomeBlocks(☃, ☃, ☃, this.biomesForGeneration);
      if (this.settings.useCaves) {
         this.caveGenerator.generate(this.world, ☃, ☃, ☃);
      }

      if (this.settings.useRavines) {
         this.ravineGenerator.generate(this.world, ☃, ☃, ☃);
      }

      if (this.mapFeaturesEnabled) {
         if (this.settings.useMineShafts) {
            this.mineshaftGenerator.generate(this.world, ☃, ☃, ☃);
         }

         if (this.settings.useVillages) {
            this.villageGenerator.generate(this.world, ☃, ☃, ☃);
         }

         if (this.settings.useStrongholds) {
            this.strongholdGenerator.generate(this.world, ☃, ☃, ☃);
         }

         if (this.settings.useTemples) {
            this.scatteredFeatureGenerator.generate(this.world, ☃, ☃, ☃);
         }

         if (this.settings.useMonuments) {
            this.oceanMonumentGenerator.generate(this.world, ☃, ☃, ☃);
         }

         if (this.settings.useMansions) {
            this.woodlandMansionGenerator.generate(this.world, ☃, ☃, ☃);
         }
      }

      Chunk ☃x = new Chunk(this.world, ☃, ☃, ☃);
      byte[] ☃xx = ☃x.getBiomeArray();

      for (int ☃xxx = 0; ☃xxx < ☃xx.length; ☃xxx++) {
         ☃xx[☃xxx] = (byte)Biome.getIdForBiome(this.biomesForGeneration[☃xxx]);
      }

      ☃x.generateSkylightMap();
      return ☃x;
   }

   private void generateHeightmap(int var1, int var2, int var3) {
      this.depthRegion = this.depthNoise
         .generateNoiseOctaves(
            this.depthRegion, ☃, ☃, 5, 5, this.settings.depthNoiseScaleX, this.settings.depthNoiseScaleZ, this.settings.depthNoiseScaleExponent
         );
      float ☃ = this.settings.coordinateScale;
      float ☃x = this.settings.heightScale;
      this.mainNoiseRegion = this.mainPerlinNoise
         .generateNoiseOctaves(
            this.mainNoiseRegion, ☃, ☃, ☃, 5, 33, 5, ☃ / this.settings.mainNoiseScaleX, ☃x / this.settings.mainNoiseScaleY, ☃ / this.settings.mainNoiseScaleZ
         );
      this.minLimitRegion = this.minLimitPerlinNoise.generateNoiseOctaves(this.minLimitRegion, ☃, ☃, ☃, 5, 33, 5, ☃, ☃x, ☃);
      this.maxLimitRegion = this.maxLimitPerlinNoise.generateNoiseOctaves(this.maxLimitRegion, ☃, ☃, ☃, 5, 33, 5, ☃, ☃x, ☃);
      int ☃xx = 0;
      int ☃xxx = 0;

      for (int ☃xxxx = 0; ☃xxxx < 5; ☃xxxx++) {
         for (int ☃xxxxx = 0; ☃xxxxx < 5; ☃xxxxx++) {
            float ☃xxxxxx = 0.0F;
            float ☃xxxxxxx = 0.0F;
            float ☃xxxxxxxx = 0.0F;
            int ☃xxxxxxxxx = 2;
            Biome ☃xxxxxxxxxx = this.biomesForGeneration[☃xxxx + 2 + (☃xxxxx + 2) * 10];

            for (int ☃xxxxxxxxxxx = -2; ☃xxxxxxxxxxx <= 2; ☃xxxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxxx = -2; ☃xxxxxxxxxxxx <= 2; ☃xxxxxxxxxxxx++) {
                  Biome ☃xxxxxxxxxxxxx = this.biomesForGeneration[☃xxxx + ☃xxxxxxxxxxx + 2 + (☃xxxxx + ☃xxxxxxxxxxxx + 2) * 10];
                  float ☃xxxxxxxxxxxxxx = this.settings.biomeDepthOffSet + ☃xxxxxxxxxxxxx.getBaseHeight() * this.settings.biomeDepthWeight;
                  float ☃xxxxxxxxxxxxxxx = this.settings.biomeScaleOffset + ☃xxxxxxxxxxxxx.getHeightVariation() * this.settings.biomeScaleWeight;
                  if (this.terrainType == WorldType.AMPLIFIED && ☃xxxxxxxxxxxxxx > 0.0F) {
                     ☃xxxxxxxxxxxxxx = 1.0F + ☃xxxxxxxxxxxxxx * 2.0F;
                     ☃xxxxxxxxxxxxxxx = 1.0F + ☃xxxxxxxxxxxxxxx * 4.0F;
                  }

                  float ☃xxxxxxxxxxxxxxxx = this.biomeWeights[☃xxxxxxxxxxx + 2 + (☃xxxxxxxxxxxx + 2) * 5] / (☃xxxxxxxxxxxxxx + 2.0F);
                  if (☃xxxxxxxxxxxxx.getBaseHeight() > ☃xxxxxxxxxx.getBaseHeight()) {
                     ☃xxxxxxxxxxxxxxxx /= 2.0F;
                  }

                  ☃xxxxxx += ☃xxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx;
                  ☃xxxxxxx += ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx;
                  ☃xxxxxxxx += ☃xxxxxxxxxxxxxxxx;
               }
            }

            ☃xxxxxx /= ☃xxxxxxxx;
            ☃xxxxxxx /= ☃xxxxxxxx;
            ☃xxxxxx = ☃xxxxxx * 0.9F + 0.1F;
            ☃xxxxxxx = (☃xxxxxxx * 4.0F - 1.0F) / 8.0F;
            double ☃xxxxxxxxxxx = this.depthRegion[☃xxx] / 8000.0;
            if (☃xxxxxxxxxxx < 0.0) {
               ☃xxxxxxxxxxx = -☃xxxxxxxxxxx * 0.3;
            }

            ☃xxxxxxxxxxx = ☃xxxxxxxxxxx * 3.0 - 2.0;
            if (☃xxxxxxxxxxx < 0.0) {
               ☃xxxxxxxxxxx /= 2.0;
               if (☃xxxxxxxxxxx < -1.0) {
                  ☃xxxxxxxxxxx = -1.0;
               }

               ☃xxxxxxxxxxx /= 1.4;
               ☃xxxxxxxxxxx /= 2.0;
            } else {
               if (☃xxxxxxxxxxx > 1.0) {
                  ☃xxxxxxxxxxx = 1.0;
               }

               ☃xxxxxxxxxxx /= 8.0;
            }

            ☃xxx++;
            double ☃xxxxxxxxxxxx = ☃xxxxxxx;
            double ☃xxxxxxxxxxxxxxxx = ☃xxxxxx;
            ☃xxxxxxxxxxxx += ☃xxxxxxxxxxx * 0.2;
            ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxx * this.settings.baseSize / 8.0;
            double ☃xxxxxxxxxxxxxxxxx = this.settings.baseSize + ☃xxxxxxxxxxxx * 4.0;

            for (int ☃xxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxx < 33; ☃xxxxxxxxxxxxxxxxxx++) {
               double ☃xxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxx) * this.settings.stretchY * 128.0 / 256.0 / ☃xxxxxxxxxxxxxxxx;
               if (☃xxxxxxxxxxxxxxxxxxx < 0.0) {
                  ☃xxxxxxxxxxxxxxxxxxx *= 4.0;
               }

               double ☃xxxxxxxxxxxxxxxxxxxx = this.minLimitRegion[☃xx] / this.settings.lowerLimitScale;
               double ☃xxxxxxxxxxxxxxxxxxxxx = this.maxLimitRegion[☃xx] / this.settings.upperLimitScale;
               double ☃xxxxxxxxxxxxxxxxxxxxxx = (this.mainNoiseRegion[☃xx] / 10.0 + 1.0) / 2.0;
               double ☃xxxxxxxxxxxxxxxxxxxxxxx = MathHelper.clampedLerp(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx)
                  - ☃xxxxxxxxxxxxxxxxxxx;
               if (☃xxxxxxxxxxxxxxxxxx > 29) {
                  double ☃xxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxx - 29) / 3.0F;
                  ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxx * (1.0 - ☃xxxxxxxxxxxxxxxxxxxxxxxx) + -10.0 * ☃xxxxxxxxxxxxxxxxxxxxxxxx;
               }

               this.heightMap[☃xx] = ☃xxxxxxxxxxxxxxxxxxxxxxx;
               ☃xx++;
            }
         }
      }
   }

   @Override
   public void populate(int var1, int var2) {
      BlockFalling.fallInstantly = true;
      int ☃ = ☃ * 16;
      int ☃x = ☃ * 16;
      BlockPos ☃xx = new BlockPos(☃, 0, ☃x);
      Biome ☃xxx = this.world.getBiome(☃xx.add(16, 0, 16));
      this.rand.setSeed(this.world.getSeed());
      long ☃xxxx = this.rand.nextLong() / 2L * 2L + 1L;
      long ☃xxxxx = this.rand.nextLong() / 2L * 2L + 1L;
      this.rand.setSeed(☃ * ☃xxxx + ☃ * ☃xxxxx ^ this.world.getSeed());
      boolean ☃xxxxxx = false;
      ChunkPos ☃xxxxxxx = new ChunkPos(☃, ☃);
      if (this.mapFeaturesEnabled) {
         if (this.settings.useMineShafts) {
            this.mineshaftGenerator.generateStructure(this.world, this.rand, ☃xxxxxxx);
         }

         if (this.settings.useVillages) {
            ☃xxxxxx = this.villageGenerator.generateStructure(this.world, this.rand, ☃xxxxxxx);
         }

         if (this.settings.useStrongholds) {
            this.strongholdGenerator.generateStructure(this.world, this.rand, ☃xxxxxxx);
         }

         if (this.settings.useTemples) {
            this.scatteredFeatureGenerator.generateStructure(this.world, this.rand, ☃xxxxxxx);
         }

         if (this.settings.useMonuments) {
            this.oceanMonumentGenerator.generateStructure(this.world, this.rand, ☃xxxxxxx);
         }

         if (this.settings.useMansions) {
            this.woodlandMansionGenerator.generateStructure(this.world, this.rand, ☃xxxxxxx);
         }
      }

      if (☃xxx != Biomes.DESERT
         && ☃xxx != Biomes.DESERT_HILLS
         && this.settings.useWaterLakes
         && !☃xxxxxx
         && this.rand.nextInt(this.settings.waterLakeChance) == 0) {
         int ☃xxxxxxxx = this.rand.nextInt(16) + 8;
         int ☃xxxxxxxxx = this.rand.nextInt(256);
         int ☃xxxxxxxxxx = this.rand.nextInt(16) + 8;
         new WorldGenLakes(Blocks.WATER).generate(this.world, this.rand, ☃xx.add(☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx));
      }

      if (!☃xxxxxx && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes) {
         int ☃xxxxxxxx = this.rand.nextInt(16) + 8;
         int ☃xxxxxxxxx = this.rand.nextInt(this.rand.nextInt(248) + 8);
         int ☃xxxxxxxxxx = this.rand.nextInt(16) + 8;
         if (☃xxxxxxxxx < this.world.getSeaLevel() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0) {
            new WorldGenLakes(Blocks.LAVA).generate(this.world, this.rand, ☃xx.add(☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx));
         }
      }

      if (this.settings.useDungeons) {
         for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < this.settings.dungeonChance; ☃xxxxxxxx++) {
            int ☃xxxxxxxxx = this.rand.nextInt(16) + 8;
            int ☃xxxxxxxxxx = this.rand.nextInt(256);
            int ☃xxxxxxxxxxx = this.rand.nextInt(16) + 8;
            new WorldGenDungeons().generate(this.world, this.rand, ☃xx.add(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx));
         }
      }

      ☃xxx.decorate(this.world, this.rand, new BlockPos(☃, 0, ☃x));
      WorldEntitySpawner.performWorldGenSpawning(this.world, ☃xxx, ☃ + 8, ☃x + 8, 16, 16, this.rand);
      ☃xx = ☃xx.add(8, 0, 8);

      for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < 16; ☃xxxxxxxx++) {
         for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < 16; ☃xxxxxxxxx++) {
            BlockPos ☃xxxxxxxxxx = this.world.getPrecipitationHeight(☃xx.add(☃xxxxxxxx, 0, ☃xxxxxxxxx));
            BlockPos ☃xxxxxxxxxxx = ☃xxxxxxxxxx.down();
            if (this.world.canBlockFreezeWater(☃xxxxxxxxxxx)) {
               this.world.setBlockState(☃xxxxxxxxxxx, Blocks.ICE.getDefaultState(), 2);
            }

            if (this.world.canSnowAt(☃xxxxxxxxxx, true)) {
               this.world.setBlockState(☃xxxxxxxxxx, Blocks.SNOW_LAYER.getDefaultState(), 2);
            }
         }
      }

      BlockFalling.fallInstantly = false;
   }

   @Override
   public boolean generateStructures(Chunk var1, int var2, int var3) {
      boolean ☃ = false;
      if (this.settings.useMonuments && this.mapFeaturesEnabled && ☃.getInhabitedTime() < 3600L) {
         ☃ |= this.oceanMonumentGenerator.generateStructure(this.world, this.rand, new ChunkPos(☃, ☃));
      }

      return ☃;
   }

   @Override
   public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType var1, BlockPos var2) {
      Biome ☃ = this.world.getBiome(☃);
      if (this.mapFeaturesEnabled) {
         if (☃ == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.isSwampHut(☃)) {
            return this.scatteredFeatureGenerator.getMonsters();
         }

         if (☃ == EnumCreatureType.MONSTER && this.settings.useMonuments && this.oceanMonumentGenerator.isPositionInStructure(this.world, ☃)) {
            return this.oceanMonumentGenerator.getMonsters();
         }
      }

      return ☃.getSpawnableList(☃);
   }

   @Override
   public boolean isInsideStructure(World var1, String var2, BlockPos var3) {
      if (!this.mapFeaturesEnabled) {
         return false;
      } else if ("Stronghold".equals(☃) && this.strongholdGenerator != null) {
         return this.strongholdGenerator.isInsideStructure(☃);
      } else if ("Mansion".equals(☃) && this.woodlandMansionGenerator != null) {
         return this.woodlandMansionGenerator.isInsideStructure(☃);
      } else if ("Monument".equals(☃) && this.oceanMonumentGenerator != null) {
         return this.oceanMonumentGenerator.isInsideStructure(☃);
      } else if ("Village".equals(☃) && this.villageGenerator != null) {
         return this.villageGenerator.isInsideStructure(☃);
      } else if ("Mineshaft".equals(☃) && this.mineshaftGenerator != null) {
         return this.mineshaftGenerator.isInsideStructure(☃);
      } else {
         return "Temple".equals(☃) && this.scatteredFeatureGenerator != null ? this.scatteredFeatureGenerator.isInsideStructure(☃) : false;
      }
   }

   @Nullable
   @Override
   public BlockPos getNearestStructurePos(World var1, String var2, BlockPos var3, boolean var4) {
      if (!this.mapFeaturesEnabled) {
         return null;
      } else if ("Stronghold".equals(☃) && this.strongholdGenerator != null) {
         return this.strongholdGenerator.getNearestStructurePos(☃, ☃, ☃);
      } else if ("Mansion".equals(☃) && this.woodlandMansionGenerator != null) {
         return this.woodlandMansionGenerator.getNearestStructurePos(☃, ☃, ☃);
      } else if ("Monument".equals(☃) && this.oceanMonumentGenerator != null) {
         return this.oceanMonumentGenerator.getNearestStructurePos(☃, ☃, ☃);
      } else if ("Village".equals(☃) && this.villageGenerator != null) {
         return this.villageGenerator.getNearestStructurePos(☃, ☃, ☃);
      } else if ("Mineshaft".equals(☃) && this.mineshaftGenerator != null) {
         return this.mineshaftGenerator.getNearestStructurePos(☃, ☃, ☃);
      } else {
         return "Temple".equals(☃) && this.scatteredFeatureGenerator != null ? this.scatteredFeatureGenerator.getNearestStructurePos(☃, ☃, ☃) : null;
      }
   }

   @Override
   public void recreateStructures(Chunk var1, int var2, int var3) {
      if (this.mapFeaturesEnabled) {
         if (this.settings.useMineShafts) {
            this.mineshaftGenerator.generate(this.world, ☃, ☃, null);
         }

         if (this.settings.useVillages) {
            this.villageGenerator.generate(this.world, ☃, ☃, null);
         }

         if (this.settings.useStrongholds) {
            this.strongholdGenerator.generate(this.world, ☃, ☃, null);
         }

         if (this.settings.useTemples) {
            this.scatteredFeatureGenerator.generate(this.world, ☃, ☃, null);
         }

         if (this.settings.useMonuments) {
            this.oceanMonumentGenerator.generate(this.world, ☃, ☃, null);
         }

         if (this.settings.useMansions) {
            this.woodlandMansionGenerator.generate(this.world, ☃, ☃, null);
         }
      }
   }
}
