package net.minecraft.enchantment;

import java.util.Random;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class EnchantmentDurability extends Enchantment {
   protected EnchantmentDurability(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.BREAKABLE, ☃);
      this.setName("durability");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 5 + (☃ - 1) * 8;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return super.getMinEnchantability(☃) + 50;
   }

   @Override
   public int getMaxLevel() {
      return 3;
   }

   @Override
   public boolean canApply(ItemStack var1) {
      return ☃.isItemStackDamageable() ? true : super.canApply(☃);
   }

   public static boolean negateDamage(ItemStack var0, int var1, Random var2) {
      return ☃.getItem() instanceof ItemArmor && ☃.nextFloat() < 0.6F ? false : ☃.nextInt(☃ + 1) > 0;
   }
}
