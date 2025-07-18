package net.minecraft.advancements;

import net.minecraft.util.text.TextFormatting;

public enum FrameType {
   TASK("task", 0, TextFormatting.GREEN),
   CHALLENGE("challenge", 26, TextFormatting.DARK_PURPLE),
   GOAL("goal", 52, TextFormatting.GREEN);

   private final String name;
   private final int icon;
   private final TextFormatting format;

   private FrameType(String var3, int var4, TextFormatting var5) {
      this.name = ☃;
      this.icon = ☃;
      this.format = ☃;
   }

   public String getName() {
      return this.name;
   }

   public int getIcon() {
      return this.icon;
   }

   public static FrameType byName(String var0) {
      for (FrameType ☃ : values()) {
         if (☃.name.equals(☃)) {
            return ☃;
         }
      }

      throw new IllegalArgumentException("Unknown frame type '" + ☃ + "'");
   }

   public TextFormatting getFormat() {
      return this.format;
   }
}
