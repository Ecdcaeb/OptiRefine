package net.minecraft.network.play.client;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.StringUtils;

public class CPacketTabComplete implements Packet<INetHandlerPlayServer> {
   private String message;
   private boolean hasTargetBlock;
   @Nullable
   private BlockPos targetBlock;

   public CPacketTabComplete() {
   }

   public CPacketTabComplete(String var1, @Nullable BlockPos var2, boolean var3) {
      this.message = ☃;
      this.targetBlock = ☃;
      this.hasTargetBlock = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.message = ☃.readString(32767);
      this.hasTargetBlock = ☃.readBoolean();
      boolean ☃ = ☃.readBoolean();
      if (☃) {
         this.targetBlock = ☃.readBlockPos();
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeString(StringUtils.substring(this.message, 0, 32767));
      ☃.writeBoolean(this.hasTargetBlock);
      boolean ☃ = this.targetBlock != null;
      ☃.writeBoolean(☃);
      if (☃) {
         ☃.writeBlockPos(this.targetBlock);
      }
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processTabComplete(this);
   }

   public String getMessage() {
      return this.message;
   }

   @Nullable
   public BlockPos getTargetBlock() {
      return this.targetBlock;
   }

   public boolean hasTargetBlock() {
      return this.hasTargetBlock;
   }
}
