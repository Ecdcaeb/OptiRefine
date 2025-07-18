package net.minecraft.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;

public class BlockStatePaletteRegistry implements IBlockStatePalette {
   @Override
   public int idFor(IBlockState var1) {
      int ☃ = Block.BLOCK_STATE_IDS.get(☃);
      return ☃ == -1 ? 0 : ☃;
   }

   @Override
   public IBlockState getBlockState(int var1) {
      IBlockState ☃ = Block.BLOCK_STATE_IDS.getByValue(☃);
      return ☃ == null ? Blocks.AIR.getDefaultState() : ☃;
   }

   @Override
   public void read(PacketBuffer var1) {
      ☃.readVarInt();
   }

   @Override
   public void write(PacketBuffer var1) {
      ☃.writeVarInt(0);
   }

   @Override
   public int getSerializedSize() {
      return PacketBuffer.getVarIntSize(0);
   }
}
