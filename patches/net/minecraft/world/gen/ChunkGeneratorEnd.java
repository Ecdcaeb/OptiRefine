package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockChorusFlower;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenEndGateway;
import net.minecraft.world.gen.feature.WorldGenEndIsland;
import net.minecraft.world.gen.structure.MapGenEndCity;

public class ChunkGeneratorEnd implements IChunkGenerator {
   private final Random rand;
   protected static final IBlockState END_STONE = Blocks.END_STONE.getDefaultState();
   protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
   private final NoiseGeneratorOctaves lperlinNoise1;
   private final NoiseGeneratorOctaves lperlinNoise2;
   private final NoiseGeneratorOctaves perlinNoise1;
   public NoiseGeneratorOctaves noiseGen5;
   public NoiseGeneratorOctaves noiseGen6;
   private final World world;
   private final boolean mapFeaturesEnabled;
   private final BlockPos spawnPoint;
   private final MapGenEndCity endCityGen = new MapGenEndCity(this);
   private final NoiseGeneratorSimplex islandNoise;
   private double[] buffer;
   private Biome[] biomesForGeneration;
   double[] pnr;
   double[] ar;
   double[] br;
   private final WorldGenEndIsland endIslands = new WorldGenEndIsland();

   public ChunkGeneratorEnd(World var1, boolean var2, long var3, BlockPos var5) {
      this.world = ☃;
      this.mapFeaturesEnabled = ☃;
      this.spawnPoint = ☃;
      this.rand = new Random(☃);
      this.lperlinNoise1 = new NoiseGeneratorOctaves(this.rand, 16);
      this.lperlinNoise2 = new NoiseGeneratorOctaves(this.rand, 16);
      this.perlinNoise1 = new NoiseGeneratorOctaves(this.rand, 8);
      this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
      this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
      this.islandNoise = new NoiseGeneratorSimplex(this.rand);
   }

