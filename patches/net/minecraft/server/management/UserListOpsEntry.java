package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class UserListOpsEntry extends UserListEntry<GameProfile> {
   private final int permissionLevel;
   private final boolean bypassesPlayerLimit;

   public UserListOpsEntry(GameProfile var1, int var2, boolean var3) {
      super(☃);
      this.permissionLevel = ☃;
      this.bypassesPlayerLimit = ☃;
   }

   public UserListOpsEntry(JsonObject var1) {
      super(constructProfile(☃), ☃);
      this.permissionLevel = ☃.has("level") ? ☃.get("level").getAsInt() : 0;
      this.bypassesPlayerLimit = ☃.has("bypassesPlayerLimit") && ☃.get("bypassesPlayerLimit").getAsBoolean();
   }

   public int getPermissionLevel() {
      return this.permissionLevel;
   }

   public boolean bypassesPlayerLimit() {
      return this.bypassesPlayerLimit;
   }

   @Override
   protected void onSerialization(JsonObject var1) {
      if (this.getValue() != null) {
         ☃.addProperty("uuid", this.getValue().getId() == null ? "" : this.getValue().getId().toString());
         ☃.addProperty("name", this.getValue().getName());
         super.onSerialization(☃);
         ☃.addProperty("level", this.permissionLevel);
         ☃.addProperty("bypassesPlayerLimit", this.bypassesPlayerLimit);
      }
   }

   private static GameProfile constructProfile(JsonObject var0) {
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
