package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketOpenWindow implements Packet<INetHandlerPlayClient> {
   private int windowId;
   private String inventoryType;
   private ITextComponent windowTitle;
   private int slotCount;
   private int entityId;

   public SPacketOpenWindow() {
   }

   public SPacketOpenWindow(int var1, String var2, ITextComponent var3) {
      this(☃, ☃, ☃, 0);
   }

   public SPacketOpenWindow(int var1, String var2, ITextComponent var3, int var4) {
      this.windowId = ☃;
      this.inventoryType = ☃;
      this.windowTitle = ☃;
      this.slotCount = ☃;
   }

   public SPacketOpenWindow(int var1, String var2, ITextComponent var3, int var4, int var5) {
      this(☃, ☃, ☃, ☃);
      this.entityId = ☃;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleOpenWindow(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = ☃.readUnsignedByte();
      this.inventoryType = ☃.readString(32);
      this.windowTitle = ☃.readTextComponent();
      this.slotCount = ☃.readUnsignedByte();
      if (this.inventoryType.equals("EntityHorse")) {
         this.entityId = ☃.readInt();
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeByte(this.windowId);
      ☃.writeString(this.inventoryType);
      ☃.writeTextComponent(this.windowTitle);
      ☃.writeByte(this.slotCount);
      if (this.inventoryType.equals("EntityHorse")) {
         ☃.writeInt(this.entityId);
      }
   }

   public int getWindowId() {
      return this.windowId;
   }

   public String getGuiId() {
      return this.inventoryType;
   }

   public ITextComponent getWindowTitle() {
      return this.windowTitle;
   }

   public int getSlotCount() {
      return this.slotCount;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public boolean hasSlots() {
      return this.slotCount > 0;
   }
}
