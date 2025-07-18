package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentMending extends Enchantment {
   public EnchantmentMending(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.BREAKABLE, ☃);
      this.setName("mending");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return ☃ * 25;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(☃) + 50;
   }

   @Override
   public boolean isTreasureEnchantment() {
      return true;
   }

   @Override
   public int getMaxLevel() {
      return 1;
   }
}
