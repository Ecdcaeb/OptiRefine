package net.minecraft.client.renderer.entity;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderEntity extends Render<Entity> {
   public RenderEntity(RenderManager var1) {
      super(☃);
   }

   @Override
   public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      renderOffsetAABB(☃.getEntityBoundingBox(), ☃ - ☃.lastTickPosX, ☃ - ☃.lastTickPosY, ☃ - ☃.lastTickPosZ);
      GlStateManager.popMatrix();
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Nullable
   @Override
   protected ResourceLocation getEntityTexture(Entity var1) {
      return null;
   }
}
