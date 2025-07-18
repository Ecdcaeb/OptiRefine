package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAIOpenDoor extends EntityAIDoorInteract {
   boolean closeDoor;
   int closeDoorTemporisation;

   public EntityAIOpenDoor(EntityLiving var1, boolean var2) {
      super(☃);
      this.entity = ☃;
      this.closeDoor = ☃;
   }

   @Override
   public boolean shouldContinueExecuting() {
      return this.closeDoor && this.closeDoorTemporisation > 0 && super.shouldContinueExecuting();
   }

   @Override
   public void startExecuting() {
      this.closeDoorTemporisation = 20;
      this.doorBlock.toggleDoor(this.entity.world, this.doorPosition, true);
   }

   @Override
   public void resetTask() {
      if (this.closeDoor) {
         this.doorBlock.toggleDoor(this.entity.world, this.doorPosition, false);
      }
   }

   @Override
   public void updateTask() {
      this.closeDoorTemporisation--;
      super.updateTask();
   }
}
