package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySlime extends EntityLiving implements IMob {
   private static final DataParameter<Integer> SLIME_SIZE = EntityDataManager.createKey(EntitySlime.class, DataSerializers.VARINT);
   public float squishAmount;
   public float squishFactor;
   public float prevSquishFactor;
   private boolean wasOnGround;

   public EntitySlime(World var1) {
      super(☃);
      this.moveHelper = new EntitySlime.SlimeMoveHelper(this);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(1, new EntitySlime.AISlimeFloat(this));
      this.tasks.addTask(2, new EntitySlime.AISlimeAttack(this));
      this.tasks.addTask(3, new EntitySlime.AISlimeFaceRandom(this));
      this.tasks.addTask(5, new EntitySlime.AISlimeHop(this));
      this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
      this.targetTasks.addTask(3, new EntityAIFindEntityNearest(this, EntityIronGolem.class));
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(SLIME_SIZE, 1);
   }

   protected void setSlimeSize(int var1, boolean var2) {
      this.dataManager.set(SLIME_SIZE, ☃);
      this.setSize(0.51000005F * ☃, 0.51000005F * ☃);
      this.setPosition(this.posX, this.posY, this.posZ);
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(☃ * ☃);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2F + 0.1F * ☃);
      if (☃) {
         this.setHealth(this.getMaxHealth());
      }

      this.experienceValue = ☃;
   }

   public int getSlimeSize() {
      return this.dataManager.get(SLIME_SIZE);
   }

   public static void registerFixesSlime(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntitySlime.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("Size", this.getSlimeSize() - 1);
      ☃.setBoolean("wasOnGround", this.wasOnGround);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      int ☃ = ☃.getInteger("Size");
      if (☃ < 0) {
         ☃ = 0;
      }

      this.setSlimeSize(☃ + 1, false);
      this.wasOnGround = ☃.getBoolean("wasOnGround");
   }

   public boolean isSmallSlime() {
      return this.getSlimeSize() <= 1;
   }

   protected EnumParticleTypes getParticleType() {
      return EnumParticleTypes.SLIME;
   }

   @Override
   public void onUpdate() {
      if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.getSlimeSize() > 0) {
         this.isDead = true;
      }

      this.squishFactor = this.squishFactor + (this.squishAmount - this.squishFactor) * 0.5F;
      this.prevSquishFactor = this.squishFactor;
      super.onUpdate();
      if (this.onGround && !this.wasOnGround) {
         int ☃ = this.getSlimeSize();

         for (int ☃x = 0; ☃x < ☃ * 8; ☃x++) {
            float ☃xx = this.rand.nextFloat() * (float) (Math.PI * 2);
            float ☃xxx = this.rand.nextFloat() * 0.5F + 0.5F;
            float ☃xxxx = MathHelper.sin(☃xx) * ☃ * 0.5F * ☃xxx;
            float ☃xxxxx = MathHelper.cos(☃xx) * ☃ * 0.5F * ☃xxx;
            World var10000 = this.world;
            EnumParticleTypes var10001 = this.getParticleType();
            double var10002 = this.posX + ☃xxxx;
            double var10004 = this.posZ + ☃xxxxx;
            var10000.spawnParticle(var10001, var10002, this.getEntityBoundingBox().minY, var10004, 0.0, 0.0, 0.0);
         }

         this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
         this.squishAmount = -0.5F;
      } else if (!this.onGround && this.wasOnGround) {
         this.squishAmount = 1.0F;
      }

      this.wasOnGround = this.onGround;
      this.alterSquishAmount();
   }

   protected void alterSquishAmount() {
      this.squishAmount *= 0.6F;
   }

   protected int getJumpDelay() {
      return this.rand.nextInt(20) + 10;
   }

   protected EntitySlime createInstance() {
      return new EntitySlime(this.world);
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      if (SLIME_SIZE.equals(☃)) {
         int ☃ = this.getSlimeSize();
         this.setSize(0.51000005F * ☃, 0.51000005F * ☃);
         this.rotationYaw = this.rotationYawHead;
         this.renderYawOffset = this.rotationYawHead;
         if (this.isInWater() && this.rand.nextInt(20) == 0) {
            this.doWaterSplashEffect();
         }
      }

      super.notifyDataManagerChange(☃);
   }

   @Override
   public void setDead() {
      int ☃ = this.getSlimeSize();
      if (!this.world.isRemote && ☃ > 1 && this.getHealth() <= 0.0F) {
         int ☃x = 2 + this.rand.nextInt(3);

         for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
            float ☃xxx = (☃xx % 2 - 0.5F) * ☃ / 4.0F;
            float ☃xxxx = (☃xx / 2 - 0.5F) * ☃ / 4.0F;
            EntitySlime ☃xxxxx = this.createInstance();
            if (this.hasCustomName()) {
               ☃xxxxx.setCustomNameTag(this.getCustomNameTag());
            }

            if (this.isNoDespawnRequired()) {
               ☃xxxxx.enablePersistence();
            }

            ☃xxxxx.setSlimeSize(☃ / 2, true);
            ☃xxxxx.setLocationAndAngles(this.posX + ☃xxx, this.posY + 0.5, this.posZ + ☃xxxx, this.rand.nextFloat() * 360.0F, 0.0F);
            this.world.spawnEntity(☃xxxxx);
         }
      }

      super.setDead();
   }

   @Override
   public void applyEntityCollision(Entity var1) {
      super.applyEntityCollision(☃);
      if (☃ instanceof EntityIronGolem && this.canDamagePlayer()) {
         this.dealDamage((EntityLivingBase)☃);
      }
   }

   @Override
   public void onCollideWithPlayer(EntityPlayer var1) {
      if (this.canDamagePlayer()) {
         this.dealDamage(☃);
      }
   }

   protected void dealDamage(EntityLivingBase var1) {
      int ☃ = this.getSlimeSize();
      if (this.canEntityBeSeen(☃)
         && this.getDistanceSq(☃) < 0.6 * ☃ * (0.6 * ☃)
         && ☃.attackEntityFrom(DamageSource.causeMobDamage(this), this.getAttackStrength())) {
         this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         this.applyEnchantments(this, ☃);
      }
   }

   @Override
   public float getEyeHeight() {
      return 0.625F * this.height;
   }

   protected boolean canDamagePlayer() {
      return !this.isSmallSlime();
   }

   protected int getAttackStrength() {
      return this.getSlimeSize();
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_HURT : SoundEvents.ENTITY_SLIME_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_DEATH : SoundEvents.ENTITY_SLIME_DEATH;
   }

   protected SoundEvent getSquishSound() {
      return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_SQUISH : SoundEvents.ENTITY_SLIME_SQUISH;
   }

   @Override
   protected Item getDropItem() {
      return this.getSlimeSize() == 1 ? Items.SLIME_BALL : null;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return this.getSlimeSize() == 1 ? LootTableList.ENTITIES_SLIME : LootTableList.EMPTY;
   }

   @Override
   public boolean getCanSpawnHere() {
      BlockPos ☃ = new BlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));
      Chunk ☃x = this.world.getChunk(☃);
      if (this.world.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1) {
         return false;
      } else {
         if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
            Biome ☃xx = this.world.getBiome(☃);
            if (☃xx == Biomes.SWAMPLAND
               && this.posY > 50.0
               && this.posY < 70.0
               && this.rand.nextFloat() < 0.5F
               && this.rand.nextFloat() < this.world.getCurrentMoonPhaseFactor()
               && this.world.getLightFromNeighbors(new BlockPos(this)) <= this.rand.nextInt(8)) {
               return super.getCanSpawnHere();
            }

            if (this.rand.nextInt(10) == 0 && ☃x.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0) {
               return super.getCanSpawnHere();
            }
         }

         return false;
      }
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F * this.getSlimeSize();
   }

   @Override
   public int getVerticalFaceSpeed() {
      return 0;
   }

   protected boolean makesSoundOnJump() {
      return this.getSlimeSize() > 0;
   }

   @Override
   protected void jump() {
      this.motionY = 0.42F;
      this.isAirBorne = true;
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      int ☃ = this.rand.nextInt(3);
      if (☃ < 2 && this.rand.nextFloat() < 0.5F * ☃.getClampedAdditionalDifficulty()) {
         ☃++;
      }

      int ☃x = 1 << ☃;
      this.setSlimeSize(☃x, true);
      return super.onInitialSpawn(☃, ☃);
   }

   protected SoundEvent getJumpSound() {
      return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_JUMP : SoundEvents.ENTITY_SLIME_JUMP;
   }

   static class AISlimeAttack extends EntityAIBase {
      private final EntitySlime slime;
      private int growTieredTimer;

      public AISlimeAttack(EntitySlime var1) {
         this.slime = ☃;
         this.setMutexBits(2);
      }

      @Override
      public boolean shouldExecute() {
         EntityLivingBase ☃ = this.slime.getAttackTarget();
         if (☃ == null) {
            return false;
         } else {
            return !☃.isEntityAlive() ? false : !(☃ instanceof EntityPlayer) || !((EntityPlayer)☃).capabilities.disableDamage;
         }
      }

      @Override
      public void startExecuting() {
         this.growTieredTimer = 300;
         super.startExecuting();
      }

      @Override
      public boolean shouldContinueExecuting() {
         EntityLivingBase ☃ = this.slime.getAttackTarget();
         if (☃ == null) {
            return false;
         } else if (!☃.isEntityAlive()) {
            return false;
         } else {
            return ☃ instanceof EntityPlayer && ((EntityPlayer)☃).capabilities.disableDamage ? false : --this.growTieredTimer > 0;
         }
      }

      @Override
      public void updateTask() {
         this.slime.faceEntity(this.slime.getAttackTarget(), 10.0F, 10.0F);
         ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setDirection(this.slime.rotationYaw, this.slime.canDamagePlayer());
      }
   }

   static class AISlimeFaceRandom extends EntityAIBase {
      private final EntitySlime slime;
      private float chosenDegrees;
      private int nextRandomizeTime;

      public AISlimeFaceRandom(EntitySlime var1) {
         this.slime = ☃;
         this.setMutexBits(2);
      }

      @Override
      public boolean shouldExecute() {
         return this.slime.getAttackTarget() == null
            && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava() || this.slime.isPotionActive(MobEffects.LEVITATION));
      }

      @Override
      public void updateTask() {
         if (--this.nextRandomizeTime <= 0) {
            this.nextRandomizeTime = 40 + this.slime.getRNG().nextInt(60);
            this.chosenDegrees = this.slime.getRNG().nextInt(360);
         }

         ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setDirection(this.chosenDegrees, false);
      }
   }

   static class AISlimeFloat extends EntityAIBase {
      private final EntitySlime slime;

      public AISlimeFloat(EntitySlime var1) {
         this.slime = ☃;
         this.setMutexBits(5);
         ((PathNavigateGround)☃.getNavigator()).setCanSwim(true);
      }

      @Override
      public boolean shouldExecute() {
         return this.slime.isInWater() || this.slime.isInLava();
      }

      @Override
      public void updateTask() {
         if (this.slime.getRNG().nextFloat() < 0.8F) {
            this.slime.getJumpHelper().setJumping();
         }

         ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.2);
      }
   }

   static class AISlimeHop extends EntityAIBase {
      private final EntitySlime slime;

      public AISlimeHop(EntitySlime var1) {
         this.slime = ☃;
         this.setMutexBits(5);
      }

      @Override
      public boolean shouldExecute() {
         return true;
      }

      @Override
      public void updateTask() {
         ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.0);
      }
   }

   static class SlimeMoveHelper extends EntityMoveHelper {
      private float yRot;
      private int jumpDelay;
      private final EntitySlime slime;
      private boolean isAggressive;

      public SlimeMoveHelper(EntitySlime var1) {
         super(☃);
         this.slime = ☃;
         this.yRot = 180.0F * ☃.rotationYaw / (float) Math.PI;
      }

      public void setDirection(float var1, boolean var2) {
         this.yRot = ☃;
         this.isAggressive = ☃;
      }

      public void setSpeed(double var1) {
         this.speed = ☃;
         this.action = EntityMoveHelper.Action.MOVE_TO;
      }

      @Override
      public void onUpdateMoveHelper() {
         this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, this.yRot, 90.0F);
         this.entity.rotationYawHead = this.entity.rotationYaw;
         this.entity.renderYawOffset = this.entity.rotationYaw;
         if (this.action != EntityMoveHelper.Action.MOVE_TO) {
            this.entity.setMoveForward(0.0F);
         } else {
            this.action = EntityMoveHelper.Action.WAIT;
            if (this.entity.onGround) {
               this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
               if (this.jumpDelay-- <= 0) {
                  this.jumpDelay = this.slime.getJumpDelay();
                  if (this.isAggressive) {
                     this.jumpDelay /= 3;
                  }

                  this.slime.getJumpHelper().setJumping();
                  if (this.slime.makesSoundOnJump()) {
                     this.slime
                        .playSound(
                           this.slime.getJumpSound(),
                           this.slime.getSoundVolume(),
                           ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F
                        );
                  }
               } else {
                  this.slime.moveStrafing = 0.0F;
                  this.slime.moveForward = 0.0F;
                  this.entity.setAIMoveSpeed(0.0F);
               }
            } else {
               this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
            }
         }
      }
   }
}
