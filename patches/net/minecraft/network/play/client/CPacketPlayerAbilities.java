package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketPlayerAbilities implements Packet<INetHandlerPlayServer> {
   private boolean invulnerable;
   private boolean flying;
   private boolean allowFlying;
   private boolean creativeMode;
   private float flySpeed;
   private float walkSpeed;

   public CPacketPlayerAbilities() {
   }

   public CPacketPlayerAbilities(PlayerCapabilities var1) {
      this.setInvulnerable(☃.disableDamage);
      this.setFlying(☃.isFlying);
      this.setAllowFlying(☃.allowFlying);
      this.setCreativeMode(☃.isCreativeMode);
      this.setFlySpeed(☃.getFlySpeed());
      this.setWalkSpeed(☃.getWalkSpeed());
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      byte ☃ = ☃.readByte();
      this.setInvulnerable((☃ & 1) > 0);
      this.setFlying((☃ & 2) > 0);
      this.setAllowFlying((☃ & 4) > 0);
      this.setCreativeMode((☃ & 8) > 0);
      this.setFlySpeed(☃.readFloat());
      this.setWalkSpeed(☃.readFloat());
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      byte ☃ = 0;
      if (this.isInvulnerable()) {
         ☃ = (byte)(☃ | 1);
      }

      if (this.isFlying()) {
         ☃ = (byte)(☃ | 2);
      }

      if (this.isAllowFlying()) {
         ☃ = (byte)(☃ | 4);
      }

      if (this.isCreativeMode()) {
         ☃ = (byte)(☃ | 8);
      }

      ☃.writeByte(☃);
      ☃.writeFloat(this.flySpeed);
      ☃.writeFloat(this.walkSpeed);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processPlayerAbilities(this);
   }

   public boolean isInvulnerable() {
      return this.invulnerable;
   }

   public void setInvulnerable(boolean var1) {
      this.invulnerable = ☃;
   }

   public boolean isFlying() {
      return this.flying;
   }

   public void setFlying(boolean var1) {
      this.flying = ☃;
   }

   public boolean isAllowFlying() {
      return this.allowFlying;
   }

   public void setAllowFlying(boolean var1) {
      this.allowFlying = ☃;
   }

   public boolean isCreativeMode() {
      return this.creativeMode;
   }

   public void setCreativeMode(boolean var1) {
      this.creativeMode = ☃;
   }

   public void setFlySpeed(float var1) {
      this.flySpeed = ☃;
   }

   public void setWalkSpeed(float var1) {
      this.walkSpeed = ☃;
   }
}
