package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkProviderServer implements IChunkProvider {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Set<Long> droppedChunks = Sets.newHashSet();
   private final IChunkGenerator chunkGenerator;
   private final IChunkLoader chunkLoader;
   private final Long2ObjectMap<Chunk> loadedChunks = new Long2ObjectOpenHashMap(8192);
   private final WorldServer world;

   public ChunkProviderServer(WorldServer var1, IChunkLoader var2, IChunkGenerator var3) {
      this.world = ☃;
      this.chunkLoader = ☃;
      this.chunkGenerator = ☃;
   }

   public Collection<Chunk> getLoadedChunks() {
      return this.loadedChunks.values();
   }

   public void queueUnload(Chunk var1) {
      if (this.world.provider.canDropChunk(☃.x, ☃.z)) {
         this.droppedChunks.add(ChunkPos.asLong(☃.x, ☃.z));
         ☃.unloadQueued = true;
      }
   }

   public void queueUnloadAll() {
      ObjectIterator var1 = this.loadedChunks.values().iterator();

      while (var1.hasNext()) {
         Chunk ☃ = (Chunk)var1.next();
         this.queueUnload(☃);
      }
   }

   @Nullable
   @Override
   public Chunk getLoadedChunk(int var1, int var2) {
      long ☃ = ChunkPos.asLong(☃, ☃);
      Chunk ☃x = (Chunk)this.loadedChunks.get(☃);
      if (☃x != null) {
         ☃x.unloadQueued = false;
      }

      return ☃x;
   }

   @Nullable
   public Chunk loadChunk(int var1, int var2) {
      Chunk ☃ = this.getLoadedChunk(☃, ☃);
      if (☃ == null) {
         ☃ = this.loadChunkFromFile(☃, ☃);
         if (☃ != null) {
            this.loadedChunks.put(ChunkPos.asLong(☃, ☃), ☃);
            ☃.onLoad();
            ☃.populate(this, this.chunkGenerator);
         }
      }

      return ☃;
   }

   @Override
   public Chunk provideChunk(int var1, int var2) {
      Chunk ☃ = this.loadChunk(☃, ☃);
      if (☃ == null) {
         long ☃x = ChunkPos.asLong(☃, ☃);

         try {
            ☃ = this.chunkGenerator.generateChunk(☃, ☃);
         } catch (Throwable var9) {
            CrashReport ☃xx = CrashReport.makeCrashReport(var9, "Exception generating new chunk");
            CrashReportCategory ☃xxx = ☃xx.makeCategory("Chunk to be generated");
            ☃xxx.addCrashSection("Location", String.format("%d,%d", ☃, ☃));
            ☃xxx.addCrashSection("Position hash", ☃x);
            ☃xxx.addCrashSection("Generator", this.chunkGenerator);
            throw new ReportedException(☃xx);
         }

         this.loadedChunks.put(☃x, ☃);
         ☃.onLoad();
         ☃.populate(this, this.chunkGenerator);
      }

      return ☃;
   }

   @Nullable
   private Chunk loadChunkFromFile(int var1, int var2) {
      try {
         Chunk ☃ = this.chunkLoader.loadChunk(this.world, ☃, ☃);
         if (☃ != null) {
            ☃.setLastSaveTime(this.world.getTotalWorldTime());
            this.chunkGenerator.recreateStructures(☃, ☃, ☃);
         }

         return ☃;
      } catch (Exception var4) {
         LOGGER.error("Couldn't load chunk", var4);
         return null;
      }
   }

   private void saveChunkExtraData(Chunk var1) {
      try {
         this.chunkLoader.saveExtraChunkData(this.world, ☃);
      } catch (Exception var3) {
         LOGGER.error("Couldn't save entities", var3);
      }
   }

   private void saveChunkData(Chunk var1) {
      try {
         ☃.setLastSaveTime(this.world.getTotalWorldTime());
         this.chunkLoader.saveChunk(this.world, ☃);
      } catch (IOException var3) {
         LOGGER.error("Couldn't save chunk", var3);
      } catch (MinecraftException var4) {
         LOGGER.error("Couldn't save chunk; already in use by another instance of Minecraft?", var4);
      }
   }

   public boolean saveChunks(boolean var1) {
      int ☃ = 0;
      List<Chunk> ☃x = Lists.newArrayList(this.loadedChunks.values());

      for (int ☃xx = 0; ☃xx < ☃x.size(); ☃xx++) {
         Chunk ☃xxx = ☃x.get(☃xx);
         if (☃) {
            this.saveChunkExtraData(☃xxx);
         }

         if (☃xxx.needsSaving(☃)) {
            this.saveChunkData(☃xxx);
            ☃xxx.setModified(false);
            if (++☃ == 24 && !☃) {
               return false;
            }
         }
      }

      return true;
   }

   public void flushToDisk() {
      this.chunkLoader.flush();
   }

   @Override
   public boolean tick() {
      if (!this.world.disableLevelSaving) {
         if (!this.droppedChunks.isEmpty()) {
            Iterator<Long> ☃ = this.droppedChunks.iterator();

            for (int ☃x = 0; ☃x < 100 && ☃.hasNext(); ☃.remove()) {
               Long ☃xx = ☃.next();
               Chunk ☃xxx = (Chunk)this.loadedChunks.get(☃xx);
               if (☃xxx != null && ☃xxx.unloadQueued) {
                  ☃xxx.onUnload();
                  this.saveChunkData(☃xxx);
                  this.saveChunkExtraData(☃xxx);
                  this.loadedChunks.remove(☃xx);
                  ☃x++;
               }
            }
         }

         this.chunkLoader.chunkTick();
      }

      return false;
   }

   public boolean canSave() {
      return !this.world.disableLevelSaving;
   }

   @Override
   public String makeString() {
      return "ServerChunkCache: " + this.loadedChunks.size() + " Drop: " + this.droppedChunks.size();
   }

   public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType var1, BlockPos var2) {
      return this.chunkGenerator.getPossibleCreatures(☃, ☃);
   }

   @Nullable
   public BlockPos getNearestStructurePos(World var1, String var2, BlockPos var3, boolean var4) {
      return this.chunkGenerator.getNearestStructurePos(☃, ☃, ☃, ☃);
   }

   public boolean isInsideStructure(World var1, String var2, BlockPos var3) {
      return this.chunkGenerator.isInsideStructure(☃, ☃, ☃);
   }

   public int getLoadedChunkCount() {
      return this.loadedChunks.size();
   }

   public boolean chunkExists(int var1, int var2) {
      return this.loadedChunks.containsKey(ChunkPos.asLong(☃, ☃));
   }

   @Override
   public boolean isChunkGeneratedAt(int var1, int var2) {
      return this.loadedChunks.containsKey(ChunkPos.asLong(☃, ☃)) || this.chunkLoader.isChunkGeneratedAt(☃, ☃);
   }
}
