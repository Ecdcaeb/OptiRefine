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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

public class PositionTrigger implements ICriterionTrigger<PositionTrigger.Instance> {
   private final ResourceLocation id;
   private final Map<PlayerAdvancements, PositionTrigger.Listeners> listeners = Maps.newHashMap();

   public PositionTrigger(ResourceLocation var1) {
      this.id = ☃;
   }

   @Override
   public ResourceLocation getId() {
      return this.id;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<PositionTrigger.Instance> var2) {
      PositionTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new PositionTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<PositionTrigger.Instance> var2) {
      PositionTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public PositionTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      LocationPredicate ☃ = LocationPredicate.deserialize(☃);
      return new PositionTrigger.Instance(this.id, ☃);
   }

   public void trigger(EntityPlayerMP var1) {
      PositionTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃.getServerWorld(), ☃.posX, ☃.posY, ☃.posZ);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final LocationPredicate location;

      public Instance(ResourceLocation var1, LocationPredicate var2) {
         super(☃);
         this.location = ☃;
      }

      public boolean test(WorldServer var1, double var2, double var4, double var6) {
         return this.location.test(☃, ☃, ☃, ☃);
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<PositionTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<PositionTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<PositionTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(WorldServer var1, double var2, double var4, double var6) {
         List<ICriterionTrigger.Listener<PositionTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<PositionTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃, ☃, ☃, ☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<PositionTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
