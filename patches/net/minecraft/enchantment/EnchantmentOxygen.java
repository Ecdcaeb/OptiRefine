package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentOxygen extends Enchantment {
   public EnchantmentOxygen(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.ARMOR_HEAD, ☃);
      this.setName("oxygen");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 10 * ☃;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(☃) + 30;
   }

   @Override
   public int getMaxLevel() {
      return 3;
   }
}
