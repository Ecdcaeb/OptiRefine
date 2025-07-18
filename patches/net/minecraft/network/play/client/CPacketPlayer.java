package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketPlayer implements Packet<INetHandlerPlayServer> {
   protected double x;
   protected double y;
   protected double z;
   protected float yaw;
   protected float pitch;
   protected boolean onGround;
   protected boolean moving;
   protected boolean rotating;

   public CPacketPlayer() {
   }

   public CPacketPlayer(boolean var1) {
      this.onGround = ☃;
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processPlayer(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.onGround = ☃.readUnsignedByte() != 0;
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.onGround ? 1 : 0);
   }

   public double getX(double var1) {
      return this.moving ? this.x : ☃;
   }

   public double getY(double var1) {
      return this.moving ? this.y : ☃;
   }

   public double getZ(double var1) {
      return this.moving ? this.z : ☃;
   }

   public float getYaw(float var1) {
      return this.rotating ? this.yaw : ☃;
   }

   public float getPitch(float var1) {
      return this.rotating ? this.pitch : ☃;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public static class Position extends CPacketPlayer {
      public Position() {
         this.moving = true;
      }

      public Position(double var1, double var3, double var5, boolean var7) {
         this.x = ☃;
         this.y = ☃;
         this.z = ☃;
         this.onGround = ☃;
         this.moving = true;
      }

      @Override
      public void readPacketData(PacketBuffer var1) throws IOException {
         this.x = ☃.readDouble();
         this.y = ☃.readDouble();
         this.z = ☃.readDouble();
         super.readPacketData(☃);
      }

      @Override
      public void writePacketData(PacketBuffer var1) throws IOException {
         ☃.writeDouble(this.x);
         ☃.writeDouble(this.y);
         ☃.writeDouble(this.z);
         super.writePacketData(☃);
      }
   }

   public static class PositionRotation extends CPacketPlayer {
      public PositionRotation() {
         this.moving = true;
         this.rotating = true;
      }

      public PositionRotation(double var1, double var3, double var5, float var7, float var8, boolean var9) {
         this.x = ☃;
         this.y = ☃;
         this.z = ☃;
         this.yaw = ☃;
         this.pitch = ☃;
         this.onGround = ☃;
         this.rotating = true;
         this.moving = true;
      }

      @Override
      public void readPacketData(PacketBuffer var1) throws IOException {
         this.x = ☃.readDouble();
         this.y = ☃.readDouble();
         this.z = ☃.readDouble();
         this.yaw = ☃.readFloat();
         this.pitch = ☃.readFloat();
         super.readPacketData(☃);
      }

      @Override
      public void writePacketData(PacketBuffer var1) throws IOException {
         ☃.writeDouble(this.x);
         ☃.writeDouble(this.y);
         ☃.writeDouble(this.z);
         ☃.writeFloat(this.yaw);
         ☃.writeFloat(this.pitch);
         super.writePacketData(☃);
      }
   }

   public static class Rotation extends CPacketPlayer {
      public Rotation() {
         this.rotating = true;
      }

      public Rotation(float var1, float var2, boolean var3) {
         this.yaw = ☃;
         this.pitch = ☃;
         this.onGround = ☃;
         this.rotating = true;
      }

      @Override
      public void readPacketData(PacketBuffer var1) throws IOException {
         this.yaw = ☃.readFloat();
         this.pitch = ☃.readFloat();
         super.readPacketData(☃);
      }

      @Override
      public void writePacketData(PacketBuffer var1) throws IOException {
         ☃.writeFloat(this.yaw);
         ☃.writeFloat(this.pitch);
         super.writePacketData(☃);
      }
   }
}
