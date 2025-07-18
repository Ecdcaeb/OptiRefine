package net.minecraft.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class EnchantmentDamage extends Enchantment {
   private static final String[] DAMAGE_NAMES = new String[]{"all", "undead", "arthropods"};
   private static final int[] MIN_COST = new int[]{1, 5, 5};
   private static final int[] LEVEL_COST = new int[]{11, 8, 8};
   private static final int[] LEVEL_COST_SPAN = new int[]{20, 20, 20};
   public final int damageType;

   public EnchantmentDamage(Enchantment.Rarity var1, int var2, EntityEquipmentSlot... var3) {
      super(☃, EnumEnchantmentType.WEAPON, ☃);
      this.damageType = ☃;
   }

   @Override
   public int getMinEnchantability(int var1) {
      return MIN_COST[this.damageType] + (☃ - 1) * LEVEL_COST[this.damageType];
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(☃) + LEVEL_COST_SPAN[this.damageType];
   }

   @Override
   public int getMaxLevel() {
      return 5;
   }

   @Override
   public float calcDamageByCreature(int var1, EnumCreatureAttribute var2) {
      if (this.damageType == 0) {
         return 1.0F + Math.max(0, ☃ - 1) * 0.5F;
      } else if (this.damageType == 1 && ☃ == EnumCreatureAttribute.UNDEAD) {
         return ☃ * 2.5F;
      } else {
         return this.damageType == 2 && ☃ == EnumCreatureAttribute.ARTHROPOD ? ☃ * 2.5F : 0.0F;
      }
   }

   @Override
   public String getName() {
      return "enchantment.damage." + DAMAGE_NAMES[this.damageType];
   }

   @Override
   public boolean canApplyTogether(Enchantment var1) {
      return !(☃ instanceof EnchantmentDamage);
   }

   @Override
   public boolean canApply(ItemStack var1) {
      return ☃.getItem() instanceof ItemAxe ? true : super.canApply(☃);
   }

   @Override
   public void onEntityDamaged(EntityLivingBase var1, Entity var2, int var3) {
      if (☃ instanceof EntityLivingBase) {
         EntityLivingBase ☃ = (EntityLivingBase)☃;
         if (this.damageType == 2 && ☃.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
            int ☃x = 20 + ☃.getRNG().nextInt(10 * ☃);
            ☃.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, ☃x, 3));
         }
      }
   }
}
