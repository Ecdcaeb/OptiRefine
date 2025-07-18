package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class UserListWhitelistEntry extends UserListEntry<GameProfile> {
   public UserListWhitelistEntry(GameProfile var1) {
      super(☃);
   }

   public UserListWhitelistEntry(JsonObject var1) {
      super(gameProfileFromJsonObject(☃), ☃);
   }

   @Override
   protected void onSerialization(JsonObject var1) {
      if (this.getValue() != null) {
         ☃.addProperty("uuid", this.getValue().getId() == null ? "" : this.getValue().getId().toString());
         ☃.addProperty("name", this.getValue().getName());
         super.onSerialization(☃);
      }
   }

   private static GameProfile gameProfileFromJsonObject(JsonObject var0) {
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
