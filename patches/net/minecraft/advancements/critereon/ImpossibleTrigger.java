package net.minecraft.advancements.critereon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.util.ResourceLocation;

public class ImpossibleTrigger implements ICriterionTrigger<ImpossibleTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("impossible");

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<ImpossibleTrigger.Instance> var2) {
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<ImpossibleTrigger.Instance> var2) {
   }

   @Override
   public void removeAllListeners(PlayerAdvancements var1) {
   }

   public ImpossibleTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      return new ImpossibleTrigger.Instance();
   }

   public static class Instance extends AbstractCriterionInstance {
      public Instance() {
         super(ImpossibleTrigger.ID);
      }
   }
}
