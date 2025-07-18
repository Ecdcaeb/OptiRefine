package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIDefendVillage;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookAtVillager;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityIronGolem extends EntityGolem {
   protected static final DataParameter<Byte> PLAYER_CREATED = EntityDataManager.createKey(EntityIronGolem.class, DataSerializers.BYTE);
   private int homeCheckTimer;
   @Nullable
   Village village;
   private int attackTimer;
   private int holdRoseTick;

   public EntityIronGolem(World var1) {
      super(☃);
      this.setSize(1.4F, 2.7F);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0, true));
      this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9, 32.0F));
      this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6, true));
      this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0));
      this.tasks.addTask(5, new EntityAILookAtVillager(this));
      this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIDefendVillage(this));
      this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityLiving.class, 10, false, true, new Predicate<EntityLiving>() {
         public boolean apply(@Nullable EntityLiving var1) {
            return ☃ != null && IMob.VISIBLE_MOB_SELECTOR.apply(☃) && !(☃ instanceof EntityCreeper);
         }
      }));
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(PLAYER_CREATED, (byte)0);
   }

   @Override
   protected void updateAITasks() {
      if (--this.homeCheckTimer <= 0) {
         this.homeCheckTimer = 70 + this.rand.nextInt(50);
         this.village = this.world.getVillageCollection().getNearestVillage(new BlockPos(this), 32);
         if (this.village == null) {
            this.detachHome();
         } else {
            BlockPos ☃ = this.village.getCenter();
            this.setHomePosAndDistance(☃, (int)(this.village.getVillageRadius() * 0.6F));
         }
      }

      super.updateAITasks();
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
      this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
   }

   @Override
   protected int decreaseAirSupply(int var1) {
      return ☃;
   }

   @Override
   protected void collideWithEntity(Entity var1) {
      if (☃ instanceof IMob && !(☃ instanceof EntityCreeper) && this.getRNG().nextInt(20) == 0) {
         this.setAttackTarget((EntityLivingBase)☃);
      }

      super.collideWithEntity(☃);
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.attackTimer > 0) {
         this.attackTimer--;
      }

      if (this.holdRoseTick > 0) {
         this.holdRoseTick--;
      }

      if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.5000003E-7F && this.rand.nextInt(5) == 0) {
         int ☃ = MathHelper.floor(this.posX);
         int ☃x = MathHelper.floor(this.posY - 0.2F);
         int ☃xx = MathHelper.floor(this.posZ);
         IBlockState ☃xxx = this.world.getBlockState(new BlockPos(☃, ☃x, ☃xx));
         if (☃xxx.getMaterial() != Material.AIR) {
            this.world
               .spawnParticle(
                  EnumParticleTypes.BLOCK_CRACK,
                  this.posX + (this.rand.nextFloat() - 0.5) * this.width,
                  this.getEntityBoundingBox().minY + 0.1,
                  this.posZ + (this.rand.nextFloat() - 0.5) * this.width,
                  4.0 * (this.rand.nextFloat() - 0.5),
                  0.5,
                  (this.rand.nextFloat() - 0.5) * 4.0,
                  Block.getStateId(☃xxx)
               );
         }
      }
   }

   @Override
   public boolean canAttackClass(Class<? extends EntityLivingBase> var1) {
      if (this.isPlayerCreated() && EntityPlayer.class.isAssignableFrom(☃)) {
         return false;
      } else {
         return ☃ == EntityCreeper.class ? false : super.canAttackClass(☃);
      }
   }

   public static void registerFixesIronGolem(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityIronGolem.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setBoolean("PlayerCreated", this.isPlayerCreated());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setPlayerCreated(☃.getBoolean("PlayerCreated"));
   }

   @Override
   public boolean attackEntityAsMob(Entity var1) {
      this.attackTimer = 10;
      this.world.setEntityState(this, (byte)4);
      boolean ☃ = ☃.attackEntityFrom(DamageSource.causeMobDamage(this), 7 + this.rand.nextInt(15));
      if (☃) {
         ☃.motionY += 0.4F;
         this.applyEnchantments(this, ☃);
      }

      this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
      return ☃;
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 4) {
         this.attackTimer = 10;
         this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
      } else if (☃ == 11) {
         this.holdRoseTick = 400;
      } else if (☃ == 34) {
         this.holdRoseTick = 0;
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   public Village getVillage() {
      return this.village;
   }

   public int getAttackTimer() {
      return this.attackTimer;
   }

   public void setHoldingRose(boolean var1) {
      if (☃) {
         this.holdRoseTick = 400;
         this.world.setEntityState(this, (byte)11);
      } else {
         this.holdRoseTick = 0;
         this.world.setEntityState(this, (byte)34);
      }
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_IRONGOLEM_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_IRONGOLEM_DEATH;
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_IRON_GOLEM;
   }

   public int getHoldRoseTick() {
      return this.holdRoseTick;
   }

   public boolean isPlayerCreated() {
      return (this.dataManager.get(PLAYER_CREATED) & 1) != 0;
   }

   public void setPlayerCreated(boolean var1) {
      byte ☃ = this.dataManager.get(PLAYER_CREATED);
      if (☃) {
         this.dataManager.set(PLAYER_CREATED, (byte)(☃ | 1));
      } else {
         this.dataManager.set(PLAYER_CREATED, (byte)(☃ & -2));
      }
   }

   @Override
   public void onDeath(DamageSource var1) {
      if (!this.isPlayerCreated() && this.attackingPlayer != null && this.village != null) {
         this.village.modifyPlayerReputation(this.attackingPlayer.getName(), -5);
      }

      super.onDeath(☃);
   }
}
