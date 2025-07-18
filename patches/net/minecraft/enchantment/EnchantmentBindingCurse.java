package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentBindingCurse extends Enchantment {
   public EnchantmentBindingCurse(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.WEARABLE, ☃);
      this.setName("binding_curse");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 25;
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
   public boolean isTreasureEnchantment() {
      return true;
   }

   @Override
   public boolean isCurse() {
      return true;
   }
}
