package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemModelMesher {
   private final Map<Integer, ModelResourceLocation> simpleShapes = Maps.newHashMap();
   private final Map<Integer, IBakedModel> simpleShapesCache = Maps.newHashMap();
   private final Map<Item, ItemMeshDefinition> shapers = Maps.newHashMap();
   private final ModelManager modelManager;

   public ItemModelMesher(ModelManager var1) {
      this.modelManager = ☃;
   }

   public TextureAtlasSprite getParticleIcon(Item var1) {
      return this.getParticleIcon(☃, 0);
   }

   public TextureAtlasSprite getParticleIcon(Item var1, int var2) {
      return this.getItemModel(new ItemStack(☃, 1, ☃)).getParticleTexture();
   }

   public IBakedModel getItemModel(ItemStack var1) {
      Item ☃ = ☃.getItem();
      IBakedModel ☃x = this.getItemModel(☃, this.getMetadata(☃));
      if (☃x == null) {
         ItemMeshDefinition ☃xx = this.shapers.get(☃);
         if (☃xx != null) {
            ☃x = this.modelManager.getModel(☃xx.getModelLocation(☃));
         }
      }

      if (☃x == null) {
         ☃x = this.modelManager.getMissingModel();
      }

      return ☃x;
   }

   protected int getMetadata(ItemStack var1) {
      return ☃.getMaxDamage() > 0 ? 0 : ☃.getMetadata();
   }

   @Nullable
   protected IBakedModel getItemModel(Item var1, int var2) {
      return this.simpleShapesCache.get(this.getIndex(☃, ☃));
   }

   private int getIndex(Item var1, int var2) {
      return Item.getIdFromItem(☃) << 16 | ☃;
   }

   public void register(Item var1, int var2, ModelResourceLocation var3) {
      this.simpleShapes.put(this.getIndex(☃, ☃), ☃);
      this.simpleShapesCache.put(this.getIndex(☃, ☃), this.modelManager.getModel(☃));
   }

   public void register(Item var1, ItemMeshDefinition var2) {
      this.shapers.put(☃, ☃);
   }

   public ModelManager getModelManager() {
      return this.modelManager;
   }

   public void rebuildCache() {
      this.simpleShapesCache.clear();

      for (Entry<Integer, ModelResourceLocation> ☃ : this.simpleShapes.entrySet()) {
         this.simpleShapesCache.put(☃.getKey(), this.modelManager.getModel(☃.getValue()));
      }
   }
}
