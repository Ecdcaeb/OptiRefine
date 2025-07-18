package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAISwimming extends EntityAIBase {
   private final EntityLiving entity;

   public EntityAISwimming(EntityLiving var1) {
      this.entity = ☃;
      this.setMutexBits(4);
      if (☃.getNavigator() instanceof PathNavigateGround) {
         ((PathNavigateGround)☃.getNavigator()).setCanSwim(true);
      } else if (☃.getNavigator() instanceof PathNavigateFlying) {
         ((PathNavigateFlying)☃.getNavigator()).setCanFloat(true);
      }
   }

   @Override
   public boolean shouldExecute() {
      return this.entity.isInWater() || this.entity.isInLava();
   }

   @Override
   public void updateTask() {
      if (this.entity.getRNG().nextFloat() < 0.8F) {
         this.entity.getJumpHelper().setJumping();
      }
   }
}
