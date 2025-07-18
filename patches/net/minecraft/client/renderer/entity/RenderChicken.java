package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelChicken;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderChicken extends RenderLiving<EntityChicken> {
   private static final ResourceLocation CHICKEN_TEXTURES = new ResourceLocation("textures/entity/chicken.png");

   public RenderChicken(RenderManager var1) {
      super(☃, new ModelChicken(), 0.3F);
   }

   protected ResourceLocation getEntityTexture(EntityChicken var1) {
      return CHICKEN_TEXTURES;
   }

   protected float handleRotationFloat(EntityChicken var1, float var2) {
      float ☃ = ☃.oFlap + (☃.wingRotation - ☃.oFlap) * ☃;
      float ☃x = ☃.oFlapSpeed + (☃.destPos - ☃.oFlapSpeed) * ☃;
      return (MathHelper.sin(☃) + 1.0F) * ☃x;
   }
}
