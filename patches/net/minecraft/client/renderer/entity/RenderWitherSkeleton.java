package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;

public class RenderWitherSkeleton extends RenderSkeleton {
   private static final ResourceLocation WITHER_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

   public RenderWitherSkeleton(RenderManager var1) {
      super(☃);
   }

   @Override
   protected ResourceLocation getEntityTexture(AbstractSkeleton var1) {
      return WITHER_SKELETON_TEXTURES;
   }

   protected void preRenderCallback(AbstractSkeleton var1, float var2) {
      GlStateManager.scale(1.2F, 1.2F, 1.2F);
   }
}
