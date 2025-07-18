package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerSlimeGel;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;

public class RenderSlime extends RenderLiving<EntitySlime> {
   private static final ResourceLocation SLIME_TEXTURES = new ResourceLocation("textures/entity/slime/slime.png");

   public RenderSlime(RenderManager var1) {
      super(☃, new ModelSlime(16), 0.25F);
      this.addLayer(new LayerSlimeGel(this));
   }

   public void doRender(EntitySlime var1, double var2, double var4, double var6, float var8, float var9) {
      this.shadowSize = 0.25F * ☃.getSlimeSize();
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected void preRenderCallback(EntitySlime var1, float var2) {
      float ☃ = 0.999F;
      GlStateManager.scale(0.999F, 0.999F, 0.999F);
      float ☃x = ☃.getSlimeSize();
      float ☃xx = (☃.prevSquishFactor + (☃.squishFactor - ☃.prevSquishFactor) * ☃) / (☃x * 0.5F + 1.0F);
      float ☃xxx = 1.0F / (☃xx + 1.0F);
      GlStateManager.scale(☃xxx * ☃x, 1.0F / ☃xxx * ☃x, ☃xxx * ☃x);
   }

   protected ResourceLocation getEntityTexture(EntitySlime var1) {
      return SLIME_TEXTURES;
   }
}
