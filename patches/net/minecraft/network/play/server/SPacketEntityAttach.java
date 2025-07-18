package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityAttach implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private int vehicleEntityId;

   public SPacketEntityAttach() {
   }

   public SPacketEntityAttach(Entity var1, @Nullable Entity var2) {
      this.entityId = ☃.getEntityId();
      this.vehicleEntityId = ☃ != null ? ☃.getEntityId() : -1;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readInt();
      this.vehicleEntityId = ☃.readInt();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeInt(this.entityId);
      ☃.writeInt(this.vehicleEntityId);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleEntityAttach(this);
   }

   public int getEntityId() {
      return this.entityId;
   }

   public int getVehicleEntityId() {
      return this.vehicleEntityId;
   }
}
