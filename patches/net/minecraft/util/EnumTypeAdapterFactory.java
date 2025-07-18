package net.minecraft.util;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;

public class EnumTypeAdapterFactory implements TypeAdapterFactory {
   @Nullable
   public <T> TypeAdapter<T> create(Gson var1, TypeToken<T> var2) {
      Class<T> ☃ = ☃.getRawType();
      if (!☃.isEnum()) {
         return null;
      } else {
         final Map<String, T> ☃x = Maps.newHashMap();

         for (T ☃xx : ☃.getEnumConstants()) {
            ☃x.put(this.getName(☃xx), ☃xx);
         }

         return new TypeAdapter<T>() {
            public void write(JsonWriter var1, T var2x) throws IOException {
               if (var2x == null) {
                  ☃.nullValue();
               } else {
                  ☃.value(EnumTypeAdapterFactory.this.getName(var2x));
               }
            }

            @Nullable
            public T read(JsonReader var1) throws IOException {
               if (☃.peek() == JsonToken.NULL) {
                  ☃.nextNull();
                  return null;
               } else {
                  return ☃.get(☃.nextString());
               }
            }
         };
      }
   }

   private String getName(Object var1) {
      return ☃ instanceof Enum ? ((Enum)☃).name().toLowerCase(Locale.ROOT) : ☃.toString().toLowerCase(Locale.ROOT);
   }
}
