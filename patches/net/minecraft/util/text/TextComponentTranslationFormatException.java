package net.minecraft.util.text;

public class TextComponentTranslationFormatException extends IllegalArgumentException {
   public TextComponentTranslationFormatException(TextComponentTranslation var1, String var2) {
      super(String.format("Error parsing: %s: %s", ☃, ☃));
   }

   public TextComponentTranslationFormatException(TextComponentTranslation var1, int var2) {
      super(String.format("Invalid index %d requested for %s", ☃, ☃));
   }

   public TextComponentTranslationFormatException(TextComponentTranslation var1, Throwable var2) {
      super(String.format("Error while parsing: %s", ☃), ☃);
   }
}
