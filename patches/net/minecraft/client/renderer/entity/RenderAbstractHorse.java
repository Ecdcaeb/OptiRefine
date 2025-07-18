package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.util.ResourceLocation;

public class RenderAbstractHorse extends RenderLiving<AbstractHorse> {
   private static final Map<Class<?>, ResourceLocation> MAP = Maps.newHashMap();
   private final float scale;

   public RenderAbstractHorse(RenderManager var1) {
      this(☃, 1.0F);
   }

   public RenderAbstractHorse(RenderManager var1, float var2) {
      super(☃, new ModelHorse(), 0.75F);
      this.scale = ☃;
   }

   protected void preRenderCallback(AbstractHorse var1, float var2) {
      GlStateManager.scale(this.scale, this.scale, this.scale);
      super.preRenderCallback(☃, ☃);
   }

   protected ResourceLocation getEntityTexture(AbstractHorse var1) {
      return MAP.get(☃.getClass());
   }

   static {
      MAP.put(EntityDonkey.class, new ResourceLocation("textures/entity/horse/donkey.png"));
      MAP.put(EntityMule.class, new ResourceLocation("textures/entity/horse/mule.png"));
      MAP.put(EntityZombieHorse.class, new ResourceLocation("textures/entity/horse/horse_zombie.png"));
      MAP.put(EntitySkeletonHorse.class, new ResourceLocation("textures/entity/horse/horse_skeleton.png"));
   }
}
