package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import org.apache.commons.lang3.Validate;

public class SPacketSoundEffect implements Packet<INetHandlerPlayClient> {
   private SoundEvent sound;
   private SoundCategory category;
   private int posX;
   private int posY;
   private int posZ;
   private float soundVolume;
   private float soundPitch;

   public SPacketSoundEffect() {
   }

   public SPacketSoundEffect(SoundEvent var1, SoundCategory var2, double var3, double var5, double var7, float var9, float var10) {
      Validate.notNull(☃, "sound", new Object[0]);
      this.sound = ☃;
      this.category = ☃;
      this.posX = (int)(☃ * 8.0);
      this.posY = (int)(☃ * 8.0);
      this.posZ = (int)(☃ * 8.0);
      this.soundVolume = ☃;
      this.soundPitch = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.sound = SoundEvent.REGISTRY.getObjectById(☃.readVarInt());
      this.category = ☃.readEnumValue(SoundCategory.class);
      this.posX = ☃.readInt();
      this.posY = ☃.readInt();
      this.posZ = ☃.readInt();
      this.soundVolume = ☃.readFloat();
      this.soundPitch = ☃.readFloat();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(SoundEvent.REGISTRY.getIDForObject(this.sound));
      ☃.writeEnumValue(this.category);
      ☃.writeInt(this.posX);
      ☃.writeInt(this.posY);
      ☃.writeInt(this.posZ);
      ☃.writeFloat(this.soundVolume);
      ☃.writeFloat(this.soundPitch);
   }

   public SoundEvent getSound() {
      return this.sound;
   }

   public SoundCategory getCategory() {
      return this.category;
   }

   public double getX() {
      return this.posX / 8.0F;
   }

   public double getY() {
      return this.posY / 8.0F;
   }

   public double getZ() {
      return this.posZ / 8.0F;
   }

   public float getVolume() {
      return this.soundVolume;
   }

   public float getPitch() {
      return this.soundPitch;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleSoundEffect(this);
   }
}
