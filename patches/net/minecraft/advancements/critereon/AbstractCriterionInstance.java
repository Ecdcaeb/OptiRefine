package net.minecraft.advancements.critereon;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.util.ResourceLocation;

public class AbstractCriterionInstance implements ICriterionInstance {
   private final ResourceLocation criterion;

   public AbstractCriterionInstance(ResourceLocation var1) {
      this.criterion = ☃;
   }

   @Override
   public ResourceLocation getId() {
      return this.criterion;
   }

   @Override
   public String toString() {
      return "AbstractCriterionInstance{criterion=" + this.criterion + '}';
   }
}
