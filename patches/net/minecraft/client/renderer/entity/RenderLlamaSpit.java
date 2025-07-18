package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelLlamaSpit;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.util.ResourceLocation;

public class RenderLlamaSpit extends Render<EntityLlamaSpit> {
   private static final ResourceLocation LLAMA_SPIT_TEXTURE = new ResourceLocation("textures/entity/llama/spit.png");
   private final ModelLlamaSpit model = new ModelLlamaSpit();

   public RenderLlamaSpit(RenderManager var1) {
      super(☃);
   }

   public void doRender(EntityLlamaSpit var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)☃, (float)☃ + 0.15F, (float)☃);
      GlStateManager.rotate(☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃ - 90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃, 0.0F, 0.0F, 1.0F);
      this.bindEntityTexture(☃);
      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.enableOutlineMode(this.getTeamColor(☃));
      }

      this.model.render(☃, ☃, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
      if (this.renderOutlines) {
         GlStateManager.disableOutlineMode();
         GlStateManager.disableColorMaterial();
      }

      GlStateManager.popMatrix();
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected ResourceLocation getEntityTexture(EntityLlamaSpit var1) {
      return LLAMA_SPIT_TEXTURE;
   }
}
