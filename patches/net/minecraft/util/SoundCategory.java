package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;

public enum SoundCategory {
   MASTER("master"),
   MUSIC("music"),
   RECORDS("record"),
   WEATHER("weather"),
   BLOCKS("block"),
   HOSTILE("hostile"),
   NEUTRAL("neutral"),
   PLAYERS("player"),
   AMBIENT("ambient"),
   VOICE("voice");

   private static final Map<String, SoundCategory> SOUND_CATEGORIES = Maps.newHashMap();
   private final String name;

   private SoundCategory(String var3) {
      this.name = ☃;
   }

   public String getName() {
      return this.name;
   }

   public static SoundCategory getByName(String var0) {
      return SOUND_CATEGORIES.get(☃);
   }

   public static Set<String> getSoundCategoryNames() {
      return SOUND_CATEGORIES.keySet();
   }

   static {
      for (SoundCategory ☃ : values()) {
         if (SOUND_CATEGORIES.containsKey(☃.getName())) {
            throw new Error("Clash in Sound Category name pools! Cannot insert " + ☃);
         }

         SOUND_CATEGORIES.put(☃.getName(), ☃);
      }
   }
}
