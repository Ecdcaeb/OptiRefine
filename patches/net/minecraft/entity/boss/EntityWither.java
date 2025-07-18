package net.minecraft.entity.boss;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWither extends EntityMob implements IRangedAttackMob {
   private static final DataParameter<Integer> FIRST_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> SECOND_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> THIRD_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
   private static final DataParameter<Integer>[] HEAD_TARGETS = new DataParameter[]{FIRST_HEAD_TARGET, SECOND_HEAD_TARGET, THIRD_HEAD_TARGET};
   private static final DataParameter<Integer> INVULNERABILITY_TIME = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
   private final float[] xRotationHeads = new float[2];
   private final float[] yRotationHeads = new float[2];
   private final float[] xRotOHeads = new float[2];
   private final float[] yRotOHeads = new float[2];
   private final int[] nextHeadUpdate = new int[2];
   private final int[] idleHeadUpdates = new int[2];
   private int blockBreakCounter;
   private final BossInfoServer bossInfo = (BossInfoServer)new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)
      .setDarkenSky(true);
   private static final Predicate<Entity> NOT_UNDEAD = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity var1) {
         return ☃ instanceof EntityLivingBase
            && ((EntityLivingBase)☃).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD
            && ((EntityLivingBase)☃).attackable();
      }
   };

   public EntityWither(World var1) {
      super(☃);
      this.setHealth(this.getMaxHealth());
      this.setSize(0.9F, 3.5F);
      this.isImmuneToFire = true;
      ((PathNavigateGround)this.getNavigator()).setCanSwim(true);
      this.experienceValue = 50;
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(0, new EntityWither.AIDoNothing());
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0, 40, 20.0F));
      this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(7, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLiving.class, 0, false, false, NOT_UNDEAD));
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(FIRST_HEAD_TARGET, 0);
      this.dataManager.register(SECOND_HEAD_TARGET, 0);
      this.dataManager.register(THIRD_HEAD_TARGET, 0);
      this.dataManager.register(INVULNERABILITY_TIME, 0);
   }

   public static void registerFixesWither(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityWither.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("Invul", this.getInvulTime());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setInvulTime(☃.getInteger("Invul"));
      if (this.hasCustomName()) {
         this.bossInfo.setName(this.getDisplayName());
      }
   }

   @Override
   public void setCustomNameTag(String var1) {
      super.setCustomNameTag(☃);
      this.bossInfo.setName(this.getDisplayName());
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_WITHER_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_WITHER_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_WITHER_DEATH;
   }

   @Override
   public void onLivingUpdate() {
      this.motionY *= 0.6F;
      if (!this.world.isRemote && this.getWatchedTargetId(0) > 0) {
         Entity ☃ = this.world.getEntityByID(this.getWatchedTargetId(0));
         if (☃ != null) {
            if (this.posY < ☃.posY || !this.isArmored() && this.posY < ☃.posY + 5.0) {
               if (this.motionY < 0.0) {
                  this.motionY = 0.0;
               }

               this.motionY = this.motionY + (0.5 - this.motionY) * 0.6F;
            }

            double ☃x = ☃.posX - this.posX;
            double ☃xx = ☃.posZ - this.posZ;
            double ☃xxx = ☃x * ☃x + ☃xx * ☃xx;
            if (☃xxx > 9.0) {
               double ☃xxxx = MathHelper.sqrt(☃xxx);
               this.motionX = this.motionX + (☃x / ☃xxxx * 0.5 - this.motionX) * 0.6F;
               this.motionZ = this.motionZ + (☃xx / ☃xxxx * 0.5 - this.motionZ) * 0.6F;
            }
         }
      }

      if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05F) {
         this.rotationYaw = (float)MathHelper.atan2(this.motionZ, this.motionX) * (180.0F / (float)Math.PI) - 90.0F;
      }

      super.onLivingUpdate();

      for (int ☃ = 0; ☃ < 2; ☃++) {
         this.yRotOHeads[☃] = this.yRotationHeads[☃];
         this.xRotOHeads[☃] = this.xRotationHeads[☃];
      }

      for (int ☃ = 0; ☃ < 2; ☃++) {
         int ☃x = this.getWatchedTargetId(☃ + 1);
         Entity ☃xx = null;
         if (☃x > 0) {
            ☃xx = this.world.getEntityByID(☃x);
         }

         if (☃xx != null) {
            double ☃xxx = this.getHeadX(☃ + 1);
            double ☃xxxx = this.getHeadY(☃ + 1);
            double ☃xxxxx = this.getHeadZ(☃ + 1);
            double ☃xxxxxx = ☃xx.posX - ☃xxx;
            double ☃xxxxxxx = ☃xx.posY + ☃xx.getEyeHeight() - ☃xxxx;
            double ☃xxxxxxxx = ☃xx.posZ - ☃xxxxx;
            double ☃xxxxxxxxx = MathHelper.sqrt(☃xxxxxx * ☃xxxxxx + ☃xxxxxxxx * ☃xxxxxxxx);
            float ☃xxxxxxxxxx = (float)(MathHelper.atan2(☃xxxxxxxx, ☃xxxxxx) * 180.0F / (float)Math.PI) - 90.0F;
            float ☃xxxxxxxxxxx = (float)(-(MathHelper.atan2(☃xxxxxxx, ☃xxxxxxxxx) * 180.0F / (float)Math.PI));
            this.xRotationHeads[☃] = this.rotlerp(this.xRotationHeads[☃], ☃xxxxxxxxxxx, 40.0F);
            this.yRotationHeads[☃] = this.rotlerp(this.yRotationHeads[☃], ☃xxxxxxxxxx, 10.0F);
         } else {
            this.yRotationHeads[☃] = this.rotlerp(this.yRotationHeads[☃], this.renderYawOffset, 10.0F);
         }
      }

      boolean ☃ = this.isArmored();

      for (int ☃xxx = 0; ☃xxx < 3; ☃xxx++) {
         double ☃xxxx = this.getHeadX(☃xxx);
         double ☃xxxxx = this.getHeadY(☃xxx);
         double ☃xxxxxx = this.getHeadZ(☃xxx);
         this.world
            .spawnParticle(
               EnumParticleTypes.SMOKE_NORMAL,
               ☃xxxx + this.rand.nextGaussian() * 0.3F,
               ☃xxxxx + this.rand.nextGaussian() * 0.3F,
               ☃xxxxxx + this.rand.nextGaussian() * 0.3F,
               0.0,
               0.0,
               0.0
            );
         if (☃ && this.world.rand.nextInt(4) == 0) {
            this.world
               .spawnParticle(
                  EnumParticleTypes.SPELL_MOB,
                  ☃xxxx + this.rand.nextGaussian() * 0.3F,
                  ☃xxxxx + this.rand.nextGaussian() * 0.3F,
                  ☃xxxxxx + this.rand.nextGaussian() * 0.3F,
                  0.7F,
                  0.7F,
                  0.5
               );
         }
      }

      if (this.getInvulTime() > 0) {
         for (int ☃xxxx = 0; ☃xxxx < 3; ☃xxxx++) {
            this.world
               .spawnParticle(
                  EnumParticleTypes.SPELL_MOB,
                  this.posX + this.rand.nextGaussian(),
                  this.posY + this.rand.nextFloat() * 3.3F,
                  this.posZ + this.rand.nextGaussian(),
                  0.7F,
                  0.7F,
                  0.9F
               );
         }
      }
   }

   @Override
   protected void updateAITasks() {
      if (this.getInvulTime() > 0) {
         int ☃ = this.getInvulTime() - 1;
         if (☃ <= 0) {
            this.world
               .newExplosion(this, this.posX, this.posY + this.getEyeHeight(), this.posZ, 7.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
            this.world.playBroadcastSound(1023, new BlockPos(this), 0);
         }

         this.setInvulTime(☃);
         if (this.ticksExisted % 10 == 0) {
            this.heal(10.0F);
         }
      } else {
         super.updateAITasks();

         for (int ☃x = 1; ☃x < 3; ☃x++) {
            if (this.ticksExisted >= this.nextHeadUpdate[☃x - 1]) {
               this.nextHeadUpdate[☃x - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);
               if ((this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD)
                  && this.idleHeadUpdates[☃x - 1]++ > 15) {
                  float ☃xx = 10.0F;
                  float ☃xxx = 5.0F;
                  double ☃xxxx = MathHelper.nextDouble(this.rand, this.posX - 10.0, this.posX + 10.0);
                  double ☃xxxxx = MathHelper.nextDouble(this.rand, this.posY - 5.0, this.posY + 5.0);
                  double ☃xxxxxx = MathHelper.nextDouble(this.rand, this.posZ - 10.0, this.posZ + 10.0);
                  this.launchWitherSkullToCoords(☃x + 1, ☃xxxx, ☃xxxxx, ☃xxxxxx, true);
                  this.idleHeadUpdates[☃x - 1] = 0;
               }

               int ☃xx = this.getWatchedTargetId(☃x);
               if (☃xx > 0) {
                  Entity ☃xxx = this.world.getEntityByID(☃xx);
                  if (☃xxx == null || !☃xxx.isEntityAlive() || this.getDistanceSq(☃xxx) > 900.0 || !this.canEntityBeSeen(☃xxx)) {
                     this.updateWatchedTargetId(☃x, 0);
                  } else if (☃xxx instanceof EntityPlayer && ((EntityPlayer)☃xxx).capabilities.disableDamage) {
                     this.updateWatchedTargetId(☃x, 0);
                  } else {
                     this.launchWitherSkullToEntity(☃x + 1, (EntityLivingBase)☃xxx);
                     this.nextHeadUpdate[☃x - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                     this.idleHeadUpdates[☃x - 1] = 0;
                  }
               } else {
                  List<EntityLivingBase> ☃xxx = this.world
                     .getEntitiesWithinAABB(
                        EntityLivingBase.class, this.getEntityBoundingBox().grow(20.0, 8.0, 20.0), Predicates.and(NOT_UNDEAD, EntitySelectors.NOT_SPECTATING)
                     );

                  for (int ☃xxxx = 0; ☃xxxx < 10 && !☃xxx.isEmpty(); ☃xxxx++) {
                     EntityLivingBase ☃xxxxx = ☃xxx.get(this.rand.nextInt(☃xxx.size()));
                     if (☃xxxxx != this && ☃xxxxx.isEntityAlive() && this.canEntityBeSeen(☃xxxxx)) {
                        if (☃xxxxx instanceof EntityPlayer) {
                           if (!((EntityPlayer)☃xxxxx).capabilities.disableDamage) {
                              this.updateWatchedTargetId(☃x, ☃xxxxx.getEntityId());
                           }
                        } else {
                           this.updateWatchedTargetId(☃x, ☃xxxxx.getEntityId());
                        }
                        break;
                     }

                     ☃xxx.remove(☃xxxxx);
                  }
               }
            }
         }

         if (this.getAttackTarget() != null) {
            this.updateWatchedTargetId(0, this.getAttackTarget().getEntityId());
         } else {
            this.updateWatchedTargetId(0, 0);
         }

         if (this.blockBreakCounter > 0) {
            this.blockBreakCounter--;
            if (this.blockBreakCounter == 0 && this.world.getGameRules().getBoolean("mobGriefing")) {
               int ☃xx = MathHelper.floor(this.posY);
               int ☃xxx = MathHelper.floor(this.posX);
               int ☃xxxx = MathHelper.floor(this.posZ);
               boolean ☃xxxxx = false;

               for (int ☃xxxxxx = -1; ☃xxxxxx <= 1; ☃xxxxxx++) {
                  for (int ☃xxxxxxx = -1; ☃xxxxxxx <= 1; ☃xxxxxxx++) {
                     for (int ☃xxxxxxxx = 0; ☃xxxxxxxx <= 3; ☃xxxxxxxx++) {
                        int ☃xxxxxxxxx = ☃xxx + ☃xxxxxx;
                        int ☃xxxxxxxxxx = ☃xx + ☃xxxxxxxx;
                        int ☃xxxxxxxxxxx = ☃xxxx + ☃xxxxxxx;
                        BlockPos ☃xxxxxxxxxxxx = new BlockPos(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx);
                        IBlockState ☃xxxxxxxxxxxxx = this.world.getBlockState(☃xxxxxxxxxxxx);
                        Block ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx.getBlock();
                        if (☃xxxxxxxxxxxxx.getMaterial() != Material.AIR && canDestroyBlock(☃xxxxxxxxxxxxxx)) {
                           ☃xxxxx = this.world.destroyBlock(☃xxxxxxxxxxxx, true) || ☃xxxxx;
                        }
                     }
                  }
               }

               if (☃xxxxx) {
                  this.world.playEvent(null, 1022, new BlockPos(this), 0);
               }
            }
         }

         if (this.ticksExisted % 20 == 0) {
            this.heal(1.0F);
         }

         this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
      }
   }

   public static boolean canDestroyBlock(Block var0) {
      return ☃ != Blocks.BEDROCK
         && ☃ != Blocks.END_PORTAL
         && ☃ != Blocks.END_PORTAL_FRAME
         && ☃ != Blocks.COMMAND_BLOCK
         && ☃ != Blocks.REPEATING_COMMAND_BLOCK
         && ☃ != Blocks.CHAIN_COMMAND_BLOCK
         && ☃ != Blocks.BARRIER
         && ☃ != Blocks.STRUCTURE_BLOCK
         && ☃ != Blocks.STRUCTURE_VOID
         && ☃ != Blocks.PISTON_EXTENSION
         && ☃ != Blocks.END_GATEWAY;
   }

   public void ignite() {
      this.setInvulTime(220);
      this.setHealth(this.getMaxHealth() / 3.0F);
   }

   @Override
   public void setInWeb() {
   }

   @Override
   public void addTrackingPlayer(EntityPlayerMP var1) {
      super.addTrackingPlayer(☃);
      this.bossInfo.addPlayer(☃);
   }

   @Override
   public void removeTrackingPlayer(EntityPlayerMP var1) {
      super.removeTrackingPlayer(☃);
      this.bossInfo.removePlayer(☃);
   }

   private double getHeadX(int var1) {
      if (☃ <= 0) {
         return this.posX;
      } else {
         float ☃ = (this.renderYawOffset + 180 * (☃ - 1)) * (float) (Math.PI / 180.0);
         float ☃x = MathHelper.cos(☃);
         return this.posX + ☃x * 1.3;
      }
   }

   private double getHeadY(int var1) {
      return ☃ <= 0 ? this.posY + 3.0 : this.posY + 2.2;
   }

   private double getHeadZ(int var1) {
      if (☃ <= 0) {
         return this.posZ;
      } else {
         float ☃ = (this.renderYawOffset + 180 * (☃ - 1)) * (float) (Math.PI / 180.0);
         float ☃x = MathHelper.sin(☃);
         return this.posZ + ☃x * 1.3;
      }
   }

   private float rotlerp(float var1, float var2, float var3) {
      float ☃ = MathHelper.wrapDegrees(☃ - ☃);
      if (☃ > ☃) {
         ☃ = ☃;
      }

      if (☃ < -☃) {
         ☃ = -☃;
      }

      return ☃ + ☃;
   }

   private void launchWitherSkullToEntity(int var1, EntityLivingBase var2) {
      this.launchWitherSkullToCoords(☃, ☃.posX, ☃.posY + ☃.getEyeHeight() * 0.5, ☃.posZ, ☃ == 0 && this.rand.nextFloat() < 0.001F);
   }

   private void launchWitherSkullToCoords(int var1, double var2, double var4, double var6, boolean var8) {
      this.world.playEvent(null, 1024, new BlockPos(this), 0);
      double ☃ = this.getHeadX(☃);
      double ☃x = this.getHeadY(☃);
      double ☃xx = this.getHeadZ(☃);
      double ☃xxx = ☃ - ☃;
      double ☃xxxx = ☃ - ☃x;
      double ☃xxxxx = ☃ - ☃xx;
      EntityWitherSkull ☃xxxxxx = new EntityWitherSkull(this.world, this, ☃xxx, ☃xxxx, ☃xxxxx);
      if (☃) {
         ☃xxxxxx.setInvulnerable(true);
      }

      ☃xxxxxx.posY = ☃x;
      ☃xxxxxx.posX = ☃;
      ☃xxxxxx.posZ = ☃xx;
      this.world.spawnEntity(☃xxxxxx);
   }

   @Override
   public void attackEntityWithRangedAttack(EntityLivingBase var1, float var2) {
      this.launchWitherSkullToEntity(0, ☃);
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else if (☃ == DamageSource.DROWN || ☃.getTrueSource() instanceof EntityWither) {
         return false;
      } else if (this.getInvulTime() > 0 && ☃ != DamageSource.OUT_OF_WORLD) {
         return false;
      } else {
         if (this.isArmored()) {
            Entity ☃ = ☃.getImmediateSource();
            if (☃ instanceof EntityArrow) {
               return false;
            }
         }

         Entity ☃ = ☃.getTrueSource();
         if (☃ != null
            && !(☃ instanceof EntityPlayer)
            && ☃ instanceof EntityLivingBase
            && ((EntityLivingBase)☃).getCreatureAttribute() == this.getCreatureAttribute()) {
            return false;
         } else {
            if (this.blockBreakCounter <= 0) {
               this.blockBreakCounter = 20;
            }

            for (int ☃x = 0; ☃x < this.idleHeadUpdates.length; ☃x++) {
               this.idleHeadUpdates[☃x] = this.idleHeadUpdates[☃x] + 3;
            }

            return super.attackEntityFrom(☃, ☃);
         }
      }
   }

   @Override
   protected void dropFewItems(boolean var1, int var2) {
      EntityItem ☃ = this.dropItem(Items.NETHER_STAR, 1);
      if (☃ != null) {
         ☃.setNoDespawn();
      }
   }

   @Override
   protected void despawnEntity() {
      this.idleTime = 0;
   }

   @Override
   public int getBrightnessForRender() {
      return 15728880;
   }

   @Override
   public void fall(float var1, float var2) {
   }

   @Override
   public void addPotionEffect(PotionEffect var1) {
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6F);
      this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0);
      this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0);
   }

   public float getHeadYRotation(int var1) {
      return this.yRotationHeads[☃];
   }

   public float getHeadXRotation(int var1) {
      return this.xRotationHeads[☃];
   }

   public int getInvulTime() {
      return this.dataManager.get(INVULNERABILITY_TIME);
   }

   public void setInvulTime(int var1) {
      this.dataManager.set(INVULNERABILITY_TIME, ☃);
   }

   public int getWatchedTargetId(int var1) {
      return this.dataManager.get(HEAD_TARGETS[☃]);
   }

   public void updateWatchedTargetId(int var1, int var2) {
      this.dataManager.set(HEAD_TARGETS[☃], ☃);
   }

   public boolean isArmored() {
      return this.getHealth() <= this.getMaxHealth() / 2.0F;
   }

   @Override
   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   @Override
   protected boolean canBeRidden(Entity var1) {
      return false;
   }

   @Override
   public boolean isNonBoss() {
      return false;
   }

   @Override
   public void setSwingingArms(boolean var1) {
   }

   class AIDoNothing extends EntityAIBase {
      public AIDoNothing() {
         this.setMutexBits(7);
      }

      @Override
      public boolean shouldExecute() {
         return EntityWither.this.getInvulTime() > 0;
      }
   }
}
