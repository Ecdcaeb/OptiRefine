package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketClickWindow implements Packet<INetHandlerPlayServer> {
   private int windowId;
   private int slotId;
   private int packedClickData;
   private short actionNumber;
   private ItemStack clickedItem = ItemStack.EMPTY;
   private ClickType mode;

   public CPacketClickWindow() {
   }

   public CPacketClickWindow(int var1, int var2, int var3, ClickType var4, ItemStack var5, short var6) {
      this.windowId = ☃;
      this.slotId = ☃;
      this.packedClickData = ☃;
      this.clickedItem = ☃.copy();
      this.actionNumber = ☃;
      this.mode = ☃;
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processClickWindow(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = ☃.readByte();
      this.slotId = ☃.readShort();
      this.packedClickData = ☃.readByte();
      this.actionNumber = ☃.readShort();
      this.mode = ☃.readEnumValue(ClickType.class);
      this.clickedItem = ☃.readItemStack();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.windowId);
      ☃.writeShort(this.slotId);
      ☃.writeByte(this.packedClickData);
      ☃.writeShort(this.actionNumber);
      ☃.writeEnumValue(this.mode);
      ☃.writeItemStack(this.clickedItem);
   }

   public int getWindowId() {
      return this.windowId;
   }

   public int getSlotId() {
      return this.slotId;
   }

   public int getUsedButton() {
      return this.packedClickData;
   }

   public short getActionNumber() {
      return this.actionNumber;
   }

   public ItemStack getClickedItem() {
      return this.clickedItem;
   }

   public ClickType getClickType() {
      return this.mode;
   }
}
