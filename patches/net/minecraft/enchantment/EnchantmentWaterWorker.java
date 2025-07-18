package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentWaterWorker extends Enchantment {
   public EnchantmentWaterWorker(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.ARMOR_HEAD, ☃);
      this.setName("waterWorker");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 1;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(☃) + 40;
   }

   @Override
   public int getMaxLevel() {
      return 1;
   }
}
