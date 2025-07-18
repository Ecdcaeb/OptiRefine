package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorEnd;

public class MapGenEndCity extends MapGenStructure {
   private final int citySpacing = 20;
   private final int minCitySeparation = 11;
   private final ChunkGeneratorEnd endProvider;

   public MapGenEndCity(ChunkGeneratorEnd var1) {
      this.endProvider = ☃;
   }

   @Override
   public String getStructureName() {
      return "EndCity";
   }

   @Override
   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      int ☃ = ☃;
      int ☃x = ☃;
      if (☃ < 0) {
         ☃ -= 19;
      }

      if (☃ < 0) {
         ☃ -= 19;
      }

      int ☃xx = ☃ / 20;
      int ☃xxx = ☃ / 20;
      Random ☃xxxx = this.world.setRandomSeed(☃xx, ☃xxx, 10387313);
      ☃xx *= 20;
      ☃xxx *= 20;
      ☃xx += (☃xxxx.nextInt(9) + ☃xxxx.nextInt(9)) / 2;
      ☃xxx += (☃xxxx.nextInt(9) + ☃xxxx.nextInt(9)) / 2;
      if (☃ == ☃xx && ☃x == ☃xxx && this.endProvider.isIslandChunk(☃, ☃x)) {
         int ☃xxxxx = getYPosForStructure(☃, ☃x, this.endProvider);
         return ☃xxxxx >= 60;
      } else {
         return false;
      }
   }

   @Override
   protected StructureStart getStructureStart(int var1, int var2) {
      return new MapGenEndCity.Start(this.world, this.endProvider, this.rand, ☃, ☃);
   }

   @Override
   public BlockPos getNearestStructurePos(World var1, BlockPos var2, boolean var3) {
      this.world = ☃;
      return findNearestStructurePosBySpacing(☃, this, ☃, 20, 11, 10387313, true, 100, ☃);
   }

   private static int getYPosForStructure(int var0, int var1, ChunkGeneratorEnd var2) {
      Random ☃ = new Random(☃ + ☃ * 10387313);
      Rotation ☃x = Rotation.values()[☃.nextInt(Rotation.values().length)];
      ChunkPrimer ☃xx = new ChunkPrimer();
      ☃.setBlocksInChunk(☃, ☃, ☃xx);
      int ☃xxx = 5;
      int ☃xxxx = 5;
      if (☃x == Rotation.CLOCKWISE_90) {
         ☃xxx = -5;
      } else if (☃x == Rotation.CLOCKWISE_180) {
         ☃xxx = -5;
         ☃xxxx = -5;
      } else if (☃x == Rotation.COUNTERCLOCKWISE_90) {
         ☃xxxx = -5;
      }

      int ☃xxxxx = ☃xx.findGroundBlockIdx(7, 7);
      int ☃xxxxxx = ☃xx.findGroundBlockIdx(7, 7 + ☃xxxx);
      int ☃xxxxxxx = ☃xx.findGroundBlockIdx(7 + ☃xxx, 7);
      int ☃xxxxxxxx = ☃xx.findGroundBlockIdx(7 + ☃xxx, 7 + ☃xxxx);
      return Math.min(Math.min(☃xxxxx, ☃xxxxxx), Math.min(☃xxxxxxx, ☃xxxxxxxx));
   }

   public static class Start extends StructureStart {
      private boolean isSizeable;

      public Start() {
      }

      public Start(World var1, ChunkGeneratorEnd var2, Random var3, int var4, int var5) {
         super(☃, ☃);
         this.create(☃, ☃, ☃, ☃, ☃);
      }

      private void create(World var1, ChunkGeneratorEnd var2, Random var3, int var4, int var5) {
         Random ☃ = new Random(☃ + ☃ * 10387313);
         Rotation ☃x = Rotation.values()[☃.nextInt(Rotation.values().length)];
         int ☃xx = MapGenEndCity.getYPosForStructure(☃, ☃, ☃);
         if (☃xx < 60) {
            this.isSizeable = false;
         } else {
            BlockPos ☃xxx = new BlockPos(☃ * 16 + 8, ☃xx, ☃ * 16 + 8);
            StructureEndCityPieces.startHouseTower(☃.getSaveHandler().getStructureTemplateManager(), ☃xxx, ☃x, this.components, ☃);
            this.updateBoundingBox();
            this.isSizeable = true;
         }
      }

      @Override
      public boolean isSizeableStructure() {
         return this.isSizeable;
      }
   }
}
