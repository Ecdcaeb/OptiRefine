package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHand;

public class CPacketPlayerTryUseItem implements Packet<INetHandlerPlayServer> {
   private EnumHand hand;

   public CPacketPlayerTryUseItem() {
   }

   public CPacketPlayerTryUseItem(EnumHand var1) {
      this.hand = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.hand = ☃.readEnumValue(EnumHand.class);
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeEnumValue(this.hand);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processTryUseItem(this);
   }

   public EnumHand getHand() {
      return this.hand;
   }
}
