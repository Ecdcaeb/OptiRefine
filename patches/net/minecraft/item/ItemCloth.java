package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemCloth extends ItemBlock {
   public ItemCloth(Block var1) {
      super(☃);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   @Override
   public int getMetadata(int var1) {
      return ☃;
   }

   @Override
   public String getTranslationKey(ItemStack var1) {
      return super.getTranslationKey() + "." + EnumDyeColor.byMetadata(☃.getMetadata()).getTranslationKey();
   }
}
