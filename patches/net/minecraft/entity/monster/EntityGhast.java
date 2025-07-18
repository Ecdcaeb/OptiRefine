package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGhast extends EntityFlying implements IMob {
   private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(EntityGhast.class, DataSerializers.BOOLEAN);
   private int explosionStrength = 1;

   public EntityGhast(World var1) {
      super(☃);
      this.setSize(4.0F, 4.0F);
      this.isImmuneToFire = true;
      this.experienceValue = 5;
      this.moveHelper = new EntityGhast.GhastMoveHelper(this);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(5, new EntityGhast.AIRandomFly(this));
      this.tasks.addTask(7, new EntityGhast.AILookAround(this));
      this.tasks.addTask(7, new EntityGhast.AIFireballAttack(this));
      this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
   }

   public boolean isAttacking() {
      return this.dataManager.get(ATTACKING);
   }

   public void setAttacking(boolean var1) {
      this.dataManager.set(ATTACKING, ☃);
   }

   public int getFireballStrength() {
      return this.explosionStrength;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
         this.setDead();
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else if (☃.getImmediateSource() instanceof EntityLargeFireball && ☃.getTrueSource() instanceof EntityPlayer) {
         super.attackEntityFrom(☃, 1000.0F);
         return true;
      } else {
         return super.attackEntityFrom(☃, ☃);
      }
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(ATTACKING, false);
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
      this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0);
   }

   @Override
   public SoundCategory getSoundCategory() {
      return SoundCategory.HOSTILE;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_GHAST_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_GHAST_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_GHAST_DEATH;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_GHAST;
   }

   @Override
   protected float getSoundVolume() {
      return 10.0F;
   }

   @Override
   public boolean getCanSpawnHere() {
      return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
   }

   @Override
   public int getMaxSpawnedInChunk() {
      return 1;
   }

   public static void registerFixesGhast(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityGhast.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("ExplosionPower", this.explosionStrength);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      if (☃.hasKey("ExplosionPower", 99)) {
         this.explosionStrength = ☃.getInteger("ExplosionPower");
      }
   }

   @Override
   public float getEyeHeight() {
      return 2.6F;
   }

   static class AIFireballAttack extends EntityAIBase {
      private final EntityGhast parentEntity;
      public int attackTimer;

      public AIFireballAttack(EntityGhast var1) {
         this.parentEntity = ☃;
      }

      @Override
      public boolean shouldExecute() {
         return this.parentEntity.getAttackTarget() != null;
      }

      @Override
      public void startExecuting() {
         this.attackTimer = 0;
      }

      @Override
      public void resetTask() {
         this.parentEntity.setAttacking(false);
      }

      @Override
      public void updateTask() {
         EntityLivingBase ☃ = this.parentEntity.getAttackTarget();
         double ☃x = 64.0;
         if (☃.getDistanceSq(this.parentEntity) < 4096.0 && this.parentEntity.canEntityBeSeen(☃)) {
            World ☃xx = this.parentEntity.world;
            this.attackTimer++;
            if (this.attackTimer == 10) {
               ☃xx.playEvent(null, 1015, new BlockPos(this.parentEntity), 0);
            }

            if (this.attackTimer == 20) {
               double ☃xxx = 4.0;
               Vec3d ☃xxxx = this.parentEntity.getLook(1.0F);
               double ☃xxxxx = ☃.posX - (this.parentEntity.posX + ☃xxxx.x * 4.0);
               double ☃xxxxxx = ☃.getEntityBoundingBox().minY + ☃.height / 2.0F - (0.5 + this.parentEntity.posY + this.parentEntity.height / 2.0F);
               double ☃xxxxxxx = ☃.posZ - (this.parentEntity.posZ + ☃xxxx.z * 4.0);
               ☃xx.playEvent(null, 1016, new BlockPos(this.parentEntity), 0);
               EntityLargeFireball ☃xxxxxxxx = new EntityLargeFireball(☃xx, this.parentEntity, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx);
               ☃xxxxxxxx.explosionPower = this.parentEntity.getFireballStrength();
               ☃xxxxxxxx.posX = this.parentEntity.posX + ☃xxxx.x * 4.0;
               ☃xxxxxxxx.posY = this.parentEntity.posY + this.parentEntity.height / 2.0F + 0.5;
               ☃xxxxxxxx.posZ = this.parentEntity.posZ + ☃xxxx.z * 4.0;
               ☃xx.spawnEntity(☃xxxxxxxx);
               this.attackTimer = -40;
            }
         } else if (this.attackTimer > 0) {
            this.attackTimer--;
         }

         this.parentEntity.setAttacking(this.attackTimer > 10);
      }
   }

   static class AILookAround extends EntityAIBase {
      private final EntityGhast parentEntity;

      public AILookAround(EntityGhast var1) {
         this.parentEntity = ☃;
         this.setMutexBits(2);
      }

      @Override
      public boolean shouldExecute() {
         return true;
      }

      @Override
      public void updateTask() {
         if (this.parentEntity.getAttackTarget() == null) {
            this.parentEntity.rotationYaw = -((float)MathHelper.atan2(this.parentEntity.motionX, this.parentEntity.motionZ)) * (180.0F / (float)Math.PI);
            this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
         } else {
            EntityLivingBase ☃ = this.parentEntity.getAttackTarget();
            double ☃x = 64.0;
            if (☃.getDistanceSq(this.parentEntity) < 4096.0) {
               double ☃xx = ☃.posX - this.parentEntity.posX;
               double ☃xxx = ☃.posZ - this.parentEntity.posZ;
               this.parentEntity.rotationYaw = -((float)MathHelper.atan2(☃xx, ☃xxx)) * (180.0F / (float)Math.PI);
               this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
            }
         }
      }
   }

   static class AIRandomFly extends EntityAIBase {
      private final EntityGhast parentEntity;

      public AIRandomFly(EntityGhast var1) {
         this.parentEntity = ☃;
         this.setMutexBits(1);
      }

      @Override
      public boolean shouldExecute() {
         EntityMoveHelper ☃ = this.parentEntity.getMoveHelper();
         if (!☃.isUpdating()) {
            return true;
         } else {
            double ☃x = ☃.getX() - this.parentEntity.posX;
            double ☃xx = ☃.getY() - this.parentEntity.posY;
            double ☃xxx = ☃.getZ() - this.parentEntity.posZ;
            double ☃xxxx = ☃x * ☃x + ☃xx * ☃xx + ☃xxx * ☃xxx;
            return ☃xxxx < 1.0 || ☃xxxx > 3600.0;
         }
      }

      @Override
      public boolean shouldContinueExecuting() {
         return false;
      }

      @Override
      public void startExecuting() {
         Random ☃ = this.parentEntity.getRNG();
         double ☃x = this.parentEntity.posX + (☃.nextFloat() * 2.0F - 1.0F) * 16.0F;
         double ☃xx = this.parentEntity.posY + (☃.nextFloat() * 2.0F - 1.0F) * 16.0F;
         double ☃xxx = this.parentEntity.posZ + (☃.nextFloat() * 2.0F - 1.0F) * 16.0F;
         this.parentEntity.getMoveHelper().setMoveTo(☃x, ☃xx, ☃xxx, 1.0);
      }
   }

   static class GhastMoveHelper extends EntityMoveHelper {
      private final EntityGhast parentEntity;
      private int courseChangeCooldown;

      public GhastMoveHelper(EntityGhast var1) {
         super(☃);
         this.parentEntity = ☃;
      }

      @Override
      public void onUpdateMoveHelper() {
         if (this.action == EntityMoveHelper.Action.MOVE_TO) {
            double ☃ = this.posX - this.parentEntity.posX;
            double ☃x = this.posY - this.parentEntity.posY;
            double ☃xx = this.posZ - this.parentEntity.posZ;
            double ☃xxx = ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
            if (this.courseChangeCooldown-- <= 0) {
               this.courseChangeCooldown = this.courseChangeCooldown + this.parentEntity.getRNG().nextInt(5) + 2;
               ☃xxx = MathHelper.sqrt(☃xxx);
               if (this.isNotColliding(this.posX, this.posY, this.posZ, ☃xxx)) {
                  this.parentEntity.motionX += ☃ / ☃xxx * 0.1;
                  this.parentEntity.motionY += ☃x / ☃xxx * 0.1;
                  this.parentEntity.motionZ += ☃xx / ☃xxx * 0.1;
               } else {
                  this.action = EntityMoveHelper.Action.WAIT;
               }
            }
         }
      }

      private boolean isNotColliding(double var1, double var3, double var5, double var7) {
         double ☃ = (☃ - this.parentEntity.posX) / ☃;
         double ☃x = (☃ - this.parentEntity.posY) / ☃;
         double ☃xx = (☃ - this.parentEntity.posZ) / ☃;
         AxisAlignedBB ☃xxx = this.parentEntity.getEntityBoundingBox();

         for (int ☃xxxx = 1; ☃xxxx < ☃; ☃xxxx++) {
            ☃xxx = ☃xxx.offset(☃, ☃x, ☃xx);
            if (!this.parentEntity.world.getCollisionBoxes(this.parentEntity, ☃xxx).isEmpty()) {
               return false;
            }
         }

         return true;
      }
   }
}
