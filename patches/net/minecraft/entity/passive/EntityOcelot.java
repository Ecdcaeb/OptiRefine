package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityOcelot extends EntityTameable {
   private static final DataParameter<Integer> OCELOT_VARIANT = EntityDataManager.createKey(EntityOcelot.class, DataSerializers.VARINT);
   private EntityAIAvoidEntity<EntityPlayer> avoidEntity;
   private EntityAITempt aiTempt;

   public EntityOcelot(World var1) {
      super(☃);
      this.setSize(0.6F, 0.7F);
   }

   @Override
   protected void initEntityAI() {
      this.aiSit = new EntityAISit(this);
      this.aiTempt = new EntityAITempt(this, 0.6, Items.FISH, true);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, this.aiSit);
      this.tasks.addTask(3, this.aiTempt);
      this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0, 10.0F, 5.0F));
      this.tasks.addTask(6, new EntityAIOcelotSit(this, 0.8));
      this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
      this.tasks.addTask(8, new EntityAIOcelotAttack(this));
      this.tasks.addTask(9, new EntityAIMate(this, 0.8));
      this.tasks.addTask(10, new EntityAIWanderAvoidWater(this, 0.8, 1.0000001E-5F));
      this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
      this.targetTasks.addTask(1, new EntityAITargetNonTamed<>(this, EntityChicken.class, false, null));
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(OCELOT_VARIANT, 0);
   }

   @Override
   public void updateAITasks() {
      if (this.getMoveHelper().isUpdating()) {
         double ☃ = this.getMoveHelper().getSpeed();
         if (☃ == 0.6) {
            this.setSneaking(true);
            this.setSprinting(false);
         } else if (☃ == 1.33) {
            this.setSneaking(false);
            this.setSprinting(true);
         } else {
            this.setSneaking(false);
            this.setSprinting(false);
         }
      } else {
         this.setSneaking(false);
         this.setSprinting(false);
      }
   }

   @Override
   protected boolean canDespawn() {
      return !this.isTamed() && this.ticksExisted > 2400;
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3F);
   }

   @Override
   public void fall(float var1, float var2) {
   }

   public static void registerFixesOcelot(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityOcelot.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("CatType", this.getTameSkin());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setTameSkin(☃.getInteger("CatType"));
   }

   @Nullable
   @Override
   protected SoundEvent getAmbientSound() {
      if (this.isTamed()) {
         if (this.isInLove()) {
            return SoundEvents.ENTITY_CAT_PURR;
         } else {
            return this.rand.nextInt(4) == 0 ? SoundEvents.ENTITY_CAT_PURREOW : SoundEvents.ENTITY_CAT_AMBIENT;
         }
      } else {
         return null;
      }
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_CAT_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_CAT_DEATH;
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F;
   }

   @Override
   public boolean attackEntityAsMob(Entity var1) {
      return ☃.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else {
         if (this.aiSit != null) {
            this.aiSit.setSitting(false);
         }

         return super.attackEntityFrom(☃, ☃);
      }
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_OCELOT;
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (this.isTamed()) {
         if (this.isOwner(☃) && !this.world.isRemote && !this.isBreedingItem(☃)) {
            this.aiSit.setSitting(!this.isSitting());
         }
      } else if ((this.aiTempt == null || this.aiTempt.isRunning()) && ☃.getItem() == Items.FISH && ☃.getDistanceSq(this) < 9.0) {
         if (!☃.capabilities.isCreativeMode) {
            ☃.shrink(1);
         }

         if (!this.world.isRemote) {
            if (this.rand.nextInt(3) == 0) {
               this.setTamedBy(☃);
               this.setTameSkin(1 + this.world.rand.nextInt(3));
               this.playTameEffect(true);
               this.aiSit.setSitting(true);
               this.world.setEntityState(this, (byte)7);
            } else {
               this.playTameEffect(false);
               this.world.setEntityState(this, (byte)6);
            }
         }

         return true;
      }

      return super.processInteract(☃, ☃);
   }

   public EntityOcelot createChild(EntityAgeable var1) {
      EntityOcelot ☃ = new EntityOcelot(this.world);
      if (this.isTamed()) {
         ☃.setOwnerId(this.getOwnerId());
         ☃.setTamed(true);
         ☃.setTameSkin(this.getTameSkin());
      }

      return ☃;
   }

   @Override
   public boolean isBreedingItem(ItemStack var1) {
      return ☃.getItem() == Items.FISH;
   }

   @Override
   public boolean canMateWith(EntityAnimal var1) {
      if (☃ == this) {
         return false;
      } else if (!this.isTamed()) {
         return false;
      } else if (!(☃ instanceof EntityOcelot)) {
         return false;
      } else {
         EntityOcelot ☃ = (EntityOcelot)☃;
         return !☃.isTamed() ? false : this.isInLove() && ☃.isInLove();
      }
   }

   public int getTameSkin() {
      return this.dataManager.get(OCELOT_VARIANT);
   }

   public void setTameSkin(int var1) {
      this.dataManager.set(OCELOT_VARIANT, ☃);
   }

   @Override
   public boolean getCanSpawnHere() {
      return this.world.rand.nextInt(3) != 0;
   }

   @Override
   public boolean isNotColliding() {
      if (this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this)
         && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()
         && !this.world.containsAnyLiquid(this.getEntityBoundingBox())) {
         BlockPos ☃ = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
         if (☃.getY() < this.world.getSeaLevel()) {
            return false;
         }

         IBlockState ☃x = this.world.getBlockState(☃.down());
         Block ☃xx = ☃x.getBlock();
         if (☃xx == Blocks.GRASS || ☃x.getMaterial() == Material.LEAVES) {
            return true;
         }
      }

      return false;
   }

   @Override
   public String getName() {
      if (this.hasCustomName()) {
         return this.getCustomNameTag();
      } else {
         return this.isTamed() ? I18n.translateToLocal("entity.Cat.name") : super.getName();
      }
   }

   @Override
   protected void setupTamedAI() {
      if (this.avoidEntity == null) {
         this.avoidEntity = new EntityAIAvoidEntity<>(this, EntityPlayer.class, 16.0F, 0.8, 1.33);
      }

      this.tasks.removeTask(this.avoidEntity);
      if (!this.isTamed()) {
         this.tasks.addTask(4, this.avoidEntity);
      }
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      ☃ = super.onInitialSpawn(☃, ☃);
      if (this.getTameSkin() == 0 && this.world.rand.nextInt(7) == 0) {
         for (int ☃ = 0; ☃ < 2; ☃++) {
            EntityOcelot ☃x = new EntityOcelot(this.world);
            ☃x.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            ☃x.setGrowingAge(-24000);
            this.world.spawnEntity(☃x);
         }
      }

      return ☃;
   }
}
