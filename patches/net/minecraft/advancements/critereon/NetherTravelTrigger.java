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
import net.minecraft.world.WorldServer;

public class NetherTravelTrigger implements ICriterionTrigger<NetherTravelTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("nether_travel");
   private final Map<PlayerAdvancements, NetherTravelTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<NetherTravelTrigger.Instance> var2) {
      NetherTravelTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new NetherTravelTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<NetherTravelTrigger.Instance> var2) {
      NetherTravelTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public NetherTravelTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      LocationPredicate ☃ = LocationPredicate.deserialize(☃.get("entered"));
      LocationPredicate ☃x = LocationPredicate.deserialize(☃.get("exited"));
      DistancePredicate ☃xx = DistancePredicate.deserialize(☃.get("distance"));
      return new NetherTravelTrigger.Instance(☃, ☃x, ☃xx);
   }

   public void trigger(EntityPlayerMP var1, Vec3d var2) {
      NetherTravelTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃.getServerWorld(), ☃, ☃.posX, ☃.posY, ☃.posZ);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final LocationPredicate entered;
      private final LocationPredicate exited;
      private final DistancePredicate distance;

      public Instance(LocationPredicate var1, LocationPredicate var2, DistancePredicate var3) {
         super(NetherTravelTrigger.ID);
         this.entered = ☃;
         this.exited = ☃;
         this.distance = ☃;
      }

      public boolean test(WorldServer var1, Vec3d var2, double var3, double var5, double var7) {
         if (!this.entered.test(☃, ☃.x, ☃.y, ☃.z)) {
            return false;
         } else {
            return !this.exited.test(☃, ☃, ☃, ☃) ? false : this.distance.test(☃.x, ☃.y, ☃.z, ☃, ☃, ☃);
         }
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<NetherTravelTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<NetherTravelTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<NetherTravelTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(WorldServer var1, Vec3d var2, double var3, double var5, double var7) {
         List<ICriterionTrigger.Listener<NetherTravelTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<NetherTravelTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃, ☃, ☃, ☃, ☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<NetherTravelTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
