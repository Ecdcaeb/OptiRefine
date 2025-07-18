package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveThroughVillage extends EntityAIBase {
   private final EntityCreature entity;
   private final double movementSpeed;
   private Path path;
   private VillageDoorInfo doorInfo;
   private final boolean isNocturnal;
   private final List<VillageDoorInfo> doorList = Lists.newArrayList();

   public EntityAIMoveThroughVillage(EntityCreature var1, double var2, boolean var4) {
      this.entity = ☃;
      this.movementSpeed = ☃;
      this.isNocturnal = ☃;
      this.setMutexBits(1);
      if (!(☃.getNavigator() instanceof PathNavigateGround)) {
         throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
      }
   }

   @Override
   public boolean shouldExecute() {
      this.resizeDoorList();
      if (this.isNocturnal && this.entity.world.isDaytime()) {
         return false;
      } else {
         Village ☃ = this.entity.world.getVillageCollection().getNearestVillage(new BlockPos(this.entity), 0);
         if (☃ == null) {
            return false;
         } else {
            this.doorInfo = this.findNearestDoor(☃);
            if (this.doorInfo == null) {
               return false;
            } else {
               PathNavigateGround ☃x = (PathNavigateGround)this.entity.getNavigator();
               boolean ☃xx = ☃x.getEnterDoors();
               ☃x.setBreakDoors(false);
               this.path = ☃x.getPathToPos(this.doorInfo.getDoorBlockPos());
               ☃x.setBreakDoors(☃xx);
               if (this.path != null) {
                  return true;
               } else {
                  Vec3d ☃xxx = RandomPositionGenerator.findRandomTargetBlockTowards(
                     this.entity,
                     10,
                     7,
                     new Vec3d(this.doorInfo.getDoorBlockPos().getX(), this.doorInfo.getDoorBlockPos().getY(), this.doorInfo.getDoorBlockPos().getZ())
                  );
                  if (☃xxx == null) {
                     return false;
                  } else {
                     ☃x.setBreakDoors(false);
                     this.path = this.entity.getNavigator().getPathToXYZ(☃xxx.x, ☃xxx.y, ☃xxx.z);
                     ☃x.setBreakDoors(☃xx);
                     return this.path != null;
                  }
               }
            }
         }
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      if (this.entity.getNavigator().noPath()) {
         return false;
      } else {
         float ☃ = this.entity.width + 4.0F;
         return this.entity.getDistanceSq(this.doorInfo.getDoorBlockPos()) > ☃ * ☃;
      }
   }

   @Override
   public void startExecuting() {
      this.entity.getNavigator().setPath(this.path, this.movementSpeed);
   }

   @Override
   public void resetTask() {
      if (this.entity.getNavigator().noPath() || this.entity.getDistanceSq(this.doorInfo.getDoorBlockPos()) < 16.0) {
         this.doorList.add(this.doorInfo);
      }
   }

   private VillageDoorInfo findNearestDoor(Village var1) {
      VillageDoorInfo ☃ = null;
      int ☃x = Integer.MAX_VALUE;

      for (VillageDoorInfo ☃xx : ☃.getVillageDoorInfoList()) {
         int ☃xxx = ☃xx.getDistanceSquared(MathHelper.floor(this.entity.posX), MathHelper.floor(this.entity.posY), MathHelper.floor(this.entity.posZ));
         if (☃xxx < ☃x && !this.doesDoorListContain(☃xx)) {
            ☃ = ☃xx;
            ☃x = ☃xxx;
         }
      }

      return ☃;
   }

   private boolean doesDoorListContain(VillageDoorInfo var1) {
      for (VillageDoorInfo ☃ : this.doorList) {
         if (☃.getDoorBlockPos().equals(☃.getDoorBlockPos())) {
            return true;
         }
      }

      return false;
   }

   private void resizeDoorList() {
      if (this.doorList.size() > 15) {
         this.doorList.remove(0);
      }
   }
}
