package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderBat extends RenderLiving<EntityBat> {
   private static final ResourceLocation BAT_TEXTURES = new ResourceLocation("textures/entity/bat.png");

   public RenderBat(RenderManager var1) {
      super(☃, new ModelBat(), 0.25F);
   }

   protected ResourceLocation getEntityTexture(EntityBat var1) {
      return BAT_TEXTURES;
   }

   protected void preRenderCallback(EntityBat var1, float var2) {
      GlStateManager.scale(0.35F, 0.35F, 0.35F);
   }

   protected void applyRotations(EntityBat var1, float var2, float var3, float var4) {
      if (☃.getIsBatHanging()) {
         GlStateManager.translate(0.0F, -0.1F, 0.0F);
      } else {
         GlStateManager.translate(0.0F, MathHelper.cos(☃ * 0.3F) * 0.1F, 0.0F);
      }

      super.applyRotations(☃, ☃, ☃, ☃);
   }
}
