package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SimpleBakedModel implements IBakedModel {
   protected final List<BakedQuad> generalQuads;
   protected final Map<EnumFacing, List<BakedQuad>> faceQuads;
   protected final boolean ambientOcclusion;
   protected final boolean gui3d;
   protected final TextureAtlasSprite texture;
   protected final ItemCameraTransforms cameraTransforms;
   protected final ItemOverrideList itemOverrideList;

   public SimpleBakedModel(
      List<BakedQuad> var1,
      Map<EnumFacing, List<BakedQuad>> var2,
      boolean var3,
      boolean var4,
      TextureAtlasSprite var5,
      ItemCameraTransforms var6,
      ItemOverrideList var7
   ) {
      this.generalQuads = ☃;
      this.faceQuads = ☃;
      this.ambientOcclusion = ☃;
      this.gui3d = ☃;
      this.texture = ☃;
      this.cameraTransforms = ☃;
      this.itemOverrideList = ☃;
   }

   @Override
   public List<BakedQuad> getQuads(@Nullable IBlockState var1, @Nullable EnumFacing var2, long var3) {
      return ☃ == null ? this.generalQuads : this.faceQuads.get(☃);
   }

   @Override
   public boolean isAmbientOcclusion() {
      return this.ambientOcclusion;
   }

   @Override
   public boolean isGui3d() {
      return this.gui3d;
   }

   @Override
   public boolean isBuiltInRenderer() {
      return false;
   }

   @Override
   public TextureAtlasSprite getParticleTexture() {
      return this.texture;
   }

   @Override
   public ItemCameraTransforms getItemCameraTransforms() {
      return this.cameraTransforms;
   }

   @Override
   public ItemOverrideList getOverrides() {
      return this.itemOverrideList;
   }

   public static class Builder {
      private final List<BakedQuad> builderGeneralQuads = Lists.newArrayList();
      private final Map<EnumFacing, List<BakedQuad>> builderFaceQuads = Maps.newEnumMap(EnumFacing.class);
      private final ItemOverrideList builderItemOverrideList;
      private final boolean builderAmbientOcclusion;
      private TextureAtlasSprite builderTexture;
      private final boolean builderGui3d;
      private final ItemCameraTransforms builderCameraTransforms;

      public Builder(ModelBlock var1, ItemOverrideList var2) {
         this(☃.isAmbientOcclusion(), ☃.isGui3d(), ☃.getAllTransforms(), ☃);
      }

      public Builder(IBlockState var1, IBakedModel var2, TextureAtlasSprite var3, BlockPos var4) {
         this(☃.isAmbientOcclusion(), ☃.isGui3d(), ☃.getItemCameraTransforms(), ☃.getOverrides());
         this.builderTexture = ☃.getParticleTexture();
         long ☃ = MathHelper.getPositionRandom(☃);

         for (EnumFacing ☃x : EnumFacing.values()) {
            this.addFaceQuads(☃, ☃, ☃, ☃x, ☃);
         }

         this.addGeneralQuads(☃, ☃, ☃, ☃);
      }

      private Builder(boolean var1, boolean var2, ItemCameraTransforms var3, ItemOverrideList var4) {
         for (EnumFacing ☃ : EnumFacing.values()) {
            this.builderFaceQuads.put(☃, Lists.newArrayList());
         }

         this.builderItemOverrideList = ☃;
         this.builderAmbientOcclusion = ☃;
         this.builderGui3d = ☃;
         this.builderCameraTransforms = ☃;
      }

      private void addFaceQuads(IBlockState var1, IBakedModel var2, TextureAtlasSprite var3, EnumFacing var4, long var5) {
         for (BakedQuad ☃ : ☃.getQuads(☃, ☃, ☃)) {
            this.addFaceQuad(☃, new BakedQuadRetextured(☃, ☃));
         }
      }

      private void addGeneralQuads(IBlockState var1, IBakedModel var2, TextureAtlasSprite var3, long var4) {
         for (BakedQuad ☃ : ☃.getQuads(☃, null, ☃)) {
            this.addGeneralQuad(new BakedQuadRetextured(☃, ☃));
         }
      }

      public SimpleBakedModel.Builder addFaceQuad(EnumFacing var1, BakedQuad var2) {
         this.builderFaceQuads.get(☃).add(☃);
         return this;
      }

      public SimpleBakedModel.Builder addGeneralQuad(BakedQuad var1) {
         this.builderGeneralQuads.add(☃);
         return this;
      }

      public SimpleBakedModel.Builder setTexture(TextureAtlasSprite var1) {
         this.builderTexture = ☃;
         return this;
      }

      public IBakedModel makeBakedModel() {
         if (this.builderTexture == null) {
            throw new RuntimeException("Missing particle!");
         } else {
            return new SimpleBakedModel(
               this.builderGeneralQuads,
               this.builderFaceQuads,
               this.builderAmbientOcclusion,
               this.builderGui3d,
               this.builderTexture,
               this.builderCameraTransforms,
               this.builderItemOverrideList
            );
         }
      }
   }
}
