package net.minecraft.world.chunk;

import javax.annotation.Nullable;

public interface IChunkProvider {
   @Nullable
   Chunk getLoadedChunk(int var1, int var2);

   Chunk provideChunk(int var1, int var2);

   boolean tick();

   String makeString();

   boolean isChunkGeneratedAt(int var1, int var2);
}
