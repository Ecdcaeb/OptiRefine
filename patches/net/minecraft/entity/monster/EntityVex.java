package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityVex extends EntityMob {
   protected static final DataParameter<Byte> VEX_FLAGS = EntityDataManager.createKey(EntityVex.class, DataSerializers.BYTE);
   private EntityLiving owner;
   @Nullable
   private BlockPos boundOrigin;
   private boolean limitedLifespan;
   private int limitedLifeTicks;

   public EntityVex(World var1) {
      super(☃);
      this.isImmuneToFire = true;
      this.moveHelper = new EntityVex.AIMoveControl(this);
      this.setSize(0.4F, 0.8F);
      this.experienceValue = 3;
   }

   @Override
   public void move(MoverType var1, double var2, double var4, double var6) {
      super.move(☃, ☃, ☃, ☃);
      this.doBlockCollisions();
   }

   @Override
   public void onUpdate() {
      this.noClip = true;
      super.onUpdate();
      this.noClip = false;
      this.setNoGravity(true);
      if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
         this.limitedLifeTicks = 20;
         this.attackEntityFrom(DamageSource.STARVE, 1.0F);
      }
   }

   @Override
   protected void initEntityAI() {
      super.initEntityAI();
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(4, new EntityVex.AIChargeAttack());
      this.tasks.addTask(8, new EntityVex.AIMoveRandom());
      this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
      this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityVex.class));
      this.targetTasks.addTask(2, new EntityVex.AICopyOwnerTarget(this));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0);
      this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(VEX_FLAGS, (byte)0);
   }

   public static void registerFixesVex(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityVex.class);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      if (☃.hasKey("BoundX")) {
         this.boundOrigin = new BlockPos(☃.getInteger("BoundX"), ☃.getInteger("BoundY"), ☃.getInteger("BoundZ"));
      }

      if (☃.hasKey("LifeTicks")) {
         this.setLimitedLife(☃.getInteger("LifeTicks"));
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      if (this.boundOrigin != null) {
         ☃.setInteger("BoundX", this.boundOrigin.getX());
         ☃.setInteger("BoundY", this.boundOrigin.getY());
         ☃.setInteger("BoundZ", this.boundOrigin.getZ());
      }

      if (this.limitedLifespan) {
         ☃.setInteger("LifeTicks", this.limitedLifeTicks);
      }
   }

   public EntityLiving getOwner() {
      return this.owner;
   }

   @Nullable
   public BlockPos getBoundOrigin() {
      return this.boundOrigin;
   }

   public void setBoundOrigin(@Nullable BlockPos var1) {
      this.boundOrigin = ☃;
   }

   private boolean getVexFlag(int var1) {
      int ☃ = this.dataManager.get(VEX_FLAGS);
      return (☃ & ☃) != 0;
   }

   private void setVexFlag(int var1, boolean var2) {
      int ☃ = this.dataManager.get(VEX_FLAGS);
      if (☃) {
         ☃ |= ☃;
      } else {
         ☃ &= ~☃;
      }

      this.dataManager.set(VEX_FLAGS, (byte)(☃ & 0xFF));
   }

   public boolean isCharging() {
      return this.getVexFlag(1);
   }

   public void setCharging(boolean var1) {
      this.setVexFlag(1, ☃);
   }

   public void setOwner(EntityLiving var1) {
      this.owner = ☃;
   }

   public void setLimitedLife(int var1) {
      this.limitedLifespan = true;
      this.limitedLifeTicks = ☃;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_VEX_AMBIENT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_VEX_DEATH;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_VEX_HURT;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_VEX;
   }

   @Override
   public int getBrightnessForRender() {
      return 15728880;
   }

   @Override
   public float getBrightness() {
      return 1.0F;
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      this.setEquipmentBasedOnDifficulty(☃);
      this.setEnchantmentBasedOnDifficulty(☃);
      return super.onInitialSpawn(☃, ☃);
   }

   @Override
   protected void setEquipmentBasedOnDifficulty(DifficultyInstance var1) {
      this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
      this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
   }

   class AIChargeAttack extends EntityAIBase {
      public AIChargeAttack() {
         this.setMutexBits(1);
      }

      @Override
      public boolean shouldExecute() {
         return EntityVex.this.getAttackTarget() != null && !EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.rand.nextInt(7) == 0
            ? EntityVex.this.getDistanceSq(EntityVex.this.getAttackTarget()) > 4.0
            : false;
      }

      @Override
      public boolean shouldContinueExecuting() {
         return EntityVex.this.getMoveHelper().isUpdating()
            && EntityVex.this.isCharging()
            && EntityVex.this.getAttackTarget() != null
            && EntityVex.this.getAttackTarget().isEntityAlive();
      }

      @Override
      public void startExecuting() {
         EntityLivingBase ☃ = EntityVex.this.getAttackTarget();
         Vec3d ☃x = ☃.getPositionEyes(1.0F);
         EntityVex.this.moveHelper.setMoveTo(☃x.x, ☃x.y, ☃x.z, 1.0);
         EntityVex.this.setCharging(true);
         EntityVex.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
      }

      @Override
      public void resetTask() {
         EntityVex.this.setCharging(false);
      }

      @Override
      public void updateTask() {
         EntityLivingBase ☃ = EntityVex.this.getAttackTarget();
         if (EntityVex.this.getEntityBoundingBox().intersects(☃.getEntityBoundingBox())) {
            EntityVex.this.attackEntityAsMob(☃);
            EntityVex.this.setCharging(false);
         } else {
            double ☃x = EntityVex.this.getDistanceSq(☃);
            if (☃x < 9.0) {
               Vec3d ☃xx = ☃.getPositionEyes(1.0F);
               EntityVex.this.moveHelper.setMoveTo(☃xx.x, ☃xx.y, ☃xx.z, 1.0);
            }
         }
      }
   }

   class AICopyOwnerTarget extends EntityAITarget {
      public AICopyOwnerTarget(EntityCreature var2) {
         super(☃, false);
      }

      @Override
      public boolean shouldExecute() {
         return EntityVex.this.owner != null
            && EntityVex.this.owner.getAttackTarget() != null
            && this.isSuitableTarget(EntityVex.this.owner.getAttackTarget(), false);
      }

      @Override
      public void startExecuting() {
         EntityVex.this.setAttackTarget(EntityVex.this.owner.getAttackTarget());
         super.startExecuting();
      }
   }

   class AIMoveControl extends EntityMoveHelper {
      public AIMoveControl(EntityVex var2) {
         super(☃);
      }

      @Override
      public void onUpdateMoveHelper() {
         if (this.action == EntityMoveHelper.Action.MOVE_TO) {
            double ☃ = this.posX - EntityVex.this.posX;
            double ☃x = this.posY - EntityVex.this.posY;
            double ☃xx = this.posZ - EntityVex.this.posZ;
            double ☃xxx = ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
            ☃xxx = MathHelper.sqrt(☃xxx);
            if (☃xxx < EntityVex.this.getEntityBoundingBox().getAverageEdgeLength()) {
               this.action = EntityMoveHelper.Action.WAIT;
               EntityVex.this.motionX *= 0.5;
               EntityVex.this.motionY *= 0.5;
               EntityVex.this.motionZ *= 0.5;
            } else {
               EntityVex.this.motionX = EntityVex.this.motionX + ☃ / ☃xxx * 0.05 * this.speed;
               EntityVex.this.motionY = EntityVex.this.motionY + ☃x / ☃xxx * 0.05 * this.speed;
               EntityVex.this.motionZ = EntityVex.this.motionZ + ☃xx / ☃xxx * 0.05 * this.speed;
               if (EntityVex.this.getAttackTarget() == null) {
                  EntityVex.this.rotationYaw = -((float)MathHelper.atan2(EntityVex.this.motionX, EntityVex.this.motionZ)) * (180.0F / (float)Math.PI);
                  EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
               } else {
                  double ☃xxxx = EntityVex.this.getAttackTarget().posX - EntityVex.this.posX;
                  double ☃xxxxx = EntityVex.this.getAttackTarget().posZ - EntityVex.this.posZ;
                  EntityVex.this.rotationYaw = -((float)MathHelper.atan2(☃xxxx, ☃xxxxx)) * (180.0F / (float)Math.PI);
                  EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
               }
            }
         }
      }
   }

   class AIMoveRandom extends EntityAIBase {
      public AIMoveRandom() {
         this.setMutexBits(1);
      }

      @Override
      public boolean shouldExecute() {
         return !EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.rand.nextInt(7) == 0;
      }

      @Override
      public boolean shouldContinueExecuting() {
         return false;
      }

      @Override
      public void updateTask() {
         BlockPos ☃ = EntityVex.this.getBoundOrigin();
         if (☃ == null) {
            ☃ = new BlockPos(EntityVex.this);
         }

         for (int ☃x = 0; ☃x < 3; ☃x++) {
            BlockPos ☃xx = ☃.add(EntityVex.this.rand.nextInt(15) - 7, EntityVex.this.rand.nextInt(11) - 5, EntityVex.this.rand.nextInt(15) - 7);
            if (EntityVex.this.world.isAirBlock(☃xx)) {
               EntityVex.this.moveHelper.setMoveTo(☃xx.getX() + 0.5, ☃xx.getY() + 0.5, ☃xx.getZ() + 0.5, 0.25);
               if (EntityVex.this.getAttackTarget() == null) {
                  EntityVex.this.getLookHelper().setLookPosition(☃xx.getX() + 0.5, ☃xx.getY() + 0.5, ☃xx.getZ() + 0.5, 180.0F, 20.0F);
               }
               break;
            }
         }
      }
   }
}
