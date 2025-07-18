package net.minecraft.enchantment;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public class EnchantmentProtection extends Enchantment {
   public final EnchantmentProtection.Type protectionType;

   public EnchantmentProtection(Enchantment.Rarity var1, EnchantmentProtection.Type var2, EntityEquipmentSlot... var3) {
      super(☃, EnumEnchantmentType.ARMOR, ☃);
      this.protectionType = ☃;
      if (☃ == EnchantmentProtection.Type.FALL) {
         this.type = EnumEnchantmentType.ARMOR_FEET;
      }
   }

   @Override
   public int getMinEnchantability(int var1) {
      return this.protectionType.getMinimalEnchantability() + (☃ - 1) * this.protectionType.getEnchantIncreasePerLevel();
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(☃) + this.protectionType.getEnchantIncreasePerLevel();
   }

   @Override
   public int getMaxLevel() {
      return 4;
   }

   @Override
   public int calcModifierDamage(int var1, DamageSource var2) {
      if (☃.canHarmInCreative()) {
         return 0;
      } else if (this.protectionType == EnchantmentProtection.Type.ALL) {
         return ☃;
      } else if (this.protectionType == EnchantmentProtection.Type.FIRE && ☃.isFireDamage()) {
         return ☃ * 2;
      } else if (this.protectionType == EnchantmentProtection.Type.FALL && ☃ == DamageSource.FALL) {
         return ☃ * 3;
      } else if (this.protectionType == EnchantmentProtection.Type.EXPLOSION && ☃.isExplosion()) {
         return ☃ * 2;
      } else {
         return this.protectionType == EnchantmentProtection.Type.PROJECTILE && ☃.isProjectile() ? ☃ * 2 : 0;
      }
   }

   @Override
   public String getName() {
      return "enchantment.protect." + this.protectionType.getTypeName();
   }

   @Override
   public boolean canApplyTogether(Enchantment var1) {
      if (☃ instanceof EnchantmentProtection) {
         EnchantmentProtection ☃ = (EnchantmentProtection)☃;
         return this.protectionType == ☃.protectionType
            ? false
            : this.protectionType == EnchantmentProtection.Type.FALL || ☃.protectionType == EnchantmentProtection.Type.FALL;
      } else {
         return super.canApplyTogether(☃);
      }
   }

   public static int getFireTimeForEntity(EntityLivingBase var0, int var1) {
      int ☃ = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FIRE_PROTECTION, ☃);
      if (☃ > 0) {
         ☃ -= MathHelper.floor(☃ * (☃ * 0.15F));
      }

      return ☃;
   }

   public static double getBlastDamageReduction(EntityLivingBase var0, double var1) {
      int ☃ = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.BLAST_PROTECTION, ☃);
      if (☃ > 0) {
         ☃ -= MathHelper.floor(☃ * (☃ * 0.15F));
      }

      return ☃;
   }

   public static enum Type {
      ALL("all", 1, 11, 20),
      FIRE("fire", 10, 8, 12),
      FALL("fall", 5, 6, 10),
      EXPLOSION("explosion", 5, 8, 12),
      PROJECTILE("projectile", 3, 6, 15);

      private final String typeName;
      private final int minEnchantability;
      private final int levelCost;
      private final int levelCostSpan;

      private Type(String var3, int var4, int var5, int var6) {
         this.typeName = ☃;
         this.minEnchantability = ☃;
         this.levelCost = ☃;
         this.levelCostSpan = ☃;
      }

      public String getTypeName() {
         return this.typeName;
      }

      public int getMinimalEnchantability() {
         return this.minEnchantability;
      }

      public int getEnchantIncreasePerLevel() {
         return this.levelCost;
      }
   }
}
