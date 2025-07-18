package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnExperienceOrb implements Packet<INetHandlerPlayClient> {
   private int entityID;
   private double posX;
   private double posY;
   private double posZ;
   private int xpValue;

   public SPacketSpawnExperienceOrb() {
   }

   public SPacketSpawnExperienceOrb(EntityXPOrb var1) {
      this.entityID = ☃.getEntityId();
      this.posX = ☃.posX;
      this.posY = ☃.posY;
      this.posZ = ☃.posZ;
      this.xpValue = ☃.getXpValue();
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityID = ☃.readVarInt();
      this.posX = ☃.readDouble();
      this.posY = ☃.readDouble();
      this.posZ = ☃.readDouble();
      this.xpValue = ☃.readShort();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityID);
      ☃.writeDouble(this.posX);
      ☃.writeDouble(this.posY);
      ☃.writeDouble(this.posZ);
      ☃.writeShort(this.xpValue);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleSpawnExperienceOrb(this);
   }

   public int getEntityID() {
      return this.entityID;
   }

   public double getX() {
      return this.posX;
   }

   public double getY() {
      return this.posY;
   }

   public double getZ() {
      return this.posZ;
   }

   public int getXPValue() {
      return this.xpValue;
   }
}
