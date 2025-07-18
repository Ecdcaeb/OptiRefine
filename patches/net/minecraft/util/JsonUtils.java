package net.minecraft.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import net.minecraft.item.Item;

public class JsonUtils {
   public static boolean isString(JsonObject var0, String var1) {
      return !isJsonPrimitive(☃, ☃) ? false : ☃.getAsJsonPrimitive(☃).isString();
   }

   public static boolean isString(JsonElement var0) {
      return !☃.isJsonPrimitive() ? false : ☃.getAsJsonPrimitive().isString();
   }

   public static boolean isNumber(JsonElement var0) {
      return !☃.isJsonPrimitive() ? false : ☃.getAsJsonPrimitive().isNumber();
   }

   public static boolean isBoolean(JsonObject var0, String var1) {
      return !isJsonPrimitive(☃, ☃) ? false : ☃.getAsJsonPrimitive(☃).isBoolean();
   }

   public static boolean isJsonArray(JsonObject var0, String var1) {
      return !hasField(☃, ☃) ? false : ☃.get(☃).isJsonArray();
   }

   public static boolean isJsonPrimitive(JsonObject var0, String var1) {
      return !hasField(☃, ☃) ? false : ☃.get(☃).isJsonPrimitive();
   }

   public static boolean hasField(JsonObject var0, String var1) {
      return ☃ == null ? false : ☃.get(☃) != null;
   }

   public static String getString(JsonElement var0, String var1) {
      if (☃.isJsonPrimitive()) {
         return ☃.getAsString();
      } else {
         throw new JsonSyntaxException("Expected " + ☃ + " to be a string, was " + toString(☃));
      }
   }

   public static String getString(JsonObject var0, String var1) {
      if (☃.has(☃)) {
         return getString(☃.get(☃), ☃);
      } else {
         throw new JsonSyntaxException("Missing " + ☃ + ", expected to find a string");
      }
   }

   public static String getString(JsonObject var0, String var1, String var2) {
      return ☃.has(☃) ? getString(☃.get(☃), ☃) : ☃;
   }

   public static Item getItem(JsonElement var0, String var1) {
      if (☃.isJsonPrimitive()) {
         String ☃ = ☃.getAsString();
         Item ☃x = Item.getByNameOrId(☃);
         if (☃x == null) {
            throw new JsonSyntaxException("Expected " + ☃ + " to be an item, was unknown string '" + ☃ + "'");
         } else {
            return ☃x;
         }
      } else {
         throw new JsonSyntaxException("Expected " + ☃ + " to be an item, was " + toString(☃));
      }
   }

   public static Item getItem(JsonObject var0, String var1) {
      if (☃.has(☃)) {
         return getItem(☃.get(☃), ☃);
      } else {
         throw new JsonSyntaxException("Missing " + ☃ + ", expected to find an item");
      }
   }

   public static boolean getBoolean(JsonElement var0, String var1) {
      if (☃.isJsonPrimitive()) {
         return ☃.getAsBoolean();
      } else {
         throw new JsonSyntaxException("Expected " + ☃ + " to be a Boolean, was " + toString(☃));
      }
   }

   public static boolean getBoolean(JsonObject var0, String var1) {
      if (☃.has(☃)) {
         return getBoolean(☃.get(☃), ☃);
      } else {
         throw new JsonSyntaxException("Missing " + ☃ + ", expected to find a Boolean");
      }
   }

   public static boolean getBoolean(JsonObject var0, String var1, boolean var2) {
      return ☃.has(☃) ? getBoolean(☃.get(☃), ☃) : ☃;
   }

   public static float getFloat(JsonElement var0, String var1) {
      if (☃.isJsonPrimitive() && ☃.getAsJsonPrimitive().isNumber()) {
         return ☃.getAsFloat();
      } else {
         throw new JsonSyntaxException("Expected " + ☃ + " to be a Float, was " + toString(☃));
      }
   }

   public static float getFloat(JsonObject var0, String var1) {
      if (☃.has(☃)) {
         return getFloat(☃.get(☃), ☃);
      } else {
         throw new JsonSyntaxException("Missing " + ☃ + ", expected to find a Float");
      }
   }

   public static float getFloat(JsonObject var0, String var1, float var2) {
      return ☃.has(☃) ? getFloat(☃.get(☃), ☃) : ☃;
   }

   public static int getInt(JsonElement var0, String var1) {
      if (☃.isJsonPrimitive() && ☃.getAsJsonPrimitive().isNumber()) {
         return ☃.getAsInt();
      } else {
         throw new JsonSyntaxException("Expected " + ☃ + " to be a Int, was " + toString(☃));
      }
   }

   public static int getInt(JsonObject var0, String var1) {
      if (☃.has(☃)) {
         return getInt(☃.get(☃), ☃);
      } else {
         throw new JsonSyntaxException("Missing " + ☃ + ", expected to find a Int");
      }
   }

   public static int getInt(JsonObject var0, String var1, int var2) {
      return ☃.has(☃) ? getInt(☃.get(☃), ☃) : ☃;
   }

