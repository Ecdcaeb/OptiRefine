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

public class PlayerHurtEntityTrigger implements ICriterionTrigger<PlayerHurtEntityTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("player_hurt_entity");
   private final Map<PlayerAdvancements, PlayerHurtEntityTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> var2) {
      PlayerHurtEntityTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new PlayerHurtEntityTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> var2) {
      PlayerHurtEntityTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public PlayerHurtEntityTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      DamagePredicate ☃ = DamagePredicate.deserialize(☃.get("damage"));
      EntityPredicate ☃x = EntityPredicate.deserialize(☃.get("entity"));
      return new PlayerHurtEntityTrigger.Instance(☃, ☃x);
   }

   public void trigger(EntityPlayerMP var1, Entity var2, DamageSource var3, float var4, float var5, boolean var6) {
      PlayerHurtEntityTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final DamagePredicate damage;
      private final EntityPredicate entity;

      public Instance(DamagePredicate var1, EntityPredicate var2) {
         super(PlayerHurtEntityTrigger.ID);
         this.damage = ☃;
         this.entity = ☃;
      }

      public boolean test(EntityPlayerMP var1, Entity var2, DamageSource var3, float var4, float var5, boolean var6) {
         return !this.damage.test(☃, ☃, ☃, ☃, ☃) ? false : this.entity.test(☃, ☃);
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(EntityPlayerMP var1, Entity var2, DamageSource var3, float var4, float var5, boolean var6) {
         List<ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃, ☃, ☃, ☃, ☃, ☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
