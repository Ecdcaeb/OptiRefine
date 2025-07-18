package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.layers.LayerSaddle;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;

public class RenderPig extends RenderLiving<EntityPig> {
   private static final ResourceLocation PIG_TEXTURES = new ResourceLocation("textures/entity/pig/pig.png");

   public RenderPig(RenderManager var1) {
      super(☃, new ModelPig(), 0.7F);
      this.addLayer(new LayerSaddle(this));
   }

   protected ResourceLocation getEntityTexture(EntityPig var1) {
      return PIG_TEXTURES;
   }
}
