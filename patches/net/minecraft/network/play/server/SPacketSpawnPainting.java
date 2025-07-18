package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.UUID;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class SPacketSpawnPainting implements Packet<INetHandlerPlayClient> {
   private int entityID;
   private UUID uniqueId;
   private BlockPos position;
   private EnumFacing facing;
   private String title;

   public SPacketSpawnPainting() {
   }

   public SPacketSpawnPainting(EntityPainting var1) {
      this.entityID = ☃.getEntityId();
      this.uniqueId = ☃.getUniqueID();
      this.position = ☃.getHangingPosition();
      this.facing = ☃.facingDirection;
      this.title = ☃.art.title;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityID = ☃.readVarInt();
      this.uniqueId = ☃.readUniqueId();
      this.title = ☃.readString(EntityPainting.EnumArt.MAX_NAME_LENGTH);
      this.position = ☃.readBlockPos();
      this.facing = EnumFacing.byHorizontalIndex(☃.readUnsignedByte());
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityID);
      ☃.writeUniqueId(this.uniqueId);
      ☃.writeString(this.title);
      ☃.writeBlockPos(this.position);
      ☃.writeByte(this.facing.getHorizontalIndex());
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleSpawnPainting(this);
   }

   public int getEntityID() {
      return this.entityID;
   }

   public UUID getUniqueId() {
      return this.uniqueId;
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public EnumFacing getFacing() {
      return this.facing;
   }

   public String getTitle() {
      return this.title;
   }
}
