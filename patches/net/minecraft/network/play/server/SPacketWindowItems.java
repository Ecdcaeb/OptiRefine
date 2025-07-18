package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.NonNullList;

public class SPacketWindowItems implements Packet<INetHandlerPlayClient> {
   private int windowId;
   private List<ItemStack> itemStacks;

   public SPacketWindowItems() {
   }

   public SPacketWindowItems(int var1, NonNullList<ItemStack> var2) {
      this.windowId = ☃;
      this.itemStacks = NonNullList.withSize(☃.size(), ItemStack.EMPTY);

      for (int ☃ = 0; ☃ < this.itemStacks.size(); ☃++) {
         ItemStack ☃x = ☃.get(☃);
         this.itemStacks.set(☃, ☃x.copy());
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = ☃.readUnsignedByte();
      int ☃ = ☃.readShort();
      this.itemStacks = NonNullList.withSize(☃, ItemStack.EMPTY);

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         this.itemStacks.set(☃x, ☃.readItemStack());
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.windowId);
      ☃.writeShort(this.itemStacks.size());

      for (ItemStack ☃ : this.itemStacks) {
         ☃.writeItemStack(☃);
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleWindowItems(this);
   }

   public int getWindowId() {
      return this.windowId;
   }

   public List<ItemStack> getItemStacks() {
      return this.itemStacks;
   }
}
