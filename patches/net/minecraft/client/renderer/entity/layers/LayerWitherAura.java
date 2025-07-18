package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class LayerWitherAura implements LayerRenderer<EntityWither> {
   private static final ResourceLocation WITHER_ARMOR = new ResourceLocation("textures/entity/wither/wither_armor.png");
   private final RenderWither witherRenderer;
   private final ModelWither witherModel = new ModelWither(0.5F);

   public LayerWitherAura(RenderWither var1) {
      this.witherRenderer = ☃;
   }

   public void doRenderLayer(EntityWither var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (☃.isArmored()) {
         GlStateManager.depthMask(!☃.isInvisible());
         this.witherRenderer.bindTexture(WITHER_ARMOR);
         GlStateManager.matrixMode(5890);
         GlStateManager.loadIdentity();
         float ☃ = ☃.ticksExisted + ☃;
         float ☃x = MathHelper.cos(☃ * 0.02F) * 3.0F;
         float ☃xx = ☃ * 0.01F;
         GlStateManager.translate(☃x, ☃xx, 0.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.enableBlend();
         float ☃xxx = 0.5F;
         GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
         GlStateManager.disableLighting();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
         this.witherModel.setLivingAnimations(☃, ☃, ☃, ☃);
         this.witherModel.setModelAttributes(this.witherRenderer.getMainModel());
         Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
         this.witherModel.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
         GlStateManager.matrixMode(5890);
         GlStateManager.loadIdentity();
         GlStateManager.matrixMode(5888);
         GlStateManager.enableLighting();
         GlStateManager.disableBlend();
      }
   }

   @Override
   public boolean shouldCombineTextures() {
      return false;
   }
}
