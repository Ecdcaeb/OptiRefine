package net.minecraft.world.gen.structure;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class MapGenVillage extends MapGenStructure {
   public static final List<Biome> VILLAGE_SPAWN_BIOMES = Arrays.asList(Biomes.PLAINS, Biomes.DESERT, Biomes.SAVANNA, Biomes.TAIGA);
   private int size;
   private int distance = 32;
   private final int minTownSeparation = 8;

   public MapGenVillage() {
   }

   public MapGenVillage(Map<String, String> var1) {
      this();

      for (Entry<String, String> ☃ : ☃.entrySet()) {
         if (☃.getKey().equals("size")) {
            this.size = MathHelper.getInt(☃.getValue(), this.size, 0);
         } else if (☃.getKey().equals("distance")) {
            this.distance = MathHelper.getInt(☃.getValue(), this.distance, 9);
         }
      }
   }

   @Override
   public String getStructureName() {
      return "Village";
   }

   @Override
   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      int ☃ = ☃;
      int ☃x = ☃;
      if (☃ < 0) {
         ☃ -= this.distance - 1;
      }

      if (☃ < 0) {
         ☃ -= this.distance - 1;
      }

      int ☃xx = ☃ / this.distance;
      int ☃xxx = ☃ / this.distance;
      Random ☃xxxx = this.world.setRandomSeed(☃xx, ☃xxx, 10387312);
      ☃xx *= this.distance;
      ☃xxx *= this.distance;
      ☃xx += ☃xxxx.nextInt(this.distance - 8);
      ☃xxx += ☃xxxx.nextInt(this.distance - 8);
      if (☃ == ☃xx && ☃x == ☃xxx) {
         boolean ☃xxxxx = this.world.getBiomeProvider().areBiomesViable(☃ * 16 + 8, ☃x * 16 + 8, 0, VILLAGE_SPAWN_BIOMES);
         if (☃xxxxx) {
            return true;
         }
      }

      return false;
   }

   @Override
   public BlockPos getNearestStructurePos(World var1, BlockPos var2, boolean var3) {
      this.world = ☃;
      return findNearestStructurePosBySpacing(☃, this, ☃, this.distance, 8, 10387312, false, 100, ☃);
   }

   @Override
   protected StructureStart getStructureStart(int var1, int var2) {
      return new MapGenVillage.Start(this.world, this.rand, ☃, ☃, this.size);
   }

   public static class Start extends StructureStart {
      private boolean hasMoreThanTwoComponents;

      public Start() {
      }

      public Start(World var1, Random var2, int var3, int var4, int var5) {
         super(☃, ☃);
         List<StructureVillagePieces.PieceWeight> ☃ = StructureVillagePieces.getStructureVillageWeightedPieceList(☃, ☃);
         StructureVillagePieces.Start ☃x = new StructureVillagePieces.Start(☃.getBiomeProvider(), 0, ☃, (☃ << 4) + 2, (☃ << 4) + 2, ☃, ☃);
         this.components.add(☃x);
         ☃x.buildComponent(☃x, this.components, ☃);
         List<StructureComponent> ☃xx = ☃x.pendingRoads;
         List<StructureComponent> ☃xxx = ☃x.pendingHouses;

         while (!☃xx.isEmpty() || !☃xxx.isEmpty()) {
            if (☃xx.isEmpty()) {
               int ☃xxxx = ☃.nextInt(☃xxx.size());
               StructureComponent ☃xxxxx = ☃xxx.remove(☃xxxx);
               ☃xxxxx.buildComponent(☃x, this.components, ☃);
            } else {
               int ☃xxxx = ☃.nextInt(☃xx.size());
               StructureComponent ☃xxxxx = ☃xx.remove(☃xxxx);
               ☃xxxxx.buildComponent(☃x, this.components, ☃);
            }
         }

         this.updateBoundingBox();
         int ☃xxxx = 0;

         for (StructureComponent ☃xxxxx : this.components) {
            if (!(☃xxxxx instanceof StructureVillagePieces.Road)) {
               ☃xxxx++;
            }
         }

         this.hasMoreThanTwoComponents = ☃xxxx > 2;
      }

      @Override
      public boolean isSizeableStructure() {
         return this.hasMoreThanTwoComponents;
      }

      @Override
      public void writeToNBT(NBTTagCompound var1) {
         super.writeToNBT(☃);
         ☃.setBoolean("Valid", this.hasMoreThanTwoComponents);
      }

      @Override
      public void readFromNBT(NBTTagCompound var1) {
         super.readFromNBT(☃);
         this.hasMoreThanTwoComponents = ☃.getBoolean("Valid");
      }
   }
}
