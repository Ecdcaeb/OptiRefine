package net.minecraft.enchantment;

import net.minecraft.util.WeightedRandom;

public class EnchantmentData extends WeightedRandom.Item {
   public final Enchantment enchantment;
   public final int enchantmentLevel;

   public EnchantmentData(Enchantment var1, int var2) {
      super(☃.getRarity().getWeight());
      this.enchantment = ☃;
      this.enchantmentLevel = ☃;
   }
}
