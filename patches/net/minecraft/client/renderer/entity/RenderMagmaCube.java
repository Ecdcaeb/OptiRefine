package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.util.ResourceLocation;

public class RenderMagmaCube extends RenderLiving<EntityMagmaCube> {
   private static final ResourceLocation MAGMA_CUBE_TEXTURES = new ResourceLocation("textures/entity/slime/magmacube.png");

   public RenderMagmaCube(RenderManager var1) {
      super(☃, new ModelMagmaCube(), 0.25F);
   }

   protected ResourceLocation getEntityTexture(EntityMagmaCube var1) {
      return MAGMA_CUBE_TEXTURES;
   }

   protected void preRenderCallback(EntityMagmaCube var1, float var2) {
      int ☃ = ☃.getSlimeSize();
      float ☃x = (☃.prevSquishFactor + (☃.squishFactor - ☃.prevSquishFactor) * ☃) / (☃ * 0.5F + 1.0F);
      float ☃xx = 1.0F / (☃x + 1.0F);
      GlStateManager.scale(☃xx * ☃, 1.0F / ☃xx * ☃, ☃xx * ☃);
   }
}
