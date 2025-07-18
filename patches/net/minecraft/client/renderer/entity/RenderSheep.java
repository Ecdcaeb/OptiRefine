package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

public class RenderSheep extends RenderLiving<EntitySheep> {
   private static final ResourceLocation SHEARED_SHEEP_TEXTURES = new ResourceLocation("textures/entity/sheep/sheep.png");

   public RenderSheep(RenderManager var1) {
      super(☃, new ModelSheep2(), 0.7F);
      this.addLayer(new LayerSheepWool(this));
   }

   protected ResourceLocation getEntityTexture(EntitySheep var1) {
      return SHEARED_SHEEP_TEXTURES;
   }
}
