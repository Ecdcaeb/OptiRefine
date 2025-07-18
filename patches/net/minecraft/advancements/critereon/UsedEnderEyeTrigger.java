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
import net.minecraft.util.math.BlockPos;

public class UsedEnderEyeTrigger implements ICriterionTrigger<UsedEnderEyeTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("used_ender_eye");
   private final Map<PlayerAdvancements, UsedEnderEyeTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance> var2) {
      UsedEnderEyeTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new UsedEnderEyeTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance> var2) {
      UsedEnderEyeTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public UsedEnderEyeTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      MinMaxBounds ☃ = MinMaxBounds.deserialize(☃.get("distance"));
      return new UsedEnderEyeTrigger.Instance(☃);
   }

   public void trigger(EntityPlayerMP var1, BlockPos var2) {
      UsedEnderEyeTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         double ☃x = ☃.posX - ☃.getX();
         double ☃xx = ☃.posZ - ☃.getZ();
         ☃.trigger(☃x * ☃x + ☃xx * ☃xx);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final MinMaxBounds distance;

      public Instance(MinMaxBounds var1) {
         super(UsedEnderEyeTrigger.ID);
         this.distance = ☃;
      }

      public boolean test(double var1) {
         return this.distance.testSquare(☃);
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(double var1) {
         List<ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
