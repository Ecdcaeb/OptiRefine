package net.minecraft.client.renderer.block.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class MultipartBakedModel implements IBakedModel {
   private final Map<Predicate<IBlockState>, IBakedModel> selectors;
   protected final boolean ambientOcclusion;
   protected final boolean gui3D;
   protected final TextureAtlasSprite particleTexture;
   protected final ItemCameraTransforms cameraTransforms;
   protected final ItemOverrideList overrides;

   public MultipartBakedModel(Map<Predicate<IBlockState>, IBakedModel> var1) {
      this.selectors = ☃;
      IBakedModel ☃ = ☃.values().iterator().next();
      this.ambientOcclusion = ☃.isAmbientOcclusion();
      this.gui3D = ☃.isGui3d();
      this.particleTexture = ☃.getParticleTexture();
      this.cameraTransforms = ☃.getItemCameraTransforms();
      this.overrides = ☃.getOverrides();
   }

   @Override
   public List<BakedQuad> getQuads(@Nullable IBlockState var1, @Nullable EnumFacing var2, long var3) {
      List<BakedQuad> ☃ = Lists.newArrayList();
      if (☃ != null) {
         for (Entry<Predicate<IBlockState>, IBakedModel> ☃x : this.selectors.entrySet()) {
            if (☃x.getKey().apply(☃)) {
               ☃.addAll(☃x.getValue().getQuads(☃, ☃, ☃++));
            }
         }
      }

      return ☃;
   }

   @Override
   public boolean isAmbientOcclusion() {
      return this.ambientOcclusion;
   }

   @Override
   public boolean isGui3d() {
      return this.gui3D;
   }

   @Override
   public boolean isBuiltInRenderer() {
      return false;
   }

   @Override
   public TextureAtlasSprite getParticleTexture() {
      return this.particleTexture;
   }

   @Override
   public ItemCameraTransforms getItemCameraTransforms() {
      return this.cameraTransforms;
   }

   @Override
   public ItemOverrideList getOverrides() {
      return this.overrides;
   }

   public static class Builder {
      private final Map<Predicate<IBlockState>, IBakedModel> builderSelectors = Maps.newLinkedHashMap();

      public void putModel(Predicate<IBlockState> var1, IBakedModel var2) {
         this.builderSelectors.put(☃, ☃);
      }

      public IBakedModel makeMultipartModel() {
         return new MultipartBakedModel(this.builderSelectors);
      }
   }
}
