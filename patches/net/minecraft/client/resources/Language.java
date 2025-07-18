package net.minecraft.client.resources;

public class Language implements Comparable<Language> {
   private final String languageCode;
   private final String region;
   private final String name;
   private final boolean bidirectional;

   public Language(String var1, String var2, String var3, boolean var4) {
      this.languageCode = ☃;
      this.region = ☃;
      this.name = ☃;
      this.bidirectional = ☃;
   }

   public String getLanguageCode() {
      return this.languageCode;
   }

   public boolean isBidirectional() {
      return this.bidirectional;
   }

   @Override
   public String toString() {
      return String.format("%s (%s)", this.name, this.region);
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else {
         return !(☃ instanceof Language) ? false : this.languageCode.equals(((Language)☃).languageCode);
      }
   }

   @Override
   public int hashCode() {
      return this.languageCode.hashCode();
   }

   public int compareTo(Language var1) {
      return this.languageCode.compareTo(☃.languageCode);
   }
}
