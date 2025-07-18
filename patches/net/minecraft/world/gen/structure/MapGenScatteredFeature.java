package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class MapGenScatteredFeature extends MapGenStructure {
   private static final List<Biome> BIOMELIST = Arrays.asList(
      Biomes.DESERT, Biomes.DESERT_HILLS, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.SWAMPLAND, Biomes.ICE_PLAINS, Biomes.COLD_TAIGA
   );
   private final List<Biome.SpawnListEntry> monsters = Lists.newArrayList();
   private int maxDistanceBetweenScatteredFeatures = 32;
   private final int minDistanceBetweenScatteredFeatures = 8;

   public MapGenScatteredFeature() {
      this.monsters.add(new Biome.SpawnListEntry(EntityWitch.class, 1, 1, 1));
   }

   public MapGenScatteredFeature(Map<String, String> var1) {
      this();

      for (Entry<String, String> ☃ : ☃.entrySet()) {
         if (☃.getKey().equals("distance")) {
            this.maxDistanceBetweenScatteredFeatures = MathHelper.getInt(☃.getValue(), this.maxDistanceBetweenScatteredFeatures, 9);
         }
      }
   }

   @Override
   public String getStructureName() {
      return "Temple";
   }

   @Override
   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      int ☃ = ☃;
      int ☃x = ☃;
      if (☃ < 0) {
         ☃ -= this.maxDistanceBetweenScatteredFeatures - 1;
      }

      if (☃ < 0) {
         ☃ -= this.maxDistanceBetweenScatteredFeatures - 1;
      }

      int ☃xx = ☃ / this.maxDistanceBetweenScatteredFeatures;
      int ☃xxx = ☃ / this.maxDistanceBetweenScatteredFeatures;
      Random ☃xxxx = this.world.setRandomSeed(☃xx, ☃xxx, 14357617);
      ☃xx *= this.maxDistanceBetweenScatteredFeatures;
      ☃xxx *= this.maxDistanceBetweenScatteredFeatures;
      ☃xx += ☃xxxx.nextInt(this.maxDistanceBetweenScatteredFeatures - 8);
      ☃xxx += ☃xxxx.nextInt(this.maxDistanceBetweenScatteredFeatures - 8);
      if (☃ == ☃xx && ☃x == ☃xxx) {
         Biome ☃xxxxx = this.world.getBiomeProvider().getBiome(new BlockPos(☃ * 16 + 8, 0, ☃x * 16 + 8));
         if (☃xxxxx == null) {
            return false;
         }

         for (Biome ☃xxxxxx : BIOMELIST) {
            if (☃xxxxx == ☃xxxxxx) {
               return true;
            }
         }
      }

      return false;
   }

   @Override
   public BlockPos getNearestStructurePos(World var1, BlockPos var2, boolean var3) {
      this.world = ☃;
      return findNearestStructurePosBySpacing(☃, this, ☃, this.maxDistanceBetweenScatteredFeatures, 8, 14357617, false, 100, ☃);
   }

   @Override
   protected StructureStart getStructureStart(int var1, int var2) {
      return new MapGenScatteredFeature.Start(this.world, this.rand, ☃, ☃);
   }

   public boolean isSwampHut(BlockPos var1) {
      StructureStart ☃ = this.getStructureAt(☃);
      if (☃ != null && ☃ instanceof MapGenScatteredFeature.Start && !☃.components.isEmpty()) {
         StructureComponent ☃x = ☃.components.get(0);
         return ☃x instanceof ComponentScatteredFeaturePieces.SwampHut;
      } else {
         return false;
      }
   }

   public List<Biome.SpawnListEntry> getMonsters() {
      return this.monsters;
   }

   public static class Start extends StructureStart {
      public Start() {
      }

      public Start(World var1, Random var2, int var3, int var4) {
         this(☃, ☃, ☃, ☃, ☃.getBiome(new BlockPos(☃ * 16 + 8, 0, ☃ * 16 + 8)));
      }

      public Start(World var1, Random var2, int var3, int var4, Biome var5) {
         super(☃, ☃);
         if (☃ == Biomes.JUNGLE || ☃ == Biomes.JUNGLE_HILLS) {
            ComponentScatteredFeaturePieces.JunglePyramid ☃ = new ComponentScatteredFeaturePieces.JunglePyramid(☃, ☃ * 16, ☃ * 16);
            this.components.add(☃);
         } else if (☃ == Biomes.SWAMPLAND) {
            ComponentScatteredFeaturePieces.SwampHut ☃ = new ComponentScatteredFeaturePieces.SwampHut(☃, ☃ * 16, ☃ * 16);
            this.components.add(☃);
         } else if (☃ == Biomes.DESERT || ☃ == Biomes.DESERT_HILLS) {
            ComponentScatteredFeaturePieces.DesertPyramid ☃ = new ComponentScatteredFeaturePieces.DesertPyramid(☃, ☃ * 16, ☃ * 16);
            this.components.add(☃);
         } else if (☃ == Biomes.ICE_PLAINS || ☃ == Biomes.COLD_TAIGA) {
            ComponentScatteredFeaturePieces.Igloo ☃ = new ComponentScatteredFeaturePieces.Igloo(☃, ☃ * 16, ☃ * 16);
            this.components.add(☃);
         }

         this.updateBoundingBox();
      }
   }
}
