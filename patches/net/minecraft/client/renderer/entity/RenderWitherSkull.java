package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.ResourceLocation;

public class RenderWitherSkull extends Render<EntityWitherSkull> {
   private static final ResourceLocation INVULNERABLE_WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
   private static final ResourceLocation WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither.png");
   private final ModelSkeletonHead skeletonHeadModel = new ModelSkeletonHead();

   public RenderWitherSkull(RenderManager var1) {
      super(☃);
   }

   private float getRenderYaw(float var1, float var2, float var3) {
      float ☃ = ☃ - ☃;

      while (☃ < -180.0F) {
         ☃ += 360.0F;
      }

      while (☃ >= 180.0F) {
         ☃ -= 360.0F;
      }

      return ☃ + ☃ * ☃;
   }

   public void doRender(EntityWitherSkull var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      GlStateManager.disableCull();
      float ☃ = this.getRenderYaw(☃.prevRotationYaw, ☃.rotationYaw, ☃);
      float ☃x = ☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃;
      GlStateManager.translate((float)☃, (float)☃, (float)☃);
      float ☃xx = 0.0625F;
      GlStateManager.enableRescaleNormal();
      GlStateManager.scale(-1.0F, -1.0F, 1.0F);
      GlStateManager.enableAlpha();
      this.bindEntityTexture(☃);
      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.enableOutlineMode(this.getTeamColor(☃));
      }

      this.skeletonHeadModel.render(☃, 0.0F, 0.0F, 0.0F, ☃, ☃x, 0.0625F);
      if (this.renderOutlines) {
         GlStateManager.disableOutlineMode();
         GlStateManager.disableColorMaterial();
      }

      GlStateManager.popMatrix();
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected ResourceLocation getEntityTexture(EntityWitherSkull var1) {
      return ☃.isInvulnerable() ? INVULNERABLE_WITHER_TEXTURES : WITHER_TEXTURES;
   }
}
