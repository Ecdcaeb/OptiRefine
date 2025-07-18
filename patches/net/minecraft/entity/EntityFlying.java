package net.minecraft.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityFlying extends EntityLiving {
   public EntityFlying(World var1) {
      super(☃);
   }

   @Override
   public void fall(float var1, float var2) {
   }

   @Override
   protected void updateFallState(double var1, boolean var3, IBlockState var4, BlockPos var5) {
   }

   @Override
   public void travel(float var1, float var2, float var3) {
      if (this.isInWater()) {
         this.moveRelative(☃, ☃, ☃, 0.02F);
         this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.8F;
         this.motionY *= 0.8F;
         this.motionZ *= 0.8F;
      } else if (this.isInLava()) {
         this.moveRelative(☃, ☃, ☃, 0.02F);
         this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.5;
         this.motionY *= 0.5;
         this.motionZ *= 0.5;
      } else {
         float ☃ = 0.91F;
         if (this.onGround) {
            ☃ = this.world
                  .getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ)))
                  .getBlock()
                  .slipperiness
               * 0.91F;
         }

         float ☃x = 0.16277136F / (☃ * ☃ * ☃);
         this.moveRelative(☃, ☃, ☃, this.onGround ? 0.1F * ☃x : 0.02F);
         ☃ = 0.91F;
         if (this.onGround) {
            ☃ = this.world
                  .getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ)))
                  .getBlock()
                  .slipperiness
               * 0.91F;
         }

         this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
         this.motionX *= ☃;
         this.motionY *= ☃;
         this.motionZ *= ☃;
      }

      this.prevLimbSwingAmount = this.limbSwingAmount;
      double ☃x = this.posX - this.prevPosX;
      double ☃xx = this.posZ - this.prevPosZ;
      float ☃xxx = MathHelper.sqrt(☃x * ☃x + ☃xx * ☃xx) * 4.0F;
      if (☃xxx > 1.0F) {
         ☃xxx = 1.0F;
      }

      this.limbSwingAmount = this.limbSwingAmount + (☃xxx - this.limbSwingAmount) * 0.4F;
      this.limbSwing = this.limbSwing + this.limbSwingAmount;
   }

   @Override
   public boolean isOnLadder() {
      return false;
   }
}
