package net.minecraft.client.network;

import net.minecraft.client.Minecraft;

public class LanServerInfo {
   private final String lanServerMotd;
   private final String lanServerIpPort;
   private long timeLastSeen;

   public LanServerInfo(String var1, String var2) {
      this.lanServerMotd = ☃;
      this.lanServerIpPort = ☃;
      this.timeLastSeen = Minecraft.getSystemTime();
   }

   public String getServerMotd() {
      return this.lanServerMotd;
   }

   public String getServerIpPort() {
      return this.lanServerIpPort;
   }

   public void updateLastSeen() {
      this.timeLastSeen = Minecraft.getSystemTime();
   }
}
