package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class StateMap extends StateMapperBase {
   private final IProperty<?> name;
   private final String suffix;
   private final List<IProperty<?>> ignored;

   private StateMap(@Nullable IProperty<?> var1, @Nullable String var2, List<IProperty<?>> var3) {
      this.name = ☃;
      this.suffix = ☃;
      this.ignored = ☃;
   }

   @Override
   protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
      Map<IProperty<?>, Comparable<?>> ☃ = Maps.newLinkedHashMap(☃.getProperties());
      String ☃x;
      if (this.name == null) {
         ☃x = Block.REGISTRY.getNameForObject(☃.getBlock()).toString();
      } else {
         ☃x = this.removeName(this.name, ☃);
      }

      if (this.suffix != null) {
         ☃x = ☃x + this.suffix;
      }

      for (IProperty<?> ☃xx : this.ignored) {
         ☃.remove(☃xx);
      }

      return new ModelResourceLocation(☃x, this.getPropertyString(☃));
   }

   private <T extends Comparable<T>> String removeName(IProperty<T> var1, Map<IProperty<?>, Comparable<?>> var2) {
      return ☃.getName((T)☃.remove(this.name));
   }

   public static class Builder {
      private IProperty<?> name;
      private String suffix;
      private final List<IProperty<?>> ignored = Lists.newArrayList();

      public StateMap.Builder withName(IProperty<?> var1) {
         this.name = ☃;
         return this;
      }

      public StateMap.Builder withSuffix(String var1) {
         this.suffix = ☃;
         return this;
      }

      public StateMap.Builder ignore(IProperty<?>... var1) {
         Collections.addAll(this.ignored, ☃);
         return this;
      }

      public StateMap build() {
         return new StateMap(this.name, this.suffix, this.ignored);
      }
   }
}
