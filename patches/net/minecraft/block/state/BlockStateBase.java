package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public abstract class BlockStateBase implements IBlockState {
   private static final Joiner COMMA_JOINER = Joiner.on(',');
   private static final Function<Entry<IProperty<?>, Comparable<?>>, String> MAP_ENTRY_TO_STRING = new Function<Entry<IProperty<?>, Comparable<?>>, String>() {
      @Nullable
      public String apply(@Nullable Entry<IProperty<?>, Comparable<?>> var1) {
         if (☃ == null) {
            return "<NULL>";
         } else {
            IProperty<?> ☃ = ☃.getKey();
            return ☃.getName() + "=" + this.getPropertyName(☃, ☃.getValue());
         }
      }

      private <T extends Comparable<T>> String getPropertyName(IProperty<T> var1, Comparable<?> var2) {
         return ☃.getName((T)☃);
      }
   };

   @Override
   public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> var1) {
      return this.withProperty(☃, cyclePropertyValue(☃.getAllowedValues(), this.getValue(☃)));
   }

   protected static <T> T cyclePropertyValue(Collection<T> var0, T var1) {
      Iterator<T> ☃ = ☃.iterator();

      while (☃.hasNext()) {
         if (☃.next().equals(☃)) {
            if (☃.hasNext()) {
               return ☃.next();
            }

            return ☃.iterator().next();
         }
      }

      return ☃.next();
   }

   @Override
   public String toString() {
      StringBuilder ☃ = new StringBuilder();
      ☃.append(Block.REGISTRY.getNameForObject(this.getBlock()));
      if (!this.getProperties().isEmpty()) {
         ☃.append("[");
         COMMA_JOINER.appendTo(☃, Iterables.transform(this.getProperties().entrySet(), MAP_ENTRY_TO_STRING));
         ☃.append("]");
      }

      return ☃.toString();
   }
}
