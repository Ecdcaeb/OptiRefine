package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCollectItem implements Packet<INetHandlerPlayClient> {
   private int collectedItemEntityId;
   private int entityId;
   private int collectedQuantity;

   public SPacketCollectItem() {
   }

   public SPacketCollectItem(int var1, int var2, int var3) {
      this.collectedItemEntityId = ☃;
      this.entityId = ☃;
      this.collectedQuantity = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.collectedItemEntityId = ☃.readVarInt();
      this.entityId = ☃.readVarInt();
      this.collectedQuantity = ☃.readVarInt();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.collectedItemEntityId);
      ☃.writeVarInt(this.entityId);
      ☃.writeVarInt(this.collectedQuantity);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleCollectItem(this);
   }

   public int getCollectedItemEntityID() {
      return this.collectedItemEntityId;
   }

   public int getEntityID() {
      return this.entityId;
   }

   public int getAmount() {
      return this.collectedQuantity;
   }
}
