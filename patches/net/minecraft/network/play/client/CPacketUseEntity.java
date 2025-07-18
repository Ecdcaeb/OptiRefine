package net.minecraft.network.play.client;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CPacketUseEntity implements Packet<INetHandlerPlayServer> {
   private int entityId;
   private CPacketUseEntity.Action action;
   private Vec3d hitVec;
   private EnumHand hand;

   public CPacketUseEntity() {
   }

   public CPacketUseEntity(Entity var1) {
      this.entityId = ☃.getEntityId();
      this.action = CPacketUseEntity.Action.ATTACK;
   }

   public CPacketUseEntity(Entity var1, EnumHand var2) {
      this.entityId = ☃.getEntityId();
      this.action = CPacketUseEntity.Action.INTERACT;
      this.hand = ☃;
   }

   public CPacketUseEntity(Entity var1, EnumHand var2, Vec3d var3) {
      this.entityId = ☃.getEntityId();
      this.action = CPacketUseEntity.Action.INTERACT_AT;
      this.hand = ☃;
      this.hitVec = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
      this.action = ☃.readEnumValue(CPacketUseEntity.Action.class);
      if (this.action == CPacketUseEntity.Action.INTERACT_AT) {
         this.hitVec = new Vec3d(☃.readFloat(), ☃.readFloat(), ☃.readFloat());
      }

      if (this.action == CPacketUseEntity.Action.INTERACT || this.action == CPacketUseEntity.Action.INTERACT_AT) {
         this.hand = ☃.readEnumValue(EnumHand.class);
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
      ☃.writeEnumValue(this.action);
      if (this.action == CPacketUseEntity.Action.INTERACT_AT) {
         ☃.writeFloat((float)this.hitVec.x);
         ☃.writeFloat((float)this.hitVec.y);
         ☃.writeFloat((float)this.hitVec.z);
      }

      if (this.action == CPacketUseEntity.Action.INTERACT || this.action == CPacketUseEntity.Action.INTERACT_AT) {
         ☃.writeEnumValue(this.hand);
      }
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processUseEntity(this);
   }

   @Nullable
   public Entity getEntityFromWorld(World var1) {
      return ☃.getEntityByID(this.entityId);
   }

   public CPacketUseEntity.Action getAction() {
      return this.action;
   }

   public EnumHand getHand() {
      return this.hand;
   }

   public Vec3d getHitVec() {
      return this.hitVec;
   }

   public static enum Action {
      INTERACT,
      ATTACK,
      INTERACT_AT;
   }
}
