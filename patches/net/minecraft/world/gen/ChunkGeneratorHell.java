package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.feature.WorldGenGlowStone2;
import net.minecraft.world.gen.feature.WorldGenHellLava;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.MapGenNetherBridge;

public class ChunkGeneratorHell implements IChunkGenerator {
   protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
   protected static final IBlockState NETHERRACK = Blocks.NETHERRACK.getDefaultState();
   protected static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
   protected static final IBlockState LAVA = Blocks.LAVA.getDefaultState();
   protected static final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
   protected static final IBlockState SOUL_SAND = Blocks.SOUL_SAND.getDefaultState();
   private final World world;
   private final boolean generateStructures;
   private final Random rand;
   private double[] slowsandNoise = new double[256];
   private double[] gravelNoise = new double[256];
   private double[] depthBuffer = new double[256];
   private double[] buffer;
   private final NoiseGeneratorOctaves lperlinNoise1;
   private final NoiseGeneratorOctaves lperlinNoise2;
   private final NoiseGeneratorOctaves perlinNoise1;
   private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
   private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
   public final NoiseGeneratorOctaves scaleNoise;
   public final NoiseGeneratorOctaves depthNoise;
   private final WorldGenFire fireFeature = new WorldGenFire();
   private final WorldGenGlowStone1 lightGemGen = new WorldGenGlowStone1();
   private final WorldGenGlowStone2 hellPortalGen = new WorldGenGlowStone2();
   private final WorldGenerator quartzGen = new WorldGenMinable(Blocks.QUARTZ_ORE.getDefaultState(), 14, BlockMatcher.forBlock(Blocks.NETHERRACK));
   private final WorldGenerator magmaGen = new WorldGenMinable(Blocks.MAGMA.getDefaultState(), 33, BlockMatcher.forBlock(Blocks.NETHERRACK));
   private final WorldGenHellLava lavaTrapGen = new WorldGenHellLava(Blocks.FLOWING_LAVA, true);
   private final WorldGenHellLava hellSpringGen = new WorldGenHellLava(Blocks.FLOWING_LAVA, false);
   private final WorldGenBush brownMushroomFeature = new WorldGenBush(Blocks.BROWN_MUSHROOM);
   private final WorldGenBush redMushroomFeature = new WorldGenBush(Blocks.RED_MUSHROOM);
   private final MapGenNetherBridge genNetherBridge = new MapGenNetherBridge();
   private final MapGenBase genNetherCaves = new MapGenCavesHell();
   double[] pnr;
   double[] ar;
   double[] br;
   double[] noiseData4;
   double[] dr;

