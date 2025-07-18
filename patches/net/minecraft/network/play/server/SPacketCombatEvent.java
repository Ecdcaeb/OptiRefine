package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class SPacketCombatEvent implements Packet<INetHandlerPlayClient> {
   public SPacketCombatEvent.Event eventType;
   public int playerId;
   public int entityId;
   public int duration;
   public ITextComponent deathMessage;

   public SPacketCombatEvent() {
   }

   public SPacketCombatEvent(CombatTracker var1, SPacketCombatEvent.Event var2) {
      this(☃, ☃, true);
   }

   public SPacketCombatEvent(CombatTracker var1, SPacketCombatEvent.Event var2, boolean var3) {
      this.eventType = ☃;
      EntityLivingBase ☃ = ☃.getBestAttacker();
      switch (☃) {
         case END_COMBAT:
            this.duration = ☃.getCombatDuration();
            this.entityId = ☃ == null ? -1 : ☃.getEntityId();
            break;
         case ENTITY_DIED:
            this.playerId = ☃.getFighter().getEntityId();
            this.entityId = ☃ == null ? -1 : ☃.getEntityId();
            if (☃) {
               this.deathMessage = ☃.getDeathMessage();
            } else {
               this.deathMessage = new TextComponentString("");
            }
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.eventType = ☃.readEnumValue(SPacketCombatEvent.Event.class);
      if (this.eventType == SPacketCombatEvent.Event.END_COMBAT) {
         this.duration = ☃.readVarInt();
         this.entityId = ☃.readInt();
      } else if (this.eventType == SPacketCombatEvent.Event.ENTITY_DIED) {
         this.playerId = ☃.readVarInt();
         this.entityId = ☃.readInt();
         this.deathMessage = ☃.readTextComponent();
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeEnumValue(this.eventType);
      if (this.eventType == SPacketCombatEvent.Event.END_COMBAT) {
         ☃.writeVarInt(this.duration);
         ☃.writeInt(this.entityId);
      } else if (this.eventType == SPacketCombatEvent.Event.ENTITY_DIED) {
         ☃.writeVarInt(this.playerId);
         ☃.writeInt(this.entityId);
         ☃.writeTextComponent(this.deathMessage);
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleCombatEvent(this);
   }

   public static enum Event {
      ENTER_COMBAT,
      END_COMBAT,
      ENTITY_DIED;
   }
}
