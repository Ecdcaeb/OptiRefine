package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.registry.IRegistry;

public class ModelManager implements IResourceManagerReloadListener {
   private IRegistry<ModelResourceLocation, IBakedModel> modelRegistry;
   private final TextureMap texMap;
   private final BlockModelShapes modelProvider;
   private IBakedModel defaultModel;

   public ModelManager(TextureMap var1) {
      this.texMap = ☃;
      this.modelProvider = new BlockModelShapes(this);
   }

   @Override
   public void onResourceManagerReload(IResourceManager var1) {
      ModelBakery ☃ = new ModelBakery(☃, this.texMap, this.modelProvider);
      this.modelRegistry = ☃.setupModelRegistry();
      this.defaultModel = this.modelRegistry.getObject(ModelBakery.MODEL_MISSING);
      this.modelProvider.reloadModels();
   }

   public IBakedModel getModel(ModelResourceLocation var1) {
      if (☃ == null) {
         return this.defaultModel;
      } else {
         IBakedModel ☃ = this.modelRegistry.getObject(☃);
         return ☃ == null ? this.defaultModel : ☃;
      }
   }

   public IBakedModel getMissingModel() {
      return this.defaultModel;
   }

   public TextureMap getTextureMap() {
      return this.texMap;
   }

   public BlockModelShapes getBlockModelShapes() {
      return this.modelProvider;
   }
}
