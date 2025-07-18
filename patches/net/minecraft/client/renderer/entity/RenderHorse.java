package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;

public class RenderHorse extends RenderLiving<EntityHorse> {
   private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.newHashMap();

   public RenderHorse(RenderManager var1) {
      super(☃, new ModelHorse(), 0.75F);
   }

   protected ResourceLocation getEntityTexture(EntityHorse var1) {
      String ☃ = ☃.getHorseTexture();
      ResourceLocation ☃x = LAYERED_LOCATION_CACHE.get(☃);
      if (☃x == null) {
         ☃x = new ResourceLocation(☃);
         Minecraft.getMinecraft().getTextureManager().loadTexture(☃x, new LayeredTexture(☃.getVariantTexturePaths()));
         LAYERED_LOCATION_CACHE.put(☃, ☃x);
      }

      return ☃x;
   }
}
