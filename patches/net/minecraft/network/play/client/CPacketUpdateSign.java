package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class CPacketUpdateSign implements Packet<INetHandlerPlayServer> {
   private BlockPos pos;
   private String[] lines;

   public CPacketUpdateSign() {
   }

   public CPacketUpdateSign(BlockPos var1, ITextComponent[] var2) {
      this.pos = ☃;
      this.lines = new String[]{☃[0].getUnformattedText(), ☃[1].getUnformattedText(), ☃[2].getUnformattedText(), ☃[3].getUnformattedText()};
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.pos = ☃.readBlockPos();
      this.lines = new String[4];

      for (int ☃ = 0; ☃ < 4; ☃++) {
         this.lines[☃] = ☃.readString(384);
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeBlockPos(this.pos);

      for (int ☃ = 0; ☃ < 4; ☃++) {
         ☃.writeString(this.lines[☃]);
      }
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processUpdateSign(this);
   }

   public BlockPos getPosition() {
      return this.pos;
   }

   public String[] getLines() {
      return this.lines;
   }
}
