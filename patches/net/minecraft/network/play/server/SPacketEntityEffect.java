package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SPacketEntityEffect implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private byte effectId;
   private byte amplifier;
   private int duration;
   private byte flags;

   public SPacketEntityEffect() {
   }

   public SPacketEntityEffect(int var1, PotionEffect var2) {
      this.entityId = ☃;
      this.effectId = (byte)(Potion.getIdFromPotion(☃.getPotion()) & 0xFF);
      this.amplifier = (byte)(☃.getAmplifier() & 0xFF);
      if (☃.getDuration() > 32767) {
         this.duration = 32767;
      } else {
         this.duration = ☃.getDuration();
      }

      this.flags = 0;
      if (☃.getIsAmbient()) {
         this.flags = (byte)(this.flags | 1);
      }

      if (☃.doesShowParticles()) {
         this.flags = (byte)(this.flags | 2);
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
      this.effectId = ☃.readByte();
      this.amplifier = ☃.readByte();
      this.duration = ☃.readVarInt();
      this.flags = ☃.readByte();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
      ☃.writeByte(this.effectId);
      ☃.writeByte(this.amplifier);
      ☃.writeVarInt(this.duration);
      ☃.writeByte(this.flags);
   }

   public boolean isMaxDuration() {
      return this.duration == 32767;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleEntityEffect(this);
   }

   public int getEntityId() {
      return this.entityId;
   }

   public byte getEffectId() {
      return this.effectId;
   }

   public byte getAmplifier() {
      return this.amplifier;
   }

   public int getDuration() {
      return this.duration;
   }

   public boolean doesShowParticles() {
      return (this.flags & 2) == 2;
   }

   public boolean getIsAmbient() {
      return (this.flags & 1) == 1;
   }
}
