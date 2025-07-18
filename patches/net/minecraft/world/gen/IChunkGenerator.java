package net.minecraft.world.gen;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public interface IChunkGenerator {
   Chunk generateChunk(int var1, int var2);

   void populate(int var1, int var2);

   boolean generateStructures(Chunk var1, int var2, int var3);

   List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType var1, BlockPos var2);

   @Nullable
   BlockPos getNearestStructurePos(World var1, String var2, BlockPos var3, boolean var4);

   void recreateStructures(Chunk var1, int var2, int var3);

   boolean isInsideStructure(World var1, String var2, BlockPos var3);
}
