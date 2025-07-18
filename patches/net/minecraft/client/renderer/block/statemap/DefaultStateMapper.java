package net.minecraft.client.renderer.block.statemap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class DefaultStateMapper extends StateMapperBase {
   @Override
   protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
      return new ModelResourceLocation(Block.REGISTRY.getNameForObject(☃.getBlock()), this.getPropertyString(☃.getProperties()));
   }
}
