package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Util;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.MathHelper;

public class EnchantmentHelper {
   private static final EnchantmentHelper.ModifierDamage ENCHANTMENT_MODIFIER_DAMAGE = new EnchantmentHelper.ModifierDamage();
   private static final EnchantmentHelper.ModifierLiving ENCHANTMENT_MODIFIER_LIVING = new EnchantmentHelper.ModifierLiving();
   private static final EnchantmentHelper.HurtIterator ENCHANTMENT_ITERATOR_HURT = new EnchantmentHelper.HurtIterator();
   private static final EnchantmentHelper.DamageIterator ENCHANTMENT_ITERATOR_DAMAGE = new EnchantmentHelper.DamageIterator();

   public static int getEnchantmentLevel(Enchantment var0, ItemStack var1) {
      if (☃.isEmpty()) {
         return 0;
      } else {
         NBTTagList ☃ = ☃.getEnchantmentTagList();

         for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
            NBTTagCompound ☃xx = ☃.getCompoundTagAt(☃x);
            Enchantment ☃xxx = Enchantment.getEnchantmentByID(☃xx.getShort("id"));
            int ☃xxxx = ☃xx.getShort("lvl");
            if (☃xxx == ☃) {
               return ☃xxxx;
            }
         }

         return 0;
      }
   }

   public static Map<Enchantment, Integer> getEnchantments(ItemStack var0) {
      Map<Enchantment, Integer> ☃ = Maps.newLinkedHashMap();
      NBTTagList ☃x = ☃.getItem() == Items.ENCHANTED_BOOK ? ItemEnchantedBook.getEnchantments(☃) : ☃.getEnchantmentTagList();

      for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
         NBTTagCompound ☃xxx = ☃x.getCompoundTagAt(☃xx);
         Enchantment ☃xxxx = Enchantment.getEnchantmentByID(☃xxx.getShort("id"));
         int ☃xxxxx = ☃xxx.getShort("lvl");
         ☃.put(☃xxxx, ☃xxxxx);
      }

      return ☃;
   }

   public static void setEnchantments(Map<Enchantment, Integer> var0, ItemStack var1) {
      NBTTagList ☃ = new NBTTagList();

      for (Entry<Enchantment, Integer> ☃x : ☃.entrySet()) {
         Enchantment ☃xx = ☃x.getKey();
         if (☃xx != null) {
            int ☃xxx = ☃x.getValue();
            NBTTagCompound ☃xxxx = new NBTTagCompound();
            ☃xxxx.setShort("id", (short)Enchantment.getEnchantmentID(☃xx));
            ☃xxxx.setShort("lvl", (short)☃xxx);
            ☃.appendTag(☃xxxx);
            if (☃.getItem() == Items.ENCHANTED_BOOK) {
               ItemEnchantedBook.addEnchantment(☃, new EnchantmentData(☃xx, ☃xxx));
            }
         }
      }

      if (☃.isEmpty()) {
         if (☃.hasTagCompound()) {
            ☃.getTagCompound().removeTag("ench");
         }
      } else if (☃.getItem() != Items.ENCHANTED_BOOK) {
         ☃.setTagInfo("ench", ☃);
      }
   }

   private static void applyEnchantmentModifier(EnchantmentHelper.IModifier var0, ItemStack var1) {
      if (!☃.isEmpty()) {
         NBTTagList ☃ = ☃.getEnchantmentTagList();

         for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
            int ☃xx = ☃.getCompoundTagAt(☃x).getShort("id");
            int ☃xxx = ☃.getCompoundTagAt(☃x).getShort("lvl");
            if (Enchantment.getEnchantmentByID(☃xx) != null) {
               ☃.calculateModifier(Enchantment.getEnchantmentByID(☃xx), ☃xxx);
            }
         }
      }
   }

   private static void applyEnchantmentModifierArray(EnchantmentHelper.IModifier var0, Iterable<ItemStack> var1) {
      for (ItemStack ☃ : ☃) {
         applyEnchantmentModifier(☃, ☃);
      }
   }

   public static int getEnchantmentModifierDamage(Iterable<ItemStack> var0, DamageSource var1) {
      ENCHANTMENT_MODIFIER_DAMAGE.damageModifier = 0;
      ENCHANTMENT_MODIFIER_DAMAGE.source = ☃;
      applyEnchantmentModifierArray(ENCHANTMENT_MODIFIER_DAMAGE, ☃);
      return ENCHANTMENT_MODIFIER_DAMAGE.damageModifier;
   }

   public static float getModifierForCreature(ItemStack var0, EnumCreatureAttribute var1) {
      ENCHANTMENT_MODIFIER_LIVING.livingModifier = 0.0F;
      ENCHANTMENT_MODIFIER_LIVING.entityLiving = ☃;
      applyEnchantmentModifier(ENCHANTMENT_MODIFIER_LIVING, ☃);
      return ENCHANTMENT_MODIFIER_LIVING.livingModifier;
   }

   public static float getSweepingDamageRatio(EntityLivingBase var0) {
      int ☃ = getMaxEnchantmentLevel(Enchantments.SWEEPING, ☃);
      return ☃ > 0 ? EnchantmentSweepingEdge.getSweepingDamageRatio(☃) : 0.0F;
   }

   public static void applyThornEnchantments(EntityLivingBase var0, Entity var1) {
      ENCHANTMENT_ITERATOR_HURT.attacker = ☃;
      ENCHANTMENT_ITERATOR_HURT.user = ☃;
      if (☃ != null) {
         applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_HURT, ☃.getEquipmentAndArmor());
      }

      if (☃ instanceof EntityPlayer) {
         applyEnchantmentModifier(ENCHANTMENT_ITERATOR_HURT, ☃.getHeldItemMainhand());
      }
   }

   public static void applyArthropodEnchantments(EntityLivingBase var0, Entity var1) {
      ENCHANTMENT_ITERATOR_DAMAGE.user = ☃;
      ENCHANTMENT_ITERATOR_DAMAGE.target = ☃;
      if (☃ != null) {
         applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_DAMAGE, ☃.getEquipmentAndArmor());
      }

      if (☃ instanceof EntityPlayer) {
         applyEnchantmentModifier(ENCHANTMENT_ITERATOR_DAMAGE, ☃.getHeldItemMainhand());
      }
   }

   public static int getMaxEnchantmentLevel(Enchantment var0, EntityLivingBase var1) {
      Iterable<ItemStack> ☃ = ☃.getEntityEquipment(☃);
      if (☃ == null) {
         return 0;
      } else {
         int ☃x = 0;

         for (ItemStack ☃xx : ☃) {
            int ☃xxx = getEnchantmentLevel(☃, ☃xx);
            if (☃xxx > ☃x) {
               ☃x = ☃xxx;
            }
         }

         return ☃x;
      }
   }

   public static int getKnockbackModifier(EntityLivingBase var0) {
      return getMaxEnchantmentLevel(Enchantments.KNOCKBACK, ☃);
   }

   public static int getFireAspectModifier(EntityLivingBase var0) {
      return getMaxEnchantmentLevel(Enchantments.FIRE_ASPECT, ☃);
   }

   public static int getRespirationModifier(EntityLivingBase var0) {
      return getMaxEnchantmentLevel(Enchantments.RESPIRATION, ☃);
   }

   public static int getDepthStriderModifier(EntityLivingBase var0) {
      return getMaxEnchantmentLevel(Enchantments.DEPTH_STRIDER, ☃);
   }

   public static int getEfficiencyModifier(EntityLivingBase var0) {
      return getMaxEnchantmentLevel(Enchantments.EFFICIENCY, ☃);
   }

   public static int getFishingLuckBonus(ItemStack var0) {
      return getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, ☃);
   }

   public static int getFishingSpeedBonus(ItemStack var0) {
      return getEnchantmentLevel(Enchantments.LURE, ☃);
   }

   public static int getLootingModifier(EntityLivingBase var0) {
      return getMaxEnchantmentLevel(Enchantments.LOOTING, ☃);
   }

   public static boolean getAquaAffinityModifier(EntityLivingBase var0) {
      return getMaxEnchantmentLevel(Enchantments.AQUA_AFFINITY, ☃) > 0;
   }

   public static boolean hasFrostWalkerEnchantment(EntityLivingBase var0) {
      return getMaxEnchantmentLevel(Enchantments.FROST_WALKER, ☃) > 0;
   }

   public static boolean hasBindingCurse(ItemStack var0) {
      return getEnchantmentLevel(Enchantments.BINDING_CURSE, ☃) > 0;
   }

   public static boolean hasVanishingCurse(ItemStack var0) {
      return getEnchantmentLevel(Enchantments.VANISHING_CURSE, ☃) > 0;
   }

   public static ItemStack getEnchantedItem(Enchantment var0, EntityLivingBase var1) {
      List<ItemStack> ☃ = ☃.getEntityEquipment(☃);
      if (☃.isEmpty()) {
         return ItemStack.EMPTY;
      } else {
         List<ItemStack> ☃x = Lists.newArrayList();

         for (ItemStack ☃xx : ☃) {
            if (!☃xx.isEmpty() && getEnchantmentLevel(☃, ☃xx) > 0) {
               ☃x.add(☃xx);
            }
         }

         return ☃x.isEmpty() ? ItemStack.EMPTY : ☃x.get(☃.getRNG().nextInt(☃x.size()));
      }
   }

   public static int calcItemStackEnchantability(Random var0, int var1, int var2, ItemStack var3) {
      Item ☃ = ☃.getItem();
      int ☃x = ☃.getItemEnchantability();
      if (☃x <= 0) {
         return 0;
      } else {
         if (☃ > 15) {
            ☃ = 15;
         }

         int ☃xx = ☃.nextInt(8) + 1 + (☃ >> 1) + ☃.nextInt(☃ + 1);
         if (☃ == 0) {
            return Math.max(☃xx / 3, 1);
         } else {
            return ☃ == 1 ? ☃xx * 2 / 3 + 1 : Math.max(☃xx, ☃ * 2);
         }
      }
   }

   public static ItemStack addRandomEnchantment(Random var0, ItemStack var1, int var2, boolean var3) {
      List<EnchantmentData> ☃ = buildEnchantmentList(☃, ☃, ☃, ☃);
      boolean ☃x = ☃.getItem() == Items.BOOK;
      if (☃x) {
         ☃ = new ItemStack(Items.ENCHANTED_BOOK);
      }

      for (EnchantmentData ☃xx : ☃) {
         if (☃x) {
            ItemEnchantedBook.addEnchantment(☃, ☃xx);
         } else {
            ☃.addEnchantment(☃xx.enchantment, ☃xx.enchantmentLevel);
         }
      }

      return ☃;
   }

   public static List<EnchantmentData> buildEnchantmentList(Random var0, ItemStack var1, int var2, boolean var3) {
      List<EnchantmentData> ☃ = Lists.newArrayList();
      Item ☃x = ☃.getItem();
      int ☃xx = ☃x.getItemEnchantability();
      if (☃xx <= 0) {
         return ☃;
      } else {
         ☃ += 1 + ☃.nextInt(☃xx / 4 + 1) + ☃.nextInt(☃xx / 4 + 1);
         float ☃xxx = (☃.nextFloat() + ☃.nextFloat() - 1.0F) * 0.15F;
         ☃ = MathHelper.clamp(Math.round(☃ + ☃ * ☃xxx), 1, Integer.MAX_VALUE);
         List<EnchantmentData> ☃xxxx = getEnchantmentDatas(☃, ☃, ☃);
         if (!☃xxxx.isEmpty()) {
            ☃.add(WeightedRandom.getRandomItem(☃, ☃xxxx));

            while (☃.nextInt(50) <= ☃) {
               removeIncompatible(☃xxxx, Util.getLastElement(☃));
               if (☃xxxx.isEmpty()) {
                  break;
               }

               ☃.add(WeightedRandom.getRandomItem(☃, ☃xxxx));
               ☃ /= 2;
            }
         }

         return ☃;
      }
   }

   public static void removeIncompatible(List<EnchantmentData> var0, EnchantmentData var1) {
      Iterator<EnchantmentData> ☃ = ☃.iterator();

      while (☃.hasNext()) {
         if (!☃.enchantment.isCompatibleWith(☃.next().enchantment)) {
            ☃.remove();
         }
      }
   }

   public static List<EnchantmentData> getEnchantmentDatas(int var0, ItemStack var1, boolean var2) {
      List<EnchantmentData> ☃ = Lists.newArrayList();
      Item ☃x = ☃.getItem();
      boolean ☃xx = ☃.getItem() == Items.BOOK;

      for (Enchantment ☃xxx : Enchantment.REGISTRY) {
         if ((!☃xxx.isTreasureEnchantment() || ☃) && (☃xxx.type.canEnchantItem(☃x) || ☃xx)) {
            for (int ☃xxxx = ☃xxx.getMaxLevel(); ☃xxxx > ☃xxx.getMinLevel() - 1; ☃xxxx--) {
               if (☃ >= ☃xxx.getMinEnchantability(☃xxxx) && ☃ <= ☃xxx.getMaxEnchantability(☃xxxx)) {
                  ☃.add(new EnchantmentData(☃xxx, ☃xxxx));
                  break;
               }
            }
         }
      }

      return ☃;
   }

   static final class DamageIterator implements EnchantmentHelper.IModifier {
      public EntityLivingBase user;
      public Entity target;

      private DamageIterator() {
      }

      @Override
      public void calculateModifier(Enchantment var1, int var2) {
         ☃.onEntityDamaged(this.user, this.target, ☃);
      }
   }

   static final class HurtIterator implements EnchantmentHelper.IModifier {
      public EntityLivingBase user;
      public Entity attacker;

      private HurtIterator() {
      }

      @Override
      public void calculateModifier(Enchantment var1, int var2) {
         ☃.onUserHurt(this.user, this.attacker, ☃);
      }
   }

   interface IModifier {
      void calculateModifier(Enchantment var1, int var2);
   }

   static final class ModifierDamage implements EnchantmentHelper.IModifier {
      public int damageModifier;
      public DamageSource source;

      private ModifierDamage() {
      }

      @Override
      public void calculateModifier(Enchantment var1, int var2) {
         this.damageModifier = this.damageModifier + ☃.calcModifierDamage(☃, this.source);
      }
   }

   static final class ModifierLiving implements EnchantmentHelper.IModifier {
      public float livingModifier;
      public EnumCreatureAttribute entityLiving;

      private ModifierLiving() {
      }

      @Override
      public void calculateModifier(Enchantment var1, int var2) {
         this.livingModifier = this.livingModifier + ☃.calcDamageByCreature(☃, this.entityLiving);
      }
   }
}
