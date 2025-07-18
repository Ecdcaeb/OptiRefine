package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.entity.layers.LayerSnowmanHead;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.util.ResourceLocation;

public class RenderSnowMan extends RenderLiving<EntitySnowman> {
   private static final ResourceLocation SNOW_MAN_TEXTURES = new ResourceLocation("textures/entity/snowman.png");

   public RenderSnowMan(RenderManager var1) {
      super(☃, new ModelSnowMan(), 0.5F);
      this.addLayer(new LayerSnowmanHead(this));
   }

   protected ResourceLocation getEntityTexture(EntitySnowman var1) {
      return SNOW_MAN_TEXTURES;
   }

   public ModelSnowMan getMainModel() {
      return (ModelSnowMan)super.getMainModel();
   }
}
