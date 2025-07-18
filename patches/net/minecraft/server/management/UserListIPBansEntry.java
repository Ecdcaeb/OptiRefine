package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.util.Date;

public class UserListIPBansEntry extends UserListEntryBan<String> {
   public UserListIPBansEntry(String var1) {
      this(☃, null, null, null, null);
   }

   public UserListIPBansEntry(String var1, Date var2, String var3, Date var4, String var5) {
      super(☃, ☃, ☃, ☃, ☃);
   }

   public UserListIPBansEntry(JsonObject var1) {
      super(getIPFromJson(☃), ☃);
   }

   private static String getIPFromJson(JsonObject var0) {
      return ☃.has("ip") ? ☃.get("ip").getAsString() : null;
   }

   @Override
   protected void onSerialization(JsonObject var1) {
      if (this.getValue() != null) {
         ☃.addProperty("ip", this.getValue());
         super.onSerialization(☃);
      }
   }
}
