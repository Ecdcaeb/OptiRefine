package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class StructureOceanMonument extends MapGenStructure {
   private int spacing = 32;
   private int separation = 5;
   public static final List<Biome> WATER_BIOMES = Arrays.asList(Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.RIVER, Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER);
   public static final List<Biome> SPAWN_BIOMES = Arrays.asList(Biomes.DEEP_OCEAN);
   private static final List<Biome.SpawnListEntry> MONUMENT_ENEMIES = Lists.newArrayList();

   public StructureOceanMonument() {
   }

   public StructureOceanMonument(Map<String, String> var1) {
      this();

      for (Entry<String, String> ☃ : ☃.entrySet()) {
         if (☃.getKey().equals("spacing")) {
            this.spacing = MathHelper.getInt(☃.getValue(), this.spacing, 1);
         } else if (☃.getKey().equals("separation")) {
            this.separation = MathHelper.getInt(☃.getValue(), this.separation, 1);
         }
      }
   }

   @Override
   public String getStructureName() {
      return "Monument";
   }

   @Override
   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      int ☃ = ☃;
      int ☃x = ☃;
      if (☃ < 0) {
         ☃ -= this.spacing - 1;
      }

      if (☃ < 0) {
         ☃ -= this.spacing - 1;
      }

      int ☃xx = ☃ / this.spacing;
      int ☃xxx = ☃ / this.spacing;
      Random ☃xxxx = this.world.setRandomSeed(☃xx, ☃xxx, 10387313);
      ☃xx *= this.spacing;
      ☃xxx *= this.spacing;
      ☃xx += (☃xxxx.nextInt(this.spacing - this.separation) + ☃xxxx.nextInt(this.spacing - this.separation)) / 2;
      ☃xxx += (☃xxxx.nextInt(this.spacing - this.separation) + ☃xxxx.nextInt(this.spacing - this.separation)) / 2;
      if (☃ == ☃xx && ☃x == ☃xxx) {
         if (!this.world.getBiomeProvider().areBiomesViable(☃ * 16 + 8, ☃x * 16 + 8, 16, SPAWN_BIOMES)) {
            return false;
         }

         boolean ☃xxxxx = this.world.getBiomeProvider().areBiomesViable(☃ * 16 + 8, ☃x * 16 + 8, 29, WATER_BIOMES);
         if (☃xxxxx) {
            return true;
         }
      }

      return false;
   }

   @Override
   public BlockPos getNearestStructurePos(World var1, BlockPos var2, boolean var3) {
      this.world = ☃;
      return findNearestStructurePosBySpacing(☃, this, ☃, this.spacing, this.separation, 10387313, true, 100, ☃);
   }

   @Override
   protected StructureStart getStructureStart(int var1, int var2) {
      return new StructureOceanMonument.StartMonument(this.world, this.rand, ☃, ☃);
   }

   public List<Biome.SpawnListEntry> getMonsters() {
      return MONUMENT_ENEMIES;
   }

   static {
      MONUMENT_ENEMIES.add(new Biome.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
   }

   public static class StartMonument extends StructureStart {
      private final Set<ChunkPos> processed = Sets.newHashSet();
      private boolean wasCreated;

      public StartMonument() {
      }

      public StartMonument(World var1, Random var2, int var3, int var4) {
         super(☃, ☃);
         this.create(☃, ☃, ☃, ☃);
      }

      private void create(World var1, Random var2, int var3, int var4) {
         ☃.setSeed(☃.getSeed());
         long ☃ = ☃.nextLong();
         long ☃x = ☃.nextLong();
         long ☃xx = ☃ * ☃;
         long ☃xxx = ☃ * ☃x;
         ☃.setSeed(☃xx ^ ☃xxx ^ ☃.getSeed());
         int ☃xxxx = ☃ * 16 + 8 - 29;
         int ☃xxxxx = ☃ * 16 + 8 - 29;
         EnumFacing ☃xxxxxx = EnumFacing.Plane.HORIZONTAL.random(☃);
         this.components.add(new StructureOceanMonumentPieces.MonumentBuilding(☃, ☃xxxx, ☃xxxxx, ☃xxxxxx));
         this.updateBoundingBox();
         this.wasCreated = true;
      }

      @Override
      public void generateStructure(World var1, Random var2, StructureBoundingBox var3) {
         if (!this.wasCreated) {
            this.components.clear();
            this.create(☃, ☃, this.getChunkPosX(), this.getChunkPosZ());
         }

         super.generateStructure(☃, ☃, ☃);
      }

      @Override
      public boolean isValidForPostProcess(ChunkPos var1) {
         return this.processed.contains(☃) ? false : super.isValidForPostProcess(☃);
      }

      @Override
      public void notifyPostProcessAt(ChunkPos var1) {
         super.notifyPostProcessAt(☃);
         this.processed.add(☃);
      }

      @Override
      public void writeToNBT(NBTTagCompound var1) {
         super.writeToNBT(☃);
         NBTTagList ☃ = new NBTTagList();

         for (ChunkPos ☃x : this.processed) {
            NBTTagCompound ☃xx = new NBTTagCompound();
            ☃xx.setInteger("X", ☃x.x);
            ☃xx.setInteger("Z", ☃x.z);
            ☃.appendTag(☃xx);
         }

         ☃.setTag("Processed", ☃);
      }

      @Override
      public void readFromNBT(NBTTagCompound var1) {
         super.readFromNBT(☃);
         if (☃.hasKey("Processed", 9)) {
            NBTTagList ☃ = ☃.getTagList("Processed", 10);

            for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
               NBTTagCompound ☃xx = ☃.getCompoundTagAt(☃x);
               this.processed.add(new ChunkPos(☃xx.getInteger("X"), ☃xx.getInteger("Z")));
            }
         }
      }
   }
}
