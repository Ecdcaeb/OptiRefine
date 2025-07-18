package net.minecraft.client.multiplayer;

import java.net.IDN;
import java.util.Hashtable;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class ServerAddress {
   private final String ipAddress;
   private final int serverPort;

   private ServerAddress(String var1, int var2) {
      this.ipAddress = ☃;
      this.serverPort = ☃;
   }

   public String getIP() {
      try {
         return IDN.toASCII(this.ipAddress);
      } catch (IllegalArgumentException var2) {
         return "";
      }
   }

   public int getPort() {
      return this.serverPort;
   }

   public static ServerAddress fromString(String var0) {
      if (☃ == null) {
         return null;
      } else {
         String[] ☃ = ☃.split(":");
         if (☃.startsWith("[")) {
            int ☃x = ☃.indexOf("]");
            if (☃x > 0) {
               String ☃xx = ☃.substring(1, ☃x);
               String ☃xxx = ☃.substring(☃x + 1).trim();
               if (☃xxx.startsWith(":") && !☃xxx.isEmpty()) {
                  ☃xxx = ☃xxx.substring(1);
                  ☃ = new String[]{☃xx, ☃xxx};
               } else {
                  ☃ = new String[]{☃xx};
               }
            }
         }

         if (☃.length > 2) {
            ☃ = new String[]{☃};
         }

         String ☃x = ☃[0];
         int ☃xx = ☃.length > 1 ? getInt(☃[1], 25565) : 25565;
         if (☃xx == 25565) {
            String[] ☃xxx = getServerAddress(☃x);
            ☃x = ☃xxx[0];
            ☃xx = getInt(☃xxx[1], 25565);
         }

         return new ServerAddress(☃x, ☃xx);
      }
   }

   private static String[] getServerAddress(String var0) {
      try {
         String ☃ = "com.sun.jndi.dns.DnsContextFactory";
         Class.forName("com.sun.jndi.dns.DnsContextFactory");
         Hashtable<String, String> ☃x = new Hashtable<>();
         ☃x.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
         ☃x.put("java.naming.provider.url", "dns:");
         ☃x.put("com.sun.jndi.dns.timeout.retries", "1");
         DirContext ☃xx = new InitialDirContext(☃x);
         Attributes ☃xxx = ☃xx.getAttributes("_minecraft._tcp." + ☃, new String[]{"SRV"});
         String[] ☃xxxx = ☃xxx.get("srv").get().toString().split(" ", 4);
         return new String[]{☃xxxx[3], ☃xxxx[2]};
      } catch (Throwable var6) {
         return new String[]{☃, Integer.toString(25565)};
      }
   }

   private static int getInt(String var0, int var1) {
      try {
         return Integer.parseInt(☃.trim());
      } catch (Exception var3) {
         return ☃;
      }
   }
}
