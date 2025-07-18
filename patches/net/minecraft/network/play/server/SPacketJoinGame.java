package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class SPacketJoinGame implements Packet<INetHandlerPlayClient> {
   private int playerId;
   private boolean hardcoreMode;
   private GameType gameType;
   private int dimension;
   private EnumDifficulty difficulty;
   private int maxPlayers;
   private WorldType worldType;
   private boolean reducedDebugInfo;

   public SPacketJoinGame() {
   }

   public SPacketJoinGame(int var1, GameType var2, boolean var3, int var4, EnumDifficulty var5, int var6, WorldType var7, boolean var8) {
      this.playerId = ☃;
      this.dimension = ☃;
      this.difficulty = ☃;
      this.gameType = ☃;
      this.maxPlayers = ☃;
      this.hardcoreMode = ☃;
      this.worldType = ☃;
      this.reducedDebugInfo = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.playerId = ☃.readInt();
      int ☃ = ☃.readUnsignedByte();
      this.hardcoreMode = (☃ & 8) == 8;
      ☃ &= -9;
      this.gameType = GameType.getByID(☃);
      this.dimension = ☃.readInt();
      this.difficulty = EnumDifficulty.byId(☃.readUnsignedByte());
      this.maxPlayers = ☃.readUnsignedByte();
      this.worldType = WorldType.byName(☃.readString(16));
      if (this.worldType == null) {
         this.worldType = WorldType.DEFAULT;
      }

      this.reducedDebugInfo = ☃.readBoolean();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeInt(this.playerId);
      int ☃ = this.gameType.getID();
      if (this.hardcoreMode) {
         ☃ |= 8;
      }

      ☃.writeByte(☃);
      ☃.writeInt(this.dimension);
      ☃.writeByte(this.difficulty.getId());
      ☃.writeByte(this.maxPlayers);
      ☃.writeString(this.worldType.getName());
      ☃.writeBoolean(this.reducedDebugInfo);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleJoinGame(this);
   }

   public int getPlayerId() {
      return this.playerId;
   }

   public boolean isHardcoreMode() {
      return this.hardcoreMode;
   }

   public GameType getGameType() {
      return this.gameType;
   }

   public int getDimension() {
      return this.dimension;
   }

   public EnumDifficulty getDifficulty() {
      return this.difficulty;
   }

   public int getMaxPlayers() {
      return this.maxPlayers;
   }

   public WorldType getWorldType() {
      return this.worldType;
   }

   public boolean isReducedDebugInfo() {
      return this.reducedDebugInfo;
   }
}
