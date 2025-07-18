package net.minecraft.stats;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatisticsManagerServer extends StatisticsManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private final MinecraftServer server;
   private final File statsFile;
   private final Set<StatBase> dirty = Sets.newHashSet();
   private int lastStatRequest = -300;

   public StatisticsManagerServer(MinecraftServer var1, File var2) {
      this.server = ☃;
      this.statsFile = ☃;
   }

   public void readStatFile() {
      if (this.statsFile.isFile()) {
         try {
            this.statsData.clear();
            this.statsData.putAll(this.parseJson(FileUtils.readFileToString(this.statsFile)));
         } catch (IOException var2) {
            LOGGER.error("Couldn't read statistics file {}", this.statsFile, var2);
         } catch (JsonParseException var3) {
            LOGGER.error("Couldn't parse statistics file {}", this.statsFile, var3);
         }
      }
   }

   public void saveStatFile() {
      try {
         FileUtils.writeStringToFile(this.statsFile, dumpJson(this.statsData));
      } catch (IOException var2) {
         LOGGER.error("Couldn't save stats", var2);
      }
   }

   @Override
   public void unlockAchievement(EntityPlayer var1, StatBase var2, int var3) {
      super.unlockAchievement(☃, ☃, ☃);
      this.dirty.add(☃);
   }

   private Set<StatBase> getDirty() {
      Set<StatBase> ☃ = Sets.newHashSet(this.dirty);
      this.dirty.clear();
      return ☃;
   }

   public Map<StatBase, TupleIntJsonSerializable> parseJson(String var1) {
      JsonElement ☃ = new JsonParser().parse(☃);
      if (!☃.isJsonObject()) {
         return Maps.newHashMap();
      } else {
         JsonObject ☃x = ☃.getAsJsonObject();
         Map<StatBase, TupleIntJsonSerializable> ☃xx = Maps.newHashMap();

         for (Entry<String, JsonElement> ☃xxx : ☃x.entrySet()) {
            StatBase ☃xxxx = StatList.getOneShotStat(☃xxx.getKey());
            if (☃xxxx != null) {
               TupleIntJsonSerializable ☃xxxxx = new TupleIntJsonSerializable();
               if (☃xxx.getValue().isJsonPrimitive() && ☃xxx.getValue().getAsJsonPrimitive().isNumber()) {
                  ☃xxxxx.setIntegerValue(☃xxx.getValue().getAsInt());
               } else if (☃xxx.getValue().isJsonObject()) {
                  JsonObject ☃xxxxxx = ☃xxx.getValue().getAsJsonObject();
                  if (☃xxxxxx.has("value") && ☃xxxxxx.get("value").isJsonPrimitive() && ☃xxxxxx.get("value").getAsJsonPrimitive().isNumber()) {
                     ☃xxxxx.setIntegerValue(☃xxxxxx.getAsJsonPrimitive("value").getAsInt());
                  }

                  if (☃xxxxxx.has("progress") && ☃xxxx.getSerializableClazz() != null) {
                     try {
                        Constructor<? extends IJsonSerializable> ☃xxxxxxx = ☃xxxx.getSerializableClazz().getConstructor();
                        IJsonSerializable ☃xxxxxxxx = ☃xxxxxxx.newInstance();
                        ☃xxxxxxxx.fromJson(☃xxxxxx.get("progress"));
                        ☃xxxxx.setJsonSerializableValue(☃xxxxxxxx);
                     } catch (Throwable var12) {
                        LOGGER.warn("Invalid statistic progress in {}", this.statsFile, var12);
                     }
                  }
               }

               ☃xx.put(☃xxxx, ☃xxxxx);
            } else {
               LOGGER.warn("Invalid statistic in {}: Don't know what {} is", this.statsFile, ☃xxx.getKey());
            }
         }

         return ☃xx;
      }
   }

   public static String dumpJson(Map<StatBase, TupleIntJsonSerializable> var0) {
      JsonObject ☃ = new JsonObject();

      for (Entry<StatBase, TupleIntJsonSerializable> ☃x : ☃.entrySet()) {
         if (☃x.getValue().getJsonSerializableValue() != null) {
            JsonObject ☃xx = new JsonObject();
            ☃xx.addProperty("value", ☃x.getValue().getIntegerValue());

            try {
               ☃xx.add("progress", ☃x.getValue().<IJsonSerializable>getJsonSerializableValue().getSerializableElement());
            } catch (Throwable var6) {
               LOGGER.warn("Couldn't save statistic {}: error serializing progress", ☃x.getKey().getStatName(), var6);
            }

            ☃.add(☃x.getKey().statId, ☃xx);
         } else {
            ☃.addProperty(☃x.getKey().statId, ☃x.getValue().getIntegerValue());
         }
      }

      return ☃.toString();
   }

   public void markAllDirty() {
      this.dirty.addAll(this.statsData.keySet());
   }

   public void sendStats(EntityPlayerMP var1) {
      int ☃ = this.server.getTickCounter();
      Map<StatBase, Integer> ☃x = Maps.newHashMap();
      if (☃ - this.lastStatRequest > 300) {
         this.lastStatRequest = ☃;

         for (StatBase ☃xx : this.getDirty()) {
            ☃x.put(☃xx, this.readStat(☃xx));
         }
      }

      ☃.connection.sendPacket(new SPacketStatistics(☃x));
   }
}
