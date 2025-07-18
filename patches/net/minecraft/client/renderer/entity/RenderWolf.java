package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;

public class RenderWolf extends RenderLiving<EntityWolf> {
   private static final ResourceLocation WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf.png");
   private static final ResourceLocation TAMED_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
   private static final ResourceLocation ANRGY_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_angry.png");

   public RenderWolf(RenderManager var1) {
      super(☃, new ModelWolf(), 0.5F);
      this.addLayer(new LayerWolfCollar(this));
   }

   protected float handleRotationFloat(EntityWolf var1, float var2) {
      return ☃.getTailRotation();
   }

   public void doRender(EntityWolf var1, double var2, double var4, double var6, float var8, float var9) {
      if (☃.isWolfWet()) {
         float ☃ = ☃.getBrightness() * ☃.getShadingWhileWet(☃);
         GlStateManager.color(☃, ☃, ☃);
      }

      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected ResourceLocation getEntityTexture(EntityWolf var1) {
      if (☃.isTamed()) {
         return TAMED_WOLF_TEXTURES;
      } else {
         return ☃.isAngry() ? ANRGY_WOLF_TEXTURES : WOLF_TEXTURES;
      }
   }
}
