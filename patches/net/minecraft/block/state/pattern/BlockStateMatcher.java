package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class BlockStateMatcher implements Predicate<IBlockState> {
   public static final Predicate<IBlockState> ANY = new Predicate<IBlockState>() {
      public boolean apply(@Nullable IBlockState var1) {
         return true;
      }
   };
   private final BlockStateContainer blockstate;
   private final Map<IProperty<?>, Predicate<?>> propertyPredicates = Maps.newHashMap();

   private BlockStateMatcher(BlockStateContainer var1) {
      this.blockstate = ☃;
   }

   public static BlockStateMatcher forBlock(Block var0) {
      return new BlockStateMatcher(☃.getBlockState());
   }

   public boolean apply(@Nullable IBlockState var1) {
      if (☃ != null && ☃.getBlock().equals(this.blockstate.getBlock())) {
         if (this.propertyPredicates.isEmpty()) {
            return true;
         } else {
            for (Entry<IProperty<?>, Predicate<?>> ☃ : this.propertyPredicates.entrySet()) {
               if (!this.matches(☃, ☃.getKey(), ☃.getValue())) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   protected <T extends Comparable<T>> boolean matches(IBlockState var1, IProperty<T> var2, Predicate<?> var3) {
      return ☃.apply(☃.getValue(☃));
   }

   public <V extends Comparable<V>> BlockStateMatcher where(IProperty<V> var1, Predicate<? extends V> var2) {
      if (!this.blockstate.getProperties().contains(☃)) {
         throw new IllegalArgumentException(this.blockstate + " cannot support property " + ☃);
      } else {
         this.propertyPredicates.put(☃, ☃);
         return this;
      }
   }
}
