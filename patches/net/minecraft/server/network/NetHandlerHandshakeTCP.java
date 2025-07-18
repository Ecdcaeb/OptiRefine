package net.minecraft.server.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer {
   private final MinecraftServer server;
   private final NetworkManager networkManager;

   public NetHandlerHandshakeTCP(MinecraftServer var1, NetworkManager var2) {
      this.server = ☃;
      this.networkManager = ☃;
   }

   @Override
   public void processHandshake(C00Handshake var1) {
      switch (☃.getRequestedState()) {
         case LOGIN:
            this.networkManager.setConnectionState(EnumConnectionState.LOGIN);
            if (☃.getProtocolVersion() > 340) {
               ITextComponent ☃ = new TextComponentTranslation("multiplayer.disconnect.outdated_server", "1.12.2");
               this.networkManager.sendPacket(new SPacketDisconnect(☃));
               this.networkManager.closeChannel(☃);
            } else if (☃.getProtocolVersion() < 340) {
               ITextComponent ☃ = new TextComponentTranslation("multiplayer.disconnect.outdated_client", "1.12.2");
               this.networkManager.sendPacket(new SPacketDisconnect(☃));
               this.networkManager.closeChannel(☃);
            } else {
               this.networkManager.setNetHandler(new NetHandlerLoginServer(this.server, this.networkManager));
            }
            break;
         case STATUS:
            this.networkManager.setConnectionState(EnumConnectionState.STATUS);
            this.networkManager.setNetHandler(new NetHandlerStatusServer(this.server, this.networkManager));
            break;
         default:
            throw new UnsupportedOperationException("Invalid intention " + ☃.getRequestedState());
      }
   }

   @Override
   public void onDisconnect(ITextComponent var1) {
   }
}
