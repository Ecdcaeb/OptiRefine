package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityTNTPrimed extends Entity {
   private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(EntityTNTPrimed.class, DataSerializers.VARINT);
   @Nullable
   private EntityLivingBase tntPlacedBy;
   private int fuse = 80;

   public EntityTNTPrimed(World var1) {
      super(☃);
      this.preventEntitySpawning = true;
      this.isImmuneToFire = true;
      this.setSize(0.98F, 0.98F);
   }

   public EntityTNTPrimed(World var1, double var2, double var4, double var6, EntityLivingBase var8) {
      this(☃);
      this.setPosition(☃, ☃, ☃);
      float ☃ = (float)(Math.random() * (float) (Math.PI * 2));
      this.motionX = -((float)Math.sin(☃)) * 0.02F;
      this.motionY = 0.2F;
      this.motionZ = -((float)Math.cos(☃)) * 0.02F;
      this.setFuse(80);
      this.prevPosX = ☃;
      this.prevPosY = ☃;
      this.prevPosZ = ☃;
      this.tntPlacedBy = ☃;
   }

   @Override
   protected void entityInit() {
      this.dataManager.register(FUSE, 80);
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (!this.hasNoGravity()) {
         this.motionY -= 0.04F;
      }

      this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.98F;
      this.motionY *= 0.98F;
      this.motionZ *= 0.98F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
         this.motionY *= -0.5;
      }

      this.fuse--;
      if (this.fuse <= 0) {
         this.setDead();
         if (!this.world.isRemote) {
            this.explode();
         }
      } else {
         this.handleWaterMovement();
         this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0);
      }
   }

   private void explode() {
      float ☃ = 4.0F;
      this.world.createExplosion(this, this.posX, this.posY + this.height / 16.0F, this.posZ, 4.0F, true);
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      ☃.setShort("Fuse", (short)this.getFuse());
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      this.setFuse(☃.getShort("Fuse"));
   }

   @Nullable
   public EntityLivingBase getTntPlacedBy() {
      return this.tntPlacedBy;
   }

   @Override
   public float getEyeHeight() {
      return 0.0F;
   }

   public void setFuse(int var1) {
      this.dataManager.set(FUSE, ☃);
      this.fuse = ☃;
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      if (FUSE.equals(☃)) {
         this.fuse = this.getFuseDataManager();
      }
   }

   public int getFuseDataManager() {
      return this.dataManager.get(FUSE);
   }

   public int getFuse() {
      return this.fuse;
   }
}
