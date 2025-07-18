package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class EffectsChangedTrigger implements ICriterionTrigger<EffectsChangedTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("effects_changed");
   private final Map<PlayerAdvancements, EffectsChangedTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<EffectsChangedTrigger.Instance> var2) {
      EffectsChangedTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new EffectsChangedTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<EffectsChangedTrigger.Instance> var2) {
      EffectsChangedTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ != null) {
         ☃.remove(☃);
         if (☃.isEmpty()) {
            this.listeners.remove(☃);
         }
      }
   }

   @Override
   public void removeAllListeners(PlayerAdvancements var1) {
      this.listeners.remove(☃);
   }

   public EffectsChangedTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      MobEffectsPredicate ☃ = MobEffectsPredicate.deserialize(☃.get("effects"));
      return new EffectsChangedTrigger.Instance(☃);
   }

   public void trigger(EntityPlayerMP var1) {
      EffectsChangedTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final MobEffectsPredicate effects;

      public Instance(MobEffectsPredicate var1) {
         super(EffectsChangedTrigger.ID);
         this.effects = ☃;
      }

      public boolean test(EntityPlayerMP var1) {
         return this.effects.test((EntityLivingBase)☃);
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<EffectsChangedTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<EffectsChangedTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<EffectsChangedTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(EntityPlayerMP var1) {
         List<ICriterionTrigger.Listener<EffectsChangedTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<EffectsChangedTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<EffectsChangedTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
