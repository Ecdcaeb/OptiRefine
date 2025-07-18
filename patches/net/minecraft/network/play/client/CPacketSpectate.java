package net.minecraft.network.play.client;

import java.io.IOException;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.world.WorldServer;

public class CPacketSpectate implements Packet<INetHandlerPlayServer> {
   private UUID id;

   public CPacketSpectate() {
   }

   public CPacketSpectate(UUID var1) {
      this.id = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.id = ☃.readUniqueId();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeUniqueId(this.id);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.handleSpectate(this);
   }

   @Nullable
   public Entity getEntity(WorldServer var1) {
      return ☃.getEntityFromUuid(this.id);
   }
}
