package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderIllusionIllager extends RenderLiving<EntityMob> {
   private static final ResourceLocation ILLUSIONIST = new ResourceLocation("textures/entity/illager/illusionist.png");

   public RenderIllusionIllager(RenderManager var1) {
      super(☃, new ModelIllager(0.0F, 0.0F, 64, 64), 0.5F);
      this.addLayer(new LayerHeldItem(this) {
         @Override
         public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
            if (((EntityIllusionIllager)☃).isSpellcasting() || ((EntityIllusionIllager)☃).isAggressive()) {
               super.doRenderLayer(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
            }
         }

         @Override
         protected void translateToHand(EnumHandSide var1) {
            ((ModelIllager)this.livingEntityRenderer.getMainModel()).getArm(☃).postRender(0.0625F);
         }
      });
      ((ModelIllager)this.getMainModel()).hat.showModel = true;
   }

   protected ResourceLocation getEntityTexture(EntityMob var1) {
      return ILLUSIONIST;
   }

   protected void preRenderCallback(EntityMob var1, float var2) {
      float ☃ = 0.9375F;
      GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
   }

   public void doRender(EntityMob var1, double var2, double var4, double var6, float var8, float var9) {
      if (☃.isInvisible()) {
         Vec3d[] ☃ = ((EntityIllusionIllager)☃).getRenderLocations(☃);
         float ☃x = this.handleRotationFloat(☃, ☃);

         for (int ☃xx = 0; ☃xx < ☃.length; ☃xx++) {
            super.doRender(
               ☃,
               ☃ + ☃[☃xx].x + MathHelper.cos(☃xx + ☃x * 0.5F) * 0.025,
               ☃ + ☃[☃xx].y + MathHelper.cos(☃xx + ☃x * 0.75F) * 0.0125,
               ☃ + ☃[☃xx].z + MathHelper.cos(☃xx + ☃x * 0.7F) * 0.025,
               ☃,
               ☃
            );
         }
      } else {
         super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   public void renderName(EntityMob var1, double var2, double var4, double var6) {
      super.renderName(☃, ☃, ☃, ☃);
   }

   protected boolean isVisible(EntityMob var1) {
      return true;
   }
}
