package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class SPacketEntityHeadLook implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private byte yaw;

   public SPacketEntityHeadLook() {
   }

   public SPacketEntityHeadLook(Entity var1, byte var2) {
      this.entityId = ☃.getEntityId();
      this.yaw = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
      this.yaw = ☃.readByte();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
      ☃.writeByte(this.yaw);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleEntityHeadLook(this);
   }

   public Entity getEntity(World var1) {
      return ☃.getEntityByID(this.entityId);
   }

   public byte getYaw() {
      return this.yaw;
   }
}
