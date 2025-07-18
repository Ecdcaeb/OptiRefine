package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentArrowInfinite extends Enchantment {
   public EnchantmentArrowInfinite(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.BOW, ☃);
      this.setName("arrowInfinite");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 20;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return 50;
   }

   @Override
   public int getMaxLevel() {
      return 1;
   }

   @Override
   public boolean canApplyTogether(Enchantment var1) {
      return ☃ instanceof EnchantmentMending ? false : super.canApplyTogether(☃);
   }
}
