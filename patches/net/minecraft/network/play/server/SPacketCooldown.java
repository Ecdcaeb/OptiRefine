package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCooldown implements Packet<INetHandlerPlayClient> {
   private Item item;
   private int ticks;

   public SPacketCooldown() {
   }

   public SPacketCooldown(Item var1, int var2) {
      this.item = ☃;
      this.ticks = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.item = Item.getItemById(☃.readVarInt());
      this.ticks = ☃.readVarInt();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(Item.getIdFromItem(this.item));
      ☃.writeVarInt(this.ticks);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleCooldown(this);
   }

   public Item getItem() {
      return this.item;
   }

   public int getTicks() {
      return this.ticks;
   }
}
