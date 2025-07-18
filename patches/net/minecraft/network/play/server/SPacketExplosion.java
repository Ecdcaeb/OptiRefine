package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SPacketExplosion implements Packet<INetHandlerPlayClient> {
   private double posX;
   private double posY;
   private double posZ;
   private float strength;
   private List<BlockPos> affectedBlockPositions;
   private float motionX;
   private float motionY;
   private float motionZ;

   public SPacketExplosion() {
   }

   public SPacketExplosion(double var1, double var3, double var5, float var7, List<BlockPos> var8, Vec3d var9) {
      this.posX = ☃;
      this.posY = ☃;
      this.posZ = ☃;
      this.strength = ☃;
      this.affectedBlockPositions = Lists.newArrayList(☃);
      if (☃ != null) {
         this.motionX = (float)☃.x;
         this.motionY = (float)☃.y;
         this.motionZ = (float)☃.z;
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.posX = ☃.readFloat();
      this.posY = ☃.readFloat();
      this.posZ = ☃.readFloat();
      this.strength = ☃.readFloat();
      int ☃ = ☃.readInt();
      this.affectedBlockPositions = Lists.newArrayListWithCapacity(☃);
      int ☃x = (int)this.posX;
      int ☃xx = (int)this.posY;
      int ☃xxx = (int)this.posZ;

      for (int ☃xxxx = 0; ☃xxxx < ☃; ☃xxxx++) {
         int ☃xxxxx = ☃.readByte() + ☃x;
         int ☃xxxxxx = ☃.readByte() + ☃xx;
         int ☃xxxxxxx = ☃.readByte() + ☃xxx;
         this.affectedBlockPositions.add(new BlockPos(☃xxxxx, ☃xxxxxx, ☃xxxxxxx));
      }

      this.motionX = ☃.readFloat();
      this.motionY = ☃.readFloat();
      this.motionZ = ☃.readFloat();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeFloat((float)this.posX);
      ☃.writeFloat((float)this.posY);
      ☃.writeFloat((float)this.posZ);
      ☃.writeFloat(this.strength);
      ☃.writeInt(this.affectedBlockPositions.size());
      int ☃ = (int)this.posX;
      int ☃x = (int)this.posY;
      int ☃xx = (int)this.posZ;

      for (BlockPos ☃xxx : this.affectedBlockPositions) {
         int ☃xxxx = ☃xxx.getX() - ☃;
         int ☃xxxxx = ☃xxx.getY() - ☃x;
         int ☃xxxxxx = ☃xxx.getZ() - ☃xx;
         ☃.writeByte(☃xxxx);
         ☃.writeByte(☃xxxxx);
         ☃.writeByte(☃xxxxxx);
      }

      ☃.writeFloat(this.motionX);
      ☃.writeFloat(this.motionY);
      ☃.writeFloat(this.motionZ);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleExplosion(this);
   }

   public float getMotionX() {
      return this.motionX;
   }

   public float getMotionY() {
      return this.motionY;
   }

   public float getMotionZ() {
      return this.motionZ;
   }

   public double getX() {
      return this.posX;
   }

   public double getY() {
      return this.posY;
   }

   public double getZ() {
      return this.posZ;
   }

   public float getStrength() {
      return this.strength;
   }

   public List<BlockPos> getAffectedBlockPositions() {
      return this.affectedBlockPositions;
   }
}
