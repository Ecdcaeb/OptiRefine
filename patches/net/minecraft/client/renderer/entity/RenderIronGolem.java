package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerIronGolemFlower;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;

public class RenderIronGolem extends RenderLiving<EntityIronGolem> {
   private static final ResourceLocation IRON_GOLEM_TEXTURES = new ResourceLocation("textures/entity/iron_golem.png");

   public RenderIronGolem(RenderManager var1) {
      super(☃, new ModelIronGolem(), 0.5F);
      this.addLayer(new LayerIronGolemFlower(this));
   }

   protected ResourceLocation getEntityTexture(EntityIronGolem var1) {
      return IRON_GOLEM_TEXTURES;
   }

   protected void applyRotations(EntityIronGolem var1, float var2, float var3, float var4) {
      super.applyRotations(☃, ☃, ☃, ☃);
      if (!(☃.limbSwingAmount < 0.01)) {
         float ☃ = 13.0F;
         float ☃x = ☃.limbSwing - ☃.limbSwingAmount * (1.0F - ☃) + 6.0F;
         float ☃xx = (Math.abs(☃x % 13.0F - 6.5F) - 3.25F) / 3.25F;
         GlStateManager.rotate(6.5F * ☃xx, 0.0F, 0.0F, 1.0F);
      }
   }
}
