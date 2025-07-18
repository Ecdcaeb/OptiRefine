package net.minecraft.client.multiplayer;

import com.google.common.base.MoreObjects;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import javax.annotation.Nullable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkProviderClient implements IChunkProvider {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Chunk blankChunk;
   private final Long2ObjectMap<Chunk> loadedChunks = new Long2ObjectOpenHashMap<Chunk>(8192) {
      protected void rehash(int var1) {
         if (☃ > this.key.length) {
            super.rehash(☃);
         }
      }
   };
   private final World world;

   public ChunkProviderClient(World var1) {
      this.blankChunk = new EmptyChunk(☃, 0, 0);
      this.world = ☃;
   }

   public void unloadChunk(int var1, int var2) {
      Chunk ☃ = this.provideChunk(☃, ☃);
      if (!☃.isEmpty()) {
         ☃.onUnload();
      }

      this.loadedChunks.remove(ChunkPos.asLong(☃, ☃));
   }

   @Nullable
   @Override
   public Chunk getLoadedChunk(int var1, int var2) {
      return (Chunk)this.loadedChunks.get(ChunkPos.asLong(☃, ☃));
   }

   public Chunk loadChunk(int var1, int var2) {
      Chunk ☃ = new Chunk(this.world, ☃, ☃);
      this.loadedChunks.put(ChunkPos.asLong(☃, ☃), ☃);
      ☃.markLoaded(true);
      return ☃;
   }

   @Override
   public Chunk provideChunk(int var1, int var2) {
      return (Chunk)MoreObjects.firstNonNull(this.getLoadedChunk(☃, ☃), this.blankChunk);
   }

   @Override
   public boolean tick() {
      long ☃ = System.currentTimeMillis();
      ObjectIterator var3 = this.loadedChunks.values().iterator();

      while (var3.hasNext()) {
         Chunk ☃x = (Chunk)var3.next();
         ☃x.onTick(System.currentTimeMillis() - ☃ > 5L);
      }

      if (System.currentTimeMillis() - ☃ > 100L) {
         LOGGER.info("Warning: Clientside chunk ticking took {} ms", System.currentTimeMillis() - ☃);
      }

      return false;
   }

   @Override
   public String makeString() {
      return "MultiplayerChunkCache: " + this.loadedChunks.size() + ", " + this.loadedChunks.size();
   }

   @Override
   public boolean isChunkGeneratedAt(int var1, int var2) {
      return this.loadedChunks.containsKey(ChunkPos.asLong(☃, ☃));
   }
}
