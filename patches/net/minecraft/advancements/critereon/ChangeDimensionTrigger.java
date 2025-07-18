package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;

public class ChangeDimensionTrigger implements ICriterionTrigger<ChangeDimensionTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("changed_dimension");
   private final Map<PlayerAdvancements, ChangeDimensionTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance> var2) {
      ChangeDimensionTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new ChangeDimensionTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance> var2) {
      ChangeDimensionTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public ChangeDimensionTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      DimensionType ☃ = ☃.has("from") ? DimensionType.byName(JsonUtils.getString(☃, "from")) : null;
      DimensionType ☃x = ☃.has("to") ? DimensionType.byName(JsonUtils.getString(☃, "to")) : null;
      return new ChangeDimensionTrigger.Instance(☃, ☃x);
   }

   public void trigger(EntityPlayerMP var1, DimensionType var2, DimensionType var3) {
      ChangeDimensionTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃, ☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      @Nullable
      private final DimensionType from;
      @Nullable
      private final DimensionType to;

      public Instance(@Nullable DimensionType var1, @Nullable DimensionType var2) {
         super(ChangeDimensionTrigger.ID);
         this.from = ☃;
         this.to = ☃;
      }

      public boolean test(DimensionType var1, DimensionType var2) {
         return this.from != null && this.from != ☃ ? false : this.to == null || this.to == ☃;
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(DimensionType var1, DimensionType var2) {
         List<ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃, ☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
