package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityFireball extends Entity {
   public EntityLivingBase shootingEntity;
   private int ticksAlive;
   private int ticksInAir;
   public double accelerationX;
   public double accelerationY;
   public double accelerationZ;

   public EntityFireball(World var1) {
      super(☃);
      this.setSize(1.0F, 1.0F);
   }

   @Override
   protected void entityInit() {
   }

   @Override
   public boolean isInRangeToRenderDist(double var1) {
      double ☃ = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
      if (Double.isNaN(☃)) {
         ☃ = 4.0;
      }

      ☃ *= 64.0;
      return ☃ < ☃ * ☃;
   }

   public EntityFireball(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃);
      this.setSize(1.0F, 1.0F);
      this.setLocationAndAngles(☃, ☃, ☃, this.rotationYaw, this.rotationPitch);
      this.setPosition(☃, ☃, ☃);
      double ☃ = MathHelper.sqrt(☃ * ☃ + ☃ * ☃ + ☃ * ☃);
      this.accelerationX = ☃ / ☃ * 0.1;
      this.accelerationY = ☃ / ☃ * 0.1;
      this.accelerationZ = ☃ / ☃ * 0.1;
   }

   public EntityFireball(World var1, EntityLivingBase var2, double var3, double var5, double var7) {
      super(☃);
      this.shootingEntity = ☃;
      this.setSize(1.0F, 1.0F);
      this.setLocationAndAngles(☃.posX, ☃.posY, ☃.posZ, ☃.rotationYaw, ☃.rotationPitch);
      this.setPosition(this.posX, this.posY, this.posZ);
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      ☃ += this.rand.nextGaussian() * 0.4;
      ☃ += this.rand.nextGaussian() * 0.4;
      ☃ += this.rand.nextGaussian() * 0.4;
      double ☃ = MathHelper.sqrt(☃ * ☃ + ☃ * ☃ + ☃ * ☃);
      this.accelerationX = ☃ / ☃ * 0.1;
      this.accelerationY = ☃ / ☃ * 0.1;
      this.accelerationZ = ☃ / ☃ * 0.1;
   }

   @Override
   public void onUpdate() {
      if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this))) {
         super.onUpdate();
         if (this.isFireballFiery()) {
            this.setFire(1);
         }

         this.ticksInAir++;
         RayTraceResult ☃ = ProjectileHelper.forwardsRaycast(this, true, this.ticksInAir >= 25, this.shootingEntity);
         if (☃ != null) {
            this.onImpact(☃);
         }

         this.posX = this.posX + this.motionX;
         this.posY = this.posY + this.motionY;
         this.posZ = this.posZ + this.motionZ;
         ProjectileHelper.rotateTowardsMovement(this, 0.2F);
         float ☃x = this.getMotionFactor();
         if (this.isInWater()) {
            for (int ☃xx = 0; ☃xx < 4; ☃xx++) {
               float ☃xxx = 0.25F;
               this.world
                  .spawnParticle(
                     EnumParticleTypes.WATER_BUBBLE,
                     this.posX - this.motionX * 0.25,
                     this.posY - this.motionY * 0.25,
                     this.posZ - this.motionZ * 0.25,
                     this.motionX,
                     this.motionY,
                     this.motionZ
                  );
            }

            ☃x = 0.8F;
         }

         this.motionX = this.motionX + this.accelerationX;
         this.motionY = this.motionY + this.accelerationY;
         this.motionZ = this.motionZ + this.accelerationZ;
         this.motionX *= ☃x;
         this.motionY *= ☃x;
         this.motionZ *= ☃x;
         this.world.spawnParticle(this.getParticleType(), this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0);
         this.setPosition(this.posX, this.posY, this.posZ);
      } else {
         this.setDead();
      }
   }

   protected boolean isFireballFiery() {
      return true;
   }

   protected EnumParticleTypes getParticleType() {
      return EnumParticleTypes.SMOKE_NORMAL;
   }

   protected float getMotionFactor() {
      return 0.95F;
   }

   protected abstract void onImpact(RayTraceResult var1);

   public static void registerFixesFireball(DataFixer var0, String var1) {
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      ☃.setTag("direction", this.newDoubleNBTList(new double[]{this.motionX, this.motionY, this.motionZ}));
      ☃.setTag("power", this.newDoubleNBTList(new double[]{this.accelerationX, this.accelerationY, this.accelerationZ}));
      ☃.setInteger("life", this.ticksAlive);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      if (☃.hasKey("power", 9)) {
         NBTTagList ☃ = ☃.getTagList("power", 6);
         if (☃.tagCount() == 3) {
            this.accelerationX = ☃.getDoubleAt(0);
            this.accelerationY = ☃.getDoubleAt(1);
            this.accelerationZ = ☃.getDoubleAt(2);
         }
      }

      this.ticksAlive = ☃.getInteger("life");
      if (☃.hasKey("direction", 9) && ☃.getTagList("direction", 6).tagCount() == 3) {
         NBTTagList ☃ = ☃.getTagList("direction", 6);
         this.motionX = ☃.getDoubleAt(0);
         this.motionY = ☃.getDoubleAt(1);
         this.motionZ = ☃.getDoubleAt(2);
      } else {
         this.setDead();
      }
   }

   @Override
   public boolean canBeCollidedWith() {
      return true;
   }

   @Override
   public float getCollisionBorderSize() {
      return 1.0F;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else {
         this.markVelocityChanged();
         if (☃.getTrueSource() != null) {
            Vec3d ☃ = ☃.getTrueSource().getLookVec();
            if (☃ != null) {
               this.motionX = ☃.x;
               this.motionY = ☃.y;
               this.motionZ = ☃.z;
               this.accelerationX = this.motionX * 0.1;
               this.accelerationY = this.motionY * 0.1;
               this.accelerationZ = this.motionZ * 0.1;
            }

            if (☃.getTrueSource() instanceof EntityLivingBase) {
               this.shootingEntity = (EntityLivingBase)☃.getTrueSource();
            }

            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public float getBrightness() {
      return 1.0F;
   }

   @Override
   public int getBrightnessForRender() {
      return 15728880;
   }
}
