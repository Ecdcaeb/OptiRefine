package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketDestroyEntities implements Packet<INetHandlerPlayClient> {
   private int[] entityIDs;

   public SPacketDestroyEntities() {
   }

   public SPacketDestroyEntities(int... var1) {
      this.entityIDs = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityIDs = new int[☃.readVarInt()];

      for (int ☃ = 0; ☃ < this.entityIDs.length; ☃++) {
         this.entityIDs[☃] = ☃.readVarInt();
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityIDs.length);

      for (int ☃ : this.entityIDs) {
         ☃.writeVarInt(☃);
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleDestroyEntities(this);
   }

   public int[] getEntityIDs() {
      return this.entityIDs;
   }
}
