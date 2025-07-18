package net.minecraft.client.util;

import com.google.common.collect.Lists;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class JsonException extends IOException {
   private final List<JsonException.Entry> entries = Lists.newArrayList();
   private final String message;

   public JsonException(String var1) {
      this.entries.add(new JsonException.Entry());
      this.message = ☃;
   }

   public JsonException(String var1, Throwable var2) {
      super(☃);
      this.entries.add(new JsonException.Entry());
      this.message = ☃;
   }

   public void prependJsonKey(String var1) {
      this.entries.get(0).addJsonKey(☃);
   }

   public void setFilenameAndFlush(String var1) {
      this.entries.get(0).filename = ☃;
      this.entries.add(0, new JsonException.Entry());
   }

   @Override
   public String getMessage() {
      return "Invalid " + this.entries.get(this.entries.size() - 1) + ": " + this.message;
   }

   public static JsonException forException(Exception var0) {
      if (☃ instanceof JsonException) {
         return (JsonException)☃;
      } else {
         String ☃ = ☃.getMessage();
         if (☃ instanceof FileNotFoundException) {
            ☃ = "File not found";
         }

         return new JsonException(☃, ☃);
      }
   }

   public static class Entry {
      private String filename;
      private final List<String> jsonKeys = Lists.newArrayList();

      private Entry() {
      }

      private void addJsonKey(String var1) {
         this.jsonKeys.add(0, ☃);
      }

      public String getJsonKeys() {
         return StringUtils.join(this.jsonKeys, "->");
      }

      @Override
      public String toString() {
         if (this.filename != null) {
            return this.jsonKeys.isEmpty() ? this.filename : this.filename + " " + this.getJsonKeys();
         } else {
            return this.jsonKeys.isEmpty() ? "(Unknown file)" : "(Unknown file) " + this.getJsonKeys();
         }
      }
   }
}
