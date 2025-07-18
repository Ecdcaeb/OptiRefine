package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetPassengers implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private int[] passengerIds;

   public SPacketSetPassengers() {
   }

   public SPacketSetPassengers(Entity var1) {
      this.entityId = ☃.getEntityId();
      List<Entity> ☃ = ☃.getPassengers();
      this.passengerIds = new int[☃.size()];

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         this.passengerIds[☃x] = ☃.get(☃x).getEntityId();
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
      this.passengerIds = ☃.readVarIntArray();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
      ☃.writeVarIntArray(this.passengerIds);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleSetPassengers(this);
   }

   public int[] getPassengerIds() {
      return this.passengerIds;
   }

   public int getEntityId() {
      return this.entityId;
   }
}
