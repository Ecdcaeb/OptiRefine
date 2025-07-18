package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketCreativeInventoryAction implements Packet<INetHandlerPlayServer> {
   private int slotId;
   private ItemStack stack = ItemStack.EMPTY;

   public CPacketCreativeInventoryAction() {
   }

   public CPacketCreativeInventoryAction(int var1, ItemStack var2) {
      this.slotId = ☃;
      this.stack = ☃.copy();
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processCreativeInventoryAction(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.slotId = ☃.readShort();
      this.stack = ☃.readItemStack();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeShort(this.slotId);
      ☃.writeItemStack(this.stack);
   }

   public int getSlotId() {
      return this.slotId;
   }

   public ItemStack getStack() {
      return this.stack;
   }
}
