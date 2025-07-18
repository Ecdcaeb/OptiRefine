package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class RenderEnderCrystal extends Render<EntityEnderCrystal> {
   private static final ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
   private final ModelBase modelEnderCrystal = new ModelEnderCrystal(0.0F, true);
   private final ModelBase modelEnderCrystalNoBase = new ModelEnderCrystal(0.0F, false);

   public RenderEnderCrystal(RenderManager var1) {
      super(☃);
      this.shadowSize = 0.5F;
   }

   public void doRender(EntityEnderCrystal var1, double var2, double var4, double var6, float var8, float var9) {
      float ☃ = ☃.innerRotation + ☃;
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)☃, (float)☃, (float)☃);
      this.bindTexture(ENDER_CRYSTAL_TEXTURES);
      float ☃x = MathHelper.sin(☃ * 0.2F) / 2.0F + 0.5F;
      ☃x = ☃x * ☃x + ☃x;
      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.enableOutlineMode(this.getTeamColor(☃));
      }

      if (☃.shouldShowBottom()) {
         this.modelEnderCrystal.render(☃, 0.0F, ☃ * 3.0F, ☃x * 0.2F, 0.0F, 0.0F, 0.0625F);
      } else {
         this.modelEnderCrystalNoBase.render(☃, 0.0F, ☃ * 3.0F, ☃x * 0.2F, 0.0F, 0.0F, 0.0625F);
      }

      if (this.renderOutlines) {
         GlStateManager.disableOutlineMode();
         GlStateManager.disableColorMaterial();
      }

      GlStateManager.popMatrix();
      BlockPos ☃xx = ☃.getBeamTarget();
      if (☃xx != null) {
         this.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
         float ☃xxx = ☃xx.getX() + 0.5F;
         float ☃xxxx = ☃xx.getY() + 0.5F;
         float ☃xxxxx = ☃xx.getZ() + 0.5F;
         double ☃xxxxxx = ☃xxx - ☃.posX;
         double ☃xxxxxxx = ☃xxxx - ☃.posY;
         double ☃xxxxxxxx = ☃xxxxx - ☃.posZ;
         RenderDragon.renderCrystalBeams(
            ☃ + ☃xxxxxx, ☃ - 0.3 + ☃x * 0.4F + ☃xxxxxxx, ☃ + ☃xxxxxxxx, ☃, ☃xxx, ☃xxxx, ☃xxxxx, ☃.innerRotation, ☃.posX, ☃.posY, ☃.posZ
         );
      }

      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected ResourceLocation getEntityTexture(EntityEnderCrystal var1) {
      return ENDER_CRYSTAL_TEXTURES;
   }

   public boolean shouldRender(EntityEnderCrystal var1, ICamera var2, double var3, double var5, double var7) {
      return super.shouldRender(☃, ☃, ☃, ☃, ☃) || ☃.getBeamTarget() != null;
   }
}
