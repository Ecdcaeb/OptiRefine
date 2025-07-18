package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketEntityAction implements Packet<INetHandlerPlayServer> {
   private int entityID;
   private CPacketEntityAction.Action action;
   private int auxData;

   public CPacketEntityAction() {
   }

   public CPacketEntityAction(Entity var1, CPacketEntityAction.Action var2) {
      this(☃, ☃, 0);
   }

   public CPacketEntityAction(Entity var1, CPacketEntityAction.Action var2, int var3) {
      this.entityID = ☃.getEntityId();
      this.action = ☃;
      this.auxData = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityID = ☃.readVarInt();
      this.action = ☃.readEnumValue(CPacketEntityAction.Action.class);
      this.auxData = ☃.readVarInt();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityID);
      ☃.writeEnumValue(this.action);
      ☃.writeVarInt(this.auxData);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processEntityAction(this);
   }

   public CPacketEntityAction.Action getAction() {
      return this.action;
   }

   public int getAuxData() {
      return this.auxData;
   }

   public static enum Action {
      START_SNEAKING,
      STOP_SNEAKING,
      STOP_SLEEPING,
      START_SPRINTING,
      STOP_SPRINTING,
      START_RIDING_JUMP,
      STOP_RIDING_JUMP,
      OPEN_INVENTORY,
      START_FALL_FLYING;
   }
}
