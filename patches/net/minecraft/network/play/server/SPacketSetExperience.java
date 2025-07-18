package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetExperience implements Packet<INetHandlerPlayClient> {
   private float experienceBar;
   private int totalExperience;
   private int level;

   public SPacketSetExperience() {
   }

   public SPacketSetExperience(float var1, int var2, int var3) {
      this.experienceBar = ☃;
      this.totalExperience = ☃;
      this.level = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.experienceBar = ☃.readFloat();
      this.level = ☃.readVarInt();
      this.totalExperience = ☃.readVarInt();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeFloat(this.experienceBar);
      ☃.writeVarInt(this.level);
      ☃.writeVarInt(this.totalExperience);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleSetExperience(this);
   }

   public float getExperienceBar() {
      return this.experienceBar;
   }

   public int getTotalExperience() {
      return this.totalExperience;
   }

   public int getLevel() {
      return this.level;
   }
}
