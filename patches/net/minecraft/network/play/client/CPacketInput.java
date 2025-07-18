package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketInput implements Packet<INetHandlerPlayServer> {
   private float strafeSpeed;
   private float forwardSpeed;
   private boolean jumping;
   private boolean sneaking;

   public CPacketInput() {
   }

   public CPacketInput(float var1, float var2, boolean var3, boolean var4) {
      this.strafeSpeed = ☃;
      this.forwardSpeed = ☃;
      this.jumping = ☃;
      this.sneaking = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.strafeSpeed = ☃.readFloat();
      this.forwardSpeed = ☃.readFloat();
      byte ☃ = ☃.readByte();
      this.jumping = (☃ & 1) > 0;
      this.sneaking = (☃ & 2) > 0;
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeFloat(this.strafeSpeed);
      ☃.writeFloat(this.forwardSpeed);
      byte ☃ = 0;
      if (this.jumping) {
         ☃ = (byte)(☃ | 1);
      }

      if (this.sneaking) {
         ☃ = (byte)(☃ | 2);
      }

      ☃.writeByte(☃);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processInput(this);
   }

   public float getStrafeSpeed() {
      return this.strafeSpeed;
   }

   public float getForwardSpeed() {
      return this.forwardSpeed;
   }

   public boolean isJumping() {
      return this.jumping;
   }

   public boolean isSneaking() {
      return this.sneaking;
   }
}
