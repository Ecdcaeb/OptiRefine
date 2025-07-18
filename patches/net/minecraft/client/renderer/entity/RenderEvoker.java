package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

public class RenderEvoker extends RenderLiving<EntityMob> {
   private static final ResourceLocation EVOKER_ILLAGER = new ResourceLocation("textures/entity/illager/evoker.png");

   public RenderEvoker(RenderManager var1) {
      super(☃, new ModelIllager(0.0F, 0.0F, 64, 64), 0.5F);
      this.addLayer(new LayerHeldItem(this) {
         @Override
         public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
            if (((EntitySpellcasterIllager)☃).isSpellcasting()) {
               super.doRenderLayer(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
            }
         }

         @Override
         protected void translateToHand(EnumHandSide var1) {
            ((ModelIllager)this.livingEntityRenderer.getMainModel()).getArm(☃).postRender(0.0625F);
         }
      });
   }

   protected ResourceLocation getEntityTexture(EntityMob var1) {
      return EVOKER_ILLAGER;
   }

   protected void preRenderCallback(EntityMob var1, float var2) {
      float ☃ = 0.9375F;
      GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
   }
}
