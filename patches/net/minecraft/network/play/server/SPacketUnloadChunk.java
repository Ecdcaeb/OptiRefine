package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketUnloadChunk implements Packet<INetHandlerPlayClient> {
   private int x;
   private int z;

   public SPacketUnloadChunk() {
   }

   public SPacketUnloadChunk(int var1, int var2) {
      this.x = ☃;
      this.z = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.x = ☃.readInt();
      this.z = ☃.readInt();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeInt(this.x);
      ☃.writeInt(this.z);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.processChunkUnload(this);
   }

   public int getX() {
      return this.x;
   }

   public int getZ() {
      return this.z;
   }
}
