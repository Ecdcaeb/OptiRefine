package net.minecraft.item;

public class ItemBook extends Item {
   @Override
   public boolean isEnchantable(ItemStack var1) {
      return ☃.getCount() == 1;
   }

   @Override
   public int getItemEnchantability() {
      return 1;
   }
}
