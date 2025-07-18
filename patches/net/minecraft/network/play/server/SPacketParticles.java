package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.EnumParticleTypes;

public class SPacketParticles implements Packet<INetHandlerPlayClient> {
   private EnumParticleTypes particleType;
   private float xCoord;
   private float yCoord;
   private float zCoord;
   private float xOffset;
   private float yOffset;
   private float zOffset;
   private float particleSpeed;
   private int particleCount;
   private boolean longDistance;
   private int[] particleArguments;

   public SPacketParticles() {
   }

   public SPacketParticles(
      EnumParticleTypes var1, boolean var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10, int... var11
   ) {
      this.particleType = ☃;
      this.longDistance = ☃;
      this.xCoord = ☃;
      this.yCoord = ☃;
      this.zCoord = ☃;
      this.xOffset = ☃;
      this.yOffset = ☃;
      this.zOffset = ☃;
      this.particleSpeed = ☃;
      this.particleCount = ☃;
      this.particleArguments = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.particleType = EnumParticleTypes.getParticleFromId(☃.readInt());
      if (this.particleType == null) {
         this.particleType = EnumParticleTypes.BARRIER;
      }

      this.longDistance = ☃.readBoolean();
      this.xCoord = ☃.readFloat();
      this.yCoord = ☃.readFloat();
      this.zCoord = ☃.readFloat();
      this.xOffset = ☃.readFloat();
      this.yOffset = ☃.readFloat();
      this.zOffset = ☃.readFloat();
      this.particleSpeed = ☃.readFloat();
      this.particleCount = ☃.readInt();
      int ☃ = this.particleType.getArgumentCount();
      this.particleArguments = new int[☃];

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         this.particleArguments[☃x] = ☃.readVarInt();
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeInt(this.particleType.getParticleID());
      ☃.writeBoolean(this.longDistance);
      ☃.writeFloat(this.xCoord);
      ☃.writeFloat(this.yCoord);
      ☃.writeFloat(this.zCoord);
      ☃.writeFloat(this.xOffset);
      ☃.writeFloat(this.yOffset);
      ☃.writeFloat(this.zOffset);
      ☃.writeFloat(this.particleSpeed);
      ☃.writeInt(this.particleCount);
      int ☃ = this.particleType.getArgumentCount();

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         ☃.writeVarInt(this.particleArguments[☃x]);
      }
   }

   public EnumParticleTypes getParticleType() {
      return this.particleType;
   }

   public boolean isLongDistance() {
      return this.longDistance;
   }

   public double getXCoordinate() {
      return this.xCoord;
   }

   public double getYCoordinate() {
      return this.yCoord;
   }

   public double getZCoordinate() {
      return this.zCoord;
   }

   public float getXOffset() {
      return this.xOffset;
   }

   public float getYOffset() {
      return this.yOffset;
   }

   public float getZOffset() {
      return this.zOffset;
   }

   public float getParticleSpeed() {
      return this.particleSpeed;
   }

   public int getParticleCount() {
      return this.particleCount;
   }

   public int[] getParticleArgs() {
      return this.particleArguments;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleParticles(this);
   }
}
