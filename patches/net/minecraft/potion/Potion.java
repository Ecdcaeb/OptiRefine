package net.minecraft.potion;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryNamespaced;

public class Potion {
   public static final RegistryNamespaced<ResourceLocation, Potion> REGISTRY = new RegistryNamespaced<>();
   private final Map<IAttribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();
   private final boolean isBadEffect;
   private final int liquidColor;
   private String name = "";
   private int statusIconIndex = -1;
   private double effectiveness;
   private boolean beneficial;

   @Nullable
   public static Potion getPotionById(int var0) {
      return REGISTRY.getObjectById(☃);
   }

   public static int getIdFromPotion(Potion var0) {
      return REGISTRY.getIDForObject(☃);
   }

   @Nullable
   public static Potion getPotionFromResourceLocation(String var0) {
      return REGISTRY.getObject(new ResourceLocation(☃));
   }

   protected Potion(boolean var1, int var2) {
      this.isBadEffect = ☃;
      if (☃) {
         this.effectiveness = 0.5;
      } else {
         this.effectiveness = 1.0;
      }

      this.liquidColor = ☃;
   }

   protected Potion setIconIndex(int var1, int var2) {
      this.statusIconIndex = ☃ + ☃ * 8;
      return this;
   }

   public void performEffect(EntityLivingBase var1, int var2) {
      if (this == MobEffects.REGENERATION) {
         if (☃.getHealth() < ☃.getMaxHealth()) {
            ☃.heal(1.0F);
         }
      } else if (this == MobEffects.POISON) {
         if (☃.getHealth() > 1.0F) {
            ☃.attackEntityFrom(DamageSource.MAGIC, 1.0F);
         }
      } else if (this == MobEffects.WITHER) {
         ☃.attackEntityFrom(DamageSource.WITHER, 1.0F);
      } else if (this == MobEffects.HUNGER && ☃ instanceof EntityPlayer) {
         ((EntityPlayer)☃).addExhaustion(0.005F * (☃ + 1));
      } else if (this == MobEffects.SATURATION && ☃ instanceof EntityPlayer) {
         if (!☃.world.isRemote) {
            ((EntityPlayer)☃).getFoodStats().addStats(☃ + 1, 1.0F);
         }
      } else if ((this != MobEffects.INSTANT_HEALTH || ☃.isEntityUndead()) && (this != MobEffects.INSTANT_DAMAGE || !☃.isEntityUndead())) {
         if (this == MobEffects.INSTANT_DAMAGE && !☃.isEntityUndead() || this == MobEffects.INSTANT_HEALTH && ☃.isEntityUndead()) {
            ☃.attackEntityFrom(DamageSource.MAGIC, 6 << ☃);
         }
      } else {
         ☃.heal(Math.max(4 << ☃, 0));
      }
   }

   public void affectEntity(@Nullable Entity var1, @Nullable Entity var2, EntityLivingBase var3, int var4, double var5) {
      if ((this != MobEffects.INSTANT_HEALTH || ☃.isEntityUndead()) && (this != MobEffects.INSTANT_DAMAGE || !☃.isEntityUndead())) {
         if (this == MobEffects.INSTANT_DAMAGE && !☃.isEntityUndead() || this == MobEffects.INSTANT_HEALTH && ☃.isEntityUndead()) {
            int ☃ = (int)(☃ * (6 << ☃) + 0.5);
            if (☃ == null) {
               ☃.attackEntityFrom(DamageSource.MAGIC, ☃);
            } else {
               ☃.attackEntityFrom(DamageSource.causeIndirectMagicDamage(☃, ☃), ☃);
            }
         }
      } else {
         int ☃ = (int)(☃ * (4 << ☃) + 0.5);
         ☃.heal(☃);
      }
   }

   public boolean isReady(int var1, int var2) {
      if (this == MobEffects.REGENERATION) {
         int ☃ = 50 >> ☃;
         return ☃ > 0 ? ☃ % ☃ == 0 : true;
      } else if (this == MobEffects.POISON) {
         int ☃ = 25 >> ☃;
         return ☃ > 0 ? ☃ % ☃ == 0 : true;
      } else if (this == MobEffects.WITHER) {
         int ☃ = 40 >> ☃;
         return ☃ > 0 ? ☃ % ☃ == 0 : true;
      } else {
         return this == MobEffects.HUNGER;
      }
   }

