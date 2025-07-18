package net.minecraft.realms;

import net.minecraft.client.multiplayer.ServerAddress;

public class RealmsServerAddress {
   private final String host;
   private final int port;

   protected RealmsServerAddress(String var1, int var2) {
      this.host = ☃;
      this.port = ☃;
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public static RealmsServerAddress parseString(String var0) {
      ServerAddress ☃ = ServerAddress.fromString(☃);
      return new RealmsServerAddress(☃.getIP(), ☃.getPort());
   }
}
