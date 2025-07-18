package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketUpdateHealth implements Packet<INetHandlerPlayClient> {
   private float health;
   private int foodLevel;
   private float saturationLevel;

   public SPacketUpdateHealth() {
   }

   public SPacketUpdateHealth(float var1, int var2, float var3) {
      this.health = ☃;
      this.foodLevel = ☃;
      this.saturationLevel = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.health = ☃.readFloat();
      this.foodLevel = ☃.readVarInt();
      this.saturationLevel = ☃.readFloat();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeFloat(this.health);
      ☃.writeVarInt(this.foodLevel);
      ☃.writeFloat(this.saturationLevel);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleUpdateHealth(this);
   }

   public float getHealth() {
      return this.health;
   }

   public int getFoodLevel() {
      return this.foodLevel;
   }

   public float getSaturationLevel() {
      return this.saturationLevel;
   }
}
