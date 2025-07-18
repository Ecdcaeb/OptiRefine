package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelShulkerBullet;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderShulkerBullet extends Render<EntityShulkerBullet> {
   private static final ResourceLocation SHULKER_SPARK_TEXTURE = new ResourceLocation("textures/entity/shulker/spark.png");
   private final ModelShulkerBullet model = new ModelShulkerBullet();

   public RenderShulkerBullet(RenderManager var1) {
      super(☃);
   }

   private float rotLerp(float var1, float var2, float var3) {
      float ☃ = ☃ - ☃;

      while (☃ < -180.0F) {
         ☃ += 360.0F;
      }

      while (☃ >= 180.0F) {
         ☃ -= 360.0F;
      }

      return ☃ + ☃ * ☃;
   }

   public void doRender(EntityShulkerBullet var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      float ☃ = this.rotLerp(☃.prevRotationYaw, ☃.rotationYaw, ☃);
      float ☃x = ☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃;
      float ☃xx = ☃.ticksExisted + ☃;
      GlStateManager.translate((float)☃, (float)☃ + 0.15F, (float)☃);
      GlStateManager.rotate(MathHelper.sin(☃xx * 0.1F) * 180.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(MathHelper.cos(☃xx * 0.1F) * 180.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(MathHelper.sin(☃xx * 0.15F) * 360.0F, 0.0F, 0.0F, 1.0F);
      float ☃xxx = 0.03125F;
      GlStateManager.enableRescaleNormal();
      GlStateManager.scale(-1.0F, -1.0F, 1.0F);
      this.bindEntityTexture(☃);
      this.model.render(☃, 0.0F, 0.0F, 0.0F, ☃, ☃x, 0.03125F);
      GlStateManager.enableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
      GlStateManager.scale(1.5F, 1.5F, 1.5F);
      this.model.render(☃, 0.0F, 0.0F, 0.0F, ☃, ☃x, 0.03125F);
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected ResourceLocation getEntityTexture(EntityShulkerBullet var1) {
      return SHULKER_SPARK_TEXTURE;
   }
}
