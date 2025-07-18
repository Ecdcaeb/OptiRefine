package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemOverride {
   private final ResourceLocation location;
   private final Map<ResourceLocation, Float> mapResourceValues;

   public ItemOverride(ResourceLocation var1, Map<ResourceLocation, Float> var2) {
      this.location = ☃;
      this.mapResourceValues = ☃;
   }

   public ResourceLocation getLocation() {
      return this.location;
   }

   boolean matchesItemStack(ItemStack var1, @Nullable World var2, @Nullable EntityLivingBase var3) {
      Item ☃ = ☃.getItem();

      for (Entry<ResourceLocation, Float> ☃x : this.mapResourceValues.entrySet()) {
         IItemPropertyGetter ☃xx = ☃.getPropertyGetter(☃x.getKey());
         if (☃xx == null || ☃xx.apply(☃, ☃, ☃) < ☃x.getValue()) {
            return false;
         }
      }

      return true;
   }

   static class Deserializer implements JsonDeserializer<ItemOverride> {
      public ItemOverride deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = ☃.getAsJsonObject();
         ResourceLocation ☃x = new ResourceLocation(JsonUtils.getString(☃, "model"));
         Map<ResourceLocation, Float> ☃xx = this.makeMapResourceValues(☃);
         return new ItemOverride(☃x, ☃xx);
      }

      protected Map<ResourceLocation, Float> makeMapResourceValues(JsonObject var1) {
         Map<ResourceLocation, Float> ☃ = Maps.newLinkedHashMap();
         JsonObject ☃x = JsonUtils.getJsonObject(☃, "predicate");

         for (Entry<String, JsonElement> ☃xx : ☃x.entrySet()) {
            ☃.put(new ResourceLocation(☃xx.getKey()), JsonUtils.getFloat(☃xx.getValue(), ☃xx.getKey()));
         }

         return ☃;
      }
   }
}
