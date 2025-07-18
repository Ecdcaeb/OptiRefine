package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySpider extends EntityMob {
   private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntitySpider.class, DataSerializers.BYTE);

   public EntitySpider(World var1) {
      super(☃);
      this.setSize(1.4F, 0.9F);
   }

   public static void registerFixesSpider(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntitySpider.class);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
      this.tasks.addTask(4, new EntitySpider.AISpiderAttack(this));
      this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(6, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntitySpider.AISpiderTarget<>(this, EntityPlayer.class));
      this.targetTasks.addTask(3, new EntitySpider.AISpiderTarget<>(this, EntityIronGolem.class));
   }

   @Override
   public double getMountedYOffset() {
      return this.height * 0.5F;
   }

   @Override
   protected PathNavigate createNavigator(World var1) {
      return new PathNavigateClimber(this, ☃);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(CLIMBING, (byte)0);
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (!this.world.isRemote) {
         this.setBesideClimbableBlock(this.collidedHorizontally);
      }
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3F);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_SPIDER_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_SPIDER_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_SPIDER_DEATH;
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_SPIDER;
   }

   @Override
   public boolean isOnLadder() {
      return this.isBesideClimbableBlock();
   }

   @Override
   public void setInWeb() {
   }

   @Override
   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.ARTHROPOD;
   }

   @Override
   public boolean isPotionApplicable(PotionEffect var1) {
      return ☃.getPotion() == MobEffects.POISON ? false : super.isPotionApplicable(☃);
   }

   public boolean isBesideClimbableBlock() {
      return (this.dataManager.get(CLIMBING) & 1) != 0;
   }

   public void setBesideClimbableBlock(boolean var1) {
      byte ☃ = this.dataManager.get(CLIMBING);
      if (☃) {
         ☃ = (byte)(☃ | 1);
      } else {
         ☃ = (byte)(☃ & -2);
      }

      this.dataManager.set(CLIMBING, ☃);
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      ☃ = super.onInitialSpawn(☃, ☃);
      if (this.world.rand.nextInt(100) == 0) {
         EntitySkeleton ☃ = new EntitySkeleton(this.world);
         ☃.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
         ☃.onInitialSpawn(☃, null);
         this.world.spawnEntity(☃);
         ☃.startRiding(this);
      }

      if (☃ == null) {
         ☃ = new EntitySpider.GroupData();
         if (this.world.getDifficulty() == EnumDifficulty.HARD && this.world.rand.nextFloat() < 0.1F * ☃.getClampedAdditionalDifficulty()) {
            ((EntitySpider.GroupData)☃).setRandomEffect(this.world.rand);
         }
      }

      if (☃ instanceof EntitySpider.GroupData) {
         Potion ☃ = ((EntitySpider.GroupData)☃).effect;
         if (☃ != null) {
            this.addPotionEffect(new PotionEffect(☃, Integer.MAX_VALUE));
         }
      }

      return ☃;
   }

   @Override
   public float getEyeHeight() {
      return 0.65F;
   }

   static class AISpiderAttack extends EntityAIAttackMelee {
      public AISpiderAttack(EntitySpider var1) {
         super(☃, 1.0, true);
      }

      @Override
      public boolean shouldContinueExecuting() {
         float ☃ = this.attacker.getBrightness();
         if (☃ >= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
            this.attacker.setAttackTarget(null);
            return false;
         } else {
            return super.shouldContinueExecuting();
         }
      }

      @Override
      protected double getAttackReachSqr(EntityLivingBase var1) {
         return 4.0F + ☃.width;
      }
   }

   static class AISpiderTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
      public AISpiderTarget(EntitySpider var1, Class<T> var2) {
         super(☃, ☃, true);
      }

      @Override
      public boolean shouldExecute() {
         float ☃ = this.taskOwner.getBrightness();
         return ☃ >= 0.5F ? false : super.shouldExecute();
      }
   }

   public static class GroupData implements IEntityLivingData {
      public Potion effect;

      public void setRandomEffect(Random var1) {
         int ☃ = ☃.nextInt(5);
         if (☃ <= 1) {
            this.effect = MobEffects.SPEED;
         } else if (☃ <= 2) {
            this.effect = MobEffects.STRENGTH;
         } else if (☃ <= 3) {
            this.effect = MobEffects.REGENERATION;
         } else if (☃ <= 4) {
            this.effect = MobEffects.INVISIBILITY;
         }
      }
   }
}
