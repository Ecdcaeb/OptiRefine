package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.layers.LayerStrayClothing;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;

public class RenderStray extends RenderSkeleton {
   private static final ResourceLocation STRAY_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/stray.png");

   public RenderStray(RenderManager var1) {
      super(☃);
      this.addLayer(new LayerStrayClothing(this));
   }

   @Override
   protected ResourceLocation getEntityTexture(AbstractSkeleton var1) {
      return STRAY_SKELETON_TEXTURES;
   }
}
