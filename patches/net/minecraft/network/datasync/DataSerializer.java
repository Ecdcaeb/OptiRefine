package net.minecraft.network.datasync;

import net.minecraft.network.PacketBuffer;

public interface DataSerializer<T> {
   void write(PacketBuffer var1, T var2);

   T read(PacketBuffer var1);

   DataParameter<T> createKey(int var1);

   T copyValue(T var1);
}
