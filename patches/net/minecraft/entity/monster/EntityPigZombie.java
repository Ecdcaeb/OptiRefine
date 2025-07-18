package net.minecraft.entity.monster;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityPigZombie extends EntityZombie {
   private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
   private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER = new AttributeModifier(
         ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.05, 0
      )
      .setSaved(false);
   private int angerLevel;
   private int randomSoundDelay;
   private UUID angerTargetUUID;

   public EntityPigZombie(World var1) {
      super(☃);
      this.isImmuneToFire = true;
   }

   @Override
   public void setRevengeTarget(@Nullable EntityLivingBase var1) {
      super.setRevengeTarget(☃);
      if (☃ != null) {
         this.angerTargetUUID = ☃.getUniqueID();
      }
   }

   @Override
   protected void applyEntityAI() {
      this.targetTasks.addTask(1, new EntityPigZombie.AIHurtByAggressor(this));
      this.targetTasks.addTask(2, new EntityPigZombie.AITargetAggressor(this));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23F);
      this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0);
   }

   @Override
   protected void updateAITasks() {
      IAttributeInstance ☃ = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
      if (this.isAngry()) {
         if (!this.isChild() && !☃.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
            ☃.applyModifier(ATTACK_SPEED_BOOST_MODIFIER);
         }

         this.angerLevel--;
      } else if (☃.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
         ☃.removeModifier(ATTACK_SPEED_BOOST_MODIFIER);
      }

      if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0) {
         this.playSound(
            SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY, this.getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F
         );
      }

      if (this.angerLevel > 0 && this.angerTargetUUID != null && this.getRevengeTarget() == null) {
         EntityPlayer ☃x = this.world.getPlayerEntityByUUID(this.angerTargetUUID);
         this.setRevengeTarget(☃x);
         this.attackingPlayer = ☃x;
         this.recentlyHit = this.getRevengeTimer();
      }

      super.updateAITasks();
   }

   @Override
   public boolean getCanSpawnHere() {
      return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
   }

   @Override
   public boolean isNotColliding() {
      return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this)
         && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()
         && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
   }

   public static void registerFixesPigZombie(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityPigZombie.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setShort("Anger", (short)this.angerLevel);
      if (this.angerTargetUUID != null) {
         ☃.setString("HurtBy", this.angerTargetUUID.toString());
      } else {
         ☃.setString("HurtBy", "");
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.angerLevel = ☃.getShort("Anger");
      String ☃ = ☃.getString("HurtBy");
      if (!☃.isEmpty()) {
         this.angerTargetUUID = UUID.fromString(☃);
         EntityPlayer ☃x = this.world.getPlayerEntityByUUID(this.angerTargetUUID);
         this.setRevengeTarget(☃x);
         if (☃x != null) {
            this.attackingPlayer = ☃x;
            this.recentlyHit = this.getRevengeTimer();
         }
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else {
         Entity ☃ = ☃.getTrueSource();
         if (☃ instanceof EntityPlayer) {
            this.becomeAngryAt(☃);
         }

         return super.attackEntityFrom(☃, ☃);
      }
   }

   private void becomeAngryAt(Entity var1) {
      this.angerLevel = 400 + this.rand.nextInt(400);
      this.randomSoundDelay = this.rand.nextInt(40);
      if (☃ instanceof EntityLivingBase) {
         this.setRevengeTarget((EntityLivingBase)☃);
      }
   }

   public boolean isAngry() {
      return this.angerLevel > 0;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_ZOMBIE_PIG_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_ZOMBIE_PIG_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_ZOMBIE_PIG_DEATH;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_ZOMBIE_PIGMAN;
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      return false;
   }

   @Override
   protected void setEquipmentBasedOnDifficulty(DifficultyInstance var1) {
      this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
   }

   @Override
   protected ItemStack getSkullDrop() {
      return ItemStack.EMPTY;
   }

   @Override
   public boolean isPreventingPlayerRest(EntityPlayer var1) {
      return this.isAngry();
   }

   static class AIHurtByAggressor extends EntityAIHurtByTarget {
      public AIHurtByAggressor(EntityPigZombie var1) {
         super(☃, true);
      }

      @Override
      protected void setEntityAttackTarget(EntityCreature var1, EntityLivingBase var2) {
         super.setEntityAttackTarget(☃, ☃);
         if (☃ instanceof EntityPigZombie) {
            ((EntityPigZombie)☃).becomeAngryAt(☃);
         }
      }
   }

   static class AITargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer> {
      public AITargetAggressor(EntityPigZombie var1) {
         super(☃, EntityPlayer.class, true);
      }

      @Override
      public boolean shouldExecute() {
         return ((EntityPigZombie)this.taskOwner).isAngry() && super.shouldExecute();
      }
   }
}
