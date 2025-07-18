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
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class VillagerTradeTrigger implements ICriterionTrigger<VillagerTradeTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("villager_trade");
   private final Map<PlayerAdvancements, VillagerTradeTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<VillagerTradeTrigger.Instance> var2) {
      VillagerTradeTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new VillagerTradeTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<VillagerTradeTrigger.Instance> var2) {
      VillagerTradeTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public VillagerTradeTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      EntityPredicate ☃ = EntityPredicate.deserialize(☃.get("villager"));
      ItemPredicate ☃x = ItemPredicate.deserialize(☃.get("item"));
      return new VillagerTradeTrigger.Instance(☃, ☃x);
   }

   public void trigger(EntityPlayerMP var1, EntityVillager var2, ItemStack var3) {
      VillagerTradeTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃, ☃, ☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final EntityPredicate villager;
      private final ItemPredicate item;

      public Instance(EntityPredicate var1, ItemPredicate var2) {
         super(VillagerTradeTrigger.ID);
         this.villager = ☃;
         this.item = ☃;
      }

      public boolean test(EntityPlayerMP var1, EntityVillager var2, ItemStack var3) {
         return !this.villager.test(☃, ☃) ? false : this.item.test(☃);
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<VillagerTradeTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<VillagerTradeTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<VillagerTradeTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(EntityPlayerMP var1, EntityVillager var2, ItemStack var3) {
         List<ICriterionTrigger.Listener<VillagerTradeTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<VillagerTradeTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃, ☃, ☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<VillagerTradeTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
