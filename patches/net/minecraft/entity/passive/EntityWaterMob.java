package net.minecraft.entity.passive;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityWaterMob extends EntityLiving implements IAnimals {
   public EntityWaterMob(World var1) {
      super(☃);
   }

   @Override
   public boolean canBreatheUnderwater() {
      return true;
   }

   @Override
   public boolean getCanSpawnHere() {
      return true;
   }

   @Override
   public boolean isNotColliding() {
      return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this);
   }

   @Override
   public int getTalkInterval() {
      return 120;
   }

   @Override
   protected boolean canDespawn() {
      return true;
   }

   @Override
   protected int getExperiencePoints(EntityPlayer var1) {
      return 1 + this.world.rand.nextInt(3);
   }

   @Override
   public void onEntityUpdate() {
      int ☃ = this.getAir();
      super.onEntityUpdate();
      if (this.isEntityAlive() && !this.isInWater()) {
         this.setAir(--☃);
         if (this.getAir() == -20) {
            this.setAir(0);
            this.attackEntityFrom(DamageSource.DROWN, 2.0F);
         }
      } else {
         this.setAir(300);
      }
   }

   @Override
   public boolean isPushedByWater() {
      return false;
   }
}
