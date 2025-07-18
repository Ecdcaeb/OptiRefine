package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketUpdateTileEntity implements Packet<INetHandlerPlayClient> {
   private BlockPos blockPos;
   private int tileEntityType;
   private NBTTagCompound nbt;

   public SPacketUpdateTileEntity() {
   }

   public SPacketUpdateTileEntity(BlockPos var1, int var2, NBTTagCompound var3) {
      this.blockPos = ☃;
      this.tileEntityType = ☃;
      this.nbt = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.blockPos = ☃.readBlockPos();
      this.tileEntityType = ☃.readUnsignedByte();
      this.nbt = ☃.readCompoundTag();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeBlockPos(this.blockPos);
      ☃.writeByte((byte)this.tileEntityType);
      ☃.writeCompoundTag(this.nbt);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleUpdateTileEntity(this);
   }

   public BlockPos getPos() {
      return this.blockPos;
   }

   public int getTileEntityType() {
      return this.tileEntityType;
   }

   public NBTTagCompound getNbtCompound() {
      return this.nbt;
   }
}
