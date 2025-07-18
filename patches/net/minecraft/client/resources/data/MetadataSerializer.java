package net.minecraft.client.resources.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;

public class MetadataSerializer {
   private final IRegistry<String, MetadataSerializer.Registration<? extends IMetadataSection>> metadataSectionSerializerRegistry = new RegistrySimple<>();
   private final GsonBuilder gsonBuilder = new GsonBuilder();
   private Gson gson;

   public MetadataSerializer() {
      this.gsonBuilder.registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer());
      this.gsonBuilder.registerTypeHierarchyAdapter(Style.class, new Style.Serializer());
      this.gsonBuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
   }

   public <T extends IMetadataSection> void registerMetadataSectionType(IMetadataSectionSerializer<T> var1, Class<T> var2) {
      this.metadataSectionSerializerRegistry.putObject(☃.getSectionName(), new MetadataSerializer.Registration<>(☃, ☃));
      this.gsonBuilder.registerTypeAdapter(☃, ☃);
      this.gson = null;
   }

   public <T extends IMetadataSection> T parseMetadataSection(String var1, JsonObject var2) {
      if (☃ == null) {
         throw new IllegalArgumentException("Metadata section name cannot be null");
      } else if (!☃.has(☃)) {
         return null;
      } else if (!☃.get(☃).isJsonObject()) {
         throw new IllegalArgumentException("Invalid metadata for '" + ☃ + "' - expected object, found " + ☃.get(☃));
      } else {
         MetadataSerializer.Registration<?> ☃ = this.metadataSectionSerializerRegistry.getObject(☃);
         if (☃ == null) {
            throw new IllegalArgumentException("Don't know how to handle metadata section '" + ☃ + "'");
         } else {
            return (T)this.getGson().fromJson(☃.getAsJsonObject(☃), ☃.clazz);
         }
      }
   }

   private Gson getGson() {
      if (this.gson == null) {
         this.gson = this.gsonBuilder.create();
      }

      return this.gson;
   }

   class Registration<T extends IMetadataSection> {
      final IMetadataSectionSerializer<T> section;
      final Class<T> clazz;

      private Registration(IMetadataSectionSerializer<T> var2, Class<T> var3) {
         this.section = ☃;
         this.clazz = ☃;
      }
   }
}
