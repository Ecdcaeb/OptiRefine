package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.ResourceLocation;

public class RenderElderGuardian extends RenderGuardian {
   private static final ResourceLocation GUARDIAN_ELDER_TEXTURE = new ResourceLocation("textures/entity/guardian_elder.png");

   public RenderElderGuardian(RenderManager var1) {
      super(☃);
   }

   protected void preRenderCallback(EntityGuardian var1, float var2) {
      GlStateManager.scale(2.35F, 2.35F, 2.35F);
   }

   @Override
   protected ResourceLocation getEntityTexture(EntityGuardian var1) {
      return GUARDIAN_ELDER_TEXTURE;
   }
}
