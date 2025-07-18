package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentSweepingEdge extends Enchantment {
   public EnchantmentSweepingEdge(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.WEAPON, ☃);
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 5 + (☃ - 1) * 9;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(☃) + 15;
   }

   @Override
   public int getMaxLevel() {
      return 3;
   }

   public static float getSweepingDamageRatio(int var0) {
      return 1.0F - 1.0F / (☃ + 1);
   }

   @Override
   public String getName() {
      return "enchantment.sweeping";
   }
}
