package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SPacketUseBed implements Packet<INetHandlerPlayClient> {
   private int playerID;
   private BlockPos bedPos;

   public SPacketUseBed() {
   }

   public SPacketUseBed(EntityPlayer var1, BlockPos var2) {
      this.playerID = ☃.getEntityId();
      this.bedPos = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.playerID = ☃.readVarInt();
      this.bedPos = ☃.readBlockPos();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.playerID);
      ☃.writeBlockPos(this.bedPos);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleUseBed(this);
   }

   public EntityPlayer getPlayer(World var1) {
      return (EntityPlayer)☃.getEntityByID(this.playerID);
   }

   public BlockPos getBedPosition() {
      return this.bedPos;
   }
}
