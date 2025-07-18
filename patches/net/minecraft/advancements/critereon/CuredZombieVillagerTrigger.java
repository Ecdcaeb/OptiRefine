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
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class CuredZombieVillagerTrigger implements ICriterionTrigger<CuredZombieVillagerTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("cured_zombie_villager");
   private final Map<PlayerAdvancements, CuredZombieVillagerTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> var2) {
      CuredZombieVillagerTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new CuredZombieVillagerTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> var2) {
      CuredZombieVillagerTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public CuredZombieVillagerTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      EntityPredicate ☃ = EntityPredicate.deserialize(☃.get("zombie"));
      EntityPredicate ☃x = EntityPredicate.deserialize(☃.get("villager"));
      return new CuredZombieVillagerTrigger.Instance(☃, ☃x);
   }

   public void trigger(EntityPlayerMP var1, EntityZombie var2, EntityVillager var3) {
      CuredZombieVillagerTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃, ☃, ☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final EntityPredicate zombie;
      private final EntityPredicate villager;

      public Instance(EntityPredicate var1, EntityPredicate var2) {
         super(CuredZombieVillagerTrigger.ID);
         this.zombie = ☃;
         this.villager = ☃;
      }

      public boolean test(EntityPlayerMP var1, EntityZombie var2, EntityVillager var3) {
         return !this.zombie.test(☃, ☃) ? false : this.villager.test(☃, ☃);
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(EntityPlayerMP var1, EntityZombie var2, EntityVillager var3) {
         List<ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃, ☃, ☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
