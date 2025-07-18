package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorOverworld;

public class WoodlandMansion extends MapGenStructure {
   private final int featureSpacing = 80;
   private final int minFeatureSeparation = 20;
   public static final List<Biome> ALLOWED_BIOMES = Arrays.asList(Biomes.ROOFED_FOREST, Biomes.MUTATED_ROOFED_FOREST);
   private final ChunkGeneratorOverworld provider;

   public WoodlandMansion(ChunkGeneratorOverworld var1) {
      this.provider = ☃;
   }

   @Override
   public String getStructureName() {
      return "Mansion";
   }

   @Override
   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      int ☃ = ☃;
      int ☃x = ☃;
      if (☃ < 0) {
         ☃ = ☃ - 79;
      }

      if (☃ < 0) {
         ☃x = ☃ - 79;
      }

      int ☃xx = ☃ / 80;
      int ☃xxx = ☃x / 80;
      Random ☃xxxx = this.world.setRandomSeed(☃xx, ☃xxx, 10387319);
      ☃xx *= 80;
      ☃xxx *= 80;
      ☃xx += (☃xxxx.nextInt(60) + ☃xxxx.nextInt(60)) / 2;
      ☃xxx += (☃xxxx.nextInt(60) + ☃xxxx.nextInt(60)) / 2;
      if (☃ == ☃xx && ☃ == ☃xxx) {
         boolean ☃xxxxx = this.world.getBiomeProvider().areBiomesViable(☃ * 16 + 8, ☃ * 16 + 8, 32, ALLOWED_BIOMES);
         if (☃xxxxx) {
            return true;
         }
      }

      return false;
   }

   @Override
   public BlockPos getNearestStructurePos(World var1, BlockPos var2, boolean var3) {
      this.world = ☃;
      BiomeProvider ☃ = ☃.getBiomeProvider();
      return ☃.isFixedBiome() && ☃.getFixedBiome() != Biomes.ROOFED_FOREST
         ? null
         : findNearestStructurePosBySpacing(☃, this, ☃, 80, 20, 10387319, true, 100, ☃);
   }

   @Override
   protected StructureStart getStructureStart(int var1, int var2) {
      return new WoodlandMansion.Start(this.world, this.provider, this.rand, ☃, ☃);
   }

   public static class Start extends StructureStart {
      private boolean isValid;

      public Start() {
      }

      public Start(World var1, ChunkGeneratorOverworld var2, Random var3, int var4, int var5) {
         super(☃, ☃);
         this.create(☃, ☃, ☃, ☃, ☃);
      }

      private void create(World var1, ChunkGeneratorOverworld var2, Random var3, int var4, int var5) {
         Rotation ☃ = Rotation.values()[☃.nextInt(Rotation.values().length)];
         ChunkPrimer ☃x = new ChunkPrimer();
         ☃.setBlocksInChunk(☃, ☃, ☃x);
         int ☃xx = 5;
         int ☃xxx = 5;
         if (☃ == Rotation.CLOCKWISE_90) {
            ☃xx = -5;
         } else if (☃ == Rotation.CLOCKWISE_180) {
            ☃xx = -5;
            ☃xxx = -5;
         } else if (☃ == Rotation.COUNTERCLOCKWISE_90) {
            ☃xxx = -5;
         }

         int ☃xxxx = ☃x.findGroundBlockIdx(7, 7);
         int ☃xxxxx = ☃x.findGroundBlockIdx(7, 7 + ☃xxx);
         int ☃xxxxxx = ☃x.findGroundBlockIdx(7 + ☃xx, 7);
         int ☃xxxxxxx = ☃x.findGroundBlockIdx(7 + ☃xx, 7 + ☃xxx);
         int ☃xxxxxxxx = Math.min(Math.min(☃xxxx, ☃xxxxx), Math.min(☃xxxxxx, ☃xxxxxxx));
         if (☃xxxxxxxx < 60) {
            this.isValid = false;
         } else {
            BlockPos ☃xxxxxxxxx = new BlockPos(☃ * 16 + 8, ☃xxxxxxxx + 1, ☃ * 16 + 8);
            List<WoodlandMansionPieces.MansionTemplate> ☃xxxxxxxxxx = Lists.newLinkedList();
            WoodlandMansionPieces.generateMansion(☃.getSaveHandler().getStructureTemplateManager(), ☃xxxxxxxxx, ☃, ☃xxxxxxxxxx, ☃);
            this.components.addAll(☃xxxxxxxxxx);
            this.updateBoundingBox();
            this.isValid = true;
         }
      }

      @Override
      public void generateStructure(World var1, Random var2, StructureBoundingBox var3) {
         super.generateStructure(☃, ☃, ☃);
         int ☃ = this.boundingBox.minY;

         for (int ☃x = ☃.minX; ☃x <= ☃.maxX; ☃x++) {
            for (int ☃xx = ☃.minZ; ☃xx <= ☃.maxZ; ☃xx++) {
               BlockPos ☃xxx = new BlockPos(☃x, ☃, ☃xx);
               if (!☃.isAirBlock(☃xxx) && this.boundingBox.isVecInside(☃xxx)) {
                  boolean ☃xxxx = false;

                  for (StructureComponent ☃xxxxx : this.components) {
                     if (☃xxxxx.boundingBox.isVecInside(☃xxx)) {
                        ☃xxxx = true;
                        break;
                     }
                  }

                  if (☃xxxx) {
                     for (int ☃xxxxxx = ☃ - 1; ☃xxxxxx > 1; ☃xxxxxx--) {
                        BlockPos ☃xxxxxxx = new BlockPos(☃x, ☃xxxxxx, ☃xx);
                        if (!☃.isAirBlock(☃xxxxxxx) && !☃.getBlockState(☃xxxxxxx).getMaterial().isLiquid()) {
                           break;
                        }

                        ☃.setBlockState(☃xxxxxxx, Blocks.COBBLESTONE.getDefaultState(), 2);
                     }
                  }
               }
            }
         }
      }

      @Override
      public boolean isSizeableStructure() {
         return this.isValid;
      }
   }
}
