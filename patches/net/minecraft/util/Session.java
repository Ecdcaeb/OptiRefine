package net.minecraft.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

public class Session {
   private final String username;
   private final String playerID;
   private final String token;
   private final Session.Type sessionType;

   public Session(String var1, String var2, String var3, String var4) {
      this.username = ☃;
      this.playerID = ☃;
      this.token = ☃;
      this.sessionType = Session.Type.setSessionType(☃);
   }

   public String getSessionID() {
      return "token:" + this.token + ":" + this.playerID;
   }

   public String getPlayerID() {
      return this.playerID;
   }

   public String getUsername() {
      return this.username;
   }

   public String getToken() {
      return this.token;
   }

   public GameProfile getProfile() {
      try {
         UUID ☃ = UUIDTypeAdapter.fromString(this.getPlayerID());
         return new GameProfile(☃, this.getUsername());
      } catch (IllegalArgumentException var2) {
         return new GameProfile(null, this.getUsername());
      }
   }

   public static enum Type {
      LEGACY("legacy"),
      MOJANG("mojang");

      private static final Map<String, Session.Type> SESSION_TYPES = Maps.newHashMap();
      private final String sessionType;

      private Type(String var3) {
         this.sessionType = ☃;
      }

      @Nullable
      public static Session.Type setSessionType(String var0) {
         return SESSION_TYPES.get(☃.toLowerCase(Locale.ROOT));
      }

      static {
         for (Session.Type ☃ : values()) {
            SESSION_TYPES.put(☃.sessionType, ☃);
         }
      }
   }
}
