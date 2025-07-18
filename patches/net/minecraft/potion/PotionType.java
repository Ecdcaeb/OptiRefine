package net.minecraft.potion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;

public class PotionType {
   private static final ResourceLocation EMPTY = new ResourceLocation("empty");
   public static final RegistryNamespacedDefaultedByKey<ResourceLocation, PotionType> REGISTRY = new RegistryNamespacedDefaultedByKey<>(EMPTY);
   private static int nextPotionTypeId;
   private final String baseName;
   private final ImmutableList<PotionEffect> effects;

   @Nullable
   public static PotionType getPotionTypeForName(String var0) {
      return REGISTRY.getObject(new ResourceLocation(☃));
   }

   public PotionType(PotionEffect... var1) {
      this(null, ☃);
   }

   public PotionType(@Nullable String var1, PotionEffect... var2) {
      this.baseName = ☃;
      this.effects = ImmutableList.copyOf(☃);
   }

   public String getNamePrefixed(String var1) {
      return this.baseName == null ? ☃ + REGISTRY.getNameForObject(this).getPath() : ☃ + this.baseName;
   }

   public List<PotionEffect> getEffects() {
      return this.effects;
   }

   public static void registerPotionTypes() {
      registerPotionType("empty", new PotionType());
      registerPotionType("water", new PotionType());
      registerPotionType("mundane", new PotionType());
      registerPotionType("thick", new PotionType());
      registerPotionType("awkward", new PotionType());
      registerPotionType("night_vision", new PotionType(new PotionEffect(MobEffects.NIGHT_VISION, 3600)));
      registerPotionType("long_night_vision", new PotionType("night_vision", new PotionEffect(MobEffects.NIGHT_VISION, 9600)));
      registerPotionType("invisibility", new PotionType(new PotionEffect(MobEffects.INVISIBILITY, 3600)));
      registerPotionType("long_invisibility", new PotionType("invisibility", new PotionEffect(MobEffects.INVISIBILITY, 9600)));
      registerPotionType("leaping", new PotionType(new PotionEffect(MobEffects.JUMP_BOOST, 3600)));
      registerPotionType("long_leaping", new PotionType("leaping", new PotionEffect(MobEffects.JUMP_BOOST, 9600)));
      registerPotionType("strong_leaping", new PotionType("leaping", new PotionEffect(MobEffects.JUMP_BOOST, 1800, 1)));
      registerPotionType("fire_resistance", new PotionType(new PotionEffect(MobEffects.FIRE_RESISTANCE, 3600)));
      registerPotionType("long_fire_resistance", new PotionType("fire_resistance", new PotionEffect(MobEffects.FIRE_RESISTANCE, 9600)));
      registerPotionType("swiftness", new PotionType(new PotionEffect(MobEffects.SPEED, 3600)));
      registerPotionType("long_swiftness", new PotionType("swiftness", new PotionEffect(MobEffects.SPEED, 9600)));
      registerPotionType("strong_swiftness", new PotionType("swiftness", new PotionEffect(MobEffects.SPEED, 1800, 1)));
      registerPotionType("slowness", new PotionType(new PotionEffect(MobEffects.SLOWNESS, 1800)));
      registerPotionType("long_slowness", new PotionType("slowness", new PotionEffect(MobEffects.SLOWNESS, 4800)));
      registerPotionType("water_breathing", new PotionType(new PotionEffect(MobEffects.WATER_BREATHING, 3600)));
      registerPotionType("long_water_breathing", new PotionType("water_breathing", new PotionEffect(MobEffects.WATER_BREATHING, 9600)));
      registerPotionType("healing", new PotionType(new PotionEffect(MobEffects.INSTANT_HEALTH, 1)));
      registerPotionType("strong_healing", new PotionType("healing", new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 1)));
      registerPotionType("harming", new PotionType(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1)));
      registerPotionType("strong_harming", new PotionType("harming", new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1)));
      registerPotionType("poison", new PotionType(new PotionEffect(MobEffects.POISON, 900)));
      registerPotionType("long_poison", new PotionType("poison", new PotionEffect(MobEffects.POISON, 1800)));
      registerPotionType("strong_poison", new PotionType("poison", new PotionEffect(MobEffects.POISON, 432, 1)));
      registerPotionType("regeneration", new PotionType(new PotionEffect(MobEffects.REGENERATION, 900)));
      registerPotionType("long_regeneration", new PotionType("regeneration", new PotionEffect(MobEffects.REGENERATION, 1800)));
      registerPotionType("strong_regeneration", new PotionType("regeneration", new PotionEffect(MobEffects.REGENERATION, 450, 1)));
      registerPotionType("strength", new PotionType(new PotionEffect(MobEffects.STRENGTH, 3600)));
      registerPotionType("long_strength", new PotionType("strength", new PotionEffect(MobEffects.STRENGTH, 9600)));
      registerPotionType("strong_strength", new PotionType("strength", new PotionEffect(MobEffects.STRENGTH, 1800, 1)));
      registerPotionType("weakness", new PotionType(new PotionEffect(MobEffects.WEAKNESS, 1800)));
      registerPotionType("long_weakness", new PotionType("weakness", new PotionEffect(MobEffects.WEAKNESS, 4800)));
      registerPotionType("luck", new PotionType("luck", new PotionEffect(MobEffects.LUCK, 6000)));
      REGISTRY.validateKey();
   }

   protected static void registerPotionType(String var0, PotionType var1) {
      REGISTRY.register(nextPotionTypeId++, new ResourceLocation(☃), ☃);
   }

   public boolean hasInstantEffect() {
      if (!this.effects.isEmpty()) {
         UnmodifiableIterator var1 = this.effects.iterator();

         while (var1.hasNext()) {
            PotionEffect ☃ = (PotionEffect)var1.next();
            if (☃.getPotion().isInstant()) {
               return true;
            }
         }
      }

      return false;
   }
}
