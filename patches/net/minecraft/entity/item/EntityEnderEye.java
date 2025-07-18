package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityEnderEye extends Entity {
   private double targetX;
   private double targetY;
   private double targetZ;
   private int despawnTimer;
   private boolean shatterOrDrop;

   public EntityEnderEye(World var1) {
      super(☃);
      this.setSize(0.25F, 0.25F);
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

   public EntityEnderEye(World var1, double var2, double var4, double var6) {
      super(☃);
      this.despawnTimer = 0;
      this.setSize(0.25F, 0.25F);
      this.setPosition(☃, ☃, ☃);
   }

   public void moveTowards(BlockPos var1) {
      double ☃ = ☃.getX();
      int ☃x = ☃.getY();
      double ☃xx = ☃.getZ();
      double ☃xxx = ☃ - this.posX;
      double ☃xxxx = ☃xx - this.posZ;
      float ☃xxxxx = MathHelper.sqrt(☃xxx * ☃xxx + ☃xxxx * ☃xxxx);
      if (☃xxxxx > 12.0F) {
         this.targetX = this.posX + ☃xxx / ☃xxxxx * 12.0;
         this.targetZ = this.posZ + ☃xxxx / ☃xxxxx * 12.0;
         this.targetY = this.posY + 8.0;
      } else {
         this.targetX = ☃;
         this.targetY = ☃x;
         this.targetZ = ☃xx;
      }

      this.despawnTimer = 0;
      this.shatterOrDrop = this.rand.nextInt(5) > 0;
   }

   @Override
   public void setVelocity(double var1, double var3, double var5) {
      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
      if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
         float ☃ = MathHelper.sqrt(☃ * ☃ + ☃ * ☃);
         this.rotationYaw = (float)(MathHelper.atan2(☃, ☃) * 180.0F / (float)Math.PI);
         this.rotationPitch = (float)(MathHelper.atan2(☃, ☃) * 180.0F / (float)Math.PI);
         this.prevRotationYaw = this.rotationYaw;
         this.prevRotationPitch = this.rotationPitch;
      }
   }

   @Override
   public void onUpdate() {
      this.lastTickPosX = this.posX;
      this.lastTickPosY = this.posY;
      this.lastTickPosZ = this.posZ;
      super.onUpdate();
      this.posX = this.posX + this.motionX;
      this.posY = this.posY + this.motionY;
      this.posZ = this.posZ + this.motionZ;
      float ☃ = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0F / (float)Math.PI);
      this.rotationPitch = (float)(MathHelper.atan2(this.motionY, ☃) * 180.0F / (float)Math.PI);

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
      if (!this.world.isRemote) {
         double ☃x = this.targetX - this.posX;
         double ☃xx = this.targetZ - this.posZ;
         float ☃xxx = (float)Math.sqrt(☃x * ☃x + ☃xx * ☃xx);
         float ☃xxxx = (float)MathHelper.atan2(☃xx, ☃x);
         double ☃xxxxx = ☃ + (☃xxx - ☃) * 0.0025;
         if (☃xxx < 1.0F) {
            ☃xxxxx *= 0.8;
            this.motionY *= 0.8;
         }

         this.motionX = Math.cos(☃xxxx) * ☃xxxxx;
         this.motionZ = Math.sin(☃xxxx) * ☃xxxxx;
         if (this.posY < this.targetY) {
            this.motionY = this.motionY + (1.0 - this.motionY) * 0.015F;
         } else {
            this.motionY = this.motionY + (-1.0 - this.motionY) * 0.015F;
         }
      }

      float ☃xxxxxx = 0.25F;
      if (this.isInWater()) {
         for (int ☃xxxxxxx = 0; ☃xxxxxxx < 4; ☃xxxxxxx++) {
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
      } else {
         this.world
            .spawnParticle(
               EnumParticleTypes.PORTAL,
               this.posX - this.motionX * 0.25 + this.rand.nextDouble() * 0.6 - 0.3,
               this.posY - this.motionY * 0.25 - 0.5,
               this.posZ - this.motionZ * 0.25 + this.rand.nextDouble() * 0.6 - 0.3,
               this.motionX,
               this.motionY,
               this.motionZ
            );
      }

      if (!this.world.isRemote) {
         this.setPosition(this.posX, this.posY, this.posZ);
         this.despawnTimer++;
         if (this.despawnTimer > 80 && !this.world.isRemote) {
            this.playSound(SoundEvents.ENTITY_ENDEREYE_DEATH, 1.0F, 1.0F);
            this.setDead();
            if (this.shatterOrDrop) {
               this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, new ItemStack(Items.ENDER_EYE)));
            } else {
               this.world.playEvent(2003, new BlockPos(this), 0);
            }
         }
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
   }

   @Override
   public float getBrightness() {
      return 1.0F;
   }

   @Override
   public int getBrightnessForRender() {
      return 15728880;
   }

   @Override
   public boolean canBeAttackedWithItem() {
      return false;
   }
}
