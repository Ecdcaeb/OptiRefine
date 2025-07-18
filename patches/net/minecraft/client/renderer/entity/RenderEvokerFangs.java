package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelEvokerFangs;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.util.ResourceLocation;

public class RenderEvokerFangs extends Render<EntityEvokerFangs> {
   private static final ResourceLocation EVOKER_ILLAGER_FANGS = new ResourceLocation("textures/entity/illager/fangs.png");
   private final ModelEvokerFangs model = new ModelEvokerFangs();

   public RenderEvokerFangs(RenderManager var1) {
      super(☃);
   }

   public void doRender(EntityEvokerFangs var1, double var2, double var4, double var6, float var8, float var9) {
      float ☃ = ☃.getAnimationProgress(☃);
      if (☃ != 0.0F) {
         float ☃x = 2.0F;
         if (☃ > 0.9F) {
            ☃x = (float)(☃x * ((1.0 - ☃) / 0.1F));
         }

         GlStateManager.pushMatrix();
         GlStateManager.disableCull();
         GlStateManager.enableAlpha();
         this.bindEntityTexture(☃);
         GlStateManager.translate((float)☃, (float)☃, (float)☃);
         GlStateManager.rotate(90.0F - ☃.rotationYaw, 0.0F, 1.0F, 0.0F);
         GlStateManager.scale(-☃x, -☃x, ☃x);
         float ☃xx = 0.03125F;
         GlStateManager.translate(0.0F, -0.626F, 0.0F);
         this.model.render(☃, ☃, 0.0F, 0.0F, ☃.rotationYaw, ☃.rotationPitch, 0.03125F);
         GlStateManager.popMatrix();
         GlStateManager.enableCull();
         super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   protected ResourceLocation getEntityTexture(EntityEvokerFangs var1) {
      return EVOKER_ILLAGER_FANGS;
   }
}
