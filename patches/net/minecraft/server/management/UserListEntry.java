package net.minecraft.server.management;

import com.google.gson.JsonObject;

public class UserListEntry<T> {
   private final T value;

   public UserListEntry(T var1) {
      this.value = ☃;
   }

   protected UserListEntry(T var1, JsonObject var2) {
      this.value = ☃;
   }

   T getValue() {
      return this.value;
   }

   boolean hasBanExpired() {
      return false;
   }

   protected void onSerialization(JsonObject var1) {
   }
}
