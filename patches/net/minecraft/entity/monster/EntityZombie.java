package net.minecraft.entity.monster;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityZombie extends EntityMob {
   protected static final IAttribute SPAWN_REINFORCEMENTS_CHANCE = new RangedAttribute(null, "zombie.spawnReinforcements", 0.0, 0.0, 1.0)
      .setDescription("Spawn Reinforcements Chance");
   private static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
   private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.5, 1);
   private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(EntityZombie.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Integer> VILLAGER_TYPE = EntityDataManager.createKey(EntityZombie.class, DataSerializers.VARINT);
   private static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.createKey(EntityZombie.class, DataSerializers.BOOLEAN);
   private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor(this);
   private boolean isBreakDoorsTaskSet;
   private float zombieWidth = -1.0F;
   private float zombieHeight;

   public EntityZombie(World var1) {
      super(☃);
      this.setSize(0.6F, 1.95F);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0, false));
      this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
      this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.applyEntityAI();
   }

   @Override
   protected void applyEntityAI() {
      this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0, false));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityPigZombie.class));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityVillager.class, false));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityIronGolem.class, true));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23F);
      this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0);
      this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0);
      this.getAttributeMap().registerAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.rand.nextDouble() * 0.1F);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.getDataManager().register(IS_CHILD, false);
      this.getDataManager().register(VILLAGER_TYPE, 0);
      this.getDataManager().register(ARMS_RAISED, false);
   }

   public void setArmsRaised(boolean var1) {
      this.getDataManager().set(ARMS_RAISED, ☃);
   }

   public boolean isArmsRaised() {
      return this.getDataManager().get(ARMS_RAISED);
   }

   public boolean isBreakDoorsTaskSet() {
      return this.isBreakDoorsTaskSet;
   }

   public void setBreakDoorsAItask(boolean var1) {
      if (this.isBreakDoorsTaskSet != ☃) {
         this.isBreakDoorsTaskSet = ☃;
         ((PathNavigateGround)this.getNavigator()).setBreakDoors(☃);
         if (☃) {
            this.tasks.addTask(1, this.breakDoor);
         } else {
            this.tasks.removeTask(this.breakDoor);
         }
      }
   }

   @Override
   public boolean isChild() {
      return this.getDataManager().get(IS_CHILD);
   }

   @Override
   protected int getExperiencePoints(EntityPlayer var1) {
      if (this.isChild()) {
         this.experienceValue = (int)(this.experienceValue * 2.5F);
      }

      return super.getExperiencePoints(☃);
   }

   public void setChild(boolean var1) {
      this.getDataManager().set(IS_CHILD, ☃);
      if (this.world != null && !this.world.isRemote) {
         IAttributeInstance ☃ = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
         ☃.removeModifier(BABY_SPEED_BOOST);
         if (☃) {
            ☃.applyModifier(BABY_SPEED_BOOST);
         }
      }

      this.setChildSize(☃);
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      if (IS_CHILD.equals(☃)) {
         this.setChildSize(this.isChild());
      }

      super.notifyDataManagerChange(☃);
   }

   @Override
   public void onLivingUpdate() {
      if (this.world.isDaytime() && !this.world.isRemote && !this.isChild() && this.shouldBurnInDay()) {
         float ☃ = this.getBrightness();
         if (☃ > 0.5F
            && this.rand.nextFloat() * 30.0F < (☃ - 0.4F) * 2.0F
            && this.world.canSeeSky(new BlockPos(this.posX, this.posY + this.getEyeHeight(), this.posZ))) {
            boolean ☃x = true;
            ItemStack ☃xx = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (!☃xx.isEmpty()) {
               if (☃xx.isItemStackDamageable()) {
                  ☃xx.setItemDamage(☃xx.getItemDamage() + this.rand.nextInt(2));
                  if (☃xx.getItemDamage() >= ☃xx.getMaxDamage()) {
                     this.renderBrokenItemStack(☃xx);
                     this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
                  }
               }

               ☃x = false;
            }

            if (☃x) {
               this.setFire(8);
            }
         }
      }

      super.onLivingUpdate();
   }

   protected boolean shouldBurnInDay() {
      return true;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (super.attackEntityFrom(☃, ☃)) {
         EntityLivingBase ☃ = this.getAttackTarget();
         if (☃ == null && ☃.getTrueSource() instanceof EntityLivingBase) {
            ☃ = (EntityLivingBase)☃.getTrueSource();
         }

         if (☃ != null
            && this.world.getDifficulty() == EnumDifficulty.HARD
            && this.rand.nextFloat() < this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).getAttributeValue()
            && this.world.getGameRules().getBoolean("doMobSpawning")) {
            int ☃x = MathHelper.floor(this.posX);
            int ☃xx = MathHelper.floor(this.posY);
            int ☃xxx = MathHelper.floor(this.posZ);
            EntityZombie ☃xxxx = new EntityZombie(this.world);

            for (int ☃xxxxx = 0; ☃xxxxx < 50; ☃xxxxx++) {
               int ☃xxxxxx = ☃x + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
               int ☃xxxxxxx = ☃xx + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
               int ☃xxxxxxxx = ☃xxx + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
               if (this.world.getBlockState(new BlockPos(☃xxxxxx, ☃xxxxxxx - 1, ☃xxxxxxxx)).isTopSolid()
                  && this.world.getLightFromNeighbors(new BlockPos(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx)) < 10) {
                  ☃xxxx.setPosition(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx);
                  if (!this.world.isAnyPlayerWithinRangeAt(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 7.0)
                     && this.world.checkNoEntityCollision(☃xxxx.getEntityBoundingBox(), ☃xxxx)
                     && this.world.getCollisionBoxes(☃xxxx, ☃xxxx.getEntityBoundingBox()).isEmpty()
                     && !this.world.containsAnyLiquid(☃xxxx.getEntityBoundingBox())) {
                     this.world.spawnEntity(☃xxxx);
                     ☃xxxx.setAttackTarget(☃);
                     ☃xxxx.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(☃xxxx)), null);
                     this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05F, 0));
                     ☃xxxx.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE)
                        .applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05F, 0));
                     break;
                  }
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean attackEntityAsMob(Entity var1) {
      boolean ☃ = super.attackEntityAsMob(☃);
      if (☃) {
         float ☃x = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();
         if (this.getHeldItemMainhand().isEmpty() && this.isBurning() && this.rand.nextFloat() < ☃x * 0.3F) {
            ☃.setFire(2 * (int)☃x);
         }
      }

      return ☃;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_ZOMBIE_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_ZOMBIE_DEATH;
   }

   protected SoundEvent getStepSound() {
      return SoundEvents.ENTITY_ZOMBIE_STEP;
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound(this.getStepSound(), 0.15F, 1.0F);
   }

   @Override
   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_ZOMBIE;
   }

   @Override
   protected void setEquipmentBasedOnDifficulty(DifficultyInstance var1) {
      super.setEquipmentBasedOnDifficulty(☃);
      if (this.rand.nextFloat() < (this.world.getDifficulty() == EnumDifficulty.HARD ? 0.05F : 0.01F)) {
         int ☃ = this.rand.nextInt(3);
         if (☃ == 0) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
         } else {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
         }
      }
   }

   public static void registerFixesZombie(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityZombie.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      if (this.isChild()) {
         ☃.setBoolean("IsBaby", true);
      }

      ☃.setBoolean("CanBreakDoors", this.isBreakDoorsTaskSet());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      if (☃.getBoolean("IsBaby")) {
         this.setChild(true);
      }

      this.setBreakDoorsAItask(☃.getBoolean("CanBreakDoors"));
   }

   @Override
   public void onKillEntity(EntityLivingBase var1) {
      super.onKillEntity(☃);
      if ((this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) && ☃ instanceof EntityVillager) {
         if (this.world.getDifficulty() != EnumDifficulty.HARD && this.rand.nextBoolean()) {
            return;
         }

         EntityVillager ☃ = (EntityVillager)☃;
         EntityZombieVillager ☃x = new EntityZombieVillager(this.world);
         ☃x.copyLocationAndAnglesFrom(☃);
         this.world.removeEntity(☃);
         ☃x.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(☃x)), new EntityZombie.GroupData(false));
         ☃x.setProfession(☃.getProfession());
         ☃x.setChild(☃.isChild());
         ☃x.setNoAI(☃.isAIDisabled());
         if (☃.hasCustomName()) {
            ☃x.setCustomNameTag(☃.getCustomNameTag());
            ☃x.setAlwaysRenderNameTag(☃.getAlwaysRenderNameTag());
         }

         this.world.spawnEntity(☃x);
         this.world.playEvent(null, 1026, new BlockPos(this), 0);
      }
   }

   @Override
   public float getEyeHeight() {
      float ☃ = 1.74F;
      if (this.isChild()) {
         ☃ = (float)(☃ - 0.81);
      }

      return ☃;
   }

   @Override
   protected boolean canEquipItem(ItemStack var1) {
      return ☃.getItem() == Items.EGG && this.isChild() && this.isRiding() ? false : super.canEquipItem(☃);
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      ☃ = super.onInitialSpawn(☃, ☃);
      float ☃ = ☃.getClampedAdditionalDifficulty();
      this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * ☃);
      if (☃ == null) {
         ☃ = new EntityZombie.GroupData(this.world.rand.nextFloat() < 0.05F);
      }

      if (☃ instanceof EntityZombie.GroupData) {
         EntityZombie.GroupData ☃x = (EntityZombie.GroupData)☃;
         if (☃x.isChild) {
            this.setChild(true);
            if (this.world.rand.nextFloat() < 0.05) {
               List<EntityChicken> ☃xx = this.world
                  .getEntitiesWithinAABB(EntityChicken.class, this.getEntityBoundingBox().grow(5.0, 3.0, 5.0), EntitySelectors.IS_STANDALONE);
               if (!☃xx.isEmpty()) {
                  EntityChicken ☃xxx = ☃xx.get(0);
                  ☃xxx.setChickenJockey(true);
                  this.startRiding(☃xxx);
               }
            } else if (this.world.rand.nextFloat() < 0.05) {
               EntityChicken ☃xx = new EntityChicken(this.world);
               ☃xx.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
               ☃xx.onInitialSpawn(☃, null);
               ☃xx.setChickenJockey(true);
               this.world.spawnEntity(☃xx);
               this.startRiding(☃xx);
            }
         }
      }

      this.setBreakDoorsAItask(this.rand.nextFloat() < ☃ * 0.1F);
      this.setEquipmentBasedOnDifficulty(☃);
      this.setEnchantmentBasedOnDifficulty(☃);
      if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
         Calendar ☃x = this.world.getCurrentDate();
         if (☃x.get(2) + 1 == 10 && ☃x.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
            this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
         }
      }

      this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE)
         .applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05F, 0));
      double ☃x = this.rand.nextDouble() * 1.5 * ☃;
      if (☃x > 1.0) {
         this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", ☃x, 2));
      }

      if (this.rand.nextFloat() < ☃ * 0.05F) {
         this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE)
            .applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25 + 0.5, 0));
         this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH)
            .applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0 + 1.0, 2));
         this.setBreakDoorsAItask(true);
      }

      return ☃;
   }

   public void setChildSize(boolean var1) {
      this.multiplySize(☃ ? 0.5F : 1.0F);
   }

   @Override
   protected final void setSize(float var1, float var2) {
      boolean ☃ = this.zombieWidth > 0.0F && this.zombieHeight > 0.0F;
      this.zombieWidth = ☃;
      this.zombieHeight = ☃;
      if (!☃) {
         this.multiplySize(1.0F);
      }
   }

   protected final void multiplySize(float var1) {
      super.setSize(this.zombieWidth * ☃, this.zombieHeight * ☃);
   }

   @Override
   public double getYOffset() {
      return this.isChild() ? 0.0 : -0.45;
   }

   @Override
   public void onDeath(DamageSource var1) {
      super.onDeath(☃);
      if (☃.getTrueSource() instanceof EntityCreeper) {
         EntityCreeper ☃ = (EntityCreeper)☃.getTrueSource();
         if (☃.getPowered() && ☃.ableToCauseSkullDrop()) {
            ☃.incrementDroppedSkulls();
            ItemStack ☃x = this.getSkullDrop();
            if (!☃x.isEmpty()) {
               this.entityDropItem(☃x, 0.0F);
            }
         }
      }
   }

   protected ItemStack getSkullDrop() {
      return new ItemStack(Items.SKULL, 1, 2);
   }

   class GroupData implements IEntityLivingData {
      public boolean isChild;

      private GroupData(boolean var2) {
         this.isChild = ☃;
      }
   }
}
