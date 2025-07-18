package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public abstract class TileEntitySpecialRenderer<T extends TileEntity> {
   protected static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[]{
      new ResourceLocation("textures/blocks/destroy_stage_0.png"),
      new ResourceLocation("textures/blocks/destroy_stage_1.png"),
      new ResourceLocation("textures/blocks/destroy_stage_2.png"),
      new ResourceLocation("textures/blocks/destroy_stage_3.png"),
      new ResourceLocation("textures/blocks/destroy_stage_4.png"),
      new ResourceLocation("textures/blocks/destroy_stage_5.png"),
      new ResourceLocation("textures/blocks/destroy_stage_6.png"),
      new ResourceLocation("textures/blocks/destroy_stage_7.png"),
      new ResourceLocation("textures/blocks/destroy_stage_8.png"),
      new ResourceLocation("textures/blocks/destroy_stage_9.png")
   };
   protected TileEntityRendererDispatcher rendererDispatcher;

   public void render(T var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      ITextComponent ☃ = ☃.getDisplayName();
      if (☃ != null && this.rendererDispatcher.cameraHitResult != null && ☃.getPos().equals(this.rendererDispatcher.cameraHitResult.getBlockPos())) {
         this.setLightmapDisabled(true);
         this.drawNameplate(☃, ☃.getFormattedText(), ☃, ☃, ☃, 12);
         this.setLightmapDisabled(false);
      }
   }

   protected void setLightmapDisabled(boolean var1) {
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      if (☃) {
         GlStateManager.disableTexture2D();
      } else {
         GlStateManager.enableTexture2D();
      }

      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   protected void bindTexture(ResourceLocation var1) {
      TextureManager ☃ = this.rendererDispatcher.renderEngine;
      if (☃ != null) {
         ☃.bindTexture(☃);
      }
   }

   protected World getWorld() {
      return this.rendererDispatcher.world;
   }

   public void setRendererDispatcher(TileEntityRendererDispatcher var1) {
      this.rendererDispatcher = ☃;
   }

   public FontRenderer getFontRenderer() {
      return this.rendererDispatcher.getFontRenderer();
   }

   public boolean isGlobalRenderer(T var1) {
      return false;
   }

   protected void drawNameplate(T var1, String var2, double var3, double var5, double var7, int var9) {
      Entity ☃ = this.rendererDispatcher.entity;
      double ☃x = ☃.getDistanceSq(☃.posX, ☃.posY, ☃.posZ);
      if (!(☃x > ☃ * ☃)) {
         float ☃xx = this.rendererDispatcher.entityYaw;
         float ☃xxx = this.rendererDispatcher.entityPitch;
         boolean ☃xxxx = false;
         EntityRenderer.drawNameplate(this.getFontRenderer(), ☃, (float)☃ + 0.5F, (float)☃ + 1.5F, (float)☃ + 0.5F, 0, ☃xx, ☃xxx, false, false);
      }
   }
}
