package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityVindicator extends AbstractIllager {
   private boolean johnny;
   private static final Predicate<Entity> JOHNNY_SELECTOR = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity var1) {
         return ☃ instanceof EntityLivingBase && ((EntityLivingBase)☃).attackable();
      }
   };

   public EntityVindicator(World var1) {
      super(☃);
      this.setSize(0.6F, 1.95F);
   }

   public static void registerFixesVindicator(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityVindicator.class);
   }

   @Override
   protected void initEntityAI() {
      super.initEntityAI();
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0, false));
      this.tasks.addTask(8, new EntityAIWander(this, 0.6));
      this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
      this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityVindicator.class));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityVillager.class, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityIronGolem.class, true));
      this.targetTasks.addTask(4, new EntityVindicator.AIJohnnyAttack(this));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35F);
      this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0);
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0);
      this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
   }

   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_VINDICATION_ILLAGER;
   }

   public boolean isAggressive() {
      return this.isAggressive(1);
   }

   public void setAggressive(boolean var1) {
      this.setAggressive(1, ☃);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      if (this.johnny) {
         ☃.setBoolean("Johnny", true);
      }
   }

   @Override
   public AbstractIllager.IllagerArmPose getArmPose() {
      return this.isAggressive() ? AbstractIllager.IllagerArmPose.ATTACKING : AbstractIllager.IllagerArmPose.CROSSED;
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      if (☃.hasKey("Johnny", 99)) {
         this.johnny = ☃.getBoolean("Johnny");
      }
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      IEntityLivingData ☃ = super.onInitialSpawn(☃, ☃);
      this.setEquipmentBasedOnDifficulty(☃);
      this.setEnchantmentBasedOnDifficulty(☃);
      return ☃;
   }

   @Override
   protected void setEquipmentBasedOnDifficulty(DifficultyInstance var1) {
      this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
   }

   @Override
   protected void updateAITasks() {
      super.updateAITasks();
      this.setAggressive(this.getAttackTarget() != null);
   }

   @Override
   public boolean isOnSameTeam(Entity var1) {
      if (super.isOnSameTeam(☃)) {
         return true;
      } else {
         return ☃ instanceof EntityLivingBase && ((EntityLivingBase)☃).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER
            ? this.getTeam() == null && ☃.getTeam() == null
            : false;
      }
   }

   @Override
   public void setCustomNameTag(String var1) {
      super.setCustomNameTag(☃);
      if (!this.johnny && "Johnny".equals(☃)) {
         this.johnny = true;
      }
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.VINDICATION_ILLAGER_AMBIENT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.VINDICATION_ILLAGER_DEATH;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_VINDICATION_ILLAGER_HURT;
   }

   static class AIJohnnyAttack extends EntityAINearestAttackableTarget<EntityLivingBase> {
      public AIJohnnyAttack(EntityVindicator var1) {
         super(☃, EntityLivingBase.class, 0, true, true, EntityVindicator.JOHNNY_SELECTOR);
      }

      @Override
      public boolean shouldExecute() {
         return ((EntityVindicator)this.taskOwner).johnny && super.shouldExecute();
      }
   }
}
