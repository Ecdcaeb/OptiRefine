package net.minecraft.entity.projectile;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityShulkerBullet extends Entity {
   private EntityLivingBase owner;
   private Entity target;
   @Nullable
   private EnumFacing direction;
   private int steps;
   private double targetDeltaX;
   private double targetDeltaY;
   private double targetDeltaZ;
   @Nullable
   private UUID ownerUniqueId;
   private BlockPos ownerBlockPos;
   @Nullable
   private UUID targetUniqueId;
   private BlockPos targetBlockPos;

   public EntityShulkerBullet(World var1) {
      super(☃);
      this.setSize(0.3125F, 0.3125F);
      this.noClip = true;
   }

   @Override
   public SoundCategory getSoundCategory() {
      return SoundCategory.HOSTILE;
   }

   public EntityShulkerBullet(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      this(☃);
      this.setLocationAndAngles(☃, ☃, ☃, this.rotationYaw, this.rotationPitch);
      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
   }

   public EntityShulkerBullet(World var1, EntityLivingBase var2, Entity var3, EnumFacing.Axis var4) {
      this(☃);
      this.owner = ☃;
      BlockPos ☃ = new BlockPos(☃);
      double ☃x = ☃.getX() + 0.5;
      double ☃xx = ☃.getY() + 0.5;
      double ☃xxx = ☃.getZ() + 0.5;
      this.setLocationAndAngles(☃x, ☃xx, ☃xxx, this.rotationYaw, this.rotationPitch);
      this.target = ☃;
      this.direction = EnumFacing.UP;
      this.selectNextMoveDirection(☃);
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      if (this.owner != null) {
         BlockPos ☃ = new BlockPos(this.owner);
         NBTTagCompound ☃x = NBTUtil.createUUIDTag(this.owner.getUniqueID());
         ☃x.setInteger("X", ☃.getX());
         ☃x.setInteger("Y", ☃.getY());
         ☃x.setInteger("Z", ☃.getZ());
         ☃.setTag("Owner", ☃x);
      }

      if (this.target != null) {
         BlockPos ☃ = new BlockPos(this.target);
         NBTTagCompound ☃x = NBTUtil.createUUIDTag(this.target.getUniqueID());
         ☃x.setInteger("X", ☃.getX());
         ☃x.setInteger("Y", ☃.getY());
         ☃x.setInteger("Z", ☃.getZ());
         ☃.setTag("Target", ☃x);
      }

      if (this.direction != null) {
         ☃.setInteger("Dir", this.direction.getIndex());
      }

      ☃.setInteger("Steps", this.steps);
      ☃.setDouble("TXD", this.targetDeltaX);
      ☃.setDouble("TYD", this.targetDeltaY);
      ☃.setDouble("TZD", this.targetDeltaZ);
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      this.steps = ☃.getInteger("Steps");
      this.targetDeltaX = ☃.getDouble("TXD");
      this.targetDeltaY = ☃.getDouble("TYD");
      this.targetDeltaZ = ☃.getDouble("TZD");
      if (☃.hasKey("Dir", 99)) {
         this.direction = EnumFacing.byIndex(☃.getInteger("Dir"));
      }

      if (☃.hasKey("Owner", 10)) {
         NBTTagCompound ☃ = ☃.getCompoundTag("Owner");
         this.ownerUniqueId = NBTUtil.getUUIDFromTag(☃);
         this.ownerBlockPos = new BlockPos(☃.getInteger("X"), ☃.getInteger("Y"), ☃.getInteger("Z"));
      }

      if (☃.hasKey("Target", 10)) {
         NBTTagCompound ☃ = ☃.getCompoundTag("Target");
         this.targetUniqueId = NBTUtil.getUUIDFromTag(☃);
         this.targetBlockPos = new BlockPos(☃.getInteger("X"), ☃.getInteger("Y"), ☃.getInteger("Z"));
      }
   }

   @Override
   protected void entityInit() {
   }

   private void setDirection(@Nullable EnumFacing var1) {
      this.direction = ☃;
   }

   private void selectNextMoveDirection(@Nullable EnumFacing.Axis var1) {
      double ☃ = 0.5;
      BlockPos ☃x;
      if (this.target == null) {
         ☃x = new BlockPos(this).down();
      } else {
         ☃ = this.target.height * 0.5;
         ☃x = new BlockPos(this.target.posX, this.target.posY + ☃, this.target.posZ);
      }

      double ☃xx = ☃x.getX() + 0.5;
      double ☃xxx = ☃x.getY() + ☃;
      double ☃xxxx = ☃x.getZ() + 0.5;
      EnumFacing ☃xxxxx = null;
      if (☃x.distanceSqToCenter(this.posX, this.posY, this.posZ) >= 4.0) {
         BlockPos ☃xxxxxx = new BlockPos(this);
         List<EnumFacing> ☃xxxxxxx = Lists.newArrayList();
         if (☃ != EnumFacing.Axis.X) {
            if (☃xxxxxx.getX() < ☃x.getX() && this.world.isAirBlock(☃xxxxxx.east())) {
               ☃xxxxxxx.add(EnumFacing.EAST);
            } else if (☃xxxxxx.getX() > ☃x.getX() && this.world.isAirBlock(☃xxxxxx.west())) {
               ☃xxxxxxx.add(EnumFacing.WEST);
            }
         }

         if (☃ != EnumFacing.Axis.Y) {
            if (☃xxxxxx.getY() < ☃x.getY() && this.world.isAirBlock(☃xxxxxx.up())) {
               ☃xxxxxxx.add(EnumFacing.UP);
            } else if (☃xxxxxx.getY() > ☃x.getY() && this.world.isAirBlock(☃xxxxxx.down())) {
               ☃xxxxxxx.add(EnumFacing.DOWN);
            }
         }

         if (☃ != EnumFacing.Axis.Z) {
            if (☃xxxxxx.getZ() < ☃x.getZ() && this.world.isAirBlock(☃xxxxxx.south())) {
               ☃xxxxxxx.add(EnumFacing.SOUTH);
            } else if (☃xxxxxx.getZ() > ☃x.getZ() && this.world.isAirBlock(☃xxxxxx.north())) {
               ☃xxxxxxx.add(EnumFacing.NORTH);
            }
         }

         ☃xxxxx = EnumFacing.random(this.rand);
         if (☃xxxxxxx.isEmpty()) {
            for (int ☃xxxxxxxx = 5; !this.world.isAirBlock(☃xxxxxx.offset(☃xxxxx)) && ☃xxxxxxxx > 0; ☃xxxxxxxx--) {
               ☃xxxxx = EnumFacing.random(this.rand);
            }
         } else {
            ☃xxxxx = ☃xxxxxxx.get(this.rand.nextInt(☃xxxxxxx.size()));
         }

         ☃xx = this.posX + ☃xxxxx.getXOffset();
         ☃xxx = this.posY + ☃xxxxx.getYOffset();
         ☃xxxx = this.posZ + ☃xxxxx.getZOffset();
      }

      this.setDirection(☃xxxxx);
      double ☃xxxxxxxx = ☃xx - this.posX;
      double ☃xxxxxxxxx = ☃xxx - this.posY;
      double ☃xxxxxxxxxx = ☃xxxx - this.posZ;
      double ☃xxxxxxxxxxx = MathHelper.sqrt(☃xxxxxxxx * ☃xxxxxxxx + ☃xxxxxxxxx * ☃xxxxxxxxx + ☃xxxxxxxxxx * ☃xxxxxxxxxx);
      if (☃xxxxxxxxxxx == 0.0) {
         this.targetDeltaX = 0.0;
         this.targetDeltaY = 0.0;
         this.targetDeltaZ = 0.0;
      } else {
         this.targetDeltaX = ☃xxxxxxxx / ☃xxxxxxxxxxx * 0.15;
         this.targetDeltaY = ☃xxxxxxxxx / ☃xxxxxxxxxxx * 0.15;
         this.targetDeltaZ = ☃xxxxxxxxxx / ☃xxxxxxxxxxx * 0.15;
      }

      this.isAirBorne = true;
      this.steps = 10 + this.rand.nextInt(5) * 10;
   }

   @Override
   public void onUpdate() {
      if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
         this.setDead();
      } else {
         super.onUpdate();
         if (!this.world.isRemote) {
            if (this.target == null && this.targetUniqueId != null) {
               for (EntityLivingBase ☃ : this.world
                  .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.targetBlockPos.add(-2, -2, -2), this.targetBlockPos.add(2, 2, 2)))) {
                  if (☃.getUniqueID().equals(this.targetUniqueId)) {
                     this.target = ☃;
                     break;
                  }
               }

               this.targetUniqueId = null;
            }

            if (this.owner == null && this.ownerUniqueId != null) {
               for (EntityLivingBase ☃x : this.world
                  .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.ownerBlockPos.add(-2, -2, -2), this.ownerBlockPos.add(2, 2, 2)))) {
                  if (☃x.getUniqueID().equals(this.ownerUniqueId)) {
                     this.owner = ☃x;
                     break;
                  }
               }

               this.ownerUniqueId = null;
            }

            if (this.target == null || !this.target.isEntityAlive() || this.target instanceof EntityPlayer && ((EntityPlayer)this.target).isSpectator()) {
               if (!this.hasNoGravity()) {
                  this.motionY -= 0.04;
               }
            } else {
               this.targetDeltaX = MathHelper.clamp(this.targetDeltaX * 1.025, -1.0, 1.0);
               this.targetDeltaY = MathHelper.clamp(this.targetDeltaY * 1.025, -1.0, 1.0);
               this.targetDeltaZ = MathHelper.clamp(this.targetDeltaZ * 1.025, -1.0, 1.0);
               this.motionX = this.motionX + (this.targetDeltaX - this.motionX) * 0.2;
               this.motionY = this.motionY + (this.targetDeltaY - this.motionY) * 0.2;
               this.motionZ = this.motionZ + (this.targetDeltaZ - this.motionZ) * 0.2;
            }

            RayTraceResult ☃xx = ProjectileHelper.forwardsRaycast(this, true, false, this.owner);
            if (☃xx != null) {
               this.bulletHit(☃xx);
            }
         }

         this.setPosition(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         ProjectileHelper.rotateTowardsMovement(this, 0.5F);
         if (this.world.isRemote) {
            this.world
               .spawnParticle(EnumParticleTypes.END_ROD, this.posX - this.motionX, this.posY - this.motionY + 0.15, this.posZ - this.motionZ, 0.0, 0.0, 0.0);
         } else if (this.target != null && !this.target.isDead) {
            if (this.steps > 0) {
               this.steps--;
               if (this.steps == 0) {
                  this.selectNextMoveDirection(this.direction == null ? null : this.direction.getAxis());
               }
            }

            if (this.direction != null) {
               BlockPos ☃xx = new BlockPos(this);
               EnumFacing.Axis ☃xxx = this.direction.getAxis();
               if (this.world.isBlockNormalCube(☃xx.offset(this.direction), false)) {
                  this.selectNextMoveDirection(☃xxx);
               } else {
                  BlockPos ☃xxxx = new BlockPos(this.target);
                  if (☃xxx == EnumFacing.Axis.X && ☃xx.getX() == ☃xxxx.getX()
                     || ☃xxx == EnumFacing.Axis.Z && ☃xx.getZ() == ☃xxxx.getZ()
                     || ☃xxx == EnumFacing.Axis.Y && ☃xx.getY() == ☃xxxx.getY()) {
                     this.selectNextMoveDirection(☃xxx);
                  }
               }
            }
         }
      }
   }

   @Override
   public boolean isBurning() {
      return false;
   }

   @Override
   public boolean isInRangeToRenderDist(double var1) {
      return ☃ < 16384.0;
   }

   @Override
   public float getBrightness() {
      return 1.0F;
   }

   @Override
   public int getBrightnessForRender() {
      return 15728880;
   }

   protected void bulletHit(RayTraceResult var1) {
      if (☃.entityHit == null) {
         ((WorldServer)this.world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 2, 0.2, 0.2, 0.2, 0.0);
         this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
      } else {
         boolean ☃ = ☃.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.owner).setProjectile(), 4.0F);
         if (☃) {
            this.applyEnchantments(this.owner, ☃.entityHit);
            if (☃.entityHit instanceof EntityLivingBase) {
               ((EntityLivingBase)☃.entityHit).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 200));
            }
         }
      }

      this.setDead();
   }

   @Override
   public boolean canBeCollidedWith() {
      return true;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (!this.world.isRemote) {
         this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HURT, 1.0F, 1.0F);
         ((WorldServer)this.world).spawnParticle(EnumParticleTypes.CRIT, this.posX, this.posY, this.posZ, 15, 0.2, 0.2, 0.2, 0.0);
         this.setDead();
      }

      return true;
   }
}
