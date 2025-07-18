package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

public class RenderHusk extends RenderZombie {
   private static final ResourceLocation HUSK_ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/husk.png");

   public RenderHusk(RenderManager var1) {
      super(☃);
   }

   protected void preRenderCallback(EntityZombie var1, float var2) {
      float ☃ = 1.0625F;
      GlStateManager.scale(1.0625F, 1.0625F, 1.0625F);
      super.preRenderCallback(☃, ☃);
   }

   @Override
   protected ResourceLocation getEntityTexture(EntityZombie var1) {
      return HUSK_ZOMBIE_TEXTURES;
   }
}
