package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketAnimation implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private int type;

   public SPacketAnimation() {
   }

   public SPacketAnimation(Entity var1, int var2) {
      this.entityId = ☃.getEntityId();
      this.type = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
      this.type = ☃.readUnsignedByte();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
      ☃.writeByte(this.type);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleAnimation(this);
   }

   public int getEntityID() {
      return this.entityId;
   }

   public int getAnimationType() {
      return this.type;
   }
}
