package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;

public class BiomeProviderSingle extends BiomeProvider {
   private final Biome biome;

   public BiomeProviderSingle(Biome var1) {
      this.biome = ☃;
   }

   @Override
   public Biome getBiome(BlockPos var1) {
      return this.biome;
   }

   @Override
   public Biome[] getBiomesForGeneration(Biome[] var1, int var2, int var3, int var4, int var5) {
      if (☃ == null || ☃.length < ☃ * ☃) {
         ☃ = new Biome[☃ * ☃];
      }

      Arrays.fill(☃, 0, ☃ * ☃, this.biome);
      return ☃;
   }

   @Override
   public Biome[] getBiomes(@Nullable Biome[] var1, int var2, int var3, int var4, int var5) {
      if (☃ == null || ☃.length < ☃ * ☃) {
         ☃ = new Biome[☃ * ☃];
      }

      Arrays.fill(☃, 0, ☃ * ☃, this.biome);
      return ☃;
   }

   @Override
   public Biome[] getBiomes(@Nullable Biome[] var1, int var2, int var3, int var4, int var5, boolean var6) {
      return this.getBiomes(☃, ☃, ☃, ☃, ☃);
   }

   @Nullable
   @Override
   public BlockPos findBiomePosition(int var1, int var2, int var3, List<Biome> var4, Random var5) {
      return ☃.contains(this.biome) ? new BlockPos(☃ - ☃ + ☃.nextInt(☃ * 2 + 1), 0, ☃ - ☃ + ☃.nextInt(☃ * 2 + 1)) : null;
   }

   @Override
   public boolean areBiomesViable(int var1, int var2, int var3, List<Biome> var4) {
      return ☃.contains(this.biome);
   }

   @Override
   public boolean isFixedBiome() {
      return true;
   }

   @Override
   public Biome getFixedBiome() {
      return this.biome;
   }
}
