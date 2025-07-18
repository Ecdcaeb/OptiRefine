package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenBase {
   protected int range = 8;
   protected Random rand = new Random();
   protected World world;

   public void generate(World var1, int var2, int var3, ChunkPrimer var4) {
      int ☃ = this.range;
      this.world = ☃;
      this.rand.setSeed(☃.getSeed());
      long ☃x = this.rand.nextLong();
      long ☃xx = this.rand.nextLong();

      for (int ☃xxx = ☃ - ☃; ☃xxx <= ☃ + ☃; ☃xxx++) {
         for (int ☃xxxx = ☃ - ☃; ☃xxxx <= ☃ + ☃; ☃xxxx++) {
            long ☃xxxxx = ☃xxx * ☃x;
            long ☃xxxxxx = ☃xxxx * ☃xx;
            this.rand.setSeed(☃xxxxx ^ ☃xxxxxx ^ ☃.getSeed());
            this.recursiveGenerate(☃, ☃xxx, ☃xxxx, ☃, ☃, ☃);
         }
      }
   }

   public static void setupChunkSeed(long var0, Random var2, int var3, int var4) {
      ☃.setSeed(☃);
      long ☃ = ☃.nextLong();
      long ☃x = ☃.nextLong();
      long ☃xx = ☃ * ☃;
      long ☃xxx = ☃ * ☃x;
      ☃.setSeed(☃xx ^ ☃xxx ^ ☃);
   }

   protected void recursiveGenerate(World var1, int var2, int var3, int var4, int var5, ChunkPrimer var6) {
   }
}