   public static JsonObject getJsonObject(JsonElement var0, String var1) {
      if (☃.isJsonObject()) {
         return ☃.getAsJsonObject();
      } else {
         throw new JsonSyntaxException("Expected " + ☃ + " to be a JsonObject, was " + toString(☃));
      }
   }

   public static JsonObject getJsonObject(JsonObject var0, String var1) {
      if (☃.has(☃)) {
         return getJsonObject(☃.get(☃), ☃);
      } else {
         throw new JsonSyntaxException("Missing " + ☃ + ", expected to find a JsonObject");
      }
   }

   public static JsonObject getJsonObject(JsonObject var0, String var1, JsonObject var2) {
      return ☃.has(☃) ? getJsonObject(☃.get(☃), ☃) : ☃;
   }

   public static JsonArray getJsonArray(JsonElement var0, String var1) {
      if (☃.isJsonArray()) {
         return ☃.getAsJsonArray();
      } else {
         throw new JsonSyntaxException("Expected " + ☃ + " to be a JsonArray, was " + toString(☃));
      }
   }

   public static JsonArray getJsonArray(JsonObject var0, String var1) {
      if (☃.has(☃)) {
         return getJsonArray(☃.get(☃), ☃);
      } else {
         throw new JsonSyntaxException("Missing " + ☃ + ", expected to find a JsonArray");
      }
   }

   public static JsonArray getJsonArray(JsonObject var0, String var1, @Nullable JsonArray var2) {
      return ☃.has(☃) ? getJsonArray(☃.get(☃), ☃) : ☃;
   }

   public static <T> T deserializeClass(@Nullable JsonElement var0, String var1, JsonDeserializationContext var2, Class<? extends T> var3) {
      if (☃ != null) {
         return (T)☃.deserialize(☃, ☃);
      } else {
         throw new JsonSyntaxException("Missing " + ☃);
      }
   }

   public static <T> T deserializeClass(JsonObject var0, String var1, JsonDeserializationContext var2, Class<? extends T> var3) {
      if (☃.has(☃)) {
         return deserializeClass(☃.get(☃), ☃, ☃, ☃);
      } else {
         throw new JsonSyntaxException("Missing " + ☃);
      }
   }

   public static <T> T deserializeClass(JsonObject var0, String var1, T var2, JsonDeserializationContext var3, Class<? extends T> var4) {
      return ☃.has(☃) ? deserializeClass(☃.get(☃), ☃, ☃, ☃) : ☃;
   }

   public static String toString(JsonElement var0) {
      String ☃ = org.apache.commons.lang3.StringUtils.abbreviateMiddle(String.valueOf(☃), "...", 10);
      if (☃ == null) {
         return "null (missing)";
      } else if (☃.isJsonNull()) {
         return "null (json)";
      } else if (☃.isJsonArray()) {
         return "an array (" + ☃ + ")";
      } else if (☃.isJsonObject()) {
         return "an object (" + ☃ + ")";
      } else {
         if (☃.isJsonPrimitive()) {
            JsonPrimitive ☃x = ☃.getAsJsonPrimitive();
            if (☃x.isNumber()) {
               return "a number (" + ☃ + ")";
            }

            if (☃x.isBoolean()) {
               return "a boolean (" + ☃ + ")";
            }
         }

         return ☃;
      }
   }

   @Nullable
   public static <T> T gsonDeserialize(Gson var0, Reader var1, Class<T> var2, boolean var3) {
      try {
         JsonReader ☃ = new JsonReader(☃);
         ☃.setLenient(☃);
         return (T)☃.getAdapter(☃).read(☃);
      } catch (IOException var5) {
         throw new JsonParseException(var5);
      }
   }

   @Nullable
   public static <T> T fromJson(Gson var0, Reader var1, Type var2, boolean var3) {
      try {
         JsonReader ☃ = new JsonReader(☃);
         ☃.setLenient(☃);
         return (T)☃.getAdapter(TypeToken.get(☃)).read(☃);
      } catch (IOException var5) {
         throw new JsonParseException(var5);
      }
   }

   @Nullable
   public static <T> T fromJson(Gson var0, String var1, Type var2, boolean var3) {
      return fromJson(☃, new StringReader(☃), ☃, ☃);
   }

   @Nullable
   public static <T> T gsonDeserialize(Gson var0, String var1, Class<T> var2, boolean var3) {
      return gsonDeserialize(☃, new StringReader(☃), ☃, ☃);
   }

   @Nullable
   public static <T> T fromJson(Gson var0, Reader var1, Type var2) {
      return fromJson(☃, ☃, ☃, false);
   }

   @Nullable
   public static <T> T gsonDeserialize(Gson var0, String var1, Type var2) {
      return fromJson(☃, ☃, ☃, false);
   }

   @Nullable
   public static <T> T fromJson(Gson var0, Reader var1, Class<T> var2) {
      return gsonDeserialize(☃, ☃, ☃, false);
   }

   @Nullable
   public static <T> T gsonDeserialize(Gson var0, String var1, Class<T> var2) {
      return gsonDeserialize(☃, ☃, ☃, false);
   }
}
