package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketTimeUpdate implements Packet<INetHandlerPlayClient> {
   private long totalWorldTime;
   private long worldTime;

   public SPacketTimeUpdate() {
   }

   public SPacketTimeUpdate(long var1, long var3, boolean var5) {
      this.totalWorldTime = ☃;
      this.worldTime = ☃;
      if (!☃) {
         this.worldTime = -this.worldTime;
         if (this.worldTime == 0L) {
            this.worldTime = -1L;
         }
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.totalWorldTime = ☃.readLong();
      this.worldTime = ☃.readLong();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeLong(this.totalWorldTime);
      ☃.writeLong(this.worldTime);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleTimeUpdate(this);
   }

   public long getTotalWorldTime() {
      return this.totalWorldTime;
   }

   public long getWorldTime() {
      return this.worldTime;
   }
}
