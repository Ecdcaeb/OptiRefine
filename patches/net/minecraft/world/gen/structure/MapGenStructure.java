package net.minecraft.world.gen.structure;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

public abstract class MapGenStructure extends MapGenBase {
   private MapGenStructureData structureData;
   protected Long2ObjectMap<StructureStart> structureMap = new Long2ObjectOpenHashMap(1024);

   public abstract String getStructureName();

   @Override
   protected final synchronized void recursiveGenerate(World var1, final int var2, final int var3, int var4, int var5, ChunkPrimer var6) {
      this.initializeStructureData(☃);
      if (!this.structureMap.containsKey(ChunkPos.asLong(☃, ☃))) {
         this.rand.nextInt();

         try {
            if (this.canSpawnStructureAtCoords(☃, ☃)) {
               StructureStart ☃ = this.getStructureStart(☃, ☃);
               this.structureMap.put(ChunkPos.asLong(☃, ☃), ☃);
               if (☃.isSizeableStructure()) {
                  this.setStructureStart(☃, ☃, ☃);
               }
            }
         } catch (Throwable var10) {
            CrashReport ☃ = CrashReport.makeCrashReport(var10, "Exception preparing structure feature");
            CrashReportCategory ☃x = ☃.makeCategory("Feature being prepared");
            ☃x.addDetail("Is feature chunk", new ICrashReportDetail<String>() {
               public String call() throws Exception {
                  return MapGenStructure.this.canSpawnStructureAtCoords(☃, ☃) ? "True" : "False";
               }
            });
            ☃x.addCrashSection("Chunk location", String.format("%d,%d", ☃, ☃));
            ☃x.addDetail("Chunk pos hash", new ICrashReportDetail<String>() {
               public String call() throws Exception {
                  return String.valueOf(ChunkPos.asLong(☃, ☃));
               }
            });
            ☃x.addDetail("Structure type", new ICrashReportDetail<String>() {
               public String call() throws Exception {
                  return MapGenStructure.this.getClass().getCanonicalName();
               }
            });
            throw new ReportedException(☃);
         }
      }
   }

   public synchronized boolean generateStructure(World var1, Random var2, ChunkPos var3) {
      this.initializeStructureData(☃);
      int ☃ = (☃.x << 4) + 8;
      int ☃x = (☃.z << 4) + 8;
      boolean ☃xx = false;
      ObjectIterator var7 = this.structureMap.values().iterator();

      while (var7.hasNext()) {
         StructureStart ☃xxx = (StructureStart)var7.next();
         if (☃xxx.isSizeableStructure() && ☃xxx.isValidForPostProcess(☃) && ☃xxx.getBoundingBox().intersectsWith(☃, ☃x, ☃ + 15, ☃x + 15)) {
            ☃xxx.generateStructure(☃, ☃, new StructureBoundingBox(☃, ☃x, ☃ + 15, ☃x + 15));
            ☃xxx.notifyPostProcessAt(☃);
            ☃xx = true;
            this.setStructureStart(☃xxx.getChunkPosX(), ☃xxx.getChunkPosZ(), ☃xxx);
         }
      }

      return ☃xx;
   }

   public boolean isInsideStructure(BlockPos var1) {
      if (this.world == null) {
         return false;
      } else {
         this.initializeStructureData(this.world);
         return this.getStructureAt(☃) != null;
      }
   }

   @Nullable
   protected StructureStart getStructureAt(BlockPos var1) {
      ObjectIterator var2 = this.structureMap.values().iterator();

      while (var2.hasNext()) {
         StructureStart ☃ = (StructureStart)var2.next();
         if (☃.isSizeableStructure() && ☃.getBoundingBox().isVecInside(☃)) {
            for (StructureComponent ☃x : ☃.getComponents()) {
               if (☃x.getBoundingBox().isVecInside(☃)) {
                  return ☃;
               }
            }
         }
      }

      return null;
   }

   public boolean isPositionInStructure(World var1, BlockPos var2) {
      this.initializeStructureData(☃);
      ObjectIterator var3 = this.structureMap.values().iterator();

      while (var3.hasNext()) {
         StructureStart ☃ = (StructureStart)var3.next();
         if (☃.isSizeableStructure() && ☃.getBoundingBox().isVecInside(☃)) {
            return true;
         }
      }

      return false;
   }

   @Nullable
   public abstract BlockPos getNearestStructurePos(World var1, BlockPos var2, boolean var3);

