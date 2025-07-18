package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityVelocity implements Packet<INetHandlerPlayClient> {
   private int entityID;
   private int motionX;
   private int motionY;
   private int motionZ;

   public SPacketEntityVelocity() {
   }

   public SPacketEntityVelocity(Entity var1) {
      this(☃.getEntityId(), ☃.motionX, ☃.motionY, ☃.motionZ);
   }

   public SPacketEntityVelocity(int var1, double var2, double var4, double var6) {
      this.entityID = ☃;
      double ☃ = 3.9;
      if (☃ < -3.9) {
         ☃ = -3.9;
      }

      if (☃ < -3.9) {
         ☃ = -3.9;
      }

      if (☃ < -3.9) {
         ☃ = -3.9;
      }

      if (☃ > 3.9) {
         ☃ = 3.9;
      }

      if (☃ > 3.9) {
         ☃ = 3.9;
      }

      if (☃ > 3.9) {
         ☃ = 3.9;
      }

      this.motionX = (int)(☃ * 8000.0);
      this.motionY = (int)(☃ * 8000.0);
      this.motionZ = (int)(☃ * 8000.0);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityID = ☃.readVarInt();
      this.motionX = ☃.readShort();
      this.motionY = ☃.readShort();
      this.motionZ = ☃.readShort();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityID);
      ☃.writeShort(this.motionX);
      ☃.writeShort(this.motionY);
      ☃.writeShort(this.motionZ);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleEntityVelocity(this);
   }

   public int getEntityID() {
      return this.entityID;
   }

   public int getMotionX() {
      return this.motionX;
   }

   public int getMotionY() {
      return this.motionY;
   }

   public int getMotionZ() {
      return this.motionZ;
   }
}
