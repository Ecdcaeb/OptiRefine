package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketVehicleMove implements Packet<INetHandlerPlayServer> {
   private double x;
   private double y;
   private double z;
   private float yaw;
   private float pitch;

   public CPacketVehicleMove() {
   }

   public CPacketVehicleMove(Entity var1) {
      this.x = ☃.posX;
      this.y = ☃.posY;
      this.z = ☃.posZ;
      this.yaw = ☃.rotationYaw;
      this.pitch = ☃.rotationPitch;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.x = ☃.readDouble();
      this.y = ☃.readDouble();
      this.z = ☃.readDouble();
      this.yaw = ☃.readFloat();
      this.pitch = ☃.readFloat();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeDouble(this.x);
      ☃.writeDouble(this.y);
      ☃.writeDouble(this.z);
      ☃.writeFloat(this.yaw);
      ☃.writeFloat(this.pitch);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processVehicleMove(this);
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

   public float getYaw() {
      return this.yaw;
   }

   public float getPitch() {
      return this.pitch;
   }
}
