package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentFireAspect extends Enchantment {
   protected EnchantmentFireAspect(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.WEAPON, ☃);
      this.setName("fire");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 10 + 20 * (☃ - 1);
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return super.getMinEnchantability(☃) + 50;
   }

   @Override
   public int getMaxLevel() {
      return 2;
   }
}
