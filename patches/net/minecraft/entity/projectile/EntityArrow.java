package net.minecraft.entity.projectile;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityArrow extends Entity implements IProjectile {
   private static final Predicate<Entity> ARROW_TARGETS = Predicates.and(
      new Predicate[]{EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
         public boolean apply(@Nullable Entity var1) {
            return ☃.canBeCollidedWith();
         }
      }}
   );
   private static final DataParameter<Byte> CRITICAL = EntityDataManager.createKey(EntityArrow.class, DataSerializers.BYTE);
   private int xTile = -1;
   private int yTile = -1;
   private int zTile = -1;
   private Block inTile;
   private int inData;
   protected boolean inGround;
   protected int timeInGround;
   public EntityArrow.PickupStatus pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
   public int arrowShake;
   public Entity shootingEntity;
   private int ticksInGround;
   private int ticksInAir;
   private double damage = 2.0;
   private int knockbackStrength;

   public EntityArrow(World var1) {
      super(☃);
      this.setSize(0.5F, 0.5F);
   }

   public EntityArrow(World var1, double var2, double var4, double var6) {
      this(☃);
      this.setPosition(☃, ☃, ☃);
   }

   public EntityArrow(World var1, EntityLivingBase var2) {
      this(☃, ☃.posX, ☃.posY + ☃.getEyeHeight() - 0.1F, ☃.posZ);
      this.shootingEntity = ☃;
      if (☃ instanceof EntityPlayer) {
         this.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
      }
   }

   @Override
   public boolean isInRangeToRenderDist(double var1) {
      double ☃ = this.getEntityBoundingBox().getAverageEdgeLength() * 10.0;
      if (Double.isNaN(☃)) {
         ☃ = 1.0;
      }

      ☃ *= 64.0 * getRenderDistanceWeight();
      return ☃ < ☃ * ☃;
   }

   @Override
   protected void entityInit() {
      this.dataManager.register(CRITICAL, (byte)0);
   }

   public void shoot(Entity var1, float var2, float var3, float var4, float var5, float var6) {
      float ☃ = -MathHelper.sin(☃ * (float) (Math.PI / 180.0)) * MathHelper.cos(☃ * (float) (Math.PI / 180.0));
      float ☃x = -MathHelper.sin(☃ * (float) (Math.PI / 180.0));
      float ☃xx = MathHelper.cos(☃ * (float) (Math.PI / 180.0)) * MathHelper.cos(☃ * (float) (Math.PI / 180.0));
      this.shoot(☃, ☃x, ☃xx, ☃, ☃);
      this.motionX = this.motionX + ☃.motionX;
      this.motionZ = this.motionZ + ☃.motionZ;
      if (!☃.onGround) {
         this.motionY = this.motionY + ☃.motionY;
      }
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
      this.ticksInGround = 0;
   }

   @Override
   public void setPositionAndRotationDirect(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
      this.setPosition(☃, ☃, ☃);
      this.setRotation(☃, ☃);
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
         this.ticksInGround = 0;
      }
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
         float ☃ = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0F / (float)Math.PI);
         this.rotationPitch = (float)(MathHelper.atan2(this.motionY, ☃) * 180.0F / (float)Math.PI);
         this.prevRotationYaw = this.rotationYaw;
         this.prevRotationPitch = this.rotationPitch;
      }

      BlockPos ☃ = new BlockPos(this.xTile, this.yTile, this.zTile);
      IBlockState ☃x = this.world.getBlockState(☃);
      Block ☃xx = ☃x.getBlock();
      if (☃x.getMaterial() != Material.AIR) {
         AxisAlignedBB ☃xxx = ☃x.getCollisionBoundingBox(this.world, ☃);
         if (☃xxx != Block.NULL_AABB && ☃xxx.offset(☃).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
            this.inGround = true;
         }
      }

      if (this.arrowShake > 0) {
         this.arrowShake--;
      }

      if (this.inGround) {
         int ☃xxx = ☃xx.getMetaFromState(☃x);
         if ((☃xx != this.inTile || ☃xxx != this.inData) && !this.world.collidesWithAnyBlock(this.getEntityBoundingBox().grow(0.05))) {
            this.inGround = false;
            this.motionX = this.motionX * (this.rand.nextFloat() * 0.2F);
            this.motionY = this.motionY * (this.rand.nextFloat() * 0.2F);
            this.motionZ = this.motionZ * (this.rand.nextFloat() * 0.2F);
            this.ticksInGround = 0;
            this.ticksInAir = 0;
         } else {
            this.ticksInGround++;
            if (this.ticksInGround >= 1200) {
               this.setDead();
            }
         }

         this.timeInGround++;
      } else {
         this.timeInGround = 0;
         this.ticksInAir++;
         Vec3d ☃xxx = new Vec3d(this.posX, this.posY, this.posZ);
         Vec3d ☃xxxx = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         RayTraceResult ☃xxxxx = this.world.rayTraceBlocks(☃xxx, ☃xxxx, false, true, false);
         ☃xxx = new Vec3d(this.posX, this.posY, this.posZ);
         ☃xxxx = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         if (☃xxxxx != null) {
            ☃xxxx = new Vec3d(☃xxxxx.hitVec.x, ☃xxxxx.hitVec.y, ☃xxxxx.hitVec.z);
         }

         Entity ☃xxxxxx = this.findEntityOnPath(☃xxx, ☃xxxx);
         if (☃xxxxxx != null) {
            ☃xxxxx = new RayTraceResult(☃xxxxxx);
         }

         if (☃xxxxx != null && ☃xxxxx.entityHit instanceof EntityPlayer) {
            EntityPlayer ☃xxxxxxx = (EntityPlayer)☃xxxxx.entityHit;
            if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(☃xxxxxxx)) {
               ☃xxxxx = null;
            }
         }

         if (☃xxxxx != null) {
            this.onHit(☃xxxxx);
         }

         if (this.getIsCritical()) {
            for (int ☃xxxxxxx = 0; ☃xxxxxxx < 4; ☃xxxxxxx++) {
               this.world
                  .spawnParticle(
                     EnumParticleTypes.CRIT,
                     this.posX + this.motionX * ☃xxxxxxx / 4.0,
                     this.posY + this.motionY * ☃xxxxxxx / 4.0,
                     this.posZ + this.motionZ * ☃xxxxxxx / 4.0,
                     -this.motionX,
                     -this.motionY + 0.2,
                     -this.motionZ
                  );
            }
         }

         this.posX = this.posX + this.motionX;
         this.posY = this.posY + this.motionY;
         this.posZ = this.posZ + this.motionZ;
         float ☃xxxxxxx = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0F / (float)Math.PI);
         this.rotationPitch = (float)(MathHelper.atan2(this.motionY, ☃xxxxxxx) * 180.0F / (float)Math.PI);

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
         float ☃xxxxxxxx = 0.99F;
         float ☃xxxxxxxxx = 0.05F;
         if (this.isInWater()) {
            for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx < 4; ☃xxxxxxxxxx++) {
               float ☃xxxxxxxxxxx = 0.25F;
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

            ☃xxxxxxxx = 0.6F;
         }

         if (this.isWet()) {
            this.extinguish();
         }

         this.motionX *= ☃xxxxxxxx;
         this.motionY *= ☃xxxxxxxx;
         this.motionZ *= ☃xxxxxxxx;
         if (!this.hasNoGravity()) {
            this.motionY -= 0.05F;
         }

         this.setPosition(this.posX, this.posY, this.posZ);
         this.doBlockCollisions();
      }
   }

   protected void onHit(RayTraceResult var1) {
      Entity ☃ = ☃.entityHit;
      if (☃ != null) {
         float ☃x = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
         int ☃xx = MathHelper.ceil(☃x * this.damage);
         if (this.getIsCritical()) {
            ☃xx += this.rand.nextInt(☃xx / 2 + 2);
         }

         DamageSource ☃xxx;
         if (this.shootingEntity == null) {
            ☃xxx = DamageSource.causeArrowDamage(this, this);
         } else {
            ☃xxx = DamageSource.causeArrowDamage(this, this.shootingEntity);
         }

         if (this.isBurning() && !(☃ instanceof EntityEnderman)) {
            ☃.setFire(5);
         }

         if (☃.attackEntityFrom(☃xxx, ☃xx)) {
            if (☃ instanceof EntityLivingBase) {
               EntityLivingBase ☃xxxx = (EntityLivingBase)☃;
               if (!this.world.isRemote) {
                  ☃xxxx.setArrowCountInEntity(☃xxxx.getArrowCountInEntity() + 1);
               }

               if (this.knockbackStrength > 0) {
                  float ☃xxxxx = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                  if (☃xxxxx > 0.0F) {
                     ☃xxxx.addVelocity(this.motionX * this.knockbackStrength * 0.6F / ☃xxxxx, 0.1, this.motionZ * this.knockbackStrength * 0.6F / ☃xxxxx);
                  }
               }

               if (this.shootingEntity instanceof EntityLivingBase) {
                  EnchantmentHelper.applyThornEnchantments(☃xxxx, this.shootingEntity);
                  EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, ☃xxxx);
               }

               this.arrowHit(☃xxxx);
               if (this.shootingEntity != null
                  && ☃xxxx != this.shootingEntity
                  && ☃xxxx instanceof EntityPlayer
                  && this.shootingEntity instanceof EntityPlayerMP) {
                  ((EntityPlayerMP)this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
               }
            }

            this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            if (!(☃ instanceof EntityEnderman)) {
               this.setDead();
            }
         } else {
            this.motionX *= -0.1F;
            this.motionY *= -0.1F;
            this.motionZ *= -0.1F;
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
            this.ticksInAir = 0;
            if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < 0.001F) {
               if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED) {
                  this.entityDropItem(this.getArrowStack(), 0.1F);
               }

               this.setDead();
            }
         }
      } else {
         BlockPos ☃xxxxx = ☃.getBlockPos();
         this.xTile = ☃xxxxx.getX();
         this.yTile = ☃xxxxx.getY();
         this.zTile = ☃xxxxx.getZ();
         IBlockState ☃xxxxxx = this.world.getBlockState(☃xxxxx);
         this.inTile = ☃xxxxxx.getBlock();
         this.inData = this.inTile.getMetaFromState(☃xxxxxx);
         this.motionX = (float)(☃.hitVec.x - this.posX);
         this.motionY = (float)(☃.hitVec.y - this.posY);
         this.motionZ = (float)(☃.hitVec.z - this.posZ);
         float ☃xxxxxxx = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
         this.posX = this.posX - this.motionX / ☃xxxxxxx * 0.05F;
         this.posY = this.posY - this.motionY / ☃xxxxxxx * 0.05F;
         this.posZ = this.posZ - this.motionZ / ☃xxxxxxx * 0.05F;
         this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
         this.inGround = true;
         this.arrowShake = 7;
         this.setIsCritical(false);
         if (☃xxxxxx.getMaterial() != Material.AIR) {
            this.inTile.onEntityCollision(this.world, ☃xxxxx, ☃xxxxxx, this);
         }
      }
   }

   @Override
   public void move(MoverType var1, double var2, double var4, double var6) {
      super.move(☃, ☃, ☃, ☃);
      if (this.inGround) {
         this.xTile = MathHelper.floor(this.posX);
         this.yTile = MathHelper.floor(this.posY);
         this.zTile = MathHelper.floor(this.posZ);
      }
   }

   protected void arrowHit(EntityLivingBase var1) {
   }

   @Nullable
   protected Entity findEntityOnPath(Vec3d var1, Vec3d var2) {
      Entity ☃ = null;
      List<Entity> ☃x = this.world
         .getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0), ARROW_TARGETS);
      double ☃xx = 0.0;

      for (int ☃xxx = 0; ☃xxx < ☃x.size(); ☃xxx++) {
         Entity ☃xxxx = ☃x.get(☃xxx);
         if (☃xxxx != this.shootingEntity || this.ticksInAir >= 5) {
            AxisAlignedBB ☃xxxxx = ☃xxxx.getEntityBoundingBox().grow(0.3F);
            RayTraceResult ☃xxxxxx = ☃xxxxx.calculateIntercept(☃, ☃);
            if (☃xxxxxx != null) {
               double ☃xxxxxxx = ☃.squareDistanceTo(☃xxxxxx.hitVec);
               if (☃xxxxxxx < ☃xx || ☃xx == 0.0) {
                  ☃ = ☃xxxx;
                  ☃xx = ☃xxxxxxx;
               }
            }
         }
      }

      return ☃;
   }

   public static void registerFixesArrow(DataFixer var0, String var1) {
   }

   public static void registerFixesArrow(DataFixer var0) {
      registerFixesArrow(☃, "Arrow");
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      ☃.setInteger("xTile", this.xTile);
      ☃.setInteger("yTile", this.yTile);
      ☃.setInteger("zTile", this.zTile);
      ☃.setShort("life", (short)this.ticksInGround);
      ResourceLocation ☃ = Block.REGISTRY.getNameForObject(this.inTile);
      ☃.setString("inTile", ☃ == null ? "" : ☃.toString());
      ☃.setByte("inData", (byte)this.inData);
      ☃.setByte("shake", (byte)this.arrowShake);
      ☃.setByte("inGround", (byte)(this.inGround ? 1 : 0));
      ☃.setByte("pickup", (byte)this.pickupStatus.ordinal());
      ☃.setDouble("damage", this.damage);
      ☃.setBoolean("crit", this.getIsCritical());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      this.xTile = ☃.getInteger("xTile");
      this.yTile = ☃.getInteger("yTile");
      this.zTile = ☃.getInteger("zTile");
      this.ticksInGround = ☃.getShort("life");
      if (☃.hasKey("inTile", 8)) {
         this.inTile = Block.getBlockFromName(☃.getString("inTile"));
      } else {
         this.inTile = Block.getBlockById(☃.getByte("inTile") & 255);
      }

      this.inData = ☃.getByte("inData") & 255;
      this.arrowShake = ☃.getByte("shake") & 255;
      this.inGround = ☃.getByte("inGround") == 1;
      if (☃.hasKey("damage", 99)) {
         this.damage = ☃.getDouble("damage");
      }

      if (☃.hasKey("pickup", 99)) {
         this.pickupStatus = EntityArrow.PickupStatus.getByOrdinal(☃.getByte("pickup"));
      } else if (☃.hasKey("player", 99)) {
         this.pickupStatus = ☃.getBoolean("player") ? EntityArrow.PickupStatus.ALLOWED : EntityArrow.PickupStatus.DISALLOWED;
      }

      this.setIsCritical(☃.getBoolean("crit"));
   }

   @Override
   public void onCollideWithPlayer(EntityPlayer var1) {
      if (!this.world.isRemote && this.inGround && this.arrowShake <= 0) {
         boolean ☃ = this.pickupStatus == EntityArrow.PickupStatus.ALLOWED
            || this.pickupStatus == EntityArrow.PickupStatus.CREATIVE_ONLY && ☃.capabilities.isCreativeMode;
         if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED && !☃.inventory.addItemStackToInventory(this.getArrowStack())) {
            ☃ = false;
         }

         if (☃) {
            ☃.onItemPickup(this, 1);
            this.setDead();
         }
      }
   }

   protected abstract ItemStack getArrowStack();

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   public void setDamage(double var1) {
      this.damage = ☃;
   }

   public double getDamage() {
      return this.damage;
   }

   public void setKnockbackStrength(int var1) {
      this.knockbackStrength = ☃;
   }

   @Override
   public boolean canBeAttackedWithItem() {
      return false;
   }

   @Override
   public float getEyeHeight() {
      return 0.0F;
   }

   public void setIsCritical(boolean var1) {
      byte ☃ = this.dataManager.get(CRITICAL);
      if (☃) {
         this.dataManager.set(CRITICAL, (byte)(☃ | 1));
      } else {
         this.dataManager.set(CRITICAL, (byte)(☃ & -2));
      }
   }

   public boolean getIsCritical() {
      byte ☃ = this.dataManager.get(CRITICAL);
      return (☃ & 1) != 0;
   }

   public void setEnchantmentEffectsFromEntity(EntityLivingBase var1, float var2) {
      int ☃ = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, ☃);
      int ☃x = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, ☃);
      this.setDamage(☃ * 2.0F + (this.rand.nextGaussian() * 0.25 + this.world.getDifficulty().getId() * 0.11F));
      if (☃ > 0) {
         this.setDamage(this.getDamage() + ☃ * 0.5 + 0.5);
      }

      if (☃x > 0) {
         this.setKnockbackStrength(☃x);
      }

      if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, ☃) > 0) {
         this.setFire(100);
      }
   }

   public static enum PickupStatus {
      DISALLOWED,
      ALLOWED,
      CREATIVE_ONLY;

      public static EntityArrow.PickupStatus getByOrdinal(int var0) {
         if (☃ < 0 || ☃ > values().length) {
            ☃ = 0;
         }

         return values()[☃];
      }
   }
}
