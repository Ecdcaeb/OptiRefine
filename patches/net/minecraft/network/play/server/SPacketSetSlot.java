package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetSlot implements Packet<INetHandlerPlayClient> {
   private int windowId;
   private int slot;
   private ItemStack item = ItemStack.EMPTY;

   public SPacketSetSlot() {
   }

   public SPacketSetSlot(int var1, int var2, ItemStack var3) {
      this.windowId = ☃;
      this.slot = ☃;
      this.item = ☃.copy();
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleSetSlot(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = ☃.readByte();
      this.slot = ☃.readShort();
      this.item = ☃.readItemStack();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.windowId);
      ☃.writeShort(this.slot);
      ☃.writeItemStack(this.item);
   }

   public int getWindowId() {
      return this.windowId;
   }

   public int getSlot() {
      return this.slot;
   }

   public ItemStack getStack() {
      return this.item;
   }
}
