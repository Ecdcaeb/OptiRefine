package net.minecraft.network.login.server;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class SPacketLoginSuccess implements Packet<INetHandlerLoginClient> {
   private GameProfile profile;

   public SPacketLoginSuccess() {
   }

   public SPacketLoginSuccess(GameProfile var1) {
      this.profile = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      String ☃ = ☃.readString(36);
      String ☃x = ☃.readString(16);
      UUID ☃xx = UUID.fromString(☃);
      this.profile = new GameProfile(☃xx, ☃x);
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      UUID ☃ = this.profile.getId();
      ☃.writeString(☃ == null ? "" : ☃.toString());
      ☃.writeString(this.profile.getName());
   }

   public void processPacket(INetHandlerLoginClient var1) {
      ☃.handleLoginSuccess(this);
   }

   public GameProfile getProfile() {
      return this.profile;
   }
}
