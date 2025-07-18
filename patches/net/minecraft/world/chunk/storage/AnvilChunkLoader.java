package net.minecraft.world.chunk.storage;

import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnvilChunkLoader implements IChunkLoader, IThreadedFileIO {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Map<ChunkPos, NBTTagCompound> chunksToSave = Maps.newConcurrentMap();
   private final Set<ChunkPos> chunksBeingSaved = Collections.newSetFromMap(Maps.newConcurrentMap());
   private final File chunkSaveLocation;
   private final DataFixer fixer;
   private boolean flushing;

   public AnvilChunkLoader(File var1, DataFixer var2) {
      this.chunkSaveLocation = ☃;
      this.fixer = ☃;
   }

   @Nullable
   @Override
   public Chunk loadChunk(World var1, int var2, int var3) throws IOException {
      ChunkPos ☃ = new ChunkPos(☃, ☃);
      NBTTagCompound ☃x = this.chunksToSave.get(☃);
      if (☃x == null) {
         DataInputStream ☃xx = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, ☃, ☃);
         if (☃xx == null) {
            return null;
         }

         ☃x = this.fixer.process(FixTypes.CHUNK, CompressedStreamTools.read(☃xx));
      }

      return this.checkedReadChunkFromNBT(☃, ☃, ☃, ☃x);
   }

   @Override
   public boolean isChunkGeneratedAt(int var1, int var2) {
      ChunkPos ☃ = new ChunkPos(☃, ☃);
      NBTTagCompound ☃x = this.chunksToSave.get(☃);
      return ☃x != null ? true : RegionFileCache.chunkExists(this.chunkSaveLocation, ☃, ☃);
   }

   @Nullable
   protected Chunk checkedReadChunkFromNBT(World var1, int var2, int var3, NBTTagCompound var4) {
      if (!☃.hasKey("Level", 10)) {
         LOGGER.error("Chunk file at {},{} is missing level data, skipping", ☃, ☃);
         return null;
      } else {
         NBTTagCompound ☃ = ☃.getCompoundTag("Level");
         if (!☃.hasKey("Sections", 9)) {
            LOGGER.error("Chunk file at {},{} is missing block data, skipping", ☃, ☃);
            return null;
         } else {
            Chunk ☃x = this.readChunkFromNBT(☃, ☃);
            if (!☃x.isAtLocation(☃, ☃)) {
               LOGGER.error("Chunk file at {},{} is in the wrong location; relocating. (Expected {}, {}, got {}, {})", ☃, ☃, ☃, ☃, ☃x.x, ☃x.z);
               ☃.setInteger("xPos", ☃);
               ☃.setInteger("zPos", ☃);
               ☃x = this.readChunkFromNBT(☃, ☃);
            }

            return ☃x;
         }
      }
   }

   @Override
   public void saveChunk(World var1, Chunk var2) throws IOException, MinecraftException {
      ☃.checkSessionLock();

      try {
         NBTTagCompound ☃ = new NBTTagCompound();
         NBTTagCompound ☃x = new NBTTagCompound();
         ☃.setTag("Level", ☃x);
         ☃.setInteger("DataVersion", 1343);
         this.writeChunkToNBT(☃, ☃, ☃x);
         this.addChunkToPending(☃.getPos(), ☃);
      } catch (Exception var5) {
         LOGGER.error("Failed to save chunk", var5);
      }
   }

   protected void addChunkToPending(ChunkPos var1, NBTTagCompound var2) {
      if (!this.chunksBeingSaved.contains(☃)) {
         this.chunksToSave.put(☃, ☃);
      }

      ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
   }

   @Override
   public boolean writeNextIO() {
      if (this.chunksToSave.isEmpty()) {
         if (this.flushing) {
            LOGGER.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", this.chunkSaveLocation.getName());
         }

         return false;
      } else {
         ChunkPos ☃ = this.chunksToSave.keySet().iterator().next();

         boolean var3;
         try {
            this.chunksBeingSaved.add(☃);
            NBTTagCompound ☃x = this.chunksToSave.remove(☃);
            if (☃x != null) {
               try {
                  this.writeChunkData(☃, ☃x);
               } catch (Exception var7) {
                  LOGGER.error("Failed to save chunk", var7);
               }
            }

            var3 = true;
         } finally {
            this.chunksBeingSaved.remove(☃);
         }

         return var3;
      }
   }

   private void writeChunkData(ChunkPos var1, NBTTagCompound var2) throws IOException {
      DataOutputStream ☃ = RegionFileCache.getChunkOutputStream(this.chunkSaveLocation, ☃.x, ☃.z);
      CompressedStreamTools.write(☃, ☃);
      ☃.close();
   }

   @Override
   public void saveExtraChunkData(World var1, Chunk var2) throws IOException {
   }

   @Override
   public void chunkTick() {
   }

   @Override
   public void flush() {
      try {
         this.flushing = true;

         while (this.writeNextIO()) {
         }
      } finally {
         this.flushing = false;
      }
   }

   public static void registerFixes(DataFixer var0) {
      ☃.registerWalker(FixTypes.CHUNK, new IDataWalker() {
         @Override
         public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3) {
            if (☃.hasKey("Level", 10)) {
               NBTTagCompound ☃ = ☃.getCompoundTag("Level");
               if (☃.hasKey("Entities", 9)) {
                  NBTTagList ☃x = ☃.getTagList("Entities", 10);

                  for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
                     ☃x.set(☃xx, ☃.process(FixTypes.ENTITY, (NBTTagCompound)☃x.get(☃xx), ☃));
                  }
               }

               if (☃.hasKey("TileEntities", 9)) {
                  NBTTagList ☃x = ☃.getTagList("TileEntities", 10);

                  for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
                     ☃x.set(☃xx, ☃.process(FixTypes.BLOCK_ENTITY, (NBTTagCompound)☃x.get(☃xx), ☃));
                  }
               }
            }

            return ☃;
         }
      });
   }

   private void writeChunkToNBT(Chunk var1, World var2, NBTTagCompound var3) {
      ☃.setInteger("xPos", ☃.x);
      ☃.setInteger("zPos", ☃.z);
      ☃.setLong("LastUpdate", ☃.getTotalWorldTime());
      ☃.setIntArray("HeightMap", ☃.getHeightMap());
      ☃.setBoolean("TerrainPopulated", ☃.isTerrainPopulated());
      ☃.setBoolean("LightPopulated", ☃.isLightPopulated());
      ☃.setLong("InhabitedTime", ☃.getInhabitedTime());
      ExtendedBlockStorage[] ☃ = ☃.getBlockStorageArray();
      NBTTagList ☃x = new NBTTagList();
      boolean ☃xx = ☃.provider.hasSkyLight();

      for (ExtendedBlockStorage ☃xxx : ☃) {
         if (☃xxx != Chunk.NULL_BLOCK_STORAGE) {
            NBTTagCompound ☃xxxx = new NBTTagCompound();
            ☃xxxx.setByte("Y", (byte)(☃xxx.getYLocation() >> 4 & 0xFF));
            byte[] ☃xxxxx = new byte[4096];
            NibbleArray ☃xxxxxx = new NibbleArray();
            NibbleArray ☃xxxxxxx = ☃xxx.getData().getDataForNBT(☃xxxxx, ☃xxxxxx);
            ☃xxxx.setByteArray("Blocks", ☃xxxxx);
            ☃xxxx.setByteArray("Data", ☃xxxxxx.getData());
            if (☃xxxxxxx != null) {
               ☃xxxx.setByteArray("Add", ☃xxxxxxx.getData());
            }

            ☃xxxx.setByteArray("BlockLight", ☃xxx.getBlockLight().getData());
            if (☃xx) {
               ☃xxxx.setByteArray("SkyLight", ☃xxx.getSkyLight().getData());
            } else {
               ☃xxxx.setByteArray("SkyLight", new byte[☃xxx.getBlockLight().getData().length]);
            }

            ☃x.appendTag(☃xxxx);
         }
      }

      ☃.setTag("Sections", ☃x);
      ☃.setByteArray("Biomes", ☃.getBiomeArray());
      ☃.setHasEntities(false);
      NBTTagList ☃xxxxxxxx = new NBTTagList();

      for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < ☃.getEntityLists().length; ☃xxxxxxxxx++) {
         for (Entity ☃xxxxxxxxxx : ☃.getEntityLists()[☃xxxxxxxxx]) {
            NBTTagCompound ☃xxxxxxxxxxx = new NBTTagCompound();
            if (☃xxxxxxxxxx.writeToNBTOptional(☃xxxxxxxxxxx)) {
               ☃.setHasEntities(true);
               ☃xxxxxxxx.appendTag(☃xxxxxxxxxxx);
            }
         }
      }

      ☃.setTag("Entities", ☃xxxxxxxx);
      NBTTagList ☃xxxxxxxxx = new NBTTagList();

      for (TileEntity ☃xxxxxxxxxxx : ☃.getTileEntityMap().values()) {
         NBTTagCompound ☃xxxxxxxxxxxx = ☃xxxxxxxxxxx.writeToNBT(new NBTTagCompound());
         ☃xxxxxxxxx.appendTag(☃xxxxxxxxxxxx);
      }

      ☃.setTag("TileEntities", ☃xxxxxxxxx);
      List<NextTickListEntry> ☃xxxxxxxxxxx = ☃.getPendingBlockUpdates(☃, false);
      if (☃xxxxxxxxxxx != null) {
         long ☃xxxxxxxxxxxx = ☃.getTotalWorldTime();
         NBTTagList ☃xxxxxxxxxxxxx = new NBTTagList();

         for (NextTickListEntry ☃xxxxxxxxxxxxxx : ☃xxxxxxxxxxx) {
            NBTTagCompound ☃xxxxxxxxxxxxxxx = new NBTTagCompound();
            ResourceLocation ☃xxxxxxxxxxxxxxxx = Block.REGISTRY.getNameForObject(☃xxxxxxxxxxxxxx.getBlock());
            ☃xxxxxxxxxxxxxxx.setString("i", ☃xxxxxxxxxxxxxxxx == null ? "" : ☃xxxxxxxxxxxxxxxx.toString());
            ☃xxxxxxxxxxxxxxx.setInteger("x", ☃xxxxxxxxxxxxxx.position.getX());
            ☃xxxxxxxxxxxxxxx.setInteger("y", ☃xxxxxxxxxxxxxx.position.getY());
            ☃xxxxxxxxxxxxxxx.setInteger("z", ☃xxxxxxxxxxxxxx.position.getZ());
            ☃xxxxxxxxxxxxxxx.setInteger("t", (int)(☃xxxxxxxxxxxxxx.scheduledTime - ☃xxxxxxxxxxxx));
            ☃xxxxxxxxxxxxxxx.setInteger("p", ☃xxxxxxxxxxxxxx.priority);
            ☃xxxxxxxxxxxxx.appendTag(☃xxxxxxxxxxxxxxx);
         }

         ☃.setTag("TileTicks", ☃xxxxxxxxxxxxx);
      }
   }

   private Chunk readChunkFromNBT(World var1, NBTTagCompound var2) {
      int ☃ = ☃.getInteger("xPos");
      int ☃x = ☃.getInteger("zPos");
      Chunk ☃xx = new Chunk(☃, ☃, ☃x);
      ☃xx.setHeightMap(☃.getIntArray("HeightMap"));
      ☃xx.setTerrainPopulated(☃.getBoolean("TerrainPopulated"));
      ☃xx.setLightPopulated(☃.getBoolean("LightPopulated"));
      ☃xx.setInhabitedTime(☃.getLong("InhabitedTime"));
      NBTTagList ☃xxx = ☃.getTagList("Sections", 10);
      int ☃xxxx = 16;
      ExtendedBlockStorage[] ☃xxxxx = new ExtendedBlockStorage[16];
      boolean ☃xxxxxx = ☃.provider.hasSkyLight();

      for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃xxx.tagCount(); ☃xxxxxxx++) {
         NBTTagCompound ☃xxxxxxxx = ☃xxx.getCompoundTagAt(☃xxxxxxx);
         int ☃xxxxxxxxx = ☃xxxxxxxx.getByte("Y");
         ExtendedBlockStorage ☃xxxxxxxxxx = new ExtendedBlockStorage(☃xxxxxxxxx << 4, ☃xxxxxx);
         byte[] ☃xxxxxxxxxxx = ☃xxxxxxxx.getByteArray("Blocks");
         NibbleArray ☃xxxxxxxxxxxx = new NibbleArray(☃xxxxxxxx.getByteArray("Data"));
         NibbleArray ☃xxxxxxxxxxxxx = ☃xxxxxxxx.hasKey("Add", 7) ? new NibbleArray(☃xxxxxxxx.getByteArray("Add")) : null;
         ☃xxxxxxxxxx.getData().setDataFromNBT(☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx);
         ☃xxxxxxxxxx.setBlockLight(new NibbleArray(☃xxxxxxxx.getByteArray("BlockLight")));
         if (☃xxxxxx) {
            ☃xxxxxxxxxx.setSkyLight(new NibbleArray(☃xxxxxxxx.getByteArray("SkyLight")));
         }

         ☃xxxxxxxxxx.recalculateRefCounts();
         ☃xxxxx[☃xxxxxxxxx] = ☃xxxxxxxxxx;
      }

      ☃xx.setStorageArrays(☃xxxxx);
      if (☃.hasKey("Biomes", 7)) {
         ☃xx.setBiomeArray(☃.getByteArray("Biomes"));
      }

      NBTTagList ☃xxxxxxx = ☃.getTagList("Entities", 10);

      for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃xxxxxxx.tagCount(); ☃xxxxxxxx++) {
         NBTTagCompound ☃xxxxxxxxx = ☃xxxxxxx.getCompoundTagAt(☃xxxxxxxx);
         readChunkEntity(☃xxxxxxxxx, ☃, ☃xx);
         ☃xx.setHasEntities(true);
      }

      NBTTagList ☃xxxxxxxx = ☃.getTagList("TileEntities", 10);

      for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < ☃xxxxxxxx.tagCount(); ☃xxxxxxxxx++) {
         NBTTagCompound ☃xxxxxxxxxx = ☃xxxxxxxx.getCompoundTagAt(☃xxxxxxxxx);
         TileEntity ☃xxxxxxxxxxx = TileEntity.create(☃, ☃xxxxxxxxxx);
         if (☃xxxxxxxxxxx != null) {
            ☃xx.addTileEntity(☃xxxxxxxxxxx);
         }
      }

      if (☃.hasKey("TileTicks", 9)) {
         NBTTagList ☃xxxxxxxxxx = ☃.getTagList("TileTicks", 10);

         for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < ☃xxxxxxxxxx.tagCount(); ☃xxxxxxxxxxx++) {
            NBTTagCompound ☃xxxxxxxxxxxx = ☃xxxxxxxxxx.getCompoundTagAt(☃xxxxxxxxxxx);
            Block ☃xxxxxxxxxxxxx;
            if (☃xxxxxxxxxxxx.hasKey("i", 8)) {
               ☃xxxxxxxxxxxxx = Block.getBlockFromName(☃xxxxxxxxxxxx.getString("i"));
            } else {
               ☃xxxxxxxxxxxxx = Block.getBlockById(☃xxxxxxxxxxxx.getInteger("i"));
            }

            ☃.scheduleBlockUpdate(
               new BlockPos(☃xxxxxxxxxxxx.getInteger("x"), ☃xxxxxxxxxxxx.getInteger("y"), ☃xxxxxxxxxxxx.getInteger("z")),
               ☃xxxxxxxxxxxxx,
               ☃xxxxxxxxxxxx.getInteger("t"),
               ☃xxxxxxxxxxxx.getInteger("p")
            );
         }
      }

      return ☃xx;
   }

   @Nullable
   public static Entity readChunkEntity(NBTTagCompound var0, World var1, Chunk var2) {
      Entity ☃ = createEntityFromNBT(☃, ☃);
      if (☃ == null) {
         return null;
      } else {
         ☃.addEntity(☃);
         if (☃.hasKey("Passengers", 9)) {
            NBTTagList ☃x = ☃.getTagList("Passengers", 10);

            for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
               Entity ☃xxx = readChunkEntity(☃x.getCompoundTagAt(☃xx), ☃, ☃);
               if (☃xxx != null) {
                  ☃xxx.startRiding(☃, true);
               }
            }
         }

         return ☃;
      }
   }

   @Nullable
   public static Entity readWorldEntityPos(NBTTagCompound var0, World var1, double var2, double var4, double var6, boolean var8) {
      Entity ☃ = createEntityFromNBT(☃, ☃);
      if (☃ == null) {
         return null;
      } else {
         ☃.setLocationAndAngles(☃, ☃, ☃, ☃.rotationYaw, ☃.rotationPitch);
         if (☃ && !☃.spawnEntity(☃)) {
            return null;
         } else {
            if (☃.hasKey("Passengers", 9)) {
               NBTTagList ☃x = ☃.getTagList("Passengers", 10);

               for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
                  Entity ☃xxx = readWorldEntityPos(☃x.getCompoundTagAt(☃xx), ☃, ☃, ☃, ☃, ☃);
                  if (☃xxx != null) {
                     ☃xxx.startRiding(☃, true);
                  }
               }
            }

            return ☃;
         }
      }
   }

   @Nullable
   protected static Entity createEntityFromNBT(NBTTagCompound var0, World var1) {
      try {
         return EntityList.createEntityFromNBT(☃, ☃);
      } catch (RuntimeException var3) {
         return null;
      }
   }

   public static void spawnEntity(Entity var0, World var1) {
      if (☃.spawnEntity(☃) && ☃.isBeingRidden()) {
         for (Entity ☃ : ☃.getPassengers()) {
            spawnEntity(☃, ☃);
         }
      }
   }

   @Nullable
   public static Entity readWorldEntity(NBTTagCompound var0, World var1, boolean var2) {
      Entity ☃ = createEntityFromNBT(☃, ☃);
      if (☃ == null) {
         return null;
      } else if (☃ && !☃.spawnEntity(☃)) {
         return null;
      } else {
         if (☃.hasKey("Passengers", 9)) {
            NBTTagList ☃x = ☃.getTagList("Passengers", 10);

            for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
               Entity ☃xxx = readWorldEntity(☃x.getCompoundTagAt(☃xx), ☃, ☃);
               if (☃xxx != null) {
                  ☃xxx.startRiding(☃, true);
               }
            }
         }

         return ☃;
      }
   }
}
