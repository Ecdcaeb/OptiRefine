package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SPacketBlockChange implements Packet<INetHandlerPlayClient> {
   private BlockPos blockPosition;
   private IBlockState blockState;

   public SPacketBlockChange() {
   }

   public SPacketBlockChange(World var1, BlockPos var2) {
      this.blockPosition = ☃;
      this.blockState = ☃.getBlockState(☃);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.blockPosition = ☃.readBlockPos();
      this.blockState = Block.BLOCK_STATE_IDS.getByValue(☃.readVarInt());
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeBlockPos(this.blockPosition);
      ☃.writeVarInt(Block.BLOCK_STATE_IDS.get(this.blockState));
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleBlockChange(this);
   }

   public IBlockState getBlockState() {
      return this.blockState;
   }

   public BlockPos getBlockPosition() {
      return this.blockPosition;
   }
}
