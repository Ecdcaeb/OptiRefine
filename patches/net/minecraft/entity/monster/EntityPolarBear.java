package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityPolarBear extends EntityAnimal {
   private static final DataParameter<Boolean> IS_STANDING = EntityDataManager.createKey(EntityPolarBear.class, DataSerializers.BOOLEAN);
   private float clientSideStandAnimation0;
   private float clientSideStandAnimation;
   private int warningSoundTicks;

   public EntityPolarBear(World var1) {
      super(☃);
      this.setSize(1.3F, 1.4F);
   }

   @Override
   public EntityAgeable createChild(EntityAgeable var1) {
      return new EntityPolarBear(this.world);
   }

   @Override
   public boolean isBreedingItem(ItemStack var1) {
      return false;
   }

   @Override
   protected void initEntityAI() {
      super.initEntityAI();
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityPolarBear.AIMeleeAttack());
      this.tasks.addTask(1, new EntityPolarBear.AIPanic());
      this.tasks.addTask(4, new EntityAIFollowParent(this, 1.25));
      this.tasks.addTask(5, new EntityAIWander(this, 1.0));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(7, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityPolarBear.AIHurtByTarget());
      this.targetTasks.addTask(2, new EntityPolarBear.AIAttackPlayer());
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0);
      this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
      this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return this.isChild() ? SoundEvents.ENTITY_POLAR_BEAR_BABY_AMBIENT : SoundEvents.ENTITY_POLAR_BEAR_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_POLAR_BEAR_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_POLAR_BEAR_DEATH;
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound(SoundEvents.ENTITY_POLAR_BEAR_STEP, 0.15F, 1.0F);
   }

   protected void playWarningSound() {
      if (this.warningSoundTicks <= 0) {
         this.playSound(SoundEvents.ENTITY_POLAR_BEAR_WARNING, 1.0F, 1.0F);
         this.warningSoundTicks = 40;
      }
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_POLAR_BEAR;
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(IS_STANDING, false);
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.world.isRemote) {
         this.clientSideStandAnimation0 = this.clientSideStandAnimation;
         if (this.isStanding()) {
            this.clientSideStandAnimation = MathHelper.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
         } else {
            this.clientSideStandAnimation = MathHelper.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
         }
      }

      if (this.warningSoundTicks > 0) {
         this.warningSoundTicks--;
      }
   }

   @Override
   public boolean attackEntityAsMob(Entity var1) {
      boolean ☃ = ☃.attackEntityFrom(DamageSource.causeMobDamage(this), (int)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
      if (☃) {
         this.applyEnchantments(this, ☃);
      }

      return ☃;
   }

   public boolean isStanding() {
      return this.dataManager.get(IS_STANDING);
   }

   public void setStanding(boolean var1) {
      this.dataManager.set(IS_STANDING, ☃);
   }

   public float getStandingAnimationScale(float var1) {
      return (this.clientSideStandAnimation0 + (this.clientSideStandAnimation - this.clientSideStandAnimation0) * ☃) / 6.0F;
   }

   @Override
   protected float getWaterSlowDown() {
      return 0.98F;
   }

   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, IEntityLivingData var2) {
      if (☃ instanceof EntityPolarBear.GroupData) {
         if (((EntityPolarBear.GroupData)☃).madeParent) {
            this.setGrowingAge(-24000);
         }
      } else {
         EntityPolarBear.GroupData ☃ = new EntityPolarBear.GroupData();
         ☃.madeParent = true;
         ☃ = ☃;
      }

      return ☃;
   }

   class AIAttackPlayer extends EntityAINearestAttackableTarget<EntityPlayer> {
      public AIAttackPlayer() {
         super(EntityPolarBear.this, EntityPlayer.class, 20, true, true, null);
      }

      @Override
      public boolean shouldExecute() {
         if (EntityPolarBear.this.isChild()) {
            return false;
         } else {
            if (super.shouldExecute()) {
               for (EntityPolarBear ☃ : EntityPolarBear.this.world
                  .getEntitiesWithinAABB(EntityPolarBear.class, EntityPolarBear.this.getEntityBoundingBox().grow(8.0, 4.0, 8.0))) {
                  if (☃.isChild()) {
                     return true;
                  }
               }
            }

            EntityPolarBear.this.setAttackTarget(null);
            return false;
         }
      }

      @Override
      protected double getTargetDistance() {
         return super.getTargetDistance() * 0.5;
      }
   }

   class AIHurtByTarget extends EntityAIHurtByTarget {
      public AIHurtByTarget() {
         super(EntityPolarBear.this, false);
      }

      @Override
      public void startExecuting() {
         super.startExecuting();
         if (EntityPolarBear.this.isChild()) {
            this.alertOthers();
            this.resetTask();
         }
      }

      @Override
      protected void setEntityAttackTarget(EntityCreature var1, EntityLivingBase var2) {
         if (☃ instanceof EntityPolarBear && !☃.isChild()) {
            super.setEntityAttackTarget(☃, ☃);
         }
      }
   }

   class AIMeleeAttack extends EntityAIAttackMelee {
      public AIMeleeAttack() {
         super(EntityPolarBear.this, 1.25, true);
      }

      @Override
      protected void checkAndPerformAttack(EntityLivingBase var1, double var2) {
         double ☃ = this.getAttackReachSqr(☃);
         if (☃ <= ☃ && this.attackTick <= 0) {
            this.attackTick = 20;
            this.attacker.attackEntityAsMob(☃);
            EntityPolarBear.this.setStanding(false);
         } else if (☃ <= ☃ * 2.0) {
            if (this.attackTick <= 0) {
               EntityPolarBear.this.setStanding(false);
               this.attackTick = 20;
            }

            if (this.attackTick <= 10) {
               EntityPolarBear.this.setStanding(true);
               EntityPolarBear.this.playWarningSound();
            }
         } else {
            this.attackTick = 20;
            EntityPolarBear.this.setStanding(false);
         }
      }

      @Override
      public void resetTask() {
         EntityPolarBear.this.setStanding(false);
         super.resetTask();
      }

      @Override
      protected double getAttackReachSqr(EntityLivingBase var1) {
         return 4.0F + ☃.width;
      }
   }

   class AIPanic extends EntityAIPanic {
      public AIPanic() {
         super(EntityPolarBear.this, 2.0);
      }

      @Override
      public boolean shouldExecute() {
         return !EntityPolarBear.this.isChild() && !EntityPolarBear.this.isBurning() ? false : super.shouldExecute();
      }
   }

   static class GroupData implements IEntityLivingData {
      public boolean madeParent;

      private GroupData() {
      }
   }
}
