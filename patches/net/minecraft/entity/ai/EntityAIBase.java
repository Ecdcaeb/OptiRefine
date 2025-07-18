package net.minecraft.entity.ai;

public abstract class EntityAIBase {
   private int mutexBits;

   public abstract boolean shouldExecute();

   public boolean shouldContinueExecuting() {
      return this.shouldExecute();
   }

   public boolean isInterruptible() {
      return true;
   }

   public void startExecuting() {
   }

   public void resetTask() {
   }

   public void updateTask() {
   }

   public void setMutexBits(int var1) {
      this.mutexBits = ☃;
   }

   public int getMutexBits() {
      return this.mutexBits;
   }
}
