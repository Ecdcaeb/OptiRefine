package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class SPacketRespawn implements Packet<INetHandlerPlayClient> {
   private int dimensionID;
   private EnumDifficulty difficulty;
   private GameType gameType;
   private WorldType worldType;

   public SPacketRespawn() {
   }

   public SPacketRespawn(int var1, EnumDifficulty var2, WorldType var3, GameType var4) {
      this.dimensionID = ☃;
      this.difficulty = ☃;
      this.gameType = ☃;
      this.worldType = ☃;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleRespawn(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.dimensionID = ☃.readInt();
      this.difficulty = EnumDifficulty.byId(☃.readUnsignedByte());
      this.gameType = GameType.getByID(☃.readUnsignedByte());
      this.worldType = WorldType.byName(☃.readString(16));
      if (this.worldType == null) {
         this.worldType = WorldType.DEFAULT;
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeInt(this.dimensionID);
      ☃.writeByte(this.difficulty.getId());
      ☃.writeByte(this.gameType.getID());
      ☃.writeString(this.worldType.getName());
   }

   public int getDimensionID() {
      return this.dimensionID;
   }

   public EnumDifficulty getDifficulty() {
      return this.difficulty;
   }

   public GameType getGameType() {
      return this.gameType;
   }

   public WorldType getWorldType() {
      return this.worldType;
   }
}
