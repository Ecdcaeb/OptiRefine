package net.minecraft.entity.projectile;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityLlamaSpit extends Entity implements IProjectile {
   public EntityLlama owner;
   private NBTTagCompound ownerNbt;

   public EntityLlamaSpit(World var1) {
      super(☃);
   }

   public EntityLlamaSpit(World var1, EntityLlama var2) {
      super(☃);
      this.owner = ☃;
      this.setPosition(
         ☃.posX - (☃.width + 1.0F) * 0.5 * MathHelper.sin(☃.renderYawOffset * (float) (Math.PI / 180.0)),
         ☃.posY + ☃.getEyeHeight() - 0.1F,
         ☃.posZ + (☃.width + 1.0F) * 0.5 * MathHelper.cos(☃.renderYawOffset * (float) (Math.PI / 180.0))
      );
      this.setSize(0.25F, 0.25F);
   }

   public EntityLlamaSpit(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃);
      this.setPosition(☃, ☃, ☃);

      for (int ☃ = 0; ☃ < 7; ☃++) {
         double ☃x = 0.4 + 0.1 * ☃;
         ☃.spawnParticle(EnumParticleTypes.SPIT, ☃, ☃, ☃, ☃ * ☃x, ☃, ☃ * ☃x);
      }

      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.ownerNbt != null) {
         this.restoreOwnerFromSave();
      }

      Vec3d ☃ = new Vec3d(this.posX, this.posY, this.posZ);
      Vec3d ☃x = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      RayTraceResult ☃xx = this.world.rayTraceBlocks(☃, ☃x);
      ☃ = new Vec3d(this.posX, this.posY, this.posZ);
      ☃x = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      if (☃xx != null) {
         ☃x = new Vec3d(☃xx.hitVec.x, ☃xx.hitVec.y, ☃xx.hitVec.z);
      }

      Entity ☃xxx = this.getHitEntity(☃, ☃x);
      if (☃xxx != null) {
         ☃xx = new RayTraceResult(☃xxx);
      }

      if (☃xx != null) {
         this.onHit(☃xx);
      }

      this.posX = this.posX + this.motionX;
      this.posY = this.posY + this.motionY;
      this.posZ = this.posZ + this.motionZ;
      float ☃xxxx = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0F / (float)Math.PI);
      this.rotationPitch = (float)(MathHelper.atan2(this.motionY, ☃xxxx) * 180.0F / (float)Math.PI);

      while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
         this.prevRotationPitch -= 360.0F;
      }

      while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
         this.prevRotationPitch += 360.0F;
      }

      while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
         this.prevRotationYaw -= 360.0F;
      }

      while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
         this.prevRotationYaw += 360.0F;
      }

      this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
      this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
      float ☃xxxxx = 0.99F;
      float ☃xxxxxx = 0.06F;
      if (!this.world.isMaterialInBB(this.getEntityBoundingBox(), Material.AIR)) {
         this.setDead();
      } else if (this.isInWater()) {
         this.setDead();
      } else {
         this.motionX *= 0.99F;
         this.motionY *= 0.99F;
         this.motionZ *= 0.99F;
         if (!this.hasNoGravity()) {
            this.motionY -= 0.06F;
         }

         this.setPosition(this.posX, this.posY, this.posZ);
      }
   }

   @Override
   public void setVelocity(double var1, double var3, double var5) {
      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
      if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
         float ☃ = MathHelper.sqrt(☃ * ☃ + ☃ * ☃);
         this.rotationPitch = (float)(MathHelper.atan2(☃, ☃) * 180.0F / (float)Math.PI);
         this.rotationYaw = (float)(MathHelper.atan2(☃, ☃) * 180.0F / (float)Math.PI);
         this.prevRotationPitch = this.rotationPitch;
         this.prevRotationYaw = this.rotationYaw;
         this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
      }
   }

   @Nullable
   private Entity getHitEntity(Vec3d var1, Vec3d var2) {
      Entity ☃ = null;
      List<Entity> ☃x = this.world
         .getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0));
      double ☃xx = 0.0;

      for (Entity ☃xxx : ☃x) {
         if (☃xxx != this.owner) {
            AxisAlignedBB ☃xxxx = ☃xxx.getEntityBoundingBox().grow(0.3F);
            RayTraceResult ☃xxxxx = ☃xxxx.calculateIntercept(☃, ☃);
            if (☃xxxxx != null) {
               double ☃xxxxxx = ☃.squareDistanceTo(☃xxxxx.hitVec);
               if (☃xxxxxx < ☃xx || ☃xx == 0.0) {
                  ☃ = ☃xxx;
                  ☃xx = ☃xxxxxx;
               }
            }
         }
      }

      return ☃;
   }

   @Override
   public void shoot(double var1, double var3, double var5, float var7, float var8) {
      float ☃ = MathHelper.sqrt(☃ * ☃ + ☃ * ☃ + ☃ * ☃);
      ☃ /= ☃;
      ☃ /= ☃;
      ☃ /= ☃;
      ☃ += this.rand.nextGaussian() * 0.0075F * ☃;
      ☃ += this.rand.nextGaussian() * 0.0075F * ☃;
      ☃ += this.rand.nextGaussian() * 0.0075F * ☃;
      ☃ *= ☃;
      ☃ *= ☃;
      ☃ *= ☃;
      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
      float ☃x = MathHelper.sqrt(☃ * ☃ + ☃ * ☃);
      this.rotationYaw = (float)(MathHelper.atan2(☃, ☃) * 180.0F / (float)Math.PI);
      this.rotationPitch = (float)(MathHelper.atan2(☃, ☃x) * 180.0F / (float)Math.PI);
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
   }

   public void onHit(RayTraceResult var1) {
      if (☃.entityHit != null && this.owner != null) {
         ☃.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.owner).setProjectile(), 1.0F);
      }

      if (!this.world.isRemote) {
         this.setDead();
      }
   }

   @Override
   protected void entityInit() {
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      if (☃.hasKey("Owner", 10)) {
         this.ownerNbt = ☃.getCompoundTag("Owner");
      }
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      if (this.owner != null) {
         NBTTagCompound ☃ = new NBTTagCompound();
         UUID ☃x = this.owner.getUniqueID();
         ☃.setUniqueId("OwnerUUID", ☃x);
         ☃.setTag("Owner", ☃);
      }
   }

   private void restoreOwnerFromSave() {
      if (this.ownerNbt != null && this.ownerNbt.hasUniqueId("OwnerUUID")) {
         UUID ☃ = this.ownerNbt.getUniqueId("OwnerUUID");

         for (EntityLlama ☃x : this.world.getEntitiesWithinAABB(EntityLlama.class, this.getEntityBoundingBox().grow(15.0))) {
            if (☃x.getUniqueID().equals(☃)) {
               this.owner = ☃x;
               break;
            }
         }
      }

      this.ownerNbt = null;
   }
}
