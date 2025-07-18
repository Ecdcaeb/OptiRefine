package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;

public class RenderOcelot extends RenderLiving<EntityOcelot> {
   private static final ResourceLocation BLACK_OCELOT_TEXTURES = new ResourceLocation("textures/entity/cat/black.png");
   private static final ResourceLocation OCELOT_TEXTURES = new ResourceLocation("textures/entity/cat/ocelot.png");
   private static final ResourceLocation RED_OCELOT_TEXTURES = new ResourceLocation("textures/entity/cat/red.png");
   private static final ResourceLocation SIAMESE_OCELOT_TEXTURES = new ResourceLocation("textures/entity/cat/siamese.png");

   public RenderOcelot(RenderManager var1) {
      super(☃, new ModelOcelot(), 0.4F);
   }

   protected ResourceLocation getEntityTexture(EntityOcelot var1) {
      switch (☃.getTameSkin()) {
         case 0:
         default:
            return OCELOT_TEXTURES;
         case 1:
            return BLACK_OCELOT_TEXTURES;
         case 2:
            return RED_OCELOT_TEXTURES;
         case 3:
            return SIAMESE_OCELOT_TEXTURES;
      }
   }

   protected void preRenderCallback(EntityOcelot var1, float var2) {
      super.preRenderCallback(☃, ☃);
      if (☃.isTamed()) {
         GlStateManager.scale(0.8F, 0.8F, 0.8F);
      }
   }
}
