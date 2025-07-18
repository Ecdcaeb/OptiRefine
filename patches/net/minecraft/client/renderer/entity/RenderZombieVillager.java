package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.util.ResourceLocation;

public class RenderZombieVillager extends RenderBiped<EntityZombieVillager> {
   private static final ResourceLocation ZOMBIE_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/zombie_villager/zombie_villager.png");
   private static final ResourceLocation ZOMBIE_VILLAGER_FARMER_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_farmer.png");
   private static final ResourceLocation ZOMBIE_VILLAGER_LIBRARIAN_LOC = new ResourceLocation("textures/entity/zombie_villager/zombie_librarian.png");
   private static final ResourceLocation ZOMBIE_VILLAGER_PRIEST_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_priest.png");
   private static final ResourceLocation ZOMBIE_VILLAGER_SMITH_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_smith.png");
   private static final ResourceLocation ZOMBIE_VILLAGER_BUTCHER_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_butcher.png");

   public RenderZombieVillager(RenderManager var1) {
      super(☃, new ModelZombieVillager(), 0.5F);
      this.addLayer(new LayerVillagerArmor(this));
   }

   protected ResourceLocation getEntityTexture(EntityZombieVillager var1) {
      switch (☃.getProfession()) {
         case 0:
            return ZOMBIE_VILLAGER_FARMER_LOCATION;
         case 1:
            return ZOMBIE_VILLAGER_LIBRARIAN_LOC;
         case 2:
            return ZOMBIE_VILLAGER_PRIEST_LOCATION;
         case 3:
            return ZOMBIE_VILLAGER_SMITH_LOCATION;
         case 4:
            return ZOMBIE_VILLAGER_BUTCHER_LOCATION;
         case 5:
         default:
            return ZOMBIE_VILLAGER_TEXTURES;
      }
   }

   protected void applyRotations(EntityZombieVillager var1, float var2, float var3, float var4) {
      if (☃.isConverting()) {
         ☃ += (float)(Math.cos(☃.ticksExisted * 3.25) * Math.PI * 0.25);
      }

      super.applyRotations(☃, ☃, ☃, ☃);
   }
}
