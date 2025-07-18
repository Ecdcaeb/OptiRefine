package net.minecraft.client.renderer.block.model;

import java.util.Locale;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

public class ModelResourceLocation extends ResourceLocation {
   private final String variant;

   protected ModelResourceLocation(int var1, String... var2) {
      super(0, ☃[0], ☃[1]);
      this.variant = StringUtils.isEmpty(☃[2]) ? "normal" : ☃[2].toLowerCase(Locale.ROOT);
   }

   public ModelResourceLocation(String var1) {
      this(0, parsePathString(☃));
   }

   public ModelResourceLocation(ResourceLocation var1, String var2) {
      this(☃.toString(), ☃);
   }

   public ModelResourceLocation(String var1, String var2) {
      this(0, parsePathString(☃ + '#' + (☃ == null ? "normal" : ☃)));
   }

   protected static String[] parsePathString(String var0) {
      String[] ☃ = new String[]{null, ☃, null};
      int ☃x = ☃.indexOf(35);
      String ☃xx = ☃;
      if (☃x >= 0) {
         ☃[2] = ☃.substring(☃x + 1, ☃.length());
         if (☃x > 1) {
            ☃xx = ☃.substring(0, ☃x);
         }
      }

      System.arraycopy(ResourceLocation.splitObjectName(☃xx), 0, ☃, 0, 2);
      return ☃;
   }

   public String getVariant() {
      return this.variant;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (☃ instanceof ModelResourceLocation && super.equals(☃)) {
         ModelResourceLocation ☃ = (ModelResourceLocation)☃;
         return this.variant.equals(☃.variant);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return 31 * super.hashCode() + this.variant.hashCode();
   }

   @Override
   public String toString() {
      return super.toString() + '#' + this.variant;
   }
}
