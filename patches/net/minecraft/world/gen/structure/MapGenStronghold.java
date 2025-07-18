package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class MapGenStronghold extends MapGenStructure {
   private final List<Biome> allowedBiomes;
   private boolean ranBiomeCheck;
   private ChunkPos[] structureCoords = new ChunkPos[128];
   private double distance = 32.0;
   private int spread = 3;

   public MapGenStronghold() {
      this.allowedBiomes = Lists.newArrayList();

      for (Biome ☃ : Biome.REGISTRY) {
         if (☃ != null && ☃.getBaseHeight() > 0.0F) {
            this.allowedBiomes.add(☃);
         }
      }
   }

   public MapGenStronghold(Map<String, String> var1) {
      this();

      for (Entry<String, String> ☃ : ☃.entrySet()) {
         if (☃.getKey().equals("distance")) {
            this.distance = MathHelper.getDouble(☃.getValue(), this.distance, 1.0);
         } else if (☃.getKey().equals("count")) {
            this.structureCoords = new ChunkPos[MathHelper.getInt(☃.getValue(), this.structureCoords.length, 1)];
         } else if (☃.getKey().equals("spread")) {
            this.spread = MathHelper.getInt(☃.getValue(), this.spread, 1);
         }
      }
   }

   @Override
   public String getStructureName() {
      return "Stronghold";
   }

   @Override
   public BlockPos getNearestStructurePos(World var1, BlockPos var2, boolean var3) {
      if (!this.ranBiomeCheck) {
         this.generatePositions();
         this.ranBiomeCheck = true;
      }

      BlockPos ☃ = null;
      BlockPos.MutableBlockPos ☃x = new BlockPos.MutableBlockPos(0, 0, 0);
      double ☃xx = Double.MAX_VALUE;

      for (ChunkPos ☃xxx : this.structureCoords) {
         ☃x.setPos((☃xxx.x << 4) + 8, 32, (☃xxx.z << 4) + 8);
         double ☃xxxx = ☃x.distanceSq(☃);
         if (☃ == null) {
            ☃ = new BlockPos(☃x);
            ☃xx = ☃xxxx;
         } else if (☃xxxx < ☃xx) {
            ☃ = new BlockPos(☃x);
            ☃xx = ☃xxxx;
         }
      }

      return ☃;
   }

   @Override
   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      if (!this.ranBiomeCheck) {
         this.generatePositions();
         this.ranBiomeCheck = true;
      }

      for (ChunkPos ☃ : this.structureCoords) {
         if (☃ == ☃.x && ☃ == ☃.z) {
            return true;
         }
      }

      return false;
   }

   private void generatePositions() {
      this.initializeStructureData(this.world);
      int ☃ = 0;
      ObjectIterator var2 = this.structureMap.values().iterator();

      while (var2.hasNext()) {
         StructureStart ☃x = (StructureStart)var2.next();
         if (☃ < this.structureCoords.length) {
            this.structureCoords[☃++] = new ChunkPos(☃x.getChunkPosX(), ☃x.getChunkPosZ());
         }
      }

      Random ☃x = new Random();
      ☃x.setSeed(this.world.getSeed());
      double ☃xx = ☃x.nextDouble() * Math.PI * 2.0;
      int ☃xxx = 0;
      int ☃xxxx = 0;
      int ☃xxxxx = this.structureMap.size();
      if (☃xxxxx < this.structureCoords.length) {
         for (int ☃xxxxxx = 0; ☃xxxxxx < this.structureCoords.length; ☃xxxxxx++) {
            double ☃xxxxxxx = 4.0 * this.distance + this.distance * ☃xxx * 6.0 + (☃x.nextDouble() - 0.5) * (this.distance * 2.5);
            int ☃xxxxxxxx = (int)Math.round(Math.cos(☃xx) * ☃xxxxxxx);
            int ☃xxxxxxxxx = (int)Math.round(Math.sin(☃xx) * ☃xxxxxxx);
            BlockPos ☃xxxxxxxxxx = this.world.getBiomeProvider().findBiomePosition((☃xxxxxxxx << 4) + 8, (☃xxxxxxxxx << 4) + 8, 112, this.allowedBiomes, ☃x);
            if (☃xxxxxxxxxx != null) {
               ☃xxxxxxxx = ☃xxxxxxxxxx.getX() >> 4;
               ☃xxxxxxxxx = ☃xxxxxxxxxx.getZ() >> 4;
            }

            if (☃xxxxxx >= ☃xxxxx) {
               this.structureCoords[☃xxxxxx] = new ChunkPos(☃xxxxxxxx, ☃xxxxxxxxx);
            }

            ☃xx += (Math.PI * 2) / this.spread;
            if (++☃xxxx == this.spread) {
               ☃xxx++;
               ☃xxxx = 0;
               this.spread = this.spread + 2 * this.spread / (☃xxx + 1);
               this.spread = Math.min(this.spread, this.structureCoords.length - ☃xxxxxx);
               ☃xx += ☃x.nextDouble() * Math.PI * 2.0;
            }
         }
      }
   }

   @Override
   protected StructureStart getStructureStart(int var1, int var2) {
      MapGenStronghold.Start ☃ = new MapGenStronghold.Start(this.world, this.rand, ☃, ☃);

      while (☃.getComponents().isEmpty() || ((StructureStrongholdPieces.Stairs2)☃.getComponents().get(0)).strongholdPortalRoom == null) {
         ☃ = new MapGenStronghold.Start(this.world, this.rand, ☃, ☃);
      }

      return ☃;
   }

   public static class Start extends StructureStart {
      public Start() {
      }

      public Start(World var1, Random var2, int var3, int var4) {
         super(☃, ☃);
         StructureStrongholdPieces.prepareStructurePieces();
         StructureStrongholdPieces.Stairs2 ☃ = new StructureStrongholdPieces.Stairs2(0, ☃, (☃ << 4) + 2, (☃ << 4) + 2);
         this.components.add(☃);
         ☃.buildComponent(☃, this.components, ☃);
         List<StructureComponent> ☃x = ☃.pendingChildren;

         while (!☃x.isEmpty()) {
            int ☃xx = ☃.nextInt(☃x.size());
            StructureComponent ☃xxx = ☃x.remove(☃xx);
            ☃xxx.buildComponent(☃, this.components, ☃);
         }

         this.updateBoundingBox();
         this.markAvailableHeight(☃, ☃, 10);
      }
   }
}