   public ChunkGeneratorHell(World var1, boolean var2, long var3) {
      this.world = ☃;
      this.generateStructures = ☃;
      this.rand = new Random(☃);
      this.lperlinNoise1 = new NoiseGeneratorOctaves(this.rand, 16);
      this.lperlinNoise2 = new NoiseGeneratorOctaves(this.rand, 16);
      this.perlinNoise1 = new NoiseGeneratorOctaves(this.rand, 8);
      this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.rand, 4);
      this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(this.rand, 4);
      this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
      this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
      ☃.setSeaLevel(63);
   }

   public void prepareHeights(int var1, int var2, ChunkPrimer var3) {
      int ☃ = 4;
      int ☃x = this.world.getSeaLevel() / 2 + 1;
      int ☃xx = 5;
      int ☃xxx = 17;
      int ☃xxxx = 5;
      this.buffer = this.getHeights(this.buffer, ☃ * 4, 0, ☃ * 4, 5, 17, 5);

      for (int ☃xxxxx = 0; ☃xxxxx < 4; ☃xxxxx++) {
         for (int ☃xxxxxx = 0; ☃xxxxxx < 4; ☃xxxxxx++) {
            for (int ☃xxxxxxx = 0; ☃xxxxxxx < 16; ☃xxxxxxx++) {
               double ☃xxxxxxxx = 0.125;
               double ☃xxxxxxxxx = this.buffer[((☃xxxxx + 0) * 5 + ☃xxxxxx + 0) * 17 + ☃xxxxxxx + 0];
               double ☃xxxxxxxxxx = this.buffer[((☃xxxxx + 0) * 5 + ☃xxxxxx + 1) * 17 + ☃xxxxxxx + 0];
               double ☃xxxxxxxxxxx = this.buffer[((☃xxxxx + 1) * 5 + ☃xxxxxx + 0) * 17 + ☃xxxxxxx + 0];
               double ☃xxxxxxxxxxxx = this.buffer[((☃xxxxx + 1) * 5 + ☃xxxxxx + 1) * 17 + ☃xxxxxxx + 0];
               double ☃xxxxxxxxxxxxx = (this.buffer[((☃xxxxx + 0) * 5 + ☃xxxxxx + 0) * 17 + ☃xxxxxxx + 1] - ☃xxxxxxxxx) * 0.125;
               double ☃xxxxxxxxxxxxxx = (this.buffer[((☃xxxxx + 0) * 5 + ☃xxxxxx + 1) * 17 + ☃xxxxxxx + 1] - ☃xxxxxxxxxx) * 0.125;
               double ☃xxxxxxxxxxxxxxx = (this.buffer[((☃xxxxx + 1) * 5 + ☃xxxxxx + 0) * 17 + ☃xxxxxxx + 1] - ☃xxxxxxxxxxx) * 0.125;
               double ☃xxxxxxxxxxxxxxxx = (this.buffer[((☃xxxxx + 1) * 5 + ☃xxxxxx + 1) * 17 + ☃xxxxxxx + 1] - ☃xxxxxxxxxxxx) * 0.125;

               for (int ☃xxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxx < 8; ☃xxxxxxxxxxxxxxxxx++) {
                  double ☃xxxxxxxxxxxxxxxxxx = 0.25;
                  double ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxx;
                  double ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxx;
                  double ☃xxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxx - ☃xxxxxxxxx) * 0.25;
                  double ☃xxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxx - ☃xxxxxxxxxx) * 0.25;

                  for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxxx++) {
                     double ☃xxxxxxxxxxxxxxxxxxxxxxxx = 0.25;
                     double ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx;
                     double ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxx) * 0.25;

                     for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
                        IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = null;
                        if (☃xxxxxxx * 8 + ☃xxxxxxxxxxxxxxxxx < ☃x) {
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = LAVA;
                        }

                        if (☃xxxxxxxxxxxxxxxxxxxxxxxxx > 0.0) {
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = NETHERRACK;
                        }

                        int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxx * 4;
                        int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx + ☃xxxxxxx * 8;
                        int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxx * 4;
                        ☃.setBlockState(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        );
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxxxxxxxxxxxx;
                     }

                     ☃xxxxxxxxxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxxxxxxx;
                     ☃xxxxxxxxxxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxxxxxxxx;
                  }

                  ☃xxxxxxxxx += ☃xxxxxxxxxxxxx;
                  ☃xxxxxxxxxx += ☃xxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxx += ☃xxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxx;
               }
            }
         }
      }
   }

   public void buildSurfaces(int var1, int var2, ChunkPrimer var3) {
      int ☃ = this.world.getSeaLevel() + 1;
      double ☃x = 0.03125;
      this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, ☃ * 16, ☃ * 16, 0, 16, 16, 1, 0.03125, 0.03125, 1.0);
      this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, ☃ * 16, 109, ☃ * 16, 16, 1, 16, 0.03125, 1.0, 0.03125);
      this.depthBuffer = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.depthBuffer, ☃ * 16, ☃ * 16, 0, 16, 16, 1, 0.0625, 0.0625, 0.0625);

      for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
         for (int ☃xxx = 0; ☃xxx < 16; ☃xxx++) {
            boolean ☃xxxx = this.slowsandNoise[☃xx + ☃xxx * 16] + this.rand.nextDouble() * 0.2 > 0.0;
            boolean ☃xxxxx = this.gravelNoise[☃xx + ☃xxx * 16] + this.rand.nextDouble() * 0.2 > 0.0;
            int ☃xxxxxx = (int)(this.depthBuffer[☃xx + ☃xxx * 16] / 3.0 + 3.0 + this.rand.nextDouble() * 0.25);
            int ☃xxxxxxx = -1;
            IBlockState ☃xxxxxxxx = NETHERRACK;
            IBlockState ☃xxxxxxxxx = NETHERRACK;

            for (int ☃xxxxxxxxxx = 127; ☃xxxxxxxxxx >= 0; ☃xxxxxxxxxx--) {
               if (☃xxxxxxxxxx < 127 - this.rand.nextInt(5) && ☃xxxxxxxxxx > this.rand.nextInt(5)) {
                  IBlockState ☃xxxxxxxxxxx = ☃.getBlockState(☃xxx, ☃xxxxxxxxxx, ☃xx);
                  if (☃xxxxxxxxxxx.getBlock() == null || ☃xxxxxxxxxxx.getMaterial() == Material.AIR) {
                     ☃xxxxxxx = -1;
                  } else if (☃xxxxxxxxxxx.getBlock() == Blocks.NETHERRACK) {
                     if (☃xxxxxxx == -1) {
                        if (☃xxxxxx <= 0) {
                           ☃xxxxxxxx = AIR;
                           ☃xxxxxxxxx = NETHERRACK;
                        } else if (☃xxxxxxxxxx >= ☃ - 4 && ☃xxxxxxxxxx <= ☃ + 1) {
                           ☃xxxxxxxx = NETHERRACK;
                           ☃xxxxxxxxx = NETHERRACK;
                           if (☃xxxxx) {
                              ☃xxxxxxxx = GRAVEL;
                              ☃xxxxxxxxx = NETHERRACK;
                           }

                           if (☃xxxx) {
                              ☃xxxxxxxx = SOUL_SAND;
                              ☃xxxxxxxxx = SOUL_SAND;
                           }
                        }

                        if (☃xxxxxxxxxx < ☃ && (☃xxxxxxxx == null || ☃xxxxxxxx.getMaterial() == Material.AIR)) {
                           ☃xxxxxxxx = LAVA;
                        }

                        ☃xxxxxxx = ☃xxxxxx;
                        if (☃xxxxxxxxxx >= ☃ - 1) {
                           ☃.setBlockState(☃xxx, ☃xxxxxxxxxx, ☃xx, ☃xxxxxxxx);
                        } else {
                           ☃.setBlockState(☃xxx, ☃xxxxxxxxxx, ☃xx, ☃xxxxxxxxx);
                        }
                     } else if (☃xxxxxxx > 0) {
                        ☃xxxxxxx--;
                        ☃.setBlockState(☃xxx, ☃xxxxxxxxxx, ☃xx, ☃xxxxxxxxx);
                     }
                  }
               } else {
                  ☃.setBlockState(☃xxx, ☃xxxxxxxxxx, ☃xx, BEDROCK);
               }
            }
         }
      }
   }

   @Override
   public Chunk generateChunk(int var1, int var2) {
      this.rand.setSeed(☃ * 341873128712L + ☃ * 132897987541L);
      ChunkPrimer ☃ = new ChunkPrimer();
      this.prepareHeights(☃, ☃, ☃);
      this.buildSurfaces(☃, ☃, ☃);
      this.genNetherCaves.generate(this.world, ☃, ☃, ☃);
      if (this.generateStructures) {
         this.genNetherBridge.generate(this.world, ☃, ☃, ☃);
      }

      Chunk ☃x = new Chunk(this.world, ☃, ☃, ☃);
      Biome[] ☃xx = this.world.getBiomeProvider().getBiomes(null, ☃ * 16, ☃ * 16, 16, 16);
      byte[] ☃xxx = ☃x.getBiomeArray();

      for (int ☃xxxx = 0; ☃xxxx < ☃xxx.length; ☃xxxx++) {
         ☃xxx[☃xxxx] = (byte)Biome.getIdForBiome(☃xx[☃xxxx]);
      }

      ☃x.resetRelightChecks();
      return ☃x;
   }

   private double[] getHeights(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if (☃ == null) {
         ☃ = new double[☃ * ☃ * ☃];
      }

      double ☃ = 684.412;
      double ☃x = 2053.236;
      this.noiseData4 = this.scaleNoise.generateNoiseOctaves(this.noiseData4, ☃, ☃, ☃, ☃, 1, ☃, 1.0, 0.0, 1.0);
      this.dr = this.depthNoise.generateNoiseOctaves(this.dr, ☃, ☃, ☃, ☃, 1, ☃, 100.0, 0.0, 100.0);
      this.pnr = this.perlinNoise1.generateNoiseOctaves(this.pnr, ☃, ☃, ☃, ☃, ☃, ☃, 8.555150000000001, 34.2206, 8.555150000000001);
      this.ar = this.lperlinNoise1.generateNoiseOctaves(this.ar, ☃, ☃, ☃, ☃, ☃, ☃, 684.412, 2053.236, 684.412);
      this.br = this.lperlinNoise2.generateNoiseOctaves(this.br, ☃, ☃, ☃, ☃, ☃, ☃, 684.412, 2053.236, 684.412);
      int ☃xx = 0;
      double[] ☃xxx = new double[☃];

      for (int ☃xxxx = 0; ☃xxxx < ☃; ☃xxxx++) {
         ☃xxx[☃xxxx] = Math.cos(☃xxxx * Math.PI * 6.0 / ☃) * 2.0;
         double ☃xxxxx = ☃xxxx;
         if (☃xxxx > ☃ / 2) {
            ☃xxxxx = ☃ - 1 - ☃xxxx;
         }

         if (☃xxxxx < 4.0) {
            ☃xxxxx = 4.0 - ☃xxxxx;
            ☃xxx[☃xxxx] -= ☃xxxxx * ☃xxxxx * ☃xxxxx * 10.0;
         }
      }

      for (int ☃xxxx = 0; ☃xxxx < ☃; ☃xxxx++) {
         for (int ☃xxxxxx = 0; ☃xxxxxx < ☃; ☃xxxxxx++) {
            double ☃xxxxxxx = 0.0;

            for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃; ☃xxxxxxxx++) {
               double ☃xxxxxxxxx = ☃xxx[☃xxxxxxxx];
               double ☃xxxxxxxxxx = this.ar[☃xx] / 512.0;
               double ☃xxxxxxxxxxx = this.br[☃xx] / 512.0;
               double ☃xxxxxxxxxxxx = (this.pnr[☃xx] / 10.0 + 1.0) / 2.0;
               double ☃xxxxxxxxxxxxx;
               if (☃xxxxxxxxxxxx < 0.0) {
                  ☃xxxxxxxxxxxxx = ☃xxxxxxxxxx;
               } else if (☃xxxxxxxxxxxx > 1.0) {
                  ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxx;
               } else {
                  ☃xxxxxxxxxxxxx = ☃xxxxxxxxxx + (☃xxxxxxxxxxx - ☃xxxxxxxxxx) * ☃xxxxxxxxxxxx;
               }

               ☃xxxxxxxxxxxxx -= ☃xxxxxxxxx;
               if (☃xxxxxxxx > ☃ - 4) {
                  double ☃xxxxxxxxxxxxxx = (☃xxxxxxxx - (☃ - 4)) / 3.0F;
                  ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxxx * (1.0 - ☃xxxxxxxxxxxxxx) + -10.0 * ☃xxxxxxxxxxxxxx;
               }

               if (☃xxxxxxxx < 0.0) {
                  double ☃xxxxxxxxxxxxxx = (0.0 - ☃xxxxxxxx) / 4.0;
                  ☃xxxxxxxxxxxxxx = MathHelper.clamp(☃xxxxxxxxxxxxxx, 0.0, 1.0);
                  ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxxx * (1.0 - ☃xxxxxxxxxxxxxx) + -10.0 * ☃xxxxxxxxxxxxxx;
               }

               ☃[☃xx] = ☃xxxxxxxxxxxxx;
               ☃xx++;
            }
         }
      }

      return ☃;
   }

   @Override
   public void populate(int var1, int var2) {
      BlockFalling.fallInstantly = true;
      int ☃ = ☃ * 16;
      int ☃x = ☃ * 16;
      BlockPos ☃xx = new BlockPos(☃, 0, ☃x);
      Biome ☃xxx = this.world.getBiome(☃xx.add(16, 0, 16));
      ChunkPos ☃xxxx = new ChunkPos(☃, ☃);
      this.genNetherBridge.generateStructure(this.world, this.rand, ☃xxxx);

      for (int ☃xxxxx = 0; ☃xxxxx < 8; ☃xxxxx++) {
         this.hellSpringGen.generate(this.world, this.rand, ☃xx.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
      }

      for (int ☃xxxxx = 0; ☃xxxxx < this.rand.nextInt(this.rand.nextInt(10) + 1) + 1; ☃xxxxx++) {
         this.fireFeature.generate(this.world, this.rand, ☃xx.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
      }

      for (int ☃xxxxx = 0; ☃xxxxx < this.rand.nextInt(this.rand.nextInt(10) + 1); ☃xxxxx++) {
         this.lightGemGen.generate(this.world, this.rand, ☃xx.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
      }

      for (int ☃xxxxx = 0; ☃xxxxx < 10; ☃xxxxx++) {
         this.hellPortalGen.generate(this.world, this.rand, ☃xx.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
      }

      if (this.rand.nextBoolean()) {
         this.brownMushroomFeature.generate(this.world, this.rand, ☃xx.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
      }

      if (this.rand.nextBoolean()) {
         this.redMushroomFeature.generate(this.world, this.rand, ☃xx.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
      }

      for (int ☃xxxxx = 0; ☃xxxxx < 16; ☃xxxxx++) {
         this.quartzGen.generate(this.world, this.rand, ☃xx.add(this.rand.nextInt(16), this.rand.nextInt(108) + 10, this.rand.nextInt(16)));
      }

      int ☃xxxxx = this.world.getSeaLevel() / 2 + 1;

      for (int ☃xxxxxx = 0; ☃xxxxxx < 4; ☃xxxxxx++) {
         this.magmaGen.generate(this.world, this.rand, ☃xx.add(this.rand.nextInt(16), ☃xxxxx - 5 + this.rand.nextInt(10), this.rand.nextInt(16)));
      }

      for (int ☃xxxxxx = 0; ☃xxxxxx < 16; ☃xxxxxx++) {
         this.lavaTrapGen.generate(this.world, this.rand, ☃xx.add(this.rand.nextInt(16), this.rand.nextInt(108) + 10, this.rand.nextInt(16)));
      }

      ☃xxx.decorate(this.world, this.rand, new BlockPos(☃, 0, ☃x));
      BlockFalling.fallInstantly = false;
   }

   @Override
   public boolean generateStructures(Chunk var1, int var2, int var3) {
      return false;
   }

   @Override
   public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType var1, BlockPos var2) {
      if (☃ == EnumCreatureType.MONSTER) {
         if (this.genNetherBridge.isInsideStructure(☃)) {
            return this.genNetherBridge.getSpawnList();
         }

         if (this.genNetherBridge.isPositionInStructure(this.world, ☃) && this.world.getBlockState(☃.down()).getBlock() == Blocks.NETHER_BRICK) {
            return this.genNetherBridge.getSpawnList();
         }
      }

      Biome ☃ = this.world.getBiome(☃);
      return ☃.getSpawnableList(☃);
   }

   @Nullable
   @Override
   public BlockPos getNearestStructurePos(World var1, String var2, BlockPos var3, boolean var4) {
      return "Fortress".equals(☃) && this.genNetherBridge != null ? this.genNetherBridge.getNearestStructurePos(☃, ☃, ☃) : null;
   }

   @Override
   public boolean isInsideStructure(World var1, String var2, BlockPos var3) {
      return "Fortress".equals(☃) && this.genNetherBridge != null ? this.genNetherBridge.isInsideStructure(☃) : false;
   }

   @Override
   public void recreateStructures(Chunk var1, int var2, int var3) {
      this.genNetherBridge.generate(this.world, ☃, ☃, null);
   }
}
