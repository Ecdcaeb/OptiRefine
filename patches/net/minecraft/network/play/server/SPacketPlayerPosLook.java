package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketPlayerPosLook implements Packet<INetHandlerPlayClient> {
   private double x;
   private double y;
   private double z;
   private float yaw;
   private float pitch;
   private Set<SPacketPlayerPosLook.EnumFlags> flags;
   private int teleportId;

   public SPacketPlayerPosLook() {
   }

   public SPacketPlayerPosLook(double var1, double var3, double var5, float var7, float var8, Set<SPacketPlayerPosLook.EnumFlags> var9, int var10) {
      this.x = ☃;
      this.y = ☃;
      this.z = ☃;
      this.yaw = ☃;
      this.pitch = ☃;
      this.flags = ☃;
      this.teleportId = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.x = ☃.readDouble();
      this.y = ☃.readDouble();
      this.z = ☃.readDouble();
      this.yaw = ☃.readFloat();
      this.pitch = ☃.readFloat();
      this.flags = SPacketPlayerPosLook.EnumFlags.unpack(☃.readUnsignedByte());
      this.teleportId = ☃.readVarInt();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeDouble(this.x);
      ☃.writeDouble(this.y);
      ☃.writeDouble(this.z);
      ☃.writeFloat(this.yaw);
      ☃.writeFloat(this.pitch);
      ☃.writeByte(SPacketPlayerPosLook.EnumFlags.pack(this.flags));
      ☃.writeVarInt(this.teleportId);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handlePlayerPosLook(this);
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

   public int getTeleportId() {
      return this.teleportId;
   }

   public Set<SPacketPlayerPosLook.EnumFlags> getFlags() {
      return this.flags;
   }

   public static enum EnumFlags {
      X(0),
      Y(1),
      Z(2),
      Y_ROT(3),
      X_ROT(4);

      private final int bit;

      private EnumFlags(int var3) {
         this.bit = ☃;
      }

      private int getMask() {
         return 1 << this.bit;
      }

      private boolean isSet(int var1) {
         return (☃ & this.getMask()) == this.getMask();
      }

      public static Set<SPacketPlayerPosLook.EnumFlags> unpack(int var0) {
         Set<SPacketPlayerPosLook.EnumFlags> ☃ = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);

         for (SPacketPlayerPosLook.EnumFlags ☃x : values()) {
            if (☃x.isSet(☃)) {
               ☃.add(☃x);
            }
         }

         return ☃;
      }

      public static int pack(Set<SPacketPlayerPosLook.EnumFlags> var0) {
         int ☃ = 0;

         for (SPacketPlayerPosLook.EnumFlags ☃x : ☃) {
            ☃ |= ☃x.getMask();
         }

         return ☃;
      }
   }
}
