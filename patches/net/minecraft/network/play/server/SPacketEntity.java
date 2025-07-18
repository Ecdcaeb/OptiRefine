package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class SPacketEntity implements Packet<INetHandlerPlayClient> {
   protected int entityId;
   protected int posX;
   protected int posY;
   protected int posZ;
   protected byte yaw;
   protected byte pitch;
   protected boolean onGround;
   protected boolean rotating;

   public SPacketEntity() {
   }

   public SPacketEntity(int var1) {
      this.entityId = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleEntityMovement(this);
   }

   @Override
   public String toString() {
      return "Entity_" + super.toString();
   }

   public Entity getEntity(World var1) {
      return ☃.getEntityByID(this.entityId);
   }

   public int getX() {
      return this.posX;
   }

   public int getY() {
      return this.posY;
   }

   public int getZ() {
      return this.posZ;
   }

   public byte getYaw() {
      return this.yaw;
   }

   public byte getPitch() {
      return this.pitch;
   }

   public boolean isRotating() {
      return this.rotating;
   }

   public boolean getOnGround() {
      return this.onGround;
   }

   public static class S15PacketEntityRelMove extends SPacketEntity {
      public S15PacketEntityRelMove() {
      }

      public S15PacketEntityRelMove(int var1, long var2, long var4, long var6, boolean var8) {
         super(☃);
         this.posX = (int)☃;
         this.posY = (int)☃;
         this.posZ = (int)☃;
         this.onGround = ☃;
      }

      @Override
      public void readPacketData(PacketBuffer var1) throws IOException {
         super.readPacketData(☃);
         this.posX = ☃.readShort();
         this.posY = ☃.readShort();
         this.posZ = ☃.readShort();
         this.onGround = ☃.readBoolean();
      }

      @Override
      public void writePacketData(PacketBuffer var1) throws IOException {
         super.writePacketData(☃);
         ☃.writeShort(this.posX);
         ☃.writeShort(this.posY);
         ☃.writeShort(this.posZ);
         ☃.writeBoolean(this.onGround);
      }
   }

   public static class S16PacketEntityLook extends SPacketEntity {
      public S16PacketEntityLook() {
         this.rotating = true;
      }

      public S16PacketEntityLook(int var1, byte var2, byte var3, boolean var4) {
         super(☃);
         this.yaw = ☃;
         this.pitch = ☃;
         this.rotating = true;
         this.onGround = ☃;
      }

      @Override
      public void readPacketData(PacketBuffer var1) throws IOException {
         super.readPacketData(☃);
         this.yaw = ☃.readByte();
         this.pitch = ☃.readByte();
         this.onGround = ☃.readBoolean();
      }

      @Override
      public void writePacketData(PacketBuffer var1) throws IOException {
         super.writePacketData(☃);
         ☃.writeByte(this.yaw);
         ☃.writeByte(this.pitch);
         ☃.writeBoolean(this.onGround);
      }
   }

   public static class S17PacketEntityLookMove extends SPacketEntity {
      public S17PacketEntityLookMove() {
         this.rotating = true;
      }

      public S17PacketEntityLookMove(int var1, long var2, long var4, long var6, byte var8, byte var9, boolean var10) {
         super(☃);
         this.posX = (int)☃;
         this.posY = (int)☃;
         this.posZ = (int)☃;
         this.yaw = ☃;
         this.pitch = ☃;
         this.onGround = ☃;
         this.rotating = true;
      }

      @Override
      public void readPacketData(PacketBuffer var1) throws IOException {
         super.readPacketData(☃);
         this.posX = ☃.readShort();
         this.posY = ☃.readShort();
         this.posZ = ☃.readShort();
         this.yaw = ☃.readByte();
         this.pitch = ☃.readByte();
         this.onGround = ☃.readBoolean();
      }

      @Override
      public void writePacketData(PacketBuffer var1) throws IOException {
         super.writePacketData(☃);
         ☃.writeShort(this.posX);
         ☃.writeShort(this.posY);
         ☃.writeShort(this.posZ);
         ☃.writeByte(this.yaw);
         ☃.writeByte(this.pitch);
         ☃.writeBoolean(this.onGround);
      }
   }
}
