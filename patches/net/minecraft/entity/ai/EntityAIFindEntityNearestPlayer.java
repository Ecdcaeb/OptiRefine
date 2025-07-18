package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearestPlayer extends EntityAIBase {
   private static final Logger LOGGER = LogManager.getLogger();
   private final EntityLiving entityLiving;
   private final Predicate<Entity> predicate;
   private final EntityAINearestAttackableTarget.Sorter sorter;
   private EntityLivingBase entityTarget;

   public EntityAIFindEntityNearestPlayer(EntityLiving var1) {
      this.entityLiving = ☃;
      if (☃ instanceof EntityCreature) {
         LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
      }

      this.predicate = new Predicate<Entity>() {
         public boolean apply(@Nullable Entity var1) {
            if (!(☃ instanceof EntityPlayer)) {
               return false;
            } else if (((EntityPlayer)☃).capabilities.disableDamage) {
               return false;
            } else {
               double ☃ = EntityAIFindEntityNearestPlayer.this.maxTargetRange();
               if (☃.isSneaking()) {
                  ☃ *= 0.8F;
               }

               if (☃.isInvisible()) {
                  float ☃x = ((EntityPlayer)☃).getArmorVisibility();
                  if (☃x < 0.1F) {
                     ☃x = 0.1F;
                  }

                  ☃ *= 0.7F * ☃x;
               }

               return ☃.getDistance(EntityAIFindEntityNearestPlayer.this.entityLiving) > ☃
                  ? false
                  : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearestPlayer.this.entityLiving, (EntityLivingBase)☃, false, true);
            }
         }
      };
      this.sorter = new EntityAINearestAttackableTarget.Sorter(☃);
   }

   @Override
   public boolean shouldExecute() {
      double ☃ = this.maxTargetRange();
      List<EntityPlayer> ☃x = this.entityLiving
         .world
         .getEntitiesWithinAABB(EntityPlayer.class, this.entityLiving.getEntityBoundingBox().grow(☃, 4.0, ☃), this.predicate);
      Collections.sort(☃x, this.sorter);
      if (☃x.isEmpty()) {
         return false;
      } else {
         this.entityTarget = ☃x.get(0);
         return true;
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      EntityLivingBase ☃ = this.entityLiving.getAttackTarget();
      if (☃ == null) {
         return false;
      } else if (!☃.isEntityAlive()) {
         return false;
      } else if (☃ instanceof EntityPlayer && ((EntityPlayer)☃).capabilities.disableDamage) {
         return false;
      } else {
         Team ☃x = this.entityLiving.getTeam();
         Team ☃xx = ☃.getTeam();
         if (☃x != null && ☃xx == ☃x) {
            return false;
         } else {
            double ☃xxx = this.maxTargetRange();
            return this.entityLiving.getDistanceSq(☃) > ☃xxx * ☃xxx
               ? false
               : !(☃ instanceof EntityPlayerMP) || !((EntityPlayerMP)☃).interactionManager.isCreative();
         }
      }
   }

   @Override
   public void startExecuting() {
      this.entityLiving.setAttackTarget(this.entityTarget);
      super.startExecuting();
   }

   @Override
   public void resetTask() {
      this.entityLiving.setAttackTarget(null);
      super.startExecuting();
   }

   protected double maxTargetRange() {
      IAttributeInstance ☃ = this.entityLiving.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
      return ☃ == null ? 16.0 : ☃.getAttributeValue();
   }
}
