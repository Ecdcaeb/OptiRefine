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
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;

public class ConstructBeaconTrigger implements ICriterionTrigger<ConstructBeaconTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("construct_beacon");
   private final Map<PlayerAdvancements, ConstructBeaconTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance> var2) {
      ConstructBeaconTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new ConstructBeaconTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance> var2) {
      ConstructBeaconTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public ConstructBeaconTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      MinMaxBounds ☃ = MinMaxBounds.deserialize(☃.get("level"));
      return new ConstructBeaconTrigger.Instance(☃);
   }

   public void trigger(EntityPlayerMP var1, TileEntityBeacon var2) {
      ConstructBeaconTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final MinMaxBounds level;

      public Instance(MinMaxBounds var1) {
         super(ConstructBeaconTrigger.ID);
         this.level = ☃;
      }

      public boolean test(TileEntityBeacon var1) {
         return this.level.test(☃.getLevels());
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(TileEntityBeacon var1) {
         List<ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
