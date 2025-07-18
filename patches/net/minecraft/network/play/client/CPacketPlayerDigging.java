package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CPacketPlayerDigging implements Packet<INetHandlerPlayServer> {
   private BlockPos position;
   private EnumFacing facing;
   private CPacketPlayerDigging.Action action;

   public CPacketPlayerDigging() {
   }

   public CPacketPlayerDigging(CPacketPlayerDigging.Action var1, BlockPos var2, EnumFacing var3) {
      this.action = ☃;
      this.position = ☃;
      this.facing = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.action = ☃.readEnumValue(CPacketPlayerDigging.Action.class);
      this.position = ☃.readBlockPos();
      this.facing = EnumFacing.byIndex(☃.readUnsignedByte());
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeEnumValue(this.action);
      ☃.writeBlockPos(this.position);
      ☃.writeByte(this.facing.getIndex());
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processPlayerDigging(this);
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public EnumFacing getFacing() {
      return this.facing;
   }

   public CPacketPlayerDigging.Action getAction() {
      return this.action;
   }

   public static enum Action {
      START_DESTROY_BLOCK,
      ABORT_DESTROY_BLOCK,
      STOP_DESTROY_BLOCK,
      DROP_ALL_ITEMS,
      DROP_ITEM,
      RELEASE_USE_ITEM,
      SWAP_HELD_ITEMS;
   }
}
