package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySquid extends EntityWaterMob {
   public float squidPitch;
   public float prevSquidPitch;
   public float squidYaw;
   public float prevSquidYaw;
   public float squidRotation;
   public float prevSquidRotation;
   public float tentacleAngle;
   public float lastTentacleAngle;
   private float randomMotionSpeed;
   private float rotationVelocity;
   private float rotateSpeed;
   private float randomMotionVecX;
   private float randomMotionVecY;
   private float randomMotionVecZ;

   public EntitySquid(World var1) {
      super(☃);
      this.setSize(0.8F, 0.8F);
      this.rand.setSeed(1 + this.getEntityId());
      this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
   }

   public static void registerFixesSquid(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntitySquid.class);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(0, new EntitySquid.AIMoveRandom(this));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
   }

   @Override
   public float getEyeHeight() {
      return this.height * 0.5F;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_SQUID_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_SQUID_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_SQUID_DEATH;
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F;
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_SQUID;
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      this.prevSquidPitch = this.squidPitch;
      this.prevSquidYaw = this.squidYaw;
      this.prevSquidRotation = this.squidRotation;
      this.lastTentacleAngle = this.tentacleAngle;
      this.squidRotation = this.squidRotation + this.rotationVelocity;
      if (this.squidRotation > Math.PI * 2) {
         if (this.world.isRemote) {
            this.squidRotation = (float) (Math.PI * 2);
         } else {
            this.squidRotation = (float)(this.squidRotation - (Math.PI * 2));
            if (this.rand.nextInt(10) == 0) {
               this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
            }

            this.world.setEntityState(this, (byte)19);
         }
      }

      if (this.inWater) {
         if (this.squidRotation < (float) Math.PI) {
            float ☃ = this.squidRotation / (float) Math.PI;
            this.tentacleAngle = MathHelper.sin(☃ * ☃ * (float) Math.PI) * (float) Math.PI * 0.25F;
            if (☃ > 0.75) {
               this.randomMotionSpeed = 1.0F;
               this.rotateSpeed = 1.0F;
            } else {
               this.rotateSpeed *= 0.8F;
            }
         } else {
            this.tentacleAngle = 0.0F;
            this.randomMotionSpeed *= 0.9F;
            this.rotateSpeed *= 0.99F;
         }

         if (!this.world.isRemote) {
            this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
            this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
            this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
         }

         float ☃ = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         this.renderYawOffset = this.renderYawOffset
            + (-((float)MathHelper.atan2(this.motionX, this.motionZ)) * (180.0F / (float)Math.PI) - this.renderYawOffset) * 0.1F;
         this.rotationYaw = this.renderYawOffset;
         this.squidYaw = (float)(this.squidYaw + Math.PI * this.rotateSpeed * 1.5);
         this.squidPitch = this.squidPitch + (-((float)MathHelper.atan2(☃, this.motionY)) * (180.0F / (float)Math.PI) - this.squidPitch) * 0.1F;
      } else {
         this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * (float) Math.PI * 0.25F;
         if (!this.world.isRemote) {
            this.motionX = 0.0;
            this.motionZ = 0.0;
            if (this.isPotionActive(MobEffects.LEVITATION)) {
               this.motionY = this.motionY + (0.05 * (this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY);
            } else if (!this.hasNoGravity()) {
               this.motionY -= 0.08;
            }

            this.motionY *= 0.98F;
         }

         this.squidPitch = (float)(this.squidPitch + (-90.0F - this.squidPitch) * 0.02);
      }
   }

   @Override
   public void travel(float var1, float var2, float var3) {
      this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
   }

   @Override
   public boolean getCanSpawnHere() {
      return this.posY > 45.0 && this.posY < this.world.getSeaLevel() && super.getCanSpawnHere();
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 19) {
         this.squidRotation = 0.0F;
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   public void setMovementVector(float var1, float var2, float var3) {
      this.randomMotionVecX = ☃;
      this.randomMotionVecY = ☃;
      this.randomMotionVecZ = ☃;
   }

   public boolean hasMovementVector() {
      return this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F;
   }

   static class AIMoveRandom extends EntityAIBase {
      private final EntitySquid squid;

      public AIMoveRandom(EntitySquid var1) {
         this.squid = ☃;
      }

      @Override
      public boolean shouldExecute() {
         return true;
      }

      @Override
      public void updateTask() {
         int ☃ = this.squid.getIdleTime();
         if (☃ > 100) {
            this.squid.setMovementVector(0.0F, 0.0F, 0.0F);
         } else if (this.squid.getRNG().nextInt(50) == 0 || !this.squid.inWater || !this.squid.hasMovementVector()) {
            float ☃x = this.squid.getRNG().nextFloat() * (float) (Math.PI * 2);
            float ☃xx = MathHelper.cos(☃x) * 0.2F;
            float ☃xxx = -0.1F + this.squid.getRNG().nextFloat() * 0.2F;
            float ☃xxxx = MathHelper.sin(☃x) * 0.2F;
            this.squid.setMovementVector(☃xx, ☃xxx, ☃xxxx);
         }
      }
   }
}
