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
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class RecipeUnlockedTrigger implements ICriterionTrigger<RecipeUnlockedTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("recipe_unlocked");
   private final Map<PlayerAdvancements, RecipeUnlockedTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance> var2) {
      RecipeUnlockedTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new RecipeUnlockedTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance> var2) {
      RecipeUnlockedTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public RecipeUnlockedTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      ResourceLocation ☃ = new ResourceLocation(JsonUtils.getString(☃, "recipe"));
      IRecipe ☃x = CraftingManager.getRecipe(☃);
      if (☃x == null) {
         throw new JsonSyntaxException("Unknown recipe '" + ☃ + "'");
      } else {
         return new RecipeUnlockedTrigger.Instance(☃x);
      }
   }

   public void trigger(EntityPlayerMP var1, IRecipe var2) {
      RecipeUnlockedTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final IRecipe recipe;

      public Instance(IRecipe var1) {
         super(RecipeUnlockedTrigger.ID);
         this.recipe = ☃;
      }

      public boolean test(IRecipe var1) {
         return this.recipe == ☃;
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(IRecipe var1) {
         List<ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
