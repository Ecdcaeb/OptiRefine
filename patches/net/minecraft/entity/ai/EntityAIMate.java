package net.minecraft.entity.ai;

import java.util.List;
import java.util.Random;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityAIMate extends EntityAIBase {
   private final EntityAnimal animal;
   private final Class<? extends EntityAnimal> mateClass;
   World world;
   private EntityAnimal targetMate;
   int spawnBabyDelay;
   double moveSpeed;

   public EntityAIMate(EntityAnimal var1, double var2) {
      this(☃, ☃, (Class<? extends EntityAnimal>)☃.getClass());
   }

   public EntityAIMate(EntityAnimal var1, double var2, Class<? extends EntityAnimal> var4) {
      this.animal = ☃;
      this.world = ☃.world;
      this.mateClass = ☃;
      this.moveSpeed = ☃;
      this.setMutexBits(3);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.animal.isInLove()) {
         return false;
      } else {
         this.targetMate = this.getNearbyMate();
         return this.targetMate != null;
      }
   }

   @Override
   public boolean shouldContinueExecuting() {
      return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
   }

   @Override
   public void resetTask() {
      this.targetMate = null;
      this.spawnBabyDelay = 0;
   }

   @Override
   public void updateTask() {
      this.animal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, this.animal.getVerticalFaceSpeed());
      this.animal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
      this.spawnBabyDelay++;
      if (this.spawnBabyDelay >= 60 && this.animal.getDistanceSq(this.targetMate) < 9.0) {
         this.spawnBaby();
      }
   }

   private EntityAnimal getNearbyMate() {
      List<EntityAnimal> ☃ = this.world.getEntitiesWithinAABB(this.mateClass, this.animal.getEntityBoundingBox().grow(8.0));
      double ☃x = Double.MAX_VALUE;
      EntityAnimal ☃xx = null;

      for (EntityAnimal ☃xxx : ☃) {
         if (this.animal.canMateWith(☃xxx) && this.animal.getDistanceSq(☃xxx) < ☃x) {
            ☃xx = ☃xxx;
            ☃x = this.animal.getDistanceSq(☃xxx);
         }
      }

      return ☃xx;
   }

   private void spawnBaby() {
      EntityAgeable ☃ = this.animal.createChild(this.targetMate);
      if (☃ != null) {
         EntityPlayerMP ☃x = this.animal.getLoveCause();
         if (☃x == null && this.targetMate.getLoveCause() != null) {
            ☃x = this.targetMate.getLoveCause();
         }

         if (☃x != null) {
            ☃x.addStat(StatList.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(☃x, this.animal, this.targetMate, ☃);
         }

         this.animal.setGrowingAge(6000);
         this.targetMate.setGrowingAge(6000);
         this.animal.resetInLove();
         this.targetMate.resetInLove();
         ☃.setGrowingAge(-24000);
         ☃.setLocationAndAngles(this.animal.posX, this.animal.posY, this.animal.posZ, 0.0F, 0.0F);
         this.world.spawnEntity(☃);
         Random ☃xx = this.animal.getRNG();

         for (int ☃xxx = 0; ☃xxx < 7; ☃xxx++) {
            double ☃xxxx = ☃xx.nextGaussian() * 0.02;
            double ☃xxxxx = ☃xx.nextGaussian() * 0.02;
            double ☃xxxxxx = ☃xx.nextGaussian() * 0.02;
            double ☃xxxxxxx = ☃xx.nextDouble() * this.animal.width * 2.0 - this.animal.width;
            double ☃xxxxxxxx = 0.5 + ☃xx.nextDouble() * this.animal.height;
            double ☃xxxxxxxxx = ☃xx.nextDouble() * this.animal.width * 2.0 - this.animal.width;
            this.world
               .spawnParticle(
                  EnumParticleTypes.HEART, this.animal.posX + ☃xxxxxxx, this.animal.posY + ☃xxxxxxxx, this.animal.posZ + ☃xxxxxxxxx, ☃xxxx, ☃xxxxx, ☃xxxxxx
               );
         }

         if (this.world.getGameRules().getBoolean("doMobLoot")) {
            this.world.spawnEntity(new EntityXPOrb(this.world, this.animal.posX, this.animal.posY, this.animal.posZ, ☃xx.nextInt(7) + 1));
         }
      }
   }
}
