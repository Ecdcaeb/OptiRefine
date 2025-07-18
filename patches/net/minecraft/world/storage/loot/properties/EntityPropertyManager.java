package net.minecraft.world.storage.loot.properties;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.ResourceLocation;

public class EntityPropertyManager {
   private static final Map<ResourceLocation, EntityProperty.Serializer<?>> NAME_TO_SERIALIZER_MAP = Maps.newHashMap();
   private static final Map<Class<? extends EntityProperty>, EntityProperty.Serializer<?>> CLASS_TO_SERIALIZER_MAP = Maps.newHashMap();

   public static <T extends EntityProperty> void registerProperty(EntityProperty.Serializer<? extends T> var0) {
      ResourceLocation ☃ = ☃.getName();
      Class<T> ☃x = (Class<T>)☃.getPropertyClass();
      if (NAME_TO_SERIALIZER_MAP.containsKey(☃)) {
         throw new IllegalArgumentException("Can't re-register entity property name " + ☃);
      } else if (CLASS_TO_SERIALIZER_MAP.containsKey(☃x)) {
         throw new IllegalArgumentException("Can't re-register entity property class " + ☃x.getName());
      } else {
         NAME_TO_SERIALIZER_MAP.put(☃, ☃);
         CLASS_TO_SERIALIZER_MAP.put(☃x, ☃);
      }
   }

   public static EntityProperty.Serializer<?> getSerializerForName(ResourceLocation var0) {
      EntityProperty.Serializer<?> ☃ = NAME_TO_SERIALIZER_MAP.get(☃);
      if (☃ == null) {
         throw new IllegalArgumentException("Unknown loot entity property '" + ☃ + "'");
      } else {
         return ☃;
      }
   }

   public static <T extends EntityProperty> EntityProperty.Serializer<T> getSerializerFor(T var0) {
      EntityProperty.Serializer<?> ☃ = CLASS_TO_SERIALIZER_MAP.get(☃.getClass());
      if (☃ == null) {
         throw new IllegalArgumentException("Unknown loot entity property " + ☃);
      } else {
         return (EntityProperty.Serializer<T>)☃;
      }
   }

   static {
      registerProperty(new EntityOnFire.Serializer());
   }
}
