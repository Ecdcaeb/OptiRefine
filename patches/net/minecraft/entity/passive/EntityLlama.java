package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILlamaFollowCaravan;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityLlama extends AbstractChestHorse implements IRangedAttackMob {
   private static final DataParameter<Integer> DATA_STRENGTH_ID = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> DATA_VARIANT_ID = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
   private boolean didSpit;
   @Nullable
   private EntityLlama caravanHead;
   @Nullable
   private EntityLlama caravanTail;

   public EntityLlama(World var1) {
      super(☃);
      this.setSize(0.9F, 1.87F);
   }

   private void setStrength(int var1) {
      this.dataManager.set(DATA_STRENGTH_ID, Math.max(1, Math.min(5, ☃)));
   }

   private void setRandomStrength() {
      int ☃ = this.rand.nextFloat() < 0.04F ? 5 : 3;
      this.setStrength(1 + this.rand.nextInt(☃));
   }

   public int getStrength() {
      return this.dataManager.get(DATA_STRENGTH_ID);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("Variant", this.getVariant());
      ☃.setInteger("Strength", this.getStrength());
      if (!this.horseChest.getStackInSlot(1).isEmpty()) {
         ☃.setTag("DecorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      this.setStrength(☃.getInteger("Strength"));
      super.readEntityFromNBT(☃);
      this.setVariant(☃.getInteger("Variant"));
      if (☃.hasKey("DecorItem", 10)) {
         this.horseChest.setInventorySlotContents(1, new ItemStack(☃.getCompoundTag("DecorItem")));
      }

      this.updateHorseSlots();
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2));
      this.tasks.addTask(2, new EntityAILlamaFollowCaravan(this, 2.1F));
      this.tasks.addTask(3, new EntityAIAttackRanged(this, 1.25, 40, 20.0F));
      this.tasks.addTask(3, new EntityAIPanic(this, 1.2));
      this.tasks.addTask(4, new EntityAIMate(this, 1.0));
      this.tasks.addTask(5, new EntityAIFollowParent(this, 1.0));
      this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.7));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityLlama.AIHurtByTarget(this));
      this.targetTasks.addTask(2, new EntityLlama.AIDefendTarget(this));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(DATA_STRENGTH_ID, 0);
      this.dataManager.register(DATA_COLOR_ID, -1);
      this.dataManager.register(DATA_VARIANT_ID, 0);
   }

   public int getVariant() {
      return MathHelper.clamp(this.dataManager.get(DATA_VARIANT_ID), 0, 3);
   }

   public void setVariant(int var1) {
      this.dataManager.set(DATA_VARIANT_ID, ☃);
   }

   @Override
   protected int getInventorySize() {
      return this.hasChest() ? 2 + 3 * this.getInventoryColumns() : super.getInventorySize();
   }

   @Override
   public void updatePassenger(Entity var1) {
      if (this.isPassenger(☃)) {
         float ☃ = MathHelper.cos(this.renderYawOffset * (float) (Math.PI / 180.0));
         float ☃x = MathHelper.sin(this.renderYawOffset * (float) (Math.PI / 180.0));
         float ☃xx = 0.3F;
         ☃.setPosition(this.posX + 0.3F * ☃x, this.posY + this.getMountedYOffset() + ☃.getYOffset(), this.posZ - 0.3F * ☃);
      }
   }

   @Override
   public double getMountedYOffset() {
      return this.height * 0.67;
   }

   @Override
   public boolean canBeSteered() {
      return false;
   }

   @Override
   protected boolean handleEating(EntityPlayer var1, ItemStack var2) {
      int ☃ = 0;
      int ☃x = 0;
      float ☃xx = 0.0F;
      boolean ☃xxx = false;
      Item ☃xxxx = ☃.getItem();
      if (☃xxxx == Items.WHEAT) {
         ☃ = 10;
         ☃x = 3;
         ☃xx = 2.0F;
      } else if (☃xxxx == Item.getItemFromBlock(Blocks.HAY_BLOCK)) {
         ☃ = 90;
         ☃x = 6;
         ☃xx = 10.0F;
         if (this.isTame() && this.getGrowingAge() == 0) {
            ☃xxx = true;
            this.setInLove(☃);
         }
      }

      if (this.getHealth() < this.getMaxHealth() && ☃xx > 0.0F) {
         this.heal(☃xx);
         ☃xxx = true;
      }

      if (this.isChild() && ☃ > 0) {
         this.world
            .spawnParticle(
               EnumParticleTypes.VILLAGER_HAPPY,
               this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
               this.posY + 0.5 + this.rand.nextFloat() * this.height,
               this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
               0.0,
               0.0,
               0.0
            );
         if (!this.world.isRemote) {
            this.addGrowth(☃);
         }

         ☃xxx = true;
      }

      if (☃x > 0 && (☃xxx || !this.isTame()) && this.getTemper() < this.getMaxTemper()) {
         ☃xxx = true;
         if (!this.world.isRemote) {
            this.increaseTemper(☃x);
         }
      }

      if (☃xxx && !this.isSilent()) {
         this.world
            .playSound(
               null,
               this.posX,
               this.posY,
               this.posZ,
               SoundEvents.ENTITY_LLAMA_EAT,
               this.getSoundCategory(),
               1.0F,
               1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F
            );
      }

      return ☃xxx;
   }

   @Override
   protected boolean isMovementBlocked() {
      return this.getHealth() <= 0.0F || this.isEatingHaystack();
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      ☃ = super.onInitialSpawn(☃, ☃);
      this.setRandomStrength();
      int ☃;
      if (☃ instanceof EntityLlama.GroupData) {
         ☃ = ((EntityLlama.GroupData)☃).variant;
      } else {
         ☃ = this.rand.nextInt(4);
         ☃ = new EntityLlama.GroupData(☃);
      }

      this.setVariant(☃);
      return ☃;
   }

   public boolean hasColor() {
      return this.getColor() != null;
   }

   @Override
   protected SoundEvent getAngrySound() {
      return SoundEvents.ENTITY_LLAMA_ANGRY;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_LLAMA_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_LLAMA_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_LLAMA_DEATH;
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound(SoundEvents.ENTITY_LLAMA_STEP, 0.15F, 1.0F);
   }

   @Override
   protected void playChestEquipSound() {
      this.playSound(SoundEvents.ENTITY_LLAMA_CHEST, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
   }

   @Override
   public void makeMad() {
      SoundEvent ☃ = this.getAngrySound();
      if (☃ != null) {
         this.playSound(☃, this.getSoundVolume(), this.getSoundPitch());
      }
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_LLAMA;
   }

   @Override
   public int getInventoryColumns() {
      return this.getStrength();
   }

   @Override
   public boolean wearsArmor() {
      return true;
   }

   @Override
   public boolean isArmor(ItemStack var1) {
      return ☃.getItem() == Item.getItemFromBlock(Blocks.CARPET);
   }

   @Override
   public boolean canBeSaddled() {
      return false;
   }

   @Override
   public void onInventoryChanged(IInventory var1) {
      EnumDyeColor ☃ = this.getColor();
      super.onInventoryChanged(☃);
      EnumDyeColor ☃x = this.getColor();
      if (this.ticksExisted > 20 && ☃x != null && ☃x != ☃) {
         this.playSound(SoundEvents.ENTITY_LLAMA_SWAG, 0.5F, 1.0F);
      }
   }

   @Override
   protected void updateHorseSlots() {
      if (!this.world.isRemote) {
         super.updateHorseSlots();
         this.setColorByItem(this.horseChest.getStackInSlot(1));
      }
   }

   private void setColor(@Nullable EnumDyeColor var1) {
      this.dataManager.set(DATA_COLOR_ID, ☃ == null ? -1 : ☃.getMetadata());
   }

   private void setColorByItem(ItemStack var1) {
      if (this.isArmor(☃)) {
         this.setColor(EnumDyeColor.byMetadata(☃.getMetadata()));
      } else {
         this.setColor(null);
      }
   }

   @Nullable
   public EnumDyeColor getColor() {
      int ☃ = this.dataManager.get(DATA_COLOR_ID);
      return ☃ == -1 ? null : EnumDyeColor.byMetadata(☃);
   }

   @Override
   public int getMaxTemper() {
      return 30;
   }

   @Override
   public boolean canMateWith(EntityAnimal var1) {
      return ☃ != this && ☃ instanceof EntityLlama && this.canMate() && ((EntityLlama)☃).canMate();
   }

   public EntityLlama createChild(EntityAgeable var1) {
      EntityLlama ☃ = new EntityLlama(this.world);
      this.setOffspringAttributes(☃, ☃);
      EntityLlama ☃x = (EntityLlama)☃;
      int ☃xx = this.rand.nextInt(Math.max(this.getStrength(), ☃x.getStrength())) + 1;
      if (this.rand.nextFloat() < 0.03F) {
         ☃xx++;
      }

      ☃.setStrength(☃xx);
      ☃.setVariant(this.rand.nextBoolean() ? this.getVariant() : ☃x.getVariant());
      return ☃;
   }

   private void spit(EntityLivingBase var1) {
      EntityLlamaSpit ☃ = new EntityLlamaSpit(this.world, this);
      double ☃x = ☃.posX - this.posX;
      double ☃xx = ☃.getEntityBoundingBox().minY + ☃.height / 3.0F - ☃.posY;
      double ☃xxx = ☃.posZ - this.posZ;
      float ☃xxxx = MathHelper.sqrt(☃x * ☃x + ☃xxx * ☃xxx) * 0.2F;
      ☃.shoot(☃x, ☃xx + ☃xxxx, ☃xxx, 1.5F, 10.0F);
      this.world
         .playSound(
            null,
            this.posX,
            this.posY,
            this.posZ,
            SoundEvents.ENTITY_LLAMA_SPIT,
            this.getSoundCategory(),
            1.0F,
            1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F
         );
      this.world.spawnEntity(☃);
      this.didSpit = true;
   }

   private void setDidSpit(boolean var1) {
      this.didSpit = ☃;
   }

   @Override
   public void fall(float var1, float var2) {
      int ☃ = MathHelper.ceil((☃ * 0.5F - 3.0F) * ☃);
      if (☃ > 0) {
         if (☃ >= 6.0F) {
            this.attackEntityFrom(DamageSource.FALL, ☃);
            if (this.isBeingRidden()) {
               for (Entity ☃x : this.getRecursivePassengers()) {
                  ☃x.attackEntityFrom(DamageSource.FALL, ☃);
               }
            }
         }

         IBlockState ☃x = this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.2 - this.prevRotationYaw, this.posZ));
         Block ☃xx = ☃x.getBlock();
         if (☃x.getMaterial() != Material.AIR && !this.isSilent()) {
            SoundType ☃xxx = ☃xx.getSoundType();
            this.world
               .playSound(null, this.posX, this.posY, this.posZ, ☃xxx.getStepSound(), this.getSoundCategory(), ☃xxx.getVolume() * 0.5F, ☃xxx.getPitch() * 0.75F);
         }
      }
   }

   public void leaveCaravan() {
      if (this.caravanHead != null) {
         this.caravanHead.caravanTail = null;
      }

      this.caravanHead = null;
   }

   public void joinCaravan(EntityLlama var1) {
      this.caravanHead = ☃;
      this.caravanHead.caravanTail = this;
   }

   public boolean hasCaravanTrail() {
      return this.caravanTail != null;
   }

   public boolean inCaravan() {
      return this.caravanHead != null;
   }

   @Nullable
   public EntityLlama getCaravanHead() {
      return this.caravanHead;
   }

   @Override
   protected double followLeashSpeed() {
      return 2.0;
   }

   @Override
   protected void followMother() {
      if (!this.inCaravan() && this.isChild()) {
         super.followMother();
      }
   }

   @Override
   public boolean canEatGrass() {
      return false;
   }

   @Override
   public void attackEntityWithRangedAttack(EntityLivingBase var1, float var2) {
      this.spit(☃);
   }

   @Override
   public void setSwingingArms(boolean var1) {
   }

   static class AIDefendTarget extends EntityAINearestAttackableTarget<EntityWolf> {
      public AIDefendTarget(EntityLlama var1) {
         super(☃, EntityWolf.class, 16, false, true, null);
      }

      @Override
      public boolean shouldExecute() {
         if (super.shouldExecute() && this.targetEntity != null && !this.targetEntity.isTamed()) {
            return true;
         } else {
            this.taskOwner.setAttackTarget(null);
            return false;
         }
      }

      @Override
      protected double getTargetDistance() {
         return super.getTargetDistance() * 0.25;
      }
   }

   static class AIHurtByTarget extends EntityAIHurtByTarget {
      public AIHurtByTarget(EntityLlama var1) {
         super(☃, false);
      }

      @Override
      public boolean shouldContinueExecuting() {
         if (this.taskOwner instanceof EntityLlama) {
            EntityLlama ☃ = (EntityLlama)this.taskOwner;
            if (☃.didSpit) {
               ☃.setDidSpit(false);
               return false;
            }
         }

         return super.shouldContinueExecuting();
      }
   }

   static class GroupData implements IEntityLivingData {
      public int variant;

      private GroupData(int var1) {
         this.variant = ☃;
      }
   }
}
