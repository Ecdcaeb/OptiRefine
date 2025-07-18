package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.Vec3d;

public class EntityAIWander extends EntityAIBase {
   protected final EntityCreature entity;
   protected double x;
   protected double y;
   protected double z;
   protected final double speed;
   protected int executionChance;
   protected boolean mustUpdate;

   public EntityAIWander(EntityCreature var1, double var2) {
      this(☃, ☃, 120);
   }

   public EntityAIWander(EntityCreature var1, double var2, int var4) {
      this.entity = ☃;
      this.speed = ☃;
      this.executionChance = ☃;
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.mustUpdate) {
         if (this.entity.getIdleTime() >= 100) {
            return false;
         }

         if (this.entity.getRNG().nextInt(this.executionChance) != 0) {
            return false;
         }
      }

      Vec3d ☃ = this.getPosition();
      if (☃ == null) {
         return false;
      } else {
         this.x = ☃.x;
         this.y = ☃.y;
         this.z = ☃.z;
         this.mustUpdate = false;
         return true;
      }
   }

   @Nullable
   protected Vec3d getPosition() {
      return RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
   }

   @Override
   public boolean shouldContinueExecuting() {
      return !this.entity.getNavigator().noPath();
   }

   @Override
   public void startExecuting() {
      this.entity.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
   }

   public void makeUpdate() {
      this.mustUpdate = true;
   }

   public void setExecutionChance(int var1) {
      this.executionChance = ☃;
   }
}
