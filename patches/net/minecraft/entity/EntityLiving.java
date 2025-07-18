package net.minecraft.entity;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public abstract class EntityLiving extends EntityLivingBase {
   private static final DataParameter<Byte> AI_FLAGS = EntityDataManager.createKey(EntityLiving.class, DataSerializers.BYTE);
   public int livingSoundTime;
   protected int experienceValue;
   private final EntityLookHelper lookHelper;
   protected EntityMoveHelper moveHelper;
   protected EntityJumpHelper jumpHelper;
   private final EntityBodyHelper bodyHelper;
   protected PathNavigate navigator;
   protected final EntityAITasks tasks;
   protected final EntityAITasks targetTasks;
   private EntityLivingBase attackTarget;
   private final EntitySenses senses;
   private final NonNullList<ItemStack> inventoryHands = NonNullList.withSize(2, ItemStack.EMPTY);
   protected float[] inventoryHandsDropChances = new float[2];
   private final NonNullList<ItemStack> inventoryArmor = NonNullList.withSize(4, ItemStack.EMPTY);
   protected float[] inventoryArmorDropChances = new float[4];
   private boolean canPickUpLoot;
   private boolean persistenceRequired;
   private final Map<PathNodeType, Float> mapPathPriority = Maps.newEnumMap(PathNodeType.class);
   private ResourceLocation deathLootTable;
   private long deathLootTableSeed;
   private boolean isLeashed;
   private Entity leashHolder;
   private NBTTagCompound leashNBTTag;

   public EntityLiving(World var1) {
      super(☃);
      this.tasks = new EntityAITasks(☃ != null && ☃.profiler != null ? ☃.profiler : null);
      this.targetTasks = new EntityAITasks(☃ != null && ☃.profiler != null ? ☃.profiler : null);
      this.lookHelper = new EntityLookHelper(this);
      this.moveHelper = new EntityMoveHelper(this);
      this.jumpHelper = new EntityJumpHelper(this);
      this.bodyHelper = this.createBodyHelper();
      this.navigator = this.createNavigator(☃);
      this.senses = new EntitySenses(this);
      Arrays.fill(this.inventoryArmorDropChances, 0.085F);
      Arrays.fill(this.inventoryHandsDropChances, 0.085F);
      if (☃ != null && !☃.isRemote) {
         this.initEntityAI();
      }
   }

   protected void initEntityAI() {
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0);
   }

   protected PathNavigate createNavigator(World var1) {
      return new PathNavigateGround(this, ☃);
   }

   public float getPathPriority(PathNodeType var1) {
      Float ☃ = this.mapPathPriority.get(☃);
      return ☃ == null ? ☃.getPriority() : ☃;
   }

   public void setPathPriority(PathNodeType var1, float var2) {
      this.mapPathPriority.put(☃, ☃);
   }

   protected EntityBodyHelper createBodyHelper() {
      return new EntityBodyHelper(this);
   }

   public EntityLookHelper getLookHelper() {
      return this.lookHelper;
   }

   public EntityMoveHelper getMoveHelper() {
      return this.moveHelper;
   }

   public EntityJumpHelper getJumpHelper() {
      return this.jumpHelper;
   }

   public PathNavigate getNavigator() {
      return this.navigator;
   }

   public EntitySenses getEntitySenses() {
      return this.senses;
   }

   @Nullable
   public EntityLivingBase getAttackTarget() {
      return this.attackTarget;
   }

   public void setAttackTarget(@Nullable EntityLivingBase var1) {
      this.attackTarget = ☃;
   }

   public boolean canAttackClass(Class<? extends EntityLivingBase> var1) {
      return ☃ != EntityGhast.class;
   }

   public void eatGrassBonus() {
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(AI_FLAGS, (byte)0);
   }

   public int getTalkInterval() {
      return 80;
   }

   public void playLivingSound() {
      SoundEvent ☃ = this.getAmbientSound();
      if (☃ != null) {
         this.playSound(☃, this.getSoundVolume(), this.getSoundPitch());
      }
   }

   @Override
   public void onEntityUpdate() {
      super.onEntityUpdate();
      this.world.profiler.startSection("mobBaseTick");
      if (this.isEntityAlive() && this.rand.nextInt(1000) < this.livingSoundTime++) {
         this.applyEntityAI();
         this.playLivingSound();
      }

      this.world.profiler.endSection();
   }

   @Override
   protected void playHurtSound(DamageSource var1) {
      this.applyEntityAI();
      super.playHurtSound(☃);
   }

   private void applyEntityAI() {
      this.livingSoundTime = -this.getTalkInterval();
   }

   @Override
   protected int getExperiencePoints(EntityPlayer var1) {
      if (this.experienceValue > 0) {
         int ☃ = this.experienceValue;

         for (int ☃x = 0; ☃x < this.inventoryArmor.size(); ☃x++) {
            if (!this.inventoryArmor.get(☃x).isEmpty() && this.inventoryArmorDropChances[☃x] <= 1.0F) {
               ☃ += 1 + this.rand.nextInt(3);
            }
         }

         for (int ☃xx = 0; ☃xx < this.inventoryHands.size(); ☃xx++) {
            if (!this.inventoryHands.get(☃xx).isEmpty() && this.inventoryHandsDropChances[☃xx] <= 1.0F) {
               ☃ += 1 + this.rand.nextInt(3);
            }
         }

         return ☃;
      } else {
         return this.experienceValue;
      }
   }

   public void spawnExplosionParticle() {
      if (this.world.isRemote) {
         for (int ☃ = 0; ☃ < 20; ☃++) {
            double ☃x = this.rand.nextGaussian() * 0.02;
            double ☃xx = this.rand.nextGaussian() * 0.02;
            double ☃xxx = this.rand.nextGaussian() * 0.02;
            double ☃xxxx = 10.0;
            this.world
               .spawnParticle(
                  EnumParticleTypes.EXPLOSION_NORMAL,
                  this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width - ☃x * 10.0,
                  this.posY + this.rand.nextFloat() * this.height - ☃xx * 10.0,
                  this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width - ☃xxx * 10.0,
                  ☃x,
                  ☃xx,
                  ☃xxx
               );
         }
      } else {
         this.world.setEntityState(this, (byte)20);
      }
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 20) {
         this.spawnExplosionParticle();
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (!this.world.isRemote) {
         this.updateLeashedState();
         if (this.ticksExisted % 5 == 0) {
            boolean ☃ = !(this.getControllingPassenger() instanceof EntityLiving);
            boolean ☃x = !(this.getRidingEntity() instanceof EntityBoat);
            this.tasks.setControlFlag(1, ☃);
            this.tasks.setControlFlag(4, ☃ && ☃x);
            this.tasks.setControlFlag(2, ☃);
         }
      }
   }

   @Override
   protected float updateDistance(float var1, float var2) {
      this.bodyHelper.updateRenderAngles();
      return ☃;
   }

   @Nullable
   protected SoundEvent getAmbientSound() {
      return null;
   }

   @Nullable
   protected Item getDropItem() {
      return null;
   }

   @Override
   protected void dropFewItems(boolean var1, int var2) {
      Item ☃ = this.getDropItem();
      if (☃ != null) {
         int ☃x = this.rand.nextInt(3);
         if (☃ > 0) {
            ☃x += this.rand.nextInt(☃ + 1);
         }

         for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
            this.dropItem(☃, 1);
         }
      }
   }

   public static void registerFixesMob(DataFixer var0, Class<?> var1) {
      ☃.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(☃, "ArmorItems", "HandItems"));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setBoolean("CanPickUpLoot", this.canPickUpLoot());
      ☃.setBoolean("PersistenceRequired", this.persistenceRequired);
      NBTTagList ☃ = new NBTTagList();

      for (ItemStack ☃x : this.inventoryArmor) {
         NBTTagCompound ☃xx = new NBTTagCompound();
         if (!☃x.isEmpty()) {
            ☃x.writeToNBT(☃xx);
         }

         ☃.appendTag(☃xx);
      }

      ☃.setTag("ArmorItems", ☃);
      NBTTagList ☃x = new NBTTagList();

      for (ItemStack ☃xx : this.inventoryHands) {
         NBTTagCompound ☃xxx = new NBTTagCompound();
         if (!☃xx.isEmpty()) {
            ☃xx.writeToNBT(☃xxx);
         }

         ☃x.appendTag(☃xxx);
      }

      ☃.setTag("HandItems", ☃x);
      NBTTagList ☃xx = new NBTTagList();

      for (float ☃xxx : this.inventoryArmorDropChances) {
         ☃xx.appendTag(new NBTTagFloat(☃xxx));
      }

      ☃.setTag("ArmorDropChances", ☃xx);
      NBTTagList ☃xxx = new NBTTagList();

      for (float ☃xxxx : this.inventoryHandsDropChances) {
         ☃xxx.appendTag(new NBTTagFloat(☃xxxx));
      }

      ☃.setTag("HandDropChances", ☃xxx);
      ☃.setBoolean("Leashed", this.isLeashed);
      if (this.leashHolder != null) {
         NBTTagCompound ☃xxxx = new NBTTagCompound();
         if (this.leashHolder instanceof EntityLivingBase) {
            UUID ☃xxxxx = this.leashHolder.getUniqueID();
            ☃xxxx.setUniqueId("UUID", ☃xxxxx);
         } else if (this.leashHolder instanceof EntityHanging) {
            BlockPos ☃xxxxx = ((EntityHanging)this.leashHolder).getHangingPosition();
            ☃xxxx.setInteger("X", ☃xxxxx.getX());
            ☃xxxx.setInteger("Y", ☃xxxxx.getY());
            ☃xxxx.setInteger("Z", ☃xxxxx.getZ());
         }

         ☃.setTag("Leash", ☃xxxx);
      }

      ☃.setBoolean("LeftHanded", this.isLeftHanded());
      if (this.deathLootTable != null) {
         ☃.setString("DeathLootTable", this.deathLootTable.toString());
         if (this.deathLootTableSeed != 0L) {
            ☃.setLong("DeathLootTableSeed", this.deathLootTableSeed);
         }
      }

      if (this.isAIDisabled()) {
         ☃.setBoolean("NoAI", this.isAIDisabled());
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      if (☃.hasKey("CanPickUpLoot", 1)) {
         this.setCanPickUpLoot(☃.getBoolean("CanPickUpLoot"));
      }

      this.persistenceRequired = ☃.getBoolean("PersistenceRequired");
      if (☃.hasKey("ArmorItems", 9)) {
         NBTTagList ☃ = ☃.getTagList("ArmorItems", 10);

         for (int ☃x = 0; ☃x < this.inventoryArmor.size(); ☃x++) {
            this.inventoryArmor.set(☃x, new ItemStack(☃.getCompoundTagAt(☃x)));
         }
      }

      if (☃.hasKey("HandItems", 9)) {
         NBTTagList ☃ = ☃.getTagList("HandItems", 10);

         for (int ☃x = 0; ☃x < this.inventoryHands.size(); ☃x++) {
            this.inventoryHands.set(☃x, new ItemStack(☃.getCompoundTagAt(☃x)));
         }
      }

      if (☃.hasKey("ArmorDropChances", 9)) {
         NBTTagList ☃ = ☃.getTagList("ArmorDropChances", 5);

         for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
            this.inventoryArmorDropChances[☃x] = ☃.getFloatAt(☃x);
         }
      }

      if (☃.hasKey("HandDropChances", 9)) {
         NBTTagList ☃ = ☃.getTagList("HandDropChances", 5);

         for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
            this.inventoryHandsDropChances[☃x] = ☃.getFloatAt(☃x);
         }
      }

      this.isLeashed = ☃.getBoolean("Leashed");
      if (this.isLeashed && ☃.hasKey("Leash", 10)) {
         this.leashNBTTag = ☃.getCompoundTag("Leash");
      }

      this.setLeftHanded(☃.getBoolean("LeftHanded"));
      if (☃.hasKey("DeathLootTable", 8)) {
         this.deathLootTable = new ResourceLocation(☃.getString("DeathLootTable"));
         this.deathLootTableSeed = ☃.getLong("DeathLootTableSeed");
      }

      this.setNoAI(☃.getBoolean("NoAI"));
   }

   @Nullable
   protected ResourceLocation getLootTable() {
      return null;
   }

   @Override
   protected void dropLoot(boolean var1, int var2, DamageSource var3) {
      ResourceLocation ☃ = this.deathLootTable;
      if (☃ == null) {
         ☃ = this.getLootTable();
      }

      if (☃ != null) {
         LootTable ☃x = this.world.getLootTableManager().getLootTableFromLocation(☃);
         this.deathLootTable = null;
         LootContext.Builder ☃xx = new LootContext.Builder((WorldServer)this.world).withLootedEntity(this).withDamageSource(☃);
         if (☃ && this.attackingPlayer != null) {
            ☃xx = ☃xx.withPlayer(this.attackingPlayer).withLuck(this.attackingPlayer.getLuck());
         }

         for (ItemStack ☃xxx : ☃x.generateLootForPools(this.deathLootTableSeed == 0L ? this.rand : new Random(this.deathLootTableSeed), ☃xx.build())) {
            this.entityDropItem(☃xxx, 0.0F);
         }

         this.dropEquipment(☃, ☃);
      } else {
         super.dropLoot(☃, ☃, ☃);
      }
   }

   public void setMoveForward(float var1) {
      this.moveForward = ☃;
   }

   public void setMoveVertical(float var1) {
      this.moveVertical = ☃;
   }

   public void setMoveStrafing(float var1) {
      this.moveStrafing = ☃;
   }

   @Override
   public void setAIMoveSpeed(float var1) {
      super.setAIMoveSpeed(☃);
      this.setMoveForward(☃);
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      this.world.profiler.startSection("looting");
      if (!this.world.isRemote && this.canPickUpLoot() && !this.dead && this.world.getGameRules().getBoolean("mobGriefing")) {
         for (EntityItem ☃ : this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().grow(1.0, 0.0, 1.0))) {
            if (!☃.isDead && !☃.getItem().isEmpty() && !☃.cannotPickup()) {
               this.updateEquipmentIfNeeded(☃);
            }
         }
      }

      this.world.profiler.endSection();
   }

   protected void updateEquipmentIfNeeded(EntityItem var1) {
      ItemStack ☃ = ☃.getItem();
      EntityEquipmentSlot ☃x = getSlotForItemStack(☃);
      boolean ☃xx = true;
      ItemStack ☃xxx = this.getItemStackFromSlot(☃x);
      if (!☃xxx.isEmpty()) {
         if (☃x.getSlotType() == EntityEquipmentSlot.Type.HAND) {
            if (☃.getItem() instanceof ItemSword && !(☃xxx.getItem() instanceof ItemSword)) {
               ☃xx = true;
            } else if (☃.getItem() instanceof ItemSword && ☃xxx.getItem() instanceof ItemSword) {
               ItemSword ☃xxxx = (ItemSword)☃.getItem();
               ItemSword ☃xxxxx = (ItemSword)☃xxx.getItem();
               if (☃xxxx.getAttackDamage() == ☃xxxxx.getAttackDamage()) {
                  ☃xx = ☃.getMetadata() > ☃xxx.getMetadata() || ☃.hasTagCompound() && !☃xxx.hasTagCompound();
               } else {
                  ☃xx = ☃xxxx.getAttackDamage() > ☃xxxxx.getAttackDamage();
               }
            } else if (☃.getItem() instanceof ItemBow && ☃xxx.getItem() instanceof ItemBow) {
               ☃xx = ☃.hasTagCompound() && !☃xxx.hasTagCompound();
            } else {
               ☃xx = false;
            }
         } else if (☃.getItem() instanceof ItemArmor && !(☃xxx.getItem() instanceof ItemArmor)) {
            ☃xx = true;
         } else if (☃.getItem() instanceof ItemArmor && ☃xxx.getItem() instanceof ItemArmor && !EnchantmentHelper.hasBindingCurse(☃xxx)) {
            ItemArmor ☃xxxx = (ItemArmor)☃.getItem();
            ItemArmor ☃xxxxx = (ItemArmor)☃xxx.getItem();
            if (☃xxxx.damageReduceAmount == ☃xxxxx.damageReduceAmount) {
               ☃xx = ☃.getMetadata() > ☃xxx.getMetadata() || ☃.hasTagCompound() && !☃xxx.hasTagCompound();
            } else {
               ☃xx = ☃xxxx.damageReduceAmount > ☃xxxxx.damageReduceAmount;
            }
         } else {
            ☃xx = false;
         }
      }

      if (☃xx && this.canEquipItem(☃)) {
         double ☃xxxx;
         switch (☃x.getSlotType()) {
            case HAND:
               ☃xxxx = this.inventoryHandsDropChances[☃x.getIndex()];
               break;
            case ARMOR:
               ☃xxxx = this.inventoryArmorDropChances[☃x.getIndex()];
               break;
            default:
               ☃xxxx = 0.0;
         }

         if (!☃xxx.isEmpty() && this.rand.nextFloat() - 0.1F < ☃xxxx) {
            this.entityDropItem(☃xxx, 0.0F);
         }

         this.setItemStackToSlot(☃x, ☃);
         switch (☃x.getSlotType()) {
            case HAND:
               this.inventoryHandsDropChances[☃x.getIndex()] = 2.0F;
               break;
            case ARMOR:
               this.inventoryArmorDropChances[☃x.getIndex()] = 2.0F;
         }

         this.persistenceRequired = true;
         this.onItemPickup(☃, ☃.getCount());
         ☃.setDead();
      }
   }

   protected boolean canEquipItem(ItemStack var1) {
      return true;
   }

   protected boolean canDespawn() {
      return true;
   }

   protected void despawnEntity() {
      if (this.persistenceRequired) {
         this.idleTime = 0;
      } else {
         Entity ☃ = this.world.getClosestPlayerToEntity(this, -1.0);
         if (☃ != null) {
            double ☃x = ☃.posX - this.posX;
            double ☃xx = ☃.posY - this.posY;
            double ☃xxx = ☃.posZ - this.posZ;
            double ☃xxxx = ☃x * ☃x + ☃xx * ☃xx + ☃xxx * ☃xxx;
            if (this.canDespawn() && ☃xxxx > 16384.0) {
               this.setDead();
            }

            if (this.idleTime > 600 && this.rand.nextInt(800) == 0 && ☃xxxx > 1024.0 && this.canDespawn()) {
               this.setDead();
            } else if (☃xxxx < 1024.0) {
               this.idleTime = 0;
            }
         }
      }
   }

   @Override
   protected final void updateEntityActionState() {
      this.idleTime++;
      this.world.profiler.startSection("checkDespawn");
      this.despawnEntity();
      this.world.profiler.endSection();
      this.world.profiler.startSection("sensing");
      this.senses.clearSensingCache();
      this.world.profiler.endSection();
      this.world.profiler.startSection("targetSelector");
      this.targetTasks.onUpdateTasks();
      this.world.profiler.endSection();
      this.world.profiler.startSection("goalSelector");
      this.tasks.onUpdateTasks();
      this.world.profiler.endSection();
      this.world.profiler.startSection("navigation");
      this.navigator.onUpdateNavigation();
      this.world.profiler.endSection();
      this.world.profiler.startSection("mob tick");
      this.updateAITasks();
      this.world.profiler.endSection();
      if (this.isRiding() && this.getRidingEntity() instanceof EntityLiving) {
         EntityLiving ☃ = (EntityLiving)this.getRidingEntity();
         ☃.getNavigator().setPath(this.getNavigator().getPath(), 1.5);
         ☃.getMoveHelper().read(this.getMoveHelper());
      }

      this.world.profiler.startSection("controls");
      this.world.profiler.startSection("move");
      this.moveHelper.onUpdateMoveHelper();
      this.world.profiler.endStartSection("look");
      this.lookHelper.onUpdateLook();
      this.world.profiler.endStartSection("jump");
      this.jumpHelper.doJump();
      this.world.profiler.endSection();
      this.world.profiler.endSection();
   }

   protected void updateAITasks() {
   }

   public int getVerticalFaceSpeed() {
      return 40;
   }

   public int getHorizontalFaceSpeed() {
      return 10;
   }

   public void faceEntity(Entity var1, float var2, float var3) {
      double ☃ = ☃.posX - this.posX;
      double ☃x = ☃.posZ - this.posZ;
      double ☃xx;
      if (☃ instanceof EntityLivingBase) {
         EntityLivingBase ☃xxx = (EntityLivingBase)☃;
         ☃xx = ☃xxx.posY + ☃xxx.getEyeHeight() - (this.posY + this.getEyeHeight());
      } else {
         ☃xx = (☃.getEntityBoundingBox().minY + ☃.getEntityBoundingBox().maxY) / 2.0 - (this.posY + this.getEyeHeight());
      }

      double ☃xxx = MathHelper.sqrt(☃ * ☃ + ☃x * ☃x);
      float ☃xxxx = (float)(MathHelper.atan2(☃x, ☃) * 180.0F / (float)Math.PI) - 90.0F;
      float ☃xxxxx = (float)(-(MathHelper.atan2(☃xx, ☃xxx) * 180.0F / (float)Math.PI));
      this.rotationPitch = this.updateRotation(this.rotationPitch, ☃xxxxx, ☃);
      this.rotationYaw = this.updateRotation(this.rotationYaw, ☃xxxx, ☃);
   }

   private float updateRotation(float var1, float var2, float var3) {
      float ☃ = MathHelper.wrapDegrees(☃ - ☃);
      if (☃ > ☃) {
         ☃ = ☃;
      }

      if (☃ < -☃) {
         ☃ = -☃;
      }

      return ☃ + ☃;
   }

   public boolean getCanSpawnHere() {
      IBlockState ☃ = this.world.getBlockState(new BlockPos(this).down());
      return ☃.canEntitySpawn(this);
   }

   public boolean isNotColliding() {
      return !this.world.containsAnyLiquid(this.getEntityBoundingBox())
         && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()
         && this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this);
   }

   public float getRenderSizeModifier() {
      return 1.0F;
   }

   public int getMaxSpawnedInChunk() {
      return 4;
   }

   @Override
   public int getMaxFallHeight() {
      if (this.getAttackTarget() == null) {
         return 3;
      } else {
         int ☃ = (int)(this.getHealth() - this.getMaxHealth() * 0.33F);
         ☃ -= (3 - this.world.getDifficulty().getId()) * 4;
         if (☃ < 0) {
            ☃ = 0;
         }

         return ☃ + 3;
      }
   }

   @Override
   public Iterable<ItemStack> getHeldEquipment() {
      return this.inventoryHands;
   }

   @Override
   public Iterable<ItemStack> getArmorInventoryList() {
      return this.inventoryArmor;
   }

   @Override
   public ItemStack getItemStackFromSlot(EntityEquipmentSlot var1) {
      switch (☃.getSlotType()) {
         case HAND:
            return this.inventoryHands.get(☃.getIndex());
         case ARMOR:
            return this.inventoryArmor.get(☃.getIndex());
         default:
            return ItemStack.EMPTY;
      }
   }

   @Override
   public void setItemStackToSlot(EntityEquipmentSlot var1, ItemStack var2) {
      switch (☃.getSlotType()) {
         case HAND:
            this.inventoryHands.set(☃.getIndex(), ☃);
            break;
         case ARMOR:
            this.inventoryArmor.set(☃.getIndex(), ☃);
      }
   }

   @Override
   protected void dropEquipment(boolean var1, int var2) {
      for (EntityEquipmentSlot ☃ : EntityEquipmentSlot.values()) {
         ItemStack ☃x = this.getItemStackFromSlot(☃);
         double ☃xx;
         switch (☃.getSlotType()) {
            case HAND:
               ☃xx = this.inventoryHandsDropChances[☃.getIndex()];
               break;
            case ARMOR:
               ☃xx = this.inventoryArmorDropChances[☃.getIndex()];
               break;
            default:
               ☃xx = 0.0;
         }

         boolean ☃x = ☃xx > 1.0;
         if (!☃x.isEmpty() && !EnchantmentHelper.hasVanishingCurse(☃x) && (☃ || ☃x) && this.rand.nextFloat() - ☃ * 0.01F < ☃xx) {
            if (!☃x && ☃x.isItemStackDamageable()) {
               ☃x.setItemDamage(☃x.getMaxDamage() - this.rand.nextInt(1 + this.rand.nextInt(Math.max(☃x.getMaxDamage() - 3, 1))));
            }

            this.entityDropItem(☃x, 0.0F);
         }
      }
   }

   protected void setEquipmentBasedOnDifficulty(DifficultyInstance var1) {
      if (this.rand.nextFloat() < 0.15F * ☃.getClampedAdditionalDifficulty()) {
         int ☃ = this.rand.nextInt(2);
         float ☃x = this.world.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.25F;
         if (this.rand.nextFloat() < 0.095F) {
            ☃++;
         }

         if (this.rand.nextFloat() < 0.095F) {
            ☃++;
         }

         if (this.rand.nextFloat() < 0.095F) {
            ☃++;
         }

         boolean ☃xx = true;

         for (EntityEquipmentSlot ☃xxx : EntityEquipmentSlot.values()) {
            if (☃xxx.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
               ItemStack ☃xxxx = this.getItemStackFromSlot(☃xxx);
               if (!☃xx && this.rand.nextFloat() < ☃x) {
                  break;
               }

               ☃xx = false;
               if (☃xxxx.isEmpty()) {
                  Item ☃xxxxx = getArmorByChance(☃xxx, ☃);
                  if (☃xxxxx != null) {
                     this.setItemStackToSlot(☃xxx, new ItemStack(☃xxxxx));
                  }
               }
            }
         }
      }
   }

   public static EntityEquipmentSlot getSlotForItemStack(ItemStack var0) {
      if (☃.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN) || ☃.getItem() == Items.SKULL) {
         return EntityEquipmentSlot.HEAD;
      } else if (☃.getItem() instanceof ItemArmor) {
         return ((ItemArmor)☃.getItem()).armorType;
      } else if (☃.getItem() == Items.ELYTRA) {
         return EntityEquipmentSlot.CHEST;
      } else {
         return ☃.getItem() == Items.SHIELD ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND;
      }
   }

   @Nullable
   public static Item getArmorByChance(EntityEquipmentSlot var0, int var1) {
      switch (☃) {
         case HEAD:
            if (☃ == 0) {
               return Items.LEATHER_HELMET;
            } else if (☃ == 1) {
               return Items.GOLDEN_HELMET;
            } else if (☃ == 2) {
               return Items.CHAINMAIL_HELMET;
            } else if (☃ == 3) {
               return Items.IRON_HELMET;
            } else if (☃ == 4) {
               return Items.DIAMOND_HELMET;
            }
         case CHEST:
            if (☃ == 0) {
               return Items.LEATHER_CHESTPLATE;
            } else if (☃ == 1) {
               return Items.GOLDEN_CHESTPLATE;
            } else if (☃ == 2) {
               return Items.CHAINMAIL_CHESTPLATE;
            } else if (☃ == 3) {
               return Items.IRON_CHESTPLATE;
            } else if (☃ == 4) {
               return Items.DIAMOND_CHESTPLATE;
            }
         case LEGS:
            if (☃ == 0) {
               return Items.LEATHER_LEGGINGS;
            } else if (☃ == 1) {
               return Items.GOLDEN_LEGGINGS;
            } else if (☃ == 2) {
               return Items.CHAINMAIL_LEGGINGS;
            } else if (☃ == 3) {
               return Items.IRON_LEGGINGS;
            } else if (☃ == 4) {
               return Items.DIAMOND_LEGGINGS;
            }
         case FEET:
            if (☃ == 0) {
               return Items.LEATHER_BOOTS;
            } else if (☃ == 1) {
               return Items.GOLDEN_BOOTS;
            } else if (☃ == 2) {
               return Items.CHAINMAIL_BOOTS;
            } else if (☃ == 3) {
               return Items.IRON_BOOTS;
            } else if (☃ == 4) {
               return Items.DIAMOND_BOOTS;
            }
         default:
            return null;
      }
   }

   protected void setEnchantmentBasedOnDifficulty(DifficultyInstance var1) {
      float ☃ = ☃.getClampedAdditionalDifficulty();
      if (!this.getHeldItemMainhand().isEmpty() && this.rand.nextFloat() < 0.25F * ☃) {
         this.setItemStackToSlot(
            EntityEquipmentSlot.MAINHAND,
            EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItemMainhand(), (int)(5.0F + ☃ * this.rand.nextInt(18)), false)
         );
      }

      for (EntityEquipmentSlot ☃x : EntityEquipmentSlot.values()) {
         if (☃x.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
            ItemStack ☃xx = this.getItemStackFromSlot(☃x);
            if (!☃xx.isEmpty() && this.rand.nextFloat() < 0.5F * ☃) {
               this.setItemStackToSlot(☃x, EnchantmentHelper.addRandomEnchantment(this.rand, ☃xx, (int)(5.0F + ☃ * this.rand.nextInt(18)), false));
            }
         }
      }
   }

   @Nullable
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE)
         .applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05, 1));
      if (this.rand.nextFloat() < 0.05F) {
         this.setLeftHanded(true);
      } else {
         this.setLeftHanded(false);
      }

      return ☃;
   }

   public boolean canBeSteered() {
      return false;
   }

   public void enablePersistence() {
      this.persistenceRequired = true;
   }

   public void setDropChance(EntityEquipmentSlot var1, float var2) {
      switch (☃.getSlotType()) {
         case HAND:
            this.inventoryHandsDropChances[☃.getIndex()] = ☃;
            break;
         case ARMOR:
            this.inventoryArmorDropChances[☃.getIndex()] = ☃;
      }
   }

   public boolean canPickUpLoot() {
      return this.canPickUpLoot;
   }

   public void setCanPickUpLoot(boolean var1) {
      this.canPickUpLoot = ☃;
   }

   public boolean isNoDespawnRequired() {
      return this.persistenceRequired;
   }

   @Override
   public final boolean processInitialInteract(EntityPlayer var1, EnumHand var2) {
      if (this.getLeashed() && this.getLeashHolder() == ☃) {
         this.clearLeashed(true, !☃.capabilities.isCreativeMode);
         return true;
      } else {
         ItemStack ☃ = ☃.getHeldItem(☃);
         if (☃.getItem() == Items.LEAD && this.canBeLeashedTo(☃)) {
            this.setLeashHolder(☃, true);
            ☃.shrink(1);
            return true;
         } else {
            return this.processInteract(☃, ☃) ? true : super.processInitialInteract(☃, ☃);
         }
      }
   }

   protected boolean processInteract(EntityPlayer var1, EnumHand var2) {
      return false;
   }

   protected void updateLeashedState() {
      if (this.leashNBTTag != null) {
         this.recreateLeash();
      }

      if (this.isLeashed) {
         if (!this.isEntityAlive()) {
            this.clearLeashed(true, true);
         }

         if (this.leashHolder == null || this.leashHolder.isDead) {
            this.clearLeashed(true, true);
         }
      }
   }

   public void clearLeashed(boolean var1, boolean var2) {
      if (this.isLeashed) {
         this.isLeashed = false;
         this.leashHolder = null;
         if (!this.world.isRemote && ☃) {
            this.dropItem(Items.LEAD, 1);
         }

         if (!this.world.isRemote && ☃ && this.world instanceof WorldServer) {
            ((WorldServer)this.world).getEntityTracker().sendToTracking(this, new SPacketEntityAttach(this, null));
         }
      }
   }

   public boolean canBeLeashedTo(EntityPlayer var1) {
      return !this.getLeashed() && !(this instanceof IMob);
   }

   public boolean getLeashed() {
      return this.isLeashed;
   }

   public Entity getLeashHolder() {
      return this.leashHolder;
   }

   public void setLeashHolder(Entity var1, boolean var2) {
      this.isLeashed = true;
      this.leashHolder = ☃;
      if (!this.world.isRemote && ☃ && this.world instanceof WorldServer) {
         ((WorldServer)this.world).getEntityTracker().sendToTracking(this, new SPacketEntityAttach(this, this.leashHolder));
      }

      if (this.isRiding()) {
         this.dismountRidingEntity();
      }
   }

   @Override
   public boolean startRiding(Entity var1, boolean var2) {
      boolean ☃ = super.startRiding(☃, ☃);
      if (☃ && this.getLeashed()) {
         this.clearLeashed(true, true);
      }

      return ☃;
   }

   private void recreateLeash() {
      if (this.isLeashed && this.leashNBTTag != null) {
         if (this.leashNBTTag.hasUniqueId("UUID")) {
            UUID ☃ = this.leashNBTTag.getUniqueId("UUID");

            for (EntityLivingBase ☃x : this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(10.0))) {
               if (☃x.getUniqueID().equals(☃)) {
                  this.setLeashHolder(☃x, true);
                  break;
               }
            }
         } else if (this.leashNBTTag.hasKey("X", 99) && this.leashNBTTag.hasKey("Y", 99) && this.leashNBTTag.hasKey("Z", 99)) {
            BlockPos ☃ = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"), this.leashNBTTag.getInteger("Z"));
            EntityLeashKnot ☃xx = EntityLeashKnot.getKnotForPosition(this.world, ☃);
            if (☃xx == null) {
               ☃xx = EntityLeashKnot.createKnot(this.world, ☃);
            }

            this.setLeashHolder(☃xx, true);
         } else {
            this.clearLeashed(false, true);
         }
      }

      this.leashNBTTag = null;
   }

   @Override
   public boolean replaceItemInInventory(int var1, ItemStack var2) {
      EntityEquipmentSlot ☃;
      if (☃ == 98) {
         ☃ = EntityEquipmentSlot.MAINHAND;
      } else if (☃ == 99) {
         ☃ = EntityEquipmentSlot.OFFHAND;
      } else if (☃ == 100 + EntityEquipmentSlot.HEAD.getIndex()) {
         ☃ = EntityEquipmentSlot.HEAD;
      } else if (☃ == 100 + EntityEquipmentSlot.CHEST.getIndex()) {
         ☃ = EntityEquipmentSlot.CHEST;
      } else if (☃ == 100 + EntityEquipmentSlot.LEGS.getIndex()) {
         ☃ = EntityEquipmentSlot.LEGS;
      } else {
         if (☃ != 100 + EntityEquipmentSlot.FEET.getIndex()) {
            return false;
         }

         ☃ = EntityEquipmentSlot.FEET;
      }

      if (!☃.isEmpty() && !isItemStackInSlot(☃, ☃) && ☃ != EntityEquipmentSlot.HEAD) {
         return false;
      } else {
         this.setItemStackToSlot(☃, ☃);
         return true;
      }
   }

   @Override
   public boolean canPassengerSteer() {
      return this.canBeSteered() && super.canPassengerSteer();
   }

   public static boolean isItemStackInSlot(EntityEquipmentSlot var0, ItemStack var1) {
      EntityEquipmentSlot ☃ = getSlotForItemStack(☃);
      return ☃ == ☃
         || ☃ == EntityEquipmentSlot.MAINHAND && ☃ == EntityEquipmentSlot.OFFHAND
         || ☃ == EntityEquipmentSlot.OFFHAND && ☃ == EntityEquipmentSlot.MAINHAND;
   }

   @Override
   public boolean isServerWorld() {
      return super.isServerWorld() && !this.isAIDisabled();
   }

   public void setNoAI(boolean var1) {
      byte ☃ = this.dataManager.get(AI_FLAGS);
      this.dataManager.set(AI_FLAGS, ☃ ? (byte)(☃ | 1) : (byte)(☃ & -2));
   }

   public void setLeftHanded(boolean var1) {
      byte ☃ = this.dataManager.get(AI_FLAGS);
      this.dataManager.set(AI_FLAGS, ☃ ? (byte)(☃ | 2) : (byte)(☃ & -3));
   }

   public boolean isAIDisabled() {
      return (this.dataManager.get(AI_FLAGS) & 1) != 0;
   }

   public boolean isLeftHanded() {
      return (this.dataManager.get(AI_FLAGS) & 2) != 0;
   }

   @Override
   public EnumHandSide getPrimaryHand() {
      return this.isLeftHanded() ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
   }

   public static enum SpawnPlacementType {
      ON_GROUND,
      IN_AIR,
      IN_WATER;
   }
}
