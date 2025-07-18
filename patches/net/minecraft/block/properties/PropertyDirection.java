package net.minecraft.block.properties;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.util.EnumFacing;

public class PropertyDirection extends PropertyEnum<EnumFacing> {
   protected PropertyDirection(String var1, Collection<EnumFacing> var2) {
      super(☃, EnumFacing.class, ☃);
   }

   public static PropertyDirection create(String var0) {
      return create(☃, Predicates.alwaysTrue());
   }

   public static PropertyDirection create(String var0, Predicate<EnumFacing> var1) {
      return create(☃, Collections2.filter(Lists.newArrayList(EnumFacing.values()), ☃));
   }

   public static PropertyDirection create(String var0, Collection<EnumFacing> var1) {
      return new PropertyDirection(☃, ☃);
   }
}
