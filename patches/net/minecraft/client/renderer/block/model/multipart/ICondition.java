package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public interface ICondition {
   ICondition TRUE = new ICondition() {
      @Override
      public Predicate<IBlockState> getPredicate(BlockStateContainer var1) {
         return new Predicate<IBlockState>() {
            public boolean apply(@Nullable IBlockState var1) {
               return true;
            }
         };
      }
   };
   ICondition FALSE = new ICondition() {
      @Override
      public Predicate<IBlockState> getPredicate(BlockStateContainer var1) {
         return new Predicate<IBlockState>() {
            public boolean apply(@Nullable IBlockState var1) {
               return false;
            }
         };
      }
   };

   Predicate<IBlockState> getPredicate(BlockStateContainer var1);
}
