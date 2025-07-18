package net.minecraft.world.biome;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenSpikes;

public class BiomeEndDecorator extends BiomeDecorator {
   private static final LoadingCache<Long, WorldGenSpikes.EndSpike[]> SPIKE_CACHE = CacheBuilder.newBuilder()
      .expireAfterWrite(5L, TimeUnit.MINUTES)
      .build(new BiomeEndDecorator.SpikeCacheLoader());
   private final WorldGenSpikes spikeGen = new WorldGenSpikes();

   @Override
   protected void genDecorations(Biome var1, World var2, Random var3) {
      this.generateOres(☃, ☃);
      WorldGenSpikes.EndSpike[] ☃ = getSpikesForWorld(☃);

      for (WorldGenSpikes.EndSpike ☃x : ☃) {
         if (☃x.doesStartInChunk(this.chunkPos)) {
            this.spikeGen.setSpike(☃x);
            this.spikeGen.generate(☃, ☃, new BlockPos(☃x.getCenterX(), 45, ☃x.getCenterZ()));
         }
      }
   }

   public static WorldGenSpikes.EndSpike[] getSpikesForWorld(World var0) {
      Random ☃ = new Random(☃.getSeed());
      long ☃x = ☃.nextLong() & 65535L;
      return (WorldGenSpikes.EndSpike[])SPIKE_CACHE.getUnchecked(☃x);
   }

   static class SpikeCacheLoader extends CacheLoader<Long, WorldGenSpikes.EndSpike[]> {
      private SpikeCacheLoader() {
      }

      public WorldGenSpikes.EndSpike[] load(Long var1) throws Exception {
         List<Integer> ☃ = Lists.newArrayList(ContiguousSet.create(Range.closedOpen(0, 10), DiscreteDomain.integers()));
         Collections.shuffle(☃, new Random(☃));
         WorldGenSpikes.EndSpike[] ☃x = new WorldGenSpikes.EndSpike[10];

         for (int ☃xx = 0; ☃xx < 10; ☃xx++) {
            int ☃xxx = (int)(42.0 * Math.cos(2.0 * (-Math.PI + (Math.PI / 10) * ☃xx)));
            int ☃xxxx = (int)(42.0 * Math.sin(2.0 * (-Math.PI + (Math.PI / 10) * ☃xx)));
            int ☃xxxxx = ☃.get(☃xx);
            int ☃xxxxxx = 2 + ☃xxxxx / 3;
            int ☃xxxxxxx = 76 + ☃xxxxx * 3;
            boolean ☃xxxxxxxx = ☃xxxxx == 1 || ☃xxxxx == 2;
            ☃x[☃xx] = new WorldGenSpikes.EndSpike(☃xxx, ☃xxxx, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx);
         }

         return ☃x;
      }
   }
}
