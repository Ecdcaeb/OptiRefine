package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

public class SPacketRemoveEntityEffect implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private Potion effectId;

   public SPacketRemoveEntityEffect() {
   }

   public SPacketRemoveEntityEffect(int var1, Potion var2) {
      this.entityId = ☃;
      this.effectId = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
      this.effectId = Potion.getPotionById(☃.readUnsignedByte());
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
      ☃.writeByte(Potion.getIdFromPotion(this.effectId));
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleRemoveEntityEffect(this);
   }

   @Nullable
   public Entity getEntity(World var1) {
      return ☃.getEntityByID(this.entityId);
   }

   @Nullable
   public Potion getPotion() {
      return this.effectId;
   }
}
