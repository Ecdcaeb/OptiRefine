package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemColored extends ItemBlock {
   private String[] subtypeNames;

   public ItemColored(Block var1, boolean var2) {
      super(☃);
      if (☃) {
         this.setMaxDamage(0);
         this.setHasSubtypes(true);
      }
   }

   @Override
   public int getMetadata(int var1) {
      return ☃;
   }

   public ItemColored setSubtypeNames(String[] var1) {
      this.subtypeNames = ☃;
      return this;
   }

   @Override
   public String getTranslationKey(ItemStack var1) {
      if (this.subtypeNames == null) {
         return super.getTranslationKey(☃);
      } else {
         int ☃ = ☃.getMetadata();
         return ☃ >= 0 && ☃ < this.subtypeNames.length ? super.getTranslationKey(☃) + "." + this.subtypeNames[☃] : super.getTranslationKey(☃);
      }
   }
}
