package net.minecraft.entity.item;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityXPOrb extends Entity {
   public int xpColor;
   public int xpOrbAge;
   public int delayBeforeCanPickup;
   private int xpOrbHealth = 5;
   private int xpValue;
   private EntityPlayer closestPlayer;
   private int xpTargetColor;

   public EntityXPOrb(World var1, double var2, double var4, double var6, int var8) {
      super(☃);
      this.setSize(0.5F, 0.5F);
      this.setPosition(☃, ☃, ☃);
      this.rotationYaw = (float)(Math.random() * 360.0);
      this.motionX = (float)(Math.random() * 0.2F - 0.1F) * 2.0F;
      this.motionY = (float)(Math.random() * 0.2) * 2.0F;
      this.motionZ = (float)(Math.random() * 0.2F - 0.1F) * 2.0F;
      this.xpValue = ☃;
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   public EntityXPOrb(World var1) {
      super(☃);
      this.setSize(0.25F, 0.25F);
   }

   @Override
   protected void entityInit() {
   }

   @Override
   public int getBrightnessForRender() {
      float ☃ = 0.5F;
      ☃ = MathHelper.clamp(☃, 0.0F, 1.0F);
      int ☃x = super.getBrightnessForRender();
      int ☃xx = ☃x & 0xFF;
      int ☃xxx = ☃x >> 16 & 0xFF;
      ☃xx += (int)(☃ * 15.0F * 16.0F);
      if (☃xx > 240) {
         ☃xx = 240;
      }

      return ☃xx | ☃xxx << 16;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.delayBeforeCanPickup > 0) {
         this.delayBeforeCanPickup--;
      }

      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (!this.hasNoGravity()) {
         this.motionY -= 0.03F;
      }

      if (this.world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA) {
         this.motionY = 0.2F;
         this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
         this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
         this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
      }

      this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0, this.posZ);
      double ☃ = 8.0;
      if (this.xpTargetColor < this.xpColor - 20 + this.getEntityId() % 100) {
         if (this.closestPlayer == null || this.closestPlayer.getDistanceSq(this) > 64.0) {
            this.closestPlayer = this.world.getClosestPlayerToEntity(this, 8.0);
         }

         this.xpTargetColor = this.xpColor;
      }

      if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
         this.closestPlayer = null;
      }

      if (this.closestPlayer != null) {
         double ☃x = (this.closestPlayer.posX - this.posX) / 8.0;
         double ☃xx = (this.closestPlayer.posY + this.closestPlayer.getEyeHeight() / 2.0 - this.posY) / 8.0;
         double ☃xxx = (this.closestPlayer.posZ - this.posZ) / 8.0;
         double ☃xxxx = Math.sqrt(☃x * ☃x + ☃xx * ☃xx + ☃xxx * ☃xxx);
         double ☃xxxxx = 1.0 - ☃xxxx;
         if (☃xxxxx > 0.0) {
            ☃xxxxx *= ☃xxxxx;
            this.motionX += ☃x / ☃xxxx * ☃xxxxx * 0.1;
            this.motionY += ☃xx / ☃xxxx * ☃xxxxx * 0.1;
            this.motionZ += ☃xxx / ☃xxxx * ☃xxxxx * 0.1;
         }
      }

      this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
      float ☃x = 0.98F;
      if (this.onGround) {
         ☃x = this.world
               .getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ)))
               .getBlock()
               .slipperiness
            * 0.98F;
      }

      this.motionX *= ☃x;
      this.motionY *= 0.98F;
      this.motionZ *= ☃x;
      if (this.onGround) {
         this.motionY *= -0.9F;
      }

      this.xpColor++;
      this.xpOrbAge++;
      if (this.xpOrbAge >= 6000) {
         this.setDead();
      }
   }

   @Override
   public boolean handleWaterMovement() {
      return this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, this);
   }

   @Override
   protected void dealFireDamage(int var1) {
      this.attackEntityFrom(DamageSource.IN_FIRE, ☃);
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else {
         this.markVelocityChanged();
         this.xpOrbHealth = (int)(this.xpOrbHealth - ☃);
         if (this.xpOrbHealth <= 0) {
            this.setDead();
         }

         return false;
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      ☃.setShort("Health", (short)this.xpOrbHealth);
      ☃.setShort("Age", (short)this.xpOrbAge);
      ☃.setShort("Value", (short)this.xpValue);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      this.xpOrbHealth = ☃.getShort("Health");
      this.xpOrbAge = ☃.getShort("Age");
      this.xpValue = ☃.getShort("Value");
   }

   @Override
   public void onCollideWithPlayer(EntityPlayer var1) {
      if (!this.world.isRemote) {
         if (this.delayBeforeCanPickup == 0 && ☃.xpCooldown == 0) {
            ☃.xpCooldown = 2;
            ☃.onItemPickup(this, 1);
            ItemStack ☃ = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, ☃);
            if (!☃.isEmpty() && ☃.isItemDamaged()) {
               int ☃x = Math.min(this.xpToDurability(this.xpValue), ☃.getItemDamage());
               this.xpValue = this.xpValue - this.durabilityToXp(☃x);
               ☃.setItemDamage(☃.getItemDamage() - ☃x);
            }

            if (this.xpValue > 0) {
               ☃.addExperience(this.xpValue);
            }

            this.setDead();
         }
      }
   }

   private int durabilityToXp(int var1) {
      return ☃ / 2;
   }

   private int xpToDurability(int var1) {
      return ☃ * 2;
   }

   public int getXpValue() {
      return this.xpValue;
   }

   public int getTextureByXP() {
      if (this.xpValue >= 2477) {
         return 10;
      } else if (this.xpValue >= 1237) {
         return 9;
      } else if (this.xpValue >= 617) {
         return 8;
      } else if (this.xpValue >= 307) {
         return 7;
      } else if (this.xpValue >= 149) {
         return 6;
      } else if (this.xpValue >= 73) {
         return 5;
      } else if (this.xpValue >= 37) {
         return 4;
      } else if (this.xpValue >= 17) {
         return 3;
      } else if (this.xpValue >= 7) {
         return 2;
      } else {
         return this.xpValue >= 3 ? 1 : 0;
      }
   }

   public static int getXPSplit(int var0) {
      if (☃ >= 2477) {
         return 2477;
      } else if (☃ >= 1237) {
         return 1237;
      } else if (☃ >= 617) {
         return 617;
      } else if (☃ >= 307) {
         return 307;
      } else if (☃ >= 149) {
         return 149;
      } else if (☃ >= 73) {
         return 73;
      } else if (☃ >= 37) {
         return 37;
      } else if (☃ >= 17) {
         return 17;
      } else if (☃ >= 7) {
         return 7;
      } else {
         return ☃ >= 3 ? 3 : 1;
      }
   }

   @Override
   public boolean canBeAttackedWithItem() {
      return false;
   }
}
