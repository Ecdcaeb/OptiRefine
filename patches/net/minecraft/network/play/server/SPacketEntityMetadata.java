package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityMetadata implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private List<EntityDataManager.DataEntry<?>> dataManagerEntries;

   public SPacketEntityMetadata() {
   }

   public SPacketEntityMetadata(int var1, EntityDataManager var2, boolean var3) {
      this.entityId = ☃;
      if (☃) {
         this.dataManagerEntries = ☃.getAll();
         ☃.setClean();
      } else {
         this.dataManagerEntries = ☃.getDirty();
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
      this.dataManagerEntries = EntityDataManager.readEntries(☃);
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
      EntityDataManager.writeEntries(this.dataManagerEntries, ☃);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleEntityMetadata(this);
   }

   public List<EntityDataManager.DataEntry<?>> getDataManagerEntries() {
      return this.dataManagerEntries;
   }

   public int getEntityId() {
      return this.entityId;
   }
}
