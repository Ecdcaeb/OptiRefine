package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketConfirmTransaction implements Packet<INetHandlerPlayServer> {
   private int windowId;
   private short uid;
   private boolean accepted;

   public CPacketConfirmTransaction() {
   }

   public CPacketConfirmTransaction(int var1, short var2, boolean var3) {
      this.windowId = ☃;
      this.uid = ☃;
      this.accepted = ☃;
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processConfirmTransaction(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = ☃.readByte();
      this.uid = ☃.readShort();
      this.accepted = ☃.readByte() != 0;
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.windowId);
      ☃.writeShort(this.uid);
      ☃.writeByte(this.accepted ? 1 : 0);
   }

   public int getWindowId() {
      return this.windowId;
   }

   public short getUid() {
      return this.uid;
   }
}