   public void setBlocksInChunk(int var1, int var2, ChunkPrimer var3) {
      int ☃ = 2;
      int ☃x = 3;
      int ☃xx = 33;
      int ☃xxx = 3;
      this.buffer = this.getHeights(this.buffer, ☃ * 2, 0, ☃ * 2, 3, 33, 3);

      for (int ☃xxxx = 0; ☃xxxx < 2; ☃xxxx++) {
         for (int ☃xxxxx = 0; ☃xxxxx < 2; ☃xxxxx++) {
            for (int ☃xxxxxx = 0; ☃xxxxxx < 32; ☃xxxxxx++) {
               double ☃xxxxxxx = 0.25;
               double ☃xxxxxxxx = this.buffer[((☃xxxx + 0) * 3 + ☃xxxxx + 0) * 33 + ☃xxxxxx + 0];
               double ☃xxxxxxxxx = this.buffer[((☃xxxx + 0) * 3 + ☃xxxxx + 1) * 33 + ☃xxxxxx + 0];
               double ☃xxxxxxxxxx = this.buffer[((☃xxxx + 1) * 3 + ☃xxxxx + 0) * 33 + ☃xxxxxx + 0];
               double ☃xxxxxxxxxxx = this.buffer[((☃xxxx + 1) * 3 + ☃xxxxx + 1) * 33 + ☃xxxxxx + 0];
               double ☃xxxxxxxxxxxx = (this.buffer[((☃xxxx + 0) * 3 + ☃xxxxx + 0) * 33 + ☃xxxxxx + 1] - ☃xxxxxxxx) * 0.25;
               double ☃xxxxxxxxxxxxx = (this.buffer[((☃xxxx + 0) * 3 + ☃xxxxx + 1) * 33 + ☃xxxxxx + 1] - ☃xxxxxxxxx) * 0.25;
               double ☃xxxxxxxxxxxxxx = (this.buffer[((☃xxxx + 1) * 3 + ☃xxxxx + 0) * 33 + ☃xxxxxx + 1] - ☃xxxxxxxxxx) * 0.25;
               double ☃xxxxxxxxxxxxxxx = (this.buffer[((☃xxxx + 1) * 3 + ☃xxxxx + 1) * 33 + ☃xxxxxx + 1] - ☃xxxxxxxxxxx) * 0.25;

               for (int ☃xxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxx++) {
                  double ☃xxxxxxxxxxxxxxxxx = 0.125;
                  double ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxx;
                  double ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxx;
                  double ☃xxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxx - ☃xxxxxxxx) * 0.125;
                  double ☃xxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxx - ☃xxxxxxxxx) * 0.125;

                  for (int ☃xxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxx < 8; ☃xxxxxxxxxxxxxxxxxxxxxx++) {
                     double ☃xxxxxxxxxxxxxxxxxxxxxxx = 0.125;
                     double ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxx;
                     double ☃xxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxx) * 0.125;

                     for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxxxx < 8; ☃xxxxxxxxxxxxxxxxxxxxxxxxxx++) {
                        IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = AIR;
                        if (☃xxxxxxxxxxxxxxxxxxxxxxxx > 0.0) {
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = END_STONE;
                        }

                        int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx + ☃xxxx * 8;
                        int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx + ☃xxxxxx * 4;
                        int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxx * 8;
                        ☃.setBlockState(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
                        );
                        ☃xxxxxxxxxxxxxxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxxxxxxxxxxx;
                     }

                     ☃xxxxxxxxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxxxxxx;
                     ☃xxxxxxxxxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxxxxxxx;
                  }

                  ☃xxxxxxxx += ☃xxxxxxxxxxxx;
                  ☃xxxxxxxxx += ☃xxxxxxxxxxxxx;
                  ☃xxxxxxxxxx += ☃xxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxx += ☃xxxxxxxxxxxxxxx;
               }
            }
         }
      }
   }

   public void buildSurfaces(ChunkPrimer var1) {
      for (int ☃ = 0; ☃ < 16; ☃++) {
         for (int ☃x = 0; ☃x < 16; ☃x++) {
            int ☃xx = 1;
            int ☃xxx = -1;
            IBlockState ☃xxxx = END_STONE;
            IBlockState ☃xxxxx = END_STONE;

            for (int ☃xxxxxx = 127; ☃xxxxxx >= 0; ☃xxxxxx--) {
               IBlockState ☃xxxxxxx = ☃.getBlockState(☃, ☃xxxxxx, ☃x);
               if (☃xxxxxxx.getMaterial() == Material.AIR) {
                  ☃xxx = -1;
               } else if (☃xxxxxxx.getBlock() == Blocks.STONE) {
                  if (☃xxx == -1) {
                     ☃xxx = 1;
                     if (☃xxxxxx >= 0) {
                        ☃.setBlockState(☃, ☃xxxxxx, ☃x, ☃xxxx);
                     } else {
                        ☃.setBlockState(☃, ☃xxxxxx, ☃x, ☃xxxxx);
                     }
                  } else if (☃xxx > 0) {
                     ☃xxx--;
                     ☃.setBlockState(☃, ☃xxxxxx, ☃x, ☃xxxxx);
                  }
               }
            }
         }
      }
   }

   @Override
   public Chunk generateChunk(int var1, int var2) {
      this.rand.setSeed(☃ * 341873128712L + ☃ * 132897987541L);
      ChunkPrimer ☃ = new ChunkPrimer();
      this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, ☃ * 16, ☃ * 16, 16, 16);
      this.setBlocksInChunk(☃, ☃, ☃);
      this.buildSurfaces(☃);
      if (this.mapFeaturesEnabled) {
         this.endCityGen.generate(this.world, ☃, ☃, ☃);
      }

      Chunk ☃x = new Chunk(this.world, ☃, ☃, ☃);
      byte[] ☃xx = ☃x.getBiomeArray();

      for (int ☃xxx = 0; ☃xxx < ☃xx.length; ☃xxx++) {
         ☃xx[☃xxx] = (byte)Biome.getIdForBiome(this.biomesForGeneration[☃xxx]);
      }

      ☃x.generateSkylightMap();
      return ☃x;
   }

   private float getIslandHeightValue(int var1, int var2, int var3, int var4) {
      float ☃ = ☃ * 2 + ☃;
      float ☃x = ☃ * 2 + ☃;
      float ☃xx = 100.0F - MathHelper.sqrt(☃ * ☃ + ☃x * ☃x) * 8.0F;
      if (☃xx > 80.0F) {
         ☃xx = 80.0F;
      }

      if (☃xx < -100.0F) {
         ☃xx = -100.0F;
      }

      for (int ☃xxx = -12; ☃xxx <= 12; ☃xxx++) {
         for (int ☃xxxx = -12; ☃xxxx <= 12; ☃xxxx++) {
            long ☃xxxxx = ☃ + ☃xxx;
            long ☃xxxxxx = ☃ + ☃xxxx;
            if (☃xxxxx * ☃xxxxx + ☃xxxxxx * ☃xxxxxx > 4096L && this.islandNoise.getValue(☃xxxxx, ☃xxxxxx) < -0.9F) {
               float ☃xxxxxxx = (MathHelper.abs((float)☃xxxxx) * 3439.0F + MathHelper.abs((float)☃xxxxxx) * 147.0F) % 13.0F + 9.0F;
               ☃ = ☃ - ☃xxx * 2;
               ☃x = ☃ - ☃xxxx * 2;
               float ☃xxxxxxxx = 100.0F - MathHelper.sqrt(☃ * ☃ + ☃x * ☃x) * ☃xxxxxxx;
               if (☃xxxxxxxx > 80.0F) {
                  ☃xxxxxxxx = 80.0F;
               }

               if (☃xxxxxxxx < -100.0F) {
                  ☃xxxxxxxx = -100.0F;
               }

               if (☃xxxxxxxx > ☃xx) {
                  ☃xx = ☃xxxxxxxx;
               }
            }
         }
      }

      return ☃xx;
   }

   public boolean isIslandChunk(int var1, int var2) {
      return (long)☃ * ☃ + (long)☃ * ☃ > 4096L && this.getIslandHeightValue(☃, ☃, 1, 1) >= 0.0F;
   }

   private double[] getHeights(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if (☃ == null) {
         ☃ = new double[☃ * ☃ * ☃];
      }

      double ☃ = 684.412;
      double ☃x = 684.412;
      ☃ *= 2.0;
      this.pnr = this.perlinNoise1.generateNoiseOctaves(this.pnr, ☃, ☃, ☃, ☃, ☃, ☃, ☃ / 80.0, 4.277575000000001, ☃ / 80.0);
      this.ar = this.lperlinNoise1.generateNoiseOctaves(this.ar, ☃, ☃, ☃, ☃, ☃, ☃, ☃, 684.412, ☃);
      this.br = this.lperlinNoise2.generateNoiseOctaves(this.br, ☃, ☃, ☃, ☃, ☃, ☃, ☃, 684.412, ☃);
      int ☃xx = ☃ / 2;
      int ☃xxx = ☃ / 2;
      int ☃xxxx = 0;

      for (int ☃xxxxx = 0; ☃xxxxx < ☃; ☃xxxxx++) {
         for (int ☃xxxxxx = 0; ☃xxxxxx < ☃; ☃xxxxxx++) {
            float ☃xxxxxxx = this.getIslandHeightValue(☃xx, ☃xxx, ☃xxxxx, ☃xxxxxx);

            for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃; ☃xxxxxxxx++) {
               double ☃xxxxxxxxx = this.ar[☃xxxx] / 512.0;
               double ☃xxxxxxxxxx = this.br[☃xxxx] / 512.0;
               double ☃xxxxxxxxxxx = (this.pnr[☃xxxx] / 10.0 + 1.0) / 2.0;
               double ☃xxxxxxxxxxxx;
               if (☃xxxxxxxxxxx < 0.0) {
                  ☃xxxxxxxxxxxx = ☃xxxxxxxxx;
               } else if (☃xxxxxxxxxxx > 1.0) {
                  ☃xxxxxxxxxxxx = ☃xxxxxxxxxx;
               } else {
                  ☃xxxxxxxxxxxx = ☃xxxxxxxxx + (☃xxxxxxxxxx - ☃xxxxxxxxx) * ☃xxxxxxxxxxx;
               }

               ☃xxxxxxxxxxxx -= 8.0;
               ☃xxxxxxxxxxxx += ☃xxxxxxx;
               int ☃xxxxxxxxxxxxx = 2;
               if (☃xxxxxxxx > ☃ / 2 - ☃xxxxxxxxxxxxx) {
                  double ☃xxxxxxxxxxxxxx = (☃xxxxxxxx - (☃ / 2 - ☃xxxxxxxxxxxxx)) / 64.0F;
                  ☃xxxxxxxxxxxxxx = MathHelper.clamp(☃xxxxxxxxxxxxxx, 0.0, 1.0);
                  ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxx * (1.0 - ☃xxxxxxxxxxxxxx) + -3000.0 * ☃xxxxxxxxxxxxxx;
               }

               int var33 = 8;
               if (☃xxxxxxxx < var33) {
                  double ☃xxxxxxxxxxxxxx = (var33 - ☃xxxxxxxx) / (var33 - 1.0F);
                  ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxx * (1.0 - ☃xxxxxxxxxxxxxx) + -30.0 * ☃xxxxxxxxxxxxxx;
               }

               ☃[☃xxxx] = ☃xxxxxxxxxxxx;
               ☃xxxx++;
            }
         }
      }

      return ☃;
   }

   @Override
   public void populate(int var1, int var2) {
      BlockFalling.fallInstantly = true;
      BlockPos ☃ = new BlockPos(☃ * 16, 0, ☃ * 16);
      if (this.mapFeaturesEnabled) {
         this.endCityGen.generateStructure(this.world, this.rand, new ChunkPos(☃, ☃));
      }

      this.world.getBiome(☃.add(16, 0, 16)).decorate(this.world, this.world.rand, ☃);
      long ☃x = (long)☃ * ☃ + (long)☃ * ☃;
      if (☃x > 4096L) {
         float ☃xx = this.getIslandHeightValue(☃, ☃, 1, 1);
         if (☃xx < -20.0F && this.rand.nextInt(14) == 0) {
            this.endIslands.generate(this.world, this.rand, ☃.add(this.rand.nextInt(16) + 8, 55 + this.rand.nextInt(16), this.rand.nextInt(16) + 8));
            if (this.rand.nextInt(4) == 0) {
               this.endIslands.generate(this.world, this.rand, ☃.add(this.rand.nextInt(16) + 8, 55 + this.rand.nextInt(16), this.rand.nextInt(16) + 8));
            }
         }

         if (this.getIslandHeightValue(☃, ☃, 1, 1) > 40.0F) {
            int ☃xxx = this.rand.nextInt(5);

            for (int ☃xxxx = 0; ☃xxxx < ☃xxx; ☃xxxx++) {
               int ☃xxxxx = this.rand.nextInt(16) + 8;
               int ☃xxxxxx = this.rand.nextInt(16) + 8;
               int ☃xxxxxxx = this.world.getHeight(☃.add(☃xxxxx, 0, ☃xxxxxx)).getY();
               if (☃xxxxxxx > 0) {
                  int ☃xxxxxxxx = ☃xxxxxxx - 1;
                  if (this.world.isAirBlock(☃.add(☃xxxxx, ☃xxxxxxxx + 1, ☃xxxxxx))
                     && this.world.getBlockState(☃.add(☃xxxxx, ☃xxxxxxxx, ☃xxxxxx)).getBlock() == Blocks.END_STONE) {
                     BlockChorusFlower.generatePlant(this.world, ☃.add(☃xxxxx, ☃xxxxxxxx + 1, ☃xxxxxx), this.rand, 8);
                  }
               }
            }

            if (this.rand.nextInt(700) == 0) {
               int ☃xxxxx = this.rand.nextInt(16) + 8;
               int ☃xxxxxx = this.rand.nextInt(16) + 8;
               int ☃xxxxxxx = this.world.getHeight(☃.add(☃xxxxx, 0, ☃xxxxxx)).getY();
               if (☃xxxxxxx > 0) {
                  int ☃xxxxxxxx = ☃xxxxxxx + 3 + this.rand.nextInt(7);
                  BlockPos ☃xxxxxxxxx = ☃.add(☃xxxxx, ☃xxxxxxxx, ☃xxxxxx);
                  new WorldGenEndGateway().generate(this.world, this.rand, ☃xxxxxxxxx);
                  TileEntity ☃xxxxxxxxxx = this.world.getTileEntity(☃xxxxxxxxx);
                  if (☃xxxxxxxxxx instanceof TileEntityEndGateway) {
                     TileEntityEndGateway ☃xxxxxxxxxxx = (TileEntityEndGateway)☃xxxxxxxxxx;
                     ☃xxxxxxxxxxx.setExactPosition(this.spawnPoint);
                  }
               }
            }
         }
      }

      BlockFalling.fallInstantly = false;
   }

   @Override
   public boolean generateStructures(Chunk var1, int var2, int var3) {
      return false;
   }

   @Override
   public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType var1, BlockPos var2) {
      return this.world.getBiome(☃).getSpawnableList(☃);
   }

   @Nullable
   @Override
   public BlockPos getNearestStructurePos(World var1, String var2, BlockPos var3, boolean var4) {
      return "EndCity".equals(☃) && this.endCityGen != null ? this.endCityGen.getNearestStructurePos(☃, ☃, ☃) : null;
   }

   @Override
   public boolean isInsideStructure(World var1, String var2, BlockPos var3) {
      return "EndCity".equals(☃) && this.endCityGen != null ? this.endCityGen.isInsideStructure(☃) : false;
   }

   @Override
   public void recreateStructures(Chunk var1, int var2, int var3) {
   }
}
