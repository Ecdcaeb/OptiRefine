package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

public class ChunkGeneratorDebug implements IChunkGenerator {
   private static final List<IBlockState> ALL_VALID_STATES = Lists.newArrayList();
   private static final int GRID_WIDTH;
   private static final int GRID_HEIGHT;
   protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
   protected static final IBlockState BARRIER = Blocks.BARRIER.getDefaultState();
   private final World world;

   public ChunkGeneratorDebug(World var1) {
      this.world = ☃;
   }

   @Override
   public Chunk generateChunk(int var1, int var2) {
      ChunkPrimer ☃ = new ChunkPrimer();

      for (int ☃x = 0; ☃x < 16; ☃x++) {
         for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
            int ☃xxx = ☃ * 16 + ☃x;
            int ☃xxxx = ☃ * 16 + ☃xx;
            ☃.setBlockState(☃x, 60, ☃xx, BARRIER);
            IBlockState ☃xxxxx = getBlockStateFor(☃xxx, ☃xxxx);
            if (☃xxxxx != null) {
               ☃.setBlockState(☃x, 70, ☃xx, ☃xxxxx);
            }
         }
      }

      Chunk ☃x = new Chunk(this.world, ☃, ☃, ☃);
      ☃x.generateSkylightMap();
      Biome[] ☃xxx = this.world.getBiomeProvider().getBiomes(null, ☃ * 16, ☃ * 16, 16, 16);
      byte[] ☃xxxx = ☃x.getBiomeArray();

      for (int ☃xxxxx = 0; ☃xxxxx < ☃xxxx.length; ☃xxxxx++) {
         ☃xxxx[☃xxxxx] = (byte)Biome.getIdForBiome(☃xxx[☃xxxxx]);
      }

      ☃x.generateSkylightMap();
      return ☃x;
   }

   public static IBlockState getBlockStateFor(int var0, int var1) {
      IBlockState ☃ = AIR;
      if (☃ > 0 && ☃ > 0 && ☃ % 2 != 0 && ☃ % 2 != 0) {
         ☃ /= 2;
         ☃ /= 2;
         if (☃ <= GRID_WIDTH && ☃ <= GRID_HEIGHT) {
            int ☃x = MathHelper.abs(☃ * GRID_WIDTH + ☃);
            if (☃x < ALL_VALID_STATES.size()) {
               ☃ = ALL_VALID_STATES.get(☃x);
            }
         }
      }

      return ☃;
   }

   @Override
   public void populate(int var1, int var2) {
   }

   @Override
   public boolean generateStructures(Chunk var1, int var2, int var3) {
      return false;
   }

   @Override
   public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType var1, BlockPos var2) {
      Biome ☃ = this.world.getBiome(☃);
      return ☃.getSpawnableList(☃);
   }

   @Nullable
   @Override
   public BlockPos getNearestStructurePos(World var1, String var2, BlockPos var3, boolean var4) {
      return null;
   }

   @Override
   public boolean isInsideStructure(World var1, String var2, BlockPos var3) {
      return false;
   }

   @Override
   public void recreateStructures(Chunk var1, int var2, int var3) {
   }

   static {
      for (Block ☃ : Block.REGISTRY) {
         ALL_VALID_STATES.addAll(☃.getBlockState().getValidStates());
      }

      GRID_WIDTH = MathHelper.ceil(MathHelper.sqrt((float)ALL_VALID_STATES.size()));
      GRID_HEIGHT = MathHelper.ceil((float)ALL_VALID_STATES.size() / GRID_WIDTH);
   }
}
