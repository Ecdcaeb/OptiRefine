package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public enum BannerPattern {
   BASE("base", "b"),
   SQUARE_BOTTOM_LEFT("square_bottom_left", "bl", "   ", "   ", "#  "),
   SQUARE_BOTTOM_RIGHT("square_bottom_right", "br", "   ", "   ", "  #"),
   SQUARE_TOP_LEFT("square_top_left", "tl", "#  ", "   ", "   "),
   SQUARE_TOP_RIGHT("square_top_right", "tr", "  #", "   ", "   "),
   STRIPE_BOTTOM("stripe_bottom", "bs", "   ", "   ", "###"),
   STRIPE_TOP("stripe_top", "ts", "###", "   ", "   "),
   STRIPE_LEFT("stripe_left", "ls", "#  ", "#  ", "#  "),
   STRIPE_RIGHT("stripe_right", "rs", "  #", "  #", "  #"),
   STRIPE_CENTER("stripe_center", "cs", " # ", " # ", " # "),
   STRIPE_MIDDLE("stripe_middle", "ms", "   ", "###", "   "),
   STRIPE_DOWNRIGHT("stripe_downright", "drs", "#  ", " # ", "  #"),
   STRIPE_DOWNLEFT("stripe_downleft", "dls", "  #", " # ", "#  "),
   STRIPE_SMALL("small_stripes", "ss", "# #", "# #", "   "),
   CROSS("cross", "cr", "# #", " # ", "# #"),
   STRAIGHT_CROSS("straight_cross", "sc", " # ", "###", " # "),
   TRIANGLE_BOTTOM("triangle_bottom", "bt", "   ", " # ", "# #"),
   TRIANGLE_TOP("triangle_top", "tt", "# #", " # ", "   "),
   TRIANGLES_BOTTOM("triangles_bottom", "bts", "   ", "# #", " # "),
   TRIANGLES_TOP("triangles_top", "tts", " # ", "# #", "   "),
   DIAGONAL_LEFT("diagonal_left", "ld", "## ", "#  ", "   "),
   DIAGONAL_RIGHT("diagonal_up_right", "rd", "   ", "  #", " ##"),
   DIAGONAL_LEFT_MIRROR("diagonal_up_left", "lud", "   ", "#  ", "## "),
   DIAGONAL_RIGHT_MIRROR("diagonal_right", "rud", " ##", "  #", "   "),
   CIRCLE_MIDDLE("circle", "mc", "   ", " # ", "   "),
   RHOMBUS_MIDDLE("rhombus", "mr", " # ", "# #", " # "),
   HALF_VERTICAL("half_vertical", "vh", "## ", "## ", "## "),
   HALF_HORIZONTAL("half_horizontal", "hh", "###", "###", "   "),
   HALF_VERTICAL_MIRROR("half_vertical_right", "vhr", " ##", " ##", " ##"),
   HALF_HORIZONTAL_MIRROR("half_horizontal_bottom", "hhb", "   ", "###", "###"),
   BORDER("border", "bo", "###", "# #", "###"),
   CURLY_BORDER("curly_border", "cbo", new ItemStack(Blocks.VINE)),
   CREEPER("creeper", "cre", new ItemStack(Items.SKULL, 1, 4)),
   GRADIENT("gradient", "gra", "# #", " # ", " # "),
   GRADIENT_UP("gradient_up", "gru", " # ", " # ", "# #"),
   BRICKS("bricks", "bri", new ItemStack(Blocks.BRICK_BLOCK)),
   SKULL("skull", "sku", new ItemStack(Items.SKULL, 1, 1)),
   FLOWER("flower", "flo", new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta())),
   MOJANG("mojang", "moj", new ItemStack(Items.GOLDEN_APPLE, 1, 1));

   private final String fileName;
   private final String hashname;
   private final String[] patterns = new String[3];
   private ItemStack patternItem = ItemStack.EMPTY;

   private BannerPattern(String var3, String var4) {
      this.fileName = ☃;
      this.hashname = ☃;
   }

   private BannerPattern(String var3, String var4, ItemStack var5) {
      this(☃, ☃);
      this.patternItem = ☃;
   }

   private BannerPattern(String var3, String var4, String var5, String var6, String var7) {
      this(☃, ☃);
      this.patterns[0] = ☃;
      this.patterns[1] = ☃;
      this.patterns[2] = ☃;
   }

   public String getFileName() {
      return this.fileName;
   }

   public String getHashname() {
      return this.hashname;
   }

   public String[] getPatterns() {
      return this.patterns;
   }

   public boolean hasPattern() {
      return !this.patternItem.isEmpty() || this.patterns[0] != null;
   }

   public boolean hasPatternItem() {
      return !this.patternItem.isEmpty();
   }

   public ItemStack getPatternItem() {
      return this.patternItem;
   }

   @Nullable
   public static BannerPattern byHash(String var0) {
      for (BannerPattern ☃ : values()) {
         if (☃.hashname.equals(☃)) {
            return ☃;
         }
      }

      return null;
   }
}
