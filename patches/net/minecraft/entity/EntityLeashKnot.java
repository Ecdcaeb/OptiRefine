package net.minecraft.entity;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockFence;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityLeashKnot extends EntityHanging {
   public EntityLeashKnot(World var1) {
      super(☃);
   }

   public EntityLeashKnot(World var1, BlockPos var2) {
      super(☃, ☃);
      this.setPosition(☃.getX() + 0.5, ☃.getY() + 0.5, ☃.getZ() + 0.5);
      float ☃ = 0.125F;
      float ☃x = 0.1875F;
      float ☃xx = 0.25F;
      this.setEntityBoundingBox(
         new AxisAlignedBB(this.posX - 0.1875, this.posY - 0.25 + 0.125, this.posZ - 0.1875, this.posX + 0.1875, this.posY + 0.25 + 0.125, this.posZ + 0.1875)
      );
      this.forceSpawn = true;
   }

   @Override
   public void setPosition(double var1, double var3, double var5) {
      super.setPosition(MathHelper.floor(☃) + 0.5, MathHelper.floor(☃) + 0.5, MathHelper.floor(☃) + 0.5);
   }

   @Override
   protected void updateBoundingBox() {
      this.posX = this.hangingPosition.getX() + 0.5;
      this.posY = this.hangingPosition.getY() + 0.5;
      this.posZ = this.hangingPosition.getZ() + 0.5;
   }

   @Override
   public void updateFacingWithBoundingBox(EnumFacing var1) {
   }

   @Override
   public int getWidthPixels() {
      return 9;
   }

   @Override
   public int getHeightPixels() {
      return 9;
   }

   @Override
   public float getEyeHeight() {
      return -0.0625F;
   }

   @Override
   public boolean isInRangeToRenderDist(double var1) {
      return ☃ < 1024.0;
   }

   @Override
   public void onBroken(@Nullable Entity var1) {
      this.playSound(SoundEvents.ENTITY_LEASHKNOT_BREAK, 1.0F, 1.0F);
   }

   @Override
   public boolean writeToNBTOptional(NBTTagCompound var1) {
      return false;
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
   }

   @Override
   public boolean processInitialInteract(EntityPlayer var1, EnumHand var2) {
      if (this.world.isRemote) {
         return true;
      } else {
         boolean ☃ = false;
         double ☃x = 7.0;
         List<EntityLiving> ☃xx = this.world
            .getEntitiesWithinAABB(
               EntityLiving.class, new AxisAlignedBB(this.posX - 7.0, this.posY - 7.0, this.posZ - 7.0, this.posX + 7.0, this.posY + 7.0, this.posZ + 7.0)
            );

         for (EntityLiving ☃xxx : ☃xx) {
            if (☃xxx.getLeashed() && ☃xxx.getLeashHolder() == ☃) {
               ☃xxx.setLeashHolder(this, true);
               ☃ = true;
            }
         }

         if (!☃) {
            this.setDead();
            if (☃.capabilities.isCreativeMode) {
               for (EntityLiving ☃xxxx : ☃xx) {
                  if (☃xxxx.getLeashed() && ☃xxxx.getLeashHolder() == this) {
                     ☃xxxx.clearLeashed(true, false);
                  }
               }
            }
         }

         return true;
      }
   }

   @Override
   public boolean onValidSurface() {
      return this.world.getBlockState(this.hangingPosition).getBlock() instanceof BlockFence;
   }

   public static EntityLeashKnot createKnot(World var0, BlockPos var1) {
      EntityLeashKnot ☃ = new EntityLeashKnot(☃, ☃);
      ☃.spawnEntity(☃);
      ☃.playPlaceSound();
      return ☃;
   }

   @Nullable
   public static EntityLeashKnot getKnotForPosition(World var0, BlockPos var1) {
      int ☃ = ☃.getX();
      int ☃x = ☃.getY();
      int ☃xx = ☃.getZ();

      for (EntityLeashKnot ☃xxx : ☃.getEntitiesWithinAABB(EntityLeashKnot.class, new AxisAlignedBB(☃ - 1.0, ☃x - 1.0, ☃xx - 1.0, ☃ + 1.0, ☃x + 1.0, ☃xx + 1.0))) {
         if (☃xxx.getHangingPosition().equals(☃)) {
            return ☃xxx;
         }
      }

      return null;
   }

   @Override
   public void playPlaceSound() {
      this.playSound(SoundEvents.ENTITY_LEASHKNOT_PLACE, 1.0F, 1.0F);
   }
}
