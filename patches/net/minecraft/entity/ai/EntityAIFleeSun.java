package net.minecraft.entity.ai;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIFleeSun extends EntityAIBase {
   private final EntityCreature creature;
   private double shelterX;
   private double shelterY;
   private double shelterZ;
   private final double movementSpeed;
   private final World world;

   public EntityAIFleeSun(EntityCreature var1, double var2) {
      this.creature = ☃;
      this.movementSpeed = ☃;
      this.world = ☃.world;
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.world.isDaytime()) {
         return false;
      } else if (!this.creature.isBurning()) {
         return false;
      } else if (!this.world.canSeeSky(new BlockPos(this.creature.posX, this.creature.getEntityBoundingBox().minY, this.creature.posZ))) {
         return false;
      } else if (!this.creature.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
         return false;
      } else {
         Vec3d ☃ = this.findPossibleShelter();
         if (☃ == null) {
            return false;
         } else {
            this.shelterX = ☃.x;
            this.shelterY = ☃.y;
            this.shelterZ = ☃.z;
            return true;
         }
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return !this.creature.getNavigator().noPath();
   }

   @Override
   public void startExecuting() {
      this.creature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
   }

   @Nullable
   private Vec3d findPossibleShelter() {
      Random ☃ = this.creature.getRNG();
      BlockPos ☃x = new BlockPos(this.creature.posX, this.creature.getEntityBoundingBox().minY, this.creature.posZ);

      for (int ☃xx = 0; ☃xx < 10; ☃xx++) {
         BlockPos ☃xxx = ☃x.add(☃.nextInt(20) - 10, ☃.nextInt(6) - 3, ☃.nextInt(20) - 10);
         if (!this.world.canSeeSky(☃xxx) && this.creature.getBlockPathWeight(☃xxx) < 0.0F) {
            return new Vec3d(☃xxx.getX(), ☃xxx.getY(), ☃xxx.getZ());
         }
      }

      return null;
   }
}
