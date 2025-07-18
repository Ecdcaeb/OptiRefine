package net.minecraft.entity.monster;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public abstract class EntityMob extends EntityCreature implements IMob {
   public EntityMob(World var1) {
      super(☃);
      this.experienceValue = 5;
   }

   @Override
   public SoundCategory getSoundCategory() {
      return SoundCategory.HOSTILE;
   }

   @Override
   public void onLivingUpdate() {
      this.updateArmSwingProgress();
      float ☃ = this.getBrightness();
      if (☃ > 0.5F) {
         this.idleTime += 2;
      }

      super.onLivingUpdate();
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
         this.setDead();
      }
   }

   @Override
   protected SoundEvent getSwimSound() {
      return SoundEvents.ENTITY_HOSTILE_SWIM;
   }

   @Override
   protected SoundEvent getSplashSound() {
      return SoundEvents.ENTITY_HOSTILE_SPLASH;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      return this.isEntityInvulnerable(☃) ? false : super.attackEntityFrom(☃, ☃);
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_HOSTILE_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_HOSTILE_DEATH;
   }

   @Override
   protected SoundEvent getFallSound(int var1) {
      return ☃ > 4 ? SoundEvents.ENTITY_HOSTILE_BIG_FALL : SoundEvents.ENTITY_HOSTILE_SMALL_FALL;
   }

   @Override
   public boolean attackEntityAsMob(Entity var1) {
      float ☃ = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
      int ☃x = 0;
      if (☃ instanceof EntityLivingBase) {
         ☃ += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)☃).getCreatureAttribute());
         ☃x += EnchantmentHelper.getKnockbackModifier(this);
      }

      boolean ☃xx = ☃.attackEntityFrom(DamageSource.causeMobDamage(this), ☃);
      if (☃xx) {
         if (☃x > 0 && ☃ instanceof EntityLivingBase) {
            ((EntityLivingBase)☃)
               .knockBack(
                  this, ☃x * 0.5F, MathHelper.sin(this.rotationYaw * (float) (Math.PI / 180.0)), -MathHelper.cos(this.rotationYaw * (float) (Math.PI / 180.0))
               );
            this.motionX *= 0.6;
            this.motionZ *= 0.6;
         }

         int ☃xxx = EnchantmentHelper.getFireAspectModifier(this);
         if (☃xxx > 0) {
            ☃.setFire(☃xxx * 4);
         }

         if (☃ instanceof EntityPlayer) {
            EntityPlayer ☃xxxx = (EntityPlayer)☃;
            ItemStack ☃xxxxx = this.getHeldItemMainhand();
            ItemStack ☃xxxxxx = ☃xxxx.isHandActive() ? ☃xxxx.getActiveItemStack() : ItemStack.EMPTY;
            if (!☃xxxxx.isEmpty() && !☃xxxxxx.isEmpty() && ☃xxxxx.getItem() instanceof ItemAxe && ☃xxxxxx.getItem() == Items.SHIELD) {
               float ☃xxxxxxx = 0.25F + EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
               if (this.rand.nextFloat() < ☃xxxxxxx) {
                  ☃xxxx.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                  this.world.setEntityState(☃xxxx, (byte)30);
               }
            }
         }

         this.applyEnchantments(this, ☃);
      }

      return ☃xx;
   }

   @Override
   public float getBlockPathWeight(BlockPos var1) {
      return 0.5F - this.world.getLightBrightness(☃);
   }

   protected boolean isValidLightLevel() {
      BlockPos ☃ = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
      if (this.world.getLightFor(EnumSkyBlock.SKY, ☃) > this.rand.nextInt(32)) {
         return false;
      } else {
         int ☃x = this.world.getLightFromNeighbors(☃);
         if (this.world.isThundering()) {
            int ☃xx = this.world.getSkylightSubtracted();
            this.world.setSkylightSubtracted(10);
            ☃x = this.world.getLightFromNeighbors(☃);
            this.world.setSkylightSubtracted(☃xx);
         }

         return ☃x <= this.rand.nextInt(8);
      }
   }

   @Override
   public boolean getCanSpawnHere() {
      return this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
   }

   @Override
   protected boolean canDropLoot() {
      return true;
   }

   public boolean isPreventingPlayerRest(EntityPlayer var1) {
      return true;
   }
}
