package net.minecraft.world.chunk;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;

public class BlockStatePaletteLinear implements IBlockStatePalette {
   private final IBlockState[] states;
   private final IBlockStatePaletteResizer resizeHandler;
   private final int bits;
   private int arraySize;

   public BlockStatePaletteLinear(int var1, IBlockStatePaletteResizer var2) {
      this.states = new IBlockState[1 << ☃];
      this.bits = ☃;
      this.resizeHandler = ☃;
   }

   @Override
   public int idFor(IBlockState var1) {
      for (int ☃ = 0; ☃ < this.arraySize; ☃++) {
         if (this.states[☃] == ☃) {
            return ☃;
         }
      }

      int ☃x = this.arraySize;
      if (☃x < this.states.length) {
         this.states[☃x] = ☃;
         this.arraySize++;
         return ☃x;
      } else {
         return this.resizeHandler.onResize(this.bits + 1, ☃);
      }
   }

   @Nullable
   @Override
   public IBlockState getBlockState(int var1) {
      return ☃ >= 0 && ☃ < this.arraySize ? this.states[☃] : null;
   }

   @Override
   public void read(PacketBuffer var1) {
      this.arraySize = ☃.readVarInt();

      for (int ☃ = 0; ☃ < this.arraySize; ☃++) {
         this.states[☃] = Block.BLOCK_STATE_IDS.getByValue(☃.readVarInt());
      }
   }

   @Override
   public void write(PacketBuffer var1) {
      ☃.writeVarInt(this.arraySize);

      for (int ☃ = 0; ☃ < this.arraySize; ☃++) {
         ☃.writeVarInt(Block.BLOCK_STATE_IDS.get(this.states[☃]));
      }
   }

   @Override
   public int getSerializedSize() {
      int ☃ = PacketBuffer.getVarIntSize(this.arraySize);

      for (int ☃x = 0; ☃x < this.arraySize; ☃x++) {
         ☃ += PacketBuffer.getVarIntSize(Block.BLOCK_STATE_IDS.get(this.states[☃x]));
      }

      return ☃;
   }
}
