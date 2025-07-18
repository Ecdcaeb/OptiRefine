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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantedItemTrigger implements ICriterionTrigger<EnchantedItemTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("enchanted_item");
   private final Map<PlayerAdvancements, EnchantedItemTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<EnchantedItemTrigger.Instance> var2) {
      EnchantedItemTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new EnchantedItemTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<EnchantedItemTrigger.Instance> var2) {
      EnchantedItemTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public EnchantedItemTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      ItemPredicate ☃ = ItemPredicate.deserialize(☃.get("item"));
      MinMaxBounds ☃x = MinMaxBounds.deserialize(☃.get("levels"));
      return new EnchantedItemTrigger.Instance(☃, ☃x);
   }

   public void trigger(EntityPlayerMP var1, ItemStack var2, int var3) {
      EnchantedItemTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃, ☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final ItemPredicate item;
      private final MinMaxBounds levels;

      public Instance(ItemPredicate var1, MinMaxBounds var2) {
         super(EnchantedItemTrigger.ID);
         this.item = ☃;
         this.levels = ☃;
      }

      public boolean test(ItemStack var1, int var2) {
         return !this.item.test(☃) ? false : this.levels.test(☃);
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<EnchantedItemTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<EnchantedItemTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<EnchantedItemTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(ItemStack var1, int var2) {
         List<ICriterionTrigger.Listener<EnchantedItemTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<EnchantedItemTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃, ☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<EnchantedItemTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
