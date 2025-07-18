package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.util.ResourceLocation;

public class LayerStrayClothing implements LayerRenderer<EntityStray> {
   private static final ResourceLocation STRAY_CLOTHES_TEXTURES = new ResourceLocation("textures/entity/skeleton/stray_overlay.png");
   private final RenderLivingBase<?> renderer;
   private final ModelSkeleton layerModel = new ModelSkeleton(0.25F, true);

   public LayerStrayClothing(RenderLivingBase<?> var1) {
      this.renderer = ☃;
   }

   public void doRenderLayer(EntityStray var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.layerModel.setModelAttributes(this.renderer.getMainModel());
      this.layerModel.setLivingAnimations(☃, ☃, ☃, ☃);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.renderer.bindTexture(STRAY_CLOTHES_TEXTURES);
      this.layerModel.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public boolean shouldCombineTextures() {
      return true;
   }
}
