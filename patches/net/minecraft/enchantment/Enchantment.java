package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public abstract class Enchantment {
   public static final RegistryNamespaced<ResourceLocation, Enchantment> REGISTRY = new RegistryNamespaced<>();
   private final EntityEquipmentSlot[] applicableEquipmentTypes;
   private final Enchantment.Rarity rarity;
   @Nullable
   public EnumEnchantmentType type;
   protected String name;

   @Nullable
   public static Enchantment getEnchantmentByID(int var0) {
      return REGISTRY.getObjectById(☃);
   }

   public static int getEnchantmentID(Enchantment var0) {
      return REGISTRY.getIDForObject(☃);
   }

   @Nullable
   public static Enchantment getEnchantmentByLocation(String var0) {
      return REGISTRY.getObject(new ResourceLocation(☃));
   }

   protected Enchantment(Enchantment.Rarity var1, EnumEnchantmentType var2, EntityEquipmentSlot[] var3) {
      this.rarity = ☃;
      this.type = ☃;
      this.applicableEquipmentTypes = ☃;
   }

   public List<ItemStack> getEntityEquipment(EntityLivingBase var1) {
      List<ItemStack> ☃ = Lists.newArrayList();

      for (EntityEquipmentSlot ☃x : this.applicableEquipmentTypes) {
         ItemStack ☃xx = ☃.getItemStackFromSlot(☃x);
         if (!☃xx.isEmpty()) {
            ☃.add(☃xx);
         }
      }

      return ☃;
   }

   public Enchantment.Rarity getRarity() {
      return this.rarity;
   }

   public int getMinLevel() {
      return 1;
   }

   public int getMaxLevel() {
      return 1;
   }

   public int getMinEnchantability(int var1) {
      return 1 + ☃ * 10;
   }

   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(☃) + 5;
   }

   public int calcModifierDamage(int var1, DamageSource var2) {
      return 0;
   }

   public float calcDamageByCreature(int var1, EnumCreatureAttribute var2) {
      return 0.0F;
   }

   public final boolean isCompatibleWith(Enchantment var1) {
      return this.canApplyTogether(☃) && ☃.canApplyTogether(this);
   }

   protected boolean canApplyTogether(Enchantment var1) {
      return this != ☃;
   }

   public Enchantment setName(String var1) {
      this.name = ☃;
      return this;
   }

   public String getName() {
      return "enchantment." + this.name;
   }

   public String getTranslatedName(int var1) {
      String ☃ = I18n.translateToLocal(this.getName());
      if (this.isCurse()) {
         ☃ = TextFormatting.RED + ☃;
      }

      return ☃ == 1 && this.getMaxLevel() == 1 ? ☃ : ☃ + " " + I18n.translateToLocal("enchantment.level." + ☃);
   }

   public boolean canApply(ItemStack var1) {
      return this.type.canEnchantItem(☃.getItem());
   }

   public void onEntityDamaged(EntityLivingBase var1, Entity var2, int var3) {
   }

   public void onUserHurt(EntityLivingBase var1, Entity var2, int var3) {
   }

   public boolean isTreasureEnchantment() {
      return false;
   }

   public boolean isCurse() {
      return false;
   }

   public static void registerEnchantments() {
      EntityEquipmentSlot[] ☃ = new EntityEquipmentSlot[]{
         EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET
      };
      REGISTRY.register(0, new ResourceLocation("protection"), new EnchantmentProtection(Enchantment.Rarity.COMMON, EnchantmentProtection.Type.ALL, ☃));
      REGISTRY.register(1, new ResourceLocation("fire_protection"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.FIRE, ☃));
      REGISTRY.register(2, new ResourceLocation("feather_falling"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.FALL, ☃));
      REGISTRY.register(
         3, new ResourceLocation("blast_protection"), new EnchantmentProtection(Enchantment.Rarity.RARE, EnchantmentProtection.Type.EXPLOSION, ☃)
      );
      REGISTRY.register(
         4, new ResourceLocation("projectile_protection"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.PROJECTILE, ☃)
      );
      REGISTRY.register(5, new ResourceLocation("respiration"), new EnchantmentOxygen(Enchantment.Rarity.RARE, ☃));
      REGISTRY.register(6, new ResourceLocation("aqua_affinity"), new EnchantmentWaterWorker(Enchantment.Rarity.RARE, ☃));
      REGISTRY.register(7, new ResourceLocation("thorns"), new EnchantmentThorns(Enchantment.Rarity.VERY_RARE, ☃));
      REGISTRY.register(8, new ResourceLocation("depth_strider"), new EnchantmentWaterWalker(Enchantment.Rarity.RARE, ☃));
      REGISTRY.register(9, new ResourceLocation("frost_walker"), new EnchantmentFrostWalker(Enchantment.Rarity.RARE, EntityEquipmentSlot.FEET));
      REGISTRY.register(10, new ResourceLocation("binding_curse"), new EnchantmentBindingCurse(Enchantment.Rarity.VERY_RARE, ☃));
      REGISTRY.register(16, new ResourceLocation("sharpness"), new EnchantmentDamage(Enchantment.Rarity.COMMON, 0, EntityEquipmentSlot.MAINHAND));
      REGISTRY.register(17, new ResourceLocation("smite"), new EnchantmentDamage(Enchantment.Rarity.UNCOMMON, 1, EntityEquipmentSlot.MAINHAND));
      REGISTRY.register(18, new ResourceLocation("bane_of_arthropods"), new EnchantmentDamage(Enchantment.Rarity.UNCOMMON, 2, EntityEquipmentSlot.MAINHAND));
      REGISTRY.register(19, new ResourceLocation("knockback"), new EnchantmentKnockback(Enchantment.Rarity.UNCOMMON, EntityEquipmentSlot.MAINHAND));
      REGISTRY.register(20, new ResourceLocation("fire_aspect"), new EnchantmentFireAspect(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND));
      REGISTRY.register(
         21, new ResourceLocation("looting"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, EntityEquipmentSlot.MAINHAND)
      );
      REGISTRY.register(22, new ResourceLocation("sweeping"), new EnchantmentSweepingEdge(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND));
      REGISTRY.register(32, new ResourceLocation("efficiency"), new EnchantmentDigging(Enchantment.Rarity.COMMON, EntityEquipmentSlot.MAINHAND));
      REGISTRY.register(33, new ResourceLocation("silk_touch"), new EnchantmentUntouching(Enchantment.Rarity.VERY_RARE, EntityEquipmentSlot.MAINHAND));
      REGISTRY.register(34, new ResourceLocation("unbreaking"), new EnchantmentDurability(Enchantment.Rarity.UNCOMMON, EntityEquipmentSlot.MAINHAND));
      REGISTRY.register(
         35, new ResourceLocation("fortune"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.DIGGER, EntityEquipmentSlot.MAINHAND)
      );
      REGISTRY.register(48, new ResourceLocation("power"), new EnchantmentArrowDamage(Enchantment.Rarity.COMMON, EntityEquipmentSlot.MAINHAND));
      REGISTRY.register(49, new ResourceLocation("punch"), new EnchantmentArrowKnockback(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND));
      REGISTRY.register(50, new ResourceLocation("flame"), new EnchantmentArrowFire(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND));
      REGISTRY.register(51, new ResourceLocation("infinity"), new EnchantmentArrowInfinite(Enchantment.Rarity.VERY_RARE, EntityEquipmentSlot.MAINHAND));
      REGISTRY.register(
         61,
         new ResourceLocation("luck_of_the_sea"),
         new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.FISHING_ROD, EntityEquipmentSlot.MAINHAND)
      );
      REGISTRY.register(
         62, new ResourceLocation("lure"), new EnchantmentFishingSpeed(Enchantment.Rarity.RARE, EnumEnchantmentType.FISHING_ROD, EntityEquipmentSlot.MAINHAND)
      );
      REGISTRY.register(70, new ResourceLocation("mending"), new EnchantmentMending(Enchantment.Rarity.RARE, EntityEquipmentSlot.values()));
      REGISTRY.register(71, new ResourceLocation("vanishing_curse"), new EnchantmentVanishingCurse(Enchantment.Rarity.VERY_RARE, EntityEquipmentSlot.values()));
   }

   public static enum Rarity {
      COMMON(10),
      UNCOMMON(5),
      RARE(2),
      VERY_RARE(1);

      private final int weight;

      private Rarity(int var3) {
         this.weight = ☃;
      }

      public int getWeight() {
         return this.weight;
      }
   }
}
