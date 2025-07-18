package net.minecraft.network.login;

import net.minecraft.network.INetHandler;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.client.CPacketLoginStart;

public interface INetHandlerLoginServer extends INetHandler {
   void processLoginStart(CPacketLoginStart var1);

   void processEncryptionResponse(CPacketEncryptionResponse var1);
}
