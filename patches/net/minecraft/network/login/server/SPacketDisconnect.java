package net.minecraft.network.login.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketDisconnect implements Packet<INetHandlerLoginClient> {
   private ITextComponent reason;

   public SPacketDisconnect() {
   }

   public SPacketDisconnect(ITextComponent var1) {
      this.reason = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.reason = ITextComponent.Serializer.fromJsonLenient(☃.readString(32767));
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeTextComponent(this.reason);
   }

   public void processPacket(INetHandlerLoginClient var1) {
      ☃.handleDisconnect(this);
   }

   public ITextComponent getReason() {
      return this.reason;
   }
}
