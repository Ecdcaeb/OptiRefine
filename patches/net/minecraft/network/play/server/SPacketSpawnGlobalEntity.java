package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnGlobalEntity implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private double x;
   private double y;
   private double z;
   private int type;

   public SPacketSpawnGlobalEntity() {
   }

   public SPacketSpawnGlobalEntity(Entity var1) {
      this.entityId = ☃.getEntityId();
      this.x = ☃.posX;
      this.y = ☃.posY;
      this.z = ☃.posZ;
      if (☃ instanceof EntityLightningBolt) {
         this.type = 1;
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
      this.type = ☃.readByte();
      this.x = ☃.readDouble();
      this.y = ☃.readDouble();
      this.z = ☃.readDouble();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
      ☃.writeByte(this.type);
      ☃.writeDouble(this.x);
      ☃.writeDouble(this.y);
      ☃.writeDouble(this.z);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleSpawnGlobalEntity(this);
   }

   public int getEntityId() {
      return this.entityId;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public int getType() {
      return this.type;
   }
}
