package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public class BiomeCache {
   private final BiomeProvider provider;
   private long lastCleanupTime;
   private final Long2ObjectMap<BiomeCache.Block> cacheMap = new Long2ObjectOpenHashMap(4096);
   private final List<BiomeCache.Block> cache = Lists.newArrayList();

   public BiomeCache(BiomeProvider var1) {
      this.provider = ☃;
   }

   public BiomeCache.Block getEntry(int var1, int var2) {
      ☃ >>= 4;
      ☃ >>= 4;
      long ☃ = ☃ & 4294967295L | (☃ & 4294967295L) << 32;
      BiomeCache.Block ☃x = (BiomeCache.Block)this.cacheMap.get(☃);
      if (☃x == null) {
         ☃x = new BiomeCache.Block(☃, ☃);
         this.cacheMap.put(☃, ☃x);
         this.cache.add(☃x);
      }

      ☃x.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
      return ☃x;
   }

   public Biome getBiome(int var1, int var2, Biome var3) {
      Biome ☃ = this.getEntry(☃, ☃).getBiome(☃, ☃);
      return ☃ == null ? ☃ : ☃;
   }

   public void cleanupCache() {
      long ☃ = MinecraftServer.getCurrentTimeMillis();
      long ☃x = ☃ - this.lastCleanupTime;
      if (☃x > 7500L || ☃x < 0L) {
         this.lastCleanupTime = ☃;

         for (int ☃xx = 0; ☃xx < this.cache.size(); ☃xx++) {
            BiomeCache.Block ☃xxx = this.cache.get(☃xx);
            long ☃xxxx = ☃ - ☃xxx.lastAccessTime;
            if (☃xxxx > 30000L || ☃xxxx < 0L) {
               this.cache.remove(☃xx--);
               long ☃xxxxx = ☃xxx.x & 4294967295L | (☃xxx.z & 4294967295L) << 32;
               this.cacheMap.remove(☃xxxxx);
            }
         }
      }
   }

   public Biome[] getCachedBiomes(int var1, int var2) {
      return this.getEntry(☃, ☃).biomes;
   }

   public class Block {
      public Biome[] biomes = new Biome[256];
      public int x;
      public int z;
      public long lastAccessTime;

      public Block(int var2, int var3) {
         this.x = ☃;
         this.z = ☃;
         BiomeCache.this.provider.getBiomes(this.biomes, ☃ << 4, ☃ << 4, 16, 16, false);
      }

      public Biome getBiome(int var1, int var2) {
         return this.biomes[☃ & 15 | (☃ & 15) << 4];
      }
   }
}
