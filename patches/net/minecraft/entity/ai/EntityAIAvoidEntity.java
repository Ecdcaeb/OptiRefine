package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

public class EntityAIAvoidEntity<T extends Entity> extends EntityAIBase {
   private final Predicate<Entity> canBeSeenSelector = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity var1) {
         return ☃.isEntityAlive() && EntityAIAvoidEntity.this.entity.getEntitySenses().canSee(☃) && !EntityAIAvoidEntity.this.entity.isOnSameTeam(☃);
      }
   };
   protected EntityCreature entity;
   private final double farSpeed;
   private final double nearSpeed;
   protected T closestLivingEntity;
   private final float avoidDistance;
   private Path path;
   private final PathNavigate navigation;
   private final Class<T> classToAvoid;
   private final Predicate<? super T> avoidTargetSelector;

   public EntityAIAvoidEntity(EntityCreature var1, Class<T> var2, float var3, double var4, double var6) {
      this(☃, ☃, Predicates.alwaysTrue(), ☃, ☃, ☃);
   }

   public EntityAIAvoidEntity(EntityCreature var1, Class<T> var2, Predicate<? super T> var3, float var4, double var5, double var7) {
      this.entity = ☃;
      this.classToAvoid = ☃;
      this.avoidTargetSelector = ☃;
      this.avoidDistance = ☃;
      this.farSpeed = ☃;
      this.nearSpeed = ☃;
      this.navigation = ☃.getNavigator();
      this.setMutexBits(1);
   }

   @Override
   public boolean shouldExecute() {
      List<T> ☃ = this.entity
         .world
         .getEntitiesWithinAABB(
            this.classToAvoid,
            this.entity.getEntityBoundingBox().grow(this.avoidDistance, 3.0, this.avoidDistance),
            Predicates.and(new Predicate[]{EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector})
         );
      if (☃.isEmpty()) {
         return false;
      } else {
         this.closestLivingEntity = ☃.get(0);
         Vec3d ☃x = RandomPositionGenerator.findRandomTargetBlockAwayFrom(
            this.entity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ)
         );
         if (☃x == null) {
            return false;
         } else if (this.closestLivingEntity.getDistanceSq(☃x.x, ☃x.y, ☃x.z) < this.closestLivingEntity.getDistanceSq(this.entity)) {
            return false;
         } else {
            this.path = this.navigation.getPathToXYZ(☃x.x, ☃x.y, ☃x.z);
            return this.path != null;
         }
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return !this.navigation.noPath();
   }

   @Override
   public void startExecuting() {
      this.navigation.setPath(this.path, this.farSpeed);
   }

   @Override
   public void resetTask() {
      this.closestLivingEntity = null;
   }

   @Override
   public void updateTask() {
      if (this.entity.getDistanceSq(this.closestLivingEntity) < 49.0) {
         this.entity.getNavigator().setSpeed(this.nearSpeed);
      } else {
         this.entity.getNavigator().setSpeed(this.farSpeed);
      }
   }
}
