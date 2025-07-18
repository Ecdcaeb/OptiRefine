package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveIndoors extends EntityAIBase {
   private final EntityCreature entity;
   private VillageDoorInfo doorInfo;
   private int insidePosX = -1;
   private int insidePosZ = -1;

   public EntityAIMoveIndoors(EntityCreature var1) {
      this.entity = ☃;
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      BlockPos ☃ = new BlockPos(this.entity);
      if ((!this.entity.world.isDaytime() || this.entity.world.isRaining() && !this.entity.world.getBiome(☃).canRain())
         && this.entity.world.provider.hasSkyLight()) {
         if (this.entity.getRNG().nextInt(50) != 0) {
            return false;
         } else if (this.insidePosX != -1 && this.entity.getDistanceSq(this.insidePosX, this.entity.posY, this.insidePosZ) < 4.0) {
            return false;
         } else {
            Village ☃x = this.entity.world.getVillageCollection().getNearestVillage(☃, 14);
            if (☃x == null) {
               return false;
            } else {
               this.doorInfo = ☃x.getDoorInfo(☃);
               return this.doorInfo != null;
            }
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return !this.entity.getNavigator().noPath();
   }

   @Override
   public void startExecuting() {
      this.insidePosX = -1;
      BlockPos ☃ = this.doorInfo.getInsideBlockPos();
      int ☃x = ☃.getX();
      int ☃xx = ☃.getY();
      int ☃xxx = ☃.getZ();
      if (this.entity.getDistanceSq(☃) > 256.0) {
         Vec3d ☃xxxx = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 14, 3, new Vec3d(☃x + 0.5, ☃xx, ☃xxx + 0.5));
         if (☃xxxx != null) {
            this.entity.getNavigator().tryMoveToXYZ(☃xxxx.x, ☃xxxx.y, ☃xxxx.z, 1.0);
         }
      } else {
         this.entity.getNavigator().tryMoveToXYZ(☃x + 0.5, ☃xx, ☃xxx + 0.5, 1.0);
      }
   }

   @Override
   public void resetTask() {
      this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
      this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
      this.doorInfo = null;
   }
}
