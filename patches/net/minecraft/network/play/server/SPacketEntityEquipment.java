package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityEquipment implements Packet<INetHandlerPlayClient> {
   private int entityID;
   private EntityEquipmentSlot equipmentSlot;
   private ItemStack itemStack = ItemStack.EMPTY;

   public SPacketEntityEquipment() {
   }

   public SPacketEntityEquipment(int var1, EntityEquipmentSlot var2, ItemStack var3) {
      this.entityID = ☃;
      this.equipmentSlot = ☃;
      this.itemStack = ☃.copy();
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityID = ☃.readVarInt();
      this.equipmentSlot = ☃.readEnumValue(EntityEquipmentSlot.class);
      this.itemStack = ☃.readItemStack();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityID);
      ☃.writeEnumValue(this.equipmentSlot);
      ☃.writeItemStack(this.itemStack);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleEntityEquipment(this);
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public int getEntityID() {
      return this.entityID;
   }

   public EntityEquipmentSlot getEquipmentSlot() {
      return this.equipmentSlot;
   }
}
