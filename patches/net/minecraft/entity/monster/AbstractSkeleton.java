package net.minecraft.entity.monster;

import java.util.Calendar;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public abstract class AbstractSkeleton extends EntityMob implements IRangedAttackMob {
   private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(AbstractSkeleton.class, DataSerializers.BOOLEAN);
   private final EntityAIAttackRangedBow<AbstractSkeleton> aiArrowAttack = new EntityAIAttackRangedBow<>(this, 1.0, 20, 15.0F);
   private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2, false) {
      @Override
      public void resetTask() {
         super.resetTask();
         AbstractSkeleton.this.setSwingingArms(false);
      }

      @Override
      public void startExecuting() {
         super.startExecuting();
         AbstractSkeleton.this.setSwingingArms(true);
      }
   };

   public AbstractSkeleton(World var1) {
      super(☃);
      this.setSize(0.6F, 1.99F);
      this.setCombatTask();
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIRestrictSun(this));
      this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0));
      this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityWolf.class, 6.0F, 1.0, 1.2));
      this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(6, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityIronGolem.class, true));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(SWINGING_ARMS, false);
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound(this.getStepSound(), 0.15F, 1.0F);
   }

   abstract SoundEvent getStepSound();

   @Override
   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   @Override
   public void onLivingUpdate() {
      if (this.world.isDaytime() && !this.world.isRemote) {
         float ☃ = this.getBrightness();
         BlockPos ☃x = this.getRidingEntity() instanceof EntityBoat
            ? new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ).up()
            : new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ);
         if (☃ > 0.5F && this.rand.nextFloat() * 30.0F < (☃ - 0.4F) * 2.0F && this.world.canSeeSky(☃x)) {
            boolean ☃xx = true;
            ItemStack ☃xxx = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (!☃xxx.isEmpty()) {
               if (☃xxx.isItemStackDamageable()) {
                  ☃xxx.setItemDamage(☃xxx.getItemDamage() + this.rand.nextInt(2));
                  if (☃xxx.getItemDamage() >= ☃xxx.getMaxDamage()) {
                     this.renderBrokenItemStack(☃xxx);
                     this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
                  }
               }

               ☃xx = false;
            }

            if (☃xx) {
               this.setFire(8);
            }
         }
      }

      super.onLivingUpdate();
   }

   @Override
   public void updateRidden() {
      super.updateRidden();
      if (this.getRidingEntity() instanceof EntityCreature) {
         EntityCreature ☃ = (EntityCreature)this.getRidingEntity();
         this.renderYawOffset = ☃.renderYawOffset;
      }
   }

   @Override
   protected void setEquipmentBasedOnDifficulty(DifficultyInstance var1) {
      super.setEquipmentBasedOnDifficulty(☃);
      this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      ☃ = super.onInitialSpawn(☃, ☃);
      this.setEquipmentBasedOnDifficulty(☃);
      this.setEnchantmentBasedOnDifficulty(☃);
      this.setCombatTask();
      this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * ☃.getClampedAdditionalDifficulty());
      if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
         Calendar ☃ = this.world.getCurrentDate();
         if (☃.get(2) + 1 == 10 && ☃.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
            this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
         }
      }

      return ☃;
   }

   public void setCombatTask() {
      if (this.world != null && !this.world.isRemote) {
         this.tasks.removeTask(this.aiAttackOnCollide);
         this.tasks.removeTask(this.aiArrowAttack);
         ItemStack ☃ = this.getHeldItemMainhand();
         if (☃.getItem() == Items.BOW) {
            int ☃x = 20;
            if (this.world.getDifficulty() != EnumDifficulty.HARD) {
               ☃x = 40;
            }

            this.aiArrowAttack.setAttackCooldown(☃x);
            this.tasks.addTask(4, this.aiArrowAttack);
         } else {
            this.tasks.addTask(4, this.aiAttackOnCollide);
         }
      }
   }

   @Override
   public void attackEntityWithRangedAttack(EntityLivingBase var1, float var2) {
      EntityArrow ☃ = this.getArrow(☃);
      double ☃x = ☃.posX - this.posX;
      double ☃xx = ☃.getEntityBoundingBox().minY + ☃.height / 3.0F - ☃.posY;
      double ☃xxx = ☃.posZ - this.posZ;
      double ☃xxxx = MathHelper.sqrt(☃x * ☃x + ☃xxx * ☃xxx);
      ☃.shoot(☃x, ☃xx + ☃xxxx * 0.2F, ☃xxx, 1.6F, 14 - this.world.getDifficulty().getId() * 4);
      this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
      this.world.spawnEntity(☃);
   }

   protected EntityArrow getArrow(float var1) {
      EntityTippedArrow ☃ = new EntityTippedArrow(this.world, this);
      ☃.setEnchantmentEffectsFromEntity(this, ☃);
      return ☃;
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setCombatTask();
   }

   @Override
   public void setItemStackToSlot(EntityEquipmentSlot var1, ItemStack var2) {
      super.setItemStackToSlot(☃, ☃);
      if (!this.world.isRemote && ☃ == EntityEquipmentSlot.MAINHAND) {
         this.setCombatTask();
      }
   }

   @Override
   public float getEyeHeight() {
      return 1.74F;
   }

   @Override
   public double getYOffset() {
      return -0.6;
   }

   public boolean isSwingingArms() {
      return this.dataManager.get(SWINGING_ARMS);
   }

   @Override
   public void setSwingingArms(boolean var1) {
      this.dataManager.set(SWINGING_ARMS, ☃);
   }
}
