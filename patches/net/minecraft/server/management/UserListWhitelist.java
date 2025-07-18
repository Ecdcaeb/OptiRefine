package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;

public class UserListWhitelist extends UserList<GameProfile, UserListWhitelistEntry> {
   public UserListWhitelist(File var1) {
      super(☃);
   }

   @Override
   protected UserListEntry<GameProfile> createEntry(JsonObject var1) {
      return new UserListWhitelistEntry(☃);
   }

   @Override
   public String[] getKeys() {
      String[] ☃ = new String[this.getValues().size()];
      int ☃x = 0;

      for (UserListWhitelistEntry ☃xx : this.getValues().values()) {
         ☃[☃x++] = ☃xx.getValue().getName();
      }

      return ☃;
   }

   protected String getObjectKey(GameProfile var1) {
      return ☃.getId().toString();
   }

   public GameProfile getByName(String var1) {
      for (UserListWhitelistEntry ☃ : this.getValues().values()) {
         if (☃.equalsIgnoreCase(☃.getValue().getName())) {
            return ☃.getValue();
         }
      }

      return null;
   }
}
