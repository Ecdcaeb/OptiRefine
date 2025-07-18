package net.minecraft.entity.passive;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals {
   protected Block spawnableBlock = Blocks.GRASS;
   private int inLove;
   private UUID playerInLove;

   public EntityAnimal(World var1) {
      super(☃);
   }

   @Override
   protected void updateAITasks() {
      if (this.getGrowingAge() != 0) {
         this.inLove = 0;
      }

      super.updateAITasks();
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.getGrowingAge() != 0) {
         this.inLove = 0;
      }

      if (this.inLove > 0) {
         this.inLove--;
         if (this.inLove % 10 == 0) {
            double ☃ = this.rand.nextGaussian() * 0.02;
            double ☃x = this.rand.nextGaussian() * 0.02;
            double ☃xx = this.rand.nextGaussian() * 0.02;
            this.world
               .spawnParticle(
                  EnumParticleTypes.HEART,
                  this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
                  this.posY + 0.5 + this.rand.nextFloat() * this.height,
                  this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
                  ☃,
                  ☃x,
                  ☃xx
               );
         }
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else {
         this.inLove = 0;
         return super.attackEntityFrom(☃, ☃);
      }
   }

   @Override
   public float getBlockPathWeight(BlockPos var1) {
      return this.world.getBlockState(☃.down()).getBlock() == this.spawnableBlock ? 10.0F : this.world.getLightBrightness(☃) - 0.5F;
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("InLove", this.inLove);
      if (this.playerInLove != null) {
         ☃.setUniqueId("LoveCause", this.playerInLove);
      }
   }

   @Override
   public double getYOffset() {
      return 0.14;
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.inLove = ☃.getInteger("InLove");
      this.playerInLove = ☃.hasUniqueId("LoveCause") ? ☃.getUniqueId("LoveCause") : null;
   }

   @Override
   public boolean getCanSpawnHere() {
      int ☃ = MathHelper.floor(this.posX);
      int ☃x = MathHelper.floor(this.getEntityBoundingBox().minY);
      int ☃xx = MathHelper.floor(this.posZ);
      BlockPos ☃xxx = new BlockPos(☃, ☃x, ☃xx);
      return this.world.getBlockState(☃xxx.down()).getBlock() == this.spawnableBlock && this.world.getLight(☃xxx) > 8 && super.getCanSpawnHere();
   }

   @Override
   public int getTalkInterval() {
      return 120;
   }

   @Override
   protected boolean canDespawn() {
      return false;
   }

   @Override
   protected int getExperiencePoints(EntityPlayer var1) {
      return 1 + this.world.rand.nextInt(3);
   }

   public boolean isBreedingItem(ItemStack var1) {
      return ☃.getItem() == Items.WHEAT;
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (!☃.isEmpty()) {
         if (this.isBreedingItem(☃) && this.getGrowingAge() == 0 && this.inLove <= 0) {
            this.consumeItemFromStack(☃, ☃);
            this.setInLove(☃);
            return true;
         }

         if (this.isChild() && this.isBreedingItem(☃)) {
            this.consumeItemFromStack(☃, ☃);
            this.ageUp((int)(-this.getGrowingAge() / 20 * 0.1F), true);
            return true;
         }
      }

      return super.processInteract(☃, ☃);
   }

   protected void consumeItemFromStack(EntityPlayer var1, ItemStack var2) {
      if (!☃.capabilities.isCreativeMode) {
         ☃.shrink(1);
      }
   }

   public void setInLove(@Nullable EntityPlayer var1) {
      this.inLove = 600;
      if (☃ != null) {
         this.playerInLove = ☃.getUniqueID();
      }

      this.world.setEntityState(this, (byte)18);
   }

   @Nullable
   public EntityPlayerMP getLoveCause() {
      if (this.playerInLove == null) {
         return null;
      } else {
         EntityPlayer ☃ = this.world.getPlayerEntityByUUID(this.playerInLove);
         return ☃ instanceof EntityPlayerMP ? (EntityPlayerMP)☃ : null;
      }
   }

   public boolean isInLove() {
      return this.inLove > 0;
   }

   public void resetInLove() {
      this.inLove = 0;
   }

   public boolean canMateWith(EntityAnimal var1) {
      if (☃ == this) {
         return false;
      } else {
         return ☃.getClass() != this.getClass() ? false : this.isInLove() && ☃.isInLove();
      }
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 18) {
         for (int ☃ = 0; ☃ < 7; ☃++) {
            double ☃x = this.rand.nextGaussian() * 0.02;
            double ☃xx = this.rand.nextGaussian() * 0.02;
            double ☃xxx = this.rand.nextGaussian() * 0.02;
            this.world
               .spawnParticle(
                  EnumParticleTypes.HEART,
                  this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
                  this.posY + 0.5 + this.rand.nextFloat() * this.height,
                  this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
                  ☃x,
                  ☃xx,
                  ☃xxx
               );
         }
      } else {
         super.handleStatusUpdate(☃);
      }
   }
}
