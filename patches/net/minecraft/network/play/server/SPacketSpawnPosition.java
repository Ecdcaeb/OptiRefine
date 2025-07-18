package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketSpawnPosition implements Packet<INetHandlerPlayClient> {
   private BlockPos spawnBlockPos;

   public SPacketSpawnPosition() {
   }

   public SPacketSpawnPosition(BlockPos var1) {
      this.spawnBlockPos = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.spawnBlockPos = ☃.readBlockPos();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeBlockPos(this.spawnBlockPos);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleSpawnPosition(this);
   }

   public BlockPos getSpawnPos() {
      return this.spawnBlockPos;
   }
}
