package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Biomes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.storage.WorldInfo;

public class BiomeProvider {
   private ChunkGeneratorSettings settings;
   private GenLayer genBiomes;
   private GenLayer biomeIndexLayer;
   private final BiomeCache biomeCache = new BiomeCache(this);
   private final List<Biome> biomesToSpawnIn = Lists.newArrayList(
      new Biome[]{Biomes.FOREST, Biomes.PLAINS, Biomes.TAIGA, Biomes.TAIGA_HILLS, Biomes.FOREST_HILLS, Biomes.JUNGLE, Biomes.JUNGLE_HILLS}
   );

   protected BiomeProvider() {
   }

   private BiomeProvider(long var1, WorldType var3, String var4) {
      this();
      if (☃ == WorldType.CUSTOMIZED && !☃.isEmpty()) {
         this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(☃).build();
      }

      GenLayer[] ☃ = GenLayer.initializeAllBiomeGenerators(☃, ☃, this.settings);
      this.genBiomes = ☃[0];
      this.biomeIndexLayer = ☃[1];
   }

   public BiomeProvider(WorldInfo var1) {
      this(☃.getSeed(), ☃.getTerrainType(), ☃.getGeneratorOptions());
   }

   public List<Biome> getBiomesToSpawnIn() {
      return this.biomesToSpawnIn;
   }

   public Biome getBiome(BlockPos var1) {
      return this.getBiome(☃, null);
   }

   public Biome getBiome(BlockPos var1, Biome var2) {
      return this.biomeCache.getBiome(☃.getX(), ☃.getZ(), ☃);
   }

   public float getTemperatureAtHeight(float var1, int var2) {
      return ☃;
   }

   public Biome[] getBiomesForGeneration(Biome[] var1, int var2, int var3, int var4, int var5) {
      IntCache.resetIntCache();
      if (☃ == null || ☃.length < ☃ * ☃) {
         ☃ = new Biome[☃ * ☃];
      }

      int[] ☃ = this.genBiomes.getInts(☃, ☃, ☃, ☃);

      try {
         for (int ☃x = 0; ☃x < ☃ * ☃; ☃x++) {
            ☃[☃x] = Biome.getBiome(☃[☃x], Biomes.DEFAULT);
         }

         return ☃;
      } catch (Throwable var10) {
         CrashReport ☃x = CrashReport.makeCrashReport(var10, "Invalid Biome id");
         CrashReportCategory ☃xx = ☃x.makeCategory("RawBiomeBlock");
         ☃xx.addCrashSection("biomes[] size", ☃.length);
         ☃xx.addCrashSection("x", ☃);
         ☃xx.addCrashSection("z", ☃);
         ☃xx.addCrashSection("w", ☃);
         ☃xx.addCrashSection("h", ☃);
         throw new ReportedException(☃x);
      }
   }

   public Biome[] getBiomes(@Nullable Biome[] var1, int var2, int var3, int var4, int var5) {
      return this.getBiomes(☃, ☃, ☃, ☃, ☃, true);
   }

   public Biome[] getBiomes(@Nullable Biome[] var1, int var2, int var3, int var4, int var5, boolean var6) {
      IntCache.resetIntCache();
      if (☃ == null || ☃.length < ☃ * ☃) {
         ☃ = new Biome[☃ * ☃];
      }

      if (☃ && ☃ == 16 && ☃ == 16 && (☃ & 15) == 0 && (☃ & 15) == 0) {
         Biome[] ☃ = this.biomeCache.getCachedBiomes(☃, ☃);
         System.arraycopy(☃, 0, ☃, 0, ☃ * ☃);
         return ☃;
      } else {
         int[] ☃ = this.biomeIndexLayer.getInts(☃, ☃, ☃, ☃);

         for (int ☃x = 0; ☃x < ☃ * ☃; ☃x++) {
            ☃[☃x] = Biome.getBiome(☃[☃x], Biomes.DEFAULT);
         }

         return ☃;
      }
   }

   public boolean areBiomesViable(int var1, int var2, int var3, List<Biome> var4) {
      IntCache.resetIntCache();
      int ☃ = ☃ - ☃ >> 2;
      int ☃x = ☃ - ☃ >> 2;
      int ☃xx = ☃ + ☃ >> 2;
      int ☃xxx = ☃ + ☃ >> 2;
      int ☃xxxx = ☃xx - ☃ + 1;
      int ☃xxxxx = ☃xxx - ☃x + 1;
      int[] ☃xxxxxx = this.genBiomes.getInts(☃, ☃x, ☃xxxx, ☃xxxxx);

      try {
         for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃xxxx * ☃xxxxx; ☃xxxxxxx++) {
            Biome ☃xxxxxxxx = Biome.getBiome(☃xxxxxx[☃xxxxxxx]);
            if (!☃.contains(☃xxxxxxxx)) {
               return false;
            }
         }

         return true;
      } catch (Throwable var15) {
         CrashReport ☃xxxxxxxx = CrashReport.makeCrashReport(var15, "Invalid Biome id");
         CrashReportCategory ☃xxxxxxxxx = ☃xxxxxxxx.makeCategory("Layer");
         ☃xxxxxxxxx.addCrashSection("Layer", this.genBiomes.toString());
         ☃xxxxxxxxx.addCrashSection("x", ☃);
         ☃xxxxxxxxx.addCrashSection("z", ☃);
         ☃xxxxxxxxx.addCrashSection("radius", ☃);
         ☃xxxxxxxxx.addCrashSection("allowed", ☃);
         throw new ReportedException(☃xxxxxxxx);
      }
   }

   @Nullable
   public BlockPos findBiomePosition(int var1, int var2, int var3, List<Biome> var4, Random var5) {
      IntCache.resetIntCache();
      int ☃ = ☃ - ☃ >> 2;
      int ☃x = ☃ - ☃ >> 2;
      int ☃xx = ☃ + ☃ >> 2;
      int ☃xxx = ☃ + ☃ >> 2;
      int ☃xxxx = ☃xx - ☃ + 1;
      int ☃xxxxx = ☃xxx - ☃x + 1;
      int[] ☃xxxxxx = this.genBiomes.getInts(☃, ☃x, ☃xxxx, ☃xxxxx);
      BlockPos ☃xxxxxxx = null;
      int ☃xxxxxxxx = 0;

      for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < ☃xxxx * ☃xxxxx; ☃xxxxxxxxx++) {
         int ☃xxxxxxxxxx = ☃ + ☃xxxxxxxxx % ☃xxxx << 2;
         int ☃xxxxxxxxxxx = ☃x + ☃xxxxxxxxx / ☃xxxx << 2;
         Biome ☃xxxxxxxxxxxx = Biome.getBiome(☃xxxxxx[☃xxxxxxxxx]);
         if (☃.contains(☃xxxxxxxxxxxx) && (☃xxxxxxx == null || ☃.nextInt(☃xxxxxxxx + 1) == 0)) {
            ☃xxxxxxx = new BlockPos(☃xxxxxxxxxx, 0, ☃xxxxxxxxxxx);
            ☃xxxxxxxx++;
         }
      }

      return ☃xxxxxxx;
   }

   public void cleanupCache() {
      this.biomeCache.cleanupCache();
   }

   public boolean isFixedBiome() {
      return this.settings != null && this.settings.fixedBiome >= 0;
   }

   public Biome getFixedBiome() {
      return this.settings != null && this.settings.fixedBiome >= 0 ? Biome.getBiomeForId(this.settings.fixedBiome) : null;
   }
}
