package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import javax.annotation.Nullable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class ConditionOr implements ICondition {
   final Iterable<ICondition> conditions;

   public ConditionOr(Iterable<ICondition> var1) {
      this.conditions = ☃;
   }

   @Override
   public Predicate<IBlockState> getPredicate(final BlockStateContainer var1) {
      return Predicates.or(Iterables.transform(this.conditions, new Function<ICondition, Predicate<IBlockState>>() {
         @Nullable
         public Predicate<IBlockState> apply(@Nullable ICondition var1x) {
            return ☃ == null ? null : ☃.getPredicate(☃);
         }
      }));
   }
}
