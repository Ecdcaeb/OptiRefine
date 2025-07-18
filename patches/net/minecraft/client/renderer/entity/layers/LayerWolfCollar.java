package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;

public class LayerWolfCollar implements LayerRenderer<EntityWolf> {
   private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
   private final RenderWolf wolfRenderer;

   public LayerWolfCollar(RenderWolf var1) {
      this.wolfRenderer = ☃;
   }

   public void doRenderLayer(EntityWolf var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (☃.isTamed() && !☃.isInvisible()) {
         this.wolfRenderer.bindTexture(WOLF_COLLAR);
         float[] ☃ = ☃.getCollarColor().getColorComponentValues();
         GlStateManager.color(☃[0], ☃[1], ☃[2]);
         this.wolfRenderer.getMainModel().render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   @Override
   public boolean shouldCombineTextures() {
      return true;
   }
}
