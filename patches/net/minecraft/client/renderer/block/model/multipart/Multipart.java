package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.renderer.block.model.VariantList;

public class Multipart {
   private final List<Selector> selectors;
   private BlockStateContainer stateContainer;

   public Multipart(List<Selector> var1) {
      this.selectors = ☃;
   }

   public List<Selector> getSelectors() {
      return this.selectors;
   }

   public Set<VariantList> getVariants() {
      Set<VariantList> ☃ = Sets.newHashSet();

      for (Selector ☃x : this.selectors) {
         ☃.add(☃x.getVariantList());
      }

      return ☃;
   }

   public void setStateContainer(BlockStateContainer var1) {
      this.stateContainer = ☃;
   }

   public BlockStateContainer getStateContainer() {
      return this.stateContainer;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else {
         if (☃ instanceof Multipart) {
            Multipart ☃ = (Multipart)☃;
            if (this.selectors.equals(☃.selectors)) {
               if (this.stateContainer == null) {
                  return ☃.stateContainer == null;
               }

               return this.stateContainer.equals(☃.stateContainer);
            }
         }

         return false;
      }
   }

   @Override
   public int hashCode() {
      return 31 * this.selectors.hashCode() + (this.stateContainer == null ? 0 : this.stateContainer.hashCode());
   }

   public static class Deserializer implements JsonDeserializer<Multipart> {
      public Multipart deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return new Multipart(this.getSelectors(☃, ☃.getAsJsonArray()));
      }

      private List<Selector> getSelectors(JsonDeserializationContext var1, JsonArray var2) {
         List<Selector> ☃ = Lists.newArrayList();

         for (JsonElement ☃x : ☃) {
            ☃.add((Selector)☃.deserialize(☃x, Selector.class));
         }

         return ☃;
      }
   }
}
