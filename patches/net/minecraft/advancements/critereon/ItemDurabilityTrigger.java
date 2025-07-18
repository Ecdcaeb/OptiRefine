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

public class ItemDurabilityTrigger implements ICriterionTrigger<ItemDurabilityTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("item_durability_changed");
   private final Map<PlayerAdvancements, ItemDurabilityTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance> var2) {
      ItemDurabilityTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new ItemDurabilityTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance> var2) {
      ItemDurabilityTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public ItemDurabilityTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      ItemPredicate ☃ = ItemPredicate.deserialize(☃.get("item"));
      MinMaxBounds ☃x = MinMaxBounds.deserialize(☃.get("durability"));
      MinMaxBounds ☃xx = MinMaxBounds.deserialize(☃.get("delta"));
      return new ItemDurabilityTrigger.Instance(☃, ☃x, ☃xx);
   }

   public void trigger(EntityPlayerMP var1, ItemStack var2, int var3) {
      ItemDurabilityTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃, ☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final ItemPredicate item;
      private final MinMaxBounds durability;
      private final MinMaxBounds delta;

      public Instance(ItemPredicate var1, MinMaxBounds var2, MinMaxBounds var3) {
         super(ItemDurabilityTrigger.ID);
         this.item = ☃;
         this.durability = ☃;
         this.delta = ☃;
      }

      public boolean test(ItemStack var1, int var2) {
         if (!this.item.test(☃)) {
            return false;
         } else {
            return !this.durability.test(☃.getMaxDamage() - ☃) ? false : this.delta.test(☃.getItemDamage() - ☃);
         }
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(ItemStack var1, int var2) {
         List<ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃, ☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
