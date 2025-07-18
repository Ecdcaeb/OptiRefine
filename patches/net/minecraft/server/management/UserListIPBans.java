package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.io.File;
import java.net.SocketAddress;

public class UserListIPBans extends UserList<String, UserListIPBansEntry> {
   public UserListIPBans(File var1) {
      super(☃);
   }

   @Override
   protected UserListEntry<String> createEntry(JsonObject var1) {
      return new UserListIPBansEntry(☃);
   }

   public boolean isBanned(SocketAddress var1) {
      String ☃ = this.addressToString(☃);
      return this.hasEntry(☃);
   }

   public UserListIPBansEntry getBanEntry(SocketAddress var1) {
      String ☃ = this.addressToString(☃);
      return this.getEntry(☃);
   }

   private String addressToString(SocketAddress var1) {
      String ☃ = ☃.toString();
      if (☃.contains("/")) {
         ☃ = ☃.substring(☃.indexOf(47) + 1);
      }

      if (☃.contains(":")) {
         ☃ = ☃.substring(0, ☃.indexOf(58));
      }

      return ☃;
   }
}