   protected void initializeStructureData(World var1) {
      if (this.structureData == null && ☃ != null) {
         this.structureData = (MapGenStructureData)☃.loadData(MapGenStructureData.class, this.getStructureName());
         if (this.structureData == null) {
            this.structureData = new MapGenStructureData(this.getStructureName());
            ☃.setData(this.getStructureName(), this.structureData);
         } else {
            NBTTagCompound ☃ = this.structureData.getTagCompound();

            for (String ☃x : ☃.getKeySet()) {
               NBTBase ☃xx = ☃.getTag(☃x);
               if (☃xx.getId() == 10) {
                  NBTTagCompound ☃xxx = (NBTTagCompound)☃xx;
                  if (☃xxx.hasKey("ChunkX") && ☃xxx.hasKey("ChunkZ")) {
                     int ☃xxxx = ☃xxx.getInteger("ChunkX");
                     int ☃xxxxx = ☃xxx.getInteger("ChunkZ");
                     StructureStart ☃xxxxxx = MapGenStructureIO.getStructureStart(☃xxx, ☃);
                     if (☃xxxxxx != null) {
                        this.structureMap.put(ChunkPos.asLong(☃xxxx, ☃xxxxx), ☃xxxxxx);
                     }
                  }
               }
            }
         }
      }
   }

   private void setStructureStart(int var1, int var2, StructureStart var3) {
      this.structureData.writeInstance(☃.writeStructureComponentsToNBT(☃, ☃), ☃, ☃);
      this.structureData.markDirty();
   }

   protected abstract boolean canSpawnStructureAtCoords(int var1, int var2);

   protected abstract StructureStart getStructureStart(int var1, int var2);

   protected static BlockPos findNearestStructurePosBySpacing(
      World var0, MapGenStructure var1, BlockPos var2, int var3, int var4, int var5, boolean var6, int var7, boolean var8
   ) {
      int ☃ = ☃.getX() >> 4;
      int ☃x = ☃.getZ() >> 4;
      int ☃xx = 0;

      for (Random ☃xxx = new Random(); ☃xx <= ☃; ☃xx++) {
         for (int ☃xxxx = -☃xx; ☃xxxx <= ☃xx; ☃xxxx++) {
            boolean ☃xxxxx = ☃xxxx == -☃xx || ☃xxxx == ☃xx;

            for (int ☃xxxxxx = -☃xx; ☃xxxxxx <= ☃xx; ☃xxxxxx++) {
               boolean ☃xxxxxxx = ☃xxxxxx == -☃xx || ☃xxxxxx == ☃xx;
               if (☃xxxxx || ☃xxxxxxx) {
                  int ☃xxxxxxxx = ☃ + ☃ * ☃xxxx;
                  int ☃xxxxxxxxx = ☃x + ☃ * ☃xxxxxx;
                  if (☃xxxxxxxx < 0) {
                     ☃xxxxxxxx -= ☃ - 1;
                  }

                  if (☃xxxxxxxxx < 0) {
                     ☃xxxxxxxxx -= ☃ - 1;
                  }

                  int ☃xxxxxxxxxx = ☃xxxxxxxx / ☃;
                  int ☃xxxxxxxxxxx = ☃xxxxxxxxx / ☃;
                  Random ☃xxxxxxxxxxxx = ☃.setRandomSeed(☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃);
                  ☃xxxxxxxxxx *= ☃;
                  ☃xxxxxxxxxxx *= ☃;
                  if (☃) {
                     ☃xxxxxxxxxx += (☃xxxxxxxxxxxx.nextInt(☃ - ☃) + ☃xxxxxxxxxxxx.nextInt(☃ - ☃)) / 2;
                     ☃xxxxxxxxxxx += (☃xxxxxxxxxxxx.nextInt(☃ - ☃) + ☃xxxxxxxxxxxx.nextInt(☃ - ☃)) / 2;
                  } else {
                     ☃xxxxxxxxxx += ☃xxxxxxxxxxxx.nextInt(☃ - ☃);
                     ☃xxxxxxxxxxx += ☃xxxxxxxxxxxx.nextInt(☃ - ☃);
                  }

                  MapGenBase.setupChunkSeed(☃.getSeed(), ☃xxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx);
                  ☃xxx.nextInt();
                  if (☃.canSpawnStructureAtCoords(☃xxxxxxxxxx, ☃xxxxxxxxxxx)) {
                     if (!☃ || !☃.isChunkGeneratedAt(☃xxxxxxxxxx, ☃xxxxxxxxxxx)) {
                        return new BlockPos((☃xxxxxxxxxx << 4) + 8, 64, (☃xxxxxxxxxxx << 4) + 8);
                     }
                  } else if (☃xx == 0) {
                     break;
                  }
               }
            }

            if (☃xx == 0) {
               break;
            }
         }
      }

      return null;
   }
}
