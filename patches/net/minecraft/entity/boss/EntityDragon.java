package net.minecraft.entity.boss;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.dragon.phase.IPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathHeap;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityDragon extends EntityLiving implements IEntityMultiPart, IMob {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final DataParameter<Integer> PHASE = EntityDataManager.createKey(EntityDragon.class, DataSerializers.VARINT);
   public double[][] ringBuffer = new double[64][3];
   public int ringBufferIndex = -1;
   public MultiPartEntityPart[] dragonPartArray;
   public MultiPartEntityPart dragonPartHead;
   public MultiPartEntityPart dragonPartNeck;
   public MultiPartEntityPart dragonPartBody;
   public MultiPartEntityPart dragonPartTail1;
   public MultiPartEntityPart dragonPartTail2;
   public MultiPartEntityPart dragonPartTail3;
   public MultiPartEntityPart dragonPartWing1;
   public MultiPartEntityPart dragonPartWing2;
   public float prevAnimTime;
   public float animTime;
   public boolean slowed;
   public int deathTicks;
   public EntityEnderCrystal healingEnderCrystal;
   private final DragonFightManager fightManager;
   private final PhaseManager phaseManager;
   private int growlTime = 200;
   private int sittingDamageReceived;
   private final PathPoint[] pathPoints = new PathPoint[24];
   private final int[] neighbors = new int[24];
   private final PathHeap pathFindQueue = new PathHeap();

   public EntityDragon(World var1) {
      super(☃);
      this.dragonPartHead = new MultiPartEntityPart(this, "head", 6.0F, 6.0F);
      this.dragonPartNeck = new MultiPartEntityPart(this, "neck", 6.0F, 6.0F);
      this.dragonPartBody = new MultiPartEntityPart(this, "body", 8.0F, 8.0F);
      this.dragonPartTail1 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
      this.dragonPartTail2 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
      this.dragonPartTail3 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
      this.dragonPartWing1 = new MultiPartEntityPart(this, "wing", 4.0F, 4.0F);
      this.dragonPartWing2 = new MultiPartEntityPart(this, "wing", 4.0F, 4.0F);
      this.dragonPartArray = new MultiPartEntityPart[]{
         this.dragonPartHead,
         this.dragonPartNeck,
         this.dragonPartBody,
         this.dragonPartTail1,
         this.dragonPartTail2,
         this.dragonPartTail3,
         this.dragonPartWing1,
         this.dragonPartWing2
      };
      this.setHealth(this.getMaxHealth());
      this.setSize(16.0F, 8.0F);
      this.noClip = true;
      this.isImmuneToFire = true;
      this.growlTime = 100;
      this.ignoreFrustumCheck = true;
      if (!☃.isRemote && ☃.provider instanceof WorldProviderEnd) {
         this.fightManager = ((WorldProviderEnd)☃.provider).getDragonFightManager();
      } else {
         this.fightManager = null;
      }

      this.phaseManager = new PhaseManager(this);
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.getDataManager().register(PHASE, PhaseList.HOVER.getId());
   }

   public double[] getMovementOffsets(int var1, float var2) {
      if (this.getHealth() <= 0.0F) {
         ☃ = 0.0F;
      }

      ☃ = 1.0F - ☃;
      int ☃ = this.ringBufferIndex - ☃ & 63;
      int ☃x = this.ringBufferIndex - ☃ - 1 & 63;
      double[] ☃xx = new double[3];
      double ☃xxx = this.ringBuffer[☃][0];
      double ☃xxxx = MathHelper.wrapDegrees(this.ringBuffer[☃x][0] - ☃xxx);
      ☃xx[0] = ☃xxx + ☃xxxx * ☃;
      ☃xxx = this.ringBuffer[☃][1];
      ☃xxxx = this.ringBuffer[☃x][1] - ☃xxx;
      ☃xx[1] = ☃xxx + ☃xxxx * ☃;
      ☃xx[2] = this.ringBuffer[☃][2] + (this.ringBuffer[☃x][2] - this.ringBuffer[☃][2]) * ☃;
      return ☃xx;
   }

   @Override
   public void onLivingUpdate() {
      if (this.world.isRemote) {
         this.setHealth(this.getHealth());
         if (!this.isSilent()) {
            float ☃ = MathHelper.cos(this.animTime * (float) (Math.PI * 2));
            float ☃x = MathHelper.cos(this.prevAnimTime * (float) (Math.PI * 2));
            if (☃x <= -0.3F && ☃ >= -0.3F) {
               this.world
                  .playSound(
                     this.posX,
                     this.posY,
                     this.posZ,
                     SoundEvents.ENTITY_ENDERDRAGON_FLAP,
                     this.getSoundCategory(),
                     5.0F,
                     0.8F + this.rand.nextFloat() * 0.3F,
                     false
                  );
            }

            if (!this.phaseManager.getCurrentPhase().getIsStationary() && --this.growlTime < 0) {
               this.world
                  .playSound(
                     this.posX,
                     this.posY,
                     this.posZ,
                     SoundEvents.ENTITY_ENDERDRAGON_GROWL,
                     this.getSoundCategory(),
                     2.5F,
                     0.8F + this.rand.nextFloat() * 0.3F,
                     false
                  );
               this.growlTime = 200 + this.rand.nextInt(200);
            }
         }
      }

      this.prevAnimTime = this.animTime;
      if (this.getHealth() <= 0.0F) {
         float ☃xx = (this.rand.nextFloat() - 0.5F) * 8.0F;
         float ☃xxx = (this.rand.nextFloat() - 0.5F) * 4.0F;
         float ☃xxxx = (this.rand.nextFloat() - 0.5F) * 8.0F;
         this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + ☃xx, this.posY + 2.0 + ☃xxx, this.posZ + ☃xxxx, 0.0, 0.0, 0.0);
      } else {
         this.updateDragonEnderCrystal();
         float ☃xx = 0.2F / (MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
         ☃xx *= (float)Math.pow(2.0, this.motionY);
         if (this.phaseManager.getCurrentPhase().getIsStationary()) {
            this.animTime += 0.1F;
         } else if (this.slowed) {
            this.animTime += ☃xx * 0.5F;
         } else {
            this.animTime += ☃xx;
         }

         this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
         if (this.isAIDisabled()) {
            this.animTime = 0.5F;
         } else {
            if (this.ringBufferIndex < 0) {
               for (int ☃xxx = 0; ☃xxx < this.ringBuffer.length; ☃xxx++) {
                  this.ringBuffer[☃xxx][0] = this.rotationYaw;
                  this.ringBuffer[☃xxx][1] = this.posY;
               }
            }

            if (++this.ringBufferIndex == this.ringBuffer.length) {
               this.ringBufferIndex = 0;
            }

            this.ringBuffer[this.ringBufferIndex][0] = this.rotationYaw;
            this.ringBuffer[this.ringBufferIndex][1] = this.posY;
            if (this.world.isRemote) {
               if (this.newPosRotationIncrements > 0) {
                  double ☃xxx = this.posX + (this.interpTargetX - this.posX) / this.newPosRotationIncrements;
                  double ☃xxxx = this.posY + (this.interpTargetY - this.posY) / this.newPosRotationIncrements;
                  double ☃xxxxx = this.posZ + (this.interpTargetZ - this.posZ) / this.newPosRotationIncrements;
                  double ☃xxxxxx = MathHelper.wrapDegrees(this.interpTargetYaw - this.rotationYaw);
                  this.rotationYaw = (float)(this.rotationYaw + ☃xxxxxx / this.newPosRotationIncrements);
                  this.rotationPitch = (float)(this.rotationPitch + (this.interpTargetPitch - this.rotationPitch) / this.newPosRotationIncrements);
                  this.newPosRotationIncrements--;
                  this.setPosition(☃xxx, ☃xxxx, ☃xxxxx);
                  this.setRotation(this.rotationYaw, this.rotationPitch);
               }

               this.phaseManager.getCurrentPhase().doClientRenderEffects();
            } else {
               IPhase ☃xxx = this.phaseManager.getCurrentPhase();
               ☃xxx.doLocalUpdate();
               if (this.phaseManager.getCurrentPhase() != ☃xxx) {
                  ☃xxx = this.phaseManager.getCurrentPhase();
                  ☃xxx.doLocalUpdate();
               }

               Vec3d ☃xxxx = ☃xxx.getTargetLocation();
               if (☃xxxx != null) {
                  double ☃xxxxx = ☃xxxx.x - this.posX;
                  double ☃xxxxxx = ☃xxxx.y - this.posY;
                  double ☃xxxxxxx = ☃xxxx.z - this.posZ;
                  double ☃xxxxxxxx = ☃xxxxx * ☃xxxxx + ☃xxxxxx * ☃xxxxxx + ☃xxxxxxx * ☃xxxxxxx;
                  float ☃xxxxxxxxx = ☃xxx.getMaxRiseOrFall();
                  ☃xxxxxx = MathHelper.clamp(☃xxxxxx / MathHelper.sqrt(☃xxxxx * ☃xxxxx + ☃xxxxxxx * ☃xxxxxxx), (double)(-☃xxxxxxxxx), (double)☃xxxxxxxxx);
                  this.motionY += ☃xxxxxx * 0.1F;
                  this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
                  double ☃xxxxxxxxxx = MathHelper.clamp(
                     MathHelper.wrapDegrees(180.0 - MathHelper.atan2(☃xxxxx, ☃xxxxxxx) * 180.0F / (float)Math.PI - this.rotationYaw), -50.0, 50.0
                  );
                  Vec3d ☃xxxxxxxxxxx = new Vec3d(☃xxxx.x - this.posX, ☃xxxx.y - this.posY, ☃xxxx.z - this.posZ).normalize();
                  Vec3d ☃xxxxxxxxxxxx = new Vec3d(
                        MathHelper.sin(this.rotationYaw * (float) (Math.PI / 180.0)),
                        this.motionY,
                        -MathHelper.cos(this.rotationYaw * (float) (Math.PI / 180.0))
                     )
                     .normalize();
                  float ☃xxxxxxxxxxxxx = Math.max(((float)☃xxxxxxxxxxxx.dotProduct(☃xxxxxxxxxxx) + 0.5F) / 1.5F, 0.0F);
                  this.randomYawVelocity *= 0.8F;
                  this.randomYawVelocity = (float)(this.randomYawVelocity + ☃xxxxxxxxxx * ☃xxx.getYawFactor());
                  this.rotationYaw = this.rotationYaw + this.randomYawVelocity * 0.1F;
                  float ☃xxxxxxxxxxxxxx = (float)(2.0 / (☃xxxxxxxx + 1.0));
                  float ☃xxxxxxxxxxxxxxx = 0.06F;
                  this.moveRelative(0.0F, 0.0F, -1.0F, 0.06F * (☃xxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx + (1.0F - ☃xxxxxxxxxxxxxx)));
                  if (this.slowed) {
                     this.move(MoverType.SELF, this.motionX * 0.8F, this.motionY * 0.8F, this.motionZ * 0.8F);
                  } else {
                     this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                  }

                  Vec3d ☃xxxxxxxxxxxxxxxx = new Vec3d(this.motionX, this.motionY, this.motionZ).normalize();
                  float ☃xxxxxxxxxxxxxxxxx = ((float)☃xxxxxxxxxxxxxxxx.dotProduct(☃xxxxxxxxxxxx) + 1.0F) / 2.0F;
                  ☃xxxxxxxxxxxxxxxxx = 0.8F + 0.15F * ☃xxxxxxxxxxxxxxxxx;
                  this.motionX *= ☃xxxxxxxxxxxxxxxxx;
                  this.motionZ *= ☃xxxxxxxxxxxxxxxxx;
                  this.motionY *= 0.91F;
               }
            }

            this.renderYawOffset = this.rotationYaw;
            this.dragonPartHead.width = 1.0F;
            this.dragonPartHead.height = 1.0F;
            this.dragonPartNeck.width = 3.0F;
            this.dragonPartNeck.height = 3.0F;
            this.dragonPartTail1.width = 2.0F;
            this.dragonPartTail1.height = 2.0F;
            this.dragonPartTail2.width = 2.0F;
            this.dragonPartTail2.height = 2.0F;
            this.dragonPartTail3.width = 2.0F;
            this.dragonPartTail3.height = 2.0F;
            this.dragonPartBody.height = 3.0F;
            this.dragonPartBody.width = 5.0F;
            this.dragonPartWing1.height = 2.0F;
            this.dragonPartWing1.width = 4.0F;
            this.dragonPartWing2.height = 3.0F;
            this.dragonPartWing2.width = 4.0F;
            Vec3d[] ☃xxxx = new Vec3d[this.dragonPartArray.length];

            for (int ☃xxxxx = 0; ☃xxxxx < this.dragonPartArray.length; ☃xxxxx++) {
               ☃xxxx[☃xxxxx] = new Vec3d(this.dragonPartArray[☃xxxxx].posX, this.dragonPartArray[☃xxxxx].posY, this.dragonPartArray[☃xxxxx].posZ);
            }

            float ☃xxxxx = (float)(this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F * (float) (Math.PI / 180.0);
            float ☃xxxxxx = MathHelper.cos(☃xxxxx);
            float ☃xxxxxxx = MathHelper.sin(☃xxxxx);
            float ☃xxxxxxxx = this.rotationYaw * (float) (Math.PI / 180.0);
            float ☃xxxxxxxxx = MathHelper.sin(☃xxxxxxxx);
            float ☃xxxxxxxxxx = MathHelper.cos(☃xxxxxxxx);
            this.dragonPartBody.onUpdate();
            this.dragonPartBody.setLocationAndAngles(this.posX + ☃xxxxxxxxx * 0.5F, this.posY, this.posZ - ☃xxxxxxxxxx * 0.5F, 0.0F, 0.0F);
            this.dragonPartWing1.onUpdate();
            this.dragonPartWing1.setLocationAndAngles(this.posX + ☃xxxxxxxxxx * 4.5F, this.posY + 2.0, this.posZ + ☃xxxxxxxxx * 4.5F, 0.0F, 0.0F);
            this.dragonPartWing2.onUpdate();
            this.dragonPartWing2.setLocationAndAngles(this.posX - ☃xxxxxxxxxx * 4.5F, this.posY + 2.0, this.posZ - ☃xxxxxxxxx * 4.5F, 0.0F, 0.0F);
            if (!this.world.isRemote && this.hurtTime == 0) {
               this.collideWithEntities(
                  this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().grow(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0))
               );
               this.collideWithEntities(
                  this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().grow(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0))
               );
               this.attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().grow(1.0)));
               this.attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartNeck.getEntityBoundingBox().grow(1.0)));
            }

            double[] ☃xxxxxxxxxxx = this.getMovementOffsets(5, 1.0F);
            float ☃xxxxxxxxxxxx = MathHelper.sin(this.rotationYaw * (float) (Math.PI / 180.0) - this.randomYawVelocity * 0.01F);
            float ☃xxxxxxxxxxxxx = MathHelper.cos(this.rotationYaw * (float) (Math.PI / 180.0) - this.randomYawVelocity * 0.01F);
            this.dragonPartHead.onUpdate();
            this.dragonPartNeck.onUpdate();
            float ☃xxxxxxxxxxxxxx = this.getHeadYOffset(1.0F);
            this.dragonPartHead
               .setLocationAndAngles(
                  this.posX + ☃xxxxxxxxxxxx * 6.5F * ☃xxxxxx,
                  this.posY + ☃xxxxxxxxxxxxxx + ☃xxxxxxx * 6.5F,
                  this.posZ - ☃xxxxxxxxxxxxx * 6.5F * ☃xxxxxx,
                  0.0F,
                  0.0F
               );
            this.dragonPartNeck
               .setLocationAndAngles(
                  this.posX + ☃xxxxxxxxxxxx * 5.5F * ☃xxxxxx,
                  this.posY + ☃xxxxxxxxxxxxxx + ☃xxxxxxx * 5.5F,
                  this.posZ - ☃xxxxxxxxxxxxx * 5.5F * ☃xxxxxx,
                  0.0F,
                  0.0F
               );

            for (int ☃xxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxx < 3; ☃xxxxxxxxxxxxxxx++) {
               MultiPartEntityPart ☃xxxxxxxxxxxxxxxx = null;
               if (☃xxxxxxxxxxxxxxx == 0) {
                  ☃xxxxxxxxxxxxxxxx = this.dragonPartTail1;
               }

               if (☃xxxxxxxxxxxxxxx == 1) {
                  ☃xxxxxxxxxxxxxxxx = this.dragonPartTail2;
               }

               if (☃xxxxxxxxxxxxxxx == 2) {
                  ☃xxxxxxxxxxxxxxxx = this.dragonPartTail3;
               }

               double[] ☃xxxxxxxxxxxxxxxxx = this.getMovementOffsets(12 + ☃xxxxxxxxxxxxxxx * 2, 1.0F);
               float ☃xxxxxxxxxxxxxxxxxx = this.rotationYaw * (float) (Math.PI / 180.0)
                  + this.simplifyAngle(☃xxxxxxxxxxxxxxxxx[0] - ☃xxxxxxxxxxx[0]) * (float) (Math.PI / 180.0);
               float ☃xxxxxxxxxxxxxxxxxxx = MathHelper.sin(☃xxxxxxxxxxxxxxxxxx);
               float ☃xxxxxxxxxxxxxxxxxxxx = MathHelper.cos(☃xxxxxxxxxxxxxxxxxx);
               float ☃xxxxxxxxxxxxxxxxxxxxx = 1.5F;
               float ☃xxxxxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxx + 1) * 2.0F;
               ☃xxxxxxxxxxxxxxxx.onUpdate();
               ☃xxxxxxxxxxxxxxxx.setLocationAndAngles(
                  this.posX - (☃xxxxxxxxx * 1.5F + ☃xxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxx) * ☃xxxxxx,
                  this.posY + (☃xxxxxxxxxxxxxxxxx[1] - ☃xxxxxxxxxxx[1]) - (☃xxxxxxxxxxxxxxxxxxxxxx + 1.5F) * ☃xxxxxxx + 1.5,
                  this.posZ + (☃xxxxxxxxxx * 1.5F + ☃xxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxx) * ☃xxxxxx,
                  0.0F,
                  0.0F
               );
            }

            if (!this.world.isRemote) {
               this.slowed = this.destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox())
                  | this.destroyBlocksInAABB(this.dragonPartNeck.getEntityBoundingBox())
                  | this.destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox());
               if (this.fightManager != null) {
                  this.fightManager.dragonUpdate(this);
               }
            }

            for (int ☃xxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxx < this.dragonPartArray.length; ☃xxxxxxxxxxxxxxx++) {
               this.dragonPartArray[☃xxxxxxxxxxxxxxx].prevPosX = ☃xxxx[☃xxxxxxxxxxxxxxx].x;
               this.dragonPartArray[☃xxxxxxxxxxxxxxx].prevPosY = ☃xxxx[☃xxxxxxxxxxxxxxx].y;
               this.dragonPartArray[☃xxxxxxxxxxxxxxx].prevPosZ = ☃xxxx[☃xxxxxxxxxxxxxxx].z;
            }
         }
      }
   }

   private float getHeadYOffset(float var1) {
      double ☃;
      if (this.phaseManager.getCurrentPhase().getIsStationary()) {
         ☃ = -1.0;
      } else {
         double[] ☃x = this.getMovementOffsets(5, 1.0F);
         double[] ☃xx = this.getMovementOffsets(0, 1.0F);
         ☃ = ☃x[1] - ☃xx[1];
      }

      return (float)☃;
   }

   private void updateDragonEnderCrystal() {
      if (this.healingEnderCrystal != null) {
         if (this.healingEnderCrystal.isDead) {
            this.healingEnderCrystal = null;
         } else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
            this.setHealth(this.getHealth() + 1.0F);
         }
      }

      if (this.rand.nextInt(10) == 0) {
         List<EntityEnderCrystal> ☃ = this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, this.getEntityBoundingBox().grow(32.0));
         EntityEnderCrystal ☃x = null;
         double ☃xx = Double.MAX_VALUE;

         for (EntityEnderCrystal ☃xxx : ☃) {
            double ☃xxxx = ☃xxx.getDistanceSq(this);
            if (☃xxxx < ☃xx) {
               ☃xx = ☃xxxx;
               ☃x = ☃xxx;
            }
         }

         this.healingEnderCrystal = ☃x;
      }
   }

   private void collideWithEntities(List<Entity> var1) {
      double ☃ = (this.dragonPartBody.getEntityBoundingBox().minX + this.dragonPartBody.getEntityBoundingBox().maxX) / 2.0;
      double ☃x = (this.dragonPartBody.getEntityBoundingBox().minZ + this.dragonPartBody.getEntityBoundingBox().maxZ) / 2.0;

      for (Entity ☃xx : ☃) {
         if (☃xx instanceof EntityLivingBase) {
            double ☃xxx = ☃xx.posX - ☃;
            double ☃xxxx = ☃xx.posZ - ☃x;
            double ☃xxxxx = ☃xxx * ☃xxx + ☃xxxx * ☃xxxx;
            ☃xx.addVelocity(☃xxx / ☃xxxxx * 4.0, 0.2F, ☃xxxx / ☃xxxxx * 4.0);
            if (!this.phaseManager.getCurrentPhase().getIsStationary() && ((EntityLivingBase)☃xx).getRevengeTimer() < ☃xx.ticksExisted - 2) {
               ☃xx.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0F);
               this.applyEnchantments(this, ☃xx);
            }
         }
      }
   }

   private void attackEntitiesInList(List<Entity> var1) {
      for (int ☃ = 0; ☃ < ☃.size(); ☃++) {
         Entity ☃x = ☃.get(☃);
         if (☃x instanceof EntityLivingBase) {
            ☃x.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
            this.applyEnchantments(this, ☃x);
         }
      }
   }

   private float simplifyAngle(double var1) {
      return (float)MathHelper.wrapDegrees(☃);
   }

   private boolean destroyBlocksInAABB(AxisAlignedBB var1) {
      int ☃ = MathHelper.floor(☃.minX);
      int ☃x = MathHelper.floor(☃.minY);
      int ☃xx = MathHelper.floor(☃.minZ);
      int ☃xxx = MathHelper.floor(☃.maxX);
      int ☃xxxx = MathHelper.floor(☃.maxY);
      int ☃xxxxx = MathHelper.floor(☃.maxZ);
      boolean ☃xxxxxx = false;
      boolean ☃xxxxxxx = false;

      for (int ☃xxxxxxxx = ☃; ☃xxxxxxxx <= ☃xxx; ☃xxxxxxxx++) {
         for (int ☃xxxxxxxxx = ☃x; ☃xxxxxxxxx <= ☃xxxx; ☃xxxxxxxxx++) {
            for (int ☃xxxxxxxxxx = ☃xx; ☃xxxxxxxxxx <= ☃xxxxx; ☃xxxxxxxxxx++) {
               BlockPos ☃xxxxxxxxxxx = new BlockPos(☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx);
               IBlockState ☃xxxxxxxxxxxx = this.world.getBlockState(☃xxxxxxxxxxx);
               Block ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxx.getBlock();
               if (☃xxxxxxxxxxxx.getMaterial() != Material.AIR && ☃xxxxxxxxxxxx.getMaterial() != Material.FIRE) {
                  if (!this.world.getGameRules().getBoolean("mobGriefing")) {
                     ☃xxxxxx = true;
                  } else if (☃xxxxxxxxxxxxx == Blocks.BARRIER
                     || ☃xxxxxxxxxxxxx == Blocks.OBSIDIAN
                     || ☃xxxxxxxxxxxxx == Blocks.END_STONE
                     || ☃xxxxxxxxxxxxx == Blocks.BEDROCK
                     || ☃xxxxxxxxxxxxx == Blocks.END_PORTAL
                     || ☃xxxxxxxxxxxxx == Blocks.END_PORTAL_FRAME) {
                     ☃xxxxxx = true;
                  } else if (☃xxxxxxxxxxxxx != Blocks.COMMAND_BLOCK
                     && ☃xxxxxxxxxxxxx != Blocks.REPEATING_COMMAND_BLOCK
                     && ☃xxxxxxxxxxxxx != Blocks.CHAIN_COMMAND_BLOCK
                     && ☃xxxxxxxxxxxxx != Blocks.IRON_BARS
                     && ☃xxxxxxxxxxxxx != Blocks.END_GATEWAY) {
                     ☃xxxxxxx = this.world.setBlockToAir(☃xxxxxxxxxxx) || ☃xxxxxxx;
                  } else {
                     ☃xxxxxx = true;
                  }
               }
            }
         }
      }

      if (☃xxxxxxx) {
         double ☃xxxxxxxx = ☃.minX + (☃.maxX - ☃.minX) * this.rand.nextFloat();
         double ☃xxxxxxxxx = ☃.minY + (☃.maxY - ☃.minY) * this.rand.nextFloat();
         double ☃xxxxxxxxxxx = ☃.minZ + (☃.maxZ - ☃.minZ) * this.rand.nextFloat();
         this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, ☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxxx, 0.0, 0.0, 0.0);
      }

      return ☃xxxxxx;
   }

   @Override
   public boolean attackEntityFromPart(MultiPartEntityPart var1, DamageSource var2, float var3) {
      ☃ = this.phaseManager.getCurrentPhase().getAdjustedDamage(☃, ☃, ☃);
      if (☃ != this.dragonPartHead) {
         ☃ = ☃ / 4.0F + Math.min(☃, 1.0F);
      }

      if (☃ < 0.01F) {
         return false;
      } else {
         if (☃.getTrueSource() instanceof EntityPlayer || ☃.isExplosion()) {
            float ☃ = this.getHealth();
            this.attackDragonFrom(☃, ☃);
            if (this.getHealth() <= 0.0F && !this.phaseManager.getCurrentPhase().getIsStationary()) {
               this.setHealth(1.0F);
               this.phaseManager.setPhase(PhaseList.DYING);
            }

            if (this.phaseManager.getCurrentPhase().getIsStationary()) {
               this.sittingDamageReceived = (int)(this.sittingDamageReceived + (☃ - this.getHealth()));
               if (this.sittingDamageReceived > 0.25F * this.getMaxHealth()) {
                  this.sittingDamageReceived = 0;
                  this.phaseManager.setPhase(PhaseList.TAKEOFF);
               }
            }
         }

         return true;
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (☃ instanceof EntityDamageSource && ((EntityDamageSource)☃).getIsThornsDamage()) {
         this.attackEntityFromPart(this.dragonPartBody, ☃, ☃);
      }

      return false;
   }

   protected boolean attackDragonFrom(DamageSource var1, float var2) {
      return super.attackEntityFrom(☃, ☃);
   }

   @Override
   public void onKillCommand() {
      this.setDead();
      if (this.fightManager != null) {
         this.fightManager.dragonUpdate(this);
         this.fightManager.processDragonDeath(this);
      }
   }

   @Override
   protected void onDeathUpdate() {
      if (this.fightManager != null) {
         this.fightManager.dragonUpdate(this);
      }

      this.deathTicks++;
      if (this.deathTicks >= 180 && this.deathTicks <= 200) {
         float ☃ = (this.rand.nextFloat() - 0.5F) * 8.0F;
         float ☃x = (this.rand.nextFloat() - 0.5F) * 4.0F;
         float ☃xx = (this.rand.nextFloat() - 0.5F) * 8.0F;
         this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + ☃, this.posY + 2.0 + ☃x, this.posZ + ☃xx, 0.0, 0.0, 0.0);
      }

      boolean ☃ = this.world.getGameRules().getBoolean("doMobLoot");
      int ☃x = 500;
      if (this.fightManager != null && !this.fightManager.hasPreviouslyKilledDragon()) {
         ☃x = 12000;
      }

      if (!this.world.isRemote) {
         if (this.deathTicks > 150 && this.deathTicks % 5 == 0 && ☃) {
            this.dropExperience(MathHelper.floor(☃x * 0.08F));
         }

         if (this.deathTicks == 1) {
            this.world.playBroadcastSound(1028, new BlockPos(this), 0);
         }
      }

      this.move(MoverType.SELF, 0.0, 0.1F, 0.0);
      this.rotationYaw += 20.0F;
      this.renderYawOffset = this.rotationYaw;
      if (this.deathTicks == 200 && !this.world.isRemote) {
         if (☃) {
            this.dropExperience(MathHelper.floor(☃x * 0.2F));
         }

         if (this.fightManager != null) {
            this.fightManager.processDragonDeath(this);
         }

         this.setDead();
      }
   }

   private void dropExperience(int var1) {
      while (☃ > 0) {
         int ☃ = EntityXPOrb.getXPSplit(☃);
         ☃ -= ☃;
         this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, ☃));
      }
   }

   public int initPathPoints() {
      if (this.pathPoints[0] == null) {
         for (int ☃ = 0; ☃ < 24; ☃++) {
            int ☃x = 5;
            int ☃xx;
            int ☃xxx;
            if (☃ < 12) {
               ☃xx = (int)(60.0F * MathHelper.cos(2.0F * ((float) -Math.PI + (float) (Math.PI / 12) * ☃)));
               ☃xxx = (int)(60.0F * MathHelper.sin(2.0F * ((float) -Math.PI + (float) (Math.PI / 12) * ☃)));
            } else if (☃ < 20) {
               int var3 = ☃ - 12;
               ☃xx = (int)(40.0F * MathHelper.cos(2.0F * ((float) -Math.PI + (float) (Math.PI / 8) * var3)));
               ☃xxx = (int)(40.0F * MathHelper.sin(2.0F * ((float) -Math.PI + (float) (Math.PI / 8) * var3)));
               ☃x += 10;
            } else {
               int var7 = ☃ - 20;
               ☃xx = (int)(20.0F * MathHelper.cos(2.0F * ((float) -Math.PI + (float) (Math.PI / 4) * var7)));
               ☃xxx = (int)(20.0F * MathHelper.sin(2.0F * ((float) -Math.PI + (float) (Math.PI / 4) * var7)));
            }

            int ☃xxxx = Math.max(this.world.getSeaLevel() + 10, this.world.getTopSolidOrLiquidBlock(new BlockPos(☃xx, 0, ☃xxx)).getY() + ☃x);
            this.pathPoints[☃] = new PathPoint(☃xx, ☃xxxx, ☃xxx);
         }

         this.neighbors[0] = 6146;
         this.neighbors[1] = 8197;
         this.neighbors[2] = 8202;
         this.neighbors[3] = 16404;
         this.neighbors[4] = 32808;
         this.neighbors[5] = 32848;
         this.neighbors[6] = 65696;
         this.neighbors[7] = 131392;
         this.neighbors[8] = 131712;
         this.neighbors[9] = 263424;
         this.neighbors[10] = 526848;
         this.neighbors[11] = 525313;
         this.neighbors[12] = 1581057;
         this.neighbors[13] = 3166214;
         this.neighbors[14] = 2138120;
         this.neighbors[15] = 6373424;
         this.neighbors[16] = 4358208;
         this.neighbors[17] = 12910976;
         this.neighbors[18] = 9044480;
         this.neighbors[19] = 9706496;
         this.neighbors[20] = 15216640;
         this.neighbors[21] = 13688832;
         this.neighbors[22] = 11763712;
         this.neighbors[23] = 8257536;
      }

      return this.getNearestPpIdx(this.posX, this.posY, this.posZ);
   }

   public int getNearestPpIdx(double var1, double var3, double var5) {
      float ☃ = 10000.0F;
      int ☃x = 0;
      PathPoint ☃xx = new PathPoint(MathHelper.floor(☃), MathHelper.floor(☃), MathHelper.floor(☃));
      int ☃xxx = 0;
      if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0) {
         ☃xxx = 12;
      }

      for (int ☃xxxx = ☃xxx; ☃xxxx < 24; ☃xxxx++) {
         if (this.pathPoints[☃xxxx] != null) {
            float ☃xxxxx = this.pathPoints[☃xxxx].distanceToSquared(☃xx);
            if (☃xxxxx < ☃) {
               ☃ = ☃xxxxx;
               ☃x = ☃xxxx;
            }
         }
      }

      return ☃x;
   }

   @Nullable
   public Path findPath(int var1, int var2, @Nullable PathPoint var3) {
      for (int ☃ = 0; ☃ < 24; ☃++) {
         PathPoint ☃x = this.pathPoints[☃];
         ☃x.visited = false;
         ☃x.distanceToTarget = 0.0F;
         ☃x.totalPathDistance = 0.0F;
         ☃x.distanceToNext = 0.0F;
         ☃x.previous = null;
         ☃x.index = -1;
      }

      PathPoint ☃ = this.pathPoints[☃];
      PathPoint ☃x = this.pathPoints[☃];
      ☃.totalPathDistance = 0.0F;
      ☃.distanceToNext = ☃.distanceTo(☃x);
      ☃.distanceToTarget = ☃.distanceToNext;
      this.pathFindQueue.clearPath();
      this.pathFindQueue.addPoint(☃);
      PathPoint ☃xx = ☃;
      int ☃xxx = 0;
      if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0) {
         ☃xxx = 12;
      }

      while (!this.pathFindQueue.isPathEmpty()) {
         PathPoint ☃xxxx = this.pathFindQueue.dequeue();
         if (☃xxxx.equals(☃x)) {
            if (☃ != null) {
               ☃.previous = ☃x;
               ☃x = ☃;
            }

            return this.makePath(☃, ☃x);
         }

         if (☃xxxx.distanceTo(☃x) < ☃xx.distanceTo(☃x)) {
            ☃xx = ☃xxxx;
         }

         ☃xxxx.visited = true;
         int ☃xxxxx = 0;

         for (int ☃xxxxxx = 0; ☃xxxxxx < 24; ☃xxxxxx++) {
            if (this.pathPoints[☃xxxxxx] == ☃xxxx) {
               ☃xxxxx = ☃xxxxxx;
               break;
            }
         }

         for (int ☃xxxxxxx = ☃xxx; ☃xxxxxxx < 24; ☃xxxxxxx++) {
            if ((this.neighbors[☃xxxxx] & 1 << ☃xxxxxxx) > 0) {
               PathPoint ☃xxxxxxxx = this.pathPoints[☃xxxxxxx];
               if (!☃xxxxxxxx.visited) {
                  float ☃xxxxxxxxx = ☃xxxx.totalPathDistance + ☃xxxx.distanceTo(☃xxxxxxxx);
                  if (!☃xxxxxxxx.isAssigned() || ☃xxxxxxxxx < ☃xxxxxxxx.totalPathDistance) {
                     ☃xxxxxxxx.previous = ☃xxxx;
                     ☃xxxxxxxx.totalPathDistance = ☃xxxxxxxxx;
                     ☃xxxxxxxx.distanceToNext = ☃xxxxxxxx.distanceTo(☃x);
                     if (☃xxxxxxxx.isAssigned()) {
                        this.pathFindQueue.changeDistance(☃xxxxxxxx, ☃xxxxxxxx.totalPathDistance + ☃xxxxxxxx.distanceToNext);
                     } else {
                        ☃xxxxxxxx.distanceToTarget = ☃xxxxxxxx.totalPathDistance + ☃xxxxxxxx.distanceToNext;
                        this.pathFindQueue.addPoint(☃xxxxxxxx);
                     }
                  }
               }
            }
         }
      }

      if (☃xx == ☃) {
         return null;
      } else {
         LOGGER.debug("Failed to find path from {} to {}", ☃, ☃);
         if (☃ != null) {
            ☃.previous = ☃xx;
            ☃xx = ☃;
         }

         return this.makePath(☃, ☃xx);
      }
   }

   private Path makePath(PathPoint var1, PathPoint var2) {
      int ☃ = 1;

      for (PathPoint ☃x = ☃; ☃x.previous != null; ☃x = ☃x.previous) {
         ☃++;
      }

      PathPoint[] ☃x = new PathPoint[☃];
      PathPoint var7 = ☃;

      for (☃x[--☃] = ☃; var7.previous != null; ☃x[--☃] = var7) {
         var7 = var7.previous;
      }

      return new Path(☃x);
   }

   public static void registerFixesDragon(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityDragon.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("DragonPhase", this.phaseManager.getCurrentPhase().getType().getId());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      if (☃.hasKey("DragonPhase")) {
         this.phaseManager.setPhase(PhaseList.getById(☃.getInteger("DragonPhase")));
      }
   }

   @Override
   protected void despawnEntity() {
   }

   @Override
   public Entity[] getParts() {
      return this.dragonPartArray;
   }

   @Override
   public boolean canBeCollidedWith() {
      return false;
   }

   @Override
   public World getWorld() {
      return this.world;
   }

   @Override
   public SoundCategory getSoundCategory() {
      return SoundCategory.HOSTILE;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_ENDERDRAGON_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_ENDERDRAGON_HURT;
   }

   @Override
   protected float getSoundVolume() {
      return 5.0F;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_ENDER_DRAGON;
   }

   public float getHeadPartYOffset(int var1, double[] var2, double[] var3) {
      IPhase ☃ = this.phaseManager.getCurrentPhase();
      PhaseList<? extends IPhase> ☃x = ☃.getType();
      double ☃xx;
      if (☃x == PhaseList.LANDING || ☃x == PhaseList.TAKEOFF) {
         BlockPos ☃xxx = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
         float ☃xxxx = Math.max(MathHelper.sqrt(this.getDistanceSqToCenter(☃xxx)) / 4.0F, 1.0F);
         ☃xx = ☃ / ☃xxxx;
      } else if (☃.getIsStationary()) {
         ☃xx = ☃;
      } else if (☃ == 6) {
         ☃xx = 0.0;
      } else {
         ☃xx = ☃[1] - ☃[1];
      }

      return (float)☃xx;
   }

   public Vec3d getHeadLookVec(float var1) {
      IPhase ☃ = this.phaseManager.getCurrentPhase();
      PhaseList<? extends IPhase> ☃x = ☃.getType();
      Vec3d ☃xx;
      if (☃x == PhaseList.LANDING || ☃x == PhaseList.TAKEOFF) {
         BlockPos ☃xxx = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
         float ☃xxxx = Math.max(MathHelper.sqrt(this.getDistanceSqToCenter(☃xxx)) / 4.0F, 1.0F);
         float ☃xxxxx = 6.0F / ☃xxxx;
         float ☃xxxxxx = this.rotationPitch;
         float ☃xxxxxxx = 1.5F;
         this.rotationPitch = -☃xxxxx * 1.5F * 5.0F;
         ☃xx = this.getLook(☃);
         this.rotationPitch = ☃xxxxxx;
      } else if (☃.getIsStationary()) {
         float ☃xxx = this.rotationPitch;
         float ☃xxxx = 1.5F;
         this.rotationPitch = -45.0F;
         ☃xx = this.getLook(☃);
         this.rotationPitch = ☃xxx;
      } else {
         ☃xx = this.getLook(☃);
      }

      return ☃xx;
   }

   public void onCrystalDestroyed(EntityEnderCrystal var1, BlockPos var2, DamageSource var3) {
      EntityPlayer ☃;
      if (☃.getTrueSource() instanceof EntityPlayer) {
         ☃ = (EntityPlayer)☃.getTrueSource();
      } else {
         ☃ = this.world.getNearestAttackablePlayer(☃, 64.0, 64.0);
      }

      if (☃ == this.healingEnderCrystal) {
         this.attackEntityFromPart(this.dragonPartHead, DamageSource.causeExplosionDamage(☃), 10.0F);
      }

      this.phaseManager.getCurrentPhase().onCrystalDestroyed(☃, ☃, ☃, ☃);
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      if (PHASE.equals(☃) && this.world.isRemote) {
         this.phaseManager.setPhase(PhaseList.getById(this.getDataManager().get(PHASE)));
      }

      super.notifyDataManagerChange(☃);
   }

   public PhaseManager getPhaseManager() {
      return this.phaseManager;
   }

   @Nullable
   public DragonFightManager getFightManager() {
      return this.fightManager;
   }

   @Override
   public void addPotionEffect(PotionEffect var1) {
   }

   @Override
   protected boolean canBeRidden(Entity var1) {
      return false;
   }

   @Override
   public boolean isNonBoss() {
      return false;
   }
}
