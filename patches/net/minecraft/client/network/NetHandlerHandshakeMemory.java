package net.minecraft.client.network;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.text.ITextComponent;

public class NetHandlerHandshakeMemory implements INetHandlerHandshakeServer {
   private final MinecraftServer server;
   private final NetworkManager networkManager;

   public NetHandlerHandshakeMemory(MinecraftServer var1, NetworkManager var2) {
      this.server = ☃;
      this.networkManager = ☃;
   }

   @Override
   public void processHandshake(C00Handshake var1) {
      this.networkManager.setConnectionState(☃.getRequestedState());
      this.networkManager.setNetHandler(new NetHandlerLoginServer(this.server, this.networkManager));
   }

   @Override
   public void onDisconnect(ITextComponent var1) {
   }
}
