package net.minecraft.network.play.server;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

public class SPacketStatistics implements Packet<INetHandlerPlayClient> {
   private Map<StatBase, Integer> statisticMap;

   public SPacketStatistics() {
   }

   public SPacketStatistics(Map<StatBase, Integer> var1) {
      this.statisticMap = ☃;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleStatistics(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      int ☃ = ☃.readVarInt();
      this.statisticMap = Maps.newHashMap();

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         StatBase ☃xx = StatList.getOneShotStat(☃.readString(32767));
         int ☃xxx = ☃.readVarInt();
         if (☃xx != null) {
            this.statisticMap.put(☃xx, ☃xxx);
         }
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.statisticMap.size());

      for (Entry<StatBase, Integer> ☃ : this.statisticMap.entrySet()) {
         ☃.writeString(☃.getKey().statId);
         ☃.writeVarInt(☃.getValue());
      }
   }

   public Map<StatBase, Integer> getStatisticMap() {
      return this.statisticMap;
   }
}
