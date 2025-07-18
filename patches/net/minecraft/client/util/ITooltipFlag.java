package net.minecraft.client.util;

public interface ITooltipFlag {
   boolean isAdvanced();

   public static enum TooltipFlags implements ITooltipFlag {
      NORMAL(false),
      ADVANCED(true);

      final boolean isAdvanced;

      private TooltipFlags(boolean var3) {
         this.isAdvanced = ☃;
      }

      @Override
      public boolean isAdvanced() {
         return this.isAdvanced;
      }
   }
}
