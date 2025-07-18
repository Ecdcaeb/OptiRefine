package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityIllusionIllager extends EntitySpellcasterIllager implements IRangedAttackMob {
   private int ghostTime;
   private final Vec3d[][] renderLocations;

   public EntityIllusionIllager(World var1) {
      super(☃);
      this.setSize(0.6F, 1.95F);
      this.experienceValue = 5;
      this.renderLocations = new Vec3d[2][4];

      for (int ☃ = 0; ☃ < 4; ☃++) {
         this.renderLocations[0][☃] = new Vec3d(0.0, 0.0, 0.0);
         this.renderLocations[1][☃] = new Vec3d(0.0, 0.0, 0.0);
      }
   }

   @Override
   protected void initEntityAI() {
      super.initEntityAI();
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntitySpellcasterIllager.AICastingApell());
      this.tasks.addTask(4, new EntityIllusionIllager.AIMirriorSpell());
      this.tasks.addTask(5, new EntityIllusionIllager.AIBlindnessSpell());
      this.tasks.addTask(6, new EntityAIAttackRangedBow<>(this, 0.5, 20, 15.0F));
      this.tasks.addTask(8, new EntityAIWander(this, 0.6));
      this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
      this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityIllusionIllager.class));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true).setUnseenMemoryTicks(300));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityVillager.class, false).setUnseenMemoryTicks(300));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityIronGolem.class, false).setUnseenMemoryTicks(300));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
      this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(18.0);
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(32.0);
   }

   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, IEntityLivingData var2) {
      this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
      return super.onInitialSpawn(☃, ☃);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
   }

   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.EMPTY;
   }

   @Override
   public AxisAlignedBB getRenderBoundingBox() {
      return this.getEntityBoundingBox().grow(3.0, 0.0, 3.0);
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.world.isRemote && this.isInvisible()) {
         this.ghostTime--;
         if (this.ghostTime < 0) {
            this.ghostTime = 0;
         }

         if (this.hurtTime == 1 || this.ticksExisted % 1200 == 0) {
            this.ghostTime = 3;
            float ☃ = -6.0F;
            int ☃x = 13;

            for (int ☃xx = 0; ☃xx < 4; ☃xx++) {
               this.renderLocations[0][☃xx] = this.renderLocations[1][☃xx];
               this.renderLocations[1][☃xx] = new Vec3d(
                  (-6.0F + this.rand.nextInt(13)) * 0.5, Math.max(0, this.rand.nextInt(6) - 4), (-6.0F + this.rand.nextInt(13)) * 0.5
               );
            }

            for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
               this.world
                  .spawnParticle(
                     EnumParticleTypes.CLOUD,
                     this.posX + (this.rand.nextDouble() - 0.5) * this.width,
                     this.posY + this.rand.nextDouble() * this.height,
                     this.posZ + (this.rand.nextDouble() - 0.5) * this.width,
                     0.0,
                     0.0,
                     0.0
                  );
            }

            this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ILLAGER_MIRROR_MOVE, this.getSoundCategory(), 1.0F, 1.0F, false);
         } else if (this.hurtTime == this.maxHurtTime - 1) {
            this.ghostTime = 3;

            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.renderLocations[0][☃] = this.renderLocations[1][☃];
               this.renderLocations[1][☃] = new Vec3d(0.0, 0.0, 0.0);
            }
         }
      }
   }

   public Vec3d[] getRenderLocations(float var1) {
      if (this.ghostTime <= 0) {
         return this.renderLocations[1];
      } else {
         double ☃ = (this.ghostTime - ☃) / 3.0F;
         ☃ = Math.pow(☃, 0.25);
         Vec3d[] ☃x = new Vec3d[4];

         for (int ☃xx = 0; ☃xx < 4; ☃xx++) {
            ☃x[☃xx] = this.renderLocations[1][☃xx].scale(1.0 - ☃).add(this.renderLocations[0][☃xx].scale(☃));
         }

         return ☃x;
      }
   }

   @Override
   public boolean isOnSameTeam(Entity var1) {
      if (super.isOnSameTeam(☃)) {
         return true;
      } else {
         return ☃ instanceof EntityLivingBase && ((EntityLivingBase)☃).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER
            ? this.getTeam() == null && ☃.getTeam() == null
            : false;
      }
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_ILLUSION_ILLAGER_AMBIENT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_ILLAGER_DEATH;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_ILLUSION_ILLAGER_HURT;
   }

   @Override
   protected SoundEvent getSpellSound() {
      return SoundEvents.ENTITY_ILLAGER_CAST_SPELL;
   }

   @Override
   public void attackEntityWithRangedAttack(EntityLivingBase var1, float var2) {
      EntityArrow ☃ = this.createArrowEntity(☃);
      double ☃x = ☃.posX - this.posX;
      double ☃xx = ☃.getEntityBoundingBox().minY + ☃.height / 3.0F - ☃.posY;
      double ☃xxx = ☃.posZ - this.posZ;
      double ☃xxxx = MathHelper.sqrt(☃x * ☃x + ☃xxx * ☃xxx);
      ☃.shoot(☃x, ☃xx + ☃xxxx * 0.2F, ☃xxx, 1.6F, 14 - this.world.getDifficulty().getId() * 4);
      this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
      this.world.spawnEntity(☃);
   }

   protected EntityArrow createArrowEntity(float var1) {
      EntityTippedArrow ☃ = new EntityTippedArrow(this.world, this);
      ☃.setEnchantmentEffectsFromEntity(this, ☃);
      return ☃;
   }

   public boolean isAggressive() {
      return this.isAggressive(1);
   }

   @Override
   public void setSwingingArms(boolean var1) {
      this.setAggressive(1, ☃);
   }

   @Override
   public AbstractIllager.IllagerArmPose getArmPose() {
      if (this.isSpellcasting()) {
         return AbstractIllager.IllagerArmPose.SPELLCASTING;
      } else {
         return this.isAggressive() ? AbstractIllager.IllagerArmPose.BOW_AND_ARROW : AbstractIllager.IllagerArmPose.CROSSED;
      }
   }

   class AIBlindnessSpell extends EntitySpellcasterIllager.AIUseSpell {
      private int lastTargetId;

      private AIBlindnessSpell() {
      }

      @Override
      public boolean shouldExecute() {
         if (!super.shouldExecute()) {
            return false;
         } else if (EntityIllusionIllager.this.getAttackTarget() == null) {
            return false;
         } else {
            return EntityIllusionIllager.this.getAttackTarget().getEntityId() == this.lastTargetId
               ? false
               : EntityIllusionIllager.this.world
                  .getDifficultyForLocation(new BlockPos(EntityIllusionIllager.this))
                  .isHarderThan(EnumDifficulty.NORMAL.ordinal());
         }
      }

      @Override
      public void startExecuting() {
         super.startExecuting();
         this.lastTargetId = EntityIllusionIllager.this.getAttackTarget().getEntityId();
      }

      @Override
      protected int getCastingTime() {
         return 20;
      }

      @Override
      protected int getCastingInterval() {
         return 180;
      }

      @Override
      protected void castSpell() {
         EntityIllusionIllager.this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400));
      }

      @Override
      protected SoundEvent getSpellPrepareSound() {
         return SoundEvents.ENTITY_ILLAGER_PREPARE_BLINDNESS;
      }

      @Override
      protected EntitySpellcasterIllager.SpellType getSpellType() {
         return EntitySpellcasterIllager.SpellType.BLINDNESS;
      }
   }

   class AIMirriorSpell extends EntitySpellcasterIllager.AIUseSpell {
      private AIMirriorSpell() {
      }

      @Override
      public boolean shouldExecute() {
         return !super.shouldExecute() ? false : !EntityIllusionIllager.this.isPotionActive(MobEffects.INVISIBILITY);
      }

      @Override
      protected int getCastingTime() {
         return 20;
      }

      @Override
      protected int getCastingInterval() {
         return 340;
      }

      @Override
      protected void castSpell() {
         EntityIllusionIllager.this.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 1200));
      }

      @Nullable
      @Override
      protected SoundEvent getSpellPrepareSound() {
         return SoundEvents.ENTITY_ILLAGER_PREPARE_MIRROR;
      }

      @Override
      protected EntitySpellcasterIllager.SpellType getSpellType() {
         return EntitySpellcasterIllager.SpellType.DISAPPEAR;
      }
   }
}
