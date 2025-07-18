package net.minecraft.server.management;

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
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserList<K, V extends UserListEntry<K>> {
   protected static final Logger LOGGER = LogManager.getLogger();
   protected final Gson gson;
   private final File saveFile;
   private final Map<String, V> values = Maps.newHashMap();
   private boolean lanServer = true;
   private static final ParameterizedType USER_LIST_ENTRY_TYPE = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
         return new Type[]{UserListEntry.class};
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

   public UserList(File var1) {
      this.saveFile = ☃;
      GsonBuilder ☃ = new GsonBuilder().setPrettyPrinting();
      ☃.registerTypeHierarchyAdapter(UserListEntry.class, new UserList.Serializer());
      this.gson = ☃.create();
   }

   public boolean isLanServer() {
      return this.lanServer;
   }

   public void setLanServer(boolean var1) {
      this.lanServer = ☃;
   }

   public void addEntry(V var1) {
      this.values.put(this.getObjectKey(☃.getValue()), ☃);

      try {
         this.writeChanges();
      } catch (IOException var3) {
         LOGGER.warn("Could not save the list after adding a user.", var3);
      }
   }

   public V getEntry(K var1) {
      this.removeExpired();
      return this.values.get(this.getObjectKey(☃));
   }

   public void removeEntry(K var1) {
      this.values.remove(this.getObjectKey(☃));

      try {
         this.writeChanges();
      } catch (IOException var3) {
         LOGGER.warn("Could not save the list after removing a user.", var3);
      }
   }

   public String[] getKeys() {
      return this.values.keySet().toArray(new String[this.values.size()]);
   }

   protected String getObjectKey(K var1) {
      return ☃.toString();
   }

   protected boolean hasEntry(K var1) {
      return this.values.containsKey(this.getObjectKey(☃));
   }

   private void removeExpired() {
      List<K> ☃ = Lists.newArrayList();

      for (V ☃x : this.values.values()) {
         if (☃x.hasBanExpired()) {
            ☃.add(☃x.getValue());
         }
      }

      for (K ☃xx : ☃) {
         this.values.remove(☃xx);
      }
   }

   protected UserListEntry<K> createEntry(JsonObject var1) {
      return new UserListEntry<>(null, ☃);
   }

   protected Map<String, V> getValues() {
      return this.values;
   }

   public void writeChanges() throws IOException {
      Collection<V> ☃ = this.values.values();
      String ☃x = this.gson.toJson(☃);
      BufferedWriter ☃xx = null;

      try {
         ☃xx = Files.newWriter(this.saveFile, StandardCharsets.UTF_8);
         ☃xx.write(☃x);
      } finally {
         IOUtils.closeQuietly(☃xx);
      }
   }

   class Serializer implements JsonDeserializer<UserListEntry<K>>, JsonSerializer<UserListEntry<K>> {
      private Serializer() {
      }

      public JsonElement serialize(UserListEntry<K> var1, Type var2, JsonSerializationContext var3) {
         JsonObject ☃ = new JsonObject();
         ☃.onSerialization(☃);
         return ☃;
      }

      public UserListEntry<K> deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         if (☃.isJsonObject()) {
            JsonObject ☃ = ☃.getAsJsonObject();
            return UserList.this.createEntry(☃);
         } else {
            return null;
         }
      }
   }
}
