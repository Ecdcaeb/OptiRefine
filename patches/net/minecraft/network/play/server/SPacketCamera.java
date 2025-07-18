package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class SPacketCamera implements Packet<INetHandlerPlayClient> {
   public int entityId;

   public SPacketCamera() {
   }

   public SPacketCamera(Entity var1) {
      this.entityId = ☃.getEntityId();
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleCamera(this);
   }

   @Nullable
   public Entity getEntity(World var1) {
      return ☃.getEntityByID(this.entityId);
   }
}
