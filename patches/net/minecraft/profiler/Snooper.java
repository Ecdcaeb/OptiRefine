package net.minecraft.profiler;

import com.google.common.collect.Maps;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.HttpUtil;

public class Snooper {
   private final Map<String, Object> snooperStats = Maps.newHashMap();
   private final Map<String, Object> clientStats = Maps.newHashMap();
   private final String uniqueID = UUID.randomUUID().toString();
   private final URL serverUrl;
   private final ISnooperInfo playerStatsCollector;
   private final Timer threadTrigger = new Timer("Snooper Timer", true);
   private final Object syncLock = new Object();
   private final long minecraftStartTimeMilis;
   private boolean isRunning;
   private int selfCounter;

   public Snooper(String var1, ISnooperInfo var2, long var3) {
      try {
         this.serverUrl = new URL("http://snoop.minecraft.net/" + ☃ + "?version=" + 2);
      } catch (MalformedURLException var6) {
         throw new IllegalArgumentException();
      }

      this.playerStatsCollector = ☃;
      this.minecraftStartTimeMilis = ☃;
   }

   public void startSnooper() {
      if (!this.isRunning) {
         this.isRunning = true;
         this.addOSData();
         this.threadTrigger.schedule(new TimerTask() {
            @Override
            public void run() {
               if (Snooper.this.playerStatsCollector.isSnooperEnabled()) {
                  Map<String, Object> ☃;
                  synchronized (Snooper.this.syncLock) {
                     ☃ = Maps.newHashMap(Snooper.this.clientStats);
                     if (Snooper.this.selfCounter == 0) {
                        ☃.putAll(Snooper.this.snooperStats);
                     }

                     ☃.put("snooper_count", Snooper.this.selfCounter++);
                     ☃.put("snooper_token", Snooper.this.uniqueID);
                  }

                  MinecraftServer ☃ = Snooper.this.playerStatsCollector instanceof MinecraftServer ? (MinecraftServer)Snooper.this.playerStatsCollector : null;
                  HttpUtil.postMap(Snooper.this.serverUrl, ☃, true, ☃ == null ? null : ☃.getServerProxy());
               }
            }
         }, 0L, 900000L);
      }
   }

   private void addOSData() {
      this.addJvmArgsToSnooper();
      this.addClientStat("snooper_token", this.uniqueID);
      this.addStatToSnooper("snooper_token", this.uniqueID);
      this.addStatToSnooper("os_name", System.getProperty("os.name"));
      this.addStatToSnooper("os_version", System.getProperty("os.version"));
      this.addStatToSnooper("os_architecture", System.getProperty("os.arch"));
      this.addStatToSnooper("java_version", System.getProperty("java.version"));
      this.addClientStat("version", "1.12.2");
      this.playerStatsCollector.addServerTypeToSnooper(this);
   }

   private void addJvmArgsToSnooper() {
      RuntimeMXBean ☃ = ManagementFactory.getRuntimeMXBean();
      List<String> ☃x = ☃.getInputArguments();
      int ☃xx = 0;

      for (String ☃xxx : ☃x) {
         if (☃xxx.startsWith("-X")) {
            this.addClientStat("jvm_arg[" + ☃xx++ + "]", ☃xxx);
         }
      }

      this.addClientStat("jvm_args", ☃xx);
   }

   public void addMemoryStatsToSnooper() {
      this.addStatToSnooper("memory_total", Runtime.getRuntime().totalMemory());
      this.addStatToSnooper("memory_max", Runtime.getRuntime().maxMemory());
      this.addStatToSnooper("memory_free", Runtime.getRuntime().freeMemory());
      this.addStatToSnooper("cpu_cores", Runtime.getRuntime().availableProcessors());
      this.playerStatsCollector.addServerStatsToSnooper(this);
   }

   public void addClientStat(String var1, Object var2) {
      synchronized (this.syncLock) {
         this.clientStats.put(☃, ☃);
      }
   }

   public void addStatToSnooper(String var1, Object var2) {
      synchronized (this.syncLock) {
         this.snooperStats.put(☃, ☃);
      }
   }

   public Map<String, String> getCurrentStats() {
      Map<String, String> ☃ = Maps.newLinkedHashMap();
      synchronized (this.syncLock) {
         this.addMemoryStatsToSnooper();

         for (Entry<String, Object> ☃x : this.snooperStats.entrySet()) {
            ☃.put(☃x.getKey(), ☃x.getValue().toString());
         }

         for (Entry<String, Object> ☃x : this.clientStats.entrySet()) {
            ☃.put(☃x.getKey(), ☃x.getValue().toString());
         }

         return ☃;
      }
   }

   public boolean isSnooperRunning() {
      return this.isRunning;
   }

   public void stopSnooper() {
      this.threadTrigger.cancel();
   }

   public String getUniqueID() {
      return this.uniqueID;
   }

   public long getMinecraftStartTimeMillis() {
      return this.minecraftStartTimeMilis;
   }
}
