package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderCreeper extends RenderLiving<EntityCreeper> {
   private static final ResourceLocation CREEPER_TEXTURES = new ResourceLocation("textures/entity/creeper/creeper.png");

   public RenderCreeper(RenderManager var1) {
      super(☃, new ModelCreeper(), 0.5F);
      this.addLayer(new LayerCreeperCharge(this));
   }

   protected void preRenderCallback(EntityCreeper var1, float var2) {
      float ☃ = ☃.getCreeperFlashIntensity(☃);
      float ☃x = 1.0F + MathHelper.sin(☃ * 100.0F) * ☃ * 0.01F;
      ☃ = MathHelper.clamp(☃, 0.0F, 1.0F);
      ☃ *= ☃;
      ☃ *= ☃;
      float ☃xx = (1.0F + ☃ * 0.4F) * ☃x;
      float ☃xxx = (1.0F + ☃ * 0.1F) / ☃x;
      GlStateManager.scale(☃xx, ☃xxx, ☃xx);
   }

   protected int getColorMultiplier(EntityCreeper var1, float var2, float var3) {
      float ☃ = ☃.getCreeperFlashIntensity(☃);
      if ((int)(☃ * 10.0F) % 2 == 0) {
         return 0;
      } else {
         int ☃x = (int)(☃ * 0.2F * 255.0F);
         ☃x = MathHelper.clamp(☃x, 0, 255);
         return ☃x << 24 | 822083583;
      }
   }

   protected ResourceLocation getEntityTexture(EntityCreeper var1) {
      return CREEPER_TEXTURES;
   }
}