   public boolean isInstant() {
      return false;
   }

   public Potion setPotionName(String var1) {
      this.name = ☃;
      return this;
   }

   public String getName() {
      return this.name;
   }

   public boolean hasStatusIcon() {
      return this.statusIconIndex >= 0;
   }

   public int getStatusIconIndex() {
      return this.statusIconIndex;
   }

   public boolean isBadEffect() {
      return this.isBadEffect;
   }

   public static String getPotionDurationString(PotionEffect var0, float var1) {
      if (☃.getIsPotionDurationMax()) {
         return "**:**";
      } else {
         int ☃ = MathHelper.floor(☃.getDuration() * ☃);
         return StringUtils.ticksToElapsedTime(☃);
      }
   }

   protected Potion setEffectiveness(double var1) {
      this.effectiveness = ☃;
      return this;
   }

   public int getLiquidColor() {
      return this.liquidColor;
   }

   public Potion registerPotionAttributeModifier(IAttribute var1, String var2, double var3, int var5) {
      AttributeModifier ☃ = new AttributeModifier(UUID.fromString(☃), this.getName(), ☃, ☃);
      this.attributeModifierMap.put(☃, ☃);
      return this;
   }

   public Map<IAttribute, AttributeModifier> getAttributeModifierMap() {
      return this.attributeModifierMap;
   }

   public void removeAttributesModifiersFromEntity(EntityLivingBase var1, AbstractAttributeMap var2, int var3) {
      for (Entry<IAttribute, AttributeModifier> ☃ : this.attributeModifierMap.entrySet()) {
         IAttributeInstance ☃x = ☃.getAttributeInstance(☃.getKey());
         if (☃x != null) {
            ☃x.removeModifier(☃.getValue());
         }
      }
   }

   public void applyAttributesModifiersToEntity(EntityLivingBase var1, AbstractAttributeMap var2, int var3) {
      for (Entry<IAttribute, AttributeModifier> ☃ : this.attributeModifierMap.entrySet()) {
         IAttributeInstance ☃x = ☃.getAttributeInstance(☃.getKey());
         if (☃x != null) {
            AttributeModifier ☃xx = ☃.getValue();
            ☃x.removeModifier(☃xx);
            ☃x.applyModifier(new AttributeModifier(☃xx.getID(), this.getName() + " " + ☃, this.getAttributeModifierAmount(☃, ☃xx), ☃xx.getOperation()));
         }
      }
   }

   public double getAttributeModifierAmount(int var1, AttributeModifier var2) {
      return ☃.getAmount() * (☃ + 1);
   }

   public boolean isBeneficial() {
      return this.beneficial;
   }

   public Potion setBeneficial() {
      this.beneficial = true;
      return this;
   }

