package net.minecraft.network.handshake.client;

import java.io.IOException;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;

public class C00Handshake implements Packet<INetHandlerHandshakeServer> {
   private int protocolVersion;
   private String ip;
   private int port;
   private EnumConnectionState requestedState;

   public C00Handshake() {
   }

   public C00Handshake(String var1, int var2, EnumConnectionState var3) {
      this.protocolVersion = 340;
      this.ip = ☃;
      this.port = ☃;
      this.requestedState = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.protocolVersion = ☃.readVarInt();
      this.ip = ☃.readString(255);
      this.port = ☃.readUnsignedShort();
      this.requestedState = EnumConnectionState.getById(☃.readVarInt());
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.protocolVersion);
      ☃.writeString(this.ip);
      ☃.writeShort(this.port);
      ☃.writeVarInt(this.requestedState.getId());
   }

   public void processPacket(INetHandlerHandshakeServer var1) {
      ☃.processHandshake(this);
   }

   public EnumConnectionState getRequestedState() {
      return this.requestedState;
   }

   public int getProtocolVersion() {
      return this.protocolVersion;
   }
}
