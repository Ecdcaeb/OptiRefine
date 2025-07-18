package net.minecraft.world.chunk;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;

public interface IBlockStatePalette {
   int idFor(IBlockState var1);

   @Nullable
   IBlockState getBlockState(int var1);

   void read(PacketBuffer var1);

   void write(PacketBuffer var1);

   int getSerializedSize();
}
