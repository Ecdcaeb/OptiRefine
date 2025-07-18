package net.minecraft.network.login.client;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;

public class CPacketLoginStart implements Packet<INetHandlerLoginServer> {
   private GameProfile profile;

   public CPacketLoginStart() {
   }

   public CPacketLoginStart(GameProfile var1) {
      this.profile = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.profile = new GameProfile(null, ☃.readString(16));
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeString(this.profile.getName());
   }

   public void processPacket(INetHandlerLoginServer var1) {
      ☃.processLoginStart(this);
   }

   public GameProfile getProfile() {
      return this.profile;
   }
}
