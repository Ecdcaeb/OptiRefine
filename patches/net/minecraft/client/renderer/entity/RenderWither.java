package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerWitherAura;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.ResourceLocation;

public class RenderWither extends RenderLiving<EntityWither> {
   private static final ResourceLocation INVULNERABLE_WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
   private static final ResourceLocation WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither.png");

   public RenderWither(RenderManager var1) {
      super(☃, new ModelWither(0.0F), 1.0F);
      this.addLayer(new LayerWitherAura(this));
   }

   protected ResourceLocation getEntityTexture(EntityWither var1) {
      int ☃ = ☃.getInvulTime();
      return ☃ > 0 && (☃ > 80 || ☃ / 5 % 2 != 1) ? INVULNERABLE_WITHER_TEXTURES : WITHER_TEXTURES;
   }

   protected void preRenderCallback(EntityWither var1, float var2) {
      float ☃ = 2.0F;
      int ☃x = ☃.getInvulTime();
      if (☃x > 0) {
         ☃ -= (☃x - ☃) / 220.0F * 0.5F;
      }

      GlStateManager.scale(☃, ☃, ☃);
   }
}