   public static void registerPotions() {
      REGISTRY.register(
         1,
         new ResourceLocation("speed"),
         new Potion(false, 8171462)
            .setPotionName("effect.moveSpeed")
            .setIconIndex(0, 0)
            .registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", 0.2F, 2)
            .setBeneficial()
      );
      REGISTRY.register(
         2,
         new ResourceLocation("slowness"),
         new Potion(true, 5926017)
            .setPotionName("effect.moveSlowdown")
            .setIconIndex(1, 0)
            .registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15F, 2)
      );
      REGISTRY.register(
         3,
         new ResourceLocation("haste"),
         new Potion(false, 14270531)
            .setPotionName("effect.digSpeed")
            .setIconIndex(2, 0)
            .setEffectiveness(1.5)
            .setBeneficial()
            .registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3", 0.1F, 2)
      );
      REGISTRY.register(
         4,
         new ResourceLocation("mining_fatigue"),
         new Potion(true, 4866583)
            .setPotionName("effect.digSlowDown")
            .setIconIndex(3, 0)
            .registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "55FCED67-E92A-486E-9800-B47F202C4386", -0.1F, 2)
      );
      REGISTRY.register(
         5,
         new ResourceLocation("strength"),
         new PotionAttackDamage(false, 9643043, 3.0)
            .setPotionName("effect.damageBoost")
            .setIconIndex(4, 0)
            .registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 0.0, 0)
            .setBeneficial()
      );
      REGISTRY.register(6, new ResourceLocation("instant_health"), new PotionHealth(false, 16262179).setPotionName("effect.heal").setBeneficial());
      REGISTRY.register(7, new ResourceLocation("instant_damage"), new PotionHealth(true, 4393481).setPotionName("effect.harm").setBeneficial());
      REGISTRY.register(8, new ResourceLocation("jump_boost"), new Potion(false, 2293580).setPotionName("effect.jump").setIconIndex(2, 1).setBeneficial());
      REGISTRY.register(
         9, new ResourceLocation("nausea"), new Potion(true, 5578058).setPotionName("effect.confusion").setIconIndex(3, 1).setEffectiveness(0.25)
      );
      REGISTRY.register(
         10,
         new ResourceLocation("regeneration"),
         new Potion(false, 13458603).setPotionName("effect.regeneration").setIconIndex(7, 0).setEffectiveness(0.25).setBeneficial()
      );
      REGISTRY.register(
         11, new ResourceLocation("resistance"), new Potion(false, 10044730).setPotionName("effect.resistance").setIconIndex(6, 1).setBeneficial()
      );
      REGISTRY.register(
         12, new ResourceLocation("fire_resistance"), new Potion(false, 14981690).setPotionName("effect.fireResistance").setIconIndex(7, 1).setBeneficial()
      );
      REGISTRY.register(
         13, new ResourceLocation("water_breathing"), new Potion(false, 3035801).setPotionName("effect.waterBreathing").setIconIndex(0, 2).setBeneficial()
      );
      REGISTRY.register(
         14, new ResourceLocation("invisibility"), new Potion(false, 8356754).setPotionName("effect.invisibility").setIconIndex(0, 1).setBeneficial()
      );
      REGISTRY.register(
         15, new ResourceLocation("blindness"), new Potion(true, 2039587).setPotionName("effect.blindness").setIconIndex(5, 1).setEffectiveness(0.25)
      );
      REGISTRY.register(
         16, new ResourceLocation("night_vision"), new Potion(false, 2039713).setPotionName("effect.nightVision").setIconIndex(4, 1).setBeneficial()
      );
      REGISTRY.register(17, new ResourceLocation("hunger"), new Potion(true, 5797459).setPotionName("effect.hunger").setIconIndex(1, 1));
      REGISTRY.register(
         18,
         new ResourceLocation("weakness"),
         new PotionAttackDamage(true, 4738376, -4.0)
            .setPotionName("effect.weakness")
            .setIconIndex(5, 0)
            .registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "22653B89-116E-49DC-9B6B-9971489B5BE5", 0.0, 0)
      );
      REGISTRY.register(19, new ResourceLocation("poison"), new Potion(true, 5149489).setPotionName("effect.poison").setIconIndex(6, 0).setEffectiveness(0.25));
      REGISTRY.register(20, new ResourceLocation("wither"), new Potion(true, 3484199).setPotionName("effect.wither").setIconIndex(1, 2).setEffectiveness(0.25));
      REGISTRY.register(
         21,
         new ResourceLocation("health_boost"),
         new PotionHealthBoost(false, 16284963)
            .setPotionName("effect.healthBoost")
            .setIconIndex(7, 2)
            .registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 4.0, 0)
            .setBeneficial()
      );
      REGISTRY.register(
         22, new ResourceLocation("absorption"), new PotionAbsorption(false, 2445989).setPotionName("effect.absorption").setIconIndex(2, 2).setBeneficial()
      );
      REGISTRY.register(23, new ResourceLocation("saturation"), new PotionHealth(false, 16262179).setPotionName("effect.saturation").setBeneficial());
      REGISTRY.register(24, new ResourceLocation("glowing"), new Potion(false, 9740385).setPotionName("effect.glowing").setIconIndex(4, 2));
      REGISTRY.register(25, new ResourceLocation("levitation"), new Potion(true, 13565951).setPotionName("effect.levitation").setIconIndex(3, 2));
      REGISTRY.register(
         26,
         new ResourceLocation("luck"),
         new Potion(false, 3381504)
            .setPotionName("effect.luck")
            .setIconIndex(5, 2)
            .setBeneficial()
            .registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, "03C3C89D-7037-4B42-869F-B146BCB64D2E", 1.0, 0)
      );
      REGISTRY.register(
         27,
         new ResourceLocation("unluck"),
         new Potion(true, 12624973)
            .setPotionName("effect.unluck")
            .setIconIndex(6, 2)
            .registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, "CC5AF142-2BD2-4215-B636-2605AED11727", -1.0, 0)
      );
   }
}
