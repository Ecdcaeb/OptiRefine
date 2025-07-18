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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class KilledTrigger implements ICriterionTrigger<KilledTrigger.Instance> {
   private final Map<PlayerAdvancements, KilledTrigger.Listeners> listeners = Maps.newHashMap();
   private final ResourceLocation id;

   public KilledTrigger(ResourceLocation var1) {
      this.id = ☃;
   }

   @Override
   public ResourceLocation getId() {
      return this.id;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<KilledTrigger.Instance> var2) {
      KilledTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new KilledTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<KilledTrigger.Instance> var2) {
      KilledTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public KilledTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      return new KilledTrigger.Instance(this.id, EntityPredicate.deserialize(☃.get("entity")), DamageSourcePredicate.deserialize(☃.get("killing_blow")));
   }

   public void trigger(EntityPlayerMP var1, Entity var2, DamageSource var3) {
      KilledTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃, ☃, ☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final EntityPredicate entity;
      private final DamageSourcePredicate killingBlow;

      public Instance(ResourceLocation var1, EntityPredicate var2, DamageSourcePredicate var3) {
         super(☃);
         this.entity = ☃;
         this.killingBlow = ☃;
      }

      public boolean test(EntityPlayerMP var1, Entity var2, DamageSource var3) {
         return !this.killingBlow.test(☃, ☃) ? false : this.entity.test(☃, ☃);
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<KilledTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<KilledTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<KilledTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(EntityPlayerMP var1, Entity var2, DamageSource var3) {
         List<ICriterionTrigger.Listener<KilledTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<KilledTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃, ☃, ☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<KilledTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
