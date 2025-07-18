package net.minecraft.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Type;
import java.util.UUID;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.text.ITextComponent;

public class ServerStatusResponse {
   private ITextComponent description;
   private ServerStatusResponse.Players players;
   private ServerStatusResponse.Version version;
   private String favicon;

   public ITextComponent getServerDescription() {
      return this.description;
   }

   public void setServerDescription(ITextComponent var1) {
      this.description = ☃;
   }

   public ServerStatusResponse.Players getPlayers() {
      return this.players;
   }

   public void setPlayers(ServerStatusResponse.Players var1) {
      this.players = ☃;
   }

   public ServerStatusResponse.Version getVersion() {
      return this.version;
   }

   public void setVersion(ServerStatusResponse.Version var1) {
      this.version = ☃;
   }

   public void setFavicon(String var1) {
      this.favicon = ☃;
   }

   public String getFavicon() {
      return this.favicon;
   }

   public static class Players {
      private final int maxPlayers;
      private final int onlinePlayerCount;
      private GameProfile[] players;

      public Players(int var1, int var2) {
         this.maxPlayers = ☃;
         this.onlinePlayerCount = ☃;
      }

      public int getMaxPlayers() {
         return this.maxPlayers;
      }

      public int getOnlinePlayerCount() {
         return this.onlinePlayerCount;
      }

      public GameProfile[] getPlayers() {
         return this.players;
      }

      public void setPlayers(GameProfile[] var1) {
         this.players = ☃;
      }

      public static class Serializer implements JsonDeserializer<ServerStatusResponse.Players>, JsonSerializer<ServerStatusResponse.Players> {
         public ServerStatusResponse.Players deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
            JsonObject ☃ = JsonUtils.getJsonObject(☃, "players");
            ServerStatusResponse.Players ☃x = new ServerStatusResponse.Players(JsonUtils.getInt(☃, "max"), JsonUtils.getInt(☃, "online"));
            if (JsonUtils.isJsonArray(☃, "sample")) {
               JsonArray ☃xx = JsonUtils.getJsonArray(☃, "sample");
               if (☃xx.size() > 0) {
                  GameProfile[] ☃xxx = new GameProfile[☃xx.size()];

                  for (int ☃xxxx = 0; ☃xxxx < ☃xxx.length; ☃xxxx++) {
                     JsonObject ☃xxxxx = JsonUtils.getJsonObject(☃xx.get(☃xxxx), "player[" + ☃xxxx + "]");
                     String ☃xxxxxx = JsonUtils.getString(☃xxxxx, "id");
                     ☃xxx[☃xxxx] = new GameProfile(UUID.fromString(☃xxxxxx), JsonUtils.getString(☃xxxxx, "name"));
                  }

                  ☃x.setPlayers(☃xxx);
               }
            }

            return ☃x;
         }

         public JsonElement serialize(ServerStatusResponse.Players var1, Type var2, JsonSerializationContext var3) {
            JsonObject ☃ = new JsonObject();
            ☃.addProperty("max", ☃.getMaxPlayers());
            ☃.addProperty("online", ☃.getOnlinePlayerCount());
            if (☃.getPlayers() != null && ☃.getPlayers().length > 0) {
               JsonArray ☃x = new JsonArray();

               for (int ☃xx = 0; ☃xx < ☃.getPlayers().length; ☃xx++) {
                  JsonObject ☃xxx = new JsonObject();
                  UUID ☃xxxx = ☃.getPlayers()[☃xx].getId();
                  ☃xxx.addProperty("id", ☃xxxx == null ? "" : ☃xxxx.toString());
                  ☃xxx.addProperty("name", ☃.getPlayers()[☃xx].getName());
                  ☃x.add(☃xxx);
               }

               ☃.add("sample", ☃x);
            }

            return ☃;
         }
      }
   }

   public static class Serializer implements JsonDeserializer<ServerStatusResponse>, JsonSerializer<ServerStatusResponse> {
      public ServerStatusResponse deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "status");
         ServerStatusResponse ☃x = new ServerStatusResponse();
         if (☃.has("description")) {
            ☃x.setServerDescription((ITextComponent)☃.deserialize(☃.get("description"), ITextComponent.class));
         }

         if (☃.has("players")) {
            ☃x.setPlayers((ServerStatusResponse.Players)☃.deserialize(☃.get("players"), ServerStatusResponse.Players.class));
         }

         if (☃.has("version")) {
            ☃x.setVersion((ServerStatusResponse.Version)☃.deserialize(☃.get("version"), ServerStatusResponse.Version.class));
         }

         if (☃.has("favicon")) {
            ☃x.setFavicon(JsonUtils.getString(☃, "favicon"));
         }

         return ☃x;
      }

      public JsonElement serialize(ServerStatusResponse var1, Type var2, JsonSerializationContext var3) {
         JsonObject ☃ = new JsonObject();
         if (☃.getServerDescription() != null) {
            ☃.add("description", ☃.serialize(☃.getServerDescription()));
         }

         if (☃.getPlayers() != null) {
            ☃.add("players", ☃.serialize(☃.getPlayers()));
         }

         if (☃.getVersion() != null) {
            ☃.add("version", ☃.serialize(☃.getVersion()));
         }

         if (☃.getFavicon() != null) {
            ☃.addProperty("favicon", ☃.getFavicon());
         }

         return ☃;
      }
   }

   public static class Version {
      private final String name;
      private final int protocol;

      public Version(String var1, int var2) {
         this.name = ☃;
         this.protocol = ☃;
      }

      public String getName() {
         return this.name;
      }

      public int getProtocol() {
         return this.protocol;
      }

      public static class Serializer implements JsonDeserializer<ServerStatusResponse.Version>, JsonSerializer<ServerStatusResponse.Version> {
         public ServerStatusResponse.Version deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
            JsonObject ☃ = JsonUtils.getJsonObject(☃, "version");
            return new ServerStatusResponse.Version(JsonUtils.getString(☃, "name"), JsonUtils.getInt(☃, "protocol"));
         }

         public JsonElement serialize(ServerStatusResponse.Version var1, Type var2, JsonSerializationContext var3) {
            JsonObject ☃ = new JsonObject();
            ☃.addProperty("name", ☃.getName());
            ☃.addProperty("protocol", ☃.getProtocol());
            return ☃;
         }
      }
   }
}
