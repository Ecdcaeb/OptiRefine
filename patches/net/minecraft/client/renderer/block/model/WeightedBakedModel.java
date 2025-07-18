package net.minecraft.client.renderer.block.model;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;

public class WeightedBakedModel implements IBakedModel {
   private final int totalWeight;
   private final List<WeightedBakedModel.WeightedModel> models;
   private final IBakedModel baseModel;

   public WeightedBakedModel(List<WeightedBakedModel.WeightedModel> var1) {
      this.models = ☃;
      this.totalWeight = WeightedRandom.getTotalWeight(☃);
      this.baseModel = ☃.get(0).model;
   }

   private IBakedModel getRandomModel(long var1) {
      return WeightedRandom.getRandomItem(this.models, Math.abs((int)☃ >> 16) % this.totalWeight).model;
   }

   @Override
   public List<BakedQuad> getQuads(@Nullable IBlockState var1, @Nullable EnumFacing var2, long var3) {
      return this.getRandomModel(☃).getQuads(☃, ☃, ☃);
   }

   @Override
   public boolean isAmbientOcclusion() {
      return this.baseModel.isAmbientOcclusion();
   }

   @Override
   public boolean isGui3d() {
      return this.baseModel.isGui3d();
   }

   @Override
   public boolean isBuiltInRenderer() {
      return this.baseModel.isBuiltInRenderer();
   }

   @Override
   public TextureAtlasSprite getParticleTexture() {
      return this.baseModel.getParticleTexture();
   }

   @Override
   public ItemCameraTransforms getItemCameraTransforms() {
      return this.baseModel.getItemCameraTransforms();
   }

   @Override
   public ItemOverrideList getOverrides() {
      return this.baseModel.getOverrides();
   }

   public static class Builder {
      private final List<WeightedBakedModel.WeightedModel> listItems = Lists.newArrayList();

      public WeightedBakedModel.Builder add(IBakedModel var1, int var2) {
         this.listItems.add(new WeightedBakedModel.WeightedModel(☃, ☃));
         return this;
      }

      public WeightedBakedModel build() {
         Collections.sort(this.listItems);
         return new WeightedBakedModel(this.listItems);
      }

      public IBakedModel first() {
         return this.listItems.get(0).model;
      }
   }

   static class WeightedModel extends WeightedRandom.Item implements Comparable<WeightedBakedModel.WeightedModel> {
      protected final IBakedModel model;

      public WeightedModel(IBakedModel var1, int var2) {
         super(☃);
         this.model = ☃;
      }

      public int compareTo(WeightedBakedModel.WeightedModel var1) {
         return ComparisonChain.start().compare(☃.itemWeight, this.itemWeight).result();
      }

      @Override
      public String toString() {
         return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
      }
   }
}
