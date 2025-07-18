package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketConfirmTransaction implements Packet<INetHandlerPlayClient> {
   private int windowId;
   private short actionNumber;
   private boolean accepted;

   public SPacketConfirmTransaction() {
   }

   public SPacketConfirmTransaction(int var1, short var2, boolean var3) {
      this.windowId = ☃;
      this.actionNumber = ☃;
      this.accepted = ☃;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleConfirmTransaction(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = ☃.readUnsignedByte();
      this.actionNumber = ☃.readShort();
      this.accepted = ☃.readBoolean();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.windowId);
      ☃.writeShort(this.actionNumber);
      ☃.writeBoolean(this.accepted);
   }

   public int getWindowId() {
      return this.windowId;
   }

   public short getActionNumber() {
      return this.actionNumber;
   }

   public boolean wasAccepted() {
      return this.accepted;
   }
}
