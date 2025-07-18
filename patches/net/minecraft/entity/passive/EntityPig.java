package net.minecraft.entity.passive;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityPig extends EntityAnimal {
   private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(EntityPig.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Integer> BOOST_TIME = EntityDataManager.createKey(EntityPig.class, DataSerializers.VARINT);
   private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(new Item[]{Items.CARROT, Items.POTATO, Items.BEETROOT});
   private boolean boosting;
   private int boostTime;
   private int totalBoostTime;

   public EntityPig(World var1) {
      super(☃);
      this.setSize(0.9F, 0.9F);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIPanic(this, 1.25));
      this.tasks.addTask(3, new EntityAIMate(this, 1.0));
      this.tasks.addTask(4, new EntityAITempt(this, 1.2, Items.CARROT_ON_A_STICK, false));
      this.tasks.addTask(4, new EntityAITempt(this, 1.2, false, TEMPTATION_ITEMS));
      this.tasks.addTask(5, new EntityAIFollowParent(this, 1.1));
      this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
   }

   @Nullable
   @Override
   public Entity getControllingPassenger() {
      return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
   }

   @Override
   public boolean canBeSteered() {
      Entity ☃ = this.getControllingPassenger();
      if (!(☃ instanceof EntityPlayer)) {
         return false;
      } else {
         EntityPlayer ☃x = (EntityPlayer)☃;
         return ☃x.getHeldItemMainhand().getItem() == Items.CARROT_ON_A_STICK || ☃x.getHeldItemOffhand().getItem() == Items.CARROT_ON_A_STICK;
      }
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      if (BOOST_TIME.equals(☃) && this.world.isRemote) {
         this.boosting = true;
         this.boostTime = 0;
         this.totalBoostTime = this.dataManager.get(BOOST_TIME);
      }

      super.notifyDataManagerChange(☃);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(SADDLED, false);
      this.dataManager.register(BOOST_TIME, 0);
   }

   public static void registerFixesPig(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityPig.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setBoolean("Saddle", this.getSaddled());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setSaddled(☃.getBoolean("Saddle"));
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_PIG_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_PIG_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_PIG_DEATH;
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      if (!super.processInteract(☃, ☃)) {
         ItemStack ☃ = ☃.getHeldItem(☃);
         if (☃.getItem() == Items.NAME_TAG) {
            ☃.interactWithEntity(☃, this, ☃);
            return true;
         } else if (this.getSaddled() && !this.isBeingRidden()) {
            if (!this.world.isRemote) {
               ☃.startRiding(this);
            }

            return true;
         } else if (☃.getItem() == Items.SADDLE) {
            ☃.interactWithEntity(☃, this, ☃);
            return true;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   @Override
   public void onDeath(DamageSource var1) {
      super.onDeath(☃);
      if (!this.world.isRemote) {
         if (this.getSaddled()) {
            this.dropItem(Items.SADDLE, 1);
         }
      }
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_PIG;
   }

   public boolean getSaddled() {
      return this.dataManager.get(SADDLED);
   }

   public void setSaddled(boolean var1) {
      if (☃) {
         this.dataManager.set(SADDLED, true);
      } else {
         this.dataManager.set(SADDLED, false);
      }
   }

   @Override
   public void onStruckByLightning(EntityLightningBolt var1) {
      if (!this.world.isRemote && !this.isDead) {
         EntityPigZombie ☃ = new EntityPigZombie(this.world);
         ☃.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
         ☃.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
         ☃.setNoAI(this.isAIDisabled());
         if (this.hasCustomName()) {
            ☃.setCustomNameTag(this.getCustomNameTag());
            ☃.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
         }

         this.world.spawnEntity(☃);
         this.setDead();
      }
   }

   @Override
   public void travel(float var1, float var2, float var3) {
      Entity ☃ = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
      if (this.isBeingRidden() && this.canBeSteered()) {
         this.rotationYaw = ☃.rotationYaw;
         this.prevRotationYaw = this.rotationYaw;
         this.rotationPitch = ☃.rotationPitch * 0.5F;
         this.setRotation(this.rotationYaw, this.rotationPitch);
         this.renderYawOffset = this.rotationYaw;
         this.rotationYawHead = this.rotationYaw;
         this.stepHeight = 1.0F;
         this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
         if (this.boosting && this.boostTime++ > this.totalBoostTime) {
            this.boosting = false;
         }

         if (this.canPassengerSteer()) {
            float ☃x = (float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.225F;
            if (this.boosting) {
               ☃x += ☃x * 1.15F * MathHelper.sin((float)this.boostTime / this.totalBoostTime * (float) Math.PI);
            }

            this.setAIMoveSpeed(☃x);
            super.travel(0.0F, 0.0F, 1.0F);
         } else {
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
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
         this.stepHeight = 0.5F;
         this.jumpMovementFactor = 0.02F;
         super.travel(☃, ☃, ☃);
      }
   }

   public boolean boost() {
      if (this.boosting) {
         return false;
      } else {
         this.boosting = true;
         this.boostTime = 0;
         this.totalBoostTime = this.getRNG().nextInt(841) + 140;
         this.getDataManager().set(BOOST_TIME, this.totalBoostTime);
         return true;
      }
   }

   public EntityPig createChild(EntityAgeable var1) {
      return new EntityPig(this.world);
   }

   @Override
   public boolean isBreedingItem(ItemStack var1) {
      return TEMPTATION_ITEMS.contains(☃.getItem());
   }
}
