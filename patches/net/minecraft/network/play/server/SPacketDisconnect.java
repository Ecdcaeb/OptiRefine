package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketDisconnect implements Packet<INetHandlerPlayClient> {
   private ITextComponent reason;

   public SPacketDisconnect() {
   }

   public SPacketDisconnect(ITextComponent var1) {
      this.reason = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.reason = ☃.readTextComponent();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeTextComponent(this.reason);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleDisconnect(this);
   }

   public ITextComponent getReason() {
      return this.reason;
   }
}
