package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityBlaze extends EntityMob {
   private float heightOffset = 0.5F;
   private int heightOffsetUpdateTime;
   private static final DataParameter<Byte> ON_FIRE = EntityDataManager.createKey(EntityBlaze.class, DataSerializers.BYTE);

   public EntityBlaze(World var1) {
      super(☃);
      this.setPathPriority(PathNodeType.WATER, -1.0F);
      this.setPathPriority(PathNodeType.LAVA, 8.0F);
      this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
      this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
      this.isImmuneToFire = true;
      this.experienceValue = 10;
   }

   public static void registerFixesBlaze(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityBlaze.class);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(4, new EntityBlaze.AIFireballAttack(this));
      this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
      this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0, 0.0F));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23F);
      this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(ON_FIRE, (byte)0);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_BLAZE_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_BLAZE_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_BLAZE_DEATH;
   }

   @Override
   public int getBrightnessForRender() {
      return 15728880;
   }

   @Override
   public float getBrightness() {
      return 1.0F;
   }

   @Override
   public void onLivingUpdate() {
      if (!this.onGround && this.motionY < 0.0) {
         this.motionY *= 0.6;
      }

      if (this.world.isRemote) {
         if (this.rand.nextInt(24) == 0 && !this.isSilent()) {
            this.world
               .playSound(
                  this.posX + 0.5,
                  this.posY + 0.5,
                  this.posZ + 0.5,
                  SoundEvents.ENTITY_BLAZE_BURN,
                  this.getSoundCategory(),
                  1.0F + this.rand.nextFloat(),
                  this.rand.nextFloat() * 0.7F + 0.3F,
                  false
               );
         }

         for (int ☃ = 0; ☃ < 2; ☃++) {
            this.world
               .spawnParticle(
                  EnumParticleTypes.SMOKE_LARGE,
                  this.posX + (this.rand.nextDouble() - 0.5) * this.width,
                  this.posY + this.rand.nextDouble() * this.height,
                  this.posZ + (this.rand.nextDouble() - 0.5) * this.width,
                  0.0,
                  0.0,
                  0.0
               );
         }
      }

      super.onLivingUpdate();
   }

   @Override
   protected void updateAITasks() {
      if (this.isWet()) {
         this.attackEntityFrom(DamageSource.DROWN, 1.0F);
      }

      this.heightOffsetUpdateTime--;
      if (this.heightOffsetUpdateTime <= 0) {
         this.heightOffsetUpdateTime = 100;
         this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
      }

      EntityLivingBase ☃ = this.getAttackTarget();
      if (☃ != null && ☃.posY + ☃.getEyeHeight() > this.posY + this.getEyeHeight() + this.heightOffset) {
         this.motionY = this.motionY + (0.3F - this.motionY) * 0.3F;
         this.isAirBorne = true;
      }

      super.updateAITasks();
   }

   @Override
   public void fall(float var1, float var2) {
   }

   @Override
   public boolean isBurning() {
      return this.isCharged();
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_BLAZE;
   }

   public boolean isCharged() {
      return (this.dataManager.get(ON_FIRE) & 1) != 0;
   }

   public void setOnFire(boolean var1) {
      byte ☃ = this.dataManager.get(ON_FIRE);
      if (☃) {
         ☃ = (byte)(☃ | 1);
      } else {
         ☃ = (byte)(☃ & -2);
      }

      this.dataManager.set(ON_FIRE, ☃);
   }

   @Override
   protected boolean isValidLightLevel() {
      return true;
   }

   static class AIFireballAttack extends EntityAIBase {
      private final EntityBlaze blaze;
      private int attackStep;
      private int attackTime;

      public AIFireballAttack(EntityBlaze var1) {
         this.blaze = ☃;
         this.setMutexBits(3);
      }

      @Override
      public boolean shouldExecute() {
         EntityLivingBase ☃ = this.blaze.getAttackTarget();
         return ☃ != null && ☃.isEntityAlive();
      }

      @Override
      public void startExecuting() {
         this.attackStep = 0;
      }

      @Override
      public void resetTask() {
         this.blaze.setOnFire(false);
      }

      @Override
      public void updateTask() {
         this.attackTime--;
         EntityLivingBase ☃ = this.blaze.getAttackTarget();
         double ☃x = this.blaze.getDistanceSq(☃);
         if (☃x < 4.0) {
            if (this.attackTime <= 0) {
               this.attackTime = 20;
               this.blaze.attackEntityAsMob(☃);
            }

            this.blaze.getMoveHelper().setMoveTo(☃.posX, ☃.posY, ☃.posZ, 1.0);
         } else if (☃x < this.getFollowDistance() * this.getFollowDistance()) {
            double ☃xx = ☃.posX - this.blaze.posX;
            double ☃xxx = ☃.getEntityBoundingBox().minY + ☃.height / 2.0F - (this.blaze.posY + this.blaze.height / 2.0F);
            double ☃xxxx = ☃.posZ - this.blaze.posZ;
            if (this.attackTime <= 0) {
               this.attackStep++;
               if (this.attackStep == 1) {
                  this.attackTime = 60;
                  this.blaze.setOnFire(true);
               } else if (this.attackStep <= 4) {
                  this.attackTime = 6;
               } else {
                  this.attackTime = 100;
                  this.attackStep = 0;
                  this.blaze.setOnFire(false);
               }

               if (this.attackStep > 1) {
                  float ☃xxxxx = MathHelper.sqrt(MathHelper.sqrt(☃x)) * 0.5F;
                  this.blaze.world.playEvent(null, 1018, new BlockPos((int)this.blaze.posX, (int)this.blaze.posY, (int)this.blaze.posZ), 0);

                  for (int ☃xxxxxx = 0; ☃xxxxxx < 1; ☃xxxxxx++) {
                     EntitySmallFireball ☃xxxxxxx = new EntitySmallFireball(
                        this.blaze.world,
                        this.blaze,
                        ☃xx + this.blaze.getRNG().nextGaussian() * ☃xxxxx,
                        ☃xxx,
                        ☃xxxx + this.blaze.getRNG().nextGaussian() * ☃xxxxx
                     );
                     ☃xxxxxxx.posY = this.blaze.posY + this.blaze.height / 2.0F + 0.5;
                     this.blaze.world.spawnEntity(☃xxxxxxx);
                  }
               }
            }

            this.blaze.getLookHelper().setLookPositionWithEntity(☃, 10.0F, 10.0F);
         } else {
            this.blaze.getNavigator().clearPath();
            this.blaze.getMoveHelper().setMoveTo(☃.posX, ☃.posY, ☃.posZ, 1.0);
         }

         super.updateTask();
      }

      private double getFollowDistance() {
         IAttributeInstance ☃ = this.blaze.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
         return ☃ == null ? 16.0 : ☃.getAttributeValue();
      }
   }
}
