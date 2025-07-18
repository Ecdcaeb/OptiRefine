package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketChangeGameState implements Packet<INetHandlerPlayClient> {
   public static final String[] MESSAGE_NAMES = new String[]{"tile.bed.notValid"};
   private int state;
   private float value;

   public SPacketChangeGameState() {
   }

   public SPacketChangeGameState(int var1, float var2) {
      this.state = ☃;
      this.value = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.state = ☃.readUnsignedByte();
      this.value = ☃.readFloat();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.state);
      ☃.writeFloat(this.value);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleChangeGameState(this);
   }

   public int getGameState() {
      return this.state;
   }

   public float getValue() {
      return this.value;
   }
}
