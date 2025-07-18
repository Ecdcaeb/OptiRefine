package net.minecraft.util;

import java.util.List;
import java.util.Random;
import net.minecraft.client.gui.FontRenderer;

public class EnchantmentNameParts {
   private static final EnchantmentNameParts INSTANCE = new EnchantmentNameParts();
   private final Random rand = new Random();
   private final String[] namePartsArray = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale phnglui mglwnafh cthulhu rlyeh wgahnagl fhtagnbaguette"
      .split(" ");

   private EnchantmentNameParts() {
   }

   public static EnchantmentNameParts getInstance() {
      return INSTANCE;
   }

   public String generateNewRandomName(FontRenderer var1, int var2) {
      int ☃ = this.rand.nextInt(2) + 3;
      String ☃x = "";

      for (int ☃xx = 0; ☃xx < ☃; ☃xx++) {
         if (☃xx > 0) {
            ☃x = ☃x + " ";
         }

         ☃x = ☃x + this.namePartsArray[this.rand.nextInt(this.namePartsArray.length)];
      }

      List<String> ☃xx = ☃.listFormattedStringToWidth(☃x, ☃);
      return org.apache.commons.lang3.StringUtils.join(☃xx.size() >= 2 ? ☃xx.subList(0, 2) : ☃xx, " ");
   }

   public void reseedRandomGenerator(long var1) {
      this.rand.setSeed(☃);
   }
}
