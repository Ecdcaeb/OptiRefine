package net.minecraft.world;

public enum EnumDifficulty {
   PEACEFUL(0, "options.difficulty.peaceful"),
   EASY(1, "options.difficulty.easy"),
   NORMAL(2, "options.difficulty.normal"),
   HARD(3, "options.difficulty.hard");

   private static final EnumDifficulty[] ID_MAPPING = new EnumDifficulty[values().length];
   private final int id;
   private final String translationKey;

   private EnumDifficulty(int var3, String var4) {
      this.id = ☃;
      this.translationKey = ☃;
   }

   public int getId() {
      return this.id;
   }

   public static EnumDifficulty byId(int var0) {
      return ID_MAPPING[☃ % ID_MAPPING.length];
   }

   public String getTranslationKey() {
      return this.translationKey;
   }

   static {
      for (EnumDifficulty ☃ : values()) {
         ID_MAPPING[☃.id] = ☃;
      }
   }
}
