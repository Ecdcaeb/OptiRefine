package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelCow;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;

public class RenderCow extends RenderLiving<EntityCow> {
   private static final ResourceLocation COW_TEXTURES = new ResourceLocation("textures/entity/cow/cow.png");

   public RenderCow(RenderManager var1) {
      super(☃, new ModelCow(), 0.7F);
   }

   protected ResourceLocation getEntityTexture(EntityCow var1) {
      return COW_TEXTURES;
   }
}
