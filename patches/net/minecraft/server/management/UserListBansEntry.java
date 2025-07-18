package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.Date;
import java.util.UUID;

public class UserListBansEntry extends UserListEntryBan<GameProfile> {
   public UserListBansEntry(GameProfile var1) {
      this(☃, null, null, null, null);
   }

   public UserListBansEntry(GameProfile var1, Date var2, String var3, Date var4, String var5) {
      super(☃, ☃, ☃, ☃, ☃);
   }

   public UserListBansEntry(JsonObject var1) {
      super(toGameProfile(☃), ☃);
   }

   @Override
   protected void onSerialization(JsonObject var1) {
      if (this.getValue() != null) {
         ☃.addProperty("uuid", this.getValue().getId() == null ? "" : this.getValue().getId().toString());
         ☃.addProperty("name", this.getValue().getName());
         super.onSerialization(☃);
      }
   }

   private static GameProfile toGameProfile(JsonObject var0) {
      if (☃.has("uuid") && ☃.has("name")) {
         String ☃ = ☃.get("uuid").getAsString();

         UUID ☃x;
         try {
            ☃x = UUID.fromString(☃);
         } catch (Throwable var4) {
            return null;
         }

         return new GameProfile(☃x, ☃.get("name").getAsString());
      } else {
         return null;
      }
   }
}
