package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;

public class UserListBans extends UserList<GameProfile, UserListBansEntry> {
   public UserListBans(File var1) {
      super(☃);
   }

   @Override
   protected UserListEntry<GameProfile> createEntry(JsonObject var1) {
      return new UserListBansEntry(☃);
   }

   public boolean isBanned(GameProfile var1) {
      return this.hasEntry(☃);
   }

   @Override
   public String[] getKeys() {
      String[] ☃ = new String[this.getValues().size()];
      int ☃x = 0;

      for (UserListBansEntry ☃xx : this.getValues().values()) {
         ☃[☃x++] = ☃xx.getValue().getName();
      }

      return ☃;
   }

   protected String getObjectKey(GameProfile var1) {
      return ☃.getId().toString();
   }

   public GameProfile getBannedProfile(String var1) {
      for (UserListBansEntry ☃ : this.getValues().values()) {
         if (☃.equalsIgnoreCase(☃.getValue().getName())) {
            return ☃.getValue();
         }
      }

      return null;
   }
}
