package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFireworkRocket extends Entity {
   private static final DataParameter<ItemStack> FIREWORK_ITEM = EntityDataManager.createKey(EntityFireworkRocket.class, DataSerializers.ITEM_STACK);
   private static final DataParameter<Integer> BOOSTED_ENTITY_ID = EntityDataManager.createKey(EntityFireworkRocket.class, DataSerializers.VARINT);
   private int fireworkAge;
   private int lifetime;
   private EntityLivingBase boostedEntity;

   public EntityFireworkRocket(World var1) {
      super(☃);
      this.setSize(0.25F, 0.25F);
   }

   @Override
   protected void entityInit() {
      this.dataManager.register(FIREWORK_ITEM, ItemStack.EMPTY);
      this.dataManager.register(BOOSTED_ENTITY_ID, 0);
   }

   @Override
   public boolean isInRangeToRenderDist(double var1) {
      return ☃ < 4096.0 && !this.isAttachedToEntity();
   }

   @Override
   public boolean isInRangeToRender3d(double var1, double var3, double var5) {
      return super.isInRangeToRender3d(☃, ☃, ☃) && !this.isAttachedToEntity();
   }

   public EntityFireworkRocket(World var1, double var2, double var4, double var6, ItemStack var8) {
      super(☃);
      this.fireworkAge = 0;
      this.setSize(0.25F, 0.25F);
      this.setPosition(☃, ☃, ☃);
      int ☃ = 1;
      if (!☃.isEmpty() && ☃.hasTagCompound()) {
         this.dataManager.set(FIREWORK_ITEM, ☃.copy());
         NBTTagCompound ☃x = ☃.getTagCompound();
         NBTTagCompound ☃xx = ☃x.getCompoundTag("Fireworks");
         ☃ += ☃xx.getByte("Flight");
      }

      this.motionX = this.rand.nextGaussian() * 0.001;
      this.motionZ = this.rand.nextGaussian() * 0.001;
      this.motionY = 0.05;
      this.lifetime = 10 * ☃ + this.rand.nextInt(6) + this.rand.nextInt(7);
   }

   public EntityFireworkRocket(World var1, ItemStack var2, EntityLivingBase var3) {
      this(☃, ☃.posX, ☃.posY, ☃.posZ, ☃);
      this.dataManager.set(BOOSTED_ENTITY_ID, ☃.getEntityId());
      this.boostedEntity = ☃;
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
      if (this.isAttachedToEntity()) {
         if (this.boostedEntity == null) {
            Entity ☃ = this.world.getEntityByID(this.dataManager.get(BOOSTED_ENTITY_ID));
            if (☃ instanceof EntityLivingBase) {
               this.boostedEntity = (EntityLivingBase)☃;
            }
         }

         if (this.boostedEntity != null) {
            if (this.boostedEntity.isElytraFlying()) {
               Vec3d ☃ = this.boostedEntity.getLookVec();
               double ☃x = 1.5;
               double ☃xx = 0.1;
               this.boostedEntity.motionX = this.boostedEntity.motionX + (☃.x * 0.1 + (☃.x * 1.5 - this.boostedEntity.motionX) * 0.5);
               this.boostedEntity.motionY = this.boostedEntity.motionY + (☃.y * 0.1 + (☃.y * 1.5 - this.boostedEntity.motionY) * 0.5);
               this.boostedEntity.motionZ = this.boostedEntity.motionZ + (☃.z * 0.1 + (☃.z * 1.5 - this.boostedEntity.motionZ) * 0.5);
            }

            this.setPosition(this.boostedEntity.posX, this.boostedEntity.posY, this.boostedEntity.posZ);
            this.motionX = this.boostedEntity.motionX;
            this.motionY = this.boostedEntity.motionY;
            this.motionZ = this.boostedEntity.motionZ;
         }
      } else {
         this.motionX *= 1.15;
         this.motionZ *= 1.15;
         this.motionY += 0.04;
         this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
      }

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
      if (this.fireworkAge == 0 && !this.isSilent()) {
         this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_FIREWORK_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
      }

      this.fireworkAge++;
      if (this.world.isRemote && this.fireworkAge % 2 < 2) {
         this.world
            .spawnParticle(
               EnumParticleTypes.FIREWORKS_SPARK,
               this.posX,
               this.posY - 0.3,
               this.posZ,
               this.rand.nextGaussian() * 0.05,
               -this.motionY * 0.5,
               this.rand.nextGaussian() * 0.05
            );
      }

      if (!this.world.isRemote && this.fireworkAge > this.lifetime) {
         this.world.setEntityState(this, (byte)17);
         this.dealExplosionDamage();
         this.setDead();
      }
   }

   private void dealExplosionDamage() {
      float ☃ = 0.0F;
      ItemStack ☃x = this.dataManager.get(FIREWORK_ITEM);
      NBTTagCompound ☃xx = ☃x.isEmpty() ? null : ☃x.getSubCompound("Fireworks");
      NBTTagList ☃xxx = ☃xx != null ? ☃xx.getTagList("Explosions", 10) : null;
      if (☃xxx != null && !☃xxx.isEmpty()) {
         ☃ = 5 + ☃xxx.tagCount() * 2;
      }

      if (☃ > 0.0F) {
         if (this.boostedEntity != null) {
            this.boostedEntity.attackEntityFrom(DamageSource.FIREWORKS, 5 + ☃xxx.tagCount() * 2);
         }

         double ☃xxxx = 5.0;
         Vec3d ☃xxxxx = new Vec3d(this.posX, this.posY, this.posZ);

         for (EntityLivingBase ☃xxxxxx : this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(5.0))) {
            if (☃xxxxxx != this.boostedEntity && !(this.getDistanceSq(☃xxxxxx) > 25.0)) {
               boolean ☃xxxxxxx = false;

               for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < 2; ☃xxxxxxxx++) {
                  RayTraceResult ☃xxxxxxxxx = this.world
                     .rayTraceBlocks(☃xxxxx, new Vec3d(☃xxxxxx.posX, ☃xxxxxx.posY + ☃xxxxxx.height * 0.5 * ☃xxxxxxxx, ☃xxxxxx.posZ), false, true, false);
                  if (☃xxxxxxxxx == null || ☃xxxxxxxxx.typeOfHit == RayTraceResult.Type.MISS) {
                     ☃xxxxxxx = true;
                     break;
                  }
               }

               if (☃xxxxxxx) {
                  float ☃xxxxxxxxx = ☃ * (float)Math.sqrt((5.0 - this.getDistance(☃xxxxxx)) / 5.0);
                  ☃xxxxxx.attackEntityFrom(DamageSource.FIREWORKS, ☃xxxxxxxxx);
               }
            }
         }
      }
   }

   public boolean isAttachedToEntity() {
      return this.dataManager.get(BOOSTED_ENTITY_ID) > 0;
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 17 && this.world.isRemote) {
         ItemStack ☃ = this.dataManager.get(FIREWORK_ITEM);
         NBTTagCompound ☃x = ☃.isEmpty() ? null : ☃.getSubCompound("Fireworks");
         this.world.makeFireworks(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, ☃x);
      }

      super.handleStatusUpdate(☃);
   }

   public static void registerFixesFireworkRocket(DataFixer var0) {
      ☃.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityFireworkRocket.class, "FireworksItem"));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      ☃.setInteger("Life", this.fireworkAge);
      ☃.setInteger("LifeTime", this.lifetime);
      ItemStack ☃ = this.dataManager.get(FIREWORK_ITEM);
      if (!☃.isEmpty()) {
         ☃.setTag("FireworksItem", ☃.writeToNBT(new NBTTagCompound()));
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      this.fireworkAge = ☃.getInteger("Life");
      this.lifetime = ☃.getInteger("LifeTime");
      NBTTagCompound ☃ = ☃.getCompoundTag("FireworksItem");
      if (☃ != null) {
         ItemStack ☃x = new ItemStack(☃);
         if (!☃x.isEmpty()) {
            this.dataManager.set(FIREWORK_ITEM, ☃x);
         }
      }
   }

   @Override
   public boolean canBeAttackedWithItem() {
      return false;
   }
}
