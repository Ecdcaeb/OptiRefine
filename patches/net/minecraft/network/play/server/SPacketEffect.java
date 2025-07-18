package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketEffect implements Packet<INetHandlerPlayClient> {
   private int soundType;
   private BlockPos soundPos;
   private int soundData;
   private boolean serverWide;

   public SPacketEffect() {
   }

   public SPacketEffect(int var1, BlockPos var2, int var3, boolean var4) {
      this.soundType = ☃;
      this.soundPos = ☃;
      this.soundData = ☃;
      this.serverWide = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.soundType = ☃.readInt();
      this.soundPos = ☃.readBlockPos();
      this.soundData = ☃.readInt();
      this.serverWide = ☃.readBoolean();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeInt(this.soundType);
      ☃.writeBlockPos(this.soundPos);
      ☃.writeInt(this.soundData);
      ☃.writeBoolean(this.serverWide);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleEffect(this);
   }

   public boolean isSoundServerwide() {
      return this.serverWide;
   }

   public int getSoundType() {
      return this.soundType;
   }

   public int getSoundData() {
      return this.soundData;
   }

   public BlockPos getSoundPos() {
      return this.soundPos;
   }
}
