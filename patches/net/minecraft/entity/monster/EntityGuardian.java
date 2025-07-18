package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGuardian extends EntityMob {
   private static final DataParameter<Boolean> MOVING = EntityDataManager.createKey(EntityGuardian.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Integer> TARGET_ENTITY = EntityDataManager.createKey(EntityGuardian.class, DataSerializers.VARINT);
   protected float clientSideTailAnimation;
   protected float clientSideTailAnimationO;
   protected float clientSideTailAnimationSpeed;
   protected float clientSideSpikesAnimation;
   protected float clientSideSpikesAnimationO;
   private EntityLivingBase targetedEntity;
   private int clientSideAttackTime;
   private boolean clientSideTouchedGround;
   protected EntityAIWander wander;

   public EntityGuardian(World var1) {
      super(☃);
      this.experienceValue = 10;
      this.setSize(0.85F, 0.85F);
      this.moveHelper = new EntityGuardian.GuardianMoveHelper(this);
      this.clientSideTailAnimation = this.rand.nextFloat();
      this.clientSideTailAnimationO = this.clientSideTailAnimation;
   }

   @Override
   protected void initEntityAI() {
      EntityAIMoveTowardsRestriction ☃ = new EntityAIMoveTowardsRestriction(this, 1.0);
      this.wander = new EntityAIWander(this, 1.0, 80);
      this.tasks.addTask(4, new EntityGuardian.AIGuardianAttack(this));
      this.tasks.addTask(5, ☃);
      this.tasks.addTask(7, this.wander);
      this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityGuardian.class, 12.0F, 0.01F));
      this.tasks.addTask(9, new EntityAILookIdle(this));
      this.wander.setMutexBits(3);
      ☃.setMutexBits(3);
      this.targetTasks
         .addTask(1, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 10, true, false, new EntityGuardian.GuardianTargetSelector(this)));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
      this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0);
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0);
   }

   public static void registerFixesGuardian(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityGuardian.class);
   }

   @Override
   protected PathNavigate createNavigator(World var1) {
      return new PathNavigateSwimmer(this, ☃);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(MOVING, false);
      this.dataManager.register(TARGET_ENTITY, 0);
   }

   public boolean isMoving() {
      return this.dataManager.get(MOVING);
   }

   private void setMoving(boolean var1) {
      this.dataManager.set(MOVING, ☃);
   }

   public int getAttackDuration() {
      return 80;
   }

   private void setTargetedEntity(int var1) {
      this.dataManager.set(TARGET_ENTITY, ☃);
   }

   public boolean hasTargetedEntity() {
      return this.dataManager.get(TARGET_ENTITY) != 0;
   }

   @Nullable
   public EntityLivingBase getTargetedEntity() {
      if (!this.hasTargetedEntity()) {
         return null;
      } else if (this.world.isRemote) {
         if (this.targetedEntity != null) {
            return this.targetedEntity;
         } else {
            Entity ☃ = this.world.getEntityByID(this.dataManager.get(TARGET_ENTITY));
            if (☃ instanceof EntityLivingBase) {
               this.targetedEntity = (EntityLivingBase)☃;
               return this.targetedEntity;
            } else {
               return null;
            }
         }
      } else {
         return this.getAttackTarget();
      }
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      super.notifyDataManagerChange(☃);
      if (TARGET_ENTITY.equals(☃)) {
         this.clientSideAttackTime = 0;
         this.targetedEntity = null;
      }
   }

   @Override
   public int getTalkInterval() {
      return 160;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_AMBIENT : SoundEvents.ENTITY_GUARDIAN_AMBIENT_LAND;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_HURT : SoundEvents.ENTITY_GUARDIAN_HURT_LAND;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_DEATH : SoundEvents.ENTITY_GUARDIAN_DEATH_LAND;
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   public float getEyeHeight() {
      return this.height * 0.5F;
   }

   @Override
   public float getBlockPathWeight(BlockPos var1) {
      return this.world.getBlockState(☃).getMaterial() == Material.WATER ? 10.0F + this.world.getLightBrightness(☃) - 0.5F : super.getBlockPathWeight(☃);
   }

   @Override
   public void onLivingUpdate() {
      if (this.world.isRemote) {
         this.clientSideTailAnimationO = this.clientSideTailAnimation;
         if (!this.isInWater()) {
            this.clientSideTailAnimationSpeed = 2.0F;
            if (this.motionY > 0.0 && this.clientSideTouchedGround && !this.isSilent()) {
               this.world.playSound(this.posX, this.posY, this.posZ, this.getFlopSound(), this.getSoundCategory(), 1.0F, 1.0F, false);
            }

            this.clientSideTouchedGround = this.motionY < 0.0 && this.world.isBlockNormalCube(new BlockPos(this).down(), false);
         } else if (this.isMoving()) {
            if (this.clientSideTailAnimationSpeed < 0.5F) {
               this.clientSideTailAnimationSpeed = 4.0F;
            } else {
               this.clientSideTailAnimationSpeed = this.clientSideTailAnimationSpeed + (0.5F - this.clientSideTailAnimationSpeed) * 0.1F;
            }
         } else {
            this.clientSideTailAnimationSpeed = this.clientSideTailAnimationSpeed + (0.125F - this.clientSideTailAnimationSpeed) * 0.2F;
         }

         this.clientSideTailAnimation = this.clientSideTailAnimation + this.clientSideTailAnimationSpeed;
         this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
         if (!this.isInWater()) {
            this.clientSideSpikesAnimation = this.rand.nextFloat();
         } else if (this.isMoving()) {
            this.clientSideSpikesAnimation = this.clientSideSpikesAnimation + (0.0F - this.clientSideSpikesAnimation) * 0.25F;
         } else {
            this.clientSideSpikesAnimation = this.clientSideSpikesAnimation + (1.0F - this.clientSideSpikesAnimation) * 0.06F;
         }

         if (this.isMoving() && this.isInWater()) {
            Vec3d ☃ = this.getLook(0.0F);

            for (int ☃x = 0; ☃x < 2; ☃x++) {
               this.world
                  .spawnParticle(
                     EnumParticleTypes.WATER_BUBBLE,
                     this.posX + (this.rand.nextDouble() - 0.5) * this.width - ☃.x * 1.5,
                     this.posY + this.rand.nextDouble() * this.height - ☃.y * 1.5,
                     this.posZ + (this.rand.nextDouble() - 0.5) * this.width - ☃.z * 1.5,
                     0.0,
                     0.0,
                     0.0
                  );
            }
         }

         if (this.hasTargetedEntity()) {
            if (this.clientSideAttackTime < this.getAttackDuration()) {
               this.clientSideAttackTime++;
            }

            EntityLivingBase ☃ = this.getTargetedEntity();
            if (☃ != null) {
               this.getLookHelper().setLookPositionWithEntity(☃, 90.0F, 90.0F);
               this.getLookHelper().onUpdateLook();
               double ☃x = this.getAttackAnimationScale(0.0F);
               double ☃xx = ☃.posX - this.posX;
               double ☃xxx = ☃.posY + ☃.height * 0.5F - (this.posY + this.getEyeHeight());
               double ☃xxxx = ☃.posZ - this.posZ;
               double ☃xxxxx = Math.sqrt(☃xx * ☃xx + ☃xxx * ☃xxx + ☃xxxx * ☃xxxx);
               ☃xx /= ☃xxxxx;
               ☃xxx /= ☃xxxxx;
               ☃xxxx /= ☃xxxxx;
               double ☃xxxxxx = this.rand.nextDouble();

               while (☃xxxxxx < ☃xxxxx) {
                  ☃xxxxxx += 1.8 - ☃x + this.rand.nextDouble() * (1.7 - ☃x);
                  this.world
                     .spawnParticle(
                        EnumParticleTypes.WATER_BUBBLE,
                        this.posX + ☃xx * ☃xxxxxx,
                        this.posY + ☃xxx * ☃xxxxxx + this.getEyeHeight(),
                        this.posZ + ☃xxxx * ☃xxxxxx,
                        0.0,
                        0.0,
                        0.0
                     );
               }
            }
         }
      }

      if (this.inWater) {
         this.setAir(300);
      } else if (this.onGround) {
         this.motionY += 0.5;
         this.motionX = this.motionX + (this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F;
         this.motionZ = this.motionZ + (this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F;
         this.rotationYaw = this.rand.nextFloat() * 360.0F;
         this.onGround = false;
         this.isAirBorne = true;
      }

      if (this.hasTargetedEntity()) {
         this.rotationYaw = this.rotationYawHead;
      }

      super.onLivingUpdate();
   }

   protected SoundEvent getFlopSound() {
      return SoundEvents.ENTITY_GUARDIAN_FLOP;
   }

   public float getTailAnimation(float var1) {
      return this.clientSideTailAnimationO + (this.clientSideTailAnimation - this.clientSideTailAnimationO) * ☃;
   }

   public float getSpikesAnimation(float var1) {
      return this.clientSideSpikesAnimationO + (this.clientSideSpikesAnimation - this.clientSideSpikesAnimationO) * ☃;
   }

   public float getAttackAnimationScale(float var1) {
      return (this.clientSideAttackTime + ☃) / this.getAttackDuration();
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_GUARDIAN;
   }

   @Override
   protected boolean isValidLightLevel() {
      return true;
   }

   @Override
   public boolean isNotColliding() {
      return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty();
   }

   @Override
   public boolean getCanSpawnHere() {
      return (this.rand.nextInt(20) == 0 || !this.world.canBlockSeeSky(new BlockPos(this))) && super.getCanSpawnHere();
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (!this.isMoving() && !☃.isMagicDamage() && ☃.getImmediateSource() instanceof EntityLivingBase) {
         EntityLivingBase ☃ = (EntityLivingBase)☃.getImmediateSource();
         if (!☃.isExplosion()) {
            ☃.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0F);
         }
      }

      if (this.wander != null) {
         this.wander.makeUpdate();
      }

      return super.attackEntityFrom(☃, ☃);
   }

   @Override
   public int getVerticalFaceSpeed() {
      return 180;
   }

   @Override
   public void travel(float var1, float var2, float var3) {
      if (this.isServerWorld() && this.isInWater()) {
         this.moveRelative(☃, ☃, ☃, 0.1F);
         this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.9F;
         this.motionY *= 0.9F;
         this.motionZ *= 0.9F;
         if (!this.isMoving() && this.getAttackTarget() == null) {
            this.motionY -= 0.005;
         }
      } else {
         super.travel(☃, ☃, ☃);
      }
   }

   static class AIGuardianAttack extends EntityAIBase {
      private final EntityGuardian guardian;
      private int tickCounter;
      private final boolean isElder;

      public AIGuardianAttack(EntityGuardian var1) {
         this.guardian = ☃;
         this.isElder = ☃ instanceof EntityElderGuardian;
         this.setMutexBits(3);
      }

      @Override
      public boolean shouldExecute() {
         EntityLivingBase ☃ = this.guardian.getAttackTarget();
         return ☃ != null && ☃.isEntityAlive();
      }

      @Override
      public boolean shouldContinueExecuting() {
         return super.shouldContinueExecuting() && (this.isElder || this.guardian.getDistanceSq(this.guardian.getAttackTarget()) > 9.0);
      }

      @Override
      public void startExecuting() {
         this.tickCounter = -10;
         this.guardian.getNavigator().clearPath();
         this.guardian.getLookHelper().setLookPositionWithEntity(this.guardian.getAttackTarget(), 90.0F, 90.0F);
         this.guardian.isAirBorne = true;
      }

      @Override
      public void resetTask() {
         this.guardian.setTargetedEntity(0);
         this.guardian.setAttackTarget(null);
         this.guardian.wander.makeUpdate();
      }

      @Override
      public void updateTask() {
         EntityLivingBase ☃ = this.guardian.getAttackTarget();
         this.guardian.getNavigator().clearPath();
         this.guardian.getLookHelper().setLookPositionWithEntity(☃, 90.0F, 90.0F);
         if (!this.guardian.canEntityBeSeen(☃)) {
            this.guardian.setAttackTarget(null);
         } else {
            this.tickCounter++;
            if (this.tickCounter == 0) {
               this.guardian.setTargetedEntity(this.guardian.getAttackTarget().getEntityId());
               this.guardian.world.setEntityState(this.guardian, (byte)21);
            } else if (this.tickCounter >= this.guardian.getAttackDuration()) {
               float ☃x = 1.0F;
               if (this.guardian.world.getDifficulty() == EnumDifficulty.HARD) {
                  ☃x += 2.0F;
               }

               if (this.isElder) {
                  ☃x += 2.0F;
               }

               ☃.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.guardian, this.guardian), ☃x);
               ☃.attackEntityFrom(
                  DamageSource.causeMobDamage(this.guardian),
                  (float)this.guardian.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()
               );
               this.guardian.setAttackTarget(null);
            }

            super.updateTask();
         }
      }
   }

   static class GuardianMoveHelper extends EntityMoveHelper {
      private final EntityGuardian entityGuardian;

      public GuardianMoveHelper(EntityGuardian var1) {
         super(☃);
         this.entityGuardian = ☃;
      }

      @Override
      public void onUpdateMoveHelper() {
         if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.entityGuardian.getNavigator().noPath()) {
            double ☃ = this.posX - this.entityGuardian.posX;
            double ☃x = this.posY - this.entityGuardian.posY;
            double ☃xx = this.posZ - this.entityGuardian.posZ;
            double ☃xxx = MathHelper.sqrt(☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx);
            ☃x /= ☃xxx;
            float ☃xxxx = (float)(MathHelper.atan2(☃xx, ☃) * 180.0F / (float)Math.PI) - 90.0F;
            this.entityGuardian.rotationYaw = this.limitAngle(this.entityGuardian.rotationYaw, ☃xxxx, 90.0F);
            this.entityGuardian.renderYawOffset = this.entityGuardian.rotationYaw;
            float ☃xxxxx = (float)(this.speed * this.entityGuardian.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
            this.entityGuardian.setAIMoveSpeed(this.entityGuardian.getAIMoveSpeed() + (☃xxxxx - this.entityGuardian.getAIMoveSpeed()) * 0.125F);
            double ☃xxxxxx = Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.5) * 0.05;
            double ☃xxxxxxx = Math.cos(this.entityGuardian.rotationYaw * (float) (Math.PI / 180.0));
            double ☃xxxxxxxx = Math.sin(this.entityGuardian.rotationYaw * (float) (Math.PI / 180.0));
            this.entityGuardian.motionX += ☃xxxxxx * ☃xxxxxxx;
            this.entityGuardian.motionZ += ☃xxxxxx * ☃xxxxxxxx;
            ☃xxxxxx = Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.75) * 0.05;
            this.entityGuardian.motionY += ☃xxxxxx * (☃xxxxxxxx + ☃xxxxxxx) * 0.25;
            this.entityGuardian.motionY = this.entityGuardian.motionY + this.entityGuardian.getAIMoveSpeed() * ☃x * 0.1;
            EntityLookHelper ☃xxxxxxxxx = this.entityGuardian.getLookHelper();
            double ☃xxxxxxxxxx = this.entityGuardian.posX + ☃ / ☃xxx * 2.0;
            double ☃xxxxxxxxxxx = this.entityGuardian.getEyeHeight() + this.entityGuardian.posY + ☃x / ☃xxx;
            double ☃xxxxxxxxxxxx = this.entityGuardian.posZ + ☃xx / ☃xxx * 2.0;
            double ☃xxxxxxxxxxxxx = ☃xxxxxxxxx.getLookPosX();
            double ☃xxxxxxxxxxxxxx = ☃xxxxxxxxx.getLookPosY();
            double ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxx.getLookPosZ();
            if (!☃xxxxxxxxx.getIsLooking()) {
               ☃xxxxxxxxxxxxx = ☃xxxxxxxxxx;
               ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx;
            }

            this.entityGuardian
               .getLookHelper()
               .setLookPosition(
                  ☃xxxxxxxxxxxxx + (☃xxxxxxxxxx - ☃xxxxxxxxxxxxx) * 0.125,
                  ☃xxxxxxxxxxxxxx + (☃xxxxxxxxxxx - ☃xxxxxxxxxxxxxx) * 0.125,
                  ☃xxxxxxxxxxxxxxx + (☃xxxxxxxxxxxx - ☃xxxxxxxxxxxxxxx) * 0.125,
                  10.0F,
                  40.0F
               );
            this.entityGuardian.setMoving(true);
         } else {
            this.entityGuardian.setAIMoveSpeed(0.0F);
            this.entityGuardian.setMoving(false);
         }
      }
   }

   static class GuardianTargetSelector implements Predicate<EntityLivingBase> {
      private final EntityGuardian parentEntity;

      public GuardianTargetSelector(EntityGuardian var1) {
         this.parentEntity = ☃;
      }

      public boolean apply(@Nullable EntityLivingBase var1) {
         return (☃ instanceof EntityPlayer || ☃ instanceof EntitySquid) && ☃.getDistanceSq(this.parentEntity) > 9.0;
      }
   }
}
