package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentArrowKnockback extends Enchantment {
   public EnchantmentArrowKnockback(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.BOW, ☃);
      this.setName("arrowKnockback");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 12 + (☃ - 1) * 20;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(☃) + 25;
   }

   @Override
   public int getMaxLevel() {
      return 2;
   }
}
