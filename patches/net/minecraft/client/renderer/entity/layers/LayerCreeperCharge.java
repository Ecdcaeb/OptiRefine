package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;

public class LayerCreeperCharge implements LayerRenderer<EntityCreeper> {
   private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
   private final RenderCreeper creeperRenderer;
   private final ModelCreeper creeperModel = new ModelCreeper(2.0F);

   public LayerCreeperCharge(RenderCreeper var1) {
      this.creeperRenderer = ☃;
   }

   public void doRenderLayer(EntityCreeper var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (☃.getPowered()) {
         boolean ☃ = ☃.isInvisible();
         GlStateManager.depthMask(!☃);
         this.creeperRenderer.bindTexture(LIGHTNING_TEXTURE);
         GlStateManager.matrixMode(5890);
         GlStateManager.loadIdentity();
         float ☃x = ☃.ticksExisted + ☃;
         GlStateManager.translate(☃x * 0.01F, ☃x * 0.01F, 0.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.enableBlend();
         float ☃xx = 0.5F;
         GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
         GlStateManager.disableLighting();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
         this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
         Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
         this.creeperModel.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
         GlStateManager.matrixMode(5890);
         GlStateManager.loadIdentity();
         GlStateManager.matrixMode(5888);
         GlStateManager.enableLighting();
         GlStateManager.disableBlend();
         GlStateManager.depthMask(☃);
      }
   }

   @Override
   public boolean shouldCombineTextures() {
      return false;
   }
}
