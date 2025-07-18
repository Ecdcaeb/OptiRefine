package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityTeleport implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private double posX;
   private double posY;
   private double posZ;
   private byte yaw;
   private byte pitch;
   private boolean onGround;

   public SPacketEntityTeleport() {
   }

   public SPacketEntityTeleport(Entity var1) {
      this.entityId = ☃.getEntityId();
      this.posX = ☃.posX;
      this.posY = ☃.posY;
      this.posZ = ☃.posZ;
      this.yaw = (byte)(☃.rotationYaw * 256.0F / 360.0F);
      this.pitch = (byte)(☃.rotationPitch * 256.0F / 360.0F);
      this.onGround = ☃.onGround;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
      this.posX = ☃.readDouble();
      this.posY = ☃.readDouble();
      this.posZ = ☃.readDouble();
      this.yaw = ☃.readByte();
      this.pitch = ☃.readByte();
      this.onGround = ☃.readBoolean();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
      ☃.writeDouble(this.posX);
      ☃.writeDouble(this.posY);
      ☃.writeDouble(this.posZ);
      ☃.writeByte(this.yaw);
      ☃.writeByte(this.pitch);
      ☃.writeBoolean(this.onGround);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleEntityTeleport(this);
   }

   public int getEntityId() {
      return this.entityId;
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

   public byte getYaw() {
      return this.yaw;
   }

   public byte getPitch() {
      return this.pitch;
   }

   public boolean getOnGround() {
      return this.onGround;
   }
}
