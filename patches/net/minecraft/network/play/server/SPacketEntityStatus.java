package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class SPacketEntityStatus implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private byte logicOpcode;

   public SPacketEntityStatus() {
   }

   public SPacketEntityStatus(Entity var1, byte var2) {
      this.entityId = ☃.getEntityId();
      this.logicOpcode = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readInt();
      this.logicOpcode = ☃.readByte();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeInt(this.entityId);
      ☃.writeByte(this.logicOpcode);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleEntityStatus(this);
   }

   public Entity getEntity(World var1) {
      return ☃.getEntityByID(this.entityId);
   }

   public byte getOpCode() {
      return this.logicOpcode;
   }
}
