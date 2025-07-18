package net.minecraft.client.renderer.block.model;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class BuiltInModel implements IBakedModel {
   private final ItemCameraTransforms cameraTransforms;
   private final ItemOverrideList overrideList;

   public BuiltInModel(ItemCameraTransforms var1, ItemOverrideList var2) {
      this.cameraTransforms = ☃;
      this.overrideList = ☃;
   }

   @Override
   public List<BakedQuad> getQuads(@Nullable IBlockState var1, @Nullable EnumFacing var2, long var3) {
      return Collections.emptyList();
   }

   @Override
   public boolean isAmbientOcclusion() {
      return false;
   }

   @Override
   public boolean isGui3d() {
      return true;
   }

   @Override
   public boolean isBuiltInRenderer() {
      return true;
   }

   @Override
   public TextureAtlasSprite getParticleTexture() {
      return null;
   }

   @Override
   public ItemCameraTransforms getItemCameraTransforms() {
      return this.cameraTransforms;
   }

   @Override
   public ItemOverrideList getOverrides() {
      return this.overrideList;
   }
}
