package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class InventoryChangeTrigger implements ICriterionTrigger<InventoryChangeTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("inventory_changed");
   private final Map<PlayerAdvancements, InventoryChangeTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> var2) {
      InventoryChangeTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new InventoryChangeTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> var2) {
      InventoryChangeTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public InventoryChangeTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      JsonObject ☃ = JsonUtils.getJsonObject(☃, "slots", new JsonObject());
      MinMaxBounds ☃x = MinMaxBounds.deserialize(☃.get("occupied"));
      MinMaxBounds ☃xx = MinMaxBounds.deserialize(☃.get("full"));
      MinMaxBounds ☃xxx = MinMaxBounds.deserialize(☃.get("empty"));
      ItemPredicate[] ☃xxxx = ItemPredicate.deserializeArray(☃.get("items"));
      return new InventoryChangeTrigger.Instance(☃x, ☃xx, ☃xxx, ☃xxxx);
   }

   public void trigger(EntityPlayerMP var1, InventoryPlayer var2) {
      InventoryChangeTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final MinMaxBounds occupied;
      private final MinMaxBounds full;
      private final MinMaxBounds empty;
      private final ItemPredicate[] items;

      public Instance(MinMaxBounds var1, MinMaxBounds var2, MinMaxBounds var3, ItemPredicate[] var4) {
         super(InventoryChangeTrigger.ID);
         this.occupied = ☃;
         this.full = ☃;
         this.empty = ☃;
         this.items = ☃;
      }

      public boolean test(InventoryPlayer var1) {
         int ☃ = 0;
         int ☃x = 0;
         int ☃xx = 0;
         List<ItemPredicate> ☃xxx = Lists.newArrayList(this.items);

         for (int ☃xxxx = 0; ☃xxxx < ☃.getSizeInventory(); ☃xxxx++) {
            ItemStack ☃xxxxx = ☃.getStackInSlot(☃xxxx);
            if (☃xxxxx.isEmpty()) {
               ☃x++;
            } else {
               ☃xx++;
               if (☃xxxxx.getCount() >= ☃xxxxx.getMaxStackSize()) {
                  ☃++;
               }

               Iterator<ItemPredicate> ☃xxxxxx = ☃xxx.iterator();

               while (☃xxxxxx.hasNext()) {
                  ItemPredicate ☃xxxxxxx = ☃xxxxxx.next();
                  if (☃xxxxxxx.test(☃xxxxx)) {
                     ☃xxxxxx.remove();
                  }
               }
            }
         }

         if (!this.full.test(☃)) {
            return false;
         } else if (!this.empty.test(☃x)) {
            return false;
         } else {
            return !this.occupied.test(☃xx) ? false : ☃xxx.isEmpty();
         }
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<InventoryChangeTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(InventoryPlayer var1) {
         List<ICriterionTrigger.Listener<InventoryChangeTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
