package net.minecraft.enchantment;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentDigging extends Enchantment {
   protected EnchantmentDigging(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.DIGGER, ☃);
      this.setName("digging");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 1 + 10 * (☃ - 1);
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return super.getMinEnchantability(☃) + 50;
   }

   @Override
   public int getMaxLevel() {
      return 5;
   }

   @Override
   public boolean canApply(ItemStack var1) {
      return ☃.getItem() == Items.SHEARS ? true : super.canApply(☃);
   }
}
