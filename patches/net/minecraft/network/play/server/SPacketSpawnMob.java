package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnMob implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private UUID uniqueId;
   private int type;
   private double x;
   private double y;
   private double z;
   private int velocityX;
   private int velocityY;
   private int velocityZ;
   private byte yaw;
   private byte pitch;
   private byte headPitch;
   private EntityDataManager dataManager;
   private List<EntityDataManager.DataEntry<?>> dataManagerEntries;

   public SPacketSpawnMob() {
   }

   public SPacketSpawnMob(EntityLivingBase var1) {
      this.entityId = ☃.getEntityId();
      this.uniqueId = ☃.getUniqueID();
      this.type = EntityList.REGISTRY.getIDForObject((Class<? extends Entity>)☃.getClass());
      this.x = ☃.posX;
      this.y = ☃.posY;
      this.z = ☃.posZ;
      this.yaw = (byte)(☃.rotationYaw * 256.0F / 360.0F);
      this.pitch = (byte)(☃.rotationPitch * 256.0F / 360.0F);
      this.headPitch = (byte)(☃.rotationYawHead * 256.0F / 360.0F);
      double ☃ = 3.9;
      double ☃x = ☃.motionX;
      double ☃xx = ☃.motionY;
      double ☃xxx = ☃.motionZ;
      if (☃x < -3.9) {
         ☃x = -3.9;
      }

      if (☃xx < -3.9) {
         ☃xx = -3.9;
      }

      if (☃xxx < -3.9) {
         ☃xxx = -3.9;
      }

      if (☃x > 3.9) {
         ☃x = 3.9;
      }

      if (☃xx > 3.9) {
         ☃xx = 3.9;
      }

      if (☃xxx > 3.9) {
         ☃xxx = 3.9;
      }

      this.velocityX = (int)(☃x * 8000.0);
      this.velocityY = (int)(☃xx * 8000.0);
      this.velocityZ = (int)(☃xxx * 8000.0);
      this.dataManager = ☃.getDataManager();
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
      this.uniqueId = ☃.readUniqueId();
      this.type = ☃.readVarInt();
      this.x = ☃.readDouble();
      this.y = ☃.readDouble();
      this.z = ☃.readDouble();
      this.yaw = ☃.readByte();
      this.pitch = ☃.readByte();
      this.headPitch = ☃.readByte();
      this.velocityX = ☃.readShort();
      this.velocityY = ☃.readShort();
      this.velocityZ = ☃.readShort();
      this.dataManagerEntries = EntityDataManager.readEntries(☃);
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
      ☃.writeUniqueId(this.uniqueId);
      ☃.writeVarInt(this.type);
      ☃.writeDouble(this.x);
      ☃.writeDouble(this.y);
      ☃.writeDouble(this.z);
      ☃.writeByte(this.yaw);
      ☃.writeByte(this.pitch);
      ☃.writeByte(this.headPitch);
      ☃.writeShort(this.velocityX);
      ☃.writeShort(this.velocityY);
      ☃.writeShort(this.velocityZ);
      this.dataManager.writeEntries(☃);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleSpawnMob(this);
   }

   @Nullable
   public List<EntityDataManager.DataEntry<?>> getDataManagerEntries() {
      return this.dataManagerEntries;
   }

   public int getEntityID() {
      return this.entityId;
   }

   public UUID getUniqueId() {
      return this.uniqueId;
   }

   public int getEntityType() {
      return this.type;
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

   public int getVelocityX() {
      return this.velocityX;
   }

   public int getVelocityY() {
      return this.velocityY;
   }

   public int getVelocityZ() {
      return this.velocityZ;
   }

   public byte getYaw() {
      return this.yaw;
   }

   public byte getPitch() {
      return this.pitch;
   }

   public byte getHeadPitch() {
      return this.headPitch;
   }
}
