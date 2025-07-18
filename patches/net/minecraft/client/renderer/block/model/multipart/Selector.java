package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.VariantList;
import net.minecraft.util.JsonUtils;

public class Selector {
   private final ICondition condition;
   private final VariantList variantList;

   public Selector(ICondition var1, VariantList var2) {
      if (☃ == null) {
         throw new IllegalArgumentException("Missing condition for selector");
      } else if (☃ == null) {
         throw new IllegalArgumentException("Missing variant for selector");
      } else {
         this.condition = ☃;
         this.variantList = ☃;
      }
   }

   public VariantList getVariantList() {
      return this.variantList;
   }

   public Predicate<IBlockState> getPredicate(BlockStateContainer var1) {
      return this.condition.getPredicate(☃);
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else {
         if (☃ instanceof Selector) {
            Selector ☃ = (Selector)☃;
            if (this.condition.equals(☃.condition)) {
               return this.variantList.equals(☃.variantList);
            }
         }

         return false;
      }
   }

   @Override
   public int hashCode() {
      return 31 * this.condition.hashCode() + this.variantList.hashCode();
   }

   public static class Deserializer implements JsonDeserializer<Selector> {
      private static final Function<JsonElement, ICondition> FUNCTION_OR_AND = new Function<JsonElement, ICondition>() {
         @Nullable
         public ICondition apply(@Nullable JsonElement var1) {
            return ☃ == null ? null : Selector.Deserializer.getOrAndCondition(☃.getAsJsonObject());
         }
      };
      private static final Function<Entry<String, JsonElement>, ICondition> FUNCTION_PROPERTY_VALUE = new Function<Entry<String, JsonElement>, ICondition>() {
         @Nullable
         public ICondition apply(@Nullable Entry<String, JsonElement> var1) {
            return ☃ == null ? null : Selector.Deserializer.makePropertyValue(☃);
         }
      };

      public Selector deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = ☃.getAsJsonObject();
         return new Selector(this.getWhenCondition(☃), (VariantList)☃.deserialize(☃.get("apply"), VariantList.class));
      }

      private ICondition getWhenCondition(JsonObject var1) {
         return ☃.has("when") ? getOrAndCondition(JsonUtils.getJsonObject(☃, "when")) : ICondition.TRUE;
      }

      @VisibleForTesting
      static ICondition getOrAndCondition(JsonObject var0) {
         Set<Entry<String, JsonElement>> ☃ = ☃.entrySet();
         if (☃.isEmpty()) {
            throw new JsonParseException("No elements found in selector");
         } else if (☃.size() == 1) {
            if (☃.has("OR")) {
               return new ConditionOr(Iterables.transform(JsonUtils.getJsonArray(☃, "OR"), FUNCTION_OR_AND));
            } else {
               return (ICondition)(☃.has("AND")
                  ? new ConditionAnd(Iterables.transform(JsonUtils.getJsonArray(☃, "AND"), FUNCTION_OR_AND))
                  : makePropertyValue(☃.iterator().next()));
            }
         } else {
            return new ConditionAnd(Iterables.transform(☃, FUNCTION_PROPERTY_VALUE));
         }
      }

      private static ConditionPropertyValue makePropertyValue(Entry<String, JsonElement> var0) {
         return new ConditionPropertyValue(☃.getKey(), ☃.getValue().getAsString());
      }
   }
}
