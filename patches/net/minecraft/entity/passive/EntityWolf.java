package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityWolf extends EntityTameable {
   private static final DataParameter<Float> DATA_HEALTH_ID = EntityDataManager.createKey(EntityWolf.class, DataSerializers.FLOAT);
   private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(EntityWolf.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Integer> COLLAR_COLOR = EntityDataManager.createKey(EntityWolf.class, DataSerializers.VARINT);
   private float headRotationCourse;
   private float headRotationCourseOld;
   private boolean isWet;
   private boolean isShaking;
   private float timeWolfIsShaking;
   private float prevTimeWolfIsShaking;

   public EntityWolf(World var1) {
      super(☃);
      this.setSize(0.6F, 0.85F);
      this.setTamed(false);
   }

   @Override
   protected void initEntityAI() {
      this.aiSit = new EntityAISit(this);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, this.aiSit);
      this.tasks.addTask(3, new EntityWolf.AIAvoidEntity<>(this, EntityLlama.class, 24.0F, 1.5, 1.5));
      this.tasks.addTask(4, new EntityAILeapAtTarget(this, 0.4F));
      this.tasks.addTask(5, new EntityAIAttackMelee(this, 1.0, true));
      this.tasks.addTask(6, new EntityAIFollowOwner(this, 1.0, 10.0F, 2.0F));
      this.tasks.addTask(7, new EntityAIMate(this, 1.0));
      this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1.0));
      this.tasks.addTask(9, new EntityAIBeg(this, 8.0F));
      this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(10, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
      this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
      this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
      this.targetTasks.addTask(4, new EntityAITargetNonTamed<>(this, EntityAnimal.class, false, new Predicate<Entity>() {
         public boolean apply(@Nullable Entity var1) {
            return ☃ instanceof EntitySheep || ☃ instanceof EntityRabbit;
         }
      }));
      this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, AbstractSkeleton.class, false));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3F);
      if (this.isTamed()) {
         this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0);
      } else {
         this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0);
      }

      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0);
   }

   @Override
   public void setAttackTarget(@Nullable EntityLivingBase var1) {
      super.setAttackTarget(☃);
      if (☃ == null) {
         this.setAngry(false);
      } else if (!this.isTamed()) {
         this.setAngry(true);
      }
   }

   @Override
   protected void updateAITasks() {
      this.dataManager.set(DATA_HEALTH_ID, this.getHealth());
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(DATA_HEALTH_ID, this.getHealth());
      this.dataManager.register(BEGGING, false);
      this.dataManager.register(COLLAR_COLOR, EnumDyeColor.RED.getDyeDamage());
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
   }

   public static void registerFixesWolf(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityWolf.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setBoolean("Angry", this.isAngry());
      ☃.setByte("CollarColor", (byte)this.getCollarColor().getDyeDamage());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setAngry(☃.getBoolean("Angry"));
      if (☃.hasKey("CollarColor", 99)) {
         this.setCollarColor(EnumDyeColor.byDyeDamage(☃.getByte("CollarColor")));
      }
   }

   @Override
   protected SoundEvent getAmbientSound() {
      if (this.isAngry()) {
         return SoundEvents.ENTITY_WOLF_GROWL;
      } else if (this.rand.nextInt(3) == 0) {
         return this.isTamed() && this.dataManager.get(DATA_HEALTH_ID) < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
      } else {
         return SoundEvents.ENTITY_WOLF_AMBIENT;
      }
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_WOLF_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_WOLF_DEATH;
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_WOLF;
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (!this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
         this.isShaking = true;
         this.timeWolfIsShaking = 0.0F;
         this.prevTimeWolfIsShaking = 0.0F;
         this.world.setEntityState(this, (byte)8);
      }

      if (!this.world.isRemote && this.getAttackTarget() == null && this.isAngry()) {
         this.setAngry(false);
      }
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      this.headRotationCourseOld = this.headRotationCourse;
      if (this.isBegging()) {
         this.headRotationCourse = this.headRotationCourse + (1.0F - this.headRotationCourse) * 0.4F;
      } else {
         this.headRotationCourse = this.headRotationCourse + (0.0F - this.headRotationCourse) * 0.4F;
      }

      if (this.isWet()) {
         this.isWet = true;
         this.isShaking = false;
         this.timeWolfIsShaking = 0.0F;
         this.prevTimeWolfIsShaking = 0.0F;
      } else if ((this.isWet || this.isShaking) && this.isShaking) {
         if (this.timeWolfIsShaking == 0.0F) {
            this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         }

         this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
         this.timeWolfIsShaking += 0.05F;
         if (this.prevTimeWolfIsShaking >= 2.0F) {
            this.isWet = false;
            this.isShaking = false;
            this.prevTimeWolfIsShaking = 0.0F;
            this.timeWolfIsShaking = 0.0F;
         }

         if (this.timeWolfIsShaking > 0.4F) {
            float ☃ = (float)this.getEntityBoundingBox().minY;
            int ☃x = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float) Math.PI) * 7.0F);

            for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
               float ☃xxx = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
               float ☃xxxx = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
               this.world
                  .spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + ☃xxx, ☃ + 0.8F, this.posZ + ☃xxxx, this.motionX, this.motionY, this.motionZ);
            }
         }
      }
   }

   public boolean isWolfWet() {
      return this.isWet;
   }

   public float getShadingWhileWet(float var1) {
      return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * ☃) / 2.0F * 0.25F;
   }

   public float getShakeAngle(float var1, float var2) {
      float ☃ = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * ☃ + ☃) / 1.8F;
      if (☃ < 0.0F) {
         ☃ = 0.0F;
      } else if (☃ > 1.0F) {
         ☃ = 1.0F;
      }

      return MathHelper.sin(☃ * (float) Math.PI) * MathHelper.sin(☃ * (float) Math.PI * 11.0F) * 0.15F * (float) Math.PI;
   }

   public float getInterestedAngle(float var1) {
      return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * ☃) * 0.15F * (float) Math.PI;
   }

   @Override
   public float getEyeHeight() {
      return this.height * 0.8F;
   }

   @Override
   public int getVerticalFaceSpeed() {
      return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else {
         Entity ☃ = ☃.getTrueSource();
         if (this.aiSit != null) {
            this.aiSit.setSitting(false);
         }

         if (☃ != null && !(☃ instanceof EntityPlayer) && !(☃ instanceof EntityArrow)) {
            ☃ = (☃ + 1.0F) / 2.0F;
         }

         return super.attackEntityFrom(☃, ☃);
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

   @Override
   public void setTamed(boolean var1) {
      super.setTamed(☃);
      if (☃) {
         this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0);
      } else {
         this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0);
      }

      this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0);
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (this.isTamed()) {
         if (!☃.isEmpty()) {
            if (☃.getItem() instanceof ItemFood) {
               ItemFood ☃x = (ItemFood)☃.getItem();
               if (☃x.isWolfsFavoriteMeat() && this.dataManager.get(DATA_HEALTH_ID) < 20.0F) {
                  if (!☃.capabilities.isCreativeMode) {
                     ☃.shrink(1);
                  }

                  this.heal(☃x.getHealAmount(☃));
                  return true;
               }
            } else if (☃.getItem() == Items.DYE) {
               EnumDyeColor ☃x = EnumDyeColor.byDyeDamage(☃.getMetadata());
               if (☃x != this.getCollarColor()) {
                  this.setCollarColor(☃x);
                  if (!☃.capabilities.isCreativeMode) {
                     ☃.shrink(1);
                  }

                  return true;
               }
            }
         }

         if (this.isOwner(☃) && !this.world.isRemote && !this.isBreedingItem(☃)) {
            this.aiSit.setSitting(!this.isSitting());
            this.isJumping = false;
            this.navigator.clearPath();
            this.setAttackTarget(null);
         }
      } else if (☃.getItem() == Items.BONE && !this.isAngry()) {
         if (!☃.capabilities.isCreativeMode) {
            ☃.shrink(1);
         }

         if (!this.world.isRemote) {
            if (this.rand.nextInt(3) == 0) {
               this.setTamedBy(☃);
               this.navigator.clearPath();
               this.setAttackTarget(null);
               this.aiSit.setSitting(true);
               this.setHealth(20.0F);
               this.playTameEffect(true);
               this.world.setEntityState(this, (byte)7);
            } else {
               this.playTameEffect(false);
               this.world.setEntityState(this, (byte)6);
            }
         }

         return true;
      }

      return super.processInteract(☃, ☃);
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 8) {
         this.isShaking = true;
         this.timeWolfIsShaking = 0.0F;
         this.prevTimeWolfIsShaking = 0.0F;
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   public float getTailRotation() {
      if (this.isAngry()) {
         return 1.5393804F;
      } else {
         return this.isTamed() ? (0.55F - (this.getMaxHealth() - this.dataManager.get(DATA_HEALTH_ID)) * 0.02F) * (float) Math.PI : (float) (Math.PI / 5);
      }
   }

   @Override
   public boolean isBreedingItem(ItemStack var1) {
      return ☃.getItem() instanceof ItemFood && ((ItemFood)☃.getItem()).isWolfsFavoriteMeat();
   }

   @Override
   public int getMaxSpawnedInChunk() {
      return 8;
   }

   public boolean isAngry() {
      return (this.dataManager.get(TAMED) & 2) != 0;
   }

   public void setAngry(boolean var1) {
      byte ☃ = this.dataManager.get(TAMED);
      if (☃) {
         this.dataManager.set(TAMED, (byte)(☃ | 2));
      } else {
         this.dataManager.set(TAMED, (byte)(☃ & -3));
      }
   }

   public EnumDyeColor getCollarColor() {
      return EnumDyeColor.byDyeDamage(this.dataManager.get(COLLAR_COLOR) & 15);
   }

   public void setCollarColor(EnumDyeColor var1) {
      this.dataManager.set(COLLAR_COLOR, ☃.getDyeDamage());
   }

   public EntityWolf createChild(EntityAgeable var1) {
      EntityWolf ☃ = new EntityWolf(this.world);
      UUID ☃x = this.getOwnerId();
      if (☃x != null) {
         ☃.setOwnerId(☃x);
         ☃.setTamed(true);
      }

      return ☃;
   }

   public void setBegging(boolean var1) {
      this.dataManager.set(BEGGING, ☃);
   }

   @Override
   public boolean canMateWith(EntityAnimal var1) {
      if (☃ == this) {
         return false;
      } else if (!this.isTamed()) {
         return false;
      } else if (!(☃ instanceof EntityWolf)) {
         return false;
      } else {
         EntityWolf ☃ = (EntityWolf)☃;
         if (!☃.isTamed()) {
            return false;
         } else {
            return ☃.isSitting() ? false : this.isInLove() && ☃.isInLove();
         }
      }
   }

   public boolean isBegging() {
      return this.dataManager.get(BEGGING);
   }

   @Override
   public boolean shouldAttackEntity(EntityLivingBase var1, EntityLivingBase var2) {
      if (!(☃ instanceof EntityCreeper) && !(☃ instanceof EntityGhast)) {
         if (☃ instanceof EntityWolf) {
            EntityWolf ☃ = (EntityWolf)☃;
            if (☃.isTamed() && ☃.getOwner() == ☃) {
               return false;
            }
         }

         return ☃ instanceof EntityPlayer && ☃ instanceof EntityPlayer && !((EntityPlayer)☃).canAttackPlayer((EntityPlayer)☃)
            ? false
            : !(☃ instanceof AbstractHorse) || !((AbstractHorse)☃).isTame();
      } else {
         return false;
      }
   }

   @Override
   public boolean canBeLeashedTo(EntityPlayer var1) {
      return !this.isAngry() && super.canBeLeashedTo(☃);
   }

   class AIAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T> {
      private final EntityWolf wolf;

      public AIAvoidEntity(EntityWolf var2, Class<T> var3, float var4, double var5, double var7) {
         super(☃, ☃, ☃, ☃, ☃);
         this.wolf = ☃;
      }

      @Override
      public boolean shouldExecute() {
         return super.shouldExecute() && this.closestLivingEntity instanceof EntityLlama
            ? !this.wolf.isTamed() && this.avoidLlama((EntityLlama)this.closestLivingEntity)
            : false;
      }

      private boolean avoidLlama(EntityLlama var1) {
         return ☃.getStrength() >= EntityWolf.this.rand.nextInt(5);
      }

      @Override
      public void startExecuting() {
         EntityWolf.this.setAttackTarget(null);
         super.startExecuting();
      }

      @Override
      public void updateTask() {
         EntityWolf.this.setAttackTarget(null);
         super.updateTask();
      }
   }
}
