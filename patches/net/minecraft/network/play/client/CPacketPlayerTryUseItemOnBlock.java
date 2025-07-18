package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class CPacketPlayerTryUseItemOnBlock implements Packet<INetHandlerPlayServer> {
   private BlockPos position;
   private EnumFacing placedBlockDirection;
   private EnumHand hand;
   private float facingX;
   private float facingY;
   private float facingZ;

   public CPacketPlayerTryUseItemOnBlock() {
   }

   public CPacketPlayerTryUseItemOnBlock(BlockPos var1, EnumFacing var2, EnumHand var3, float var4, float var5, float var6) {
      this.position = ☃;
      this.placedBlockDirection = ☃;
      this.hand = ☃;
      this.facingX = ☃;
      this.facingY = ☃;
      this.facingZ = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.position = ☃.readBlockPos();
      this.placedBlockDirection = ☃.readEnumValue(EnumFacing.class);
      this.hand = ☃.readEnumValue(EnumHand.class);
      this.facingX = ☃.readFloat();
      this.facingY = ☃.readFloat();
      this.facingZ = ☃.readFloat();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeBlockPos(this.position);
      ☃.writeEnumValue(this.placedBlockDirection);
      ☃.writeEnumValue(this.hand);
      ☃.writeFloat(this.facingX);
      ☃.writeFloat(this.facingY);
      ☃.writeFloat(this.facingZ);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processTryUseItemOnBlock(this);
   }

   public BlockPos getPos() {
      return this.position;
   }

   public EnumFacing getDirection() {
      return this.placedBlockDirection;
   }

   public EnumHand getHand() {
      return this.hand;
   }

   public float getFacingX() {
      return this.facingX;
   }

   public float getFacingY() {
      return this.facingY;
   }

   public float getFacingZ() {
      return this.facingZ;
   }
}
