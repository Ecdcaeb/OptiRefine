package net.minecraft.network.login;

import net.minecraft.network.INetHandler;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;

public interface INetHandlerLoginClient extends INetHandler {
   void handleEncryptionRequest(SPacketEncryptionRequest var1);

   void handleLoginSuccess(SPacketLoginSuccess var1);

   void handleDisconnect(SPacketDisconnect var1);

   void handleEnableCompression(SPacketEnableCompression var1);
}
