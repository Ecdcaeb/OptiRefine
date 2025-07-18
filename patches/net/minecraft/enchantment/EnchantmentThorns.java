package net.minecraft.enchantment;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class EnchantmentThorns extends Enchantment {
   public EnchantmentThorns(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.ARMOR_CHEST, ☃);
      this.setName("thorns");
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
      return 3;
   }

   @Override
   public boolean canApply(ItemStack var1) {
      return ☃.getItem() instanceof ItemArmor ? true : super.canApply(☃);
   }

   @Override
   public void onUserHurt(EntityLivingBase var1, Entity var2, int var3) {
      Random ☃ = ☃.getRNG();
      ItemStack ☃x = EnchantmentHelper.getEnchantedItem(Enchantments.THORNS, ☃);
      if (shouldHit(☃, ☃)) {
         if (☃ != null) {
            ☃.attackEntityFrom(DamageSource.causeThornsDamage(☃), getDamage(☃, ☃));
         }

         if (!☃x.isEmpty()) {
            ☃x.damageItem(3, ☃);
         }
      } else if (!☃x.isEmpty()) {
         ☃x.damageItem(1, ☃);
      }
   }

   public static boolean shouldHit(int var0, Random var1) {
      return ☃ <= 0 ? false : ☃.nextFloat() < 0.15F * ☃;
   }

   public static int getDamage(int var0, Random var1) {
      return ☃ > 10 ? ☃ - 10 : 1 + ☃.nextInt(4);
   }
}
