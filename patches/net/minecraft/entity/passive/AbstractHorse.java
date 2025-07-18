package net.minecraft.entity.passive;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public abstract class AbstractHorse extends EntityAnimal implements IInventoryChangedListener, IJumpingMount {
   private static final Predicate<Entity> IS_HORSE_BREEDING = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity var1) {
         return ☃ instanceof AbstractHorse && ((AbstractHorse)☃).isBreeding();
      }
   };
   protected static final IAttribute JUMP_STRENGTH = new RangedAttribute(null, "horse.jumpStrength", 0.7, 0.0, 2.0)
      .setDescription("Jump Strength")
      .setShouldWatch(true);
   private static final DataParameter<Byte> STATUS = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.BYTE);
   private static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.OPTIONAL_UNIQUE_ID);
   private int eatingCounter;
   private int openMouthCounter;
   private int jumpRearingCounter;
   public int tailCounter;
   public int sprintCounter;
   protected boolean horseJumping;
   protected ContainerHorseChest horseChest;
   protected int temper;
   protected float jumpPower;
   private boolean allowStandSliding;
   private float headLean;
   private float prevHeadLean;
   private float rearingAmount;
   private float prevRearingAmount;
   private float mouthOpenness;
   private float prevMouthOpenness;
   protected boolean canGallop = true;
   protected int gallopTime;

   public AbstractHorse(World var1) {
      super(☃);
      this.setSize(1.3964844F, 1.6F);
      this.stepHeight = 1.0F;
      this.initHorseChest();
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIPanic(this, 1.2));
      this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2));
      this.tasks.addTask(2, new EntityAIMate(this, 1.0, AbstractHorse.class));
      this.tasks.addTask(4, new EntityAIFollowParent(this, 1.0));
      this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.7));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(STATUS, (byte)0);
      this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
   }

   protected boolean getHorseWatchableBoolean(int var1) {
      return (this.dataManager.get(STATUS) & ☃) != 0;
   }

   protected void setHorseWatchableBoolean(int var1, boolean var2) {
      byte ☃ = this.dataManager.get(STATUS);
      if (☃) {
         this.dataManager.set(STATUS, (byte)(☃ | ☃));
      } else {
         this.dataManager.set(STATUS, (byte)(☃ & ~☃));
      }
   }

   public boolean isTame() {
      return this.getHorseWatchableBoolean(2);
   }

   @Nullable
   public UUID getOwnerUniqueId() {
      return (UUID)this.dataManager.get(OWNER_UNIQUE_ID).orNull();
   }

   public void setOwnerUniqueId(@Nullable UUID var1) {
      this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(☃));
   }

   public float getHorseSize() {
      return 0.5F;
   }

   @Override
   public void setScaleForAge(boolean var1) {
      this.setScale(☃ ? this.getHorseSize() : 1.0F);
   }

   public boolean isHorseJumping() {
      return this.horseJumping;
   }

   public void setHorseTamed(boolean var1) {
      this.setHorseWatchableBoolean(2, ☃);
   }

   public void setHorseJumping(boolean var1) {
      this.horseJumping = ☃;
   }

   @Override
   public boolean canBeLeashedTo(EntityPlayer var1) {
      return super.canBeLeashedTo(☃) && this.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
   }

   @Override
   protected void onLeashDistance(float var1) {
      if (☃ > 6.0F && this.isEatingHaystack()) {
         this.setEatingHaystack(false);
      }
   }

   public boolean isEatingHaystack() {
      return this.getHorseWatchableBoolean(16);
   }

   public boolean isRearing() {
      return this.getHorseWatchableBoolean(32);
   }

   public boolean isBreeding() {
      return this.getHorseWatchableBoolean(8);
   }

   public void setBreeding(boolean var1) {
      this.setHorseWatchableBoolean(8, ☃);
   }

   public void setHorseSaddled(boolean var1) {
      this.setHorseWatchableBoolean(4, ☃);
   }

   public int getTemper() {
      return this.temper;
   }

   public void setTemper(int var1) {
      this.temper = ☃;
   }

   public int increaseTemper(int var1) {
      int ☃ = MathHelper.clamp(this.getTemper() + ☃, 0, this.getMaxTemper());
      this.setTemper(☃);
      return ☃;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      Entity ☃ = ☃.getTrueSource();
      return this.isBeingRidden() && ☃ != null && this.isRidingOrBeingRiddenBy(☃) ? false : super.attackEntityFrom(☃, ☃);
   }

   @Override
   public boolean canBePushed() {
      return !this.isBeingRidden();
   }

   private void eatingHorse() {
      this.openHorseMouth();
      if (!this.isSilent()) {
         this.world
            .playSound(
               null,
               this.posX,
               this.posY,
               this.posZ,
               SoundEvents.ENTITY_HORSE_EAT,
               this.getSoundCategory(),
               1.0F,
               1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F
            );
      }
   }

   @Override
   public void fall(float var1, float var2) {
      if (☃ > 1.0F) {
         this.playSound(SoundEvents.ENTITY_HORSE_LAND, 0.4F, 1.0F);
      }

      int ☃ = MathHelper.ceil((☃ * 0.5F - 3.0F) * ☃);
      if (☃ > 0) {
         this.attackEntityFrom(DamageSource.FALL, ☃);
         if (this.isBeingRidden()) {
            for (Entity ☃x : this.getRecursivePassengers()) {
               ☃x.attackEntityFrom(DamageSource.FALL, ☃);
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

   protected int getInventorySize() {
      return 2;
   }

   protected void initHorseChest() {
      ContainerHorseChest ☃ = this.horseChest;
      this.horseChest = new ContainerHorseChest("HorseChest", this.getInventorySize());
      this.horseChest.setCustomName(this.getName());
      if (☃ != null) {
         ☃.removeInventoryChangeListener(this);
         int ☃x = Math.min(☃.getSizeInventory(), this.horseChest.getSizeInventory());

         for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
            ItemStack ☃xxx = ☃.getStackInSlot(☃xx);
            if (!☃xxx.isEmpty()) {
               this.horseChest.setInventorySlotContents(☃xx, ☃xxx.copy());
            }
         }
      }

      this.horseChest.addInventoryChangeListener(this);
      this.updateHorseSlots();
   }

   protected void updateHorseSlots() {
      if (!this.world.isRemote) {
         this.setHorseSaddled(!this.horseChest.getStackInSlot(0).isEmpty() && this.canBeSaddled());
      }
   }

   @Override
   public void onInventoryChanged(IInventory var1) {
      boolean ☃ = this.isHorseSaddled();
      this.updateHorseSlots();
      if (this.ticksExisted > 20 && !☃ && this.isHorseSaddled()) {
         this.playSound(SoundEvents.ENTITY_HORSE_SADDLE, 0.5F, 1.0F);
      }
   }

   @Nullable
   protected AbstractHorse getClosestHorse(Entity var1, double var2) {
      double ☃ = Double.MAX_VALUE;
      Entity ☃x = null;

      for (Entity ☃xx : this.world.getEntitiesInAABBexcluding(☃, ☃.getEntityBoundingBox().expand(☃, ☃, ☃), IS_HORSE_BREEDING)) {
         double ☃xxx = ☃xx.getDistanceSq(☃.posX, ☃.posY, ☃.posZ);
         if (☃xxx < ☃) {
            ☃x = ☃xx;
            ☃ = ☃xxx;
         }
      }

      return (AbstractHorse)☃x;
   }

   public double getHorseJumpStrength() {
      return this.getEntityAttribute(JUMP_STRENGTH).getAttributeValue();
   }

   @Nullable
   @Override
   protected SoundEvent getDeathSound() {
      this.openHorseMouth();
      return null;
   }

   @Nullable
   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      this.openHorseMouth();
      if (this.rand.nextInt(3) == 0) {
         this.makeHorseRear();
      }

      return null;
   }

   @Nullable
   @Override
   protected SoundEvent getAmbientSound() {
      this.openHorseMouth();
      if (this.rand.nextInt(10) == 0 && !this.isMovementBlocked()) {
         this.makeHorseRear();
      }

      return null;
   }

   public boolean canBeSaddled() {
      return true;
   }

   public boolean isHorseSaddled() {
      return this.getHorseWatchableBoolean(4);
   }

   @Nullable
   protected SoundEvent getAngrySound() {
      this.openHorseMouth();
      this.makeHorseRear();
      return null;
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      if (!☃.getDefaultState().getMaterial().isLiquid()) {
         SoundType ☃ = ☃.getSoundType();
         if (this.world.getBlockState(☃.up()).getBlock() == Blocks.SNOW_LAYER) {
            ☃ = Blocks.SNOW_LAYER.getSoundType();
         }

         if (this.isBeingRidden() && this.canGallop) {
            this.gallopTime++;
            if (this.gallopTime > 5 && this.gallopTime % 3 == 0) {
               this.playGallopSound(☃);
            } else if (this.gallopTime <= 5) {
               this.playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, ☃.getVolume() * 0.15F, ☃.getPitch());
            }
         } else if (☃ == SoundType.WOOD) {
            this.playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, ☃.getVolume() * 0.15F, ☃.getPitch());
         } else {
            this.playSound(SoundEvents.ENTITY_HORSE_STEP, ☃.getVolume() * 0.15F, ☃.getPitch());
         }
      }
   }

   protected void playGallopSound(SoundType var1) {
      this.playSound(SoundEvents.ENTITY_HORSE_GALLOP, ☃.getVolume() * 0.15F, ☃.getPitch());
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getAttributeMap().registerAttribute(JUMP_STRENGTH);
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(53.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.225F);
   }

   @Override
   public int getMaxSpawnedInChunk() {
      return 6;
   }

   public int getMaxTemper() {
      return 100;
   }

   @Override
   protected float getSoundVolume() {
      return 0.8F;
   }

   @Override
   public int getTalkInterval() {
      return 400;
   }

   public void openGUI(EntityPlayer var1) {
      if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(☃)) && this.isTame()) {
         this.horseChest.setCustomName(this.getName());
         ☃.openGuiHorseInventory(this, this.horseChest);
      }
   }

   protected boolean handleEating(EntityPlayer var1, ItemStack var2) {
      boolean ☃ = false;
      float ☃x = 0.0F;
      int ☃xx = 0;
      int ☃xxx = 0;
      Item ☃xxxx = ☃.getItem();
      if (☃xxxx == Items.WHEAT) {
         ☃x = 2.0F;
         ☃xx = 20;
         ☃xxx = 3;
      } else if (☃xxxx == Items.SUGAR) {
         ☃x = 1.0F;
         ☃xx = 30;
         ☃xxx = 3;
      } else if (☃xxxx == Item.getItemFromBlock(Blocks.HAY_BLOCK)) {
         ☃x = 20.0F;
         ☃xx = 180;
      } else if (☃xxxx == Items.APPLE) {
         ☃x = 3.0F;
         ☃xx = 60;
         ☃xxx = 3;
      } else if (☃xxxx == Items.GOLDEN_CARROT) {
         ☃x = 4.0F;
         ☃xx = 60;
         ☃xxx = 5;
         if (this.isTame() && this.getGrowingAge() == 0 && !this.isInLove()) {
            ☃ = true;
            this.setInLove(☃);
         }
      } else if (☃xxxx == Items.GOLDEN_APPLE) {
         ☃x = 10.0F;
         ☃xx = 240;
         ☃xxx = 10;
         if (this.isTame() && this.getGrowingAge() == 0 && !this.isInLove()) {
            ☃ = true;
            this.setInLove(☃);
         }
      }

      if (this.getHealth() < this.getMaxHealth() && ☃x > 0.0F) {
         this.heal(☃x);
         ☃ = true;
      }

      if (this.isChild() && ☃xx > 0) {
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
            this.addGrowth(☃xx);
         }

         ☃ = true;
      }

      if (☃xxx > 0 && (☃ || !this.isTame()) && this.getTemper() < this.getMaxTemper()) {
         ☃ = true;
         if (!this.world.isRemote) {
            this.increaseTemper(☃xxx);
         }
      }

      if (☃) {
         this.eatingHorse();
      }

      return ☃;
   }

   protected void mountTo(EntityPlayer var1) {
      ☃.rotationYaw = this.rotationYaw;
      ☃.rotationPitch = this.rotationPitch;
      this.setEatingHaystack(false);
      this.setRearing(false);
      if (!this.world.isRemote) {
         ☃.startRiding(this);
      }
   }

   @Override
   protected boolean isMovementBlocked() {
      return super.isMovementBlocked() && this.isBeingRidden() && this.isHorseSaddled() || this.isEatingHaystack() || this.isRearing();
   }

   @Override
   public boolean isBreedingItem(ItemStack var1) {
      return false;
   }

   private void moveTail() {
      this.tailCounter = 1;
   }

   @Override
   public void onDeath(DamageSource var1) {
      super.onDeath(☃);
      if (!this.world.isRemote && this.horseChest != null) {
         for (int ☃ = 0; ☃ < this.horseChest.getSizeInventory(); ☃++) {
            ItemStack ☃x = this.horseChest.getStackInSlot(☃);
            if (!☃x.isEmpty()) {
               this.entityDropItem(☃x, 0.0F);
            }
         }
      }
   }

   @Override
   public void onLivingUpdate() {
      if (this.rand.nextInt(200) == 0) {
         this.moveTail();
      }

      super.onLivingUpdate();
      if (!this.world.isRemote) {
         if (this.rand.nextInt(900) == 0 && this.deathTime == 0) {
            this.heal(1.0F);
         }

         if (this.canEatGrass()) {
            if (!this.isEatingHaystack()
               && !this.isBeingRidden()
               && this.rand.nextInt(300) == 0
               && this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY) - 1, MathHelper.floor(this.posZ))).getBlock()
                  == Blocks.GRASS) {
               this.setEatingHaystack(true);
            }

            if (this.isEatingHaystack() && ++this.eatingCounter > 50) {
               this.eatingCounter = 0;
               this.setEatingHaystack(false);
            }
         }

         this.followMother();
      }
   }

   protected void followMother() {
      if (this.isBreeding() && this.isChild() && !this.isEatingHaystack()) {
         AbstractHorse ☃ = this.getClosestHorse(this, 16.0);
         if (☃ != null && this.getDistanceSq(☃) > 4.0) {
            this.navigator.getPathToEntityLiving(☃);
         }
      }
   }

   public boolean canEatGrass() {
      return true;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.openMouthCounter > 0 && ++this.openMouthCounter > 30) {
         this.openMouthCounter = 0;
         this.setHorseWatchableBoolean(64, false);
      }

      if (this.canPassengerSteer() && this.jumpRearingCounter > 0 && ++this.jumpRearingCounter > 20) {
         this.jumpRearingCounter = 0;
         this.setRearing(false);
      }

      if (this.tailCounter > 0 && ++this.tailCounter > 8) {
         this.tailCounter = 0;
      }

      if (this.sprintCounter > 0) {
         this.sprintCounter++;
         if (this.sprintCounter > 300) {
            this.sprintCounter = 0;
         }
      }

      this.prevHeadLean = this.headLean;
      if (this.isEatingHaystack()) {
         this.headLean = this.headLean + ((1.0F - this.headLean) * 0.4F + 0.05F);
         if (this.headLean > 1.0F) {
            this.headLean = 1.0F;
         }
      } else {
         this.headLean = this.headLean + ((0.0F - this.headLean) * 0.4F - 0.05F);
         if (this.headLean < 0.0F) {
            this.headLean = 0.0F;
         }
      }

      this.prevRearingAmount = this.rearingAmount;
      if (this.isRearing()) {
         this.headLean = 0.0F;
         this.prevHeadLean = this.headLean;
         this.rearingAmount = this.rearingAmount + ((1.0F - this.rearingAmount) * 0.4F + 0.05F);
         if (this.rearingAmount > 1.0F) {
            this.rearingAmount = 1.0F;
         }
      } else {
         this.allowStandSliding = false;
         this.rearingAmount = this.rearingAmount + ((0.8F * this.rearingAmount * this.rearingAmount * this.rearingAmount - this.rearingAmount) * 0.6F - 0.05F);
         if (this.rearingAmount < 0.0F) {
            this.rearingAmount = 0.0F;
         }
      }

      this.prevMouthOpenness = this.mouthOpenness;
      if (this.getHorseWatchableBoolean(64)) {
         this.mouthOpenness = this.mouthOpenness + ((1.0F - this.mouthOpenness) * 0.7F + 0.05F);
         if (this.mouthOpenness > 1.0F) {
            this.mouthOpenness = 1.0F;
         }
      } else {
         this.mouthOpenness = this.mouthOpenness + ((0.0F - this.mouthOpenness) * 0.7F - 0.05F);
         if (this.mouthOpenness < 0.0F) {
            this.mouthOpenness = 0.0F;
         }
      }
   }

   private void openHorseMouth() {
      if (!this.world.isRemote) {
         this.openMouthCounter = 1;
         this.setHorseWatchableBoolean(64, true);
      }
   }

   public void setEatingHaystack(boolean var1) {
      this.setHorseWatchableBoolean(16, ☃);
   }

   public void setRearing(boolean var1) {
      if (☃) {
         this.setEatingHaystack(false);
      }

      this.setHorseWatchableBoolean(32, ☃);
   }

   private void makeHorseRear() {
      if (this.canPassengerSteer()) {
         this.jumpRearingCounter = 1;
         this.setRearing(true);
      }
   }

   public void makeMad() {
      this.makeHorseRear();
      SoundEvent ☃ = this.getAngrySound();
      if (☃ != null) {
         this.playSound(☃, this.getSoundVolume(), this.getSoundPitch());
      }
   }

   public boolean setTamedBy(EntityPlayer var1) {
      this.setOwnerUniqueId(☃.getUniqueID());
      this.setHorseTamed(true);
      if (☃ instanceof EntityPlayerMP) {
         CriteriaTriggers.TAME_ANIMAL.trigger((EntityPlayerMP)☃, this);
      }

      this.world.setEntityState(this, (byte)7);
      return true;
   }

   @Override
   public void travel(float var1, float var2, float var3) {
      if (this.isBeingRidden() && this.canBeSteered() && this.isHorseSaddled()) {
         EntityLivingBase ☃ = (EntityLivingBase)this.getControllingPassenger();
         this.rotationYaw = ☃.rotationYaw;
         this.prevRotationYaw = this.rotationYaw;
         this.rotationPitch = ☃.rotationPitch * 0.5F;
         this.setRotation(this.rotationYaw, this.rotationPitch);
         this.renderYawOffset = this.rotationYaw;
         this.rotationYawHead = this.renderYawOffset;
         ☃ = ☃.moveStrafing * 0.5F;
         ☃ = ☃.moveForward;
         if (☃ <= 0.0F) {
            ☃ *= 0.25F;
            this.gallopTime = 0;
         }

         if (this.onGround && this.jumpPower == 0.0F && this.isRearing() && !this.allowStandSliding) {
            ☃ = 0.0F;
            ☃ = 0.0F;
         }

         if (this.jumpPower > 0.0F && !this.isHorseJumping() && this.onGround) {
            this.motionY = this.getHorseJumpStrength() * this.jumpPower;
            if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
               this.motionY = this.motionY + (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
            }

            this.setHorseJumping(true);
            this.isAirBorne = true;
            if (☃ > 0.0F) {
               float ☃x = MathHelper.sin(this.rotationYaw * (float) (Math.PI / 180.0));
               float ☃xx = MathHelper.cos(this.rotationYaw * (float) (Math.PI / 180.0));
               this.motionX = this.motionX + -0.4F * ☃x * this.jumpPower;
               this.motionZ = this.motionZ + 0.4F * ☃xx * this.jumpPower;
               this.playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
            }

            this.jumpPower = 0.0F;
         }

         this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
         if (this.canPassengerSteer()) {
            this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
            super.travel(☃, ☃, ☃);
         } else if (☃ instanceof EntityPlayer) {
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
         }

         if (this.onGround) {
            this.jumpPower = 0.0F;
            this.setHorseJumping(false);
         }

         this.prevLimbSwingAmount = this.limbSwingAmount;
         double ☃x = this.posX - this.prevPosX;
         double ☃xx = this.posZ - this.prevPosZ;
         float ☃xxx = MathHelper.sqrt(☃x * ☃x + ☃xx * ☃xx) * 4.0F;
         if (☃xxx > 1.0F) {
            ☃xxx = 1.0F;
         }

         this.limbSwingAmount = this.limbSwingAmount + (☃xxx - this.limbSwingAmount) * 0.4F;
         this.limbSwing = this.limbSwing + this.limbSwingAmount;
      } else {
         this.jumpMovementFactor = 0.02F;
         super.travel(☃, ☃, ☃);
      }
   }

   public static void registerFixesAbstractHorse(DataFixer var0, Class<?> var1) {
      EntityLiving.registerFixesMob(☃, ☃);
      ☃.registerWalker(FixTypes.ENTITY, new ItemStackData(☃, "SaddleItem"));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setBoolean("EatingHaystack", this.isEatingHaystack());
      ☃.setBoolean("Bred", this.isBreeding());
      ☃.setInteger("Temper", this.getTemper());
      ☃.setBoolean("Tame", this.isTame());
      if (this.getOwnerUniqueId() != null) {
         ☃.setString("OwnerUUID", this.getOwnerUniqueId().toString());
      }

      if (!this.horseChest.getStackInSlot(0).isEmpty()) {
         ☃.setTag("SaddleItem", this.horseChest.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setEatingHaystack(☃.getBoolean("EatingHaystack"));
      this.setBreeding(☃.getBoolean("Bred"));
      this.setTemper(☃.getInteger("Temper"));
      this.setHorseTamed(☃.getBoolean("Tame"));
      String ☃;
      if (☃.hasKey("OwnerUUID", 8)) {
         ☃ = ☃.getString("OwnerUUID");
      } else {
         String ☃x = ☃.getString("Owner");
         ☃ = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), ☃x);
      }

      if (!☃.isEmpty()) {
         this.setOwnerUniqueId(UUID.fromString(☃));
      }

      IAttributeInstance ☃x = this.getAttributeMap().getAttributeInstanceByName("Speed");
      if (☃x != null) {
         this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(☃x.getBaseValue() * 0.25);
      }

      if (☃.hasKey("SaddleItem", 10)) {
         ItemStack ☃xx = new ItemStack(☃.getCompoundTag("SaddleItem"));
         if (☃xx.getItem() == Items.SADDLE) {
            this.horseChest.setInventorySlotContents(0, ☃xx);
         }
      }

      this.updateHorseSlots();
   }

   @Override
   public boolean canMateWith(EntityAnimal var1) {
      return false;
   }

   protected boolean canMate() {
      return !this.isBeingRidden() && !this.isRiding() && this.isTame() && !this.isChild() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
   }

   @Nullable
   @Override
   public EntityAgeable createChild(EntityAgeable var1) {
      return null;
   }

   protected void setOffspringAttributes(EntityAgeable var1, AbstractHorse var2) {
      double ☃ = this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue()
         + ☃.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue()
         + this.getModifiedMaxHealth();
      ☃.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(☃ / 3.0);
      double ☃x = this.getEntityAttribute(JUMP_STRENGTH).getBaseValue() + ☃.getEntityAttribute(JUMP_STRENGTH).getBaseValue() + this.getModifiedJumpStrength();
      ☃.getEntityAttribute(JUMP_STRENGTH).setBaseValue(☃x / 3.0);
      double ☃xx = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue()
         + ☃.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue()
         + this.getModifiedMovementSpeed();
      ☃.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(☃xx / 3.0);
   }

   @Override
   public boolean canBeSteered() {
      return this.getControllingPassenger() instanceof EntityLivingBase;
   }

   public float getGrassEatingAmount(float var1) {
      return this.prevHeadLean + (this.headLean - this.prevHeadLean) * ☃;
   }

   public float getRearingAmount(float var1) {
      return this.prevRearingAmount + (this.rearingAmount - this.prevRearingAmount) * ☃;
   }

   public float getMouthOpennessAngle(float var1) {
      return this.prevMouthOpenness + (this.mouthOpenness - this.prevMouthOpenness) * ☃;
   }

   @Override
   public void setJumpPower(int var1) {
      if (this.isHorseSaddled()) {
         if (☃ < 0) {
            ☃ = 0;
         } else {
            this.allowStandSliding = true;
            this.makeHorseRear();
         }

         if (☃ >= 90) {
            this.jumpPower = 1.0F;
         } else {
            this.jumpPower = 0.4F + 0.4F * ☃ / 90.0F;
         }
      }
   }

   @Override
   public boolean canJump() {
      return this.isHorseSaddled();
   }

   @Override
   public void handleStartJump(int var1) {
      this.allowStandSliding = true;
      this.makeHorseRear();
   }

   @Override
   public void handleStopJump() {
   }

   protected void spawnHorseParticles(boolean var1) {
      EnumParticleTypes ☃ = ☃ ? EnumParticleTypes.HEART : EnumParticleTypes.SMOKE_NORMAL;

      for (int ☃x = 0; ☃x < 7; ☃x++) {
         double ☃xx = this.rand.nextGaussian() * 0.02;
         double ☃xxx = this.rand.nextGaussian() * 0.02;
         double ☃xxxx = this.rand.nextGaussian() * 0.02;
         this.world
            .spawnParticle(
               ☃,
               this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
               this.posY + 0.5 + this.rand.nextFloat() * this.height,
               this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
               ☃xx,
               ☃xxx,
               ☃xxxx
            );
      }
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 7) {
         this.spawnHorseParticles(true);
      } else if (☃ == 6) {
         this.spawnHorseParticles(false);
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   @Override
   public void updatePassenger(Entity var1) {
      super.updatePassenger(☃);
      if (☃ instanceof EntityLiving) {
         EntityLiving ☃ = (EntityLiving)☃;
         this.renderYawOffset = ☃.renderYawOffset;
      }

      if (this.prevRearingAmount > 0.0F) {
         float ☃ = MathHelper.sin(this.renderYawOffset * (float) (Math.PI / 180.0));
         float ☃x = MathHelper.cos(this.renderYawOffset * (float) (Math.PI / 180.0));
         float ☃xx = 0.7F * this.prevRearingAmount;
         float ☃xxx = 0.15F * this.prevRearingAmount;
         ☃.setPosition(this.posX + ☃xx * ☃, this.posY + this.getMountedYOffset() + ☃.getYOffset() + ☃xxx, this.posZ - ☃xx * ☃x);
         if (☃ instanceof EntityLivingBase) {
            ((EntityLivingBase)☃).renderYawOffset = this.renderYawOffset;
         }
      }
   }

   protected float getModifiedMaxHealth() {
      return 15.0F + this.rand.nextInt(8) + this.rand.nextInt(9);
   }

   protected double getModifiedJumpStrength() {
      return 0.4F + this.rand.nextDouble() * 0.2 + this.rand.nextDouble() * 0.2 + this.rand.nextDouble() * 0.2;
   }

   protected double getModifiedMovementSpeed() {
      return (0.45F + this.rand.nextDouble() * 0.3 + this.rand.nextDouble() * 0.3 + this.rand.nextDouble() * 0.3) * 0.25;
   }

   @Override
   public boolean isOnLadder() {
      return false;
   }

   @Override
   public float getEyeHeight() {
      return this.height;
   }

   public boolean wearsArmor() {
      return false;
   }

   public boolean isArmor(ItemStack var1) {
      return false;
   }

   @Override
   public boolean replaceItemInInventory(int var1, ItemStack var2) {
      int ☃ = ☃ - 400;
      if (☃ >= 0 && ☃ < 2 && ☃ < this.horseChest.getSizeInventory()) {
         if (☃ == 0 && ☃.getItem() != Items.SADDLE) {
            return false;
         } else if (☃ != 1 || this.wearsArmor() && this.isArmor(☃)) {
            this.horseChest.setInventorySlotContents(☃, ☃);
            this.updateHorseSlots();
            return true;
         } else {
            return false;
         }
      } else {
         int ☃x = ☃ - 500 + 2;
         if (☃x >= 2 && ☃x < this.horseChest.getSizeInventory()) {
            this.horseChest.setInventorySlotContents(☃x, ☃);
            return true;
         } else {
            return false;
         }
      }
   }

   @Nullable
   @Override
   public Entity getControllingPassenger() {
      return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      ☃ = super.onInitialSpawn(☃, ☃);
      if (this.rand.nextInt(5) == 0) {
         this.setGrowingAge(-24000);
      }

      return ☃;
   }
}
