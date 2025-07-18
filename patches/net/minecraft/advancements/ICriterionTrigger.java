package net.minecraft.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

public interface ICriterionTrigger<T extends ICriterionInstance> {
   ResourceLocation getId();

   void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<T> var2);

   void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<T> var2);

   void removeAllListeners(PlayerAdvancements var1);

   T deserializeInstance(JsonObject var1, JsonDeserializationContext var2);

   public static class Listener<T extends ICriterionInstance> {
      private final T criterionInstance;
      private final Advancement advancement;
      private final String criterionName;

      public Listener(T var1, Advancement var2, String var3) {
         this.criterionInstance = ☃;
         this.advancement = ☃;
         this.criterionName = ☃;
      }

      public T getCriterionInstance() {
         return this.criterionInstance;
      }

      public void grantCriterion(PlayerAdvancements var1) {
         ☃.grantCriterion(this.advancement, this.criterionName);
      }

      @Override
      public boolean equals(Object var1) {
         if (this == ☃) {
            return true;
         } else if (☃ != null && this.getClass() == ☃.getClass()) {
            ICriterionTrigger.Listener<?> ☃ = (ICriterionTrigger.Listener<?>)☃;
            if (!this.criterionInstance.equals(☃.criterionInstance)) {
               return false;
            } else {
               return !this.advancement.equals(☃.advancement) ? false : this.criterionName.equals(☃.criterionName);
            }
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         int ☃ = this.criterionInstance.hashCode();
         ☃ = 31 * ☃ + this.advancement.hashCode();
         return 31 * ☃ + this.criterionName.hashCode();
      }
   }
}
