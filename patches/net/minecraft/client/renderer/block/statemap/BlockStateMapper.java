package net.minecraft.client.renderer.block.statemap;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

public class BlockStateMapper {
   private final Map<Block, IStateMapper> blockStateMap = Maps.newIdentityHashMap();
   private final Set<Block> setBuiltInBlocks = Sets.newIdentityHashSet();

   public void registerBlockStateMapper(Block var1, IStateMapper var2) {
      this.blockStateMap.put(☃, ☃);
   }

   public void registerBuiltInBlocks(Block... var1) {
      Collections.addAll(this.setBuiltInBlocks, ☃);
   }

   public Map<IBlockState, ModelResourceLocation> putAllStateModelLocations() {
      Map<IBlockState, ModelResourceLocation> ☃ = Maps.newIdentityHashMap();

      for (Block ☃x : Block.REGISTRY) {
         ☃.putAll(this.getVariants(☃x));
      }

      return ☃;
   }

   public Set<ResourceLocation> getBlockstateLocations(Block var1) {
      if (this.setBuiltInBlocks.contains(☃)) {
         return Collections.emptySet();
      } else {
         IStateMapper ☃ = this.blockStateMap.get(☃);
         if (☃ == null) {
            return Collections.singleton(Block.REGISTRY.getNameForObject(☃));
         } else {
            Set<ResourceLocation> ☃x = Sets.newHashSet();

            for (ModelResourceLocation ☃xx : ☃.putStateModelLocations(☃).values()) {
               ☃x.add(new ResourceLocation(☃xx.getNamespace(), ☃xx.getPath()));
            }

            return ☃x;
         }
      }
   }

   public Map<IBlockState, ModelResourceLocation> getVariants(Block var1) {
      return this.setBuiltInBlocks.contains(☃)
         ? Collections.emptyMap()
         : ((IStateMapper)MoreObjects.firstNonNull(this.blockStateMap.get(☃), new DefaultStateMapper())).putStateModelLocations(☃);
   }
}
