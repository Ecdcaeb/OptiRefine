package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;

public class EntityAIFollow extends EntityAIBase {
   private final EntityLiving entity;
   private final Predicate<EntityLiving> followPredicate;
   private EntityLiving followingEntity;
   private final double speedModifier;
   private final PathNavigate navigation;
   private int timeToRecalcPath;
   private final float stopDistance;
   private float oldWaterCost;
   private final float areaSize;

   public EntityAIFollow(final EntityLiving var1, double var2, float var4, float var5) {
      this.entity = ☃;
      this.followPredicate = new Predicate<EntityLiving>() {
         public boolean apply(@Nullable EntityLiving var1x) {
            return ☃ != null && ☃.getClass() != ☃.getClass();
         }
      };
      this.speedModifier = ☃;
      this.navigation = ☃.getNavigator();
      this.stopDistance = ☃;
      this.areaSize = ☃;
      this.setMutexBits(3);
      if (!(☃.getNavigator() instanceof PathNavigateGround) && !(☃.getNavigator() instanceof PathNavigateFlying)) {
         throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
      }
   }

   @Override
   public boolean shouldExecute() {
      List<EntityLiving> ☃ = this.entity
         .world
         .getEntitiesWithinAABB(EntityLiving.class, this.entity.getEntityBoundingBox().grow(this.areaSize), this.followPredicate);
      if (!☃.isEmpty()) {
         for (EntityLiving ☃x : ☃) {
            if (!☃x.isInvisible()) {
               this.followingEntity = ☃x;
               return true;
            }
         }
      }

      return false;
   }

   @Override
   public boolean shouldContinueExecuting() {
      return this.followingEntity != null
         && !this.navigation.noPath()
         && this.entity.getDistanceSq(this.followingEntity) > this.stopDistance * this.stopDistance;
   }

   @Override
   public void startExecuting() {
      this.timeToRecalcPath = 0;
      this.oldWaterCost = this.entity.getPathPriority(PathNodeType.WATER);
      this.entity.setPathPriority(PathNodeType.WATER, 0.0F);
   }

   @Override
   public void resetTask() {
      this.followingEntity = null;
      this.navigation.clearPath();
      this.entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
   }

   @Override
   public void updateTask() {
      if (this.followingEntity != null && !this.entity.getLeashed()) {
         this.entity.getLookHelper().setLookPositionWithEntity(this.followingEntity, 10.0F, this.entity.getVerticalFaceSpeed());
         if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            double ☃ = this.entity.posX - this.followingEntity.posX;
            double ☃x = this.entity.posY - this.followingEntity.posY;
            double ☃xx = this.entity.posZ - this.followingEntity.posZ;
            double ☃xxx = ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
            if (!(☃xxx <= this.stopDistance * this.stopDistance)) {
               this.navigation.tryMoveToEntityLiving(this.followingEntity, this.speedModifier);
            } else {
               this.navigation.clearPath();
               EntityLookHelper ☃xxxx = this.followingEntity.getLookHelper();
               if (☃xxx <= this.stopDistance
                  || ☃xxxx.getLookPosX() == this.entity.posX && ☃xxxx.getLookPosY() == this.entity.posY && ☃xxxx.getLookPosZ() == this.entity.posZ) {
                  double ☃xxxxx = this.followingEntity.posX - this.entity.posX;
                  double ☃xxxxxx = this.followingEntity.posZ - this.entity.posZ;
                  this.navigation.tryMoveToXYZ(this.entity.posX - ☃xxxxx, this.entity.posY, this.entity.posZ - ☃xxxxxx, this.speedModifier);
               }
            }
         }
      }
   }
}
