package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityProperties implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private final List<SPacketEntityProperties.Snapshot> snapshots = Lists.newArrayList();

   public SPacketEntityProperties() {
   }

   public SPacketEntityProperties(int var1, Collection<IAttributeInstance> var2) {
      this.entityId = ☃;

      for (IAttributeInstance ☃ : ☃) {
         this.snapshots.add(new SPacketEntityProperties.Snapshot(☃.getAttribute().getName(), ☃.getBaseValue(), ☃.getModifiers()));
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = ☃.readVarInt();
      int ☃ = ☃.readInt();

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         String ☃xx = ☃.readString(64);
         double ☃xxx = ☃.readDouble();
         List<AttributeModifier> ☃xxxx = Lists.newArrayList();
         int ☃xxxxx = ☃.readVarInt();

         for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxxxx; ☃xxxxxx++) {
            UUID ☃xxxxxxx = ☃.readUniqueId();
            ☃xxxx.add(new AttributeModifier(☃xxxxxxx, "Unknown synced attribute modifier", ☃.readDouble(), ☃.readByte()));
         }

         this.snapshots.add(new SPacketEntityProperties.Snapshot(☃xx, ☃xxx, ☃xxxx));
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.entityId);
      ☃.writeInt(this.snapshots.size());

      for (SPacketEntityProperties.Snapshot ☃ : this.snapshots) {
         ☃.writeString(☃.getName());
         ☃.writeDouble(☃.getBaseValue());
         ☃.writeVarInt(☃.getModifiers().size());

         for (AttributeModifier ☃x : ☃.getModifiers()) {
            ☃.writeUniqueId(☃x.getID());
            ☃.writeDouble(☃x.getAmount());
            ☃.writeByte(☃x.getOperation());
         }
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleEntityProperties(this);
   }

   public int getEntityId() {
      return this.entityId;
   }

   public List<SPacketEntityProperties.Snapshot> getSnapshots() {
      return this.snapshots;
   }

   public class Snapshot {
      private final String name;
      private final double baseValue;
      private final Collection<AttributeModifier> modifiers;

      public Snapshot(String var2, double var3, Collection<AttributeModifier> var5) {
         this.name = ☃;
         this.baseValue = ☃;
         this.modifiers = ☃;
      }

      public String getName() {
         return this.name;
      }

      public double getBaseValue() {
         return this.baseValue;
      }

      public Collection<AttributeModifier> getModifiers() {
         return this.modifiers;
      }
   }
}
