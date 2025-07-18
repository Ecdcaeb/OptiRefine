package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIPanic extends EntityAIBase {
   protected final EntityCreature creature;
   protected double speed;
   protected double randPosX;
   protected double randPosY;
   protected double randPosZ;

   public EntityAIPanic(EntityCreature var1, double var2) {
      this.creature = ☃;
      this.speed = ☃;
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      if (this.creature.getRevengeTarget() == null && !this.creature.isBurning()) {
         return false;
      } else {
         if (this.creature.isBurning()) {
            BlockPos ☃ = this.getRandPos(this.creature.world, this.creature, 5, 4);
            if (☃ != null) {
               this.randPosX = ☃.getX();
               this.randPosY = ☃.getY();
               this.randPosZ = ☃.getZ();
               return true;
            }
         }

         return this.findRandomPosition();
      }
   }

   protected boolean findRandomPosition() {
      Vec3d ☃ = RandomPositionGenerator.findRandomTarget(this.creature, 5, 4);
      if (☃ == null) {
         return false;
      } else {
         this.randPosX = ☃.x;
         this.randPosY = ☃.y;
         this.randPosZ = ☃.z;
         return true;
      }
   }

   @Override
   public void startExecuting() {
      this.creature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
   }

   @Override
   public boolean shouldContinueExecuting() {
      return !this.creature.getNavigator().noPath();
   }

   @Nullable
   private BlockPos getRandPos(World var1, Entity var2, int var3, int var4) {
      BlockPos ☃ = new BlockPos(☃);
      int ☃x = ☃.getX();
      int ☃xx = ☃.getY();
      int ☃xxx = ☃.getZ();
      float ☃xxxx = ☃ * ☃ * ☃ * 2;
      BlockPos ☃xxxxx = null;
      BlockPos.MutableBlockPos ☃xxxxxx = new BlockPos.MutableBlockPos();

      for (int ☃xxxxxxx = ☃x - ☃; ☃xxxxxxx <= ☃x + ☃; ☃xxxxxxx++) {
         for (int ☃xxxxxxxx = ☃xx - ☃; ☃xxxxxxxx <= ☃xx + ☃; ☃xxxxxxxx++) {
            for (int ☃xxxxxxxxx = ☃xxx - ☃; ☃xxxxxxxxx <= ☃xxx + ☃; ☃xxxxxxxxx++) {
               ☃xxxxxx.setPos(☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx);
               IBlockState ☃xxxxxxxxxx = ☃.getBlockState(☃xxxxxx);
               if (☃xxxxxxxxxx.getMaterial() == Material.WATER) {
                  float ☃xxxxxxxxxxx = (☃xxxxxxx - ☃x) * (☃xxxxxxx - ☃x) + (☃xxxxxxxx - ☃xx) * (☃xxxxxxxx - ☃xx) + (☃xxxxxxxxx - ☃xxx) * (☃xxxxxxxxx - ☃xxx);
                  if (☃xxxxxxxxxxx < ☃xxxx) {
                     ☃xxxx = ☃xxxxxxxxxxx;
                     ☃xxxxx = new BlockPos(☃xxxxxx);
                  }
               }
            }
         }
      }

      return ☃xxxxx;
   }
}
