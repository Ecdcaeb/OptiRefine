package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityRabbit extends EntityAnimal {
   private static final DataParameter<Integer> RABBIT_TYPE = EntityDataManager.createKey(EntityRabbit.class, DataSerializers.VARINT);
   private int jumpTicks;
   private int jumpDuration;
   private boolean wasOnGround;
   private int currentMoveTypeDuration;
   private int carrotTicks;

   public EntityRabbit(World var1) {
      super(☃);
      this.setSize(0.4F, 0.5F);
      this.jumpHelper = new EntityRabbit.RabbitJumpHelper(this);
      this.moveHelper = new EntityRabbit.RabbitMoveHelper(this);
      this.setMovementSpeed(0.0);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityRabbit.AIPanic(this, 2.2));
      this.tasks.addTask(2, new EntityAIMate(this, 0.8));
      this.tasks.addTask(3, new EntityAITempt(this, 1.0, Items.CARROT, false));
      this.tasks.addTask(3, new EntityAITempt(this, 1.0, Items.GOLDEN_CARROT, false));
      this.tasks.addTask(3, new EntityAITempt(this, 1.0, Item.getItemFromBlock(Blocks.YELLOW_FLOWER), false));
      this.tasks.addTask(4, new EntityRabbit.AIAvoidEntity<>(this, EntityPlayer.class, 8.0F, 2.2, 2.2));
      this.tasks.addTask(4, new EntityRabbit.AIAvoidEntity<>(this, EntityWolf.class, 10.0F, 2.2, 2.2));
      this.tasks.addTask(4, new EntityRabbit.AIAvoidEntity<>(this, EntityMob.class, 4.0F, 2.2, 2.2));
      this.tasks.addTask(5, new EntityRabbit.AIRaidFarm(this));
      this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6));
      this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
   }

   @Override
   protected float getJumpUpwardsMotion() {
      if (!this.collidedHorizontally && (!this.moveHelper.isUpdating() || !(this.moveHelper.getY() > this.posY + 0.5))) {
         Path ☃ = this.navigator.getPath();
         if (☃ != null && ☃.getCurrentPathIndex() < ☃.getCurrentPathLength()) {
            Vec3d ☃x = ☃.getPosition(this);
            if (☃x.y > this.posY + 0.5) {
               return 0.5F;
            }
         }

         return this.moveHelper.getSpeed() <= 0.6 ? 0.2F : 0.3F;
      } else {
         return 0.5F;
      }
   }

   @Override
   protected void jump() {
      super.jump();
      double ☃ = this.moveHelper.getSpeed();
      if (☃ > 0.0) {
         double ☃x = this.motionX * this.motionX + this.motionZ * this.motionZ;
         if (☃x < 0.010000000000000002) {
            this.moveRelative(0.0F, 0.0F, 1.0F, 0.1F);
         }
      }

      if (!this.world.isRemote) {
         this.world.setEntityState(this, (byte)1);
      }
   }

   public float getJumpCompletion(float var1) {
      return this.jumpDuration == 0 ? 0.0F : (this.jumpTicks + ☃) / this.jumpDuration;
   }

   public void setMovementSpeed(double var1) {
      this.getNavigator().setSpeed(☃);
      this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), ☃);
   }

   @Override
   public void setJumping(boolean var1) {
      super.setJumping(☃);
      if (☃) {
         this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
      }
   }

   public void startJumping() {
      this.setJumping(true);
      this.jumpDuration = 10;
      this.jumpTicks = 0;
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(RABBIT_TYPE, 0);
   }

   @Override
   public void updateAITasks() {
      if (this.currentMoveTypeDuration > 0) {
         this.currentMoveTypeDuration--;
      }

      if (this.carrotTicks > 0) {
         this.carrotTicks = this.carrotTicks - this.rand.nextInt(3);
         if (this.carrotTicks < 0) {
            this.carrotTicks = 0;
         }
      }

      if (this.onGround) {
         if (!this.wasOnGround) {
            this.setJumping(false);
            this.checkLandingDelay();
         }

         if (this.getRabbitType() == 99 && this.currentMoveTypeDuration == 0) {
            EntityLivingBase ☃ = this.getAttackTarget();
            if (☃ != null && this.getDistanceSq(☃) < 16.0) {
               this.calculateRotationYaw(☃.posX, ☃.posZ);
               this.moveHelper.setMoveTo(☃.posX, ☃.posY, ☃.posZ, this.moveHelper.getSpeed());
               this.startJumping();
               this.wasOnGround = true;
            }
         }

         EntityRabbit.RabbitJumpHelper ☃ = (EntityRabbit.RabbitJumpHelper)this.jumpHelper;
         if (!☃.getIsJumping()) {
            if (this.moveHelper.isUpdating() && this.currentMoveTypeDuration == 0) {
               Path ☃x = this.navigator.getPath();
               Vec3d ☃xx = new Vec3d(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ());
               if (☃x != null && ☃x.getCurrentPathIndex() < ☃x.getCurrentPathLength()) {
                  ☃xx = ☃x.getPosition(this);
               }

               this.calculateRotationYaw(☃xx.x, ☃xx.z);
               this.startJumping();
            }
         } else if (!☃.canJump()) {
            this.enableJumpControl();
         }
      }

      this.wasOnGround = this.onGround;
   }

   @Override
   public void spawnRunningParticles() {
   }

   private void calculateRotationYaw(double var1, double var3) {
      this.rotationYaw = (float)(MathHelper.atan2(☃ - this.posZ, ☃ - this.posX) * 180.0F / (float)Math.PI) - 90.0F;
   }

   private void enableJumpControl() {
      ((EntityRabbit.RabbitJumpHelper)this.jumpHelper).setCanJump(true);
   }

   private void disableJumpControl() {
      ((EntityRabbit.RabbitJumpHelper)this.jumpHelper).setCanJump(false);
   }

   private void updateMoveTypeDuration() {
      if (this.moveHelper.getSpeed() < 2.2) {
         this.currentMoveTypeDuration = 10;
      } else {
         this.currentMoveTypeDuration = 1;
      }
   }

   private void checkLandingDelay() {
      this.updateMoveTypeDuration();
      this.disableJumpControl();
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.jumpTicks != this.jumpDuration) {
         this.jumpTicks++;
      } else if (this.jumpDuration != 0) {
         this.jumpTicks = 0;
         this.jumpDuration = 0;
         this.setJumping(false);
      }
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3F);
   }

   public static void registerFixesRabbit(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityRabbit.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("RabbitType", this.getRabbitType());
      ☃.setInteger("MoreCarrotTicks", this.carrotTicks);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setRabbitType(☃.getInteger("RabbitType"));
      this.carrotTicks = ☃.getInteger("MoreCarrotTicks");
   }

   protected SoundEvent getJumpSound() {
      return SoundEvents.ENTITY_RABBIT_JUMP;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_RABBIT_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_RABBIT_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_RABBIT_DEATH;
   }

   @Override
   public boolean attackEntityAsMob(Entity var1) {
      if (this.getRabbitType() == 99) {
         this.playSound(SoundEvents.ENTITY_RABBIT_ATTACK, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         return ☃.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0F);
      } else {
         return ☃.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
      }
   }

   @Override
   public SoundCategory getSoundCategory() {
      return this.getRabbitType() == 99 ? SoundCategory.HOSTILE : SoundCategory.NEUTRAL;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      return this.isEntityInvulnerable(☃) ? false : super.attackEntityFrom(☃, ☃);
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_RABBIT;
   }

   private boolean isRabbitBreedingItem(Item var1) {
      return ☃ == Items.CARROT || ☃ == Items.GOLDEN_CARROT || ☃ == Item.getItemFromBlock(Blocks.YELLOW_FLOWER);
   }

   public EntityRabbit createChild(EntityAgeable var1) {
      EntityRabbit ☃ = new EntityRabbit(this.world);
      int ☃x = this.getRandomRabbitType();
      if (this.rand.nextInt(20) != 0) {
         if (☃ instanceof EntityRabbit && this.rand.nextBoolean()) {
            ☃x = ((EntityRabbit)☃).getRabbitType();
         } else {
            ☃x = this.getRabbitType();
         }
      }

      ☃.setRabbitType(☃x);
      return ☃;
   }

   @Override
   public boolean isBreedingItem(ItemStack var1) {
      return this.isRabbitBreedingItem(☃.getItem());
   }

   public int getRabbitType() {
      return this.dataManager.get(RABBIT_TYPE);
   }

   public void setRabbitType(int var1) {
      if (☃ == 99) {
         this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0);
         this.tasks.addTask(4, new EntityRabbit.AIEvilAttack(this));
         this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
         this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
         this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityWolf.class, true));
         if (!this.hasCustomName()) {
            this.setCustomNameTag(I18n.translateToLocal("entity.KillerBunny.name"));
         }
      }

      this.dataManager.set(RABBIT_TYPE, ☃);
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      ☃ = super.onInitialSpawn(☃, ☃);
      int ☃ = this.getRandomRabbitType();
      boolean ☃x = false;
      if (☃ instanceof EntityRabbit.RabbitTypeData) {
         ☃ = ((EntityRabbit.RabbitTypeData)☃).typeData;
         ☃x = true;
      } else {
         ☃ = new EntityRabbit.RabbitTypeData(☃);
      }

      this.setRabbitType(☃);
      if (☃x) {
         this.setGrowingAge(-24000);
      }

      return ☃;
   }

   private int getRandomRabbitType() {
      Biome ☃ = this.world.getBiome(new BlockPos(this));
      int ☃x = this.rand.nextInt(100);
      if (☃.isSnowyBiome()) {
         return ☃x < 80 ? 1 : 3;
      } else if (☃ instanceof BiomeDesert) {
         return 4;
      } else {
         return ☃x < 50 ? 0 : (☃x < 90 ? 5 : 2);
      }
   }

   private boolean isCarrotEaten() {
      return this.carrotTicks == 0;
   }

   protected void createEatingParticles() {
      BlockCarrot ☃ = (BlockCarrot)Blocks.CARROTS;
      IBlockState ☃x = ☃.withAge(☃.getMaxAge());
      this.world
         .spawnParticle(
            EnumParticleTypes.BLOCK_DUST,
            this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
            this.posY + 0.5 + this.rand.nextFloat() * this.height,
            this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
            0.0,
            0.0,
            0.0,
            Block.getStateId(☃x)
         );
      this.carrotTicks = 40;
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 1) {
         this.createRunningParticles();
         this.jumpDuration = 10;
         this.jumpTicks = 0;
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   static class AIAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T> {
      private final EntityRabbit rabbit;

      public AIAvoidEntity(EntityRabbit var1, Class<T> var2, float var3, double var4, double var6) {
         super(☃, ☃, ☃, ☃, ☃);
         this.rabbit = ☃;
      }

      @Override
      public boolean shouldExecute() {
         return this.rabbit.getRabbitType() != 99 && super.shouldExecute();
      }
   }

   static class AIEvilAttack extends EntityAIAttackMelee {
      public AIEvilAttack(EntityRabbit var1) {
         super(☃, 1.4, true);
      }

      @Override
      protected double getAttackReachSqr(EntityLivingBase var1) {
         return 4.0F + ☃.width;
      }
   }

   static class AIPanic extends EntityAIPanic {
      private final EntityRabbit rabbit;

      public AIPanic(EntityRabbit var1, double var2) {
         super(☃, ☃);
         this.rabbit = ☃;
      }

      @Override
      public void updateTask() {
         super.updateTask();
         this.rabbit.setMovementSpeed(this.speed);
      }
   }

   static class AIRaidFarm extends EntityAIMoveToBlock {
      private final EntityRabbit rabbit;
      private boolean wantsToRaid;
      private boolean canRaid;

      public AIRaidFarm(EntityRabbit var1) {
         super(☃, 0.7F, 16);
         this.rabbit = ☃;
      }

      @Override
      public boolean shouldExecute() {
         if (this.runDelay <= 0) {
            if (!this.rabbit.world.getGameRules().getBoolean("mobGriefing")) {
               return false;
            }

            this.canRaid = false;
            this.wantsToRaid = this.rabbit.isCarrotEaten();
            this.wantsToRaid = true;
         }

         return super.shouldExecute();
      }

      @Override
      public boolean shouldContinueExecuting() {
         return this.canRaid && super.shouldContinueExecuting();
      }

      @Override
      public void updateTask() {
         super.updateTask();
         this.rabbit
            .getLookHelper()
            .setLookPosition(
               this.destinationBlock.getX() + 0.5,
               this.destinationBlock.getY() + 1,
               this.destinationBlock.getZ() + 0.5,
               10.0F,
               this.rabbit.getVerticalFaceSpeed()
            );
         if (this.getIsAboveDestination()) {
            World ☃ = this.rabbit.world;
            BlockPos ☃x = this.destinationBlock.up();
            IBlockState ☃xx = ☃.getBlockState(☃x);
            Block ☃xxx = ☃xx.getBlock();
            if (this.canRaid && ☃xxx instanceof BlockCarrot) {
               Integer ☃xxxx = ☃xx.getValue(BlockCarrot.AGE);
               if (☃xxxx == 0) {
                  ☃.setBlockState(☃x, Blocks.AIR.getDefaultState(), 2);
                  ☃.destroyBlock(☃x, true);
               } else {
                  ☃.setBlockState(☃x, ☃xx.withProperty(BlockCarrot.AGE, ☃xxxx - 1), 2);
                  ☃.playEvent(2001, ☃x, Block.getStateId(☃xx));
               }

               this.rabbit.createEatingParticles();
            }

            this.canRaid = false;
            this.runDelay = 10;
         }
      }

      @Override
      protected boolean shouldMoveTo(World var1, BlockPos var2) {
         Block ☃ = ☃.getBlockState(☃).getBlock();
         if (☃ == Blocks.FARMLAND && this.wantsToRaid && !this.canRaid) {
            ☃ = ☃.up();
            IBlockState ☃x = ☃.getBlockState(☃);
            ☃ = ☃x.getBlock();
            if (☃ instanceof BlockCarrot && ((BlockCarrot)☃).isMaxAge(☃x)) {
               this.canRaid = true;
               return true;
            }
         }

         return false;
      }
   }

   public class RabbitJumpHelper extends EntityJumpHelper {
      private final EntityRabbit rabbit;
      private boolean canJump;

      public RabbitJumpHelper(EntityRabbit var2) {
         super(☃);
         this.rabbit = ☃;
      }

      public boolean getIsJumping() {
         return this.isJumping;
      }

      public boolean canJump() {
         return this.canJump;
      }

      public void setCanJump(boolean var1) {
         this.canJump = ☃;
      }

      @Override
      public void doJump() {
         if (this.isJumping) {
            this.rabbit.startJumping();
            this.isJumping = false;
         }
      }
   }

   static class RabbitMoveHelper extends EntityMoveHelper {
      private final EntityRabbit rabbit;
      private double nextJumpSpeed;

      public RabbitMoveHelper(EntityRabbit var1) {
         super(☃);
         this.rabbit = ☃;
      }

      @Override
      public void onUpdateMoveHelper() {
         if (this.rabbit.onGround && !this.rabbit.isJumping && !((EntityRabbit.RabbitJumpHelper)this.rabbit.jumpHelper).getIsJumping()) {
            this.rabbit.setMovementSpeed(0.0);
         } else if (this.isUpdating()) {
            this.rabbit.setMovementSpeed(this.nextJumpSpeed);
         }

         super.onUpdateMoveHelper();
      }

      @Override
      public void setMoveTo(double var1, double var3, double var5, double var7) {
         if (this.rabbit.isInWater()) {
            ☃ = 1.5;
         }

         super.setMoveTo(☃, ☃, ☃, ☃);
         if (☃ > 0.0) {
            this.nextJumpSpeed = ☃;
         }
      }
   }

   public static class RabbitTypeData implements IEntityLivingData {
      public int typeData;

      public RabbitTypeData(int var1) {
         this.typeData = ☃;
      }
   }
}
