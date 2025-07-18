package net.minecraft.advancements.critereon;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class MobEffectsPredicate {
   public static final MobEffectsPredicate ANY = new MobEffectsPredicate(Collections.emptyMap());
   private final Map<Potion, MobEffectsPredicate.InstancePredicate> effects;

   public MobEffectsPredicate(Map<Potion, MobEffectsPredicate.InstancePredicate> var1) {
      this.effects = ☃;
   }

   public boolean test(Entity var1) {
      if (this == ANY) {
         return true;
      } else {
         return ☃ instanceof EntityLivingBase ? this.test(((EntityLivingBase)☃).getActivePotionMap()) : false;
      }
   }

   public boolean test(EntityLivingBase var1) {
      return this == ANY ? true : this.test(☃.getActivePotionMap());
   }

   public boolean test(Map<Potion, PotionEffect> var1) {
      if (this == ANY) {
         return true;
      } else {
         for (Entry<Potion, MobEffectsPredicate.InstancePredicate> ☃ : this.effects.entrySet()) {
            PotionEffect ☃x = ☃.get(☃.getKey());
            if (!☃.getValue().test(☃x)) {
               return false;
            }
         }

         return true;
      }
   }

   public static MobEffectsPredicate deserialize(@Nullable JsonElement var0) {
      if (☃ != null && !☃.isJsonNull()) {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "effects");
         Map<Potion, MobEffectsPredicate.InstancePredicate> ☃x = Maps.newHashMap();

         for (Entry<String, JsonElement> ☃xx : ☃.entrySet()) {
            ResourceLocation ☃xxx = new ResourceLocation(☃xx.getKey());
            Potion ☃xxxx = Potion.REGISTRY.getObject(☃xxx);
            if (☃xxxx == null) {
               throw new JsonSyntaxException("Unknown effect '" + ☃xxx + "'");
            }

            MobEffectsPredicate.InstancePredicate ☃xxxxx = MobEffectsPredicate.InstancePredicate.deserialize(
               JsonUtils.getJsonObject(☃xx.getValue(), ☃xx.getKey())
            );
            ☃x.put(☃xxxx, ☃xxxxx);
         }

         return new MobEffectsPredicate(☃x);
      } else {
         return ANY;
      }
   }

   public static class InstancePredicate {
      private final MinMaxBounds amplifier;
      private final MinMaxBounds duration;
      @Nullable
      private final Boolean ambient;
      @Nullable
      private final Boolean visible;

      public InstancePredicate(MinMaxBounds var1, MinMaxBounds var2, @Nullable Boolean var3, @Nullable Boolean var4) {
         this.amplifier = ☃;
         this.duration = ☃;
         this.ambient = ☃;
         this.visible = ☃;
      }

      public boolean test(@Nullable PotionEffect var1) {
         if (☃ == null) {
            return false;
         } else if (!this.amplifier.test(☃.getAmplifier())) {
            return false;
         } else if (!this.duration.test(☃.getDuration())) {
            return false;
         } else {
            return this.ambient != null && this.ambient != ☃.getIsAmbient() ? false : this.visible == null || this.visible == ☃.doesShowParticles();
         }
      }

      public static MobEffectsPredicate.InstancePredicate deserialize(JsonObject var0) {
         MinMaxBounds ☃ = MinMaxBounds.deserialize(☃.get("amplifier"));
         MinMaxBounds ☃x = MinMaxBounds.deserialize(☃.get("duration"));
         Boolean ☃xx = ☃.has("ambient") ? JsonUtils.getBoolean(☃, "ambient") : null;
         Boolean ☃xxx = ☃.has("visible") ? JsonUtils.getBoolean(☃, "visible") : null;
         return new MobEffectsPredicate.InstancePredicate(☃, ☃x, ☃xx, ☃xxx);
      }
   }
}
