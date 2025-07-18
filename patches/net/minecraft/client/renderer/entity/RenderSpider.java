package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

public class RenderSpider<T extends EntitySpider> extends RenderLiving<T> {
   private static final ResourceLocation SPIDER_TEXTURES = new ResourceLocation("textures/entity/spider/spider.png");

   public RenderSpider(RenderManager var1) {
      super(☃, new ModelSpider(), 1.0F);
      this.addLayer(new LayerSpiderEyes<>(this));
   }

   protected float getDeathMaxRotation(T var1) {
      return 180.0F;
   }

   protected ResourceLocation getEntityTexture(T var1) {
      return SPIDER_TEXTURES;
   }
}
