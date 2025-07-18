package net.minecraft.entity.player;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityPlayer extends EntityLivingBase {
   private static final DataParameter<Float> ABSORPTION = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.FLOAT);
   private static final DataParameter<Integer> PLAYER_SCORE = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
   protected static final DataParameter<Byte> PLAYER_MODEL_FLAG = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BYTE);
   protected static final DataParameter<Byte> MAIN_HAND = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BYTE);
   protected static final DataParameter<NBTTagCompound> LEFT_SHOULDER_ENTITY = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.COMPOUND_TAG);
   protected static final DataParameter<NBTTagCompound> RIGHT_SHOULDER_ENTITY = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.COMPOUND_TAG);
   public InventoryPlayer inventory = new InventoryPlayer(this);
   protected InventoryEnderChest enderChest = new InventoryEnderChest();
   public Container inventoryContainer;
   public Container openContainer;
   protected FoodStats foodStats = new FoodStats();
   protected int flyToggleTimer;
   public float prevCameraYaw;
   public float cameraYaw;
   public int xpCooldown;
   public double prevChasingPosX;
   public double prevChasingPosY;
   public double prevChasingPosZ;
   public double chasingPosX;
   public double chasingPosY;
   public double chasingPosZ;
   protected boolean sleeping;
   public BlockPos bedLocation;
   private int sleepTimer;
   public float renderOffsetX;
   public float renderOffsetY;
   public float renderOffsetZ;
   private BlockPos spawnPos;
   private boolean spawnForced;
   public PlayerCapabilities capabilities = new PlayerCapabilities();
   public int experienceLevel;
   public int experienceTotal;
   public float experience;
   protected int xpSeed;
   protected float speedInAir = 0.02F;
   private int lastXPSound;
   private final GameProfile gameProfile;
   private boolean hasReducedDebug;
   private ItemStack itemStackMainHand = ItemStack.EMPTY;
   private final CooldownTracker cooldownTracker = this.createCooldownTracker();
   @Nullable
   public EntityFishHook fishEntity;

   protected CooldownTracker createCooldownTracker() {
      return new CooldownTracker();
   }

   public EntityPlayer(World var1, GameProfile var2) {
      super(☃);
      this.setUniqueId(getUUID(☃));
      this.gameProfile = ☃;
      this.inventoryContainer = new ContainerPlayer(this.inventory, !☃.isRemote, this);
      this.openContainer = this.inventoryContainer;
      BlockPos ☃ = ☃.getSpawnPoint();
      this.setLocationAndAngles(☃.getX() + 0.5, ☃.getY() + 1, ☃.getZ() + 0.5, 0.0F, 0.0F);
      this.unused180 = 180.0F;
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1F);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.LUCK);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(ABSORPTION, 0.0F);
      this.dataManager.register(PLAYER_SCORE, 0);
      this.dataManager.register(PLAYER_MODEL_FLAG, (byte)0);
      this.dataManager.register(MAIN_HAND, (byte)1);
      this.dataManager.register(LEFT_SHOULDER_ENTITY, new NBTTagCompound());
      this.dataManager.register(RIGHT_SHOULDER_ENTITY, new NBTTagCompound());
   }

   @Override
   public void onUpdate() {
      this.noClip = this.isSpectator();
      if (this.isSpectator()) {
         this.onGround = false;
      }

      if (this.xpCooldown > 0) {
         this.xpCooldown--;
      }

      if (this.isPlayerSleeping()) {
         this.sleepTimer++;
         if (this.sleepTimer > 100) {
            this.sleepTimer = 100;
         }

         if (!this.world.isRemote) {
            if (!this.isInBed()) {
               this.wakeUpPlayer(true, true, false);
            } else if (this.world.isDaytime()) {
               this.wakeUpPlayer(false, true, true);
            }
         }
      } else if (this.sleepTimer > 0) {
         this.sleepTimer++;
         if (this.sleepTimer >= 110) {
            this.sleepTimer = 0;
         }
      }

      super.onUpdate();
      if (!this.world.isRemote && this.openContainer != null && !this.openContainer.canInteractWith(this)) {
         this.closeScreen();
         this.openContainer = this.inventoryContainer;
      }

      if (this.isBurning() && this.capabilities.disableDamage) {
         this.extinguish();
      }

      this.updateCape();
      if (!this.world.isRemote) {
         this.foodStats.onUpdate(this);
         this.addStat(StatList.PLAY_ONE_MINUTE);
         if (this.isEntityAlive()) {
            this.addStat(StatList.TIME_SINCE_DEATH);
         }

         if (this.isSneaking()) {
            this.addStat(StatList.SNEAK_TIME);
         }
      }

      int ☃ = 29999999;
      double ☃x = MathHelper.clamp(this.posX, -2.9999999E7, 2.9999999E7);
      double ☃xx = MathHelper.clamp(this.posZ, -2.9999999E7, 2.9999999E7);
      if (☃x != this.posX || ☃xx != this.posZ) {
         this.setPosition(☃x, this.posY, ☃xx);
      }

      this.ticksSinceLastSwing++;
      ItemStack ☃xxx = this.getHeldItemMainhand();
      if (!ItemStack.areItemStacksEqual(this.itemStackMainHand, ☃xxx)) {
         if (!ItemStack.areItemsEqualIgnoreDurability(this.itemStackMainHand, ☃xxx)) {
            this.resetCooldown();
         }

         this.itemStackMainHand = ☃xxx.isEmpty() ? ItemStack.EMPTY : ☃xxx.copy();
      }

      this.cooldownTracker.tick();
      this.updateSize();
   }

   private void updateCape() {
      this.prevChasingPosX = this.chasingPosX;
      this.prevChasingPosY = this.chasingPosY;
      this.prevChasingPosZ = this.chasingPosZ;
      double ☃ = this.posX - this.chasingPosX;
      double ☃x = this.posY - this.chasingPosY;
      double ☃xx = this.posZ - this.chasingPosZ;
      double ☃xxx = 10.0;
      if (☃ > 10.0) {
         this.chasingPosX = this.posX;
         this.prevChasingPosX = this.chasingPosX;
      }

      if (☃xx > 10.0) {
         this.chasingPosZ = this.posZ;
         this.prevChasingPosZ = this.chasingPosZ;
      }

      if (☃x > 10.0) {
         this.chasingPosY = this.posY;
         this.prevChasingPosY = this.chasingPosY;
      }

      if (☃ < -10.0) {
         this.chasingPosX = this.posX;
         this.prevChasingPosX = this.chasingPosX;
      }

      if (☃xx < -10.0) {
         this.chasingPosZ = this.posZ;
         this.prevChasingPosZ = this.chasingPosZ;
      }

      if (☃x < -10.0) {
         this.chasingPosY = this.posY;
         this.prevChasingPosY = this.chasingPosY;
      }

      this.chasingPosX += ☃ * 0.25;
      this.chasingPosZ += ☃xx * 0.25;
      this.chasingPosY += ☃x * 0.25;
   }

   protected void updateSize() {
      float ☃;
      float ☃x;
      if (this.isElytraFlying()) {
         ☃ = 0.6F;
         ☃x = 0.6F;
      } else if (this.isPlayerSleeping()) {
         ☃ = 0.2F;
         ☃x = 0.2F;
      } else if (this.isSneaking()) {
         ☃ = 0.6F;
         ☃x = 1.65F;
      } else {
         ☃ = 0.6F;
         ☃x = 1.8F;
      }

      if (☃ != this.width || ☃x != this.height) {
         AxisAlignedBB ☃xx = this.getEntityBoundingBox();
         ☃xx = new AxisAlignedBB(☃xx.minX, ☃xx.minY, ☃xx.minZ, ☃xx.minX + ☃, ☃xx.minY + ☃x, ☃xx.minZ + ☃);
         if (!this.world.collidesWithAnyBlock(☃xx)) {
            this.setSize(☃, ☃x);
         }
      }
   }

   @Override
   public int getMaxInPortalTime() {
      return this.capabilities.disableDamage ? 1 : 80;
   }

   @Override
   protected SoundEvent getSwimSound() {
      return SoundEvents.ENTITY_PLAYER_SWIM;
   }

   @Override
   protected SoundEvent getSplashSound() {
      return SoundEvents.ENTITY_PLAYER_SPLASH;
   }

   @Override
   public int getPortalCooldown() {
      return 10;
   }

   @Override
   public void playSound(SoundEvent var1, float var2, float var3) {
      this.world.playSound(this, this.posX, this.posY, this.posZ, ☃, this.getSoundCategory(), ☃, ☃);
   }

   @Override
   public SoundCategory getSoundCategory() {
      return SoundCategory.PLAYERS;
   }

   @Override
   protected int getFireImmuneTicks() {
      return 20;
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 9) {
         this.onItemUseFinish();
      } else if (☃ == 23) {
         this.hasReducedDebug = false;
      } else if (☃ == 22) {
         this.hasReducedDebug = true;
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   @Override
   protected boolean isMovementBlocked() {
      return this.getHealth() <= 0.0F || this.isPlayerSleeping();
   }

   protected void closeScreen() {
      this.openContainer = this.inventoryContainer;
   }

   @Override
   public void updateRidden() {
      if (!this.world.isRemote && this.isSneaking() && this.isRiding()) {
         this.dismountRidingEntity();
         this.setSneaking(false);
      } else {
         double ☃ = this.posX;
         double ☃x = this.posY;
         double ☃xx = this.posZ;
         float ☃xxx = this.rotationYaw;
         float ☃xxxx = this.rotationPitch;
         super.updateRidden();
         this.prevCameraYaw = this.cameraYaw;
         this.cameraYaw = 0.0F;
         this.addMountedMovementStat(this.posX - ☃, this.posY - ☃x, this.posZ - ☃xx);
         if (this.getRidingEntity() instanceof EntityPig) {
            this.rotationPitch = ☃xxxx;
            this.rotationYaw = ☃xxx;
            this.renderYawOffset = ((EntityPig)this.getRidingEntity()).renderYawOffset;
         }
      }
   }

   @Override
   public void preparePlayerToSpawn() {
      this.setSize(0.6F, 1.8F);
      super.preparePlayerToSpawn();
      this.setHealth(this.getMaxHealth());
      this.deathTime = 0;
   }

   @Override
   protected void updateEntityActionState() {
      super.updateEntityActionState();
      this.updateArmSwingProgress();
      this.rotationYawHead = this.rotationYaw;
   }

   @Override
   public void onLivingUpdate() {
      if (this.flyToggleTimer > 0) {
         this.flyToggleTimer--;
      }

      if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.world.getGameRules().getBoolean("naturalRegeneration")) {
         if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 20 == 0) {
            this.heal(1.0F);
         }

         if (this.foodStats.needFood() && this.ticksExisted % 10 == 0) {
            this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
         }
      }

      this.inventory.decrementAnimations();
      this.prevCameraYaw = this.cameraYaw;
      super.onLivingUpdate();
      IAttributeInstance ☃ = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
      if (!this.world.isRemote) {
         ☃.setBaseValue(this.capabilities.getWalkSpeed());
      }

      this.jumpMovementFactor = this.speedInAir;
      if (this.isSprinting()) {
         this.jumpMovementFactor = (float)(this.jumpMovementFactor + this.speedInAir * 0.3);
      }

      this.setAIMoveSpeed((float)☃.getAttributeValue());
      float ☃x = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      float ☃xx = (float)(Math.atan(-this.motionY * 0.2F) * 15.0);
      if (☃x > 0.1F) {
         ☃x = 0.1F;
      }

      if (!this.onGround || this.getHealth() <= 0.0F) {
         ☃x = 0.0F;
      }

      if (this.onGround || this.getHealth() <= 0.0F) {
         ☃xx = 0.0F;
      }

      this.cameraYaw = this.cameraYaw + (☃x - this.cameraYaw) * 0.4F;
      this.cameraPitch = this.cameraPitch + (☃xx - this.cameraPitch) * 0.8F;
      if (this.getHealth() > 0.0F && !this.isSpectator()) {
         AxisAlignedBB ☃xxx;
         if (this.isRiding() && !this.getRidingEntity().isDead) {
            ☃xxx = this.getEntityBoundingBox().union(this.getRidingEntity().getEntityBoundingBox()).grow(1.0, 0.0, 1.0);
         } else {
            ☃xxx = this.getEntityBoundingBox().grow(1.0, 0.5, 1.0);
         }

         List<Entity> ☃xxxx = this.world.getEntitiesWithinAABBExcludingEntity(this, ☃xxx);

         for (int ☃xxxxx = 0; ☃xxxxx < ☃xxxx.size(); ☃xxxxx++) {
            Entity ☃xxxxxx = ☃xxxx.get(☃xxxxx);
            if (!☃xxxxxx.isDead) {
               this.collideWithPlayer(☃xxxxxx);
            }
         }
      }

      this.playShoulderEntityAmbientSound(this.getLeftShoulderEntity());
      this.playShoulderEntityAmbientSound(this.getRightShoulderEntity());
      if (!this.world.isRemote && (this.fallDistance > 0.5F || this.isInWater() || this.isRiding()) || this.capabilities.isFlying) {
         this.spawnShoulderEntities();
      }
   }

   private void playShoulderEntityAmbientSound(@Nullable NBTTagCompound var1) {
      if (☃ != null && !☃.hasKey("Silent") || !☃.getBoolean("Silent")) {
         String ☃ = ☃.getString("id");
         if (☃.equals(EntityList.getKey(EntityParrot.class).toString())) {
            EntityParrot.playAmbientSound(this.world, this);
         }
      }
   }

   private void collideWithPlayer(Entity var1) {
      ☃.onCollideWithPlayer(this);
   }

   public int getScore() {
      return this.dataManager.get(PLAYER_SCORE);
   }

   public void setScore(int var1) {
      this.dataManager.set(PLAYER_SCORE, ☃);
   }

   public void addScore(int var1) {
      int ☃ = this.getScore();
      this.dataManager.set(PLAYER_SCORE, ☃ + ☃);
   }

   @Override
   public void onDeath(DamageSource var1) {
      super.onDeath(☃);
      this.setSize(0.2F, 0.2F);
      this.setPosition(this.posX, this.posY, this.posZ);
      this.motionY = 0.1F;
      if ("Notch".equals(this.getName())) {
         this.dropItem(new ItemStack(Items.APPLE, 1), true, false);
      }

      if (!this.world.getGameRules().getBoolean("keepInventory") && !this.isSpectator()) {
         this.destroyVanishingCursedItems();
         this.inventory.dropAllItems();
      }

      if (☃ != null) {
         this.motionX = -MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * (float) (Math.PI / 180.0)) * 0.1F;
         this.motionZ = -MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * (float) (Math.PI / 180.0)) * 0.1F;
      } else {
         this.motionX = 0.0;
         this.motionZ = 0.0;
      }

      this.addStat(StatList.DEATHS);
      this.takeStat(StatList.TIME_SINCE_DEATH);
      this.extinguish();
      this.setFlag(0, false);
   }

   protected void destroyVanishingCursedItems() {
      for (int ☃ = 0; ☃ < this.inventory.getSizeInventory(); ☃++) {
         ItemStack ☃x = this.inventory.getStackInSlot(☃);
         if (!☃x.isEmpty() && EnchantmentHelper.hasVanishingCurse(☃x)) {
            this.inventory.removeStackFromSlot(☃);
         }
      }
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      if (☃ == DamageSource.ON_FIRE) {
         return SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE;
      } else {
         return ☃ == DamageSource.DROWN ? SoundEvents.ENTITY_PLAYER_HURT_DROWN : SoundEvents.ENTITY_PLAYER_HURT;
      }
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_PLAYER_DEATH;
   }

   @Nullable
   public EntityItem dropItem(boolean var1) {
      return this.dropItem(
         this.inventory
            .decrStackSize(this.inventory.currentItem, ☃ && !this.inventory.getCurrentItem().isEmpty() ? this.inventory.getCurrentItem().getCount() : 1),
         false,
         true
      );
   }

   @Nullable
   public EntityItem dropItem(ItemStack var1, boolean var2) {
      return this.dropItem(☃, false, ☃);
   }

   @Nullable
   public EntityItem dropItem(ItemStack var1, boolean var2, boolean var3) {
      if (☃.isEmpty()) {
         return null;
      } else {
         double ☃ = this.posY - 0.3F + this.getEyeHeight();
         EntityItem ☃x = new EntityItem(this.world, this.posX, ☃, this.posZ, ☃);
         ☃x.setPickupDelay(40);
         if (☃) {
            ☃x.setThrower(this.getName());
         }

         if (☃) {
            float ☃xx = this.rand.nextFloat() * 0.5F;
            float ☃xxx = this.rand.nextFloat() * (float) (Math.PI * 2);
            ☃x.motionX = -MathHelper.sin(☃xxx) * ☃xx;
            ☃x.motionZ = MathHelper.cos(☃xxx) * ☃xx;
            ☃x.motionY = 0.2F;
         } else {
            float ☃xx = 0.3F;
            ☃x.motionX = -MathHelper.sin(this.rotationYaw * (float) (Math.PI / 180.0)) * MathHelper.cos(this.rotationPitch * (float) (Math.PI / 180.0)) * ☃xx;
            ☃x.motionZ = MathHelper.cos(this.rotationYaw * (float) (Math.PI / 180.0)) * MathHelper.cos(this.rotationPitch * (float) (Math.PI / 180.0)) * ☃xx;
            ☃x.motionY = -MathHelper.sin(this.rotationPitch * (float) (Math.PI / 180.0)) * ☃xx + 0.1F;
            float ☃xxx = this.rand.nextFloat() * (float) (Math.PI * 2);
            ☃xx = 0.02F * this.rand.nextFloat();
            ☃x.motionX = ☃x.motionX + Math.cos(☃xxx) * ☃xx;
            ☃x.motionY = ☃x.motionY + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
            ☃x.motionZ = ☃x.motionZ + Math.sin(☃xxx) * ☃xx;
         }

         ItemStack ☃xx = this.dropItemAndGetStack(☃x);
         if (☃) {
            if (!☃xx.isEmpty()) {
               this.addStat(StatList.getDroppedObjectStats(☃xx.getItem()), ☃.getCount());
            }

            this.addStat(StatList.DROP);
         }

         return ☃x;
      }
   }

   protected ItemStack dropItemAndGetStack(EntityItem var1) {
      this.world.spawnEntity(☃);
      return ☃.getItem();
   }

   public float getDigSpeed(IBlockState var1) {
      float ☃ = this.inventory.getDestroySpeed(☃);
      if (☃ > 1.0F) {
         int ☃x = EnchantmentHelper.getEfficiencyModifier(this);
         ItemStack ☃xx = this.getHeldItemMainhand();
         if (☃x > 0 && !☃xx.isEmpty()) {
            ☃ += ☃x * ☃x + 1;
         }
      }

      if (this.isPotionActive(MobEffects.HASTE)) {
         ☃ *= 1.0F + (this.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2F;
      }

      if (this.isPotionActive(MobEffects.MINING_FATIGUE)) {
         float ☃x;
         switch (this.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
            case 0:
               ☃x = 0.3F;
               break;
            case 1:
               ☃x = 0.09F;
               break;
            case 2:
               ☃x = 0.0027F;
               break;
            case 3:
            default:
               ☃x = 8.1E-4F;
         }

         ☃ *= ☃x;
      }

      if (this.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
         ☃ /= 5.0F;
      }

      if (!this.onGround) {
         ☃ /= 5.0F;
      }

      return ☃;
   }

   public boolean canHarvestBlock(IBlockState var1) {
      return this.inventory.canHarvestBlock(☃);
   }

   public static void registerFixesPlayer(DataFixer var0) {
      ☃.registerWalker(FixTypes.PLAYER, new IDataWalker() {
         @Override
         public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3) {
            DataFixesManager.processInventory(☃, ☃, ☃, "Inventory");
            DataFixesManager.processInventory(☃, ☃, ☃, "EnderItems");
            if (☃.hasKey("ShoulderEntityLeft", 10)) {
               ☃.setTag("ShoulderEntityLeft", ☃.process(FixTypes.ENTITY, ☃.getCompoundTag("ShoulderEntityLeft"), ☃));
            }

            if (☃.hasKey("ShoulderEntityRight", 10)) {
               ☃.setTag("ShoulderEntityRight", ☃.process(FixTypes.ENTITY, ☃.getCompoundTag("ShoulderEntityRight"), ☃));
            }

            return ☃;
         }
      });
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setUniqueId(getUUID(this.gameProfile));
      NBTTagList ☃ = ☃.getTagList("Inventory", 10);
      this.inventory.readFromNBT(☃);
      this.inventory.currentItem = ☃.getInteger("SelectedItemSlot");
      this.sleeping = ☃.getBoolean("Sleeping");
      this.sleepTimer = ☃.getShort("SleepTimer");
      this.experience = ☃.getFloat("XpP");
      this.experienceLevel = ☃.getInteger("XpLevel");
      this.experienceTotal = ☃.getInteger("XpTotal");
      this.xpSeed = ☃.getInteger("XpSeed");
      if (this.xpSeed == 0) {
         this.xpSeed = this.rand.nextInt();
      }

      this.setScore(☃.getInteger("Score"));
      if (this.sleeping) {
         this.bedLocation = new BlockPos(this);
         this.wakeUpPlayer(true, true, false);
      }

      if (☃.hasKey("SpawnX", 99) && ☃.hasKey("SpawnY", 99) && ☃.hasKey("SpawnZ", 99)) {
         this.spawnPos = new BlockPos(☃.getInteger("SpawnX"), ☃.getInteger("SpawnY"), ☃.getInteger("SpawnZ"));
         this.spawnForced = ☃.getBoolean("SpawnForced");
      }

      this.foodStats.readNBT(☃);
      this.capabilities.readCapabilitiesFromNBT(☃);
      if (☃.hasKey("EnderItems", 9)) {
         NBTTagList ☃x = ☃.getTagList("EnderItems", 10);
         this.enderChest.loadInventoryFromNBT(☃x);
      }

      if (☃.hasKey("ShoulderEntityLeft", 10)) {
         this.setLeftShoulderEntity(☃.getCompoundTag("ShoulderEntityLeft"));
      }

      if (☃.hasKey("ShoulderEntityRight", 10)) {
         this.setRightShoulderEntity(☃.getCompoundTag("ShoulderEntityRight"));
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("DataVersion", 1343);
      ☃.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
      ☃.setInteger("SelectedItemSlot", this.inventory.currentItem);
      ☃.setBoolean("Sleeping", this.sleeping);
      ☃.setShort("SleepTimer", (short)this.sleepTimer);
      ☃.setFloat("XpP", this.experience);
      ☃.setInteger("XpLevel", this.experienceLevel);
      ☃.setInteger("XpTotal", this.experienceTotal);
      ☃.setInteger("XpSeed", this.xpSeed);
      ☃.setInteger("Score", this.getScore());
      if (this.spawnPos != null) {
         ☃.setInteger("SpawnX", this.spawnPos.getX());
         ☃.setInteger("SpawnY", this.spawnPos.getY());
         ☃.setInteger("SpawnZ", this.spawnPos.getZ());
         ☃.setBoolean("SpawnForced", this.spawnForced);
      }

      this.foodStats.writeNBT(☃);
      this.capabilities.writeCapabilitiesToNBT(☃);
      ☃.setTag("EnderItems", this.enderChest.saveInventoryToNBT());
      if (!this.getLeftShoulderEntity().isEmpty()) {
         ☃.setTag("ShoulderEntityLeft", this.getLeftShoulderEntity());
      }

      if (!this.getRightShoulderEntity().isEmpty()) {
         ☃.setTag("ShoulderEntityRight", this.getRightShoulderEntity());
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else if (this.capabilities.disableDamage && !☃.canHarmInCreative()) {
         return false;
      } else {
         this.idleTime = 0;
         if (this.getHealth() <= 0.0F) {
            return false;
         } else {
            if (this.isPlayerSleeping() && !this.world.isRemote) {
               this.wakeUpPlayer(true, true, false);
            }

            this.spawnShoulderEntities();
            if (☃.isDifficultyScaled()) {
               if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
                  ☃ = 0.0F;
               }

               if (this.world.getDifficulty() == EnumDifficulty.EASY) {
                  ☃ = Math.min(☃ / 2.0F + 1.0F, ☃);
               }

               if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                  ☃ = ☃ * 3.0F / 2.0F;
               }
            }

            return ☃ == 0.0F ? false : super.attackEntityFrom(☃, ☃);
         }
      }
   }

   @Override
   protected void blockUsingShield(EntityLivingBase var1) {
      super.blockUsingShield(☃);
      if (☃.getHeldItemMainhand().getItem() instanceof ItemAxe) {
         this.disableShield(true);
      }
   }

   public boolean canAttackPlayer(EntityPlayer var1) {
      Team ☃ = this.getTeam();
      Team ☃x = ☃.getTeam();
      if (☃ == null) {
         return true;
      } else {
         return !☃.isSameTeam(☃x) ? true : ☃.getAllowFriendlyFire();
      }
   }

   @Override
   protected void damageArmor(float var1) {
      this.inventory.damageArmor(☃);
   }

   @Override
   protected void damageShield(float var1) {
      if (☃ >= 3.0F && this.activeItemStack.getItem() == Items.SHIELD) {
         int ☃ = 1 + MathHelper.floor(☃);
         this.activeItemStack.damageItem(☃, this);
         if (this.activeItemStack.isEmpty()) {
            EnumHand ☃x = this.getActiveHand();
            if (☃x == EnumHand.MAIN_HAND) {
               this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
            } else {
               this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
            }

            this.activeItemStack = ItemStack.EMPTY;
            this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
         }
      }
   }

   public float getArmorVisibility() {
      int ☃ = 0;

      for (ItemStack ☃x : this.inventory.armorInventory) {
         if (!☃x.isEmpty()) {
            ☃++;
         }
      }

      return (float)☃ / this.inventory.armorInventory.size();
   }

   @Override
   protected void damageEntity(DamageSource var1, float var2) {
      if (!this.isEntityInvulnerable(☃)) {
         ☃ = this.applyArmorCalculations(☃, ☃);
         ☃ = this.applyPotionDamageCalculations(☃, ☃);
         float var7 = Math.max(☃ - this.getAbsorptionAmount(), 0.0F);
         this.setAbsorptionAmount(this.getAbsorptionAmount() - (☃ - var7));
         if (var7 != 0.0F) {
            this.addExhaustion(☃.getHungerDamage());
            float ☃ = this.getHealth();
            this.setHealth(this.getHealth() - var7);
            this.getCombatTracker().trackDamage(☃, ☃, var7);
            if (var7 < 3.4028235E37F) {
               this.addStat(StatList.DAMAGE_TAKEN, Math.round(var7 * 10.0F));
            }
         }
      }
   }

   public void openEditSign(TileEntitySign var1) {
   }

   public void displayGuiEditCommandCart(CommandBlockBaseLogic var1) {
   }

   public void displayGuiCommandBlock(TileEntityCommandBlock var1) {
   }

   public void openEditStructure(TileEntityStructure var1) {
   }

   public void displayVillagerTradeGui(IMerchant var1) {
   }

   public void displayGUIChest(IInventory var1) {
   }

   public void openGuiHorseInventory(AbstractHorse var1, IInventory var2) {
   }

   public void displayGui(IInteractionObject var1) {
   }

   public void openBook(ItemStack var1, EnumHand var2) {
   }

   public EnumActionResult interactOn(Entity var1, EnumHand var2) {
      if (this.isSpectator()) {
         if (☃ instanceof IInventory) {
            this.displayGUIChest((IInventory)☃);
         }

         return EnumActionResult.PASS;
      } else {
         ItemStack ☃ = this.getHeldItem(☃);
         ItemStack ☃x = ☃.isEmpty() ? ItemStack.EMPTY : ☃.copy();
         if (☃.processInitialInteract(this, ☃)) {
            if (this.capabilities.isCreativeMode && ☃ == this.getHeldItem(☃) && ☃.getCount() < ☃x.getCount()) {
               ☃.setCount(☃x.getCount());
            }

            return EnumActionResult.SUCCESS;
         } else {
            if (!☃.isEmpty() && ☃ instanceof EntityLivingBase) {
               if (this.capabilities.isCreativeMode) {
                  ☃ = ☃x;
               }

               if (☃.interactWithEntity(this, (EntityLivingBase)☃, ☃)) {
                  if (☃.isEmpty() && !this.capabilities.isCreativeMode) {
                     this.setHeldItem(☃, ItemStack.EMPTY);
                  }

                  return EnumActionResult.SUCCESS;
               }
            }

            return EnumActionResult.PASS;
         }
      }
   }

   @Override
   public double getYOffset() {
      return -0.35;
   }

   @Override
   public void dismountRidingEntity() {
      super.dismountRidingEntity();
      this.rideCooldown = 0;
   }

   public void attackTargetEntityWithCurrentItem(Entity var1) {
      if (☃.canBeAttackedWithItem()) {
         if (!☃.hitByEntity(this)) {
            float ☃ = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
            float ☃x;
            if (☃ instanceof EntityLivingBase) {
               ☃x = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)☃).getCreatureAttribute());
            } else {
               ☃x = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), EnumCreatureAttribute.UNDEFINED);
            }

            float ☃xx = this.getCooledAttackStrength(0.5F);
            ☃ *= 0.2F + ☃xx * ☃xx * 0.8F;
            ☃x *= ☃xx;
            this.resetCooldown();
            if (☃ > 0.0F || ☃x > 0.0F) {
               boolean ☃xxx = ☃xx > 0.9F;
               boolean ☃xxxx = false;
               int ☃xxxxx = 0;
               ☃xxxxx += EnchantmentHelper.getKnockbackModifier(this);
               if (this.isSprinting() && ☃xxx) {
                  this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, this.getSoundCategory(), 1.0F, 1.0F);
                  ☃xxxxx++;
                  ☃xxxx = true;
               }

               boolean ☃xxxxxx = ☃xxx
                  && this.fallDistance > 0.0F
                  && !this.onGround
                  && !this.isOnLadder()
                  && !this.isInWater()
                  && !this.isPotionActive(MobEffects.BLINDNESS)
                  && !this.isRiding()
                  && ☃ instanceof EntityLivingBase;
               ☃xxxxxx = ☃xxxxxx && !this.isSprinting();
               if (☃xxxxxx) {
                  ☃ *= 1.5F;
               }

               ☃ += ☃x;
               boolean ☃xxxxxxx = false;
               double ☃xxxxxxxx = this.distanceWalkedModified - this.prevDistanceWalkedModified;
               if (☃xxx && !☃xxxxxx && !☃xxxx && this.onGround && ☃xxxxxxxx < this.getAIMoveSpeed()) {
                  ItemStack ☃xxxxxxxxx = this.getHeldItem(EnumHand.MAIN_HAND);
                  if (☃xxxxxxxxx.getItem() instanceof ItemSword) {
                     ☃xxxxxxx = true;
                  }
               }

               float ☃xxxxxxxxx = 0.0F;
               boolean ☃xxxxxxxxxx = false;
               int ☃xxxxxxxxxxx = EnchantmentHelper.getFireAspectModifier(this);
               if (☃ instanceof EntityLivingBase) {
                  ☃xxxxxxxxx = ((EntityLivingBase)☃).getHealth();
                  if (☃xxxxxxxxxxx > 0 && !☃.isBurning()) {
                     ☃xxxxxxxxxx = true;
                     ☃.setFire(1);
                  }
               }

               double ☃xxxxxxxxxxxx = ☃.motionX;
               double ☃xxxxxxxxxxxxx = ☃.motionY;
               double ☃xxxxxxxxxxxxxx = ☃.motionZ;
               boolean ☃xxxxxxxxxxxxxxx = ☃.attackEntityFrom(DamageSource.causePlayerDamage(this), ☃);
               if (☃xxxxxxxxxxxxxxx) {
                  if (☃xxxxx > 0) {
                     if (☃ instanceof EntityLivingBase) {
                        ((EntityLivingBase)☃)
                           .knockBack(
                              this,
                              ☃xxxxx * 0.5F,
                              MathHelper.sin(this.rotationYaw * (float) (Math.PI / 180.0)),
                              -MathHelper.cos(this.rotationYaw * (float) (Math.PI / 180.0))
                           );
                     } else {
                        ☃.addVelocity(
                           -MathHelper.sin(this.rotationYaw * (float) (Math.PI / 180.0)) * ☃xxxxx * 0.5F,
                           0.1,
                           MathHelper.cos(this.rotationYaw * (float) (Math.PI / 180.0)) * ☃xxxxx * 0.5F
                        );
                     }

                     this.motionX *= 0.6;
                     this.motionZ *= 0.6;
                     this.setSprinting(false);
                  }

                  if (☃xxxxxxx) {
                     float ☃xxxxxxxxxxxxxxxx = 1.0F + EnchantmentHelper.getSweepingDamageRatio(this) * ☃;

                     for (EntityLivingBase ☃xxxxxxxxxxxxxxxxx : this.world
                        .getEntitiesWithinAABB(EntityLivingBase.class, ☃.getEntityBoundingBox().grow(1.0, 0.25, 1.0))) {
                        if (☃xxxxxxxxxxxxxxxxx != this
                           && ☃xxxxxxxxxxxxxxxxx != ☃
                           && !this.isOnSameTeam(☃xxxxxxxxxxxxxxxxx)
                           && this.getDistanceSq(☃xxxxxxxxxxxxxxxxx) < 9.0) {
                           ☃xxxxxxxxxxxxxxxxx.knockBack(
                              this,
                              0.4F,
                              MathHelper.sin(this.rotationYaw * (float) (Math.PI / 180.0)),
                              -MathHelper.cos(this.rotationYaw * (float) (Math.PI / 180.0))
                           );
                           ☃xxxxxxxxxxxxxxxxx.attackEntityFrom(DamageSource.causePlayerDamage(this), ☃xxxxxxxxxxxxxxxx);
                        }
                     }

                     this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
                     this.spawnSweepParticles();
                  }

                  if (☃ instanceof EntityPlayerMP && ☃.velocityChanged) {
                     ((EntityPlayerMP)☃).connection.sendPacket(new SPacketEntityVelocity(☃));
                     ☃.velocityChanged = false;
                     ☃.motionX = ☃xxxxxxxxxxxx;
                     ☃.motionY = ☃xxxxxxxxxxxxx;
                     ☃.motionZ = ☃xxxxxxxxxxxxxx;
                  }

                  if (☃xxxxxx) {
                     this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F, 1.0F);
                     this.onCriticalHit(☃);
                  }

                  if (!☃xxxxxx && !☃xxxxxxx) {
                     if (☃xxx) {
                        this.world
                           .playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0F, 1.0F);
                     } else {
                        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, this.getSoundCategory(), 1.0F, 1.0F);
                     }
                  }

                  if (☃x > 0.0F) {
                     this.onEnchantmentCritical(☃);
                  }

                  this.setLastAttackedEntity(☃);
                  if (☃ instanceof EntityLivingBase) {
                     EnchantmentHelper.applyThornEnchantments((EntityLivingBase)☃, this);
                  }

                  EnchantmentHelper.applyArthropodEnchantments(this, ☃);
                  ItemStack ☃xxxxxxxxxxxxxxxx = this.getHeldItemMainhand();
                  Entity ☃xxxxxxxxxxxxxxxxxx = ☃;
                  if (☃ instanceof MultiPartEntityPart) {
                     IEntityMultiPart ☃xxxxxxxxxxxxxxxxxxx = ((MultiPartEntityPart)☃).parent;
                     if (☃xxxxxxxxxxxxxxxxxxx instanceof EntityLivingBase) {
                        ☃xxxxxxxxxxxxxxxxxx = (EntityLivingBase)☃xxxxxxxxxxxxxxxxxxx;
                     }
                  }

                  if (!☃xxxxxxxxxxxxxxxx.isEmpty() && ☃xxxxxxxxxxxxxxxxxx instanceof EntityLivingBase) {
                     ☃xxxxxxxxxxxxxxxx.hitEntity((EntityLivingBase)☃xxxxxxxxxxxxxxxxxx, this);
                     if (☃xxxxxxxxxxxxxxxx.isEmpty()) {
                        this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                     }
                  }

                  if (☃ instanceof EntityLivingBase) {
                     float ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxx - ((EntityLivingBase)☃).getHealth();
                     this.addStat(StatList.DAMAGE_DEALT, Math.round(☃xxxxxxxxxxxxxxxxxxx * 10.0F));
                     if (☃xxxxxxxxxxx > 0) {
                        ☃.setFire(☃xxxxxxxxxxx * 4);
                     }

                     if (this.world instanceof WorldServer && ☃xxxxxxxxxxxxxxxxxxx > 2.0F) {
                        int ☃xxxxxxxxxxxxxxxxxxxx = (int)(☃xxxxxxxxxxxxxxxxxxx * 0.5);
                        ((WorldServer)this.world)
                           .spawnParticle(
                              EnumParticleTypes.DAMAGE_INDICATOR, ☃.posX, ☃.posY + ☃.height * 0.5F, ☃.posZ, ☃xxxxxxxxxxxxxxxxxxxx, 0.1, 0.0, 0.1, 0.2
                           );
                     }
                  }

                  this.addExhaustion(0.1F);
               } else {
                  this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0F, 1.0F);
                  if (☃xxxxxxxxxx) {
                     ☃.extinguish();
                  }
               }
            }
         }
      }
   }

   public void disableShield(boolean var1) {
      float ☃ = 0.25F + EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
      if (☃) {
         ☃ += 0.75F;
      }

      if (this.rand.nextFloat() < ☃) {
         this.getCooldownTracker().setCooldown(Items.SHIELD, 100);
         this.resetActiveHand();
         this.world.setEntityState(this, (byte)30);
      }
   }

   public void onCriticalHit(Entity var1) {
   }

   public void onEnchantmentCritical(Entity var1) {
   }

   public void spawnSweepParticles() {
      double ☃ = -MathHelper.sin(this.rotationYaw * (float) (Math.PI / 180.0));
      double ☃x = MathHelper.cos(this.rotationYaw * (float) (Math.PI / 180.0));
      if (this.world instanceof WorldServer) {
         ((WorldServer)this.world)
            .spawnParticle(EnumParticleTypes.SWEEP_ATTACK, this.posX + ☃, this.posY + this.height * 0.5, this.posZ + ☃x, 0, ☃, 0.0, ☃x, 0.0);
      }
   }

   public void respawnPlayer() {
   }

   @Override
   public void setDead() {
      super.setDead();
      this.inventoryContainer.onContainerClosed(this);
      if (this.openContainer != null) {
         this.openContainer.onContainerClosed(this);
      }
   }

   @Override
   public boolean isEntityInsideOpaqueBlock() {
      return !this.sleeping && super.isEntityInsideOpaqueBlock();
   }

   public boolean isUser() {
      return false;
   }

   public GameProfile getGameProfile() {
      return this.gameProfile;
   }

   public EntityPlayer.SleepResult trySleep(BlockPos var1) {
      EnumFacing ☃ = this.world.getBlockState(☃).getValue(BlockHorizontal.FACING);
      if (!this.world.isRemote) {
         if (this.isPlayerSleeping() || !this.isEntityAlive()) {
            return EntityPlayer.SleepResult.OTHER_PROBLEM;
         }

         if (!this.world.provider.isSurfaceWorld()) {
            return EntityPlayer.SleepResult.NOT_POSSIBLE_HERE;
         }

         if (this.world.isDaytime()) {
            return EntityPlayer.SleepResult.NOT_POSSIBLE_NOW;
         }

         if (!this.bedInRange(☃, ☃)) {
            return EntityPlayer.SleepResult.TOO_FAR_AWAY;
         }

         double ☃x = 8.0;
         double ☃xx = 5.0;
         List<EntityMob> ☃xxx = this.world
            .getEntitiesWithinAABB(
               EntityMob.class,
               new AxisAlignedBB(☃.getX() - 8.0, ☃.getY() - 5.0, ☃.getZ() - 8.0, ☃.getX() + 8.0, ☃.getY() + 5.0, ☃.getZ() + 8.0),
               new EntityPlayer.SleepEnemyPredicate(this)
            );
         if (!☃xxx.isEmpty()) {
            return EntityPlayer.SleepResult.NOT_SAFE;
         }
      }

      if (this.isRiding()) {
         this.dismountRidingEntity();
      }

      this.spawnShoulderEntities();
      this.setSize(0.2F, 0.2F);
      if (this.world.isBlockLoaded(☃)) {
         float ☃x = 0.5F + ☃.getXOffset() * 0.4F;
         float ☃xx = 0.5F + ☃.getZOffset() * 0.4F;
         this.setRenderOffsetForSleep(☃);
         this.setPosition(☃.getX() + ☃x, ☃.getY() + 0.6875F, ☃.getZ() + ☃xx);
      } else {
         this.setPosition(☃.getX() + 0.5F, ☃.getY() + 0.6875F, ☃.getZ() + 0.5F);
      }

      this.sleeping = true;
      this.sleepTimer = 0;
      this.bedLocation = ☃;
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      if (!this.world.isRemote) {
         this.world.updateAllPlayersSleepingFlag();
      }

      return EntityPlayer.SleepResult.OK;
   }

   private boolean bedInRange(BlockPos var1, EnumFacing var2) {
      if (Math.abs(this.posX - ☃.getX()) <= 3.0 && Math.abs(this.posY - ☃.getY()) <= 2.0 && Math.abs(this.posZ - ☃.getZ()) <= 3.0) {
         return true;
      } else {
         BlockPos ☃ = ☃.offset(☃.getOpposite());
         return Math.abs(this.posX - ☃.getX()) <= 3.0 && Math.abs(this.posY - ☃.getY()) <= 2.0 && Math.abs(this.posZ - ☃.getZ()) <= 3.0;
      }
   }

   private void setRenderOffsetForSleep(EnumFacing var1) {
      this.renderOffsetX = -1.8F * ☃.getXOffset();
      this.renderOffsetZ = -1.8F * ☃.getZOffset();
   }

   public void wakeUpPlayer(boolean var1, boolean var2, boolean var3) {
      this.setSize(0.6F, 1.8F);
      IBlockState ☃ = this.world.getBlockState(this.bedLocation);
      if (this.bedLocation != null && ☃.getBlock() == Blocks.BED) {
         this.world.setBlockState(this.bedLocation, ☃.withProperty(BlockBed.OCCUPIED, false), 4);
         BlockPos ☃x = BlockBed.getSafeExitLocation(this.world, this.bedLocation, 0);
         if (☃x == null) {
            ☃x = this.bedLocation.up();
         }

         this.setPosition(☃x.getX() + 0.5F, ☃x.getY() + 0.1F, ☃x.getZ() + 0.5F);
      }

      this.sleeping = false;
      if (!this.world.isRemote && ☃) {
         this.world.updateAllPlayersSleepingFlag();
      }

      this.sleepTimer = ☃ ? 0 : 100;
      if (☃) {
         this.setSpawnPoint(this.bedLocation, false);
      }
   }

   private boolean isInBed() {
      return this.world.getBlockState(this.bedLocation).getBlock() == Blocks.BED;
   }

   @Nullable
   public static BlockPos getBedSpawnLocation(World var0, BlockPos var1, boolean var2) {
      Block ☃ = ☃.getBlockState(☃).getBlock();
      if (☃ != Blocks.BED) {
         if (!☃) {
            return null;
         } else {
            boolean ☃x = ☃.canSpawnInBlock();
            boolean ☃xx = ☃.getBlockState(☃.up()).getBlock().canSpawnInBlock();
            return ☃x && ☃xx ? ☃ : null;
         }
      } else {
         return BlockBed.getSafeExitLocation(☃, ☃, 0);
      }
   }

   public float getBedOrientationInDegrees() {
      if (this.bedLocation != null) {
         EnumFacing ☃ = this.world.getBlockState(this.bedLocation).getValue(BlockHorizontal.FACING);
         switch (☃) {
            case SOUTH:
               return 90.0F;
            case WEST:
               return 0.0F;
            case NORTH:
               return 270.0F;
            case EAST:
               return 180.0F;
         }
      }

      return 0.0F;
   }

   @Override
   public boolean isPlayerSleeping() {
      return this.sleeping;
   }

   public boolean isPlayerFullyAsleep() {
      return this.sleeping && this.sleepTimer >= 100;
   }

   public int getSleepTimer() {
      return this.sleepTimer;
   }

   public void sendStatusMessage(ITextComponent var1, boolean var2) {
   }

   public BlockPos getBedLocation() {
      return this.spawnPos;
   }

   public boolean isSpawnForced() {
      return this.spawnForced;
   }

   public void setSpawnPoint(BlockPos var1, boolean var2) {
      if (☃ != null) {
         this.spawnPos = ☃;
         this.spawnForced = ☃;
      } else {
         this.spawnPos = null;
         this.spawnForced = false;
      }
   }

   public void addStat(StatBase var1) {
      this.addStat(☃, 1);
   }

   public void addStat(StatBase var1, int var2) {
   }

   public void takeStat(StatBase var1) {
   }

   public void unlockRecipes(List<IRecipe> var1) {
   }

   public void unlockRecipes(ResourceLocation[] var1) {
   }

   public void resetRecipes(List<IRecipe> var1) {
   }

   @Override
   public void jump() {
      super.jump();
      this.addStat(StatList.JUMP);
      if (this.isSprinting()) {
         this.addExhaustion(0.2F);
      } else {
         this.addExhaustion(0.05F);
      }
   }

   @Override
   public void travel(float var1, float var2, float var3) {
      double ☃ = this.posX;
      double ☃x = this.posY;
      double ☃xx = this.posZ;
      if (this.capabilities.isFlying && !this.isRiding()) {
         double ☃xxx = this.motionY;
         float ☃xxxx = this.jumpMovementFactor;
         this.jumpMovementFactor = this.capabilities.getFlySpeed() * (this.isSprinting() ? 2 : 1);
         super.travel(☃, ☃, ☃);
         this.motionY = ☃xxx * 0.6;
         this.jumpMovementFactor = ☃xxxx;
         this.fallDistance = 0.0F;
         this.setFlag(7, false);
      } else {
         super.travel(☃, ☃, ☃);
      }

      this.addMovementStat(this.posX - ☃, this.posY - ☃x, this.posZ - ☃xx);
   }

   @Override
   public float getAIMoveSpeed() {
      return (float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
   }

   public void addMovementStat(double var1, double var3, double var5) {
      if (!this.isRiding()) {
         if (this.isInsideOfMaterial(Material.WATER)) {
            int ☃ = Math.round(MathHelper.sqrt(☃ * ☃ + ☃ * ☃ + ☃ * ☃) * 100.0F);
            if (☃ > 0) {
               this.addStat(StatList.DIVE_ONE_CM, ☃);
               this.addExhaustion(0.01F * ☃ * 0.01F);
            }
         } else if (this.isInWater()) {
            int ☃ = Math.round(MathHelper.sqrt(☃ * ☃ + ☃ * ☃) * 100.0F);
            if (☃ > 0) {
               this.addStat(StatList.SWIM_ONE_CM, ☃);
               this.addExhaustion(0.01F * ☃ * 0.01F);
            }
         } else if (this.isOnLadder()) {
            if (☃ > 0.0) {
               this.addStat(StatList.CLIMB_ONE_CM, (int)Math.round(☃ * 100.0));
            }
         } else if (this.onGround) {
            int ☃ = Math.round(MathHelper.sqrt(☃ * ☃ + ☃ * ☃) * 100.0F);
            if (☃ > 0) {
               if (this.isSprinting()) {
                  this.addStat(StatList.SPRINT_ONE_CM, ☃);
                  this.addExhaustion(0.1F * ☃ * 0.01F);
               } else if (this.isSneaking()) {
                  this.addStat(StatList.CROUCH_ONE_CM, ☃);
                  this.addExhaustion(0.0F * ☃ * 0.01F);
               } else {
                  this.addStat(StatList.WALK_ONE_CM, ☃);
                  this.addExhaustion(0.0F * ☃ * 0.01F);
               }
            }
         } else if (this.isElytraFlying()) {
            int ☃ = Math.round(MathHelper.sqrt(☃ * ☃ + ☃ * ☃ + ☃ * ☃) * 100.0F);
            this.addStat(StatList.AVIATE_ONE_CM, ☃);
         } else {
            int ☃ = Math.round(MathHelper.sqrt(☃ * ☃ + ☃ * ☃) * 100.0F);
            if (☃ > 25) {
               this.addStat(StatList.FLY_ONE_CM, ☃);
            }
         }
      }
   }

   private void addMountedMovementStat(double var1, double var3, double var5) {
      if (this.isRiding()) {
         int ☃ = Math.round(MathHelper.sqrt(☃ * ☃ + ☃ * ☃ + ☃ * ☃) * 100.0F);
         if (☃ > 0) {
            if (this.getRidingEntity() instanceof EntityMinecart) {
               this.addStat(StatList.MINECART_ONE_CM, ☃);
            } else if (this.getRidingEntity() instanceof EntityBoat) {
               this.addStat(StatList.BOAT_ONE_CM, ☃);
            } else if (this.getRidingEntity() instanceof EntityPig) {
               this.addStat(StatList.PIG_ONE_CM, ☃);
            } else if (this.getRidingEntity() instanceof AbstractHorse) {
               this.addStat(StatList.HORSE_ONE_CM, ☃);
            }
         }
      }
   }

   @Override
   public void fall(float var1, float var2) {
      if (!this.capabilities.allowFlying) {
         if (☃ >= 2.0F) {
            this.addStat(StatList.FALL_ONE_CM, (int)Math.round(☃ * 100.0));
         }

         super.fall(☃, ☃);
      }
   }

   @Override
   protected void doWaterSplashEffect() {
      if (!this.isSpectator()) {
         super.doWaterSplashEffect();
      }
   }

   @Override
   protected SoundEvent getFallSound(int var1) {
      return ☃ > 4 ? SoundEvents.ENTITY_PLAYER_BIG_FALL : SoundEvents.ENTITY_PLAYER_SMALL_FALL;
   }

   @Override
   public void onKillEntity(EntityLivingBase var1) {
      EntityList.EntityEggInfo ☃ = EntityList.ENTITY_EGGS.get(EntityList.getKey(☃));
      if (☃ != null) {
         this.addStat(☃.killEntityStat);
      }
   }

   @Override
   public void setInWeb() {
      if (!this.capabilities.isFlying) {
         super.setInWeb();
      }
   }

   public void addExperience(int var1) {
      this.addScore(☃);
      int ☃ = Integer.MAX_VALUE - this.experienceTotal;
      if (☃ > ☃) {
         ☃ = ☃;
      }

      this.experience = this.experience + (float)☃ / this.xpBarCap();

      for (this.experienceTotal += ☃; this.experience >= 1.0F; this.experience = this.experience / this.xpBarCap()) {
         this.experience = (this.experience - 1.0F) * this.xpBarCap();
         this.addExperienceLevel(1);
      }
   }

   public int getXPSeed() {
      return this.xpSeed;
   }

   public void onEnchant(ItemStack var1, int var2) {
      this.experienceLevel -= ☃;
      if (this.experienceLevel < 0) {
         this.experienceLevel = 0;
         this.experience = 0.0F;
         this.experienceTotal = 0;
      }

      this.xpSeed = this.rand.nextInt();
   }

   public void addExperienceLevel(int var1) {
      this.experienceLevel += ☃;
      if (this.experienceLevel < 0) {
         this.experienceLevel = 0;
         this.experience = 0.0F;
         this.experienceTotal = 0;
      }

      if (☃ > 0 && this.experienceLevel % 5 == 0 && this.lastXPSound < this.ticksExisted - 100.0F) {
         float ☃ = this.experienceLevel > 30 ? 1.0F : this.experienceLevel / 30.0F;
         this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, this.getSoundCategory(), ☃ * 0.75F, 1.0F);
         this.lastXPSound = this.ticksExisted;
      }
   }

   public int xpBarCap() {
      if (this.experienceLevel >= 30) {
         return 112 + (this.experienceLevel - 30) * 9;
      } else {
         return this.experienceLevel >= 15 ? 37 + (this.experienceLevel - 15) * 5 : 7 + this.experienceLevel * 2;
      }
   }

   public void addExhaustion(float var1) {
      if (!this.capabilities.disableDamage) {
         if (!this.world.isRemote) {
            this.foodStats.addExhaustion(☃);
         }
      }
   }

   public FoodStats getFoodStats() {
      return this.foodStats;
   }

   public boolean canEat(boolean var1) {
      return (☃ || this.foodStats.needFood()) && !this.capabilities.disableDamage;
   }

   public boolean shouldHeal() {
      return this.getHealth() > 0.0F && this.getHealth() < this.getMaxHealth();
   }

   public boolean isAllowEdit() {
      return this.capabilities.allowEdit;
   }

   public boolean canPlayerEdit(BlockPos var1, EnumFacing var2, ItemStack var3) {
      if (this.capabilities.allowEdit) {
         return true;
      } else if (☃.isEmpty()) {
         return false;
      } else {
         BlockPos ☃ = ☃.offset(☃.getOpposite());
         Block ☃x = this.world.getBlockState(☃).getBlock();
         return ☃.canPlaceOn(☃x) || ☃.canEditBlocks();
      }
   }

   @Override
   protected int getExperiencePoints(EntityPlayer var1) {
      if (!this.world.getGameRules().getBoolean("keepInventory") && !this.isSpectator()) {
         int ☃ = this.experienceLevel * 7;
         return ☃ > 100 ? 100 : ☃;
      } else {
         return 0;
      }
   }

   @Override
   protected boolean isPlayer() {
      return true;
   }

   @Override
   public boolean getAlwaysRenderNameTagForRender() {
      return true;
   }

   @Override
   protected boolean canTriggerWalking() {
      return !this.capabilities.isFlying;
   }

   public void sendPlayerAbilities() {
   }

   public void setGameType(GameType var1) {
   }

   @Override
   public String getName() {
      return this.gameProfile.getName();
   }

   public InventoryEnderChest getInventoryEnderChest() {
      return this.enderChest;
   }

   @Override
   public ItemStack getItemStackFromSlot(EntityEquipmentSlot var1) {
      if (☃ == EntityEquipmentSlot.MAINHAND) {
         return this.inventory.getCurrentItem();
      } else if (☃ == EntityEquipmentSlot.OFFHAND) {
         return this.inventory.offHandInventory.get(0);
      } else {
         return ☃.getSlotType() == EntityEquipmentSlot.Type.ARMOR ? this.inventory.armorInventory.get(☃.getIndex()) : ItemStack.EMPTY;
      }
   }

   @Override
   public void setItemStackToSlot(EntityEquipmentSlot var1, ItemStack var2) {
      if (☃ == EntityEquipmentSlot.MAINHAND) {
         this.playEquipSound(☃);
         this.inventory.mainInventory.set(this.inventory.currentItem, ☃);
      } else if (☃ == EntityEquipmentSlot.OFFHAND) {
         this.playEquipSound(☃);
         this.inventory.offHandInventory.set(0, ☃);
      } else if (☃.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
         this.playEquipSound(☃);
         this.inventory.armorInventory.set(☃.getIndex(), ☃);
      }
   }

   public boolean addItemStackToInventory(ItemStack var1) {
      this.playEquipSound(☃);
      return this.inventory.addItemStackToInventory(☃);
   }

   @Override
   public Iterable<ItemStack> getHeldEquipment() {
      return Lists.newArrayList(new ItemStack[]{this.getHeldItemMainhand(), this.getHeldItemOffhand()});
   }

   @Override
   public Iterable<ItemStack> getArmorInventoryList() {
      return this.inventory.armorInventory;
   }

   public boolean addShoulderEntity(NBTTagCompound var1) {
      if (this.isRiding() || !this.onGround || this.isInWater()) {
         return false;
      } else if (this.getLeftShoulderEntity().isEmpty()) {
         this.setLeftShoulderEntity(☃);
         return true;
      } else if (this.getRightShoulderEntity().isEmpty()) {
         this.setRightShoulderEntity(☃);
         return true;
      } else {
         return false;
      }
   }

   protected void spawnShoulderEntities() {
      this.spawnShoulderEntity(this.getLeftShoulderEntity());
      this.setLeftShoulderEntity(new NBTTagCompound());
      this.spawnShoulderEntity(this.getRightShoulderEntity());
      this.setRightShoulderEntity(new NBTTagCompound());
   }

   private void spawnShoulderEntity(@Nullable NBTTagCompound var1) {
      if (!this.world.isRemote && !☃.isEmpty()) {
         Entity ☃ = EntityList.createEntityFromNBT(☃, this.world);
         if (☃ instanceof EntityTameable) {
            ((EntityTameable)☃).setOwnerId(this.entityUniqueID);
         }

         ☃.setPosition(this.posX, this.posY + 0.7F, this.posZ);
         this.world.spawnEntity(☃);
      }
   }

   @Override
   public boolean isInvisibleToPlayer(EntityPlayer var1) {
      if (!this.isInvisible()) {
         return false;
      } else if (☃.isSpectator()) {
         return false;
      } else {
         Team ☃ = this.getTeam();
         return ☃ == null || ☃ == null || ☃.getTeam() != ☃ || !☃.getSeeFriendlyInvisiblesEnabled();
      }
   }

   public abstract boolean isSpectator();

   public abstract boolean isCreative();

   @Override
   public boolean isPushedByWater() {
      return !this.capabilities.isFlying;
   }

   public Scoreboard getWorldScoreboard() {
      return this.world.getScoreboard();
   }

   @Override
   public Team getTeam() {
      return this.getWorldScoreboard().getPlayersTeam(this.getName());
   }

   @Override
   public ITextComponent getDisplayName() {
      ITextComponent ☃ = new TextComponentString(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));
      ☃.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + this.getName() + " "));
      ☃.getStyle().setHoverEvent(this.getHoverEvent());
      ☃.getStyle().setInsertion(this.getName());
      return ☃;
   }

   @Override
   public float getEyeHeight() {
      float ☃ = 1.62F;
      if (this.isPlayerSleeping()) {
         ☃ = 0.2F;
      } else if (this.isSneaking() || this.height == 1.65F) {
         ☃ -= 0.08F;
      } else if (this.isElytraFlying() || this.height == 0.6F) {
         ☃ = 0.4F;
      }

      return ☃;
   }

   @Override
   public void setAbsorptionAmount(float var1) {
      if (☃ < 0.0F) {
         ☃ = 0.0F;
      }

      this.getDataManager().set(ABSORPTION, ☃);
   }

   @Override
   public float getAbsorptionAmount() {
      return this.getDataManager().get(ABSORPTION);
   }

   public static UUID getUUID(GameProfile var0) {
      UUID ☃ = ☃.getId();
      if (☃ == null) {
         ☃ = getOfflineUUID(☃.getName());
      }

      return ☃;
   }

   public static UUID getOfflineUUID(String var0) {
      return UUID.nameUUIDFromBytes(("OfflinePlayer:" + ☃).getBytes(StandardCharsets.UTF_8));
   }

   public boolean canOpen(LockCode var1) {
      if (☃.isEmpty()) {
         return true;
      } else {
         ItemStack ☃ = this.getHeldItemMainhand();
         return !☃.isEmpty() && ☃.hasDisplayName() ? ☃.getDisplayName().equals(☃.getLock()) : false;
      }
   }

   public boolean isWearing(EnumPlayerModelParts var1) {
      return (this.getDataManager().get(PLAYER_MODEL_FLAG) & ☃.getPartMask()) == ☃.getPartMask();
   }

   @Override
   public boolean sendCommandFeedback() {
      return this.getServer().worlds[0].getGameRules().getBoolean("sendCommandFeedback");
   }

   @Override
   public boolean replaceItemInInventory(int var1, ItemStack var2) {
      if (☃ >= 0 && ☃ < this.inventory.mainInventory.size()) {
         this.inventory.setInventorySlotContents(☃, ☃);
         return true;
      } else {
         EntityEquipmentSlot ☃;
         if (☃ == 100 + EntityEquipmentSlot.HEAD.getIndex()) {
            ☃ = EntityEquipmentSlot.HEAD;
         } else if (☃ == 100 + EntityEquipmentSlot.CHEST.getIndex()) {
            ☃ = EntityEquipmentSlot.CHEST;
         } else if (☃ == 100 + EntityEquipmentSlot.LEGS.getIndex()) {
            ☃ = EntityEquipmentSlot.LEGS;
         } else if (☃ == 100 + EntityEquipmentSlot.FEET.getIndex()) {
            ☃ = EntityEquipmentSlot.FEET;
         } else {
            ☃ = null;
         }

         if (☃ == 98) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ☃);
            return true;
         } else if (☃ == 99) {
            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ☃);
            return true;
         } else if (☃ == null) {
            int ☃x = ☃ - 200;
            if (☃x >= 0 && ☃x < this.enderChest.getSizeInventory()) {
               this.enderChest.setInventorySlotContents(☃x, ☃);
               return true;
            } else {
               return false;
            }
         } else {
            if (!☃.isEmpty()) {
               if (!(☃.getItem() instanceof ItemArmor) && !(☃.getItem() instanceof ItemElytra)) {
                  if (☃ != EntityEquipmentSlot.HEAD) {
                     return false;
                  }
               } else if (EntityLiving.getSlotForItemStack(☃) != ☃) {
                  return false;
               }
            }

            this.inventory.setInventorySlotContents(☃.getIndex() + this.inventory.mainInventory.size(), ☃);
            return true;
         }
      }
   }

   public boolean hasReducedDebug() {
      return this.hasReducedDebug;
   }

   public void setReducedDebug(boolean var1) {
      this.hasReducedDebug = ☃;
   }

   @Override
   public EnumHandSide getPrimaryHand() {
      return this.dataManager.get(MAIN_HAND) == 0 ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
   }

   public void setPrimaryHand(EnumHandSide var1) {
      this.dataManager.set(MAIN_HAND, (byte)(☃ == EnumHandSide.LEFT ? 0 : 1));
   }

   public NBTTagCompound getLeftShoulderEntity() {
      return this.dataManager.get(LEFT_SHOULDER_ENTITY);
   }

   protected void setLeftShoulderEntity(NBTTagCompound var1) {
      this.dataManager.set(LEFT_SHOULDER_ENTITY, ☃);
   }

   public NBTTagCompound getRightShoulderEntity() {
      return this.dataManager.get(RIGHT_SHOULDER_ENTITY);
   }

   protected void setRightShoulderEntity(NBTTagCompound var1) {
      this.dataManager.set(RIGHT_SHOULDER_ENTITY, ☃);
   }

   public float getCooldownPeriod() {
      return (float)(1.0 / this.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue() * 20.0);
   }

   public float getCooledAttackStrength(float var1) {
      return MathHelper.clamp((this.ticksSinceLastSwing + ☃) / this.getCooldownPeriod(), 0.0F, 1.0F);
   }

   public void resetCooldown() {
      this.ticksSinceLastSwing = 0;
   }

   public CooldownTracker getCooldownTracker() {
      return this.cooldownTracker;
   }

   @Override
   public void applyEntityCollision(Entity var1) {
      if (!this.isPlayerSleeping()) {
         super.applyEntityCollision(☃);
      }
   }

   public float getLuck() {
      return (float)this.getEntityAttribute(SharedMonsterAttributes.LUCK).getAttributeValue();
   }

   public boolean canUseCommandBlock() {
      return this.capabilities.isCreativeMode && this.canUseCommand(2, "");
   }

   public static enum EnumChatVisibility {
      FULL(0, "options.chat.visibility.full"),
      SYSTEM(1, "options.chat.visibility.system"),
      HIDDEN(2, "options.chat.visibility.hidden");

      private static final EntityPlayer.EnumChatVisibility[] ID_LOOKUP = new EntityPlayer.EnumChatVisibility[values().length];
      private final int chatVisibility;
      private final String resourceKey;

      private EnumChatVisibility(int var3, String var4) {
         this.chatVisibility = ☃;
         this.resourceKey = ☃;
      }

      public int getChatVisibility() {
         return this.chatVisibility;
      }

      public static EntityPlayer.EnumChatVisibility getEnumChatVisibility(int var0) {
         return ID_LOOKUP[☃ % ID_LOOKUP.length];
      }

      public String getResourceKey() {
         return this.resourceKey;
      }

      static {
         for (EntityPlayer.EnumChatVisibility ☃ : values()) {
            ID_LOOKUP[☃.chatVisibility] = ☃;
         }
      }
   }

   static class SleepEnemyPredicate implements Predicate<EntityMob> {
      private final EntityPlayer player;

      private SleepEnemyPredicate(EntityPlayer var1) {
         this.player = ☃;
      }

      public boolean apply(@Nullable EntityMob var1) {
         return ☃.isPreventingPlayerRest(this.player);
      }
   }

   public static enum SleepResult {
      OK,
      NOT_POSSIBLE_HERE,
      NOT_POSSIBLE_NOW,
      TOO_FAR_AWAY,
      OTHER_PROBLEM,
      NOT_SAFE;
   }
}
