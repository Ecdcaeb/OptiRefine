package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.ResourceLocation;

public class RenderSquid extends RenderLiving<EntitySquid> {
   private static final ResourceLocation SQUID_TEXTURES = new ResourceLocation("textures/entity/squid.png");

   public RenderSquid(RenderManager var1) {
      super(☃, new ModelSquid(), 0.7F);
   }

   protected ResourceLocation getEntityTexture(EntitySquid var1) {
      return SQUID_TEXTURES;
   }

   protected void applyRotations(EntitySquid var1, float var2, float var3, float var4) {
      float ☃ = ☃.prevSquidPitch + (☃.squidPitch - ☃.prevSquidPitch) * ☃;
      float ☃x = ☃.prevSquidYaw + (☃.squidYaw - ☃.prevSquidYaw) * ☃;
      GlStateManager.translate(0.0F, 0.5F, 0.0F);
      GlStateManager.rotate(180.0F - ☃, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(☃, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(☃x, 0.0F, 1.0F, 0.0F);
      GlStateManager.translate(0.0F, -1.2F, 0.0F);
   }

   protected float handleRotationFloat(EntitySquid var1, float var2) {
      return ☃.lastTentacleAngle + (☃.tentacleAngle - ☃.lastTentacleAngle) * ☃;
   }
}
