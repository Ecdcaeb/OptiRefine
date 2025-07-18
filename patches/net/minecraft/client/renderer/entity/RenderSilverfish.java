package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.ResourceLocation;

public class RenderSilverfish extends RenderLiving<EntitySilverfish> {
   private static final ResourceLocation SILVERFISH_TEXTURES = new ResourceLocation("textures/entity/silverfish.png");

   public RenderSilverfish(RenderManager var1) {
      super(☃, new ModelSilverfish(), 0.3F);
   }

   protected float getDeathMaxRotation(EntitySilverfish var1) {
      return 180.0F;
   }

   protected ResourceLocation getEntityTexture(EntitySilverfish var1) {
      return SILVERFISH_TEXTURES;
   }
}
