package net.minecraft.server.management;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.ProfileLookupCallback;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import org.apache.commons.io.IOUtils;

public class PlayerProfileCache {
   public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
   private static boolean onlineMode;
   private final Map<String, PlayerProfileCache.ProfileEntry> usernameToProfileEntryMap = Maps.newHashMap();
   private final Map<UUID, PlayerProfileCache.ProfileEntry> uuidToProfileEntryMap = Maps.newHashMap();
   private final Deque<GameProfile> gameProfiles = Lists.newLinkedList();
   private final GameProfileRepository profileRepo;
   protected final Gson gson;
   private final File usercacheFile;
   private static final ParameterizedType TYPE = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
         return new Type[]{PlayerProfileCache.ProfileEntry.class};
      }

      @Override
      public Type getRawType() {
         return List.class;
      }

      @Override
      public Type getOwnerType() {
         return null;
      }
   };

   public PlayerProfileCache(GameProfileRepository var1, File var2) {
      this.profileRepo = ☃;
      this.usercacheFile = ☃;
      GsonBuilder ☃ = new GsonBuilder();
      ☃.registerTypeHierarchyAdapter(PlayerProfileCache.ProfileEntry.class, new PlayerProfileCache.Serializer());
      this.gson = ☃.create();
      this.load();
   }

   private static GameProfile lookupProfile(GameProfileRepository var0, String var1) {
      final GameProfile[] ☃ = new GameProfile[1];
      ProfileLookupCallback ☃x = new ProfileLookupCallback() {
         public void onProfileLookupSucceeded(GameProfile var1) {
            ☃[0] = ☃;
         }

         public void onProfileLookupFailed(GameProfile var1, Exception var2x) {
            ☃[0] = null;
         }
      };
      ☃.findProfilesByNames(new String[]{☃}, Agent.MINECRAFT, ☃x);
      if (!isOnlineMode() && ☃[0] == null) {
         UUID ☃xx = EntityPlayer.getUUID(new GameProfile(null, ☃));
         GameProfile ☃xxx = new GameProfile(☃xx, ☃);
         ☃x.onProfileLookupSucceeded(☃xxx);
      }

      return ☃[0];
   }

   public static void setOnlineMode(boolean var0) {
      onlineMode = ☃;
   }

   private static boolean isOnlineMode() {
      return onlineMode;
   }

   public void addEntry(GameProfile var1) {
      this.addEntry(☃, null);
   }

   private void addEntry(GameProfile var1, Date var2) {
      UUID ☃ = ☃.getId();
      if (☃ == null) {
         Calendar ☃x = Calendar.getInstance();
         ☃x.setTime(new Date());
         ☃x.add(2, 1);
         ☃ = ☃x.getTime();
      }

      String ☃x = ☃.getName().toLowerCase(Locale.ROOT);
      PlayerProfileCache.ProfileEntry ☃xx = new PlayerProfileCache.ProfileEntry(☃, ☃);
      if (this.uuidToProfileEntryMap.containsKey(☃)) {
         PlayerProfileCache.ProfileEntry ☃xxx = this.uuidToProfileEntryMap.get(☃);
         this.usernameToProfileEntryMap.remove(☃xxx.getGameProfile().getName().toLowerCase(Locale.ROOT));
         this.gameProfiles.remove(☃);
      }

      this.usernameToProfileEntryMap.put(☃.getName().toLowerCase(Locale.ROOT), ☃xx);
      this.uuidToProfileEntryMap.put(☃, ☃xx);
      this.gameProfiles.addFirst(☃);
      this.save();
   }

   @Nullable
   public GameProfile getGameProfileForUsername(String var1) {
      String ☃ = ☃.toLowerCase(Locale.ROOT);
      PlayerProfileCache.ProfileEntry ☃x = this.usernameToProfileEntryMap.get(☃);
      if (☃x != null && new Date().getTime() >= ☃x.expirationDate.getTime()) {
         this.uuidToProfileEntryMap.remove(☃x.getGameProfile().getId());
         this.usernameToProfileEntryMap.remove(☃x.getGameProfile().getName().toLowerCase(Locale.ROOT));
         this.gameProfiles.remove(☃x.getGameProfile());
         ☃x = null;
      }

      if (☃x != null) {
         GameProfile ☃xx = ☃x.getGameProfile();
         this.gameProfiles.remove(☃xx);
         this.gameProfiles.addFirst(☃xx);
      } else {
         GameProfile ☃xx = lookupProfile(this.profileRepo, ☃);
         if (☃xx != null) {
            this.addEntry(☃xx);
            ☃x = this.usernameToProfileEntryMap.get(☃);
         }
      }

      this.save();
      return ☃x == null ? null : ☃x.getGameProfile();
   }

   public String[] getUsernames() {
      List<String> ☃ = Lists.newArrayList(this.usernameToProfileEntryMap.keySet());
      return ☃.toArray(new String[☃.size()]);
   }

   @Nullable
   public GameProfile getProfileByUUID(UUID var1) {
      PlayerProfileCache.ProfileEntry ☃ = this.uuidToProfileEntryMap.get(☃);
      return ☃ == null ? null : ☃.getGameProfile();
   }

   private PlayerProfileCache.ProfileEntry getByUUID(UUID var1) {
      PlayerProfileCache.ProfileEntry ☃ = this.uuidToProfileEntryMap.get(☃);
      if (☃ != null) {
         GameProfile ☃x = ☃.getGameProfile();
         this.gameProfiles.remove(☃x);
         this.gameProfiles.addFirst(☃x);
      }

      return ☃;
   }

   public void load() {
      BufferedReader ☃ = null;

      try {
         ☃ = Files.newReader(this.usercacheFile, StandardCharsets.UTF_8);
         List<PlayerProfileCache.ProfileEntry> ☃x = JsonUtils.fromJson(this.gson, ☃, TYPE);
         this.usernameToProfileEntryMap.clear();
         this.uuidToProfileEntryMap.clear();
         this.gameProfiles.clear();
         if (☃x != null) {
            for (PlayerProfileCache.ProfileEntry ☃xx : Lists.reverse(☃x)) {
               if (☃xx != null) {
                  this.addEntry(☃xx.getGameProfile(), ☃xx.getExpirationDate());
               }
            }
         }
      } catch (FileNotFoundException var9) {
      } catch (JsonParseException var10) {
      } finally {
         IOUtils.closeQuietly(☃);
      }
   }

   public void save() {
      String ☃ = this.gson.toJson(this.getEntriesWithLimit(1000));
      BufferedWriter ☃x = null;

      try {
         ☃x = Files.newWriter(this.usercacheFile, StandardCharsets.UTF_8);
         ☃x.write(☃);
         return;
      } catch (FileNotFoundException var8) {
         return;
      } catch (IOException var9) {
      } finally {
         IOUtils.closeQuietly(☃x);
      }
   }

   private List<PlayerProfileCache.ProfileEntry> getEntriesWithLimit(int var1) {
      List<PlayerProfileCache.ProfileEntry> ☃ = Lists.newArrayList();

      for (GameProfile ☃x : Lists.newArrayList(Iterators.limit(this.gameProfiles.iterator(), ☃))) {
         PlayerProfileCache.ProfileEntry ☃xx = this.getByUUID(☃x.getId());
         if (☃xx != null) {
            ☃.add(☃xx);
         }
      }

      return ☃;
   }

   class ProfileEntry {
      private final GameProfile gameProfile;
      private final Date expirationDate;

      private ProfileEntry(GameProfile var2, Date var3) {
         this.gameProfile = ☃;
         this.expirationDate = ☃;
      }

      public GameProfile getGameProfile() {
         return this.gameProfile;
      }

      public Date getExpirationDate() {
         return this.expirationDate;
      }
   }

   class Serializer implements JsonDeserializer<PlayerProfileCache.ProfileEntry>, JsonSerializer<PlayerProfileCache.ProfileEntry> {
      private Serializer() {
      }

      public JsonElement serialize(PlayerProfileCache.ProfileEntry var1, Type var2, JsonSerializationContext var3) {
         JsonObject ☃ = new JsonObject();
         ☃.addProperty("name", ☃.getGameProfile().getName());
         UUID ☃x = ☃.getGameProfile().getId();
         ☃.addProperty("uuid", ☃x == null ? "" : ☃x.toString());
         ☃.addProperty("expiresOn", PlayerProfileCache.DATE_FORMAT.format(☃.getExpirationDate()));
         return ☃;
      }

      public PlayerProfileCache.ProfileEntry deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         if (☃.isJsonObject()) {
            JsonObject ☃ = ☃.getAsJsonObject();
            JsonElement ☃x = ☃.get("name");
            JsonElement ☃xx = ☃.get("uuid");
            JsonElement ☃xxx = ☃.get("expiresOn");
            if (☃x != null && ☃xx != null) {
               String ☃xxxx = ☃xx.getAsString();
               String ☃xxxxx = ☃x.getAsString();
               Date ☃xxxxxx = null;
               if (☃xxx != null) {
                  try {
                     ☃xxxxxx = PlayerProfileCache.DATE_FORMAT.parse(☃xxx.getAsString());
                  } catch (ParseException var14) {
                     ☃xxxxxx = null;
                  }
               }

               if (☃xxxxx != null && ☃xxxx != null) {
                  UUID ☃xxxxxxx;
                  try {
                     ☃xxxxxxx = UUID.fromString(☃xxxx);
                  } catch (Throwable var13) {
                     return null;
                  }

                  return PlayerProfileCache.this.new ProfileEntry(new GameProfile(☃xxxxxxx, ☃xxxxx), ☃xxxxxx);
               } else {
                  return null;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }
}
