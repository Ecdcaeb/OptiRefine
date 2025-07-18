package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.SoundCategory;
import org.apache.commons.lang3.Validate;

public class SPacketCustomSound implements Packet<INetHandlerPlayClient> {
   private String soundName;
   private SoundCategory category;
   private int x;
   private int y = Integer.MAX_VALUE;
   private int z;
   private float volume;
   private float pitch;

   public SPacketCustomSound() {
   }

   public SPacketCustomSound(String var1, SoundCategory var2, double var3, double var5, double var7, float var9, float var10) {
      Validate.notNull(☃, "name", new Object[0]);
      this.soundName = ☃;
      this.category = ☃;
      this.x = (int)(☃ * 8.0);
      this.y = (int)(☃ * 8.0);
      this.z = (int)(☃ * 8.0);
      this.volume = ☃;
      this.pitch = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.soundName = ☃.readString(256);
      this.category = ☃.readEnumValue(SoundCategory.class);
      this.x = ☃.readInt();
      this.y = ☃.readInt();
      this.z = ☃.readInt();
      this.volume = ☃.readFloat();
      this.pitch = ☃.readFloat();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeString(this.soundName);
      ☃.writeEnumValue(this.category);
      ☃.writeInt(this.x);
      ☃.writeInt(this.y);
      ☃.writeInt(this.z);
      ☃.writeFloat(this.volume);
      ☃.writeFloat(this.pitch);
   }

   public String getSoundName() {
      return this.soundName;
   }

   public SoundCategory getCategory() {
      return this.category;
   }

   public double getX() {
      return this.x / 8.0F;
   }

   public double getY() {
      return this.y / 8.0F;
   }

   public double getZ() {
      return this.z / 8.0F;
   }

   public float getVolume() {
      return this.volume;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleCustomSound(this);
   }
}
