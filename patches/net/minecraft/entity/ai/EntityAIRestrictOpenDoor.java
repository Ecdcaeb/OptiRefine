package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIRestrictOpenDoor extends EntityAIBase {
   private final EntityCreature entity;
   private VillageDoorInfo frontDoor;

   public EntityAIRestrictOpenDoor(EntityCreature var1) {
      this.entity = ☃;
      if (!(☃.getNavigator() instanceof PathNavigateGround)) {
         throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
      }
   }

   @Override
   public boolean shouldExecute() {
      if (this.entity.world.isDaytime()) {
         return false;
      } else {
         BlockPos ☃ = new BlockPos(this.entity);
         Village ☃x = this.entity.world.getVillageCollection().getNearestVillage(☃, 16);
         if (☃x == null) {
            return false;
         } else {
            this.frontDoor = ☃x.getNearestDoor(☃);
            return this.frontDoor == null ? false : this.frontDoor.getDistanceToInsideBlockSq(☃) < 2.25;
         }
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return this.entity.world.isDaytime() ? false : !this.frontDoor.getIsDetachedFromVillageFlag() && this.frontDoor.isInsideSide(new BlockPos(this.entity));
   }

   @Override
   public void startExecuting() {
      ((PathNavigateGround)this.entity.getNavigator()).setBreakDoors(false);
      ((PathNavigateGround)this.entity.getNavigator()).setEnterDoors(false);
   }

   @Override
   public void resetTask() {
      ((PathNavigateGround)this.entity.getNavigator()).setBreakDoors(true);
      ((PathNavigateGround)this.entity.getNavigator()).setEnterDoors(true);
      this.frontDoor = null;
   }

   @Override
   public void updateTask() {
      this.frontDoor.incrementDoorOpeningRestrictionCounter();
   }
}
