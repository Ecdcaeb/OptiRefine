package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelVex;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.util.ResourceLocation;

public class RenderVex extends RenderBiped<EntityVex> {
   private static final ResourceLocation VEX_TEXTURE = new ResourceLocation("textures/entity/illager/vex.png");
   private static final ResourceLocation VEX_CHARGING_TEXTURE = new ResourceLocation("textures/entity/illager/vex_charging.png");
   private int modelVersion = ((ModelVex)this.mainModel).getModelVersion();

   public RenderVex(RenderManager var1) {
      super(☃, new ModelVex(), 0.3F);
   }

   protected ResourceLocation getEntityTexture(EntityVex var1) {
      return ☃.isCharging() ? VEX_CHARGING_TEXTURE : VEX_TEXTURE;
   }

   public void doRender(EntityVex var1, double var2, double var4, double var6, float var8, float var9) {
      int ☃ = ((ModelVex)this.mainModel).getModelVersion();
      if (☃ != this.modelVersion) {
         this.mainModel = new ModelVex();
         this.modelVersion = ☃;
      }

      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected void preRenderCallback(EntityVex var1, float var2) {
      GlStateManager.scale(0.4F, 0.4F, 0.4F);
   }
}
