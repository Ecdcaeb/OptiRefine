package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketSteerBoat implements Packet<INetHandlerPlayServer> {
   private boolean left;
   private boolean right;

   public CPacketSteerBoat() {
   }

   public CPacketSteerBoat(boolean var1, boolean var2) {
      this.left = ☃;
      this.right = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.left = ☃.readBoolean();
      this.right = ☃.readBoolean();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeBoolean(this.left);
      ☃.writeBoolean(this.right);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processSteerBoat(this);
   }

   public boolean getLeft() {
      return this.left;
   }

   public boolean getRight() {
      return this.right;
   }
}
