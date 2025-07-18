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

public class UsedTotemTrigger implements ICriterionTrigger<UsedTotemTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("used_totem");
   private final Map<PlayerAdvancements, UsedTotemTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<UsedTotemTrigger.Instance> var2) {
      UsedTotemTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new UsedTotemTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<UsedTotemTrigger.Instance> var2) {
      UsedTotemTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public UsedTotemTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      ItemPredicate ☃ = ItemPredicate.deserialize(☃.get("item"));
      return new UsedTotemTrigger.Instance(☃);
   }

   public void trigger(EntityPlayerMP var1, ItemStack var2) {
      UsedTotemTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final ItemPredicate item;

      public Instance(ItemPredicate var1) {
         super(UsedTotemTrigger.ID);
         this.item = ☃;
      }

      public boolean test(ItemStack var1) {
         return this.item.test(☃);
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<UsedTotemTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<UsedTotemTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<UsedTotemTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(ItemStack var1) {
         List<ICriterionTrigger.Listener<UsedTotemTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<UsedTotemTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<UsedTotemTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
