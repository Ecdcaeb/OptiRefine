package net.minecraft.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class ChunkPrimer {
   private static final IBlockState DEFAULT_STATE = Blocks.AIR.getDefaultState();
   private final char[] data = new char[65536];

   public IBlockState getBlockState(int var1, int var2, int var3) {
      IBlockState ☃ = Block.BLOCK_STATE_IDS.getByValue(this.data[getBlockIndex(☃, ☃, ☃)]);
      return ☃ == null ? DEFAULT_STATE : ☃;
   }

   public void setBlockState(int var1, int var2, int var3, IBlockState var4) {
      this.data[getBlockIndex(☃, ☃, ☃)] = (char)Block.BLOCK_STATE_IDS.get(☃);
   }

   private static int getBlockIndex(int var0, int var1, int var2) {
      return ☃ << 12 | ☃ << 8 | ☃;
   }

   public int findGroundBlockIdx(int var1, int var2) {
      int ☃ = (☃ << 12 | ☃ << 8) + 256 - 1;

      for (int ☃x = 255; ☃x >= 0; ☃x--) {
         IBlockState ☃xx = Block.BLOCK_STATE_IDS.getByValue(this.data[☃ + ☃x]);
         if (☃xx != null && ☃xx != DEFAULT_STATE) {
            return ☃x;
         }
      }

      return 0;
   }
}
