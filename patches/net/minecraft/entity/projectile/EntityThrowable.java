package net.minecraft.entity.projectile;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityThrowable extends Entity implements IProjectile {
   private int xTile = -1;
   private int yTile = -1;
   private int zTile = -1;
   private Block inTile;
   protected boolean inGround;
   public int throwableShake;
   protected EntityLivingBase thrower;
   private String throwerName;
   private int ticksInGround;
   private int ticksInAir;
   public Entity ignoreEntity;
   private int ignoreTime;

   public EntityThrowable(World var1) {
      super(☃);
      this.setSize(0.25F, 0.25F);
   }

   public EntityThrowable(World var1, double var2, double var4, double var6) {
      this(☃);
      this.setPosition(☃, ☃, ☃);
   }

   public EntityThrowable(World var1, EntityLivingBase var2) {
      this(☃, ☃.posX, ☃.posY + ☃.getEyeHeight() - 0.1F, ☃.posZ);
      this.thrower = ☃;
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

   public void shoot(Entity var1, float var2, float var3, float var4, float var5, float var6) {
      float ☃ = -MathHelper.sin(☃ * (float) (Math.PI / 180.0)) * MathHelper.cos(☃ * (float) (Math.PI / 180.0));
      float ☃x = -MathHelper.sin((☃ + ☃) * (float) (Math.PI / 180.0));
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
      if (this.throwableShake > 0) {
         this.throwableShake--;
      }

      if (this.inGround) {
         if (this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
            this.ticksInGround++;
            if (this.ticksInGround == 1200) {
               this.setDead();
            }

            return;
         }

         this.inGround = false;
         this.motionX = this.motionX * (this.rand.nextFloat() * 0.2F);
         this.motionY = this.motionY * (this.rand.nextFloat() * 0.2F);
         this.motionZ = this.motionZ * (this.rand.nextFloat() * 0.2F);
         this.ticksInGround = 0;
         this.ticksInAir = 0;
      } else {
         this.ticksInAir++;
      }

      Vec3d ☃ = new Vec3d(this.posX, this.posY, this.posZ);
      Vec3d ☃x = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      RayTraceResult ☃xx = this.world.rayTraceBlocks(☃, ☃x);
      ☃ = new Vec3d(this.posX, this.posY, this.posZ);
      ☃x = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      if (☃xx != null) {
         ☃x = new Vec3d(☃xx.hitVec.x, ☃xx.hitVec.y, ☃xx.hitVec.z);
      }

      Entity ☃xxx = null;
      List<Entity> ☃xxxx = this.world
         .getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0));
      double ☃xxxxx = 0.0;
      boolean ☃xxxxxx = false;

      for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃xxxx.size(); ☃xxxxxxx++) {
         Entity ☃xxxxxxxx = ☃xxxx.get(☃xxxxxxx);
         if (☃xxxxxxxx.canBeCollidedWith()) {
            if (☃xxxxxxxx == this.ignoreEntity) {
               ☃xxxxxx = true;
            } else if (this.thrower != null && this.ticksExisted < 2 && this.ignoreEntity == null) {
               this.ignoreEntity = ☃xxxxxxxx;
               ☃xxxxxx = true;
            } else {
               ☃xxxxxx = false;
               AxisAlignedBB ☃xxxxxxxxx = ☃xxxxxxxx.getEntityBoundingBox().grow(0.3F);
               RayTraceResult ☃xxxxxxxxxx = ☃xxxxxxxxx.calculateIntercept(☃, ☃x);
               if (☃xxxxxxxxxx != null) {
                  double ☃xxxxxxxxxxx = ☃.squareDistanceTo(☃xxxxxxxxxx.hitVec);
                  if (☃xxxxxxxxxxx < ☃xxxxx || ☃xxxxx == 0.0) {
                     ☃xxx = ☃xxxxxxxx;
                     ☃xxxxx = ☃xxxxxxxxxxx;
                  }
               }
            }
         }
      }

      if (this.ignoreEntity != null) {
         if (☃xxxxxx) {
            this.ignoreTime = 2;
         } else if (this.ignoreTime-- <= 0) {
            this.ignoreEntity = null;
         }
      }

      if (☃xxx != null) {
         ☃xx = new RayTraceResult(☃xxx);
      }

      if (☃xx != null) {
         if (☃xx.typeOfHit == RayTraceResult.Type.BLOCK && this.world.getBlockState(☃xx.getBlockPos()).getBlock() == Blocks.PORTAL) {
            this.setPortal(☃xx.getBlockPos());
         } else {
            this.onImpact(☃xx);
         }
      }

      this.posX = this.posX + this.motionX;
      this.posY = this.posY + this.motionY;
      this.posZ = this.posZ + this.motionZ;
      float ☃xxxxxxxx = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0F / (float)Math.PI);
      this.rotationPitch = (float)(MathHelper.atan2(this.motionY, ☃xxxxxxxx) * 180.0F / (float)Math.PI);

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
      float ☃xxxxxxxxx = 0.99F;
      float ☃xxxxxxxxxx = this.getGravityVelocity();
      if (this.isInWater()) {
         for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < 4; ☃xxxxxxxxxxx++) {
            float ☃xxxxxxxxxxxx = 0.25F;
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

         ☃xxxxxxxxx = 0.8F;
      }

      this.motionX *= ☃xxxxxxxxx;
      this.motionY *= ☃xxxxxxxxx;
      this.motionZ *= ☃xxxxxxxxx;
      if (!this.hasNoGravity()) {
         this.motionY -= ☃xxxxxxxxxx;
      }

      this.setPosition(this.posX, this.posY, this.posZ);
   }

   protected float getGravityVelocity() {
      return 0.03F;
   }

   protected abstract void onImpact(RayTraceResult var1);

   public static void registerFixesThrowable(DataFixer var0, String var1) {
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      ☃.setInteger("xTile", this.xTile);
      ☃.setInteger("yTile", this.yTile);
      ☃.setInteger("zTile", this.zTile);
      ResourceLocation ☃ = Block.REGISTRY.getNameForObject(this.inTile);
      ☃.setString("inTile", ☃ == null ? "" : ☃.toString());
      ☃.setByte("shake", (byte)this.throwableShake);
      ☃.setByte("inGround", (byte)(this.inGround ? 1 : 0));
      if ((this.throwerName == null || this.throwerName.isEmpty()) && this.thrower instanceof EntityPlayer) {
         this.throwerName = this.thrower.getName();
      }

      ☃.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      this.xTile = ☃.getInteger("xTile");
      this.yTile = ☃.getInteger("yTile");
      this.zTile = ☃.getInteger("zTile");
      if (☃.hasKey("inTile", 8)) {
         this.inTile = Block.getBlockFromName(☃.getString("inTile"));
      } else {
         this.inTile = Block.getBlockById(☃.getByte("inTile") & 255);
      }

      this.throwableShake = ☃.getByte("shake") & 255;
      this.inGround = ☃.getByte("inGround") == 1;
      this.thrower = null;
      this.throwerName = ☃.getString("ownerName");
      if (this.throwerName != null && this.throwerName.isEmpty()) {
         this.throwerName = null;
      }

      this.thrower = this.getThrower();
   }

   @Nullable
   public EntityLivingBase getThrower() {
      if (this.thrower == null && this.throwerName != null && !this.throwerName.isEmpty()) {
         this.thrower = this.world.getPlayerEntityByName(this.throwerName);
         if (this.thrower == null && this.world instanceof WorldServer) {
            try {
               Entity ☃ = ((WorldServer)this.world).getEntityFromUuid(UUID.fromString(this.throwerName));
               if (☃ instanceof EntityLivingBase) {
                  this.thrower = (EntityLivingBase)☃;
               }
            } catch (Throwable var2) {
               this.thrower = null;
            }
         }
      }

      return this.thrower;
   }
}
