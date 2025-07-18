package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public abstract class StateMapperBase implements IStateMapper {
   protected Map<IBlockState, ModelResourceLocation> mapStateModelLocations = Maps.newLinkedHashMap();

   public String getPropertyString(Map<IProperty<?>, Comparable<?>> var1) {
      StringBuilder ☃ = new StringBuilder();

      for (Entry<IProperty<?>, Comparable<?>> ☃x : ☃.entrySet()) {
         if (☃.length() != 0) {
            ☃.append(",");
         }

         IProperty<?> ☃xx = ☃x.getKey();
         ☃.append(☃xx.getName());
         ☃.append("=");
         ☃.append(this.getPropertyName(☃xx, ☃x.getValue()));
      }

      if (☃.length() == 0) {
         ☃.append("normal");
      }

      return ☃.toString();
   }

   private <T extends Comparable<T>> String getPropertyName(IProperty<T> var1, Comparable<?> var2) {
      return ☃.getName((T)☃);
   }

   @Override
   public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block var1) {
      UnmodifiableIterator var2 = ☃.getBlockState().getValidStates().iterator();

      while (var2.hasNext()) {
         IBlockState ☃ = (IBlockState)var2.next();
         this.mapStateModelLocations.put(☃, this.getModelResourceLocation(☃));
      }

      return this.mapStateModelLocations;
   }

   protected abstract ModelResourceLocation getModelResourceLocation(IBlockState var1);
}
