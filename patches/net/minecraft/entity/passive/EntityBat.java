package net.minecraft.entity.passive;

import java.util.Calendar;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
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
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityBat extends EntityAmbientCreature {
   private static final DataParameter<Byte> HANGING = EntityDataManager.createKey(EntityBat.class, DataSerializers.BYTE);
   private BlockPos spawnPosition;

   public EntityBat(World var1) {
      super(☃);
      this.setSize(0.5F, 0.9F);
      this.setIsBatHanging(true);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(HANGING, (byte)0);
   }

   @Override
   protected float getSoundVolume() {
      return 0.1F;
   }

   @Override
   protected float getSoundPitch() {
      return super.getSoundPitch() * 0.95F;
   }

   @Nullable
   @Override
   public SoundEvent getAmbientSound() {
      return this.getIsBatHanging() && this.rand.nextInt(4) != 0 ? null : SoundEvents.ENTITY_BAT_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_BAT_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_BAT_DEATH;
   }

   @Override
   public boolean canBePushed() {
      return false;
   }

   @Override
   protected void collideWithEntity(Entity var1) {
   }

   @Override
   protected void collideWithNearbyEntities() {
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0);
   }

   public boolean getIsBatHanging() {
      return (this.dataManager.get(HANGING) & 1) != 0;
   }

   public void setIsBatHanging(boolean var1) {
      byte ☃ = this.dataManager.get(HANGING);
      if (☃) {
         this.dataManager.set(HANGING, (byte)(☃ | 1));
      } else {
         this.dataManager.set(HANGING, (byte)(☃ & -2));
      }
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.getIsBatHanging()) {
         this.motionX = 0.0;
         this.motionY = 0.0;
         this.motionZ = 0.0;
         this.posY = MathHelper.floor(this.posY) + 1.0 - this.height;
      } else {
         this.motionY *= 0.6F;
      }
   }

   @Override
   protected void updateAITasks() {
      super.updateAITasks();
      BlockPos ☃ = new BlockPos(this);
      BlockPos ☃x = ☃.up();
      if (this.getIsBatHanging()) {
         if (this.world.getBlockState(☃x).isNormalCube()) {
            if (this.rand.nextInt(200) == 0) {
               this.rotationYawHead = this.rand.nextInt(360);
            }

            if (this.world.getNearestPlayerNotCreative(this, 4.0) != null) {
               this.setIsBatHanging(false);
               this.world.playEvent(null, 1025, ☃, 0);
            }
         } else {
            this.setIsBatHanging(false);
            this.world.playEvent(null, 1025, ☃, 0);
         }
      } else {
         if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
            this.spawnPosition = null;
         }

         if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0) {
            this.spawnPosition = new BlockPos(
               (int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7),
               (int)this.posY + this.rand.nextInt(6) - 2,
               (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7)
            );
         }

         double ☃xx = this.spawnPosition.getX() + 0.5 - this.posX;
         double ☃xxx = this.spawnPosition.getY() + 0.1 - this.posY;
         double ☃xxxx = this.spawnPosition.getZ() + 0.5 - this.posZ;
         this.motionX = this.motionX + (Math.signum(☃xx) * 0.5 - this.motionX) * 0.1F;
         this.motionY = this.motionY + (Math.signum(☃xxx) * 0.7F - this.motionY) * 0.1F;
         this.motionZ = this.motionZ + (Math.signum(☃xxxx) * 0.5 - this.motionZ) * 0.1F;
         float ☃xxxxx = (float)(MathHelper.atan2(this.motionZ, this.motionX) * 180.0F / (float)Math.PI) - 90.0F;
         float ☃xxxxxx = MathHelper.wrapDegrees(☃xxxxx - this.rotationYaw);
         this.moveForward = 0.5F;
         this.rotationYaw += ☃xxxxxx;
         if (this.rand.nextInt(100) == 0 && this.world.getBlockState(☃x).isNormalCube()) {
            this.setIsBatHanging(true);
         }
      }
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   public void fall(float var1, float var2) {
   }

   @Override
   protected void updateFallState(double var1, boolean var3, IBlockState var4, BlockPos var5) {
   }

   @Override
   public boolean doesEntityNotTriggerPressurePlate() {
      return true;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else {
         if (!this.world.isRemote && this.getIsBatHanging()) {
            this.setIsBatHanging(false);
         }

         return super.attackEntityFrom(☃, ☃);
      }
   }

   public static void registerFixesBat(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityBat.class);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.dataManager.set(HANGING, ☃.getByte("BatFlags"));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setByte("BatFlags", this.dataManager.get(HANGING));
   }

   @Override
   public boolean getCanSpawnHere() {
      BlockPos ☃ = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
      if (☃.getY() >= this.world.getSeaLevel()) {
         return false;
      } else {
         int ☃x = this.world.getLightFromNeighbors(☃);
         int ☃xx = 4;
         if (this.isDateAroundHalloween(this.world.getCurrentDate())) {
            ☃xx = 7;
         } else if (this.rand.nextBoolean()) {
            return false;
         }

         return ☃x > this.rand.nextInt(☃xx) ? false : super.getCanSpawnHere();
      }
   }

   private boolean isDateAroundHalloween(Calendar var1) {
      return ☃.get(2) + 1 == 10 && ☃.get(5) >= 20 || ☃.get(2) + 1 == 11 && ☃.get(5) <= 3;
   }

   @Override
   public float getEyeHeight() {
      return this.height / 2.0F;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_BAT;
   }
}
