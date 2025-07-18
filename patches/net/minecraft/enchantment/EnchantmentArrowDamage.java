package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentArrowDamage extends Enchantment {
   public EnchantmentArrowDamage(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.BOW, ☃);
      this.setName("arrowDamage");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 1 + (☃ - 1) * 10;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(☃) + 15;
   }

   @Override
   public int getMaxLevel() {
      return 5;
   }
}
