package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnPlayer implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private UUID uniqueId;
   private double x;
   private double y;
   private double z;
   private byte yaw;
   private byte pitch;
   private EntityDataManager watcher;
   private List<EntityDataManager.DataEntry<?>> dataManagerEntries;

   public SPacketSpawnPlayer() {
   }

   public SPacketSpawnPlayer(EntityPlayer var1) {
      this.entityId = ☃.getEntityId();
      this.uniqueId = ☃.getGameProfile().getId();
      this.x = ☃.posX;
      this.y = ☃.posY;
      this.z = ☃.posZ;
      this.yaw = (byte)(☃.rotationYaw * 256.0F / 360.0F);
      this.pitch = (byte)(☃.rotationPitch * 256.0F / 360.0F);
      this.watcher = ☃.getDataManager();
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
      this.uniqueId = ☃.readUniqueId();
      this.x = ☃.readDouble();
      this.y = ☃.readDouble();
      this.z = ☃.readDouble();
      this.yaw = ☃.readByte();
      this.pitch = ☃.readByte();
      this.dataManagerEntries = EntityDataManager.readEntries(☃);
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
      ☃.writeUniqueId(this.uniqueId);
      ☃.writeDouble(this.x);
      ☃.writeDouble(this.y);
      ☃.writeDouble(this.z);
      ☃.writeByte(this.yaw);
      ☃.writeByte(this.pitch);
      this.watcher.writeEntries(☃);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleSpawnPlayer(this);
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

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public byte getYaw() {
      return this.yaw;
   }

   public byte getPitch() {
      return this.pitch;
   }
}
