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
import net.minecraft.util.math.Vec3d;

public class LevitationTrigger implements ICriterionTrigger<LevitationTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("levitation");
   private final Map<PlayerAdvancements, LevitationTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<LevitationTrigger.Instance> var2) {
      LevitationTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new LevitationTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<LevitationTrigger.Instance> var2) {
      LevitationTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public LevitationTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      DistancePredicate ☃ = DistancePredicate.deserialize(☃.get("distance"));
      MinMaxBounds ☃x = MinMaxBounds.deserialize(☃.get("duration"));
      return new LevitationTrigger.Instance(☃, ☃x);
   }

   public void trigger(EntityPlayerMP var1, Vec3d var2, int var3) {
      LevitationTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃, ☃, ☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final DistancePredicate distance;
      private final MinMaxBounds duration;

      public Instance(DistancePredicate var1, MinMaxBounds var2) {
         super(LevitationTrigger.ID);
         this.distance = ☃;
         this.duration = ☃;
      }

      public boolean test(EntityPlayerMP var1, Vec3d var2, int var3) {
         return !this.distance.test(☃.x, ☃.y, ☃.z, ☃.posX, ☃.posY, ☃.posZ) ? false : this.duration.test(☃);
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<LevitationTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<LevitationTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<LevitationTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(EntityPlayerMP var1, Vec3d var2, int var3) {
         List<ICriterionTrigger.Listener<LevitationTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<LevitationTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃, ☃, ☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<LevitationTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
