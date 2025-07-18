package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SPacketSpawnObject implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private UUID uniqueId;
   private double x;
   private double y;
   private double z;
   private int speedX;
   private int speedY;
   private int speedZ;
   private int pitch;
   private int yaw;
   private int type;
   private int data;

   public SPacketSpawnObject() {
   }

   public SPacketSpawnObject(Entity var1, int var2) {
      this(☃, ☃, 0);
   }

   public SPacketSpawnObject(Entity var1, int var2, int var3) {
      this.entityId = ☃.getEntityId();
      this.uniqueId = ☃.getUniqueID();
      this.x = ☃.posX;
      this.y = ☃.posY;
      this.z = ☃.posZ;
      this.pitch = MathHelper.floor(☃.rotationPitch * 256.0F / 360.0F);
      this.yaw = MathHelper.floor(☃.rotationYaw * 256.0F / 360.0F);
      this.type = ☃;
      this.data = ☃;
      double ☃ = 3.9;
      this.speedX = (int)(MathHelper.clamp(☃.motionX, -3.9, 3.9) * 8000.0);
      this.speedY = (int)(MathHelper.clamp(☃.motionY, -3.9, 3.9) * 8000.0);
      this.speedZ = (int)(MathHelper.clamp(☃.motionZ, -3.9, 3.9) * 8000.0);
   }

   public SPacketSpawnObject(Entity var1, int var2, int var3, BlockPos var4) {
      this(☃, ☃, ☃);
      this.x = ☃.getX();
      this.y = ☃.getY();
      this.z = ☃.getZ();
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
      this.uniqueId = ☃.readUniqueId();
      this.type = ☃.readByte();
      this.x = ☃.readDouble();
      this.y = ☃.readDouble();
      this.z = ☃.readDouble();
      this.pitch = ☃.readByte();
      this.yaw = ☃.readByte();
      this.data = ☃.readInt();
      this.speedX = ☃.readShort();
      this.speedY = ☃.readShort();
      this.speedZ = ☃.readShort();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
      ☃.writeUniqueId(this.uniqueId);
      ☃.writeByte(this.type);
      ☃.writeDouble(this.x);
      ☃.writeDouble(this.y);
      ☃.writeDouble(this.z);
      ☃.writeByte(this.pitch);
      ☃.writeByte(this.yaw);
      ☃.writeInt(this.data);
      ☃.writeShort(this.speedX);
      ☃.writeShort(this.speedY);
      ☃.writeShort(this.speedZ);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleSpawnObject(this);
   }

   public int getEntityID() {
      return this.entityId;
   }

   public UUID getUniqueId() {
      return this.uniqueId;
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

   public int getSpeedX() {
      return this.speedX;
   }

   public int getSpeedY() {
      return this.speedY;
   }

   public int getSpeedZ() {
      return this.speedZ;
   }

   public int getPitch() {
      return this.pitch;
   }

   public int getYaw() {
      return this.yaw;
   }

   public int getType() {
      return this.type;
   }

   public int getData() {
      return this.data;
   }

   public void setSpeedX(int var1) {
      this.speedX = ☃;
   }

   public void setSpeedY(int var1) {
      this.speedY = ☃;
   }

   public void setSpeedZ(int var1) {
      this.speedZ = ☃;
   }

   public void setData(int var1) {
      this.data = ☃;
   }
}
