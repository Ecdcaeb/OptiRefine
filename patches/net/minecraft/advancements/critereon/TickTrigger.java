package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class TickTrigger implements ICriterionTrigger<TickTrigger.Instance> {
   public static final ResourceLocation ID = new ResourceLocation("tick");
   private final Map<PlayerAdvancements, TickTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<TickTrigger.Instance> var2) {
      TickTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new TickTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<TickTrigger.Instance> var2) {
      TickTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public TickTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      return new TickTrigger.Instance();
   }

   public void trigger(EntityPlayerMP var1) {
      TickTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger();
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      public Instance() {
         super(TickTrigger.ID);
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<TickTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<TickTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<TickTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger() {
         for (ICriterionTrigger.Listener<TickTrigger.Instance> ☃ : Lists.newArrayList(this.listeners)) {
            ☃.grantCriterion(this.playerAdvancements);
         }
      }
   }
}
