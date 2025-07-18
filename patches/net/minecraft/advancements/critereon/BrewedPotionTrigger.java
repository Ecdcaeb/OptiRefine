package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionType;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class BrewedPotionTrigger implements ICriterionTrigger<BrewedPotionTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("brewed_potion");
   private final Map<PlayerAdvancements, BrewedPotionTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<BrewedPotionTrigger.Instance> var2) {
      BrewedPotionTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new BrewedPotionTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.addListener(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<BrewedPotionTrigger.Instance> var2) {
      BrewedPotionTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ != null) {
         ☃.removeListener(☃);
         if (☃.isEmpty()) {
            this.listeners.remove(☃);
         }
      }
   }

   @Override
   public void removeAllListeners(PlayerAdvancements var1) {
      this.listeners.remove(☃);
   }

   public BrewedPotionTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      PotionType ☃ = null;
      if (☃.has("potion")) {
         ResourceLocation ☃x = new ResourceLocation(JsonUtils.getString(☃, "potion"));
         if (!PotionType.REGISTRY.containsKey(☃x)) {
            throw new JsonSyntaxException("Unknown potion '" + ☃x + "'");
         }

         ☃ = PotionType.REGISTRY.getObject(☃x);
      }

      return new BrewedPotionTrigger.Instance(☃);
   }

   public void trigger(EntityPlayerMP var1, PotionType var2) {
      BrewedPotionTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final PotionType potion;

      public Instance(@Nullable PotionType var1) {
         super(BrewedPotionTrigger.ID);
         this.potion = ☃;
      }

      public boolean test(PotionType var1) {
         return this.potion == null || this.potion == ☃;
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<BrewedPotionTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void addListener(ICriterionTrigger.Listener<BrewedPotionTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void removeListener(ICriterionTrigger.Listener<BrewedPotionTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(PotionType var1) {
         List<ICriterionTrigger.Listener<BrewedPotionTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<BrewedPotionTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<BrewedPotionTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
