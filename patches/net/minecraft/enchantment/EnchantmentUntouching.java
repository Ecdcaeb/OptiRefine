package net.minecraft.enchantment;

import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentUntouching extends Enchantment {
   protected EnchantmentUntouching(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.DIGGER, ☃);
      this.setName("untouching");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 15;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return super.getMinEnchantability(☃) + 50;
   }

   @Override
   public int getMaxLevel() {
      return 1;
   }

   @Override
   public boolean canApplyTogether(Enchantment var1) {
      return super.canApplyTogether(☃) && ☃ != Enchantments.FORTUNE;
   }
}
