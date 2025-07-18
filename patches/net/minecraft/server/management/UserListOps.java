package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;

public class UserListOps extends UserList<GameProfile, UserListOpsEntry> {
   public UserListOps(File var1) {
      super(☃);
   }

   @Override
   protected UserListEntry<GameProfile> createEntry(JsonObject var1) {
      return new UserListOpsEntry(☃);
   }

   @Override
   public String[] getKeys() {
      String[] ☃ = new String[this.getValues().size()];
      int ☃x = 0;

      for (UserListOpsEntry ☃xx : this.getValues().values()) {
         ☃[☃x++] = ☃xx.getValue().getName();
      }

      return ☃;
   }

   public int getPermissionLevel(GameProfile var1) {
      UserListOpsEntry ☃ = this.getEntry(☃);
      return ☃ != null ? ☃.getPermissionLevel() : 0;
   }

   public boolean bypassesPlayerLimit(GameProfile var1) {
      UserListOpsEntry ☃ = this.getEntry(☃);
      return ☃ != null ? ☃.bypassesPlayerLimit() : false;
   }

   protected String getObjectKey(GameProfile var1) {
      return ☃.getId().toString();
   }

   public GameProfile getGameProfileFromName(String var1) {
      for (UserListOpsEntry ☃ : this.getValues().values()) {
         if (☃.equalsIgnoreCase(☃.getValue().getName())) {
            return ☃.getValue();
         }
      }

      return null;
   }
}
