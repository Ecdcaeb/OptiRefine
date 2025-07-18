package net.minecraft.entity.ai;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowOwner extends EntityAIBase {
   private final EntityTameable tameable;
   private EntityLivingBase owner;
   World world;
   private final double followSpeed;
   private final PathNavigate petPathfinder;
   private int timeToRecalcPath;
   float maxDist;
   float minDist;
   private float oldWaterCost;

   public EntityAIFollowOwner(EntityTameable var1, double var2, float var4, float var5) {
      this.tameable = ☃;
      this.world = ☃.world;
      this.followSpeed = ☃;
      this.petPathfinder = ☃.getNavigator();
      this.minDist = ☃;
      this.maxDist = ☃;
      this.setMutexBits(3);
      if (!(☃.getNavigator() instanceof PathNavigateGround) && !(☃.getNavigator() instanceof PathNavigateFlying)) {
         throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
      }
   }

   @Override
   public boolean shouldExecute() {
      EntityLivingBase ☃ = this.tameable.getOwner();
      if (☃ == null) {
         return false;
      } else if (☃ instanceof EntityPlayer && ((EntityPlayer)☃).isSpectator()) {
         return false;
      } else if (this.tameable.isSitting()) {
         return false;
      } else if (this.tameable.getDistanceSq(☃) < this.minDist * this.minDist) {
         return false;
      } else {
         this.owner = ☃;
         return true;
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return !this.petPathfinder.noPath() && this.tameable.getDistanceSq(this.owner) > this.maxDist * this.maxDist && !this.tameable.isSitting();
   }

   @Override
   public void startExecuting() {
      this.timeToRecalcPath = 0;
      this.oldWaterCost = this.tameable.getPathPriority(PathNodeType.WATER);
      this.tameable.setPathPriority(PathNodeType.WATER, 0.0F);
   }

   @Override
   public void resetTask() {
      this.owner = null;
      this.petPathfinder.clearPath();
      this.tameable.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
   }

   @Override
   public void updateTask() {
      this.tameable.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, this.tameable.getVerticalFaceSpeed());
      if (!this.tameable.isSitting()) {
         if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.petPathfinder.tryMoveToEntityLiving(this.owner, this.followSpeed)) {
               if (!this.tameable.getLeashed() && !this.tameable.isRiding()) {
                  if (!(this.tameable.getDistanceSq(this.owner) < 144.0)) {
                     int ☃ = MathHelper.floor(this.owner.posX) - 2;
                     int ☃x = MathHelper.floor(this.owner.posZ) - 2;
                     int ☃xx = MathHelper.floor(this.owner.getEntityBoundingBox().minY);

                     for (int ☃xxx = 0; ☃xxx <= 4; ☃xxx++) {
                        for (int ☃xxxx = 0; ☃xxxx <= 4; ☃xxxx++) {
                           if ((☃xxx < 1 || ☃xxxx < 1 || ☃xxx > 3 || ☃xxxx > 3) && this.isTeleportFriendlyBlock(☃, ☃x, ☃xx, ☃xxx, ☃xxxx)) {
                              this.tameable
                                 .setLocationAndAngles(☃ + ☃xxx + 0.5F, ☃xx, ☃x + ☃xxxx + 0.5F, this.tameable.rotationYaw, this.tameable.rotationPitch);
                              this.petPathfinder.clearPath();
                              return;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   protected boolean isTeleportFriendlyBlock(int var1, int var2, int var3, int var4, int var5) {
      BlockPos ☃ = new BlockPos(☃ + ☃, ☃ - 1, ☃ + ☃);
      IBlockState ☃x = this.world.getBlockState(☃);
      return ☃x.getBlockFaceShape(this.world, ☃, EnumFacing.DOWN) == BlockFaceShape.SOLID
         && ☃x.canEntitySpawn(this.tameable)
         && this.world.isAirBlock(☃.up())
         && this.world.isAirBlock(☃.up(2));
   }
}
