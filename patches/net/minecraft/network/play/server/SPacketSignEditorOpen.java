package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketSignEditorOpen implements Packet<INetHandlerPlayClient> {
   private BlockPos signPosition;

   public SPacketSignEditorOpen() {
   }

   public SPacketSignEditorOpen(BlockPos var1) {
      this.signPosition = ☃;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleSignEditorOpen(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.signPosition = ☃.readBlockPos();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeBlockPos(this.signPosition);
   }

   public BlockPos getSignPosition() {
      return this.signPosition;
   }
}
