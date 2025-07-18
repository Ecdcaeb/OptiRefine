package net.minecraft.entity;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

public abstract class EntityHanging extends Entity {
   private static final Predicate<Entity> IS_HANGING_ENTITY = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity var1) {
         return ☃ instanceof EntityHanging;
      }
   };
   private int tickCounter1;
   protected BlockPos hangingPosition;
   @Nullable
   public EnumFacing facingDirection;

   public EntityHanging(World var1) {
      super(☃);
      this.setSize(0.5F, 0.5F);
   }

   public EntityHanging(World var1, BlockPos var2) {
      this(☃);
      this.hangingPosition = ☃;
   }

   @Override
   protected void entityInit() {
   }

   protected void updateFacingWithBoundingBox(EnumFacing var1) {
      Validate.notNull(☃);
      Validate.isTrue(☃.getAxis().isHorizontal());
      this.facingDirection = ☃;
      this.rotationYaw = this.facingDirection.getHorizontalIndex() * 90;
      this.prevRotationYaw = this.rotationYaw;
      this.updateBoundingBox();
   }

   protected void updateBoundingBox() {
      if (this.facingDirection != null) {
         double ☃ = this.hangingPosition.getX() + 0.5;
         double ☃x = this.hangingPosition.getY() + 0.5;
         double ☃xx = this.hangingPosition.getZ() + 0.5;
         double ☃xxx = 0.46875;
         double ☃xxxx = this.offs(this.getWidthPixels());
         double ☃xxxxx = this.offs(this.getHeightPixels());
         ☃ -= this.facingDirection.getXOffset() * 0.46875;
         ☃xx -= this.facingDirection.getZOffset() * 0.46875;
         ☃x += ☃xxxxx;
         EnumFacing ☃xxxxxx = this.facingDirection.rotateYCCW();
         ☃ += ☃xxxx * ☃xxxxxx.getXOffset();
         ☃xx += ☃xxxx * ☃xxxxxx.getZOffset();
         this.posX = ☃;
         this.posY = ☃x;
         this.posZ = ☃xx;
         double ☃xxxxxxx = this.getWidthPixels();
         double ☃xxxxxxxx = this.getHeightPixels();
         double ☃xxxxxxxxx = this.getWidthPixels();
         if (this.facingDirection.getAxis() == EnumFacing.Axis.Z) {
            ☃xxxxxxxxx = 1.0;
         } else {
            ☃xxxxxxx = 1.0;
         }

         ☃xxxxxxx /= 32.0;
         ☃xxxxxxxx /= 32.0;
         ☃xxxxxxxxx /= 32.0;
         this.setEntityBoundingBox(new AxisAlignedBB(☃ - ☃xxxxxxx, ☃x - ☃xxxxxxxx, ☃xx - ☃xxxxxxxxx, ☃ + ☃xxxxxxx, ☃x + ☃xxxxxxxx, ☃xx + ☃xxxxxxxxx));
      }
   }

   private double offs(int var1) {
      return ☃ % 32 == 0 ? 0.5 : 0.0;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.tickCounter1++ == 100 && !this.world.isRemote) {
         this.tickCounter1 = 0;
         if (!this.isDead && !this.onValidSurface()) {
            this.setDead();
            this.onBroken(null);
         }
      }
   }

   public boolean onValidSurface() {
      if (!this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
         return false;
      } else {
         int ☃ = Math.max(1, this.getWidthPixels() / 16);
         int ☃x = Math.max(1, this.getHeightPixels() / 16);
         BlockPos ☃xx = this.hangingPosition.offset(this.facingDirection.getOpposite());
         EnumFacing ☃xxx = this.facingDirection.rotateYCCW();
         BlockPos.MutableBlockPos ☃xxxx = new BlockPos.MutableBlockPos();

         for (int ☃xxxxx = 0; ☃xxxxx < ☃; ☃xxxxx++) {
            for (int ☃xxxxxx = 0; ☃xxxxxx < ☃x; ☃xxxxxx++) {
               int ☃xxxxxxx = (☃ - 1) / -2;
               int ☃xxxxxxxx = (☃x - 1) / -2;
               ☃xxxx.setPos(☃xx).move(☃xxx, ☃xxxxx + ☃xxxxxxx).move(EnumFacing.UP, ☃xxxxxx + ☃xxxxxxxx);
               IBlockState ☃xxxxxxxxx = this.world.getBlockState(☃xxxx);
               if (!☃xxxxxxxxx.getMaterial().isSolid() && !BlockRedstoneDiode.isDiode(☃xxxxxxxxx)) {
                  return false;
               }
            }
         }

         return this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), IS_HANGING_ENTITY).isEmpty();
      }
   }

   @Override
   public boolean canBeCollidedWith() {
      return true;
   }

   @Override
   public boolean hitByEntity(Entity var1) {
      return ☃ instanceof EntityPlayer ? this.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)☃), 0.0F) : false;
   }

   @Override
   public EnumFacing getHorizontalFacing() {
      return this.facingDirection;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else {
         if (!this.isDead && !this.world.isRemote) {
            this.setDead();
            this.markVelocityChanged();
            this.onBroken(☃.getTrueSource());
         }

         return true;
      }
   }

   @Override
   public void move(MoverType var1, double var2, double var4, double var6) {
      if (!this.world.isRemote && !this.isDead && ☃ * ☃ + ☃ * ☃ + ☃ * ☃ > 0.0) {
         this.setDead();
         this.onBroken(null);
      }
   }

   @Override
   public void addVelocity(double var1, double var3, double var5) {
      if (!this.world.isRemote && !this.isDead && ☃ * ☃ + ☃ * ☃ + ☃ * ☃ > 0.0) {
         this.setDead();
         this.onBroken(null);
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      ☃.setByte("Facing", (byte)this.facingDirection.getHorizontalIndex());
      BlockPos ☃ = this.getHangingPosition();
      ☃.setInteger("TileX", ☃.getX());
      ☃.setInteger("TileY", ☃.getY());
      ☃.setInteger("TileZ", ☃.getZ());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      this.hangingPosition = new BlockPos(☃.getInteger("TileX"), ☃.getInteger("TileY"), ☃.getInteger("TileZ"));
      this.updateFacingWithBoundingBox(EnumFacing.byHorizontalIndex(☃.getByte("Facing")));
   }

   public abstract int getWidthPixels();

   public abstract int getHeightPixels();

   public abstract void onBroken(@Nullable Entity var1);

   public abstract void playPlaceSound();

   @Override
   public EntityItem entityDropItem(ItemStack var1, float var2) {
      EntityItem ☃ = new EntityItem(
         this.world, this.posX + this.facingDirection.getXOffset() * 0.15F, this.posY + ☃, this.posZ + this.facingDirection.getZOffset() * 0.15F, ☃
      );
      ☃.setDefaultPickupDelay();
      this.world.spawnEntity(☃);
      return ☃;
   }

   @Override
   protected boolean shouldSetPosAfterLoading() {
      return false;
   }

   @Override
   public void setPosition(double var1, double var3, double var5) {
      this.hangingPosition = new BlockPos(☃, ☃, ☃);
      this.updateBoundingBox();
      this.isAirBorne = true;
   }

   public BlockPos getHangingPosition() {
      return this.hangingPosition;
   }

   @Override
   public float getRotatedYaw(Rotation var1) {
      if (this.facingDirection != null && this.facingDirection.getAxis() != EnumFacing.Axis.Y) {
         switch (☃) {
            case CLOCKWISE_180:
               this.facingDirection = this.facingDirection.getOpposite();
               break;
            case COUNTERCLOCKWISE_90:
               this.facingDirection = this.facingDirection.rotateYCCW();
               break;
            case CLOCKWISE_90:
               this.facingDirection = this.facingDirection.rotateY();
         }
      }

      float ☃ = MathHelper.wrapDegrees(this.rotationYaw);
      switch (☃) {
         case CLOCKWISE_180:
            return ☃ + 180.0F;
         case COUNTERCLOCKWISE_90:
            return ☃ + 90.0F;
         case CLOCKWISE_90:
            return ☃ + 270.0F;
         default:
            return ☃;
      }
   }

   @Override
   public float getMirroredYaw(Mirror var1) {
      return this.getRotatedYaw(☃.toRotation(this.facingDirection));
   }

   @Override
   public void onStruckByLightning(EntityLightningBolt var1) {
   }
}
