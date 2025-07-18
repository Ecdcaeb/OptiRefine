package net.minecraft.network.status;

import net.minecraft.network.INetHandler;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;

public interface INetHandlerStatusServer extends INetHandler {
   void processPing(CPacketPing var1);

   void processServerQuery(CPacketServerQuery var1);
}
