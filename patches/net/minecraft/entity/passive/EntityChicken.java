package net.minecraft.entity.passive;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityChicken extends EntityAnimal {
   private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(
      new Item[]{Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS}
   );
   public float wingRotation;
   public float destPos;
   public float oFlapSpeed;
   public float oFlap;
   public float wingRotDelta = 1.0F;
   public int timeUntilNextEgg;
   public boolean chickenJockey;

   public EntityChicken(World var1) {
      super(☃);
      this.setSize(0.4F, 0.7F);
      this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
      this.setPathPriority(PathNodeType.WATER, 0.0F);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIPanic(this, 1.4));
      this.tasks.addTask(2, new EntityAIMate(this, 1.0));
      this.tasks.addTask(3, new EntityAITempt(this, 1.0, false, TEMPTATION_ITEMS));
      this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1));
      this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(7, new EntityAILookIdle(this));
   }

   @Override
   public float getEyeHeight() {
      return this.height;
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      this.oFlap = this.wingRotation;
      this.oFlapSpeed = this.destPos;
      this.destPos = (float)(this.destPos + (this.onGround ? -1 : 4) * 0.3);
      this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);
      if (!this.onGround && this.wingRotDelta < 1.0F) {
         this.wingRotDelta = 1.0F;
      }

      this.wingRotDelta = (float)(this.wingRotDelta * 0.9);
      if (!this.onGround && this.motionY < 0.0) {
         this.motionY *= 0.6;
      }

      this.wingRotation = this.wingRotation + this.wingRotDelta * 2.0F;
      if (!this.world.isRemote && !this.isChild() && !this.isChickenJockey() && --this.timeUntilNextEgg <= 0) {
         this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         this.dropItem(Items.EGG, 1);
         this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
      }
   }

   @Override
   public void fall(float var1, float var2) {
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_CHICKEN_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_CHICKEN_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_CHICKEN_DEATH;
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_CHICKEN;
   }

   public EntityChicken createChild(EntityAgeable var1) {
      return new EntityChicken(this.world);
   }

   @Override
   public boolean isBreedingItem(ItemStack var1) {
      return TEMPTATION_ITEMS.contains(☃.getItem());
   }

   @Override
   protected int getExperiencePoints(EntityPlayer var1) {
      return this.isChickenJockey() ? 10 : super.getExperiencePoints(☃);
   }

   public static void registerFixesChicken(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityChicken.class);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.chickenJockey = ☃.getBoolean("IsChickenJockey");
      if (☃.hasKey("EggLayTime")) {
         this.timeUntilNextEgg = ☃.getInteger("EggLayTime");
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setBoolean("IsChickenJockey", this.chickenJockey);
      ☃.setInteger("EggLayTime", this.timeUntilNextEgg);
   }

   @Override
   protected boolean canDespawn() {
      return this.isChickenJockey() && !this.isBeingRidden();
   }

   @Override
   public void updatePassenger(Entity var1) {
      super.updatePassenger(☃);
      float ☃ = MathHelper.sin(this.renderYawOffset * (float) (Math.PI / 180.0));
      float ☃x = MathHelper.cos(this.renderYawOffset * (float) (Math.PI / 180.0));
      float ☃xx = 0.1F;
      float ☃xxx = 0.0F;
      ☃.setPosition(this.posX + 0.1F * ☃, this.posY + this.height * 0.5F + ☃.getYOffset() + 0.0, this.posZ - 0.1F * ☃x);
      if (☃ instanceof EntityLivingBase) {
         ((EntityLivingBase)☃).renderYawOffset = this.renderYawOffset;
      }
   }

   public boolean isChickenJockey() {
      return this.chickenJockey;
   }

   public void setChickenJockey(boolean var1) {
      this.chickenJockey = ☃;
   }
}
