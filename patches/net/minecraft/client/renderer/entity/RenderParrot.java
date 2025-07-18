package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelParrot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderParrot extends RenderLiving<EntityParrot> {
   public static final ResourceLocation[] PARROT_TEXTURES = new ResourceLocation[]{
      new ResourceLocation("textures/entity/parrot/parrot_red_blue.png"),
      new ResourceLocation("textures/entity/parrot/parrot_blue.png"),
      new ResourceLocation("textures/entity/parrot/parrot_green.png"),
      new ResourceLocation("textures/entity/parrot/parrot_yellow_blue.png"),
      new ResourceLocation("textures/entity/parrot/parrot_grey.png")
   };

   public RenderParrot(RenderManager var1) {
      super(☃, new ModelParrot(), 0.3F);
   }

   protected ResourceLocation getEntityTexture(EntityParrot var1) {
      return PARROT_TEXTURES[☃.getVariant()];
   }

   public float handleRotationFloat(EntityParrot var1, float var2) {
      return this.getCustomBob(☃, ☃);
   }

   private float getCustomBob(EntityParrot var1, float var2) {
      float ☃ = ☃.oFlap + (☃.flap - ☃.oFlap) * ☃;
      float ☃x = ☃.oFlapSpeed + (☃.flapSpeed - ☃.oFlapSpeed) * ☃;
      return (MathHelper.sin(☃) + 1.0F) * ☃x;
   }
}
