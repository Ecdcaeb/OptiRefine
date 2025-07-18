package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAIRestrictSun extends EntityAIBase {
   private final EntityCreature entity;

   public EntityAIRestrictSun(EntityCreature var1) {
      this.entity = ☃;
   }

   @Override
   public boolean shouldExecute() {
      return this.entity.world.isDaytime() && this.entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
   }

   @Override
   public void startExecuting() {
      ((PathNavigateGround)this.entity.getNavigator()).setAvoidSun(true);
   }

   @Override
   public void resetTask() {
      ((PathNavigateGround)this.entity.getNavigator()).setAvoidSun(false);
   }
}
