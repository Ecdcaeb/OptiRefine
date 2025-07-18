package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;

public class SPacketServerDifficulty implements Packet<INetHandlerPlayClient> {
   private EnumDifficulty difficulty;
   private boolean difficultyLocked;

   public SPacketServerDifficulty() {
   }

   public SPacketServerDifficulty(EnumDifficulty var1, boolean var2) {
      this.difficulty = ☃;
      this.difficultyLocked = ☃;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleServerDifficulty(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.difficulty = EnumDifficulty.byId(☃.readUnsignedByte());
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.difficulty.getId());
   }

   public boolean isDifficultyLocked() {
      return this.difficultyLocked;
   }

   public EnumDifficulty getDifficulty() {
      return this.difficulty;
   }
}
