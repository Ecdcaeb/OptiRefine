package net.minecraft.world.chunk;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntIdentityHashBiMap;

public class BlockStatePaletteHashMap implements IBlockStatePalette {
   private final IntIdentityHashBiMap<IBlockState> statePaletteMap;
   private final IBlockStatePaletteResizer paletteResizer;
   private final int bits;

   public BlockStatePaletteHashMap(int var1, IBlockStatePaletteResizer var2) {
      this.bits = ☃;
      this.paletteResizer = ☃;
      this.statePaletteMap = new IntIdentityHashBiMap<>(1 << ☃);
   }

   @Override
   public int idFor(IBlockState var1) {
      int ☃ = this.statePaletteMap.getId(☃);
      if (☃ == -1) {
         ☃ = this.statePaletteMap.add(☃);
         if (☃ >= 1 << this.bits) {
            ☃ = this.paletteResizer.onResize(this.bits + 1, ☃);
         }
      }

      return ☃;
   }

   @Nullable
   @Override
   public IBlockState getBlockState(int var1) {
      return this.statePaletteMap.get(☃);
   }

   @Override
   public void read(PacketBuffer var1) {
      this.statePaletteMap.clear();
      int ☃ = ☃.readVarInt();

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         this.statePaletteMap.add(Block.BLOCK_STATE_IDS.getByValue(☃.readVarInt()));
      }
   }

   @Override
   public void write(PacketBuffer var1) {
      int ☃ = this.statePaletteMap.size();
      ☃.writeVarInt(☃);

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         ☃.writeVarInt(Block.BLOCK_STATE_IDS.get(this.statePaletteMap.get(☃x)));
      }
   }

   @Override
   public int getSerializedSize() {
      int ☃ = PacketBuffer.getVarIntSize(this.statePaletteMap.size());

      for (int ☃x = 0; ☃x < this.statePaletteMap.size(); ☃x++) {
         ☃ += PacketBuffer.getVarIntSize(Block.BLOCK_STATE_IDS.get(this.statePaletteMap.get(☃x)));
      }

      return ☃;
   }
}
