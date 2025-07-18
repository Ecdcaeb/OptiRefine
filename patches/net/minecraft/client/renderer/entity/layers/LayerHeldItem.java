package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;

public class LayerHeldItem implements LayerRenderer<EntityLivingBase> {
   protected final RenderLivingBase<?> livingEntityRenderer;

   public LayerHeldItem(RenderLivingBase<?> var1) {
      this.livingEntityRenderer = ☃;
   }

   @Override
   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      boolean ☃ = ☃.getPrimaryHand() == EnumHandSide.RIGHT;
      ItemStack ☃x = ☃ ? ☃.getHeldItemOffhand() : ☃.getHeldItemMainhand();
      ItemStack ☃xx = ☃ ? ☃.getHeldItemMainhand() : ☃.getHeldItemOffhand();
      if (!☃x.isEmpty() || !☃xx.isEmpty()) {
         GlStateManager.pushMatrix();
         if (this.livingEntityRenderer.getMainModel().isChild) {
            float ☃xxx = 0.5F;
            GlStateManager.translate(0.0F, 0.75F, 0.0F);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
         }

         this.renderHeldItem(☃, ☃xx, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
         this.renderHeldItem(☃, ☃x, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
         GlStateManager.popMatrix();
      }
   }

   private void renderHeldItem(EntityLivingBase var1, ItemStack var2, ItemCameraTransforms.TransformType var3, EnumHandSide var4) {
      if (!☃.isEmpty()) {
         GlStateManager.pushMatrix();
         this.translateToHand(☃);
         if (☃.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
         }

         GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
         boolean ☃ = ☃ == EnumHandSide.LEFT;
         GlStateManager.translate((☃ ? -1 : 1) / 16.0F, 0.125F, -0.625F);
         Minecraft.getMinecraft().getItemRenderer().renderItemSide(☃, ☃, ☃, ☃);
         GlStateManager.popMatrix();
      }
   }

   protected void translateToHand(EnumHandSide var1) {
      ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, ☃);
   }

   @Override
   public boolean shouldCombineTextures() {
      return false;
   }
}
