package net.minecraft.network.play.server;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.ResourceLocation;

public class SPacketAdvancementInfo implements Packet<INetHandlerPlayClient> {
   private boolean firstSync;
   private Map<ResourceLocation, Advancement.Builder> advancementsToAdd;
   private Set<ResourceLocation> advancementsToRemove;
   private Map<ResourceLocation, AdvancementProgress> progressUpdates;

   public SPacketAdvancementInfo() {
   }

   public SPacketAdvancementInfo(boolean var1, Collection<Advancement> var2, Set<ResourceLocation> var3, Map<ResourceLocation, AdvancementProgress> var4) {
      this.firstSync = ☃;
      this.advancementsToAdd = Maps.newHashMap();

      for (Advancement ☃ : ☃) {
         this.advancementsToAdd.put(☃.getId(), ☃.copy());
      }

      this.advancementsToRemove = ☃;
      this.progressUpdates = Maps.newHashMap(☃);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleAdvancementInfo(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.firstSync = ☃.readBoolean();
      this.advancementsToAdd = Maps.newHashMap();
      this.advancementsToRemove = Sets.newLinkedHashSet();
      this.progressUpdates = Maps.newHashMap();
      int ☃ = ☃.readVarInt();

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         ResourceLocation ☃xx = ☃.readResourceLocation();
         Advancement.Builder ☃xxx = Advancement.Builder.readFrom(☃);
         this.advancementsToAdd.put(☃xx, ☃xxx);
      }

      ☃ = ☃.readVarInt();

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         ResourceLocation ☃xx = ☃.readResourceLocation();
         this.advancementsToRemove.add(☃xx);
      }

      ☃ = ☃.readVarInt();

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         ResourceLocation ☃xx = ☃.readResourceLocation();
         this.progressUpdates.put(☃xx, AdvancementProgress.fromNetwork(☃));
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeBoolean(this.firstSync);
      ☃.writeVarInt(this.advancementsToAdd.size());

      for (Entry<ResourceLocation, Advancement.Builder> ☃ : this.advancementsToAdd.entrySet()) {
         ResourceLocation ☃x = ☃.getKey();
         Advancement.Builder ☃xx = ☃.getValue();
         ☃.writeResourceLocation(☃x);
         ☃xx.writeTo(☃);
      }

      ☃.writeVarInt(this.advancementsToRemove.size());

      for (ResourceLocation ☃ : this.advancementsToRemove) {
         ☃.writeResourceLocation(☃);
      }

      ☃.writeVarInt(this.progressUpdates.size());

      for (Entry<ResourceLocation, AdvancementProgress> ☃ : this.progressUpdates.entrySet()) {
         ☃.writeResourceLocation(☃.getKey());
         ☃.getValue().serializeToNetwork(☃);
      }
   }

   public Map<ResourceLocation, Advancement.Builder> getAdvancementsToAdd() {
      return this.advancementsToAdd;
   }

   public Set<ResourceLocation> getAdvancementsToRemove() {
      return this.advancementsToRemove;
   }

   public Map<ResourceLocation, AdvancementProgress> getProgressUpdates() {
      return this.progressUpdates;
   }

   public boolean isFirstSync() {
      return this.firstSync;
   }
}
