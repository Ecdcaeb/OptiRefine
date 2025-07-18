package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class ConditionPropertyValue implements ICondition {
   private static final Splitter SPLITTER = Splitter.on('|').omitEmptyStrings();
   private final String key;
   private final String value;

   public ConditionPropertyValue(String var1, String var2) {
      this.key = ☃;
      this.value = ☃;
   }

   @Override
   public Predicate<IBlockState> getPredicate(BlockStateContainer var1) {
      final IProperty<?> ☃ = ☃.getProperty(this.key);
      if (☃ == null) {
         throw new RuntimeException(this.toString() + ": Definition: " + ☃ + " has no property: " + this.key);
      } else {
         String ☃x = this.value;
         boolean ☃xx = !☃x.isEmpty() && ☃x.charAt(0) == '!';
         if (☃xx) {
            ☃x = ☃x.substring(1);
         }

         List<String> ☃xxx = SPLITTER.splitToList(☃x);
         if (☃xxx.isEmpty()) {
            throw new RuntimeException(this.toString() + ": has an empty value: " + this.value);
         } else {
            Predicate<IBlockState> ☃xxxx;
            if (☃xxx.size() == 1) {
               ☃xxxx = this.makePredicate(☃, ☃x);
            } else {
               ☃xxxx = Predicates.or(Iterables.transform(☃xxx, new Function<String, Predicate<IBlockState>>() {
                  @Nullable
                  public Predicate<IBlockState> apply(@Nullable String var1) {
                     return ConditionPropertyValue.this.makePredicate(☃, ☃);
                  }
               }));
            }

            return ☃xx ? Predicates.not(☃xxxx) : ☃xxxx;
         }
      }
   }

   private Predicate<IBlockState> makePredicate(final IProperty<?> var1, String var2) {
      final Optional<?> ☃ = ☃.parseValue(☃);
      if (!☃.isPresent()) {
         throw new RuntimeException(this.toString() + ": has an unknown value: " + this.value);
      } else {
         return new Predicate<IBlockState>() {
            public boolean apply(@Nullable IBlockState var1x) {
               return ☃ != null && ☃.getValue(☃).equals(☃.get());
            }
         };
      }
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this).add("key", this.key).add("value", this.value).toString();
   }
}
