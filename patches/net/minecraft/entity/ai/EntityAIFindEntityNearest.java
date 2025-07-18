package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearest extends EntityAIBase {
   private static final Logger LOGGER = LogManager.getLogger();
   private final EntityLiving mob;
   private final Predicate<EntityLivingBase> predicate;
   private final EntityAINearestAttackableTarget.Sorter sorter;
   private EntityLivingBase target;
   private final Class<? extends EntityLivingBase> classToCheck;

   public EntityAIFindEntityNearest(EntityLiving var1, Class<? extends EntityLivingBase> var2) {
      this.mob = ☃;
      this.classToCheck = ☃;
      if (☃ instanceof EntityCreature) {
         LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
      }

      this.predicate = new Predicate<EntityLivingBase>() {
         public boolean apply(@Nullable EntityLivingBase var1) {
            double ☃ = EntityAIFindEntityNearest.this.getFollowRange();
            if (☃.isSneaking()) {
               ☃ *= 0.8F;
            }

            if (☃.isInvisible()) {
               return false;
            } else {
               return ☃.getDistance(EntityAIFindEntityNearest.this.mob) > ☃
                  ? false
                  : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearest.this.mob, ☃, false, true);
            }
         }
      };
      this.sorter = new EntityAINearestAttackableTarget.Sorter(☃);
   }

   @Override
   public boolean shouldExecute() {
      double ☃ = this.getFollowRange();
      List<EntityLivingBase> ☃x = this.mob.world.getEntitiesWithinAABB(this.classToCheck, this.mob.getEntityBoundingBox().grow(☃, 4.0, ☃), this.predicate);
      Collections.sort(☃x, this.sorter);
      if (☃x.isEmpty()) {
         return false;
      } else {
         this.target = ☃x.get(0);
         return true;
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      EntityLivingBase ☃ = this.mob.getAttackTarget();
      if (☃ == null) {
         return false;
      } else if (!☃.isEntityAlive()) {
         return false;
      } else {
         double ☃x = this.getFollowRange();
         return this.mob.getDistanceSq(☃) > ☃x * ☃x ? false : !(☃ instanceof EntityPlayerMP) || !((EntityPlayerMP)☃).interactionManager.isCreative();
      }
   }

   @Override
   public void startExecuting() {
      this.mob.setAttackTarget(this.target);
      super.startExecuting();
   }

   @Override
   public void resetTask() {
      this.mob.setAttackTarget(null);
      super.startExecuting();
   }

   protected double getFollowRange() {
      IAttributeInstance ☃ = this.mob.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
      return ☃ == null ? 16.0 : ☃.getAttributeValue();
   }
}
