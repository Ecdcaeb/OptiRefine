package net.minecraft.world.gen.structure;

import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMesa;

public class MapGenMineshaft extends MapGenStructure {
   private double chance = 0.004;

   public MapGenMineshaft() {
   }

   @Override
   public String getStructureName() {
      return "Mineshaft";
   }

   public MapGenMineshaft(Map<String, String> var1) {
      for (Entry<String, String> ☃ : ☃.entrySet()) {
         if (☃.getKey().equals("chance")) {
            this.chance = MathHelper.getDouble(☃.getValue(), this.chance);
         }
      }
   }

   @Override
   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      return this.rand.nextDouble() < this.chance && this.rand.nextInt(80) < Math.max(Math.abs(☃), Math.abs(☃));
   }

   @Override
   public BlockPos getNearestStructurePos(World var1, BlockPos var2, boolean var3) {
      int ☃ = 1000;
      int ☃x = ☃.getX() >> 4;
      int ☃xx = ☃.getZ() >> 4;

      for (int ☃xxx = 0; ☃xxx <= 1000; ☃xxx++) {
         for (int ☃xxxx = -☃xxx; ☃xxxx <= ☃xxx; ☃xxxx++) {
            boolean ☃xxxxx = ☃xxxx == -☃xxx || ☃xxxx == ☃xxx;

            for (int ☃xxxxxx = -☃xxx; ☃xxxxxx <= ☃xxx; ☃xxxxxx++) {
               boolean ☃xxxxxxx = ☃xxxxxx == -☃xxx || ☃xxxxxx == ☃xxx;
               if (☃xxxxx || ☃xxxxxxx) {
                  int ☃xxxxxxxx = ☃x + ☃xxxx;
                  int ☃xxxxxxxxx = ☃xx + ☃xxxxxx;
                  this.rand.setSeed(☃xxxxxxxx ^ ☃xxxxxxxxx ^ ☃.getSeed());
                  this.rand.nextInt();
                  if (this.canSpawnStructureAtCoords(☃xxxxxxxx, ☃xxxxxxxxx) && (!☃ || !☃.isChunkGeneratedAt(☃xxxxxxxx, ☃xxxxxxxxx))) {
                     return new BlockPos((☃xxxxxxxx << 4) + 8, 64, (☃xxxxxxxxx << 4) + 8);
                  }
               }
            }
         }
      }

      return null;
   }

   @Override
   protected StructureStart getStructureStart(int var1, int var2) {
      Biome ☃ = this.world.getBiome(new BlockPos((☃ << 4) + 8, 64, (☃ << 4) + 8));
      MapGenMineshaft.Type ☃x = ☃ instanceof BiomeMesa ? MapGenMineshaft.Type.MESA : MapGenMineshaft.Type.NORMAL;
      return new StructureMineshaftStart(this.world, this.rand, ☃, ☃, ☃x);
   }

   public static enum Type {
      NORMAL,
      MESA;

      public static MapGenMineshaft.Type byId(int var0) {
         return ☃ >= 0 && ☃ < values().length ? values()[☃] : NORMAL;
      }
   }
}
