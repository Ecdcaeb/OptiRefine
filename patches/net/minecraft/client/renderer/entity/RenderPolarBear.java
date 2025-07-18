package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelPolarBear;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.util.ResourceLocation;

public class RenderPolarBear extends RenderLiving<EntityPolarBear> {
   private static final ResourceLocation POLAR_BEAR_TEXTURE = new ResourceLocation("textures/entity/bear/polarbear.png");

   public RenderPolarBear(RenderManager var1) {
      super(☃, new ModelPolarBear(), 0.7F);
   }

   protected ResourceLocation getEntityTexture(EntityPolarBear var1) {
      return POLAR_BEAR_TEXTURE;
   }

   public void doRender(EntityPolarBear var1, double var2, double var4, double var6, float var8, float var9) {
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected void preRenderCallback(EntityPolarBear var1, float var2) {
      GlStateManager.scale(1.2F, 1.2F, 1.2F);
      super.preRenderCallback(☃, ☃);
   }
}
