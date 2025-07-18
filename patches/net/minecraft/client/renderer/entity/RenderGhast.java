package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.ResourceLocation;

public class RenderGhast extends RenderLiving<EntityGhast> {
   private static final ResourceLocation GHAST_TEXTURES = new ResourceLocation("textures/entity/ghast/ghast.png");
   private static final ResourceLocation GHAST_SHOOTING_TEXTURES = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");

   public RenderGhast(RenderManager var1) {
      super(☃, new ModelGhast(), 0.5F);
   }

   protected ResourceLocation getEntityTexture(EntityGhast var1) {
      return ☃.isAttacking() ? GHAST_SHOOTING_TEXTURES : GHAST_TEXTURES;
   }

   protected void preRenderCallback(EntityGhast var1, float var2) {
      float ☃ = 1.0F;
      float ☃x = 4.5F;
      float ☃xx = 4.5F;
      GlStateManager.scale(4.5F, 4.5F, 4.5F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
